
function validate()
{
	 if (document.loginForm.company.value.length < 0)
	{
		alert("Please fill in the Company Code ")
	return false;
	}
   if (document.loginForm.pwd.value.length < 0)
	{
		alert("Please fill in the Password ")
	return false;
	}
  if (document.loginForm.name.value.length < 0)
	{
		alert("Please fill in the UserID ")
	return false;
	}
 if (document.loginForm.name.value.length < 2)
	{
		alert("Please fill in the Login Name with 2 - 10 characters!")
	return false;
	}
 	if (document.loginForm.pwd.value.length < 5)
	{
		alert("Please fill in the Password with 6 - 10  characters!")
	return false;
	}
 
  if (document.loginForm.pwd.value.length < 5)
	{
		alert("Please fill in the Password ")
	return false;
	}
 
  else
  {
  document.loginForm.submit();
  }
}

function ref()
{
	document.loginForm.name.focus();
}

// -->
