create database carrinho_db;
use carrinho_db;

CREATE TABLE carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrinho_id BIGINT,
    produto_id BIGINT NOT NULL,
    nome_produto VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (carrinho_id) REFERENCES carrinho(id)
);

DROP TABLE item_carrinho;
DROP TABLE carrinho;

CREATE TABLE carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ABERTO',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE item_carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrinho_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (carrinho_id) REFERENCES carrinho(id)
);

INSERT INTO item_carrinho (produto_id, quantidade, preco, carrinho_id)
VALUES (101, 2, 49.99, 1);

