package com.track.db.object;

public class MultiPoEstHdr {

	@DBTable(columnName ="PLANT") 
	private String PLANT;

	@DBTable(columnName ="POMULTIESTNO") 
	private String POMULTIESTNO;

	@DBTable(columnName ="STATUS") 
	private String STATUS;

	@DBTable(columnName ="ORDERTYPE") 
	private String ORDERTYPE;

	@DBTable(columnName ="DELDATE") 
	private String DELDATE;

	@DBTable(columnName ="CRAT") 
	private String CRAT;

	@DBTable(columnName ="CRBY") 
	private String CRBY;

	@DBTable(columnName ="UPAT") 
	private String UPAT;

	@DBTable(columnName ="UPBY") 
	private String UPBY;

	

	@DBTable(columnName ="JobNum") 
	private String JobNum;

	

	

	@DBTable(columnName ="CollectionDate") 
	private String CollectionDate;

	@DBTable(columnName ="CollectionTime") 
	private String CollectionTime;

	@DBTable(columnName ="Remark1") 
	private String Remark1;

	@DBTable(columnName ="Remark2") 
	private String Remark2;

	@DBTable(columnName ="SHIPPINGID") 
	private String SHIPPINGID;

	@DBTable(columnName ="SHIPPINGCUSTOMER") 
	private String SHIPPINGCUSTOMER;

	@DBTable(columnName ="INBOUND_GST") 
	private double INBOUND_GST;



	@DBTable(columnName ="STATUS_ID") 
	private String STATUS_ID;

	@DBTable(columnName ="REMARK3") 
	private String REMARK3;

	

	@DBTable(columnName ="INCOTERMS") 
	private String INCOTERMS;


	@DBTable(columnName ="PAYMENTTYPE") 
	private String PAYMENTTYPE;

	@DBTable(columnName ="DELIVERYDATEFORMAT") 
	private Short DELIVERYDATEFORMAT;

	@DBTable(columnName ="PURCHASE_LOCATION") 
	private String PURCHASE_LOCATION;


	@DBTable(columnName ="REVERSECHARGE") 
	private Short REVERSECHARGE;

	@DBTable(columnName ="GOODSIMPORT") 
	private Short GOODSIMPORT;
	
	@DBTable(columnName ="ORDER_STATUS") 
	private String ORDER_STATUS;
	
	@DBTable(columnName ="LOCALEXPENSES")
	private double LOCALEXPENSES;

	
	@DBTable(columnName ="TAXID") 
	private int TAXID;
	
	@DBTable(columnName ="ISTAXINCLUSIVE") 
	private Short ISTAXINCLUSIVE;

	
	
	
	@DBTable(columnName ="PROJECTID") 
	private int PROJECTID;
	
	@DBTable(columnName ="TRANSPORTID") 
	private int TRANSPORTID;
	
	@DBTable(columnName ="PAYMENT_TERMS") 
	private String PAYMENT_TERMS;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNO")
	private String EMPNO;
	
	@DBTable(columnName ="SAMEASEXPDATE")
	private short SAMEASEXPDATE;
	
	@DBTable(columnName ="ISPRODUCTASSIGNEDSUPPLIER")
	private short ISPRODUCTASSIGNEDSUPPLIER;
	
	

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getPOMULTIESTNO() {
		return POMULTIESTNO;
	}

	public void setPOMULTIESTNO(String pOMULTIESTNO) {
		POMULTIESTNO = pOMULTIESTNO;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getORDERTYPE() {
		return ORDERTYPE;
	}

	public void setORDERTYPE(String oRDERTYPE) {
		ORDERTYPE = oRDERTYPE;
	}

	public String getDELDATE() {
		return DELDATE;
	}

	public void setDELDATE(String dELDATE) {
		DELDATE = dELDATE;
	}

	public String getCRAT() {
		return CRAT;
	}

	public void setCRAT(String cRAT) {
		CRAT = cRAT;
	}

	public String getCRBY() {
		return CRBY;
	}

	public void setCRBY(String cRBY) {
		CRBY = cRBY;
	}

	public String getUPAT() {
		return UPAT;
	}

	public void setUPAT(String uPAT) {
		UPAT = uPAT;
	}

	public String getUPBY() {
		return UPBY;
	}

	public void setUPBY(String uPBY) {
		UPBY = uPBY;
	}

	

	public String getJobNum() {
		return JobNum;
	}

	public void setJobNum(String jobNum) {
		JobNum = jobNum;
	}

	

	


	public String getCollectionDate() {
		return CollectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		CollectionDate = collectionDate;
	}

	public String getCollectionTime() {
		return CollectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		CollectionTime = collectionTime;
	}

	public String getRemark1() {
		return Remark1;
	}

	public void setRemark1(String remark1) {
		Remark1 = remark1;
	}

	public String getRemark2() {
		return Remark2;
	}

	public void setRemark2(String remark2) {
		Remark2 = remark2;
	}

	public String getSHIPPINGID() {
		return SHIPPINGID;
	}

	public void setSHIPPINGID(String sHIPPINGID) {
		SHIPPINGID = sHIPPINGID;
	}

	public String getSHIPPINGCUSTOMER() {
		return SHIPPINGCUSTOMER;
	}

	public void setSHIPPINGCUSTOMER(String sHIPPINGCUSTOMER) {
		SHIPPINGCUSTOMER = sHIPPINGCUSTOMER;
	}

	public double getINBOUND_GST() {
		return INBOUND_GST;
	}

	public void setINBOUND_GST(double iNBOUND_GST) {
		INBOUND_GST = iNBOUND_GST;
	}



	public String getSTATUS_ID() {
		return STATUS_ID;
	}

	public void setSTATUS_ID(String sTATUS_ID) {
		STATUS_ID = sTATUS_ID;
	}

	public String getREMARK3() {
		return REMARK3;
	}

	public void setREMARK3(String rEMARK3) {
		REMARK3 = rEMARK3;
	}



	public String getINCOTERMS() {
		return INCOTERMS;
	}

	public void setINCOTERMS(String iNCOTERMS) {
		INCOTERMS = iNCOTERMS;
	}

	

	public String getPAYMENTTYPE() {
		return PAYMENTTYPE;
	}

	public void setPAYMENTTYPE(String pAYMENTTYPE) {
		PAYMENTTYPE = pAYMENTTYPE;
	}

	public Short getDELIVERYDATEFORMAT() {
		return DELIVERYDATEFORMAT;
	}

	public void setDELIVERYDATEFORMAT(Short dELIVERYDATEFORMAT) {
		DELIVERYDATEFORMAT = dELIVERYDATEFORMAT;
	}

	public String getPURCHASE_LOCATION() {
		return PURCHASE_LOCATION;
	}

	public void setPURCHASE_LOCATION(String pURCHASE_LOCATION) {
		PURCHASE_LOCATION = pURCHASE_LOCATION;
	}



	public Short getREVERSECHARGE() {
		return REVERSECHARGE;
	}

	public void setREVERSECHARGE(Short rEVERSECHARGE) {
		REVERSECHARGE = rEVERSECHARGE;
	}

	public Short getGOODSIMPORT() {
		return GOODSIMPORT;
	}

	public void setGOODSIMPORT(Short gOODSIMPORT) {
		GOODSIMPORT = gOODSIMPORT;
	}

	public String getORDER_STATUS() {
		return ORDER_STATUS;
	}

	public void setORDER_STATUS(String oRDER_STATUS) {
		ORDER_STATUS = oRDER_STATUS;
	}

	public double getLOCALEXPENSES() {
		return LOCALEXPENSES;
	}

	public void setLOCALEXPENSES(double lOCALEXPENSES) {
		LOCALEXPENSES = lOCALEXPENSES;
	}

	

	public int getTAXID() {
		return TAXID;
	}

	public void setTAXID(int tAXID) {
		TAXID = tAXID;
	}

	public Short getISTAXINCLUSIVE() {
		return ISTAXINCLUSIVE;
	}

	public void setISTAXINCLUSIVE(Short iSTAXINCLUSIVE) {
		ISTAXINCLUSIVE = iSTAXINCLUSIVE;
	}

	

	public int getPROJECTID() {
		return PROJECTID;
	}

	public void setPROJECTID(int pROJECTID) {
		PROJECTID = pROJECTID;
	}
	
	public int getTRANSPORTID() {
		return TRANSPORTID;
	}
	
	public void setTRANSPORTID(int tRANSPORTID) {
		TRANSPORTID = tRANSPORTID;
	}
	
	public String getPAYMENT_TERMS() {
		return PAYMENT_TERMS;
	}
	public void setPAYMENT_TERMS(String pAYMENT_TERMS) {
		PAYMENT_TERMS = pAYMENT_TERMS;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String getEMPNO() {
		return EMPNO;
	}

	public void setEMPNO(String eMPNO) {
		EMPNO = eMPNO;
	}
	
	public int getSAMEASEXPDATE() {
		return SAMEASEXPDATE;
	}
	
	public void setSAMEASEXPDATE(short sAMEASEXPDATE) {
		SAMEASEXPDATE = sAMEASEXPDATE;
	}
	
	public int getISPRODUCTASSIGNEDSUPPLIER() {
		return ISPRODUCTASSIGNEDSUPPLIER;
	}
	
	public void setISPRODUCTASSIGNEDSUPPLIER(short iSPRODUCTASSIGNEDSUPPLIER) {
		ISPRODUCTASSIGNEDSUPPLIER = iSPRODUCTASSIGNEDSUPPLIER;
	}

	

	@Override
	public String toString() {
		return "multiPoEstHdr [PLANT=" + PLANT + ", POMULTIESTNO=" + POMULTIESTNO + ", STATUS=" + STATUS + ", ORDERTYPE=" + ORDERTYPE
				+ ", DELDATE=" + DELDATE + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY
				+ ",  JobNum=" + JobNum + ",  CollectionDate=" + CollectionDate + ", CollectionTime=" + CollectionTime
				+ ", Remark1=" + Remark1 + ", Remark2=" + Remark2 + ", SHIPPINGID=" + SHIPPINGID + ", SHIPPINGCUSTOMER="
				+ SHIPPINGCUSTOMER + ", INBOUND_GST=" + INBOUND_GST + ", STATUS_ID="
				+ STATUS_ID + ", REMARK3=" + REMARK3 + ", INCOTERMS=" 
				+ INCOTERMS + ",  PAYMENTTYPE=" + PAYMENTTYPE + ", DELIVERYDATEFORMAT=" + DELIVERYDATEFORMAT + ", PURCHASE_LOCATION="
				+ PURCHASE_LOCATION + ", REVERSECHARGE=" + REVERSECHARGE
				+ ", GOODSIMPORT=" + GOODSIMPORT + ", ORDER_STATUS=" + ORDER_STATUS + ", LOCALEXPENSES=" + LOCALEXPENSES
				+ ", TAXID=" + TAXID + ", ISTAXINCLUSIVE=" + ISTAXINCLUSIVE  
			    + ", PROJECTID=" + PROJECTID + ", TRANSPORTID=" + TRANSPORTID + ",EMPNO=" + EMPNO + ",  PAYMENT_TERMS=" + PAYMENT_TERMS + " ID = "+ID+", SAMEASEXPDATE = "+SAMEASEXPDATE+", ISPRODUCTASSIGNEDSUPPLIER = "+ISPRODUCTASSIGNEDSUPPLIER+"]";
	}

	

}
