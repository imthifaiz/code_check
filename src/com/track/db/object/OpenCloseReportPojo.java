package com.track.db.object;

public class OpenCloseReportPojo {
	
	private String PLANT;
	private String ITEM;
	private String ITEMDESC;
	private String PRDCLSID;
	private String ITEMTYPE;
	private String PRD_BRAND_ID;
	private String PRD_DEPT_ID;
	private double TOTALRECEIVEDQTY;
	private double TOTALISSUEDQTY;
	private double OPENINGSTOCKQTY;
	private double CLOSINGSTOCKQTY;
	private double AVERAGECOST;
	private double TOTALCOST;
	private double PRICE;
	private double TOTALPRICE;
	private double LASTRECEIVEDQTY;
	private double LASTISSUEDQTY;
	private double STOCKONHAND;
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
	public String getITEMDESC() {
		return ITEMDESC;
	}
	public void setITEMDESC(String iTEMDESC) {
		ITEMDESC = iTEMDESC;
	}
	public String getPRDCLSID() {
		return PRDCLSID;
	}
	public void setPRDCLSID(String pRDCLSID) {
		PRDCLSID = pRDCLSID;
	}
	public String getITEMTYPE() {
		return ITEMTYPE;
	}
	public void setITEMTYPE(String iTEMTYPE) {
		ITEMTYPE = iTEMTYPE;
	}
	public String getPRD_BRAND_ID() {
		return PRD_BRAND_ID;
	}
	public void setPRD_BRAND_ID(String pRD_BRAND_ID) {
		PRD_BRAND_ID = pRD_BRAND_ID;
	}
	public String getPRD_DEPT_ID() {
		return PRD_DEPT_ID;
	}
	public void setPRD_DEPT_ID(String pRD_DEPT_ID) {
		PRD_DEPT_ID = pRD_DEPT_ID;
	}
	public double getTOTALRECEIVEDQTY() {
		return TOTALRECEIVEDQTY;
	}
	public void setTOTALRECEIVEDQTY(double tOTALRECEIVEDQTY) {
		TOTALRECEIVEDQTY = tOTALRECEIVEDQTY;
	}
	public double getTOTALISSUEDQTY() {
		return TOTALISSUEDQTY;
	}
	public void setTOTALISSUEDQTY(double tOTALISSUEDQTY) {
		TOTALISSUEDQTY = tOTALISSUEDQTY;
	}
	public double getOPENINGSTOCKQTY() {
		return OPENINGSTOCKQTY;
	}
	public void setOPENINGSTOCKQTY(double oPENINGSTOCKQTY) {
		OPENINGSTOCKQTY = oPENINGSTOCKQTY;
	}
	public double getCLOSINGSTOCKQTY() {
		return CLOSINGSTOCKQTY;
	}
	public void setCLOSINGSTOCKQTY(double cLOSINGSTOCKQTY) {
		CLOSINGSTOCKQTY = cLOSINGSTOCKQTY;
	}
	public double getAVERAGECOST() {
		return AVERAGECOST;
	}
	public void setAVERAGECOST(double aVERAGECOST) {
		AVERAGECOST = aVERAGECOST;
	}
	public double getTOTALCOST() {
		return TOTALCOST;
	}
	public void setTOTALCOST(double tOTALCOST) {
		TOTALCOST = tOTALCOST;
	}
	public double getPRICE() {
		return PRICE;
	}
	public void setPRICE(double pRICE) {
		PRICE = pRICE;
	}
	public double getTOTALPRICE() {
		return TOTALPRICE;
	}
	public void setTOTALPRICE(double tOTALPRICE) {
		TOTALPRICE = tOTALPRICE;
	}
	public double getLASTRECEIVEDQTY() {
		return LASTRECEIVEDQTY;
	}
	public void setLASTRECEIVEDQTY(double lASTRECEIVEDQTY) {
		LASTRECEIVEDQTY = lASTRECEIVEDQTY;
	}
	public double getLASTISSUEDQTY() {
		return LASTISSUEDQTY;
	}
	public void setLASTISSUEDQTY(double lASTISSUEDQTY) {
		LASTISSUEDQTY = lASTISSUEDQTY;
	}
	public double getSTOCKONHAND() {
		return STOCKONHAND;
	}
	public void setSTOCKONHAND(double sTOCKONHAND) {
		STOCKONHAND = sTOCKONHAND;
	}
	@Override
	public String toString() {
		return "OpenCloseReportPojo [PLANT=" + PLANT + ", ITEM=" + ITEM + ", ITEMDESC=" + ITEMDESC + ", PRDCLSID="
				+ PRDCLSID + ", ITEMTYPE=" + ITEMTYPE + ", PRD_BRAND_ID=" + PRD_BRAND_ID + ", PRD_DEPT_ID="
				+ PRD_DEPT_ID + ", TOTALRECEIVEDQTY=" + TOTALRECEIVEDQTY + ", TOTALISSUEDQTY=" + TOTALISSUEDQTY
				+ ", OPENINGSTOCKQTY=" + OPENINGSTOCKQTY + ", CLOSINGSTOCKQTY=" + CLOSINGSTOCKQTY + ", AVERAGECOST="
				+ AVERAGECOST + ", TOTALCOST=" + TOTALCOST + ", PRICE=" + PRICE + ", TOTALPRICE=" + TOTALPRICE
				+ ", LASTRECEIVEDQTY=" + LASTRECEIVEDQTY + ", LASTISSUEDQTY=" + LASTISSUEDQTY + ", STOCKONHAND="
				+ STOCKONHAND + "]";
	}

}
