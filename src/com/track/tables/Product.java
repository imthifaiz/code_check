package com.track.tables;

public class Product {
	private static float UNITPRICE;
	private static  int stkqty ;String sITEM = "";
	String sITEMDESC = "";
	public static float getUNITPRICE() {
		return UNITPRICE;
	}
	public static void setUNITPRICE(float uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}
	public static int getStkqty() {
		return stkqty;
	}
	public static void setStkqty(int stkqty) {
		Product.stkqty = stkqty;
	}
	public String getsITEM() {
		return sITEM;
	}
	public void setsITEM(String sITEM) {
		this.sITEM = sITEM;
	}
	public String getsITEMDESC() {
		return sITEMDESC;
	}
	public void setsITEMDESC(String sITEMDESC) {
		this.sITEMDESC = sITEMDESC;
	}

}
