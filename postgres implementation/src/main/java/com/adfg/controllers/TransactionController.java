package com.adfg.controllers;

import com.adfg.domain.Transaction;
import com.adfg.interfaces.TransactionService;
import com.adfg.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/transaction")
public class TransactionController {

    private final TransactionService service;
    @Autowired
    public TransactionController(TransactionServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity addNewTransaction (@RequestBody Transaction transaction) {
        return new ResponseEntity(service.transactionProceed(transaction), HttpStatus.OK);
    }

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity getCardReport (@RequestParam Long cardId, int hours) {
        return new ResponseEntity(service.getCardReport(cardId, hours), HttpStatus.OK);
    }


}
