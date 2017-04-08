package es.tid.bgp.bgp4.update.tlv;

import java.net.Inet4Address;
import java.net.UnknownHostException;


/**
 2.1.1.  PCE Descriptors

 The PCE Descriptor field is a set of Type/Length/Value (TLV)
 triplets.  The format of each TLV is as per Section 3.1 of [RFC7752].
 The PCE Descriptor TLVs uniquely identify a PCE.  The following PCE
 descriptor are defined -

 +-----------+-----------------------+----------+
 | Codepoint |       Descriptor TLV  | Length   |
 +-----------+-----------------------+----------+
 |  TBD2     | IPv4 PCE Address      |   4      |
 |  TBD3     | IPv6 PCE Address      |   16     |
 +-----------+-----------------------+----------+
 Table 1: PCE Descriptors

 The PCE address TLVs specifies an IP address that can be used to
 reach the PCE.  The PCE-ADDRESS Sub-TLV defined in [RFC5088] and
 [RFC5089] is used in the OSPF and IS-IS respectively.  The format of
 the PCE address TLV are -



 0                   1                   2                   3
 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |              Type=TBD2        |             Length=4          |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                         IPv4 PCE Address                      |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

 0                   1                   2                   3
 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |              Type=TBD3        |             Length=16         |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                                                               |
 |                         IPv6 PCE Address                      |
 |                                                               |
 |                                                               |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 Figure 2. PCE Address TLVs

 When the PCE has both an IPv4 and IPv6 address, both the TLVs MAY be
 included.
 *
 */
/**
 *
 * @author Andrea and Ajmal
 *
 */


public class PCEv4DescriptorsTLV extends BGP4TLVFormat{


	public static final int PCE_Descriptors_TLV = 1000; //to be verified
	private Inet4Address PCEipv4 = null;




	public PCEv4DescriptorsTLV(){
		super();
		this.setTLVType(PCE_Descriptors_TLV);
	}


	public PCEv4DescriptorsTLV(byte []bytes, int offset) {
		super(bytes, offset);
		decode();
	}

	public void encode(){

		int len = 4;
		this.setTLVValueLength(len);
		this.setTlv_bytes(new byte[this.getTotalTLVLength()]);

		encodeHeader();
		int offset=4;//Header TLV

		byte[] bytesIPv4 = PCEipv4.getAddress();
		System.arraycopy(bytesIPv4,0,this.tlv_bytes,offset,4);
		//offset=offset+4;// 4-Byte counts for the Header

	}


	public void decode(){
		//Decoding PCE DescriptorsTLV
		int offset=4;//4-Bytes for Header


		byte[] PCEipv4bytes=new byte[4];
		System.arraycopy(this.tlv_bytes,offset, PCEipv4bytes, 0, 4);

		try {
			PCEipv4=(Inet4Address)Inet4Address.getByAddress(PCEipv4bytes);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//offset=offset+4;// 4-Byte counts for PCEipv4 Length

	}

	public Inet4Address getPCEv4Address() {
		return PCEipv4;
	}

	public void setPCEv4Address(Inet4Address ip) {
		PCEipv4= ip;
	}



	@Override
	public String toString() {

		StringBuffer sb=new StringBuffer(1000);

		sb.append("PCE ip: "+PCEipv4.getHostAddress());


		return sb.toString();
	}


}
