<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="locale" type="java.util.Locale"--%>
<fmt:setLocale value="${locale.language}" scope="session"/>
<fmt:setBundle basename="messages.app" scope="session"/>
