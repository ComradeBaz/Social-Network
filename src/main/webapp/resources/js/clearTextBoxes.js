/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    // Email Address
    var watermarkEmail = "Email Address";
    if ($('#emailAddress').val().length > 0) {
        $('#emailAddress').val("");

    }
    //init, set watermark text and class
    $('#emailAddress').val(watermarkEmail).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#emailAddress').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkEmail).addClass('watermark');
        }
    });

});

$(window).on("load", function () {
    var someVariable = "";
    $('input[type="password"]#password').val("");
    $('input[type="password"]#confirmPassword').val("");
    $('#password').val(someVariable);

    var emailAddressTextBox = document.getElementById("emailAddress");
    emailAddressTextBox.value = "";
});



$(document).ready(function () {
    setAutocomplete();
   $('input[type="password"]#password').val("");
    var watermarkFirstName = 'First Name';
    //init, set watermark text and class
    $('#firstName').val(watermarkFirstName).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#firstName').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkFirstName).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#firstName').focus(function () {
        if ($(this).val() == watermarkFirstName) {
            $(this).val('').removeClass('watermark');
        }
    });

    // Last Name   
    var watermarkLastName = "Last Name";

    //init, set watermark text and class
    $('#lastName').val(watermarkLastName).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#lastName').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkLastName).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#lastName').focus(function () {
        if ($(this).val() == watermarkLastName) {
            $(this).val('').removeClass('watermark');
        }
    });

    // Email Address
    var watermarkEmail = "Email Address";

    //init, set watermark text and class
    $('#emailAddress').val(watermarkEmail).addClass('watermark');
    var emailAddressText = document.getElementById("emailAddress");
    emailAddressText.value = "Email Address";

    //if blur and no value inside, set watermark text and class again.
    $('#emailAddress').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkEmail).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#emailAddress').focus(function () {
        if ($(this).val() == watermarkEmail) {
            $(this).val('').removeClass('watermark');
        }
    });

    // Password
    var watermarkPassword = "Password";

    //init, set watermark text and class
    $('#password').val(watermarkPassword).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#password').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkPassword).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#password').focus(function () {
        if ($(this).val() == watermarkPassword) {
            $(this).val('').removeClass('watermark');
        }
    });

    // Password
    var watermarkPasswordConfirm = "Password";

    //init, set watermark text and class
    $('#confirmPassword').val(watermarkPasswordConfirm).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#confirmPassword').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkPasswordConfirm).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#confirmPassword').focus(function () {
        if ($(this).val() == watermarkPasswordConfirm) {
            $(this).val('').removeClass('watermark');
        }
    });

});

// Prevent Chrome from autocompleting 
function setAutocomplete()
{
    if (navigator.userAgent.toLowerCase().indexOf('chrome') >= 0)
    {
        setTimeout(function () {
            document.getElementById('emailAddress').setAttribute('autocomplete', 'off');
        }, 100);
    }
}