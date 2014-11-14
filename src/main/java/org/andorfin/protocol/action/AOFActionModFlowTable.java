package org.andorfin.protocol.action;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

import org.andorfin.protocol.SerializationUtils;



public class AOFActionModFlowTable extends AOFAction implements Cloneable {
	
	// the key is the hash of flow rules in flow table and value is modifcation action
	//protected AOFFlowMod aof_flow_mod;
	protected byte mod_action;
	protected byte flow_entry;
	
	
	public AOFActionModFlowTable(){
		super.setType(AOFActionType.MOD_FLOWTABLE);
		this.mod_action = ModFlowTableType.DISABLE;
	}
	
	public AOFActionModFlowTable(byte flow){
		super.setType(AOFActionType.MOD_FLOWTABLE);
		this.mod_action = ModFlowTableType.DISABLE;
		this.flow_entry = flow;
	}
	
	public AOFActionModFlowTable setFlowEntry(byte flow){
		this.flow_entry = flow;
		
		return this;
	}
	
	//default flow mod action is "DISABLE"
		public AOFActionModFlowTable disableFlowRuleAction(){
			this.mod_action = ModFlowTableType.DISABLE;
			return this;
		}
		
		public AOFActionModFlowTable enableFlowRuleAction() {
			this.mod_action = ModFlowTableType.ENABLE;
			return this;
		}
	
	public  byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
//		DataOutputStream dos = new DataOutputStream( out );
		
		
		out.write(super.serialize());
		out.write(this.flow_entry);
		out.write(this.mod_action);
	   
		return out.toByteArray();
	}
}
