


						SQL executado produção dia 01/02/2019 Zero horas.


/*
update projudi.custa set minimo = 70.93, valor_maximo = 102927.40 where id_custa = 190 and codigo_regimento = 2011;

update projudi.custa_valor set valor_custa = 14.06 where valor_custa = 13.13 and id_custa = 191;
update projudi.custa_valor set valor_custa = 50.54 where valor_custa = 47.19 and id_custa = 203;
update projudi.custa_valor set valor_custa = 14.06 where valor_custa = 13.13 and id_custa = 300;

insert into projudi.ufr_valor (id_ufr_valor, valor, valor_taxa_judiciaria, mes_inicio, mes_final, data_atualizacao) values (148, 6.67, 51.55, '01/02/2019', '31/01/2020', SYSDATE);

--insert into projudi.propriedade (propriedade, propriedade_codigo, valor) values ('TAXA_JUDICIARIA_VALOR_MEIO_PORCENTO', 80, '83732.83');
--insert into projudi.propriedade (propriedade, propriedade_codigo, valor) values ('TAXA_JUDICIARIA_VALOR_UM_SETENTA_CINCO_PORCENTO', 81, );

update projudi.propriedade set valor = '83732.83' where propriedade like 'TAXA_JUDICIARIA_VALOR_MEIO_PORCENTO' and propriedade_codigo = 80;
update projudi.propriedade set valor = '418525.03' where propriedade like 'TAXA_JUDICIARIA_VALOR_UM_SETENTA_CINCO_PORCENTO' and propriedade_codigo = 81;
*/