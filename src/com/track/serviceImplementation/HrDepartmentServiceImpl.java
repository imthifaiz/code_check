package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrDepartmentDAO;
import com.track.db.object.HrEmpDepartmentMst;
import com.track.service.HrDepartmentService;

public class HrDepartmentServiceImpl implements HrDepartmentService {
	
	private HrDepartmentDAO hrDepartmentDAO=new HrDepartmentDAO();
	
	@Override
	public int addDepartment(HrEmpDepartmentMst department) throws Exception {
		return hrDepartmentDAO.addDepartment(department);
	}

	@Override
	public List<HrEmpDepartmentMst> getAllDepartment(String plant) throws Exception {
		return hrDepartmentDAO.getAllDepartment(plant);
	}

	@Override
	public HrEmpDepartmentMst getDepartmentById(String plant, int id) throws Exception {
		return hrDepartmentDAO.getDepartmentById(plant, id);
	}

	@Override
	public int updateDepartment(HrEmpDepartmentMst department, String user) throws Exception {
		return hrDepartmentDAO.updateDepartment(department, user);
	}

	@Override
	public boolean DeleteDepartment(String plant, int id) throws Exception {
		return hrDepartmentDAO.DeleteDepartment(plant, id);
	}
	
	@Override
	public boolean IsDepartmentExists(String plant, String department) throws Exception {
		return hrDepartmentDAO.IsDepartmentExists(plant, department);
	}
	
	@Override
	public List<HrEmpDepartmentMst> IsDepartmentlist(String plant, String department)throws Exception {
		return hrDepartmentDAO.IsDepartmentlist(plant, department);
	}

	@Override
	public List<HrEmpDepartmentMst> IsDepartmentlistdropdown(String plant, String department) throws Exception {
		return hrDepartmentDAO.IsDepartmentlistdropdown(plant, department);
	}

}
