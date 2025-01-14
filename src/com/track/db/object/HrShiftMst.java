package com.track.db.object;

public class HrShiftMst {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="SHIFTNAME")
	private String SHIFTNAME;
	
	@DBTable(columnName ="ISHOURBASED")
	private Short ISHOURBASED;
	
	@DBTable(columnName ="ISTIMEBASED")
	private Short ISTIMEBASED;
	
	@DBTable(columnName ="ALLOCATEHOUR")
	private String ALLOCATEHOUR;
	
	@DBTable(columnName ="INTIME")
	private String INTIME;
	
	@DBTable(columnName ="OUTTIME")
	private String OUTTIME;
	
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

public String getSHIFTNAME() {
	return SHIFTNAME;
}

public void setSHIFTNAME(String sHIFTNAME) {
	SHIFTNAME = sHIFTNAME;
}

public Short getISHOURBASED() {
	return ISHOURBASED;
}

public void setISHOURBASED(Short iSHOURBASED) {
	ISHOURBASED = iSHOURBASED;
}

public Short getISTIMEBASED() {
	return ISTIMEBASED;
}

public void setISTIMEBASED(Short iSTIMEBASED) {
	ISTIMEBASED = iSTIMEBASED;
}

public String getALLOCATEHOUR() {
	return ALLOCATEHOUR;
}

public void setALLOCATEHOUR(String aLLOCATEHOUR) {
	ALLOCATEHOUR = aLLOCATEHOUR;
}

public String getINTIME() {
	return INTIME;
}

public void setINTIME(String iNTIME) {
	INTIME = iNTIME;
}

public String getOUTTIME() {
	return OUTTIME;
}

public void setOUTTIME(String oUTTIME) {
	OUTTIME = oUTTIME;
}

public String getIsActive() {
	return IsActive;
}

public void setIsActive(String isActive) {
	IsActive = isActive;
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
	return "HrShiftMst [PLANT=" + PLANT + ", ID=" + ID + ", SHIFTNAME=" + SHIFTNAME + ", ISHOURBASED="
			+ ISHOURBASED + ", ISTIMEBASED=" + ISTIMEBASED + ", ALLOCATEHOUR=" + ALLOCATEHOUR
			+ ", INTIME=" + INTIME + ", OUTTIME=" + OUTTIME + ",IsActive=" + IsActive + ", CRAT=" + CRAT + ", CRBY=" + CRBY
			+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
}

}
