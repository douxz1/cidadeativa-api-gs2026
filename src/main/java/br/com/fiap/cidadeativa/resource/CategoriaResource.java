package br.com.fiap.cidadeativa.resource;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.fiap.cidadeativa.bo.CategoriaBO;
import br.com.fiap.cidadeativa.dto.CategoriaEntrada;
import br.com.fiap.cidadeativa.dto.CategoriaSaida;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
    private final CategoriaBO bo;

    public CategoriaResource(CategoriaBO bo) {
        this.bo = bo;
    }

    @PostMapping
    public ResponseEntity<CategoriaSaida> cadastrar(@Valid @RequestBody CategoriaEntrada entrada) {
        CategoriaSaida salvo = bo.cadastrar(entrada);
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(salvo);
    }

    @GetMapping
    public List<CategoriaSaida> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public CategoriaSaida buscar(@PathVariable @Positive long id) {
        return bo.buscar(id);
    }

    @PutMapping("/{id}")
    public CategoriaSaida atualizar(@PathVariable @Positive long id, @Valid @RequestBody CategoriaEntrada entrada) {
        return bo.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
