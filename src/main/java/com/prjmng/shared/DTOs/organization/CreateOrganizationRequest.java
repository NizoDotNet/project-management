package com.prjmng.shared.DTOs.organization;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateOrganizationRequest {
    @Length(min=2, max=100)
    private String name;
}
