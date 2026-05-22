package baza.dto;

import baza.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

/*
 * DTO пользователя для ответа API.
 *
 * DTO нужен, чтобы не отдавать frontend полную Entity User.
 * В User есть password, а пароль нельзя отправлять в браузер даже в виде hash.
 */
public class UserDto {
    private Long id;
    private String username;
    private String hobby;
    private Collection<RoleDto> roles;

    public UserDto() {
    }

    // Конструктор копирует только безопасные поля из User.
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.hobby = user.getHobby();
        // Роли тоже преобразуются в DTO, чтобы API возвращал простую JSON-структуру.
        this.roles = user.getRoles().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Collection<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleDto> roles) {
        this.roles = roles;
    }
}

