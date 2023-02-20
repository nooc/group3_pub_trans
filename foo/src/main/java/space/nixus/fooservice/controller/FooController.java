package space.nixus.fooservice.controller;

import space.nixus.fooservice.model.Item;
import space.nixus.fooservice.model.NewItem;
import space.nixus.fooservice.service.ItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class FooController {

    @Autowired
    ItemService itemService;

    @GetMapping("/items")
    List<Item> listItems() {
        return itemService.getAll();
    }

    @PostMapping("/items")
    Item createItem(@RequestBody NewItem info) {
        var item = new Item(null, info.getName(), info.getType());
        return itemService.create(item);
    }

    @PutMapping("/items/{id}")
    Item updateItem(@PathVariable("id") long id, @RequestBody Item item) {
        if(itemService.exists(id) && id == item.getId())
        {
            return itemService.update(item);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found");
    }

    @DeleteMapping("/items/{id}")
    String deleteItem(@PathVariable("id") long id) {
        itemService.delete(id);
        return "OK";
    }
}
