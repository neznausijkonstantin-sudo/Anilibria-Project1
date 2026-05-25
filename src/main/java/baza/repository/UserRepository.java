package baza.repository;
import baza.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 Repository - слой доступа к базе данных.
 JpaRepository уже дает готовые методы:
 findAll, findById, getById, save, deleteById и другие.
 Spring сам создает реализацию этого интерфейса во время запуска приложения.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /*
     Поиск пользователя по username нужен Spring Security при логине.
     left join fetch u.roles сразу подгружает роли пользователя вместе с ним,
     чтобы потом Security мог проверить ROLE_USER / ROLE_ADMIN без отдельного
     ленивого запроса к базе.
     */
    @Query("Select u from User u left join fetch u.roles where u.username=:username")
    User findByUsername(@Param("username") String name);
}
