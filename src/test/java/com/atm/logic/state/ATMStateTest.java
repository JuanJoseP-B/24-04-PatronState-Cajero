package com.atm.logic.state;

import com.atm.logic.ATMContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ATMStateTest {

    private ATMContext context;

    @BeforeEach
    void setUp() {
        context = new ATMContext();
    }

    @Test
    void testInitialStateIsIdle() {
        assertEquals("IdleState", context.getCurrentState().getStateName());
    }

    @Test
    void testInsertCardTransitionsToCardInserted() {
        context.insertCard();
        assertEquals("CardInsertedState", context.getCurrentState().getStateName());
        assertTrue(context.isLastActionSuccess());
    }

    @Test
    void testEnterPinTransitionsToTransactionState() {
        context.insertCard();
        context.enterPin("1234");
        assertEquals("TransactionState", context.getCurrentState().getStateName());
        assertTrue(context.isLastActionSuccess());
    }

    @Test
    void testWithdrawCashTransitionsToDispensing() {
        context.insertCard();
        context.enterPin("1234");
        context.withdrawCash(100.0);
        assertEquals("DispensingState", context.getCurrentState().getStateName());
    }

    @Test
    void testIllegalActionFails() {
        context.withdrawCash(100.0); // Should fail in IdleState
        assertEquals("IdleState", context.getCurrentState().getStateName());
        assertFalse(context.isLastActionSuccess());
    }
}
