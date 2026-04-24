package com.atm.logic;

public class StateTestRunner {
    public static void main(String[] args) {
        ATMContext context = new ATMContext();
        
        System.out.println("Initial State: " + context.getCurrentState().getStateName());
        
        System.out.println("\n--- Attempting to withdraw without card ---");
        context.withdrawCash(100);
        System.out.println("Message: " + context.getLastMessage() + " | Success: " + context.isLastActionSuccess());
        
        System.out.println("\n--- Inserting Card ---");
        context.insertCard();
        System.out.println("Current State: " + context.getCurrentState().getStateName());
        System.out.println("Message: " + context.getLastMessage());
        
        System.out.println("\n--- Entering wrong PIN ---");
        context.enterPin("0000");
        System.out.println("Current State: " + context.getCurrentState().getStateName());
        System.out.println("Message: " + context.getLastMessage() + " | Success: " + context.isLastActionSuccess());
        
        System.out.println("\n--- Entering correct PIN (1234) ---");
        context.enterPin("1234");
        System.out.println("Current State: " + context.getCurrentState().getStateName());
        System.out.println("Message: " + context.getLastMessage());
        
        System.out.println("\n--- Withdrawing Cash (500) ---");
        context.withdrawCash(500);
        System.out.println("Current State: " + context.getCurrentState().getStateName());
        System.out.println("Message: " + context.getLastMessage());
        
        System.out.println("\n--- Attempting action while dispensing ---");
        context.insertCard();
        System.out.println("Message: " + context.getLastMessage() + " | Success: " + context.isLastActionSuccess());
    }
}
