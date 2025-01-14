<!--
function popUpWin(URL) {
 subWin = window.open(URL, 'Vendors', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onAdd(){
//alert("In onAdd()");
     var vno   = document.form.VENDNO.value;
     var vname = document.form.VNAME.value;
     //alert("vno " + vno);
     //alert("vname " + vname);
     if(vno == "" || vno == null) {alert("Please Enter the vendor Number"); document.form.VENDNO.focus(); return false; }
     if(vname == "" || vname == null) {alert("Please Enter the Vendor Name"); document.form.VNAME.focus(); return false; }
     document.form.action = "vendor_process.jsp";
     document.form.submit();
}
function onModify(){
//alert("In onAdd()");
     var vno   = document.form.VENDNO.value;
     var vname = document.form.VNAME.value;
     //alert("vno " + vno);
     //alert("vname " + vname);
     if(vno == "" || vno == null) {alert("Please Enter the vendor Number"); document.form.VENDNO.focus(); return false; }
     if(vname == "" || vname == null) {alert("Please Enter the Vendor Name"); document.form.VNAME.focus(); return false; }
     document.form.action = "vendor_process.jsp";
     document.form.submit();
}
function onDelete(){
     return confirm ("Are you sure want to delete the vendor Master?");
     document.form.action = "vendor_process.jsp";
     document.form.submit();
}
function onBack(){
   window.location.href='indexPage.jsp';
}
//-->