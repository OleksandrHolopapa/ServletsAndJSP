package com.codegym.web.repository;

import com.codegym.web.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {
    private InMemoryUserRepository repository;
    private User testUser;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        testUser = new User("John", "Doe", "john@doe.com", 30);
    }

    @Test
    void shouldAddUserAndAssignIdInMethodSave() {
        User savedUser = repository.save(testUser);

        assertNotNull(savedUser.getId(), "User ID should be assigned");
        assertEquals("John", savedUser.getFirstName(), "First name should match");
        assertEquals("Doe", savedUser.getLastName(), "Last name should match");
        assertEquals("john@doe.com", savedUser.getEmail(), "Email should match");
        assertEquals(30, savedUser.getAge(), "Age should match");

        Optional<User> foundUser = repository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent(), "User should be found in repository");
        assertEquals(savedUser, foundUser.get(), "Saved user should match the returned user");
    }
    
    @Test
    void shouldIncrementIdForEachNewUserInMethodSave() {
        User firstUser = new User("First", "User", "first@user.com", 25);
        User secondUser = new User("Second", "User", "second@user.com", 30);

        User savedFirst = repository.save(firstUser);
        User savedSecond = repository.save(secondUser);

        assertNotNull(savedFirst.getId(), "First user should have an ID");
        assertNotNull(savedSecond.getId(), "Second user should have an ID");
        assertNotEquals(savedFirst.getId(), savedSecond.getId(),"Every user should have a unique ID");
        assertEquals(1, savedSecond.getId() - savedFirst.getId(), "IDs should increment by 1");
    }
    
    @Test
    void shouldMethodFindByIdReturnUserWhenUserExists() {
        User savedUser = repository.save(testUser);
        Optional<User> foundUser = repository.findById(savedUser.getId());
        
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser, foundUser.get());
    }

    @Test
    void shouldMethodFindByIdReturnEmptyOptionalWhenUserDoesNotExist() {
        Optional<User> foundUser = repository.findById(-1L);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldMethodFindAllReturnAllUsers() {
        repository.save(testUser);
        User anotherUser = new User("Jane", "Smith", "jane@smith.com", 25);
        repository.save(anotherUser);

        List<User> users = repository.findAll();
        
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getFirstName().equals("John") && u.getLastName().equals("Doe")));
        assertTrue(users.stream().anyMatch(u -> u.getFirstName().equals("Jane") && u.getLastName().equals("Smith")));
    }

    @Test
    void shouldMethodUpdateUpdateExistingUser() {
        User savedUser = repository.save(testUser);
        savedUser.setFirstName("JohnUpdated");
        savedUser.setLastName("DoeUpdated");
        
        boolean result = repository.update(savedUser);
        Optional<User> updatedUser = repository.findById(savedUser.getId());
        
        assertTrue(result);
        assertTrue(updatedUser.isPresent());
        assertEquals("JohnUpdated", updatedUser.get().getFirstName());
        assertEquals("DoeUpdated", updatedUser.get().getLastName());
    }

    @Test
    void shouldMethodUpdateReturnFalseWhenUserDoesNotExist() {
        User nonExistentUser = new User();
        nonExistentUser.setId(-1L);
        nonExistentUser.setFirstName("NameFirst");
        nonExistentUser.setLastName("NameLast");
        
        boolean result = repository.update(nonExistentUser);
        
        assertFalse(result);
    }

    @Test
    void shouldMethodDeleteRemoveUser() {
        User savedUser = repository.save(testUser);
        int sizeBeforeDelete = repository.findAll().size();
        
        boolean result = repository.delete(savedUser.getId());
        Optional<User> deletedUser = repository.findById(savedUser.getId());
        int sizeAfterDelete = repository.findAll().size();
        
        assertTrue(result);
        assertTrue(deletedUser.isEmpty());
        assertEquals(sizeBeforeDelete - 1, sizeAfterDelete);
    }

    @Test
    void shouldMethodDeleteReturnFalseWhenUserDoesNotExist() {
        boolean result = repository.delete(-1L);
        assertFalse(result);
    }
}
