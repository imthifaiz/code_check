package com.track.db.object;

public class PayrollPojo {
	
	private int ID;
	
	private String PAYROLL;
	
	private String EMPCODE;
	
	private String EMPNAME;
	
	private String MONTH;
	
	private String ATDAYS;
	
	private String SALARY;
	
	private String STATUS;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPAYROLL() {
		return PAYROLL;
	}

	public void setPAYROLL(String pAYROLL) {
		PAYROLL = pAYROLL;
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

	public String getATDAYS() {
		return ATDAYS;
	}

	public void setATDAYS(String aTDAYS) {
		ATDAYS = aTDAYS;
	}

	public String getSALARY() {
		return SALARY;
	}

	public void setSALARY(String sALARY) {
		SALARY = sALARY;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	@Override
	public String toString() {
		return "PayrollPojo [ID=" + ID + ", PAYROLL=" + PAYROLL + ", EMPCODE=" + EMPCODE + ", EMPNAME=" + EMPNAME
				+ ", MONTH=" + MONTH + ", ATDAYS=" + ATDAYS + ", SALARY=" + SALARY + ", STATUS=" + STATUS + "]";
	}

}
