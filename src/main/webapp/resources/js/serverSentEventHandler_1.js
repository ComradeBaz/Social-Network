/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
var sseBroadcasterUrl;
var source;
var notificationIcon;
var pathName;



$(document).ready(function () {
    /*
     * 
     *  <h:inputHidden id="messageCategory"/>
     <h:inputHidden id="messageType"/>
     <h:inputHidden id="entityId"/>
     */


    sseBroadcasterUrl = document.getElementById("sseBroadcasterUrlForBrowser").textContent;
    var pageUrl = window.location.pathname;
    if (pageUrl === "/secured/view/manageNetworkUsers.xhtml") {
        notificationIcon = document.getElementById("pendingJoinRequests:newNotificationIcon");
    } else {
        notificationIcon = document.getElementById("newNotificationIcon");
    }
    pathName = window.location.pathname;
    listenForServerSentEvents();
});

function listenForServerSentEvents() {
    source = new EventSource(sseBroadcasterUrl);
    source.onmessage = async function (event) {
        //document.getElementById("dataToSubmit").value = event.data;
        var obj = JSON.parse(event.data);
        var userId = document.getElementById("userIdSse").value;
        var userNetworkId = document.getElementById("userNetworkIdSse").value;
        var messageUserId = obj.userId;
        var messageNetworkId = obj.networkId;
        var messageType = obj.messageType;
        var messageCategory = obj.messageCategory;
        var userWhoGeneratedEvent = obj.userWhoGeneratedEvent;
        var entityId = obj.entityId;

        await sleep(2000);

        processSse(messageType, messageCategory, messageNetworkId, messageUserId, userNetworkId, userId, userWhoGeneratedEvent, entityId);
    };
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function processSse(messageType, messageCategory, messageNetworkId, messageUserId, networkId, userId, userWhoGeneratedEvent, entityId) {
    var userRole = document.getElementById("userRole").value;

    switch (messageCategory) {
        case "FOR_USERS":
            if (messageUserId !== userId) {
                // Current user is not the target of the event, do nothing
                break;
            }
            processMessageForUsers(messageType, messageUserId, userWhoGeneratedEvent, entityId);
            break;
        case "FOR_ADMIN":
            if (userRole === "ROLE_ADMIN_MEMBER") {
                if (messageNetworkId !== networkId) {
                    // Message does not apply to the network of which the logged 
                    // in user is an admin
                    break;
                }
                processMessageForAdmins(messageType, userWhoGeneratedEvent);
            }
            break;
        case "FOR_NETWORK_MEMBERS":
            if (messageNetworkId !== networkId) {
                // Message does not apply to logged in user's network
                break;
            }
            if (messageUserId !== userId)
                // User not notified when they make an update
                processMessageForNetworkMembers(messageType, userWhoGeneratedEvent);
            break;
    }
}

function processMessageForUsers(messageType, messageUserId, userWhoGeneratedEvent, entityId) {
    var messageMenuItemToUpdateList = document.getElementsByClassName("messagesNotNotified");
    var friendMenuItemToUpdateList = document.getElementsByClassName("friendsNotNotified");
    var updateFriendsCollectionsButton = document.getElementById("updateFriendCollections");
    var updateMessagesCollectionsButton = document.getElementById("updateMessageCollections");

    var messageCategoryToSubmit = document.getElementById("messageCategory");
    var messageTypeToSubmit = document.getElementById("messageType");
    var entityIdToSubmit = document.getElementById("entityId");
    var submitSseDataButton = document.getElementById("submitSse");

    messageCategoryToSubmit.value = "FOR_USERS";
    messageTypeToSubmit.value = messageType;
    entityIdToSubmit.value = entityId;
    // Submit the data to update loggedInUser's collections
    submitSseDataButton.click();

    switch (messageType) {
        case "SEND_FRIEND_REQUEST":
            // Logged in user may have received the request
            var pageUrl = window.location.pathname;
            var theDiv = document.getElementById("newAlertDiv");
            if ((pageUrl === "/secured/view/manageFriends.xhtml") || (pageUrl === "/app/secured/view/manageFriends.xhtml")) {
                doFormatting(friendMenuItemToUpdateList, false);
                updateFriendsCollectionsButton.click();
            } else {
                doFormatting(friendMenuItemToUpdateList, true);
            }
            theDiv.innerText = userWhoGeneratedEvent + " sent you a friend request";
            showAlertModal();
            break;
        case "DELETE_FRIEND_REQUEST_SENT":
            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/manageFriends.xhtml") || (pageUrl === "/app/secured/view/manageFriends.xhtml")) {
                updateFriendsCollectionsButton.click();
            }
            break;
        case "ACCEPT_FRIEND_REQUEST":
            // Let the user know

            var pageUrl = window.location.pathname;
            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " accepted your friend request.";
            if ((pageUrl === "/secured/view/manageFriends.xhtml") || (pageUrl === "/app/secured/view/manageFriends.xhtml")) {
                doFormatting(friendMenuItemToUpdateList, false);
                updateFriendsCollectionsButton.click();
            } else {
                doFormatting(friendMenuItemToUpdateList, true);
            }
            showAlertModal();
            break;
        case "DELETE_FRIEND":
            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/manageFriends.xhtml") || (pageUrl === "/app/secured/view/manageFriends.xhtml")) {
                updateFriendsCollectionsButton.click();
            }
            break;
        case "DELETE_FRIEND_REQUEST_RECEIVED":
            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/manageFriends.xhtml") || (pageUrl === "/app/secured/view/manageFriends.xhtml")) {
                updateFriendsCollectionsButton.click();
            }
            break;
        case "SEND_MESSAGE":
            //submitSseDataButton.click();
            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " sent you a message.";
            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/messages.xhtml") || (pageUrl === "/app/secured/view/messages.xhtml")) {
                doFormatting(messageMenuItemToUpdateList, false);
                updateMessagesCollectionsButton.click();
            } else {
                doFormatting(messageMenuItemToUpdateList, true);
            }
            showAlertModal();
            break;
        case "ACCEPT_REQUEST_JOIN_NETWORK":
            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " accepted your request to join their network.";
            //doFormatting(messageMenuItemToUpdateList, true);
            showAlertModal();
            break;

        case "REJECT_REQUEST_JOIN_NETWORK":
            break;
        default:
    }
}

function processMessageForAdmins(messageType, userWhoGeneratedEvent) {
    var manageGuestsMenuItemToUpdateList = document.getElementsByClassName("manageGuestsNotNotified");

    var messageCategoryToSubmit = document.getElementById("messageCategory");
    var messageTypeToSubmit = document.getElementById("messageType");
    var entityIdToSubmit = document.getElementById("entityId");
    var submitSseDataButton = document.getElementById("submitSse");
    messageCategoryToSubmit.value = "FOR_ADMIN";
    messageTypeToSubmit.value = messageType;
    // Add entityId default value. It is ignored.
    entityIdToSubmit.value = -1;
    // Submit the data to update loggedInUser's collections
    submitSseDataButton.click();

    switch (messageType) {
        case "REQUEST_JOIN_NETWORK":

            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " is requesting to join your network.";
            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/manageNetworkUsers.xhtml") || (pageUrl === "/app/secured/view/manageNetworkUsers.xhtml")) {
                doFormatting(manageGuestsMenuItemToUpdateList, false);
                var updateButton = document.getElementById("updateNetworkAdminCollections");
                updateButton.click();
            } else {
                doFormatting(manageGuestsMenuItemToUpdateList, true);
            }
            showAlertModal();
            break;
        case "CANCEL_REQUEST_JOIN_NETWORK":
            if ((pageUrl === "/secured/view/manageNetworkUsers.xhtml") || (pageUrl === "/app/secured/view/manageNetworkUsers.xhtml")) {
                var updateButton = document.getElementById("updateNetworkAdminCollections");
                updateButton.click();
            }
            break;
        case "LEAVE_NETWORK":
            if ((pageUrl === "/secured/view/manageNetworkUsers.xhtml") || (pageUrl === "/app/secured/view/manageNetworkUsers.xhtml")) {
                var updateButton = document.getElementById("updateNetworkAdminCollections");
                updateButton.click();
            }
            break;
    }
}

function processMessageForNetworkMembers(messageType, userWhoGeneratedEvent) {
    var eventMenuItemToUpdateList = document.getElementsByClassName("eventsNotNotified");
    var wallMenuItemToUpdateList = document.getElementsByClassName("wallNotNotified");

    var messageCategoryToSubmit = document.getElementById("messageCategory");
    var messageTypeToSubmit = document.getElementById("messageType");
    var entityIdToSubmit = document.getElementById("entityId");
    var submitSseDataButton = document.getElementById("submitSse");
    messageCategoryToSubmit.value = "FOR_NETWORK_MEMBERS";
    messageTypeToSubmit.value = messageType;
    // Add entityId default value. It is ignored.
    entityIdToSubmit.value = -1;
    // Submit the data to update loggedInUser's collections
    submitSseDataButton.click();

    switch (messageType) {
        case "NETWORK_WALL_UPDATED":
            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " added a new wall post.";

            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/network.xhtml") || (pageUrl === "/app/secured/view/network.xhtml")) {
                doFormatting(wallMenuItemToUpdateList, false);
                var theButton = document.getElementById("updateTimeline");
                theButton.style.display = "block";
                theDiv.innerText = "";
            } else {
                doFormatting(wallMenuItemToUpdateList, true);
            }

            showAlertModal();
            break;
        case "EVENTS_UPDATED":
            var theDiv = document.getElementById("newAlertDiv");
            theDiv.innerText = userWhoGeneratedEvent + " added a new event.";

            var pageUrl = window.location.pathname;
            if ((pageUrl === "/secured/view/events.xhtml") || (pageUrl === "/app/secured/view/events.xhtml")) {
                doFormatting(eventMenuItemToUpdateList, false);
                var updateButton = document.getElementById("updateEventsCollections");
                updateButton.click();
            } else {
                doFormatting(eventMenuItemToUpdateList, true);
            }
            showAlertModal();
    }
}

/*
 *  Display the notification icon
 *  Highlight the relevant menu item in bold and add an "*"
 */
function doFormatting(elementToFormat, displayIcon) {
    if (displayIcon === true) {
        notificationIcon.style.display = "block";
    }
    var i;
    for (i = 0; i < elementToFormat.length; i++) {
        var txt = elementToFormat[i].innerHTML;
        elementToFormat[i].innerHTML = txt;
        elementToFormat[i].style.fontWeight = "bold";
    }
}

/*
 *  Hide the notification icon when the user clicks the menu showing the icon
 */
function hideNotificationIcon() {
    if (notificationIcon.style.display === "block") {
        notificationIcon.style.display = "none";
    }
}

/*
 *  Hide the notification icon when the user clicks the menu showing the icon 
 *  only for memberProfile.xhtml
 */
function hideNotificationIconMemberProfile() {
    if (notificationIcon.style.display === "block") {
        notificationIcon.style.display = "none";
    }
    if ($("#menuList").is(":visible")) {
        ($("#menuList").hide(500));
    } else {
        ($("#menuList").show(500));
    }
    if ($("#newAlertModal").is(":visible")) {
        ($("#newAlertModal")).hide(200);
    }
}

/*
 *  Pop up an alert informing the user they have a notification for the
 *  they are on
 */
function showAlertModal() {
    var theModal = document.getElementById("newAlertModal");
    theModal.style.display = "block";
}

function closeAlertModal() {
    var theModal = document.getElementById("newAlertModal");
    var notificationIcon = document.getElementById("newNotificationIcon");
    theModal.style.display = "none";
    notificationIcon.style.display = "none";
    var pageUrl = window.location.pathname;
    if ((pageUrl === "/secured/view/network.xhtml") || (pageUrl === "/app/secured/view/network.xhtml")) {
        window.scrollTo({top: 0, left: 0, behavior: 'smooth'});
        var theButton = document.getElementById("updateTimeline");
        theButton.style.display = "none";
    }
}
