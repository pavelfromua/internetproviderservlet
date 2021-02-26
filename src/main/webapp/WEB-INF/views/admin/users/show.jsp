<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cust" uri="/WEB-INF/tld/customTags.tld" %>

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
                    <button type="submit" class="btn btn-primary" value="signIn"><fmt:message key="show.user.edit" /></button>
                </form>
            </div>
            <div class="tab-pane fade" id="v-pills-plans" role="tabpanel" aria-labelledby="v-pills-plans-tab">
                <c:if test="${account.active == false}">
                    <div class="alert alert-info" role="alert">
                        <fmt:message key="cabinet.account.status.message" />
                    </div>
                </c:if>
                <ul class="list-group">
                <c:forEach var="product" items="${products}">
                    <li  class="list-group-item d-flex justify-content-between align-items-center">
                        <span>${product.description}</span>
                        <a href="/admin/users/cab/assign?uid=${client.id}&aid=${account.id}&pid=${product.product.id}&plid=${product.plan.id}">
                            <c:if test="${account.active == true}">
                                <span class="badge badge-primary badge-pill">+</span>
                            </c:if>
                        </a>
                    </li>
                </c:forEach>
                </ul>
            </div>
            <div class="tab-pane fade" id="v-pills-payments" role="tabpanel" aria-labelledby="v-pills-payments-tab">
                <c:if test="${account.active == false}">
                    <div class="alert alert-info" role="alert">
                        <fmt:message key="cabinet.account.status.message" />
                    </div>
                </c:if>
                <table width="100%">
                    <tr><td>
                        <form method="POST" action="/admin/users/cab/pay" class="form-inline">
                            <c:if test="${messages.containsKey('name')}">
                                <p style="color: red">${messages.get('name')}</p>
                            </c:if>
                            <div class="form-group mb-2" >
                                <fmt:message key="cabinet.menu.payments.menupay.label" />
                            </div>
                            <input type="hidden" id="uid" name="uid" value="${client.id}">
                            <input type="hidden" id="aid" name="aid" value="${account.id}">
                            <div class="form-group mb-2" >
                                <input type="number" name="amount" class="form-control" min="0.01" value="0" step="0.01">
                            </div>
                            <button type="submit" class="btn btn-primary mb-2" >
                                <fmt:message key="cabinet.menu.payments.menupay.btn" />
                            </button>
                        </form>
                    </td></tr>
                    <tr><td><fmt:message key="cabinet.menu.payments.balance" />: ${balance}</td></tr>
                </table>
                <table class="table table-hover">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col" ><fmt:message key="cabinet.menu.payments.history.name" /></th>
                    <th scope="col" ><fmt:message key="cabinet.menu.payments.history.amount" /></th>
                    <th scope="col" ><fmt:message key="cabinet.menu.payments.history.date" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="payment" items="${payments}" varStatus="custStat">
                <tr>
                    <th scope="row"><c:out value="${custStat.index + 1}"/></th>
                    <td><c:out value="${payment.name}"/></td>
                    <td><c:out value="${payment.amount}"/></td>
                    <td><cust:dtf><c:out value="${payment.date}"/></cust:dtf></td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
            </div>
        <div class="tab-pane fade" id="v-pills-settings" role="tabpanel" aria-labelledby="v-pills-settings-tab">
            <c:if test="${account.active == true}">
                <span class="badge badge-pill badge-success"><fmt:message key="cabinet.account.status.active" /></span>
            </c:if>
            <c:if test="${account.active == false}">
                <span class="badge badge-pill badge-danger"><fmt:message key="cabinet.account.status.inactive" /></span>
            </c:if>

            <form method="POST" action="/admin/users/cab/status" class="form-inline">
                <input type="hidden" name="uid" value="${client.id}">
                <input type="hidden" name="aid" value="${account.id}">
                <div class="form-check form-check-inline">
                    <c:choose>
                        <c:when test="${account.active == true}">
                            <input checked class="form-check-input" type="radio" name="as" id="inlineRadio1" value=true><fmt:message key="cabinet.menu.settings.activate" />
                        </c:when>
                        <c:otherwise>
                            <input class="form-check-input" type="radio" name="as" id="inlineRadio1" value=true><fmt:message key="cabinet.menu.settings.activate" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-check form-check-inline">
                    <c:choose>
                        <c:when test="${account.active == false}">
                            <input checked class="form-check-input" type="radio" name="as" id="inlineRadio2" value=false><fmt:message key="cabinet.menu.settings.deactivate" />
                        </c:when>
                        <c:otherwise>
                            <input class="form-check-input" type="radio" name="as" id="inlineRadio2" value=false><fmt:message key="cabinet.menu.settings.deactivate" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <button type="submit" class="btn btn-primary mb-2"><fmt:message key="cabinet.menu.settings.set" /></button>
            </form>
        </div>
    </div>
</div>
</div>

</body>
</html>
