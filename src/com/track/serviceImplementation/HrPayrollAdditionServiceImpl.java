package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrPayrollAdditionDAO;
import com.track.db.object.HrPayrollAddition;
import com.track.service.HrPayrollAdditionService;

public class HrPayrollAdditionServiceImpl implements HrPayrollAdditionService {
	
	HrPayrollAdditionDAO hrPayrollAdditionDAO = new HrPayrollAdditionDAO();

	@Override
	public int addpayrolladdition(HrPayrollAddition payrolladdition) throws Exception {
		return hrPayrollAdditionDAO.addpayrolladdition(payrolladdition);
	}

	@Override
	public List<HrPayrollAddition> getAllpayrolladdition(String plant) throws Exception {
		return hrPayrollAdditionDAO.getAllpayrolladdition(plant);
	}

	@Override
	public HrPayrollAddition getpayrolladditionById(String plant, int id) throws Exception {
		return hrPayrollAdditionDAO.getpayrolladditionById(plant, id);
	}

	@Override
	public int updatepayrolladdition(HrPayrollAddition payrolladdition, String user) throws Exception {
		return hrPayrollAdditionDAO.updatepayrolladdition(payrolladdition, user);
	}

	@Override
	public boolean Deletepayrolladdition(String plant, int id) throws Exception {
		return hrPayrollAdditionDAO.Deletepayrolladdition(plant, id);
	}

}
