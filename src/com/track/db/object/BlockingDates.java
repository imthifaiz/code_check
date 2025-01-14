package com.track.db.object;

public class BlockingDates {
	
	@DBTable(columnName ="BLOCKING_DATE")
	private String BLOCKING_DATE;

	public String getBLOCKING_DATE() {
		return BLOCKING_DATE;
	}

	public void setBLOCKING_DATE(String bLOCKING_DATE) {
		BLOCKING_DATE = bLOCKING_DATE;
	}

	@Override
	public String toString() {
		return "BlockingDates [BLOCKING_DATE=" + BLOCKING_DATE + "]";
	}
	

}
