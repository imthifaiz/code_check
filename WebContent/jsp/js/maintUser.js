<!--
function validateUser(form)
{
   
    if (form.PASSWORD.value.length < 6)
    {
    alert("Please Fill in the Password with 6 - 10 characters..!");
    return false;
    }
    
    if (form.USER_ID.value == form.PASSWORD.value)
    {
    alert("Please choose a password different from User ID..!")
    return false;
    }

    if (form.PASSWORD.value != form.CPASSWORD.value)
    {
    alert("Your Password and Confirm Password does not match..!")
    return false;
    }

    if (form.USER_NAME.value.length < 1)
    {
    alert("Please Fill in the User Name ..!")
    form.USER_NAME.focus();
    return false;
    }

    if (form.EFFECTIVE_DATE.value.length > 0)
    {
       if (form.EFFECTIVE_DATE.value.length < 8)
        {
        alert("Invalid Date ..!")
        form.EFFECTIVE_DATE.focus();
        return false;
        }
    }

    if (form.USER_LEVEL.value.length < 1)
    {
    alert("Please Choose the User Level ..!")
    form.USER_LEVEL.focus();
    return false;
    }

}

function clearText(form)
{
    form.PASSWORD.value="";
    form.CPASSWORD.value="";
    form.ENCRYPT_FLAG.value=1;
} 

// -->