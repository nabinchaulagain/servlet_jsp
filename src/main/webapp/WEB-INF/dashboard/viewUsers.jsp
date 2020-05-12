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
                <h1 class="my-2">Users</h1>
                <div class="table-responsive">
                    <table class="table my-5 table table-hover table-bordered table-striped w-auto mx-auto">
                        <thead>
                            <tr class="bg-dark text-light">
                                <th>Username</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Joined Date</th>
                                <th style="width:auto !important">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${allusers}" var="user">
                                <tr>
                                    <td>${user.getUsername()}</td>
                                    <td>${user.getFullName()}</td>
                                    <td>${user.getEmail()}</td>
                                    <td>${user.getRole()}</td>
                                    <td>${user.getJoinedDate()}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/profile?id=${user.getId()}" class="btn btn-sm btn-success"><i class="fa fa-eye"></i> View</a>
                                        <c:if test="${!user.isAdmin()}">
                                            <a 
                                                href="${pageContext.request.contextPath}/admin/deleteUser?id=${user.getId()}"
                                                class="btn btn-sm btn-danger"><i class="fa fa-trash"></i> Delete
                                            </a>
                                            <a
                                                href="${pageContext.request.contextPath}/admin/editUser?id=${user.getId()}"
                                                class="btn btn-sm btn-warning">
                                                <i class="fa fa-edit"></i> Edit
                                            </a>
                                            <a href="#" class="btn btn-sm btn-danger"><i class="fa fa-ban"></i> Block</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>

</html>