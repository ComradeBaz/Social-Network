/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    // Network ID
    var watermarkNetworkName = "Enter Network ID";
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
});