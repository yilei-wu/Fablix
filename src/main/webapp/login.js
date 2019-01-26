/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    var resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to movie_list.html
    console.log(resultDataJson["status"]);
    if (resultDataJson["status"] === "success") {
        window.location.replace("index.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form  " + formSubmitEvent);
    console.log($("#login_form").serialize());
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        $("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    ).fail($("#login_error_message").text('Failed connect to api.'));
}

$(function () {
    // Bind the submit action of the form to a handler function
    $("#login_form").submit(submitLoginForm);
});
