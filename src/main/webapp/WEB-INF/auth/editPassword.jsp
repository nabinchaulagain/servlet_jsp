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
        <form class="container col-xl-5 col-sm-7 col-11 p-3 bg-light authForm" method="POST">
            <h3 class="text-center">Edit Password</h3>
            <div class="col-9 mx-auto">
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class='form-control ${error != null?"is-invalid":""}' value="${initialValue}" required>
                    <c:if test='${error != null}'>
                        <small class="text-danger">${error}</small>
                    </c:if>
                </div>   
                <div class="text-center">
                    <input type="submit" class="btn btn-info" value="Reset password">
                </div>
            </div>
        </form>
    </body>
</html>
