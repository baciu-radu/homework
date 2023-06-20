//package com.ing.hubs.services;
//
//import com.ing.hubs.exceptions.*;
//import com.ing.hubs.models.Account;
//import com.ing.hubs.models.AccountBalanceDTO;
//import com.ing.hubs.models.AccountDTO;
//import com.ing.hubs.models.User;
//import com.ing.hubs.repositories.AccountRepository;
//import com.ing.hubs.repositories.UserRepository;
//import com.ing.hubs.security.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class AccountServiceTest {
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private HttpServletRequest request;
//
//    private AccountService accountService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        accountService = new AccountService(jwtService, userRepository, accountRepository);
//    }
//
//
//    @Test
//    public void createAccount_ShouldCreateAccountSuccessfully() throws UserNotFoundException, ConstraintException {
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setCurrency("EUR");
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findByCurrencyAndUserId("EUR", 1)).thenReturn(Optional.empty());
//        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
//        accountService.createAccount(accountDTO, request);
//        verify(accountRepository).save(any(Account.class));
//    }
//
//    @Test
//    public void createAccount_UserNotFound_ShouldThrowUserNotFoundException() {
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setCurrency("EUR");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(accountDTO, request));
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    public void createAccount_ExistingAccount_ShouldThrowConstraintException() {
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setCurrency("EUR");
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        Account existingAccount = new Account();
//        existingAccount.setCurrency("EUR");
//        existingAccount.setUser(user);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findByCurrencyAndUserId("EUR", 1)).thenReturn(Optional.of(existingAccount));
//        assertThrows(ConstraintException.class, () -> accountService.createAccount(accountDTO, request));
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    public void updateBalance_ShouldUpdateBalanceSuccessfully() {
//        Account account = new Account();
//        account.setBalance(100.0);
//        double amount = 50.0;
//        double expectedBalance = 150.0;
//        accountService.updateBalance(account, amount);
//        assertEquals(expectedBalance, account.getBalance());
//        verify(accountRepository).save(account);
//    }
//
//    @Test
//    public void updateBalance_NegativeAmount_ShouldUpdateBalanceSuccessfully() {
//        Account account = new Account();
//        account.setBalance(100.0);
//        double amount = -50.0;
//        double expectedBalance = 50.0;
//        accountService.updateBalance(account, amount);
//        assertEquals(expectedBalance, account.getBalance());
//        verify(accountRepository).save(account);
//    }
//
//    @Test
//    public void getBalanceOne_UserAndAccountMatch_ShouldReturnBalance() throws UserNotFoundException, AccountNotFoundException, UnauthorizedException {
//        int accountId = 1;
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        Account account = new Account();
//        account.setId(accountId);
//        account.setBalance(100.0);
//        account.setUser(user);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
//        double expectedBalance = 100.0;
//        double balance = accountService.getBalanceOne(accountId, request);
//        assertEquals(expectedBalance, balance);
//    }
//
//    @Test
//    public void getBalanceOne_UserNotFound_ShouldThrowUserNotFoundException() {
//        int accountId = 1;
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class, () -> accountService.getBalanceOne(accountId, request));
//    }
//
//    @Test
//    public void getBalanceOne_AccountNotFound_ShouldThrowAccountNotFoundException() {
//        int accountId = 1;
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
//        assertThrows(AccountNotFoundException.class, () -> accountService.getBalanceOne(accountId, request));
//    }
//
//    @Test
//    public void getBalanceOne_UserAndAccountDoNotMatch_ShouldThrowUnauthorizedException() {
//        int accountId = 1;
//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("test_username");
//        Account account = new Account();
//        account.setId(accountId);
//        account.setBalance(100.0);
//        account.setUser(user1);
//        User user2 = new User();
//        user2.setId(2);
//        user2.setUsername("test_other_username");
//
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user2));
//        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
//        assertThrows(UnauthorizedException.class, () -> accountService.getBalanceOne(accountId, request));
//    }
//
//    @Test
//    public void getBalanceAll_UserHasAccounts_ShouldReturnAccountBalances() throws UserNotFoundException, AccountNotFoundException {
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        Account account1 = new Account();
//        account1.setBalance(100.0);
//        account1.setCurrency("RON");
//        Account account2 = new Account();
//        account2.setBalance(200.0);
//        account2.setCurrency("EUR");
//        List<Account> accounts = new ArrayList<>();
//        accounts.add(account1);
//        accounts.add(account2);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findAllByUserId(user.getId())).thenReturn(accounts);
//        List<AccountBalanceDTO> expectedAccountBalances = new ArrayList<>();
//        expectedAccountBalances.add(new AccountBalanceDTO("RON", 100.0));
//        expectedAccountBalances.add(new AccountBalanceDTO("EUR", 200.0));
//        List<AccountBalanceDTO> accountBalances = accountService.getBalanceAll(request);
//        assertEquals(expectedAccountBalances.size(), accountBalances.size());
//        for (int i = 0; i < expectedAccountBalances.size(); i++) {
//            AccountBalanceDTO expectedBalance = expectedAccountBalances.get(i);
//            AccountBalanceDTO actualBalance = accountBalances.get(i);
//            assertEquals(expectedBalance.getBalance(), actualBalance.getBalance());
//            assertEquals(expectedBalance.getCurrency(), actualBalance.getCurrency());
//        }
//    }
//
//    @Test
//    public void getBalanceAll_UserHasNoAccounts_ShouldThrowAccountNotFoundException() {
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findAllByUserId(user.getId())).thenReturn(Collections.emptyList());
//        assertThrows(AccountNotFoundException.class, () -> accountService.getBalanceAll(request));
//    }
//
//    @Test
//    public void getBalanceAll_UserNotFound_ShouldThrowUserNotFoundException() {
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class, () -> accountService.getBalanceAll(request));
//    }
//
//    @Test
//    public void deposit_AmountIsPositive_ShouldIncreaseAccountBalance() throws UserNotFoundException, AccountNotFoundException, NotPositiveNumericException {
//        String currency = "RON";
//        double amount = 100.0;
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        Account account = new Account();
//        account.setId(1);
//        account.setCurrency("RON");
//        account.setBalance(200.0);
//        account.setUser(user);
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findByCurrencyAndUserId(currency, user.getId())).thenReturn(Optional.of(account));
//        accountService.deposit(currency, amount, request);
//        assertEquals(300.0, account.getBalance());
//    }
//
//    @Test
//    public void deposit_AmountIsNegative_ShouldThrowNotPositiveNumericException() {
//        String currency = "RON";
//        double amount = -100.0;
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        assertThrows(NotPositiveNumericException.class, () -> accountService.deposit(currency, amount, request));
//        verifyNoInteractions(userRepository);
//        verifyNoInteractions(accountRepository);
//    }
//
//    @Test
//    public void deposit_AccountNotFound_ShouldThrowAccountNotFoundException() {
//        String currency = "RON";
//        double amount = 100.0;
//        User user = new User();
//        user.setId(1);
//        user.setUsername("test_username");
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.of(user));
//        when(accountRepository.findByCurrencyAndUserId(currency, user.getId())).thenReturn(Optional.empty());
//        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(currency, amount, request));
//    }
//
//    @Test
//    public void deposit_UserNotFound_ShouldThrowUserNotFoundException() {
//        String currency = "RON";
//        double amount = 100.0;
//        when(jwtService.extractJwtFromRequest(request)).thenReturn("jwt_token");
//        when(jwtService.extractUsername("jwt_token")).thenReturn("test_username");
//        when(userRepository.findByUsername("test_username")).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class, () -> accountService.deposit(currency, amount, request));
//    }
//
//    @Test
//    public void checkIfOwnerDifferent_AccountsOwnedByDifferentUsers_ShouldReturnTrue() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        User initializerUser = new User();
//        initializerUser.setId(1);
//        initializerAccount.setUser(initializerUser);
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        User targetUser = new User();
//        targetUser.setId(2);
//        targetAccount.setUser(targetUser);
//        when(accountRepository.findById(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
//        boolean result = accountService.checkIfOwnerDifferent(initializerAccountId, targetAccountId);
//        assertTrue(result);
//        verify(accountRepository, times(1)).findById(initializerAccountId);
//        verify(accountRepository, times(1)).findById(targetAccountId);
//    }
//
//    @Test
//    public void checkIfOwnerDifferent_ShouldReturnFalse_WhenSameAccountOwner() {
//        int initializerAccountId = 1;
//        int targetAccountId = 2;
//        Account initializerAccount = new Account();
//        initializerAccount.setId(initializerAccountId);
//        User initializerUser = new User();
//        initializerUser.setId(1);
//        initializerAccount.setUser(initializerUser);
//        Account targetAccount = new Account();
//        targetAccount.setId(targetAccountId);
//        targetAccount.setUser(initializerUser);
//        when(accountRepository.findById(initializerAccountId)).thenReturn(Optional.of(initializerAccount));
//        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
//        boolean result = accountService.checkIfOwnerDifferent(initializerAccountId, targetAccountId);
//        assertTrue(result);
//        verify(accountRepository, times(1)).findById(initializerAccountId);
//        verify(accountRepository, times(1)).findById(targetAccountId);
//    }
//
//    @Test
//    public void findAccount_AccountExists_ShouldReturnAccount() {
//        int accountId = 1;
//        Account account = new Account();
//        account.setId(accountId);
//        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
//        Optional<Account> result = accountService.findAccount(accountId);
//        assertTrue(result.isPresent());
//        assertEquals(account, result.get());
//        verify(accountRepository, times(1)).findById(accountId);
//    }
//
//    @Test
//    public void findAccount_AccountDoesNotExist_ShouldReturnEmptyOptional() {
//        int accountId = 1;
//        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
//        Optional<Account> result = accountService.findAccount(accountId);
//        assertFalse(result.isPresent());
//        verify(accountRepository, times(1)).findById(accountId);
//    }
//
//    @Test
//    public void getAllAccountsOfUser_UserHasAccounts_ShouldReturnListOfAccounts() {
//        int userId = 1;
//        Account account1 = new Account();
//        account1.setId(1);
//        Account account2 = new Account();
//        account2.setId(2);
//        when(accountRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(account1, account2));
//        List<Account> result = accountService.getAllAccountsOfUser(userId);
//        assertEquals(2, result.size());
//        assertTrue(result.contains(account1));
//        assertTrue(result.contains(account2));
//        verify(accountRepository, times(1)).findAllByUserId(userId);
//    }
//
//    @Test
//    public void getAllAccountsOfUser_UserHasNoAccounts_ShouldReturnEmptyList() {
//        int userId = 1;
//        when(accountRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
//        List<Account> result = accountService.getAllAccountsOfUser(userId);
//        assertTrue(result.isEmpty());
//        verify(accountRepository, times(1)).findAllByUserId(userId);
//    }
//}