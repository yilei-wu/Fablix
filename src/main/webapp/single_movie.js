function showAddCartToast() {
    $('#add_cart_toast').toast('show');
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
        // $('#rating').html(resultData['rating'] === -1? 'No Rating' : resultData['rating']);
        $('#movie_id').html(resultData['id']);
        $('#genre').html(resultData['genre_list']);
        $('#stars').html(getStars(resultData));

        $('#add_to_cart').attr
        ('onclick', 'addMovieToCart("m_' + resultData['id'] +  '");showAddCartToast()');

        var bar = new ProgressBar.Circle('#rating', {
            strokeWidth: 6,
            easing: 'easeInOut',
            duration: 1400,
            color: '#157ffb',
            trailColor: 'white',
            trailWidth: 1,
            svgStyle: {
                // display: 'flex',
                // 'align-items': 'center',
                // margin: 'auto'
            },
            text: {
                value: resultData['rating']
            }
        });

        bar.animate(resultData['rating'] / 10);  // Number from 0.0 to 1.0
    })
}



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