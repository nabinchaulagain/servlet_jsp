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
                <h4 class="my-3">${user.getUsername()}'s profile</h4>
                <table class="table table-borderless table-sm profile-details-table">
                    <tbody>
                        <tr>
                            <th scope="row">Name</th>
                            <td>${user.getFullName()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Email</th>
                            <td>${user.getEmail()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Mobile Number</th>
                            <td>${user.getPhoneNum()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Joined On</th>
                            <td>${user.getJoinedDate()}</td>
                        </tr>
                    </tbody>
                </table>
                        
                <h4 class="mt-5">History:</h4>
                <table class="table my-2 table-hover">
                    <thead>
                        <tr>
                            <th>History Id</th>
                            <th>Action</th>
                            <th>Detail</th>
                            <th>Date and Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${allHistories}" var="history">
                            <tr>
                                <td>${history.getId()}</td>
                                <td>${history.getAction()}</td>
                                <td>${history.getDetail()}</td>
                                <td>${history.getDateAndTime()}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>