package com.track.db.object;

public class StoreDet {
	
	    private int ID;

	    private String PLANT;

	    private int HDRID;

	    private String DONO;

	    private Short IS_CHILD_ITEM;
	   
	    private String PITEM;
	    
	    private String PITEM_DESC;
	   
	    private String ITEM;
	    
	    private String ITEM_DESC;
	    
	    private String UOM;
	    
	    private Double ORDER_QTY;
	    
	    private String DEPARTMENT;
	    
	    private String STATUS;
	    
	    private String CRAT;
	    
	    private String CRBY;
	    
	    private String UPAT;

	    private String UPBY;

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}

		public String getPLANT() {
			return PLANT;
		}

		public void setPLANT(String pLANT) {
			PLANT = pLANT;
		}

		public int getHDRID() {
			return HDRID;
		}

		public void setHDRID(int hDRID) {
			HDRID = hDRID;
		}

		public String getDONO() {
			return DONO;
		}

		public void setDONO(String dONO) {
			DONO = dONO;
		}

		public Short getIS_CHILD_ITEM() {
			return IS_CHILD_ITEM;
		}

		public void setIS_CHILD_ITEM(Short iS_CHILD_ITEM) {
			IS_CHILD_ITEM = iS_CHILD_ITEM;
		}

		public String getPITEM() {
			return PITEM;
		}

		public void setPITEM(String pITEM) {
			PITEM = pITEM;
		}

		public String getPITEM_DESC() {
			return PITEM_DESC;
		}

		public void setPITEM_DESC(String pITEM_DESC) {
			PITEM_DESC = pITEM_DESC;
		}

		public String getITEM() {
			return ITEM;
		}

		public void setITEM(String iTEM) {
			ITEM = iTEM;
		}

		public String getITEM_DESC() {
			return ITEM_DESC;
		}

		public void setITEM_DESC(String iTEM_DESC) {
			ITEM_DESC = iTEM_DESC;
		}

		public String getUOM() {
			return UOM;
		}

		public void setUOM(String uOM) {
			UOM = uOM;
		}

		public Double getORDER_QTY() {
			return ORDER_QTY;
		}

		public void setORDER_QTY(Double oRDER_QTY) {
			ORDER_QTY = oRDER_QTY;
		}

		public String getDEPARTMENT() {
			return DEPARTMENT;
		}

		public void setDEPARTMENT(String dEPARTMENT) {
			DEPARTMENT = dEPARTMENT;
		}

		public String getSTATUS() {
			return STATUS;
		}

		public void setSTATUS(String sTATUS) {
			STATUS = sTATUS;
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
			return "StoreDet [ID=" + ID + ", PLANT=" + PLANT + ", HDRID=" + HDRID + ", DONO=" + DONO
					+ ", IS_CHILD_ITEM=" + IS_CHILD_ITEM + ", PITEM=" + PITEM + ", PITEM_DESC=" + PITEM_DESC + ", ITEM="
					+ ITEM + ", ITEM_DESC=" + ITEM_DESC + ", UOM=" + UOM + ", ORDER_QTY=" + ORDER_QTY + ", DEPARTMENT="
					+ DEPARTMENT + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
					+ ", UPBY=" + UPBY + "]";
		}
	    
	    

}
