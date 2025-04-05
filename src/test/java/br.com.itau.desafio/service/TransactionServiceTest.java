package br.com.itau.desafio.service;

import br.com.itau.desafio.model.Transaction;
import br.com.itau.desafio.model.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private TransactionService service;

    @BeforeEach
    void setup() {
        service = new TransactionService();
    }

    @Test
    void shouldAddTransactionSuccessfully() {
        // Cria a transação para 1 segundo atrás (garantindo que está no range dos últimos 60 segundos)
        Transaction transaction = new Transaction(100.0, OffsetDateTime.now().plusSeconds(1));
    
        // Adiciona a transação
        boolean result = service.addTransaction(transaction);
    
        // Deve aceitar
        assertTrue(result, "Transação deveria ser aceita");
    
        // Agora checa as estatísticas
        Statistics stats = service.getStatistics();
    
        // Como garantimos 1 transação apenas (tudo limpo antes), podemos testar valores exatos
        assertEquals(1, stats.getCount(), "Deveria ter 1 transação registrada");
        assertEquals(100.0, stats.getSum(), 0.001, "Soma deveria ser 100.0");
        assertEquals(100.0, stats.getAvg(), 0.001, "Média deveria ser 100.0");
        assertEquals(100.0, stats.getMin(), 0.001, "Mínimo deveria ser 100.0");
        assertEquals(100.0, stats.getMax(), 0.001, "Máximo deveria ser 100.0");
    }    

    @Test
    void shouldIgnoreOldTransaction() {
        Transaction oldTransaction = new Transaction(150.0, OffsetDateTime.now().minusSeconds(70));
        boolean result = service.addTransaction(oldTransaction);

        assertFalse(result, "Old transaction should be ignored");
        Statistics stats = service.getStatistics();
        assertEquals(0, stats.getCount());
    }

    @Test
    void shouldClearAllTransactions() {
        Transaction transaction = new Transaction(200.0, OffsetDateTime.now());
        service.addTransaction(transaction);

        service.clearAllTransactions();

        Statistics stats = service.getStatistics();
        assertEquals(0, stats.getCount());
    }
}
