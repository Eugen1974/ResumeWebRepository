<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="com.urise.webapp.util.DateUtil"%>
<%@ page import="com.urise.webapp.util.Util" %>
<%@ page import="com.urise.webapp.CheckErrors"%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="styles/resumeStyles.css">
<title>Просмотр резюме ${requestScope.RESUME.fullName}</title>
<style>
	td.fullName {
		background-color: rgb(255, 255, 153);
		font-size: 15pt;
		text-align: center;
	}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />
	<c:if test="${requestScope.RESUME == null}">
		<c:set var="ERROR" value="Object resume is null !" scope="request" />
		<jsp:forward page="${CheckErrors.ERROR_JSP}" />
	</c:if>
	<div class="div1" align="center">
		<a href="main?uuid=${requestScope.RESUME.uuid}&action=update"><img src="images/pencil.png" title="Update resume" /></a>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="main?uuid=${requestScope.RESUME.uuid}&action=delete"><img src="images/close.png"  title="Delete resume" /></a>
	</div>
	<div align="center">
		<table>
			<tr>
				<td class="fullName" colspan="2">${requestScope.RESUME.fullName}</td>
			</tr>
			<tr>
				<td class="myFont2" colspan="2" align="center">Контакты</td>
			</tr>
			<c:forEach items="${requestScope.RESUME.contacts}" var="contact">
				<tr>
					<td>${contact.key.title}</td>
					<c:choose>
						<c:when test="${contact.key == ContactType.HOME_ADDRESS ||
										contact.key == ContactType.HOME_PHONE	||
										contact.key == ContactType.MOBILE}">
							<td>${contact.value}</td>
						</c:when>
						<c:when test="${contact.key == ContactType.MAIL}">
							<td><a href="mailto:${contact.value}?subject=Письмо">${contact.value}</a></td>
						</c:when>
						<c:otherwise>
							<td><a href="${contact.value}" target="_blank">${contact.value}</a></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			<tr>
				<td class="myFont2" colspan="2" align="center">Секции</td>
			</tr>
			<c:forEach var="sections" items="${requestScope.RESUME.sections}">
				<c:set var="sectionType" value="${sections.key}" />
				<tr>
					<td align="center">${sectionType.title}</td>
					<td>
						<c:set var="section" value="${sections.value}" />
						<c:choose>
							<c:when test="${sectionType == SectionType.PERSONAL    || sectionType == SectionType.OBJECTIVE}">
								${section.content}
							</c:when>
							<c:when test="${sectionType == SectionType.ACHIEVEMENT || sectionType == SectionType.QUALIFICATIONS}">
								<c:forEach var="item" items="${section.items}">
									${item}<br>
								</c:forEach>
							</c:when>
							<c:when test="${sectionType == SectionType.EXPERIENCE  || sectionType == SectionType.EDUCATION}">
								<c:forEach var="organization" items="${section.organizations}" varStatus="status">
									<c:choose>
										<c:when test="${Util.isEmpty(organization.homePage.link)}">
											${organization.homePage.name}
										</c:when>
										<c:otherwise>
											<a href="${organization.homePage.link}" target="_blank">${organization.homePage.name}</a>
										</c:otherwise>
									</c:choose>
									<br>
									<c:forEach var="position" items="${organization.positions}">
										<c:choose>
											<c:when test="${sectionType == SectionType.EXPERIENCE}">
												<c:set var="varPattern" value="${DateUtil.PATTERN_1}" />
											</c:when>
											<c:otherwise> <%-- EDUCATION --%>
												<c:set var="varPattern" value="${DateUtil.PATTERN_2}" />
											</c:otherwise>
										</c:choose>
										<c:set var="varStartDate" value="${DateUtil.getStringFromLocalDate(position.startDate, varPattern)}" />
										<c:set var="varEndDate"   value="${DateUtil.getStringFromLocalDate(position.endDate,   varPattern)}" />
										${varStartDate}&minus;${varEndDate}&nbsp;&nbsp;&nbsp;${position.title}<br>
										<c:forEach var="description" items="${position.descriptions}">
											${description}<br>
										</c:forEach>
									</c:forEach>
									<c:if test="${!status.last}">
										<br>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:set var="ERROR" value="Doesn't processed case for sectionType ${sectionType.name()} !" scope="request" />
								<jsp:forward page="${CheckErrors.ERROR_JSP}" />
							</c:otherwise>
						</c:choose>
					</td>					
				</tr>
			</c:forEach>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
</body>
</html>
