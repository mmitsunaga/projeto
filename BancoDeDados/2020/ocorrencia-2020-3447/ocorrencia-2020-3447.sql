Problema da ocorrencia 2020/3447 e já foi EXECUTADO no projudi produção e SPG.

Sql de identificação:

select loc.*, 
'update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = ' || loc.id_locomocao || ' and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = ' || guia.numero_guia_completo || ' )',
'update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = ' || loc.id_locomocao || ' and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = ' || guia.numero_guia_completo || ' )'
from projudi.locomocao loc
inner join projudi.guia_item item on (loc.id_guia_item  = item.id_guia_item)
inner join projudi.guia_emis guia on (guia.id_guia_emis = item.id_guia_emis)
where 
--guia.numero_guia_completo = 1764241850
guia.data_emis >= '01/02/2019'
and loc.id_bairro = 32985


Sql de update no Projudi:

update projudi.locomocao set id_bairro = 35627 where id_bairro = 32985 and id_guia_item in (select id_guia_item from projudi.guia_item where id_guia_emis in (select id_guia_emis from projudi.guia_emis where data_emis >= '01/02/2019') );

E o primeiro Sql acima gerou duas colunas para CAPITAL e REMOTO no SPG, sendo que o resultado de execução alterou 
somente locomoções no REMOTO.

update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899865 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899866 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899867 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899868 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899869 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925364 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925365 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925366 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925367 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979658 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979659 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979660 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979661 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1012358 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1764241850 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060733 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060734 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060735 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178837 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178838 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178839 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204679 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204680 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204681 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1240079 and ISN_SPGA_GUIAS = (select ISN_SPGA_GUIAS from V_SPGAGUIAS where NUMR_GUIA = 2126520850 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899865 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899866 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899867 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899868 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 899869 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1581588950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925364 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925365 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925366 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 925367 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1621966150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979658 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979659 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979660 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 979661 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1711109950 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1012358 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1764241850 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060733 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060734 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1060735 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 1842910650 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178837 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178838 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1178839 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2032095750 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204679 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204680 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1204681 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2075016150 )
update V_SPGAGUIAS_LOCOMOCOES_REM set codg_bairro = 11631 where codg_bairro = 20738 and CODG_MUNICIPIO = 6097 and ID_PROJUDI = 1240079 and ISN_SPGA_GUIAS_REM = (select ISN_SPGA_GUIAS_REM from V_SPGAGUIAS_REM where NUMR_GUIA = 2126520850 )