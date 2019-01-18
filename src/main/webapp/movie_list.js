

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
        var table = $('#movie-list');
        var loadSign = $('#load_sign');

        console.log(resultData);
        var table_body = $('#movie-list > tbody');
        for (var i = 0; i < resultData.length; i++){
            // var row = $('<tr><td>' + i + '</td></tr>');
            table_body.append(getTableRow(resultData, i))
        }
        loadSign.hide();
        table.show();
    })
}

$(function () {
    var table = $('#movie-list');

    console.log(table);
    table.hide(10);

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/movie_list",
        success: handleMovieListResult
        // error:
    });
});

