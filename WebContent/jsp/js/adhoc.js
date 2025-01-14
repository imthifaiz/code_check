<!--
function validateAdhoc(form,eng_length,big5_length)
{
    if ((form.MOBILE_LIST.value.length < 3) && (form.PHONE_FILE.value.length < 1))
    {
    alert("Please enter the Mobile number..!")
    form.MOBILE_LIST.focus();
    return false;
    }
 
    if (form.MOBILE_LIST.value.length >200)
    {
    alert('Mobile Number List Exceeds 200 characters'+'\nPlease do not exceed the maximum limit')
    form.MOBILE_LIST.focus();
    return false;
    }
   
    if ((form.MSG_TEXT.value.length < 1) && (form.MSG_FILE.value.length < 1))
    {
    alert("Please enter the Message..!")
    form.MSG_TEXT.focus();
    return false;
    }
    

    var language = form.SMS_LANGUAGE.value;
        language = language.toLowerCase();
    var msg_text = form.MSG_TEXT.value;
    var maxlimit = '';
        
    if(language=="c") maxlimit = big5_length; else maxlimit = eng_length;
    
    if(language=="e")
    {  
            for(i=0,n = msg_text.length; i< n;i++)
            {
                if(inputMsg.charCodeAt(i)>127)
                {
                    alert('Non - English Character not allowed');
                    return false;
                }
             }    
    }

    if(msg_text.length>maxlimit) 
    {
            alert('Message Content Exceeds '+maxlimit+' characters'+'\nPlease do not exceed the maximum limit');
            return false;
    }
    return true;
}


function textFocus(obj,event) {

// IME disallowed for character set other than BIG_5

        var language = form.SMS_LANGUAGE.value;
        language = language.toLowerCase();

        if(language=="c")
            obj.style.imeMode="active"; 
        else
	    obj.style.imeMode="disabled"; 
        
   return true;
}

function textareaMax(myfield, myfield1, eng_length, big5_length) 
{
        var maxlimit = '';
        var language = form.SMS_LANGUAGE.value;
        language = language.toLowerCase();
        if(language=="c") maxlimit = big5_length; else maxlimit = eng_length;

        inputMsg  = myfield.value;
	myfield1.value = inputMsg.length;   
      
	if (inputMsg.length >= maxlimit) // if too long...trim it!
	{
		alert('Your message exceeds the maximum range..!');
	}
    
        if(language=="e")
        {  
            for(i=0,n = inputMsg.length; i< n;i++)
            {
                if(inputMsg.charCodeAt(i)>127)
                {
                    alert('Non - English Character not allowed' + '\nPlease Change the Language');
                    break;
                }
            }
        }
}

//-->
