package com.track.db.object;

public class PayrollDeductionPojo {

	private int ID;
	
	private String EMPCODE;
	
	private String EMPNAME;
	
	private String MONTH;
	
	private String DNAME;
	
	private String DAMOUNT;
	
	private String DUEAMOUNT;
	
	private short ISGRATUITY;
	
	private short ISPROCESSED;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getEMPCODE() {
		return EMPCODE;
	}

	public void setEMPCODE(String eMPCODE) {
		EMPCODE = eMPCODE;
	}

	public String getEMPNAME() {
		return EMPNAME;
	}

	public void setEMPNAME(String eMPNAME) {
		EMPNAME = eMPNAME;
	}

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	public String getDNAME() {
		return DNAME;
	}

	public void setDNAME(String dNAME) {
		DNAME = dNAME;
	}

	public String getDAMOUNT() {
		return DAMOUNT;
	}

	public void setDAMOUNT(String dAMOUNT) {
		DAMOUNT = dAMOUNT;
	}

	public String getDUEAMOUNT() {
		return DUEAMOUNT;
	}

	public void setDUEAMOUNT(String dUEAMOUNT) {
		DUEAMOUNT = dUEAMOUNT;
	}

	public short getISGRATUITY() {
		return ISGRATUITY;
	}

	public void setISGRATUITY(short iSGRATUITY) {
		ISGRATUITY = iSGRATUITY;
	}

	public short getISPROCESSED() {
		return ISPROCESSED;
	}

	public void setISPROCESSED(short iSPROCESSED) {
		ISPROCESSED = iSPROCESSED;
	}

	@Override
	public String toString() {
		return "PayrollDeductionPojo [ID=" + ID + ", EMPCODE=" + EMPCODE + ", EMPNAME=" + EMPNAME + ", MONTH=" + MONTH
				+ ", DNAME=" + DNAME + ", DAMOUNT=" + DAMOUNT + ", DUEAMOUNT=" + DUEAMOUNT + ", ISGRATUITY="
				+ ISGRATUITY + ", ISPROCESSED=" + ISPROCESSED + "]";
	}
	
	



	
	
	
}
