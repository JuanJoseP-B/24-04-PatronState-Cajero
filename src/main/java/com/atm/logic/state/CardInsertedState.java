package com.atm.logic.state;

import com.atm.logic.ATMContext;

public class CardInsertedState implements ATMState {

    @Override
    public void insertCard(ATMContext context) {
        context.setLastMessage("Card already inserted.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void ejectCard(ATMContext context) {
        System.out.println("Card ejected. Switching to IdleState.");
        context.setCurrentState(context.getIdleState());
        context.setLastMessage("Card ejected successfully.");
        context.setLastActionSuccess(true);
    }

    @Override
    public void enterPin(ATMContext context, String pin) {
        System.out.println("Moving to PinValidationState...");
        context.setCurrentState(context.getPinValidationState());
        context.getCurrentState().enterPin(context, pin);
    }

    @Override
    public void withdrawCash(ATMContext context, double amount) {
        context.setLastMessage("Enter PIN first.");
        context.setLastActionSuccess(false);
    }

    @Override
    public String getStateName() {
        return "CardInsertedState";
    }
}
