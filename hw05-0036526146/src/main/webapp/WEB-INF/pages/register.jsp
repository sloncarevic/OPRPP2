<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  
 	<style type="text/css">
	.greska {
	   color: #FF0000;
	}
	</style>
</head>
<body>
	<a href="<%=request.getContextPath()%>/servleti/main">Main</a>
	<br>
	<hr>
	<br>
	<form action="register" method="POST">
		First name:<input type="text" name="firstName" value="${form.firstName}" >
		 <c:if test="${form.hasError('firstName')}">
		 	<div class="greska"><c:out value="${form.getError('firstName')}"/></div>
		 </c:if>
		<br>
		
		Last name:<input type="text" name="lastName" value="${form.lastName}">
		 <c:if test="${form.hasError('lastName')}">
		 	<div class="greska"><c:out value="${form.getError('lastName')}"/></div>
		 </c:if>
		 <br>
		 
		Email:<input type="text" name="email" value="${form.email}">
		 <c:if test="${form.hasError('email')}">
		 	<div class="greska"><c:out value="${form.getError('email')}"/></div>
		 </c:if>
		 <br>
		
		Nickname:<input type="text" name="nick" value="${form.nick}">
		 <c:if test="${form.hasError('nick')}">
		 	<div class="greska"><c:out value="${form.getError('nick')}"/></div>
		 </c:if>
		 <br>
		 
		Password:<input type="password" name="password">
		<c:if test="${form.hasError('password')}">
		 	<div class="greska"><c:out value="${form.getError('password')}"/></div>
		 </c:if>
		<br>
		
		<c:if test="${error != null }">
			<p style="color: red"> ${error}</p>
		</c:if>
		
		<input type="submit" value="Register"><input type="reset" value="Reset">
	</form>
	
	
</body>
</html>