package com.track.db.object;


public class StoreHdr {

	    private int ID;

	    private String PLANT;

	    private String ITEM;

	    private String ITEM_DESC;

	    private String UOM;

	    private Double ORDER_QTY;

	    private Double PROCESSED_QTY;

	    private Double BALANCE_QTY;

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

		public Double getPROCESSED_QTY() {
			return PROCESSED_QTY;
		}

		public void setPROCESSED_QTY(Double pROCESSED_QTY) {
			PROCESSED_QTY = pROCESSED_QTY;
		}

		public Double getBALANCE_QTY() {
			return BALANCE_QTY;
		}

		public void setBALANCE_QTY(Double bALANCE_QTY) {
			BALANCE_QTY = bALANCE_QTY;
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
			return "StoreHdr [ID=" + ID + ", PLANT=" + PLANT + ", ITEM=" + ITEM + ", ITEM_DESC=" + ITEM_DESC + ", UOM="
					+ UOM + ", ORDER_QTY=" + ORDER_QTY + ", PROCESSED_QTY=" + PROCESSED_QTY + ", BALANCE_QTY="
					+ BALANCE_QTY + ", DEPARTMENT=" + DEPARTMENT + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY="
					+ CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
		}
	    
	    
		
	    
}
