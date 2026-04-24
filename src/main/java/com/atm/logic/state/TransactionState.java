package com.atm.logic.state;

import com.atm.logic.ATMContext;

public class TransactionState implements ATMState {

    @Override
    public void insertCard(ATMContext context) {
        context.setLastMessage("Card already inserted.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void ejectCard(ATMContext context) {
        System.out.println("Card ejected. Switching to IdleState.");
        context.setCurrentState(context.getIdleState());
        context.setLastMessage("Card ejected. Thank you.");
        context.setLastActionSuccess(true);
    }

    @Override
    public void enterPin(ATMContext context, String pin) {
        context.setLastMessage("PIN already validated.");
        context.setLastActionSuccess(false);
    }

    @Override
    public void withdrawCash(ATMContext context, double amount) {
        System.out.println("Processing withdrawal request for: " + amount);
        if (amount <= 0) {
            context.setLastMessage("Monto inválido.");
            context.setLastActionSuccess(false);
        } else if (amount > context.getBalance()) {
            context.setLastMessage("Saldo insuficiente. Saldo actual: $" + context.getBalance());
            context.setLastActionSuccess(false);
        } else {
            // Business Logic: Deduct balance BEFORE signaling success to UI
            context.deductBalance(amount);
            context.setCurrentState(context.getDispensingState());
            context.setLastMessage("Retiro exitoso. Dispensando $" + amount);
            context.setLastActionSuccess(true);
            
            // In a real system, we would trigger the hardware here
            System.out.println("New Balance: " + context.getBalance());
        }
    }

    @Override
    public String getStateName() {
        return "TransactionState";
    }
}
