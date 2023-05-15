<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Blog</title>
     <style type="text/css">
	.greska {
	   color: #FF0000;
	}
	</style>
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

	<hr>
	<br>
	
	<p> Author: ${blogEntry.author.nick}</p>

	<p> Created: ${blogEntry.createdAt}</p>

	<c:if test="${blogEntry.lastModifiedAt != null}">
		<p> Last modified: ${blogEntry.lastModifiedAt}</p>
	</c:if>
	
	<c:if test="${sessionScope['current.user.nick'].equals(blogEntry.author.nick)}">
		<p> <a href="<%=request.getContextPath()%>/servleti/author/${blogEntry.author.nick}/edit/${blogEntry.id}"> Edit blog entry</a> </p>
	</c:if>
	
	<br>
	
	<h3> Title: ${blogEntry.title}</h3>
	
	<h3> Text: </h3>
	<textarea rows="10" cols="70" readonly>${blogEntry.text}</textarea>

	<br>
	
	<hr>
	
	<h3> Comments: </h3>
	<c:choose>
    	<c:when test="${blogEntry.comments.isEmpty()}">
      		There is no comments
    	</c:when>
    	<c:otherwise>
    		<ol>
    		<c:forEach var="comment" items="${blogEntry.comments}">
    			<li> ${comment.usersEMail} ${comment.postedOn} <br>
    				${comment.message} 
    			</li>
    		</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>
	
	<hr>
	
	<h4> Add comment</h4>
	<form action="<%=request.getContextPath()%>/servleti/author/${blogEntry.author.nick}/${blogEntry.id}" method="POST">
		Email: <input type="email" name="email" value="${sessionScope['current.user.email']}" required>
		<c:if test="${form.hasError('email')}">
		 	<div class="greska"><c:out value="${form.getError('email')}"/></div>
		 </c:if>
		 <br>
		Comment: <textarea name="message" cols="50" rows="3" maxlength="4096" required></textarea>
		<c:if test="${form.hasError('message')}">
		 	<div class="greska"><c:out value="${form.getError('message')}"/></div>
		 </c:if>
		 <br>
		<input type="submit" value="Submit"><input type="reset" value="Reset">
	
	</form>
	
	
</body>
</html>