package com.track.service;

import java.util.List;

import com.track.db.object.HrPayrollDET;

public interface HrPayrollDETService {
	
	public int addpayrolldet(HrPayrollDET payrolldet) throws Exception;
	
	public List<HrPayrollDET> getAllpayrolldet(String plant) throws Exception;
	
	public HrPayrollDET getpayrolldetById(String plant,int id) throws Exception;
	
	public int updatepayrolldet(HrPayrollDET payrolldet,String user) throws Exception;
	
	public boolean Deletepayrolldet(String plant,int id) throws Exception;
	
	public boolean Ispayrolldet(String plant,String salarytype, int hdrid) throws Exception;
	
	public HrPayrollDET payrolldetbyhdrisandtype(String plant,String salarytype, int hdrid) throws Exception;
	
	public List<HrPayrollDET> getpayrolldetByhdrid(String plant,int hdrid) throws Exception;

}
