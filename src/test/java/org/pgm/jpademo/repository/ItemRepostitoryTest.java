package org.pgm.jpademo.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ItemRepostitoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void saveItemTest() {
        Item item = Item.builder()
                .price(10000)
                .itemDetail("detail2")
                .itemNm("itemNm2")
                .stockNumber(20)
                .build();
        itemRepository.save(item);

    }
    @Test
    public void getListTest() {
        List<Item> list = itemRepository.findAll();
        for (Item item : list) {
            log.info(item);
        }
    }
    @Test
    public void updateItemTest() {
        Item item = itemRepository.findById(1L).get();
        item.setItemNm("itemNm3");
        item.setPrice(30000);
        itemRepository.save(item);
    }



}
