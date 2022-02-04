--**********************************************
--**********************************************
--**********************************************
--**********************************************
EXECUTADO 19/05/2016 11:31
--**********************************************
--**********************************************
--**********************************************
--**********************************************
insert into projudi.guia_tipo (id_guia_tipo, guia_tipo, guia_tipo_codigo, ativo, guia_tipo_codigo_externo, flag_grau) 
values (28, 'FAZENDA PUBLICA AUTOMATICA', 28, 1, 5, 1);
commit;
insert into projudi.guia_modelo (guia_modelo, id_guia_tipo, id_proc_tipo) 
values ('FAZENDA PUBLICA AUTOMATICA - Execução Fiscal', 28, 184);
commit;

XXX = select id_guia_modelo from projudi.guia_modelo where id_guia_tipo = 28 and id_proc_tipo = 184;

insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa) 
values (XXX, 196);
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa) 
values (XXX, 68);
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa) 
values (XXX, 75);
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa) 
values (XXX, 39);
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa) 
values (XXX, 64);
commit;

--**********************************************
--**********************************************
--**********************************************
--**********************************************
EXECUTADO 19/05/2016 11:31
--**********************************************
--**********************************************
--**********************************************
--**********************************************