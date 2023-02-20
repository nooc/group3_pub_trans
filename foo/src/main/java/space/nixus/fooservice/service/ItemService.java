package space.nixus.fooservice.service;

import space.nixus.fooservice.model.Item;
import space.nixus.fooservice.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public List<Item> getAll() {
        var items = new ArrayList<Item>();
        itemRepository.findAll(Sort.unsorted()).forEach(items::add);
        return items;
    }

    public Item update(Item item) {
        return itemRepository.save(item);
    }

    public boolean exists(long id) {
        return itemRepository.existsById(id);
    }

    public Item create(Item item) {
        return item.getId() == null ? itemRepository.save(item) : null;
    }

    public void delete(long id) {
        itemRepository.deleteById(id);
    }
}
