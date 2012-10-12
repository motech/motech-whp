<form class="modal hide fade" id="setReason" method="POST"
      action="<@spring.url '/sputum-tracking/updateReasonForClosure'/>"
      xmlns="http://www.w3.org/1999/html">
    <input id="containerId" type="hidden" name="containerId" value=""/>

    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Reason For Closure</h3>
    </div>
    <div class="modal-body">
        <div class="control-group">
            <table id="reasonUpdateTable" class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td>
                        <div class="control-group">
                            <label class="control-label">Reason*</label>

                            <div class="controls">
                                <select id="reason" name="selectedReason">
                                    <option value=""></option>
                                <#list reasons as reason>
                                    <option value="${reason.code}">${reason.name}</option>
                                </#list>
                                </select>
                                <div id="tbNegativeControls">
                                    <select id="alternateDiagnosis" name="selectedAlternateDiagnosis">
                                        <option value=""></option>
                                    <#list alternateDiagnosisList as alternateDiagnosis>
                                        <option value="${alternateDiagnosis.code}">${alternateDiagnosis.name}</option>
                                    </#list>
                                    </select>
                                    <input class="span 2" data-date-format="dd/mm/yyyy" id="consultationDatePicker" name="consultationDate" type="text" />
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal" id="close">Close</button>
        <button type="submit" class="btn btn-group btn-primary" id="saveReason">Save</button>
    </div>
</form>