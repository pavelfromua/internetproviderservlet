<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${language}">
<head>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/style.jsp"/>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/head.jsp"/>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/admin/menu.jsp"/>

<div class="container" style="width: 50%">
    <form method="POST" action="/admin/users/new">
        <div class="form-group">
            <label for="login"><fmt:message key="new.user.login" /></label>
            <input type="text" class="form-control" value="${login}" id="login" name="login" />
            <c:if test="${messages.containsKey('login')}">
                <p style="color: red">${messages.get('login')}</p>
            </c:if>
        </div>
        <div class="form-group">
            <label for="name"><fmt:message key="new.user.name" /></label>
            <input type="text" class="form-control" value="${name}" id="name" name="name"/>
            <c:if test="${messages.containsKey('name')}">
                <p style="color: red">${messages.get('name')}</p>
            </c:if>
        </div>
        <div class="form-group">
            <label for="email"><fmt:message key="new.user.email" /></label>
            <input type="text" class="form-control" value="${email}" id="email" name="email"/>
            <c:if test="${messages.containsKey('email')}">
                <p style="color: red">${messages.get('email')}</p>
            </c:if>
        </div>
        <div class="form-group">
            <label for="password"><fmt:message key="new.user.password" /></label>
            <input type="text" class="form-control" value="${password}" id="password" name="password"/>
            <c:if test="${messages.containsKey('password')}">
                <p style="color: red">${messages.get('password')}</p>
            </c:if>
        </div>
        <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="new.user.create" /></button>
    </form>
</div>
</body>
</html>
