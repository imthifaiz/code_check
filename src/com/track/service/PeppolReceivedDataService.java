package com.track.service;

import java.util.List;

import com.track.db.object.PEPPOL_RECEIVED_DATA;

public interface PeppolReceivedDataService {

	public int addPeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData) throws Exception;
	
	public int updatePeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData,String user,int id) throws Exception;
	
	public boolean DeletePeppolReceivedData(String plant,int id) throws Exception;
	
	public List<PEPPOL_RECEIVED_DATA> getAllPeppolReceivedData(String plant) throws Exception;
	
	public PEPPOL_RECEIVED_DATA getPeppolReceivedDataById(String plant,int id) throws Exception;

}
