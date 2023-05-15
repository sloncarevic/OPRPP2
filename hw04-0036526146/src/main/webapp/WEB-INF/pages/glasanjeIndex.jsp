<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Glasanje</title>
</head>
<body>
<p><a href="<%=request.getContextPath()%>/index.html"> Home</a></p>		

<h1>${poll.getTitle()}</h1>

 <p>${poll.getMessage()}</p>

 <ol>
 	<c:forEach var="pollOption" items="${pollOptions}">
 		<li> <a href="glasanje-glasaj?pollOptionId=${ pollOption.pollOptionId }"> ${ pollOption.optionTitle } </a> </li>
 	</c:forEach>
 </ol>

</body>
</html>