package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.ParentChildCmpDetDAO;
import com.track.db.object.ParentChildCmpDet;
import com.track.service.ParentChildCmpDetService;

public class ParentChildCmpDetServiceImpl implements ParentChildCmpDetService {
	
	private ParentChildCmpDetDAO parentChildCmpDetDAO = new ParentChildCmpDetDAO();

	@Override
	public int addParentChildCmpDet(ParentChildCmpDet parentChildCmpDet) throws Exception {
		return parentChildCmpDetDAO.addParentChildCmpDet(parentChildCmpDet);
	}

	@Override
	public List<ParentChildCmpDet> getAllParentChildCmpDet() throws Exception {
		return parentChildCmpDetDAO.getAllParentChildCmpDet();
	}

	@Override
	public List<ParentChildCmpDet> getAllParentChildCmpDetdropdown(String parent, String child) throws Exception {
		return parentChildCmpDetDAO.getAllParentChildCmpDetdropdown(parent, child);
	}

	@Override
	public ParentChildCmpDet getParentChildCmpDetById(int id) throws Exception {
		return parentChildCmpDetDAO.getParentChildCmpDetById(id);
	}

	@Override
	public boolean DeleteParentChildCmpDet(int id) throws Exception {
		return parentChildCmpDetDAO.DeleteParentChildCmpDet(id);
	}

	@Override
	public boolean IsParentChildCmpDet(String parent) throws Exception {
		return parentChildCmpDetDAO.IsParentChildCmpDet(parent);
	}

	@Override
	public boolean DeleteParentChildCmpDetByParent(String parent) throws Exception {
		return parentChildCmpDetDAO.DeleteParentChildCmpDetByParent(parent);
	}

}
