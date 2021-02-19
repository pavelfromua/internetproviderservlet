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



<%--<form method="post">--%>
<%--    <label for="username"><fmt:message key="login.login" />:</label>--%>
<%--    <input type="text" id="username" name="username">--%>
<%--    <br>--%>
<%--    <label for="password"><fmt:message key="login.password" />:</label>--%>
<%--    <input type="password" id="password" name="password">--%>
<%--    <br>--%>
<%--    <fmt:message key="login.signIn" var="buttonValue" />--%>
<%--    <input type="submit" name="submit" value="${buttonValue}">--%>
<%--</form>--%>
<%--</body>--%>
<%--</html>--%>


</body>
</html>
