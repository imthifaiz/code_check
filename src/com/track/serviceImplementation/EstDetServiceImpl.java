/*created by navas*/
package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.EstDetDAO;
import com.track.db.object.estDet;
import com.track.service.EstDetService;

public class EstDetServiceImpl implements EstDetService{
	private EstDetDAO estDetDAO = new EstDetDAO();

	public boolean addestDet(List<estDet> estdetList) throws Exception {
		boolean insertFlag = estDetDAO.insertestDet(estdetList);
		return insertFlag;
	}

	public List<estDet> getestDetById(String plant, String dono) throws Exception {
		List<estDet> estdetList = estDetDAO.getestDetById(plant, dono);
		return estdetList;
	}
	
	public boolean updateestDet(List<estDet> estdetList) throws Exception {
		boolean updateFlag = estDetDAO.updateestDet(estdetList);
		return updateFlag;
	}
	
	@Override
	public estDet isgetestDetById(String plant, String estno, int lnno, String item) throws Exception {
		return estDetDAO.isgetEstDetById(plant,estno,lnno,item);
	}

}

