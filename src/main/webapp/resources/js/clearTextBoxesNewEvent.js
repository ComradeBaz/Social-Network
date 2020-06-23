/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
 
    var watermarkTitle = 'Event Name';
    //init, set watermark text and class
    $('#title').val(watermarkTitle).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#title').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkTitle).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#title').focus(function () {
        if ($(this).val() == watermarkTitle) {
            $(this).val('').removeClass('watermark');
        }
    });
    
    var watermarkLocation = 'Location';
    //init, set watermark text and class
    $('#location').val(watermarkLocation).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#location').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkLocation).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#location').focus(function () {
        if ($(this).val() == watermarkLocation) {
            $(this).val('').removeClass('watermark');
        }
    });
    
    var watermarkDate = 'Date';
    //init, set watermark text and class
    $('#date_input').val(watermarkDate).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#date_input').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkDate).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#date_input').focus(function () {
        if ($(this).val() == watermarkDate) {
            $(this).val('').removeClass('watermark');
        }
    });
    
    var watermarkTime = 'Time';
    //init, set watermark text and class
    $('#time_input').val(watermarkTime).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#time_input').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkTime).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#time_input').focus(function () {
        if ($(this).val() == watermarkTime) {
            $(this).val('').removeClass('watermark');
        }
    });
    
    var watermarkDescription = 'Description...';
    //init, set watermark text and class
    $('#description').val(watermarkDescription).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#description').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkDescription).addClass('watermark');
        }
    });

    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#description').focus(function () {
        if ($(this).val() == watermarkDescription) {
            $(this).val('').removeClass('watermark');
        }
    });
});
