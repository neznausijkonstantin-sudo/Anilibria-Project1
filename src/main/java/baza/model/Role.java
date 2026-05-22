package baza.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/*
 * Entity-класс роли пользователя.
 *
 * Роли используются Spring Security как права доступа. Например:
 * ROLE_USER дает доступ к /user/**, ROLE_ADMIN дает доступ к /admin/**.
 */
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    // Первичный ключ таблицы roles.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название роли: ROLE_USER или ROLE_ADMIN.
    @Column(name = "name")
    private String name;

    /*
     * Обратная ссылка на пользователей с этой ролью.
     * @Transient говорит JPA не сохранять это поле как отдельную колонку.
     * В текущей логике проекта это поле почти не используется, основная связь
     * описана на стороне User.roles.
     */
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role() {
    }

    @Override
    public String toString() {
        return name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    /*
     * Метод интерфейса GrantedAuthority.
     * Spring Security вызывает его и получает строку роли.
     */
    @Override
    public String getAuthority() {
        return name;
    }
}