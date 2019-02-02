var page_num = 1;

function getTableRow(data, row_num) {
    var stars = getStars(data, row_num);
    var row = $('<tr></tr>');
    row.append($('<td>' + (row_num + 1) + '</td>'))
        .append($('<td></td>').append(getMovie(data, row_num)))
        .append($('<td>' + data[row_num]['year'] + '</td>'))
        .append($('<td>' + data[row_num]['director'] + '</td>'))
        .append($('<td>' + data[row_num]['genre_list'] + '</td>'))
        .append($('<td></td>').append(stars))
        .append($('<td>' + data[row_num]['rating'] + '</td>'))
        .append($('<td><button class="btn btn-primary"><i class="fas fa-cart-plus"></i></button> </td>'));
    return row;
}

function changePage() {
    if (page_num === null) {
        page_num = 1;
    }
    $('#page_container').pagination(page_num);
}

window.onbeforeunload = function () {
    sessionStorage.setItem('page', $('#page_container').pagination('getSelectedPageNum'));
};

/**
 @param resultData jsonObject
 * @param pagination
 */
function handleMovieListResult(resultData, pagination){
    $(function () {
        console.log(resultData);

        var table_body = $('#movie-list > tbody');
        table_body.empty();
        for (var i = 0; i < resultData.length; i++){
            table_body.append(getTableRow(resultData, i))
        }

        // if (pagination.pageNumber !== page_num) {
        //     changePage();
        // }

        // sessionStorage.setItem('page', pagination.pageNumber);

        $('#load_sign').hide();
        $('#progress_holder').hide();
        $('#movie-list').show();
    })
}

function setPage(){
    $(function () {
        sessionStorage.setItem('page', $('#page_container').pagination('getSelectedPageNum'));
    });
}

$(function () {

    // $( window ).unload(function() {
    //     return "Handler for .unload() called.";
    // });

    $('#movie-list').hide();
    $('.progress-bar').animate({
        width: '95%'
    },
        300);

    var title, year, director, star, sort;
    // var page_num;

    let from = getParameterByName('from');
    if (from === 'search') {
        title = getParameterByName('title');
        year = getParameterByName('year');
        director = getParameterByName('director');
        star = getParameterByName('star');
        sort = getParameterByName('sort');

        sessionStorage.setItem('title', title);
        sessionStorage.setItem('year', year);
        sessionStorage.setItem('director', director);
        sessionStorage.setItem('star', star);
        sessionStorage.setItem('sort', sort);
    } else if (from === 'back') {
        title = sessionStorage.getItem('title');
        year = sessionStorage.getItem('year');
        director = sessionStorage.getItem('director');
        star = sessionStorage.getItem('star');
        sort = sessionStorage.getItem('sort');

        page_num = sessionStorage.getItem('page');
    }
    console.log(page_num);




    $('#page_container').pagination({
        dataSource: 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
                '&star=' + star + '&sort=' + sort,
        // locator: function () {
        //     return 'movielist'
        // },
        locator: 'movielist',
        totalNumberLocator: function (response) {
            // console.log(response['total_page']);
            return response['total_number'];
            // return 2000
        },
        showGoInput: true,
        showGoButton: true,
        pageSize: 22,
        pageNumber: page_num,
        className: 'paginationjs-theme-blue',
        afterRender: function (){
            // console.log(this);
            // sessionStorage.setItem('page', $('#page_container').pagination('getSelectedPageNum'));
            // changePage()
        },
        // ajax: {
        //     beforeSend: function() {
        //         dataContainer.html('Loading data from flickr.com ...');
        //     }
        // },
        callback: function(data, pagination) {
            // template method of yourself
            handleMovieListResult(data, pagination)
        },
        alias: {
            pageNumber: 'page',
            pageSize: 'records'
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

