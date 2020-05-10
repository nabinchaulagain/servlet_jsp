<div class="d-flex">
    <div class="bg-light border-right" id="sidebar-wrapper">
        <div class="list-group list-group-flush">
            <a href="${pageContext.request.contextPath}/admin" class="list-group-item list-group-item-action bg-light">Dashboard</a>
            <a class="list-group-item list-group-item-action bg-light" data-toggle="collapse" href="#collapseExample">Users</a>
            <div class="collapse" id="collapseExample">
                <a href="${pageContext.request.contextPath}/admin/users" class="list-group-item list-group-item-action bg-light">View Users</a>
                <a href="${pageContext.request.contextPath}/admin/addUser" class="list-group-item list-group-item-action bg-light">Add User</a>
            </div>
            <a href="#" class="list-group-item list-group-item-action bg-light">Report</a>
            <a href="${pageContext.request.contextPath}/profile?id=<c:out value='${sessionUser.getId()}' />" class="list-group-item list-group-item-action bg-light">My Profile</a>
        </div>
    </div>