package es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 
 * @author pac
 *
 */

public class IGPRouterIDNodeDescriptorSubTLV extends NodeDescriptorsSubTLV {

	public static final int IGP_ROUTER_ID_TYPE_GENERIC=0;
	public static final int IGP_ROUTER_ID_TYPE_IS_IS_NON_PSEUDO = 1;
	public static final int IGP_ROUTER_ID_TYPE_IS_IS_PSEUDO = 2;
	//en un futuro habra que distinguir entre ospf_v2 y ospf_v3 usando la longitud en combinacion con el protocol_id
	//de momento no hay protocol_id especificico para ospf_v3
	public static final int IGP_ROUTER_ID_TYPE_OSPF_NON_PSEUDO = 3;
	public static final int IGP_ROUTER_ID_TYPE_OSPF_PSEUDO=4;

	
	private int igp_router_id_type;//initialized to 2--> rest not implemented yet
	
	private Inet4Address ipv4Address_ospf = null;
	private Inet4Address ipv4Address_ospf_dr_address = null;
	private int ISIS_ISO_NODE_ID;
	private BigInteger newISIS_ISO_NODE_ID;
	private byte[] address = null;
	private int PSN_IDENT;
	//private String charPSN_IDENT;




	public IGPRouterIDNodeDescriptorSubTLV() {
		super();
		this.setSubTLVType(NodeDescriptorsSubTLVTypes.NODE_DESCRIPTORS_SUBTLV_TYPE_IGP_ROUTER_ID);
	}
	
	public IGPRouterIDNodeDescriptorSubTLV(byte [] bytes, int offset){
		super(bytes, offset);
		decode();
	}

	private void decode() {
		int length = this.getSubTLVValueLength();
		int offset = 4; //SubTLV header
		switch(length){
		case 4:
			setIGP_router_id_type(IGP_ROUTER_ID_TYPE_OSPF_NON_PSEUDO);
			
		    address=new byte[4]; 

			System.arraycopy(this.subtlv_bytes,offset, address, 0, 4);
			try {
				ipv4Address_ospf =  (Inet4Address)Inet4Address.getByAddress(address);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} 	
			break;
		case 6:
			setIGP_router_id_type(IGP_ROUTER_ID_TYPE_IS_IS_NON_PSEUDO);
			
			address=new byte[6]; 

			System.arraycopy(this.subtlv_bytes,offset, address, 0, 6);

			/*int address  = addr[3] & 0xFF;
			address |= ((addr[2] << 8) & 0xFF00);
			address |= ((addr[1] << 16) & 0xFF0000);
			address |= ((addr[0] << 24) & 0xFF000000);

			*/
			this.ISIS_ISO_NODE_ID= ((this.subtlv_bytes[offset]&0xFF)<<40) | ((this.subtlv_bytes[offset+1]&0xFF)<<32) | ((this.subtlv_bytes[offset+2]&0xFF)<<24) |  (this.subtlv_bytes[offset+3]&0xFF<<16)  |  (this.subtlv_bytes[offset+4]&0xFF<<8)|  (this.subtlv_bytes[offset+5]&0xFF);
			break;
			
		case 7:
			setIGP_router_id_type(IGP_ROUTER_ID_TYPE_IS_IS_PSEUDO);


			address=new byte[6];
			byte[] psn=new byte[1];
			System.arraycopy(this.subtlv_bytes,offset, address, 0, 6);
			System.arraycopy(this.subtlv_bytes,offset+6, psn, 0, 1);

			this.ISIS_ISO_NODE_ID= ((this.subtlv_bytes[offset]&0xFF)<<40) | ((this.subtlv_bytes[offset+1]&0xFF)<<32) | ((this.subtlv_bytes[offset+2]&0xFF)<<24) |  (this.subtlv_bytes[offset+3]&0xFF<<16)  |  (this.subtlv_bytes[offset+4]&0xFF<<8)|  (this.subtlv_bytes[offset+5]&0xFF);
			this.PSN_IDENT = this.subtlv_bytes[offset+6]&0xFF;
			//this.charPSN_IDENT=new String(psn);
			//this.charPSN_IDENT=new String(psn);
			break;
		
		case 8:
			setIGP_router_id_type(IGP_ROUTER_ID_TYPE_OSPF_PSEUDO);
			
			 address=new byte[4]; 

			System.arraycopy(this.subtlv_bytes,offset, address, 0, 4);
			try {
					ipv4Address_ospf =  (Inet4Address)Inet4Address.getByAddress(address);
			} catch (UnknownHostException e) {
					e.printStackTrace();
			}
			
			System.arraycopy(this.subtlv_bytes,offset+4, address, 0, 4);
			try {
					ipv4Address_ospf_dr_address =  (Inet4Address)Inet4Address.getByAddress(address);
			} catch (UnknownHostException e) {
					e.printStackTrace();
			}			
			break;			
		default:
			log.debug("IGP Node ID Type: UNKNOWN/GENERIC");
			setIGP_router_id_type(IGP_ROUTER_ID_TYPE_GENERIC);
		
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public void encode() {
		switch(igp_router_id_type){
		case IGP_ROUTER_ID_TYPE_OSPF_NON_PSEUDO:
			log.debug("Encoding IGP Node ID Type: OSPF NON PSEUDO");
			int length = 4;
			this.setSubTLVValueLength(length);
			this.subtlv_bytes=new byte[this.getTotalSubTLVLength()];
			encodeHeader();
			int offset = 4;
			try{
				System.arraycopy(this.getIpv4AddressOSPF().getAddress(), 0, this.subtlv_bytes, offset, 4);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		case IGP_ROUTER_ID_TYPE_IS_IS_PSEUDO:
			log.debug("Encoding IGP Node ID Type: ISIS PSEUDO");
			length = 7;
			this.setSubTLVValueLength(length);
			this.subtlv_bytes=new byte[this.getTotalSubTLVLength()];
			encodeHeader();
			offset = 4;
			byte[] addr = new byte[7];

			addr[0] = (byte) ((this.ISIS_ISO_NODE_ID >>> 40) & 0xFF);
			addr[1] = (byte) ((this.ISIS_ISO_NODE_ID >>> 32) & 0xFF);
			addr[2] = (byte) ((this.ISIS_ISO_NODE_ID >>> 24) & 0xFF);
			addr[3] = (byte) ((this.ISIS_ISO_NODE_ID >>> 16)& 0xFF);
			addr[4] = (byte) ((this.ISIS_ISO_NODE_ID >>> 8) & 0xFF);
			addr[5] = (byte) (this.ISIS_ISO_NODE_ID & 0xFF);
			addr[6] = (byte) (this.PSN_IDENT & 0xFF);

			System.arraycopy(addr, 0, this.subtlv_bytes, offset, 7);
			break;
		case IGP_ROUTER_ID_TYPE_IS_IS_NON_PSEUDO:
			log.debug("Encoding IGP Node ID Type: ISIS NON PSEUDO");
			length = 6;
			this.setSubTLVValueLength(length);
			this.subtlv_bytes=new byte[this.getTotalSubTLVLength()];
			encodeHeader();
			offset = 4;
			addr = new byte[6];

			addr[0] = (byte) ((this.ISIS_ISO_NODE_ID >>> 40) & 0xFF);
			addr[1] = (byte) ((this.ISIS_ISO_NODE_ID >>> 32) & 0xFF);
			addr[2] = (byte) ((this.ISIS_ISO_NODE_ID >>> 24) & 0xFF);
			addr[3] = (byte) ((this.ISIS_ISO_NODE_ID >>> 16)& 0xFF);
			addr[4] = (byte) ((this.ISIS_ISO_NODE_ID >>> 8) & 0xFF);
			addr[5] = (byte) (this.ISIS_ISO_NODE_ID & 0xFF);
			System.arraycopy(addr, 0, this.subtlv_bytes, offset, 6);
			break;
		default:
			log.error("Please set the type code");
		}
	}

	public Inet4Address getIpv4AddressOSPF() {
		return ipv4Address_ospf;
	}

	public void setIpv4AddressOSPF(Inet4Address ipv4Address) {
		this.ipv4Address_ospf = ipv4Address;
	}

	public int getIGP_router_id_type() {
		return igp_router_id_type;
	}

	public void setIGP_router_id_type(int igp_router_id_type) {
		this.igp_router_id_type = igp_router_id_type;
	}

	public byte[] getAddress() {
		return address;
	}

	public void setAddress(byte[] unknown_address) {
		this.address = unknown_address;
	}
	
	public Inet4Address getIpv4Address_ospf_dr_address() {
		return ipv4Address_ospf_dr_address;
	}

	public void setIpv4Address_ospf_dr_address(
			Inet4Address ipv4Address_ospf_dr_address) {
		this.ipv4Address_ospf_dr_address = ipv4Address_ospf_dr_address;
	}

	public BigInteger getcharISIS_ISO_NODE_ID() {
		return newISIS_ISO_NODE_ID;
	}

	public void setcharISIS_ISO_NODE_ID(long xx) {
		newISIS_ISO_NODE_ID = BigInteger.valueOf(xx);
	}


	public int getISIS_ISO_NODE_ID() {
		return ISIS_ISO_NODE_ID;
	}

	public void setISIS_ISO_NODE_ID(int iSIS_ISO_NODE_ID) {
		ISIS_ISO_NODE_ID = iSIS_ISO_NODE_ID;
	}

	public int getPSN_IDENT() {
		return PSN_IDENT;
	}

	public void setPSN_IDENT(int pSN_IDENT) {
		PSN_IDENT = pSN_IDENT;
	}



	public String toString() {
		int length=this.getSubTLVValueLength();
		switch(length){
		case 4:
			return "IGP_ROUTER_ID [type=" + this.getIGP_router_id_type() + ", ID_OSPF_NON_PSEUDO="
			+ this.getIpv4AddressOSPF() + "]";
		case 6:
			return "IGP_ROUTER_ID [type=" + this.getIGP_router_id_type() + ", ISO_NODE_ID="
			+ this.getISIS_ISO_NODE_ID() + " charISO_NODE_ID="+ this.getcharISIS_ISO_NODE_ID()+" ]";
		case 7:
			return "IGP_ROUTER_ID [type=" + this.getIGP_router_id_type() + ", ISO_NODE_ID_DESIGNATED_ROUTER="
			+ this.getISIS_ISO_NODE_ID() + " PSN_IDENT " +this.getPSN_IDENT()+"/n" +
					" charISO_NODE_ID="+ this.getcharISIS_ISO_NODE_ID()+" ]";
		case 8:
			return "IGP_ROUTER_ID [type=" + this.getIGP_router_id_type() + ", ID_OSPF_PSEUDO="
			+ this.getIpv4AddressOSPF() + "IPv4 address of DRouter Interface"+ipv4Address_ospf_dr_address+"]";
		default:
			return "IGP_ROUTER_ID [type=" + this.getIGP_router_id_type() + "]";
		}
	}

	public int getIgp_router_id_type() {
		return igp_router_id_type;
	}

	public void setIgp_router_id_type(int igp_router_id_type) {
		this.igp_router_id_type = igp_router_id_type;
	}

	public Inet4Address getIpv4Address_ospf() {
		return ipv4Address_ospf;
	}

	public void setIpv4Address_ospf(Inet4Address ipv4Address_ospf) {
		this.ipv4Address_ospf = ipv4Address_ospf;
	}

}
