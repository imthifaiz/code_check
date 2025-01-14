

package com.track.DataDownloader;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.track.constants.MLoggerConstant;
import com.track.db.object.ShopifyPojo;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.io.File;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


import java.util.Map;
import java.util.Properties;

import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;


public class POIReadExcel  {

	/** Creates a new instance of ReadExcel */

	private int _sheetNo = 0;

	private String _fileName = "";
        private static String NumberOfColumn;
        private static Map mapTableFileld=null;
         public static String DB_PROPS_FILE = MLoggerConstant.PROPS_FOLDER + "/track/config/trackConstants.properties";
	private String _sheetName = "";
	StrUtils strutils = new StrUtils();
	public POIReadExcel(String file) {

	}
	private MLogger mlogger=new MLogger();
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {

		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mlogger.setLoggerConstans(dataForLogging);
	}
	public POIReadExcel(String file, String sheetName) {
		this._fileName = file;
		this._sheetName = sheetName;
	}

	public POIReadExcel(String file, int sheetNo) {
		this._fileName = file;
		this._sheetNo = sheetNo;
	}

	
	public String[] readExcelFile(int no_of_column,String moduleType) throws Exception {

		String[] arrayData = null;
		ArrayList _alRecord = new ArrayList();

		InputStream inp = null;
		HSSFWorkbook wb = null;
	        Properties dbpr;
	        InputStream dbip;
	        mapTableFileld=new HashMap();
	       
	         

		try {
		    dbip = new FileInputStream(new File(DB_PROPS_FILE));
		    dbpr = new Properties();
		    dbpr.load(dbip);
		    NumberOfColumn  = dbpr.getProperty(moduleType+"_NumberOfColumn");
                
		    for(int i=0; i<=Integer.parseInt(NumberOfColumn);i++)
                    {
                      mapTableFileld.put(moduleType+"_TableFiled" + i,dbpr.getProperty(moduleType+"_TableFiled" + i));
                        System.out.println(moduleType+"_TableFiled" + i + "          "+dbpr.getProperty(moduleType+"_TableFiled" + i));
                    }
			try {
                        
			
				inp = new FileInputStream(_fileName);
				wb = new HSSFWorkbook(new POIFSFileSystem(inp));

			} catch (FileNotFoundException fn) {
				throw new Exception("Exception : " + fn.getMessage());
			}

			HSSFSheet sheet;

			if (_sheetName == "") {

				//System.out.println("sheetNo : " + _sheetNo);
				sheet = wb.getSheetAt(_sheetNo);

			} else {
				//System.out.println("SheetName : " + _sheetName);
				sheet = wb.getSheet(_sheetName);
			}

			if (sheet == null) {

				throw new Exception(
						"Sheet name : "
								+ _sheetName
								+ " not found please check the sheet name and download again. Note : Sheet name is case sensitive");
			}

		mlogger.info("*********** Total Rows found in sheet : "
					+ sheet.getPhysicalNumberOfRows());

			int dataStartRow = 65536;

			HSSFRow row = null;
			HSSFCell cell = null;

			// MLogger.info("Stage : 2");
                        
			 

			int breakcouter1 = 0;
		        String strCellValue    ="";
			for (int i = 0; i <= sheet.getPhysicalNumberOfRows()- 1; i++) {

		
//				mlogger.info("breakcouter1 : " + breakcouter1);

				if (breakcouter1 > 0) {
					mlogger.info("data counter is " + breakcouter1 + " breaing the row ");
					break;
				}

				row = sheet.getRow(i);
                                
                                
				StringBuffer data = new StringBuffer("");
			   
				// getting column data
				for (int j = 0; j <= no_of_column-1; j++) {
				   
                                   char ch= ((String)mapTableFileld.get(moduleType+"_TableFiled"+(j+1))).charAt(0);
				    String chartemp = String.valueOf(ch);
				  
				   
					try {
						//System.out.println("row :" + i);
						cell = row.getCell((short) j);
                                                    if(i==0){
					    try{
                                               
                                                    strCellValue = cell.toString();
                                                    data.append(StrUtils.fString(strCellValue));
                                                 }catch(Exception ex){ 
                                                   System.out.println("Please download the latest Import Template,Few Columns are missing");
                                                   throw new Exception ("Please download the latest Import Template,Few Columns are missing");
                                                }
                                          }
                                            else if (cell == null||cell.toString()=="") {
					            
					             data.append("null");
					    }else { 
                                            
                                            if(chartemp.equalsIgnoreCase("d"))
                                            {
                                                
                                             /*  try{
                                                    if (HSSFDateUtil.isCellDateFormatted(cell)) { 
                                                        double value = cell.getNumericCellValue();
                                                        Date date = HSSFDateUtil.getJavaDate(value);
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MMM-yyyy");      
                                                       strCellValue = dateFormat.format(date);
                                                    }else{
                                                        throw new Exception("Not an accepted date format (mm/dd/yyyy) at line "+(i+1)); 
                                                    }
                                                }catch(Exception e){ throw new Exception("Not an accepted date format (mm/dd/yyyy) at line "+(i+1));}*/
                                              try{
                                                      switch(cell.getCellType()) { 
                                                      case HSSFCell.CELL_TYPE_NUMERIC:
                                                      double value = cell.getNumericCellValue();
                                                          Date date = HSSFDateUtil.getJavaDate(value);
                                                           SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy");      
                                                            strCellValue = dateFormat.format(date);
                                                      break;
                                                      case HSSFCell.CELL_TYPE_STRING:
                                                    
                                                          strCellValue = cell.toString().trim();
                                                          if(strCellValue.length()>0){
                                                          SimpleDateFormat dft1 = new SimpleDateFormat( "dd/MM/yyyy");   
                                                          SimpleDateFormat dateFrmt = new SimpleDateFormat( "dd/MM/yyyy"); 
                                                          Date dt = dft1.parse(strCellValue);
                                                          strCellValue = dateFrmt.format(dt);
                                                          }
                                                      break;
                                                      default:
                                                           value = cell.getNumericCellValue();
                                                           Date date1 = HSSFDateUtil.getJavaDate(value);
                                                           SimpleDateFormat dateFormat1 = new SimpleDateFormat( "dd/MM/yyyy");      
                                                            strCellValue = dateFormat1.format(date1);
                                                      }
                                                      
                                                          
                                              }catch(Exception e){
                                              System.out.println("Exception::::"+ e.getMessage());
                                              throw new Exception("Not an accepted Order Date or Due Date or Expiry Date format at line "+(i+1));
                                              }

                                            }else if (chartemp.equalsIgnoreCase("n")){
                                                     
                                                        switch(cell.getCellType()) { 
                                                            case HSSFCell.CELL_TYPE_NUMERIC:
                                                            DecimalFormat df = new DecimalFormat("#.######");                                                                   
                                                           // System.out.print(df.format(cell.getNumericCellValue()));
                                                            double k =cell.getNumericCellValue();
                                                             strCellValue = String.valueOf(df.format(k)); 
                                                             strCellValue =StrUtils.displayDecimal(strCellValue);
                                                            break;
                                                        case HSSFCell.CELL_TYPE_STRING:
                                                         strCellValue = cell.toString().trim();
                                                          break;
                                                        default:
                                                        	  df = new DecimalFormat("#.######");                                                                   
                                                             // System.out.print(df.format(cell.getNumericCellValue()));
                                                               k =cell.getNumericCellValue();
                                                               strCellValue = String.valueOf(df.format(k)); 
                                                               strCellValue =StrUtils.displayDecimal(strCellValue);
                                                        	
                                                        }
                                                      
                                                    }
                                              
						    else if(chartemp.equalsIgnoreCase("s"))
						    {
						       
						        switch(cell.getCellType()) { 
						            case HSSFCell.CELL_TYPE_NUMERIC:
                                                            DecimalFormat df = new DecimalFormat("#.######");                                                                   
                                                          
                                                            double k =cell.getNumericCellValue();
                                                             strCellValue = String.valueOf(df.format(k)); 
                                                            
                                                             // long StrVal =(long) cell.getNumericCellValue(); 
						            //strCellValue = String.valueOf(StrVal); 
						           //  System.out.print(strCellValue);
                                                            break;
						        case HSSFCell.CELL_TYPE_STRING:
						         strCellValue = cell.toString().trim();
						         break;
						         default:
						        	 strCellValue = cell.toString().trim();
						        	 
                                                        }
						    }
						    else if(chartemp.equalsIgnoreCase("b"))
						    {
						       
						        switch(cell.getCellType()) { 
						            case HSSFCell.CELL_TYPE_NUMERIC:
                                                            DecimalFormat df = new DecimalFormat("#.######");                                                                   
                                                          
                                                            double k =cell.getNumericCellValue();
                                                             strCellValue = String.valueOf(df.format(k)); 
                                                            
                                                             // long StrVal =(long) cell.getNumericCellValue(); 
						            //strCellValue = String.valueOf(StrVal); 
						           //  System.out.print(strCellValue);
                                                            break;
						        case HSSFCell.CELL_TYPE_STRING:
						         strCellValue = cell.toString().trim();
						         break;
						         default:
						        	 strCellValue = cell.toString().trim();
						        	 
                                                        }
						    }
						    
                                                    // To Check if the Delimiter "|" is not include in data
                                                    if(strCellValue.indexOf("|")!=-1){
                                                        throw new Exception(strCellValue + " has special character | which is not allowed "+(i+1));  
                                                    }
                                                    
                                                   
                                        
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger
										.info("********** Data equals to end increase break counter1 "
												+ breakcouter1);
								break;

							} else {
                                                                if (strCellValue.length()==0)strCellValue="null";
								data.append(strCellValue);

							}
						
                                        }

					} catch (Exception e) {
						throw e;
					}
                                       
					data.append("|||");

				}// end of column loop



				if (data.length() > 0) {
					
					if (data != null || data.length() > 0) {
						data.replace(data.length() - 1, data.length(), "");
					}
                                _alRecord.add(strutils.InsertQuotes(data.toString()));

				} else {
					System.out.println("********** Data size less than zero *************");
				}

			System.out.println("no_of_column:" + data.toString());

			}// end of row loop

//			mlogger.info("Adding data to array to send to caller");

			arrayData = (String[]) _alRecord.toArray(new String[_alRecord.size()]);

//			mlogger.info("End of reading POIREadExcel()");

		} catch (Exception e) {
		System.out.println(" POIReadExcel :: readExcelFile() : "+ e.getMessage());
		    throw new Exception(e.getMessage());
			// e.printStackTrace();
			//throw new Exception("Added new column 'Min Selling Price' to template.Download template and add 'Min Selling Price' column in between Price and Cost and do 'Import'.");
		} finally {

			inp.close();

		}

		arrayData = (String[]) _alRecord.toArray(new String[_alRecord.size()]);
		mlogger.info("Data length : " + arrayData.length);
		return arrayData;
	}
	
	
	public String[] readExcelFileCustTemplate(int no_of_column,String moduleType,int no_of_excel_column) throws Exception {

		String[] arrayData = null;
		ArrayList _alRecord = new ArrayList();

		InputStream inp = null;
		HSSFWorkbook wb = null;
	        Properties dbpr;
	        InputStream dbip;
	        mapTableFileld=new HashMap();
	       
	         

		try {
		    dbip = new FileInputStream(new File(DB_PROPS_FILE));
		    dbpr = new Properties();
		    dbpr.load(dbip);
		    NumberOfColumn  = dbpr.getProperty(moduleType+"_NumberOfColumn");
            
		    if(no_of_excel_column==Integer.parseInt(NumberOfColumn))
		    {
			    for(int i=0; i<=Integer.parseInt(NumberOfColumn);i++)
	            {
	                    mapTableFileld.put(moduleType+"_TableFiled" + i,dbpr.getProperty(moduleType+"_TableFiled" + i));
	                    System.out.println(moduleType+"_TableFiled" + i + "          "+dbpr.getProperty(moduleType+"_TableFiled" + i));
	            }
		    }
		    else
		    {
		    	if(no_of_excel_column==20)
		    	{
		    		 for(int i=0; i<=no_of_excel_column;i++)
			            {
			                    
			                    if(i==20)
			                    {
			                    	 mapTableFileld.put(moduleType+"_TableFiled" + i,"sCustomerStatus");
					                    System.out.println(moduleType+"_TableFiled" + i + "          "+"sCustomerStatus");
			                    }
			                    else
			                    {
			                    	mapTableFileld.put(moduleType+"_TableFiled" + i,dbpr.getProperty(moduleType+"_TableFiled" + i));
				                    System.out.println(moduleType+"_TableFiled" + i + "          "+dbpr.getProperty(moduleType+"_TableFiled" + i));
			                    }
			            }
		    		
		    	}
		    }
			try {
                        
			
				inp = new FileInputStream(_fileName);
				wb = new HSSFWorkbook(new POIFSFileSystem(inp));

			} catch (FileNotFoundException fn) {
				throw new Exception("Exception : " + fn.getMessage());
			}

			HSSFSheet sheet;

			if (_sheetName == "") {

				//System.out.println("sheetNo : " + _sheetNo);
				sheet = wb.getSheetAt(_sheetNo);

			} else {
				//System.out.println("SheetName : " + _sheetName);
				sheet = wb.getSheet(_sheetName);
			}

			if (sheet == null) {

				throw new Exception(
						"Sheet name : "
								+ _sheetName
								+ " not found please check the sheet name and download again. Note : Sheet name is case sensitive");
			}

		mlogger.info("*********** Total Rows found in sheet : "
					+ sheet.getPhysicalNumberOfRows());

			int dataStartRow = 65536;

			HSSFRow row = null;
			HSSFCell cell = null;

			// MLogger.info("Stage : 2");
                        
			 

			int breakcouter1 = 0;
		        String strCellValue    ="";
			for (int i = 0; i <= sheet.getPhysicalNumberOfRows()- 1; i++) {

		
//				mlogger.info("breakcouter1 : " + breakcouter1);

				if (breakcouter1 > 0) {
					mlogger.info("data counter is " + breakcouter1 + " breaing the row ");
					break;
				}

				row = sheet.getRow(i);
                                
                                
				StringBuffer data = new StringBuffer("");
			   
				// getting column data
				
								
					 if(no_of_excel_column!=no_of_column)
					 {
						 no_of_column=no_of_excel_column;
					 }
				
				for (int j = 0; j <= no_of_column-1; j++) {
				   
                    char ch= ((String)mapTableFileld.get(moduleType+"_TableFiled"+(j+1))).charAt(0);
				    String chartemp = String.valueOf(ch);
				  
				   
					try {
						//System.out.println("row :" + i);
						cell = row.getCell((short) j);
                                                    if(i==0){
					    try{
                                               
                                                    strCellValue = cell.toString();
                                                    data.append(StrUtils.fString(strCellValue));
                                                 }catch(Exception ex){ 
                                                   System.out.println("Please download the latest Customer Master Template,Few Columns are missing");
                                                   throw new Exception ("Please download the latest Customer Master Template,Few Columns are missing");
                                                }
                                          }
                                            else if (cell == null||cell.toString()=="") {
					            
					             data.append("null");
					    }else { 
                                            
                                            if(chartemp.equalsIgnoreCase("d"))
                                            {
                                                
                                   
                                              try{
                                                      switch(cell.getCellType()) { 
                                                      case HSSFCell.CELL_TYPE_NUMERIC:
                                                      double value = cell.getNumericCellValue();
                                                          Date date = HSSFDateUtil.getJavaDate(value);
                                                           SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy");      
                                                            strCellValue = dateFormat.format(date);
                                                      break;
                                                      case HSSFCell.CELL_TYPE_STRING:
                                                    
                                                          strCellValue = cell.toString().trim();
                                                          if(strCellValue.length()>0){
                                                          SimpleDateFormat dft1 = new SimpleDateFormat( "MM/dd/yyyy");   
                                                          SimpleDateFormat dateFrmt = new SimpleDateFormat( "dd/MM/yyyy"); 
                                                          Date dt = dft1.parse(strCellValue);
                                                          strCellValue = dateFrmt.format(dt);
                                                          }
                                                      break;
                                                      default:
                                                           value = cell.getNumericCellValue();
                                                           Date date1 = HSSFDateUtil.getJavaDate(value);
                                                           SimpleDateFormat dateFormat1 = new SimpleDateFormat( "dd/MM/yyyy");      
                                                            strCellValue = dateFormat1.format(date1);
                                                      }
                                                      
                                                          
                                              }catch(Exception e){
                                              System.out.println("Exception::::"+ e.getMessage());
                                              throw new Exception("Not an accepted date format at line "+(i+1));
                                              }

                                            }else if (chartemp.equalsIgnoreCase("n")){
                                                     
                                                        switch(cell.getCellType()) { 
                                                            case HSSFCell.CELL_TYPE_NUMERIC:
                                                            DecimalFormat df = new DecimalFormat("#.######");                                                                   
                                                           // System.out.print(df.format(cell.getNumericCellValue()));
                                                            double k =cell.getNumericCellValue();
                                                             strCellValue = String.valueOf(df.format(k)); 
                                                             strCellValue =StrUtils.displayDecimal(strCellValue);
                                                            break;
                                                        case HSSFCell.CELL_TYPE_STRING:
                                                         strCellValue = cell.toString().trim();
                                                          break;
                                                        default:
                                                        	  df = new DecimalFormat("#.######");                                                                   
                                                             // System.out.print(df.format(cell.getNumericCellValue()));
                                                               k =cell.getNumericCellValue();
                                                               strCellValue = String.valueOf(df.format(k)); 
                                                               strCellValue =StrUtils.displayDecimal(strCellValue);
                                                        	
                                                        }
                                                      
                                                    }
                                              
						    else if(chartemp.equalsIgnoreCase("s"))
						    {
						       
						        switch(cell.getCellType()) { 
						            case HSSFCell.CELL_TYPE_NUMERIC:
                                                            DecimalFormat df = new DecimalFormat("#.######");                                                                   
                                                          
                                                            double k =cell.getNumericCellValue();
                                                             strCellValue = String.valueOf(df.format(k)); 
                                                            
                                                             // long StrVal =(long) cell.getNumericCellValue(); 
						            //strCellValue = String.valueOf(StrVal); 
						           //  System.out.print(strCellValue);
                                                            break;
						        case HSSFCell.CELL_TYPE_STRING:
						         strCellValue = cell.toString().trim();
						         break;
						         default:
						        	 strCellValue = cell.toString().trim();
						        	 
                                                        }
						    }
						    
                                                    // To Check if the Delimiter "|" is not include in data
                                                    if(strCellValue.indexOf("|")!=-1){
                                                        throw new Exception(strCellValue + " has special character | which is not allowed "+(i+1));  
                                                    }
                                                    
                                                   
                                        
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger
										.info("********** Data equals to end increase break counter1 "
												+ breakcouter1);
								break;

							} else {
                                                                if (strCellValue.length()==0)strCellValue="null";
								data.append(strCellValue);

							}
						
                                        }

					} catch (Exception e) {
						throw e;
					}
                                       
					data.append("|||");

				}// end of column loop



				if (data.length() > 0) {
					
					if (data != null || data.length() > 0) {
						data.replace(data.length() - 1, data.length(), "");
					}
                                _alRecord.add(strutils.InsertQuotes(data.toString()));

				} else {
					System.out.println("********** Data size less than zero *************");
				}

			System.out.println("no_of_column:" + data.toString());

			}// end of row loop

//			mlogger.info("Adding data to array to send to caller");

			arrayData = (String[]) _alRecord.toArray(new String[_alRecord.size()]);

//			mlogger.info("End of reading POIREadExcel()");

		} catch (Exception e) {
		System.out.println(" POIReadExcel :: readExcelFile() : "+ e.getMessage());
		    throw new Exception(e.getMessage());
			
		} finally {

			inp.close();

		}

		arrayData = (String[]) _alRecord.toArray(new String[_alRecord.size()]);
		mlogger.info("Data length : " + arrayData.length);
		return arrayData;
	}
	
	
	public String[] readExcelFileShopify(int no_of_column,String moduleType) throws Exception {

		String[] arrayData = null;
		ArrayList _alRecord = new ArrayList();
		ArrayList _alRecordnew = new ArrayList();
		InputStream inp = null;
		HSSFWorkbook wb = null;
	        Properties dbpr;
	        InputStream dbip;
	        mapTableFileld=new HashMap();
	       
	         

		try {
		    dbip = new FileInputStream(new File(DB_PROPS_FILE));
		    dbpr = new Properties();
		    dbpr.load(dbip);
		    NumberOfColumn  = dbpr.getProperty(moduleType+"_NumberOfColumn");
                
			try {      
				inp = new FileInputStream(_fileName);
				wb = new HSSFWorkbook(new POIFSFileSystem(inp));
			} catch (FileNotFoundException fn) {
				throw new Exception("Exception : " + fn.getMessage());
			}

			HSSFSheet sheet;

			if (_sheetName == "") {

				//System.out.println("sheetNo : " + _sheetNo);
				sheet = wb.getSheetAt(_sheetNo);

			} else {
				//System.out.println("SheetName : " + _sheetName);
				sheet = wb.getSheet(_sheetName);
			}

			if (sheet == null) {

				throw new Exception(
						"Sheet name : "
								+ _sheetName
								+ " not found please check the sheet name and download again. Note : Sheet name is case sensitive");
			}

		mlogger.info("*********** Total Rows found in sheet : "
					+ sheet.getPhysicalNumberOfRows());

			int dataStartRow = 65536;

			HSSFRow row = null;
			HSSFCell cell = null;

			// MLogger.info("Stage : 2");
                        
			 
			List<ShopifyPojo> shpolist = new ArrayList<ShopifyPojo>();
			int breakcouter1 = 0;
		    String strCellValue    ="";
			for (int i = 0; i <= sheet.getPhysicalNumberOfRows()- 1; i++) {

				if (breakcouter1 > 0) {
					mlogger.info("data counter is " + breakcouter1 + " breaing the row ");
					break;
				}

				row = sheet.getRow(i);
                                
                                
				StringBuffer data = new StringBuffer("");
				
				// getting column data
				for (int j = 0; j <= no_of_column - 1; j++) {
					try {
						cell = row.getCell((short) j);
						
						if (i == 0) {
							try {
								strCellValue = cell.toString();
								data.append(StrUtils.fString(strCellValue));
								data.append("|||");
							} catch (Exception ex) {
								System.out
										.println("Please download the latest Import Template,Few Columns are missing");
								throw new Exception(
										"Please download the latest Import Template,Few Columns are missing");
							}
						} else if (cell == null || cell.toString() == "") {
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger.info("********** Data equals to end increase break counter1 " + breakcouter1);
								break;

							}else if (j == 0) {
								data.append("null");
								data.append("|||");
							} else if (j == 1) {
								data.append("null");
								data.append("|||");
							} else if (j == 7) {
								data.append("null");
								data.append("|||");
							} else if (j == 9) {
								data.append("null");
								data.append("|||");
							} else if (j == 8) {
								data.append("null");
								data.append("|||");
							} else if (j == 10) {
								data.append("null");
								data.append("|||");
							} else if (j == 13) {
								data.append("null");
								data.append("|||");
							} else if (j == 15) {
								data.append("null");
								data.append("|||");
							} else if (j == 16) {
								data.append("null");
								data.append("|||");
							} else if (j == 17) {
								data.append("null");
								data.append("|||");
							} else if (j == 18) {
								data.append("null");
								data.append("|||");
							} else if (j == 20) {
								data.append("null");
								data.append("|||");
							} else if (j == 24) {
								data.append("null");
								data.append("|||");
							} else if (j == 25) {
								data.append("null");
								data.append("|||");
							} else if (j == 26) {
								data.append("null");
								data.append("|||");
							} else if (j == 27) {
								data.append("null");
								data.append("|||");
							} else if (j == 28) {
								data.append("null");
								data.append("|||");
							} else if (j == 29) {
								data.append("null");
								data.append("|||");
							} else if (j == 30) {
								data.append("null");
								data.append("|||");
							} else if (j == 32) {
								data.append("null");
								data.append("|||");
							} else if (j == 33) {
								data.append("null");
								data.append("|||");
							}
							
						} else {
							strCellValue = cell.toString().trim();
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger.info("********** Data equals to end increase break counter1 " + breakcouter1);
								break;

							} else if (j == 0) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 1) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 7) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 8) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}else if (j == 9) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 10) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 13) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 15) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								strCellValue = strCellValue.replace("-", "/");
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 16) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 17) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 18) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 20) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 24) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 25) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 26) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 27) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 28) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 29) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 30) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 32) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 33) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}

							// To Check if the Delimiter "|" is not include in data
							if (strCellValue.indexOf("|") != -1) {
								throw new Exception(
										strCellValue + " has special character | which is not allowed " + (i + 1));
							}

						}

					} catch (Exception e) {
						throw e;
					}

					

				}// end of column loop

				

				if (data.length() > 0) {
					
					StringTokenizer parserdata = new StringTokenizer(data.toString(), "|||");
					List listdata = new LinkedList();
					listdata = new ArrayList();
			          while (parserdata.hasMoreTokens()) {
			        	  listdata.add(parserdata.nextToken());
			          }
			          if(listdata.size() > 0) {
			        	  String dono = StrUtils.replaceStr2Char(((String) listdata.get(0)).trim());
			        	  if(!dono.equalsIgnoreCase("null")) {
			        	  List<ShopifyPojo> sfilter = shpolist.stream().filter((s)->s.getDONO().equalsIgnoreCase(dono)).collect(Collectors.toList());
			        	  System.out.println("size------"+sfilter.size());
			        	  if(sfilter.size() == 0) {
				        	  ShopifyPojo shopify = new ShopifyPojo();
				        	  shopify.setDONO(StrUtils.replaceStr2Char(((String) listdata.get(0)).trim()));
				        	  shopify.setCURRENCYID(StrUtils.replaceStr2Char(((String) listdata.get(2)).trim()));
				        	  shopify.setSUBTOTAL(StrUtils.replaceStr2Char(((String) listdata.get(3)).trim()));
				        	  shopify.setSHIPPINGCOST(StrUtils.replaceStr2Char(((String) listdata.get(4)).trim()));
				        	  shopify.setTAXAMOUNT(StrUtils.replaceStr2Char(((String) listdata.get(5)).trim()));
				        	  shopify.setDISCOUNTAMOUNT(StrUtils.replaceStr2Char(((String) listdata.get(6)).trim()));
				        	  shopify.setBILLNAME(StrUtils.replaceStr2Char(((String) listdata.get(12)).trim()));
				        	  shopify.setBILLSTREET(StrUtils.replaceStr2Char(((String) listdata.get(13)).trim()));
				        	  shopify.setBILLADD1(StrUtils.replaceStr2Char(((String) listdata.get(14)).trim()));
				        	  shopify.setBILLADD2(StrUtils.replaceStr2Char(((String) listdata.get(15)).trim()));
				        	  shopify.setBILLCOMPANY(StrUtils.replaceStr2Char(((String) listdata.get(16)).trim()));
				        	  shopify.setBILLCITY(StrUtils.replaceStr2Char(((String) listdata.get(17)).trim()));
				        	  shopify.setBILLZIP(StrUtils.replaceStr2Char(((String) listdata.get(18)).trim()));
				        	  shopify.setBILLCOUNTRY(StrUtils.replaceStr2Char(((String) listdata.get(19)).trim()));
				        	  shopify.setBILLPHONE(StrUtils.replaceStr2Char(((String) listdata.get(20)).trim()));
				        	  shpolist.add(shopify);
			        	  }
			        	  }
			          }
					
					if (data != null || data.length() > 0) {
						data.replace(data.length() - 1, data.length(), "");
					}
                                _alRecord.add(strutils.InsertQuotes(data.toString()));

				} else {
					System.out.println("********** Data size less than zero *************");
				}

			System.out.println("no_of_column:" + data.toString());

			}// end of row loop

//			mlogger.info("Adding data to array to send to caller");
			String olddono = "";
			int lino = 0;
			
			 for (int iCnt =0; iCnt<_alRecord.size(); iCnt++){
		          String lineArr = (String) _alRecord.get(iCnt);
		          StringTokenizer parser = new StringTokenizer(lineArr, "|||");
		          List list = new LinkedList();
		          list = new ArrayList();
		          while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
		          }
		          if(list.size() > 0) {
		        	  String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
		        	  if(!dono.equalsIgnoreCase("null")) {
		        	  if(dono.equalsIgnoreCase(olddono)) {
		        		  List<ShopifyPojo> sfilter = shpolist.stream().filter((s)->s.getDONO().equalsIgnoreCase(dono)).collect(Collectors.toList());
			        	  if(sfilter.size() != 0) {
			        		  String ldono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
			        		  String lemail = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
			        		  String lcurrency = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
			        		  String lsubtotal = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
			        		  String lshipping = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
			        		  String ltax = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
			        		  String ldiscamount = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
			        		  String lcreateat = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
			        		  String lqty = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
			        		  String litemdisc = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
			        		  String lunitprice = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
			        		  String litem = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
			        		  String lcustname = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
			        		  String lstreet = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
			        		  String ladd1 = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
			        		  String ladd2 = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
			        		  String lcompany = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
			        		  String lcity = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
			        		  String lzip = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
			        		  String lcountry = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
			        		  String lphone = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
			        		  
			        		for (ShopifyPojo shopifyPojo : sfilter) {
			        			lcurrency = shopifyPojo.getCURRENCYID();
			        			lsubtotal = shopifyPojo.getSUBTOTAL();
			        			lshipping = shopifyPojo.getSHIPPINGCOST();
			        			ltax = shopifyPojo.getTAXAMOUNT();
			        			ldiscamount = shopifyPojo.getDISCOUNTAMOUNT();
			        			lcustname = shopifyPojo.getBILLNAME();
			        			lstreet = shopifyPojo.getBILLSTREET();
			        			ladd1 = shopifyPojo.getBILLADD1();
			        			ladd2 = shopifyPojo.getBILLADD2();
			        			lcompany = shopifyPojo.getBILLCOMPANY();
			        			lcity = shopifyPojo.getBILLCITY();
			        			lzip = shopifyPojo.getBILLZIP();
			        			lcountry = shopifyPojo.getBILLCOUNTRY();
			        			lphone = shopifyPojo.getBILLPHONE();
							}
			        		
			        		String linnar = ldono+"|||"+lemail+"|||"+lcurrency+"|||"+lsubtotal+"|||"+lshipping+"|||"+ltax+"|||"+ldiscamount+"|||"+lcreateat+"|||"+lqty+"|||"+litemdisc+"|||"+lunitprice+"|||"+litem+"|||"+lcustname+"|||"+lstreet+"|||"+ladd1+"|||"+ladd2+"|||"+lcompany+"|||"+lcity+"|||"+lzip+"|||"+lcountry+"|||"+lphone+"|||"+(lino+1)+"||";
			        		_alRecordnew.add(strutils.InsertQuotes(linnar));
			        		lino = lino + 1;
			        	  }
		        	  }else {
		        		  olddono = dono;
		        		  lino = 0;
		        		  lineArr = lineArr+"|0||";
		        		  _alRecordnew.add(strutils.InsertQuotes(lineArr));
		        	  }
		        	  }
		        	  
		          }
			}

			arrayData = (String[]) _alRecordnew.toArray(new String[_alRecordnew.size()]);

//			mlogger.info("End of reading POIREadExcel()");

		} catch (Exception e) {
		System.out.println(" POIReadExcel :: readExcelFile() : "+ e.getMessage());
		    throw new Exception(e.getMessage());
			// e.printStackTrace();
			//throw new Exception("Added new column 'Min Selling Price' to template.Download template and add 'Min Selling Price' column in between Price and Cost and do 'Import'.");
		} finally {

			inp.close();

		}

		arrayData = (String[]) _alRecordnew.toArray(new String[_alRecordnew.size()]);
		mlogger.info("Data length : " + arrayData.length);
		return arrayData;
	}
	
	public String[] readExcelFileShopee(int no_of_column,String moduleType) throws Exception {

		String[] arrayData = null;
		ArrayList _alRecord = new ArrayList();
		ArrayList _alRecordnew = new ArrayList();
		InputStream inp = null;
		HSSFWorkbook wb = null;
	        Properties dbpr;
	        InputStream dbip;
	        mapTableFileld=new HashMap();
	       
	         

		try {
		    dbip = new FileInputStream(new File(DB_PROPS_FILE));
		    dbpr = new Properties();
		    dbpr.load(dbip);
		    NumberOfColumn  = dbpr.getProperty(moduleType+"_NumberOfColumn");
                
			try {      
				inp = new FileInputStream(_fileName);
				wb = new HSSFWorkbook(new POIFSFileSystem(inp));
			} catch (FileNotFoundException fn) {
				throw new Exception("Exception : " + fn.getMessage());
			}

			HSSFSheet sheet;

			if (_sheetName == "") {

				//System.out.println("sheetNo : " + _sheetNo);
				sheet = wb.getSheetAt(_sheetNo);

			} else {
				//System.out.println("SheetName : " + _sheetName);
				sheet = wb.getSheet(_sheetName);
			}

			if (sheet == null) {

				throw new Exception(
						"Sheet name : "
								+ _sheetName
								+ " not found please check the sheet name and download again. Note : Sheet name is case sensitive");
			}

		mlogger.info("*********** Total Rows found in sheet : "
					+ sheet.getPhysicalNumberOfRows());

			int dataStartRow = 65536;

			HSSFRow row = null;
			HSSFCell cell = null;

			// MLogger.info("Stage : 2");
                        
			 
			List<ShopifyPojo> shpolist = new ArrayList<ShopifyPojo>();
			int breakcouter1 = 0;
		    String strCellValue    ="";
			for (int i = 0; i <= sheet.getPhysicalNumberOfRows()- 1; i++) {

				if (breakcouter1 > 0) {
					mlogger.info("data counter is " + breakcouter1 + " breaing the row ");
					break;
				}

				row = sheet.getRow(i);
                                
                                
				StringBuffer data = new StringBuffer("");
				
				// getting column data
				for (int j = 0; j <= no_of_column - 1; j++) {
					try {
						cell = row.getCell((short) j);
						
						if (i == 0) {
							try {
								strCellValue = cell.toString();
								data.append(StrUtils.fString(strCellValue));
								data.append("|||");
							} catch (Exception ex) {
								System.out
										.println("Please download the latest Import Template,Few Columns are missing");
								throw new Exception(
										"Please download the latest Import Template,Few Columns are missing");
							}
						} else if (cell == null || cell.toString() == "") {
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger.info("********** Data equals to end increase break counter1 " + breakcouter1);
								break;

							}else if (j == 0) {
								data.append("null");
								data.append("|||");
							} /*else if (j == 1) {
								data.append("null");
								data.append("|||");
							} else if (j == 7) {
								data.append("null");
								data.append("|||");
							}*/ else if (j == 37) {
								data.append("null");
								data.append("|||");
							} else if (j == 43) {
								data.append("null");
								data.append("|||");
							} /*else if (j == 10) {
								data.append("null");
								data.append("|||");
							} else if (j == 13) {
								data.append("null");
								data.append("|||");
							}*/ else if (j == 6) {
								data.append("null");
								data.append("|||");
							} else if (j == 20) {
								data.append("null");
								data.append("|||");
							} else if (j == 15) {
								data.append("null");
								data.append("|||");
							}else if (j == 17) {
								data.append("null");
								data.append("|||");
							} else if (j == 18) {
								data.append("null");
								data.append("|||");
							} else if (j == 16) {
								data.append("null");
								data.append("|||");
							} else if (j == 45) {
								data.append("null");
								data.append("|||");
							} else if (j == 51) {
								data.append("null");
								data.append("|||");
							} else if (j == 47) {
								data.append("null");
								data.append("|||");
							} /*else if (j == 27) {
								data.append("null");
								data.append("|||");
							}*/ else if (j == 45) {
								data.append("null");
								data.append("|||");
							} else if (j == 50) {
								data.append("null");
								data.append("|||");
							} else if (j == 53) {
								data.append("null");
								data.append("|||");
							} else if (j == 52) {
								data.append("null");
								data.append("|||");
							} else if (j == 46) {
								data.append("null");
								data.append("|||");
							}
							
						} else {
							strCellValue = cell.toString().trim();
							if (strCellValue.equalsIgnoreCase("end")) {

								breakcouter1++;
								mlogger.info("********** Data equals to end increase break counter1 " + breakcouter1);
								break;

							} else if (j == 0) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} /*else if (j == 1) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 7) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}*/ else if (j == 37) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}else if (j == 43) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} /*else if (j == 10) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 13) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}*/ else if (j == 6) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 20) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 15) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 17) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 18) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 16) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 45) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 51) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 47) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} /*else if (j == 27) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}*/ else if (j == 45) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 50) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 53) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 52) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							} else if (j == 46) {
								if (strCellValue.length() == 0)
									strCellValue = "null";
								data.append(strCellValue);
								data.append("|||");
							}

							// To Check if the Delimiter "|" is not include in data
							if (strCellValue.indexOf("|") != -1) {
								throw new Exception(
										strCellValue + " has special character | which is not allowed " + (i + 1));
							}

						}

					} catch (Exception e) {
						throw e;
					}

					

				}// end of column loop

				

				if (data.length() > 0) {
					
					StringTokenizer parserdata = new StringTokenizer(data.toString(), "|||");
					List listdata = new LinkedList();
					listdata = new ArrayList();
			          while (parserdata.hasMoreTokens()) {
			        	  listdata.add(parserdata.nextToken());
			          }
			          if(listdata.size() > 0) {
			        	  String dono = StrUtils.replaceStr2Char(((String) listdata.get(0)).trim());
			        	  if(!dono.equalsIgnoreCase("null")) {
			        	  List<ShopifyPojo> sfilter = shpolist.stream().filter((s)->s.getDONO().equalsIgnoreCase(dono)).collect(Collectors.toList());
			        	  System.out.println("size------"+sfilter.size());
			        	  if(sfilter.size() == 0) {
				        	  ShopifyPojo shopify = new ShopifyPojo();
				        	  shopify.setDONO(StrUtils.replaceStr2Char(((String) listdata.get(0)).trim()));
				        	  //shopify.setCURRENCYID(StrUtils.replaceStr2Char(((String) listdata.get(2)).trim()));
				        	  shopify.setSUBTOTAL(StrUtils.replaceStr2Char(((String) listdata.get(7)).trim()));
				        	  shopify.setSHIPPINGCOST(StrUtils.replaceStr2Char(((String) listdata.get(8)).trim()));
				        	  shopify.setTAXAMOUNT("0");
				        	  shopify.setDISCOUNTAMOUNT("0");
				        	  shopify.setBILLNAME(StrUtils.replaceStr2Char(((String) listdata.get(9)).trim()));
				        	  shopify.setBILLSTREET(StrUtils.replaceStr2Char(((String) listdata.get(12)).trim()));
				        	  shopify.setBILLADD1(StrUtils.replaceStr2Char(((String) listdata.get(11)).trim()));
				        	  shopify.setBILLADD2("");
				        	  shopify.setBILLCOMPANY(StrUtils.replaceStr2Char(((String) listdata.get(9)).trim()));
				        	  shopify.setBILLCITY(StrUtils.replaceStr2Char(((String) listdata.get(13)).trim()));
				        	  shopify.setBILLZIP(StrUtils.replaceStr2Char(((String) listdata.get(15)).trim()));
				        	  shopify.setBILLCOUNTRY(StrUtils.replaceStr2Char(((String) listdata.get(14)).trim()));
				        	  shopify.setBILLPHONE(StrUtils.replaceStr2Char(((String) listdata.get(10)).trim()));
				        	  shpolist.add(shopify);
			        	  }
			        	  }
			          }
					
					if (data != null || data.length() > 0) {
						data.replace(data.length() - 1, data.length(), "");
					}
                                _alRecord.add(strutils.InsertQuotes(data.toString()));

				} else {
					System.out.println("********** Data size less than zero *************");
				}

			System.out.println("no_of_column:" + data.toString());

			}// end of row loop

//			mlogger.info("Adding data to array to send to caller");
			String olddono = "";
			int lino = 1;
			
			 for (int iCnt =0; iCnt<_alRecord.size(); iCnt++){
		          String lineArr = (String) _alRecord.get(iCnt);
		          StringTokenizer parser = new StringTokenizer(lineArr, "|||");
		          List list = new LinkedList();
		          list = new ArrayList();
		          while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
		          }
		          if(list.size() > 0) {
		        	  String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
		        	  if(!dono.equalsIgnoreCase("null")) {
		        	  if(dono.equalsIgnoreCase(olddono)) {
		        		  List<ShopifyPojo> sfilter = shpolist.stream().filter((s)->s.getDONO().equalsIgnoreCase(dono)).collect(Collectors.toList());
			        	  if(sfilter.size() != 0) {
			        		  String ldono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
			        		  //String lemail = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
			        		  //String lcurrency = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
			        		  String lsubtotal = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
			        		  String lshipping = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
			        		  String ltax = StrUtils.replaceStr2Char(("0.0").trim());
			        		  String ldiscamount = StrUtils.replaceStr2Char(("0.0").trim());
			        		  String lcreateat = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
			        		  String lqty = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
			        		  String litemdisc = StrUtils.replaceStr2Char(((String) list.get(2)).trim()) 
			        				  + " " +StrUtils.replaceStr2Char(((String) list.get(4)).trim());
			        		  String lunitprice = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
			        		  String litem = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
			        		  String lcustname = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
			        		  String lstreet = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
			        		  String ladd1 = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
			        		  String ladd2 = StrUtils.replaceStr2Char(("").trim());
			        		  String lcompany = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
			        		  String lcity = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
			        		  String lzip = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
			        		  String lcountry = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
			        		  String lphone = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
			        		  
			        		for (ShopifyPojo shopifyPojo : sfilter) {
			        			//lcurrency = shopifyPojo.getCURRENCYID();
			        			lsubtotal = shopifyPojo.getSUBTOTAL();
			        			lshipping = shopifyPojo.getSHIPPINGCOST();
			        			ltax = shopifyPojo.getTAXAMOUNT();
			        			ldiscamount = shopifyPojo.getDISCOUNTAMOUNT();
			        			lcustname = shopifyPojo.getBILLNAME();
			        			lstreet = shopifyPojo.getBILLSTREET();
			        			ladd1 = shopifyPojo.getBILLADD1();
			        			ladd2 = shopifyPojo.getBILLADD2();
			        			lcompany = shopifyPojo.getBILLCOMPANY();
			        			lcity = shopifyPojo.getBILLCITY();
			        			lzip = shopifyPojo.getBILLZIP();
			        			lcountry = shopifyPojo.getBILLCOUNTRY();
			        			lphone = shopifyPojo.getBILLPHONE();
							}
			        		
			        		//String linnar = ldono+"|||"+lemail+"|||"+lcurrency+"|||"+lsubtotal+"|||"+lshipping+"|||"+ltax+"|||"+ldiscamount+"|||"+lcreateat+"|||"+lqty+"|||"+litemdisc+"|||"+lunitprice+"|||"+litem+"|||"+lcustname+"|||"+lstreet+"|||"+ladd1+"|||"+ladd2+"|||"+lcompany+"|||"+lcity+"|||"+lzip+"|||"+lcountry+"|||"+lphone+"|||"+(lino+1)+"||";
			        		/*String linnar = ldono+"|||"+lsubtotal+"|||"+lshipping+"|||"+ltax+"|||"+ldiscamount+"|||"+lcreateat+"|||"+lqty+"|||"+litemdisc+"|||"+lunitprice+"|||"+litem+"|||"+lcustname+"|||"+lstreet+"|||"+ladd1+"|||"+ladd2+"|||"+lcompany+"|||"+lcity+"|||"+lzip+"|||"+lcountry+"|||"+lphone+"|||"+(lino+1)+"||";
			        		_alRecordnew.add(strutils.InsertQuotes(linnar));*/
			        		lineArr = lineArr+"|"+(lino+1)+"||";
			        		_alRecordnew.add(strutils.InsertQuotes(lineArr));
			        		lino = lino + 1;
			        	  }
		        	  }else {
		        		  olddono = dono;
		        		  lino = 1;
		        		  lineArr = lineArr+"|1||";
		        		  _alRecordnew.add(strutils.InsertQuotes(lineArr));
		        	  }
		        	  }
		        	  
		          }
			}

			arrayData = (String[]) _alRecordnew.toArray(new String[_alRecordnew.size()]);

//			mlogger.info("End of reading POIREadExcel()");

		} catch (Exception e) {
		System.out.println(" POIReadExcel :: readExcelFile() : "+ e.getMessage());
		    throw new Exception(e.getMessage());
			// e.printStackTrace();
			//throw new Exception("Added new column 'Min Selling Price' to template.Download template and add 'Min Selling Price' column in between Price and Cost and do 'Import'.");
		} finally {

			inp.close();

		}

		arrayData = (String[]) _alRecordnew.toArray(new String[_alRecordnew.size()]);
		mlogger.info("Data length : " + arrayData.length);
		return arrayData;
	}
	
	public static void main(String[] args) {
		
		
               }
	

}
