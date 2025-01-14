package com.track.db.object;

public class HrHolidayMst {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="HOLIDAY_DATE")
	private String HOLIDAY_DATE;
		
	@DBTable(columnName ="HOLIDAY_DESC")
	private String HOLIDAY_DESC;

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

	public String getHOLIDAY_DATE() {
		return HOLIDAY_DATE;
	}

	public void setHOLIDAY_DATE(String hOLIDAY_DATE) {
		HOLIDAY_DATE = hOLIDAY_DATE;
	}

	public String getHOLIDAY_DESC() {
		return HOLIDAY_DESC;
	}

	public void setHOLIDAY_DESC(String hOLIDAY_DESC) {
		HOLIDAY_DESC = hOLIDAY_DESC;
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
		return "HrHolidayMst [PLANT=" + PLANT + ", ID=" + ID + ", HOLIDAY_DATE=" + HOLIDAY_DATE + ", HOLIDAY_DESC="
				+ HOLIDAY_DESC + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	

}
