package org.andorfin.protocol.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.andorfin.protocol.SerializationUtils;



public class AOFActionModFlowTable extends AOFAction implements Cloneable {
	
	// the key is the hash of flow rules in flow table and value is modifcation action
	protected AOFFlowMod aof_flow_mod;
	
	public AOFActionModFlowTable(){
		super.setType(AOFActionType.MOD_FLOWTABLE);
	}
	
	public AOFActionModFlowTable(AOFFlowMod flow_mod){
		super.setType(AOFActionType.MOD_FLOWTABLE);
		
		this.aof_flow_mod = flow_mod;
	}
	
	public AOFActionModFlowTable setFlowMod(AOFFlowMod flow_mod){
		this.aof_flow_mod = flow_mod;
		
		return this;
	}
	
	public  byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		
	//	out.write(super.serialize());
		out.write(this.aof_flow_mod.serialize());
	   
		return out.toByteArray();
	}
}
