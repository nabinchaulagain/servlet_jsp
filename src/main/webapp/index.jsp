<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="./WEB-INF//bootstrap.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <%@include file="./WEB-INF/navbar.jsp" %>
    <div class="col-md-6 mt-5 mx-auto jumbotron">
        <h1 style="font-size:500%;text-align:center"><i class="fa fa-users"></i></h1>
        <h1 class="text-center">User Management System</h1>
    </div>
    <c:if test="${sessionUser == null}">
        <div class="row col-md-10 mx-auto mt-5">
            <div class="col-md-3 mr-3 card mx-auto p-3">
                <h4 class="text-center">Login</h4>
                <p>Login to the application to explore it's features</p>
                <div class="text-center">
                    <a class="btn btn-success text-light px-3"  href="${pageContext.request.contextPath}/login">Login</a>
                </div>
            </div>
            <div class="col-md-3 mr-3 card mx-auto p-3">
                <h4 class="text-center">Sign Up</h4>
                <p>Sign up to create an account so that you can login</p>
                <div class="text-center">
                    <a class="btn btn-danger text-light px-3" href="${pageContext.request.contextPath}/register">Sign Up</a>             
                </div>
            </div>
            <div class="col-md-3 mr-3 card mx-auto p-3">
                <h4 class="text-center">Forgot password</h4>
                <p>Forgot your password? We'll help you recover your account.</p>
                <div class="text-center">
                    <a class="btn btn-info text-light" href="${pageContext.request.contextPath}/forgotPassword">Recover Password</a>
                </div>
            </div>
        </div>
    </c:if>
</body>
</html>
