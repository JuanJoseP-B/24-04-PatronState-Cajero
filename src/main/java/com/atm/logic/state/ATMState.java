package com.atm.logic.state;

import com.atm.logic.ATMContext;

public interface ATMState {
    void insertCard(ATMContext context);
    void ejectCard(ATMContext context);
    void enterPin(ATMContext context, String pin);
    void withdrawCash(ATMContext context, double amount);
    String getStateName();
}
