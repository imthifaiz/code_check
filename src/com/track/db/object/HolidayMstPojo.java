package com.track.db.object;

public class HolidayMstPojo {
	
	

	private String HOLIDAY_DATE;
	
	private int HDATE;

	private String HOLIDAY_DESC;

	public String getHOLIDAY_DATE() {
		return HOLIDAY_DATE;
	}

	public void setHOLIDAY_DATE(String hOLIDAY_DATE) {
		HOLIDAY_DATE = hOLIDAY_DATE;
	}

	public int getHDATE() {
		return HDATE;
	}

	public void setHDATE(int hDATE) {
		HDATE = hDATE;
	}

	public String getHOLIDAY_DESC() {
		return HOLIDAY_DESC;
	}

	public void setHOLIDAY_DESC(String hOLIDAY_DESC) {
		HOLIDAY_DESC = hOLIDAY_DESC;
	}

	@Override
	public String toString() {
		return "HolidayMstPojo [HOLIDAY_DATE=" + HOLIDAY_DATE + ", HDATE=" + HDATE + ", HOLIDAY_DESC=" + HOLIDAY_DESC
				+ "]";
	}
	
	

}
