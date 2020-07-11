package model.chat;

import model.ModelBasic;
import model.ModelUtilities;
import model.account.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements ModelBasic {
    private static Map<String, Message> allMessages = new HashMap<>();
    private static int lastNum = 1;
    private String messageId;
    private String chatId;
    private String senderId;
    private Date sendDate;

    public Message(String chatId, String senderId, Date sendDate) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.sendDate = sendDate;
        initialize();
    }

    public static List<Message> getAllMessages() {
        return ModelUtilities.getAllInstances(allMessages.values(), false);
    }

    public static Message getMessageById(String messageId) {
        return ModelUtilities.getInstanceById(allMessages, messageId, false);
    }

    @Override
    public void initialize() {
        if (messageId == null)
            messageId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allMessages.put(messageId, this);
        lastNum++;

        getChat().addMessage(messageId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    public void terminate() {
        allMessages.remove(messageId);
    }

    @Override
    public String getId() {
        return messageId;
    }

    public Chat getChat() {
        return Chat.getChatById(chatId);
    }

    public Account getSender() {
        return Account.getAccountById(senderId);
    }

    public Date getSendDate() {
        return sendDate;
    }
}