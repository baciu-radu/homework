package com.ing.hubs.controllers;

import com.ing.hubs.exceptions.*;
import com.ing.hubs.models.*;
import com.ing.hubs.services.AccountService;
import com.ing.hubs.services.TransactionService;
import com.ing.hubs.util.FormattedDouble;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/app/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<String> create(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) throws UserNotFoundException, AccountNotFoundException, InsufficientFundsException, UnauthorizedException, ConstraintException, NotPositiveNumericException {
        Account initializerAccount = accountService.findAccount(transactionRequest.getInitializerAccountId()).get();
        Account targetAccount = accountService.findAccount(transactionRequest.getTargetAccountId()).get();
        if (initializerAccount.getCurrency().equals(targetAccount.getCurrency())) {
            Transaction transaction = transactionService.createTransaction(transactionRequest, request);

            return ResponseEntity.ok("Transaction completed successfully!\nSuccessfully sent " + transaction.getAmount() + " " + transaction.getCurrency() + " from user " + transaction.getInitializerAccount().getUser().getFirstName() + " " + transaction.getInitializerAccount().getUser().getLastName() + " to user " + transaction.getTargetAccount().getUser().getFirstName() + " " + transaction.getTargetAccount().getUser().getLastName());


        } else {
            ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                    .amount(transactionRequest.getAmount())
                    .initializerAccountId(transactionRequest.getInitializerAccountId())
                    .targetAccountId(transactionRequest.getTargetAccountId())
                    .build();
            exchange(exchangeRequest, request);
            return ResponseEntity.ok("Exchange detected! We have completed the exchange from your account currency to the target account currency");
        }

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<String> showAccountHistory(@PathVariable(value = "accountId") int accountId, HttpServletRequest request) throws UserNotFoundException, AccountNotFoundException, ConstraintException, UnauthorizedException, NoSuchElementException {

        System.out.println(transactionService.getTransactionHistory(accountId, request));
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, request));

    }

    @PostMapping("/exchange")
    public ResponseEntity<String> exchange(@RequestBody ExchangeRequest exchangeRequest, HttpServletRequest request) throws AccountNotFoundException, ConstraintException, UserNotFoundException, UnauthorizedException, NotPositiveNumericException, InsufficientFundsException {

        TransactionHistoryDTO transaction = transactionService.exchange(exchangeRequest.getInitializerAccountId(), exchangeRequest.getTargetAccountId(), exchangeRequest.getAmount(), request);
        if (transactionService.checkIfOwnerIsDifferent(exchangeRequest.getInitializerAccountId(), exchangeRequest.getTargetAccountId())) {
            return ResponseEntity.ok("Exchange completed successfully! You have exchanged " + exchangeRequest.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " from your " + transaction.getInitializerAccount().getCurrency() + " account to the " + transactionService.getAccount(exchangeRequest.getTargetAccountId()).get().getCurrency() + " account of " + transactionService.getAccount(exchangeRequest.getTargetAccountId()).get().getUser().getFirstName() + " " + transactionService.getAccount(exchangeRequest.getTargetAccountId()).get().getUser().getLastName());

        }

        return ResponseEntity.ok("Exchange completed successfully! You have exchanged " + exchangeRequest.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " from your " + transaction.getInitializerAccount().getCurrency() + " to your " + transaction.getTargetAccount().getCurrency() + " account at a rate of 1 " + transaction.getInitializerAccount().getCurrency() + " for " + FormattedDouble.getFormattedDouble(transaction.getExchangeRate()) + " " + transaction.getTargetAccount().getCurrency());

    }


}