/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleMovieListResult(resultData){

    $(function () {
        console.log(resultData);

        $('#title').html(resultData['title'])
            .css({
                'font-size': (65 - resultData['title'].length) + 'px'
            });
        $('#year').html(resultData['year']);
        $('#director').html('<sup style="font-size: 15px">Directed by </sup>' + resultData['director']);
        $('#rating').html(resultData['rating']);
        $('#movie_id').html(resultData['id']);
        $('#genre').html(resultData['genre_list']);
        $('#stars').html(getStars(resultData));
    })
}

var printError = function(req, status, err) {
    console.log('Error: ', status, err);
};

$(function () {
    // $('#movie-list').hide();
    // $('.progress-bar').animate({
    //         width: '95%'
    //     },
    //     300);
    // var title = $('#movie_title');
    // console.log(title);
    // title.html('hello');

    let movie_id = getParameterByName('id');

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/single_movie?id=" + movie_id,
        success: handleMovieListResult,
        error: printError
    });
});