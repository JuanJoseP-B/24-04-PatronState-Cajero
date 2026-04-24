package com.atm.logic.model;

public class ATMResponseDTO {
    private String currentState;
    private String message;
    private boolean success;
    private double balance;

    public ATMResponseDTO(String currentState, String message, boolean success, double balance) {
        this.currentState = currentState;
        this.message = message;
        this.success = success;
        this.balance = balance;
    }

    // Getters for JSON serialization
    public String getCurrentState() {
        return currentState;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getBalance() {
        return balance;
    }
}
