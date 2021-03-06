CREATE TABLE PROJUDI.INTERRUPCAO_TIPO (
    ID_INTERRUPCAO_TIPO       NUMBER(24,0)  NOT NULL,
    INTERRUPCAO_TIPO          VARCHAR2(60) NOT NULL,
    INTERRUPCAO_TOTAL         NUMBER(4)     NOT NULL,
	CODIGO_TEMP               NUMBER(10)
);

CREATE UNIQUE INDEX PROJUDI.INTERRUPCAO_TIPO_IDXPK ON PROJUDI.INTERRUPCAO_TIPO (ID_INTERRUPCAO_TIPO);

ALTER TABLE PROJUDI.INTERRUPCAO_TIPO ADD CONSTRAINT PK_INTERRUPCAO_TIPO PRIMARY KEY(ID_INTERRUPCAO_TIPO);

CREATE SEQUENCE PROJUDI.INTERRUPCAO_TIPO_ID_SEQ  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;

CREATE OR REPLACE TRIGGER PROJUDI.INTERRUPCAO_TIPO_TRG BEFORE INSERT OR UPDATE ON INTERRUPCAO_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_INTERRUPCAO_TIPO IS NULL THEN
    SELECT INTERRUPCAO_TIPO_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
   
   :new.ID_INTERRUPCAO_TIPO := v_newVal;
  END IF;
END;

ALTER TRIGGER PROJUDI.INTERRUPCAO_TIPO_TRG ENABLE;

CREATE OR REPLACE FORCE VIEW PROJUDI.VIEW_INTERRUPCAO_TIPO AS 
  SELECT
    IT.ID_INTERRUPCAO_TIPO AS ID_INTERRUPCAO_TIPO,
    IT.INTERRUPCAO_TIPO AS INTERRUPCAO_TIPO,
    IT.INTERRUPCAO_TOTAL AS INTERRUPCAO_TOTAL,
    IT.CODIGO_TEMP AS CODIGO_TEMP
  FROM PROJUDI.INTERRUPCAO_TIPO IT;
  
INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('APLICAÇÃO', 1);

INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('ORACLE ESCRITA', 1);

INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('ORACLE DATAGUARD', 1);

INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('ADABAS CONNX', 0);

INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('ADABAS WEB', 0);

INSERT INTO PROJUDI.INTERRUPCAO_TIPO (INTERRUPCAO_TIPO, INTERRUPCAO_TOTAL) VALUES ('CEPH STORAGE', 0);

ALTER TABLE OCORRENCIA_INTERRUPCAO ADD ID_INTERRUPCAO_TIPO NUMBER(24) DEFAULT 1 NOT NULL;

ALTER TABLE OCORRENCIA_INTERRUPCAO DROP COLUMN MOTIVO;
