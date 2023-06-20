//package com.ing.hubs.services;
//
//
//import com.ing.hubs.exceptions.*;
//import com.ing.hubs.models.*;
//import com.ing.hubs.repositories.ExchangeRateRepository;
//import com.ing.hubs.repositories.TransactionRepository;
//import com.ing.hubs.repositories.UserRepository;
//import com.ing.hubs.security.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class TransactionServiceTest {
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ExchangeRateRepository exchangeRateRepository;
//
//    @Mock
//    private AccountService accountService;
//
//    private TransactionService transactionService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        transactionService = new TransactionService(transactionRepository, jwtService, userRepository,
//                exchangeRateRepository, accountService);
//    }
//
//    @Test
//    public void createTransaction_SuccessfulTransaction() throws AccountNotFoundException, NotPositiveNumericException, UserNotFoundException, UnauthorizedException, InsufficientFundsException, ConstraintException {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setUser(user);
//        targetAccount.setBalance(50.0);
//        targetAccount.setCurrency("EUR");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(transactionRequest.getTargetAccountId())).thenReturn(Optional.of(targetAccount));
//
//        Transaction transaction = transactionService.createTransaction(transactionRequest, request);
//
//        assertEquals(transactionRequest.getAmount(), transaction.getAmount());
//        assertEquals(initializerAccount.getCurrency(), transaction.getCurrency());
//        assertEquals(transactionRequest.getType(), transaction.getType());
//        assertEquals(initializerAccount, transaction.getInitializerAccount());
//        assertEquals(targetAccount, transaction.getTargetAccount());
//
//        verify(accountService).updateBalance(initializerAccount, 0 - transaction.getAmount());
//        verify(accountService).updateBalance(targetAccount, transaction.getAmount());
//    }
//
//    @Test
//    public void createTransaction_NegativeAmount_ShouldThrowNotPositiveNumericException() {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(-100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initializerAccount));
//
//        assertThrows(NotPositiveNumericException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_UserNotAuthorized_ShouldThrowUnauthorizedException(){
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(new User());
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setUser(user);
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(transactionRequest.getTargetAccountId())).thenReturn(Optional.of(targetAccount));
//
//        assertThrows(UnauthorizedException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_InsufficientFunds_ShouldThrowInsufficientFundsException() {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(300.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setUser(user);
//        targetAccount.setBalance(50.0);
//        targetAccount.setCurrency("EUR");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(transactionRequest.getTargetAccountId())).thenReturn(Optional.of(targetAccount));
//
//        assertThrows(InsufficientFundsException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_UserNotFound_ShouldThrowUserNotFoundException() {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_InitializerAccountNotFound_ShouldThrowAccountNotFoundException(){
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.empty());
//
//        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_TargetAccountNotFound_ShouldThrowAccountNotFoundException() {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("sendMoney");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(transactionRequest.getTargetAccountId())).thenReturn(Optional.empty());
//
//        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void createTransaction_IncorrectTransactionType_ThrowsConstraintException() {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setInitializerAccountId(1);
//        transactionRequest.setTargetAccountId(2);
//        transactionRequest.setType("invalidType");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initAccount = new Account();
//        initAccount.setUser(user);
//        initAccount.setBalance(200.0);
//        initAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setUser(user);
//        targetAccount.setBalance(50.0);
//        targetAccount.setCurrency("EUR");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(transactionRequest.getInitializerAccountId())).thenReturn(Optional.of(initAccount));
//        when(accountService.findAccount(transactionRequest.getTargetAccountId())).thenReturn(Optional.of(targetAccount));
//
//        assertThrows(ConstraintException.class, () -> transactionService.createTransaction(transactionRequest, request));
//    }
//
//    @Test
//    public void getTransactionHistory_SuccessfulHistory() throws UserNotFoundException, AccountNotFoundException, ConstraintException, UnauthorizedException {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//
//        Account account = new Account();
//        account.setId(1);
//        account.setUser(user);
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(1)).thenReturn(Optional.of(account));
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction());
//        transactions.add(new Transaction());
//        when(transactionRepository.findAllByTargetAccount(account)).thenReturn(transactions);
//        when(transactionRepository.findAllByInitializerAccount(account)).thenReturn(transactions);
//        List<Transaction> result = transactionService.getTransactionHistory(1, request);
//
//        assertEquals(4, result.size());
//    }
//
//    @Test
//    public void getTransactionHistory_UserNotAuthorized_ShouldThrowUnauthorizedException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//
//        Account account = new Account();
//        account.setId(1);
//        account.setUser(new User());
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(1)).thenReturn(Optional.of(account));
//
//        assertThrows(UnauthorizedException.class, () -> transactionService.getTransactionHistory(1, request));
//    }
//
//    @Test
//    public void getTransactionHistory_UserNotFound_ShouldThrowUserNotFoundException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> transactionService.getTransactionHistory(1, request));
//    }
//
//    @Test
//    public void getTransactionHistory_NoTransactions_ShouldThrowConstraintException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//
//        Account account = new Account();
//        account.setId(1);
//        account.setUser(user);
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(1)).thenReturn(Optional.of(account));
//        when(transactionRepository.findAllByTargetAccount(account)).thenReturn(Collections.emptyList());
//        when(transactionRepository.findAllByInitializerAccount(account)).thenReturn(Collections.emptyList());
//
//        assertThrows(ConstraintException.class, () -> transactionService.getTransactionHistory(1, request));
//    }
//
//    @Test
//    public void exchange_SuccessfulExchange() throws AccountNotFoundException, NotPositiveNumericException, ConstraintException, UserNotFoundException, UnauthorizedException {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(1);
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setId(2);
//        targetAccount.setUser(user);
//        targetAccount.setBalance(100.0);
//        targetAccount.setCurrency("EUR");
//
//        ExchangeRate exchangeRate = new ExchangeRate();
//        exchangeRate.setInputCurrency("RON");
//        exchangeRate.setOutputCurrency("EUR");
//        exchangeRate.setRate(0.9f);
//
//        when(accountService.findAccount(initializerAccount.getId())).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(targetAccount.getId())).thenReturn(Optional.of(targetAccount));
//        when(exchangeRateRepository.findByInputCurrencyAndOutputCurrency("RON", "EUR")).thenReturn(Optional.of(exchangeRate));
//
//        TransactionHistoryDTO result = transactionService.exchange(1, 2, 100.0, request);
//
//        assertNotNull(result);
//        assertEquals("Exchange", result.getType());
//        assertNotNull(result.getDate());
//        assertEquals("EUR", result.getCurrency());
//        assertEquals(initializerAccount, result.getInitializerAccount());
//        assertEquals(targetAccount, result.getTargetAccount());
//
//        verify(transactionRepository).save(any(Transaction.class));
//    }
//
//    @Test
//    public void exchange_InvalidExchangePair_ShouldThrowConstraintException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(1);
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setId(2);
//        targetAccount.setUser(user);
//        targetAccount.setBalance(50.0);
//        targetAccount.setCurrency("EUR");
//
//        when(accountService.findAccount(1)).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(2)).thenReturn(Optional.of(targetAccount));
//        when(exchangeRateRepository.findByInputCurrencyAndOutputCurrency("RON", "EUR")).thenReturn(Optional.empty());
//
//        assertThrows(ConstraintException.class, () -> transactionService.exchange(1, 2, 100.0, request));
//    }
//
//    @Test
//    public void exchange_InvalidInitializerAccount_ShouldThrowAccountNotFoundException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(user);
//        initializerAccount.setCurrency("RON");
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(1)).thenReturn(Optional.of(initializerAccount));
//
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//
//        assertThrows(AccountNotFoundException.class, () -> {
//            transactionService.exchange(initializerAccountId, targetAccountId, amount, request);
//        });
//    }
//
//    @Test
//    public void exchange_InvalidTargetAccount_ShouldThrowAccountNotFoundException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        int initializerAccountId = 1;
//        int invalidTargetAccountId = 200;
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(invalidTargetAccountId)).thenReturn(Optional.empty());
//
//        double amount = 50.0;
//
//        assertThrows(AccountNotFoundException.class, () -> {
//            transactionService.exchange(initializerAccountId, invalidTargetAccountId, amount, request);
//        });
//    }
//
//    @Test
//    public void exchange_NegativeAmount_ShouldThrowNotPositiveNumericException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(user);
//        initializerAccount.setBalance(200.0);
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        targetAccount.setUser(user);
//        targetAccount.setBalance(50.0);
//        targetAccount.setCurrency("EUR");
//
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));
//
//        double negativeAmount = -100.0;
//        assertThrows(NotPositiveNumericException.class, () -> {
//            transactionService.exchange(initializerAccountId, targetAccountId, negativeAmount, request);
//        });
//    }
//
//    @Test
//    public void exchange_UserNotFound_ShouldThrowUserNotFoundException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        assertThrows(UserNotFoundException.class, () -> {
//            transactionService.exchange(initializerAccountId, targetAccountId, amount, request);
//        });
//    }
//
//    @Test
//    public void exchange_Unauthorized_ThrowsUnauthorizedException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername(anyString())).thenReturn("test_username");
//
//        User user = new User();
//        user.setUsername("test_username");
//
//        Account initializerAccount = new Account();
//        initializerAccount.setUser(new User());
//        initializerAccount.setCurrency("RON");
//
//        Account targetAccount = new Account();
//        targetAccount.setUser(new User());
//
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(1)).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(2)).thenReturn(Optional.of(targetAccount));
//
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//
//        assertThrows(UnauthorizedException.class, () -> {
//            transactionService.exchange(initializerAccountId, targetAccountId, amount, request);
//        });
//    }
//
//    @Test
//    public void getAccount_ExistingAccountId_ShouldReturnOptionalWithAccount() {
//        Account account = new Account();
//        when(accountService.findAccount(anyInt())).thenReturn(Optional.of(account));
//        Optional<Account> result = transactionService.getAccount(1);
//        verify(accountService).findAccount(1);
//        assertTrue(result.isPresent());
//        assertEquals(account, result.get());
//    }
//
//    @Test
//    public void getAccount_NonExistingAccountId_ShouldReturnEmptyOptional() {
//        when(accountService.findAccount(anyInt())).thenReturn(Optional.empty());
//        Optional<Account> result = transactionService.getAccount(1);
//        verify(accountService).findAccount(1);
//        assertFalse(result.isPresent());
//    }
//}