package br.com.fiap.cidadeativa.resource;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.fiap.cidadeativa.bo.OcorrenciaBO;
import br.com.fiap.cidadeativa.dto.OcorrenciaEntrada;
import br.com.fiap.cidadeativa.dto.OcorrenciaSaida;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaResource {
    private final OcorrenciaBO bo;

    public OcorrenciaResource(OcorrenciaBO bo) {
        this.bo = bo;
    }

    @PostMapping
    public ResponseEntity<OcorrenciaSaida> cadastrar(@Valid @RequestBody OcorrenciaEntrada entrada) {
        OcorrenciaSaida salvo = bo.cadastrar(entrada);
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(salvo);
    }

    @GetMapping
    public List<OcorrenciaSaida> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public OcorrenciaSaida buscar(@PathVariable @Positive long id) {
        return bo.buscar(id);
    }

    @PutMapping("/{id}")
    public OcorrenciaSaida atualizar(@PathVariable @Positive long id, @Valid @RequestBody OcorrenciaEntrada entrada) {
        return bo.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
