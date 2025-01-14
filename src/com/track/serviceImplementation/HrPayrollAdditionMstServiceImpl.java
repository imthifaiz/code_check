package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrPayrollAdditionMstDAO;
import com.track.db.object.HrPayrollAdditionMst;
import com.track.service.HrPayrollAdditionMstService;

public class HrPayrollAdditionMstServiceImpl implements HrPayrollAdditionMstService {
	
	HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();

	@Override
	public int addpayrolladditionmst(HrPayrollAdditionMst payrolladditionmst) throws Exception {
		return hrPayrollAdditionMstDAO.addpayrolladditionmst(payrolladditionmst);
	}

	@Override
	public List<HrPayrollAdditionMst> getAllpayrolladditionmst(String plant) throws Exception {
		return hrPayrollAdditionMstDAO.getAllpayrolladditionmst(plant);
	}

	@Override
	public HrPayrollAdditionMst getpayrolladditionmstById(String plant, int id) throws Exception {
		return hrPayrollAdditionMstDAO.getpayrolladditionmstById(plant, id);
	}

	@Override
	public int updatepayrolladditionmst(HrPayrollAdditionMst payrolladditionmst, String user) throws Exception {
		return hrPayrollAdditionMstDAO.updatepayrolladditionmst(payrolladditionmst, user);
	}

	@Override
	public boolean Deletepayrolladditionmst(String plant, int id) throws Exception {
		return hrPayrollAdditionMstDAO.Deletepayrolladditionmst(plant, id);
	}

	@Override
	public boolean ispayrolladditionmst(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.ispayrolladditionmst(plant, addname);
	}

	@Override
	public List<HrPayrollAdditionMst> payrolladditionmstbyname(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.payrolladditionmstbyname(plant, addname);
	}

	@Override
	public List<HrPayrollAdditionMst> payrolladditionmstdeduct(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, addname);
	}

	@Override
	public List<HrPayrollAdditionMst> payrolladditionmstadd(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, addname);
	}

	@Override
	public List<HrPayrollAdditionMst> payrolladditionmstall(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.payrolladditionmstall(plant, addname);
	}

	@Override
	public List<HrPayrollAdditionMst> payrolladditionmstclaim(String plant, String addname) throws Exception {
		return hrPayrollAdditionMstDAO.payrolladditionmstclaim(plant, addname);
	}

}
