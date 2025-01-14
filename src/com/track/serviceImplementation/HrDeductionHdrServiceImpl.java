package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrDeductionHdrDAO;
import com.track.db.object.HrDeductionHdr;
import com.track.service.HrDeductionHdrService;

public class HrDeductionHdrServiceImpl implements HrDeductionHdrService {
	
	HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();

	@Override
	public int adddeductionhdr(HrDeductionHdr deductionhdr) throws Exception {
		return hrDeductionHdrDAO.adddeductionhdr(deductionhdr);
	}

	@Override
	public List<HrDeductionHdr> getAlldeductionhdr(String plant) throws Exception {
		return hrDeductionHdrDAO.getAlldeductionhdr(plant);
	}

	@Override
	public HrDeductionHdr getdeductionhdrById(String plant, int id) throws Exception {
		return hrDeductionHdrDAO.getdeductionhdrById(plant, id);
	}

	@Override
	public int updatedeductionhdr(HrDeductionHdr deductionhdr, String user) throws Exception {
		return hrDeductionHdrDAO.updatedeductionhdr(deductionhdr, user);
	}

	@Override
	public boolean Deletedeductionhdr(String plant, int id) throws Exception {
		return hrDeductionHdrDAO.Deletedeductionhdr(plant, id);
	}

}
