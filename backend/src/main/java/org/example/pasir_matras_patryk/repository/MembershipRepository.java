package org.example.pasir_matras_patryk.repository;

import org.example.pasir_matras_patryk.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupId);

    List<Membership> findByGroupIdAndUserIdIn(Long id, List<Long> selectedUserIds);
}
