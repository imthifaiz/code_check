package com.track.db.object;

public class POSShiftAmountHdr {
	@DBTable(columnName ="ID")
    private int ID;
    @DBTable(columnName ="SHIFTID")
    private int shiftId;
    @DBTable(columnName ="PLANT")
    private String PLANT;
    @DBTable(columnName ="SHIFTDATE")
    private String shiftDate;
    @DBTable(columnName ="SHIFTTIME")
    private String shiftTime;
    @DBTable(columnName ="OUTLET")
    private String Outlet;
    @DBTable(columnName ="TERMINAL")
    private String Terminal;
    @DBTable(columnName ="EMPLOYEE_ID")
    private String employeeID;
    @DBTable(columnName ="TOTALSALES")
    private Double totalSales;
    @DBTable(columnName ="TOTALRECEIVEDAMOUNT")
    private Double totalReceivedAmount;
    @DBTable(columnName ="PAYSTATUS")
    private int payStatus;
    @DBTable(columnName ="PAYRECIVEDSTATUS")
    private int payReciveStatus;
    @DBTable(columnName ="EXPENSE")
    private Double expenses;
    @DBTable(columnName ="DRAWERAMOUNT")
    private Double drawerAmount;
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
	public int getShiftId() {
		return shiftId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}
	public String getShiftDate() {
		return shiftDate;
	}
	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}
	public String getShiftTime() {
		return shiftTime;
	}
	public void setShiftTime(String shiftTime) {
		this.shiftTime = shiftTime;
	}
	public String getOutlet() {
		return Outlet;
	}
	public void setOutlet(String outlet) {
		Outlet = outlet;
	}
	public String getTerminal() {
		return Terminal;
	}
	public void setTerminal(String terminal) {
		Terminal = terminal;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public Double getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}
	public Double getTotalReceivedAmount() {
		return totalReceivedAmount;
	}
	public void setTotalReceivedAmount(Double totalReceivedAmount) {
		this.totalReceivedAmount = totalReceivedAmount;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getPayReciveStatus() {
		return payReciveStatus;
	}
	public void setPayReciveStatus(int payReciveStatus) {
		this.payReciveStatus = payReciveStatus;
	}
	public Double getExpenses() {
		return expenses;
	}
	public void setExpenses(Double expenses) {
		this.expenses = expenses;
	}
	public Double getDrawerAmount() {
		return drawerAmount;
	}
	public void setDrawerAmount(Double drawerAmount) {
		this.drawerAmount = drawerAmount;
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
		return "POSShiftAmountHdr [ID=" + ID + ", shiftId=" + shiftId + ", PLANT=" + PLANT + ", shiftDate=" + shiftDate
				+ ", shiftTime=" + shiftTime + ", Outlet=" + Outlet + ", Terminal=" + Terminal + ", employeeID="
				+ employeeID + ", totalSales=" + totalSales + ", totalReceivedAmount=" + totalReceivedAmount
				+ ", payStatus=" + payStatus + ", payReciveStatus=" + payReciveStatus + ", expenses=" + expenses
				+ ", drawerAmount=" + drawerAmount + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY="
				+ UPBY + "]";
	}
	
	
}
