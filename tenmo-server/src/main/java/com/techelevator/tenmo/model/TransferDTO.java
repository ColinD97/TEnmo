package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferDTO {

    private int transferId;
    private int type;
    private int status;
    private int account_from;
    private int account_to;
    private int senderId;
    private String senderUsername;
    private int receiverId;
    private String receiverUsername;
    private BigDecimal transferAmount;
    private String note;
    private LocalDateTime date_logged;

    public int getTransferId() {
        return transferId;
    }
    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(LocalDateTime date_logged) {
        this.date_logged = date_logged;
    }
}
