package com.ing.hubs.util;

import com.ing.hubs.models.Transaction;

public class TransactionViewFormatter {
    public static String formatViews(String response, int initializerId, int targetId, int accountId, Transaction transaction, double rate) {


//      send money
        if (transaction.getInitializerAccount().getId() == accountId)
        {
//          both account are yours
            if (transaction.getInitializerAccount().getUser().getId() == transaction.getTargetAccount().getUser().getId()) {
                if (transaction.getInitializerAccount().getCurrency().equals(transaction.getTargetAccount().getCurrency())) {
                    response += ("\nIn " + transaction.getDate() + " you deposited " + transaction.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " to your " + transaction.getTargetAccount().getCurrency() + " account");
                } else {
                    response += ("\nIn " + transaction.getDate() + " you sent " + transaction.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " to your " + transaction.getTargetAccount().getCurrency() + " " + " account at an exchange rate of " + FormattedDouble.getFormattedDouble(rate) + " " + transaction.getInitializerAccount().getCurrency() + " for 1 " + transaction.getTargetAccount().getCurrency());
                }
            }

//          2 users same currency
            else if (transaction.getInitializerAccount().getCurrency().equals(transaction.getTargetAccount().getCurrency())) {

                response += ("\nIn " + transaction.getDate() + " you sent " + transaction.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " to " + transaction.getTargetAccount().getUser().getFirstName() + " " + transaction.getTargetAccount().getUser().getLastName());

            }
//          2 users different currency
            else {


                response += ("\nIn " + transaction.getDate() + " you sent " + transaction.getAmount() + " " + transaction.getTargetAccount().getCurrency() + " to the " + transaction.getTargetAccount().getCurrency() + " account of " + transaction.getTargetAccount().getUser().getFirstName() + " " + transaction.getTargetAccount().getUser().getLastName() + " at an exchange rate of " + FormattedDouble.getFormattedDouble(rate) + " " + transaction.getInitializerAccount().getCurrency() + " for 1 " + transaction.getTargetAccount().getCurrency());
            }

        }


//      receive money
        if (transaction.getTargetAccount().getId() == accountId && transaction.getTargetAccount().getId() != transaction.getInitializerAccount().getId()) {
//              2 users same currency
            if (transaction.getInitializerAccount().getCurrency().equals(transaction.getTargetAccount().getCurrency())) {
                response += ("\nIn " + transaction.getDate() + " you received " + transaction.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " from " + transaction.getInitializerAccount().getUser().getFirstName() + " " + transaction.getInitializerAccount().getUser().getLastName());
            }
//              2 users different currency
            else
                response += ("\nIn " + transaction.getDate() + " you received " + transaction.getAmount() + " " + transaction.getInitializerAccount().getCurrency() + " from  the " + transaction.getTargetAccount().getCurrency() + " account of " + transaction.getInitializerAccount().getUser().getFirstName() + " " + transaction.getInitializerAccount().getUser().getLastName() + " at an exchange rate of " + FormattedDouble.getFormattedDouble(rate) + " " + transaction.getInitializerAccount().getCurrency() + " for 1 " + transaction.getTargetAccount().getCurrency());

        }
        return response;
    }
}