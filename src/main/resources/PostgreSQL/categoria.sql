--
-- Estrutura da tabela "categoria"
--

DROP TABLE IF EXISTS "categoria" CASCADE;

CREATE TABLE categoria (
  id     bigserial NOT NULL,
  nome   varchar(60) NOT NULL,

  /* Keys */
  CONSTRAINT categoria_pkey
    PRIMARY KEY (id)
);

COMMENT ON TABLE categoria
  IS 'Categoria dos produtos';

--
-- Inserindo dados na tabela "categoria"
--

INSERT INTO "categoria" (id, nome) VALUES
(1, 'bebidas'),
(2, 'mercearia'),
(3, 'limpeza');