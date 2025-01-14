package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.PeppolReceivedDataDAO;
import com.track.db.object.PEPPOL_RECEIVED_DATA;
import com.track.service.PeppolReceivedDataService;

public class PeppolReceivedDataServiceImpl implements PeppolReceivedDataService {
	
	private PeppolReceivedDataDAO peppolReceivedDataDAO = new PeppolReceivedDataDAO();

	@Override
	public int addPeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData) throws Exception {
		return peppolReceivedDataDAO.addPeppolReceivedData(PeppolReceivedData);
	}
	
	@Override
	public int updatePeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData, String user, int id ) throws Exception {
		return peppolReceivedDataDAO.updatePeppolReceivedData(PeppolReceivedData, user, id);
	}
	
	@Override
	public boolean DeletePeppolReceivedData(String plant, int id) throws Exception {
		return peppolReceivedDataDAO.DeletePeppolReceivedData(plant, id);
	}

	@Override
	public List<PEPPOL_RECEIVED_DATA> getAllPeppolReceivedData(String plant) throws Exception {
		return peppolReceivedDataDAO.getAllPeppolReceivedData(plant);
	}

	@Override
	public PEPPOL_RECEIVED_DATA getPeppolReceivedDataById(String plant,int id) throws Exception {
		return peppolReceivedDataDAO.getPeppolReceivedDataById(plant,id);
	}


}
