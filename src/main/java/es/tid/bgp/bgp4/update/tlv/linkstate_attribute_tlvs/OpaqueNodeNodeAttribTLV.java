package es.tid.bgp.bgp4.update.tlv.linkstate_attribute_tlvs;

import es.tid.bgp.bgp4.update.tlv.BGP4TLVFormat;
import es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs.OpaqueNodeTLVTypes;
import es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs.PCEAttribSubTLV;

public class OpaqueNodeNodeAttribTLV extends BGP4TLVFormat{

	PCEAttribSubTLV pce;

	public OpaqueNodeNodeAttribTLV(){
		super();
		//los flags estaban mal puestos
		this.setTLVType(LinkStateAttributeTLVTypes.NODE_ATTRIBUTE_TLV_TYPE_OPAQUE_NODE);
	}

	public OpaqueNodeNodeAttribTLV(byte[] bytes, int offset){
		super(bytes,offset);
		decode();
	}
	@Override
	public void encode() {
		//Encode Node Opaque TLV
		if (pce!=null){
			pce.encode();
			TLVValueLength=TLVValueLength+pce.getTotalTLVLength();
		}
		//Length
		this.TotalTLVLength=TLVValueLength+4;
		this.tlv_bytes=new byte[this.TotalTLVLength];
		encodeHeader();
		//Write the bytes
		int offset=4;
		if (pce!=null){
			System.arraycopy(pce.getTlv_bytes(),0, this.tlv_bytes,offset, pce.getTotalTLVLength());
			offset=offset+pce.getTotalTLVLength();
		}

	}

	public void decode(){
		boolean fin=false;
		int offset = 4;
		//Decoding Node Opaque TLV
		while (!fin) {
			int TLVType = BGP4TLVFormat.getType(this.tlv_bytes, offset);
			int TLVLength = BGP4TLVFormat.getTotalTLVLength(this.tlv_bytes, offset);
			//diferenciar en links y nodes para que no de error de compilacion ya que los id de los IPv4 son iguales
			switch (TLVType) {
				//LINK ATTRIBUTES
				case OpaqueNodeTLVTypes.PCE_DESCRIPTORS_TLV_TYPE:
					this.pce = new PCEAttribSubTLV(this.tlv_bytes, offset);
					break;
				default:
					log.warn("Unknown TLV found: "+TLVType);


			}

			offset=offset+TLVLength;
			if (offset>=(this.TLVValueLength)){
				fin=true;
			}
		}
	}

	public PCEAttribSubTLV getPCEAttribSubTLV() {
		return pce;
	}
	public void setPCEAttribSubTLV(PCEAttribSubTLV elem) {
		pce = elem;
	}


	public String toString() {
		StringBuffer sb=new StringBuffer(1000);
		sb.append("Node Opaque:");
		//if (this!=null){
		if (this.pce!= null) {
			sb.append("PCE TLV=\n"+pce.toString());
		}

		return sb.toString();
	}


	/*private byte[] name;
	private Hashtable<Inet4Address, Inet4Address> neighbours = null;

	public OpaqueNodeNodeAttribTLV(){
		super();
		this.setTLVType(LinkStateAttributeTLVTypes.NODE_ATTRIBUTE_TLV_TYPE_OPAQUE_NODE);
	}

	public OpaqueNodeNodeAttribTLV(byte[] bytes, int offset){
		super(bytes,offset);
		this.neighbours=new Hashtable<Inet4Address, Inet4Address>();
		decode();
	}

	@Override
	public void encode() {
		int len= 0;
		//System.out.println("neighbours list is "+String.valueOf(this.neighbours));

		if (this.neighbours!= null) {
			len=neighbours.size()*8;
			//System.out.println("len is: "+String.valueOf(len));
		}
		this.setTLVValueLength(len);
		//this.setTlv_bytes(new byte[this.getTotalTLVLength()]);
		this.tlv_bytes=new byte[len+4];
		this.encodeHeader();
		int offset=4;
		if (this.neighbours!= null) {
			Set<Inet4Address> keys= neighbours.keySet();
			for (Inet4Address as: keys){
				System.arraycopy(as.getAddress(),0, this.tlv_bytes, offset, 4);
				offset=offset+4;
				System.arraycopy(neighbours.get(as).getAddress(),0, this.tlv_bytes, offset, 4);
				offset=offset+4;

			}
		}

	}
	
	public void decode(){
		this.neighbours=new Hashtable<Inet4Address, Inet4Address>();
		int length = this.getTLVValueLength();
		int offset = 4;
		if (this.tlv_bytes==null) {
			return;
		}
		int num_neigh=(length)/8;
		//System.out.println("number of entries is "+String.valueOf(num_neigh));
		for (int i=0; i< num_neigh; i++){
			Inet4Address as =null;
			Inet4Address ip_neigh =null;
			byte[] ip_as=new byte[4];
			byte[] ip_ip=new byte[4];
			System.arraycopy(this.tlv_bytes,offset, ip_as, 0, 4);
			try {
				as=(Inet4Address)Inet4Address.getByAddress(ip_as);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			offset=offset+4;
			System.arraycopy(this.tlv_bytes,offset, ip_ip, 0, 4);
			try {
				ip_neigh=(Inet4Address)Inet4Address.getByAddress(ip_ip);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			offset=offset+4;
			if ((as!=null)&&(ip_neigh!=null)) {
				//System.out.println("AS="+as+" IP="+ip_neigh);
				neighbours.put(as, ip_neigh);
			}
		}
	}

	
	public void setNeighbours(Hashtable<Inet4Address, Inet4Address> neigs)
	{
		this.neighbours= new Hashtable<Inet4Address, Inet4Address>();
		Enumeration<Inet4Address> neigs_k= neigs.keys();
		while (neigs_k.hasMoreElements()){
			Inet4Address neig_k= neigs_k.nextElement();
			Inet4Address nIP= neigs.get(neig_k);
			if ((neig_k!=null)&&(nIP!=null)) {
				this.neighbours.put(neig_k, nIP);
			}
		}

		//this.neighbours = neigs;

	}


	public Hashtable<Inet4Address, Inet4Address> getNeighbours(){
		return this.neighbours;
	}


	public String toString() {
		StringBuffer sb=new StringBuffer(1000);
		sb.append("Node Opaque:");
		//if (this!=null){
		if (this.neighbours!= null) {
			Set<Inet4Address> keys= neighbours.keySet();
			for (Inet4Address as: keys){
				
				sb.append("AS="+((Inet4Address) as).toString() + "IPv4"+((Inet4Address) neighbours.get(as)).toString());
			}
		}

		//}
	    return sb.toString();
	}
	*/

}
