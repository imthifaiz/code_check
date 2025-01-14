package com.track.service;

import java.util.List;

import com.track.db.object.HrPayrollAddition;

public interface HrPayrollAdditionService {

	public int addpayrolladdition(HrPayrollAddition payrolladdition) throws Exception;
	
	public List<HrPayrollAddition> getAllpayrolladdition(String plant) throws Exception;
	
	public HrPayrollAddition getpayrolladditionById(String plant,int id) throws Exception;
	
	public int updatepayrolladdition(HrPayrollAddition payrolladdition,String user) throws Exception;
	
	public boolean Deletepayrolladdition(String plant,int id) throws Exception;
	
}
