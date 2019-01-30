function getTableRow(data, row_num) {
    var stars = getStars(data, row_num);
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


    $('#movie-list').pagination({
        dataSource: [1, 2, 3, 4, 5, 6, 7],
        // locator: 'items',
        // totalNumberLocator: function(response) {
        //     // you can return totalNumber by analyzing response content
        //     return Math.floor(Math.random() * (1000 - 100)) + 100;
        // },
        pageSize: 3,
        // ajax: {
        //     beforeSend: function() {
        //         dataContainer.html('Loading data from flickr.com ...');
        //     }
        // },
        // callback: function(data, pagination) {
        //     // template method of yourself
        //     var html = template(data);
        //     dataContainer.html(html);
        // }
    })



    let title = getParameterByName('title');
    let year = getParameterByName('year');
    let director = getParameterByName('director');
    let star = getParameterByName('star');
    let page = getParameterByName('page');
    let sort = getParameterByName('sort');
    let records = getParameterByName('records');

    $.ajax({
        dataType: "json",
        method: "GET",
        url: 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
        '&star=' + star + '&page=' + page + '&sort=' + sort + '&records=' + records,
        success: handleMovieListResult
        // error:
    });
});

