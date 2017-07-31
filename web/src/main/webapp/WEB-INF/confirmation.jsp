<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="fragments/lang.jsp"/>
<html>
<head>
    <title><fmt:message key="label.site.name"/> - <fmt:message key="page.confirmation"/></title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="row">
            <div class="col-md-offset-2 col-md-8">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3><fmt:message key="page.confirmation"/></h3>
                    </div>
                    <div class="panel-body">
                        <h4><fmt:message key="label.order.accepted"/></h4>
                        <br/>
                        <a href="${pageContext.request.contextPath}/"><fmt:message key="label.back.to.main"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
