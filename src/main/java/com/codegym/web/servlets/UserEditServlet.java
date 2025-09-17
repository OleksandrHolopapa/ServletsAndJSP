package com.codegym.web.servlets;

import com.codegym.web.exseptions.IllegalUserIdException;
import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserEditServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger("UserEditServlet");

    public UserEditServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "User with ID = " + id + " not found";
                    LOGGER.error("UserEditServlet: {}", errorMessage);
                    return new IllegalUserIdException();
                });
        req.setAttribute("mode", "edit");
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/jsp/form.jsp").forward(req, resp);
    }
}