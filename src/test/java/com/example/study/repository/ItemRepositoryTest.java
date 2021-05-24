package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create(){
        Item item = new Item();
        item.setStatus("UNREGISTERED");
        item.setName("Notebook");
        item.setTitle("LG Gram");
        item.setContent("LG Gram 2017");
        item.setPrice(1000000);
        item.setBrandName("LG");
        item.setRegisteredAt(LocalDateTime.now());
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("Partner01");
        //item.setPartnerId(1L);
        Item newItem = itemRepository.save(item);

        Assertions.assertNotNull(newItem);
    }

    @Test
    public void read() {

    }
}
