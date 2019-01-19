let SINGLE_STAR_URL = 'single_star.html';
let SINGLE_MOVIE_URL = 'single_movie.html';

function getStar(stars, num) {
    let id = stars[num]['star_id'];
    let name = stars[num]['star_name'];

    return $('<a href = ' + SINGLE_STAR_URL + '?id=' + id + ' >'
        + name + '</a>')
}

function getMovie(data, num) {
    return $('<a href = ' + SINGLE_MOVIE_URL + '?id=' + data[num]['id'] + ' >'
        + data[num]['title'] + '</a>')
}

function getTableRow(data, row_num) {
    var stars = $('<div></div>');
    var stars_list = data[row_num]['star_list'];
    for (var i = 0; i < stars_list.length; i++) {
        if (i !== 0) {stars.append(', ')}
        stars.append(getStar(stars_list, i));
    }
    var row = $('<tr></tr>');
    row.append($('<td>' + (row_num + 1) + '</td>'))
        .append($('<td></td>').append(getMovie(data, row_num)))
        .append($('<td>' + data[row_num]['year'] + '</td>'))
        .append($('<td>' + data[row_num]['director'] + '</td>'))
        .append($('<td>' + data[row_num]['genre_list'] + '</td>'))
        .append($('<td></td>').append(stars))
        .append($('<td>' + data[row_num]['rating'] + '</td>'));
    return row;
}

/**
    @param resultData jsonObject
*/
function handleMovieListResult(resultData){
    $(function () {
        console.log(resultData);

        var table_body = $('#movie-list > tbody');
        for (var i = 0; i < resultData.length; i++){
            table_body.append(getTableRow(resultData, i))
        }

        $('#load_sign').hide();
        $('#progress_holder').hide();
        $('#movie-list').show();
    })
}

$(function () {
    $('#movie-list').hide();
    $('.progress-bar').animate({
        width: '95%'
    },
        300);

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/movie_list",
        success: handleMovieListResult
        // error:
    });
});

