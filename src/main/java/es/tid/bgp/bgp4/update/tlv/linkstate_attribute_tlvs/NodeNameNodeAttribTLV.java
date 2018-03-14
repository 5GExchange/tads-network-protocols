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
		int len= name.length;
		this.setTLVValueLength(len);
		//this.setTlv_bytes(new byte[this.getTotalTLVLength()]);
		this.tlv_bytes=new byte[len+4];
		this.encodeHeader();
		System.arraycopy(name,0, this.tlv_bytes, 4, name.length);

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

	    System.arraycopy(b,0, this.name, 0, b.length);
	}


	public void setNameb(byte[] nameb)
	{
		if ((nameb!=null)&&(nameb.length>0)){
			if (this.name==null){
				this.name= new byte[nameb.length];
			}
			System.out.println("len di name è: "+String.valueOf(nameb.length));
	    	System.arraycopy(nameb,0, this.name, 0, nameb.length);
		}
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
