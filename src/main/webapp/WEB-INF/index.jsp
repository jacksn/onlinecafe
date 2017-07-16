<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online Cafe</title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <form class="form-horizontal">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td></td>
                        <td>Type name</td>
                        <td>Price</td>
                        <td>Order</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${coffeeTypes}" var="coffeeType">
                        <jsp:useBean id="coffeeType" scope="page" class="test.onlinecafe.model.CoffeeType"/>
                        <tr>
                            <td><input type="hidden" value="<c:out value="${coffeeType.id}"/>"></td>
                            <td><c:out value="${coffeeType.typeName}"/></td>
                            <td><c:out value="${coffeeType.price}"/></td>
                            <td width="15%"><input class="form-control input-sm" type="number" value="0"></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="col-md-6"></div>
                <div class="col-md-6">
                    <div align="right">
                        <button class="btn btn-primary btn-md" type="submit">Place order</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
