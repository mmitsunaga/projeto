CREATE TABLE PROJUDI.PROC_PARTE_DEBITO_FISICO (	
	ID_PROC_PARTE_DEBITO_FISICO NUMBER(24,0)   NOT NULL ENABLE, 
	ID_PROC_DEBITO              NUMBER(10,0)   NOT NULL ENABLE,
	PROC_NUMERO                 NUMBER(20,0)   NOT NULL ENABLE, 
	CODG_ESCRIVANIA             NUMBER(10,0)   NOT NULL ENABLE, 
	DESC_ESCRIVANIA             VARCHAR2(60)   NOT NULL ENABLE,
	ISN_PARTE                   NUMBER(24,0)   NOT NULL ENABLE, 
	NOME_PARTE                  VARCHAR2(80)   NOT NULL ENABLE, 
	TIPO_PARTE                  NUMBER(5,0)    NOT NULL ENABLE,
	NOME_SIMPLIFICADO           VARCHAR2(30)   NOT NULL ENABLE,
	CPF_CNPJ_PARTE              NUMBER(14,0)   NOT NULL ENABLE, 
	NUMERO_GUIA_COMPLETO        NUMBER(24,0), 
	VALOR_GUIA                  NUMBER(20,2), 
	NUMERO_PROAD                NUMBER(15,0), 
	STATUS                      NUMBER(5,0) DEFAULT 1 NOT NULL ENABLE, 
	DATA_BAIXA                  DATE, 
	CODIGO_TEMP                 NUMBER(10,0), 
CONSTRAINT PROC_PARTE_DEBITO_FISICO_PK PRIMARY KEY (ID_PROC_PARTE_DEBITO_FISICO),
CONSTRAINT PROCESSOPARTEDEBITOFIS_PROCDE FOREIGN KEY (ID_PROC_DEBITO) REFERENCES PROJUDI.PROC_DEBITO (ID_PROC_DEBITO) ENABLE);

CREATE INDEX PROJUDI.ID_PROCESSODEBITOFIS ON PROJUDI.PROC_PARTE_DEBITO_FISICO (ID_PROC_DEBITO);

CREATE INDEX PROJUDI.SERV_PROCESSODEBITOFIS ON PROJUDI.PROC_PARTE_DEBITO_FISICO (CODG_ESCRIVANIA);

CREATE OR REPLACE FORCE VIEW PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO AS 
  SELECT PPD.ID_PROC_PARTE_DEBITO_FISICO AS ID_PROC_PARTE_DEBITO_FISICO,
	PPD.ID_PROC_DEBITO                   AS ID_PROC_DEBITO,
	PD.PROC_DEBITO                       AS PROC_DEBITO,
	PPD.PROC_NUMERO                      AS PROC_NUMERO,
	PPD.CODG_ESCRIVANIA                  AS CODG_ESCRIVANIA,
	PPD.DESC_ESCRIVANIA                  AS DESC_ESCRIVANIA,
	PPD.ISN_PARTE                        AS ISN_PARTE,
	PPD.NOME_PARTE                       AS NOME_PARTE,
	PPD.NOME_SIMPLIFICADO                AS NOME_SIMPLIFICADO,
	PPD.TIPO_PARTE                       AS TIPO_PARTE,
	PPD.CPF_CNPJ_PARTE                   AS CPF_CNPJ_PARTE,
	PPD.NUMERO_GUIA_COMPLETO             AS NUMERO_GUIA_COMPLETO,
	PPD.VALOR_GUIA                       AS VALOR_GUIA,
	PPD.NUMERO_PROAD                     AS NUMERO_PROAD,
	PPD.STATUS                           AS STATUS,
	PPD.DATA_BAIXA                       AS DATA_BAIXA,
	PPD.CODIGO_TEMP                      AS CODIGO_TEMP
  FROM PROJUDI.PROC_PARTE_DEBITO_FISICO PPD
  INNER JOIN PROJUDI.PROC_DEBITO PD ON PD.ID_PROC_DEBITO = PPD.ID_PROC_DEBITO;

CREATE SEQUENCE  PROJUDI.PROCPARTEDEBITOFIS_ID_PROC_SEQ  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

CREATE OR REPLACE TRIGGER PROJUDI.PROCPARTEDEBITOFIS_ID_PROC_TRG BEFORE INSERT OR UPDATE ON PROC_PARTE_DEBITO_FISICO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_DEBITO_FISICO IS NULL THEN
    SELECT  PROCPARTEDEBITOFIS_ID_PROC_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    IF v_newVal = 1 THEN
      SELECT NVL(max(ID_PROC_PARTE_DEBITO_FISICO),0) INTO v_newVal FROM PROC_PARTE_DEBITO_FISICO;
      v_newVal := v_newVal + 1;
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PROCPARTEDEBITOFIS_ID_PROC_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;    
   :new.ID_PROC_PARTE_DEBITO_FISICO := v_newVal;
  END IF;
END;

ALTER TRIGGER PROJUDI.PROCPARTEDEBITOFIS_ID_PROC_TRG ENABLE;

