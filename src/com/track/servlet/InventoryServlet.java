package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.util.IMLogger;
import com.track.util.StrUtils;

@WebServlet("/inventory/*")
public class InventoryServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
//		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
		if(action.equalsIgnoreCase("valuationsummary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Inv_ValuationSummary.jsp?action=Inv_ValuationSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("salesforecasting")) {
					
					try {
						String msg = StrUtils.fString(request.getParameter("msg"));
						request.setAttribute("Msg", msg);
						RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesForecastingSummary.jsp?action=SalesForecastingSummary.jsp");
						rd.forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		
		if(action.equalsIgnoreCase("inventorysummary")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/inventorySummarys.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("minwithprtqty")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_Prnt_list.jsp");
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
//		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
//		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("reports")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/inventoryreports.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("sumqty")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryGroupByProdMultiUOM.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("sumqtybyproduct")) {
			
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryGroupMultiUOMByProd.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("inventorysummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/inventorySummarys.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("valuationsummary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/Inv_ValuationSummary.jsp?action=Inv_ValuationSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("costpriceinv")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/CostPriceAndInventoryReport.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("sumqtybatchpcs")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryGroupByProd.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("sumqtypcs")) {
			String msg= "";
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_list_withoutpriceqty.jsp");
			request.setAttribute("Msg", msg);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("sumbatchpcs")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_list_withoutpriceqtyMultiUOM.jsp");
		rd.forward(request, response);
	 } catch (Exception e) {
		e.printStackTrace();
	 }
   }
	
	if(action.equalsIgnoreCase("minqty")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_list.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
   }

	if(action.equalsIgnoreCase("minwithprtqty")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_Prnt_list.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("revenueshortage")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/Inv_RevenueShortageReport.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("expdate")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_list_withExpireDate.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
	
	if(action.equalsIgnoreCase("sumavlqty")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventorySummaryAvailableQty.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
    }
	
	if(action.equalsIgnoreCase("agingsum")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/inventoryAgingSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
	
	if(action.equalsIgnoreCase("sumavgcost")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_inv_list_withcost_cog.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
} 
	
	if(action.equalsIgnoreCase("openclosestock")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryOpenCloseStock.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

	if(action.equalsIgnoreCase("openclosestockavgcost")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryOpenCloseStockAvgCost.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("incomingoutgoingqty")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryIncomingOutgoingQty.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("incomingoutgoingqtydetail")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryIncomingOutgoingQtyDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("stkmovhis")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewInventoryMovHisDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salesforecasting")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesForecastingSummary.jsp?action=SalesForecastingSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		
}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
	}

}

		