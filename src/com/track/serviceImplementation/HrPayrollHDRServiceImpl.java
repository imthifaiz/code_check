package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrPayrollHDRDAO;
import com.track.db.object.HrPayrollHDR;
import com.track.service.HrPayrollHDRService;

public class HrPayrollHDRServiceImpl implements HrPayrollHDRService {
	
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();

	@Override
	public int addpayrollhdr(HrPayrollHDR payrollhdr) throws Exception {
		return hrPayrollHDRDAO.addpayrollhdr(payrollhdr);
	}

	@Override
	public List<HrPayrollHDR> getAllpayrollhdr(String plant) throws Exception {
		return hrPayrollHDRDAO.getAllpayrollhdr(plant);
	}

	@Override
	public HrPayrollHDR getpayrollhdrById(String plant, int id) throws Exception {
		return hrPayrollHDRDAO.getpayrollhdrById(plant, id);
	}

	@Override
	public int updatepayrollhdr(HrPayrollHDR payrollhdr, String user) throws Exception {
		return hrPayrollHDRDAO.updatepayrollhdr(payrollhdr, user);
	}

	@Override
	public boolean Deletepayrollhdr(String plant, int id) throws Exception {
		return hrPayrollHDRDAO.Deletepayrollhdr(plant, id);
	}

	@Override
	public boolean Ispayrollhdr(String plant, int empid, String month, String year) throws Exception {
		return hrPayrollHDRDAO.Ispayrollhdr(plant, empid, month, year);
	}

	@Override
	public HrPayrollHDR payrollhdrbyempidmonthyear(String plant, int empid, String month, String year)
			throws Exception {
		return hrPayrollHDRDAO.payrollhdrbyempidmonthyear(plant, empid, month, year);
	}

	@Override
	public List<HrPayrollHDR> payrollhdrbyempid(String plant, int empid) throws Exception {
		return hrPayrollHDRDAO.payrollhdrbyempid(plant, empid);
	}

	@Override
	public List<HrPayrollHDR> payrollhdrbymonthyear(String plant, String month, String year) throws Exception {
		return hrPayrollHDRDAO.payrollhdrbymonthyear(plant, month, year);
	}

	@Override
	public List<HrPayrollHDR> payrollhdrbystatus(String plant, String status) throws Exception {
		return hrPayrollHDRDAO.payrollhdrbystatus(plant, status);
	}

}
