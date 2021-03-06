package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class AccountService {

    private static final String API_BASE_URL = "http://localhost:3000/account";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal viewCurrentBalance(){
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET,
                    makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
            System.out.println(balance);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public List<User> getUsers(){
        List<User> userList = null;
        try {
            ResponseEntity<User[]> response = restTemplate.getForEntity(API_BASE_URL+"/user", User[].class);
            User[] userArray = response.getBody();
            userList = Arrays.asList(userArray);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return userList;
    }

    public Transfer transfer(Transfer transfer){
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        Transfer returnTransfer = null;
        try {
            returnTransfer = restTemplate.postForObject(API_BASE_URL + "/transfer", entity, Transfer.class);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return returnTransfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
