<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../bootstrap.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <%@include file="../navbar.jsp" %>
    <%@include file="./sidebar.jsp" %>
        <div id="page-content-wrapper">
            <div class="container-fluid">
                <h1 class="my-4">Dashboard</h1>
                <div class="card-deck my-5 px-5">
                    <div class="card">
                        <div class="card-body text-center">
                            <h5 class="card-title">Total Users</h5>
                            <p class="card-text">${totalUserCount}</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-body text-center">
                            <h4 class="card-title">Blocked Users</h4>
                            <p class="card-text">${blockedUserCount}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
