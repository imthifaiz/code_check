package com.track.service;

import java.util.List;

import com.track.db.object.ToDet;

public interface ToDetService {
	public boolean addToDet(List<ToDet> toDet) throws Exception;
	
	public List<ToDet> getToDetById(String plant,String tono) throws Exception;
	
	public boolean updateToDet(List<ToDet> toDet) throws Exception;
	
	
    public boolean isgetToDetById(String plant,String tono,int lnno, String item) throws Exception;
	
	public ToDet getToDetById(String plant,String tono,int lnno, String item) throws Exception;
}
