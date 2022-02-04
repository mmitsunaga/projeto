-- Execute esta primeira parte
insert into projudi.guia_tipo (id_guia_tipo, guia_tipo, guia_tipo_codigo, ativo, guia_tipo_codigo_externo, flag_grau) 
values (34, 'GUIA DE CERTIDÃO NARRATIVA', 34, 1, 3, 1);
update projudi.custa set id_arrecadacao_custa = 45 where id_custa = 135 and id_arrecadacao_custa = 4;

-- Execute esta segunda parte
insert into projudi.guia_modelo (guia_modelo, id_guia_tipo) 
values ('Guia de Certidão Narrativa', 34);

-- Execute esta terceira parte
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
values ((select id_guia_modelo from projudi.guia_modelo where id_guia_tipo = 34), 191);
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
values ((select id_guia_modelo from projudi.guia_modelo where id_guia_tipo = 34), 135);