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
        <a href="/admin/plans?page=${currentPage}&?sf=product&sd=${rsd}">
            <fmt:message key="list.items.plans.product" />
        </a>
    </th>
    <th scope="col">
        <a href="/admin/plans?page=${currentPage}&?sf=name&sd=${rsd}">
            <fmt:message key="list.items.plans.name" />
        </a>
    </th>
    <th scope="col">
        <a href="/admin/plans?page=${currentPage}&?sf=price&sd=${rsd}">
            <fmt:message key="list.items.plans.price" />
        </a>
    </th>
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
        <%--                <td>--%>
        <%--                    <a th:if="${currentPage > 1}" th:text="#{pagination.first}" th:href="@{'/items/plans/page/1' + '?sf=' + ${sf} + '&sd=' + ${sd}}">First</a>--%>
        <%--                    <span th:unless="${currentPage > 1}" th:text="#{pagination.first}">First</span>--%>
        <%--                    <a th:if="${currentPage > 1}" th:text="#{pagination.previous}" th:href="@{'/items/plans/page/' + ${currentPage - 1} + '?sf=' + ${sf} + '&sd=' + ${sd}}">Previous</a>--%>
        <%--                    <span th:unless="${currentPage > 1}" th:text="#{pagination.previous}">Previous</span>--%>
        <%--                    <span th:each="i: ${#numbers.sequence(1, totalPages)}">--%>
        <%--    <a th:if="${i != currentPage}" th:href="@{'/items/plans/page/' + ${i} + '?sf=' + ${sf} + '&sd=' + ${sd}}">[[${i}]]</a>--%>
        <%--    <span th:unless="${i != currentPage}">[[${i}]]</span>--%>
        <%--    </span>--%>
        <%--                    <a th:if="${currentPage < totalPages}" th:text="#{pagination.next}" th:href="@{'/items/plans/page/' + ${currentPage + 1} + '?sf=' + ${sf} + '&sd=' + ${sd}}">Next</a>--%>
        <%--                    <span th:unless="${currentPage < totalPages}" th:text="#{pagination.next}">Next</span>--%>
        <%--                    <a th:if="${currentPage < totalPages}" th:text="#{pagination.last}" th:href="@{'/items/plans/page/' + ${totalPages} + '?sf=' + ${sf} + '&sd=' + ${sd}}">Last</a>--%>
        <%--                    <span th:unless="${currentPage < totalPages}" th:text="#{pagination.last}">Last</span>--%>
        <%--                </td>--%>
    </tr>
</table>

</body>
</html>
