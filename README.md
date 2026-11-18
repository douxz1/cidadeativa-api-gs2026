# CidadeAtiva API

API da **Global Solution 2026 da FIAP** para resolver problemas urbanos, como buracos, iluminação quebrada e descarte irregular de lixo.

## Entrega

| Entrega | Preencher antes do envio |
|---|---|
| Integrantes, RMs e turma | Lucas José Paolillo Vasconcelos — 565561<br>Thiago Dias Gomes — 569675<br>Matheus Nogueira Quintas — 572542<br>João Arthur Maia — 573458<br>Guilherme Ribeiro — 573890 |
| URL pública da API |   |
| Vídeo não listado de até 5 minutos |  |
## Como rodar

Requisitos: JDK 21, Maven 3.9 e acesso ao Oracle da FIAP.

1. Abra o `pom.xml` no IntelliJ como projeto Maven e aguarde o carregamento das dependências.
2. No SQL Developer, execute `sql/01_criar_tabelas.sql` na ordem do arquivo, apenas se as tabelas ainda não existirem.
3. Em **Run → Edit Configurations → Environment variables**, configure:

| Variável | Valor |
|---|---|
| `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| `DB_USER` | Seu usuário Oracle |
| `DB_PASSWORD` | Sua senha Oracle |
| `PORT` | `8081` para executar localmente |
| `CORS_ORIGINS` | `http://localhost:5173` ou a origem do seu front |

Não coloque usuário ou senha reais no código ou no GitHub.

4. Execute a classe `CidadeAtivaApplication`. Como alternativa, com as variáveis configuradas no terminal, rode `mvn spring-boot:run`.
5. Teste `GET http://localhost:8081/health` e `GET http://localhost:8081/categorias` no Insomnia. Sem a variável `PORT`, a porta padrão é 8080.

## Testar no Insomnia

Importe `CidadeAtiva_Insomnia.json` e ajuste `base_url` para `http://localhost:8081`.
Cadastre cidadão, bairro e categoria antes de cadastrar uma ocorrência. Use os IDs retornados nas próximas requisições.

Principais rotas: `/cidadaos`, `/bairros`, `/categorias`, `/ocorrencias` e `/ocorrencias/{ocorrenciaId}/atualizacoes`.
As coleções permitem GET e POST; os registros com `/{id}` permitem GET, PUT e DELETE.
Para reabrir uma ocorrência de RESOLVIDA para PENDENTE, informe uma justificativa em `observacao`.

