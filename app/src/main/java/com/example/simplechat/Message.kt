package com.example.simplechat

/**
 * Represents a message object used in a chat application.
 * @property message The content of the message.
 * @property senderId The unique identifier of the message sender.
 */
class Message {
    var message: String? = null
    var senderId: String? = null

    /**
     * Default constructor for the Message class.
     */
    constructor() {}

    /**
     * Constructor for creating a Message object with provided message content and sender ID.
     *
     * @param message The content of the message.
     * @param senderId The unique identifier of the message sender.
     */
    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }
}
