package com.codegym.web.servlets;

import java.io.IOException;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUpdateServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserUpdateServlet.class);

    public UserUpdateServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.fillUserFieldsFromRequest(req);
        if (!userRepository.update(user)) {
            LOGGER.error("UserUpdateServlet: update failed");
            resp.sendError(400);
        } else {
            req.setAttribute("users", userRepository.findAll());
            LOGGER.info("UserUpdateServlet: ID {} user {} {} updated: ", user.getId(), user.getFirstName(), user.getLastName());
            resp.sendRedirect(req.getContextPath() + "/users");
        }
    }
}