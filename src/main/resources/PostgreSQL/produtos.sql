--
-- Estrutura da tabela "produto"
--

DROP TABLE IF EXISTS "produto" CASCADE;

CREATE TABLE produto (
  id     bigserial NOT NULL,
  nome   varchar(60) NOT NULL,
  categoria_id   integer,
  preco_compra  numeric,
  preco_venda  numeric,

  /* Keys */
  CONSTRAINT produto_pkey
    PRIMARY KEY (id)
);

COMMENT ON TABLE produto
  IS 'Produtos do Mercado';

--
-- Inserindo dados na tabela "produto"
--

INSERT INTO "produto" (id, nome, categoria_id, preco_compra, preco_venda) VALUES
(1, 'agua 500ml', 1, 0.50, 1.00),
(2, 'agua 1500ml', 1, 1.17, 2.00),
(3, 'agua 20l', 1, 2.70,   4.00),
(4, 'cerveja 473ml', 1, 33.50, 40.00),
(5, 'cerveja 350ml', 1, 27.45, 34.00),
(6, 'cerveja 1l', 1, 55.00, 72.00),
(7, 'azeite', 2, 17.00, 23.00),
(8, 'azeitona', 2, 1.49, 2.00),
(9, 'extrato de tomate', 2, 1.49, 2.00),
(10, 'creme de leite', 2, 2.49, 3.50),
(11, 'leite condensado', 2, 3.59, 4.70),
(12, 'leite de coco', 2, 2.19, 2.80),
(13, 'margarina', 2, 2.19, 3.00),
(14, 'vinagre', 2, 1.59, 2.50),
(15, 'oleo', 2, 7.59, 11.00),
(16, 'papel higienico', 3, 2.59, 4.00),
(17, 'detergente', 3, 1.18, 1.50),
(18, 'agua sanitaria', 3, 1.49, 2.00),
(19, 'macarrão', 2, 2.31, 3.00),
(20, 'cafe', 2, 4.99, 6.50),
(21, 'arroz', 2, 4.55, 5.80),
(22, 'feijao', 2, 8.49, 11.00),
(23, 'farinha', 2, 3.59, 4.70),
(24, 'sal', 2, 0.75, 1.00),
(25, 'trigo', 2, 3.77, 5.00);
