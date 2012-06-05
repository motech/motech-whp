<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Patient Dashboard">
    <#if messages?exists && (messages?size>0)>
        <div class="dateUpdated-message-alert row alert alert-info fade in">
            <button class="close" data-dismiss="alert">&times;</button>
            <#list messages as message>
                ${message}
                <#break/>
            </#list>
            <table id="messageList" class="table table-bordered table-condensed">
                <thead>
                <tr>
                    <th>Phase Information</th>
                </tr>
                <tbody>
                    <#list messages as message>
                        <#if message_index != 0>
                            <tr>
                                <td>${message}</td>
                                <br/>
                            </tr>
                        </#if>
                        <#assign message=""/>
                    </#list>
                </tbody>
            </table>
        </div>
    </#if>

    <div class="well">
        <a id="setDateLink" data-toggle="modal" href="#setDatesModal" class="brand pull-left">Adjust Phase Start Dates</a>
    </div>
    <form class="modal hide fade" id="setDatesModal" method="POST" action="<@spring.url '/patients/dashboard'/>">
        <input type="hidden" id="patientId" name="patientId" value="${patientId}"/>
        <div class="modal-header">
            <button class="close" data-dismiss="modal">x</button>
            <h3>Adjust Start Dates</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <div>
                    <table id="patientList" class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>Phase</th>
                            <th>Start Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Intensive Phase</td>
                            <td>
                                <input class="span 2" data-date-format="dd/mm/yyyy" id="ipDatePicker" name="ipStartDate" type="text" value="${patient.ipStartDate}">
                            </td>
                        </tr>
                        <tr>
                            <td>Extended Intensive Phase</td>
                            <td>
                                <input class="span 2" data-date-format="dd/mm/yyyy" id="eipDatePicker" name="eipStartDate" type="text" value="${patient.eipStartDate}">
                            </td>
                        </tr>
                        <tr>
                            <td>Continuation Phase</td>
                            <td>
                                <input class="span 2" data-date-format="dd/mm/yyyy" id="cpDatePicker" name="cpStartDate" type="text" value="${patient.cpStartDate}">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-group" data-dismiss="modal">Close</button>
            <button class="btn btn-group" id="clearFields" >Clear All</button>
            <button type="submit" class="btn btn-group btn-primary" id="saveTheDate">Save</button>
        </div>
    </form>
    <script type="text/javascript">
        $('#ipDatePicker').datepicker();
        $('#eipDatePicker').datepicker();
        $('#cpDatePicker').datepicker();
        $("#clearFields").click(function() {
            $('#ipDatePicker').val('');
            $('#eipDatePicker').val('');
            $('#cpDatePicker').val('');
            event.preventDefault();
        });
        createAutoClosingAlert(".dateUpdated-message-alert", 5000)
    </script>
</@layout.defaultLayout>
