/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 * @param isEmployee
 */
function handleLoginResult(resultDataString, isEmployee) {
    var resultDataJson = JSON.parse(resultDataString);

    console.log(resultDataJson);
    // console.log('employee? ' + isEmployee);

    if (resultDataJson["status"] === "success") {
        //TODO Check recaptcha result
        if (isEmployee) {
            window.location.replace("_dashboard.html");
        } else {
            window.location.replace("index.html");
        }
    } else {
        $('#submit_button').removeClass('disabled')
            .text('Submit');
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form  " + formSubmitEvent);
    // console.log($("#login_form").serialize());
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $("#login_error_message").text('');
    $('#submit_button').addClass('disabled')
        .text('Loading...');

    if($("#employeeCheck").is(':checked')) {
        console.log('employee login');
        $.post(
            "api/employee_login",
            // Serialize the login form to the data sent by POST request
            $("#login_form").serialize(),
            (resultDataString) => handleLoginResult(resultDataString, true)
        );
    } else {
        console.log('customer login');
        $.post(
            "api/login",
            $("#login_form").serialize(),
            (resultDataString) => handleLoginResult(resultDataString, false)
        );
    }
}

// function loginFormAnim(){
//     console.log('animate');
//
// }

$(function () {
    // Bind the submit action of the form to a handler function
    $("#login_form").submit(submitLoginForm);

    $('#fablix_title').animate({
            opacity: 1
        },
        1300,
        null,
        function () {
            $('#login_area').animate({
                    opacity: 1
                },
                600)
        })
});
