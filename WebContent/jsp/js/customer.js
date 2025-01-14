function popUpWin(URL) {
 document.form1.CUSTNO.value = "";
 document.form1.CNAME.value = "";
 document.form1.ADDR1.value = "";
 document.form1.ADDR2.value = "";
 document.form1.ADDR3.value = "";
 document.form1.STATE.value = "";
 document.form1.COUNTRY.value = "";
 document.form1.ZIP.value = "";
 document.form1.USERFLD1.value = "";
 document.form1.USERFLD2.value = "";
 subWin = window.open(URL, 'Customers', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onAdd(){
//alert("In onAdd()");
     var sCustNo   = document.form1.CUSTNO.value;
     var sCustName = document.form1.CNAME.value;
     if(sCustNo == "" || sCustNo == null) {alert("Please Enter the Customer Number"); document.form1.CUSTNO.focus(); return false; }
     if(sCustName == "" || sCustName == null) {alert("Please Enter the Customer Name"); document.form1.CNAME.focus(); return false; }
     //document.form1.action = "customer_process.jsp";
     //document.form1.submit();
}
function onModify(){
//alert("In onAdd()");
     var sCustNo   = document.form1.CUSTNO.value;
     var sCustName = document.form1.CNAME.value;
     if(sCustNo == "" || sCustNo == null) {alert("Please Choose the Customer Number"); document.form1.CUSTNO.focus(); return false; }
     if(sCustName == "" || sCustName == null) {alert("Please Enter the Customer Name"); document.form1.CNAME.focus(); return false; }
}
function onDelete(){
//alert("In onAdd()");
     var sCustNo   = document.form1.CUSTNO.value;
     if(sCustNo == "" || sCustNo == null) {alert("Please Choose the Customer Number"); return false; }
     return confirm ("Are you sure want to delete the Customer Master?");
}
function onView(){
//alert("In onAdd()");
     var sCustNo   = document.form1.CUSTNO.value;
     if(sCustNo == "" || sCustNo == null) {alert("Please Choose the Customer Number"); return false; }
}

