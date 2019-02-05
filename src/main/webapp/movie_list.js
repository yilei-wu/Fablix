var page_num = 1;
var title, year, director, star, sort, keyword, genre, first_title, size = 20;

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
        .append($('<td><button class="btn btn-primary"><i class="fas fa-cart-plus"></i></button></td>'));
    row.find('button').attr('onclick', 'addMovieToCart("m_' + data[row_num ]['movie_id'] + '");alert("succeed")');
    return row;
}

function showLoading() {
    $('#loading').show();
    $('.progress-bar').css({
        width: '0'
    }).animate({
            width: '95%'
        },
        300);
}

function hideLoading() {
    $('#loading').hide();
    $('#movie-list').show();
}

function updateInfo() {
    var source;
    let from = sessionStorage.getItem('method');
    if (from === 'a_search') {
        source = 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
            '&star=' + star + '&sort=' + sort;
    } else if (from === 'k_search') {
        source = 'api/keyword_search?keyword=' + keyword + '&sort=' + sort
    } else if (from === 'g_search') {
        source = 'api/genre_browse?genre=' + genre + '&sort=' + sort
    } else if (from === 't_search') {
        source = 'api/title_browse?title=' + first_title + '&sort=' + sort
    } else {
        console.error('not found ' + from)
    }

    size = sessionStorage.getItem('size');

    $('#page_container').pagination({
        dataSource: source,
        locator: 'movielist',
        totalNumberLocator: function (response) {
            return response['total_number'];
        },
        ajax: {
            beforeSend: showLoading
        },
        showGoInput: true,
        showGoButton: true,
        pageSize: size,
        className: 'paginationjs-theme-blue',
        callback: function(data, pagination) {
            handleMovieListResult(data, pagination)
        },
        alias: {
            pageNumber: 'page',
            pageSize: 'records'
        }
    });
}

function updateItemsPerPage() {
    let new_ipp = $('#ipp').val();
    sessionStorage.setItem('size', new_ipp | 0);
    updateInfo()
}

function resort(key) {
    let currentSort = sessionStorage.getItem('sort');
    if (key === 't') {
        if (currentSort === 'ta') {
            sessionStorage.setItem('sort', 'td');
            sort = 'td';
        } else {
            sessionStorage.setItem('sort', 'ta');
            sort = 'ta';
        }
    } else if (key === 'r') {
        if (currentSort === 'ra') {
            sessionStorage.setItem('sort', 'rd');
            sort = 'rd';
        } else {
            sessionStorage.setItem('sort', 'ra');
            sort = 'ra';
        }
    }

    updateInfo();
    // if (currentSort === 'ta') {
    //     sessionStorage.setItem('sort', 'td');
    // } else if (currentSort === 'td') {
    //     sessionStorage.setItem('sort', 'ta');
    // } else if (currentSort === 'ra') {
    //     sessionStorage.setItem('sort', 'rd');
    // } else if (currentSort === 'rd') {
    //     sessionStorage.setItem('sort', 'ra');
    // }

    // $('#page_container').pagination({
    //     dataSource: 'api/search_movie?title=' + title + '&year=' + year + '&director=' + director +
    //         '&star=' + star + '&sort=' + sort,
    // })
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

        if (pagination.pageNumber !== page_num && sessionStorage.getItem('return') === 'true') {
            changePage();
            sessionStorage.setItem('return', 'false');
        }

        window.scrollTo(0, 0);
        // sessionStorage.setItem('page', pagination.pageNumber);

        // $('#load_sign').hide();
        // $('#progress_holder').hide();
        // $('#movie-list').show();
        hideLoading();
    })
}

function setPage(){
    $(function () {
        sessionStorage.setItem('page', $('#page_container').pagination('getSelectedPageNum'));
    });
}

$(function () {
    if (sessionStorage.getItem('size') === null) {
        sessionStorage.setItem('size', '20');
    }


    let from = getParameterByName('from');
    if (from === 'a_search') {
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
        sessionStorage.setItem('return', 'false');
        sessionStorage.setItem('method', 'a_search');
    } else if (from === 'k_search') {
        sort = getParameterByName('sort');
        keyword = getParameterByName('keyword');

        sessionStorage.setItem('sort', sort);
        sessionStorage.setItem('keyword', keyword);
        sessionStorage.setItem('return', 'false');
        sessionStorage.setItem('method', 'k_search');
    } else if (from === 'g_search') {
        sort = getParameterByName('sort');
        genre = getParameterByName('genre');

        sessionStorage.setItem('sort', sort);
        sessionStorage.setItem('genre', genre);
        sessionStorage.setItem('return', 'false');
        sessionStorage.setItem('method', 'g_search');
    } else if (from === 't_search') {
        sort = getParameterByName('sort');
        first_title = getParameterByName('title');

        sessionStorage.setItem('sort', sort);
        sessionStorage.setItem('f_title', first_title);
        sessionStorage.setItem('return', 'false');
        sessionStorage.setItem('method', 't_search');
    } else if (from === 'back') {
        if (sessionStorage.getItem('method') === 'k_search') {
            keyword = sessionStorage.getItem('keyword');
        } else if (sessionStorage.getItem('method') === 'a_search') {
            title = sessionStorage.getItem('title');
            year = sessionStorage.getItem('year');
            director = sessionStorage.getItem('director');
            star = sessionStorage.getItem('star');
        } else if (sessionStorage.getItem('method') === 'g_search') {
            genre = sessionStorage.getItem('genre');
        } else if (sessionStorage.getItem('method') === 't_search') {
            first_title = sessionStorage.getItem('f_title');
        }
        sort = sessionStorage.getItem('sort');
        sessionStorage.setItem('return', 'true');

        page_num = sessionStorage.getItem('page');
    }
    // else if (from === 'resort') {
    //     title = sessionStorage.getItem('title');
    //     year = sessionStorage.getItem('year');
    //     director = sessionStorage.getItem('director');
    //     star = sessionStorage.getItem('star');
    //     sort = getParameterByName('sort');
    //     sessionStorage.setItem('sort', sort);
    //     sessionStorage.setItem('return', 'false');
    //     page_num = 1;
    // }
    console.log(page_num);

    updateInfo();





});

