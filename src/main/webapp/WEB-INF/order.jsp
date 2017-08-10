<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="fragments/lang.jsp"/>
<html>
<head>
    <title><fmt:message key="label.site.name"/> - <fmt:message key="page.order.details"/></title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<script type="text/javascript">
    $(document).ready(function () {
        <jsp:include page="fragments/notification.jsp"/>
    });
</script>
<jsp:include page="fragments/header.jsp"/>
<div class="jumbotron">
    <div class="container pad">
        <div class="row">
            <div class="col-md-offset-2 col-md-8">
                <form class="form-horizontal" name="coffeeOrderForm" method="POST" action="order"
                      onsubmit="return validate()">
                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="col-md-3 control-label"><fmt:message key="label.name"/></label>
                            <div class="col-md-8">
                                <input class="form-control" id="name" name="name"
                                       placeholder="<fmt:message key="label.name"/>" type="text">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-3 control-label">
                                <fmt:message key="label.delivery.address"/>
                            </label>
                            <div class="col-md-8">
                                <input class="form-control" id="address" name="address"
                                       placeholder="<fmt:message key="label.delivery.address"/>"
                                       type="text" oninput="removeErrorHighlight(this)"
                                       oninvalid="addErrorHighlight(this)">
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <table class="table">
                            <thead>
                            <tr>
                                <th><fmt:message key="label.coffee.type"/></th>
                                <th width="15%" class="text-center"><fmt:message key="label.quantity"/></th>
                                <th width="15%" class="text-center"><fmt:message key="label.price"/></th>
                                <th width="15%" class="text-center"><fmt:message key="label.cost"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="orderItemDtoList" type="java.util.List"--%>
                            <%--@elvariable id="orderDto" type="test.onlinecafe.dto.CoffeeOrderDto"--%>
                            <c:forEach items="${orderDto.orderItems}" var="orderItemDto">
                                <tr>
                                    <td>
                                        <c:out value="${orderItemDto.coffeeType.typeName}"/>
                                    </td>
                                    <td align="center" width="15%">
                                        <c:out value="${orderItemDto.quantity}"/>
                                    </td>
                                    <td class="text-right">
                                        <fmt:formatNumber type="currency"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 "
                                                          value="${orderItemDto.coffeeType.price}"/>
                                        <fmt:message key="label.currency.symbol"/>
                                    </td>
                                    <td class="text-right <c:if test="${orderItemDto.discounted}">text-danger</c:if>">
                                        <fmt:formatNumber type="currency"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 "
                                                          value="${orderItemDto.cost}"/>
                                        <fmt:message key="label.currency.symbol"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="3" class="text-right"><strong><fmt:message key="label.subtotal"/>:</strong>
                                </td>
                                <td class="text-right">
                                    <%--@elvariable id="orderTotalCost" type="double"--%>
                                    <fmt:formatNumber type="currency"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 "
                                                      value="${orderDto.cost - orderDto.deliveryCost}"/>
                                        <fmt:message key="label.currency.symbol"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-right"><strong><fmt:message
                                        key="label.delivery.cost"/>:</strong></td>
                                <td class="text-right <c:if test="${orderDto.deliveryCost==0}">text-danger</c:if>">
                                    <%--@elvariable id="orderDeliveryCost" type="double"--%>
                                    <fmt:formatNumber type="currency"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 "
                                                      value="${orderDto.deliveryCost}"/>
                                        <fmt:message key="label.currency.symbol"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-right"><strong><fmt:message
                                        key="label.total.cost"/>:</strong></td>
                                <td class="text-right">
                                    <%--@elvariable id="orderTotalCost" type="double"--%>
                                    <fmt:formatNumber type="currency"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"
                                                      pattern="0.00 "
                                                      value="${orderDto.cost}"/>
                                        <fmt:message key="label.currency.symbol"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit">
                                <fmt:message key="button.order"/>
                            </button>
                            <a class="btn btn-danger btn-md" href="cancel"><fmt:message key="button.cancel"/></a>
                        </div>

                    </div>
                </form>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript">
    function validate() {
        var address = $('[id=address]');
        if (!address.val()) {
            showNotification('danger', '<fmt:message key="error.empty.address"/>');
            addErrorHighlight(address);
            address.focus();
            return false;
        }
    }
</script>
</body>
</html>
