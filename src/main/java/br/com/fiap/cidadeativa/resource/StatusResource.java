package br.com.fiap.cidadeativa.resource;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusResource {
    @GetMapping("/health")
    public Map<String, String> verificarStatus() {
        return Map.of("status", "ok");
    }
}
