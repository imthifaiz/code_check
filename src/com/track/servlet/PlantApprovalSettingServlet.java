package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.dao.PlantMstDAO;
import com.track.dao.PltApprovalMatrixDAO;
import com.track.db.object.PltApprovalMatrix;
import com.track.db.object.PltApprovalMatrixPojo;
import com.track.gates.DbBean;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

@WebServlet("/PlantApprovalSettings/*")
public class PlantApprovalSettingServlet extends HttpServlet implements IMLogger {
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		if (action.equalsIgnoreCase("create")) {
			PltApprovalMatrixDAO pltApprovalMatrixDAO = new PltApprovalMatrixDAO();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String	splant = request.getParameter("PLANTLIST");
				String	pocreate = request.getParameter("pocreate");
				String	poupdate = request.getParameter("poupdate");
				String	podelete = request.getParameter("podelete");
	 			String	porcreate = request.getParameter("porcreate");
	 			String	porupdate = request.getParameter("porupdate");
	 			String	pordelete = request.getParameter("pordelete");
	 			String	socreate = request.getParameter("socreate");
	 			String	soupdate = request.getParameter("soupdate");
	 			String	sodelete = request.getParameter("sodelete");
	 			String	sorcreate = request.getParameter("sorcreate");
	 			String	sorupdate = request.getParameter("sorupdate");
	 			String	sordelete = request.getParameter("sordelete");
	 			
	 			String	pid = request.getParameter("pid");
	 			String	prid = request.getParameter("prid");
	 			String	sid = request.getParameter("sid");
	 			String	srid = request.getParameter("srid");
	 			
				int	ipocreate  = 0;
				int	ipoupdate  = 0;
				int	ipodelete  = 0;
				int	iporcreate = 0;
				int	iporupdate = 0;
				int	ipordelete = 0;
				int	isocreate  = 0;
				int	isoupdate  = 0;
				int	isodelete  = 0;
				int	isorcreate = 0;
				int	isorupdate = 0;
				int	isordelete = 0;
				if (pocreate != null) {
					if (pocreate.equalsIgnoreCase("on")) {
						ipocreate = 1;
					}
				}
				if (poupdate != null) {
					if (poupdate.equalsIgnoreCase("on")) {
						ipoupdate = 1;
					}
				}
				if (podelete != null) {
					if (podelete.equalsIgnoreCase("on")) {
						ipodelete = 1;
					}
				}
				if (porcreate != null) {
					if (porcreate.equalsIgnoreCase("on")) {
						iporcreate = 1;
					}
				}
				if (porupdate != null) {
					if (porupdate.equalsIgnoreCase("on")) {
						iporupdate = 1;
					}
				}
				if (pordelete != null) {
					if (pordelete.equalsIgnoreCase("on")) {
						ipordelete = 1;
					}
				}
				if (socreate != null) {
					if (socreate.equalsIgnoreCase("on")) {
						isocreate = 1;
					}
				}
				if (soupdate != null) {
					if (soupdate.equalsIgnoreCase("on")) {
						isoupdate = 1;
					}
				}
				if (sodelete != null) {
					if (sodelete.equalsIgnoreCase("on")) {
						isodelete = 1;
					}
				}
				if (sorcreate != null) {
					if (sorcreate.equalsIgnoreCase("on")) {
						isorcreate = 1;
					}
				}
				if (sorupdate != null) {
					if (sorupdate.equalsIgnoreCase("on")) {
						isorupdate = 1;
					}
				}
				if (sordelete != null) {
					if (sordelete.equalsIgnoreCase("on")) {
						isordelete = 1;
					}
				}

				if (pid.equalsIgnoreCase("0")) {
					PltApprovalMatrix purchasematrix = new PltApprovalMatrix();
					purchasematrix.setPLANT(splant);
					purchasematrix.setAPPROVALTYPE("PURCHASE");
					purchasematrix.setISCREATE((short) ipocreate);
					purchasematrix.setISUPDATE((short) ipoupdate);
					purchasematrix.setISDELETE((short) ipodelete);
					purchasematrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(purchasematrix);
				} else {
					PltApprovalMatrix purchasematrix = new PltApprovalMatrix();
					purchasematrix.setPLANT(splant);
					purchasematrix.setAPPROVALTYPE("PURCHASE");
					purchasematrix.setISCREATE((short) ipocreate);
					purchasematrix.setISUPDATE((short) ipoupdate);
					purchasematrix.setISDELETE((short) ipodelete);
					purchasematrix.setUPBY(username);
					purchasematrix.setID(Integer.valueOf(pid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(purchasematrix, username);
				}
				if (prid.equalsIgnoreCase("0")) {
					PltApprovalMatrix purchasereturnmatrix = new PltApprovalMatrix();
					purchasereturnmatrix.setPLANT(splant);
					purchasereturnmatrix.setAPPROVALTYPE("PURCHASE RETURN");
					purchasereturnmatrix.setISCREATE((short) iporcreate);
					purchasereturnmatrix.setISUPDATE((short) iporupdate);
					purchasereturnmatrix.setISDELETE((short) ipordelete);
					purchasereturnmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(purchasereturnmatrix);
				} else {
					PltApprovalMatrix purchasereturnmatrix = new PltApprovalMatrix();
					purchasereturnmatrix.setPLANT(splant);
					purchasereturnmatrix.setAPPROVALTYPE("PURCHASE RETURN");
					purchasereturnmatrix.setISCREATE((short) iporcreate);
					purchasereturnmatrix.setISUPDATE((short) iporupdate);
					purchasereturnmatrix.setISDELETE((short) ipordelete);
					purchasereturnmatrix.setUPBY(username);
					purchasereturnmatrix.setID(Integer.valueOf(prid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(purchasereturnmatrix, username);
				}
				if (sid.equalsIgnoreCase("0")) {
					PltApprovalMatrix salesmatrix = new PltApprovalMatrix();
					salesmatrix.setPLANT(splant);
					salesmatrix.setAPPROVALTYPE("SALES");
					salesmatrix.setISCREATE((short) isocreate);
					salesmatrix.setISUPDATE((short) isoupdate);
					salesmatrix.setISDELETE((short) isodelete);
					salesmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(salesmatrix);
					
				} else {
					PltApprovalMatrix salesmatrix = new PltApprovalMatrix();
					salesmatrix.setPLANT(splant);
					salesmatrix.setAPPROVALTYPE("SALES");
					salesmatrix.setISCREATE((short) isocreate);
					salesmatrix.setISUPDATE((short) isoupdate);
					salesmatrix.setISDELETE((short) isodelete);
					salesmatrix.setUPBY(username);
					salesmatrix.setID(Integer.valueOf(sid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(salesmatrix, username);
				}
				if (srid.equalsIgnoreCase("0")) {
					PltApprovalMatrix salesreturnmatrix = new PltApprovalMatrix();
					salesreturnmatrix.setPLANT(splant);
					salesreturnmatrix.setAPPROVALTYPE("SALES RETURN");
					salesreturnmatrix.setISCREATE((short) isorcreate);
					salesreturnmatrix.setISUPDATE((short) isorupdate);
					salesreturnmatrix.setISDELETE((short) isordelete);
					salesreturnmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(salesreturnmatrix);
					
				} else {
					PltApprovalMatrix salesreturnmatrix = new PltApprovalMatrix();
					salesreturnmatrix.setPLANT(splant);
					salesreturnmatrix.setAPPROVALTYPE("SALES RETURN");
					salesreturnmatrix.setISCREATE((short) isorcreate);
					salesreturnmatrix.setISUPDATE((short) isorupdate);
					salesreturnmatrix.setISDELETE((short) isordelete);
					salesreturnmatrix.setUPBY(username);
					salesreturnmatrix.setID(Integer.valueOf(srid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(salesreturnmatrix, username);
				}
	 			
	 			DbBean.CommitTran(ut);
				request.setAttribute("eMsg", "");
				request.setAttribute("sMsg","Plant Approval Settings Added Successfully");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PlantApprovalSettingSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				request.setAttribute("sMsg", "");
				request.setAttribute("eMsg","Unable To Add Plant Approval Settings");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PlantApprovalSettingSummary.jsp");
			}
		}
		//IMTI added on 18-03-2022  
		else if (action.equalsIgnoreCase("update")) {
			PltApprovalMatrixDAO pltApprovalMatrixDAO = new PltApprovalMatrixDAO();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String	splant = request.getParameter("plant");
				String	pocreate = request.getParameter("pocreate");
				String	poupdate = request.getParameter("poupdate");
				String	podelete = request.getParameter("podelete");
	 			String	porcreate = request.getParameter("porcreate");
	 			String	porupdate = request.getParameter("porupdate");
	 			String	pordelete = request.getParameter("pordelete");
	 			String	socreate = request.getParameter("socreate");
	 			String	soupdate = request.getParameter("soupdate");
	 			String	sodelete = request.getParameter("sodelete");
	 			String	sorcreate = request.getParameter("sorcreate");
	 			String	sorupdate = request.getParameter("sorupdate");
	 			String	sordelete = request.getParameter("sordelete");
	 			
	 			String	pid = request.getParameter("pid");
	 			String	prid = request.getParameter("prid");
	 			String	sid = request.getParameter("sid");
	 			String	srid = request.getParameter("srid");
	 			
				int	ipocreate  = 0;
				int	ipoupdate  = 0;
				int	ipodelete  = 0;
				int	iporcreate = 0;
				int	iporupdate = 0;
				int	ipordelete = 0;
				int	isocreate  = 0;
				int	isoupdate  = 0;
				int	isodelete  = 0;
				int	isorcreate = 0;
				int	isorupdate = 0;
				int	isordelete = 0;
				if (pocreate != null) {
					if (pocreate.equalsIgnoreCase("on")) {
						ipocreate = 1;
					}
				}
				if (poupdate != null) {
					if (poupdate.equalsIgnoreCase("on")) {
						ipoupdate = 1;
					}
				}
				if (podelete != null) {
					if (podelete.equalsIgnoreCase("on")) {
						ipodelete = 1;
					}
				}
				if (porcreate != null) {
					if (porcreate.equalsIgnoreCase("on")) {
						iporcreate = 1;
					}
				}
				if (porupdate != null) {
					if (porupdate.equalsIgnoreCase("on")) {
						iporupdate = 1;
					}
				}
				if (pordelete != null) {
					if (pordelete.equalsIgnoreCase("on")) {
						ipordelete = 1;
					}
				}
				if (socreate != null) {
					if (socreate.equalsIgnoreCase("on")) {
						isocreate = 1;
					}
				}
				if (soupdate != null) {
					if (soupdate.equalsIgnoreCase("on")) {
						isoupdate = 1;
					}
				}
				if (sodelete != null) {
					if (sodelete.equalsIgnoreCase("on")) {
						isodelete = 1;
					}
				}
				if (sorcreate != null) {
					if (sorcreate.equalsIgnoreCase("on")) {
						isorcreate = 1;
					}
				}
				if (sorupdate != null) {
					if (sorupdate.equalsIgnoreCase("on")) {
						isorupdate = 1;
					}
				}
				if (sordelete != null) {
					if (sordelete.equalsIgnoreCase("on")) {
						isordelete = 1;
					}
				}

				if (pid.equalsIgnoreCase("0")) {
					PltApprovalMatrix purchasematrix = new PltApprovalMatrix();
					purchasematrix.setPLANT(splant);
					purchasematrix.setAPPROVALTYPE("PURCHASE");
					purchasematrix.setISCREATE((short) ipocreate);
					purchasematrix.setISUPDATE((short) ipoupdate);
					purchasematrix.setISDELETE((short) ipodelete);
					purchasematrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(purchasematrix);
				} else {
					PltApprovalMatrix purchasematrix = new PltApprovalMatrix();
					purchasematrix.setPLANT(splant);
					purchasematrix.setAPPROVALTYPE("PURCHASE");
					purchasematrix.setISCREATE((short) ipocreate);
					purchasematrix.setISUPDATE((short) ipoupdate);
					purchasematrix.setISDELETE((short) ipodelete);
					purchasematrix.setUPBY(username);
					purchasematrix.setID(Integer.valueOf(pid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(purchasematrix, username);
				}
				if (prid.equalsIgnoreCase("0")) {
					PltApprovalMatrix purchasereturnmatrix = new PltApprovalMatrix();
					purchasereturnmatrix.setPLANT(splant);
					purchasereturnmatrix.setAPPROVALTYPE("PURCHASE RETURN");
					purchasereturnmatrix.setISCREATE((short) iporcreate);
					purchasereturnmatrix.setISUPDATE((short) iporupdate);
					purchasereturnmatrix.setISDELETE((short) ipordelete);
					purchasereturnmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(purchasereturnmatrix);
				} else {
					PltApprovalMatrix purchasereturnmatrix = new PltApprovalMatrix();
					purchasereturnmatrix.setPLANT(splant);
					purchasereturnmatrix.setAPPROVALTYPE("PURCHASE RETURN");
					purchasereturnmatrix.setISCREATE((short) iporcreate);
					purchasereturnmatrix.setISUPDATE((short) iporupdate);
					purchasereturnmatrix.setISDELETE((short) ipordelete);
					purchasereturnmatrix.setUPBY(username);
					purchasereturnmatrix.setID(Integer.valueOf(prid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(purchasereturnmatrix, username);
				}
				if (sid.equalsIgnoreCase("0")) {
					PltApprovalMatrix salesmatrix = new PltApprovalMatrix();
					salesmatrix.setPLANT(splant);
					salesmatrix.setAPPROVALTYPE("SALES");
					salesmatrix.setISCREATE((short) isocreate);
					salesmatrix.setISUPDATE((short) isoupdate);
					salesmatrix.setISDELETE((short) isodelete);
					salesmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(salesmatrix);
					
				} else {
					PltApprovalMatrix salesmatrix = new PltApprovalMatrix();
					salesmatrix.setPLANT(splant);
					salesmatrix.setAPPROVALTYPE("SALES");
					salesmatrix.setISCREATE((short) isocreate);
					salesmatrix.setISUPDATE((short) isoupdate);
					salesmatrix.setISDELETE((short) isodelete);
					salesmatrix.setUPBY(username);
					salesmatrix.setID(Integer.valueOf(sid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(salesmatrix, username);
				}
				if (srid.equalsIgnoreCase("0")) {
					PltApprovalMatrix salesreturnmatrix = new PltApprovalMatrix();
					salesreturnmatrix.setPLANT(splant);
					salesreturnmatrix.setAPPROVALTYPE("SALES RETURN");
					salesreturnmatrix.setISCREATE((short) isorcreate);
					salesreturnmatrix.setISUPDATE((short) isorupdate);
					salesreturnmatrix.setISDELETE((short) isordelete);
					salesreturnmatrix.setCRBY(username);
					pltApprovalMatrixDAO.addPltApprovalMatrix(salesreturnmatrix);
					
				} else {
					PltApprovalMatrix salesreturnmatrix = new PltApprovalMatrix();
					salesreturnmatrix.setPLANT(splant);
					salesreturnmatrix.setAPPROVALTYPE("SALES RETURN");
					salesreturnmatrix.setISCREATE((short) isorcreate);
					salesreturnmatrix.setISUPDATE((short) isorupdate);
					salesreturnmatrix.setISDELETE((short) isordelete);
					salesreturnmatrix.setUPBY(username);
					salesreturnmatrix.setID(Integer.valueOf(srid));
					pltApprovalMatrixDAO.updatePltApprovalMatrix(salesreturnmatrix, username);
				}
	 			
	 			DbBean.CommitTran(ut);
				request.setAttribute("eMsg", "");
				request.setAttribute("sMsg","Manage Workflow Updated Successfully");
				response.sendRedirect("../PlantApprovalSettings/workflow?result=Manage Workflow Updated Successfully");
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				request.setAttribute("sMsg", "");
				request.setAttribute("eMsg","Unable To Update Manage Workflow");
				response.sendRedirect("../PlantApprovalSettings/workflow?result=Unable To Update Manage Workflow");
			}
		}
		//END
		else if(action.equalsIgnoreCase("getpltApproveMatrixbyplant"))
		{
			PltApprovalMatrixDAO pltApprovalMatrixDAO = new PltApprovalMatrixDAO();
			JSONObject pltappmatx = new JSONObject();
			try {
				
				String splant = StrUtils.fString(request.getParameter("plant"));
				List<PltApprovalMatrix> Appmatrix = pltApprovalMatrixDAO.getPltApprovalMatrixByplt(splant);
				if(Appmatrix.size() > 0) {
					pltappmatx.put("appmax",Appmatrix);
					pltappmatx.put("status","ok");
				}else {
					pltappmatx.put("status","not ok");
				}
			} catch (Exception e) {
				pltappmatx.put("status","not ok");
				e.printStackTrace();
			}
			response.getWriter().write(pltappmatx.toString());
		}else if(action.equalsIgnoreCase("getallpltApproveMatrix"))
		{
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			PltApprovalMatrixDAO pltApprovalMatrixDAO = new PltApprovalMatrixDAO();
			JSONObject pltappmatx = new JSONObject();
			try {
				List<PltApprovalMatrixPojo> PltApprovalMatrixPojolist = new ArrayList<PltApprovalMatrixPojo>();
				List<PltApprovalMatrix> Appmatrix = pltApprovalMatrixDAO.getAllPltApprovalMatrix();
				for (PltApprovalMatrix appmatrix : Appmatrix) {
					PltApprovalMatrixPojo pltApprovalMatrixPojo = new PltApprovalMatrixPojo();
					pltApprovalMatrixPojo.setID(appmatrix.getID());
					pltApprovalMatrixPojo.setPLANT(appmatrix.getPLANT());
					pltApprovalMatrixPojo.setCNAME(plantMstDAO.getcmpyname(appmatrix.getPLANT()));
					pltApprovalMatrixPojo.setAPPROVALTYPE(appmatrix.getAPPROVALTYPE());
					pltApprovalMatrixPojo.setISCREATE(appmatrix.getISCREATE());
					pltApprovalMatrixPojo.setISUPDATE(appmatrix.getISUPDATE());
					pltApprovalMatrixPojo.setISDELETE(appmatrix.getISDELETE());
					pltApprovalMatrixPojo.setCRAT(appmatrix.getCRAT());
					pltApprovalMatrixPojo.setCRBY(appmatrix.getCRBY());
					pltApprovalMatrixPojo.setUPAT(appmatrix.getUPAT());
					pltApprovalMatrixPojo.setUPBY(appmatrix.getUPBY());
					PltApprovalMatrixPojolist.add(pltApprovalMatrixPojo);
				}
				
				
				
				pltappmatx.put("data",PltApprovalMatrixPojolist);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write(pltappmatx.toString());
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));

		if (action.equalsIgnoreCase("summary")) {

			try {
				request.setAttribute("sMsg", "");
				request.setAttribute("eMsg", "");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PlantApprovalSettingSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("new")) {

			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createPlantApprovalMatrix.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// IMTI added on 18-03-2022  
		if (action.equalsIgnoreCase("workflow")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String smsg = "",emsg = "";
				smsg = (String)request.getAttribute("sMsg");
				emsg = (String)request.getAttribute("eMsg");
					if(smsg==null) { 
						smsg=""; 
					}
					if(emsg==null) { 
						emsg=""; 
					}
				request.setAttribute("Msg", msg);
				request.setAttribute("sMsg", smsg);
				request.setAttribute("eMsg", emsg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ManageWorkflow.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//END
		
		
		if (action.equalsIgnoreCase("delete")) {
			PltApprovalMatrixDAO pltApprovalMatrixDAO = new PltApprovalMatrixDAO();
			String splant = StrUtils.fString(request.getParameter("SPLANT"));
			try {
				boolean check = pltApprovalMatrixDAO.DeletePltApprovalMatrix(splant);
				if(check) {
					request.setAttribute("sMsg", splant+" Deleted Successfully");
					request.setAttribute("eMsg", "");
					
				}else {
					request.setAttribute("sMsg", "");
					request.setAttribute("eMsg",splant+" Unable To Delete");
				}
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PlantApprovalSettingSummary.jsp");
				rd.forward(request, response);
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("sMsg", "");
				request.setAttribute("eMsg",splant+" Unable To Delete");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PlantApprovalSettingSummary.jsp");
			}
		}
		
		

	}

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}
	
	

}
