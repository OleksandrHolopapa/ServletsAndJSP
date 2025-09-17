package com.codegym.web.model;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserTest {

    private User user;
    
    @Mock
    private HttpServletRequest request;
    
    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }
    
    @Test
    void shouldSetAllFieldsWhenAllParametersIsPresent() {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("firstName")).thenReturn("Alice");
        when(request.getParameter("lastName")).thenReturn("Johnson");
        when(request.getParameter("email")).thenReturn("alice@johnson.com");
        when(request.getParameter("age")).thenReturn("28");
        
        user.fillUserFieldsFromRequest(request);
        
        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Johnson", user.getLastName());
        assertEquals("alice@johnson.com", user.getEmail());
        assertEquals(28, user.getAge());
    }
    
    @Test
    void shouldNotSetIdWhenIdIsNull() {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("firstName")).thenReturn("Bob");
        when(request.getParameter("lastName")).thenReturn("Williams");
        when(request.getParameter("email")).thenReturn("bob.williams@example.com");
        when(request.getParameter("age")).thenReturn("35");
        
        user.fillUserFieldsFromRequest(request);
        
        assertNull(user.getId());
        assertEquals("Bob", user.getFirstName());
        assertEquals("Williams", user.getLastName());
        assertEquals("bob.williams@example.com", user.getEmail());
        assertEquals(35, user.getAge());
    }
}