function submitKeywordSearch(formSubmitEvent){
    console.log("submit keyword form  " + formSubmitEvent);
    formSubmitEvent.preventDefault();

    console.log($('#keyword_form').serialize())
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
});