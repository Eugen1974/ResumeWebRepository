<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.CheckErrors" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page import="com.urise.webapp.util.Util" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="styles/resumeStyles.css">
<title>Корректировка резюме ${requestScope.RESUME.fullName}</title>
</head>
<body>
	<c:if test="${requestScope.RESUME == null}">
		<c:set var="ERROR" value="Object resume is null !" scope="request" />
		<jsp:forward page="${CheckErrors.ERROR_JSP}" />
	</c:if>
	<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />
	<div class="div1" align="center">
		<a href="main?uuid=${requestScope.RESUME.uuid}&action=delete"><img src="images/close.png" title="Delete resume" /></a>
	</div>
	<div align="center">
	  <form action="main?action=save" method="post">
	  	<input type="hidden" name="uuid" value="${requestScope.RESUME.uuid}" />
		<table>
			<tr>
				<td>ФИО</td>
				<td><input class="myFont" type="text" name="fullName" size="60" value="${requestScope.RESUME.fullName}" /></td>
			</tr>
			<tr>
				<td class="myFont2" colspan="2" align="center">Контакты</td>
			</tr>
			<c:forEach var="contactType" items="${ContactType.values()}">
				<tr>
					<td>${contactType.title}</td>
					<td>
						<input class="myFont" type="text" name="${contactType.name()}" size="60" value="${requestScope.RESUME.getContact(contactType)}" />
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td class="myFont2" colspan="2" align="center">Секции</td>
			</tr>
			<c:forEach var="sectionType" items="${SectionType.values()}">
				<c:set var="section" value="${requestScope.RESUME.getSection(sectionType)}" />
				<tr>
					<td align="center">${sectionType.title}</td>
					<c:choose>
						<c:when test="${sectionType == SectionType.PERSONAL    || sectionType == SectionType.OBJECTIVE}">
							<td> 
							  <textarea class="myFont" name="${sectionType.name()}" cols="80" rows="2">${section.content}</textarea>
							</td>
						</c:when>
						<c:when test="${sectionType == SectionType.ACHIEVEMENT || sectionType == SectionType.QUALIFICATIONS}">
							<c:set var="varItemsListSection" value="" />
							<c:if test="${section.items != null && section.items.size() != 0}">
								<c:set var="varItemsListSection" value="${String.join(\"&#010;\", section.items)}" />
							</c:if>
							<td>
								<textarea class="myFont" name="${sectionType.name()}" cols="80" rows="3">${varItemsListSection}</textarea>
							</td>
						</c:when>
						<c:when test="${sectionType == SectionType.EXPERIENCE  || sectionType == SectionType.EDUCATION}">
							<td>
								<table class="noneBorder">
	 								<c:forEach var="organization" items="${section.organizations}" varStatus="statusOrganization">
	 								<c:set var="varPrefix" value="${sectionType.name()}${statusOrganization.index}" />
									<tr>
										<td class="noneBorderHeader">Организация</td>
										<td class="noneBorder"><input class="myFont" type="text" name="${varPrefix}name" size="40" value="${organization.homePage.name}" /></td>
									</tr>
									<tr>
										<td class="noneBorderHeader">URL</td>
										<td class="noneBorder"><input class="myFont" type="text" name="${varPrefix}link" size="30" value="${organization.homePage.link}" /></td>
									</tr>
									<c:forEach var="position" items="${organization.positions}">
										<c:choose>
											<c:when test="${sectionType == SectionType.EXPERIENCE}">
												<c:set var="varPattern"		value="${DateUtil.PATTERN_1}" />
												<c:set var="varMaxLength" 	value="7" />
												<c:set var="varTitle" 		value="Date format MM.YYYY" />
												<c:set var="varPlaceHolder" value="mm.yyyy" />
											</c:when>
											<c:otherwise> <%-- EDUCATION --%>
												<c:set var="varPattern" 	value="${DateUtil.PATTERN_2}" />
												<c:set var="varMaxLength" 	value="4" />
												<c:set var="varTitle" 		value="Date format YYYY" />
												<c:set var="varPlaceHolder" value="yyyy" />
											</c:otherwise>
										</c:choose>
										<c:set var="varStartDate" value="${DateUtil.getStringFromLocalDate(position.startDate, varPattern)}"/>
										<c:set var="varEndDate"   value="${DateUtil.getStringFromLocalDate(position.endDate,   varPattern)}"/>
										<tr>
											<td class="noneBorderHeader">Начальная дата</td>
											<td class="noneBorder"><input class="myFont" style="text-align: center;" type="text" name="${varPrefix}startDate" size="5" maxlength="${varMaxLength}" title="${varTitle}" placeholder="${varPlaceHolder}" value="${varStartDate}" /></td>
										</tr>
										<tr>
											<td class="noneBorderHeader">Конечная дата</td>
											<td class="noneBorder"><input class="myFont" style="text-align: center;" type="text" name="${varPrefix}endDate"   size="5" maxlength="${varMaxLength}" title="${varTitle}" placeholder="${varPlaceHolder}" value="${varEndDate}" /></td>
										</tr>
										<tr>
											<td class="noneBorderHeader">Должность</td>
											<td class="noneBorder"><input class="myFont" type="text" name="${varPrefix}title" size="40" value="${position.title}" /></td>
										</tr>
										<c:set var="varDescription" value="" />					
										<c:if test="${!Util.isCollectionEmpty(position.descriptions)}">
											<c:set var="varDescription" value="${String.join(\"&#010;\", position.descriptions)}" />	
										</c:if>
										<tr>
											<td class="noneBorderHeader">Описание</td>
											<td class="noneBorder"><textarea class="myFont" name="${varPrefix}description" cols="80" rows="3">${varDescription}</textarea></td>
										</tr>
									</c:forEach>
									<c:if test="${!statusOrganization.last}">
										<tr>
											<td class="noneBorder" colspan="2">
												<hr>
											</td>
										</tr>
									</c:if>
	 								</c:forEach>
								</table>
							</td>
						</c:when>
						<c:otherwise>
							<c:set var="ERROR" value="Doesn't processed case for sectionType ${sectionType.name()} !" scope="request"/>
							<jsp:forward page="${CheckErrors.ERROR_JSP}" />
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2" align="center">
					<input class="myFont" type="reset"  title="Отменить изменения" value="Отменить" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="myFont" type="submit" title="Сохранить изменения" value="Сохранить"/>
				</td>
			</tr>
		</table>
	  </form>
	</div>
	<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
</body>
</html>
