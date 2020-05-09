<div class="d-flex">
    <div class="bg-light border-right" id="sidebar-wrapper">
        <div class="list-group list-group-flush">
            <a href="#" class="list-group-item list-group-item-action bg-light">Dashboard</a>
            <a class="list-group-item list-group-item-action bg-light" data-toggle="collapse" href="#collapseExample">Users</a>
            <div class="collapse" id="collapseExample">
                <a href="/ums/admin/users" class="list-group-item list-group-item-action bg-light">View Users</a>
                <a href="addUser" class="list-group-item list-group-item-action bg-light">Add User</a>
            </div>
            <a href="#" class="list-group-item list-group-item-action bg-light">History</a>
            <a href="/ums/profile?id=<c:out value='${user.getId()}' />" class="list-group-item list-group-item-action bg-light">Profile</a>
        </div>
    </div>