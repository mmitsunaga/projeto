
  CREATE TABLE "PROJUDI"."USU_CAND" 
   (	"ID_USU_CAND" NUMBER(24,0) NOT NULL ENABLE, 
	"CURRICULO" CLOB NOT NULL ENABLE, 
	"DATA_INSCRICAO" DATE NOT NULL ENABLE, 
	"EMAIL_INSCRICAO" VARCHAR2(70 CHAR), 
	"NOME" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"SEXO" CHAR(1 CHAR), 
	"DATA_NASCIMENTO" DATE NOT NULL ENABLE, 
	"ID_NATURALIDADE" NUMBER(10,0), 
	"ID_ENDERECO" NUMBER(24,0), 
	"RG" VARCHAR2(50 CHAR), 
	"ID_RG_ORGAO_EXP" NUMBER(10,0), 
	"RG_DATA_EXPEDICAO" DATE, 
	"CPF" VARCHAR2(30 CHAR), 
	"TITULO_ELEITOR" VARCHAR2(20 CHAR), 
	"TITULO_ELEITOR_ZONA" NUMBER(10,0), 
	"TITULO_ELEITOR_SECAO" NUMBER(10,0), 
	"CTPS" NUMBER(10,0), 
	"CTPS_SERIE" NUMBER(10,0), 
	"ID_CTPS_UF" NUMBER(10,0), 
	"PIS" NUMBER(10,0), 
	"TELEFONE" VARCHAR2(20 CHAR), 
	"CELULAR" VARCHAR2(20 CHAR), 
	"SENHA" VARCHAR2(100 CHAR), 
	"NUMERO_CONTA" VARCHAR2(100 BYTE), 
	"NUMERO_AGENCIA" VARCHAR2(100 BYTE), 
	"CODIGO_BANCO" VARCHAR2(100 BYTE), 
	"OBSERVACAO_STATUS" CLOB, 
	"CODIGO_STATUS" NUMBER(10,0), 
	"DATA_APROVADO" DATE, 
	"DATA_PENDENTE" DATE, 
	"DATA_REPROVADO" DATE, 
	"ID_USU" NUMBER(10,0), 
	 CONSTRAINT "PRIMARY_1578" PRIMARY KEY ("ID_USU_CAND")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA"  ENABLE, 
	 CONSTRAINT "USUCAND_RGORGAOEXPEDIDOR_FK" FOREIGN KEY ("ID_RG_ORGAO_EXP")
	  REFERENCES "PROJUDI"."RG_ORGAO_EXP" ("ID_RG_ORGAO_EXP") ENABLE, 
	 CONSTRAINT "USUCAND_ESTADO_CTPSUF_FK" FOREIGN KEY ("ID_CTPS_UF")
	  REFERENCES "PROJUDI"."ESTADO" ("ID_ESTADO") ENABLE, 
	 CONSTRAINT "USUCAND_ENDERECO_FK" FOREIGN KEY ("ID_ENDERECO")
	  REFERENCES "PROJUDI"."ENDERECO" ("ID_ENDERECO") ENABLE, 
	 CONSTRAINT "USUCAND_CIDADE_NATURALIDADE_FK" FOREIGN KEY ("ID_NATURALIDADE")
	  REFERENCES "PROJUDI"."CIDADE" ("ID_CIDADE") ENABLE, 
	 CONSTRAINT "USUCAND_USU_FK" FOREIGN KEY ("ID_USU")
	  REFERENCES "PROJUDI"."USU" ("ID_USU") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" 
 LOB ("CURRICULO") STORE AS BASICFILE (
  TABLESPACE "PROJUDI_DATA" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
 LOB ("OBSERVACAO_STATUS") STORE AS BASICFILE (
  TABLESPACE "PROJUDI_DATA" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;

  CREATE INDEX "PROJUDI"."CPF_12" ON "PROJUDI"."USU_CAND" (NLSSORT("CPF",'nls_sort=''BINARY_AI''')) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" ;

  CREATE INDEX "PROJUDI"."ID_CTPSUF_12" ON "PROJUDI"."USU_CAND" ("ID_CTPS_UF") 
  PCTFREE 0 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_INDX" ;

  CREATE INDEX "PROJUDI"."ID_ENDERECO_32" ON "PROJUDI"."USU_CAND" ("ID_ENDERECO") 
  PCTFREE 0 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_INDX" ;

  CREATE INDEX "PROJUDI"."ID_NATURALIDADE_22" ON "PROJUDI"."USU_CAND" ("ID_NATURALIDADE") 
  PCTFREE 0 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_INDX" ;

  CREATE INDEX "PROJUDI"."ID_RGORGAOEXPEDIDOR_22" ON "PROJUDI"."USU_CAND" ("ID_RG_ORGAO_EXP") 
  PCTFREE 0 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_INDX" ;

  CREATE INDEX "PROJUDI"."NOME2" ON "PROJUDI"."USU_CAND" (NLSSORT("NOME",'nls_sort=''BINARY_AI''')) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" ;

  CREATE INDEX "PROJUDI"."RG_12" ON "PROJUDI"."USU_CAND" (NLSSORT("RG",'nls_sort=''BINARY_AI''')) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" ;

  CREATE OR REPLACE TRIGGER "PROJUDI"."USU_CAND_ID_USU_CANDIDATO_TRG" BEFORE INSERT OR UPDATE ON "USU_CAND"
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_CAND IS NULL THEN
    SELECT  ID_USU_CAND_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_CAND),0) INTO v_newVal FROM USU_CAND;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ID_USU_CAND_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_CAND := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USU_CAND_ID_USU_CANDIDATO_TRG" ENABLE;



































  CREATE TABLE "PROJUDI"."USU_CAND_ARQ" 
   (	"ID_USU_CAND_ARQ" NUMBER(24,0) NOT NULL ENABLE, 
	"NOME_ARQ" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"CONTENT_TYPE" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"CONTEUDO_ARQ" BLOB, 
	"DATA_INSERCAO" DATE NOT NULL ENABLE, 
	"ID_USU_CAND" NUMBER(24,0), 
	 CONSTRAINT "PRIMARY_261" PRIMARY KEY ("ID_USU_CAND_ARQ")
  USING INDEX PCTFREE 0 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_INDX"  ENABLE, 
	 CONSTRAINT "USUCAND_USUCAND_FK" FOREIGN KEY ("ID_USU_CAND")
	  REFERENCES "PROJUDI"."USU_CAND" ("ID_USU_CAND") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" 
 LOB ("CONTEUDO_ARQ") STORE AS SECUREFILE (
  TABLESPACE "PROJUDI_LOB" ENABLE STORAGE IN ROW CHUNK 8192
  NOCACHE NOLOGGING  COMPRESS HIGH  KEEP_DUPLICATES 
  STORAGE(INITIAL 106496 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARQUIVO_USU_CAND_ARQ_TRIG" BEFORE INSERT OR UPDATE ON USU_CAND_ARQ
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_CAND_ARQ IS NULL THEN
    SELECT  ARQUIVO_USU_CAND_ARQ_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_CAND_ARQ),0) INTO v_newVal FROM USU_CAND_ARQ;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ARQUIVO_USU_CAND_ARQ_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_CAND_ARQ := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ARQUIVO_USU_CAND_ARQ_TRIG" ENABLE;

































  CREATE TABLE "PROJUDI"."USU_CAND_COMARCA" 
   (	"ID_USU_CAND_COMARCA" NUMBER(24,0) NOT NULL ENABLE, 
	"ID_COMARCA" NUMBER(10,0) NOT NULL ENABLE, 
	"ID_USU_CAND" NUMBER(24,0) NOT NULL ENABLE, 
	"CODIGO_ESCOLHA" NUMBER(10,0), 
	"DATA_INSCRICAO" DATE NOT NULL ENABLE, 
	"ID_SERVENTIA" NUMBER(24,0), 
	 CONSTRAINT "USU_CAND_COMARCA_PK2" PRIMARY KEY ("ID_USU_CAND_COMARCA")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA"  ENABLE, 
	 CONSTRAINT "USU_CAND_COMARCA_FK4" FOREIGN KEY ("ID_USU_CAND")
	  REFERENCES "PROJUDI"."USU_CAND" ("ID_USU_CAND") ENABLE, 
	 CONSTRAINT "USU_CAND_COMARCA_FK2" FOREIGN KEY ("ID_COMARCA")
	  REFERENCES "PROJUDI"."COMARCA" ("ID_COMARCA") ENABLE, 
	 CONSTRAINT "ID_SERVENTIA_FK11" FOREIGN KEY ("ID_SERVENTIA")
	  REFERENCES "PROJUDI"."SERV" ("ID_SERV") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" ;

  CREATE OR REPLACE TRIGGER "PROJUDI"."ID_USU_CAND_COMARCA_TRG" BEFORE INSERT OR UPDATE ON USU_CAND_COMARCA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_CAND_COMARCA IS NULL THEN
    SELECT  ID_USU_CAND_COMARCA_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_CAND_COMARCA),0) INTO v_newVal FROM USU_CAND_COMARCA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ID_USU_CAND_COMARCA_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_CAND_COMARCA := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ID_USU_CAND_COMARCA_TRG" ENABLE;































  CREATE TABLE "PROJUDI"."USU_CAND_COMARCA_TURNO" 
   (	"ID_USU_CAND_COMARCA_TURNO" NUMBER(24,0) NOT NULL ENABLE, 
	"ID_USU_CAND_COMARCA" NUMBER(24,0) NOT NULL ENABLE, 
	"CODIGO_DIA" NUMBER(10,0), 
	"CODIGO_TURNO_MATUTINO" NUMBER(10,0), 
	"CODIGO_TURNO_VESPERTINO" NUMBER(10,0), 
	 CONSTRAINT "USU_CAND_COMARCA_TURNO_PK2" PRIMARY KEY ("ID_USU_CAND_COMARCA_TURNO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA"  ENABLE, 
	 CONSTRAINT "USU_CAND_COMARCA_FK21" FOREIGN KEY ("ID_USU_CAND_COMARCA")
	  REFERENCES "PROJUDI"."USU_CAND_COMARCA" ("ID_USU_CAND_COMARCA") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "PROJUDI_DATA" ;

  CREATE OR REPLACE TRIGGER "PROJUDI"."ID_USU_CAND_COMARCA_TURNO_TRG" BEFORE INSERT OR UPDATE ON USU_CAND_COMARCA_TURNO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_CAND_COMARCA_TURNO IS NULL THEN
    SELECT  ID_USU_CAND_COMARCA_TURNO_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_CAND_COMARCA_TURNO),0) INTO v_newVal FROM USU_CAND_COMARCA_TURNO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ID_USU_CAND_COMARCA_TURNO_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_CAND_COMARCA_TURNO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ID_USU_CAND_COMARCA_TURNO_TRG" ENABLE;





