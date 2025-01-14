package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.ToDetDAO;
import com.track.db.object.ToDet;
import com.track.service.ToDetService;

public class ToDetServiceImpl implements ToDetService{
	
	private ToDetDAO toDetDAO = new ToDetDAO();
	
	public boolean addToDet(List<ToDet> toDetList) throws Exception {
		boolean insertFlag = toDetDAO.insertToDet(toDetList);
		return insertFlag;
	}

	public List<ToDet> getToDetById(String plant, String tono) throws Exception {
		List<ToDet> toDetList = toDetDAO.getToDetById(plant, tono);
		return toDetList;
	}
	
	public boolean updateToDet(List<ToDet> toDetList) throws Exception {
		boolean updateFlag = toDetDAO.updateToDet(toDetList);
		return updateFlag;
	}
	

	@Override
	public boolean isgetToDetById(String plant, String tono, int lnno, String item) throws Exception {
		return toDetDAO.isgetToDetById(plant,tono,lnno,item);
	}

	@Override
	public ToDet getToDetById(String plant, String tono, int lnno, String item) throws Exception {
		return toDetDAO.getToDetById(plant, tono, lnno, item);
	}
	
	
	
	
}
