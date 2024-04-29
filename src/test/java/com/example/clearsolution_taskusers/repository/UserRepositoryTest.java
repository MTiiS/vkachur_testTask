package com.example.clearsolution_taskusers.repository;

import com.example.clearsolution_taskusers.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllByFilter() {

        User user1 = new User(1L, "Dima", "D", "Dima@example.com", LocalDate.of(2000, 5, 10), null, null);
        User user2 = new User(2L, "Alice", "S", "Alice@example.com", LocalDate.of(2005, 8, 15), null, null);
        User user3 = new User(3L, "Artem", "J", "Artem@example.com", LocalDate.of(2010, 7, 20), null, null);
        userRepository.saveAll(List.of(user1, user2, user3));


        LocalDate dateFrom = LocalDate.of(2000, 1, 1);
        LocalDate dateTo = LocalDate.of(2005, 12, 31);
        Sort sort = Sort.by(Sort.Direction.ASC, "firstName");


        List<User> actualUsers = userRepository.findAllByFilter(dateFrom, dateTo, sort);

        //Artem not include by filter
        assertEquals(2, actualUsers.size());

        //Sort - first Alice
        assertEquals(user2.getFirstName(), actualUsers.get(0).getFirstName());
        assertEquals(user2.getLastName(), actualUsers.get(0).getLastName());

        //Second - first Alice
        assertEquals(user1.getFirstName(), actualUsers.get(1).getFirstName());
        assertEquals(user1.getLastName(), actualUsers.get(1).getLastName());
    }
}
