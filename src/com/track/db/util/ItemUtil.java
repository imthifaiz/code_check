package com.track.db.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvSesBeanDAO;
import com.track.dao.ItemLocBeanDAO;
import com.track.dao.ItemMapBeanDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PrdClassDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemUtil {

	private ItemMstDAO _ItemMstDAO = null;
	private ItemSesBeanDAO itemSesBeanDAO = new ItemSesBeanDAO();
	private ItemLocBeanDAO itemLocBean = new ItemLocBeanDAO();
	private InvSesBeanDAO invSesBeanDAO = new InvSesBeanDAO();
	private MLogger mLogger = new MLogger();
	private ItemMapBeanDAO itemMapBeanDAO = new ItemMapBeanDAO();
	private boolean printLog = MLoggerConstant.ItemUtil_PRINTPLANTMASTERLOG;
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public ItemUtil() {
		_ItemMstDAO = new ItemMstDAO();
	}

	/**
	 * method : insertItem(Hashtable ht) description : insert new record into
	 * ITEMMST
	 * 
	 * @param h
	 * @return
	 */
	public boolean insertItem(Hashtable ht) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.insertIntoItemMst(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

	/**
	 * method : updateItem(Hashtable ht) description : Update the ITEMMST
	 * 
	 * @param ht
	 * @return boolean
	 */
	public boolean updateItem(Hashtable htUpdate, Hashtable htCondition) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.updateItemMst(htUpdate, htCondition);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

	/**
	 * method : deleteItem(Hashtable ht) description : Delete record from
	 * ITEMMST
	 * 
	 * @param ht
	 * @return boolean
	 */
	public boolean deleteItem(Hashtable ht) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.deleteItemMst(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

	/**
	 * method : isExistsItemMst(Hashtable ht) description :
	 * 
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsItemMst(Hashtable ht) throws Exception {
		boolean exists = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			if (itemSesBeanDAO.getCountItemMst(ht) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public boolean isExistsPosOutletPrice(Hashtable ht) throws Exception {
		boolean exists = false;
		try {
			if (new POSHdrDAO().getCountPosOutletPrice(ht) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public boolean isExistsPosOutletminmax(Hashtable ht) throws Exception {
		boolean exists = false;
		try {
			if (new POSHdrDAO().getCountPosOutletminmax(ht) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	public boolean isExistsItemMst(String aItem) throws Exception {
		boolean exists = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, IConstants.PLANT_VAL);
			ht.put(IConstants.ITEM, aItem);
			if (isExistsItemMst(ht))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	 public ArrayList getNonStockList( String plant, String Cond) {

	        ArrayList al = null;
	        ItemMstDAO dao = new ItemMstDAO();
	        dao.setmLogger(mLogger);
	        try {
	                al = dao.getNonStkList( plant, Cond);

	        } catch (Exception e) {

	        }

	        return al;
	}
	    
 public ArrayList getMprice(String plant, String Item)
				throws Exception {
//			boolean flag = false;
			Hashtable htCondition = new Hashtable();
			ArrayList alResult = new ArrayList();

			htCondition.put(IConstants.PLANT, plant);
			htCondition.put(IConstants.ITEM, Item);

			try {
				//_GstTypeDAO.setmLogger(mLogger);
				_ItemMstDAO.setmLogger(mLogger);
				alResult = _ItemMstDAO.getMpriceDetails(
						"MINSPRICE",
						htCondition, "");

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);

			}

			return alResult;
		}


		public boolean isExistsItemMst(String aItem, String plant) throws Exception {
		boolean exists = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.ITEM, aItem);
			if (isExistsItemMst(ht))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
		
		public boolean isExistsPosOutletPrice(String Item,String Outlet, String Plant) throws Exception {
			boolean exists = false;
			try {
				itemSesBeanDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, Plant);
				if(!Item.equalsIgnoreCase(""))
				ht.put(IConstants.ITEM, Item);
				ht.put("OUTLET", Outlet);
				if (isExistsPosOutletPrice(ht))
					exists = true;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return exists;
		}
		
		public boolean isExistsPosOutletminmax(String Item,String Outlet, String Plant) throws Exception {
			boolean exists = false;
			try {
				itemSesBeanDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, Plant);
				if(!Item.equalsIgnoreCase(""))
				ht.put(IConstants.ITEM, Item);
				ht.put("OUTLET", Outlet);
				if (isExistsPosOutletminmax(ht))
					exists = true;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return exists;
		}

	/**
	 * @method : getItemDetails(String aItem)
	 * 
	 * 
	 *         @ get the details for the given item
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemDetails(String aItem) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			arrList = itemSesBeanDAO.getItemDetails(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
        
    public List queryItemMstDetails(String aItem,String aPlant) throws Exception {
            List arrList = new ArrayList();
            try {
                    itemSesBeanDAO.setmLogger(mLogger);
                    arrList = itemSesBeanDAO.queryItemMstDetails(aItem, aPlant);
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return arrList;
    }
    
    public List queryItemMstDetailsforpurchase(String aItem,String aPlant) throws Exception {
        List arrList = new ArrayList();
        try {
                itemSesBeanDAO.setmLogger(mLogger);
                arrList = itemSesBeanDAO.queryItemMstDetailsforpurchase(aItem, aPlant);
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}

	/**
	 * @method : qryItemMst(String aItem)
	 * @param aItem
	 * @return List
	 * @throws Exception
	 */
	public List qryItemMst(String aItem) throws Exception {
		List listQry = new ArrayList();
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			listQry = itemSesBeanDAO.queryItemMst(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}

	public List qryItemMst(String aItem, String plant, String cond)
			throws Exception {
		List listQry = new ArrayList();
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			listQry = itemSesBeanDAO.queryItemMst(aItem, plant, cond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}
        
    public List qryItemMstCond(String aItem, String plant, String cond)
                    throws Exception {
            List listQry = new ArrayList();
            try {
                    itemSesBeanDAO.setmLogger(mLogger);
                    listQry = itemSesBeanDAO.queryItemMstCond(aItem, plant, cond);
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return listQry;
    }

	public List queryItemMstForSearchCriteria(String aItem, String aItemDesc,String aPrdClass,String aPrdType,String aPrdBrand,String aprddept,String astocktype,
			String plant, String cond) throws Exception {
		List listQry = new ArrayList();
               
		try {
			itemSesBeanDAO.setmLogger(mLogger);
		        aItemDesc = StrUtils.InsertQuotes(aItemDesc);
			listQry = itemSesBeanDAO.queryItemMstForSearchCriteria(aItem, aItemDesc,aPrdClass,aPrdType,aPrdBrand,aprddept,astocktype,
					plant, cond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}
	public List queryAlternateItemMstForSearchCriteria(String aItem, String aItemDesc,String aPrdClass,String aPrdType,String aPrdBrand,String aprddept,
			String plant, String cond) throws Exception {
		List listQry = new ArrayList();
               
		try {
			itemSesBeanDAO.setmLogger(mLogger);
		        aItemDesc = StrUtils.InsertQuotes(aItemDesc);
			listQry = itemSesBeanDAO.queryAlternateItemMstForSearchCriteria(aItem, aItemDesc,aPrdClass,aPrdType,aPrdBrand, aprddept,
					plant, cond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}
	
	 public List queryAlternetItemMstForSearchCriteriaNew(String aItem, String aItemDesc,String aPrdClass,String aPrdType,String aPrdBrand,String aPrdDept,
             String plant, String cond,int start,int end) throws Exception {
     List listQry = new ArrayList();
    
     try {
             itemSesBeanDAO.setmLogger(mLogger);
             aItemDesc = StrUtils.InsertQuotes(aItemDesc);
             listQry = itemSesBeanDAO.queryAlternetItemMstForSearchCriteriaNew(aItem, aItemDesc,aPrdClass,aPrdType,aPrdBrand,aPrdDept,
                             plant, cond,start,end);
     } catch (Exception e) {
             this.mLogger.exception(this.printLog, "", e);
     }
     return listQry;
}
	public List queryAlternateItemMstList(String aItem, String aItemDesc,String aPrdClass,String aPrdType,String aPrdBrand,String aprddept,
			String plant, String cond) throws Exception {
		List listQry = new ArrayList();
               
		try {
			itemSesBeanDAO.setmLogger(mLogger);
		        aItemDesc = StrUtils.InsertQuotes(aItemDesc);
			listQry = itemSesBeanDAO.queryAlternateItemList(aItem, aItemDesc,aPrdClass,aPrdType,aPrdBrand,aprddept,
					plant, cond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}

	public List queryItemMstForSearchCriteria(String aItem, String aItemDesc,String aPrdClass,String aPrdType,
			String plant, String cond) throws Exception {
		List listQry = new ArrayList();
               
		try {
			itemSesBeanDAO.setmLogger(mLogger);
		        aItemDesc = StrUtils.InsertQuotes(aItemDesc);
			listQry = itemSesBeanDAO.queryItemMstForSearchCriteria(aItem, aItemDesc,aPrdClass,aPrdType,
					plant, cond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}

    public List queryItemMstForSearchCriteriaNew(String aItem, String aItemDesc,String aPrdClass,String aPrdType,String aPrdBrand,String aPrdDept,String astocktype,
                    String plant, String cond,int start,int end) throws Exception {
            List listQry = new ArrayList();
           
            try {
                    itemSesBeanDAO.setmLogger(mLogger);
                    aItemDesc = StrUtils.InsertQuotes(aItemDesc);
                    listQry = itemSesBeanDAO.queryItemMstForSearchCriteriaNew(aItem, aItemDesc,aPrdClass,aPrdType,aPrdBrand,aPrdDept,astocktype,
                                    plant, cond,start,end);
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return listQry;
    }
    
    
	/**
	 * @method : getStockReorderItemList()
	 * @return
	 * @throws Exception
	 */
	public ArrayList getStockReorderItemList() throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			arrList = itemSesBeanDAO.getStockReorderItemList();

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	/**
	 * method : insertItemLoc(Hashtable ht) description : insert new record into
	 * ITEMLOC
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertItemLoc(Hashtable ht) {
		boolean inserted = false;
		try {
			itemLocBean.setmLogger(mLogger);
			inserted = itemLocBean.insertIntoItemLoc(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}
        
        

	/**
	 * method : deleteItemLoc(Hashtable ht) description : Delete record from
	 * ITEMLOC
	 * 
	 * @param ht
	 * @return boolean
	 */
	public boolean deleteItemLoc(Hashtable ht) {
		boolean deleted = false;
		int invCnt = 0;
		try {
			itemLocBean.setmLogger(mLogger);
			invSesBeanDAO.setmLogger(mLogger);
			invCnt = invSesBeanDAO.getCountInvmst(ht);
			if (invCnt > 0) {
				deleted = invSesBeanDAO.deleteInvsmt(ht);
				if (deleted = true) {
					deleted = itemLocBean.deleteItemLoc(ht);
				}
			} else {
				deleted = itemLocBean.deleteItemLoc(ht);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return deleted;
	}

	/**
	 * @method : isExistsItemLoc(String aItem, String aLoc)
	 * @desription :
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsLoc4Item(String aItem, String aLoc) throws Exception {
		boolean exists = false;
		try {
			itemLocBean.setmLogger(mLogger);
			exists = itemLocBean.isExistItemAndLoc(aItem, aLoc);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	/**
	 * @method : getLocationList4Item(String aItem, String aLoc)
	 * @description : get location list for the given item
	 * @param aItem
	 * @param aLoc
	 * @return
	 * @throws Exception
	 */
	public List getLocationList4Item(String aItem) throws Exception {
		List listLoc = new ArrayList();
		try {
			itemLocBean.setmLogger(mLogger);
			listLoc = itemLocBean.getLocations4Item(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listLoc;
	}

	/**
	 * @method : getAllItemLoc()
	 * @return
	 * @throws Exception
	 */
	public ArrayList getItemLocList(String aItem, String aLoc) throws Exception {
		ArrayList listLoc = new ArrayList();
		try {
			itemLocBean.setmLogger(mLogger);
			listLoc = itemLocBean.getItemLocationList(aItem, aLoc);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listLoc;
	}

	/**
	 * method : insertItemMap(Hashtable ht) description : insert new record into
	 * ITEMMST
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertItemMap(Hashtable ht) {
		boolean inserted = false;
		try {
			itemMapBeanDAO.setmLogger(mLogger);
			inserted = itemMapBeanDAO.insertIntoItemMap(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

	/**
	 * method : deleteItem(Hashtable ht) description : Delete record from
	 * ITEMMST
	 * 
	 * @param ht
	 * @return boolean
	 */
	public boolean deleteItemMap(Hashtable ht) {
		boolean deleted = false;
		try {
			itemMapBeanDAO.setmLogger(mLogger);
			deleted = itemMapBeanDAO.deleteItemMap(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return deleted;
	}

	/**
	 * @method : isExistsItemMap(String aItem)
	 * @desription :
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsItemMap(String aItem) throws Exception {
		boolean exists = false;
		try {
			itemMapBeanDAO.setmLogger(mLogger);
			exists = itemMapBeanDAO.isExistsMapItem(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	/**
	 * @method : getMapItems4Key(String aKeyItem)
	 * @description : get the list of map items for the given key item
	 * @param aKeyItem
	 * @return
	 * @throws Exception
	 */
	public List getMapItems4KeyItem(String aKeyItem) throws Exception {
		List listMapItems = new ArrayList();
		try {
			itemMapBeanDAO.setmLogger(mLogger);
			listMapItems = itemMapBeanDAO.getMapItems4Key(aKeyItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listMapItems;

	}

	/**
	 * @method : getKeyItem4MapItem(String aMapItem)
	 * @description : get the key item for the given map item
	 * @param aMapItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getKeyItem4MapItem(String aMapItem) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			itemMapBeanDAO.setmLogger(mLogger);
			arrList = itemMapBeanDAO.getKeyItem4MapItem(aMapItem);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	// /////////////////////////////////PDA methods///////////////////////////
	public String getLocationList4ItemPDA(String aItem) {
		String xmlStr = "";
		try {
			List listLoc = this.getLocationList4Item(aItem);

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Location total ='"
					+ String.valueOf(listLoc.size()) + "'");
			for (int i = 0; i < listLoc.size(); i++) {
				String sLoc = (String) listLoc.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("LOC", sLoc);
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("Location");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	/**
	 * 
	 * @param aItem
	 * @param aLoc
	 * @return
	 */
	public String getItemDetails4Inbound(String aItem, String aLoc) {
		String xmlStr = "";

		try {
			ArrayList arrayList = getItemDetails(aItem);
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("record");
			xmlStr += XMLUtils.getXMLNode("ITEM", aItem);
			xmlStr += XMLUtils.getXMLNode("DESCRIPTION", StrUtils
					.replaceCharacters2Send((String) arrayList.get(1)));
			xmlStr += XMLUtils.getXMLNode("UOM", (String) arrayList.get(2));
			xmlStr += XMLUtils.getXMLNode("LOC", aLoc);
			xmlStr += XMLUtils.getEndNode("record");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr.trim();
	}

	public ArrayList getItemList(String plant, String extCond) throws Exception {
		ArrayList listItem = new ArrayList();
		Hashtable ht = new Hashtable();
		ht.put("PLANT", plant);

		String query = "item,itemDesc,status,itemCondition";

		try {
			_ItemMstDAO.setmLogger(mLogger);
			listItem = _ItemMstDAO.selectItemMst(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listItem;
	}

	public ArrayList getItemList(String plant, String item, String extCond)
			throws Exception {
		ArrayList listItem = new ArrayList();
		Hashtable ht = new Hashtable();
		ht.put("plant", plant);
		ht.put("item", item);
		String query = "item,itemDesc,status,itemCondition";
		try {
			_ItemMstDAO.setmLogger(mLogger);
			listItem = _ItemMstDAO.selectItemMst(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listItem;
	}

	public boolean insertAlternateItemLists(String plant, String item,
			List<String> alternateItemNameLists) {
		try {
			boolean executeQuery = true;
			_ItemMstDAO.setmLogger(mLogger);
			for (String alternateItemName : alternateItemNameLists) {
				ItemUtil itemUtil = new ItemUtil();
				if(!itemUtil.isAlternatePrdExists(plant,alternateItemName)){
				executeQuery = executeQuery
						& _ItemMstDAO.insertAlternateItem(plant, item,
								alternateItemName);
				}
				
			}
			return executeQuery;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAlternateItemAvalable(String plant, String item) {
		try {
			boolean executeQuery = true;
			_ItemMstDAO.setmLogger(mLogger);
			String sql = "SELECT COUNT(*) FROM [" + plant
					+ "_ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "' ";
			executeQuery = _ItemMstDAO.isExisit(sql);
			return executeQuery;
		} catch (Exception e) {
			return false;
		}
	}
        
        public boolean isAlternatePrdExists(String plant, String AlterPrd) {
                try {
                        boolean executeQuery = true;
                        _ItemMstDAO.setmLogger(mLogger);
                        String sql = "SELECT COUNT(*) FROM [" + plant
                                        + "_ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
                                        + "'  AND ALTERNATE_ITEM_NAME='" + AlterPrd + "' ";
                        executeQuery = _ItemMstDAO.isExisit(sql);
                        return executeQuery;
                } catch (Exception e) {
                        return false;
                }
        }

	public boolean removeAlternateItems(String plant, String item,String Cond) {
		_ItemMstDAO.setmLogger(mLogger);
		return _ItemMstDAO.removeAlternateItems(plant, item,Cond);
	}
        
    public ArrayList getUomList( String plant, String Cond) {

            ArrayList al = null;
            ItemMstDAO dao = new ItemMstDAO();
            dao.setmLogger(mLogger);
            try {
                    al = dao.getUomList( plant, Cond);

            } catch (Exception e) {

            }

            return al;
    }
    
    public String getkittingparentproduct(String plant1,String user) {

		String xmlStr = "";
		ArrayList al = null;
		ItemMstDAO dao = new ItemMstDAO ();
		dao.setmLogger(mLogger);

		try {
			al = dao.getKittingProducts(plant1,user);
			


			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("product", (String) map
							.get("item"));
					 xmlStr += XMLUtils.getXMLNode("desc", StrUtils.replaceCharacters2SendPDA((String) map.get("itemdesc")));
				
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    
    public String getkittingchildproduct(String plant1,String user,String parentloc) {

		String xmlStr = "";
		ArrayList al = null;
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO .setmLogger(mLogger);

		try {
			al = invMstDAO.getKittingChildProducts(plant1,user,parentloc);
			


			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("product", (String) map.get("item"));
					 xmlStr += XMLUtils.getXMLNode("desc", StrUtils.replaceCharacters2SendPDA((String) map.get("itemdesc")));
				
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    public String getkittingchildbatch(String plant1,String user,String parentloc,String childproduct) {

		String xmlStr = "";
		ArrayList al = null;
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO .setmLogger(mLogger);

		try {
			al = invMstDAO.getKittingChildBatch(plant1,user,parentloc,childproduct);
			


			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Batch", (String) map
							.get("Batch"));
					xmlStr += XMLUtils.getXMLNode("Qty", StrUtils.formatNum((String) map
							.get("qty")));
				
					xmlStr += XMLUtils.getEndNode("record");
				}
System.out.println("xml string"+xmlStr);				
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    
    public String getkittingcheckbatch(String plant1,String user,String parentloc,String parentproduct,String parentbatch) {

		String xmlStr = "";
		ArrayList al = null;
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO .setmLogger(mLogger);

		try {
			
			al = invMstDAO.getKittingCheckBatch(plant1,user,parentbatch);
			


			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Batch", (String) map
							.get("Batch"));
					xmlStr += XMLUtils.getXMLNode("Loc", (String) map
							.get("Loc"));
					xmlStr += XMLUtils.getXMLNode("ParentProduct", (String) map
							.get("Item"));
				
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    
    public String getWMSkittingcheckbatch(String plant1,String parentproduct,String parentloc,String parentbatch) {
  		String xmlStr = "";
  		ArrayList al = null;
  		InvMstDAO invMstDAO = new InvMstDAO();
  		invMstDAO .setmLogger(mLogger);
  		try {
  			
  			al = invMstDAO. getWMSKittingCheckBatch(plant1,parentproduct,parentloc,parentbatch);
  			if (al.size() > 0) {
  				xmlStr="Date Exists";
  			}
  	
  		} catch (Exception e) {
  			this.mLogger.exception(this.printLog, "", e);
  		}

  		return xmlStr;
  	}
 
    public String getDekittingparentbatch(String plant1,String user,String parentloc,String parentproduct) {

		String xmlStr = "";
		ArrayList al = null;
		BomDAO bomDAO = new BomDAO();
		bomDAO.setmLogger(mLogger);

		try {
			al = bomDAO.getDeKittingParentBatch(plant1,user,parentloc,parentproduct);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Batch", (String) map
							.get("Batch"));
					
				
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    public String getDekittingchildproduct(String plant1,String user,String parentproduct,String parentloc,String parentbatch) {

		String xmlStr = "";
		ArrayList al = null;
		BomDAO bomDAO = new BomDAO();
		bomDAO.setmLogger(mLogger);


		try {
			al = bomDAO.getDeKittingChildProducts(plant1,user,parentproduct,parentloc,parentbatch);
			
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("product", (String) map
							.get("item"));
					 xmlStr += XMLUtils.getXMLNode("desc", StrUtils.replaceCharacters2SendPDA((String) map.get("itemdesc")));
				
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    public String getDekittingchildtbatch(String plant1,String user,String parentloc,String parentproduct,String childproduct,String parentbatch) {

		String xmlStr = "";
		ArrayList al = null;
		BomDAO bomDAO = new BomDAO();
		bomDAO.setmLogger(mLogger);

		try {
			al = bomDAO.getDeKittingChildBatch(plant1,user,parentloc,parentproduct,childproduct,parentbatch);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Batch", (String) map
							.get("Batch"));
					xmlStr += XMLUtils.getXMLNode("Qty", StrUtils.formatNum((String) map
							.get("qty")));
				
				
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
	public String getItemDescriptionwithPlant(String aItem, String plant) throws Exception {
		String itemDes = "";
		try {
			_ItemMstDAO.setmLogger(mLogger);
			itemDes = _ItemMstDAO.getItemDesc(aItem, plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return itemDes;
	}
	
	public String getItemDetailDescriptionwithPlant(String aItem, String plant) throws Exception {
		String itemDetDes = "";
		
		try {
			_ItemMstDAO.setmLogger(mLogger);
			Map m = _ItemMstDAO.getItemDetailDescription(aItem, plant);
			
			if(!m.isEmpty()){
				itemDetDes = (String)m.get("DetailItemDesc");
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return itemDetDes;
	}
    
	public ArrayList getItemDetails(String aCustno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			_ItemMstDAO.setmLogger(mLogger);
			arrList = _ItemMstDAO.getItemDetails(aCustno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public boolean insertDetailDesc(Hashtable ht) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.insertIntoItemdetDesc(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}
	public boolean insertAdditionalPrd(Hashtable ht) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.insertIntoAdditionalItem(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}
public boolean insertOBCustomerDiscount(Hashtable ht) {
		boolean inserted = false;
		try {
			itemSesBeanDAO.setmLogger(mLogger);
			inserted = itemSesBeanDAO.insertIntoOBCustomerDiscount(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}

public boolean insertIBSupplierDiscount(Hashtable ht) {
	boolean inserted = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		inserted = itemSesBeanDAO.insertIntoIBSupplierDiscount(ht);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return inserted;
}
public ArrayList getSupplierDiscountList(String plant,String item,String desc,String brand,String pclass,String type) {

	ArrayList al = null;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		al = _ItemMstDAO.getSupplierDiscountDetails(plant, item, desc, brand, pclass, type,"");

	} catch (Exception e) {
	}

	return al;
}
public ArrayList getCustomerDiscountList(String plant,String item,String desc,String brand,String pclass,String type) {

	ArrayList al = null;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		al = _ItemMstDAO.getCustomerDiscountDetails(plant, item, desc, brand, pclass, type,"");

	} catch (Exception e) {
	}

	return al;
}
public boolean isExistsAlternateBrandItem(Hashtable htLoc) throws Exception {

	boolean isExists = false;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		isExists =_ItemMstDAO.isExistsAlternateBrandItem(htLoc);

	} catch (Exception e) {

		throw e;
	}

	return isExists;
}
public boolean insertAlternateBrandItem (Hashtable ht) throws Exception {
	boolean inserted = false;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		PrdClassDAO itemDao = new PrdClassDAO();
		itemDao.setmLogger(mLogger);
		inserted = _ItemMstDAO.insertAlternateBrandItem(ht);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}
public boolean deleteAlternateBrandItem (Hashtable ht) throws Exception {
	boolean deleted = false;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		PrdClassDAO itemDao = new PrdClassDAO();
		itemDao.setmLogger(mLogger);
		deleted = _ItemMstDAO.deleteAlternateBrandItem(ht);
	} catch (Exception e) {
		throw e;
	}
	return deleted;
}
public ArrayList getAltItemList(String plant,String item,String desc) {

	ArrayList al = null;
	_ItemMstDAO.setmLogger(mLogger);
	try {
		al = _ItemMstDAO.getAltItems(plant, item, desc);

	} catch (Exception e) {
	}

	return al;
}
public boolean updateItemImage(String plant , String item,String catalogPath) {
	boolean updated = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		updated = itemSesBeanDAO.updateItemImage(plant, item, catalogPath);;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return updated;
}

public boolean updatelogoImage(String plant ,String catalogPath) {
	boolean updated = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		updated = itemSesBeanDAO.updatelogoImage(plant, catalogPath);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return updated;
}

public ArrayList getUserItemDetails(String CUSTNO ,Hashtable ht,String plant) throws Exception {
	Hashtable htCondition = new Hashtable();
	ArrayList alResult = new ArrayList();
	try {
		htCondition.put("PLANT", plant);
		String loctypeid = ht.get("ITEM").toString();
		String loctypeid2 = ht.get("ITEMDESC").toString();
		String loctypeid3 = ht.get("PRD_DEPT_ID").toString();
		if(loctypeid.length()>0){
		htCondition.put("ITEM", loctypeid);}
		if(loctypeid2.length()>0){
		htCondition.put("ITEMDESC", loctypeid2);}
		if(loctypeid3.length()>0){
			htCondition.put("PRD_DEPT_ID", loctypeid3);}
		itemSesBeanDAO.setmLogger(mLogger);
		//alResult is changed by Bruhan
		
		alResult = itemSesBeanDAO.getUserItemDetails(
				"ITEM,ISNULL(ITEMDESC,'') ITEMDESC,isnull(PRD_DEPT_ID,'')as PRD_DEPT_ID,isnull(PRD_CLS_ID,'') as PRD_CLS_ID,ISNULL(ITEMTYPE,'') as ITEMTYPE,ISNULL(PRD_BRAND_ID,'')as PRD_BRAND_ID,ISACTIVE",htCondition,"");

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}

	return alResult;
}

public boolean isValidCustAssignedItem(String aPlant,String aUser, String aLoc) throws Exception {
    
    boolean isValidLoc = false;
    itemSesBeanDAO.setmLogger(mLogger);
    try {
        Hashtable htloc = new Hashtable();
        
        htloc.clear();
        htloc.put(IDBConstants.PLANT, aPlant);
    
        isValidLoc = itemSesBeanDAO.isAlreadtCustPrdExists(aUser,aLoc,aPlant);
       

    } catch (Exception e) {
    throw e;
    }
    return isValidLoc;
}
public boolean addCustPrd(Hashtable ht) throws Exception {
	boolean flag = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		flag = itemSesBeanDAO.insertIntoCustPrdMst(ht);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}

	return flag;
}
public boolean deleteCustPrd(String custno, String plant) throws Exception {
	
	boolean flag = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		flag = itemSesBeanDAO.deleteUserCustPrd(custno, plant);
		
	} catch (Exception e) {
		
		throw e;
	}
	return flag;
}

public boolean insertItemSupplier(Hashtable ht) {
	boolean inserted = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		inserted = itemSesBeanDAO.insertItemSupplier(ht);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return inserted;
}

public boolean isExistsItemMstDescription(String description, String plant) throws Exception {
	boolean exists = false;
	try {
		itemSesBeanDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT, plant);
		ht.put("REMARK1", description);
		if (isExistsItemMst(ht))
			exists = true;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return exists;
}
}