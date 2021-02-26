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
    <form method="POST" action="/admin/products/new">
        <div class="form-group">
            <label for="name"><fmt:message key="new.item.product.name" /></label>
            <input type="text" class="form-control" value="${name}" id="name" name="name" />
            <c:if test="${messages.containsKey('name')}">
                <p style="color: red">${messages.get('name')}</p>
            </c:if>
        </div>
        <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="new.item.product.create" /></button>
    </form>
</div>
</body>
</html>
