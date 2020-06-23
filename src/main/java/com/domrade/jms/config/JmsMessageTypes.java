/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.jms.config;

/**
 *
 * @author David
 */
public enum JmsMessageTypes {
    SEND_FRIEND_REQUEST,
    ACCEPT_FRIEND_REQUEST,
    DELETE_FRIEND_REQUEST_SENT,
    DELETE_FRIEND_REQUEST_RECEIVED,
    DELETE_FRIEND,
    REQUEST_JOIN_NETWORK,
    CANCEL_REQUEST_JOIN_NETWORK,
    ACCEPT_REQUEST_JOIN_NETWORK,
    DENY_REQUEST_JOIN_NETWORK,
    LOG_USER_OUT_OF_NETWORK,
    LEAVE_NETWORK,
    NETWORK_WALL_UPDATED,
    SEND_MESSAGE,
    EVENTS_UPDATED;
}
