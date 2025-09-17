package com.codegym.web.servlets;

import com.codegym.web.exseptions.IllegalUserIdException;
import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserEditServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private InMemoryUserRepository userRepository;
    private UserEditServlet servlet;
    private User testUser;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository();
        servlet = new UserEditServlet(userRepository);
        testUser = new User("Tor", "Odinson", "tor@odinson.com", 25);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void shouldSetEditModeAndUserAttributeAndForwardToFormJsp() throws Exception {
        userRepository.save(testUser);
        when(request.getParameter("id")).thenReturn("1");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("mode"), eq("edit"));
        verify(request).setAttribute(eq("user"), eq(testUser));
        verify(request).getRequestDispatcher("/WEB-INF/jsp/form.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(request.getParameter("id")).thenReturn("-1");

        assertThrows(IllegalUserIdException.class, () -> servlet.doGet(request, response));
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void shouldThrowExceptionWhenInvalidIdFormat() {
        when(request.getParameter("id")).thenReturn("invalid");

        assertThrows(NumberFormatException.class, () -> servlet.doGet(request, response));
        verify(request, never()).getRequestDispatcher(anyString());
    }
}