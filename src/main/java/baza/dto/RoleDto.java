package baza.dto;

import baza.model.Role;

/*
 * DTO роли для ответа API.
 *
 * Frontend использует name/authority, чтобы показать роли пользователя
 * и корректно отправлять роли при создании или редактировании.
 */
public class RoleDto {
    private Long id;
    private String name;
    private String authority;

    public RoleDto() {
    }

    // Преобразование Entity Role в безопасный объект для JSON-ответа.
    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
