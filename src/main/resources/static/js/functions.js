var subscription = null;
var templatePlainTweets = null;
var templateEncryptedTweets = null;
var stompClient = null;
var menu = 1;
var currentPage = 0;
var totalPages = 1;

function showEncrpytedTweet(generatedTweetDto) {
    var data = JSON.parse(generatedTweetDto.body)
    var rendered = Mustache.render(templateEncryptedTweets, {tweets: data});
    $('#resultsBlock').append(rendered);
}

function unsubscribeIfNeeded() {
    if (subscription != null) {
        subscription.unsubscribe();
        subscription = null;
    }
    $('#resultsBlock').empty();
}

function startSubscription(target, query) {
    unsubscribeIfNeeded();

    stompClient.send("/app/" + target, {}, query);
    subscription = stompClient.subscribe('/queue/search/' + query, showEncrpytedTweet);
}

function listFromDatabase(text) {
    // Peticion AJAX para buscar Tweets
    $.getJSON("/searchedTweets/search/findByTextContaining?text=" + text + "&page=" + currentPage + "&size=8", {}, function(data) {
        totalPages = data.page.totalPages;
        $('#currentPage').text("Página " + (currentPage+1) + " de " + totalPages);
        var rendered = Mustache.render(templatePlainTweets, {tweets: data._embedded.searchedTweets});
        $('#resultsBlock').html(rendered);
    });
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

function startDashboard() {
    // Dos peticiones AJAX para traer informacion del estado del sistema
    $.getJSON("/health", {}).always(updateHealthInfo);

    $.getJSON("/metrics", {}, function(metricsInfo) {
        var totalStreams = metricsInfo["counter.streams.total"];
        var currentStreams = metricsInfo["counter.streams.current"];
        var encryptedTweets = metricsInfo["counter.encryptedtweets.total"];

        if (totalStreams === undefined) totalStreams = 0;
        if (currentStreams === undefined) currentStreams = 0;
        if (encryptedTweets === undefined) encryptedTweets = 0;

        $('#totalStreamings').text(totalStreams);
        $('#currentStreamings').text(currentStreams);
        $('#encryptedTweets').text(encryptedTweets);
    });

    // Mientras siga el dashboard seleccionado, se actualiza el estado cada 5 segundos
    if (menu === 3) setTimeout(startDashboard, 5000);
}



function registerEvents() {
    $("#search").submit(function(event){
        event.preventDefault();
        var target = $(this).attr('action');
        var q = $("#q").val();

        if (menu === 1) {
            // Streaming de Tweets
            startSubscription(target, q);
        }
        else if (menu === 2) {
            // Listar Tweets de base de datos
            currentPage = 0;
            $('#divPagination').show();
            listFromDatabase(q);
        }
    });

    $("#streamingTweets").click(function(event) {
        $("#lblTitle").text("Tweets en streaming")
        $("#dashboardBlock").hide();
        $("#q").val("");
        $("#q").prop('disabled', false);
        $("#streamingTweets").addClass("active");
        $("#databaseTweets").removeClass("active");
        $("#dashboard").removeClass("active");
        $('#divPagination').hide();
        menu = 1;
        unsubscribeIfNeeded();
    });

    $("#databaseTweets").click(function(event) {
        $("#lblTitle").text("Tweets en la base de datos")
        $("#dashboardBlock").hide();
        $("#q").val("");
        $("#q").prop('disabled', false);
        $("#streamingTweets").removeClass("active");
        $("#databaseTweets").addClass("active");
        $("#dashboard").removeClass("active");
        menu = 2;
        unsubscribeIfNeeded();
    });

    $("#dashboard").click(function(event) {
        $("#lblTitle").text("Monitorización de la sesión")
        $("#dashboardBlock").show();
        $("#q").val("");
        $("#q").prop('disabled', true);
        $("#streamingTweets").removeClass("active");
        $("#databaseTweets").removeClass("active");
        $("#dashboard").addClass("active");
        menu = 3;
        unsubscribeIfNeeded();
        startDashboard();
    });

    $("#previousPage").click(function(event) {
        if (currentPage > 0) {
            currentPage = currentPage - 1;
            $('#resultsBlock').empty();
            listFromDatabase($("#q").val())
        }
    });

    $("#nextPage").click(function(event) {
        if (totalPages > currentPage + 1) {
            currentPage = currentPage + 1;
            $('#resultsBlock').empty();
            listFromDatabase($("#q").val())
        }
    });

}

function registerTemplates() {
    templatePlainTweets = $("#templatePlainTweets").html();
    Mustache.parse(templatePlainTweets); // optional, speeds up future uses

    templateEncryptedTweets = $("#templateEncryptedTweets").html();
    Mustache.parse(templateEncryptedTweets); // optional, speeds up future uses
}

function connect() {
    var socket = new SockJS('/twitter');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

$(document).ready(function() {
    connect();
    registerTemplates();
    registerEvents();
});

