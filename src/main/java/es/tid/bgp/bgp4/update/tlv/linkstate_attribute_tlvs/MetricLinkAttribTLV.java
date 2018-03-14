package es.tid.bgp.bgp4.update.tlv.linkstate_attribute_tlvs;

import es.tid.bgp.bgp4.update.tlv.BGP4TLVFormat;

import javax.print.DocFlavor;

public class MetricLinkAttribTLV extends BGP4TLVFormat{
	
	int length;
	private int metric_type;
	private static final int METRIC_TYPE_OSPF = 1;
	private static final int METRIC_TYPE_IS_IS_SHORT = 2;
	private static final int METRIC_TYPE_IS_IS_LONG = 3;
	private int metric;

	
	
	public MetricLinkAttribTLV(){
		super();
		this.setTLVType(LinkStateAttributeTLVTypes.LINK_ATTRIBUTE_TLV_TYPE_METRIC);
	}
	
	public MetricLinkAttribTLV(byte[] bytes, int offset){
		super(bytes,offset);
		length = this.getTLVValueLength();
        System.out.println("Metric type: "+ String.valueOf(length));
		switch(length){
            case 1:
                this.setMetric_type(METRIC_TYPE_IS_IS_SHORT);
                break;
            case 2:
                this.setMetric_type(METRIC_TYPE_OSPF);
                break;
            case 3:
                this.setMetric_type(METRIC_TYPE_IS_IS_LONG);
                break;
            default:
                log.debug("No metric tlv defined for this tlv length");
                break;
		}
		decode();
	}
	
	@Override
	public void encode() {//de momento solo queremos decodificar, el encode ya lo haremos
		int offset = 4;
		switch(metric_type){

			case METRIC_TYPE_OSPF:
				this.setTLVValueLength(1);
				this.tlv_bytes=new byte[this.getTotalTLVLength()];
				encodeHeader();

				this.tlv_bytes[offset] = (byte)(metric >> 0 & 0xff);
				break;
			case METRIC_TYPE_IS_IS_SHORT:
				this.setTLVValueLength(2);
				this.tlv_bytes=new byte[this.getTotalTLVLength()];
				encodeHeader();

				this.tlv_bytes[offset] = (byte)(metric >> 8 & 0xff);
				this.tlv_bytes[offset + 1] = (byte)(metric >> 16 & 0xff);
				break;
			case METRIC_TYPE_IS_IS_LONG:
				this.setTLVValueLength(3);
				this.tlv_bytes=new byte[this.getTotalTLVLength()];
				encodeHeader();

				this.tlv_bytes[offset] = (byte)(metric >> 16 & 0xff);
				this.tlv_bytes[offset + 1] = (byte)(metric >> 8 & 0xff);
				this.tlv_bytes[offset + 2] = (byte)(metric >> 0 & 0xff);
				//this.tlv_bytes[offset + 3] = (byte)(metric & 0xff);
				break;
			default:
				log.debug("This metric type does not exist");
				break;
		}


		
	}
	
	public void decode(){
		int offset = 4;
		System.out.println("Metric type: "+ String.valueOf(metric_type));
		switch(metric_type){
		case METRIC_TYPE_OSPF:
			setMetric((this.tlv_bytes[offset]&0xFF));
			break;
		case METRIC_TYPE_IS_IS_SHORT:
			setMetric(((this.tlv_bytes[offset]&0xFF)<<8) | ((this.tlv_bytes[offset+1]&0xFF)));	
			break;
		case METRIC_TYPE_IS_IS_LONG:
			setMetric(((this.tlv_bytes[offset]&0xFF)<<16) | ((this.tlv_bytes[offset+1]&0xFF)<<8) | ((this.tlv_bytes[offset+2]&0xFF)));	
			break;
		default:
			log.debug("This metric type does not exist");
			break;
		}
		
	}

	public int getMetric_type() {
		return metric_type;
	}

	public void setMetric_type(int metric_type) {
		this.metric_type = metric_type;
	}
	public int getMetric() {
			return metric;
	}
	
	public void setMetric(int metric) {
			this.metric = metric;
	}
	
	public String toString(){
		System.out.println("Metric type: "+ String.valueOf(metric_type));
		switch(metric_type){
			case METRIC_TYPE_OSPF:
				return "METRIC [type=" + this.getMetric_type() + ", OSPF METRIC="+ this.getMetric() + "]";
			case METRIC_TYPE_IS_IS_SHORT:
				return "METRIC [type=" + this.getMetric_type() + ", ISIS SHORT METRIC="	+ this.getMetric() + "]";
			case METRIC_TYPE_IS_IS_LONG:
				return "METRIC [type=" + this.getMetric_type() + ", ISIS LONG METRIC="	+ this.getMetric() + "]";
			default:
				return "METRIC [type= UNKWOWN METRIC TYPE" + "]";
		}
	}

	
	

}
