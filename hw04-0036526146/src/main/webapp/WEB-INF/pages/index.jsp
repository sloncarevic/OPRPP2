<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
</head>
<body>

	<h1>Odaberite anketu za glasanje</h1>
	
	<ol>
	 	<c:forEach var="poll" items="${polls}">
	 		<li> <a href="glasanje?pollId=${ poll.pollId }"> ${ poll.title } </a> </li>
	 	</c:forEach>
 	</ol>
	
</body>
</html>