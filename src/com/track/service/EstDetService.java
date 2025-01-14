
package com.track.service;

import java.util.List;

import com.track.db.object.estDet;

public interface EstDetService {
	public boolean addestDet(List<estDet> estdet) throws Exception;
	
	public List<estDet> getestDetById(String plant,String dono) throws Exception;
	
	public boolean updateestDet(List<estDet> estDet) throws Exception;
	
	public estDet isgetestDetById(String plant,String estno,int lnno, String item) throws Exception;	
	
}
