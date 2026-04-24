package com.atm.logic.state;

import com.atm.logic.ATMContext;

public class DispensingState implements ATMState {

    @Override
    public void insertCard(ATMContext context) {
        context.setLastMessage("Please wait, dispensing cash.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void ejectCard(ATMContext context) {
        context.setLastMessage("Please wait, dispensing cash.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void enterPin(ATMContext context, String pin) {
        context.setLastMessage("Please wait, dispensing cash.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void withdrawCash(ATMContext context, double amount) {
        context.setLastMessage("Transaction in progress.");
        context.setLastActionSuccess(false);
    }

    @Override
    public String getStateName() {
        return "DispensingState";
    }
    
    // In a real scenario, this would have a 'complete' method called by a timer or hardware sensor
    public void completeDispensing(ATMContext context) {
        System.out.println("Dispensing complete. Returning to TransactionState or Ejecting...");
        context.setCurrentState(context.getIdleState()); // Simple flow: ejects and goes idle
        context.setLastMessage("Cash dispensed. Thank you for using our ATM.");
        context.setLastActionSuccess(true);
    }
}
