package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import com.example.study.model.entity.User;
import jdk.vm.ci.meta.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import static org.junit.jupiter.api.Assertions.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        String account = "Test02";
        String password = "password02";
        String status = "REGISTERED";
        String email = "test02@gmail.com";
        String phoneNumber = "010-1234-1234";
        //LocalDateTime registeredAt = LocalDateTime.now();
        //LocalDateTime createdAt = LocalDateTime.now();
        //String createdBy = "Admin";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        //user.setRegisteredAt(registeredAt);
        //user.setCreatedAt(createdAt);
        //user.setCreatedBy(createdBy);

        User newUser = userRepository.save(user);

        Assertions.assertNotNull(newUser);
    }

    @Test
    @Transactional
    public void read() { // @RequestParam Long id
        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1234-1234");
        user.getOrderGroupList().stream().forEach(orderGroup -> {
            System.out.println(orderGroup.getRevName());
            System.out.println(orderGroup.getRevAddress());
            System.out.println(orderGroup.getTotalPrice());
            System.out.println(orderGroup.getTotalQuantity());
        });
        Assertions.assertNotNull(user);

    }

    @Test
    @Transactional
    public void update() {
    }

    @Test
    @Transactional // For Testing (Rollback!)
    public void delete() {
        Optional<User> user = userRepository.findById(4L);

        Assertions.assertTrue(user.isPresent());
        user.ifPresent(selectedUser-> {
            userRepository.delete(selectedUser);
        });
        Optional<User> deletedUser = userRepository.findById(4L);
        Assertions.assertFalse(deletedUser.isPresent());


    }

}
