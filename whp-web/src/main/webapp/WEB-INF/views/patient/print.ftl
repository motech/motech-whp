<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>Treatment Card</title>
    <#include "../layout/scripts.ftl"/>
</head>
<body>
    <div id="navibar" class="navbar-fixed-top">
        <a href="<@spring.url '/' />">
            <img  class="pull-right" src="<@spring.url '/resources-${applicationVersion}/images/whplogo.png'/>"/>
        </a>
    </div>

<div class="container">
    <div class="row-fluid" id="mainContent">

    <div id="treatmentCard"></div>
    </div>
</div>
</body>
    <script type="text/javascript">
        $('#treatmentCard').load('/whp/treatmentcard/show?patientId=${patient.patientId}', function() {
            //setUpTreatmentCardTable();

            window.print();
        });
    </script>

</html>

