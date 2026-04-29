<%-- Shared admin sidebar navigation --%>
<div class="sidebar d-flex flex-column p-3" style="width:230px; min-width:230px;">
    <div class="text-white fw-bold fs-5 mb-4 ps-2">
        <i class="bi bi-hospital-fill me-2"></i>CCHC Admin
    </div>
    <ul class="nav flex-column flex-grow-1">
        <li class="nav-item">
            <a class="nav-link <%= request.getServletPath().contains("/admin/dashboard") ? "active" : "" %>"
               href="<%= request.getContextPath() %>/admin/dashboard">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getServletPath().contains("/admin/users") ? "active" : "" %>"
               href="<%= request.getContextPath() %>/admin/users">
                <i class="bi bi-people me-2"></i>Users
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getServletPath().contains("/admin/services") ? "active" : "" %>"
               href="<%= request.getContextPath() %>/admin/services">
                <i class="bi bi-hospital me-2"></i>Clinics &amp; Services
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getServletPath().contains("/admin/reports") ? "active" : "" %>"
               href="<%= request.getContextPath() %>/admin/reports">
                <i class="bi bi-bar-chart me-2"></i>Reports
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getServletPath().contains("/admin/policy") ? "active" : "" %>"
               href="<%= request.getContextPath() %>/admin/policy">
                <i class="bi bi-shield-check me-2"></i>Policy Settings
            </a>
        </li>
    </ul>
    <div class="border-top border-white border-opacity-25 pt-3 mt-2">
        <div class="text-white-50 small ps-2 mb-2"><i class="bi bi-person-circle me-1"></i><%= user.getFullName() %></div>
        <a class="nav-link text-white-50" href="<%= request.getContextPath() %>/auth/logout">
            <i class="bi bi-box-arrow-left me-2"></i>Logout
        </a>
    </div>
</div>
