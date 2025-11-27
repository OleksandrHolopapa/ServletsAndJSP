package com.codegym.web.servlets;

import java.io.IOException;

import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

  private final InMemoryUserRepository userRepository;

  public UserServlet(InMemoryUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      req.setAttribute("users", userRepository.findAll());
      req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
  }
}