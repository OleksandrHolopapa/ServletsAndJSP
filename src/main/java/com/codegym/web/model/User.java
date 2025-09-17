package com.codegym.web.model;

import jakarta.servlet.http.HttpServletRequest;

public class User {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private int age;

  public User() {
  }

  public User(String firstName, String lastName, String email, int age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.age = age;
  }

  public User(Long id, String firstName, String lastName, String email, int age) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.age = age;
  }

    public void fillUserFieldsFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("age"));

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;

        if (idStr != null) this.id = Long.parseLong(idStr);
    }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
