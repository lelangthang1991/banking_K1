package com.bstar.banking.repository.specification;

import com.bstar.banking.entity.Card;
import com.bstar.banking.model.request.FilterCardDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class CardSpecifications {
    public static Specification<Card> findAllCardFiltered(FilterCardDTO cardDTO){
        return (root, query, criteriaBuilder)->{
            List<Predicate>  predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(cardDTO.getCardNumber())){
                Predicate cardNumber = criteriaBuilder.like(root.get("cardNumber"), "%" +cardDTO.getCardNumber() +"%");
                predicates.add(cardNumber);
            }

            if(!StringUtils.isEmpty(cardDTO.getCreatePerson())){
                Predicate createPerson = criteriaBuilder.like(root.get("createPerson"), "%" +cardDTO.getCreatePerson() +"%");
                predicates.add(createPerson);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
