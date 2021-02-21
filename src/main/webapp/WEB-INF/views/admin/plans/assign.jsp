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

<form method="POST" action="/admin/users/cab/assign" >
    <div class="form-group">
        <div class="table-responsive">
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th></th>
                    <th><fmt:message key="list.items.plans.name" /></th>
                    <th><fmt:message key="list.items.plans.price" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="plan" items="${planList}" varStatus="custStat">
                    <tr>
                        <td>
                            <div class="radio">
                                <c:choose>
                                    <c:when test="${plid == plan.id || custStat.count == 1}">
                                        <input checked type="radio" name="plan" value="${plan.id}">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="radio" name="plan" value="${plan.id}">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td><c:out value="${plan.name}"/></td>
                        <td><c:out value="${plan.price}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div>
        <input type="hidden" id="uid" name="uid" value="${uid}">
        <input type="hidden" id="aid" name="aid" value="${aid}">
        <input type="hidden" id="pid" name="pid" value="${pid}">
        <button type="submit" class="btn btn-primary" name="btnValue" value="assign"><fmt:message key="item.plan.assign" /></button>
        <button type="submit" class="btn btn-primary" name="btnValue" value="cancel"><fmt:message key="item.plan.cancel" /></button>
    </div>
</form>
</body>
</html>
