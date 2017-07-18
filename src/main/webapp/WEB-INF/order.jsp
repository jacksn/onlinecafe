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
            <jsp:include page="fragments/errorMessage.jsp"/>
            <div class="col-md-offset-2 col-md-8">
                <form class="form-horizontal" name="coffeeform" method="post">
                    <input type="hidden" name="action" value="confirm">
                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="col-md-2 control-label">Name</label>
                            <div class="col-md-10">
                                <input class="form-control" id="name" name="name" placeholder="Name" type="text">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-2 control-label">Delivery address</label>
                            <div class="col-md-10">
                                <input class="form-control" id="address" name="address" placeholder="Delivery address" type="text">
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Coffee type</th>
                                <th class="text-center">Price</th>
                                <th class="text-center">Quantity</th>
                                <th class="text-center">Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="coffeeOrderItems" type="java.util.List"--%>
                            <c:forEach items="${coffeeOrderItems}" var="coffeeOrderItem">
                                <jsp:useBean id="coffeeOrderItem" class="test.onlinecafe.model.CoffeeOrderItem"/>
                                <tr>
                                    <td><c:out value="${coffeeOrderItem.coffeeType.typeName}"/></td>
                                    <td align="center">
                                        <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          value="${coffeeOrderItem.coffeeType.price}"/>
                                    </td>
                                    <td align="center" width="15%">
                                        <c:out value="${coffeeOrderItem.quantity}"/>
                                    </td>
                                    <td></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="2"></td>
                                <td class="text-center"><strong>Order total:</strong></td>
                                <td></td>

                            </tr>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit">Order</button>
                            <a class="btn btn-danger btn-md"
                               href="${pageContext.request.contextPath}/reset">Cancel</a>
                        </div>

                    </div>
                </form>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</html>
