<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="/plans/page"><fmt:message key="main.nav.items" /></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/login"><fmt:message key="main.nav.cabinet" /></a>
            </li>
<%--            <li class="nav-item">--%>
<%--                <a class="nav-link" href="${pageContext.request.contextPath}/admin/users/new">registration</a>--%>
<%--            </li>--%>
            <li class="nav-item">
                <fmt:message key="lang.en" var="alt_en" />
                <a class="nav-link" href="/?language=en"><img src="${pageContext.request.contextPath}/images/en.png" alt="${alt_en}"></a>
            </li>
            <li class="nav-item">
                <fmt:message key="lang.ru" var="alt_ru" />
                <a class="nav-link" href="/plans/page?language=ru"><img src="${pageContext.request.contextPath}/images/ru.png" alt="${alt_ru}"></a>
            </li>
            <li class="nav-item">
                <fmt:message key="lang.ua" var="alt_ua" />
                <a class="nav-link" href="/?language=ua"><img src="${pageContext.request.contextPath}/images/ua.png" alt="${alt_ua}"></a>
            </li>
        </ul>
    </div>
</nav>
