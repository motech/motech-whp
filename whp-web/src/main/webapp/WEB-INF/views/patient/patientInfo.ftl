<div class="patient-info default-arrow" xmlns="http://www.w3.org/1999/html">
    <div id="row1">
        <div class="pull-left patient-info-left-pane">

            <table class="table table-bordered word-wrap table-striped">
                <tr>
                    <th id="treatmentDetails" colspan="2">Treatment Details</th>
                </tr>

                <tr>
                    <th>TB ID</th>
                    <td>${patient.tbId}</td>
                </tr>

                <tr>
                    <th>TB Registration Number</th>
                    <td>${patient.tbRegistrationNumber!}</td>
                </tr>

                <tr>
                    <th>PHI</th>
                    <td>${patient.phi!}</td>
                </tr>

                <tr>
                    <th>Disease Classification</th>
                    <td>${patient.diseaseClass}</td>
                </tr>

                <tr>
                    <th>Type of patient</th>
                    <td>${patient.patientType}</td>
                </tr>

                <tr>
                    <th>Treatment Category</th>
                    <td>${patient.treatmentCategoryName}</td>
                </tr>

                <tr>
                    <th>Treatment Start Date</th>
                    <td>${patient.therapyStartDate}</td>
                </tr>

                <tr>
                    <th>Current Phase</th>
                    <td id="patientCurrentPhase"><#if patient.currentPhase??>${patient.currentPhase.name.toString()}
                        (${patient.currentPhase.name.name()})</#if></td>
                </tr>
                <tr>
                    <th>Longest Interruption</th>
                    <td id="longestTreatmentInterruption">${patient.longestDoseInterruption}</td>
                </tr>
            </table>
            <table class="table table-bordered default-arrow word-wrap table-striped">
                <tr>
                    <th id="providerDetails" colspan="2">Provider Details</th>
                </tr>

                <tr>
                    <th>Provider ID</th>
                    <td>${patient.providerId}</td>
                </tr>

                <tr>
                    <th>Mobile Number</th>
                    <td>${patient.providerMobileNumber}</td>
                </tr>

            </table>
        </div>

        <div class="pull-right patient-info-right-pane">
            <div>
                <table class="table table-bordered default-arrow word-wrap table-striped">
                    <tr>
                        <th id="patientDetails" colspan="2">Patient Details</th>
                    </tr>

                    <tr>
                        <th>Patient ID</th>
                        <td>${patient.patientId}</td>
                    </tr>

                    <tr>
                        <th>Name</th>
                        <td>${patient.firstName} ${patient.lastName!}</td>
                    </tr>
                    <tr>
                        <th>State</th>
                        <td>${patient.addressState}</td>
                    </tr>

                    <tr>
                        <th>Gender</th>
                        <td>${patient.gender}</td>
                    </tr>

                    <tr>
                        <th>Age</th>
                        <td>${patient.age}</td>
                    </tr>

                    <tr>
                        <th>Address</th>
                        <td>${patient.address}</td>
                    </tr>

                    <tr>
                        <th>Mobile Number</th>
                        <td>${patient.phoneNumber}</td>
                    </tr>
                </table>
            </div>
            <table class="table table-bordered text-center table-striped">
                <tr>
                    <th id="sampleInstance" rowspan="2">Sample Instance</th>
                    <th id="smearTestDetails" colspan="4">Smear Test Results</th>
                    <th id="weight" rowspan="2">Weight</th>
                </tr>
                <tr>
                    <th>Date 1</th>
                    <th>Result 1</th>
                    <th>Date 2</th>
                    <th>Result 2</th>
                </tr>
            <#list patient.testResults as testResult>
                <tr>
                    <td>${testResult.sampleInstance}</td>
                    <td>${testResult.smearTestDate1}</td>
                    <td>${testResult.smearTestResult1}</td>
                    <td>${testResult.smearTestDate2}</td>
                    <td>${testResult.smearTestResult2}</td>
                    <td>${testResult.weight}</td>
                </tr>
            </#list>
            </table>
        </div>
    </div>
</div>