<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <div class="row">
            <div class="row">
                <div class="col-md-offset-2 col-md-8">
                    <form class="form-horizontal" id="coffeeform" name="coffeeform" method="post" action=""
                          onsubmit="return validate()">
                        <input type="hidden" name="action" value="create">
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th>Coffee type</th>
                                <th class="text-center">Price</th>
                                <th class="text-center">Quantity</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="coffeeTypes" type="java.util.List"--%>
                            <c:forEach items="${coffeeTypes}" var="coffeeType">
                                <jsp:useBean id="coffeeType" class="test.onlinecafe.model.CoffeeType"/>
                                <tr id="coffeerow_${coffeeType.id}">
                                    <td>
                                        <input type="hidden" id="id_${coffeeType.id}" name="id[]"
                                               value="${coffeeType.id}" disabled="">
                                        <input type="checkbox" id="selected_${coffeeType.id}" name="selected[]"
                                               onclick="toggleRow(this, ${coffeeType.id})"/>
                                    </td>
                                    <td><c:out value="${coffeeType.typeName}"/></td>
                                    <td align="center">
                                        <fmt:formatNumber type="currency" currencySymbol="TGR"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"
                                                          pattern="0.00 ¤"
                                                          value="${coffeeType.price}"/>
                                    </td>
                                    <td align="center" width="15%" class="order-count">
                                        <input id="quantity_${coffeeType.id}" class="form-control input-sm"
                                               name="quantity[]"
                                               type="number" value="0" minlength="1" maxlength="2" min="0" max="99"
                                               disabled=""
                                               oninput="removeErrorHighlight(this)" oninvalid="addErrorHighlight(this)">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit">Order</button>
                            <button class="btn btn-danger btn-md" type="reset">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript" src="js/util.js"></script>
</body>
</html>
