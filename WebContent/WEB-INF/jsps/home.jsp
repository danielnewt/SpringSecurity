<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Classic Video Game Trivia</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />">
</head>
<body>
	<div id="navbar">
		<sec:authorize access="isAnonymous()">
			<a class="navbarButton" href="<c:url value="/login" />">Login</a>
			<a class="navbarButton" href="<c:url value="/register" />">Register</a>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
	    	<a class="navbarButton" href="<c:url value="/logout" />">Logout</a>
		</sec:authorize>
	</div>
	
	<h1>Classic Video Game Trivia</h1>
	
	<sec:authorize access="isAnonymous()">
		<h3>Please <a href="<c:url value="/login" />">Login</a> to begin</h3>
		<h5>Don't have an account? <a href="<c:url value="/register" />">Register</a> now!</h5>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<c:choose>
			<c:when test="${not empty gameState and not empty gameWon}">
				<h3>Username: ${gameState.user.username}</h3>
				<h3>Score: ${gameState.score}</h3>
				<h1>You Won! <a href="<c:url value="/game/newGame" />">CLICK HERE</a> to start a new game!</h1>
				
				<table border=1>
							<tr>
								<th>${gameState.questions[0].question.category.name}</th>
								<th>${gameState.questions[4].question.category.name}</th>
								<th>${gameState.questions[8].question.category.name}</th>
								<th>${gameState.questions[12].question.category.name}</th>
							</tr>
							<c:forEach begin="0" end="3" step="1" var="i">
    							<tr>
    								<c:forEach begin="0" end="12" step="4" var="j">
    									<c:choose>
    										<c:when test="${gameState.questions[i + j].state == 'CORRECT'}">
    											<td><span class="answeredQuestionCorrect" >${gameState.questions[i + j].scoreValue}</span></td>
    										</c:when>
    										<c:when test="${gameState.questions[i + j].state == 'INCORRECT'}">
    											<td><span class="answeredQuestionIncorrect" >${gameState.questions[i + j].scoreValue}</span></td>
    										</c:when>
    									</c:choose>
    								</c:forEach>
    							</tr>
							</c:forEach>
						</table>
			</c:when>
			<c:when test="${not empty gameState and empty gameWon}">
				<h3>Username: ${gameState.user.username}</h3>
				<h3>Score: ${gameState.score}</h3>
				
				<c:choose>
					<c:when test="${empty gameState.selectedQuestion}">
						<h5>How to play: Select a square with a value. Answer the question correctly and the value will be added to your score!</h5>
				
						<table border=1>
							<tr>
								<th>${gameState.questions[0].question.category.name}</th>
								<th>${gameState.questions[4].question.category.name}</th>
								<th>${gameState.questions[8].question.category.name}</th>
								<th>${gameState.questions[12].question.category.name}</th>
							</tr>
							<c:forEach begin="0" end="3" step="1" var="i">
    							<tr>
    								<c:forEach begin="0" end="12" step="4" var="j">
    									<c:choose>
    										<c:when test="${gameState.questions[i + j].state == 'UNANSWERED'}">
    											<td><a class="questionBox" href="<c:url value="/game/selectQuestion/${gameState.questions[i + j].id}" />">${gameState.questions[i + j].scoreValue}</a></td>
    										</c:when>
    										<c:when test="${gameState.questions[i + j].state == 'CORRECT'}">
    											<td><span class="answeredQuestionCorrect" >${gameState.questions[i + j].scoreValue}</span></td>
    										</c:when>
    										<c:when test="${gameState.questions[i + j].state == 'INCORRECT'}">
    											<td><span class="answeredQuestionIncorrect" >${gameState.questions[i + j].scoreValue}</span></td>
    										</c:when>
    									</c:choose>
    								</c:forEach>
    							</tr>
							</c:forEach>
						</table>
					</c:when>
					<c:otherwise>
						<jsp:useBean id="random" class="java.util.Random" scope="application" />
						<c:set var="correctIndex" value="${random.nextInt(4)}"/>
						<c:url var="answerUrl" value="/game/answerQuestion"/>
						
						<table border=1>
							<tr>
								<th colspan="2">${gameState.selectedQuestion.question.question}</th>
							</tr>
							<tr>
								<c:forEach begin="0" end="1" step="1" var="i">
									<td>
										<c:choose>
											<c:when test="${i == correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.answer}"/>
											</c:when>
											<c:when test="${i < correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.wrongAnswers[i]}"/>
											</c:when>
											<c:when test="${i > correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.wrongAnswers[i - 1]}"/>
											</c:when>
										</c:choose>
										<form method="post" action="${answerUrl}">
											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
											<input type="hidden" value="${answerString}" name="answer"/>
											<input class="answerButton" type="submit" value="${answerString}"/>
										</form>
									</td>
								</c:forEach>
    						</tr>
    						<tr>
								<c:forEach begin="2" end="3" step="1" var="i">
									<td>
										<c:choose>
											<c:when test="${i == correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.answer}"/>
											</c:when>
											<c:when test="${i < correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.wrongAnswers[i]}"/>
											</c:when>
											<c:when test="${i > correctIndex}">
												<c:set var="answerString" value="${gameState.selectedQuestion.question.wrongAnswers[i - 1]}"/>
											</c:when>
										</c:choose>
										<form method="post" action="${answerUrl}">
											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
											<input type="hidden" value="${answerString}" name="answer"/>
											<input class="answerButton" type="submit" value="${answerString}"/>
										</form>
									</td>
								</c:forEach>
    						</tr>
						</table>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<h3><a href="<c:url value="/game/newGame" />">CLICK HERE</a> to start a new game!</h3>
			</c:otherwise>
		</c:choose>
	</sec:authorize>
	
	
</body>
</html>