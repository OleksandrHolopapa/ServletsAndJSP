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
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUpdateServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private InMemoryUserRepository userRepository;
    private UserUpdateServlet servlet;
    private User testUser;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository();
        servlet = new UserUpdateServlet(userRepository);
        testUser = new User();
        testUser = userRepository.save(testUser);
        when(request.getContextPath()).thenReturn("");
    }

    private static Stream<Arguments> getUserUpdateParameters() {
        return Stream.of(
            Arguments.arguments("John", "Doe", "john@doe.com", "30", 
                              "John", "Doe", "john@doe.com", 30),

            Arguments.arguments(null, null, null, "0",
                              null, null, null, 0),

            Arguments.arguments("", "", "", "0", 
                              "", "", "", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("getUserUpdateParameters")
    void shouldUpdateUserWithVariousParametersAndRedirect(
            String firstName, String lastName, String email, String age,
            String expectedFirstName, String expectedLastName, String expectedEmail, int expectedAge
    ) throws IOException {
        when(request.getParameter("id")).thenReturn(String.valueOf(testUser.getId()));
        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("lastName")).thenReturn(lastName);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("age")).thenReturn(age);

        servlet.doPost(request, response);

        Optional<User> updatedUser = userRepository.findById(testUser.getId());
        assertTrue(updatedUser.isPresent(), "User should exist after update");
        
        User user = updatedUser.get();
        assertEquals(expectedFirstName, user.getFirstName(), "First name should be updated");
        assertEquals(expectedLastName, user.getLastName(), "Last name should be updated");
        assertEquals(expectedEmail, user.getEmail(), "Email should be updated");
        assertEquals(expectedAge, user.getAge(), "Age should be updated");
        
        verify(response).sendRedirect("/users");
    }

    @Test
    void shouldHandleNonExistentUser() throws IOException {
        when(request.getParameter("id")).thenReturn("-1");
        when(request.getParameter("firstName")).thenReturn("John");
        when(request.getParameter("lastName")).thenReturn("Doe");
        when(request.getParameter("email")).thenReturn("john@doe.com");
        when(request.getParameter("age")).thenReturn("30");

        servlet.doPost(request, response);

        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void shouldHandleInvalidIdFormat() throws IOException {
        when(request.getParameter("id")).thenReturn("invalid");
        when(request.getParameter("firstName")).thenReturn("John");

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
        verify(response, never()).sendError(anyInt());
    }

    @Test
    void shouldHandleNullId() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("firstName")).thenReturn("John");

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
        verify(response, never()).sendError(anyInt());
    }

    @Test
    void shouldHandleEmptyId() throws IOException {
        when(request.getParameter("id")).thenReturn("");
        when(request.getParameter("firstName")).thenReturn("John");

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
        verify(response, never()).sendError(anyInt());
    }
}
