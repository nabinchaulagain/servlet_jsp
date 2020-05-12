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
            <form class="container col-xl-5 col-sm-8 authForm col-11 p-3 bg-light mt-4" method="POST">
                <h3 class="text-center">Edit Profile</h3>
                <div class="col-9 mx-auto">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="username" class='form-control ${(errors.containsKey("username"))?"is-invalid":""}' value="${initialValues.getUsername()}" required>
                        <c:if test='${errors.containsKey("username")}'>
                            <small class="text-danger">${errors.get("username")}</small>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" class="form-control ${errors.containsKey("email")?"is-invalid":""}" value="${initialValues.getEmail()}" required>
                        <c:if test='${errors.containsKey("email")}'>
                            <small class="text-danger">${errors.get("email")}</small>
                        </c:if>
                    </div>
                    <div class="row">
                        <div class="form-group col-6">
                            <label>First Name</label>
                            <input type="text" name="firstName" class="form-control ${errors.containsKey("firstName")?"is-invalid":""}" value="${initialValues.getFirstName()}" required>
                            <c:if test='${errors.containsKey("firstName")}'>
                                <small class="text-danger">${errors.get("firstName")}</small>
                            </c:if>
                        </div>
                        <div class="form-group col-6">
                            <label>Last Name</label>
                            <input type="text" name="lastName" class="form-control  ${errors.containsKey("lastName")?"is-invalid":""}" value="${initialValues.getLastName()}" required>
                            <c:if test='${errors.containsKey("lastName")}'>
                                <small class="text-danger">${errors.get("lastName")}</small>
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Phone number</label>
                        <input type="text" name="phoneNum" class="form-control ${errors.containsKey("phoneNum")?"is-invalid":""}"  value="${initialValues.getPhoneNum()}" required>
                        <c:if test='${errors.containsKey("phoneNum")}'>
                            <small class="text-danger">${errors.get("phoneNum")}</small>
                        </c:if>
                    </div>
                    <div class="text-center mt-2">
                        <input type="submit" class='btn btn-info' value="Edit Profile" >
                    </div>
                </div>
            </form>
    </body>
</html>