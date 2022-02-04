insert into projudi.guia_tipo (id_guia_tipo, guia_tipo, guia_tipo_codigo, ativo, guia_tipo_codigo_externo, flag_grau) values (30, 'MEDIA��O', 30, 1, 1, 1);
insert into projudi.guia_tipo (id_guia_tipo, guia_tipo, guia_tipo_codigo, ativo, guia_tipo_codigo_externo, flag_grau) values (31, 'CONCILIA��O', 31, 1, 1, 1);

insert into projudi.arrecadacao_custa (id_arrecadacao_custa, CODIGO_ARRECADACAO, ARRECADACAO_CUSTA) values (62, 1430, 'MEDIADORES');
insert into projudi.arrecadacao_custa (id_arrecadacao_custa, CODIGO_ARRECADACAO, ARRECADACAO_CUSTA) values (63, 1449, 'CONCILIADORES');

insert into projudi.custa (ID_CUSTA, CODIGO_REGIMENTO, CUSTA, CODIGO_REGIMENTO_VALOR, PORCENTAGEM, ID_ARRECADACAO_CUSTA, REFERENCIA_CALCULO, VALOR_ACRESCIMO, VALOR_MAXIMO ) values (209, 'CPC(Med)', 'MEDIA��O', 'CPC(Med)', 100, 62, 7002, 0, null);
insert into projudi.custa (ID_CUSTA, CODIGO_REGIMENTO, CUSTA, CODIGO_REGIMENTO_VALOR, PORCENTAGEM, ID_ARRECADACAO_CUSTA, REFERENCIA_CALCULO, VALOR_ACRESCIMO, VALOR_MAXIMO ) values (210, 'CPC(Conc)', 'CONCILIA��O', 'CPC(Conc)', 100, 63, 7001, 0, null);

insert into projudi.custa_valor (ID_CUSTA, LIMITE_MIN, LIMITE_MAX, VALOR_CUSTA) values (209, 0, 999999999999999999.99, 23.96);
insert into projudi.custa_valor (ID_CUSTA, LIMITE_MIN, LIMITE_MAX, VALOR_CUSTA) values (210, 0, 999999999999999999.99, 7.98);

insert into projudi.guia_modelo (guia_modelo, id_guia_tipo, id_proc_tipo)
select 'Guia Concilia��o', 31, id_proc_tipo from projudi.proc_tipo;

insert into projudi.guia_modelo (guia_modelo, id_guia_tipo, id_proc_tipo)
select 'Guia Media��o', 30, id_proc_tipo from projudi.proc_tipo;