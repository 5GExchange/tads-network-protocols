package es.tid.bgp.bgp4.update.tlv.linkstate_attribute_tlvs;

import es.tid.bgp.bgp4.update.tlv.BGP4TLVFormat;

public class NodeNameNodeAttribTLV extends BGP4TLVFormat{
	
	private byte[] name;
	private String nodename;
	public NodeNameNodeAttribTLV(){
		super();
		this.setTLVType(LinkStateAttributeTLVTypes.NODE_ATTRIBUTE_TLV_TYPE_NODE_NAME);
	}
	
	public NodeNameNodeAttribTLV(byte[] bytes, int offset){		
		super(bytes,offset);		
		decode();
	}
	
	@Override
	public void encode() {
		int len=4;// The four bytes of the header plus the 4 first bytes)
		len = len + 4 +name.length;
		this.tlv_bytes=new byte[len];
		this.encodeHeader();
		int offset = 4;
		for(int i=0;i<name.length;i++){
			this.tlv_bytes[offset]=name[i];
			offset++;
		}
		
	}
	
	public void decode(){
		int length = this.getTLVValueLength();
		int offset = 4;
		if (this.tlv_bytes==null) {
			return;
		}
		if (name==null){
			name =new byte[length];
		}
		System.arraycopy(this.tlv_bytes,offset, name, 0, length);

	}

	
	public void setName(String name)
	{
		char[] c = name.toCharArray();
	    byte[] b = new byte[c.length];
	    for (int i = 0; i < c.length; i++)
	      b[i] = (byte)(c[i] & 0x007F);

	    System.arraycopy(b,0, name, 0, b.length);
	}
	
	public byte[] getName(){
		return name;
	}
	
	public String toString(){
		//if (this!=null){
			if (this.getName()!= null) {
				//return "NODE NAME [name=" +    this.getName().toString() + "]";
				return "NODE NAME [name=" + new String(this.getName()) + "]";

			}
		//}
		return "";
	}

}
