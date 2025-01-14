package com.track.service;

import java.util.Hashtable;
import java.util.List;

import com.track.db.object.DoHdr;

public interface DoHDRService {
	public boolean addDoHdr(DoHdr doHdr) throws Exception;
	
	public boolean addDoTransferHdr(DoHdr doHdr) throws Exception;
	
	public List<DoHdr> getDoHdr(Hashtable ht, String afrmDate,
			String atoDate) throws Exception;
	
	public DoHdr getDoHdrById(String plant,String dono) throws Exception;
	
	public boolean updateDoHdr(DoHdr doHdr) throws Exception;
	
	public boolean updateDoTransferHdr(DoHdr doHdr) throws Exception;
}
