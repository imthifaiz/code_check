package com.track.db.object;

public class DoHdrApproval {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="DONO")
	private String DONO;
	
	@DBTable(columnName ="ORDERTYPE")
	private String ORDERTYPE;
	
	@DBTable(columnName ="DELDATE")
	private String DELDATE;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
	
	@DBTable(columnName ="PickStaus")
	private String PickStaus;
	
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
	
	@DBTable(columnName ="CURRENCYID")
	private String CURRENCYID;
	
	@DBTable(columnName ="DELIVERYDATE")
	private String DELIVERYDATE;
	
	@DBTable(columnName ="TIMESLOTS")
	private String TIMESLOTS;
	
	@DBTable(columnName ="OUTBOUND_GST")
	private double OUTBOUND_GST;
	
	@DBTable(columnName ="STATUS_ID")
	private String STATUS_ID;
	
	@DBTable(columnName ="EMPNO")
	private String EMPNO;
	
	@DBTable(columnName ="ESTNO")
	private String ESTNO;
	
	@DBTable(columnName ="Remark3")
	private String Remark3;
	
	@DBTable(columnName ="ORDERDISCOUNT")
	private double ORDERDISCOUNT;
	
	@DBTable(columnName ="SHIPPINGCOST")
	private double SHIPPINGCOST;

	@DBTable(columnName ="INCOTERMS")
	private String INCOTERMS;
	
	@DBTable(columnName ="PAYMENTTYPE")
	private String PAYMENTTYPE;
	
	@DBTable(columnName ="DELIVERYDATEFORMAT")
	private short DELIVERYDATEFORMAT;
	
	@DBTable(columnName ="APPROVESTATUS")
	private String APPROVESTATUS;
	
	@DBTable(columnName ="SALES_LOCATION")
	private String SALES_LOCATION;
	
	@DBTable(columnName ="TAXTREATMENT")
	private String TAXTREATMENT;
	
	@DBTable(columnName ="ORDER_STATUS")
	private String ORDER_STATUS;
	
	@DBTable(columnName ="DISCOUNT")
	private double DISCOUNT;
	
	@DBTable(columnName ="DISCOUNT_TYPE")
	private String DISCOUNT_TYPE;
	
	@DBTable(columnName ="ADJUSTMENT")
	private double ADJUSTMENT;
	
	@DBTable(columnName ="ITEM_RATES")
	private short ITEM_RATES;
	
	@DBTable(columnName ="CURRENCYUSEQT")
	private double CURRENCYUSEQT;
	
	@DBTable(columnName ="ORDERDISCOUNTTYPE")
	private String ORDERDISCOUNTTYPE;
	
	@DBTable(columnName ="TAXID")
	private int TAXID;
	
	@DBTable(columnName ="ISDISCOUNTTAX")
	private short ISDISCOUNTTAX;
	
	@DBTable(columnName ="ISORDERDISCOUNTTAX")
	private short ISORDERDISCOUNTTAX;
	
	@DBTable(columnName ="ISSHIPPINGTAX")
	private short ISSHIPPINGTAX;
	
	@DBTable(columnName ="PROJECTID")
	private int PROJECTID;
	
	@DBTable(columnName ="TRANSPORTID")
	private int TRANSPORTID;
	
	@DBTable(columnName ="PAYMENT_TERMS") 
	private String PAYMENT_TERMS;
	
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

	@DBTable(columnName ="APP_CUST_ORDER_STATUS")
	private String APP_CUST_ORDER_STATUS;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="UKEY") 
	private String UKEY;
	
	@DBTable(columnName ="REASON") 
	private String REASON;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getDONO() {
		return DONO;
	}

	public void setDONO(String dONO) {
		DONO = dONO;
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

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getPickStaus() {
		return PickStaus;
	}

	public void setPickStaus(String pickStaus) {
		PickStaus = pickStaus;
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

	public String getCURRENCYID() {
		return CURRENCYID;
	}

	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
	}

	public String getDELIVERYDATE() {
		return DELIVERYDATE;
	}

	public void setDELIVERYDATE(String dELIVERYDATE) {
		DELIVERYDATE = dELIVERYDATE;
	}

	public String getTIMESLOTS() {
		return TIMESLOTS;
	}

	public void setTIMESLOTS(String tIMESLOTS) {
		TIMESLOTS = tIMESLOTS;
	}

	public double getOUTBOUND_GST() {
		return OUTBOUND_GST;
	}

	public void setOUTBOUND_GST(double oUTBOUND_GST) {
		OUTBOUND_GST = oUTBOUND_GST;
	}

	public String getSTATUS_ID() {
		return STATUS_ID;
	}

	public void setSTATUS_ID(String sTATUS_ID) {
		STATUS_ID = sTATUS_ID;
	}

	public String getEMPNO() {
		return EMPNO;
	}

	public void setEMPNO(String eMPNO) {
		EMPNO = eMPNO;
	}

	public String getESTNO() {
		return ESTNO;
	}

	public void setESTNO(String eSTNO) {
		ESTNO = eSTNO;
	}

	public String getRemark3() {
		return Remark3;
	}

	public void setRemark3(String remark3) {
		Remark3 = remark3;
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

	public String getPAYMENTTYPE() {
		return PAYMENTTYPE;
	}

	public void setPAYMENTTYPE(String pAYMENTTYPE) {
		PAYMENTTYPE = pAYMENTTYPE;
	}

	public short getDELIVERYDATEFORMAT() {
		return DELIVERYDATEFORMAT;
	}

	public void setDELIVERYDATEFORMAT(short dELIVERYDATEFORMAT) {
		DELIVERYDATEFORMAT = dELIVERYDATEFORMAT;
	}

	public String getAPPROVESTATUS() {
		return APPROVESTATUS;
	}

	public void setAPPROVESTATUS(String aPPROVESTATUS) {
		APPROVESTATUS = aPPROVESTATUS;
	}

	public String getSALES_LOCATION() {
		return SALES_LOCATION;
	}

	public void setSALES_LOCATION(String sALES_LOCATION) {
		SALES_LOCATION = sALES_LOCATION;
	}

	public String getTAXTREATMENT() {
		return TAXTREATMENT;
	}

	public void setTAXTREATMENT(String tAXTREATMENT) {
		TAXTREATMENT = tAXTREATMENT;
	}

	public String getORDER_STATUS() {
		return ORDER_STATUS;
	}

	public void setORDER_STATUS(String oRDER_STATUS) {
		ORDER_STATUS = oRDER_STATUS;
	}

	public double getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(double dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

	public String getDISCOUNT_TYPE() {
		return DISCOUNT_TYPE;
	}

	public void setDISCOUNT_TYPE(String dISCOUNT_TYPE) {
		DISCOUNT_TYPE = dISCOUNT_TYPE;
	}

	public double getADJUSTMENT() {
		return ADJUSTMENT;
	}

	public void setADJUSTMENT(double aDJUSTMENT) {
		ADJUSTMENT = aDJUSTMENT;
	}

	public short getITEM_RATES() {
		return ITEM_RATES;
	}

	public void setITEM_RATES(short iTEM_RATES) {
		ITEM_RATES = iTEM_RATES;
	}

	public double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
	}

	public String getORDERDISCOUNTTYPE() {
		return ORDERDISCOUNTTYPE;
	}

	public void setORDERDISCOUNTTYPE(String oRDERDISCOUNTTYPE) {
		ORDERDISCOUNTTYPE = oRDERDISCOUNTTYPE;
	}

	public int getTAXID() {
		return TAXID;
	}

	public void setTAXID(int tAXID) {
		TAXID = tAXID;
	}

	public short getISDISCOUNTTAX() {
		return ISDISCOUNTTAX;
	}

	public void setISDISCOUNTTAX(short iSDISCOUNTTAX) {
		ISDISCOUNTTAX = iSDISCOUNTTAX;
	}

	public short getISORDERDISCOUNTTAX() {
		return ISORDERDISCOUNTTAX;
	}

	public void setISORDERDISCOUNTTAX(short iSORDERDISCOUNTTAX) {
		ISORDERDISCOUNTTAX = iSORDERDISCOUNTTAX;
	}

	public short getISSHIPPINGTAX() {
		return ISSHIPPINGTAX;
	}

	public void setISSHIPPINGTAX(short iSSHIPPINGTAX) {
		ISSHIPPINGTAX = iSSHIPPINGTAX;
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

	public String getAPP_CUST_ORDER_STATUS() {
		return APP_CUST_ORDER_STATUS;
	}

	public void setAPP_CUST_ORDER_STATUS(String aPP_CUST_ORDER_STATUS) {
		APP_CUST_ORDER_STATUS = aPP_CUST_ORDER_STATUS;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String getUKEY() {
		return UKEY;
	}

	public void setUKEY(String uKEY) {
		UKEY = uKEY;
	}
	
	public String getREASON() {
		return REASON;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}

	@Override
	public String toString() {
		return "DoHdrApproval [PLANT=" + PLANT + ", DONO=" + DONO + ", ORDERTYPE=" + ORDERTYPE + ", DELDATE=" + DELDATE
				+ ", STATUS=" + STATUS + ", PickStaus=" + PickStaus + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT="
				+ UPAT + ", UPBY=" + UPBY + ", CustCode=" + CustCode + ", CustName=" + CustName + ", JobNum=" + JobNum
				+ ", PersonInCharge=" + PersonInCharge + ", contactNum=" + contactNum + ", Address=" + Address
				+ ", Address2=" + Address2 + ", Address3=" + Address3 + ", CollectionDate=" + CollectionDate
				+ ", CollectionTime=" + CollectionTime + ", Remark1=" + Remark1 + ", Remark2=" + Remark2
				+ ", SHIPPINGID=" + SHIPPINGID + ", SHIPPINGCUSTOMER=" + SHIPPINGCUSTOMER + ", CURRENCYID=" + CURRENCYID
				+ ", DELIVERYDATE=" + DELIVERYDATE + ", TIMESLOTS=" + TIMESLOTS + ", OUTBOUND_GST=" + OUTBOUND_GST
				+ ", STATUS_ID=" + STATUS_ID + ", EMPNO=" + EMPNO + ", ESTNO=" + ESTNO + ", Remark3=" + Remark3
				+ ", ORDERDISCOUNT=" + ORDERDISCOUNT + ", SHIPPINGCOST=" + SHIPPINGCOST + ", INCOTERMS=" + INCOTERMS
				+ ", PAYMENTTYPE=" + PAYMENTTYPE + ", DELIVERYDATEFORMAT=" + DELIVERYDATEFORMAT + ", APPROVESTATUS="
				+ APPROVESTATUS + ", SALES_LOCATION=" + SALES_LOCATION + ", TAXTREATMENT=" + TAXTREATMENT
				+ ", ORDER_STATUS=" + ORDER_STATUS + ", DISCOUNT=" + DISCOUNT + ", DISCOUNT_TYPE=" + DISCOUNT_TYPE
				+ ", ADJUSTMENT=" + ADJUSTMENT + ", ITEM_RATES=" + ITEM_RATES + ", CURRENCYUSEQT=" + CURRENCYUSEQT
				+ ", ORDERDISCOUNTTYPE=" + ORDERDISCOUNTTYPE + ", TAXID=" + TAXID + ", ISDISCOUNTTAX=" + ISDISCOUNTTAX
				+ ", ISORDERDISCOUNTTAX=" + ISORDERDISCOUNTTAX + ", ISSHIPPINGTAX=" + ISSHIPPINGTAX + ", PROJECTID="
				+ PROJECTID + ", TRANSPORTID=" + TRANSPORTID + ", PAYMENT_TERMS=" + PAYMENT_TERMS + ", SHIPCONTACTNAME="
				+ SHIPCONTACTNAME + ", SHIPDESGINATION=" + SHIPDESGINATION + ", SHIPWORKPHONE=" + SHIPWORKPHONE
				+ ", SHIPHPNO=" + SHIPHPNO + ", SHIPEMAIL=" + SHIPEMAIL + ", SHIPCOUNTRY=" + SHIPCOUNTRY
				+ ", SHIPADDR1=" + SHIPADDR1 + ", SHIPADDR2=" + SHIPADDR2 + ", SHIPADDR3=" + SHIPADDR3 + ", SHIPADDR4="
				+ SHIPADDR4 + ", SHIPSTATE=" + SHIPSTATE + ", SHIPZIP=" + SHIPZIP + ", APP_CUST_ORDER_STATUS="
				+ APP_CUST_ORDER_STATUS + ", UKEY=" + UKEY +", REASON=" + REASON + ", ID=" + ID + "]";
	}

	
	
	
}
