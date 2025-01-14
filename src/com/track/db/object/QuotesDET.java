package com.track.db.object;

public class QuotesDET {

	private String PLANT;
	private int ID;
	private int LNNO;
	private int QUOTESHDRID;
	private String ITEM;
	private String ACCOUNT_NAME;
	private Double QTY;
	private Double UNITPRICE;
	private Double DISCOUNT;
	private String TAX_TYPE;
	private Double AMOUNT;
	private String DISCOUNT_TYPE;
	private String NOTE;
	private Double BASETOORDERCURRENCY;
	private String LOC;
	private String UOM;
	private String BATCH;
	private String PACKINGLIST;
	private String DELIVERYNOTE;
	private Double CURRENCYUSEQT;
	private String IS_COGS_SET;
	private Double QTYIS;
	private String STATUS;
	private String CRAT;
	private String CRBY;
	private String UPAT;
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
	public int getLNNO() {
		return LNNO;
	}
	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}
	public int getQUOTESHDRID() {
		return QUOTESHDRID;
	}
	public void setQUOTESHDRID(int qUOTESHDRID) {
		QUOTESHDRID = qUOTESHDRID;
	}
	public String getITEM() {
		return ITEM;
	}
	public void setITEM(String iTEM) {
		ITEM = iTEM;
	}
	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}
	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}
	public Double getQTY() {
		return QTY;
	}
	public void setQTY(Double qTY) {
		QTY = qTY;
	}
	public Double getUNITPRICE() {
		return UNITPRICE;
	}
	public void setUNITPRICE(Double uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}
	public Double getDISCOUNT() {
		return DISCOUNT;
	}
	public void setDISCOUNT(Double dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}
	public String getTAX_TYPE() {
		return TAX_TYPE;
	}
	public void setTAX_TYPE(String tAX_TYPE) {
		TAX_TYPE = tAX_TYPE;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getDISCOUNT_TYPE() {
		return DISCOUNT_TYPE;
	}
	public void setDISCOUNT_TYPE(String dISCOUNT_TYPE) {
		DISCOUNT_TYPE = dISCOUNT_TYPE;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	public Double getBASETOORDERCURRENCY() {
		return BASETOORDERCURRENCY;
	}
	public void setBASETOORDERCURRENCY(Double bASETOORDERCURRENCY) {
		BASETOORDERCURRENCY = bASETOORDERCURRENCY;
	}
	public String getLOC() {
		return LOC;
	}
	public void setLOC(String lOC) {
		LOC = lOC;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getBATCH() {
		return BATCH;
	}
	public void setBATCH(String bATCH) {
		BATCH = bATCH;
	}
	public String getPACKINGLIST() {
		return PACKINGLIST;
	}
	public void setPACKINGLIST(String pACKINGLIST) {
		PACKINGLIST = pACKINGLIST;
	}
	public String getDELIVERYNOTE() {
		return DELIVERYNOTE;
	}
	public void setDELIVERYNOTE(String dELIVERYNOTE) {
		DELIVERYNOTE = dELIVERYNOTE;
	}
	public Double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}
	public void setCURRENCYUSEQT(Double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
	}
	public String getIS_COGS_SET() {
		return IS_COGS_SET;
	}
	public void setIS_COGS_SET(String iS_COGS_SET) {
		IS_COGS_SET = iS_COGS_SET;
	}
	public Double getQTYIS() {
		return QTYIS;
	}
	public void setQTYIS(Double qTYIS) {
		QTYIS = qTYIS;
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
		return "QuotesDET [PLANT=" + PLANT + ", ID=" + ID + ", LNNO=" + LNNO + ", QUOTESHDRID=" + QUOTESHDRID
				+ ", ITEM=" + ITEM + ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", QTY=" + QTY + ", UNITPRICE=" + UNITPRICE
				+ ", DISCOUNT=" + DISCOUNT + ", TAX_TYPE=" + TAX_TYPE + ", AMOUNT=" + AMOUNT + ", DISCOUNT_TYPE="
				+ DISCOUNT_TYPE + ", NOTE=" + NOTE + ", BASETOORDERCURRENCY=" + BASETOORDERCURRENCY + ", LOC=" + LOC
				+ ", UOM=" + UOM + ", BATCH=" + BATCH + ", PACKINGLIST=" + PACKINGLIST + ", DELIVERYNOTE="
				+ DELIVERYNOTE + ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", IS_COGS_SET=" + IS_COGS_SET + ", QTYIS="
				+ QTYIS + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY="
				+ UPBY + "]";
	}
	
	
	
}
