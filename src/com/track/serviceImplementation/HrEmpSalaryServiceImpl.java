package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrEmpSalaryDAO;
import com.track.db.object.HrEmpSalaryMst;
import com.track.service.HrEmpSalaryService;

public class HrEmpSalaryServiceImpl implements HrEmpSalaryService {
	
	private HrEmpSalaryDAO hrEmpSalaryDAO=new HrEmpSalaryDAO();
	
	@Override
	public int addSalary(HrEmpSalaryMst empSalary) throws Exception {
		return hrEmpSalaryDAO.addSalary(empSalary);
	}

	@Override
	public List<HrEmpSalaryMst> getAllSalary(String plant) throws Exception {
		return hrEmpSalaryDAO.getAllSalary(plant);
	}

	@Override
	public HrEmpSalaryMst getSalaryById(String plant, int id) throws Exception {
		return hrEmpSalaryDAO.getSalaryById(plant, id);
	}

	@Override
	public int updateSalary(HrEmpSalaryMst empSalary, String user) throws Exception {
		return hrEmpSalaryDAO.updateSalary(empSalary, user);
	}

	@Override
	public boolean DeleteSalary(String plant, int id) throws Exception {
		return hrEmpSalaryDAO.DeleteSalary(plant, id);
	}
	
	@Override
	public boolean IsSalaryExists(String plant, String empSalary) throws Exception {
		return hrEmpSalaryDAO.IsSalaryExists(plant, empSalary);
	}
	
	@Override
	public List<HrEmpSalaryMst> IsSalarylist(String plant, String empSalary)throws Exception {
		return hrEmpSalaryDAO.IsSalarylist(plant, empSalary);
	}

	@Override
	public List<HrEmpSalaryMst> IsSalarylistdropdown(String plant, String empSalary) throws Exception {
		return hrEmpSalaryDAO.IsSalarylistdropdown(plant, empSalary);
	}

}
