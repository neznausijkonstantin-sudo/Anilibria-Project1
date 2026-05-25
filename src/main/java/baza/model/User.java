package baza.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

/*
 Entity-класс пользователя.
 Entity - это Java-объект, который Hibernate умеет сохранять в таблицу БД.
 Здесь класс User связан с таблицей users. Кроме обычных полей пользователя
 он реализует UserDetails, чтобы Spring Security мог использовать этот же
 объект при авторизации.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    // Первичный ключ таблицы users. ID генерирует сама MySQL база.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // В Java поле называется username, а в таблице колонка называется name.
    @Column(name = "name")
    private String username;

    // Простое текстовое поле с хобби пользователя.
    @Column(name = "hobby")
    private String hobby;

    // Пароль хранится не открытым текстом, а в виде BCrypt hash.
    @Column(name = "password")
    private String password;

    /*
     Связь многие-ко-многим:
     у одного пользователя может быть несколько ролей;
     одна роль может принадлежать многим пользователям.
     Связь хранится не в users и не в roles, а в промежуточной таблице
     users_roles с колонками user_id и role_id.
     */
    @ManyToMany(cascade = {CascadeType.MERGE})
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    /*
     Spring Security вызывает этот метод, чтобы понять права пользователя.
     Role реализует GrantedAuthority, поэтому коллекцию roles можно вернуть
     как список прав доступа.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    // Ниже четыре метода состояния аккаунта для Spring Security.
    // Сейчас все возвращают true: аккаунты не блокируются и не истекают.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}