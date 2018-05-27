/*
 *   Funciones JavaScript relacionadas con la parte STREAMING de la aplicación
 */

function streamingClicked() {
    $("#lblTitle").text("Tweets en streaming")
    $("#dashboardBlock").hide();
    $("#q").val("");
    $("#q").prop('disabled', false);
    $("#q").attr('placeholder', 'Press Enter to start a new search');
    $("#streamingTweets").addClass("active");
    $("#databaseTweets").removeClass("active");
    $("#dashboard").removeClass("active");
    $('#divPagination').hide();
    menu = 1;
    unsubscribeIfNeeded();
}

function startSubscription(target, query) {
    unsubscribeIfNeeded();

    stompClient.send("/app/" + target, {}, query);
    subscription = stompClient.subscribe('/queue/search/' + query, showEncrpytedTweet);
}

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
