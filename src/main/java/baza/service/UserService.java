package baza.service;

import baza.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

/*
 * Интерфейс сервиса пользователей.
 *
 * Он описывает, какие операции с пользователями есть в приложении.
 * Реальная логика находится в UserServiceImpl.
 *
 * extends UserDetailsService нужен Spring Security: при логине Security
 * вызывает loadUserByUsername(...), чтобы найти пользователя по имени.
 */
public interface UserService extends UserDetailsService {

    User findByUsername(String name);

    void saveUser(User user);

    User showUser(Long id);

    void removeUserById(Long id);

    List<User> getAllUsers();

    void updateUser(User user);
}

