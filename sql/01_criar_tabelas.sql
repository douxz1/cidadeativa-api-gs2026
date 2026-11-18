-- Execute uma vez em um schema Oracle disponível, na ordem deste arquivo.
CREATE TABLE tb_cidadao (
    cd_cidadao NUMBER(19) PRIMARY KEY,
    nm_cidadao VARCHAR2(120 CHAR) NOT NULL,
    ds_email VARCHAR2(160 CHAR) NOT NULL,
    CONSTRAINT uq_cidadao_email UNIQUE (ds_email)
);
CREATE SEQUENCE sq_tb_cidadao START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_bairro (
    cd_bairro NUMBER(19) PRIMARY KEY,
    nm_bairro VARCHAR2(100 CHAR) NOT NULL,
    nm_cidade VARCHAR2(100 CHAR) NOT NULL,
    CONSTRAINT uq_bairro_cidade UNIQUE (nm_bairro, nm_cidade)
);
CREATE SEQUENCE sq_tb_bairro START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_categoria (
    cd_categoria NUMBER(19) PRIMARY KEY,
    nm_categoria VARCHAR2(100 CHAR) NOT NULL,
    ds_categoria VARCHAR2(300 CHAR),
    CONSTRAINT uq_categoria_nome UNIQUE (nm_categoria)
);
CREATE SEQUENCE sq_tb_categoria START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_ocorrencia (
    cd_ocorrencia NUMBER(19) PRIMARY KEY,
    cd_cidadao NUMBER(19) NOT NULL,
    cd_bairro NUMBER(19) NOT NULL,
    cd_categoria NUMBER(19) NOT NULL,
    ds_ocorrencia VARCHAR2(1000 CHAR) NOT NULL,
    ds_endereco VARCHAR2(200 CHAR) NOT NULL,
    nr_prioridade NUMBER(1) NOT NULL,
    ds_status VARCHAR2(20 CHAR) DEFAULT 'PENDENTE' NOT NULL,
    dt_ocorrencia TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL,
    CONSTRAINT fk_ocorrencia_cidadao FOREIGN KEY (cd_cidadao) REFERENCES tb_cidadao(cd_cidadao),
    CONSTRAINT fk_ocorrencia_bairro FOREIGN KEY (cd_bairro) REFERENCES tb_bairro(cd_bairro),
    CONSTRAINT fk_ocorrencia_categoria FOREIGN KEY (cd_categoria) REFERENCES tb_categoria(cd_categoria),
    CONSTRAINT ck_ocorrencia_prioridade CHECK (nr_prioridade BETWEEN 1 AND 5),
    CONSTRAINT ck_ocorrencia_status CHECK (ds_status IN ('PENDENTE','EM_ANALISE','RESOLVIDA'))
);
CREATE SEQUENCE sq_tb_ocorrencia START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_atualizacao (
    cd_atualizacao NUMBER(19) PRIMARY KEY,
    cd_ocorrencia NUMBER(19) NOT NULL,
    ds_observacao VARCHAR2(500 CHAR),
    ds_status VARCHAR2(20 CHAR) NOT NULL,
    dt_atualizacao TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL,
    CONSTRAINT fk_atualizacao_ocorrencia FOREIGN KEY (cd_ocorrencia) REFERENCES tb_ocorrencia(cd_ocorrencia),
    CONSTRAINT ck_atualizacao_status CHECK (ds_status IN ('PENDENTE','EM_ANALISE','RESOLVIDA'))
);
CREATE SEQUENCE sq_tb_atualizacao START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE INDEX ix_ocorrencia_cidadao ON tb_ocorrencia (cd_cidadao);
CREATE INDEX ix_ocorrencia_bairro ON tb_ocorrencia (cd_bairro);
CREATE INDEX ix_ocorrencia_categoria ON tb_ocorrencia (cd_categoria);
CREATE INDEX ix_atualizacao_ocorrencia ON tb_atualizacao (cd_ocorrencia, cd_atualizacao);
