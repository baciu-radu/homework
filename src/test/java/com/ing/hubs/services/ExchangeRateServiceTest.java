//package com.ing.hubs.services;
//
//
//import com.ing.hubs.models.ExchangeRate;
//import com.ing.hubs.repositories.ExchangeRateRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class ExchangeRateServiceTest {
//
//    @Mock
//    private ExchangeRateRepository exchangeRateRepository;
//
//    @InjectMocks
//    private ExchangeRateService exchangeRateService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getExchangeRateRepositoryForCurrencies() {
//        String inputCurrency = "RON";
//        String outputCurrency = "EUR";
//        ExchangeRate exchangeRate = new ExchangeRate(1,inputCurrency, outputCurrency, 0.2f);
//        when(exchangeRateRepository.findByInputCurrencyAndOutputCurrency(inputCurrency, outputCurrency))
//                .thenReturn(Optional.of(exchangeRate));
//        ExchangeRate result = exchangeRateService.getExchangeRateRepositoryForCurrencies(inputCurrency, outputCurrency);
//        assertNotNull(result);
//        assertEquals(exchangeRate, result);
//        verify(exchangeRateRepository, times(1)).findByInputCurrencyAndOutputCurrency(inputCurrency, outputCurrency);
//    }
//
//    @Test
//    public void getExchangeRateRepositoryForCurrencies_ExchangeRateNotFound() {
//        String inputCurrency = "RON";
//        String outputCurrency = "EUR";
//        when(exchangeRateRepository.findByInputCurrencyAndOutputCurrency(inputCurrency, outputCurrency))
//                .thenReturn(Optional.empty());
//        assertThrows(NoSuchElementException.class, () -> exchangeRateService.getExchangeRateRepositoryForCurrencies(inputCurrency, outputCurrency));
//        verify(exchangeRateRepository, times(1)).findByInputCurrencyAndOutputCurrency(inputCurrency, outputCurrency);
//    }
//}