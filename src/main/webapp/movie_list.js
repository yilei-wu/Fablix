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


    let title = getParameterByName('title');
    let year = getParameterByName('year');
    let director = getParameterByName('director');
    let star = getParameterByName('star');
    // let page = getParameterByName('page');
    let sort = getParameterByName('sort');
    // let records = getParameterByName('records');


    $('#movie-list').pagination({
        dataSource: 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
                '&star=' + star + '&sort=' + sort,
        // locator: 'items',
        // totalNumberLocator: function(response) {
        //     // you can return totalNumber by analyzing response content
        //     return Math.floor(Math.random() * (1000 - 100)) + 100;
        // },
        // formatResult: function(data) {
        //     var result = [];
        //     for (var i = 0, len = data.length; i < len; i++) {
        //         result.push(data[i] + ' - good guys');
        //     }
        //     return result;
        // },
        pageSize: 20,
        className: 'paginationjs-theme-blue',
        // ajax: {
        //     beforeSend: function() {
        //         dataContainer.html('Loading data from flickr.com ...');
        //     }
        // },
        alias: {
            pageNumber: 'page',
            pageSize: 'records'
        },
        callback: function(data, pagination) {
            // template method of yourself
            console.log(data);
            $('#data_container').html(data);

            $('#load_sign').hide();
            $('#progress_holder').hide();
            $('#movie-list').show();
        }
    });





    // $.ajax({
    //     dataType: "json",
    //     method: "GET",
    //     url: 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
    //     '&star=' + star + '&page=' + page + '&sort=' + sort + '&records=' + records,
    //     success: handleMovieListResult
    //     // error:
    // });
});

