<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="notification" type="test.onlinecafe.dto.Notification"--%>
<c:if test='${notification != null}'>
    showNotification('<c:out value="${notification.type}"/>', '<c:out value="${notification.message}"/>');
</c:if>
