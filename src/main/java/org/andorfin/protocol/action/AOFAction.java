package org.andorfin.protocol.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.andorfin.protocol.SerializationUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.util.U16;


public class AOFAction {
    
	protected byte type;
	//of_action is the hash value for exact OFMatch of OF flow(s)
//	protected int of_action;
	
    public static int getLength() {
        return Byte.SIZE + Integer.SIZE;
    }


    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

	public  byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		
		out.write(this.type);
		
	   
		return out.toByteArray();
		}
}