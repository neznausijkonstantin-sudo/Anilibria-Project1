package baza.service;

import baza.model.User;
import baza.repository.RoleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import baza.model.Role;
import baza.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 Сервисный слой для пользователей.
 Контроллеры вызывают этот класс, когда нужно создать, обновить,
 удалить или найти пользователя. Здесь держится бизнес-логика:
 кодирование пароля, подстановка ролей из базы, транзакции.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    /*
     Constructor injection: Spring сам передает сюда нужные зависимости.
     @Lazy у passwordEncoder нужен, чтобы избежать циклической зависимости
     между security-конфигом и сервисом пользователей.
     */
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Поиск пользователя по логину. Используется и приложением, и Spring Security.
    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    /*
     Метод из UserDetailsService.
     Spring Security вызывает его во время авторизации пользователя.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    /*
     Создание пользователя.
     @Transactional означает: все операции с БД внутри метода выполняются
     как одна транзакция. Если что-то упадет, изменения откатятся.
     */
    @Transactional
    @Override
    public void saveUser(User user) {
        // Пароль нельзя хранить открытым текстом, поэтому сохраняем BCrypt hash.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Из JSON приходят роли с id, а сохранять нужно реальные Role entity из БД.
        user.setRoles(resolveRoles(user.getRoles()));
        userRepository.save(user);
    }

    // Получить одного пользователя по id.
    @Override
    public User showUser(Long id) {
        return userRepository.getById(id);
    }

    // Удалить пользователя по id.
    @Transactional
    @Override
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }

    // readOnly=true подсказывает, что метод только читает данные и не меняет БД.
    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /*
     Обновление пользователя.
     Если пароль в форме оставили пустым, сохраняем старый hash пароля.
     Если пароль ввели, кодируем новый пароль.
     */
    @Transactional
    @Override
    public void updateUser(User user) {
        if (user.getPassword().length() == 0) {
            user.setPassword(userRepository.getById(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(resolveRoles(user.getRoles()));
        userRepository.save(user);
    }

    /*
     Преобразует роли из запроса в роли из базы данных.
     Это важно: если сохранить "новые" Role-объекты из JSON напрямую,
     Hibernate может попытаться создать лишние записи или получить конфликт.
     */
    private List<Role> resolveRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + role.getId())))
                .collect(Collectors.toList());
    }
}
