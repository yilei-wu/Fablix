let SINGLE_STAR_URL = 'single_star.html';
let SINGLE_MOVIE_URL = 'single_movie.html';
let MOVIE_LIST_URL = 'movie_list.html';

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
// import * as ProgressBar from "./progressbar";

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


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

function getMovie(movies, num = -1) {
    let id;
    let title;
    if (num !== -1) {
        id = movies[num]['movie_id'];
        title = movies[num]['movie_title'];
    } else {
        id = movies['movie_id'];
        title = movies['movie_title'];
    }

    return $('<a href = ' + SINGLE_MOVIE_URL + '?id=' + id + '>'
        + title + '</a>')
}

function getBackButton(url) {
    return $('<a href = ' + url + '>Back to Homepage</a>')
}

function getMovies(data, num = -1) {
    var movies = $('<div></div>');
    var movie_list;
    if (num !== -1) {
        movie_list = data[num]['movie_list'];
    } else {
        movie_list = data['movie_list'];
    }
    for (var i = 0; i < movie_list.length; i++) {
        if (i !== 0) {movies.append(', ')}
        movies.append(getMovie(movie_list, i));
    }
    return movies;
}

// function getStar(data, num) {
//     return $('<a href = ' + SINGLE_MOVIE_URL + '?id=' + data[num]['id'] + ' >'
//         + data[num]['title'] + '</a>')
// }

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

function getUserInfo() {
    return $('<div class="white">Welcome, </div>')
}