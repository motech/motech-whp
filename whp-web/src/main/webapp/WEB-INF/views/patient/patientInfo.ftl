<#import "/spring.ftl" as spring />
<div class="default-arrow well" xmlns="http://www.w3.org/1999/html">

    <div class="row-fluid">

        <p class="patient">

        <#if patient.flag>
            <#assign flagValue="false"/>
        <#else>
            <#assign flagValue="true"/>
        </#if>

            <img id="flag_star"
                 endpoint="<@spring.url '/patients/${patient.patientId}/updateFlag?value=${flagValue}'/>"
                 flagValue="${patient.flag?string}" class="flagImage"
                 src="<@spring.url '/resources-${applicationVersion}/img/${patient.flag?string}-star.png'/>"/>

            <span class="name"> ${patient.firstName!} ${patient.lastName!} </span>
            <span class="sub-details"> ( Age:  ${patient.age!}, ${patient.gender!} ) </span>
         <span class="patient-addr">
          <b>Address:</b>   ${patient.address!}, ${patient.addressVillage!}, ${patient.addressDistrict!}
             |  <b>Mob:</b> ${patient.phoneNumber!}
         </span>
        </p>

        <p class="provider-details"><b>Provider:</b> <span
                class="name">${patient.providerId!}</span>, ${patient.providerDistrict!} - District |
            <b>Mob:</b> ${patient.providerMobileNumber!}

        </p>

        <hr/>

        <div>
            <table>
            <#if '${patient.adherenceMissingSeverityColor}'!="">
                <tr>
                    <td>
                        <div class="label">${patient.adherenceMissingSeverityColor}</div>
                    </td>
                    <td>
                        <@spring.message '${patient.adherenceMissingMessageCode}' />
                    </td>
                </tr>
            </#if>

            <#if '${patient.cumulativeMissedDosesSeverityColor}'!="">
                <tr>
                    <td>
                        <div class="label">${patient.cumulativeMissedDosesSeverityColor!}</div>
                    </td>
                    <td>
                        <@spring.message '${patient.cumulativeMissedDosesMessageCode!}' />
                    </td>
                </tr>
            </#if>

            <#if '${patient.treatmentNotStartedSeverityColor}'!="">
                <tr>
                    <td>
                    ${patient.treatmentNotStartedSeverityColor!}
                    </td>
                    <td>
                        <@spring.message '${patient.treatmentNotStartedMessageCode!}'></@spring.message>
                    </td>
                </tr>
            </#if>
            </table>
        </div>
    </div>

    <hr/>

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
        <div class="span8">
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
    <div class="row-fluid">

    </div>


</div>