package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.LocMstTwoDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.service.LoctaionTypeTwoService;

public class LoctaionTypeTwoServiceImpl implements LoctaionTypeTwoService{
	
	private LocMstTwoDAO locMstTwoDAO = new LocMstTwoDAO();
	
	public boolean addLOC_TYPE_MST2(LOC_TYPE_MST2 lOC_TYPE_MST2)  throws Exception {
		boolean insertFlag = locMstTwoDAO.insertLocMst(lOC_TYPE_MST2);
		return insertFlag;
	}
	
	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2ById(String plant,String id) throws Exception {
		List<LOC_TYPE_MST2> doDetList = locMstTwoDAO.getLOC_TYPE_MST2ById(plant, id);
		return doDetList;
	}

	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2(String plant,String Location_Type_ID) throws Exception {
		List<LOC_TYPE_MST2> doDetList = locMstTwoDAO.getLOC_TYPE_MST2(plant, Location_Type_ID);
		return doDetList;
	}
	
	public boolean updateLOC_TYPE_MST2(LOC_TYPE_MST2 lOC_TYPE_MST2) throws Exception {
		boolean updateFlag = locMstTwoDAO.updateLOC_TYPE_MST2(lOC_TYPE_MST2);
		return updateFlag;
	}
	
	public boolean deleteLOC_TYPE_MST2(String plant,String id)  throws Exception {		
		return locMstTwoDAO.deleteLOC_TYPE_MST2(plant, id);
	}
}

