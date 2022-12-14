package ru.practicum.ewm.event;

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
                "uris", String.join(",", uris),
                "unique", unique
        );
        ResponseEntity<Object> tempViewStats =
                get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        null, parameters);
        String body = tempViewStats.getBody().toString();
        ViewStats stat = new ViewStats();
        if (body.length() > 0 && body.indexOf("hits=") > 0) {
            String hits = body.substring(body.indexOf("hits=") + 5, body.indexOf("}"));
            stat.setHits(Integer.parseInt(hits));
        }
        return List.of(stat);
    }
}
