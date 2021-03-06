CREATE OR REPLACE VIEW VIEW_RECURSO_PARTE
AS SELECT 
    RP.ID_RECURSO_PARTE AS ID_RECURSO_PARTE,
    RP.ID_RECURSO AS ID_RECURSO,
    R.DATA_ENVIO AS DATA_ENVIO,
    RP.ID_PROC_PARTE AS ID_PROC_PARTE,
    PP.NOME AS NOME,
    RP.ID_PROC_PARTE_TIPO AS ID_PROC_PARTE_TIPO,
    PPT.PROC_PARTE_TIPO AS PROC_PARTE_TIPO,
    RP.DATA_BAIXA AS DATA_BAIXA,
    PPT.PROC_PARTE_TIPO_CODIGO AS PROC_PARTE_TIPO_CODIGO,
    R.ID_PROC AS ID_PROC,
    R.DATA_RETORNO AS DATA_RETORNO,
    RP.ID_PROC_TIPO AS ID_PROC_TIPO,
    PT.PROC_TIPO AS PROC_TIPO,
    CC.POLO_ATIVO AS POLO_ATIVO,
    CC.POLO_PASSIVO AS POLO_PASSIVO	 
  FROM 
    (((RECURSO_PARTE RP JOIN RECURSO R ON((R.ID_RECURSO = RP.ID_RECURSO))) 
	   JOIN PROC_PARTE PP ON((PP.ID_PROC_PARTE = RP.ID_PROC_PARTE))) 
	   JOIN PROC_PARTE_TIPO PPT ON((PPT.ID_PROC_PARTE_TIPO = RP.ID_PROC_PARTE_TIPO)))
	   JOIN PROC_TIPO PT ON (PT.ID_PROC_TIPO = RP.ID_PROC_TIPO)
	   LEFT JOIN CNJ_CLASSE CC ON CC.ID_CLASSE = PT.ID_CNJ_CLASSE
