//package com.ing.hubs.services;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import com.ing.hubs.exceptions.ConstraintException;
//import com.ing.hubs.models.AuthRequest;
//import com.ing.hubs.models.RegisterRequest;
//import com.ing.hubs.models.User;
//import com.ing.hubs.repositories.UserRepository;
//import com.ing.hubs.security.JwtService;
//import org.hibernate.NonUniqueResultException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootTest
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userService = new UserService(userRepository, userDetailsService, authenticationManager,
//                passwordEncoder, jwtService);
//    }
//
//    @Test
//    void createUser_shouldSaveUser() {
//        User user = new User();
//        user.setUsername("test_username");
//        user.setPassword("test_password");
//        user.setFirstName("test_firstName");
//        user.setLastName("test_lastName");
//        userService.createUser(user);
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void createUser_NullUser_shouldNotSaveUser() {
//        userService.createUser(null);
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void register_shouldCreateAndSaveUser() throws ConstraintException {
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("test_username");
//        request.setPassword("test_password");
//        request.setFirstName("test_firstName");
//        request.setLastName("test_lastName");
//        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
//        userService.register(request);
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void register_NullRequest_shouldThrowNullPointerException() {
//        assertThrows(NullPointerException.class, () -> userService.register(null));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void register_InvalidFirstName_shouldThrowConstraintException() {
//        RegisterRequest request1 = new RegisterRequest();
//        request1.setUsername("test_username");
//        request1.setPassword("test_password");
//        request1.setFirstName("test_firstName123");
//        request1.setLastName("test_lastName");
//        assertThrows(ConstraintException.class, () -> userService.register(request1));
//        verify(userRepository, never()).save(any(User.class));
//
//        RegisterRequest request2 = new RegisterRequest();
//        request2.setUsername("test_username");
//        request2.setPassword("test_password");
//        request2.setFirstName("test_@#$firstName");
//        request2.setLastName("test_lastName");
//        assertThrows(ConstraintException.class, () -> userService.register(request2));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void register_InvalidLastName_shouldThrowConstraintException() {
//        RegisterRequest request1 = new RegisterRequest();
//        request1.setUsername("test_username");
//        request1.setPassword("test_password");
//        request1.setFirstName("test_firstName");
//        request1.setLastName("test_lastName123");
//        assertThrows(ConstraintException.class, () -> userService.register(request1));
//        verify(userRepository, never()).save(any(User.class));
//
//        RegisterRequest request2 = new RegisterRequest();
//        request2.setUsername("test_username");
//        request2.setPassword("test_password");
//        request2.setFirstName("test_firstName");
//        request2.setLastName("test_@#$lastName");
//        assertThrows(ConstraintException.class, () -> userService.register(request2));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void register_NonUniqueUsername_shouldThrowConstraintException() {
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("test_username");
//        request.setPassword("test_password");
//        request.setFirstName("test_firstName");
//        request.setLastName("test_lastName");
//        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenThrow(NonUniqueResultException.class);
//        assertThrows(ConstraintException.class, () -> userService.register(request));
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void authenticate_shouldReturnTokenWhenCredentialsAreValid() {
//        AuthRequest request = new AuthRequest();
//        request.setUsername("test_username");
//        request.setPassword("test_password");
//        User user = new User();
//        when(userDetailsService.loadUserByUsername(request.getUsername())).thenReturn(user);
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
//        when(jwtService.generateToken(user)).thenReturn("generatedToken");
//        String token = userService.authenticate(request);
//        verify(userDetailsService, times(1)).loadUserByUsername(request.getUsername());
//        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(jwtService, times(1)).generateToken(user);
//    }
//
//    @Test
//    void authenticate_NullRequest_shouldNotAuthenticate() {
//        assertThrows(NullPointerException.class, () -> userService.authenticate(null));
//        verify(userDetailsService, never()).loadUserByUsername(anyString());
//        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(jwtService, never()).generateToken(any(UserDetails.class));
//    }
//}