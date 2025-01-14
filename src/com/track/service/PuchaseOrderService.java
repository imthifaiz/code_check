package com.track.service;

import java.util.List;

import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoHdr;

public interface PuchaseOrderService {

	public boolean addPoHdr(PoHdr poHdr) throws Exception;
	
	public boolean updatePoHdr(PoHdr poHdr) throws Exception;
	
	public PoHdr getPoHdrByPono(String plant,String pono) throws Exception;
	
	
	public boolean addPoDet(PoDet PoDet) throws Exception;
	
	public boolean updatePoDet(List<PoDet> PoDet) throws Exception;
	
	public  List<PoDet> getPoDetByPono(String plant,String pono) throws Exception;
	
	
	public boolean addPoDetRemarks(PoDetRemarks poDetRemarks) throws Exception;
	
	public boolean updatePoDetRemarks(List<PoDetRemarks> poDetRemarks) throws Exception;
	
	public List<PoDetRemarks> getPoDetRemarksByPono(String plant,String pono) throws Exception;
	
}
