
<!--
function validateSMSFormat(form,eng_footer,big5_footer)
{

    if (form.SMS_FORMAT.value.length < 1)
    {
    alert("Please enter the SMS Format in Message Text..!")
    form.SMS_FORMAT.focus();
    return false;
    }


    var language   = form.SMS_LANGUAGE.value;
        language   = language.toLowerCase();
    var sms_format = form.SMS_FORMAT.value;
    var footer     = form.FOOTER.value;
    var maxlimit   = '';

    if(language=="c") maxlimit = big5_footer; else maxlimit = eng_footer;

    if(language=="e")
    {
            for(i=0,n = sms_format.length; i< n;i++)
            {
                if(sms_format.charCodeAt(i)>127)
                {
                    alert('Non - English Character not allowed');
                    return false;
                }
             }
    }

    if(footer.length>maxlimit)
    {
            alert('Footer Content Exceeds '+maxlimit+' characters'+'\nPlease do not exceed the maximum limit');
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

function checkLang(form)
{
        var language = form.SMS_LANGUAGE.value;
        language = language.toLowerCase();
        inputMsg  = form.SMS_FORMAT.value;

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
