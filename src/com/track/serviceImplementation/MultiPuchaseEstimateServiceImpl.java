package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.multiPoEstDetDAO;
import com.track.dao.MultiPoEstHdrDAO;
import com.track.db.object.MultiPoEstDet;
import com.track.db.object.MultiPoEstDetRemarks ;
import com.track.db.object.MultiPoEstHdr;
import com.track.service.MultiPuchaseEstService;

public class MultiPuchaseEstimateServiceImpl implements MultiPuchaseEstService {
	
	private MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
	private multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();

	@Override
	public boolean addPoHdr(MultiPoEstHdr multiPoEstHdr) throws Exception {
		return multiPoEstHdrDAO.addMultiPoEstHdr(multiPoEstHdr);
	}

	@Override
	public boolean updatePoHdr(MultiPoEstHdr multiPoEstHdr) throws Exception {
		return multiPoEstHdrDAO.updatePoHdr(multiPoEstHdr);
	}

	@Override
	public MultiPoEstHdr getPoHdrByPono(String plant, String POMULTIESTNO) throws Exception {
		return multiPoEstHdrDAO.getPoHdrByPono(plant, POMULTIESTNO);		
	}

	@Override
	public boolean addPoDet(MultiPoEstDet multiPoEstDet) throws Exception {
		return MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstDet);
	}

	@Override
	public boolean updatePoDet(List<MultiPoEstDet> multiPoEstDet) throws Exception {
		return MultiPoEstDetDAO.updatePoDet(multiPoEstDet);
	}

	@Override
	public List<MultiPoEstDet> getPoDetByPono(String plant, String POMULTIESTNO) throws Exception {
		return MultiPoEstDetDAO.getPoDetByPono(plant, POMULTIESTNO);
	}

	@Override
	public boolean addPoDetRemarks(MultiPoEstDetRemarks  multiPoEstDetRemarks ) throws Exception {
		return MultiPoEstDetDAO.addPoDetRemarks(multiPoEstDetRemarks );
	}

	@Override
	public boolean updatePoDetRemarks(List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist) throws Exception {
		return MultiPoEstDetDAO.updatePoDetRemarks(multiPoEstDetRemarkslist);
	}

	@Override
	public List<MultiPoEstDetRemarks> getPoDetRemarksByPono(String plant, String POMULTIESTNO) throws Exception {
		return MultiPoEstDetDAO.getPoDetRemarksByPono(plant, POMULTIESTNO);
	}

}
