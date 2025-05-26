package org.example.pasir_matras_patryk.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.pasir_matras_patryk.dto.GroupTransactionDTO;
import org.example.pasir_matras_patryk.model.*;
import org.example.pasir_matras_patryk.repository.DebtRepository;
import org.example.pasir_matras_patryk.repository.GroupRepository;
import org.example.pasir_matras_patryk.repository.MembershipRepository;
import org.example.pasir_matras_patryk.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupTransactionService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;
    private final TransactionRepository transactionRepository;

    public GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository, TransactionRepository transactionRepository) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Long> selectedUserIds = dto.getSelectedUserIds();
        if (selectedUserIds == null || selectedUserIds.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano żadnych użytkowników do transakcji grupowej.");
        }

        List<Membership> selectedMembers = membershipRepository.findByGroupIdAndUserIdIn(group.getId(), selectedUserIds);
        Map<Long, User> selectedUsersMap = selectedMembers.stream()
                .map(Membership::getUser)
                .collect(Collectors.toMap(User::getId, user -> user));

        Transaction mainTransaction = createMainTransaction(dto, currentUser, group);
        transactionRepository.save(mainTransaction);

        if (mainTransaction.getType() == TransactionType.EXPENSE) {
            int numberOfParticipants = selectedUserIds.size();
            double amountPerUser = Math.round((dto.getAmount() / numberOfParticipants) * 100.0) / 100.0;

            for (Long debtorId : selectedUserIds) {
                if (!debtorId.equals(currentUser.getId())) {
                    User debtor = selectedUsersMap.get(debtorId);
                    if (debtor == null) {
                        System.err.println("Ostrzeżenie: Nie znaleziono użytkownika o ID " + debtorId + " w mapie wybranych użytkowników.");
                        continue;
                    }
                    Debt debt = new Debt();
                    debt.setDebtor(debtor);
                    debt.setCreditor(currentUser);
                    debt.setGroup(group);
                    debt.setAmount(amountPerUser);
                    debt.setTitle(dto.getTitle());
                    debtRepository.save(debt);
                }
            }
        }
    }

    private Transaction createMainTransaction(GroupTransactionDTO dto, User currentUser, Group group) {
        Transaction mainTransaction = new Transaction();
        mainTransaction.setUser(currentUser);
        mainTransaction.setAmount(dto.getAmount());
        mainTransaction.setNotes("Transakcja grupowa: " + group.getName());
        mainTransaction.setTags("Group");

        if ("EXPENSE".equalsIgnoreCase(dto.getType())) {
            mainTransaction.setType(TransactionType.EXPENSE);
        } else if ("INCOME".equalsIgnoreCase(dto.getType())) {
            mainTransaction.setType(TransactionType.INCOME);
        } else {
            throw new IllegalArgumentException("Nieznany lub nieobsługiwany typ transakcji grupowej: " + dto.getType());
        }
        return mainTransaction;
    }
}
