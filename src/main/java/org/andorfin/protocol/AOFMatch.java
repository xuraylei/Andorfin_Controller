package org.andorfin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import org.andorfin.protocol.AOFPredicate;

public class AOFMatch{
	protected int len;
	protected List<AOFPredicate> predicates;
	
	
	
	
	public AOFMatch(){
		this.predicates = new ArrayList();
		this.len = Integer.SIZE;
		for(AOFPredicate p : predicates){
			this.len += p.getLength();
		}
	}
	
	
	public int getLength(){
		return len;
	}
	
	public AOFMatch addPredicate(AOFPredicate predicate){
		this.predicates.add(predicate);
		this.len = Integer.SIZE;
		for(AOFPredicate p : predicates){
			this.len += p.getLength();
		}
		return this;
	}
	
	public  byte[] serialize() throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			
			out.write(len);
			for (AOFPredicate p : this.predicates){
				 out.write(p.serialize());
				
			}			
			return out.toByteArray();
			
		}
}
