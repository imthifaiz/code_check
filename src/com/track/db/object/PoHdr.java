package com.track.db.object;

public class PoHdr {

	@DBTable(columnName ="PLANT") 
	private String PLANT;

	@DBTable(columnName ="PONO") 
	private String PONO;

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

	@DBTable(columnName ="CustCode") 
	private String CustCode;

	@DBTable(columnName ="CustName") 
	private String CustName;

	@DBTable(columnName ="JobNum") 
	private String JobNum;

	@DBTable(columnName ="PersonInCharge") 
	private String PersonInCharge;

	@DBTable(columnName ="contactNum") 
	private String contactNum;

	@DBTable(columnName ="Address") 
	private String Address;

	@DBTable(columnName ="Address2") 
	private String Address2;

	@DBTable(columnName ="Address3") 
	private String Address3;

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

	@DBTable(columnName ="CURRENCYID") 
	private String CURRENCYID;

	@DBTable(columnName ="STATUS_ID") 
	private String STATUS_ID;

	@DBTable(columnName ="REMARK3") 
	private String REMARK3;

	@DBTable(columnName ="ORDERDISCOUNT") 
	private double ORDERDISCOUNT;

	@DBTable(columnName ="SHIPPINGCOST") 
	private double SHIPPINGCOST;

	@DBTable(columnName ="INCOTERMS") 
	private String INCOTERMS;

	@DBTable(columnName ="ADJUSTMENT") 
	private double ADJUSTMENT;

	@DBTable(columnName ="PAYMENTTYPE") 
	private String PAYMENTTYPE;

	@DBTable(columnName ="DELIVERYDATEFORMAT") 
	private Short DELIVERYDATEFORMAT;

	@DBTable(columnName ="PURCHASE_LOCATION") 
	private String PURCHASE_LOCATION;

	@DBTable(columnName ="TAXTREATMENT") 
	private String TAXTREATMENT;

	@DBTable(columnName ="REVERSECHARGE") 
	private Short REVERSECHARGE;

	@DBTable(columnName ="GOODSIMPORT") 
	private Short GOODSIMPORT;
	
	@DBTable(columnName ="ORDER_STATUS") 
	private String ORDER_STATUS;
	
	@DBTable(columnName ="LOCALEXPENSES")
	private double LOCALEXPENSES;
	
	@DBTable(columnName ="ISDISCOUNTTAX") 
	private Short ISDISCOUNTTAX;
	
	@DBTable(columnName ="ISSHIPPINGTAX") 
	private Short ISSHIPPINGTAX;
	
	@DBTable(columnName ="TAXID") 
	private int TAXID;
	
	@DBTable(columnName ="ISTAXINCLUSIVE") 
	private Short ISTAXINCLUSIVE;
	
	@DBTable(columnName ="ORDERDISCOUNTTYPE") 
	private String ORDERDISCOUNTTYPE;
	
	@DBTable(columnName ="CURRENCYUSEQT") 
	private double CURRENCYUSEQT;
	
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
	
	@DBTable(columnName ="POESTNO")   
	private String POESTNO;
	
	@DBTable(columnName ="SHIPCONTACTNAME") 
	private String SHIPCONTACTNAME;

	@DBTable(columnName ="SHIPDESGINATION") 
	private String SHIPDESGINATION;

	@DBTable(columnName ="SHIPWORKPHONE") 
	private String SHIPWORKPHONE;

	@DBTable(columnName ="SHIPHPNO") 
	private String SHIPHPNO;

	@DBTable(columnName ="SHIPEMAIL") 
	private String SHIPEMAIL;

	@DBTable(columnName ="SHIPCOUNTRY") 
	private String SHIPCOUNTRY;

	@DBTable(columnName ="SHIPADDR1") 
	private String SHIPADDR1;

	@DBTable(columnName ="SHIPADDR2") 
	private String SHIPADDR2;

	@DBTable(columnName ="SHIPADDR3") 
	private String SHIPADDR3;

	@DBTable(columnName ="SHIPADDR4") 
	private String SHIPADDR4;

	@DBTable(columnName ="SHIPSTATE") 
	private String SHIPSTATE;

	@DBTable(columnName ="SHIPZIP") 
	private String SHIPZIP;
	
	@DBTable(columnName ="UKEY") 
	private String UKEY;
	
	@DBTable(columnName ="APPROVAL_STATUS") 
	private String APPROVAL_STATUS;
	
	@DBTable(columnName ="REASON") 
	private String REASON;

	@DBTable(columnName ="PURCHASEPEPPOLID") 
	private int PURCHASEPEPPOLID;
	
	@DBTable(columnName ="PEPPOLINVOICEUUID") 
	private String PEPPOLINVOICEUUID;
	
	@DBTable(columnName ="POSENDSTATUS") 
	private String POSENDSTATUS;
	
	@DBTable(columnName ="RESPONCECODE") 
	private String RESPONCECODE;
	
	@DBTable(columnName ="RESPONCEMESSAGE") 
	private String RESPONCEMESSAGE;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getPONO() {
		return PONO;
	}

	public void setPONO(String pONO) {
		PONO = pONO;
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

	public String getCustCode() {
		return CustCode;
	}

	public void setCustCode(String custCode) {
		CustCode = custCode;
	}

	public String getCustName() {
		return CustName;
	}

	public void setCustName(String custName) {
		CustName = custName;
	}

	public String getJobNum() {
		return JobNum;
	}

	public void setJobNum(String jobNum) {
		JobNum = jobNum;
	}

	public String getPersonInCharge() {
		return PersonInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		PersonInCharge = personInCharge;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getAddress2() {
		return Address2;
	}

	public void setAddress2(String address2) {
		Address2 = address2;
	}

	public String getAddress3() {
		return Address3;
	}

	public void setAddress3(String address3) {
		Address3 = address3;
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

	public String getCURRENCYID() {
		return CURRENCYID;
	}

	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
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

	public double getORDERDISCOUNT() {
		return ORDERDISCOUNT;
	}

	public void setORDERDISCOUNT(double oRDERDISCOUNT) {
		ORDERDISCOUNT = oRDERDISCOUNT;
	}

	public double getSHIPPINGCOST() {
		return SHIPPINGCOST;
	}

	public void setSHIPPINGCOST(double sHIPPINGCOST) {
		SHIPPINGCOST = sHIPPINGCOST;
	}

	public String getINCOTERMS() {
		return INCOTERMS;
	}

	public void setINCOTERMS(String iNCOTERMS) {
		INCOTERMS = iNCOTERMS;
	}

	public double getADJUSTMENT() {
		return ADJUSTMENT;
	}

	public void setADJUSTMENT(double aDJUSTMENT) {
		ADJUSTMENT = aDJUSTMENT;
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

	public String getTAXTREATMENT() {
		return TAXTREATMENT;
	}

	public void setTAXTREATMENT(String tAXTREATMENT) {
		TAXTREATMENT = tAXTREATMENT;
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

	public Short getISDISCOUNTTAX() {
		return ISDISCOUNTTAX;
	}

	public void setISDISCOUNTTAX(Short iSDISCOUNTTAX) {
		ISDISCOUNTTAX = iSDISCOUNTTAX;
	}

	public Short getISSHIPPINGTAX() {
		return ISSHIPPINGTAX;
	}

	public void setISSHIPPINGTAX(Short iSSHIPPINGTAX) {
		ISSHIPPINGTAX = iSSHIPPINGTAX;
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

	public String getORDERDISCOUNTTYPE() {
		return ORDERDISCOUNTTYPE;
	}

	public void setORDERDISCOUNTTYPE(String oRDERDISCOUNTTYPE) {
		ORDERDISCOUNTTYPE = oRDERDISCOUNTTYPE;
	}

	public double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
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

	public String getPOESTNO() {
		return POESTNO;
	}

	public void setPOESTNO(String pOESTNO) {
		POESTNO = pOESTNO;
	}

	public String getSHIPCONTACTNAME() {
		return SHIPCONTACTNAME;
	}

	public void setSHIPCONTACTNAME(String sHIPCONTACTNAME) {
		SHIPCONTACTNAME = sHIPCONTACTNAME;
	}

	public String getSHIPDESGINATION() {
		return SHIPDESGINATION;
	}

	public void setSHIPDESGINATION(String sHIPDESGINATION) {
		SHIPDESGINATION = sHIPDESGINATION;
	}

	public String getSHIPWORKPHONE() {
		return SHIPWORKPHONE;
	}

	public void setSHIPWORKPHONE(String sHIPWORKPHONE) {
		SHIPWORKPHONE = sHIPWORKPHONE;
	}

	public String getSHIPHPNO() {
		return SHIPHPNO;
	}

	public void setSHIPHPNO(String sHIPHPNO) {
		SHIPHPNO = sHIPHPNO;
	}

	public String getSHIPEMAIL() {
		return SHIPEMAIL;
	}

	public void setSHIPEMAIL(String sHIPEMAIL) {
		SHIPEMAIL = sHIPEMAIL;
	}

	public String getSHIPCOUNTRY() {
		return SHIPCOUNTRY;
	}

	public void setSHIPCOUNTRY(String sHIPCOUNTRY) {
		SHIPCOUNTRY = sHIPCOUNTRY;
	}

	public String getSHIPADDR1() {
		return SHIPADDR1;
	}

	public void setSHIPADDR1(String sHIPADDR1) {
		SHIPADDR1 = sHIPADDR1;
	}

	public String getSHIPADDR2() {
		return SHIPADDR2;
	}

	public void setSHIPADDR2(String sHIPADDR2) {
		SHIPADDR2 = sHIPADDR2;
	}

	public String getSHIPADDR3() {
		return SHIPADDR3;
	}

	public void setSHIPADDR3(String sHIPADDR3) {
		SHIPADDR3 = sHIPADDR3;
	}

	public String getSHIPADDR4() {
		return SHIPADDR4;
	}

	public void setSHIPADDR4(String sHIPADDR4) {
		SHIPADDR4 = sHIPADDR4;
	}

	public String getSHIPSTATE() {
		return SHIPSTATE;
	}

	public void setSHIPSTATE(String sHIPSTATE) {
		SHIPSTATE = sHIPSTATE;
	}

	public String getSHIPZIP() {
		return SHIPZIP;
	}

	public void setSHIPZIP(String sHIPZIP) {
		SHIPZIP = sHIPZIP;
	}

	public String getUKEY() {
		return UKEY;
	}

	public void setUKEY(String uKEY) {
		UKEY = uKEY;
	}

	public String getAPPROVAL_STATUS() {
		return APPROVAL_STATUS;
	}

	public void setAPPROVAL_STATUS(String aPPROVAL_STATUS) {
		APPROVAL_STATUS = aPPROVAL_STATUS;
	}

	public String getREASON() {
		return REASON;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}

	public int getPURCHASEPEPPOLID() {
		return PURCHASEPEPPOLID;
	}

	public void setPURCHASEPEPPOLID(int pURCHASEPEPPOLID) {
		PURCHASEPEPPOLID = pURCHASEPEPPOLID;
	}

	public String getPEPPOLINVOICEUUID() {
		return PEPPOLINVOICEUUID;
	}

	public void setPEPPOLINVOICEUUID(String pEPPOLINVOICEUUID) {
		PEPPOLINVOICEUUID = pEPPOLINVOICEUUID;
	}

	public String getPOSENDSTATUS() {
		return POSENDSTATUS;
	}

	public void setPOSENDSTATUS(String pOSENDSTATUS) {
		POSENDSTATUS = pOSENDSTATUS;
	}

	public String getRESPONCECODE() {
		return RESPONCECODE;
	}

	public void setRESPONCECODE(String rESPONCECODE) {
		RESPONCECODE = rESPONCECODE;
	}

	public String getRESPONCEMESSAGE() {
		return RESPONCEMESSAGE;
	}

	public void setRESPONCEMESSAGE(String rESPONCEMESSAGE) {
		RESPONCEMESSAGE = rESPONCEMESSAGE;
	}

	@Override
	public String toString() {
		return "PoHdr [PLANT=" + PLANT + ", PONO=" + PONO + ", STATUS=" + STATUS + ", ORDERTYPE=" + ORDERTYPE
				+ ", DELDATE=" + DELDATE + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY
				+ ", CustCode=" + CustCode + ", CustName=" + CustName + ", JobNum=" + JobNum + ", PersonInCharge="
				+ PersonInCharge + ", contactNum=" + contactNum + ", Address=" + Address + ", Address2=" + Address2
				+ ", Address3=" + Address3 + ", CollectionDate=" + CollectionDate + ", CollectionTime=" + CollectionTime
				+ ", Remark1=" + Remark1 + ", Remark2=" + Remark2 + ", SHIPPINGID=" + SHIPPINGID + ", SHIPPINGCUSTOMER="
				+ SHIPPINGCUSTOMER + ", INBOUND_GST=" + INBOUND_GST + ", CURRENCYID=" + CURRENCYID + ", STATUS_ID="
				+ STATUS_ID + ", REMARK3=" + REMARK3 + ", ORDERDISCOUNT=" + ORDERDISCOUNT + ", SHIPPINGCOST="
				+ SHIPPINGCOST + ", INCOTERMS=" + INCOTERMS + ", ADJUSTMENT=" + ADJUSTMENT + ", PAYMENTTYPE="
				+ PAYMENTTYPE + ", DELIVERYDATEFORMAT=" + DELIVERYDATEFORMAT + ", PURCHASE_LOCATION="
				+ PURCHASE_LOCATION + ", TAXTREATMENT=" + TAXTREATMENT + ", REVERSECHARGE=" + REVERSECHARGE
				+ ", GOODSIMPORT=" + GOODSIMPORT + ", ORDER_STATUS=" + ORDER_STATUS + ", LOCALEXPENSES=" + LOCALEXPENSES
				+ ", ISDISCOUNTTAX=" + ISDISCOUNTTAX + ", ISSHIPPINGTAX=" + ISSHIPPINGTAX + ", TAXID=" + TAXID
				+ ", ISTAXINCLUSIVE=" + ISTAXINCLUSIVE + ", ORDERDISCOUNTTYPE=" + ORDERDISCOUNTTYPE + ", CURRENCYUSEQT="
				+ CURRENCYUSEQT + ", PROJECTID=" + PROJECTID + ", TRANSPORTID=" + TRANSPORTID + ", PAYMENT_TERMS="
				+ PAYMENT_TERMS + ", ID=" + ID + ", EMPNO=" + EMPNO + ", POESTNO=" + POESTNO + ", SHIPCONTACTNAME="
				+ SHIPCONTACTNAME + ", SHIPDESGINATION=" + SHIPDESGINATION + ", SHIPWORKPHONE=" + SHIPWORKPHONE
				+ ", SHIPHPNO=" + SHIPHPNO + ", SHIPEMAIL=" + SHIPEMAIL + ", SHIPCOUNTRY=" + SHIPCOUNTRY
				+ ", SHIPADDR1=" + SHIPADDR1 + ", SHIPADDR2=" + SHIPADDR2 + ", SHIPADDR3=" + SHIPADDR3 + ", SHIPADDR4="
				+ SHIPADDR4 + ", SHIPSTATE=" + SHIPSTATE + ", SHIPZIP=" + SHIPZIP + ", UKEY=" + UKEY
				+ ", APPROVAL_STATUS=" + APPROVAL_STATUS + ", REASON=" + REASON + ", PURCHASEPEPPOLID="
				+ PURCHASEPEPPOLID + ", PEPPOLINVOICEUUID=" + PEPPOLINVOICEUUID + ", POSENDSTATUS=" + POSENDSTATUS
				+ ", RESPONCECODE=" + RESPONCECODE + ", RESPONCEMESSAGE=" + RESPONCEMESSAGE + "]";
	}
	
	

}
