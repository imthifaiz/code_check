package com.track.db.object;

public class HrpayrollDeductionPojo {
	
	private int ID;
	
	private int HDRID;
	
	private String DEDUCTION_NAME;
	
	private Double DUE_AMOUNT;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getHDRID() {
		return HDRID;
	}

	public void setHDRID(int hDRID) {
		HDRID = hDRID;
	}

	public String getDEDUCTION_NAME() {
		return DEDUCTION_NAME;
	}

	public void setDEDUCTION_NAME(String dEDUCTION_NAME) {
		DEDUCTION_NAME = dEDUCTION_NAME;
	}

	public Double getDUE_AMOUNT() {
		return DUE_AMOUNT;
	}

	public void setDUE_AMOUNT(Double dUE_AMOUNT) {
		DUE_AMOUNT = dUE_AMOUNT;
	}

	@Override
	public String toString() {
		return "HrpayrollDeductionPojo [ID=" + ID + ", HDRID=" + HDRID + ", DEDUCTION_NAME=" + DEDUCTION_NAME
				+ ", DUE_AMOUNT=" + DUE_AMOUNT + "]";
	}
	
	

}
