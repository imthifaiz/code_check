package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.PeppolDocIdsDAO;
import com.track.db.object.PEPPOL_DOC_IDS;
import com.track.service.PeppolDocIdsService;

public class PeppolDocIdsServiceImpl implements PeppolDocIdsService {
	
	private PeppolDocIdsDAO peppolDocIdsDAO = new PeppolDocIdsDAO();

	@Override
	public int addPeppolDoc(PEPPOL_DOC_IDS PeppolDocIds) throws Exception {
		return peppolDocIdsDAO.addPeppolDoc(PeppolDocIds);
	}
	
	@Override
	public int updatePeppolDoc(PEPPOL_DOC_IDS PeppolDocIds, String user, int id ) throws Exception {
		return peppolDocIdsDAO.updatePeppolDoc(PeppolDocIds, user, id);
	}
	
	@Override
	public boolean DeletePeppolDoc(String plant, int id) throws Exception {
		return peppolDocIdsDAO.DeletePeppolDoc(plant, id);
	}

	@Override
	public List<PEPPOL_DOC_IDS> getAllPeppolDoc() throws Exception {
		return peppolDocIdsDAO.getAllPeppolDoc();
	}

	@Override
	public PEPPOL_DOC_IDS getPeppolDocById(int id) throws Exception {
		return peppolDocIdsDAO.getPeppolDocById(id);
	}


}
