package com.track.db.object;

public class POSAmountByPayMode {
	@DBTable(columnName ="ID")
    private int ID;
    @DBTable(columnName ="PLANT")
    private String PLANT;
    @DBTable(columnName ="HDRID")
    private int hdrId;
    @DBTable(columnName ="CURRENCYID")
    private String currencyId;
    @DBTable(columnName ="AMOUNT")
    private Double amount;
    @DBTable(columnName ="BALANEAMOUNT")
    private Double balanceAmount;
    @DBTable(columnName ="RECIVEDAMOUNT")
    private Double recivedAmount;
    @DBTable(columnName ="EXPENSES")
    private Double expenses;
    @DBTable(columnName ="POSDIFFAMOUNT")
    private Double posDiffAmount;
    @DBTable(columnName ="EXPENSESACCOUNT")
    private String expensesAccount;
    @DBTable(columnName ="POSDIFFACCOUNT")
    private String posDiffAccount;
    @DBTable(columnName ="RECEIVEDACCOUNT")
    private String receivedAccount;
    @DBTable(columnName ="PAYMENTMODE")
    private String paymentMode;
    @DBTable(columnName ="RECIVEDSTATUS")
    private int recivedStatus;
    @DBTable(columnName ="BANKINSTATUS")
    private int bankinStatus;
    @DBTable(columnName ="BANKINID")
    private int bankinId;
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
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}
	public int getHdrId() {
		return hdrId;
	}
	public void setHdrId(int hdrId) {
		this.hdrId = hdrId;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public Double getRecivedAmount() {
		return recivedAmount;
	}
	public void setRecivedAmount(Double recivedAmount) {
		this.recivedAmount = recivedAmount;
	}
	public Double getExpenses() {
		return expenses;
	}
	public void setExpenses(Double expenses) {
		this.expenses = expenses;
	}
	public Double getPosDiffAmount() {
		return posDiffAmount;
	}
	public void setPosDiffAmount(Double posDiffAmount) {
		this.posDiffAmount = posDiffAmount;
	}
	public String getExpensesAccount() {
		return expensesAccount;
	}
	public void setExpensesAccount(String expensesAccount) {
		this.expensesAccount = expensesAccount;
	}
	public String getPosDiffAccount() {
		return posDiffAccount;
	}
	public void setPosDiffAccount(String posDiffAccount) {
		this.posDiffAccount = posDiffAccount;
	}
	public String getReceivedAccount() {
		return receivedAccount;
	}
	public void setReceivedAccount(String receivedAccount) {
		this.receivedAccount = receivedAccount;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public int getRecivedStatus() {
		return recivedStatus;
	}
	public void setRecivedStatus(int recivedStatus) {
		this.recivedStatus = recivedStatus;
	}
	public int getBankinStatus() {
		return bankinStatus;
	}
	public void setBankinStatus(int bankinStatus) {
		this.bankinStatus = bankinStatus;
	}
	public int getBankinId() {
		return bankinId;
	}
	public void setBankinId(int bankinId) {
		this.bankinId = bankinId;
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
		return "POSAmountByPayMode [ID=" + ID + ", PLANT=" + PLANT + ", hdrId=" + hdrId + ", currencyId=" + currencyId
				+ ", amount=" + amount + ", balanceAmount=" + balanceAmount + ", recivedAmount=" + recivedAmount
				+ ", expenses=" + expenses + ", posDiffAmount=" + posDiffAmount + ", expensesAccount=" + expensesAccount
				+ ", posDiffAccount=" + posDiffAccount + ", receivedAccount=" + receivedAccount + ", paymentMode="
				+ paymentMode + ", recivedStatus=" + recivedStatus + ", bankinStatus=" + bankinStatus + ", bankinId="
				+ bankinId + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	

}
