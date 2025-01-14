package com.track.serviceImplementation;

import java.util.List;

import com.track.db.object.HrLeaveApplyHdr;
import com.track.service.HrLeaveApplyHdrService;
import com.track.dao.HrLeaveApplyHdrDAO;

public class HrLeaveApplyHdrServiceImpl implements HrLeaveApplyHdrService{
	
    HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();

	@Override
	public int addHrLeaveApplyHdr(HrLeaveApplyHdr HrLeaveApplyHdr) throws Exception {
		return hrLeaveApplyHdrDAO.addHrLeaveApplyHdr(HrLeaveApplyHdr);
	}

	@Override
	public List<HrLeaveApplyHdr> getAllHrLeaveApplyHdr(String plant) throws Exception {
		return hrLeaveApplyHdrDAO.getAllHrLeaveApplyHdr(plant);
	}

	@Override
	public HrLeaveApplyHdr getHrLeaveApplyHdrById(String plant, int id) throws Exception {
		return hrLeaveApplyHdrDAO.getHrLeaveApplyHdrById(plant, id);
	}

	@Override
	public int updateHrLeaveApplyHdr(HrLeaveApplyHdr HrLeaveApplyHdr, String user) throws Exception {
		return hrLeaveApplyHdrDAO.updateHrLeaveApplyHdr(HrLeaveApplyHdr, user);
	}

	@Override
	public boolean DeleteHrLeaveApplyHdr(String plant, int id) throws Exception {
		return hrLeaveApplyHdrDAO.DeleteHrLeaveApplyHdr(plant, id);
	}

	@Override
	public List<HrLeaveApplyHdr> getHrLeaveApplyHdrbyEmpid(String plant, int empnoid) throws Exception {
		return hrLeaveApplyHdrDAO.getHrLeaveApplyHdrbyEmpid(plant, empnoid);
	}

}
