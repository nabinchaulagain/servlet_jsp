<div class="d-flex">
    <div class="bg-light" id="sidebar-wrapper">
        <div class="list-group list-group-flush">
            <a href="${pageContext.request.contextPath}/admin" class='list-group-item list-group-item-action bg-light ${path.equals("/admin")?"selected":""}'>
                <i class="fa fa-tachometer mr-1"></i>Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/users" class='list-group-item list-group-item-action bg-light ${path.equals("/admin/users")?"selected":""}'> 
                <i class="fa fa-users mr-1"></i>Users
            </a>
            <a href="${pageContext.request.contextPath}/admin/reports" class="list-group-item list-group-item-action bg-light ${path.equals("/admin/reports")?"selected":""}">
                <i class="fa fa-file mr-1"></i>Reports
            </a>
            <a href="${pageContext.request.contextPath}/admin/addUser" class="list-group-item list-group-item-action bg-light ${path.equals("/admin/addUser")?"selected":""}">
                <i class="fa fa-user-plus mr-1"></i>Add User
            </a>
         
        </div>
    </div>