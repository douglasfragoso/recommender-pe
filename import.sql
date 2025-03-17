INSERT INTO tb_users (
    age, house_number, roles, states, zip_code, gender, cpf, phone, complement, country,
    first_name, last_name, neighborhood, street, user_password, email
) 
VALUES (
    30, 100, 2, 'PE', '50000000', 'Masculino', '12345678900', '81-98765-4321', 'Apto 202', 
    'Brasil', 'Douglas', 'Fragoso', 'Boa Viagem', 'Rua Exemplo', 
    '$2a$12$xCCH6JiTOTC0IDjWgBKVQeqS53c/Kw6UuKi69b/iY6lGowC.P0RzW', 'douglas@example.com'
);

-- POI ID 1
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  1, 
  'Basílica Nossa Senhora do Carmo', 
  'A Basílica e Convento de Nossa Senhora do Carmo é um conjunto moderno católico pertencente à Ordem Carmelita, localizada na cidade do Recife, em Pernambuco, Brasil. O imponente frontispício da Basílica do Carmo possui muitas volutas esculpidas em pedra, e a torre, de 50 metros de altura — a mais alta torre barroca do Brasil —, é encimada por um dos mais bulbos feitos do estilo no país. No interior, a decoração em talha dourada e a estatuária são de valor inestimável, destacando-se a capela-mor e seu fabuloso retábulo com imagens de Nossa Senhora do Carmo e dos profetas Elias e Eliseu. No seu pátio, a cabeça do líder quilombola Zumbi dos Palmares ficou exposta até completa conclusão. No convento, encontram-se enterrados os restos mortais de Frei Caneca.', 
  'Av. Dantas Barreto', 
  646, 
  NULL, 
  'Santo Antônio', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50020000'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (1, 'HERITAGE'), (1, 'SPIRITUALITY'), (1, 'CULTURE'), (1, 'APPRECIATION'), (1, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (1, 'ART'), (1, 'PHOTOGRAPHY'), (1, 'LEARNING'), (1, 'TRAVELING'), (1, 'READING');
INSERT INTO pois_themes (poi_id, theme) VALUES (1, 'CULTURAL'), (1, 'RELIGIOUS'), (1, 'HISTORY'), (1, 'COLONIAL'), (1, 'LUXURY');

-- POI ID 2
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  2, 
  'Bistrô Negra Linda', 
  'Culinária da Ilha de Deus Veja as opções de receitas tradicionais da Chef, confira os pratos típicos da culinária da Ilha de Deus que nós preparamos de maneira única! No cardápio você encontra Mariscadas, Siri no Casco, Pasteis de Sururu, Moqueca da Ilha, Escondidinho de Sururu, Peixes, Escudinho de Camarão e vários pratos diferentes que a Chef cria todos os dias.', 
  'Estr. do Encanamento', 
  675, 
  NULL, 
  'Casa Forte', 
  'Recife', 
  'PE', 
  'Brasil', 
  '52070000'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (2, 'APPRECIATION'), (2, 'MULTISENSORY'), (2, 'SOCIAL'), (2, 'TRADITION'), (2, 'ENTERTAINMENT');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (2, 'GASTRONOMY'), (2, 'SOCIAL'), (2, 'TRAVELING'), (2, 'ART'), (2, 'LEARNING');
INSERT INTO pois_themes (poi_id, theme) VALUES (2, 'GASTRONOMIC'), (2, 'CULTURAL'), (2, 'FAMILY_FRIENDLY'), (2, 'MODERN'), (2, 'NATURE');

-- POI ID 3
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  3, 
  'Cachaçaria Carvalheira', 
  'Entre mais de 2 mil barris de carvalho e sob o aroma da mais pura cachaça envelhecendo, cordas, gamelas, moendas e alambiques antigos dão a Cachaçaria Carvalheira um clima todo especial, cheio de charme, história e tradição. Aqui, você vai saber como uma das melhores cachaças do Brasil é fabricada e passear entre os bairros onde a mais pura bebida nacional descansa durante anos até estar pronto para ser engarrafado e consumido. Depois da visitação às instalações da fábrica, uma deliciosa degustação vai deixar a Cachaça Carvalheira para sempre nas suas melhores lembranças.', 
  'Av. Sul Gov. Cid Sampaio', 
  4921, 
  NULL, 
  'Imbiribeira', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50770011'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (3, 'HERITAGE'), (3, 'TRADITION'), (3, 'MULTISENSORY'), (3, 'APPRECIATION'), (3, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (3, 'LEARNING'), (3, 'COLLECTING'), (3, 'TRAVELING'), (3, 'PHOTOGRAPHY'), (3, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (3, 'GASTRONOMIC'), (3, 'CULTURAL'), (3, 'HISTORY'), (3, 'NATURE'), (3, 'LUXURY');

-- POI ID 4
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  4, 
  'Cais do Sertão', 
  'Um dos mais modernos equipamentos culturais do Brasil, o Cais do Sertão, instalado no antigo Armazém 10 do Porto do Recife, é um local de convivência, diversão e conhecimento, polo gerador de novas ideias e experiências. Abrigando e reverenciando a obra de Luiz Gonzaga, o grande homenageado do espaço, traz para a beira-mar da capital do estado um pouco do solo rico e generoso da cultura popular do sertão. O museu teve como curadora e diretora de criação a socióloga pernambucana Isa Grinspum Ferraz, também autora do Museu da Língua Portuguesa em São Paulo.', 
  'Av. Alfredo Lisboa', 
  NULL, 
  'Armazen 12', 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030150'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (4, 'CULTURE'), (4, 'EXPLORATION'), (4, 'CREATIVITY'), (4, 'IDENTITY'), (4, 'SOCIAL');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (4, 'LEARNING'), (4, 'ART'), (4, 'TECH'), (4, 'MUSIC'), (4, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (4, 'CULTURAL'), (4, 'MODERN'), (4, 'HISTORY'), (4, 'FAMILY_FRIENDLY'), (4, 'FOLKLORE');

-- POI ID 5
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  5, 
  'Caixa Cultural', 
  'A Caixa Cultural Recife é um espaço cultural localizado na cidade do Recife, capital do estado de Pernambuco, Brasil. Integra um conjunto de sete centros culturais criados pela Caixa Econômica Federal em capitais brasileiras. A Caixa Cultural Recife foi inaugurada em 2012 no edifício do antigo Bank of London & South America Limited, localizado defronte ao Marco Zero, no Recife Antigo. Conta com dois pavimentos de galerias de arte, teatro com 96 lugares, sala multimídia, duas salas para escritórios de arte-educação e amplo foyer.', 
  'Av. Alfredo Lisboa', 
  505, 
  NULL, 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030150'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (5, 'CULTURE'), (5, 'ENTERTAINMENT'), (5, 'SOCIAL'), (5, 'APPRECIATION'), (5, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (5, 'ART'), (5, 'THEATER'), (5, 'PHOTOGRAPHY'), (5, 'SOCIAL'), (5, 'LEARNING');
INSERT INTO pois_themes (poi_id, theme) VALUES (5, 'CULTURAL'), (5, 'HISTORY'), (5, 'MODERN'), (5, 'FAMILY_FRIENDLY'), (5, 'URBAN_ART');

-- POI ID 6
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  6, 
  'Capela Dourada', 
  'A Capela dos Noviços da Ordem Terceira de São Francisco de Assis ou Capela Dourada é uma capela da Ordem Franciscana localizada na cidade do Recife, capital do estado brasileiro de Pernambuco. Com construção iniciada em 1696, foi o primeiro templo do Brasil a ser integralmente coberto de decoração barroca. Está situado dentro do complexo de edifícios do Convento e Igreja de Santo Antônio, que inclui a Igreja da Ordem Terceira de São Francisco e o Museu Franciscano de Arte Sacra.', 
  'R. do Imperador Pedro II', 
  NULL, 
  'Altura da rua Siqueira Campos', 
  'Santo Antônio', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50010240'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (6, 'HERITAGE'), (6, 'SPIRITUALITY'), (6, 'APPRECIATION'), (6, 'EXPLORATION'), (6, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (6, 'ART'), (6, 'PHOTOGRAPHY'), (6, 'LEARNING'), (6, 'TRAVELING'), (6, 'READING');
INSERT INTO pois_themes (poi_id, theme) VALUES (6, 'CULTURAL'), (6, 'RELIGIOUS'), (6, 'HISTORY'), (6, 'LUXURY'), (6, 'COLONIAL');

-- POI ID 7
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  7, 
  'Casa da Cultura', 
  'A Casa da Cultura é um centro de comercialização de artesanato da cidade do Recife, capital do estado brasileiro de Pernambuco. Funciona no edifício da antiga Casa de Detenção do Recife, que foi a maior cadeia do Brasil no século XIX e a primeira prisão radial pan-ótica da América do Sul. Foi também, durante a Ditadura Civil-militar, um dos antros das torturas e assassinatos, sendo um de seus destaques Amaro Luiz de Carvalho.', 
  'R. Floriano Peixoto', 
  NULL, 
  NULL, 
  'São José', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50020060'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (7, 'HERITAGE'), (7, 'STUDY'), (7, 'EXPLORATION'), (7, 'APPRECIATION'), (7, 'CULTURE ');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (7, 'ART'), (7, 'COLLECTING'), (7, 'PHOTOGRAPHY'), (7, 'LEARNING'), (7, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (7, 'CULTURAL'), (7, 'HISTORY'), (7, 'MODERN'), (7, 'FAMILY_FRIENDLY'), (7, 'URBAN_ART');

-- POI ID 8
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  8, 
  'Catamaran Tours', 
  'Pioneira nos segmentos de passeios náuticos em Pernambuco, a Catamaran Tours tem se destacado há 30 anos no cenário turístico local por todos os avanços conquistados, 20 deles em pleno coração da capital pernambucana. Foi uma empresa, comandada pela família Britto, que tornou a navegação de turismo pelos rios que cortam o centro do Recife em um dos programas mais procurados por locais e turistas.', 
  'Cais Santa Rita', 
  NULL, 
  NULL, 
  'São José', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50020360'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (8, 'ENTERTAINMENT'), (8, 'EXPLORATION'), (8, 'CULTURE'), (8, 'SOCIAL'), (8, 'RELAXATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (8, 'ADVENTURE'), (8, 'TRAVELING'), (8, 'PHOTOGRAPHY'), (8, 'SOCIAL'), (8, 'LEARNING');
INSERT INTO pois_themes (poi_id, theme) VALUES (8, 'ADVENTURE'), (8, 'CULTURAL'), (8, 'FAMILY_FRIENDLY'), (8, 'NATURE'), (8, 'MODERN');

-- POI ID 9
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  9, 
  'Centro de Artesanato de Pernambuco', 
  'Localizado em frente ao Marco Zero, no Bairro do Recife, o Centro de Artesanato de Pernambuco é uma iniciativa de economia criativa do Estado. Inaugurado em 2012, reúne mais de 30 mil peças de cerca de 2 mil artesãos de todas as regiões pernambucanas.', 
  'Av. Alfredo Lisboa', 
  NULL, 
  'Armazém 12', 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030150'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (9, 'CULTURE'), (9, 'APPRECIATION'), (9, 'TRADITION'), (9, 'EXPLORATION'), (9, 'SOCIAL');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (9, 'ART'), (9, 'COLLECTING'), (9, 'PHOTOGRAPHY'), (9, 'TRAVELING'), (9, 'LEARNING');
INSERT INTO pois_themes (poi_id, theme) VALUES (9, 'CULTURAL'), (9, 'FAMILY_FRIENDLY'), (9, 'MODERN'), (9, 'NATURE'), (9, 'URBAN_ART');

-- POI ID 10
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  10, 
  'Concatedral de São Pedro dos Clérigos', 
  'A Concatedral de São Pedro dos Clérigos, também conhecida simplesmente como Igreja de São Pedro dos Clérigos, é um templo católico situado no município do Recife. A área defronte à Igreja, chamada de Pátio de São Pedro, conserva em seu entorno um belo conjunto de 29 casas baixas coloniais.', 
  'R. das Águas Verdes', 
  64, 
  'Pátio de São Pedro', 
  'São José', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50020240'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (10, 'HERITAGE'), (10, 'SPIRITUALITY'), (10, 'TRADITION'), (10, 'APPRECIATION'), (10, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (10, 'ART'), (10, 'PHOTOGRAPHY'), (10, 'TRAVELING'), (10, 'LEARNING'), (10, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (10, 'RELIGIOUS'), (10, 'CULTURAL'), (10, 'HISTORY'), (10, 'LUXURY'), (10, 'COLONIAL');

-- POI ID 11
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  11, 
  'Embaixada Pernambuco', 
  'Em Junho de 2015 inauguramos um novo museu, a Embaixada de Pernambuco. Localizado na Praça do Arsenal no Bairro do Recife, conta com um acervo em tamanho natural dos principais ícones de Pernambuco, como Lampião e Maria Bonita, além de apresentações ao vivo de Frevo.', 
  'R. do Apolo', 
  234, 
  NULL, 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030220'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (11, 'CULTURE'), (11, 'ENTERTAINMENT'), (11, 'TRADITION'), (11, 'APPRECIATION'), (11, 'IDENTITY ');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (11, 'ART'), (11, 'DANCING'), (11, 'PHOTOGRAPHY'), (11, 'TRAVELING'), (11, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (11, 'CULTURAL'), (11, 'MODERN'), (11, 'FAMILY_FRIENDLY'), (11, 'HISTORY'), (11, 'ADVENTURE');

-- POI ID 12
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  12, 
  'Embaixada dos Bonecos Gigantes', 
  'A Embaixada dos Bonecos Gigantes é uma exposição permanente de bonecos gigantes (também conhecidos como "bonecos de Olinda"), alegorias populares do Carnaval de Pernambuco. O local expõe 30 bonecos gigantes, incluindo ícones como Alceu Valença, Michael Jackson e Luiz Inácio Lula da Silva.', 
  'R. do Bom Jesus', 
  183, 
  NULL, 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030170'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (12, 'CULTURE'), (12, 'ENTERTAINMENT'), (12, 'TRADITION'), (12, 'APPRECIATION'), (12, 'SOCIAL');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (12, 'ART'), (12, 'PHOTOGRAPHY'), (12, 'LEARNING'), (12, 'TRAVELING'), (12, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (12, 'CULTURAL'), (12, 'FAMILY_FRIENDLY'), (12, 'HISTORY'), (12, 'FOLKLORE'), (12, 'ADVENTURE');

-- POI ID 13
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  13, 
  'Forte das Cinco Pontas', 
  'Foi instalado em 1982, no Forte de São Tiago das Cinco Pontas, na zona sul da cidade. Os documentos iconográficos que compõem o acervo do museu são voltados para a preservação da história urbana, cultural e social do Recife.', 
  'Praça das Cinco Pontas', 
  NULL, 
  NULL, 
  'São José', 
  'Recife', 
  'PE', 
  'Brasil', 
  '50020500'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (13, 'HERITAGE'), (13, 'STUDY'), (13, 'APPRECIATION'), (13, 'EXPLORATION'), (13, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (13, 'LEARNING'), (13, 'PHOTOGRAPHY'), (13, 'COLLECTING'), (13, 'TRAVELING'), (13, 'READING');
INSERT INTO pois_themes (poi_id, theme) VALUES (13, 'HISTORY'), (13, 'CULTURAL'), (13, 'FAMILY_FRIENDLY'), (13, 'MODERN'), (13, 'COLONIAL ');

-- POI ID 14
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
14,
'Forte do Brum',
'O Forte de São João Batista do Brum localiza-se no bairro do Recife, na cidade do mesmo nome, no estado de Pernambuco, no Brasil. Administrado pelo Exército brasileiro, encontra-se restaurado e aberto ao público, abrigando, desde 5 de janeiro de 1987, o Museu Militar do Forte do Brum (MMFB), que exibe armamento e peças arqueológicas. No seu interior destaca-se a Capela de São João Batista, em estilo maneirista.',
'Praça da Comunidade Luso Brasileira',
NULL,
NULL,
NULL,
'Recife',
'PE',
'Brasil',
'50030270'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (14, 'CULTURE'), (14, 'HERITAGE'), (14, 'SPIRITUALITY'), (14, 'APPRECIATION'), (14, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (14, 'ADVENTURE'), (14, 'LEARNING'), (14, 'PHOTOGRAPHY'), (14, 'COLLECTING'), (14, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (14, 'HISTORY'), (14, 'CULTURAL'), (14, 'ADVENTURE'), (14, 'RELIGIOUS'), (14, 'COLONIAL ');

-- POI ID 15
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
15,
'Fundação Gilberto Freyre',
'A Fundação Gilberto Freyre é uma instituição cultural cuja sede está localizada na Casa-Museu Magdalena e Gilberto Freyre, na cidade do Recife, capital de Pernambuco, Brasil. Foi criada em 11 de março de 1987 com o objetivo de contribuir para o desenvolvimento político-social, científico-tecnológico e cultural da sociedade brasileira, tendo como base as influências de Gilberto Freyre.',
'Rua Jorge Tasso Neto',
NULL,
'Primeiro Portão',
'Apipucos',
'Recife',
'PE',
'Brasil',
'52071440'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (15, 'CULTURE'), (15, 'STUDY'), (15, 'APPRECIATION'), (15, 'EXPLORATION'), (15, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (15, 'LEARNING'), (15, 'READING'), (15, 'ART'), (15, 'TRAVELING'), (15, 'PHOTOGRAPHY');
INSERT INTO pois_themes (poi_id, theme) VALUES (15, 'CULTURAL'), (15, 'HISTORY'), (15, 'MODERN'), (15, 'FAMILY_FRIENDLY'), (15, 'COLONIAL');

-- POI ID 16
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
16,
'Igreja de Santa Tereza D’Avila',
'Construída em 1700 e concluída sem as pinturas e a torre sineira que conhecemos hoje no ano de 1710, possui grande conjunto de pinturas dedicadas à vida de Santa Teresa de Jesus, que cobrem todo o forro e algumas paredes do templo. O autor das imagens foi João de Deus Sepúlveda, também responsável pela obra-prima da pintura barroca pernambucana presente no forro da Concatedral de São Pedro dos Clérigos.',
NULL,
NULL,
NULL,
'Santo Antônio',
'Recife',
'PE',
'Brasil',
'50020230'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (16, 'HERITAGE'), (16, 'SPIRITUALITY'), (16, 'APPRECIATION'), (16, 'TRADITION'), (16, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (16, 'ART'), (16, 'PHOTOGRAPHY'), (16, 'LEARNING'), (16, 'TRAVELING'), (16, 'READING');
INSERT INTO pois_themes (poi_id, theme) VALUES (16, 'RELIGIOUS'), (16, 'CULTURAL'), (16, 'HISTORY'), (16, 'LUXURY'), (16, 'COLONIAL');

-- POI ID 17
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
17,
'Igreja Madre de Deus',
'A Igreja Madre de Deus é um templo católico da cidade do Recife, capital do estado brasileiro de Pernambuco. Situa-se ao lado do prédio do antigo Convento dos Padres Oratorianos. O interior da nave é um amplo salão com capelas laterais e um majestoso arco triunfal de diversas cantorias com dois altares no arco cruzeiro. O altar-mor é de talha dourada com nichos sacrários e trono em estilo rococó. A sacristia possui grande arcaz e um lavabo em mármore de Estremoz considerado o mais notável do Brasil.',
'R. Me. de Deus',
NULL,
NULL,
NULL,
'Recife',
'PE',
'Brasil',
'50030100'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (17, 'HERITAGE'), (17, 'SPIRITUALITY'), (17, 'APPRECIATION'), (17, 'TRADITION'), (17, 'EXPLORATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (17, 'ART'), (17, 'PHOTOGRAPHY'), (17, 'LEARNING'), (17, 'TRAVELING'), (17, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (17, 'RELIGIOUS'), (17, 'CULTURAL'), (17, 'HISTORY'), (17, 'LUXURY'), (17, 'COLONIAL');

-- POI ID 18
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
18,
'Igreja Nossa Senhora do Rosário dos Homens Pretos',
'Fundada em 1662, pela Venerável Confraria de Nossa Senhora do Rosário dos Homens Pretos de Santo Antônio, é a segunda igreja fundada por uma irmandade de negros do Brasil. No entorno da igreja surgiu o primeiro folguedo afro-brasileiro: a congada. O culto a Nossa Senhora do Rosário foi introduzido na cultura dos escravos africanos no Brasil pelos jesuítas na catequese.',
'R. Estreita do Rosário',
NULL,
NULL,
'Santo Antônio',
'Recife',
'PE',
'Brasil',
'50010140'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (18, 'CULTURE'), (18, 'TRADITION'), (18, 'SPIRITUALITY'), (18, 'APPRECIATION'), (18, 'IDENTITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (18, 'ART'), (18, 'DANCING'), (18, 'LEARNING'), (18, 'MUSIC'), (18, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (18, 'CULTURAL'), (18, 'RELIGIOUS'), (18, 'HISTORY'), (18, 'FAMILY_FRIENDLY'), (18, 'AFRO_BRAZILIAN');

-- POI ID 19
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
19,
'Instituto Ricardo Brennand',
'O Instituto Ricardo Brennand é uma instituição cultural brasileira sem fins lucrativos localizada na cidade do Recife. Possui a maior coleção mundial do pintor holandês Frans Post e um dos maiores acervos de armas brancas do mundo, com mais de 3 mil peças, incluindo 27 armaduras medievais completas.',
'R. Mário Campelo',
700,
NULL,
'Várzea',
'Recife',
'PE',
'Brasil',
'50741904'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (19, 'HERITAGE'), (19, 'APPRECIATION'), (19, 'EXPLORATION'), (19, 'STUDY'), (19, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (19, 'ART'), (19, 'COLLECTING'), (19, 'PHOTOGRAPHY'), (19, 'LEARNING'), (19, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (19, 'CULTURAL'), (19, 'HISTORY'), (19, 'LUXURY'), (19, 'COLONIAL'), (19, 'NATURE');

-- POI ID 20
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
20,
'Jardim Botânico do Recife',
'O Jardim Botânico do Recife desenvolve atividades em educação ambiental, como caminhadas ecológicas, exposição permanente da mata atlântica, exibição de vídeos com temas ambientais e visitas a viveiros de plantas medicinais e florestais. Possui um orquidário e um meliponário de abelhas nativas.',
'Rodovia BR 232',
NULL,
'Km 7,5',
'Curado',
'Recife',
'PE',
'Brasil',
'50791540'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (20, 'EXPLORATION'), (20, 'RELAXATION'), (20, 'FAMILY'), (20, 'APPRECIATION'), (20, 'STUDY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (20, 'GARDENING'), (20, 'HIKING'), (20, 'PHOTOGRAPHY'), (20, 'BIRD_WATCHING'), (20, 'LEARNING');
INSERT INTO pois_themes (poi_id, theme) VALUES (20, 'NATURE'), (20, 'FAMILY_FRIENDLY'), (20, 'CULTURAL'), (20, 'MODERN'), (20, 'ADVENTURE');

-- POI ID 21
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
21,
'Museu da Abolição',
'O Museu da Abolição — Centro de Referência da Cultura Afro-Brasileira é um museu localizado na cidade do Recife, capital de Pernambuco, Brasil. Vinculado ao Instituto Brasileiro de Museus (IBRAM) e ao Ministério da Cultura, é um dos raros museus no país a contemplar esta parte da história, e tem suas ações norteadas pelos princípios da nova museologia e da socio museologia. Atualmente o Museu conta com: 130 peças do acervo museológico; 30 metros lineares de acervo bibliográfico; E 2 metros lineares de acervo hemerográfico.',
'R. Benfica',
1150,
NULL,
'Madalena',
'Recife',
'PE',
'Brasil',
'50720001'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (21, 'CULTURE'), (21, 'STUDY'), (21, 'TRADITION'), (21, 'APPRECIATION'), (21, 'IDENTITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (21, 'LEARNING'), (21, 'READING'), (21, 'COLLECTING'), (21, 'ART'), (21, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (21, 'AFRO_BRAZILIAN'), (21, 'COLONIAL'), (21, 'CULTURAL'), (21, 'HISTORY'), (21, 'FAMILY_FRIENDLY');

-- POI ID 22
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
22,
'Museu de Arte Moderna Aloísio Magalhães',
'O Museu de Arte Moderna Aloisio Magalhães ou MAMAM é um museu localizado na cidade do Recife, capital do estado brasileiro de Pernambuco. É considerado um centro de referência da produção moderna e contemporânea das artes visuais. Através da divulgação, registro e reflexão sobre a arte do presente e suas referências históricas, o MAMAM tem contribuído para a formação cultural do público e para o adensamento do meio institucional e artístico do Recife.',
'R. da Aurora',
265,
NULL,
'Boa Vista',
'Recife',
'PE',
'Brasil',
'50060010'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (22, 'CULTURE'), (22, 'APPRECIATION'), (22, 'STUDY'), (22, 'EXPLORATION'), (22, 'CREATIVITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (22, 'ART'), (22, 'PHOTOGRAPHY'), (22, 'LEARNING'), (22, 'SOCIAL'), (22, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (22, 'CULTURAL'), (22, 'MODERN'), (22, 'HISTORY'), (22, 'URBAN_ART'), (22, 'FAMILY_FRIENDLY');

-- POI ID 23
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
23,
'Museu do Estado de Pernambuco',
'O Museu do Estado de Pernambuco (MEPE) é um museu localizado na cidade do Recife, capital de Pernambuco, no Brasil. Possui um grande e eclético acervo, com cerca de 12 mil itens abrangendo as áreas de história, arte, antropologia e etnografia. Seu acervo inclui mobiliário, artes decorativas, documentos e livros históricos, joalheria e etnografia indígena. O Centro de Documentação do Espaço Cícero Dias oferece para consulta uma biblioteca de 4 mil volumes que inclui obras raras.',
'Av. Rui Barbosa',
960,
NULL,
'Graças',
'Recife',
'PE',
'Brasil',
'52050000'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (23, 'HERITAGE'), (23, 'STUDY'), (23, 'APPRECIATION'), (23, 'EXPLORATION'), (23, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (23, 'ART'), (23, 'COLLECTING'), (23, 'LEARNING'), (23, 'READING'), (23, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (23, 'CULTURAL'), (23, 'HISTORY'), (23, 'FAMILY_FRIENDLY'), (23, 'COLONIAL'), (23, 'NATURE');

-- POI ID 24
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
24,
'Museu do Homem do Nordeste',
'O Museu do Homem do Nordeste – Muhne – é um órgão federal (vinculado à Fundação Joaquim Nabuco/Ministério da Educação), que reúne acervos que revelam a pluralidade das culturas negras, indígenas e brancas desde nossas origens até os diferentes desdobramentos e misturas que formam o que hoje é chamado genericamente de cultura brasileira. Fazendo parte do Instituto de Documentação da Fundação Joaquim Nabuco, sua concepção museológica e museográfica foi inspirada no conceito de museu regional, idealizado pelo sociólogo-antropólogo Gilberto Freyre.',
'Av. Dezessete de Agosto',
2187,
NULL,
'Casa Forte',
'Recife',
'PE',
'Brasil',
'52061540'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (24, 'CULTURE'), (24, 'STUDY'), (24, 'TRADITION'), (24, 'APPRECIATION'), (24, 'IDENTITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (24, 'ART'), (24, 'LEARNING'), (24, 'COLLECTING'), (24, 'READING'), (24, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (24, 'CULTURAL'), (24, 'HISTORY'), (24, 'FAMILY_FRIENDLY'), (24, 'MODERN'), (24, 'NATURE');

-- POI ID 25
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
25,
'Museu do Trem',
'Museu do Trem ou Estação Central Capiba/Museu do Trem é um museu ferroviário da cidade do Recife, capital do estado brasileiro de Pernambuco. É considerado o primeiro do Brasil e o segundo do gênero da América Latina. Foi criado em 1972 na antiga Estação Central, edifício do século XIX posteriormente incorporado ao Metrô do Recife. O museu passou a ter como seu patrono Gilberto Freyre, tendo como orientador o Instituto Joaquim Nabuco de Pesquisas Sociais.',
'R.Floriano Peixoto',
NULL,
NULL,
'São José',
'Recife',
'PE',
'Brasil',
'50020060'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (25, 'HERITAGE'), (25, 'EXPLORATION'), (25, 'STUDY'), (25, 'APPRECIATION'), (25, 'TRADITION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (25, 'COLLECTING'), (25, 'LEARNING'), (25, 'TRAVELING'), (25, 'PHOTOGRAPHY'), (25, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (25, 'CULTURAL'), (25, 'HISTORY'), (25, 'MODERN'), (25, 'FAMILY_FRIENDLY'), (25, 'COLONIAL');

-- POI ID 26
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
26,
'Museu Murilo La Greca',
'O Museu Murillo La Greca é um museu brasileiro localizado no bairro do Parnamirim, na cidade do Recife, capital de Pernambuco. Inaugurado em 12 de dezembro de 1985, o acervo é composto principalmente por obras de arte produzidas pelo professor e pintor clássico Murillo La Greca, são 1400 desenhos em que La Greca utilizou diversas técnicas, desde o giz de cera ao pastel. Além dos desenhos há também 160 pinturas entre paisagens, retratos e obras impressionistas. O museu conta também com discotecas, livros e móveis, além de algumas cartas trocadas por artistas com nomes como Candido Portinari.',
'R. Leonardo Bezerra Cavalcante',
366,
NULL,
'Parnamirim',
'Recife',
'PE',
'Brasil',
'52060030'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (26, 'CULTURE'), (26, 'APPRECIATION'), (26, 'STUDY'), (26, 'EXPLORATION'), (26, 'CREATIVITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (26, 'ART'), (26, 'PHOTOGRAPHY'), (26, 'LEARNING'), (26, 'READING'), (26, 'COLLECTING');
INSERT INTO pois_themes (poi_id, theme) VALUES (26, 'CULTURAL'), (26, 'HISTORY'), (26, 'URBAN_ART'), (26, 'FAMILY_FRIENDLY'), (26, 'MODERN');

-- POI ID 27
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
27,
'Oficina Francisco Brennand',
'A Oficina Cerâmica Francisco Brennand é um museu de arte brasileiro localizado na cidade do Recife, capital de Pernambuco. Trata-se de um complexo monumental com aproximadamente 2 mil obras, entre esculturas, murais e objetos cerâmicos. A principal temática é a origem da vida e a evolução das coisas. Conta com espaços como a Accademia, Anfiteatro, Salão de Esculturas e jardins projetados por Burle Marx.',
'R. Diogo de Vasconcelos',
NULL,
'Propriedade Santos Cosme e Damião',
'Várzea',
'Recife',
'PE',
'Brasil',
'50740970'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (27, 'CULTURE'), (27, 'APPRECIATION'), (27, 'EXPLORATION'), (27, 'MULTISENSORY'), (27, 'CREATIVITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (27, 'ART'), (27, 'PHOTOGRAPHY'), (27, 'TRAVELING'), (27, 'LEARNING'), (27, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (27, 'CULTURAL'), (27, 'MODERN'), (27, 'NATURE'), (27, 'URBAN_ART'), (27, 'ADVENTURE');

-- POI ID 28
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
28,
'Paço do Frevo',
'O Paço do Frevo é um espaço cultural dedicado à difusão, pesquisa e formação nas áreas de dança e música do frevo. Recebeu mais de 120 mil visitantes em seu primeiro ano de funcionamento, sendo um dos locais mais procurados de Pernambuco.',
'Praça do Arsenal da Marinha',
NULL,
NULL,
NULL,
'Recife',
'PE',
'Brasil',
'50030360'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (28, 'CULTURE'), (28, 'ENTERTAINMENT'), (28, 'TRADITION'), (28, 'SOCIAL'), (28, 'IDENTITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (28, 'DANCING'), (28, 'MUSIC'), (28, 'SOCIAL'), (28, 'LEARNING'), (28, 'PHOTOGRAPHY');
INSERT INTO pois_themes (poi_id, theme) VALUES (28, 'CULTURAL'), (28, 'HISTORY'), (28, 'FOLKLORE'), (28, 'FAMILY_FRIENDLY'), (28, 'ADVENTURE');

-- POI ID 29
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
29,
'Palácio da Justiça',
'Prédio histórico tombado em estilo neoclássico que abriga o Tribunal de Justiça de Pernambuco. Possui um acervo de arte essencial para visitantes, incluindo obras que remontam ao centenário da Confederação do Equador.',
'Praça da República',
NULL,
NULL,
'Santo Antônio',
'Recife',
'PE',
'Brasil',
'50010040'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (29, 'HERITAGE'), (29, 'APPRECIATION'), (29, 'EXPLORATION'), (29, 'TRADITION'), (29, 'STUDY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (29, 'ART'), (29, 'PHOTOGRAPHY'), (29, 'LEARNING'), (29, 'TRAVELING'), (29, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (29, 'CULTURAL'), (29, 'HISTORY'), (29, 'LUXURY'), (29, 'COLONIAL'), (29, 'FAMILY_FRIENDLY');

-- POI ID 30
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
30,
'Palácio do Campo das Princesas',
'Sede administrativa do governo de Pernambuco, construída em 1841. Destaque para o baobá secular em frente ao palácio, possivelmente inspiração para "O Pequeno Príncipe". Localiza-se próximo ao Teatro de Santa Isabel.',
'Praça da República',
NULL,
NULL,
'Santo Antônio',
'Recife',
'PE',
'Brasil',
'50010928'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (30, 'HERITAGE'), (30, 'APPRECIATION'), (30, 'EXPLORATION'), (30, 'TRADITION'), (30, 'STUDY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (30, 'ART'), (30, 'PHOTOGRAPHY'), (30, 'LEARNING'), (30, 'TRAVELING'), (30, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (30, 'CULTURAL'), (30, 'HISTORY'), (30, 'LUXURY'), (30, 'COLONIAL'), (30, 'FAMILY_FRIENDLY');

-- POI ID 31
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
31,
'Pracinha de Boa Viagem',
'Principal ponto de venda de artesanato local, com cerca de 200 barracas. No centro, destaca-se a Igreja de Nossa Senhora de Boa Viagem (construída em 1707) e um obelisco histórico com marcos da história de Recife.',
'Av. Boa Viagem',
NULL,
NULL,
'Boa Viagem',
'Recife',
'PE',
'Brasil',
'51020010'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (31, 'CULTURE'), (31, 'TRADITION'), (31, 'APPRECIATION'), (31, 'EXPLORATION'), (31, 'SOCIAL');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (31, 'ART'), (31, 'COLLECTING'), (31, 'PHOTOGRAPHY'), (31, 'SOCIAL'), (31, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (31, 'CULTURAL'), (31, 'HISTORY'), (31, 'FAMILY_FRIENDLY'), (31, 'RELIGIOUS'), (31, 'NATURE');

-- POI ID 32
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
32,
'Sede do Galo da Madrugada',
'Atualmente a sede do Galo da Madrugada tem o programa "Galo - Alegria o Ano Inteiro", que promove festividades de valorização da cultura pernambucana não só no Carnaval, mas durante os doze meses do ano. Um exemplo é a prévia Quinta no Galo, que inicia suas edições no segundo semestre e segue até a véspera do Sábado de Zé Pereira. A festa conta com atrações principais e convidadas, além de grupos multiculturais, como palhaços mascarados do Galo, Maracatu Rural (Baque Solto), Maracatu de Baque Virado, Caboclinhos, grupo de passistas de Frevo, Bloco Lírico e fantasias do Galo da Madrugada.',
'R. da Concórdia',
984,
NULL,
'São José',
'Recife',
'PE',
'Brasil',
'50020050'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (32, 'CULTURE'), (32, 'TRADITION'), (32, 'ENTERTAINMENT'), (32, 'SOCIAL'), (32, 'IDENTITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (32, 'DANCING'), (32, 'MUSIC'), (32, 'SOCIAL'), (32, 'ART'), (32, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (32, 'CULTURAL'), (32, 'FAMILY_FRIENDLY'), (32, 'HISTORY'), (32, 'FOLKLORE'), (32, 'ADVENTURE');

-- POI ID 33
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
33,
'Sinagoga Kahal Zur Israel',
'Primeira sinagoga das Américas, fundada no século XVII por refugiados judeus. Abriga o Arquivo Histórico Judaico de Pernambuco e um "mikvê" (piscina ritual) identificado como comprovação de seu funcionamento histórico. A fachada atual data do século XIX.',
'R. do Bom Jesus',
197,
NULL,
NULL,
'Recife',
'PE',
'Brasil',
'50030170'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (33, 'HERITAGE'), (33, 'STUDY'), (33, 'SPIRITUALITY'), (33, 'TRADITION'), (33, 'APPRECIATION');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (33, 'LEARNING'), (33, 'READING'), (33, 'PHOTOGRAPHY'), (33, 'TRAVELING'), (33, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (33, 'CULTURAL'), (33, 'RELIGIOUS'), (33, 'HISTORY'), (33, 'COLONIAL'), (33, 'FAMILY_FRIENDLY');

-- POI ID 34
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
34,
'Teatro de Santa Isabel',
'Teatro histórico em estilo neoclássico, palco de eventos importantes como a campanha abolicionista de Joaquim Nabuco. Primeiro teatro do Brasil construído com mão de obra especializada, projetado pelo engenheiro francês Louis Léger Vauthier. Recebeu visitantes ilustres como Dom Pedro II.',
NULL,
NULL,
'Praça da República',
'Santo Antônio',
'Recife',
'PE',
'Brasil',
'50010040'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (34, 'CULTURE'), (34, 'ENTERTAINMENT'), (34, 'STUDY'), (34, 'APPRECIATION'), (34, 'HERITAGE');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (34, 'ART'), (34, 'THEATER'), (34, 'PHOTOGRAPHY'), (34, 'LEARNING'), (34, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (34, 'CULTURAL'), (34, 'HISTORY'), (34, 'LUXURY'), (34, 'ROMANTIC'), (34, 'COLONIAL');

-- POI ID 35
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code)
VALUES (
35,
'Teatro do Parque',
'Teatro histórico com inovação arquitetônica: um jardim interno para climatização. Construído em aço e cimento em 1915, com decoração de Henrique Elliot e Mário Nunes. Mantém conexão histórica com o Hotel do Parque para venda de ingressos.',
'R. do Hospício',
81,
NULL,
'Boa Vista',
'Recife',
'PE',
'Brasil',
'50060080'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (35, 'CULTURE'), (35, 'ENTERTAINMENT'), (35, 'APPRECIATION'), (35, 'EXPLORATION'), (35, 'CREATIVITY');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (35, 'THEATER'), (35, 'ART'), (35, 'PHOTOGRAPHY'), (35, 'GARDENING'), (35, 'SOCIAL');
INSERT INTO pois_themes (poi_id, theme) VALUES (35, 'CULTURAL'), (35, 'HISTORY'), (35, 'NATURE'), (35, 'URBAN_ART'), (35, 'FAMILY_FRIENDLY');

-- POI ID 36
INSERT INTO tb_pois (id, poi_name, poi_description, street, house_number, complement, neighborhood, city, states, country, zip_code) 
VALUES (
  36, 
  'Torre Malakoff', 
  'Torre Malakoff, localizada no Recife Antigo, na cidade do Recife, foi batizada com o nome de uma das torres da fortaleza de Sebastopol, durante a Guerra da Crimeia. A Torre Malakoff é um importante monumento localizado no Bairro do Recife, área tombada pelo Iphan. Construído no século 19 (com materiais provenientes da demolição do Forte do Bom Jesus) para servir como observatório astronômico e portão monumental do Arsenal da Marinha. O caráter militar da obra está presente em sua fachada e na simetria de sua planta lembrando também mesquitas do Oriente.', 
  NULL, 
  NULL, 
  NULL, 
  NULL, 
  'Recife', 
  'PE', 
  'Brasil', 
  '50030350'
);

INSERT INTO pois_motivations (poi_id, motivation) VALUES (36, 'CULTURE'), (36, 'EXPLORATION'), (36, 'STUDY'), (36, 'APPRECIATION'), (36, 'HERITAGE');
INSERT INTO pois_hobbies (poi_id, hobbie) VALUES (36, 'PHOTOGRAPHY'), (36, 'MUSIC'), (36, 'LEARNING'), (36, 'ART'), (36, 'TRAVELING');
INSERT INTO pois_themes (poi_id, theme) VALUES (36, 'CULTURAL'), (36, 'HISTORY'), (36, 'MODERN'), (36, 'FAMILY_FRIENDLY'), (36, 'ADVENTURE');
