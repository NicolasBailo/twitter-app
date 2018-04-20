function registerSearch() {
    $("#search").submit(function(event){
        event.preventDefault();
        var target = $(this).attr('action');
        var query = $("#q").val();
        $.get(target, { q: query } )
            .done( function(data) {
                $("#resultsBlock").empty().append(data);
                var template = $('#template').html();
                Mustache.parse(template);   // optional, speeds up future uses
                var rendered = Mustache.render(template,{tweets: data});
                $('#resultsBlock').html(rendered);
            }).fail(function() {
            $("#resultsBlock").empty();
        });
    });
}

$(document).ready(function() {
    registerSearch();
});

