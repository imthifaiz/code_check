<%

java.util.List do_list = new java.util.ArrayList();
com.track.util.StrUtils strUtils     = new com.track.util.StrUtils();
com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
com.track.db.util.InvUtil  invUtil= new com.track.db.util.InvUtil();
java.util.Hashtable ht = new java.util.Hashtable();
java.util.List do_list1 = new java.util.ArrayList();

String LOC ="",  ITEM = "", STATUS ="",QTY ="",QTYALLOC ="",xlaction="";

LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
xlaction=strUtils.fString(request.getParameter("xlAction"));



        

if(xlaction.equalsIgnoreCase("GenerateXLSheet")){
try{
   
   do_list =(java.util.ArrayList)request.getSession().getAttribute("InventryExcelResult");
   request.getSession().setAttribute("InventryExcelResult","");
  
if(do_list.size() > 0)
{ 
  response.setContentType("application/vnd.ms-excel");
  response.setHeader("Content-disposition", "attachment; filename=InventoryExcel.xls");

  out.println("Item Number\tItem Description\tLocation\tCustomer\tStatus");
  for(int i = 0; i<do_list.size(); i++){
     
   try{
 
  do_list1 = (java.util.ArrayList)do_list.get(i);
  
  out.println((String)do_list1.get(0)+"\t"+ (String)do_list1.get(1) +"\t"+(String)do_list1.get(2)+"\t"+(String)do_list1.get(3)+"\t"+(String)do_list1.get(4));
  
  }catch(Exception ee){System.out.println("######################## MovementExcel ################ :"+ee);}
  }
}else if(do_list.size() < 1){

response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-disposition", "attachment; filename=InventoryExcel.xls");
out.println("No Records Found To List");
}

}catch(Exception e){}
}


%>
