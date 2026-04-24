package com.atm.logic.controller;

import com.atm.logic.ATMContext;
import com.atm.logic.model.ATMResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/atm")
@CrossOrigin(origins = "*") // For frontend integration
public class ATMController {

    private final ATMContext atmContext;

    @Autowired
    public ATMController(ATMContext atmContext) {
        this.atmContext = atmContext;
    }

    @GetMapping("/status")
    public ATMResponseDTO getStatus() {
        return createResponse();
    }

    @PostMapping("/insert-card")
    public ATMResponseDTO insertCard() {
        atmContext.insertCard();
        return createResponse();
    }

    @PostMapping("/eject-card")
    public ATMResponseDTO ejectCard() {
        atmContext.ejectCard();
        return createResponse();
    }

    @PostMapping("/enter-pin")
    public ATMResponseDTO enterPin(@RequestBody Map<String, String> request) {
        String pin = request.get("pin");
        atmContext.enterPin(pin);
        return createResponse();
    }

    @PostMapping("/withdraw")
    public ATMResponseDTO withdraw(@RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        atmContext.withdrawCash(amount != null ? amount : 0.0);
        return createResponse();
    }

    @PostMapping("/reset")
    public ATMResponseDTO reset() {
        atmContext.reset();
        return createResponse();
    }

    private ATMResponseDTO createResponse() {
        return new ATMResponseDTO(
            atmContext.getCurrentState().getStateName(),
            atmContext.getLastMessage(),
            atmContext.isLastActionSuccess(),
            atmContext.getBalance()
        );
    }
}
