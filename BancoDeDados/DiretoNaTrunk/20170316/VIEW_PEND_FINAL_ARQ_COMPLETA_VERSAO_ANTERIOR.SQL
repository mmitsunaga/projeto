CREATE OR REPLACE FORCE VIEW "PROJUDI"."VIEW_PEND_FINAL_ARQ_COMPLETA" ("ID_PEND_ARQ", "ID_ARQ", "NOME_ARQ", "ID_PEND", "PEND_TIPO", "RESPOSTA", "CODIGO_TEMP", "ID_ARQ_TIPO", "ARQ_TIPO", "CONTENT_TYPE", "ARQ", "CAMINHO", "DATA_INSERCAO", "USU_ASSINADOR", "ARQ_TIPO_CODIGO", "RECIBO") AS 
  SELECT 
    PA.ID_PEND_ARQ AS ID_PEND_ARQ,
    PA.ID_ARQ AS ID_ARQ,
    VUS.NOME_ARQ AS NOME_ARQ,
    PA.ID_PEND AS ID_PEND,
    PT.PEND_TIPO AS PEND_TIPO,
    PA.RESPOSTA AS RESPOSTA,
    PA.CODIGO_TEMP AS CODIGO_TEMP,
    AT.ID_ARQ_TIPO AS ID_ARQ_TIPO,
    AT.ARQ_TIPO AS ARQ_TIPO,
    VUS.CONTENT_TYPE AS CONTENT_TYPE,
    VUS.ARQ AS ARQ,
    VUS.CAMINHO AS CAMINHO,
    VUS.DATA_INSERCAO AS DATA_INSERCAO,
    VUS.USU_ASSINADOR AS USU_ASSINADOR,
    AT.ARQ_TIPO_CODIGO AS ARQ_TIPO_CODIGO,
    VUS.RECIBO AS RECIBO 
  FROM 
    ((((PEND_FINAL_ARQ PA JOIN PEND_FINAL P ON((PA.ID_PEND = P.ID_PEND))) 
    JOIN PEND_TIPO PT ON((P.ID_PEND_TIPO = PT.ID_PEND_TIPO))) 
    LEFT JOIN ARQ VUS ON((VUS.ID_ARQ = PA.ID_ARQ))) 
    LEFT JOIN ARQ_TIPO AT ON((VUS.ID_ARQ_TIPO = AT.ID_ARQ_TIPO))) 
  WHERE 
    (((AT.ARQ_TIPO_CODIGO <> 100) AND (VUS.USU_ASSINADOR IS NOT NULL)) OR ((P.DATA_FIM IS NULL) AND (VUS.USU_ASSINADOR IS NULL)));
