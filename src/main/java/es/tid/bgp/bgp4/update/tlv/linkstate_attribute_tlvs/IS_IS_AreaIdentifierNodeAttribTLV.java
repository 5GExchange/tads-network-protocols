package es.tid.bgp.bgp4.update.tlv.linkstate_attribute_tlvs;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;

import es.tid.bgp.bgp4.update.tlv.BGP4TLVFormat;

/**
 * 
 * 
 * @author pac
 *
 */

public class IS_IS_AreaIdentifierNodeAttribTLV extends BGP4TLVFormat{

	
	int length;
	int valid_len;
	private byte[] address = null;
	private LinkedList <Inet4Address> ipv4areaIDs = new LinkedList <Inet4Address>();
	
	public IS_IS_AreaIdentifierNodeAttribTLV() {
		super();
		this.setTLVType(LinkStateAttributeTLVTypes.NODE_ATTRIBUTE_TLV_TYPE_IS_IS_AREA_ID);
		// TODO Auto-generated constructor stub
	}

	public IS_IS_AreaIdentifierNodeAttribTLV(byte[] bytes, int offset) {
		super(bytes, offset);
		length = this.getTLVValueLength();
		decode();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encode() {
		int len=4;// The four bytes of the header plus the 4 first bytes)
		if (valid_len==4)
			this.setTLVValueLength(len+(ipv4areaIDs.size()*valid_len));
		else
			this.setTLVValueLength(len+valid_len);


		this.tlv_bytes=new byte[this.getTotalTLVLength()];
		this.encodeHeader();
		int offset = 4;
		if (valid_len==4) {
			for (int i = 0; i < ipv4areaIDs.size(); ++i) {
				System.arraycopy(ipv4areaIDs.get(i).getAddress(),0, this.tlv_bytes, 4, 4);
			}
		}
		else{
			if ((valid_len<4)&&(ipv4areaIDs.size()==1)){
				System.arraycopy(ipv4areaIDs.get(0).getAddress(),(4-valid_len), this.tlv_bytes, 4, valid_len);
			}
		}
	}
	
	public void decode(){
		//System.out.println("xxxxxxxxxxxxxxxxxxx Andrea lenght of area  : "+String.valueOf(length));
		int offset = 4;
		if (length%4==0){
			int number_addresses = length/4;
			valid_len=4;
			address=new byte[4];
			Inet4Address idarea = null;
			for (int i=0; i<number_addresses; i++){
				System.arraycopy(this.tlv_bytes,offset, address, 0, 4);
				try {
					idarea= (Inet4Address) Inet4Address.getByAddress(address);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ipv4areaIDs.add(idarea);
				offset+=4;
			}
		}
		else{
			if (length<4){
				address=new byte[4];
				valid_len=3;
				Inet4Address idarea = null;
				System.arraycopy(this.tlv_bytes,offset, address, (4-length), length);
				try {
					idarea= (Inet4Address) Inet4Address.getByAddress(address);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ipv4areaIDs.add(idarea);

			}
		}

	}

	public byte[] getAddress() {
		return address;
	}

	public void setAddress(byte[] address) {
		this.address = address;
	}

	public int getValid_len() {
		return valid_len;
	}

	public void setValid_len(int len) {
		this.valid_len = len;
	}

	public LinkedList <Inet4Address> getIpv4areaIDs() {
		return ipv4areaIDs;
	}

	public void setIpv4areaIDs(LinkedList <Inet4Address> ipv4areaIDs) {
		this.ipv4areaIDs = ipv4areaIDs;
	}
	
	public String toString(){
			String ret="";
			for (int i=0;i<ipv4areaIDs.size();++i) {
				ret="ISIS AREA ["+i+"] IDENTIFIER: "+ipv4areaIDs.get(i).toString();
			} 
			return ret;
	}

}
