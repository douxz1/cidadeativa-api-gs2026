package br.com.fiap.cidadeativa.resource;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.fiap.cidadeativa.bo.CidadaoBO;
import br.com.fiap.cidadeativa.dto.CidadaoEntrada;
import br.com.fiap.cidadeativa.dto.CidadaoSaida;

@RestController
@RequestMapping("/cidadaos")
public class CidadaoResource {
    private final CidadaoBO bo;

    public CidadaoResource(CidadaoBO bo) {
        this.bo = bo;
    }

    @PostMapping
    public ResponseEntity<CidadaoSaida> cadastrar(@Valid @RequestBody CidadaoEntrada entrada) {
        CidadaoSaida salvo = bo.cadastrar(entrada);
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(salvo);
    }

    @GetMapping
    public List<CidadaoSaida> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public CidadaoSaida buscar(@PathVariable @Positive long id) {
        return bo.buscar(id);
    }

    @PutMapping("/{id}")
    public CidadaoSaida atualizar(@PathVariable @Positive long id, @Valid @RequestBody CidadaoEntrada entrada) {
        return bo.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
