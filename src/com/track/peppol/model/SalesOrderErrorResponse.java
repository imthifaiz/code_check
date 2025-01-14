package com.track.peppol.model;

public class SalesOrderErrorResponse {
	public String title;
    public String detail;
    public String code;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "SalesOrderErrorResponse [title=" + title + ", detail=" + detail + ", code=" + code + "]";
	}
}
