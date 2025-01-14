package com.track.db.object;

import java.math.BigDecimal;
import java.util.List;

import com.track.service.LoctaionTypeTwoService;

public class LOC_TYPE_MST2 {

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="LOC_TYPE_ID2")
	private String LOC_TYPE_ID2;
	
	@DBTable(columnName ="LOC_TYPE_DESC2")
	private String LOC_TYPE_DESC2;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="IsActive")
	private String IsActive;
	

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getLOC_TYPE_ID2() {
		return LOC_TYPE_ID2;
	}

	public void setLOC_TYPE_ID2(String lOC_TYPE_ID2) {
		LOC_TYPE_ID2 = lOC_TYPE_ID2;
	}
	
	public String getLOC_TYPE_DESC2() {
		return LOC_TYPE_DESC2;
	}

	public void setLOC_TYPE_DESC2(String lOC_TYPE_DESC2) {
		LOC_TYPE_DESC2 = lOC_TYPE_DESC2;
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
	
	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}



	public String toString() {
		return "LOC_TYPE_MST2 [PLANT=" + PLANT + ",LOC_TYPE_ID2=" + LOC_TYPE_ID2 + ",LOC_TYPE_DESC2=" + LOC_TYPE_DESC2 + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", IsActive=" + IsActive + "]";
	}
}
