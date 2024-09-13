package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.persistence.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class AccountService implements IAccountService {
    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository cRepo;

    @Override
    @Transactional
    public Account create(Account account) {
        LocalDate current_Date = LocalDate.now();
        account.setOpeningDate(current_Date);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null; // Will be gotten from user service
        account.setOwner(owner);
        return account;
    }

    @Override
    public List<Account> getAllAccountByOwnerId(Long ownerId) {

        return accountRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public Account updateAccount(Long id, Account account) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        newAccount.setType(account.getType());
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public Account addBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null;// Will be gotten from user service
        int newBalance = newAccount.getBalance() + amount;
        newAccount.setBalance(newBalance);
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public Account withdrawBalance(Long id, int amount) throws Exception {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        int newBalance = newAccount.getBalance() - amount;
        if (newBalance > 0) {
            newAccount.setBalance(newBalance);
            return accountRepository.save(newAccount);
        } else {
            throw new Exception("Insufficient balance");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        this.accountRepository.delete(account);
    }

    @Override
    @Transactional
    public void deleteAccountsUsingOwnerId(Long ownerId) {
        accountRepository.deleteByOwnerId(ownerId);
        /*List<Account> accounts = accountRepository.findByOwnerId(ownerId);
        for (Account account : accounts) {
            this.accountRepository.delete(account);
        }*/
    }

    @Override
    @Transactional
    public Account getOneAccountById(Long aId) {
       Account account = accountRepository.findById(aId).orElseThrow();
       return account;
    }

    @Override
    @Transactional
    public Account createNewOwnerAccount(Long ownerId) {
        Account newOwnerAccount = new Account();
        Customer owner = cRepo.findById(ownerId).orElseThrow(() -> new CustomerNotFoundException("Usuario no encontrado"));
        newOwnerAccount.setOwner(owner);
        return newOwnerAccount;
    }

    @Override
    @Transactional
    public Account updateOwnerAccount(Long ownerId) {
        List <Account> a = accountRepository.findByOwnerId(ownerId);
        Customer owner = cRepo.findById(ownerId).orElseThrow();
        a.get(1).setOwner(owner);
        return a.get(1);
    }

    @Override
    @Transactional
    public void deleteOwnerAccount (Long ownerId) {
        Account account = accountRepository.findByOwnerId(ownerId).get(1);
        accountRepository.delete(account);
    }

    @Override
    @Transactional
    public Account addBalance(Long aId, Integer balance) {
        Account account = accountRepository.findById(aId).orElseThrow();
        account.setBalance(account.getBalance()+balance);
        return account;

    }

    @Override
    @Transactional
    public void deleteAllOwnerAccounts(Long ownerId) {
        List<Account> allAccounts = accountRepository.findByOwnerId(ownerId);
        accountRepository.deleteAll(allAccounts);
    }

    @Override
    public boolean canOwnerLoan (Long ownerId, Integer RequestPrestamo) {
        Customer owner = cRepo.findById(ownerId).orElseThrow() ;
        List <Account> ownerAccount = accountRepository.findByOwnerId(ownerId);
        int monto= 0;

        for (Account oC : ownerAccount ) {
            monto+=oC.getBalance();
        }
        if (monto >= RequestPrestamo*0.80) {
            return  true;
        }
        else {
            return false;
        }
    }
}
