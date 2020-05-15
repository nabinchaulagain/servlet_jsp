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
                <h4 class="card-title">${pageTitle}</h4>
                <form method="POST" class="mb-2 mt-2">
                    <div >
                        <label>Reason in short</label>
                        <textarea name="reason" class='form-control  ${error != null?"is-invalid":""}' rows="3" placeholder="e.g: breaking the rules">${intialValue}</textarea>
                        <c:if test='${error != null}'>
                            <small class="text-danger">${error}</small>
                        </c:if>
                    </div>
                    <div class="text-center">
                        <input type="submit" class="btn btn-danger mt-3" value="Block">
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>