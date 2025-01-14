
/*created by navas*/

package com.track.service;

import java.util.Hashtable;
import java.util.List;

import com.track.db.object.estHdr;

public interface EstHDRService {
	public boolean addestHdr(estHdr esthdr) throws Exception;
	
	
	public List<estHdr> getestHdr(Hashtable ht, String afrmDate,
			String atoDate) throws Exception;
	
	public estHdr getestHdrById(String plant,String estno) throws Exception;
	
	public boolean updateestHdr(estHdr esthdr) throws Exception;
	
	
}
