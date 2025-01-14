package com.track.service;

import java.util.List;

import com.track.db.object.HrEmpType;


public interface HrEmpTypeService {

	public int addEmployeetype(HrEmpType employeetype) throws Exception;
	
	public List<HrEmpType> getAllEmployeetype(String plant) throws Exception;
	
	public List<HrEmpType> getAllEmployeetypedropdown(String plant, String emptype) throws Exception;
	
	public HrEmpType getEmployeetypeById(String plant,int id) throws Exception;
	
	public int updateEmployeetype(HrEmpType employeetype,String user) throws Exception;
	
	public boolean DeleteEmployeetype(String plant,int id) throws Exception;
	
	public boolean IsEmployeetype(String plant,String emptype) throws Exception;
	
	public List<HrEmpType> IsEmployeetypelist(String plant,String emptype) throws Exception;
	
}
