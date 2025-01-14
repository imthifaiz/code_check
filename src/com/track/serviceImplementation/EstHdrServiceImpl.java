/*created by RESVI*/
package com.track.serviceImplementation;

import java.util.Hashtable;
import java.util.List;


import com.track.dao.EstHdrDAO;
import com.track.db.object.estHdr;
import com.track.service.EstHDRService;

public  class EstHdrServiceImpl implements EstHDRService {
	private EstHdrDAO estHdrDAO = new EstHdrDAO();
	
	public boolean addestHdr(estHdr esthdr) throws Exception {
		boolean insertFlag = estHdrDAO.insertestHdr(esthdr);
		return insertFlag;
	}
	

	public List<estHdr> getestHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
		List<estHdr> doHeaders = estHdrDAO.getestHdr(ht, afrmDate, atoDate);		
		return doHeaders;
	}
	
	public estHdr getestHdrById(String plant,String dono) throws Exception{
		estHdr esthdr = estHdrDAO.getestHdrById(plant, dono);
		return esthdr;
	}

	public boolean updateestHdr(estHdr esthdr) throws Exception {
		boolean updateFlag = estHdrDAO.updateestHdr(esthdr);
		return updateFlag;
	}


}
