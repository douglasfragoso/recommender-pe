USE db_recommenderpe;
-- Usuário 1
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    30, 100, 2, 'PE', '50000000', 'Masculino', '12345678900', '81-98765-4321', 'Apto 202', 
    'Brasil', 'Douglas', 'Fragoso', 'Boa Viagem', 'Rua Exemplo', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'douglas@example.com'
);

-- Usuário 2
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    25, 45, 1, 'SP', '01311000', 'Feminino', '11122233344', '11-98765-4321', 'Bloco B', 
    'Brasil', 'Ana', 'Santos', 'Bela Vista', 'Avenida Paulista', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'ana.santos@example.com'
);

-- Usuário 3
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    32, 78, 1, 'RJ', '20040002', 'Masculino', '22233344455', '21-91234-5678', 'Sala 501', 
    'Brasil', 'Carlos', 'Oliveira', 'Centro', 'Rua Primeiro de Março', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'carlos.oliveira@example.com'
);

-- Usuário 4
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    29, 1023, 1, 'MG', '30170010', 'Feminino', '33344455566', '31-99876-5432', 'Andar 7', 
    'Brasil', 'Fernanda', 'Costa', 'Savassi', 'Rua da Bahia', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'fernanda.costa@example.com'
);

-- Usuário 5
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    40, 33, 1, 'BA', '40000000', 'Masculino', '44455566677', '71-91234-1234', 'Fundos', 
    'Brasil', 'Rafael', 'Almeida', 'Pelourinho', 'Largo do Cruzeiro', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'rafael.almeida@example.com'
);

-- Usuário 6
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    27, 567, 1, 'RS', '90000000', 'Outro', '55566677788', '51-94567-8901', 'Galpão 2', 
    'Brasil', 'Juliana', 'Pereira', 'Moinhos de Vento', 'Avenida Goethe', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'juliana.pereira@example.com'
);

-- Usuário 7
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    35, 89, 1, 'PR', '80000000', 'Masculino', '66677788899', '41-95678-2345', 'Cobertura', 
    'Brasil', 'Lucas', 'Rodrigues', 'Batel', 'Avenida do Batel', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'lucas.rodrigues@example.com'
);

-- Usuário 8
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    22, 12, 1, 'CE', '60000000', 'Feminino', '77788899900', '85-97865-4321', 'Casa Azul', 
    'Brasil', 'Patrícia', 'Nascimento', 'Meireles', 'Rua Maria Tomásia', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'patricia.nascimento@example.com'
);

-- Usuário 9
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    31, 150, 1, 'AM', '69000000', 'Masculino', '88899900011', '92-93456-7890', 'Beco 2', 
    'Brasil', 'Marcos', 'Souza', 'Centro', 'Rua 10 de Julho', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'marcos.souza@example.com'
);

-- Usuário 10
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    28, 300, 1, 'ES', '29000000', 'Feminino', '99900011122', '27-91234-5678', 'Loja 5', 
    'Brasil', 'Isabela', 'Martins', 'Jardim da Penha', 'Avenida Nossa Senhora da Penha', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'isabela.martins@example.com'
);

-- Usuário 11
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    45, 1001, 1, 'DF', '70000000', 'Outro', '00011122233', '61-99876-5432', 'Setor Comercial', 
    'Brasil', 'Ricardo', 'Ferreira', 'Asa Sul', 'Quadra 302', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'ricardo.ferreira@example.com'
);

-- Usuário 12 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    35, 150, 2, 'PE', '50740000', 'Masculino', '11122233399', '81-91234-5678', 'Casa 5', 
    'Brasil', 'Roberto', 'Silveira', 'Cordeiro', 'Rua do Futuro', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'roberto.silveira@example.com'
);

-- Usuário 13 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    28, 250, 2, 'SP', '01415000', 'Feminino', '22233344488', '11-92345-6789', 'Apto 101', 
    'Brasil', 'Beatriz', 'Albuquerque', 'Cerqueira César', 'Alameda Santos', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'beatriz.albuquerque@example.com'
);

-- Usuário 14 (Senha: Carioca@2024)
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    42, 30, 2, 'RJ', '22070001', 'Masculino', '33344455577', '21-93456-7890', 'Bloco C', 
    'Brasil', 'Eduardo', 'Pimentel', 'Copacabana', 'Avenida Nossa Senhora de Copacabana', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'eduardo.pimentel@example.com'
);

-- Usuário 15 (Senha: Mineira@123)
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    31, 400, 2, 'MG', '30190000', 'Feminino', '44455566666', '31-94567-8901', 'Sala 302', 
    'Brasil', 'Carolina', 'Andrade', 'Lourdes', 'Rua da Bahia', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'carolina.andrade@example.com'
);

-- Usuário 16 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    29, 22, 2, 'BA', '40110000', 'Outro', '55566677755', '71-95678-9012', 'Fundos', 
    'Brasil', 'Alex', 'Santana', 'Barra', 'Avenida Oceânica', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'alex.santana@example.com'
);

-- Usuário 17 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    45, 180, 2, 'RS', '90450090', 'Masculino', '66677788844', '51-96789-0123', 'Casa Amarela', 
    'Brasil', 'Rodrigo', 'Gonçalves', 'Moinhos de Vento', 'Rua Padre Chagas', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'rodrigo.goncalves@example.com'
);

-- Usuário 18 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    26, 90, 2, 'CE', '60160000', 'Feminino', '77788899933', '85-97890-1234', 'Apto 502', 
    'Brasil', 'Vanessa', 'Castro', 'Aldeota', 'Avenida Dom Luís', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'vanessa.castro@example.com'
);

-- Usuário 19 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    38, 600, 2, 'AM', '69050000', 'Masculino', '88899900022', '92-98901-2345', 'Conjunto 10', 
    'Brasil', 'Fábio', 'Oliveira', 'Adrianópolis', 'Avenida Joaquim Nabuco', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'fabio.oliveira@example.com'
);

-- Usuário 20 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    33, 45, 2, 'ES', '29055070', 'Feminino', '99900011111', '27-99123-4567', 'Sobrado 3', 
    'Brasil', 'Daniela', 'Ramos', 'Praia do Canto', 'Rua Professor Fernando Duarte Rabelo', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'daniela.ramos@example.com'
);

-- Usuário 21 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    27, 1200, 2, 'DF', '70297000', 'Outro', '00011122200', '81-99234-5678', 'Quadra 104', 
    'Brasil', 'Bruno', 'Lima', 'Asa Norte', 'CLN 104 Bloco C', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'bruno.lima@example.com'
);

-- Usuário 22 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    32, 75, 2, 'PB', '58015000', 'Masculino', '12345678911', '83-91234-5678', 'Casa 2', 
    'Brasil', 'Marcelo', 'Cavalcanti', 'Tambau', 'Avenida Almirante Tamandaré', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'marcelo.cavalcanti@example.com'
);

-- Usuário 23 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    29, 88, 2, 'MA', '65010000', 'Feminino', '23456789122', '98-92345-6789', 'Apto 301', 
    'Brasil', 'Tatiana', 'Sousa', 'Renascença', 'Rua Grande', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'tatiana.sousa@example.com'
);

-- Usuário 24 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    41, 120, 2, 'MT', '78005000', 'Masculino', '34567891233', '65-93456-7890', 'Quadra 5', 
    'Brasil', 'Ricardo', 'Nogueira', 'Jardim Cuiabá', 'Rua Galdino Pimentel', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'ricardo.nogueira@example.com'
);

-- Usuário 25 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    26, 340, 2, 'GO', '74030000', 'Feminino', '45678912344', '62-94567-8901', 'Lote 7', 
    'Brasil', 'Fernanda', 'Lima', 'Setor Marista', 'Avenida 85', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'fernanda.lima@example.com'
);

-- Usuário 26 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    37, 55, 2, 'TO', '77020000', 'Outro', '56789123455', '63-95678-9012', 'Conjunto 3', 
    'Brasil', 'Patricia', 'Oliveira', 'Plano Diretor Sul', 'Avenida NS-2', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'patricia.oliveira@example.com'
);

-- Usuário 27 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    44, 230, 2, 'RO', '78900000', 'Masculino', '67891234566', '69-96789-0123', 'KM 3', 
    'Brasil', 'Roberto', 'Santos', 'Nacional', 'Rua Rui Barbosa', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'roberto.santos@example.com'
);

-- Usuário 28 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    31, 18, 2, 'AC', '69900000', 'Feminino', '78912345677', '68-97890-1234', 'Conjunto 8', 
    'Brasil', 'Juliana', 'Martins', 'Base', 'Rua Benjamin Constant', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'juliana.martins@example.com'
);

-- Usuário 29 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    28, 72, 2, 'RR', '69305000', 'Masculino', '89123456788', '95-98901-2345', 'Apto 102', 
    'Brasil', 'Carlos', 'Ferreira', 'Centro', 'Avenida Capitão Júlio Bezerra', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'carlos.ferreira@example.com'
);

-- Usuário 30
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    35, 95, 2, 'AP', '68900000', 'Feminino', '91234567899', '96-99123-4567', 'Casa Verde', 
    'Brasil', 'Amanda', 'Rocha', 'Trem', 'Avenida Fab', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'amanda.rocha@example.com'
);

-- Usuário 31 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    40, 110, 2, 'SE', '49010000', 'Outro', '12345678000', '79-99234-5678', 'Bloco D', 
    'Brasil', 'Alexandre', 'Alves', 'Jardins', 'Avenida Beira Mar', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'alexandre.alves@example.com'
);

-- Usuário 32 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    33, 200, 2, 'PI', '64000000', 'Masculino', '23456789111', '86-91234-5678', 'Quadra 10', 
    'Brasil', 'Felipe', 'Carvalho', 'Jóquei', 'Avenida Frei Serafim', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'felipe.carvalho@example.com'
);

-- Usuário 33 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    27, 45, 2, 'AL', '57000000', 'Feminino', '34567891222', '82-92345-6789', 'Casa B', 
    'Brasil', 'Larissa', 'Araújo', 'Jatiúca', 'Rua Desportista Claúdio da Silva', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'larissa.araújo@example.com'
);

-- Usuário 34 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    38, 78, 2, 'RN', '59000000', 'Masculino', '45678912333', '84-93456-7890', 'Apto 201', 
    'Brasil', 'Rodrigo', 'Barbosa', 'Petrópolis', 'Avenida Campos Sales', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'rodrigo.barbosa@example.com'
);

-- Usuário 35 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    29, 33, 2, 'CE', '60170000', 'Outro', '56789123444', '85-94567-8901', 'Sobrado', 
    'Brasil', 'Camila', 'Dias', 'Meireles', 'Rua Carlos Vasconcelos', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'camila.dias@example.com'
);

-- Usuário 36
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    42, 120, 2, 'PR', '80010000', 'Feminino', '67891234555', '41-95678-9012', 'Casa 2', 
    'Brasil', 'Gabriela', 'Ribeiro', 'Água Verde', 'Rua Padre Anchieta', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'gabriela.ribeiro@example.com'
);

-- Usuário 37 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    31, 90, 2, 'SC', '88010000', 'Masculino', '78912345666', '48-96789-0123', 'Bloco B', 
    'Brasil', 'Leonardo', 'Cardoso', 'Centro', 'Rua Felipe Schmidt', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'leonardo.cardoso@example.com'
);

-- Usuário 38 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    36, 150, 2, 'MS', '79000000', 'Feminino', '89123456777', '67-97890-1234', 'Chácara 3', 
    'Brasil', 'Isabela', 'Cunha', 'Jardim dos Estados', 'Rua da Paz', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'isabela.cunha@example.com'
);

-- Usuário 39 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    25, 60, 2, 'RO', '78950000', 'Masculino', '91234567888', '69-98901-2345', 'KM 8', 
    'Brasil', 'Vinícius', 'Melo', 'Nova Porto Velho', 'Avenida Governador Jorge Teixeira', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'vinicius.melo@example.com'
);

-- Usuário 40 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    40, 85, 2, 'AC', '69915000', 'Outro', '12345678099', '68-99123-4567', 'Conjunto 4', 
    'Brasil', 'André', 'Farias', 'Bosque', 'Rua Hugo Carneiro', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'andre.farias@example.com'
);

-- Usuário 41 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    28, 110, 2, 'AP', '68909000', 'Feminino', '23456789100', '96-99234-5678', 'Apto 302', 
    'Brasil', 'Beatriz', 'Peixoto', 'Central', 'Avenida Ernestino Borges', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'beatriz.peixoto@example.com'
);

-- Usuário 42 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    45, 220, 2, 'TO', '77060000', 'Masculino', '34567891211', '63-99345-6789', 'Quadra 103', 
    'Brasil', 'Ricardo', 'Santos', 'Plano Diretor Norte', 'Avenida LO-13', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'ricardo.santos@example.com'
);

-- Usuário 43 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    32, 40, 2, 'SE', '49030000', 'Feminino', '45678912322', '79-99456-7890', 'Casa 5', 
    'Brasil', 'Daniela', 'Lopes', 'Atalaia', 'Avenida Beira Mar', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'daniela.lopes@example.com'
);

-- Usuário 44 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    29, 75, 2, 'AL', '57030000', 'Masculino', '56789123433', '82-99567-8901', 'Bloco C', 
    'Brasil', 'Eduardo', 'Nunes', 'Ponta Verde', 'Rua Sargento José da Silva', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'eduardo.nunes@example.com'
);

-- Usuário 45 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    37, 95, 2, 'RN', '59020000', 'Outro', '67891234544', '84-99678-9012', 'Apto 401', 
    'Brasil', 'Fernanda', 'Oliveira', 'Tirol', 'Rua Apodi', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'fernanda.oliveira@example.com'
);

-- Usuário 46 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    26, 130, 2, 'PI', '64030000', 'Feminino', '78912345655', '86-99789-0123', 'Conjunto 6', 
    'Brasil', 'Gustavo', 'Marques', 'Ilhotas', 'Rua Álvaro Mendes', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'gustavo.marques@example.com'
);

-- Usuário 47 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    43, 65, 2, 'MA', '65030000', 'Masculino', '89123456766', '98-99890-1234', 'Casa 4', 
    'Brasil', 'Helena', 'Castro', 'Calhau', 'Avenida dos Holandeses', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'helena.castro@example.com'
);

-- Usuário 48 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    30, 180, 2, 'MT', '78030000', 'Feminino', '91234567877', '65-99901-2345', 'Lote 8', 
    'Brasil', 'Igor', 'Fernandes', 'Jardim Itália', 'Rua General Valle', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'igor.fernandes@example.com'
);

-- Usuário 49 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    34, 25, 2, 'GO', '74060000', 'Masculino', '12345678988', '62-99123-4567', 'Apto 601', 
    'Brasil', 'Juliana', 'Gomes', 'Setor Oeste', 'Rua 9', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'juliana.gomes@example.com'
);

-- Usuário 50 
INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) VALUES (
    39, 500, 2, 'DF', '70070000', 'Outro', '23456789199', '61-99234-5679', 'Quadra 305', 
    'Brasil', 'Kaio', 'Henrique', 'Sudoeste', 'CLSW 302 Bloco B', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'kaio.henrique@example.com'
);


