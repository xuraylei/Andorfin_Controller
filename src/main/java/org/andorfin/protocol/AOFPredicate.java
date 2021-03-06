package org.andorfin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andorfin.protocol.action.Operation;
	


	public class AOFPredicate{
//		protected byte len;
		public byte event;
		protected Operation[] operations;
		
		
		public AOFPredicate (byte e){
			this.event = e;
			this.operations = new Operation[2];
//			this.len = Integer.SIZE + Byte.SIZE +
//					2*Operation.getLength();
		}
		
		public static int getLength(){
			return 15; 
		}
		public AOFPredicate addOperation(byte event, Operation operation1, Operation operation2){
			this.event = event;
			this.operations[0] = operation1;
			this.operations[1] = operation2;
	//		this.len = Integer.SIZE + Byte.SIZE + 2*Operation.getLength();
			
			return this;
		}
		
		public  byte[] serialize() throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	//		DataOutputStream dos = new DataOutputStream( out );
			
//			dos.writeInt(len);
			out.write(event);
			for (Operation op : this.operations){
				 out.write(op.serialize());
				
			}			
			return out.toByteArray();
			}
	}

