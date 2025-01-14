package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.PoEstDetDAO;
import com.track.dao.PoEstHdrDAO;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoEstDet;
import com.track.db.object.PoEstDetRemarks;
import com.track.db.object.PoEstHdr;
import com.track.db.object.PoHdr;
import com.track.service.PuchaseEstOrderService;

public class PuchaseEstOrderServiceImpl implements PuchaseEstOrderService {
	
	private PoEstHdrDAO poEstHdrDAO = new PoEstHdrDAO();
	private PoEstDetDAO poEstDetDAO = new PoEstDetDAO();

	@Override
	public boolean addPoHdr(PoEstHdr poEstHdr) throws Exception {
		return poEstHdrDAO.addPoHdr(poEstHdr);
	}

	@Override
	public boolean updatePoHdr(PoEstHdr poEstHdr) throws Exception {
		return poEstHdrDAO.updatePoHdr(poEstHdr);
	}

	@Override
	public PoEstHdr getPoHdrByPono(String plant, String pono) throws Exception {
		return poEstHdrDAO.getPoHdrByPono(plant, pono);		
	}

	@Override
	public boolean addPoDet(PoEstDet PoEstDet) throws Exception {
		return poEstDetDAO.addPoDet(PoEstDet);
	}

	@Override
	public boolean updatePoDet(List<PoEstDet> PoEstDet) throws Exception {
		return poEstDetDAO.updatePoDet(PoEstDet);
	}

	@Override
	public List<PoEstDet> getPoDetByPono(String plant, String pono) throws Exception {
		return poEstDetDAO.getPoDetByPono(plant, pono);
	}

	@Override
	public boolean addPoDetRemarks(PoEstDetRemarks poEstDetRemarks) throws Exception {
		return poEstDetDAO.addPoDetRemarks(poEstDetRemarks);
	}

	@Override
	public boolean updatePoDetRemarks(List<PoEstDetRemarks> poEstDetRemarkslist) throws Exception {
		return poEstDetDAO.updatePoDetRemarks(poEstDetRemarkslist);
	}

	@Override
	public List<PoEstDetRemarks> getPoDetRemarksByPono(String plant, String pono) throws Exception {
		return poEstDetDAO.getPoDetRemarksByPono(plant, pono);
	}

	
}
