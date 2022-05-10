package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int user_id) {
        String sql = "select balance from account where user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, user_id);
        return balance;
    }

    @Override
    public void updateBalance(int typeId, int statusId, int userId, int receiverId, BigDecimal transferAmount, String note) {
        String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, transferAmount, userId);

        String sql2 = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(sql2, transferAmount, receiverId);
        LocalDateTime currentDate = LocalDateTime.now();
        String sql3 = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount, note, date_logged) " +
                " VALUES (?, ?, " +
                "(select account_id from account where user_id = ?), "+
                "(select account_id from account where user_id = ?), ?, ?,?); ";
        int transferId = jdbcTemplate.update(sql3, typeId, statusId, userId, receiverId, transferAmount, note, currentDate);
    }

//    @Override
//    public List<Transfer> getTransfers(int user_id) {
//        String sql = "select transfer_id, transfer_type_id, transfer_status_id, user_id, amount, note, date_logged from transfer\n" +
//                "join account on transfer.account_to = account.account_id\n" +
//                "where account_from = (select account_id from account where user_id = ?);";
//        List<Transfer> listOfTransfers = new ArrayList<>();
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, user_id);
//        while(result.next()){
//            Transfer transfer = mapRowToTransfer(result);
//            listOfTransfers.add(transfer);
//        }
//        return listOfTransfers;
//    }

    @Override
    public List<TransferDTO> getTransfers(int user_id) {
        String sql = "select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, note, date_logged from transfer\n" +
                "where account_from = (select account_id from account where user_id = ?);";
        List<TransferDTO> listOfTransfers = new ArrayList<>();
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, user_id);
        while(result.next()){
            TransferDTO transfer = mapRowToTransferDTO(result);
            listOfTransfers.add(transfer);
        }
        for (TransferDTO item: listOfTransfers) {
            sql = "SELECT tenmo_user.user_id FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id WHERE account_id = ?;";
            int tempId = jdbcTemplate.queryForObject(sql, Integer.class, item.getAccount_to());
            item.setReceiverId(tempId);
            sql = "SELECT tenmo_user.username FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id WHERE account_id = ?;";
            String tempName = jdbcTemplate.queryForObject(sql, String.class, item.getAccount_to());
            item.setReceiverUsername(tempName);
            sql = "SELECT tenmo_user.user_id FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id WHERE account_id = ?;";
            tempId = jdbcTemplate.queryForObject(sql, Integer.class, item.getAccount_from());
            item.setSenderId(tempId);
            sql = "SELECT tenmo_user.username FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id WHERE account_id = ?;";
            tempName = jdbcTemplate.queryForObject(sql, String.class, item.getAccount_from());
            item.setSenderUsername(tempName);

        }
        return listOfTransfers;
    }

    public Transfer getTransferById(int transferId){
        String sql = "select transfer_id, transfer_type_id, transfer_status_id, user_id, amount, note, date_logged from transfer\n" +
                "join account on transfer.account_to = account.account_id\n" +
                "WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = new Transfer();
        if (result.next()){
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setType(rs.getInt("transfer_type_id"));
        transfer.setStatus(rs.getInt(("transfer_status_id")));
        transfer.setReceiverId(rs.getInt("user_id"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
        transfer.setNote(rs.getString("note"));
        transfer.setDate_logged(rs.getTimestamp("date_logged").toLocalDateTime());
        return transfer;
    }

    private TransferDTO mapRowToTransferDTO(SqlRowSet rs) {
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setType(rs.getInt("transfer_type_id"));
        transfer.setStatus(rs.getInt(("transfer_status_id")));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
        transfer.setNote(rs.getString("note"));
        transfer.setDate_logged(rs.getTimestamp("date_logged").toLocalDateTime());
        return transfer;
    }

}
