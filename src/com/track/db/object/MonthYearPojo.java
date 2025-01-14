package com.track.db.object;

public class MonthYearPojo {

	private String MONTH;
		
	private String YEAR;

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	@Override
	public String toString() {
		return "MonthYearPojo [MONTH=" + MONTH + ", YEAR=" + YEAR + "]";
	}
	
	
}
