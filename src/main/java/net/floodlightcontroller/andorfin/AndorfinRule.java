package net.floodlightcontroller.andorfin;

import java.util.ArrayList;
import java.util.List;

import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.util.MACAddress;

import org.andorfin.protocol.AOFPredicate;
import org.andorfin.protocol.ManagementAction;


class AccessControlAction{
	public boolean is_allowed;
	public boolean is_monitored;
	
	IPv4 monitor_address;
	
	public AccessControlAction(){
		this.is_allowed = false;
		this.is_monitored = false;
		monitor_address = null;
	}
}



/*class AnforfinRule is representation of andorfin rule inside Floodlight controller
 * 
 */

public class AndorfinRule{
	protected MACAddress app_id;
	public List<AOFPredicate> predicates;
	protected AccessControlAction  access_control;
	public ManagementAction management;
	
	public AndorfinRule(MACAddress id){
		this.app_id = id;
		this.predicates = new ArrayList();
		this.access_control = new AccessControlAction();
		this.management = new ManagementAction();
	}
	
	public AndorfinRule addPredicate(AOFPredicate predicate){
		this.predicates.add(predicate);
		
		return this;
	}
	
	public AndorfinRule setAccessControlAction(AccessControlAction ac){
		this.access_control = ac;
		
		return this;
	}
	
	public AndorfinRule setManagementAction(ManagementAction ma){
		this.management = ma;
		
		return this;
	}
}
