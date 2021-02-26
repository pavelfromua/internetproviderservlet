<%@ page contentType="text/html; charset=UTF-8; " language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${language}">
<head>
	<jsp:include page="/WEB-INF/views/style.jsp"/>
	<c:set var="title" value="Error" scope="page" />
</head>
<body>

<jsp:include page="/WEB-INF/views/head.jsp"/>

	<table id="main-container">

		<tr >
			<td class="content">
				<h2 class="error">
					The following error occurred
				</h2>


				<c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
				<c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
				<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>
				
				<c:if test="${not empty code}">
					<h3>Error code: ${code}</h3>
				</c:if>			
				
				<c:if test="${not empty message}">
					<h3>${message}</h3>
				</c:if>

				
				<%-- if we get this page using forward --%>
				<c:if test="${not empty requestScope.errorMessage}">
					<h3>${requestScope.errorMessage}</h3>
				</c:if>

			<%-- CONTENT --%>
			</td>
		</tr>
		
	</table>
</body>
</html>