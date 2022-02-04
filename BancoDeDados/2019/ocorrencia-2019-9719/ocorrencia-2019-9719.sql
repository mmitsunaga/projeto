update projudi.perm set perm = 'Vincular Guia', titulo = 'Vincular Guia' where perm_codigo = 876 and link = 'VincularGuiaInicialProcesso?PaginaAtual=4';
commit;








--*********************


select * from projudi.perm
where perm_codigo = 889

select * from projudi.perm
where id_perm_pai = 24706

select * from projudi.perm
where perm_codigo = 889
or id_perm_pai = 24706

update projudi.perm set perm = 'Vincular Guia', titulo = 'Vincular Guia' where perm_codigo = 876 and link = 'VincularGuiaInicialProcesso?PaginaAtual=4';
commit;

select * from projudi.view_grupo_perm
where id_perm = 24706
order by grupo

select * from projudi.view_grupo_perm
where id_perm in (
    select id_perm from projudi.perm
    where perm_codigo = 889
    or id_perm_pai = 24706
)
order by grupo

delete from projudi.grupo_perm
where id_perm in (
    select id_perm from projudi.perm
    where perm_codigo = 889
    or id_perm_pai = 24706
);
commit;