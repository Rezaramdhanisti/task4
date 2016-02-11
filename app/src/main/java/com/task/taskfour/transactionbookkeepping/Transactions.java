package com.task.taskfour.transactionbookkeepping;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Personal on 2/4/2016.
 */
public class Transactions {
    @SerializedName("transactions")

    public List<TransactionItem> transactions;

    public List<TransactionItem> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionItem> transactions) {
        this.transactions = transactions;
    }

    public Transactions(List<TransactionItem> transactions) {
        this.transactions = transactions;
    }

    public class TransactionItem {
        private String status;
        private String message;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
