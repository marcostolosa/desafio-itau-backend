package br.com.itau.desafio.controller;

import br.com.itau.desafio.model.Transaction;
import br.com.itau.desafio.model.Statistics;
import br.com.itau.desafio.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody Transaction transaction) {
        if (transaction.getDataHora() == null || transaction.getValor() < 0) {
            return ResponseEntity.unprocessableEntity().build();
        }
        boolean accepted = transactionService.addTransaction(transaction);
        if (!accepted) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deleteAllTransactions() {
        transactionService.clearAllTransactions();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<Statistics> getStatistics() {
        Statistics stats = transactionService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}