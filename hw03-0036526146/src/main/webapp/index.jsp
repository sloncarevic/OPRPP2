<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
</head>
<body bgcolor="${pickedBgCol}">

	<p><a href="colors.jsp">Bakcground color chooser </a></p>

	<p><a href="trigonometric?a=0&b=90">Trigonometric (a=0, b=90) link </a></p>

	<form action="trigonometric" method="GET">
	 Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
	 Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
	 <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>

	<p>
		<a href="stories/funny.jsp">Funny story</a>
	</p>
	
	<p>
	<a href="report.jsp">OS usage</a>
	</p>
	
	<p>
	<a href="powers?a=1&b=100&n=3">XLS document</a>
	</p>
	
	<p>
	<a href="appinfo.jsp">App info</a>
	</p>
	
	<p>
	<a href="glasanje">Glasanje</a>
	</p>

</body>
</html>