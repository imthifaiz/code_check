package com.track.db.util;

import com.track.constants.IConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.DoHdrDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ToHdrDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsOrderClose;
import com.track.tran.WmsOrderDetailsClose;
import com.track.tran.WmsReceiveMaterial;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.XMLUtils;

import javax.transaction.UserTransaction;

public class OrderTypeUtil {
	public OrderTypeUtil() {
	}

	private MLogger mLogger = new MLogger();
	private OrderTypeBeanDAO _OrderTypeBeanDAO= new OrderTypeBeanDAO();
	private OrderTypeDAO _OrderTypeDAO = new OrderTypeDAO();
	private boolean printLog = MLoggerConstant.OrderType_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean addOrderType(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			_OrderTypeBeanDAO.setmLogger(mLogger);
			flag = _OrderTypeBeanDAO.insertIntoOrderType(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return flag;
	}

	public ArrayList getOrderTypeDetails(String OrderType) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getOrderTypeDetails("ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS",
					htCondition, " and ORDERTYPE like ='" + OrderType + "%'"
							+ "ORDER BY ORDERTYPE  ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getOrderTypeDetails(String orderType, String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getOrderTypeDetails(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS", htCondition,
					" and orderType like '" + orderType + "%'" + " ORDER BY ORDERTYPE ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
	public ArrayList getTypeDetails(String Type, String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getOrderTypeDetails(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS", htCondition,
					" and ISACTIVE='Y'  and Type like '" + Type + "'" + " ORDER BY ORDERTYPE ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getAllOrderTypeDetails() throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", "SIS");

		try {
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getAllOrderTypeDetails("ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS",
					htCondition, " ORDER BY ORDERTYPE ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public ArrayList getAllOrderTypeDetails(String plant, String cond)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);
		//htCondition.put("ISACTIVE", "Y");
		try {
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getAllOrderTypeDetails(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS", htCondition, cond
							+ "   ORDER BY ORDERTYPE ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getOrderTypeListDetails(String plant, String orderType)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);

		try {
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getOrderTypeList(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS",
					htCondition, orderType);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public boolean updateOrderType(Hashtable htValues, Hashtable htCondition,
			String extCond) throws Exception {
		boolean flag = false;
		try {
			StringBuffer sb = new StringBuffer(" set ");
			sb.append("ORDERDESC='" + htValues.get("ORDERDESC") + "'");
			//sb.append(",TYPE='" + htValues.get("TYPE") + "'");
			sb.append(",ISACTIVE='" + htValues.get("ISACTIVE") + "'");
			sb.append(",REMARKS='" + htValues.get("REMARKS") + "'");
			_OrderTypeBeanDAO.setmLogger(mLogger);

			flag = _OrderTypeBeanDAO.updateOrderType(sb.toString(), htCondition, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	
	
	public boolean deleteOrderType(String orderType,String type,String plant)throws Exception {
		boolean flag = false;
		try {
					
		    _OrderTypeBeanDAO.setmLogger(mLogger);
			flag=_OrderTypeBeanDAO.deleteOrderType(orderType,type,plant);
	
		} catch (Exception e) {
	
			throw e;
		}
		return flag;
	   }
           
           
    public boolean process_OrderClose(Map obj) throws Exception {
            boolean flag = false;
                    UserTransaction ut = null;
            try {
                   
                ut = com.track.gates.DbBean.getUserTranaction();
                ut.begin();
                  
                this.mLogger.auditInfo(SConstant.PRINTFLAG, "ORDER CLOSE "
                                + " :: ORDERS TO CLOSE : "
                                + (String) obj.get("ORDERS") + " :: "
                                +  " MODULE : "
                                + (String) obj.get("MODULE_NAME")  +  " REMARKS : "
                                + (String) obj.get("REMARKS")) ;

                    flag = this.process_Wms_OrderClose(obj);
                    this.mLogger.auditInfo(SConstant.PRINTFLAG, "ORDER CLOSE "
                                    + " :: END : Transction returned : " + flag);

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            flag = true;
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                    }

                } catch (Exception e) {
                      DbBean.RollbackTran(ut);
                      flag = false;
                      throw e;
                     
                }
                      return flag;
                }    
           
           
    private boolean process_Wms_OrderClose(Map map) throws Exception {

            boolean flag = false;

            WmsTran tran = new WmsOrderClose();
            ((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
            flag = tran.processWmsTran(map);

            return flag;
    }
    
    
    public boolean process_OrderCloseItems(Map obj) throws Exception {
            boolean flag = false;
                    UserTransaction ut = null;
            try {
                   
                ut = com.track.gates.DbBean.getUserTranaction();
                ut.begin();
                  
                this.mLogger.auditInfo(SConstant.PRINTFLAG, "ORDER CLOSE ITEMS "
                                + " :: ORDERS TO CLOSE : "
                                + (String) obj.get("ORDERLINES") + " :: "
                                +  " MODULE : "
                                + (String) obj.get("MODULE_NAME")     +  " REMARKS : "
                                + (String) obj.get("REMARKS") ) ;

                    flag = this.process_Wms_OrderCloseItems(obj);
                    this.mLogger.auditInfo(SConstant.PRINTFLAG, "ORDER CLOSE ITEMS "
                                    + " :: END : Transction returned : " + flag);

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            flag = true;
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                    }

                } catch (Exception e) {
                      DbBean.RollbackTran(ut);
                      flag = false;
                      throw e;
                     
                }
                      return flag;
                }    
           
           
    private boolean process_Wms_OrderCloseItems(Map map) throws Exception {

            boolean flag = false;

            WmsTran tran = new WmsOrderDetailsClose();
            ((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
            flag = tran.processWmsTran(map);

            return flag;
    }
    
    
    public ArrayList getOrderHdrDetails(String plant, String moduleName,String orderNo,String status)throws Exception {
            boolean flag = false;
            POUtil _POUtil =  new POUtil();
           _POUtil.setmLogger(mLogger);
            
            DOUtil doUtil = new DOUtil();
            doUtil.setmLogger(mLogger);
        
            TOUtil toutil = new TOUtil();
            toutil.setmLogger(mLogger);
            
            LoanUtil loanUtil = new LoanUtil();
            loanUtil.setmLogger(mLogger);
            
            //WorkOrderUtil woUtil = new WorkOrderUtil();
           /// woUtil.setmLogger(mLogger);
            
           
            
        
           
            ArrayList alResult = new ArrayList();
            Hashtable htCondition = new Hashtable();
            htCondition.put("PLANT", plant);

            try {
                   if (moduleName.equalsIgnoreCase("INBOUND")){
                       String query = " pono  as orderNo ,CustName ,status  ";
                       String extCond = " and status <> 'C' ";
                       if(orderNo.length()>0){
                           extCond = extCond + " AND "+IConstants.POHDR_PONUM+" LIKE('"+orderNo+"%')";
                          
                       }
                    
                       if(status.length()>0){
                           extCond = extCond + " and status='"+status+"'";
                          
                       }
                       
                       extCond = extCond +  "  ORDER BY orderNo ,CustName ";
                      
                       alResult = _POUtil.getPoHdrDetails(query, htCondition, extCond);
                   }else if (moduleName.equalsIgnoreCase("OUTBOUND")){
                       String query = " dono as orderNo ,CustName ,status  ";
                       String extCond = " and status <> 'C'  ";
                       if(orderNo.length()>0){
                           extCond = extCond + " AND "+IConstants.DODET_DONUM+" LIKE('"+orderNo+"%')";
                          
                       }
                       
                      
                       if(status.length()>0){
                          
                           extCond = extCond + " and status='"+status+"'";
                          
                       }
                       
                       extCond = extCond + " ORDER BY orderNo ,CustName ";
                       
                       alResult = doUtil.getDoHdrDetails(query, htCondition, extCond);  
                   }else if (moduleName.equalsIgnoreCase("TRANSFER")){
                       String query = " tono as orderNo ,CustName ,status  ";
                       String extCond = " and status <> 'C' ";
                       if(orderNo.length()>0){
                           extCond = extCond + " AND "+IConstants.TOHDR_TONUM+" LIKE('"+orderNo+"%')";
                         
                       }
                       if(status.length()>0){
                           extCond = extCond + " and status='"+status+"'";
                          
                       }
                       
                       extCond =  extCond + "  ORDER BY orderNo ,CustName ";
                       
                       alResult = toutil.getToHdrDetails(query, htCondition, extCond);  
                   }else if (moduleName.equalsIgnoreCase("LOAN")){
                       String query = " ordno as orderNo  ,CustName ,status  ";
                       String extCond = " and status <> 'C' ";
                       if(orderNo.length()>0){
                           extCond = extCond + " AND "+IConstants.LOANHDR_ORDNO+" LIKE('"+orderNo+"%')";
                           
                       }
                       if(status.length()>0){
                           extCond = extCond + " and status='"+status+"'";
                          
                       }
                       
                       extCond =  extCond + "  ORDER BY orderNo ,CustName ";
                       
                       alResult = loanUtil.getLoanHdrList(query, htCondition, extCond);  
                   }/*else if (moduleName.equalsIgnoreCase("WORKORDER")){
                       String query = " WONO as orderNo  ,CustName ,status  ";
                       String extCond = " and status <> 'C' ";
                       if(orderNo.length()>0){
                           extCond = extCond + " AND "+IConstants.WOHDR_WONUM+" LIKE('"+orderNo+"%')";
                           
                       }
                       if(status.length()>0){
                           extCond = extCond + " and status='"+status+"'";
                          
                       }
                       
                       extCond =  extCond + "  ORDER BY WONO ,CustName ";
                       
                       alResult = woUtil.getWorkOrderHdrDetails(query, htCondition, extCond);  
                   }*/
                 
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);

            }

            return alResult;
    }
    
    
    
    public ArrayList getCustomerReturnOrderDetails(String plant, String moduleName,String item,String orderNo,String status)throws Exception {
        boolean flag = false;
       
        DOUtil doUtil = new DOUtil();
        doUtil.setmLogger(mLogger);
    
       
        ArrayList alResult = new ArrayList();
        Hashtable htCondition = new Hashtable();
        htCondition.put("PLANT", plant);

        try {
               if (moduleName.equalsIgnoreCase("CUSTOMERRETURN")){
            	  
                   String query = " distinct DONO as orderNo,isnull(CNAME,'') as CustName,isnull(Status,'') status    ";
                   String extCond = " and isnull(status,'') = 'C' and dono<>'MISC-ISSUE' and ITEM = '"+item+"'";
                   if(orderNo.length()>0){
                       extCond = extCond + " AND DONO LIKE('"+orderNo+"%')";
                       
                   }
            
                    alResult = doUtil.getCustomerReturnOrderNo(query, htCondition, extCond);
                    
                    
               }
           
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);

        }

        return alResult;
    }


    
    
    
    public ArrayList getOrderLineDetails(String plant, String moduleName,String orderNo)throws Exception {
            boolean flag = false;
            POUtil _POUtil =  new POUtil();
           _POUtil.setmLogger(mLogger);
            
            DOUtil doUtil = new DOUtil();
            doUtil.setmLogger(mLogger);
        
            TOUtil toutil = new TOUtil();
            toutil.setmLogger(mLogger);
            
            LoanUtil loanUtil = new LoanUtil();
            loanUtil.setmLogger(mLogger);
            
            //WorkOrderUtil woUtil = new WorkOrderUtil();
           // woUtil.setmLogger(mLogger);
        
           
            ArrayList alResult = new ArrayList();
            Hashtable htCondition = new Hashtable();
            htCondition.put("PLANT", plant);

            try {
                   if (moduleName.equalsIgnoreCase("INBOUND")){
                       String query = " POLNNO AS LNO,ITEM,ITEMDESC,UNITMO,QTYOR,QTYRC,LNSTAT   ";
                       String extCond = "  LNSTAT <> 'C'";
                       if(orderNo.length()>0){
                           htCondition.put(IConstants.POHDR_PONUM, orderNo);
                       }
                      
                       alResult = _POUtil.getLineDetailsForOrder(query, htCondition, extCond);
                   }else if (moduleName.equalsIgnoreCase("OUTBOUND")){
                       String query = "  DOLNNO AS LNO,ITEM,ITEMDESC,UNITMO,QTYOR,QtyPick,LNSTAT as LNSTAT ";
                       String extCond = "  PickStatus <> 'C'";
                       if(orderNo.length()>0){
                           htCondition.put(IConstants.DODET_DONUM, orderNo);
                       }
                       
                       alResult = doUtil.getDoDetDetails(query, htCondition, extCond);  
                   }else if (moduleName.equalsIgnoreCase("TRANSFER")){
                       String query = " TOLNNO  AS LNO,ITEM,ITEMDESC,UNITMO,QTYOR,QTYRC,QtyPick,pickstatus as LNSTAT   ";
                       String extCond = " AND  LNSTAT <> 'C'";
                       if(orderNo.length()>0){
                           htCondition.put(IConstants.TODET_TONUM, orderNo);
                       }
                       
                       alResult = toutil.getToDetDetails(query, htCondition, extCond);  
                   }else if (moduleName.equalsIgnoreCase("LOAN")){
                       String query = " ORDLNNO AS LNO,ITEM,ITEMDESC,UNITMO,QTYOR,QTYIS,QTYRC,LNSTAT  ";
                       String extCond = "   LNSTAT <> 'C'";
                       if(orderNo.length()>0){
                           htCondition.put(IConstants.LOANDET_ORDNO, orderNo);
                       }
                       alResult = loanUtil.getLineDetailsForLoanOrder(query, htCondition, extCond);  
                   }/*else if (moduleName.equalsIgnoreCase("WORKORDER")){
                       String query = " WOLNNO AS LNO,ITEM,ITEMDESC,UNITMO,QTYOR,QTYFIN,LNSTAT  ";
                       String extCond = "   LNSTAT <> 'C'";
                       if(orderNo.length()>0){
                           htCondition.put(IConstants.WODET_WONUM, orderNo);
                       }
                       //alResult = woUtil.getWorkOrderDetails(query, htCondition, extCond);  
                   }*/
               
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);

            }

            return alResult;
    }
    
    public boolean isExistsOrderType(String order, String plant) throws Exception {
		boolean exists = false;
		try {
			_OrderTypeDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.ORDERTYPE, order);
			if (isExistsOrder(ht))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
    
    public boolean isExistsOrder(Hashtable ht) throws Exception {
		boolean exists = false;
		try {
			_OrderTypeDAO.setmLogger(mLogger);
			if (_OrderTypeDAO.getCountOrderType(ht) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
    
     /* getMobileOrderOrdetTypeDetails() 
     * Created By Bruhan,June 20 2014, To get Mobile Order type details
     *  ************Modification History*********************************
     *  Bruhan,June 31 2014, To include TYPE='OUTBOUND' condition
     */
    public String getMobileSalesOrderOrderTypeDetails(String plant,String orderType) {
		String xmlStr = "";
		Hashtable htCondition = new Hashtable();
		ArrayList al = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			_OrderTypeBeanDAO.setmLogger(mLogger);
			al = _OrderTypeBeanDAO.getOrderTypeDetails(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS", htCondition,
					" and orderType like '" + orderType + "%'" + " AND TYPE='OUTBOUND' ORDER BY ORDERTYPE ");
				if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("OrderTypeDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ordertype", (String) map.get("ORDERTYPE"));
					xmlStr += XMLUtils.getXMLNode("orderdesc",  (String) map.get("ORDERDESC"));
					xmlStr += XMLUtils.getXMLNode("type",  (String) map.get("TYPE"));
					xmlStr += XMLUtils.getXMLNode("isactive",  (String) map.get("ISACTIVE"));
					xmlStr += XMLUtils.getXMLNode("remarks",  (String) map.get("REMARKS"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("OrderTypeDetails");
		
			}
		} catch (Exception e) {
			
		}
		return xmlStr;
	}
    
    public ArrayList getOrderTypeDetails(String orderType, String type, String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			_OrderTypeBeanDAO.setmLogger(mLogger);
			alResult = _OrderTypeBeanDAO.getOrderTypeDetails(
					"ORDERTYPE,ORDERDESC,TYPE,ISACTIVE,REMARKS", htCondition,
					" and TYPE = '"+ type +"' and orderType like '" + orderType + "%'" + " ORDER BY ORDERTYPE ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
    
	public boolean isExistOrdertype(String ordertype, String plant) {
		boolean exists = false;
		OrderTypeDAO ordertypeDao = new OrderTypeDAO();
		try {
			ordertypeDao.setmLogger(mLogger);
			exists = ordertypeDao.isExistordertype(ordertype, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
}


