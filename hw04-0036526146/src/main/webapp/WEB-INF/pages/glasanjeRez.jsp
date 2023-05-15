<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Rezultati glasanja</title>
   <style type="text/css">
 		table.rez td {text-align: center;}
	</style>
</head>
<body>
	
<p><a href="<%=request.getContextPath()%>/index.html"> Home</a></p>
	
<h1>Rezultati glasanja</h1>
 <p>Ovo su rezultati glasanja.</p>

<table border="1" class="rez">
 
	<thead><tr><th>Natjecatelj</th><th>Broj glasova</th></tr></thead>
 
 	<tbody>
 		<c:forEach var="pollOption" items="${pollOptions}">
 			<tr> <td>${pollOption.optionTitle} </td> <td> ${pollOption.votesCount} </td> </tr>
  		</c:forEach>
	</tbody>
 </table>
 
 <h2>Grafički prikaz rezultata</h2>

  <img alt="Pie-chart" src="<%=request.getContextPath()%>/servleti/glasanje-grafika" width="400" height="400" />
 
 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="<%=request.getContextPath()%>/servleti/glasanje-xls">ovdje</a></p>
 
 <h2>Razno</h2>
 
 <p>Primjeri pobjednika:</p>
 <ul>
 
  <li><a href="${pollOptions[0].optionLink }" target="_blank"> ${pollOptions[0].optionTitle } </a></li>
  <li><a href="${pollOptions[1].optionLink }" target="_blank"> ${pollOptions[1].optionTitle } </a></li>
 
 </ul>



</body>
</html>