package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrEmpTypeDAO;
import com.track.db.object.HrEmpType;
import com.track.service.HrEmpTypeService;

public class HrEmpTypeServiceImpl implements HrEmpTypeService {
	
	private HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();

	@Override
	public int addEmployeetype(HrEmpType employeetype) throws Exception {
		return hrEmpTypeDAO.addEmployeetype(employeetype);
	}

	@Override
	public List<HrEmpType> getAllEmployeetype(String plant) throws Exception {
		return hrEmpTypeDAO.getAllEmployeetype(plant);
	}

	@Override
	public HrEmpType getEmployeetypeById(String plant, int id) throws Exception {
		return hrEmpTypeDAO.getEmployeetypeById(plant, id);
	}

	@Override
	public int updateEmployeetype(HrEmpType employeetype, String user) throws Exception {
		return hrEmpTypeDAO.updateEmployeetype(employeetype, user);
	}

	@Override
	public boolean DeleteEmployeetype(String plant, int id) throws Exception {
		return hrEmpTypeDAO.DeleteEmployeetype(plant, id);
	}

	@Override
	public boolean IsEmployeetype(String plant, String emptype) throws Exception {
		return hrEmpTypeDAO.IsEmployeetype(plant, emptype);
	}

	@Override
	public List<HrEmpType> IsEmployeetypelist(String plant, String emptype) throws Exception {
		return hrEmpTypeDAO.IsEmployeetypelist(plant, emptype);
	}

	@Override
	public List<HrEmpType> getAllEmployeetypedropdown(String plant, String emptype) throws Exception {
		return hrEmpTypeDAO.getAllEmployeetypedropdown(plant, emptype);
	}

}
