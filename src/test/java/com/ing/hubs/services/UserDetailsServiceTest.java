//package com.ing.hubs.services;
//
//import com.ing.hubs.models.User;
//import com.ing.hubs.repositories.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class UserDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    private UserDetailsService userDetailsService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userDetailsService = new UserDetailsService(userRepository);
//    }
//
//    @Test
//    void LoadUserByUsername_ExistingUser() {
//        String username = "test_username";
//        User expectedUser = new User();
//        expectedUser.setUsername(username);
//        expectedUser.setPassword("test_password");
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        assertEquals(expectedUser.getUsername(), userDetails.getUsername());
//    }
//
//    @Test
//    void LoadUserByUsername_NonExistingUser() {
//        String username = "test_username";
//        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class,
//                () -> userDetailsService.loadUserByUsername(username));
//    }
//}
