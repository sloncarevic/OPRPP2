<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>App info</title>
</head>
<body bgcolor="${pickedBgCol}">
	
	<p><a href="index.jsp"> Home</a></p>

	<p>
	
	<%
	long start = (Long) application.getAttribute("runningSince");
	long duration = System.currentTimeMillis() - start;
	
	int milliseconds = (int) (duration % 1000);
	int seconds = (int) duration / 1000;
	int minutes = (seconds / 60);
	int hours = minutes / 60;
	int days = hours / 24;
	seconds %= 60;
	minutes %= 60;
	hours %= 24; 
			
	String formated = String.format("%d days %d hours %d minutes %d seconds and %d milliseconds ",
			days, hours, minutes, seconds, milliseconds );
	%>
	
	App has been running for <%= formated %>
	
	</p>

</body>
</html>