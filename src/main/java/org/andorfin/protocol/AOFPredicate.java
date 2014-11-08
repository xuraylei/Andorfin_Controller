package org.andorfin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andorfin.protocol.action.Operation;
	


	public class AOFPredicate{
		protected int len;
		protected byte event;
		protected List<Operation> operations;
		
		
		public AOFPredicate (byte e){
			this.event = e;
			this.operations = new ArrayList();
			this.len = Integer.SIZE + Byte.SIZE +
					operations.size()*Operation.getLength();
		}
		
		public int getLength(){
			return len;
		}
		public AOFPredicate addOperation(byte event, Operation operation){
			this.event = event;
			this.operations.add(operation);
			this.len = Integer.SIZE + Byte.SIZE +
					operations.size()*Operation.getLength();
			
			return this;
		}
		
		public  byte[] serialize() throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			
			out.write(len);
			out.write(event);
			for (Operation op : this.operations){
				 out.write(op.serialize());
				
			}			
			return out.toByteArray();
			}
	}

