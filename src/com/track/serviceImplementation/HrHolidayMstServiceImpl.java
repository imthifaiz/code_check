package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.HrHolidayMstDAO;
import com.track.db.object.HrHolidayMst;
import com.track.service.HrHolidayMstService;

public class HrHolidayMstServiceImpl implements HrHolidayMstService {
	
	HrHolidayMstDAO hrHolidayMstDAO = new HrHolidayMstDAO();

	@Override
	public int addHoliday(HrHolidayMst Holiday) throws Exception {
		return hrHolidayMstDAO.addHoliday(Holiday);
	}

	@Override
	public List<HrHolidayMst> getAllHoliday(String plant) throws Exception {
		return hrHolidayMstDAO.getAllHoliday(plant);
	}

	@Override
	public HrHolidayMst getHolidayById(String plant, int id) throws Exception {
		return hrHolidayMstDAO.getHolidayById(plant, id);
	}

	@Override
	public int updateHoliday(HrHolidayMst Holiday, String user) throws Exception {
		return hrHolidayMstDAO.updateHoliday(Holiday, user);
	}

	@Override
	public boolean DeleteHoliday(String plant, int id) throws Exception {
		return hrHolidayMstDAO.DeleteHoliday(plant, id);
	}

	@Override
	public boolean IsHoliday(String plant, String holidaydate) throws Exception {
		return hrHolidayMstDAO.IsHoliday(plant, holidaydate);
	}

	@Override
	public List<HrHolidayMst> IsHolidaylist(String plant, String holidaydate) throws Exception {
		return hrHolidayMstDAO.IsHolidaylist(plant, holidaydate);
	}

	@Override
	public boolean IsHolidayedit(String plant, String id, String holidaydate) throws Exception {
		return hrHolidayMstDAO.IsHolidayedit(plant, id, holidaydate);
	}

}
