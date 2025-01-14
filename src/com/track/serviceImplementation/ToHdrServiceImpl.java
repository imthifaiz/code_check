package com.track.serviceImplementation;

import java.util.Hashtable;
import java.util.List;

import com.track.dao.ToHdrDAO;
import com.track.db.object.ToHdr;
import com.track.service.ToHDRService;

public class ToHdrServiceImpl implements ToHDRService {
	private ToHdrDAO toHdrDAO = new ToHdrDAO();
	
	public boolean addToHdr(ToHdr toHdr) throws Exception {
		boolean insertFlag = toHdrDAO.insertToHdr(toHdr);
		return insertFlag;
	}
	
	
	public List<ToHdr> getToHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
		List<ToHdr> doHeaders = toHdrDAO.getToHdr(ht, afrmDate, atoDate);		
		return doHeaders;
	}
	//doubt azees bro
	public ToHdr getToHdrById(String plant,String tono) throws Exception{
		ToHdr toHdr = toHdrDAO.getToHdrById(plant, tono);
		return toHdr;
	}

	public boolean updateToHdr(ToHdr toHdr) throws Exception {
		boolean updateFlag = toHdrDAO.updateToHdr(toHdr);
		return updateFlag;
	}
	
	
}
