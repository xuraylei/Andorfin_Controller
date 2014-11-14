package org.andorfin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import org.andorfin.protocol.AOFPredicate;

public class AOFMatch{
//	protected int len;
//	protected List<AOFPredicate> predicates;
	public AOFPredicate predicate;
	
	
	
	public AOFMatch(){
//		this.predicates = new ArrayList();
//		this.len = Integer.SIZE + AOFPredicate.getLength();
//		for(AOFPredicate p : predicates){
//			this.len += p.getLength();
//		}
	}
	
	
	public int getLength(){
//		return len;
		return 11;
	}
		
	public AOFMatch addPredicate(AOFPredicate p){
//		this.predicates.add(predicate);
		this.predicate = p;
//		this.len = Integer.SIZE;
//		for(AOFPredicate p : predicates){
//			this.len += p.getLength();
//		}
//		this.len = Integer.SIZE + predicate.getLength();
		return this;
	}
	
	public  byte[] serialize() throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
	//		DataOutputStream dos = new DataOutputStream( out );
			
//			dos.writeInt(len);
//			for (AOFPredicate p : this.predicates){
//				 out.write(p.serialize());
				
//			}			
			out.write(this.predicate.serialize());
			return out.toByteArray();
			
		}
}
