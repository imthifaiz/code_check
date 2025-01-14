package com.track.db.object;

public class POSBankInByPayMode {

	@DBTable(columnName ="ID")
    private int ID;
    @DBTable(columnName ="PLANT")
    private String PLANT;
    @DBTable(columnName ="HDRID")
    private int HDRID;
    @DBTable(columnName ="BANKINDATE")
    private String BANKINDATE;
    @DBTable(columnName ="PAYMENTMODE")
    private String PAYMENTMODE;
    @DBTable(columnName ="BANKINAMOUNT")
    private Double BANKINAMOUNT;
    @DBTable(columnName ="BANKINACCOUNT")
    private String BANKINACCOUNT;
    @DBTable(columnName ="CHARGESPERCENTAGE")
    private Double CHARGESPERCENTAGE;
    @DBTable(columnName ="CHARGESAMOUNT")
    private Double CHARGESAMOUNT;
    @DBTable(columnName ="CHARGESACCOUNT")
    private String CHARGESACCOUNT;
    @DBTable(columnName ="CRAT")
    private String CRAT;
    @DBTable(columnName ="CRBY")
    private String CRBY;
    @DBTable(columnName ="UPAT")
    private String UPAT;
    @DBTable(columnName ="UPBY")
    private String UPBY;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}
	public int getHDRID() {
		return HDRID;
	}
	public void setHDRID(int hDRID) {
		HDRID = hDRID;
	}
	public String getBANKINDATE() {
		return BANKINDATE;
	}
	public void setBANKINDATE(String bANKINDATE) {
		BANKINDATE = bANKINDATE;
	}
	public String getPAYMENTMODE() {
		return PAYMENTMODE;
	}
	public void setPAYMENTMODE(String pAYMENTMODE) {
		PAYMENTMODE = pAYMENTMODE;
	}
	public Double getBANKINAMOUNT() {
		return BANKINAMOUNT;
	}
	public void setBANKINAMOUNT(Double bANKINAMOUNT) {
		BANKINAMOUNT = bANKINAMOUNT;
	}
	public String getBANKINACCOUNT() {
		return BANKINACCOUNT;
	}
	public void setBANKINACCOUNT(String bANKINACCOUNT) {
		BANKINACCOUNT = bANKINACCOUNT;
	}
	public Double getCHARGESPERCENTAGE() {
		return CHARGESPERCENTAGE;
	}
	public void setCHARGESPERCENTAGE(Double cHARGESPERCENTAGE) {
		CHARGESPERCENTAGE = cHARGESPERCENTAGE;
	}
	public Double getCHARGESAMOUNT() {
		return CHARGESAMOUNT;
	}
	public void setCHARGESAMOUNT(Double cHARGESAMOUNT) {
		CHARGESAMOUNT = cHARGESAMOUNT;
	}
	public String getCHARGESACCOUNT() {
		return CHARGESACCOUNT;
	}
	public void setCHARGESACCOUNT(String cHARGESACCOUNT) {
		CHARGESACCOUNT = cHARGESACCOUNT;
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
		return "POSBankInByPayMode [ID=" + ID + ", PLANT=" + PLANT + ", HDRID=" + HDRID + ", BANKINDATE=" + BANKINDATE
				+ ", PAYMENTMODE=" + PAYMENTMODE + ", BANKINAMOUNT=" + BANKINAMOUNT + ", BANKINACCOUNT=" + BANKINACCOUNT
				+ ", CHARGESPERCENTAGE=" + CHARGESPERCENTAGE + ", CHARGESAMOUNT=" + CHARGESAMOUNT + ", CHARGESACCOUNT="
				+ CHARGESACCOUNT + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
    
}
