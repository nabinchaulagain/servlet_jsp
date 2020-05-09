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
                <h1 class="my-4">Users</h1>
                
                <table class="table my-5 table-hover">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Username</th>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Joined Date</th>
                            <th>Edit User</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${allusers}" var="user">
                            <tr>      
                                <td>${user.getId()}</td>
                                <td><a href="${pageContext.request.contextPath}/profile?id=<c:out value='${user.getId()}' />">${user.getUsername()}</a></td>
                                <td>${user.getFirstName()}</td>
                                <td>${user.getLastName()}</td>
                                <td>${user.getEmail()}</td>
                                <td>${user.getRole()}</td>
                                <td>${user.getJoinedDate()}</td>
                                <td><a href="">edit</a> <a href="">delete</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
