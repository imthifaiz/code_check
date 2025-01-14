package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrPayrollDETDAO;
import com.track.db.object.HrPayrollDET;
import com.track.service.HrPayrollDETService;

public class HrPayrollDETServiceImpl implements HrPayrollDETService {
	
	HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();

	@Override
	public int addpayrolldet(HrPayrollDET payrolldet) throws Exception {
		return hrPayrollDETDAO.addpayrolldet(payrolldet);
	}

	@Override
	public List<HrPayrollDET> getAllpayrolldet(String plant) throws Exception {
		return hrPayrollDETDAO.getAllpayrolldet(plant);
	}

	@Override
	public HrPayrollDET getpayrolldetById(String plant, int id) throws Exception {
		return hrPayrollDETDAO.getpayrolldetById(plant, id);
	}

	@Override
	public int updatepayrolldet(HrPayrollDET payrolldet, String user) throws Exception {
		return hrPayrollDETDAO.updatepayrolldet(payrolldet, user);
	}

	@Override
	public boolean Deletepayrolldet(String plant, int id) throws Exception {
		return hrPayrollDETDAO.Deletepayrolldet(plant, id);
	}

	@Override
	public boolean Ispayrolldet(String plant, String salarytype, int hdrid) throws Exception {
		return hrPayrollDETDAO.Ispayrolldet(plant, salarytype, hdrid);
	}

	@Override
	public HrPayrollDET payrolldetbyhdrisandtype(String plant, String salarytype, int hdrid) throws Exception {
		return hrPayrollDETDAO.payrolldetbyhdrisandtype(plant, salarytype, hdrid);
	}

	@Override
	public List<HrPayrollDET> getpayrolldetByhdrid(String plant, int hdrid) throws Exception {
		return hrPayrollDETDAO.getpayrolldetByhdrid(plant, hdrid);
	}

}
