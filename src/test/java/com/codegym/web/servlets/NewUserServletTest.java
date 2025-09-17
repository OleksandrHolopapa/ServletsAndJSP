package com.codegym.web.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NewUserServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private NewUserServlet servlet;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        servlet = new NewUserServlet();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void shouldSetCreateModeAndForwardToFormJsp() throws Exception {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("mode"), eq("create"));
        verify(request).getRequestDispatcher("/WEB-INF/jsp/form.jsp");
        verify(requestDispatcher).forward(request, response);
    }
}