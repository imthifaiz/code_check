package com.track.db.object;

public class FinProject {

	@DBTable(columnName ="PLANT")
	private String PLANT;

	@DBTable(columnName ="ID")
	private int ID;

	@DBTable(columnName ="CUSTNO")
	private String CUSTNO;

	@DBTable(columnName ="PROJECT")
	private String PROJECT;

	@DBTable(columnName ="PROJECT_NAME")
	private String PROJECT_NAME;
	
	@DBTable(columnName ="MANDAY_HOUR")
	private Double MANDAY_HOUR;
	
	@DBTable(columnName ="ISMANDAY_HOUR")
	private Short ISMANDAY_HOUR;

	@DBTable(columnName ="PROJECT_DATE")
	private String PROJECT_DATE;
	
	@DBTable(columnName ="EXPIRY_DATE")
	private String EXPIRY_DATE;

	@DBTable(columnName ="PROJECT_STATUS")
	private String PROJECT_STATUS;

	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;

	@DBTable(columnName ="ESTIMATE_COST")
	private String ESTIMATE_COST;

	@DBTable(columnName ="ESTIMATE_TIME")
	private String ESTIMATE_TIME;

	@DBTable(columnName ="BILLING_OPTION")
	private String BILLING_OPTION;

	@DBTable(columnName ="ISPERHOURWAGES")
	private Short ISPERHOURWAGES;

	@DBTable(columnName ="PERHOURWAGESCOST")
	private Double PERHOURWAGESCOST;

	@DBTable(columnName ="NOTE")
	private String NOTE;

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

	public String getCUSTNO() {
		return CUSTNO;
	}

	public void setCUSTNO(String cUSTNO) {
		CUSTNO = cUSTNO;
	}

	public String getPROJECT() {
		return PROJECT;
	}

	public void setPROJECT(String pROJECT) {
		PROJECT = pROJECT;
	}

	public String getPROJECT_NAME() {
		return PROJECT_NAME;
	}

	public void setPROJECT_NAME(String pROJECT_NAME) {
		PROJECT_NAME = pROJECT_NAME;
	}
	
	public Double getMANDAY_HOUR() {
		return MANDAY_HOUR;
	}

	public void setMANDAY_HOUR(Double mANDAY_HOUR) {
		MANDAY_HOUR = mANDAY_HOUR;
	}
	
	public Short getISMANDAY_HOUR() {
		return ISMANDAY_HOUR;
	}
	
	public void setISMANDAY_HOUR(Short iSMANDAY_HOUR) {
		ISMANDAY_HOUR = iSMANDAY_HOUR;
	}

	public String getPROJECT_DATE() {
		return PROJECT_DATE;
	}

	public void setPROJECT_DATE(String pROJECT_DATE) {
		PROJECT_DATE = pROJECT_DATE;
	}

	public String getEXPIRY_DATE() {
		return EXPIRY_DATE;
	}

	public void setEXPIRY_DATE(String eXPIRY_DATE) {
		EXPIRY_DATE = eXPIRY_DATE;
	}

	public String getPROJECT_STATUS() {
		return PROJECT_STATUS;
	}

	public void setPROJECT_STATUS(String pROJECT_STATUS) {
		PROJECT_STATUS = pROJECT_STATUS;
	}

	public String getREFERENCE() {
		return REFERENCE;
	}

	public void setREFERENCE(String rEFERENCE) {
		REFERENCE = rEFERENCE;
	}

	public String getESTIMATE_COST() {
		return ESTIMATE_COST;
	}

	public void setESTIMATE_COST(String eSTIMATE_COST) {
		ESTIMATE_COST = eSTIMATE_COST;
	}

	public String getESTIMATE_TIME() {
		return ESTIMATE_TIME;
	}

	public void setESTIMATE_TIME(String eSTIMATE_TIME) {
		ESTIMATE_TIME = eSTIMATE_TIME;
	}

	public String getBILLING_OPTION() {
		return BILLING_OPTION;
	}

	public void setBILLING_OPTION(String bILLING_OPTION) {
		BILLING_OPTION = bILLING_OPTION;
	}

	public Short getISPERHOURWAGES() {
		return ISPERHOURWAGES;
	}

	public void setISPERHOURWAGES(Short iSPERHOURWAGES) {
		ISPERHOURWAGES = iSPERHOURWAGES;
	}

	public Double getPERHOURWAGESCOST() {
		return PERHOURWAGESCOST;
	}

	public void setPERHOURWAGESCOST(Double pERHOURWAGESCOST) {
		PERHOURWAGESCOST = pERHOURWAGESCOST;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
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
		return "FinProject [PLANT=" + PLANT + ", ID=" + ID + ", CUSTNO=" + CUSTNO + ", PROJECT=" + PROJECT
				+ ", PROJECT_NAME=" + PROJECT_NAME + ", PROJECT_DATE=" + PROJECT_DATE + ", EXPIRY_DATE=" + EXPIRY_DATE
				+ ", PROJECT_STATUS=" + PROJECT_STATUS + ", MANDAY_HOUR=" + MANDAY_HOUR + ", ISMANDAY_HOUR=" + ISMANDAY_HOUR + ", REFERENCE=" + REFERENCE + ", ESTIMATE_COST=" + ESTIMATE_COST
				+ ", ESTIMATE_TIME=" + ESTIMATE_TIME + ", BILLING_OPTION=" + BILLING_OPTION + ", ISPERHOURWAGES="
				+ ISPERHOURWAGES + ", PERHOURWAGESCOST=" + PERHOURWAGESCOST + ", NOTE=" + NOTE + ", CRAT=" + CRAT
				+ ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	
}
