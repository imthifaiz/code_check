<!--
function validateEditor(form)
{   
    if (form.MSG_FREQUENCY.value.length < 1)
    {
    alert("Please Choose the MSG Frequency..!")
    return false;
    }
    
    if (form.MSG_GROUP.value.length < 1)
    {
    alert("Please Choose the MSG Group..!")
    return false;
    }
    
    if (form.PRIORITY_CODE.value.length < 1)
    {
    alert("Please Choose the Priority Code..!")
    return false;
    }

    if (form.PRIORITY_CODE.value == 1)
    {
        if(form.FILTER_TYPE.value != 0) 
        {
            alert("Please Choose the Filter Type as DO NOT FILTER \nIf Priority Code is selected as HIGH");
            form.FILTER_TYPE.focus();
            return false;
        }
        if(form.OVERWRITE_WGT.value.length != 0) 
        {
            alert("Filter Type, Overwrite Weight and Filter Record will be ignored if Priority Code is HIGH");            
            form.OVERWRITE_WGT.value ="";
        }
        form.FILTER_REC[3].checked = 'true';
    }

    if (form.VAL_PERIOD.value > 144)
    {
    alert("Please Key in the Validity Period less than 144 hours..!")
    return false;
    }

    if (form.START_DELIVER.value.length != 4)
    {
    alert("Invalid Start Time ..!")
    return false;
    }

    if (form.STOP_DELIVER.value.length != 4)
    {
    alert("Invalid Stop Time ..!")
    return false;
    }
	 
    if (form.LOAD_FORMAT.value.length < 2)
    {
    alert("Please enter the Load fomat ..!")
    return false;
    }
    return true;
}

function checkWgt()
{ 
     if(document.editor.OVERWRITE_WGT.value.length < 1)
     alert("Overwrite Weight is mandatory if Group Batch is selected as Filter Type..!")
     return false;
}

function warn(form)
{
    alert ("Please do not alter the SMS Code...!")
    document.editor.MSG_FREQUENCY.focus();
    return false;
}

function changeState(form)
{
    if(form.FILTER_TYPE.value==0)
    {
        for(i=0;i<4;i++)
        form.FILTER_REC[i].disabled=true;
    }
    else
    {
        for(i=0;i<4;i++)
        form.FILTER_REC[i].disabled=false;
    }
}

// -->