/*
 *   Funciones JavaScript relacionadas con la parte DASHBOARD de la aplicación
 */

function dashboardClicked() {
    $("#lblTitle").text("Monitorización de la sesión")
    $("#dashboardBlock").show();
    $("#q").val("");
    $("#q").prop('disabled', true);
    $("#q").attr('placeholder', '');
    $("#streamingEncryptTweets").removeClass("active");
    $("#streamingChangeTweets").removeClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").addClass("active");
    $('#divPagination').hide();
    menu = 3;
    unsubscribeIfNeeded();
    startDashboard();
}

function startDashboard() {
    // Dos peticiones AJAX para traer informacion del estado del sistema
    $.getJSON("/health", {}).always(updateHealthInfo);

    $.getJSON("/metrics", {}, updateMetricsInfo);

    // Mientras siga el dashboard seleccionado, se actualiza el estado cada 5 segundos
    if (menu === 3)
        setTimeout(startDashboard, 5000);
}

function updateHealthInfo(healthInfo) {
    var rabbitStatus;
    var mongoStatus;

    if (healthInfo.rabbit === undefined) {
        healthInfo = healthInfo.responseJSON;
    }

    if (healthInfo.rabbit !== undefined) {
        rabbitStatus = healthInfo.rabbit.status;
    }
    if (healthInfo.mongo !== undefined) {
        mongoStatus = healthInfo.mongo.status;
    }

    if (rabbitStatus === "UP") {
        $('#rabbitStatusOk').show();
        $('#rabbitStatusNoOk').hide();
    } else {
        $('#rabbitStatusOk').hide();
        $('#rabbitStatusNoOk').show();
    }

    if (mongoStatus === "UP") {
        $('#mongoStatusOk').show();
        $('#mongoStatusNoOk').hide();
    } else {
        $('#mongoStatusOk').hide();
        $('#mongoStatusNoOk').show();
    }
}

function updateMetricsInfo(metricsInfo) {
    var totalStreams = metricsInfo["counter.streams.total"];
    var currentStreams = metricsInfo["counter.streams.current"];
    var encryptedTweets = metricsInfo["counter.encryptedtweets.total"];
    var changedTweets = metricsInfo["counter.changedtweets.total"];

    if (totalStreams === undefined) totalStreams = 0;
    if (currentStreams === undefined) currentStreams = 0;
    if (encryptedTweets === undefined) encryptedTweets = 0;
    if (changedTweets === undefined) changedTweets = 0;

    $('#totalStreamings').text(totalStreams);
    $('#currentStreamings').text(currentStreams);
    $('#encryptedTweets').text(encryptedTweets);
    $('#changedTweets').text(changedTweets);
}