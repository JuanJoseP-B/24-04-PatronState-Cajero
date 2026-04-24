package com.atm.logic;

import com.atm.logic.state.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ATMContext {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ATMState currentState;
    
    // Pre-loaded states
    private final ATMState idleState;
    private final ATMState cardInsertedState;
    private final ATMState pinValidationState;
    private final ATMState transactionState;
    private final ATMState dispensingState;

    private String lastMessage;
    private boolean lastActionSuccess;
    private double balance = 100000.0; // Initial balance for simulation

    public ATMContext() {
        // Pre-loading instances for instant transitions
        this.idleState = new IdleState();
        this.cardInsertedState = new CardInsertedState();
        this.pinValidationState = new PinValidationState();
        this.transactionState = new TransactionState();
        this.dispensingState = new DispensingState();

        // Initial state
        this.currentState = idleState;
        this.lastMessage = "Welcome to the ATM. Please insert your card.";
        this.lastActionSuccess = true;
    }

    // Delegation methods
    public void insertCard() {
        currentState.insertCard(this);
    }

    public void ejectCard() {
        currentState.ejectCard(this);
    }

    public void enterPin(String pin) {
        currentState.enterPin(this, pin);
    }

    public void withdrawCash(double amount) {
        currentState.withdrawCash(this, amount);
    }

    // Getters and Setters
    public ATMState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ATMState currentState) {
        this.currentState = currentState;
        
        // Anti-Lock Mechanism: If we enter DispensingState, auto-transition to Idle after 4 seconds
        if (currentState instanceof DispensingState) {
            scheduler.schedule(() -> {
                System.out.println("Timer: Dispensing finished. Returning to IdleState.");
                this.currentState = idleState;
                this.lastMessage = "Sesión finalizada. Gracias por usar nuestro cajero.";
                this.lastActionSuccess = true;
            }, 4, TimeUnit.SECONDS);
        }
    }

    public ATMState getIdleState() { return idleState; }
    public ATMState getCardInsertedState() { return cardInsertedState; }
    public ATMState getPinValidationState() { return pinValidationState; }
    public ATMState getTransactionState() { return transactionState; }
    public ATMState getDispensingState() { return dispensingState; }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isLastActionSuccess() {
        return lastActionSuccess;
    }

    public void setLastActionSuccess(boolean lastActionSuccess) {
        this.lastActionSuccess = lastActionSuccess;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public void reset() {
        this.balance = 100000.0;
        this.currentState = idleState;
        this.lastMessage = "Sistema reiniciado. Bienvenido.";
        this.lastActionSuccess = true;
    }
}
