package com.track.db.object;

public class FinCountryTaxType {
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="COUNTRY_CODE")
	private String COUNTRY_CODE;
	
	@DBTable(columnName ="CONFIGKEY")
	private String CONFIGKEY;
	
	@DBTable(columnName ="TAXTYPE")
	private String TAXTYPE;
	
	@DBTable(columnName ="TAXDESC")
	private String TAXDESC;
	
	@DBTable(columnName ="SHOWTAX")
	private Short SHOWTAX;
	
	@DBTable(columnName ="SHOWSTATE")
	private Short SHOWSTATE;
	
	@DBTable(columnName ="ISZERO")
	private Short ISZERO;
	
	@DBTable(columnName ="SHOWPERCENTAGE")
	private Short SHOWPERCENTAGE;
	
	@DBTable(columnName ="TAXBOX")
	private int TAXBOX ;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getCOUNTRY_CODE() {
		return COUNTRY_CODE;
	}

	public void setCOUNTRY_CODE(String cOUNTRY_CODE) {
		COUNTRY_CODE = cOUNTRY_CODE;
	}

	public String getCONFIGKEY() {
		return CONFIGKEY;
	}

	public void setCONFIGKEY(String cONFIGKEY) {
		CONFIGKEY = cONFIGKEY;
	}

	public String getTAXTYPE() {
		return TAXTYPE;
	}

	public void setTAXTYPE(String tAXTYPE) {
		TAXTYPE = tAXTYPE;
	}

	public String getTAXDESC() {
		return TAXDESC;
	}

	public void setTAXDESC(String tAXDESC) {
		TAXDESC = tAXDESC;
	}

	public Short getSHOWTAX() {
		return SHOWTAX;
	}

	public void setSHOWTAX(Short sHOWTAX) {
		SHOWTAX = sHOWTAX;
	}

	public Short getSHOWSTATE() {
		return SHOWSTATE;
	}

	public void setSHOWSTATE(Short sHOWSTATE) {
		SHOWSTATE = sHOWSTATE;
	}

	public Short getISZERO() {
		return ISZERO;
	}

	public void setISZERO(Short iSZERO) {
		ISZERO = iSZERO;
	}

	public Short getSHOWPERCENTAGE() {
		return SHOWPERCENTAGE;
	}

	public void setSHOWPERCENTAGE(Short sHOWPERCENTAGE) {
		SHOWPERCENTAGE = sHOWPERCENTAGE;
	}

	public int getTAXBOX() {
		return TAXBOX;
	}

	public void setTAXBOX(int tAXBOX) {
		TAXBOX = tAXBOX;
	}

	@Override
	public String toString() {
		return "FinCountryTaxType [ID=" + ID + ", COUNTRY_CODE=" + COUNTRY_CODE + ", CONFIGKEY=" + CONFIGKEY
				+ ", TAXTYPE=" + TAXTYPE + ", TAXDESC=" + TAXDESC + ", SHOWTAX=" + SHOWTAX + ", SHOWSTATE=" + SHOWSTATE
				+ ", ISZERO=" + ISZERO + ", SHOWPERCENTAGE=" + SHOWPERCENTAGE + ", TAXBOX=" + TAXBOX + "]";
	}

	
	

	
}
