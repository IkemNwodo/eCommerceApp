package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemControllerTest {

    @Autowired
    private ItemController itemController;
    @Autowired
    private ItemRepository itemRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private UserRepository userRepo;


    @Before
    public void setUp() {

        Item item = createItem();
        User user = createUser();
        Cart cart = createCart();

        cart.setUser(user);
        user.setCart(cart);

        cartRepo.save(cart);
        userRepo.save(user);
        itemRepo.save(item);


    }

    @Test
    public void getItemsAllItems() {
        ResponseEntity<List<Item>> itemsList = itemController.getItems();

        System.out.println("Printing Items List: " + itemsList);
        assertNotNull(itemsList);
        assertEquals(200, itemsList.getStatusCodeValue());
        assertEquals("Nivea", Objects.requireNonNull(itemsList.getBody()).get(0).getName());
        assertEquals(java.util.Optional.of(1L), Objects.requireNonNull(itemsList.getBody()).get(0).getId());
        assertEquals(java.util.Optional.of(2L), Objects.requireNonNull(itemsList.getBody()).get(1).getId());

    }


    @Test
    public void getItemsById() {
        ResponseEntity<Item> itemById = itemController.getItemById(1L);
        assertNotNull(itemById);
        assertEquals(200, itemById.getStatusCodeValue());
        assertEquals("Nivea", Objects.requireNonNull(itemById.getBody()).getName());


    }
    @Test
    public void getItemsByName() {
        ResponseEntity<List<Item>> ItemByName = itemController.getItemsByName("Pencil");
        assertNotNull(ItemByName);
        assertEquals(200, ItemByName.getStatusCodeValue());
        assertEquals("Nivea", Objects.requireNonNull(ItemByName.getBody().get(0).getName()));
        assertEquals("Nivea Roll on", Objects.requireNonNull(ItemByName.getBody().get(0).getDescription()));


    }


    private Item createItem() {
        return new Item(1L, "Nivea", new BigDecimal("10.00"), "Nivea Roll on");
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setItems(createItemsList());
        return cart;
    }


    private List<Item> createItemsList() {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1L, "Nivea", new BigDecimal("10.00"), "Nivea Roll on"));
        itemList.add(new Item(2L, "Hug", new BigDecimal("3.00"), "Hug Body Spray"));
        return itemList;
    }

    private static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("somepassword");
        return user;
    }
}