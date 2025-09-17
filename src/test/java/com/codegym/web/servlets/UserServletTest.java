package com.codegym.web.servlets;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private InMemoryUserRepository userRepository;
    private UserServlet userServlet;

    @BeforeEach
    void begin() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository();
        userServlet = new UserServlet(userRepository);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void shouldSetUsersAttributeAndForwardToJsp() throws Exception {
        List<User> expectedUsers = Arrays.asList(
            new User("Jonny", "English", "jonny@english.com", 40),
            new User("Will", "Smith", "will@smith.com", 50)
        );
        expectedUsers.forEach(userRepository::save);

        userServlet.doGet(request, response);

        verify(request).setAttribute(eq("users"), eq(expectedUsers));
        verify(request).getRequestDispatcher("/WEB-INF/jsp/users.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void shouldNotFailWithNoUsers() throws Exception {
        assertTrue(userRepository.findAll().isEmpty(), "Repository should be empty for this test");

        userServlet.doGet(request, response);

        verify(request).setAttribute(eq("users"), eq(Collections.emptyList()));
    }
}
