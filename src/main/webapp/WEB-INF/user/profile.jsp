<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../bootstrap.jsp" %>
    </head>
    <body>
        <%@include file="../navbar.jsp" %>
        <div class="container-fluid">
            <div class="my-3">
               <h4>${pageTitle}</h4>
            </div>
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
                        <c:if test="${sessionUser.isAdmin() && !user.isAdmin()}">
                            <div>
                                 <a
                                        href="${pageContext.request.contextPath}/admin/editUser?id=${user.getId()}"
                                        class="btn btn-sm btn-warning">
                                        <i class="fa fa-edit"></i> Edit
                                    </a>
                                   <a 
                                        href="${pageContext.request.contextPath}/admin/deleteUser?id=${user.getId()}"
                                        class="btn btn-sm btn-danger"><i class="fa fa-trash"></i> Delete
                                    </a>
                                    <a href="#" class="btn btn-sm btn-danger"><i class="fa fa-ban"></i>Block</a>
                            </div>
                                
                        </c:if>
            <h4 class="mt-5 text-center">History:</h4>
            <table class="table mb-4 table-hover table-bordered w-auto mx-auto">
                <thead>
                    <tr>
                        <th>Action</th>
                        <th>Detail</th>
                        <th>Date and Time</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${historyList}" var="historyEvent">
                        <tr>
                            <td>${historyEvent.getAction()}</td>
                            <td>${historyEvent.getDetail()}</td>
                            <td>${historyEvent.getDateAndTime()}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>