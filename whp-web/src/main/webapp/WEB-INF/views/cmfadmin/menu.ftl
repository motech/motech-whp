<ul class="nav nav-pills pull-left">
    <li>
        <a id="show-patients" href="/whp/patients/list#?patient_listing-searchCriteria={}&patient_listing-sortCriteria={&quot;AdherenceMissingAlertValue&quot;:&quot;DESC&quot;}"><i class="icon-user icon-white"></i> Patients</a>
    </li>

    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-check icon-white"></i> Container Registration <b class="caret"></b></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu">
            <li>
                <a id="register-container1" href="/whp/containerRegistration/by_cmfAdmin/new-container">Register a new container</a>
            </li>
            <li>
                <a id="register-container" href="/whp/containerRegistration/by_cmfAdmin/on-behalf-of-provider">Register on behalf of provider </a>
            </li>
        </ul>
    </li>

    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-th-large icon-white"></i> Container Tracking Dashboard <b class="caret"></b></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu">
            <li>
                <a href="/whp/sputum-tracking/pre-treatment/dashboard#?sputum_tracking_pagination-searchCriteria=%7B%22containerStatus%22:%22Open%22%7D&sputum_tracking_pagination-sortCriteria=%7B%22containerIssuedDate%22:%22DESC%22%7D">Pre Treatment Dashboard</a>
            </li>
            <li>
                <a href="/whp/sputum-tracking/in-treatment/dashboard#?sputum_tracking_pagination-searchCriteria=%7B%22containerStatus%22:%22Open%22%7D&sputum_tracking_pagination-sortCriteria=%7B%22containerIssuedDate%22:%22DESC%22%7D">In Treatment Dashboard</a>
            </li>
        </ul>
    </li>
</ul>

