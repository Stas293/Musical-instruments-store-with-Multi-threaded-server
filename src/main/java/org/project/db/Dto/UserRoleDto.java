package org.project.db.Dto;

public class UserRoleDto implements java.io.Serializable {
    private final String login;
    private final String roleName;

    public UserRoleDto(String login, String roleName) {
        this.login = login;
        this.roleName = roleName;
    }

    public String getLogin() {
        return login;
    }

    public String getRoleName() {
        return roleName;
    }
}
