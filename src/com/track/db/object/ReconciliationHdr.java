package com.track.db.object;

public class ReconciliationHdr {
	@DBTable(columnName ="PLANT")
	private String PLANT;
	@DBTable(columnName ="ID")
	private int ID;
	@DBTable(columnName ="RDATE")
	private String RDATE;
	@DBTable(columnName ="CURRENCYID")
	private String CURRENCYID;
	@DBTable(columnName ="RSTATUS")
	private int RSTATUS;
	@DBTable(columnName ="RMONTH")
	private int RMONTH;
	@DBTable(columnName ="RYEAR")
	private int RYEAR;
	@DBTable(columnName ="RACCOUNT")
	private String RACCOUNT;
	@DBTable(columnName ="BANKOPENBALANCE")
	private Double BANKOPENBALANCE;
	@DBTable(columnName ="BANKCLOSEBALANCE")
	private Double BANKCLOSEBALANCE;
	@DBTable(columnName ="OPENBALANCE")
	private Double OPENBALANCE;
	@DBTable(columnName ="CLOSEBALANCE")
	private Double CLOSEBALANCE;
	@DBTable(columnName ="DEPOSITS")
	private Double DEPOSITS;
	@DBTable(columnName ="WITHDRAWL")
	private Double WITHDRAWL;
	@DBTable(columnName ="CRAT")
	private String CRAT;
	@DBTable(columnName ="CRBY")
	private String CRBY;	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
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
	public String getRDATE() {
		return RDATE;
	}
	public void setRDATE(String rDATE) {
		RDATE = rDATE;
	}
	public String getCURRENCYID() {
		return CURRENCYID;
	}
	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
	}
	public int getRSTATUS() {
		return RSTATUS;
	}
	public void setRSTATUS(int rSTATUS) {
		RSTATUS = rSTATUS;
	}
	public int getRMONTH() {
		return RMONTH;
	}
	public void setRMONTH(int rMONTH) {
		RMONTH = rMONTH;
	}
	public int getRYEAR() {
		return RYEAR;
	}
	public void setRYEAR(int rYEAR) {
		RYEAR = rYEAR;
	}
	public String getRACCOUNT() {
		return RACCOUNT;
	}
	public void setRACCOUNT(String rACCOUNT) {
		RACCOUNT = rACCOUNT;
	}
	public Double getBANKOPENBALANCE() {
		return BANKOPENBALANCE;
	}
	public void setBANKOPENBALANCE(Double bANKOPENBALANCE) {
		BANKOPENBALANCE = bANKOPENBALANCE;
	}
	public Double getBANKCLOSEBALANCE() {
		return BANKCLOSEBALANCE;
	}
	public void setBANKCLOSEBALANCE(Double bANKCLOSEBALANCE) {
		BANKCLOSEBALANCE = bANKCLOSEBALANCE;
	}
	public Double getOPENBALANCE() {
		return OPENBALANCE;
	}
	public void setOPENBALANCE(Double oPENBALANCE) {
		OPENBALANCE = oPENBALANCE;
	}
	public Double getCLOSEBALANCE() {
		return CLOSEBALANCE;
	}
	public void setCLOSEBALANCE(Double cLOSEBALANCE) {
		CLOSEBALANCE = cLOSEBALANCE;
	}
	public Double getDEPOSITS() {
		return DEPOSITS;
	}
	public void setDEPOSITS(Double dEPOSITS) {
		DEPOSITS = dEPOSITS;
	}
	public Double getWITHDRAWL() {
		return WITHDRAWL;
	}
	public void setWITHDRAWL(Double wITHDRAWL) {
		WITHDRAWL = wITHDRAWL;
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
	
	@Override
	public String toString() {
		return "ReconciliationHdr [PLANT=" + PLANT + ", ID=" + ID + ", RDATE=" + RDATE + ", CURRENCYID=" + CURRENCYID
				+ ", RSTATUS=" + RSTATUS + ", RMONTH=" + RMONTH + ", RYEAR=" + RYEAR + ", RACCOUNT=" + RACCOUNT
				+ ", BANKOPENBALANCE=" + BANKOPENBALANCE + ", BANKCLOSEBALANCE=" + BANKCLOSEBALANCE + ", OPENBALANCE="
				+ OPENBALANCE + ", CLOSEBALANCE=" + CLOSEBALANCE + ", DEPOSITS=" + DEPOSITS + ", WITHDRAWL=" + WITHDRAWL
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
	

}
