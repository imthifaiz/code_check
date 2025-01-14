package com.track.service;

import java.util.List;

import com.track.db.object.HrEmpDepartmentMst;

public interface HrDepartmentService {
	
	public int addDepartment(HrEmpDepartmentMst department) throws Exception;
	
	public List<HrEmpDepartmentMst> getAllDepartment(String plant) throws Exception;
	
	public HrEmpDepartmentMst getDepartmentById(String plant, int id) throws Exception;

	public int updateDepartment(HrEmpDepartmentMst department, String user) throws Exception;
	
	public boolean DeleteDepartment(String plant,int id) throws Exception;
	
	public boolean IsDepartmentExists(String plant, String department)throws Exception;
	
	public List<HrEmpDepartmentMst> IsDepartmentlist(String plant, String department)throws Exception;
	
	public List<HrEmpDepartmentMst> IsDepartmentlistdropdown(String plant, String department)throws Exception;

}
