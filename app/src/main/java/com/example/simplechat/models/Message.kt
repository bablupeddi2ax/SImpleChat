package com.example.simplechat.models

/**
 * Represents a message object used in a chat application.
 * @property message The content of the message.
 * @property senderId The unique identifier of the message sender
 * @property imageUrl The URL of the image attached to the message.(if any).
 */
class Message {
    var message: String? = null
    var senderId: String? = null
    var imageUrl: String? = null
    /**
     * Default constructor for the Message class.
     */
    constructor() {}

    /**
     * Constructor for creating a Message object with provided message content and sender ID.
     *
     * @param message The content of the message.
     * @param senderId The unique identifier of the message sender.
     *  @param imageUrl The URL of the image attached to the message (if any).
     */
    constructor(message: String?, senderId: String?, imageUrl: String?="") {
        this.message = message
        this.senderId = senderId
        this.imageUrl = imageUrl
    }

}
