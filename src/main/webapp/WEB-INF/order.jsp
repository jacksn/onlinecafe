<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online cafe - Order details</title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="row">
            <c:if test='${param.get("error")=="empty_order"}'>
                <div class="alert alert-dismissible alert-danger">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <strong>Your order is empty. Enter order quantity</strong> and try submitting again.
                </div>
            </c:if>
        </div>
        <div class="row">
            <div class="col-md-2"></div>
            <div class="col-md-8">
                <form class="form-horizontal" name="coffeeform" method="post">
                    <input type="hidden" name="action" value="confirm">
                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="col-md-2 control-label">Name</label>
                            <div class="col-md-10">
                                <input class="form-control" id="name" placeholder="Name" type="text">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-2 control-label">Delivery address</label>
                            <div class="col-md-10">
                                <input class="form-control" id="address" placeholder="Delivery address" type="text">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <table class="table table-responsive">
                            <thead align="center">
                            <tr class="success">
                                <td>Coffee type</td>
                                <td>Price</td>
                                <td>Quantity</td>
                                <td>Total</td>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="coffeeOrderItems" type="java.util.List"--%>
                            <c:forEach items="${coffeeOrderItems}" var="coffeeType">
                                <jsp:useBean id="coffeeOrderItem" class="test.onlinecafe.model.CoffeeOrderItem"/>
                                <tr>
                                    <td><c:out value="${coffeeType.coffeeType.typeName}"/></td>
                                    <td align="center">
                                        <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                          value="${coffeeType.coffeeType.price}"/>
                                    </td>
                                    <td align="center" width="15%">
                                        <c:out value="${coffeeType.quantity}"/>
                                    </td>
                                    <td></td>
                                </tr>
                            </c:forEach>
                            <tr class="success">
                                <td colspan="2"></td>
                                <td align="middle">Order total:</td>
                                <td></td>

                            </tr>
                            <tr align="right">
                                <td colspan="4">
                                    <button class="btn btn-primary btn-md" type="submit">Order</button>
                                    <a class="btn btn-danger btn-md" href="${pageContext.request.contextPath}/">Cancel</a>
                                </td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</html>
