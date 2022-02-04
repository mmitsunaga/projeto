

				EXECUTADO 23/05/2019
/*
insert into projudi.custa (id_custa, codigo_regimento, custa, codigo_regimento_valor, porcentagem, valor_acrescimo, minimo, id_arrecadacao_custa, referencia_calculo)
values (9040, '16.II', '16.II - DESARQUIVAMENTO', '16.II', 100, null, null, 45, 9999);
commit;
insert into projudi.custa_valor (id_custa, limite_min, limite_max, valor_custa)
values (9040, 0, 999999999999999999.99, 33.6);
commit;
update projudi.custa set codigo_regimento = '16.II(TX)', codigo_regimento_valor = '16.II(TX)' where id_custa = 9025;
commit;
update projudi.custa set custa = '16.II - TRASLADOS E OUTRAS CERTIDÕES' where id_custa = 9025 and codigo_regimento = '16.II(TX)';
commit;
*/