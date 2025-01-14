package com.track.db.object;

public class HrPayrollPaymentDet {

	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="LNNO")
	private int LNNO;
	
	@DBTable(columnName ="PAYHDRID")
	private int PAYHDRID;
	
	@DBTable(columnName ="PAYID")
	private int PAYID;
	
	@DBTable(columnName ="AMOUNT")
	private double AMOUNT;
	
	@DBTable(columnName ="TYPE")
	private String TYPE;
	
	@DBTable(columnName ="CURRENCYTOBASE")
	private double CURRENCYTOBASE;
	
	@DBTable(columnName ="BASETOORDERCURRENCY")
	private double BASETOORDERCURRENCY;
	
	@DBTable(columnName ="CURRENCYUSEQT")
	private double CURRENCYUSEQT;
	
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

	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}

	public int getPAYHDRID() {
		return PAYHDRID;
	}

	public void setPAYHDRID(int pAYHDRID) {
		PAYHDRID = pAYHDRID;
	}

	public int getPAYID() {
		return PAYID;
	}

	public void setPAYID(int pAYID) {
		PAYID = pAYID;
	}

	public double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public double getCURRENCYTOBASE() {
		return CURRENCYTOBASE;
	}

	public void setCURRENCYTOBASE(double cURRENCYTOBASE) {
		CURRENCYTOBASE = cURRENCYTOBASE;
	}

	public double getBASETOORDERCURRENCY() {
		return BASETOORDERCURRENCY;
	}

	public void setBASETOORDERCURRENCY(double bASETOORDERCURRENCY) {
		BASETOORDERCURRENCY = bASETOORDERCURRENCY;
	}

	public double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
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
		return "HrPayrollPaymentDet [ID=" + ID + ", PLANT=" + PLANT + ", LNNO=" + LNNO + ", PAYHDRID=" + PAYHDRID
				+ ", PAYID=" + PAYID + ", AMOUNT=" + AMOUNT + ", TYPE=" + TYPE + ", CURRENCYTOBASE=" + CURRENCYTOBASE
				+ ", BASETOORDERCURRENCY=" + BASETOORDERCURRENCY + ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", CRAT=" + CRAT
				+ ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
}
