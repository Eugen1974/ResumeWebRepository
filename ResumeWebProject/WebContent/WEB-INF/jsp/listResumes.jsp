<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.CheckErrors" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="styles/resumeStyles.css">
<title>Список всех резюме</title>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />
	<c:if test="${requestScope.RESUMES == null}">
		<c:set var="ERROR" value="List resumes is null !" scope="request" />
		<jsp:forward page="${CheckErrors.ERROR_JSP}" />
	</c:if>
	<div class="div1" align="center">
		<a href="main?action=insert" title="Add resume"><img src="images/add.png"></a>
	</div>
	<div align="center">
		<table>
			<tr align="center">
				<td>Имя</td>
				<td>Email</td>
				<td  colspan="2">Операции</td>
			</tr>
			<c:forEach items="${requestScope.RESUMES}" var="resume">
				<tr>
					<td>
						<a href="main?uuid=${resume.uuid}&action=view"	 title="View resume">${resume.fullName}</a>
					</td>
					<td><a href="mailto:${resume.getContact(ContactType.MAIL)}?subject=Письмо">${resume.getContact(ContactType.MAIL)}</a></td>
					<td align="center">
						<a href="main?uuid=${resume.uuid}&action=update" title="Update resume"><img src="images/pencil.png"></a>
					</td>
					<td align="center">
						<a href="main?uuid=${resume.uuid}&action=delete" title="Delete resume"><img src="images/close.png"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
</body>
</html>