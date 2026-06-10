CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(50),
    instagram VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS kombuskinis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cor VARCHAR(50) NOT NULL,
    quantidade_nos INT NOT NULL,
    tipo_divisoes VARCHAR(100),
    quantidade_divisoes INT NOT NULL,
    tipo_cruz VARCHAR(100),
    has_tassel TINYINT(1) NOT NULL,
    preco DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_pedido (
    pedido_id INT NOT NULL,
    kombuskini_id INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (pedido_id, kombuskini_id),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (kombuskini_id) REFERENCES kombuskinis(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estoque_kombuskini (
    kombuskini_id INT PRIMARY KEY,
    quantidade INT NOT NULL DEFAULT 0,
    FOREIGN KEY (kombuskini_id) REFERENCES kombuskinis(id) ON DELETE CASCADE
);
