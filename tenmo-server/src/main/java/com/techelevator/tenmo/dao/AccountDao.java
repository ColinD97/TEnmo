package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(int account_id);

    void updateBalance(int type, int status, int userId, int receiverId, BigDecimal transferAmount, String note);

    List<Transfer> getTransfers(int user_id);

    Transfer getTransferById(int transferId);
}
