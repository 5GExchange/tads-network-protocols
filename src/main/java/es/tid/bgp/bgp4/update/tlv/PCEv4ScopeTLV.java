package es.tid.bgp.bgp4.update.tlv;

import es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs.PCEDescriptorsTLVTypes;


/**
 2.1.1.  PATH SCOPE TLV

 the PATH SCOPE TLV indicates the PCE path computation scope,
 which refers to the PCE's ability to compute or take part in
 the computation of paths for intra-area, inter-AS, or, inter-layer
 TE LSPs.

 TYPE: 2
 LENGTH: 3
 VALUE: 1-octet flags
 		2-octet preference field

 *
 */

/**
 *
 * @author Andrea and Ajmal
 *
 */


public class PCEv4ScopeTLV extends BGP4TLVFormat{

	protected boolean PCE_intraArea=false;
	/**
	 * This bit is set when PCE can act as a PCE for Intra-Area.
	 */
	protected boolean PCE_interArea=false;
	/**
	 * This bit is set when PCE can act as a PCE for Inter-Area TE LSP .
	 */
	protected boolean Default_PEC_interArea_TE=false;
	/**
	 * This bit is set when PCE can act as a default PCE for Inter-Area TE LSP Computation.
	 */
	protected boolean PCE_interAS_TE=false;
	/**
	 * This bit is set when PCE can act as PCE for Inter-AS TE LSP Computation.
	 */
	protected boolean Default_PCE_interAS_TE=false;
	/**
	 * This bit is set when PCE can act as a default PCE for Intra-AS TE LSP Computation.
	 */
	protected boolean Default_PCE_interlayer_TE=false;
	/**
	 * This bit is set when PCE can act as a default PCE for inter-layer TE LSP Computation.
	 */

	private int Pre_L=0;
	private int Pre_R=0;
	private int Pre_S=0;
	private int Pre_Y=0;

	/**
	 * The preference field for intra-area, inter-area ,
	 * inter-AS , inter-layer . The remaining bits are
	 * reserved for future use. These fields have 3-bit space
	 * in the 2-octet Preference field
	 * A value of 7 reflects the highest preference
	 */


	public PCEv4ScopeTLV(){
		super();
		this.setTLVType(PCEDescriptorsTLVTypes.PCE_DESCRIPTORS_TLV_TYPE_SCOPE);
	}


	public PCEv4ScopeTLV(byte []bytes, int offset) {
		super(bytes, offset);
		decode();
	}

	public void encode() {

		int len = 3;// Length= 3-octet
		int Pre_Len=3; //Length of Preference bits
		this.setTLVValueLength(len);
		this.setTlv_bytes(new byte[this.getTotalTLVLength()]);
		encodeHeader();
		int offset = 4;//Header TLV
		this.tlv_bytes[offset] = 0x00; //Setting the Flag Octet
		this.tlv_bytes[offset] = (byte) ((((PCE_intraArea ? 1 : 0) << 7) & 0x80) | (((PCE_interArea ? 1 : 0) << 6) & 0x40) |
				(((Default_PEC_interArea_TE ? 1 : 0) << 5) & 0x20) | (((PCE_interAS_TE ? 1 : 0) << 4) & 0x10) |
				(((Default_PCE_interAS_TE ? 1 : 0) << 3) & 0x08) | (((Default_PCE_interlayer_TE ? 1 : 0) << 2) & 0x04));


		StringBuffer PreL = new StringBuffer();
		StringBuffer PreR = new StringBuffer();
		StringBuffer PreS = new StringBuffer();
		StringBuffer PreY = new StringBuffer();
		StringBuilder Pre_String = new StringBuilder();

		PreL.append(Integer.toBinaryString(getPre_L()));
		for(int n=PreL.length(); n<Pre_Len; n++) //when the length of bitSting is smaller than Pre_Len
		PreL.insert(0, "0"); //Insert 0-bit to the Leftmost
		Pre_String.append(PreL);

		PreR.append(Integer.toBinaryString(getPre_R()));
		for(int n=PreR.length(); n<Pre_Len; n++) //when the length of bitSting is smaller than Pre_Len
			PreR.insert(0, "0"); //Insert 0-bit to the Leftmost
		Pre_String.append(PreR);

		PreS.append(Integer.toBinaryString(getPre_S()));
		for(int n=PreS.length(); n<Pre_Len; n++) //when the length of bitSting is smaller than Pre_Len
			PreS.insert(0, "0"); //Insert 0-bit to the Leftmost
		Pre_String.append(PreS);

		PreY.append(Integer.toBinaryString(getPre_Y()));
		for(int n=PreY.length(); n<Pre_Len; n++) //when the length of bitSting is smaller than Pre_Len
			PreY.insert(0, "0"); //Insert 0-bit to the Leftmost
		Pre_String.append(PreY);


		String Totalstring= Pre_String.toString();
		Boolean[] Arr = new Boolean[Totalstring.length()];
		//BitArray[] b = new BitArray[Totalstring.length()];

		for(int i=0; i<Totalstring.length(); i++) {
			if (Totalstring.substring(i, i + 1).equals("1"))
				Arr[i] = true;
			else
				Arr[i] = false;
		}

		offset = offset+1;// Increment offset
		this.tlv_bytes[offset] = 0x00; //Setting the bits for first octet of the Pref field
		this.tlv_bytes[offset] = (byte) ((((Arr[0] ? 1 : 0) << 7) & 0x80) | (((Arr[1] ? 1 : 0) << 6) & 0x40) |
				(((Arr[2] ? 1 : 0) << 5) & 0x20) | (((Arr[3] ? 1 : 0) << 4) & 0x10) |
				(((Arr[4] ? 1 : 0) << 3) & 0x08) | (((Arr[5] ? 1 : 0) << 2) & 0x04) |
				(((Arr[6] ? 1 : 0) << 1) & 0x02) | (((Arr[7] ? 1 : 0) << 0) & 0x01));

		offset = offset+1;// Increment offset
		this.tlv_bytes[offset] = 0x00; //Setting the bits for second octet of the Pref field
		this.tlv_bytes[offset] = (byte) ((((Arr[8] ? 1 : 0) << 7) & 0x80) | (((Arr[9] ? 1 : 0) << 6) & 0x40) |
				(((Arr[10] ? 1 : 0) << 5) & 0x20) | (((Arr[11] ? 1 : 0) << 4) & 0x10));
	}






	public void decode() {
		//Decoding Path Scope
		int offset = 4;//4-Bytes for Header
		//byte[] value_field=new byte[1];

		//System.arraycopy(this.tlv_bytes,offset, value_field, 0, 1);
		PCE_intraArea = (this.tlv_bytes[offset] & 0x80) == 0x80; //Leftmost bit
		PCE_interArea = (this.tlv_bytes[offset] & 0x40) == 0x40;
		Default_PEC_interArea_TE = (this.tlv_bytes[offset] & 0x20) == 0x20;
		PCE_interAS_TE = (this.tlv_bytes[offset] & 0x10) == 0x10;
		Default_PCE_interAS_TE = (this.tlv_bytes[offset] & 0x08) == 0x08;
		Default_PCE_interlayer_TE = (this.tlv_bytes[offset] & 0x04) == 0x04;

		/*
		 PCE_intraArea= getBit (value_field,0);
		 PCE_interArea=getBit (value_field,1);
		 Default_PEC_interArea_TE=getBit (value_field,2);
		 PCE_interAS_TE=getBit (value_field,3);
		 Default_PCE_interAS_TE=getBit (value_field,4);
		 Default_PCE_interlayer_TE=getBit (value_field,5);
		 */
		offset = offset + 1;
		byte[] Preference_field = new byte[2];
		System.arraycopy(this.tlv_bytes, offset, Preference_field, 0, 2);
		int val = Preference_field[0] | (Preference_field[1] << 8);
		Pre_L = bits(val, 0, 3); //Converting the first 3-bit of Preference field to int value
		Pre_R = bits(val, 3, 3);// Next 3-bit
		Pre_S = bits(val, 6, 3);// Next 3-bit
		Pre_Y = bits(val, 9, 3);// Next 3-bit
		
		StringBuffer sb=new StringBuffer(1000);

		sb.append( "PCEv4Scope [Pre_L=" + Pre_L +
				", Pre_R=" + Pre_R +
				", Pre_S=" + Pre_S + ", Pre_Y=" + Pre_Y);

		System.out.println(sb.toString());
	}

	private static int bits(int n, int offset, int length){
		int m= (n >> (16 - offset - length) & 0x07);
		return m;
	}

	public int getPre_L() {
		return Pre_L;
	}
	public void setPre_L(int pre_L) {
		this.Pre_L = pre_L;
	}
	public int getPre_R() {
		return Pre_R;
	}
	public void setPre_R(int pre_R) {
		this.Pre_R = pre_R;
	}
	public int getPre_S() {return Pre_S;}
	public void setPre_S(int pre_S) { this.Pre_S = pre_S;
	}
	public int getPre_Y() {
		return Pre_Y;
	}
	public void setPre_Y(int pre_Y) {
		this.Pre_Y = pre_Y;
	}

	@Override
	public String toString() {

		StringBuffer sb=new StringBuffer(1000);

		sb.append( "PCEv4Scope [Pre_L=" + Pre_L +
				", Pre_R=" + Pre_R +
				", Pre_S=" + Pre_S + ", Pre_Y=" + Pre_Y);

		return sb.toString();
	}


}
