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

${message}
<div class="container" style="width: 50%">
    <form method="POST" action="/admin/users/cab">
        <input type="hidden" name="userId" value="${userId}">
        <div class="form-group">
            <label for="login"><fmt:message key="edit.user.login" /></label>
            <input readonly type="text" class="form-control" value="${login}" id="login" name="login" />
        </div>
        <div class="form-group">
            <label for="name"><fmt:message key="edit.user.name" /></label>
            <input type="text" class="form-control" value="${name}" id="name" name="name"/>
        </div>
        <div class="form-group">
            <label for="email"><fmt:message key="edit.user.email" /></label>
            <input type="text" class="form-control" value="${email}" id="email" name="email"/>
        </div>
        <div class="form-group">
            <label for="password"><fmt:message key="edit.user.password" /></label>
            <input type="text" class="form-control" id="password" name="password"/>
        </div>
        <div class="form-group">
            <label for="password"><fmt:message key="edit.user.password" /></label>
            <input type="text" class="form-control" id="cpassword" name="cpassword"/>
        </div>
        <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="edit.user.edit" /></button>
    </form>
</div>
</body>
</html>
