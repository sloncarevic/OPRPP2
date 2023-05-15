<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Blog</title>
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
	
	<c:if test="${sessionScope[\"current.user.nick\"].equals(author.nick)}">
		<hr>
		<p> <a href="<%=request.getContextPath()%>/servleti/author/${author.nick}/new"> Create new blog entry</a> </p>
	</c:if>

	<hr>
	<p> Blog entries </p>
	<c:choose>
    	<c:when test="${authorBlogEntries.isEmpty()}">
      		There is no blog entries!
    	</c:when>
    	<c:otherwise>
    		<ol>
    		<c:forEach var="blogEntry" items="${authorBlogEntries}">
    			<li><a href="<%=request.getContextPath()%>/servleti/author/${author.nick}/${blogEntry.id}"> ${blogEntry.title} </a> </li>
    		</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>
	
</body>
</html>