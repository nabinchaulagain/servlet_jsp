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
            <div class="card bg-light mt-3 mx-auto col-md-4 col-8 p-4" style="position:fixed;top:15%;left:40%;" >
                <h4 class="card-title">Are you sure you want to delete  ${user.getUsername()}'s account? </h4>
                <div class="mb-4 mx-auto text-center">
                    <form method="POST" style="display:inline">
                        <input type="submit" class="btn btn-lg btn-success" value="Yes, I want to">
                    </form>
                    <a class="btn btn-lg btn-danger ml-3" href="${pageContext.request.contextPath}/admin/users">No, Take me back</a>
                </div>
            </div>
        </div>
    </body>
</html>