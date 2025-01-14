package com.track.service;

import java.util.List;

import com.track.db.object.HrLeaveApplyHdr;

public interface HrLeaveApplyHdrService {
	
	public int addHrLeaveApplyHdr (HrLeaveApplyHdr HrLeaveApplyHdr) throws Exception;
	
	public List<HrLeaveApplyHdr> getAllHrLeaveApplyHdr(String plant) throws Exception;
	
	public HrLeaveApplyHdr getHrLeaveApplyHdrById(String plant,int id) throws Exception;
	
	public int updateHrLeaveApplyHdr(HrLeaveApplyHdr HrLeaveApplyHdr,String user) throws Exception;
	
	public boolean DeleteHrLeaveApplyHdr(String plant,int id) throws Exception;
	
	public List<HrLeaveApplyHdr> getHrLeaveApplyHdrbyEmpid(String plant, int empnoid) throws Exception;

}
