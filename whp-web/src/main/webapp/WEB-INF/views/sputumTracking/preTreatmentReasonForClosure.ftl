<#ftl encoding="ISO-8859-5">
<form class="modal hide fade" id="setReason" action="">

    <input id="containerId" type="hidden" name="containerId" value=""/>

    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>
            Reason For Closure [<span id="containerIdDisplay" class="containerIdDisplay" name="containerIdDisplay"></span>]
        </h3>
    </div>
    <div id="container-tracking-error" class="row alert alert-error"></div>
    <div class="modal-body">
        <div class="control-group">
            <table id="reasonUpdateTable" class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td>
                        <div class="control-group">
                            <div class="controls">
                                <label class="control-label">Reason*</label>
                                <select id="reason" name="reason" validate="required:true">
                                    <option value=""></option>
                                <#list reasons as reason>
                                    <option value="${reason.code}">
                                        ${reason.name}
                                    </option>
                                </#list>
                                </select>

                                <div id="tbNegativeControls">
                                    <label class="control-label alternate-diagnosis">Alternate Diagnosis*</label>
                                    <select id="alternateDiagnosis" name="alternateDiagnosis" validate="required:true">
                                        <option value=""></option>
                                    <#list alternateDiagnosisList as diagnosis>
                                        <option value="${diagnosis.code}">${diagnosis.name}</option>
                                    </#list>
                                    </select>
                                    <label class="control-label consultationDate">Consultation Date*</label>

                                    <div class="input-prepend input-append">
                                        <span class="add-on show-date-button"><i class="icon-calendar"></i></span>
                                        <input class="" placeholder="dd/mm/yyyy" data-date-format="dd/mm/yyyy" id="consultationDate"
                                               name="consultationDate" type="text" readonly="readonly"/>
                                        <span class="add-on  date-dismiss"><i class="icon-remove-sign"></i></span>
                                    </div>

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
        <button class="btn" data-dismiss="modal" id="close">Cancel</button>
        <button type="submit" class="btn  btn-primary" id="saveReason">Save</button>
    </div>
</form>