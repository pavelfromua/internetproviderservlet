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

<a href="/plans/export/pdf"><fmt:message key="utils.export.pdf" /></a>
${message}

<table class="table table-hover">
<thead>
<tr>
    <th scope="col">#</th>
    <th scope="col">
        <a href="/admin/plans/page?pn=${currentPage}&sf=product&sd=${rsd}">
            <fmt:message key="list.items.plans.product" />
        </a>
    </th>
    <th scope="col">
        <a href="/admin/plans/page?pn=${currentPage}&sf=name&sd=${rsd}">
            <fmt:message key="list.items.plans.name" />
        </a>
    </th>
    <th scope="col">
        <a href="/admin/plans/page?pn=${currentPage}&sf=price&sd=${rsd}">
            <fmt:message key="list.items.plans.price" />
        </a>
    </th>
    <th scope="col"/>
</tr>
</thead>
<tbody>
<c:forEach var="plan" items="${planList}" varStatus="custStat">
    <tr>
        <th scope="row"><c:out value="${custStat.index + 1}"/></th>
        <td><c:out value="${plan.product}"/></td>
        <td><c:out value="${plan.name}"/></td>
        <td><c:out value="${plan.price}"/></td>
        <td align="right">
            <div class="dropdown">
                <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <fmt:message key="list.items.plans.actions" />
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <a class="dropdown-item" href="/admin/plans/edit?id=${plan.id}"><fmt:message key="list.items.plans.actions.edit" /></a>
                    <form method="POST" action="/admin/plans/delete">
                        <input type="hidden" name="planId" value="${plan.id}">
                        <fmt:message key="list.items.plans.actions.delete" var="btnValue" />
                        <input class="dropdown-item" type="submit" value="${btnValue}"/>
                    </form>
                </div>
            </div>
        </td>
    </tr>
</c:forEach>
</tbody>
</table>

<table>
    <tr>
        <td>
            <fmt:message key="pagination.items" /> | ${totalItems} - <fmt:message key="pagination.page" /> ${currentPage} <fmt:message key="pagination.of" /> ${totalPages} -
        </td>
        <td>
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="/admin/plans/page?pn=1&sf=${sf}&sd=${sd}"><fmt:message key="pagination.first" /></a>
                </c:when>
                <c:otherwise>
                    <span><fmt:message key="pagination.first" /></span>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="/admin/plans/page?pn=${currentPage - 1}&sf=${sf}&sd=${sd}"><fmt:message key="pagination.previous" /></a>
                </c:when>
                <c:otherwise>
                    <span><fmt:message key="pagination.previous" /></span>
                </c:otherwise>
            </c:choose>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i != currentPage}">
                        <a href="/admin/plans/page?pn=${i}&sf=${sf}&sd=${sd}">${i}</a>
                    </c:when>
                    <c:otherwise>
                        <span>${i}</span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="/admin/plans/page?pn=${currentPage + 1}&sf=${sf}&sd=${sd}"><fmt:message key="pagination.next" /></a>
                </c:when>
                <c:otherwise>
                    <span><fmt:message key="pagination.next" /></span>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="/admin/plans/page?pn=${totalPages}&sf=${sf}&sd=${sd}"><fmt:message key="pagination.last" /></a>
                </c:when>
                <c:otherwise>
                    <span><fmt:message key="pagination.last" /></span>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

</body>
</html>
