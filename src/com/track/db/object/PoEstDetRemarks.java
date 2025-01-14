package com.track.db.object;


public class PoEstDetRemarks {
	
	@DBTable(columnName ="PLANT") 
	private String PLANT;

	@DBTable(columnName ="ID_REMARKS") 
	private int ID_REMARKS;

	@DBTable(columnName ="POESTNO") 
	private String POESTNO;

	@DBTable(columnName ="POESTLNNO") 
	private int POESTLNNO;

	@DBTable(columnName ="ITEM") 
	private String ITEM;

	@DBTable(columnName ="REMARKS") 
	private String REMARKS;

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

	public int getID_REMARKS() {
		return ID_REMARKS;
	}

	public void setID_REMARKS(int iD_REMARKS) {
		ID_REMARKS = iD_REMARKS;
	}

	public String getPOESTNO() {
		return POESTNO;
	}

	public void setPOESTNO(String pOESTNO) {
		POESTNO = pOESTNO;
	}

	public int getPOESTLNNO() {
		return POESTLNNO;
	}

	public void setPOESTLNNO(int pOESTLNNO) {
		POESTLNNO = pOESTLNNO;
	}

	public String getITEM() {
		return ITEM;
	}

	public void setITEM(String iTEM) {
		ITEM = iTEM;
	}

	public String getREMARKS() {
		return REMARKS;
	}

	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
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
		return "PoEstDetRemarks [PLANT=" + PLANT + ", ID_REMARKS=" + ID_REMARKS + ", POESTNO=" + POESTNO + ", POESTLNNO=" + POESTLNNO
				+ ", ITEM=" + ITEM + ", REMARKS=" + REMARKS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + "]";
	}
	
	
}