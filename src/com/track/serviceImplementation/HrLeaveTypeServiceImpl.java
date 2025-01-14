package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrLeaveTypeDAO;
import com.track.db.object.HrLeaveType;
import com.track.service.HrLeaveTypeService;

public class HrLeaveTypeServiceImpl implements HrLeaveTypeService {
	
	private HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();

	@Override
	public int addLeavetype(HrLeaveType leavetype) throws Exception {
		return hrLeaveTypeDAO.addLeavetype(leavetype);
	}

	@Override
	public List<HrLeaveType> getAllLeavetype(String plant) throws Exception {
		return hrLeaveTypeDAO.getAllLeavetype(plant);
	}

	@Override
	public HrLeaveType getLeavetypeById(String plant, int id) throws Exception {
		return hrLeaveTypeDAO.getLeavetypeById(plant, id);
	}

	@Override
	public int updateLeavetype(HrLeaveType leavetype, String user) throws Exception {
		return hrLeaveTypeDAO.updateLeavetype(leavetype, user);
	}

	@Override
	public boolean DeleteLeavetype(String plant, int id) throws Exception {
		return hrLeaveTypeDAO.DeleteLeavetype(plant, id);
	}

	@Override
	public boolean IsLeavetype(String plant, String leavetype, int etid) throws Exception {
		return hrLeaveTypeDAO.IsLeavetype(plant, leavetype, etid);
	}

	@Override
	public List<HrLeaveType> IsLeavetypelist(String plant, String leavetype, int etid) throws Exception {
		return hrLeaveTypeDAO.IsLeavetypelist(plant, leavetype, etid);
	}

	@Override
	public List<HrLeaveType> IsLeavetypelistdropdown(String plant, String leavetype, int etid) throws Exception {
		return hrLeaveTypeDAO.IsLeavetypelistdropdown(plant, leavetype, etid);
	}

}
