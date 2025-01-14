package com.track.serviceImplementation;

import java.util.Hashtable;
import java.util.List;

import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.db.object.DoHdr;
import com.track.service.DoHDRService;

public class DoHdrServiceImpl implements DoHDRService {
	private DoHdrDAO doHdrDAO = new DoHdrDAO();
	private DoTransferHdrDAO doTransferHdrDAO = new DoTransferHdrDAO();
	
	public boolean addDoHdr(DoHdr doHdr) throws Exception {
		boolean insertFlag = doHdrDAO.insertDoHdr(doHdr);
		return insertFlag;
	}
	
	public boolean addDoTransferHdr(DoHdr doHdr) throws Exception {
		boolean insertFlag = doTransferHdrDAO.insertDoTransferHdr(doHdr);
		return insertFlag;
	}

	public List<DoHdr> getDoHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
		List<DoHdr> doHeaders = doHdrDAO.getDoHdr(ht, afrmDate, atoDate);		
		return doHeaders;
	}
	
	public DoHdr getDoHdrById(String plant,String dono) throws Exception{
		DoHdr doHdr = doHdrDAO.getDoHdrById(plant, dono);
		return doHdr;
	}

	public boolean updateDoHdr(DoHdr doHdr) throws Exception {
		boolean updateFlag = doHdrDAO.updateDoHdr(doHdr);
		return updateFlag;
	}
	
	public boolean updateDoTransferHdr(DoHdr doHdr) throws Exception {
		boolean updateFlag = doTransferHdrDAO.updateDoTransferHdr(doHdr);
		return updateFlag;
	}
}
