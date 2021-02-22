<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />

<nav class="navbar navbar-expand navbar-dark bg-primary">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#adminNav" aria-controls="adminNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="cabinetNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/client/plans/page?pn=1&sf=name&sd=asc"><fmt:message key="cabinet.nav.items" /></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/client/cab"><fmt:message key="cabinet.nav.user" /></a>
            </li>
        </ul>
    </div>
    <div>
        <form class="form-inline my-2 my-lg-0" action="/logout" method="post">
            <button class="btn btn-light" type="submit" value="Logout"><fmt:message key="cabinet.nav.logout" /></button>
        </form>
    </div>
</nav>