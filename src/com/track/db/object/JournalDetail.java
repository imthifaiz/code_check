package com.track.db.object;

public class JournalDetail {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="LNNO")
	private int LNNO;
	
	@DBTable(columnName ="JOURNALHDRID")
	private int JOURNALHDRID;
	
	@DBTable(columnName ="ACCOUNT_ID")
	private int ACCOUNT_ID=0;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="DESCRIPTION")
	private String DESCRIPTION="";
	
	@DBTable(columnName ="DEBITS")
	private Double DEBITS=0.00;
	
	@DBTable(columnName ="CREDITS")
	private Double CREDITS=0.00;
	
	@DBTable(columnName ="OPENINGBALANCEDEBITS")
	private Double OPENINGBALANCEDEBITS;
	
	@DBTable(columnName ="OPENINGBALANCECREDITS")
	private Double OPENINGBALANCECREDITS;
	
	@DBTable(columnName ="CLOSINGBALANCEDEBITS")
	private Double CLOSINGBALANCEDEBITS;
	
	@DBTable(columnName ="CLOSINGBALANCECREDITS")
	private Double CLOSINGBALANCECREDITS;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="RECONCILIATION")
	private Short RECONCILIATION;
	
	@DBTable(columnName ="BANKDATE")
	private String BANKDATE;
	
	@DBTable(columnName ="RID")
	private Integer RID;

	


	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}

	public int getJOURNALHDRID() {
		return JOURNALHDRID;
	}

	public void setJOURNALHDRID(int jOURNALHDRID) {
		JOURNALHDRID = jOURNALHDRID;
	}

	public int getACCOUNT_ID() {
		return ACCOUNT_ID;
	}

	public void setACCOUNT_ID(int aCCOUNT_ID) {
		ACCOUNT_ID = aCCOUNT_ID;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public Double getDEBITS() {
		return DEBITS;
	}

	public void setDEBITS(Double dEBITS) {
		DEBITS = dEBITS;
	}

	public Double getCREDITS() {
		return CREDITS;
	}

	public void setCREDITS(Double cREDITS) {
		CREDITS = cREDITS;
	}

	public Double getOPENINGBALANCEDEBITS() {
		return OPENINGBALANCEDEBITS;
	}

	public void setOPENINGBALANCEDEBITS(Double oPENINGBALANCEDEBITS) {
		OPENINGBALANCEDEBITS = oPENINGBALANCEDEBITS;
	}

	public Double getOPENINGBALANCECREDITS() {
		return OPENINGBALANCECREDITS;
	}

	public void setOPENINGBALANCECREDITS(Double oPENINGBALANCECREDITS) {
		OPENINGBALANCECREDITS = oPENINGBALANCECREDITS;
	}

	public Double getCLOSINGBALANCEDEBITS() {
		return CLOSINGBALANCEDEBITS;
	}

	public void setCLOSINGBALANCEDEBITS(Double cLOSINGBALANCEDEBITS) {
		CLOSINGBALANCEDEBITS = cLOSINGBALANCEDEBITS;
	}

	public Double getCLOSINGBALANCECREDITS() {
		return CLOSINGBALANCECREDITS;
	}

	public void setCLOSINGBALANCECREDITS(Double cLOSINGBALANCECREDITS) {
		CLOSINGBALANCECREDITS = cLOSINGBALANCECREDITS;
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

	public Short getRECONCILIATION() {
		return RECONCILIATION;
	}

	public void setRECONCILIATION(Short rECONCILIATION) {
		RECONCILIATION = rECONCILIATION;
	}

	public String getBANKDATE() {
		return BANKDATE;
	}

	public void setBANKDATE(String bANKDATE) {
		BANKDATE = bANKDATE;
	}

	public Integer getRID() {
		return RID;
	}

	public void setRID(Integer rID) {
		RID = rID;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) return false;
	    if (!(obj instanceof JournalDetail))
	        return false;
	    if (obj == this)
	        return true;
	    return this.getACCOUNT_ID() == ((JournalDetail) obj).getACCOUNT_ID();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.ACCOUNT_ID;
	}

	@Override
	public String toString() {
		return "JournalDetail [PLANT=" + PLANT + ", ID=" + ID + ", LNNO=" + LNNO + ", JOURNALHDRID=" + JOURNALHDRID
				+ ", ACCOUNT_ID=" + ACCOUNT_ID + ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", DESCRIPTION=" + DESCRIPTION
				+ ", DEBITS=" + DEBITS + ", CREDITS=" + CREDITS + ", OPENINGBALANCEDEBITS=" + OPENINGBALANCEDEBITS
				+ ", OPENINGBALANCECREDITS=" + OPENINGBALANCECREDITS + ", CLOSINGBALANCEDEBITS=" + CLOSINGBALANCEDEBITS
				+ ", CLOSINGBALANCECREDITS=" + CLOSINGBALANCECREDITS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT="
				+ UPAT + ", UPBY=" + UPBY + ", RECONCILIATION=" + RECONCILIATION + ", BANKDATE=" + BANKDATE + ", RID="
				+ RID + "]";
	}



	
	
	
	

}
