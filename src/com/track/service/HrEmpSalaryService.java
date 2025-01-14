package com.track.service;

import java.util.List;

import com.track.db.object.HrEmpSalaryMst;

public interface HrEmpSalaryService {
	
	public int addSalary(HrEmpSalaryMst empSalary) throws Exception;
	
	public List<HrEmpSalaryMst> getAllSalary(String plant) throws Exception;
	
	public HrEmpSalaryMst getSalaryById(String plant, int id) throws Exception;

	public int updateSalary(HrEmpSalaryMst empSalary, String user) throws Exception;
	
	public boolean DeleteSalary(String plant,int id) throws Exception;
	
	public boolean IsSalaryExists(String plant, String empSalary)throws Exception;
	
	public List<HrEmpSalaryMst> IsSalarylist(String plant, String empSalary)throws Exception;
	
	public List<HrEmpSalaryMst> IsSalarylistdropdown(String plant, String empSalary)throws Exception;

}
