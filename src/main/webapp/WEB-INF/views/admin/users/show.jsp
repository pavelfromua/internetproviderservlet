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

<div class="row">
    <div class="col-3">
        <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active" id="v-pills-profile-tab" data-toggle="pill" href="#v-pills-profile" role="tab" aria-controls="v-pills-profile" aria-selected="true">
                <fmt:message key="cabinet.menu.profile" />
            </a>
            <a class="nav-link" id="v-pills-plans-tab" data-toggle="pill" href="#v-pills-plans" role="tab" aria-controls="v-pills-plans" aria-selected="false">
                <fmt:message key="cabinet.menu.plans" />
            </a>
            <a class="nav-link" id="v-pills-payments-tab" data-toggle="pill" href="#v-pills-payments" role="tab" aria-controls="v-pills-payments" aria-selected="false">
                <fmt:message key="cabinet.menu.payments" />
            </a>
            <a class="nav-link" id="v-pills-settings-tab" data-toggle="pill" href="#v-pills-settings" role="tab" aria-controls="v-pills-settings" aria-selected="false">
                <fmt:message key="cabinet.menu.settings" />
            </a>
        </div>
    </div>
    <div class="col-9">
        <div class="tab-content" id="v-pills-tabContent">
            <div class="tab-pane fade show active" id="v-pills-profile" role="tabpanel" aria-labelledby="v-pills-profile-tab">
                <form method="POST" action="/admin/users/edit">
                    <input type="hidden" name="userId" value="${client.id}">
                    <div class="form-group">
                        <label for="login"><fmt:message key="show.user.login" /></label>
                        <input type="text" readonly class="form-control" value="${client.login}" id="login" name="login" />
                    </div>
                    <div class="form-group">
                        <label for="name"><fmt:message key="show.user.name" /></label>
                        <input type="text" readonly class="form-control" value="${client.name}" id="name" name="name"/>
                    </div>
                    <div class="form-group">
                        <label for="email"><fmt:message key="show.user.email" /></label>
                        <input type="text" readonly class="form-control" value="${client.email}" id="email" name="email"/>
                    </div>
                    <div class="form-group">
                        <label for="password"><fmt:message key="show.user.password" /></label>
                        <input type="text" readonly class="form-control" value="${client.password}" id="password" name="password"/>
                    </div>
                    <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="show.user.edit" /></button>
                </form>
            </div>
            <div class="tab-pane fade" id="v-pills-plans" role="tabpanel" aria-labelledby="v-pills-plans-tab">
<%--                <div th:if="${user.account.active == false}" th:text="#{cabinet.account.status.message}" class="alert alert-info" role="alert" />--%>

<%--                <ul class="list-group" th:each="product: ${products}">--%>
<%--                    <li  class="list-group-item d-flex justify-content-between align-items-center">--%>
<%--                        <span th:text="${product.description}" />--%>
<%--                        <a th:href="@{/acc/plans/{uid}/{aid}/{pid}/{plid}(uid=${user.id},aid=${user.account.id},pid=${product.product.id},plid=${product.plan.id})}">--%>
<%--                            <span th:if="${user.account.active == true}" th:text="|+|"  class="badge badge-primary badge-pill">+</span>--%>
<%--                        </a>--%>
<%--                    </li>--%>
<%--                </ul>--%>
            </div>
            <div class="tab-pane fade" id="v-pills-payments" role="tabpanel" aria-labelledby="v-pills-payments-tab">
<%--                <div th:if="${user.account.active == false}" th:text="#{cabinet.account.status.message}" class="alert alert-info" role="alert" />--%>
<%--                <table width="100%">--%>
<%--                    <tr><td>--%>
<%--                        <form th:method="POST" modelAttribute="dataform" th:action="@{/acc/pay}" class="form-inline">--%>
<%--                            <div th:text="#{cabinet.menu.payments.menupay.label}" class="form-group mb-2">Amount to pay</div>--%>
<%--                            <input type="hidden" id="uid" name="uid" th:value="${user.id}">--%>
<%--                            <input type="hidden" id="aid" name="aid" th:value="${user.account.id}">--%>
<%--                            <input type="number" name="amount" class="form-control" min="0" value="0" step="0.01">--%>
<%--                            <button th:text="#{cabinet.menu.payments.menupay.btn}" type="submit" class="btn btn-primary mb-2">Pay</button>--%>
<%--                        </form>--%>
<%--                    </td></tr>--%>
<%--                    <tr><td th:text="|#{cabinet.menu.payments.balance}: ${balance}|"></td></tr>--%>
<%--                </table>--%>
                <table class="table table-hover">
<%--                <thead>--%>
<%--                <tr>--%>
<%--                    <th scope="col">#</th>--%>
<%--                    <th th:text="#{cabinet.menu.payments.history.name}" scope="col" >Name</th>--%>
<%--                    <th th:text="#{cabinet.menu.payments.history.amount}" scope="col" >Amount</th>--%>
<%--                    <th th:text="#{cabinet.menu.payments.history.date}" scope="col" >Date</th>--%>
<%--                </tr>--%>
<%--                </thead>--%>
<%--                <tbody>--%>
<%--                <tr th:each="payment, custStat: ${payments}">--%>
<%--                    <th scope="row" th:text="${custStat.count}">1</th>--%>
<%--                    <td><p th:text="${payment.name}" /></td>--%>
<%--                    <td><p th:text="${payment.amount}" /></td>--%>
<%--                    <td><p th:text="${#temporals.format(payment.date, 'dd-MM-yyyy HH:mm')}" /></td>--%>
<%--                </tr>--%>
<%--                </tbody>--%>
            </table>
            </div>
        <div class="tab-pane fade" id="v-pills-settings" role="tabpanel" aria-labelledby="v-pills-settings-tab">
<%--            <span th:if="${user.account.active == true}" th:text="#{cabinet.account.status.active}" class="badge badge-pill badge-success">Enabled</span>--%>
<%--            <span th:if="${user.account.active == false}" th:text="#{cabinet.account.status.inactive}" class="badge badge-pill badge-danger">Disabled</span>--%>
<%--            <form th:if="${user_role == 'ROLE_ADMIN'}" th:method="POST" modelAttribute="dataform" th:action="@{/acc/status}" class="form-inline">--%>
<%--                <input type="hidden" name="uid" th:value="${user.id}">--%>
<%--                <input type="hidden" name="aid" th:value="${user.account.id}">--%>
<%--                <div class="form-check form-check-inline">--%>
<%--                    <input th:checked="${user.account.active == true}" th:text="#{cabinet.menu.settings.activate}" class="form-check-input" type="radio" name="as" id="inlineRadio1" value=true>--%>
<%--                </div>--%>
<%--                <div class="form-check form-check-inline">--%>
<%--                    <input th:checked="${user.account.active == false}" th:text="#{cabinet.menu.settings.deactivate}" class="form-check-input" type="radio" name="as" id="inlineRadio2" value=false>--%>
<%--                </div>--%>
<%--                <button th:text="#{cabinet.menu.settings.set}" type="submit" class="btn btn-primary mb-2">Set</button>--%>
<%--            </form>--%>
        </div>
    </div>
</div>
</div>

</body>
</html>
