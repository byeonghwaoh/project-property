var showSuccessFlag = function(message) {
    require(['aui/flag'], function(flag) {
        flag({
            type: 'success',
            title: 'Property Plugin',
            close: 'auto',
            body: message
        });
    });
};
var showErrorFlag = function(message) {
    require(['aui/flag'], function(flag) {
        flag({
            type: 'error',
            title: 'Property Plugin',
            close: 'auto',
            body: message
        });
    });
};



AJS.toInit(function(){
    console.log('Property Plugin initializing ...');
    console.log('AJS.params.baseURL=' + AJS.params.baseURL);
    var projectId = AJS.$('#projectId').val();
    var allUrl = AJS.params.baseURL + "/rest/ppmgmt/1.0/property/"  + projectId + "/getAll/";
    var selfUrl = AJS.params.baseURL + "/rest/ppmgmt/1.0/property/" + projectId + "/self";
    console.log('allUrl=' + allUrl);
    console.log('selfUrl=' + selfUrl);

    AJS.TableProperty = {};
    AJS.TableProperty.table = new AJS.RestfulTable({
            el: jQuery("#project-property-table"),
            autoFocus: true,
            resources: {
                all: allUrl,
                self: selfUrl
            },
            columns: [
                {
                    id: "pKey",
                    header: "Property Key"
                },
                {
                    id: "pValue",
                    header: "Property Value"
                }
            ]
    });

    console.log('Table binding ...');
    AJS.$(document).bind(AJS.RestfulTable.Events.ROW_INITIALIZED, function () {
            AJS.TableProperty.table.$tbody.empty();
            AJS.TableProperty.table.fetchInitialResources();
    });

    AJS.$(document).bind(AJS.RestfulTable.Events.ROW_ADDED, function () {
            showSuccessFlag("New row is added ...");
            AJS.TableProperty.table.$tbody.empty();
            AJS.TableProperty.table.fetchInitialResources();
    });

    AJS.$(document).bind(AJS.RestfulTable.Events.ROW_REMOVED, function () {
            showSuccessFlag("The row is deleted ...");
            AJS.TableProperty.table.$tbody.empty();
            AJS.TableProperty.table.fetchInitialResources();
    });

    AJS.$(document).bind(AJS.RestfulTable.Events.UPDATED, function () {
            showSuccessFlag("The row is updated ...");
            AJS.TableProperty.table.$tbody.empty();
            AJS.TableProperty.table.fetchInitialResources();
    });

    AJS.$(document).bind(AJS.RestfulTable.Events.SERVER_ERROR, function () {
            showErrorFlag("There is an Server Error !");
            AJS.TableProperty.table.$tbody.empty();
            AJS.TableProperty.table.fetchInitialResources();
    });
});