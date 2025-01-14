package com.track.service;

import java.util.List;

import com.track.db.object.PoEstDet;
import com.track.db.object.PoEstDetRemarks;
import com.track.db.object.PoEstHdr;

public interface PuchaseEstOrderService {

	public boolean addPoHdr(PoEstHdr poEstHdr) throws Exception;
	
	public boolean updatePoHdr(PoEstHdr poEstHdr) throws Exception;
	
	public PoEstHdr getPoHdrByPono(String plant,String pono) throws Exception;
	
	
	public boolean addPoDet(PoEstDet PoEstDet) throws Exception;
	
	public boolean updatePoDet(List<PoEstDet> PoEstDet) throws Exception;
	
	public  List<PoEstDet> getPoDetByPono(String plant,String pono) throws Exception;
	
	
	public boolean addPoDetRemarks(PoEstDetRemarks poEstDetRemarks) throws Exception;
	
	public boolean updatePoDetRemarks(List<PoEstDetRemarks> poEstDetRemarks) throws Exception;
	
	public List<PoEstDetRemarks> getPoDetRemarksByPono(String plant,String pono) throws Exception;

	
}
