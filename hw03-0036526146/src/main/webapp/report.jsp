<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Funny story</title>
</head>
<body bgcolor="${pickedBgCol}">
	
	<p><a href="index.jsp"> Home</a></p>

	<h1>OS usage</h1>
	
	<p> Here are the results of OS usage in survey that we completed</p>
	
	<img src="<%=request.getContextPath()%>/reportImage">


</body>
</html>