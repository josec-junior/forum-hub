CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,

    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_topico_autor
        FOREIGN KEY (autor_id)
            REFERENCES usuarios (id),

    CONSTRAINT fk_topico_curso
        FOREIGN KEY (curso_id)
            REFERENCES cursos (id)
);

CREATE TABLE respostas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensagem TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,

    topico_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,

    solucao BOOLEAN,

    PRIMARY KEY (id),

    CONSTRAINT fk_resposta_topico
        FOREIGN KEY (topico_id)
            REFERENCES topicos (id),

    CONSTRAINT fk_resposta_autor
        FOREIGN KEY (autor_id)
            REFERENCES usuarios (id)
);