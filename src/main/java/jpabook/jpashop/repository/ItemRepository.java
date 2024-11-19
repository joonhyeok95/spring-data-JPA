package jpabook.jpashop.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select m from Item m", Item.class).getResultList();
    }

    public List<Item> findByNames(String name) {
        return em.createQuery("select m from Item m where m.name = :name", Item.class)
                .setParameter("name", name).getResultList();
    }

}
