<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-dark main-nav">
    <a class="navbar-brand" href="${pageContext.request.contextPath}">UMS</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto">
            <c:if test='${sessionUser != null}'>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ${sessionUser.getUsername()}
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/profile?id=${sessionUser.getId()}">My Profile</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/profile/edit">Edit Profile</a>
                        <c:if test="${sessionUser.isAdmin()}">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin">Admin Dashboard</a>
                        </c:if>
                       <form method="POST" action="${pageContext.request.contextPath}/logout">
                           <input type="submit" value="Logout" style="display:none" id="logoutNavItem"> 
                           <label class="dropdown-item" for="logoutNavItem" style="cursor:pointer">Logout</label>
                       </form>
                    </div>
                </li>
            </c:if>
            <c:if test='${sessionUser == null}'>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/register">
                        Sign up
                    </a>
                </li> 
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login">
                        Login
                    </a>
                </li> 
            </c:if>
        </ul>
    </div>
</nav>
<c:if test="${toast != null}">
    <div id="toast" class="alert ${toast.getType()}">
        ${toast.getMessage()}
        <%  session.removeAttribute("toast");%>
    </div>
</c:if>

