package com.track.db.object;

public class HrLeaveApplyAttachment {

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="LEAVEAPPLYHDRID")
	private int LEAVEAPPLYHDRID;
	
	@DBTable(columnName ="FileType")
	private String FileType;
	
	@DBTable(columnName ="FileName")
	private String FileName;
	
	@DBTable(columnName ="FileSize")
	private int FileSize;
	
	@DBTable(columnName ="FilePath")
	private String FilePath;
	
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

	public int getLEAVEAPPLYHDRID() {
		return LEAVEAPPLYHDRID;
	}

	public void setLEAVEAPPLYHDRID(int lEAVEAPPLYHDRID) {
		LEAVEAPPLYHDRID = lEAVEAPPLYHDRID;
	}

	public String getFileType() {
		return FileType;
	}

	public void setFileType(String fileType) {
		FileType = fileType;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public int getFileSize() {
		return FileSize;
	}

	public void setFileSize(int fileSize) {
		FileSize = fileSize;
	}

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String filePath) {
		FilePath = filePath;
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
		return "HrLeaveApplyAttachment [PLANT=" + PLANT + ", ID=" + ID + ", LEAVEAPPLYHDRID=" + LEAVEAPPLYHDRID
				+ ", FileType=" + FileType + ", FileName=" + FileName + ", FileSize=" + FileSize + ", FilePath="
				+ FilePath + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
}
