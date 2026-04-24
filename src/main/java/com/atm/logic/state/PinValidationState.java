package com.atm.logic.state;

import com.atm.logic.ATMContext;

public class PinValidationState implements ATMState {

    @Override
    public void insertCard(ATMContext context) {
        context.setLastMessage("Card already inserted.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void ejectCard(ATMContext context) {
        System.out.println("Card ejected. Returning to IdleState.");
        context.setCurrentState(context.getIdleState());
        context.setLastMessage("Card ejected.");
        context.setLastActionSuccess(true);
    }

    @Override
    public void enterPin(ATMContext context, String pin) {
        System.out.println("Validating PIN...");
        if ("1234".equals(pin)) {
            context.setCurrentState(context.getTransactionState());
            context.setLastMessage("PIN accepted. Choose an option.");
            context.setLastActionSuccess(true);
        } else {
            context.setLastMessage("Incorrect PIN. Try again.");
            context.setLastActionSuccess(false);
        }
    }

    @Override
    public void withdrawCash(ATMContext context, double amount) {
        context.setLastMessage("Please enter your PIN first.");
        context.setLastActionSuccess(false);
    }

    @Override
    public String getStateName() {
        return "PinValidationState";
    }
}
