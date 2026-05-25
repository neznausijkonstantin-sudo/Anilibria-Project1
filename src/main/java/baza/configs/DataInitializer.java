package baza.configs;
import baza.model.User;
import baza.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import baza.model.Role;
import baza.repository.UserRepository;
import java.util.List;

/*
 Инициализатор стартовых данных.
 CommandLineRunner запускается один раз при старте приложения.
 Этот класс гарантирует, что в базе есть роли ROLE_USER / ROLE_ADMIN
 и стартовый администратор.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminHobby;

    /*
   @Value берет настройки из application.properties или переменных окружения.
    Например APP_INIT_ADMIN_USERNAME в docker-compose.yml попадет сюда как
    app.init.admin.username.
    */
    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.init.admin.username:admin}") String adminUsername,
                           @Value("${app.init.admin.password:admin}") String adminPassword,
                           @Value("${app.init.admin.hobby:local admin}") String adminHobby) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminHobby = adminHobby;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Создаем роли, если их еще нет в базе.
        Role userRole = getOrCreateRole("ROLE_USER");
        Role adminRole = getOrCreateRole("ROLE_ADMIN");

        // Админ создается только если пользователя с таким логином еще нет.
        if (userRepository.findByUsername(adminUsername) == null) {
            User admin = new User();
            admin.setUsername(adminUsername);
            // Пароль стартового админа тоже сохраняется как hash.
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setHobby(adminHobby);
            admin.setRoles(List.of(userRole, adminRole));
            userRepository.save(admin);
        }
    }

    // Найти роль по имени или создать ее, если база еще пустая.
    private Role getOrCreateRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name)));
    }
}

