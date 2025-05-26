package org.example.pasir_matras_patryk.repository;

import org.example.pasir_matras_patryk.model.Group;
import org.example.pasir_matras_patryk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMemberships_User(User user);
}
