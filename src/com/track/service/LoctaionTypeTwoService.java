package com.track.service;

import java.util.List;

import com.track.db.object.LOC_TYPE_MST2;

public interface LoctaionTypeTwoService {
	public boolean addLOC_TYPE_MST2(LOC_TYPE_MST2 lOC_TYPE_MST2) throws Exception;
	
	
	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2ById(String plant,String id) throws Exception;
	
	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2(String plant,String Location_Type_ID) throws Exception;
	
	public boolean updateLOC_TYPE_MST2(LOC_TYPE_MST2 lOC_TYPE_MST2) throws Exception;

	public boolean deleteLOC_TYPE_MST2(String plant,String id) throws Exception;
}
