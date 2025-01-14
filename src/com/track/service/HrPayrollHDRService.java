package com.track.service;

import java.util.List;

import com.track.db.object.HrPayrollHDR;


public interface HrPayrollHDRService {

	public int addpayrollhdr(HrPayrollHDR payrollhdr) throws Exception;
	
	public List<HrPayrollHDR> getAllpayrollhdr(String plant) throws Exception;
	
	public HrPayrollHDR getpayrollhdrById(String plant,int id) throws Exception;
	
	public int updatepayrollhdr(HrPayrollHDR payrollhdr,String user) throws Exception;
	
	public boolean Deletepayrollhdr(String plant,int id) throws Exception;
	
	public boolean Ispayrollhdr(String plant,int empid, String month, String year) throws Exception;
	
	public HrPayrollHDR payrollhdrbyempidmonthyear(String plant,int empid, String month, String year) throws Exception;
	
	public List<HrPayrollHDR> payrollhdrbyempid(String plant,int empid) throws Exception;
	
	public List<HrPayrollHDR>  payrollhdrbymonthyear(String plant,String month, String year) throws Exception;
	
	public List<HrPayrollHDR> payrollhdrbystatus(String plant,String status) throws Exception;
	
}
