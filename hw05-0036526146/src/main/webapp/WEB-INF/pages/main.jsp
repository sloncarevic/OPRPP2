<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Main</title>
  
   	<style type="text/css">
	.greska {
	   color: #FF0000;
	}
	</style>
</head>
<body>

	<c:if test="${sessionScope[\"current.user.id\"] != null}">
		<p> Logged in &emsp;&emsp;&emsp; Nick: ${sessionScope['current.user.nick']} 
					&emsp;&emsp;&emsp; First name: ${sessionScope['current.user.fn']}
					&emsp;&emsp;&emsp; Last name: ${sessionScope['current.user.ln']}
					&emsp;&emsp;&emsp; <a href="<%=request.getContextPath()%>/servleti/logout"> Logout</a>
					</p>
	</c:if>
	<c:if test="${sessionScope['current.user.id'] == null}">
		<h4>Not logged in </h4>
	</c:if>
	<hr>
	
	<c:if test="${sessionScope[\"current.user.id\"] == null}">
		<h3> Login </h3>
		
		<form action="login" method="POST">
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
				<p style="color: red">${error}</p>
			</c:if>
			<input type="submit" value="Login"><input type="reset" value="Reset">
		</form>
		<hr>
	</c:if>
	
	<c:if test="${sessionScope[\"current.user.id\"] == null}">
		<br>
		<p> <a href="<%=request.getContextPath()%>/servleti/register"> Register </a> </p>
	</c:if>
	
	
	<hr>
	<br>
	<p> List of registerd users </p>
	<c:choose>
    	<c:when test="${blogUsers.isEmpty()}">
      		There is no registerd users!
    	</c:when>
    	<c:otherwise>
    		<ol>
    		<c:forEach var="blogUser" items="${blogUsers}">
    			<li><a href="<%=request.getContextPath()%>/servleti/author/${blogUser.nick}"> ${blogUser.firstName} ${blogUser.lastName} </a> </li>
    		</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>

	
</body>
</html>