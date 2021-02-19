<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand" href="/">
        <img src="${pageContext.request.contextPath}/images/logo.png">
    </a>
    <span class="navbar-text"><fmt:message key="common.tagline" /></span>
</nav>
