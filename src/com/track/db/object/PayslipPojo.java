package com.track.db.object;

import java.util.List;

public class PayslipPojo {
	
	private String empcode;
	
	private String name;
	
	private String department;
	
	private String designation;
	
	private String passport;
	
	private String doj;
	
	private String labourcard;
	
	private String bankname;
	
	private String payperiod;
	
	private String paymentdate;
	
	private String paidthrough;
	
	private List<HrPayrollDET> salary;
	
	private double grosspay;
	
	private List<HrPayrollDET> addition;
	
	private List<HrPayrollDET> deduction;
	
	private double netpay;

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getLabourcard() {
		return labourcard;
	}

	public void setLabourcard(String labourcard) {
		this.labourcard = labourcard;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getPayperiod() {
		return payperiod;
	}

	public void setPayperiod(String payperiod) {
		this.payperiod = payperiod;
	}

	public String getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getPaidthrough() {
		return paidthrough;
	}

	public void setPaidthrough(String paidthrough) {
		this.paidthrough = paidthrough;
	}

	public List<HrPayrollDET> getSalary() {
		return salary;
	}

	public void setSalary(List<HrPayrollDET> salary) {
		this.salary = salary;
	}

	public double getGrosspay() {
		return grosspay;
	}

	public void setGrosspay(double grosspay) {
		this.grosspay = grosspay;
	}

	public List<HrPayrollDET> getAddition() {
		return addition;
	}

	public void setAddition(List<HrPayrollDET> addition) {
		this.addition = addition;
	}

	public List<HrPayrollDET> getDeduction() {
		return deduction;
	}

	public void setDeduction(List<HrPayrollDET> deduction) {
		this.deduction = deduction;
	}

	public double getNetpay() {
		return netpay;
	}

	public void setNetpay(double netpay) {
		this.netpay = netpay;
	}

	@Override
	public String toString() {
		return "PayslipPojo [empcode=" + empcode + ", name=" + name + ", department=" + department + ", designation="
				+ designation + ", passport=" + passport + ", doj=" + doj + ", labourcard=" + labourcard + ", bankname="
				+ bankname + ", payperiod=" + payperiod + ", paymentdate=" + paymentdate + ", paidthrough="
				+ paidthrough + ", salary=" + salary + ", grosspay=" + grosspay + ", addition=" + addition
				+ ", deduction=" + deduction + ", netpay=" + netpay + "]";
	}

	
	
	
	
}
