package com.track.service;

import java.util.List;

import com.track.db.object.HrHolidayMst;

public interface HrHolidayMstService {
	
	public int addHoliday(HrHolidayMst Holiday) throws Exception;
	
	public List<HrHolidayMst> getAllHoliday(String plant) throws Exception;
	
	public HrHolidayMst getHolidayById(String plant,int id) throws Exception;
	
	public int updateHoliday(HrHolidayMst Holiday,String user) throws Exception;
	
	public boolean DeleteHoliday(String plant,int id) throws Exception;
	
	public boolean IsHoliday(String plant,String holidaydate) throws Exception;
	
	public List<HrHolidayMst> IsHolidaylist(String plant,String holidaydate) throws Exception;
	
	public boolean IsHolidayedit(String plant,String id,String holidaydate) throws Exception;

}
