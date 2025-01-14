<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function addToCart(itemCode) {

	  // Obtain an XMLHttpRequest instance
	  var req = newXMLHttpRequest();

	  // Set the handler function to receive callback notifications
	  // from the request object
	  var handlerFunction = getReadyStateHandler(req, updateCart);
	  req.onreadystatechange = handlerFunction;
	  
	  // Open an HTTP POST connection to the shopping cart servlet.
	  // Third parameter specifies request is asynchronous.
	  req.open("POST", "cart.do", true);

	  // Specify that the body of the request contains form data
	  req.setRequestHeader("Content-Type", 
	                       "application/x-www-form-urlencoded");

	  // Send form encoded data stating that I want to add the 
	  // specified item to the cart.
	  req.send("action=add&item="+itemCode);
	}


</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<th>Name</th> <th>Description</th> <th>Price</th> <th></th>
...
<tr>
  <!-- Item details -->
  <td>Hat</td> <td>Stylish bowler hat</td> <td>$19.99</td>
  <td>
    <!-- Click button to add item to cart via Ajax request -->
    <button onclick="addToCart('hat001')">Add to Cart</button>
  </td>
</tr>
...

<!-- Representation of shopping cart, updated asynchronously -->
<ul id="cart-contents">

  <!-- List-items will be added here for each item in the cart -->
  
</ul>


</body>
</html>