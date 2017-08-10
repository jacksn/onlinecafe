<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="fragments/lang.jsp"/>
<html>
<head>
    <title><fmt:message key="label.site.name"/> - <fmt:message key="page.error"/></title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="row">
            <div class="panel panel-danger">
                <div class="panel-heading text-center">
                    <h2><fmt:message key="label.error.panel.header"/></h2>
                </div>

                <div class="panel-body">
                    <div class="col-md-2"></div>
                    <div class="col-md-8">
                        <p><b><fmt:message key="label.error.code"/>:</b> ${pageContext.errorData.statusCode}</p>
                        <p>
                            <b>URL:</b>
                            ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}
                        </p>
                        <p><a href="${pageContext.request.contextPath}/"><fmt:message key="label.back.to.main"/></a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
