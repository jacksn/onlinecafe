<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<header class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button aria-controls="bs-navbar" aria-expanded="false" class="collapsed navbar-toggle"
                    data-target="#bs-navbar" data-toggle="collapse" type="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                <img src="img/java-coffee-cup-32x32w.png" align="left">
                <fmt:message key="label.site.name"/>
            </a>
        </div>
        <nav class="collapse navbar-collapse" id="bs-navbar">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="https://github.com/jacksn/onlinecafe">
                        <i class="fa fa-github fa-lg"></i>
                        <fmt:message key="label.source_code"/>
                    </a>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <%--@elvariable id="locale" type="java.util.Locale"--%>
                        <c:out value="${locale.language}"/>
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a onclick="setLanguage('en')">English</a></li>
                        <li><a onclick="setLanguage('ru')">Русский</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>