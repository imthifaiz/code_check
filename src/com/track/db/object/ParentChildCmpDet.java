package com.track.db.object;

public class ParentChildCmpDet {
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="PARENT_PLANT")
	private String PARENT_PLANT;
		
	@DBTable(columnName ="CHILD_PLANT")
	private String CHILD_PLANT;
	
	@DBTable(columnName ="ISCHILD_AS_PARENT")
	private short ISCHILD_AS_PARENT;
		
	@DBTable(columnName ="CRAT")
	private String CRAT;
		
	@DBTable(columnName ="CRBY")
	private String CRBY;
		
	@DBTable(columnName ="UPAT")
	private String UPAT;
		
	@DBTable(columnName ="UPBY")
	private String UPBY;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPARENT_PLANT() {
		return PARENT_PLANT;
	}

	public void setPARENT_PLANT(String pARENT_PLANT) {
		PARENT_PLANT = pARENT_PLANT;
	}

	public String getCHILD_PLANT() {
		return CHILD_PLANT;
	}

	public void setCHILD_PLANT(String cHILD_PLANT) {
		CHILD_PLANT = cHILD_PLANT;
	}
	
	public short getISCHILD_AS_PARENT() {
		return ISCHILD_AS_PARENT;
	}
	
	public void setISCHILD_AS_PARENT(short iSCHILD_AS_PARENT) {
		ISCHILD_AS_PARENT = iSCHILD_AS_PARENT;
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
		return "ParentChildCmpDet [ID=" + ID + ", PARENT_PLANT=" + PARENT_PLANT + ", CHILD_PLANT=" + CHILD_PLANT
				+ ", ISCHILD_AS_PARENT=" + ISCHILD_AS_PARENT + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	

}
