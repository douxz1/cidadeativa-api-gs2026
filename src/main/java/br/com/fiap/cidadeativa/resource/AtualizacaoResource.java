package br.com.fiap.cidadeativa.resource;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.fiap.cidadeativa.bo.AtualizacaoBO;
import br.com.fiap.cidadeativa.dto.AtualizacaoEntrada;
import br.com.fiap.cidadeativa.dto.AtualizacaoEdicao;
import br.com.fiap.cidadeativa.dto.AtualizacaoSaida;

@RestController
@RequestMapping("/ocorrencias/{ocorrenciaId}/atualizacoes")
public class AtualizacaoResource {
    private final AtualizacaoBO bo;

    public AtualizacaoResource(AtualizacaoBO bo) {
        this.bo = bo;
    }

    @PostMapping
    public ResponseEntity<AtualizacaoSaida> cadastrar(@PathVariable @Positive long ocorrenciaId, @Valid @RequestBody AtualizacaoEntrada entrada) {
        AtualizacaoSaida salvo = bo.cadastrar(ocorrenciaId, entrada);
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(salvo);
    }

    @GetMapping
    public List<AtualizacaoSaida> listar(@PathVariable @Positive long ocorrenciaId) {
        return bo.listar(ocorrenciaId);
    }

    @GetMapping("/{id}")
    public AtualizacaoSaida buscar(@PathVariable @Positive long ocorrenciaId, @PathVariable @Positive long id) {
        return bo.buscar(ocorrenciaId, id);
    }

    @PutMapping("/{id}")
    public AtualizacaoSaida atualizar(@PathVariable @Positive long ocorrenciaId, @PathVariable @Positive long id, @Valid @RequestBody AtualizacaoEdicao entrada) {
        return bo.atualizar(ocorrenciaId, id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive long ocorrenciaId, @PathVariable @Positive long id) {
        bo.excluir(ocorrenciaId, id);
        return ResponseEntity.noContent().build();
    }
}
