package baza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 Главный класс приложения.
 @SpringBootApplication запускает автоконфигурацию Spring Boot:
 Spring сам поднимает web-сервер, находит контроллеры, сервисы,
 репозитории и другие компоненты.
 scanBasePackages нужен потому, что код проекта лежит в двух корневых
 пакетах: baza и AnilibriaProject.anilibria. Без этого
 Spring мог бы не найти сервисы из пакета AnilibriaProject.anilibria.
 */
@SpringBootApplication(scanBasePackages = {"baza", "AnilibriaProject.anilibria"})
public class SpringBootSecurityDemoApplication {

	/*
	Точка входа в Java-приложение.
	SpringApplication.run(...) создает Spring context и запускает
	встроенный web-сервер, после чего приложение начинает принимать
	HTTP-запросы на localhost:8080.
	*/
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

}
