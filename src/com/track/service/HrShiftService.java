package com.track.service;


import java.util.List;

import com.track.db.object.HrShiftMst;

public interface HrShiftService {
	
	public int addShiftMst(HrShiftMst ShiftMst) throws Exception;
	
	public List<HrShiftMst> getAllShiftMst(String plant) throws Exception;
	
	public HrShiftMst getShiftMstById(String plant,int id) throws Exception;
	
	public int updateShiftMst(HrShiftMst ShiftMst,String user) throws Exception;
	
	public boolean DeleteShiftMst(String plant,int id) throws Exception;
	
	public boolean IsShiftMst(String plant,String ShiftMst) throws Exception;
	
	public List<HrShiftMst> IsShiftMstlist(String plant,String ShiftMst, int etid) throws Exception;

}
