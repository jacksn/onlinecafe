<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row">
    <%--@elvariable id="lastErrorMessage" type="java.lang.String"--%>
    <c:if test='${lastErrorMessage != null}'>
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong><c:out value="${lastErrorMessage}"/></strong>
        </div>
    </c:if>
</div>
