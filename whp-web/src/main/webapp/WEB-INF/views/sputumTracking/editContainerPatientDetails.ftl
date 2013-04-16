<#ftl encoding="ISO-8859-5">
<form class="modal hide fade" id="editPatientDetails" action="" xmlns="http://www.w3.org/1999/html">

    <input id="containerId" type="hidden" name="containerId" value=""/>

    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>
            Edit Patient Details [<span id="containerIdDisplay" class="containerIdDisplay" name="containerIdDisplay"></span>]
        </h3>
    </div>
    <div class="modal-body">
        <div class="control-group">
            <table id="patientUpdateTable" class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td>
                        <div class="control-group">
                            <div class="controls">
                                <label class="control-label">Given Patient Name</label>
                                <input id="patientName" name="patientName"/>
                                <label class="control-label">Given Patient Id</label>
                                <input id="patientId" name="patientId"/>
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
        <button type="submit" class="btn  btn-primary" id="savePatientDetails">Save</button>
    </div>
</form>