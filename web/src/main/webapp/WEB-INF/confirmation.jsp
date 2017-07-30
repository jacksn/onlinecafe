<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online cafe - Order confirmation</title>
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
                        <h3>Confirmation</h3>
                    </div>
                    <div class="panel-body">
                        <h4>Your order is successfully accepted!</h4>
                        <br/>
                        <a href="${pageContext.request.contextPath}/">Back to main page</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
