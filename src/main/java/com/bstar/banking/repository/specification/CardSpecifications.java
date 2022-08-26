package com.bstar.banking.repository.specification;

import com.bstar.banking.entity.Card;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.FilterCardDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static com.bstar.banking.common.CardString.CARD_NOT_FOUND;

public class CardSpecifications {
    public static Specification<Card> findAllCardFiltered(FilterCardDTO cardDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(cardDTO.getCardNumber())) {
                Predicate cardNumber = criteriaBuilder.like(root.get("cardNumber"), "%" + cardDTO.getCardNumber() + "%");
                predicates.add(cardNumber);
            } else if (cardDTO.getIsActivated() != null) {
                Predicate createPerson = criteriaBuilder.equal(root.get("isActivated"), cardDTO.getIsActivated());
                predicates.add(createPerson);
            } else if (StringUtils.isNotBlank(cardDTO.getCreatePerson())) {
                Predicate createPerson = criteriaBuilder.like(root.get("createPerson"), "%" + cardDTO.getCreatePerson() + "%");
                predicates.add(createPerson);
            } else if (StringUtils.isNotBlank(cardDTO.getUpdatePerson())) {
                Predicate createPerson = criteriaBuilder.like(root.get("updatePerson"), "%" + cardDTO.getUpdatePerson() + "%");
                predicates.add(createPerson);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Card> findById(String cardNumber) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isBlank(cardNumber)) {
                throw new NotFoundException(CARD_NOT_FOUND);
            }
            Predicate findCardNumber = criteriaBuilder.equal(root.get("cardNumber"), cardNumber);
            return criteriaBuilder.and(findCardNumber);
        };
    }

}
