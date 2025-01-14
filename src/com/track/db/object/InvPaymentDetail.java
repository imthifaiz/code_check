package com.track.db.object;

public class InvPaymentDetail {
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="LNNO")
	private int LNNO;
	
	@DBTable(columnName ="RECEIVEHDRID")
	private int RECEIVEHDRID;
	
	@DBTable(columnName ="INVOICEHDRID")
	private int INVOICEHDRID;
	
	@DBTable(columnName ="AMOUNT")
	private Double AMOUNT=(double) 0;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="DONO")
	private String DONO;
	
	@DBTable(columnName ="TYPE")
	private String TYPE;
	
	@DBTable(columnName ="BALANCE")
	private Double BALANCE;
	
	@DBTable(columnName ="ADVANCEFROM")
	private String ADVANCEFROM;
	
	@DBTable(columnName ="CREDITAPPLYKEY")
	private String CREDITAPPLYKEY;
	
	@DBTable(columnName ="CREDITNOTEHDRID")
	private int CREDITNOTEHDRID;
	
	@DBTable(columnName ="CURRENCYUSEQT")
	private Double CURRENCYUSEQT=(double) 0;
	
	@DBTable(columnName ="TOTAL_PAYING")
	private Double TOTAL_PAYING=(double) 0;

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

	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}

	public int getRECEIVEHDRID() {
		return RECEIVEHDRID;
	}

	public void setRECEIVEHDRID(int rECEIVEHDRID) {
		RECEIVEHDRID = rECEIVEHDRID;
	}

	public int getINVOICEHDRID() {
		return INVOICEHDRID;
	}

	public void setINVOICEHDRID(int iNVOICEHDRID) {
		INVOICEHDRID = iNVOICEHDRID;
	}

	public Double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
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

	public String getDONO() {
		return DONO;
	}

	public void setDONO(String dONO) {
		DONO = dONO;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public Double getBALANCE() {
		return BALANCE;
	}

	public void setBALANCE(Double bALANCE) {
		BALANCE = bALANCE;
	}

	public String getADVANCEFROM() {
		return ADVANCEFROM;
	}

	public void setADVANCEFROM(String aDVANCEFROM) {
		ADVANCEFROM = aDVANCEFROM;
	}

	public String getCREDITAPPLYKEY() {
		return CREDITAPPLYKEY;
	}

	public void setCREDITAPPLYKEY(String cREDITAPPLYKEY) {
		CREDITAPPLYKEY = cREDITAPPLYKEY;
	}

	public int getCREDITNOTEHDRID() {
		return CREDITNOTEHDRID;
	}

	public void setCREDITNOTEHDRID(int cREDITNOTEHDRID) {
		CREDITNOTEHDRID = cREDITNOTEHDRID;
	}

	public Double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(Double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
	}
	
	public Double getTOTAL_PAYING() {
		return TOTAL_PAYING;
	}
	
	public void setTOTAL_PAYING(Double tOTAL_PAYING) {
		TOTAL_PAYING = tOTAL_PAYING;
	}

	@Override
	public String toString() {
		return "InvPaymentDetail [ID=" + ID + ", PLANT=" + PLANT + ", LNNO=" + LNNO + ", RECEIVEHDRID=" + RECEIVEHDRID
				+ ", INVOICEHDRID=" + INVOICEHDRID + ", AMOUNT=" + AMOUNT + ", CRAT=" + CRAT + ", CRBY=" + CRBY
				+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", DONO=" + DONO + ", TYPE=" + TYPE + ", BALANCE=" + BALANCE
				+ ", ADVANCEFROM=" + ADVANCEFROM + ", CREDITAPPLYKEY=" + CREDITAPPLYKEY + ", CREDITNOTEHDRID="
				+ CREDITNOTEHDRID + ", CURRENCYUSEQT=" + CURRENCYUSEQT + "]";
	}

	
	

	
	
	

}
