<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@page import="java.awt.Color"%>
<%@page import="java.util.Random"%>

<!DOCTYPE html>
<html>
<head>
  <title>Funny story</title>
</head>
<body bgcolor="${pickedBgCol}">
	
	<p><a href="/webapp2"> Home</a></p>

	<% 
	Random r = new Random();
	Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	%>
	
	<p style="color:rgb(<%=color.getRed()%>,<%=color.getGreen()%>,<%=color.getBlue()%>)">
	Funny story example
		
	</p>

</body>
</html>