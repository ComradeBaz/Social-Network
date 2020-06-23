/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var showAttendees = document.getElementById("attendeeList");
    $("#attendees").click(function () {
        if ($("#attendeeList").is(":visible")) {
            ($("#attendeeList").hide(500));
        } else {

            ($("#attendeeList").show(500));
        }
    });
});

function openConfirmAttendEventModal() {
    var theModal = document.getElementById("confirmAttendEventModal");
    theModal.style.display = "block";
}

function closeConfirmAttendEventModal() {
    var theModal = document.getElementById("confirmAttendEventModal");
    theModal.style.display = "none";
}