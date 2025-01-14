package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.track.util.IMLogger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/productclass/*")
public class ProductClassServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/prdclsSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/prdclsSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("new")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String result = StrUtils.fString(request.getParameter("result"));
			request.setAttribute("Msg", msg);
			request.setAttribute("result", result);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/prdClsMst.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	else if (action.equalsIgnoreCase("Auto_ID")) {
		String sItemId = "" ;
		TblControlDAO _TblControlDAO = new TblControlDAO();
		JSONObject json = new JSONObject();
		try {
		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		StrUtils strUtils = new StrUtils();
		Hashtable ht = new Hashtable();
		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						"PRDCLASS");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PC");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(
						htTblCntInsert, plant);

				sItemId = "PC" + "001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq
						.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "00";
				} else if (updatedSeq.length() == 2) {
					sZero = "0";
				}

				//System.out.print("..................................."+rtnBatch);
				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						"PRDCLASS");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PC");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);

				sItemId = "PC" + sZero + updatedSeq;
			}
			json.put("PRDCLASS", sItemId);
			response.setStatus(200);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	if(action.equalsIgnoreCase("detail")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/prdClsDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("edit")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String result = StrUtils.fString(request.getParameter("result"));
			request.setAttribute("Msg", msg);
			request.setAttribute("result", result);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_prdcls.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
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

		