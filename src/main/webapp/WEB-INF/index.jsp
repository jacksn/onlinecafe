<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="fragments/lang.jsp"/>

<html>
<head>
    <title><fmt:message key="label.site.name"/></title>
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
                <div class="row">
                    <form class="form-horizontal" id="coffeeForm" name="coffeeForm" method="POST" action=""
                          onsubmit="return validate()">
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th><fmt:message key="label.coffee_type"/></th>
                                <th class="text-center"><fmt:message key="label.price"/></th>
                                <th class="text-center"><fmt:message key="label.quantity"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="coffeeTypes" type="java.util.List"--%>
                            <c:forEach items="${coffeeTypes}" var="coffeeType">
                                <jsp:useBean id="coffeeType" class="test.onlinecafe.model.CoffeeType"/>
                                <tr id="coffeerow_${coffeeType.id}">
                                    <td>
                                        <input type="hidden" id="id_${coffeeType.id}" name="id"
                                               value="${coffeeType.id}" disabled="">
                                        <input type="checkbox" id="selected_${coffeeType.id}" name="selected"
                                               onclick="toggleRow(this, ${coffeeType.id})"/>
                                    </td>
                                    <td><c:out value="${coffeeType.typeName}"/></td>
                                    <td align="center">
                                        <fmt:formatNumber type="currency"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 "
                                                          value="${coffeeType.price}"/>
                                        <fmt:message key="label.currency_symbol"/>
                                    </td>
                                    <td align="center" width="15%" class="order-count">
                                        <input id="quantity_${coffeeType.id}" class="form-control input-sm"
                                               name="quantity"
                                               type="number" value="0" minlength="1" maxlength="2" min="0" max="99"
                                               disabled=""
                                               oninput="removeErrorHighlight(this)" oninvalid="addErrorHighlight(this)">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit"><fmt:message
                                    key="button.order"/></button>
                            <button class="btn btn-danger btn-md" type="reset" onclick="resetForm()">
                                <fmt:message key="button.reset"/>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%--@elvariable id="discountDescription" type="java.lang.String"--%>
        <c:if test="${not empty discountDescription}">
            <div class="row text-danger text-center">
                <h4>
                    <strong><fmt:message key="label.discount"/>:</strong> <c:out value="${discountDescription}"/>
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
                    showNotification('danger', '<fmt:message key="error.invalid_quantity"/>');
                    return false;
                }
                totalOrderQuantity += quantity;
            }
        }
        if (totalOrderQuantity === 0) {
            showNotification('danger', '<fmt:message key="error.empty_order"/>');
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
