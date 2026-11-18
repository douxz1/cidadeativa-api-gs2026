package br.com.fiap.cidadeativa.resource;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.fiap.cidadeativa.bo.BairroBO;
import br.com.fiap.cidadeativa.dto.BairroEntrada;
import br.com.fiap.cidadeativa.dto.BairroSaida;

@RestController
@RequestMapping("/bairros")
public class BairroResource {
    private final BairroBO bo;

    public BairroResource(BairroBO bo) {
        this.bo = bo;
    }

    @PostMapping
    public ResponseEntity<BairroSaida> cadastrar(@Valid @RequestBody BairroEntrada entrada) {
        BairroSaida salvo = bo.cadastrar(entrada);
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(salvo);
    }

    @GetMapping
    public List<BairroSaida> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public BairroSaida buscar(@PathVariable @Positive long id) {
        return bo.buscar(id);
    }

    @PutMapping("/{id}")
    public BairroSaida atualizar(@PathVariable @Positive long id, @Valid @RequestBody BairroEntrada entrada) {
        return bo.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
