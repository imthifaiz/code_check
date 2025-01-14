<!--
function validatePwd(form)
{
    if (form.PASSWORD.value.length < 5)
    {
    alert("Please Fill in the Old Password with 5 - 10 characters..!");
    form.PASSWORD.focus();
    return false;
    }
    
   else if (form.NPASSWORD.value.length < 5)
    {
    alert("Please Fill in the New Password with 5 - 10 characters..!");
    form.NPASSWORD.focus();
    return false;
    }

  else if (form.PASSWORD.value == form.NPASSWORD.value)
    {
    alert("Please choose a password different from old password..!")
    return false;
    }

  else if (form.USER_ID.value == form.NPASSWORD.value)
    {
    alert("Please choose a password different from User ID..!")
    return false;
    }

   else if (form.NPASSWORD.value != form.CPASSWORD.value)
    {
    alert("Your New Password and Confirm Password does not match..!")
    return false;
    }
    else
    {
    return true;
    }

}

// -->