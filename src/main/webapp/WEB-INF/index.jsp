<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="label.site.name"/></title>
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
            <div class="row">
                <h2><spring:message code="label.welcome"/></h2>
            </div>
            <div class="row">
                <h4><spring:message code="label.slogan"/></h4>
            </div>
        </div>
        <br/>
        <div class="row">
            <div class="col-md-offset-2 col-md-8">
                <div class="row">
                    <%--@elvariable id="coffeeTypes" type="test.onlinecafe.dto.CoffeeTypeDtoList"--%>
                    <form:form class="form-horizontal" method="POST"
                               onsubmit="return validate()" modelAttribute="coffeeTypes">
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th><spring:message code="label.coffee_type"/></th>
                                <th class="text-center"><spring:message code="label.price"/></th>
                                <th class="text-center"><spring:message code="label.quantity"/></th>
                            </tr>
                            </thead>
                            <tbody>
                                <%--@elvariable id="coffeeTypes" type="java.util.List"--%>
                            <c:forEach items="${coffeeTypes.coffeeTypeDtos}" var="coffeeType"
                                       varStatus="status">
                                <%--@elvariable id="coffeeType" type="test.onlinecafe.dto.CoffeeTypeDto"--%>
                                <tr id="coffeerow_${coffeeType.typeId}">
                                    <td>
                                        <form:input path="coffeeTypeDtos[${status.index}].typeId"
                                                    type="hidden" id="id_${coffeeType.typeId}"
                                                    value="${coffeeType.typeId}"/>
                                        <form:checkbox path="coffeeTypeDtos[${status.index}].selected"
                                                       id="selected_${coffeeType.typeId}"
                                                       name="selected"
                                                       onchange="toggleRow(this,${coffeeType.typeId})"/>
                                    </td>
                                    <td><c:out value="${coffeeType.typeName}"/></td>
                                    <td align="center">
                                        <fmt:formatNumber type="currency"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 "
                                                          value="${coffeeType.price}"/>
                                        <spring:message code="label.currency_symbol"/>
                                    </td>
                                    <td align="center" width="15%" class="order-count">
                                        <form:input path="coffeeTypeDtos[${status.index}].quantity"
                                                    id="quantity_${coffeeType.typeId}"
                                                    class="form-control input-sm"
                                                    type="number" value="0" minlength="1" maxlength="2" min="0" max="99"
                                                    disabled="true"
                                                    oninput="removeErrorHighlight(this)"
                                                    oninvalid="addErrorHighlight(this)"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit">
                                <spring:message code="button.continue"/>
                            </button>
                            <button class="btn btn-danger btn-md" type="reset" onclick="resetForm()">
                                <spring:message code="button.reset"/>
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <%--@elvariable id="discountDescription" type="java.lang.String"--%>
        <c:if test="${not empty discountDescription}">
            <div class="row text-danger text-center">
                <h4>
                    <strong><spring:message code="label.discount"/>:</strong> <c:out value="${discountDescription}"/>
                </h4>
            </div>
        </c:if>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript">
    function toggleRow(checkbox, itemId) {
        var disabled = !checkbox.checked;
        $("#id_" + itemId).prop('disabled', disabled);
        var inputField = $("#quantity_" + itemId);
        inputField.prop('disabled', disabled);
        if (disabled) {
            inputField.val("0");
        }
    }

    function validate() {
        var rows = $('[id^=coffeerow_]');
        var totalOrderQuantity = 0;
        for (var i = 0; i < rows.length; i++) {
            var row = $(rows[i]);
            var idField = row.find('[id^=id_]');
            var checkbox = row.find('[id^=selected_]');
            var quantityField = row.find('[id^=quantity_]');

            if (!idField.prop('disabled') && !quantityField.prop('disabled') && checkbox.prop('checked')) {
                var quantity = parseInt(quantityField.val());
                if (isNaN(quantity) || quantity === 0) {
                    addErrorHighlight(quantityField);
                    quantityField.focus();
                    showNotification('danger', '<spring:message code="error.invalid_quantity"/>');
                    return false;
                }
                totalOrderQuantity += quantity;
            }
        }
        if (totalOrderQuantity === 0) {
            showNotification('danger', '<spring:message code="error.empty_order"/>');
            return false;
        }
    }

    function resetForm() {
        var rows = $('[id^=coffeerow_]');
        for (var i = 0; i < rows.length; i++) {
            $(rows[i]).find('[id^=id_]').prop('disabled', true);
            removeErrorHighlight($(rows[i]).find('[id^=quantity_]').prop('disabled', true));
        }
    }
</script>
</body>
</html>
