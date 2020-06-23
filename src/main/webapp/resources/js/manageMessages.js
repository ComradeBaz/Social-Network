/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    $("#receiver").removeClass("ui-selectonemenu");
});

// Close the div showing an open message
$(document).ready(function () {
    $("#closeOpenMessage").click(function () {
        if ($("#showMessageDiv").is(":visible")) {
            ($("#showMessageDiv").hide(500));
            ($("#listOfMessages").show(500));
        } else {
            ($("#showMessageDiv").show(500));
            ($("#listOfMessages").hide(500));
        }
    });
});

// Close the div showing an new message
$(document).ready(function () {
    $("#closeNewMessage").click(function () {
        if ($("#newMessageDiv").is(":visible")) {
            ($("#newMessageDiv").hide(500));
            ($("#listOfMessagesWrapperDiv").show(500));
        } else {
            ($("#newMessageDiv").show(500));
            ($("#listOfMessagesWrapperDiv").hide(500));
        }
    });
    //closeOpenMessageReply
    $("#closeOpenMessageReply").click(function () {
        if ($("#replyMessageTextAreaDiv").is(":visible")) {
            ($("#replyMessageTextAreaDiv").hide(500));
            ($("#listOfMessagesWrapperDiv").show(500));
        }
        if ($("#showMessageDiv").is(":visible")) {
            ($("#showMessageDiv").hide(500));
        }
    });
    // Close the div when the user clicks send from messages.xhtml
    $("#sendMessageButton").click(function () {
        if ($("#newMessageDiv").is(":visible")) {
            ($("#newMessageDiv").hide(500));
            ($("#listOfMessagesWrapperDiv").show(500));
            var messageTextBox = document.getElementById("messageText");
            messageTextBox.value = "";
        }
    });

    // Close the div when the user clicks send from memberProfile.xhtml
    $("#sendMessageFromProfileButton").click(function () {
        if ($("#newMessageDiv").is(":visible")) {
            ($("#newMessageDiv").hide(500));
            var messageTextBox = document.getElementById("messageText");
            messageTextBox.value = "";
        }
    });

    $("#newMessageButton").click(function (inputParam) {
        if ($("#newMessageDiv").is(":visible")) {
            ($("#newMessageDiv").hide(500));
            ($("#listOfMessagesWrapperDiv").show(500));
        } else {
            ($("#newMessageDiv").show(500));
            ($("#listOfMessagesWrapperDiv").hide(500));
            if ($("#replyMessageTextAreaDiv").is(":visible")) {
                ($("#replyMessageTextAreaDiv").hide(500));
            }
            if ($("#showMessageDiv").is(":visible")) {
                ($("#showMessageDiv").hide(500));
            }
        }
    });
});

/*
 *  Toggle the div showing an open message
 */

function toggleOpenMessage() {
    if ($("#newAlertModal").is(":visible")) {
        ($("#newAlertModal").hide(300));
    }
    if ($("#showMessageDiv").is(":visible")) {
        ($("#showMessageDiv")).hide(500);
        ($("#replyMessageTextAreaDiv")).hide(500);
        ($("#listOfMessagesWrapperDiv")).show(500);
    } else {
        ($("#showMessageDiv")).show(500);
        ($("#replyMessageTextAreaDiv")).show(500);
        ($("#listOfMessagesWrapperDiv")).hide(500);
    }
}

$(document).ready(function () {
    $("#showMessageDiv").dblclick(function () {
        if ($("#showMessageDiv").is(":visible")) {
            ($("#showMessageDiv").hide(500));
            ($("#listOfMessages").show(500));
        } else {
            ($("#showMessageDiv").show(500));
            ($("#listOfMessages").hide(500));
        }
    });
});