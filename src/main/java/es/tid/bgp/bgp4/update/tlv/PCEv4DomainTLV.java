package es.tid.bgp.bgp4.update.tlv;

import es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs.*;

import java.util.ArrayList;


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


public class PCEv4DomainTLV extends BGP4TLVFormat{

	private ArrayList<AutonomousSystemNodeDescriptorSubTLV> ASSubTLVs = new ArrayList<AutonomousSystemNodeDescriptorSubTLV>();
	private ArrayList<AreaIDNodeDescriptorSubTLV> AreaIDSubTLVs = new ArrayList<AreaIDNodeDescriptorSubTLV>();
	//private AutonomousSystemNodeDescriptorSubTLV autonomousSystemSubTLV; //512
	//private AreaIDNodeDescriptorSubTLV AreaIDSubTLV; //514



	public PCEv4DomainTLV(){
		super();
		this.setTLVType(PCEDescriptorsTLVTypes.PCE_DESCRIPTORS_TLV_TYPE_DOMAIN_ID);
	}


	public PCEv4DomainTLV(byte []bytes, int offset) {
		super(bytes, offset);
		decode();
	}
	
	public void encode(){
		int len = 0;
		len= len + (ASSubTLVs.size() * 8);
		len= len + (AreaIDSubTLVs.size()*8);
		this.setTLVValueLength(len);
		this.setTlv_bytes(new byte[this.getTotalTLVLength()]);
		encodeHeader();
		int offset=4;//TLV Header
		if (ASSubTLVs.size()>0){
			for (AutonomousSystemNodeDescriptorSubTLV AS: ASSubTLVs) {
				if (AS!=null) {
					AS.encode();
					System.arraycopy(AS.getSubTLV_bytes(), 0, this.tlv_bytes, offset, AS.getTotalSubTLVLength());
					offset = offset + AS.getTotalSubTLVLength();
				}
				//else
				//	System.out.println("AS is null");
			}
		}
		//else
		//	System.out.println("AS empty list");

		if (AreaIDSubTLVs.size()>0){
			//System.out.println(" list len=="+String.valueOf(AreaIDSubTLVs.size()));
			for (AreaIDNodeDescriptorSubTLV area: AreaIDSubTLVs) {
				if (area!=null) {
					area.encode();
					//log.info(area.toString());
					//log.info(String.valueOf(area.getSubTLVValueLength()));
					//log.info(this.tlv_bytes.toString());
					//log.info (String.valueOf(area.getSubTLV_bytes()));
					//log.info (String.valueOf(area.getSubTLVValueLength()));
					//System.arraycopy(Area.getSubTLV_bytes(),0,tlv_bytes,offset,Area.getTotalSubTLVLength());
					System.arraycopy(area.getSubTLV_bytes(),0,this.tlv_bytes,offset,area.getTotalSubTLVLength());

					offset=offset+ area.getTotalSubTLVLength();
				}
				//else
					//System.out.println("area is null");

			}
		}
		//else
		//	System.out.println("areaID empty list");
		//System.out.println(this.toString());
	}
	
	
	public void decode(){
		boolean fin=false;
		int offset=4;

		while (!fin) {
			int subtlvType=  BGP4SubTLV.getType(tlv_bytes, offset);
			int subtlvLength=BGP4SubTLV.getTotalSubTLVLength(tlv_bytes, offset);
			//int tlvType=  BGP4TLVFormat.getType(tlv_bytes, offset);
			//int tlvLength=BGP4TLVFormat.getTotalTLVLength(tlv_bytes, offset);
			switch(subtlvType) {

				case NodeDescriptorsSubTLVTypes.NODE_DESCRIPTORS_SUBTLV_TYPE_AUTONOMOUS_SYSTEM:
					ASSubTLVs.add(new AutonomousSystemNodeDescriptorSubTLV(this.tlv_bytes, offset));
					break;

				case NodeDescriptorsSubTLVTypes.NODE_DESCRIPTORS_SUBTLV_TYPE_AREA_ID:
					AreaIDSubTLVs.add(new AreaIDNodeDescriptorSubTLV(this.tlv_bytes, offset));
					//System.out.println("Added a new area in the list");
					break;



				default:
					System.out.println("Local Node Descriptor subtlv Unknown, "+subtlvType);
					break;
			}
			offset=offset+subtlvLength;
			if (offset>=this.TLVValueLength){
				fin=true;
			}
			else{
				log.debug("sigo leyendo NodeDescriptorsSubTLV ");
			}
		}
	}

	public ArrayList<AutonomousSystemNodeDescriptorSubTLV> getASSubTLVs () { return ASSubTLVs; }


	public void setASSubTLVs (ArrayList<AutonomousSystemNodeDescriptorSubTLV> list) { ASSubTLVs= list; }


	public ArrayList<AreaIDNodeDescriptorSubTLV> getAreaIDSubTLVs () { return AreaIDSubTLVs; }


	public void setAreaIDSubTLVs (ArrayList<AreaIDNodeDescriptorSubTLV> list ) { AreaIDSubTLVs=list; }

	public void addASSubTLV (AutonomousSystemNodeDescriptorSubTLV AS) { ASSubTLVs.add(AS); }

	public void addAreaIDSubTLV (AreaIDNodeDescriptorSubTLV Area) { AreaIDSubTLVs.add(Area); }


	@Override
	public String toString() {
		
		StringBuffer sb=new StringBuffer(1000);

		for (AutonomousSystemNodeDescriptorSubTLV AS: ASSubTLVs) {
			sb.append("Local AS: "+AS.toString());
		}

		for (AreaIDNodeDescriptorSubTLV Area: AreaIDSubTLVs) {
			sb.append("Local Area: "+Area.toString());
		}

		return sb.toString();
	}
	
	
}
