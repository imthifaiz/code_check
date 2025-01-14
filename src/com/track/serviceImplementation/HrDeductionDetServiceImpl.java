package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrDeductionDetDAO;
import com.track.dao.HrDeductionHdrDAO;
import com.track.db.object.HrDeductionDet;
import com.track.service.HrDeductionDetService;

public class HrDeductionDetServiceImpl implements HrDeductionDetService {
	
	HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();

	@Override
	public int adddeductiondet(HrDeductionDet deductiondet) throws Exception {
		return hrDeductionDetDAO.adddeductiondet(deductiondet);
	}

	@Override
	public List<HrDeductionDet> getAlldeductiondet(String plant) throws Exception {
		return hrDeductionDetDAO.getAlldeductiondet(plant);
	}

	@Override
	public HrDeductionDet getdeductiondetById(String plant, int id) throws Exception {
		return hrDeductionDetDAO.getdeductiondetById(plant, id);
	}

	@Override
	public int updatedeductiondet(HrDeductionDet deductiondet, String user) throws Exception {
		return hrDeductionDetDAO.updatedeductiondet(deductiondet, user);
	}

	@Override
	public boolean Deletedeductiondet(String plant, int id) throws Exception {
		return hrDeductionDetDAO.Deletedeductiondet(plant, id);
	}

}
