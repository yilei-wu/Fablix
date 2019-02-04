let movie_template = $('<div class="row justify-content-center mt-5">\n' +
    '        <div class="col-sm-4 movie_title" id="title">\n' +
    '            Loading...\n' +
    '        </div>\n' +
    '        <div class="col-sm-3 mt-4">\n' +
    '            <div class="input-group">\n' +
    '                <div class="input-group-prepend">\n' +
    '                    <span class="input-group-text" id="basic-addon1">Quantity</span>\n' +
    '                </div>\n' +
    '                <input type="number" min="1" step="1" class="form-control" aria-describedby="basic-addon1" id="quantity">\n' +
    '            </div>\n' +
    '        </div>\n' +
    '        <div class="col-sm-2 mt-4">\n' +
    '            <a class="btn btn-primary" id="remove_button">\n' +
    '                Remove\n' +
    '            </a>\n' +
    '        </div>\n' +
    '    </div>');

function updateTitle(id, title){
    $('#m_' + id).find('#title').text(title);
}

function removeMovie(id){
    let holder = $('#movie_holder');
    holder.find('#' + id).remove();
}

function changeQuantity(id){
    let holder = $('#movie_holder');
    let new_value = holder.find('#' + id).find('#quantity').val();
    sessionStorage.setItem(id, new_value);
}

function handlePaymentResult(result) {
    console.log(result)
}

function submitPayment(form){
    console.log("submit payment form  " + form);
    console.log($("#payment_form").serialize());

    var movies = '';

    for (var i = 0; i < sessionStorage.length; i++) {
        var key = sessionStorage.key(i);
        if (key.startsWith('m_')) {
            key = key.substring(2);
            movies += '&' + key + '=' + sessionStorage.getItem('m_' + key);
        }
    }

    let info = $("#payment_form").serialize() + movies;
    console.log(info);

    form.preventDefault();

    $.post(
        "api/purchase?" + info,
        (resultDataString) => handlePaymentResult(resultDataString)
    );
}

$(function () {
    $('#payment_form').submit(submitPayment);
    let holder = $('#movie_holder');
    for (var i = 0; i < sessionStorage.length; i++) {
        // console.log(sessionStorage.key(i) + '   ' + sessionStorage.getItem(sessionStorage.key(i)))
        var current = movie_template.clone();
        var key = sessionStorage.key(i);
        if (key.startsWith('m_')) {
            // key = key.substring(2);
            current.attr('id', sessionStorage.key(i));

            $.ajax({
                dataType: "json",
                method: "GET",
                url: "api/single_movie?id=" + key.substring(2),
                success: function (resultData) {
                    updateTitle(resultData['id'], resultData['title'])
                },
                error: function (error) {
                    console.log(error);
                }
            });
            console.log(current);
            // console.log(current.filter('#quantity'));
            current.find('#quantity')
                .attr('value', sessionStorage.getItem(key))
                .attr('onchange', 'changeQuantity("' + key + '")');
            current.find('#remove_button')
                .attr('onclick', 'sessionStorage.removeItem("' + key + '");removeMovie("' + key + '")');

            holder.append(current);
        }
    }


});