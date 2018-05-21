var subscription = null;

function showTweet(generatedTweetDto) {
    var data = JSON.parse(generatedTweetDto.body)
    Mustache.parse(template);   // optional, speeds up future uses
    var rendered = Mustache.render(template,{tweets: data});
    $('#resultsBlock').html(rendered);
}

function registerTemplate() {
    template = $("#template").html();
    Mustache.parse(template);
}

function registerSearch() {
    $("#search").submit(function(event){
        event.preventDefault();
        var target = $(this).attr('action');
        var query = $("#q").val();

        if (subscription != null) {
            $('#resultsBlock').empty();
            subscription.unsubscribe();
        }

        stompClient.send("/app/" + target, {}, query);
        subscription = stompClient.subscribe('/queue/search/' + query, showTweet)

        // $.get(target, { q: query } )
        //     .done( function(data) {
        //         $("#resultsBlock").empty().append(data);
        //         var template = $('#template').html();
        //         Mustache.parse(template);   // optional, speeds up future uses
        //         var rendered = Mustache.render(template,{tweets: data});
        //         $('#resultsBlock').html(rendered);
        //     }).fail(function() {
        //     $("#resultsBlock").empty();
        // });
    });
}


function connect() {
        var socket = new SockJS('/twitter');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
            });
    }

$(document).ready(function() {
    registerTemplate();
    connect();
    registerSearch();
});

