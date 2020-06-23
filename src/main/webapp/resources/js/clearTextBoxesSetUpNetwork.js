/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    // Network Name
    var watermarkNetworkName = "Network  Name";
    if ($('#networkName').val().length > 0) {
        $('#networkName').val("");

    }
    //init, set watermark text and class
    $('#networkName').val(watermarkNetworkName).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#networkName').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkNetworkName).addClass('watermark');
        }
    });
    
    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#networkName').focus(function () {
        if ($(this).val() == watermarkNetworkName) {
            $(this).val('').removeClass('watermark');
        }
    });
    
    // City
    var watermarkCity = "City";
    if ($('#city').val().length > 0) {
        $('#city').val("");

    }
    //init, set watermark text and class
    $('#city').val(watermarkCity).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#city').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkCity).addClass('watermark');
        }
    });
    
    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#city').focus(function () {
        if ($(this).val() == watermarkCity) {
            $(this).val('').removeClass('watermark');
        }
    });
    // Country
    var watermarkCountry = "Country";
    if ($('#country').val().length > 0) {
        $('#country').val("");

    }
    //init, set watermark text and class
    $('#country').val(watermarkCountry).addClass('watermark');

    //if blur and no value inside, set watermark text and class again.
    $('#country').blur(function () {
        if ($(this).val().length == 0) {
            $(this).val(watermarkCountry).addClass('watermark');
        }
    });
    
    //if focus and text is watermrk, set it to empty and remove the watermark class
    $('#country').focus(function () {
        if ($(this).val() == watermarkCountry) {
            $(this).val('').removeClass('watermark');
        }
    });

});
