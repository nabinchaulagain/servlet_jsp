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
                <h1 class="mt-2 mb-3">Reports</h1>
                <form class="ml-5 form-inline" >
                    <div class="form-group mr-2">
                        <label class="mr-2"><i class="fa fa-calendar mr-1"></i>From</label>
                        <input type="date" name="from" class="form-control ${errors.containsKey("from")?'is-invalid':''}" value="${fromDate}" required>
                    </div>
                    <div class="form-group mr-2">
                         <label class="mr-2"><i class="fa fa-calendar mr-1"></i> To</label>
                        <input type="date" name="to" class="form-control ${errors.containsKey("to")?'is-invalid':''}" value="${toDate}" required>
                    </div>
                    <input type="submit" class="btn btn-info" value="Search">
                </form>
                <c:if test="${users != null && users.size() !=0}">
                    <div class="table-responsive">
                        <table class="table my-5 table table-hover table-bordered table-striped w-auto mx-auto">
                            <thead>
                                <tr class="bg-dark text-light">
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Name</th>
                                    <th>Joined Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td>${user.getUsername()}</td>
                                        <td>${user.getEmail()}</td>
                                        <td>${user.getFullName()}</td>
                                        <td>${user.getJoinedDate()}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${users.size()==0}">
                    <h1 class="text-center mt-3">No users found</h1>
                </c:if>
            </div>
        </div>
    </body>
</html>