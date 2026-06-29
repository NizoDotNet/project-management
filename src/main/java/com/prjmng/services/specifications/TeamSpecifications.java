package com.prjmng.services.specifications;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Team;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TeamSpecifications {
    public static Specification<Team> hadOrgId(UUID orgId) {
        return (root, query, cb) ->
                orgId == null?
                        cb.conjunction() :
                        cb.equal(root.get("orgId"), orgId);
    }
    public static Specification<Team> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty() ?
                        cb.conjunction() :
                        cb.like(root.get("name"), "%" + name.toLowerCase() + "%");
    }

}
