package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    @Query(value = "select u from User u " +
            " where (:mode = 0 or u.id in :ids) " +
            " order by u.name ")
    Page<User> getUsers(@Param("mode") int mode,
                        @Param("ids") List<Long> ids,
                        Pageable pageable);
}