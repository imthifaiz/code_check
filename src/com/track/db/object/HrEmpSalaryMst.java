package com.track.db.object;

public class HrEmpSalaryMst {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="SALARYTYPE")
	private String SALARYTYPE;
	
	@DBTable(columnName ="IsActive")
	private String IsActive;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
		
	@DBTable(columnName ="CRBY")
	private String CRBY;
		
	@DBTable(columnName ="UPAT")
	private String UPAT;
		
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="ISPAYROLL_BY_BASIC_SALARY")
	private Short ISPAYROLL_BY_BASIC_SALARY;
	
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

	public String getSALARYTYPE() {
		return SALARYTYPE;
	}

	public void setSALARYTYPE(String eSALARYTYPE) {
		SALARYTYPE = eSALARYTYPE;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	
	public Short getISPAYROLL_BY_BASIC_SALARY() {
		return ISPAYROLL_BY_BASIC_SALARY;
	}

	public void setISPAYROLL_BY_BASIC_SALARY(Short iSPAYROLL_BY_BASIC_SALARY) {
		ISPAYROLL_BY_BASIC_SALARY = iSPAYROLL_BY_BASIC_SALARY;
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
		return "HrEmpSalaryMst [PLANT=" + PLANT + ", ID=" + ID + ", SALARYTYPE=" + SALARYTYPE + ", IsActive="
				+ IsActive + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + ", ISPAYROLL_BY_BASIC_SALARY=" + ISPAYROLL_BY_BASIC_SALARY + "]";
	}


}
