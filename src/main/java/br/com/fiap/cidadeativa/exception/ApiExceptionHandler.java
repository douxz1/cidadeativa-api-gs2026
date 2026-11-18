package br.com.fiap.cidadeativa.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    public record Erro(int status, String mensagem) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> validarDto(MethodArgumentNotValidException erro) {
        var campo = erro.getBindingResult().getFieldError();
        String mensagem = campo == null ? "Dados inválidos." : campo.getField() + ": " + campo.getDefaultMessage();
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            HandlerMethodValidationException.class})
    public ResponseEntity<Erro> dadosInvalidos() {
        return resposta(HttpStatus.BAD_REQUEST, "Confira o JSON e os parâmetros da requisição.");
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Erro> naoEncontrado(NaoEncontradoException erro) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Erro> rotaInexistente() {
        return resposta(HttpStatus.NOT_FOUND, "Rota não encontrada.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Erro> metodoNaoPermitido(HttpRequestMethodNotSupportedException erro) {
        return new ResponseEntity<>(new Erro(405, "Método HTTP não permitido."), erro.getHeaders(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Erro> conflito(RegraDeNegocioException erro) {
        return resposta(HttpStatus.CONFLICT, erro.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Erro> integridade() {
        return resposta(HttpStatus.CONFLICT, "Operação conflita com registros existentes ou referências do banco.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Erro> inesperado(Exception erro) {
        if (erro instanceof ErrorResponse resposta && resposta.getStatusCode().is4xxClientError()) {
            return new ResponseEntity<>(new Erro(resposta.getStatusCode().value(),
                    "A requisição não pôde ser processada."), resposta.getHeaders(), resposta.getStatusCode());
        }
        LOG.error("Falha ao processar a requisição", erro);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao processar a requisição.");
    }

    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new Erro(status.value(), mensagem));
    }
}
