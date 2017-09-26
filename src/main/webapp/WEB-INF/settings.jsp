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
            <div class="row text-center">
                <h2><spring:message code="label.site.name"/> : <spring:message code="label.settings"/></h2>
            </div>
        </div>
        <br/>
        <div class="row">
            <div class="col-md-offset-1 col-md-10">
                <h3><spring:message code="label.discount_settings"/></h3>
            </div>
        </div>
        <div class="row">
            <div class="col-md-offset-1 col-md-10">
                <div class="row">
                    <form:form class="form-horizontal" method="POST">
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th><spring:message code="label.discount"/></th>
                                <th class="text-center"><spring:message code="label.description"/></th>
                            </tr>
                            </thead>
                            <tbody>
                                <%--@elvariable id="discountMap" type="java.util.Map<java.lang.String,test.onlinecafe.service.discount.Discount>"--%>
                            <c:forEach items="${discountMap.entrySet()}" var="discountEntry"
                                       varStatus="status">
                                <%--@elvariable id="discountEntry" type="java.util.Map.<java.lang.String,test.onlinecafe.service.discount.Discount>"--%>

                                <tr>
                                    <td>
                                        <input type="hidden" value="${discountEntry.key}"/>
                                        <label>
                                            <input type="radio" name="discount" id="${discountEntry.key}" value="${discountEntry.key}"
                                            <c:choose>
                                            <c:when test="${discountEntry.key == activeDiscount}">
                                                   checked="checked"
                                            </c:when>
                                            </c:choose>
                                            >
                                        </label>
                                    </td>
                                    <td>
                                        <c:out value="${discountEntry.value.displayName}"/>
                                    </td>
                                    <td>
                                        <label for="${discountEntry.key}">
                                                ${discountEntry.value.getDescription(pageContext.response.locale)}
                                        </label>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="row text-right">
                            <button class="btn btn-primary btn-md" type="submit">
                                <spring:message code="button.save"/>
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
</body>
</html>
