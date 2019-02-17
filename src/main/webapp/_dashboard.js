let attr_template = $('<div class="row justify-content-center" style="margin-top: 20px">\n' +
    '                    <div class="col-sm-2 justify-content-center overflow-auto attr_name" id="attr_name">\n' +
    '                        Attr Name\n' +
    '                    </div>\n' +
    '                    <div class="col-sm-2 justify-content-center overflow-auto attr_type" id="attr_type">\n' +
    '                        Attr Type\n' +
    '                    </div>\n' +
    '                </div>');

let table_template = $('<div class="table_meta">\n' +
    '                <div class="row justify-content-center" style="margin-top: 30px" id="table_head">\n' +
    '                    <div class="col-sm-4 justify-content-center overflow-auto table_name" id="table_name">\n' +
    '                        Table Name\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </div>');

function addAttr(table, name, type) {
    var current = attr_template.clone();
    current.find('#attr_name').text(name);
    current.find('#attr_type').text(type);
    table.find('#table_head').after(current);
}

function addTable(name) {
    var current = table_template.clone();
    current.find('#table_name').text(name);
    return current;
}

function updateMeta(metas) {
    for (var i = 0; i < metas.length; i++) {
        var current = addTable(metas[i]['name']);
        for (var j = 0; j < metas[i]['attributes'].length; j++) {
            addAttr(current, metas[i]['attributes'][j]['name'], metas[i]['attributes'][j]['type'])
        }

        $('#meta_data').append(current);
    }
}

function handleInsertResult(operation, result) {

}

function insertStar(formSubmitEvent) {
    formSubmitEvent.preventDefault();
    console.log('submit insert star form');
    // console.log("api/insert_star?" + $('#insert_star_form').serialize());

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/insert_star?" + $('#insert_star_form').serialize(),
        success: (resultDataString) => handleInsertResult('Insert Star', resultDataString),
        error: printError
    });
}

function addInfo(formSubmitEvent) {
    formSubmitEvent.preventDefault();
    console.log('submit add info form');

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/insert_movie?" + $('#add_info_form').serialize(),
        success: (resultDataString) => handleInsertResult('Add Info', resultDataString),
        error: printError
    });
}


$(function () {
    $('#insert_star_form').submit(insertStar);
    $('#add_info_form').submit(addInfo);

    $.ajax({
        dataType: "json",
        method: "GET",
        url: "api/dashboard",
        success: updateMeta,
        error: printError
    });
});