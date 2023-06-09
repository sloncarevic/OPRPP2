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
<body bgcolor="${pickedBgCol}">
	
<p><a href="index.jsp"> Home</a></p>
	
<h1>Rezultati glasanja</h1>
 <p>Ovo su rezultati glasanja.</p>

<table border="1" class="rez">
 
	<thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
 
 	<tbody>
 		<c:forEach var="band" items="${bands }">
 			<tr> <td>${band.name} </td> <td> ${band.votes} </td> </tr>
  		</c:forEach>
	</tbody>
 </table>
 
 <h2>Grafički prikaz rezultata</h2>

  <img alt="Pie-chart" src="<%=request.getContextPath()%>/glasanje-grafika" width="400" height="400" />
 
 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="<%=request.getContextPath()%>/glasanje-xls">ovdje</a></p>
 
 <h2>Razno</h2>
 
 <p>Primjeri pjesama pobjedničkih bendova:</p>
 <ul>
 
  <li><a href="${bands[0].link }" target="_blank"> ${bands[0].name } </a></li>
  <li><a href="${bands[1].link }" target="_blank"> ${bands[1].name } </a></li>
 
 </ul>



</body>
</html>