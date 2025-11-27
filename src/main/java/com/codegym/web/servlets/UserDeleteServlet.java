package com.codegym.web.servlets;

import com.codegym.web.exseptions.IllegalUserIdException;
import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserDeleteServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDeleteServlet.class);

    public UserDeleteServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "User with ID = " + id + " not found";
                    LOGGER.error("UserEditServlet: {}", errorMessage);
                    return new IllegalUserIdException();
                });
        userRepository.delete(id);
        LOGGER.info("UserDeleteServlet: ID {} user {} {} deleted", id, user.getFirstName(), user.getLastName());
        req.setAttribute("users", userRepository.findAll());
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}