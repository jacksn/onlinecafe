<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="lastErrorMessage" type="java.lang.String"--%>
<c:if test='${lastErrorMessage != null}'>
    showErrorMessage('<c:out value="${lastErrorMessage}"/>');
</c:if>
