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
    <form method="POST" action="/admin/plans">
        <input type="hidden" name="planId" value="${plan.id}">
        <div class="form-group">
            <label for="name"><fmt:message key="edit.item.plan.name" /></label>
            <input type="text" class="form-control" value="${plan.name}" id="name" name="name"/>
        </div>
        <div class="form-group">
            <label for="name"><fmt:message key="edit.item.plan.price" /></label>
            <input type="number" min="0.01" step="0.01" class="form-control" value="${plan.price}" id="price" name="price" />
        </div>
        <div class="form-group">
            <label for="name"><fmt:message key="edit.item.plan.product" /></label>
            <select class="form-control" id="productItem" name="productItem" >
                <c:forEach var="product" items="${productList}">
                    <c:choose>
                        <c:when test="${product.id == plan.product.id}">
                            <option selected="selected" value="${product.id}:${product.name}">${product.name}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${product.id}:${product.name}">${product.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="edit.item.plan.edit" /></button>
    </form>
</div>
</body>
</html>
