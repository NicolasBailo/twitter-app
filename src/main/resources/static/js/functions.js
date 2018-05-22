var subscription = null;
var templatePlainTweets = null;
var templateEncryptedTweets = null;
var stompClient = null;
var menu = 1;

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
    $.getJSON('/searchedTweets/search/findByTextContaining?text=' + text, {}, function(data) {
        var rendered = Mustache.render(templatePlainTweets, {tweets: data._embedded.searchedTweets});
        $('#resultsBlock').html(rendered);
    });
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
            listFromDatabase(q);
        }
        else if (menu === 3) {
            // Dashboard
            unsubscribeIfNeeded();

        }
    });

    $("#streamingTweets").click(function(event) {
        $("#lblTitle").text("Tweets en streaming")
        $("#q").val("");
        $("#streamingTweets").addClass("active");
        $("#databaseTweets").removeClass("active");
        menu = 1;
        unsubscribeIfNeeded();
    });

    $("#databaseTweets").click(function(event) {
        $("#lblTitle").text("Tweets en la base de datos")
        $("#q").val("");
        $("#streamingTweets").removeClass("active");
        $("#databaseTweets").addClass("active");
        menu = 2;
        unsubscribeIfNeeded();
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

