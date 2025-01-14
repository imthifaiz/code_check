package com.track.service;

import java.util.Hashtable;
import java.util.List;

import com.track.db.object.ToHdr;

public interface ToHDRService {
	public boolean addToHdr(ToHdr toHdr) throws Exception;
	
	public List<ToHdr> getToHdr(Hashtable ht, String afrmDate,
			String atoDate) throws Exception;
	
	public ToHdr getToHdrById(String plant,String tono) throws Exception;
	
	public boolean updateToHdr(ToHdr toHdr) throws Exception;
	
}
