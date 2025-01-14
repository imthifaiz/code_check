package com.track.serviceImplementation;

import java.util.Hashtable;
import java.util.List;

import com.track.dao.FinProjectDAO;
import com.track.db.object.DoHdr;
import com.track.db.object.FinProject;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.service.ProjectService;

public class ProjectServiceImpl implements ProjectService{
	
	private FinProjectDAO finProjectDAO = new FinProjectDAO();
	
	public List<FinProject> getAllFinProject(String plant) throws Exception {
		List<FinProject> FinProjectList = finProjectDAO.getAllFinProject(plant);
		return FinProjectList;
	}
	
	public FinProject getFinProjectById(String plant, int id)throws Exception {
		FinProject finProject= finProjectDAO.getFinProjectById(plant,id);
		return finProject;
	}
	
	public FinProject getFinProjectByProject(String plant, String project)throws Exception {
		FinProject finProject= finProjectDAO.getFinProjectByProject(plant,project);
		return finProject;
	}
	
	public List<FinProject> dFinProjectlist(String plant, String projectname)throws Exception {
		List<FinProject> FinProjectList = finProjectDAO.dFinProjectlist(plant,projectname);
		return FinProjectList;
	}
	
	public List<FinProject> dropFinProjectlist(String plant, String projectname, String custno)throws Exception {
		List<FinProject> FinProjectList = finProjectDAO.dropFinProjectlist(plant,projectname,custno);
		return FinProjectList;
	}
	
	public List<FinProject> dFinProjectlistwithStatus(String plant, String projectname, String expDate)throws Exception {
		List<FinProject> FinProjectList = finProjectDAO.dFinProjectlistwithStatus(plant,projectname,expDate);
		return FinProjectList;
	}
	
	public List<FinProject> dropFinProjectlistwithStatus(String plant, String projectname, String custno, String expDate)throws Exception {
		List<FinProject> FinProjectList = finProjectDAO.dropFinProjectlistwithStatus(plant,projectname,custno,expDate);
		return FinProjectList;
	}

	public boolean addFinProject(FinProject finProject) throws Exception {
		boolean insertFlag = finProjectDAO.insertFinProject(finProject);
		return insertFlag;
	}
	
	
	public boolean updateFinProject(FinProject finProject) throws Exception {
		boolean insertFlag = finProjectDAO.updatefinProject(finProject);
		return insertFlag;
	}
	
	public boolean deleteFinProject(String plant,String project)  throws Exception {		
		return finProjectDAO.deleteFinProject(plant, project);
	}
	
	public List<FinProject> getProject(Hashtable ht, String afrmDate, String atoDate,String viewstatus) throws Exception {
		List<FinProject> doHeaders = finProjectDAO.getProject(ht, afrmDate, atoDate, viewstatus);		
		return doHeaders;
	}

	public FinProject getprojectByIdname(String plant,String project) throws Exception{
		FinProject finProject = finProjectDAO.getprojectByIdname(plant, project);
		return finProject;
	}

	}
	

