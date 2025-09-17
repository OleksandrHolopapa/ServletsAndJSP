package com.codegym.web.servlets;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCreateServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private InMemoryUserRepository userRepository;
    private UserCreateServlet servlet;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository();
        servlet = new UserCreateServlet(userRepository);
    }

    private static Stream<Arguments> getUserParameters() {
        return Stream.of(
            Arguments.of("John", "Doe", "john@doe.com", "30", 
                        "John", "Doe", "john@doe.com", 30),
            Arguments.of("", "", "", "0", 
                        "", "", "", 0),
            Arguments.of(null, null, null, "0", 
                        null, null, null, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("getUserParameters")
    void shouldCreateUserWithVariousParametersAndRedirectToUsersPage(
            String inputFirstName, String inputLastName, String inputEmail, String inputAge,
            String expectedFirstName, String expectedLastName, String expectedEmail, int expectedAge
    ) throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn(inputFirstName);
        when(request.getParameter("lastName")).thenReturn(inputLastName);
        when(request.getParameter("email")).thenReturn(inputEmail);
        when(request.getParameter("age")).thenReturn(inputAge);
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size(), "Should create exactly one user");
        
        User createdUser = users.getFirst();
        assertEquals(expectedFirstName, createdUser.getFirstName(), "First name should match");
        assertEquals(expectedLastName, createdUser.getLastName(), "Last name should match");
        assertEquals(expectedEmail, createdUser.getEmail(), "Email should match");
        assertEquals(expectedAge, createdUser.getAge(), "Age should match");
        
        verify(response).sendRedirect("/users");
    }

    @Test
    void shouldHandleInvalidAgeFormat() throws IOException {
        when(request.getParameter("firstName")).thenReturn("John");
        when(request.getParameter("lastName")).thenReturn("Doe");
        when(request.getParameter("email")).thenReturn("john@doe.com");
        when(request.getParameter("age")).thenReturn("not-a-number");
        when(request.getContextPath()).thenReturn("");

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));

        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty(), "No user should be created with invalid age format");

        verify(response, never()).sendRedirect(anyString());
    }
}