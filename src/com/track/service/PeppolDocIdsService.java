package com.track.service;

import java.util.List;

import com.track.db.object.PEPPOL_DOC_IDS;

public interface PeppolDocIdsService {

	public int addPeppolDoc(PEPPOL_DOC_IDS PeppolDocIds) throws Exception;
	
	public int updatePeppolDoc(PEPPOL_DOC_IDS PeppolDocIds,String user,int id) throws Exception;
	
	public boolean DeletePeppolDoc(String plant,int id) throws Exception;
	
	public List<PEPPOL_DOC_IDS> getAllPeppolDoc() throws Exception;
	
	public PEPPOL_DOC_IDS getPeppolDocById(int id) throws Exception;

}
