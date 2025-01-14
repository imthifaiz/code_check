package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.DoDetDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.db.object.DoDet;
import com.track.service.DoDetService;

public class DoDetServiceImpl implements DoDetService{
	
	private DoDetDAO doDetDAO = new DoDetDAO();
	private DoTransferDetDAO doTransferDetDAO = new DoTransferDetDAO();
	
	public boolean addDoDet(List<DoDet> doDetList) throws Exception {
		boolean insertFlag = doDetDAO.insertDoDet(doDetList);
		return insertFlag;
	}
	
	public boolean addDoTransferDet(List<DoDet> doDetList) throws Exception {
		boolean insertFlag = doTransferDetDAO.insertDoDet(doDetList);
		return insertFlag;
	}

	public List<DoDet> getDoDetById(String plant, String dono) throws Exception {
		List<DoDet> doDetList = doDetDAO.getDoDetById(plant, dono);
		return doDetList;
	}
	
	public boolean updateDoDet(List<DoDet> doDetList) throws Exception {
		boolean updateFlag = doDetDAO.updateDoDet(doDetList);
		return updateFlag;
	}
	
	public boolean updateDoTransferDet(List<DoDet> doDetList) throws Exception {
		boolean updateFlag = doTransferDetDAO.updateDoDet(doDetList);
		return updateFlag;
	}

	@Override
	public boolean isgetDoDetById(String plant, String dono, int lnno, String item) throws Exception {
		return doDetDAO.isgetDoDetById(plant,dono,lnno,item);
	}

	@Override
	public DoDet getDoDetById(String plant, String dono, int lnno, String item) throws Exception {
		return doDetDAO.getDoDetById(plant, dono, lnno, item);
	}
	
	
	
	
}
