<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Blog form</title>
     <style type="text/css">
	.greska {
	   color: #FF0000;
	}
	</style>
</head>
<body>

	<a href="<%=request.getContextPath()%>/servleti/main">Main</a>
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
	<hr>
	<br>
	<!-- <form action="<%=request.getContextPath()%>/servleti/author/${sessionScope['current.user.nick']}/new" method="POST"> -->
		<form action="${action}" method="POST">
		Title:
		<input type="text" name="title" size="70" maxlength="200" required value="${form.title}">
		 <c:if test="${form.hasError('title')}">
		 	<div class="greska"><c:out value="${form.getError('title')}"/></div>
		 </c:if>
		 <br>
		<!-- input type="text" name="text" size="4096" required>  -->
		Text:
		<textarea name="text" cols="71" rows="10" maxlength="4096" required >${form.text}</textarea>
		<c:if test="${form.hasError('text')}">
		 	<div class="greska"><c:out value="${form.getError('text')}"/></div>
		 </c:if>
		 <br>
		<input type="submit" value="Submit"><input type="reset" value="Reset">
	
	
	</form>

	
</body>
</html>