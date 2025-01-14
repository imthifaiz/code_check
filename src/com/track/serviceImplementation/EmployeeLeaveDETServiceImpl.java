package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.EmployeeLeaveDetDAO;
import com.track.db.object.EmployeeLeaveDET;
import com.track.service.EmployeeLeaveDETService;

public class EmployeeLeaveDETServiceImpl implements EmployeeLeaveDETService {
	
	EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();

	@Override
	public int addEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet) throws Exception {
		return employeeLeaveDetDAO.addEmployeeLeavedet(EmployeeLeavedet);
	}

	@Override
	public List<EmployeeLeaveDET> getAllEmployeeLeavedet(String plant) throws Exception {
		return employeeLeaveDetDAO.getAllEmployeeLeavedet(plant);
	}

	@Override
	public EmployeeLeaveDET getEmployeeLeavedetById(String plant, int id) throws Exception {
		return employeeLeaveDetDAO.getEmployeeLeavedetById(plant, id);
	}

	@Override
	public int updateEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet, String user) throws Exception {
		return employeeLeaveDetDAO.updateEmployeeLeavedet(EmployeeLeavedet, user);
	}

	@Override
	public boolean DeleteEmployeeLeavedet(String plant, int id) throws Exception {
		return employeeLeaveDetDAO.DeleteEmployeeLeavedet(plant, id);
	}

	@Override
	public boolean IsEmployeeLeavedet(String plant, int leavetypeid, int empnoid, String leaveyear) throws Exception {
		return employeeLeaveDetDAO.IsEmployeeLeavedet(plant, leavetypeid, empnoid, leaveyear);
	}

	@Override
	public List<EmployeeLeaveDET> IsEmployeeLeavedetlist(String plant, int leavetypeid, int empnoid, String leaveyear) throws Exception {
		return employeeLeaveDetDAO.IsEmployeeLeavedetlist(plant, leavetypeid, empnoid, leaveyear);
	}

}
