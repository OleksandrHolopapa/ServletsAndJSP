package com.codegym.web.servlets;

import com.codegym.web.exseptions.IllegalUserIdException;
import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDeleteServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private InMemoryUserRepository userRepository;
    private UserDeleteServlet servlet;
    private User testUser;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository();
        servlet = new UserDeleteServlet(userRepository);
        testUser = new User("Pitter", "Parker", "pitter@parker.com", 22);
        testUser = userRepository.save(testUser);
        when(request.getContextPath()).thenReturn("");
    }

    @Test
    void shouldDeleteUserAndRedirect() throws IOException {
        when(request.getParameter("id")).thenReturn(String.valueOf(testUser.getId()));
        
        servlet.doPost(request, response);
        
        assertFalse(userRepository.findById(testUser.getId()).isPresent());
        verify(response).sendRedirect("/users");
    }

    @Test
    void shouldHandleNullIdParameter() throws IOException {
        when(request.getParameter("id")).thenReturn(null);

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void shouldHandleEmptyIdParameter() throws IOException {
        when(request.getParameter("id")).thenReturn("");

        assertThrows(NumberFormatException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
    }
    
    @Test
    void shouldThrowExceptionWhenUserNotFoundAndDoNotRedirect() throws IOException {
        long nonExistentId = -1L;
        when(request.getParameter("id")).thenReturn(String.valueOf(nonExistentId));

        assertThrows(IllegalUserIdException.class, () -> servlet.doPost(request, response));
        verify(response, never()).sendRedirect(anyString());
    }
}