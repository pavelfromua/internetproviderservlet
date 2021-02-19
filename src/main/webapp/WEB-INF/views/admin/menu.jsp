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
    <div class="collapse navbar-collapse" id="adminNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownProducts" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <fmt:message key="admin.nav.items" />
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownProducts">
                    <a class="dropdown-item" href="/admin/products/new"><fmt:message key="admin.nav.items.products.new" /></a>
                    <a class="dropdown-item" href="/admin/plans/new"><fmt:message key="admin.nav.items.plans.new" /></a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/admin/products"><fmt:message key="admin.nav.items.products.show" /></a>
                    <a class="dropdown-item" href="/admin/plans"><fmt:message key="admin.nav.items.plans.show" /></a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownUsers" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <fmt:message key="admin.nav.users" />
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownUsers">
                    <a class="dropdown-item" href="/admin/users/new"><fmt:message key="admin.nav.users.new" /></a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/admin/users"><fmt:message key="admin.nav.users.show" /></a>
                </div>
            </li>
        </ul>
    </div>
    <div>
        <form class="form-inline my-2 my-lg-0" action="/logout" method="post">
            <button class="btn btn-light" type="submit" value="Logout"><fmt:message key="admin.nav.logout" /></button>
        </form>
    </div>
</nav>