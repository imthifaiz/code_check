//<!--

function validateUser(form)
{
	 var ValidNumber   = form.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" user you can create"); return false; }
   
	var checkadmin = "0";
    if (form.USER_ID.value.length < 2)
    {
    alert("Please Fill in the User ID with 2 - 10 characters..!");
    form.USER_ID.focus();
    return false;
    }

  else  if (form.PASSWORD.value.length < 5)
    {
    alert("Please Fill in the Password with 5 - 10 characters..!");
    form.PASSWORD.focus();
    return false;
    }

  else  if (form.USER_ID.value == form.PASSWORD.value)
    {
    alert("Please choose a password different from User ID..!");
    return false;
    }

   else if (form.PASSWORD.value != form.CPASSWORD.value)
    {
    alert("Your Password and Confirm Password does not match..!");
    return false;
    }

  else  if (form.USER_NAME.value.length < 1)
    {
    alert("Please Fill in the User Name ..!");
    form.USER_NAME.focus();
    return false;
    }
  else if(form.DEPT.value.length < 1)
//    else if(form.DEPT.selectedIndex==0)
    {
     alert("Please Choose the Company!");
    form.DEPT.focus();
    return false;
    }
  else if(form.USER_LEVEL.selectedIndex=="0")
  {
   alert("Please Choose the User Level Inventory!");
  form.USER_LEVEL.focus();
  return false;
  }
  else if(form.USER_LEVEL_ACCOUNTING.selectedIndex=="0")
  {
   alert("Please Choose the User Level Accounting!");
  form.USER_LEVEL_ACCOUNTING.focus();
  return false;
  }
  else if(form.USER_LEVEL_PAYROLL.selectedIndex=="0")
  {
   alert("Please Choose the User Level Payroll!");
  form.USER_LEVEL_PAYROLL.focus();
  return false;
  }     
  else  if (form.EFFECTIVE_DATE.value.length > 0)
    {
       if (form.EFFECTIVE_DATE.value.length < 8)
        {
        alert("Invalid Date ..!");
        form.EFFECTIVE_DATE.focus();
        return false;
        }
    } else if ($('#isApprovalAdmin').is(':checked')) {
	   	if ($('#isPurchaseApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isPurchaseRetApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isSalesApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isSalesRetApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	
	   	if(checkadmin == "0"){
	   		alert("Please Select Minimun One Approval As Admin");
	   		return false;
	   	}
	   } 
	   
   
	    
	 return true;
   
    
    

}

function validateInput(event) {
    const key = String.fromCharCode(event.which || event.keyCode);
    
    // Allow digits, spaces, commas, semicolons, and plus signs
    const regex = /[\d\s,+;]/;

    if (regex.test(key)) {
        return true;
    } else {
        event.preventDefault();  // Prevent the keypress if it's not valid
        alert("Only (0-9)+;, and spaces special characters are allowed.");
        return false;
    }

}


// -->