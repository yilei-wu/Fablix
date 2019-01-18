function getTableRow(data, row_num) {
    var row = $('<tr></tr>');
    row.append($('<td>' + (row_num + 1) + '</td>'))
        .append($('<td>' + data[row_num]['title'] + '</td>'))
        .append($('<td>' + data[row_num]['year'] + '</td>'))
        .append($('<td>' + data[row_num]['director'] + '</td>'))
        .append($('<td>' + data[row_num]['genre_list'] + '</td>'))
        .append($('<td>stars...</td>'))
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

