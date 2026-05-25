package baza.repository;
import baza.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/*
 Repository для таблицы roles.
 Нужен, чтобы искать роли по id/name и сохранять стартовые роли при запуске.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /*
     Spring Data JPA сам строит SQL-запрос по имени метода.
     findByName("ROLE_ADMIN") означает: найти роль, где поле name равно ROLE_ADMIN.
     */
    Optional<Role> findByName(String name);
}

