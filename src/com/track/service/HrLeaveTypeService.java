package com.track.service;

import java.util.List;

import com.track.db.object.HrLeaveType;

public interface HrLeaveTypeService {

	public int addLeavetype(HrLeaveType leavetype) throws Exception;
	
	public List<HrLeaveType> getAllLeavetype(String plant) throws Exception;
	
	public HrLeaveType getLeavetypeById(String plant,int id) throws Exception;
	
	public int updateLeavetype(HrLeaveType leavetype,String user) throws Exception;
	
	public boolean DeleteLeavetype(String plant,int id) throws Exception;
	
	public boolean IsLeavetype(String plant,String leavetype, int etid) throws Exception;
	
	public List<HrLeaveType> IsLeavetypelist(String plant,String leavetype, int etid) throws Exception;
	
	public List<HrLeaveType> IsLeavetypelistdropdown(String plant,String leavetype, int etid) throws Exception;
}
