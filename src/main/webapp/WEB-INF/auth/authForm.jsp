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
            <h3 class="text-center">${formType}</h3>
            <div class="col-9 mx-auto">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" class='form-control ${(errors.containsKey("username"))?"is-invalid":""}' value="${initialValues.getUsername()}" required>
                    <c:if test='${errors.containsKey("username")}'>
                            <small class="text-danger">${errors.get("username")}</small>
                    </c:if>
                </div>   
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control ${errors.containsKey("password")?"is-invalid":""}"  value="${initialValues.getPassword()}" required>
                    <c:if test='${errors.containsKey("password")}'>
                            <small class="text-danger">${errors.get("password")}</small>
                    </c:if>
                </div>

                <c:if test='${formType.equals("Sign up")}'>
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
                            <input type="text" name="lastName" class="form-control  ${errors.containsKey("lastName")?"is-invalid":""}" value="${initialValues.getLastName()}"required>
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
                </c:if>
                <div class="text-center">
                    <input type="submit" class='btn btn-${formType.equals("Sign up")?"danger":"success"}' value="${formType}" >
                </div>
                <div class="text-center mt-3" style="font-size:75%;">
                    <c:if test='${formType.equals("Sign up")}'>
                        Already have an account? <a href="${pageContext.request.contextPath}/login" class="text-dark">Login here</a>
                    </c:if>
                    <c:if test='${formType.equals("Login")}'>
                        Don't have an account? <a href="${pageContext.request.contextPath}/register" class="text-dark">Sign up here</a>
                    </c:if>
                </div>
            </div>
        </form>
    </body>
</html>
