let SINGLE_STAR_URL = 'single_star.html';
let SINGLE_MOVIE_URL = 'single_movie.html';

function getStar(stars, num = -1) {
    let id;
    let name;
    if (num !== -1) {
        id = stars[num]['star_id'];
        name = stars[num]['star_name'];
    } else {
        id = stars['star_id'];
        name = stars['star_name'];
    }

    return $('<a href = ' + SINGLE_STAR_URL + '?id=' + id + ' >'
        + name + '</a>')
}

function getMovie(data, num) {
    return $('<a href = ' + SINGLE_MOVIE_URL + '?id=' + data[num]['id'] + ' >'
        + data[num]['title'] + '</a>')
}

function getStars(data, num = -1) {
    var stars = $('<div></div>');
    var stars_list;
    if (num !== -1) {
        stars_list = data[num]['star_list'];
    } else {
        stars_list = data['star_list'];
    }
    for (var i = 0; i < stars_list.length; i++) {
        if (i !== 0) {stars.append(', ')}
        stars.append(getStar(stars_list, i));
    }
    return stars;
}