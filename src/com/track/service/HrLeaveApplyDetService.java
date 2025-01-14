package com.track.service;

import java.util.List;

import com.track.db.object.BlockingDates;
import com.track.db.object.HrLeaveApplyDet;

public interface HrLeaveApplyDetService {

	public int addHrLeaveApplyDet (HrLeaveApplyDet HrLeaveApplyDet) throws Exception;
	
	public List<HrLeaveApplyDet> getAllHrLeaveApplyDet(String plant) throws Exception;
	
	public HrLeaveApplyDet getHrLeaveApplyDetById(String plant,int id) throws Exception;
	
	public int updateHrLeaveApplyDet(HrLeaveApplyDet HrLeaveApplyDet,String user) throws Exception;
	
	public boolean DeleteHrLeaveApplyDet(String plant,int id) throws Exception;
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpid(String plant, int empnoid) throws Exception;
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidAndDate(String plant, int empnoid, String fromdate, String todate) throws Exception;
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidfullday(String plant, int empnoid) throws Exception;
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpiddate(String plant, int empnoid, String leavedate) throws Exception;
	
	public List<BlockingDates> getHrLeaveApplyDetbyEmpidTwohalfdays(String plant, int empnoid) throws Exception;
	
}
