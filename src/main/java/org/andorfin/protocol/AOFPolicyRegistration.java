package org.andorfin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import org.andorfin.protocol.action.AOFAction;
import org.openflow.protocol.OFPacketOut;


/*Andorfin message is encapsulated in OFPacketOut message*/
public class AOFPolicyRegistration{

	protected int len;
	protected AOFMatch match;
	protected List<AOFAction> actions;
	
	public AOFPolicyRegistration(){
		this.match = new AOFMatch();
		this.actions = new ArrayList();
		this.len = match.getLength() + actions.size()*AOFAction.getLength();
	} 
	
	public AOFPolicyRegistration setMatch(AOFMatch match){
		this.match = match;
		this.len = match.getLength() + actions.size()*AOFAction.getLength();
		return this;
	}
	
	public AOFPolicyRegistration addAction(AOFAction action){
		this.actions.add(action);
		this.len = match.getLength() + actions.size()*AOFAction.getLength();
		return this;
		}
	
	public  byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		
		out.write(len);
		out.write(match.serialize());
		for (AOFAction act: actions){
			out.write(act.serialize());
		}
	   
		return out.toByteArray();
	}
	
}
