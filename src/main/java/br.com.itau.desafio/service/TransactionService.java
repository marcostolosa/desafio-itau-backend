package br.com.itau.desafio.service;

import br.com.itau.desafio.model.Transaction;
import br.com.itau.desafio.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    @Value("${transaction.time-window-seconds:60}")
    private long timeWindowSeconds;

    public boolean addTransaction(Transaction transaction) {
        OffsetDateTime now = OffsetDateTime.now();
        
        // Validação de valor e data
        if (transaction.getValor() < 0 || transaction.getDataHora() == null || transaction.getValor() == null) {
            logger.warn("Tentativa de adicionar transação com valor negativo ou inválido: {}", transaction);
            return false;
        }
        // Não pode ser no futuro
        if (transaction.getDataHora().isAfter(now.plusSeconds(1))) {
            logger.warn("Tentativa de adicionar transação muito no futuro: {}", transaction);
            return false;
        }
        // Tem que ser dentro dos últimos 60 segundos
        if (transaction.getDataHora().isBefore(now.minusSeconds(60))) {
            return false;
        }
    
        transactions.add(transaction);
        logger.info("Transação adicionada: {}", transaction);
        return true;
    }
    
    public void clearAllTransactions() {
        transactions.clear();
        logger.info("Todas as transações foram removidas.");
    }

    public Statistics getStatistics() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusSeconds(timeWindowSeconds);
        List<Transaction> lastWindowTransactions = transactions.stream()
                .filter(t -> !t.getDataHora().isBefore(cutoff))
                .toList();

        if (lastWindowTransactions.isEmpty()) {
            return new Statistics(0, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics stats = lastWindowTransactions.stream()
                .mapToDouble(Transaction::getValor)
                .summaryStatistics();

        return new Statistics(
                stats.getCount(),
                stats.getSum(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax()
        );
    }
}