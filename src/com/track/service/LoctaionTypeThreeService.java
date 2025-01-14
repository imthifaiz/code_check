package com.track.service;

import java.util.List;

import com.track.db.object.LOC_TYPE_MST3;

public interface LoctaionTypeThreeService {
	public boolean addLOC_TYPE_MST3(LOC_TYPE_MST3 lOC_TYPE_MST3) throws Exception;
	
	
	public List<LOC_TYPE_MST3> getLOC_TYPE_MST3ById(String plant,String id) throws Exception;
	
	public List<LOC_TYPE_MST3> getLOC_TYPE_MST3(String plant,String Location_Type_ID) throws Exception;
	
	public boolean updateLOC_TYPE_MST3(LOC_TYPE_MST3 lOC_TYPE_MST3) throws Exception;

	public boolean deleteLOC_TYPE_MST3(String plant,String id) throws Exception;
}
