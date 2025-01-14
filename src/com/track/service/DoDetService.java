package com.track.service;

import java.util.List;

import com.track.db.object.DoDet;

public interface DoDetService {
	public boolean addDoDet(List<DoDet> doDet) throws Exception;
	
	public boolean addDoTransferDet(List<DoDet> doDet) throws Exception;
	
	public List<DoDet> getDoDetById(String plant,String dono) throws Exception;
	
	public boolean updateDoDet(List<DoDet> doDet) throws Exception;
	
	public boolean updateDoTransferDet(List<DoDet> doDet) throws Exception;
	
    public boolean isgetDoDetById(String plant,String dono,int lnno, String item) throws Exception;
	
	public DoDet getDoDetById(String plant,String dono,int lnno, String item) throws Exception;
}
