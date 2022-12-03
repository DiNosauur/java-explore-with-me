package ru.practicum.ewm.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.event.dto.EndpointHit;
import ru.practicum.ewm.event.dto.ViewStats;
import ru.practicum.ewm.exception.ValidationException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EventClient extends BaseClient {

    @Autowired
    public EventClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addEndpointHit(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public Collection<ViewStats> getStats(String start,
                                          String end,
                                          List<String> uris,
                                          boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> tempViewStats =
                get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        null, parameters);
        log.info("tempViewStats={}", tempViewStats.toString());
        log.info("tempViewStats.getBody={}", tempViewStats.getBody().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStats> stats;
        try {
            stats = objectMapper.readValue(
                    tempViewStats.getBody().toString(), new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ValidationException(String.format(
                    "Ошибка валидации ответа от сервера статистики: %s", e.getMessage()));
        }
        log.info("stats={}", stats.toString());
        return stats;
    }
}
