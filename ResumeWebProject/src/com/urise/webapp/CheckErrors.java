package com.urise.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.urise.webapp.util.Util;

public class CheckErrors {
	public static final String ERROR_JSP = "/WEB-INF/jsp/error.jsp";

	private CheckErrors() {
	}

	public static boolean isErrorParameter(String value, String name, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (Util.isEmpty(value)) {
			request.setAttribute(NameOfAttribute.ERROR.name(), "The parameter " + name + " doesn't exist or empty !");
			request.getRequestDispatcher(ERROR_JSP).forward(request, response);
			return true;
		}
		return false;
	}
}
