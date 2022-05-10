package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @RequestMapping(method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.getBalance(userId);
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> getUsers() {
        //TODO get list of users
        List<User> userList = userDao.findAll();

        return userList;
    }


    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void transferMoney(@RequestBody Transfer transfer, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        accountDao.updateBalance(transfer.getType(), transfer.getStatus(), userId, transfer.getReceiverId(), transfer.getTransferAmount(), transfer.getNote());
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<TransferDTO> getTransfers(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        List<TransferDTO> listOfTransfers;
        listOfTransfers = accountDao.getTransfers(userId);
        return listOfTransfers;
    }

//    @PreAuthorize()
    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId){
        Transfer transfer;
        transfer = accountDao.getTransferById(transferId);
        return transfer;
    }

}
