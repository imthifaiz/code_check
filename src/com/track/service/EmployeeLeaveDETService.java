package com.track.service;

import java.util.List;

import com.track.db.object.EmployeeLeaveDET;


public interface EmployeeLeaveDETService {
	
	public int addEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet) throws Exception;
	
	public List<EmployeeLeaveDET> getAllEmployeeLeavedet(String plant) throws Exception;
	
	public EmployeeLeaveDET getEmployeeLeavedetById(String plant,int id) throws Exception;
	
	public int updateEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet,String user) throws Exception;
	
	public boolean DeleteEmployeeLeavedet(String plant,int id) throws Exception;
	
	public boolean IsEmployeeLeavedet(String plant,int leavetypeid,int empnoid,String leaveyear) throws Exception;
	
	public List<EmployeeLeaveDET> IsEmployeeLeavedetlist(String plant,int leavetypeid,int empnoid,String leaveyear) throws Exception;
	
}
