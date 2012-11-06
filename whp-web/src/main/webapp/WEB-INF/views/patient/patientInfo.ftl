<div class="default-arrow well" >


        <div class="row-fluid">
            <div class="span4">
                <h3>Treatment Details</h3>
                <table class="table table-bordered table-striped">

                    <tbody>
                    <tr>
                        <th>TB ID</th>
                        <td>${patient.tbId!}</td>
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
                        <td>${patient.diseaseClass!}</td>
                    </tr>

                    <tr>
                        <th>Type of patient</th>
                        <td>${patient.patientType!}</td>
                    </tr>

                    <tr>
                        <th>Treatment Category</th>
                        <td>${patient.treatmentCategoryName!}</td>
                    </tr>

                    <tr>
                        <th>Treatment Start Date</th>
                        <td>${patient.therapyStartDate!}</td>
                    </tr>

                    <tr>
                        <th>Current Phase</th>
                        <td id="patientCurrentPhase"><#if patient.currentPhase??>${patient.currentPhase.name.toString()}
                            (${patient.currentPhase.name.name()})</#if></td>
                    </tr>
                    <tr>
                        <th>Longest Interruption</th>
                        <td id="longestTreatmentInterruption">${patient.longestDoseInterruption} (in weeks)</td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div class="span4">
                <h3>Patient Details</h3>
                <table class="table table-bordered table-striped">

                    <tbody>
                    <tr>
                        <th>Patient ID</th>
                        <td>${patient.patientId!}</td>
                    </tr>

                    <tr>
                        <th>Name</th>
                        <td>${patient.firstName!} ${patient.lastName!}</td>
                    </tr>

                    <tr>
                        <th>Gender</th>
                        <td>${patient.gender!}</td>
                    </tr>

                    <tr>
                        <th>Age</th>
                        <td>${patient.age!}</td>
                    </tr>

                    <tr>
                        <th>Address</th>
                        <td>${patient.address!}</td>
                    </tr>

                    <tr>
                        <th>Village</th>
                        <td>${patient.addressVillage!}</td>
                    </tr>

                    <tr>
                        <th>District</th>
                        <td>${patient.addressDistrict!}</td>
                    </tr>

                    <tr>
                        <th>State</th>
                        <td>${patient.addressState!}</td>
                    </tr>

                    <tr>
                        <th>Mobile Number</th>
                        <td>${patient.phoneNumber!}</td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div class="span4">
                <h3>Provider Details</h3>
                <table class="table table-bordered table-striped">

                    <tbody>
                        <tr>
                            <th>Provider ID</th>
                            <td>${patient.providerId!}</td>
                         </tr>
                        <tr>
                            <th>Mobile Number</th>
                            <td>${patient.providerMobileNumber!}</td>
                        </tr>
                        <tr>
                            <th>Provider District</th>
                            <td>${patient.providerDistrict!}</td>
                        </tr>
                    </tbody>
                </table>


            </div>

        </div>
        <div class="row-fluid">
            <div class="span12">
                <h3>Smear Test Results</h3>
                <table class="table table-bordered text-center table-striped">
                    <thead>
                    <tr>
                        <th id="sampleInstance" rowspan="2">Sample Instance</th>
                        <th id="labName" rowspan="2">DMC</th>
                        <th id="labNumber" rowspan="2">Lab No.</th>
                        <th id="smearTestDetails" colspan="4">Smear Test Results</th>
                        <th id="weight" rowspan="2">Weight</th>
                    </tr>

                    <tr>
                        <th>Date 1</th>
                        <th>Result 1</th>
                        <th>Date 2</th>
                        <th>Result 2</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list patient.testResults as testResult>
                    <tr>
                        <td>${testResult.sampleInstance}</td>
                        <td>${testResult.labName}</td>
                        <td>${testResult.labNumber}</td>
                        <td>${testResult.smearTestDate1}</td>
                        <td>${testResult.smearTestResult1}</td>
                        <td>${testResult.smearTestDate2}</td>
                        <td>${testResult.smearTestResult2}</td>
                        <td>${testResult.weight}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

            </div>
         </div>







</div>