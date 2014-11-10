package net.floodlightcontroller.andorfin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.andorfin.protocol.AOFMatch;
import org.andorfin.protocol.AOFPolicyRegistration;
import org.andorfin.protocol.AOFPredicate;
import org.andorfin.protocol.action.AOFActionModFlowTable;
import org.andorfin.protocol.action.AOFFlowMod;
import org.andorfin.protocol.action.EventType;
import org.andorfin.protocol.action.Operation;
import org.andorfin.protocol.action.OperationType;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.util.AppCookie;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.util.MACAddress;


public class AndorfinManager implements IOFMessageListener, IFloodlightModule {

	
	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	
	protected List<AndorfinRule> rules;
	
	//use the src mac address as test app id
	public void addAOFRuleTest(MACAddress id){
		AndorfinRule test_rule = new AndorfinRule(id);
		
		AccessControlAction ac = new AccessControlAction();
		
		AOFPredicate p = new AOFPredicate(EventType.TIME);
		p.addOperation(EventType.TIME, new Operation(OperationType.GEQ, 8),new Operation(OperationType.LEQ, 10));
		
		ManagementAction ma = new ManagementAction();
		
		test_rule.addPredicate(p);
		test_rule.setAccessControlAction(ac);
		test_rule.setManagementAction(ma);
		
		this.rules.add(test_rule);
	}
	
	@Override
	public String getName() {
		return "Android Controller";
	}


	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);

		return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context
				.getServiceImpl(IFloodlightProviderService.class);
		
		logger = LoggerFactory.getLogger(AndorfinManager.class);
		
		rules = new ArrayList();

	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);

	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		switch (msg.getType()) {
		
		case PACKET_IN:
			return this.processAndorfinPacket(sw, (OFPacketIn) msg, cntx);
		default:
			break;
		}

		return Command.CONTINUE;
		
	}	

	
	private Command processAndorfinPacket(IOFSwitch sw, OFPacketIn pi, FloodlightContext cntx)
	{
		
		
		//flag for flow match andorfin policies
		boolean match_policy = false;
		//in Andorfin, id is stored in source mac address
		MACAddress id;
		// the hash for installed flow rule
		int flow_hash = -1;
		
		AOFPolicyRegistration reg_msg = new AOFPolicyRegistration();
		AOFMatch match = new AOFMatch();
		
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx,
                IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		
		id = eth.getSourceMAC();
		
		// Create flow-mod based on packet-in and src-switch
        OFFlowMod fm =
                (OFFlowMod) floodlightProvider.getOFMessageFactory()
                                              .getMessage(OFType.FLOW_MOD);
		
      //TEST 
      		addAOFRuleTest(id);
        
		for (AndorfinRule rule: rules){
			if (rule.app_id == id){
				match_policy = true;
				
				//according to access control policy for packet forwarding
				OFMatch of_match = new OFMatch();
		        of_match.loadFromPacket(pi.getPacketData(), pi.getInPort());
		        flow_hash = of_match.hashCode();
		        
		        List<OFAction> of_actions = new ArrayList<OFAction>(); // Set no action to drop
				if (rule.access_control.is_allowed){
					// send flow via the incoming sw port
					of_actions.add(new OFActionOutput(pi.getInPort(),
                            (short)0xFFFF));
				}
		        
				fm.setCookie(flow_hash)      //store match hash into cookie
				  .setHardTimeout((short) 0) //no hard and idle timeout
		          .setIdleTimeout((short) 0)
		          .setBufferId(OFPacketOut.BUFFER_ID_NONE)
		          .setMatch(of_match)
		          .setActions(of_actions)
		          .setLengthU(OFFlowMod.MINIMUM_LENGTH); // +OFActionOutput.MINIMUM_LENGTH);
				
				//assign predicates to AOFreigstration msg
				for (AOFPredicate p: rule.predicates){
					match.addPredicate(p);
				}
				//add flow_mod to AOFreigstration msg
				if ( flow_hash != -1){ // no corresponding flow rule
					AOFFlowMod flow_mod = new AOFFlowMod(flow_hash);
					AOFActionModFlowTable aof_action = new AOFActionModFlowTable(flow_mod);
					if (!rule.management.is_deny_of_action){ // if the action is enable flow_rule
						aof_action.setFlowMod(flow_mod.enableFlowRuleAction());
					}
						reg_msg.addAction(aof_action);
				}
				//TODO: add report action to AOFreigstration msg
				if ( rule.management.is_report){
					
				}
				
				byte[] byte_reg_msg = null;
				try{
					byte_reg_msg = reg_msg.serialize();
				}catch (IOException e) {
		            logger.error("Failure serialization", e);
		        }
				
				OFPacketOut po = new OFPacketOut()
				.setActions(Arrays.asList(new OFAction[] {new OFActionOutput().setPort(OFPort.OFPP_FLOOD.getValue())}))
	            .setActionsLength((short) OFActionOutput.MINIMUM_LENGTH)
	            .setBufferId(-1)
	            .setInPort((short)1)
	            .setPacketData(byte_reg_msg);
				po.setLengthU(OFPacketOut.MINIMUM_LENGTH + po.getActionsLengthU()
		                + byte_reg_msg.length);
		     try {
		            sw.write(fm, null);
		            sw.write(po, null);      
		        } catch (IOException e) {
		            logger.error("Failure when send out andorfin message", e);
		        }
		     break;	
			}
			
			//TODO: default behavior for andorfin packet_in
			if (!match_policy){
				
			}
		}
		
		return Command.CONTINUE;
	}
}
