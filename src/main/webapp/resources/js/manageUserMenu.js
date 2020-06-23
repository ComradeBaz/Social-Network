/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Show the menu when the user clicks the menu button
$(document).ready(function () {
    $("#menuButton").click(function () {
        if ($("#newAlertModal").is(":visible")) {
            ($("#newAlertModal")).hide(200);
        }
        if ($("#menuList").is(":visible")) {
            ($("#menuList").hide(500));
        } else {
            ($("#menuList").show(500));
        }
    });
});
$(document).ready(function () {

    var pendingRequestsButton = document.getElementById("newRequestsButton");
    var existingMembersButton = document.getElementById("membersButton");

    $(existingMembersButton).click(function () {
        if ($("#divPendingRequests").is(":visible")) {
            ($("#divPendingRequests").hide());
            ($("#divMembers").show());
            existingMembersButton.style.backgroundColor = "#e6e6e6";
            pendingRequestsButton.style.backgroundColor = "#f2f2f2";
        }
    });

    $(pendingRequestsButton).click(function () {
        if ($("#divMembers").is(":visible")) {
            ($("#divMembers").hide());
            ($("#divPendingRequests").show());
            pendingRequestsButton.style.backgroundColor = "#e6e6e6";
            existingMembersButton.style.backgroundColor = "#f2f2f2";
        }
    });
});

/* 
 * Manage friend requests - manageFriends.xhtml
 */

/*
 * 
 * When a friend is accepted show the updated pending friends list
 */
function acceptRequestCloseModal() {
    var pendingRequestsDiv = document.getElementById("divPendingFriendRequests");
    var existingFriendsDiv = document.getElementById("divFriends");
    var acceptFriendModal = document.getElementById("acceptFriendModal");

    acceptFriendModal.style.display = "none";
    pendingRequestsDiv.style.display = "block";
    existingFriendsDiv.style.display = "none";

}

$(document).ready(function () {

    var pendingRequestsButton = document.getElementById("newRequestsButton");
    var existingMembersButton = document.getElementById("membersButton");
    var pendingRequestsSentButton = document.getElementById("newRequestsButtonSent");

    /*
     * Display existing friends when the page loads
     */
    if (($("#divHidePendingFriendRequests").is(":visible")) || ($("#divSentRequests").is(":visible"))) {
        (($("#divHidePendingFriendRequests").hide()) && ($("#divSentRequests").hide()));
        ($("#hideMe").show());
        existingMembersButton.style.backgroundColor = "#e6e6e6";
        pendingRequestsButton.style.backgroundColor = "#f2f2f2";
        pendingRequestsSentButton.style.backgroundColor = "#f2f2f2";
    }

    $(existingMembersButton).click(function () {
        if (($("#divHidePendingFriendRequests").is(":visible")) || ($("#divSentRequests").is(":visible"))) {
            (($("#divHidePendingFriendRequests").hide(500)) && ($("#divSentRequests").hide(500)));
            ($("#hideMe").show(500));
            existingMembersButton.style.backgroundColor = "#e6e6e6";
            pendingRequestsButton.style.backgroundColor = "#f2f2f2";
            pendingRequestsSentButton.style.backgroundColor = "#f2f2f2";
        }
    });

    $(pendingRequestsButton).click(function () {
        if (($("#hideMe").is(":visible")) || ($("#divSentRequests").is(":visible"))) {
            (($("#hideMe").hide(500)) && ($("#divSentRequests").hide(500)));
            ($("#divHidePendingFriendRequests").show(500));
            pendingRequestsButton.style.backgroundColor = "#e6e6e6";
            existingMembersButton.style.backgroundColor = "#f2f2f2";
            pendingRequestsSentButton.style.backgroundColor = "#f2f2f2";
        }
    });

    $(pendingRequestsSentButton).click(function () {
        if (($("#hideMe").is(":visible")) || ($("#divHidePendingFriendRequests").is(":visible"))) {
            (($("#hideMe").hide(500)) && ($("#divHidePendingFriendRequests").hide(500)));
            ($("#divSentRequests").show(500));
            pendingRequestsButton.style.backgroundColor = "#f2f2f2";
            existingMembersButton.style.backgroundColor = "#f2f2f2";
            pendingRequestsSentButton.style.backgroundColor = "#e6e6e6";
        }
    });
});
