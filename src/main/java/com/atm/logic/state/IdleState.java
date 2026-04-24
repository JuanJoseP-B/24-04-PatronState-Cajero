package com.atm.logic.state;

import com.atm.logic.ATMContext;

public class IdleState implements ATMState {

    @Override
    public void insertCard(ATMContext context) {
        System.out.println("Card inserted. Switching to CardInsertedState.");
        context.setCurrentState(context.getCardInsertedState());
        context.setLastMessage("Card inserted. Please enter your PIN.");
        context.setLastActionSuccess(true);
    }

    @Override
    public void ejectCard(ATMContext context) {
        context.setLastMessage("No card to eject.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void enterPin(ATMContext context, String pin) {
        context.setLastMessage("Insert card first.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void withdrawCash(ATMContext context, double amount) {
        context.setLastMessage("Insert card first.");
        context.setLastActionSuccess(false);
    }

    @Override
    public String getStateName() {
        return "IdleState";
    }
}
