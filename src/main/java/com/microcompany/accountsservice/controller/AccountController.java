package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    @Autowired
    IAccountService accountService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity createAccount (@RequestBody Account newAccount) {
        //revisar exception

        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(newAccount));

    }

    @RequestMapping(value = "/all/{cid}",method = RequestMethod.GET)
    public ResponseEntity <List<Account>> getAccountsByCustomer(@PathVariable("cid") Long id) {
        //RE

        List<Account>allAcounts = new ArrayList<>();

        try {
            allAcounts= accountService.getAllAccountByOwnerId(id);
        } catch () {

        }




        return new ResponseEntity(allAcounts, HttpStatus.ACCEPTED);


        //Product prod = repository.findById(id).orElseThrow(() -> new ProductNotfoundException("No existe " + id));
        //return ResponseEntity.status(HttpStatus.OK).body(prod);

    }

    @RequestMapping(value="/{aid}",method = RequestMethod.GET)
    public ResponseEntity getSingleAccount(@PathVariable("aid") Long id) {
        //RE
        Account account = accountService.getAccount(id);
        return new ResponseEntity(account,HttpStatus.OK);
    }

    @RequestMapping(value="/{aid}",method = RequestMethod.PUT)
    public ResponseEntity updateAccount(@PathVariable("cid") Long id, @RequestBody Account account) {
        //RE
        account.setId(id);
        accountService.updateAccount(account.getId(),account);
        return new ResponseEntity(account,HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/{aid}",method = RequestMethod.DELETE)
    public ResponseEntity deleteAccount(@PathVariable("cid") Long id) {
        //Devolver cuerpo y RE
        accountService.delete(id);
        return new ResponseEntity(id,HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/all/{cid}",method = RequestMethod.DELETE)
    public ResponseEntity deleteAccountsByOwnerId(@PathVariable("cid") Long cid) {
        //RE
        accountService.deleteAccountsUsingOwnerId(cid);
        return new ResponseEntity(cid,HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{aid}/withdrawbalance/{monto}",method = RequestMethod.POST)
    public ResponseEntity addBalanceToAccount(@PathVariable("aid")Long id, @PathVariable("monto") Integer balance) {
        //RE
        return new ResponseEntity(accountService.addBalance(id,balance),HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{aid}/addbalance/{monto}", method = RequestMethod.POST)
    public ResponseEntity withdrawBalanceToAccount(@PathVariable("aid")Long id, @PathVariable("monto") Integer balance) {
        //RE
        return new ResponseEntity(accountService.addBalance(id,balance),HttpStatus.ACCEPTED);
    }

    @GetMapping("/canloan/{cid}/{loan}")
    public ResponseEntity canLoan(@PathVariable("cid") Long id, @PathVariable("loan") Integer loan)  {
        //incluir metodo prestamo y RE
        return null;
    }

}
