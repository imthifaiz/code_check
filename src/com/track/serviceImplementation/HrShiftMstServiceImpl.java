package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrShiftDAO;
import com.track.db.object.HrShiftMst;
import com.track.service.HrShiftService;

public class HrShiftMstServiceImpl implements HrShiftService {
	
	private HrShiftDAO hrShiftDAO = new HrShiftDAO();
	
	@Override
	public int addShiftMst(HrShiftMst ShiftMst) throws Exception {
		return hrShiftDAO.addShiftMst(ShiftMst);
	}

	@Override
	public List<HrShiftMst> getAllShiftMst(String plant) throws Exception {
		return hrShiftDAO.getAllShiftMst(plant);
	}

	@Override
	public HrShiftMst getShiftMstById(String plant, int id) throws Exception {
		return hrShiftDAO.getShiftMstById(plant, id);
	}

	@Override
	public int updateShiftMst(HrShiftMst ShiftMst, String user) throws Exception {
		return hrShiftDAO.updateShiftMst(ShiftMst, user);
	}

	@Override
	public boolean DeleteShiftMst(String plant, int id) throws Exception {
		return hrShiftDAO.DeleteShiftMst(plant, id);
	}

	@Override
	public boolean IsShiftMst(String plant, String ShiftMst) throws Exception {
		return hrShiftDAO.IsShiftMst(plant, ShiftMst);
	}

	@Override
	public List<HrShiftMst> IsShiftMstlist(String plant, String ShiftMst, int etid) throws Exception {
		return hrShiftDAO.IsShiftMstlist(plant, ShiftMst, etid);
	}


}
