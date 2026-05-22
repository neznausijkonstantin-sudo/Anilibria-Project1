package AnilibriaProject.anilibria.service;

import AnilibriaProject.anilibria.dto.FranchiseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
  Клиент для внешнего Anilibria/Aniliberty API.
  Этот класс отвечает только за HTTP-запрос наружу и преобразование JSON
  ответа в Java-объекты FranchiseDto. Бизнес-логика фильтрации по году
  находится уже в AnilinbriaServiceImpl.
 */
@Component
public class AnilibriaClientServiceImpl implements AnilibriaClientService {
    private static final Logger LOGGER = Logger.getLogger(AnilibriaClientServiceImpl.class.getName());

    @Override
    public Optional<List<FranchiseDto>> getFranchiseFromApi() {
        // HttpClient отправляет запрос. connectTimeout ограничивает время подключения.
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        // Описываем GET-запрос к endpoint со списком франшиз.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://aniliberty.top/api/v1/anime/franchises"))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Любой статус вне диапазона 2xx считаем ошибкой внешнего API.
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOGGER.warning("Anilibria API returned status " + response.statusCode());
                return Optional.empty();
            }

            // ObjectMapper из Jackson превращает JSON-строку в список FranchiseDto.
            ObjectMapper mapper = new ObjectMapper();

            List<FranchiseDto> franchises = mapper.readValue(response.body(), new TypeReference<List<FranchiseDto>>() {
            });
            return Optional.of(franchises);
        } catch (HttpTimeoutException e) {
            // Timeout обрабатываем отдельно, чтобы в логах было понятно, что API не ответило вовремя.
            LOGGER.log(Level.WARNING, "Anilibria API request timed out", e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load franchises from Anilibria API", e);
        }
        // Optional.empty() явно показывает вызывающему коду: данных нет.
        return Optional.empty();
    }
}
