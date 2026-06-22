package com.prjmng.services.specifications;

import com.prjmng.entities.Organization;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class OrganizationSpecifications {
    public static Specification<Organization> hasSlug(String slug) {
        return (root, query, cb) ->
            slug.isBlank() || slug == null ?
                    cb.conjunction() :
                    cb.like(root.get("slug"), "%" + slug.toLowerCase() + "%");
    }

    public static Specification<Organization> hasOwner(UUID ownerId) {
        return (root, query, cb) ->
                ownerId == null ?
                        cb.conjunction() :
                        cb.equal(root.get("ownerId"), ownerId);
    }
}
