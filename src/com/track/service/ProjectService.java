package com.track.service;

import java.util.Hashtable;
import java.util.List;

import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.FinProject;

public interface ProjectService {

	public List<FinProject> getAllFinProject(String plant) throws Exception;
	
	public FinProject getFinProjectById(String plant, int id)throws Exception;
	
	public FinProject getFinProjectByProject(String plant, String project)throws Exception;
	
	public List<FinProject> dFinProjectlist(String plant, String projectname)throws Exception;
	
	public List<FinProject> dropFinProjectlist(String plant, String projectname, String custno)throws Exception;
	
	public List<FinProject> dFinProjectlistwithStatus(String plant, String projectname, String expDate)throws Exception;
	
	public List<FinProject> dropFinProjectlistwithStatus(String plant, String projectname, String custno, String expDate)throws Exception;

	public boolean addFinProject(FinProject finProject) throws Exception;
	
	public boolean updateFinProject(FinProject finProject) throws Exception;
	
	public boolean deleteFinProject(String plant,String project) throws Exception;
	
	public List<FinProject> getProject(Hashtable ht, String afrmDate,String atoDate,String viewstatus) throws Exception;
	
	public FinProject getprojectByIdname(String plant,String project) throws Exception;

}
