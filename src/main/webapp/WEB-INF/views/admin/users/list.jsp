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

<table class="table table-hover">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col"><fmt:message key="list.users.login" /></th>
        <th scope="col"><fmt:message key="list.users.name" /></th>
        <th scope="col"><fmt:message key="list.users.email" /></th>
        <th/>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${userList}" varStatus="custStat">
        <tr>
            <th scope="row"><c:out value="${custStat.index + 1}"/></th>
            <td><c:out value="${user.login}"/></td>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.email}"/></td>
            <td align="right">
                <div class="dropdown">
                    <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="list.users.actions" />
                    </a>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="/admin/users/cab?id=${user.id}"><fmt:message key="list.users.actions.show" /></a>
                        <form method="POST" action="/admin/users/delete">
                            <input type="hidden" name="userId" value="${user.id}">
                            <fmt:message key="list.users.actions.delete" var="buttonValue" />
                            <input class="dropdown-item" type="submit" value="${buttonValue}"/>
                        </form>
                    </div>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
