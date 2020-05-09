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
                <h1 class="display-1 my-4">${user.getUsername()}</h1>
                
                <table class="table table-borderless profile-details-table">
                    <tbody>
                        <tr>
                            <th scope="row">First Name</th>
                            <td>${user.getFirstName()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Last Name</th>
                            <td>${user.getLastName()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Email</th>
                            <td>${user.getEmail()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Role</th>
                            <td>${user.getRole()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Mobile Number</th>
                            <td>${user.getPhoneNum()}</td>
                        </tr>
                        <tr>
                            <th scope="row">Joined date</th>
                            <td>${user.getJoinedDate()}</td>
                        </tr>
                    </tbody>
                </table>
                        
                <h1 class="mt-5">History:</h1>
                <table class="table my-5 table-hover">
                    <thead>
                        <tr>
                            <th>History Id</th>
                            <th>Action</th>
                            <th>Detail</th>
                            <th>Date and Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Created</td>
                            <td>Something goes here</td>
                            <td>2020/20/23</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>