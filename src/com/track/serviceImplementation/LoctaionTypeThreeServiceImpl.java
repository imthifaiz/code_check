package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.LocMstThreeDAO;
import com.track.db.object.LOC_TYPE_MST3;
import com.track.service.LoctaionTypeThreeService;

public class LoctaionTypeThreeServiceImpl implements LoctaionTypeThreeService{
	
	private LocMstThreeDAO locMstThreeDAO = new LocMstThreeDAO();
	
	public boolean addLOC_TYPE_MST3(LOC_TYPE_MST3 lOC_TYPE_MST3)  throws Exception {
		boolean insertFlag = locMstThreeDAO.insertLocMst(lOC_TYPE_MST3);
		return insertFlag;
	}
	
	public List<LOC_TYPE_MST3> getLOC_TYPE_MST3ById(String plant,String id) throws Exception {
		List<LOC_TYPE_MST3> doDetList = locMstThreeDAO.getLOC_TYPE_MST3ById(plant, id);
		return doDetList;
	}

	public List<LOC_TYPE_MST3> getLOC_TYPE_MST3(String plant,String Location_Type_ID) throws Exception {
		List<LOC_TYPE_MST3> doDetList = locMstThreeDAO.getLOC_TYPE_MST3(plant, Location_Type_ID);
		return doDetList;
	}
	
	public boolean updateLOC_TYPE_MST3(LOC_TYPE_MST3 lOC_TYPE_MST3) throws Exception {
		boolean updateFlag = locMstThreeDAO.updateLOC_TYPE_MST3(lOC_TYPE_MST3);
		return updateFlag;
	}
	
	public boolean deleteLOC_TYPE_MST3(String plant,String id)  throws Exception {		
		return locMstThreeDAO.deleteLOC_TYPE_MST3(plant, id);
	}
}

