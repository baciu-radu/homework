//package com.ing.hubs.services;
//
//import com.ing.hubs.exceptions.*;
//import com.ing.hubs.models.*;
//import com.ing.hubs.repositories.RequestRepository;
//import com.ing.hubs.repositories.UserRepository;
//import com.ing.hubs.security.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class RequestMoneyServiceTest {
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private RequestRepository requestRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AccountService accountService;
//
//    @Mock
//    private TransactionService transactionService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    private RequestMoneyService requestMoneyService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        requestMoneyService = new RequestMoneyService(jwtService, requestRepository, userRepository, accountService, transactionService);
//    }
//
//    @Test
//    void createMoneyRequest_UserExistsAndIsAuthorized_ShouldCreateMoneyRequest() throws UserNotFoundException, AccountNotFoundException, UnauthorizedException, HttpMessageNotReadableException {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setInitializerAccountId(initializerAccountId);
//        transactionRequest.setTargetAccountId(targetAccountId);
//        transactionRequest.setAmount(amount);
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(user);
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        when(accountService.findAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));
//
//        Request savedRequest = new Request();
//        when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);
//
//        Request createdRequest = requestMoneyService.createMoneyRequest(transactionRequest, request);
//        assertNotNull(createdRequest);
//        assertEquals(RequestStatus.PENDING, createdRequest.getRequestStatus());
//        assertEquals(amount, createdRequest.getAmount());
//        assertEquals(targetAccount, createdRequest.getSenderAccount());
//        assertEquals(initializerAccount, createdRequest.getReceiverAccount());
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).findAccount(initializerAccountId);
//        verify(accountService).findAccount(targetAccountId);
//        verify(requestRepository).save(any(Request.class));
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void createMoneyRequest_UserDoesNotExist_ShouldThrowUserNotFoundException()  {
//        TransactionRequest transactionRequest = new TransactionRequest();
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> requestMoneyService.createMoneyRequest(transactionRequest, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void createMoneyRequest_InitializerAccountNotFound_ShouldThrowAccountNotFoundException() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setInitializerAccountId(initializerAccountId);
//        transactionRequest.setTargetAccountId(targetAccountId);
//        transactionRequest.setAmount(amount);
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.empty());
//
//        assertThrows(AccountNotFoundException.class, () -> requestMoneyService.createMoneyRequest(transactionRequest, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).findAccount(initializerAccountId);
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void createMoneyRequest_TargetAccountNotFound_ShouldThrowAccountNotFoundException() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setInitializerAccountId(initializerAccountId);
//        transactionRequest.setTargetAccountId(targetAccountId);
//        transactionRequest.setAmount(amount);
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(user);
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//        when(accountService.findAccount(targetAccountId)).thenReturn(Optional.empty());
//
//        assertThrows(AccountNotFoundException.class, () -> requestMoneyService.createMoneyRequest(transactionRequest, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).findAccount(initializerAccountId);
//        verify(accountService).findAccount(targetAccountId);
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void createMoneyRequest_UserNotAuthorized_ShouldThrowUnauthorizedException() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setInitializerAccountId(initializerAccountId);
//        transactionRequest.setTargetAccountId(targetAccountId);
//        transactionRequest.setAmount(amount);
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(new User());
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        when(accountService.findAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));
//
//        assertThrows(UnauthorizedException.class, () -> requestMoneyService.createMoneyRequest(transactionRequest, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).findAccount(initializerAccountId);
//        verify(accountService).findAccount(targetAccountId);
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void createMoneyRequest_ExceptionThrownWhileSavingRequest_ShouldPropagateException() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        double amount = 100.0;
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setInitializerAccountId(initializerAccountId);
//        transactionRequest.setTargetAccountId(targetAccountId);
//        transactionRequest.setAmount(amount);
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        initializerAccount.setUser(user);
//        when(accountService.findAccount(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        when(accountService.findAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));
//        when(requestRepository.save(any(Request.class))).thenThrow(RuntimeException.class);
//
//        assertThrows(RuntimeException.class, () -> requestMoneyService.createMoneyRequest(transactionRequest, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).findAccount(initializerAccountId);
//        verify(accountService).findAccount(targetAccountId);
//        verify(requestRepository).save(any(Request.class));
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void viewPendingRequests_UserExistsAndHasAccounts_ShouldReturnListOfPendingRequests() throws UserNotFoundException, AccountNotFoundException, UnauthorizedException {
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account account1 = new Account();
//        account1.setUser(user);
//        Account account2 = new Account();
//        account2.setUser(user);
//        List<Account> accounts = new ArrayList<>();
//        accounts.add(account1);
//        accounts.add(account2);
//        when(accountService.getAllAccountsOfUser(user.getId())).thenReturn(accounts);
//
//        Request request1 = new Request();
//        request1.setRequestStatus(RequestStatus.PENDING);
//        Request request2 = new Request();
//        request2.setRequestStatus(RequestStatus.PENDING);
//        List<Request> expectedRequests = new ArrayList<>();
//        expectedRequests.add(request1);
//        expectedRequests.add(request2);
//        when(requestRepository.findAllBySenderAccountAndRequestStatus(any(Account.class), eq(RequestStatus.PENDING))).thenReturn(expectedRequests);
//
//        List<Request> result = requestMoneyService.viewPendingRequests(request);
//
//        assertNotNull(result);
//        assertEquals(2 * expectedRequests.size(), result.size());
//        assertTrue(result.containsAll(expectedRequests));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).getAllAccountsOfUser(user.getId());
//        verify(requestRepository, times(accounts.size())).findAllBySenderAccountAndRequestStatus(any(Account.class), eq(RequestStatus.PENDING));
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void viewPendingRequests_UserDoesNotExist_ShouldThrowUserNotFoundException() {
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> requestMoneyService.viewPendingRequests(request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void viewPendingRequests_NoAccountsFound_ShouldThrowAccountNotFoundException() {
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        List<Account> accounts = new ArrayList<>();
//        when(accountService.getAllAccountsOfUser(user.getId())).thenReturn(accounts);
//
//        assertThrows(AccountNotFoundException.class, () -> requestMoneyService.viewPendingRequests(request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).getAllAccountsOfUser(user.getId());
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void viewPendingRequests_UserNotAuthorized_ShouldThrowUnauthorizedException() {
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account account1 = new Account();
//        account1.setUser(new User());
//        Account account2 = new Account();
//        account2.setUser(new User());
//        List<Account> accounts = new ArrayList<>();
//        accounts.add(account1);
//        accounts.add(account2);
//        when(accountService.getAllAccountsOfUser(user.getId())).thenReturn(accounts);
//
//        assertThrows(UnauthorizedException.class, () -> requestMoneyService.viewPendingRequests(request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).getAllAccountsOfUser(user.getId());
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void viewPendingRequests_ExceptionThrownInFindAllBySenderAccountAndRequestStatus_ShouldPropagateException() {
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Account account1 = new Account();
//        account1.setUser(user);
//        Account account2 = new Account();
//        account2.setUser(user);
//        List<Account> accounts = new ArrayList<>();
//        accounts.add(account1);
//        accounts.add(account2);
//        when(accountService.getAllAccountsOfUser(user.getId())).thenReturn(accounts);
//
//        when(requestRepository.findAllBySenderAccountAndRequestStatus(any(Account.class), eq(RequestStatus.PENDING))).thenThrow(RuntimeException.class);
//
//        assertThrows(RuntimeException.class, () -> requestMoneyService.viewPendingRequests(request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(accountService).getAllAccountsOfUser(user.getId());
//        verify(requestRepository, times(accounts.size()/2)).findAllBySenderAccountAndRequestStatus(any(Account.class), eq(RequestStatus.PENDING));
//        verifyNoMoreInteractions(jwtService, userRepository, accountService, requestRepository);
//    }
//
//    @Test
//    void approveRequest_UserExistsAndIsAuthorized_ShouldApproveRequestAndCreateTransaction() throws UserNotFoundException, RequestNotFoundException, AccountNotFoundException, UnauthorizedException, InsufficientFundsException, ConstraintException, NotPositiveNumericException, InsufficientFundsException, NotPositiveNumericException {
//        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
//        Integer requestId = 1;
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Request test_request = new Request();
//        test_request.setSenderAccount(new Account());
//        test_request.getSenderAccount().setUser(user);
//        test_request.setReceiverAccount(new Account());
//        when(requestRepository.findById(requestId)).thenReturn(Optional.of(test_request));
//
//        TransactionRequest transactionRequest = new TransactionRequest();
//        transactionRequest.setAmount(100.0);
//        transactionRequest.setType("sendMoney");
//        transactionRequest.setInitializerAccountId(test_request.getSenderAccount().getId());
//        transactionRequest.setTargetAccountId(test_request.getReceiverAccount().getId());
//        when(transactionService.createTransaction(transactionRequest, request)).thenReturn(new Transaction());
//
//        Request result = requestMoneyService.approveRequest(requestId, request);
//
//        assertNotNull(result);
//        assertEquals(RequestStatus.APPROVED, result.getRequestStatus());
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(requestRepository).findById(requestId);
//        verify(requestRepository).save(test_request);
//        verifyNoMoreInteractions(jwtService, userRepository, requestRepository);
//    }
//
//    @Test
//    void approveRequest_UserDoesNotExist_ShouldThrowUserNotFoundException() {
//        Integer requestId = 1;
//
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> requestMoneyService.approveRequest(requestId, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verifyNoMoreInteractions(jwtService, userRepository, requestRepository, transactionService);
//    }
//
//    @Test
//    void approveRequest_RequestNotFound_ShouldThrowRequestNotFoundException() {
//        Integer requestId = 1;
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());
//
//        assertThrows(RequestNotFoundException.class, () -> requestMoneyService.approveRequest(requestId, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(requestRepository).findById(requestId);
//        verifyNoMoreInteractions(jwtService, userRepository, requestRepository, transactionService);
//    }
//
//    @Test
//    void approveRequest_UserNotAuthorized_ShouldThrowUnauthorizedException() {
//        Integer requestId = 1;
//
//        User user = new User();
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//
//        Request test_request = new Request();
//        test_request.setSenderAccount(new Account());
//        test_request.getSenderAccount().setUser(new User());
//        test_request.setReceiverAccount(new Account());
//        when(requestRepository.findById(requestId)).thenReturn(Optional.of(test_request));
//
//        assertThrows(UnauthorizedException.class, () -> requestMoneyService.approveRequest(requestId, request));
//
//        verify(jwtService).extractJwtFromRequest(request);
//        verify(jwtService).extractUsername("jwt_token");
//        verify(userRepository).findByUsername("test_username");
//        verify(requestRepository).findById(requestId);
//        verifyNoMoreInteractions(jwtService, userRepository, requestRepository, transactionService);
//    }
//
//    @Test
//    void getAccount_AccountExists_ShouldReturnAccount() {
//        int accountId = 1;
//        Account account = new Account();
//        when(accountService.findAccount(accountId)).thenReturn(Optional.of(account));
//        Account result = requestMoneyService.getAccount(accountId);
//        assertEquals(account, result);
//        verify(accountService).findAccount(accountId);
//        verifyNoMoreInteractions(accountService);
//    }
//
//    @Test
//    void getAccount_AccountDoesNotExist_ShouldThrowNoSuchElementException() {
//        int accountId = 1;
//        when(accountService.findAccount(accountId)).thenReturn(Optional.empty());
//        assertThrows(NoSuchElementException.class, () -> requestMoneyService.getAccount(accountId));
//        verify(accountService).findAccount(accountId);
//        verifyNoMoreInteractions(accountService);
//    }
//}