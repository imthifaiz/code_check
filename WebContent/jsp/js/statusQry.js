<!--
function validateSearch(form)
{

   if (form.MOBILE_LIST.value.length < 3)
    {
	    if (form.SMS_CODE.value.length < 1)
    	{
   		 alert("Please Choose the SMS Code..or Key in the Mobile number..!")
    	return false;
    	}

	    if (form.DATE_FROM.value.length < 8)
    	{
   		 alert("Invalid From Date..!")
    	return false;
    	}

	    if (form.DATE_TO.value.length < 8)
    	{
   		 alert("Invalid To Date..!")
    	return false;
    	}
    }
   
    if (form.MOBILE_LIST.value.length >= 3)
    {
	    if (form.DATE_FROM.value.length > 0 && form.DATE_FROM.value.length < 8)
    	{
   		 alert("Invalid From Date..!")
    	return false;
    	}

	    if (form.DATE_TO.value.length > 0 && form.DATE_TO.value.length < 8)
    	{
   		 alert("Invalid To Date..!")
    	return false;
    	}
    }
}
// -->