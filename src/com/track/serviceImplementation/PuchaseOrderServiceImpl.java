package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoHdr;
import com.track.service.PuchaseOrderService;

public class PuchaseOrderServiceImpl implements PuchaseOrderService {
	
	private PoHdrDAO poHdrDAO = new PoHdrDAO();
	private PoDetDAO poDetDAO = new PoDetDAO();

	@Override
	public boolean addPoHdr(PoHdr poHdr) throws Exception {
		return poHdrDAO.addPoHdr(poHdr);
	}

	@Override
	public boolean updatePoHdr(PoHdr poHdr) throws Exception {
		return poHdrDAO.updatePoHdr(poHdr);
	}

	@Override
	public PoHdr getPoHdrByPono(String plant, String pono) throws Exception {
		return poHdrDAO.getPoHdrByPono(plant, pono);		
	}

	@Override
	public boolean addPoDet(PoDet PoDet) throws Exception {
		return poDetDAO.addPoDet(PoDet);
	}

	@Override
	public boolean updatePoDet(List<PoDet> PoDet) throws Exception {
		return poDetDAO.updatePoDet(PoDet);
	}

	@Override
	public List<PoDet> getPoDetByPono(String plant, String pono) throws Exception {
		return poDetDAO.getPoDetByPono(plant, pono);
	}

	@Override
	public boolean addPoDetRemarks(PoDetRemarks poDetRemarks) throws Exception {
		return poDetDAO.addPoDetRemarks(poDetRemarks);
	}

	@Override
	public boolean updatePoDetRemarks(List<PoDetRemarks> poDetRemarkslist) throws Exception {
		return poDetDAO.updatePoDetRemarks(poDetRemarkslist);
	}

	@Override
	public List<PoDetRemarks> getPoDetRemarksByPono(String plant, String pono) throws Exception {
		return poDetDAO.getPoDetRemarksByPono(plant, pono);
	}

}
