package com.track.service;

import java.util.List;

import com.track.db.object.MultiPoEstDet;
import com.track.db.object.MultiPoEstDetRemarks;
import com.track.db.object.MultiPoEstHdr;

public interface MultiPuchaseEstService {

	public boolean addPoHdr(MultiPoEstHdr multiPoEstHdr) throws Exception;
	
	public boolean updatePoHdr(MultiPoEstHdr multiPoEstHdr) throws Exception;
	
	public MultiPoEstHdr getPoHdrByPono(String plant,String POMULTIESTNO) throws Exception;
	
	
	public boolean addPoDet(MultiPoEstDet multiPoEstDet) throws Exception;
	
	public boolean updatePoDet(List<MultiPoEstDet> multiPoEstDet) throws Exception;
	
	public  List<MultiPoEstDet> getPoDetByPono(String plant,String POMULTIESTNO) throws Exception;
	
	
	public boolean addPoDetRemarks(MultiPoEstDetRemarks  multiPoEstDetRemarks ) throws Exception;
	
	public boolean updatePoDetRemarks(List<MultiPoEstDetRemarks> multiPoEstDetRemarks) throws Exception;
	
	public List<MultiPoEstDetRemarks> getPoDetRemarksByPono(String plant,String POMULTIESTNO) throws Exception;
	
}
