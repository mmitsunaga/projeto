****************************
EXECUTADO DESENV
****************************


  CREATE OR REPLACE FORCE EDITIONABLE VIEW "PROJUDI"."VIEW_GUIA_EMIS" ("ID_GUIA_EMIS", "GUIA_EMIS", "ID_GUIA_MODELO", "ID_GUIA_FINALIDADE_MODELO", "ID_GUIA_TIPO_TITULO", "GUIA_MODELO", "ID_PROC", "PROC_NUMERO", "NUMERO_PROC_DEPENDENTE", "ID_PROC_TIPO", "PROC_TIPO", "ID_NATUREZA_SPG", "NATUREZA_SPG", "ID_GUIA_EMIS_PRINC", "ID_SERV", "SERV", "ID_COMARCA", "COMARCA", "ID_GUIA_TIPO", "GUIA_TIPO", "ID_GUIA_STATUS", "GUIA_STATUS", "ID_AREA_DIST", "AREA_DIST", "ID_PROC_PRIOR", "ID_USU", "DATA_EMIS", "DATA_RECEBIMENTO", "DATA_VENCIMENTO", "DATA_CANCELAMENTO", "DATA_BASE_ATUALIZACAO", "DATA_BASE_FINAL_ATUALIZACAO", "REQUERENTE", "REQUERIDO", "VALOR_ACAO", "NUMERO_GUIA_COMPLETO", "NUMERO_DUAM", "QUANTIDADE_PARCELAS_DUAM", "DATA_VENCIMENTO_DUAM", "VALOR_IMPOSTO_MUNICIPAL_DUAM", "CODIGO_TEMP", "ID_PROC_PARTE_RECORRENTE", "ID_PROC_PARTE_RECORRIDO", "PROC_PARTE_TIPO_CODIGO", "ID_PROC_PARTE_RESP_GUIA", "RATEIO_CODIGO", "CODIGO_NATUREZA", "VALOR_RECEBIMENTO", "DATA_MOVIMENTO", "DATA_REPASSE", "GUIA_COMPLEMENTAR", "ID_GUIA_EMIS_REFERENCIA", "TIPO_GUIA_DESCONTO_PARCELAMENT", "QUANTIDADE_PARCELAS", "PARCELA_ATUAL", "PORCENTAGEM_DESCONTO", "COD_SITUACAO_BOLETO", "NOSSO_NUMERO_BOLETO", "NUMERO_DOCUMENTO_BOLETO", "DATA_EMISSAO_BOLETO", "VALOR_BOLETO", "CODIGO_BARRAS_BOLETO", "LINHA_DIGITAVEL_BOLETO", "URL_PDF_BOLETO", "PAGADOR_BOLETO_NOME", "PAGADOR_BOLETO_CPF", "PAGADOR_BOLETO_RAZAO_SOCIAL", "PAGADOR_BOLETO_CNPJ", "PAGADOR_BOLETO_LOGRADOURO", "PAGADOR_BOLETO_BAIRRO", "PAGADOR_BOLETO_CIDADE", "PAGADOR_BOLETO_UF", "PAGADOR_BOLETO_CEP", "DATA_VENCIMENTO_BOLETO", "OBSERVACAO1_BOLETO", "OBSERVACAO2_BOLETO", "OBSERVACAO3_BOLETO", "OBSERVACAO4_BOLETO") AS 
  SELECT
  GE.ID_GUIA_EMIS AS ID_GUIA_EMIS,
  GE.GUIA_EMIS AS GUIA_EMIS,
  GE.ID_GUIA_MODELO AS ID_GUIA_MODELO,
  GE.ID_GUIA_FINALIDADE_MODELO AS ID_GUIA_FINALIDADE_MODELO,
  GE.ID_GUIA_TIPO AS ID_GUIA_TIPO_TITULO,
  T.GUIA_MODELO AS GUIA_MODELO,
  GE.ID_PROC AS ID_PROC,
  P.PROC_NUMERO AS PROC_NUMERO,
  GE.NUMERO_PROC_DEPENDENTE AS NUMERO_PROC_DEPENDENTE,
  GE.ID_PROC_TIPO AS ID_PROC_TIPO,
  PT.PROC_TIPO AS PROC_TIPO,
  NS.ID_NATUREZA_SPG AS ID_NATUREZA_SPG,
  NS.NATUREZA_SPG AS NATUREZA_SPG,
  GE.ID_GUIA_EMIS_PRINC AS ID_GUIA_EMIS_PRINC,
  GE.ID_SERV AS ID_SERV,
  S.SERV AS SERV,
  GE.ID_COMARCA AS ID_COMARCA,
  C.COMARCA AS COMARCA,
  GT.ID_GUIA_TIPO AS ID_GUIA_TIPO,
  GT.GUIA_TIPO AS GUIA_TIPO,
  GE.ID_GUIA_STATUS AS ID_GUIA_STATUS,
  GS.GUIA_STATUS AS GUIA_STATUS,
  GE.ID_AREA_DIST AS ID_AREA_DIST,
  AD.AREA_DIST AS AREA_DIST,
  GE.ID_PROC_PRIOR AS ID_PROC_PRIOR,
  GE.ID_USU AS ID_USU,
  GE.DATA_EMIS AS DATA_EMIS,
  GE.DATA_RECEBIMENTO AS DATA_RECEBIMENTO,
  GE.DATA_VENCIMENTO AS DATA_VENCIMENTO,
  GE.DATA_CANCELAMENTO AS DATA_CANCELAMENTO,
  GE.DATA_BASE_ATUALIZACAO AS DATA_BASE_ATUALIZACAO,
  GE.DATA_BASE_FINAL_ATUALIZACAO AS DATA_BASE_FINAL_ATUALIZACAO,
  GE.REQUERENTE AS REQUERENTE,
  GE.REQUERIDO AS REQUERIDO,
  GE.VALOR_ACAO AS VALOR_ACAO,
  GE.NUMERO_GUIA_COMPLETO AS NUMERO_GUIA_COMPLETO,
  GE.NUMERO_DUAM AS NUMERO_DUAM,
  GE.QUANTIDADE_PARCELAS_DUAM AS QUANTIDADE_PARCELAS_DUAM,
  GE.DATA_VENCIMENTO_DUAM AS DATA_VENCIMENTO_DUAM,
  GE.VALOR_IMPOSTO_MUNICIPAL_DUAM AS VALOR_IMPOSTO_MUNICIPAL_DUAM,
  GE.CODIGO_TEMP AS CODIGO_TEMP,
  GE.ID_PROC_PARTE_RECORRENTE AS ID_PROC_PARTE_RECORRENTE,
  GE.ID_PROC_PARTE_RECORRIDO AS ID_PROC_PARTE_RECORRIDO,
  GE.PROC_PARTE_TIPO_CODIGO AS PROC_PARTE_TIPO_CODIGO,
  GE.ID_PROC_PARTE_RESP_GUIA AS ID_PROC_PARTE_RESP_GUIA,
  GE.RATEIO_CODIGO AS RATEIO_CODIGO,
  GE.CODIGO_NATUREZA AS CODIGO_NATUREZA,
  GE.VALOR_RECEBIMENTO AS VALOR_RECEBIMENTO,
  GE.DATA_MOVIMENTO AS DATA_MOVIMENTO,
  GE.DATA_REPASSE AS DATA_REPASSE,
  GE.GUIA_COMPLEMENTAR AS GUIA_COMPLEMENTAR,
  GE.ID_GUIA_EMIS_REFERENCIA AS ID_GUIA_EMIS_REFERENCIA,
  GE.TIPO_GUIA_DESCONTO_PARCELAMENT AS TIPO_GUIA_DESCONTO_PARCELAMENT,
  GE.QUANTIDADE_PARCELAS AS QUANTIDADE_PARCELAS,
  GE.PARCELA_ATUAL AS PARCELA_ATUAL,
  GE.PORCENTAGEM_DESCONTO AS PORCENTAGEM_DESCONTO,
  GE.COD_SITUACAO_BOLETO AS COD_SITUACAO_BOLETO,
  GE.NOSSO_NUMERO_BOLETO AS NOSSO_NUMERO_BOLETO,
  GE.NUMERO_DOCUMENTO_BOLETO AS NUMERO_DOCUMENTO_BOLETO,
  GE.DATA_EMISSAO_BOLETO AS DATA_EMISSAO_BOLETO,
  GE.VALOR_BOLETO AS VALOR_BOLETO,
  GE.CODIGO_BARRAS_BOLETO AS CODIGO_BARRAS_BOLETO,
  GE.LINHA_DIGITAVEL_BOLETO AS LINHA_DIGITAVEL_BOLETO,
  GE.URL_PDF_BOLETO AS URL_PDF_BOLETO,
  GE.PAGADOR_BOLETO_NOME AS PAGADOR_BOLETO_NOME,
  GE.PAGADOR_BOLETO_CPF AS PAGADOR_BOLETO_CPF,
  GE.PAGADOR_BOLETO_RAZAO_SOCIAL AS PAGADOR_BOLETO_RAZAO_SOCIAL,
  GE.PAGADOR_BOLETO_CNPJ AS PAGADOR_BOLETO_CNPJ,
  GE.PAGADOR_BOLETO_LOGRADOURO AS PAGADOR_BOLETO_LOGRADOURO,
  GE.PAGADOR_BOLETO_BAIRRO AS PAGADOR_BOLETO_BAIRRO,
  GE.PAGADOR_BOLETO_CIDADE AS PAGADOR_BOLETO_CIDADE,
  GE.PAGADOR_BOLETO_UF AS PAGADOR_BOLETO_UF,
  GE.PAGADOR_BOLETO_CEP AS PAGADOR_BOLETO_CEP,
  GE.DATA_VENCIMENTO_BOLETO AS DATA_VENCIMENTO_BOLETO,
  GE.OBSERVACAO1_BOLETO AS OBSERVACAO1_BOLETO,
  GE.OBSERVACAO2_BOLETO AS OBSERVACAO2_BOLETO,
  GE.OBSERVACAO3_BOLETO AS OBSERVACAO3_BOLETO,
  GE.OBSERVACAO4_BOLETO AS OBSERVACAO4_BOLETO
FROM GUIA_EMIS GE LEFT JOIN GUIA_MODELO T ON GE.ID_GUIA_MODELO = T.ID_GUIA_MODELO
  LEFT JOIN GUIA_FINALIDADE_MODELO TF ON GE.ID_GUIA_FINALIDADE_MODELO = TF.ID_GUIA_FINALIDADE_MODELO
  LEFT JOIN PROC P ON GE.ID_PROC = P.ID_PROC
  LEFT JOIN PROC_TIPO PT ON GE.ID_PROC_TIPO = PT.ID_PROC_TIPO
  LEFT JOIN SERV S ON GE.ID_SERV = S.ID_SERV
  LEFT JOIN COMARCA C ON GE.ID_COMARCA = C.ID_COMARCA
  LEFT JOIN GUIA_TIPO GT ON T.ID_GUIA_TIPO = GT.ID_GUIA_TIPO OR TF.ID_GUIA_TIPO = GT.ID_GUIA_TIPO
  LEFT JOIN GUIA_STATUS GS ON GE.ID_GUIA_STATUS = GS.ID_GUIA_STATUS
  LEFT JOIN NATUREZA_SPG NS ON GE.ID_NATUREZA_SPG = NS.ID_NATUREZA_SPG
  LEFT JOIN AREA_DIST AD ON GE.ID_AREA_DIST = AD.ID_AREA_DIST;
  
  ALTER TABLE projudi.guia_emis SET UNUSED (assuntos);
ALTER TABLE projudi.guia_emis SET UNUSED (flag_dono);
ALTER TABLE projudi.guia_emis SET UNUSED (advogado);
ALTER TABLE projudi.guia_emis SET UNUSED (outras_partes);
ALTER TABLE projudi.guia_emis SET UNUSED (guia_emis_codigo);
commit;

ALTER TABLE projudi.guia_emis DROP UNUSED COLUMNS;
commit;