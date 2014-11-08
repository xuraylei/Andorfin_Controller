package org.andorfin.protocol.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.andorfin.protocol.SerializationUtils;



public class AOFFlowMod{
	protected int flow_entry;
	protected byte mod_action;
	
	//default flow mod action is "DISABLE"
	public AOFFlowMod(int fe){
		this.flow_entry = fe; 
		this.mod_action = ModFlowTableType.DISABLE;
	}
	
	public AOFFlowMod enableFlowRuleAction() {
		this.mod_action = ModFlowTableType.ENABLE;
		return this;
	}
	
	public  byte[] serialize() throws IOException {
ByteArrayOutputStream out = new ByteArrayOutputStream( );
		
		out.write(this.flow_entry);
		out.write(this.mod_action);

	   
		return out.toByteArray();
	}
}