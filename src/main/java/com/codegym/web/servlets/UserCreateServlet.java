package com.codegym.web.servlets;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserCreateServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreateServlet.class);

    public UserCreateServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.fillUserFieldsFromRequest(req);
        userRepository.save(user);
        LOGGER.info("UserCreateServlet: ID {} user {} {} created", user.getId(), user.getFirstName(), user.getLastName());
        req.setAttribute("users", userRepository.findAll());
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}