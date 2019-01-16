var count = 0;

$(function(){
    var button = $('button');
    button.animate({
        opacity: 0
    },
        10000,
        null,
        function () {
            this.remove();
        })
});

function greet() {
    $(function () {
        var button = $('button');

        count ++;
        var newItem = $('<li>' + count + '</li>');
        newItem.css({
            'opacity': 0
        });

        var items = $('ul');

        button.animate({
                top: '+=30'
            },
            1000);
        items.append(newItem);
        newItem.animate({
            opacity: 1
        },
            1000)
    })
}