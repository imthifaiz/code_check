package com.track.db.util;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CountryNCurrencyDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.MasterDAO;
import com.track.dao.ProductionBomDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsCOOMaster;
import com.track.tran.WmsFooterMaster;
import com.track.tran.WmsHSCODEMaster;
import com.track.tran.WmsRemarksMaster;
import com.track.tran.WmsINCOTERMSMaster;
import com.track.tran.WmsPaymentModeMaster;
import com.track.tran.WmsPaymentTermMaster;
import com.track.tran.WmsPaymentTypeMaster;
import com.track.tran.WmsShippingDetailsMaster;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class MasterUtil {
	MasterDAO _MasterDAO = null;

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.MasterUtil_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
		
	}

	public MasterUtil() {
		_MasterDAO = new MasterDAO();

	}
	
	public Boolean AddFooter(Hashtable htFooter)
			throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsFooterMaster();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(htFooter);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}

		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
	
	public ArrayList getFooterList(String plant, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getFooterDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public boolean deleteFooter(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			_MasterDAO.setmLogger(mLogger);
			deleted = _MasterDAO.deleteFooter(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public Boolean AddRemarks(Hashtable htFooter)
			throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsRemarksMaster();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(htFooter);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}

		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
	
	public ArrayList getRemarksList(String plant, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getRemarksDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	
	public ArrayList getTransportModeList(String plant, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getTransportModeDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	public boolean deleteRemarks(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			_MasterDAO.setmLogger(mLogger);
			deleted = _MasterDAO.deleteRemarks(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public Boolean AddINCOTERMS(Hashtable htFooter)
			throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsINCOTERMSMaster();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(htFooter);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}

		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
	
	public ArrayList getINCOTERMSList(String plant, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getINCOTERMSDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public boolean deleteINCOTERMS(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			_MasterDAO.setmLogger(mLogger);
			deleted = _MasterDAO.deleteINCOTERMS(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	
	public Boolean AddShippingDetails(Hashtable htFooter)
			throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsShippingDetailsMaster();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(htFooter);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}

		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
	
	public ArrayList getShippingDetailsList(String plant, String cond) {
		ArrayList al = null;
		try {
			al = _MasterDAO.getShippingDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public boolean deleteShippingDetails(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			
			_MasterDAO.setmLogger(mLogger);
			deleted = _MasterDAO.deleteShippingDetails(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	 public ArrayList getEstimateShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getEstimateShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	}   
	 public ArrayList getOutboundShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getOutboundShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	}   
	 public ArrayList getPurchaseEstimateShippingDetails(String orderno,String shippingid, String plant) {
		 ArrayList arrList = new ArrayList();
		 try {
			 _MasterDAO.setmLogger(mLogger);
			 arrList = _MasterDAO.getPOESTShippingDetails(orderno, shippingid,plant);
		 } catch (Exception e) {
		 }
		 return arrList;
	 }   
	
	 //imtinavas detail jsp
	 public ArrayList getTransferShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getTransferShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	}   
	 
	 
	 public ArrayList getRentalShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getRentalShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	} 
	
	 public ArrayList getInboundShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getInboundShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	} 
	 
	 public ArrayList getInboundShippingDetailsVendmst(String orderno,String shippingid, String plant) {
		 ArrayList arrList = new ArrayList();
		 try {
			 _MasterDAO.setmLogger(mLogger);
			 arrList = _MasterDAO.getInboundShippingDetailsVendmst(orderno, shippingid,plant);
		 } catch (Exception e) {
		 }
		 return arrList;
	 } 
	//Resvi starts
	 public ArrayList getPOMULTIEDTShippingDetails(String orderno,String shippingid, String plant) {
	        ArrayList arrList = new ArrayList();
	        try {
	             	_MasterDAO.setmLogger(mLogger);
	                arrList = _MasterDAO.getPOMULTIEDTShippingDetails(orderno, shippingid,plant);
	        } catch (Exception e) {
	        }
	        return arrList;
	} 
	//Ends
	//Check duplicate for INCOTERM & REMARKS 17.10.18 changes by Azees	 
	 public boolean isExistINCOTERM(String aINCOTERM, String plant) {
			boolean exists = false;
			try {
				_MasterDAO.setmLogger(mLogger);
				exists = _MasterDAO.isExistsINCOTERM(aINCOTERM, plant);

			} catch (Exception e) {
			}
			return exists;
		}
	 
	 public boolean isExistREMARKS(String aRemarks, String plant) {
			boolean exists = false;
			try {
				_MasterDAO.setmLogger(mLogger);
				exists = _MasterDAO.isExistsREMARKS(aRemarks, plant);

			} catch (Exception e) {
			}
			return exists;
		}
	 
	 
	 public boolean isExistTRANSPORTMODE(String aTransportMode, String plant) {
			boolean exists = false;
			try {
				_MasterDAO.setmLogger(mLogger);
				exists = _MasterDAO.isExistsTRANSPORTMODE(aTransportMode, plant);

			} catch (Exception e) {
			}
			return exists;
		}
	 
	public boolean isExistsFOOTER(String aFOOTER, String plant) {
			boolean exists = false;
			try {
				_MasterDAO.setmLogger(mLogger);
				exists = _MasterDAO.isExistsFOOTER(aFOOTER, plant);

			} catch (Exception e) {
			}
			return exists;
		}
		public ArrayList getTaxInvShippingDetails(String orderno,String shippingid, String plant) {
        ArrayList arrList = new ArrayList();
        try {
             	_MasterDAO.setmLogger(mLogger);
                arrList = _MasterDAO.getTaxInvShippingDetails(orderno, shippingid,plant);
        } catch (Exception e) {
        }
        return arrList;
}
		
		//*********************PaymentType***********************************************************//
		
		public Boolean AddPaymentType(Hashtable htFooter)
				throws Exception {
			Boolean result = Boolean.valueOf(false);
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				WmsTran tran = new WmsPaymentTypeMaster();
				((IMLogger) tran).setMapDataToLogger(this.mLogger
						.getLoggerConstans());
				result = tran.processWmsTran(htFooter);
				if (result == true) {
					DbBean.CommitTran(ut);
					result = true;
				} else {
					DbBean.RollbackTran(ut);
					result = false;
				}

			} catch (Exception e) {
				result = false;
				DbBean.RollbackTran(ut);
				throw e;
			}
			return result;
		}
		
			
		
		
		public boolean deletePaymentType(Hashtable ht) throws Exception {
			boolean deleted = false;
			try {
				_MasterDAO.setmLogger(mLogger);
				deleted = _MasterDAO.deletePaymentType(ht);
			} catch (Exception e) {
				throw e;
			}
			return deleted;
		}
		
		 public boolean isExistPaymentType(String aPaymentType, String plant) {
				boolean exists = false;
				try {
					_MasterDAO.setmLogger(mLogger);
					exists = _MasterDAO.isExistsPaymentType(aPaymentType, plant);

				} catch (Exception e) {
				}
				return exists;
			}
		
		 
		 public ArrayList getPaymentTypeList(String plant, String cond) {
				ArrayList al = null;
				_MasterDAO.setmLogger(mLogger);
				try {
					al = _MasterDAO.getPaymentTypeDetails(plant, cond);

				} catch (Exception e) {
				}

				return al;
			}
		 //*********************PaymentMode***********************************************************//
		 
		 public Boolean AddPaymentMode(Hashtable htFooter)
				 throws Exception {
			 Boolean result = Boolean.valueOf(false);
			 UserTransaction ut = null;
			 try {
				 ut = DbBean.getUserTranaction();
				 ut.begin();
				 WmsTran tran = new WmsPaymentModeMaster();
				 ((IMLogger) tran).setMapDataToLogger(this.mLogger
						 .getLoggerConstans());
				 result = tran.processWmsTran(htFooter);
				 if (result == true) {
					 DbBean.CommitTran(ut);
					 result = true;
				 } else {
					 DbBean.RollbackTran(ut);
					 result = false;
				 }
				 
			 } catch (Exception e) {
				 result = false;
				 DbBean.RollbackTran(ut);
				 throw e;
			 }
			 return result;
		 }
		 
		 
		 
		 
		 public boolean deletePaymentMode(Hashtable ht) throws Exception {
			 boolean deleted = false;
			 try {
				 _MasterDAO.setmLogger(mLogger);
				 deleted = _MasterDAO.deletePaymentMode(ht);
			 } catch (Exception e) {
				 throw e;
			 }
			 return deleted;
		 }
		 
		 public boolean isExistPaymentMode(String aPaymentType, String plant) {
			 boolean exists = false;
			 try {
				 _MasterDAO.setmLogger(mLogger);
				 exists = _MasterDAO.isExistsPaymentMode(aPaymentType, plant);
				 
			 } catch (Exception e) {
			 }
			 return exists;
		 }
		 
		 
		 public ArrayList getPaymentModeList(String plant, String cond) {
			 ArrayList al = null;
			 _MasterDAO.setmLogger(mLogger);
			 try {
				 al = _MasterDAO.getPaymentModeDetails(plant, cond);
				 
			 } catch (Exception e) {
			 }
			 
			 return al;
		 }
		 
		 
		 //*********************PaymentTerm***********************************************************//

		 public Boolean AddPaymentTerm(Hashtable htFooter)
				 throws Exception {
			 Boolean result = Boolean.valueOf(false);
			 UserTransaction ut = null;
			 try {
				 ut = DbBean.getUserTranaction();
				 ut.begin();
				 WmsTran tran = new WmsPaymentTermMaster();
				 ((IMLogger) tran).setMapDataToLogger(this.mLogger
						 .getLoggerConstans());
				 result = tran.processWmsTran(htFooter);
				 if (result == true) {
					 DbBean.CommitTran(ut);
					 result = true;
				 } else {
					 DbBean.RollbackTran(ut);
					 result = false;
				 }
				 
			 } catch (Exception e) {
				 result = false;
				 DbBean.RollbackTran(ut);
				 throw e;
			 }
			 return result;
		 }
		 
		 
		 
		 
		 public boolean deletePaymentTerm(Hashtable ht) throws Exception {
			 boolean deleted = false;
			 try {
				 _MasterDAO.setmLogger(mLogger);
				 deleted = _MasterDAO.deletePaymentTerm(ht);
			 } catch (Exception e) {
				 throw e;
			 }
			 return deleted;
		 }
		 
		 public boolean isExistPaymentTerm(String aPaymentType, String plant) {
			 boolean exists = false;
			 try {
				 _MasterDAO.setmLogger(mLogger);
				 exists = _MasterDAO.isExistsPaymentTerm(aPaymentType, plant);
				 
			 } catch (Exception e) {
			 }
			 return exists;
		 }
		 
		 
		 public ArrayList getPaymentTermList(String plant, String cond) {
			 ArrayList al = null;
			 _MasterDAO.setmLogger(mLogger);
			 try {
				 al = _MasterDAO.getPaymentTermDetails(plant, cond);
				 
			 } catch (Exception e) {
			 }
			 
			 return al;
		 }
		 
		//*********************HSCODE***********************************************************//
			
			public Boolean AddHSCODE(Hashtable htFooter)
					throws Exception {
				Boolean result = Boolean.valueOf(false);
				UserTransaction ut = null;
				try {
					ut = DbBean.getUserTranaction();
					ut.begin();
					WmsTran tran = new WmsHSCODEMaster();
					((IMLogger) tran).setMapDataToLogger(this.mLogger
							.getLoggerConstans());
					result = tran.processWmsTran(htFooter);
					if (result == true) {
						DbBean.CommitTran(ut);
						result = true;
					} else {
						DbBean.RollbackTran(ut);
						result = false;
					}

				} catch (Exception e) {
					result = false;
					DbBean.RollbackTran(ut);
					throw e;
				}
				return result;
			}
			
				
			
			public boolean deleteHSCODE(Hashtable ht) throws Exception {
				boolean deleted = false;
				try {
					_MasterDAO.setmLogger(mLogger);
					deleted = _MasterDAO.deleteHSCODE(ht);
				} catch (Exception e) {
					throw e;
				}
				return deleted;
			}
			
			 public boolean isExistHSCODE(String aHSCODE, String plant) {
					boolean exists = false;
					try {
						_MasterDAO.setmLogger(mLogger);
						exists = _MasterDAO.isExistsHSCODE(aHSCODE, plant);

					} catch (Exception e) {
					}
					return exists;
				}
			 
			 public ArrayList getHSCODEList(String plant, String cond) {
					ArrayList al = null;
					_MasterDAO.setmLogger(mLogger);
					try {
						al = _MasterDAO.getHSCODEDetails(plant, cond);

					} catch (Exception e) {
					}

					return al;
				}
			 
			//*********************COO***********************************************************//
				
				public Boolean AddCOO(Hashtable htFooter)
						throws Exception {
					Boolean result = Boolean.valueOf(false);
					UserTransaction ut = null;
					try {
						ut = DbBean.getUserTranaction();
						ut.begin();
						WmsTran tran = new WmsCOOMaster();
						((IMLogger) tran).setMapDataToLogger(this.mLogger
								.getLoggerConstans());
						result = tran.processWmsTran(htFooter);
						if (result == true) {
							DbBean.CommitTran(ut);
							result = true;
						} else {
							DbBean.RollbackTran(ut);
							result = false;
						}

					} catch (Exception e) {
						result = false;
						DbBean.RollbackTran(ut);
						throw e;
					}
					return result;
				}
				
					
				
				public boolean deleteCOO(Hashtable ht) throws Exception {
					boolean deleted = false;
					try {
						_MasterDAO.setmLogger(mLogger);
						deleted = _MasterDAO.deleteCOO(ht);
					} catch (Exception e) {
						throw e;
					}
					return deleted;
				}
				
				 public boolean isExistCOO(String aCOO, String plant) {
						boolean exists = false;
						try {
							_MasterDAO.setmLogger(mLogger);
							exists = _MasterDAO.isExistsCOO(aCOO, plant);

						} catch (Exception e) {
						}
						return exists;
					}
				 
				 public ArrayList getCOOList(String plant, String cond) {
						ArrayList al = null;
						_MasterDAO.setmLogger(mLogger);
						try {
							al = _MasterDAO.getCOODetails(plant, cond);

						} catch (Exception e) {
						}

						return al;
					}
				//*********************Shipment***********************************************************//
					
					public Boolean addShipment(Hashtable htShipment,String plant)
							throws Exception {
						Boolean result = Boolean.valueOf(false);
						try {
							result = _MasterDAO.addShipment(htShipment, plant);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
					}
					
					public Boolean isExisitShipment(Hashtable htShipment,String plant)
							throws Exception {
						Boolean result = Boolean.valueOf(false);
						try {
							result = _MasterDAO.isExisitShipment(htShipment, "");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
					}
					  public Boolean updateShipment(Hashtable ht) throws Exception {
							Boolean result = Boolean.valueOf(false);

							Hashtable htCond = new Hashtable();
							htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
							htCond.put("SHIPMENT_CODE", (String) ht
									.get("SHIPMENT_CODE"));
							try {
								
								StringBuffer updateQyery = new StringBuffer("set ");

								updateQyery.append("PONO" + " = '"
										+ (String) ht.get("PONO") + "'");
								updateQyery.append("," + "UPAT" + " = '"
										+ (String) ht.get("UPAT") + "'");
								updateQyery.append("," + "UPBY" + " = '"
										+ (String) ht.get("UPBY") + "'");
								
								
								_MasterDAO.setmLogger(mLogger);
								result = _MasterDAO.updateShipment(updateQyery.toString(), htCond,"");
							
					  } catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							throw new Exception("Edit Invoice Cannot be modified");
						}
						return result;
					  }
					  public ArrayList getShipmentSummaryView(Hashtable ht, String plant, String pono) {
							ArrayList arrList = new ArrayList();
							String sCondition = "",dtCondStr="",extraCon="";
							StringBuffer sql;
							 Hashtable htData = new Hashtable();
							try {								
								_MasterDAO.setmLogger(mLogger);								    			        
						             
						           sql = new StringBuffer("select PONO,SHIPMENT_CODE as SHIPMENTCODE");						           
						           sql.append(" from " + plant +"_SHIPMENT A WHERE A.PLANT='"+ plant+"'" + sCondition);
					                   
								            
					 				if (ht.get("PONO") != null) {
										  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
									    }
					 				
					 				if (ht.get("SHIPMENT_CODE") != null) {
										  sql.append(" AND A.SHIPMENT_CODE = '" + ht.get("SHIPMENT_CODE") + "'");
									    }
									   
									  arrList = _MasterDAO.selectForReport(sql.toString(), htData, extraCon);
					     
						
							 }catch (Exception e) {
								this.mLogger.exception(this.printLog,
										"Exception :repportUtil :: getShipmentSummary:", e);
							}
							return arrList;
						}
					  
  public ArrayList getShipmentRefForBill(String plant, String pono, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getShipmentRefForBill(plant, pono, cond);

		} catch (Exception e) {
		}

		return al;
	}  
  
  public ArrayList getExpenseDetailForBill(String plant, String id) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getExpenseDetailForBill(plant, id);
		} catch (Exception e) {
		}
		return al;
	} 
  
  public ArrayList getExpenseTaxDetailForBill(String plant, String id) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getExpenseTaxDetailForBill(plant, id);
		} catch (Exception e) {
		}
		return al;
	} 
  
  public ArrayList getShipmentDetail(String plant, String pono, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getShipmentDetail(plant, pono, cond);

		} catch (Exception e) {
		}

		return al;
	}
	public ArrayList getSalesLocationList(String aCustName,
			String plant,String country) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getSalesLocationList(aCustName,plant,country);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getSalesLocationListByCode(String aCustName,
			String plant,String countrycode) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getSalesLocationListByCode(aCustName,plant,countrycode);
		} catch (Exception e) {
		}
		return arrList;
	}
  
  public ArrayList getExpenseDetailusingponoanddnol(String plant, String pono, String shipmentcode) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getExpenseDetailusingPonoandsno(plant, pono, shipmentcode);
		} catch (Exception e) {
		}
		return al;
	}
  
  public ArrayList getExpenseDetailusingbillanddnol(String plant, String bill, String shipmentcode) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getExpenseDetailusingbillanddnol(plant, bill, shipmentcode);
		} catch (Exception e) {
		}
		return al;
	}
  
  public ArrayList getTaxTreatmentList(String ataxtrt,
			String plant,String country) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getTaxTreatmentList(ataxtrt,plant,country);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getSalesLocationByState(String aCustName,
			String plant,String country) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getSalesLocationByState(aCustName,plant,country);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getCountryList(String acountry,
			String plant,String region) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getCountryList(acountry,plant,region);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getStateList(String astate,
			String plant,String acountry) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getStateList(astate,plant,acountry);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getProductAssignedSupplier(String plant,String item) throws Exception {
	  ArrayList arrList = null;
	  _MasterDAO.setmLogger(mLogger);
	  try {
		  arrList = _MasterDAO.getProductAssignedSupplier(plant,item);
	  } catch (Exception e) {
	  }
	  return arrList;
  }
  public ArrayList getcooCountryCode(String country) throws Exception {
	  ArrayList arrList = null;
	  _MasterDAO.setmLogger(mLogger);
	  try {
		  
		  arrList = new CountryNCurrencyDAO().getCountryCode(country);
	  } catch (Exception e) {
	  }
	  return arrList;
  }
  public ArrayList getCountryCurrency(String plant,String currency) throws Exception {
	  ArrayList arrList = null;
	  _MasterDAO.setmLogger(mLogger);
	  try {
		  
		  arrList = new CurrencyDAO().getCountryCurrency(plant,currency);
	  } catch (Exception e) {
	  }
	  return arrList;
  }
  public ArrayList getBankList(String abank,
			String plant) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getBankList(abank,plant);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getRegionList(String region) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getRegionList(region);
		} catch (Exception e) {
		}
		return arrList;
	}
  
  public ArrayList getItemMasterList(String plant, String cond) {
		ArrayList al = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			al = _MasterDAO.getItemMasterList(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
  public ArrayList getDepartmentList(String department,
			String plant,String cond) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getDepartmentList(department,plant,cond);
		} catch (Exception e) {
		}
		return arrList;
	}
  public ArrayList getBankNameList(String bankname,
			String plant,String cond) throws Exception {
		ArrayList arrList = null;
		_MasterDAO.setmLogger(mLogger);
		try {
			
			arrList = _MasterDAO.getBanknameList(bankname,plant,cond);
		} catch (Exception e) {
		}
		return arrList;
	}
}
