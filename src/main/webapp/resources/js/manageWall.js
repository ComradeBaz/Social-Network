/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var editProfilePictureButton = document.getElementById("theProfilePicture");
    $(editProfilePictureButton).mouseover(function () {

    });
});

function closePostReplyModal() {
    var postReplyModal = document.getElementById("replyToPostModal");   
    postReplyModal.style.display = "none";    
}

function closeNewPostModal() {
    var newPostModal = document.getElementById("myPostModal");    
    newPostModal.style.display = "none";   
}

function openReplyToPostModal() {
    clearWallPostReplyTextArea();
    var replyToPostModal = document.getElementById("replyToPostModal")
    replyToPostModal.style.display = "block";
}

function openNewPostModal() {
    clearWallPostTextArea();
    var newPostModal = document.getElementById("myPostModal");
    newPostModal.style.display = "block";
}

function clearWallPostTextArea() {
    var newPostTextArea = document.getElementById("userPost");
    newPostTextArea.value = "";
}

function clearWallPostReplyTextArea() {
    var postReplyTextArea = document.getElementById("uReply");
    postReplyTextArea.value = "";
}