package com.track.service;

import java.util.List;

import com.track.db.object.ParentChildCmpDet;

public interface ParentChildCmpDetService {

	public int addParentChildCmpDet(ParentChildCmpDet parentChildCmpDet) throws Exception;
	
	public List<ParentChildCmpDet> getAllParentChildCmpDet() throws Exception;
	
	public List<ParentChildCmpDet> getAllParentChildCmpDetdropdown(String parent, String child) throws Exception;
	
	public ParentChildCmpDet getParentChildCmpDetById(int id) throws Exception;
	
	public boolean DeleteParentChildCmpDet(int id) throws Exception;
	
	public boolean DeleteParentChildCmpDetByParent(String parent) throws Exception;
	
	public boolean IsParentChildCmpDet(String parent) throws Exception;

}
