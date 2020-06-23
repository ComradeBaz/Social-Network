/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * 
 * @param {type} friendRequestSent
 * @param {type} currentUserAnotherUserFriends
 * Pop up a modal depending on the existing relationship between the loggedInUser
 * and anotherUser from memberProfile.xhtml
 */
function popUpModal(friendRequestSent, currentUserAnotherUserFriends) {
    var requestFriendModal = document.getElementById("requestFriendModal");
    var deleteFriendModal = document.getElementById("deleteFriendModal");
    var cancelRequestModal = document.getElementById("cancelRequestModal");

    if (friendRequestSent == "true") {
        cancelRequestModal.style.display = "block";
        //requestFriendModal.style.display = "block";
    } else if (currentUserAnotherUserFriends == "true") {
        cancelRequestModal.style.display = "block";
        //deleteFriendModal.style.display = "block";
    } else {
        //cancelRequestModal.style.display = "block";
        requestFriendModal.style.display = "block";
    }
}

function closeRequestFriendModal() {
    var requestFriendModal = document.getElementById("requestFriendModal");
    requestFriendModal.style.display = "none";
}

function closeCancelRequestModal() {
    var cancelRequestFriendModal = document.getElementById("cancelRequestModal");
    cancelRequestFriendModal.style.display = "none";
}

function closeDeleteFriendModal() {
    var deleteRequestFriendModal = document.getElementById("deleteFriendModal");
    deleteRequestFriendModal.style.display = "none";
}

function showMenu() {
    var menuList = document.getElementById("menuList");
    if ($(menuList).is(":visible")) {
        ($(menuList).hide(500));
    } else {
        ($(menuList).show(500));
    }
}

function showNewMessageDiv() {
    var newMessageDiv = document.getElementById("newMessageDiv");
    if ($(newMessageDiv).is(":visible")) {
        ($(newMessageDiv).hide(500));
    } else {
        ($(newMessageDiv).show(500));
    }
}

function closeModal() {
    if ($("#deleteFriendModal").is(":visible")) {
        ($("#deleteFriendModal").hide());
    }
    if ($("#acceptFriendModal").is(":visible")) {
        ($("#acceptFriendModal").hide());
    }
    if ($("#deleteFriendRequestSentModal").is(":visible")) {
        ($("#deleteFriendRequestSentModal").hide());
    }
    if ($("#deleteExistingFriendModal").is(":visible")) {
        ($("#deleteExistingFriendModal").hide());
    }
}

function deleteFriendClicked() {
    var theModal = document.getElementById("deleteExistingFriendModal");
    theModal.style.display = "block";
}

function deleteSentRequestClicked() {
    var theModal = document.getElementById("deleteFriendRequestSentModal");
    theModal.style.display = "block";
}

function deleteReceivedRequestClicked() {
    var theModal = document.getElementById("deleteFriendModal");
    theModal.style.display = "block";
}

function acceptFriendRequestClicked() {
    var theModal = document.getElementById("acceptFriendModal");
    theModal.style.display = "block";
}

var divPendingFriendRequests = document.getElementById("divPendingFriendRequests");
var divSentRequests = document.getElementById("divSentRequests");
var divFriends = document.getElementById("divFriends");

function closeAcceptFriendRequestModal() {
    var divPendingFriendRequests = document.getElementById("divPendingFriendRequests");
    var divSentRequests = document.getElementById("divSentRequests");
    var divFriends = document.getElementById("hideMe");
    var acceptFriendRequestModal = document.getElementById("acceptFriendModal");
    acceptFriendRequestModal.style.display = "none";
    divPendingFriendRequests.style.display = "block";
    divSentRequests.style.display = "none";
    divFriends.style.display = "none";
}

function closeDeleteFriendRequestModal() {
    var divPendingFriendRequests = document.getElementById("divPendingFriendRequests");
    var divSentRequests = document.getElementById("divSentRequests");
    var divFriends = document.getElementById("hideMe");
    var deleteFriendRequestModal = document.getElementById("deleteFriendModal");
    deleteFriendRequestModal.style.display = "none";
    divPendingFriendRequests.style.display = "block";
    divSentRequests.style.display = "none";
    divFriends.style.display = "none";
}

function closeDeleteFriendRequestSentModal() {
    var divPendingFriendRequests = document.getElementById("divPendingFriendRequests");
    var divSentRequests = document.getElementById("divSentRequests");
    var divFriends = document.getElementById("hideMe");
    var deleteFriendRequestSentModal = document.getElementById("deleteFriendRequestSentModal");
    deleteFriendRequestSentModal.style.display = "none";
    divPendingFriendRequests.style.display = "none";
    divSentRequests.style.display = "block";
    divFriends.style.display = "none";
}

function closeDeleteExistingFriendModal() {
    var divPendingFriendRequests = document.getElementById("divPendingFriendRequests");
    var divSentRequests = document.getElementById("divSentRequests");
    var divFriends = document.getElementById("divFriends");
    var deleteExistingFriendModal = document.getElementById("deleteExistingFriendModal");
    deleteExistingFriendModal.style.display = "none";
    divPendingFriendRequests.style.display = "none";
    divSentRequests.style.display = "none";
    divFriends.style.display = "block";
}

