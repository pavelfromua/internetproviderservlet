<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${language}">
<head>
	<jsp:include page="WEB-INF/views/style.jsp"/>
</head>
<body>

<jsp:include page="WEB-INF/views/head.jsp"/>
<jsp:include page="WEB-INF/views/menu.jsp"/>

<%--<div style="color: red" th:if="${param.error}" th:text="#{login.login-error}"/>--%>
<%--<div th:if="${param.logout}" th:text="#{common.logout-message}"/>--%>
${message}
<div class="container" style="width: 30%">
	<form action="${pageContext.request.contextPath}/login" method="post">
		<div class="form-group">
			<label for="inputLogin"><fmt:message key="login.login" />:</label>
			<input type="text" class="form-control" id="inputLogin" name="login">
		</div>
		<div class="form-group">
			<label for="inputPassword"><fmt:message key="login.password" />:</label>
			<input type="password" class="form-control" id="inputPassword" name="password">
		</div>
		<button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="login.signIn" /></button>
	</form>
</div>
</body>
</html>