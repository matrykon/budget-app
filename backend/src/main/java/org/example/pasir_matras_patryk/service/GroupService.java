package org.example.pasir_matras_patryk.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.pasir_matras_patryk.dto.GroupDTO;
import org.example.pasir_matras_patryk.model.Group;
import org.example.pasir_matras_patryk.model.Membership;
import org.example.pasir_matras_patryk.model.User;
import org.example.pasir_matras_patryk.repository.DebtRepository;
import org.example.pasir_matras_patryk.repository.GroupRepository;
import org.example.pasir_matras_patryk.repository.MembershipRepository;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipService membershipService;
    private final DebtRepository debtRepository;

    public GroupService(GroupRepository groupRepository, MembershipRepository membershipRepository, MembershipService membershipService, DebtRepository debtRepository) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.membershipService = membershipService;
        this.debtRepository = debtRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @MutationMapping
    public Group createGroup(GroupDTO groupDTO) {
        User owner = membershipService.getCurrentUser(); // pobieramy aktualnie zalogowanego użytkownika
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setOwner(owner);
        Group savedGroup = groupRepository.save(group);
        Membership membership = new Membership();
        membership.setUser(owner);
        membership.setGroup(savedGroup);
        membershipRepository.save(membership);
        return savedGroup;
    }

    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupa o ID " + id + " nie istnieje."));

        debtRepository.deleteAll(debtRepository.findByGroupId(id));
        membershipRepository.deleteAll(membershipRepository.findByGroupId(id));

        groupRepository.delete(group);
    }
}
