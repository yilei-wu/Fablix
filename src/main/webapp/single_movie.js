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
    })
}

var printError = function(req, status, err) {
    console.log('Error: ', status, err);
};

$(function () {
    // $('#movie-list').hide();
    // $('.progress-bar').animate({
    //         width: '95%'
    //     },
    //     300);
    // var title = $('#movie_title');
    // console.log(title);
    // title.html('hello');

    var bar = new ProgressBar.Circle('#rating', {
        strokeWidth: 6,
        easing: 'easeInOut',
        duration: 1400,
        color: '#3540ff',
        trailColor: '#eee',
        trailWidth: 1,
        svgStyle: {
            // display: 'flex',
            // 'align-items': 'center',
            // margin: 'auto'
        },
        text: {
            value: '9.0'
        }
    });

    bar.animate(0.9);  // Number from 0.0 to 1.0

    let movie_id = getParameterByName('id');

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/single_movie?id=" + movie_id,
        success: handleMovieListResult,
        error: printError
    });
});