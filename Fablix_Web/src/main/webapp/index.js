
var genres = [];

function updateGenres(response) {
    let g_container = $('#g_container');
    let t_head = MOVIE_LIST_URL + '?from=t_search&';
    let g_head = MOVIE_LIST_URL + '?from=g_search&';
    for (var j = 0; j < response.length; j++) {
        let element = $('<a>' + response[j] + '</a>');
        element.attr('href', g_head + 'genre=' + response[j] + '&sort=ta');
        g_container.append(element);

        g_container.append(' | ');
        if (j % 5 === 0) {
            g_container.append('&#13;&#10;')
        }
    }

    for (var i = 65; i <= 90; i++) {
        let element = $('<a>' + String.fromCharCode(i).toUpperCase() + '</a>');
        element.attr('href', t_head + 'title=' + String.fromCharCode(i).toUpperCase() + '&sort=ta');
        g_container.append(element);
        g_container.append(' | ');
        if ((i - 64) % 5 === 0) {
            g_container.append('&#13;&#10;')
        }
    }
}

function submitKeywordSearch(formSubmitEvent){
    console.log("submit keyword form  " + formSubmitEvent);
    formSubmitEvent.preventDefault();

    console.log($('#keyword_form').serialize());
    window.location.replace(MOVIE_LIST_URL + '?' + $('#keyword_form').serialize());
}

function submitAccurateSearch(formSubmitEvent){
    console.log("submit accurate form  " + formSubmitEvent);
    formSubmitEvent.preventDefault();

    console.log($('#accurate_form').serialize());
    window.location.replace(MOVIE_LIST_URL + '?' + $('#accurate_form').serialize());
}

$(function () {
    $('#keyword_form').submit(submitKeywordSearch);
    $('#accurate_form').submit(submitAccurateSearch);

    $('#search_box').autocomplete({
        serviceUrl: 'api/auto_complete',
        ajaxSettings:{
            beforeSend: function () {
                console.log('Sending Ajax Request')
            }
        },
        lookupLimit: 10,
        minChars: 3,
        deferRequestBy: 300,
        showNoSuggestionNotice: true,
        noSuggestionNotice: 'No Suggestion',
        autoSelectFirst: false,
        triggerSelectOnValidInput: false,
        onSearchComplete: function (query, suggestions) {
            console.log('Auto Complete initiated');
            var movies = '';
            suggestions.forEach(function (element) {
                movies += element['value'] + '    '
            });
            console.log('Suggestions: ' + movies)
        },
        onSelect: function (suggestion) {
            location.href = 'single_movie.html?from=index&id=' + suggestion['data']
            // alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
        }
    });

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/all_genres",
        success: updateGenres,
        error: printError
    });



});