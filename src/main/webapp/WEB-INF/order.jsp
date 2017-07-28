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
                            <label for="name" class="col-md-3 control-label">Name</label>
                            <div class="col-md-8">
                                <input class="form-control" id="name" name="name" placeholder="Name" type="text">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-3 control-label">Delivery address</label>
                            <div class="col-md-8">
                                <input class="form-control" id="address" name="address" placeholder="Delivery address"
                                       type="text">
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Coffee type</th>
                                <th width="15%" class="text-center">Quantity</th>
                                <th width="15%" class="text-center">Price</th>
                                <th width="15%" class="text-center">Cost</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="orderItemToList" type="java.util.List"--%>
                            <%--@elvariable id="orderTo" type="test.onlinecafe.to.CoffeeOrderTo"--%>
                            <c:forEach items="${orderTo.orderItems}" var="orderItemTo">
                                <tr>
                                    <td>
                                        <c:out value="${orderItemTo.coffeeType.typeName}"/>
                                    </td>
                                    <td align="center" width="15%">
                                        <c:out value="${orderItemTo.quantity}"/>
                                    </td>
                                    <td class="text-right">
                                        <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 ¤"
                                                          value="${orderItemTo.coffeeType.price}"/>
                                    </td>
                                    <td class="text-right <c:if test="${orderItemTo.discounted}">text-danger</c:if>">
                                        <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 ¤"
                                                          value="${orderItemTo.cost}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="3" class="text-right"><strong>Subtotal cost:</strong></td>
                                <td class="text-right">
                                    <%--@elvariable id="orderTotalCost" type="double"--%>
                                    <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 ¤"
                                                      value="${orderTo.cost - orderTo.deliveryCost}"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-right"><strong>Delivery cost:</strong></td>
                                <td class="text-right <c:if test="${orderTo.deliveryCost==0}">text-danger</c:if>">
                                    <%--@elvariable id="orderDeliveryCost" type="double"--%>
                                    <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 ¤"
                                                      value="${orderTo.deliveryCost}"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-right"><strong>Order total cost:</strong></td>
                                <td class="text-right">
                                    <%--@elvariable id="orderTotalCost" type="double"--%>
                                    <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 ¤"
                                                      value="${orderTo.cost}"/>
                                </td>
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
