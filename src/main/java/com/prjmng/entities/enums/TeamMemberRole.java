package com.prjmng.entities.enums;


public enum TeamMemberRole {
    OWNER,      // created the team, full control, can delete team
    MANAGER,    // can add/remove members, create projects
    MEMBER      // read + contribute only
}