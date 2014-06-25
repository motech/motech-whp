<#import "/spring.ftl" as spring />
<ul class="nav nav-pills pull-left itadmin-menu">
    <li>
        <a id="list-providers" href="/whp/providers/list"><i class="icon-cog icon-white"></i> Manage Providers</a>
    <li>
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-tasks icon-white"></i> Diagnostics <b class="caret"></b></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu" id="diagnostics">
            <li>
               <a id="cmf-admins" href="/whp/diagnostics/show/all" target="blank">Motech WHP</a>
            </li>
            <li>
               <a id="cmf-admins" href="<@spring.message 'whp.reports.url'/>/diagnostics/show/all" target="blank">Motech WHP Reports</a>
            </li>
        </ul>
    </li>
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-download-alt icon-white"></i> Schedules <b class="caret"></b></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu" id="providerReminders">
            <li>
                <a href="/whp/schedule/PROVIDER_ADHERENCE_WINDOW_COMMENCED">Provider Adherence Window Commenced</a>
            </li>
            <li>
                <a href="/whp/schedule/PROVIDER_ADHERENCE_NOT_REPORTED">Provider Adherence Not Reported</a>
            </li>
            <li>
                <a href="/whp/schedule/PATIENT_IVR_ALERT">Patient IVR Alert</a>
            </li>
            <li>
                <a href="/whp/schedule/PATIENT_REMINDER_CATEGORY_GOVERNMENT">Patient Reminder Call(Government Category)</a>
            </li>
            <li>
                <a href="/whp/schedule/PATIENT_REMINDER_CATEGORY_COMMERCIAL">Patient Reminder Call(Commercial Category)</a>
            </li>
        </ul>
    </li>
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-download-alt icon-white"></i> Master Data <b class="caret"></b></a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu" id="masterData">
            <li>
                <a href="/whp/crud/ProviderContainerMapping/list" target="_blank">Provider Container Mapping</a>
            </li>
            <li>
                <a href="/whp/crud/District/list" target="_blank">District</a>
            </li>
        </ul>
    </li>
    <li>
        <a href="/whp/userManagement/list" target="_blank"><i class="icon-briefcase icon-white"></i> Manage Users</a>
    </li>
</ul>

