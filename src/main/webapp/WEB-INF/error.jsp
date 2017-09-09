<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="label.site.name"/> - <spring:message code="page.error"/></title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="row">
            <div class="panel panel-danger">
                <div class="panel-heading text-center">
                    <h2><spring:message code="label.error_panel_header"/></h2>
                </div>

                <div class="panel-body">
                    <div class="col-md-2"></div>
                    <div class="col-md-8">
                        <%--@elvariable id="status" type="java.lang.String"--%>
                        <%--@elvariable id="error" type="java.lang.String"--%>
                        <%--@elvariable id="path" type="java.lang.String"--%>
                        <p>
                            <b><spring:message code="label.error"/>:</b> ${status} - ${error}
                        </p>
                        <p>
                            <b>URL:</b> ${pageContext.request.scheme}://${header.host}${path}
                        </p>
                        <p>
                            <a href="${pageContext.request.contextPath}/">
                                <spring:message code="label.back_to_main"/>
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
