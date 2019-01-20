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

function handleStarListResult(resultData){

    $(function () {
        console.log(resultData);

        $('#star_name').html(resultData['star_name'])
            .css({
                'font-size': (65 - resultData['star_name'].length) + 'px'
            });
        $('#birth_year').html(resultData['birth_year']);
        $('#movie_list').html(getMovie(resultData));
    })
}

var printError = function(req, status, err) {
    console.log('Error: ', status, err);
};

$(function () {

    let movie_id = getParameterByName('id');

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/single_star?id=" + movie_id,
        success: handleStarListResult,
        error: printError
    });
});