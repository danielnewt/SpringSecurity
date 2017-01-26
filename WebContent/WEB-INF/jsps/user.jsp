<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />">
<script>
	function verify() {

		var username = document.getElementById('username').value;

		if(username == null || username == ""){
			document.getElementById("error").innerHTML = "Username is missing";
			return false;
		}
		
		var password1 = document.getElementById('password').value;

		if(password1 == null || password1 == ""){
			document.getElementById("error").innerHTML = "Password is missing";
			return false;
		}

		var password2Elm = document.getElementById('verifyPassword');

		if(password2Elm != null){
		var password2 = password2Elm.value;

		if(password2 == null || password2 == ""){
			document.getElementById("error").innerHTML = "Confirm Password is missing";
			return false;
		}
		if (password1 == null || password1 == "" || password1 != password2) {
			document.getElementById("error").innerHTML = "Your passwords do not match. Try Again.";
			return false;
		}
		}
	}
</script>


<title>${context}</title>
</head>

<body onload='document.getElementById("username").focus();'>
	<div id="navbar">
		<a class="navbarButton" href="<c:url value="/" />">Home</a>
		<c:choose>
			<c:when test="${context == 'Login'}">
				<a class="navbarButton" href="<c:url value="/register" />">Register</a>
			</c:when>
			<c:when test="${context == 'Register'}">
				<a class="navbarButton" href="<c:url value="/login" />">Login</a>
			</c:when>
		</c:choose>
	</div>
	
	<h1>${context}</h1>
	<c:if test="${accountCreated == 'true'}">
		<h3>Account successfully created!</h3>
	</c:if>
	<div id="error">
		${error}
	</div>
	
	<c:choose>
		<c:when test="${context == 'Login'}">
			<form id=loginForm method="POST" action="j_spring_security_check" onsubmit="return verify()">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<div>
					Username: <input id="username" name="j_username" type="text" value="${SPRING_SECURITY_LAST_USERNAME}" required/>
				</div>
				<div>
					Password: <input id="password" name="j_password" type="password" required/>
				</div>
				<input type="submit" value="${context}" />
			</form>
		</c:when>
		<c:when test="${context == 'Register'}">
			<form:form commandName="user" method="post" action="saveuser" onsubmit="return verify()">
				<div>
					Username: <form:input path="username" required="required"/>
				</div>
				<div>
					Password: <form:password path="password" required="required"/>
				</div>
				<div>
					Confirm Password:
					<input id="verifyPassword" name="verifyPassword" type="password" required/>
				</div>
				<input type="submit" value="${context}" />
			</form:form>
		</c:when>
		<c:otherwise>
			<c:url var="url" value="/home" />
			<a href="${url}">Home</a>
		</c:otherwise>
	</c:choose>
</body>
</html>