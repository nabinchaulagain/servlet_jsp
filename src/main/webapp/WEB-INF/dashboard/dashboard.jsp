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
        
        <div class="d-flex">
            <div class="bg-light border-right" id="sidebar-wrapper">
                <div class="list-group list-group-flush">
                    <a href="#" class="list-group-item list-group-item-action bg-light">Dashboard</a>
                    <a class="list-group-item list-group-item-action bg-light" data-toggle="collapse" href="#collapseExample">Users</a>
                    <div class="collapse" id="collapseExample">
                        <a href="#" class="list-group-item list-group-item-action bg-light">View Users</a>
                        <a href="#" class="list-group-item list-group-item-action bg-light">Add User</a>
                    </div>
                    <a href="#" class="list-group-item list-group-item-action bg-light">History</a>
                    <a href="#" class="list-group-item list-group-item-action bg-light">Profile</a>
                    
                </div>
            </div>

            <div id="page-content-wrapper">
                <div class="container-fluid">
                    <h1 class="my-4">Dashboard</h1>
                    <h2 class="my-4">Users</h2>
                    <div class="card-deck my-5 px-5">
                        <div class="card">
                            <div class="card-body text-center">
                                <h4 class="card-title">Total Users</h4>
                                <p class="card-text">${totalUser}</p>
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-body text-center">
                                <h4 class="card-title">Blocked Users</h4>
                                <!--Todo-->
                                <p class="card-text">1000</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
