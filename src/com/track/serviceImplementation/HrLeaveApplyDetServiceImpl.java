package com.track.serviceImplementation;


import java.util.List;

import com.track.db.object.BlockingDates;
import com.track.db.object.HrLeaveApplyDet;
import com.track.service.HrLeaveApplyDetService;
import com.track.dao.HrLeaveApplyDetDAO;

public class HrLeaveApplyDetServiceImpl implements HrLeaveApplyDetService {
	
	HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();

	@Override
	public int addHrLeaveApplyDet(HrLeaveApplyDet HrLeaveApplyDet) throws Exception {
		return hrLeaveApplyDetDAO.addHrLeaveApplyDet(HrLeaveApplyDet);
	}

	@Override
	public List<HrLeaveApplyDet> getAllHrLeaveApplyDet(String plant) throws Exception {
		return hrLeaveApplyDetDAO.getAllHrLeaveApplyDet(plant);
	}

	@Override
	public HrLeaveApplyDet getHrLeaveApplyDetById(String plant, int id) throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetById(plant, id);
	}

	@Override
	public int updateHrLeaveApplyDet(HrLeaveApplyDet HrLeaveApplyDet, String user) throws Exception {
		return hrLeaveApplyDetDAO.updateHrLeaveApplyDet(HrLeaveApplyDet, user);
	}

	@Override
	public boolean DeleteHrLeaveApplyDet(String plant, int id) throws Exception {
		return hrLeaveApplyDetDAO.DeleteHrLeaveApplyDet(plant, id);
	}

	@Override
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpid(String plant, int empnoid) throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpid(plant, empnoid);
	}

	@Override
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidAndDate(String plant, int empnoid, String fromdate,
			String todate) throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpidAndDate(plant, empnoid, fromdate, todate);
	}

	@Override
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidfullday(String plant, int empnoid) throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpidfullday(plant, empnoid);
	}

	@Override
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpiddate(String plant, int empnoid, String leavedate)
			throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpiddate(plant, empnoid, leavedate);
				
	}

	@Override
	public List<BlockingDates> getHrLeaveApplyDetbyEmpidTwohalfdays(String plant, int empnoid) throws Exception {
		return hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, empnoid);
	}

}
