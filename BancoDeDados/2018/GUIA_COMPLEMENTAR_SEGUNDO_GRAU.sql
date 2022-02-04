/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 					   ATENÇÃO!!!!!!!!!!
 * 
 * 
 *                     SQL executado em produção no dia 04/09/2018 às 15:40hs. 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */



--Alteração para colocar as guias iniciais e complementares de segundo grau em produção.
--Altera o nome da guia complementar atual para "complementar de 1º grau"
update projudi.guia_tipo set guia_tipo = 'COMPLEMENTAR - 1º GRAU'
where id_guia_tipo = 2;
commit;

--Insere novo nome de guia complementar para segundo grau
insert into projudi.guia_tipo (id_guia_tipo, guia_tipo, guia_tipo_codigo, ativo, guia_tipo_codigo_externo, flag_grau) values (40, 'COMPLEMENTAR - 2º GRAU', 40, 1, 3, 2);
commit;

--Insere novos modelos de guia complementar de segundo grau SOMENTE para classes que tem modelos de guia inicial de segundo grau
insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo) 
select 40, id_proc_tipo, 'Guia Complementar 2º Grau' from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.guia_modelo 
	where codigo_temp = 1
	and id_guia_tipo = 22
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%PROCE%CAUTELAR%'
and upper(proc_tipo) not like 'MEDIDA%'
and upper(proc_tipo) not like '%FISCAL%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%PROCE%CAUTELAR%'
    and upper(proc_tipo) not like 'MEDIDA%'
    and upper(proc_tipo) not like '%FISCAL%'
);
commit;

-- 9002 = reg. 02
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%PROCE%CAUTELAR%'
          and upper(proc_tipo) not like 'MEDIDA%'
          and upper(proc_tipo) not like '%FISCAL%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%PROCE%CAUTELAR%'
          and upper(proc_tipo) not like 'MEDIDA%'
          and upper(proc_tipo) not like '%FISCAL%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'CAUTELAR%'
and upper(proc_tipo) not like 'MEDIDA%'
and upper(proc_tipo) not like '%FISCAL%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'CAUTELAR%'
    and upper(proc_tipo) not like 'MEDIDA%'
    and upper(proc_tipo) not like '%FISCAL%'
);
commit;

-- 9002 = reg. 02
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'CAUTELAR%'
          and upper(proc_tipo) not like 'MEDIDA%'
          and upper(proc_tipo) not like '%FISCAL%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'CAUTELAR%'
          and upper(proc_tipo) not like 'MEDIDA%'
          and upper(proc_tipo) not like '%FISCAL%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%AÇÃO%RESC%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%AÇÃO%RESC%'
);
commit;

-- 9002 = reg. 02
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%AÇÃO%RESC%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%AÇÃO%RESC%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%AGRAV%INS%'
or upper(proc_tipo) like '%AGRAV%REG%'
/*and upper(proc_tipo) not like '%FISCAL%'*/
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%AGRAV%INS%'
    or upper(proc_tipo) like '%AGRAV%REG%'
);
commit;

-- 9001 = reg. 01
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9001 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%AGRAV%INS%'
          or upper(proc_tipo) like '%AGRAV%REG%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%CONFLITO%COMP%'
/*and upper(proc_tipo) not like '%FISCAL%'*/
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%CONFLITO%COMP%'
);
commit;

-- 9001 = reg. 01
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9001 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%CONFLITO%COMP%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%CONFLITO%COMP%'
      )
);
commit;

*************************************************************
*************************************************************

--Ligação da Fernanda (31/08/2018 às 10:50hs) dizendo que não existe mais essa classe no segundo grau.

/*
select * from projudi.proc_tipo
where upper(proc_tipo) like '%EMBARG%INFRIN%'
and upper(proc_tipo) not like '%EMBARG%NULIDAD%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%EMBARG%INFRIN%'
    and upper(proc_tipo) not like '%EMBARG%NULIDAD%'
);
commit;

-- 9001 = reg. 01
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9001 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%EMBARG%INFRIN%'
          and upper(proc_tipo) not like '%EMBARG%NULIDAD%'
      )
);
commit;
*/

*************************************************************
*************************************************************

select * from projudi.proc_tipo
where upper(proc_tipo) like '%EMBARG%NULIDAD%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%EMBARG%NULIDAD%'
);
commit;

-- 9003 = reg. 03
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%EMBARG%NULIDAD%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where (upper(proc_tipo) like 'EXCE%IMPE%'
or upper(proc_tipo) like 'EXCE%INCOM%'
or upper(proc_tipo) like 'EXCE%SUSP%')
and upper(proc_tipo) not like '%CPP%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where (upper(proc_tipo) like 'EXCE%IMPE%'
    or upper(proc_tipo) like 'EXCE%INCOM%'
    or upper(proc_tipo) like 'EXCE%SUSP%')
    and upper(proc_tipo) not like '%CPP%'
);
commit;

-- 9001 = reg. 01
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9001 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where (upper(proc_tipo) like 'EXCE%IMPE%'
          or upper(proc_tipo) like 'EXCE%INCOM%'
          or upper(proc_tipo) like 'EXCE%SUSP%')
          and upper(proc_tipo) not like '%CPP%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where (upper(proc_tipo) like 'EXCE%IMPE%'
          or upper(proc_tipo) like 'EXCE%INCOM%'
          or upper(proc_tipo) like 'EXCE%SUSP%')
          and upper(proc_tipo) not like '%CPP%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%INCI%FALS%'
and upper(proc_tipo) not like '%INCI%FALS%CPP%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%INCI%FALS%'
    and upper(proc_tipo) not like '%INCI%FALS%CPP%'
);
commit;

-- 9001 = reg. 01
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9001 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%INCI%FALS%'
          and upper(proc_tipo) not like '%INCI%FALS%CPP%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%INCI%FALS%'
          and upper(proc_tipo) not like '%INCI%FALS%CPP%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%INCI%FALS%CPP%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%INCI%FALS%CPP%'
);
commit;

-- 9003 = reg. 03
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%INCI%FALS%CPP%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'RESTAU%CPP%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'RESTAU%CPP%'
);
commit;

-- 9006 = reg. 4
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9006 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'RESTAU%CPP%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'RESTAU%CPC%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'RESTAU%CPC%'
);
commit;

-- 9006 = reg. 4
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9006 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'RESTAU%CPC%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'RESTAU%CPC%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'MEDIDA%CAUTE%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'MEDIDA%CAUTE%'
);
commit;

-- 9002 = reg. 2
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'MEDIDA%CAUTE%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'MEDIDA%CAUTE%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'RECLAMAÇÃO'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'RECLAMAÇÃO'
);
commit;

-- 9002 = reg. 2
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'RECLAMAÇÃO'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'RECLAMAÇÃO'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'MANDADO%SEGURA%'
or upper(proc_tipo) like 'MANDADO%INJUN%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'MANDADO%SEGURA%'
    or upper(proc_tipo) like 'MANDADO%INJUN%'
);
commit;

-- 9002 = reg. 2
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9002 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'MANDADO%SEGURA%'
          or upper(proc_tipo) like 'MANDADO%INJUN%'
      )
);
commit;

-- tx. jud.
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 190 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'MANDADO%SEGURA%'
          or upper(proc_tipo) like 'MANDADO%INJUN%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like 'AÇÃO%PENAL%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like 'AÇÃO%PENAL%'
);
commit;

-- 9003 = reg. 3
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like 'AÇÃO%PENAL%'
      )
);
commit;

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%DESAFOR%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%DESAFOR%'
);
commit;

-- 9003 = reg. 3
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%DESAFOR%'
      )
);
commit;

*************************************************************
*************************************************************


--Ligação da Fernanda (31/08/2018 às 10:50hs) dizendo que não existe mais essa classe no segundo grau.


/*select * from projudi.proc_tipo
where upper(proc_tipo) like '%QUEIXA%CRIME%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%QUEIXA%CRIME%'
);
commit;

-- 9003 = reg. 3
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%QUEIXA%CRIME%'
      )
);
commit;
*/

*************************************************************
*************************************************************
select * from projudi.proc_tipo
where upper(proc_tipo) like '%REVIS%CRIMINA%'
order by proc_tipo desc

insert into projudi.guia_modelo (id_guia_tipo, id_proc_tipo, guia_modelo, codigo_temp) 
select 22, id_proc_tipo, 'Guia Inicial 2º Grau', 1 from projudi.proc_tipo
where id_proc_tipo in (
    select id_proc_tipo from projudi.proc_tipo
    where upper(proc_tipo) like '%REVIS%CRIMINA%'
);
commit;

-- 9003 = reg. 3
insert into projudi.guia_custa_modelo (id_guia_modelo, id_custa)
select id_guia_modelo, 9003 from projudi.guia_modelo
where id_guia_modelo in (
      select id_guia_modelo from projudi.guia_modelo 
      where codigo_temp = 1
      and id_guia_tipo = 22
      and id_proc_tipo in (
          select id_proc_tipo from projudi.proc_tipo
          where upper(proc_tipo) like '%REVIS%CRIMINA%'
      )
);
commit;