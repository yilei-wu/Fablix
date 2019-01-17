function getTableRow(data, row_num) {
    var row = $('<tr></tr>');
    row.append($('<td>' + (row_num + 1) + '</td>'))
        .append($('<td>name...</td>'))
        .append($('<td>year...</td>'))
        .append($('<td>director...</td>'))
        .append($('<td>genres...</td>'))
        .append($('<td>stars...</td>'))
        .append($('<td>rating...</td>'));
    return row;
}

/**
    @param resultData jsonObject
*/
function handleMovieListResult(resultData){
    $(function () {
        var table_body = $('#movie-list > tbody');
        for (var i = 0; i < 20; i++){
            // var row = $('<tr><td>' + i + '</td></tr>');
            table_body.append(getTableRow(null, i))


        }
    })
}

handleMovieListResult(null);

// var table_body = $('#movie-list');
// console.log(table_body);
// var row = $('<tr>10000</tr>');
// table_body.append(row);

// $.ajax({
//     dataType: "json",
//     method: "GET",
//     url: "api/stars",
//     success: handleMovieListResult
//     // error:
// });