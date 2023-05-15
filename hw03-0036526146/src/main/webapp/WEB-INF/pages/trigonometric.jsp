<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Trigonometric</title>
</head>
<body bgcolor="${pickedBgCol}">
	
	<p><a href="index.jsp"> Home</a></p>
	
	<table>
    <tr>
        <th>Angle</th><th>Sin</th><th>Cos</th>
    </tr>
    <c:forEach var="value" items="${values}">
        <tr>
            <td>${value[0]}</td>
            <td>${value[1]}</td>
            <td>${value[2]}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>