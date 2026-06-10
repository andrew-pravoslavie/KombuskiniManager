CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS item_pedido (
    pedido_id INT NOT NULL,
    kombuskini_id INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (pedido_id, kombuskini_id),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estoque_kombuskini (
    kombuskini_id INT PRIMARY KEY,
    quantidade INT NOT NULL DEFAULT 0
);
