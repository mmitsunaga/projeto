--------------------------------------------------------
--  DDL for Trigger AFASTAMENTO_ID_AFASTAMENTO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AFASTAMENTO_ID_AFASTAMENTO_TRG" BEFORE INSERT OR UPDATE ON Afastamento
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Afastamento IS NULL THEN
    SELECT  Afastamento_Id_Afastamento_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Afastamento),0) INTO v_newVal FROM Afastamento;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Afastamento_Id_Afastamento_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Afastamento := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."AFASTAMENTO_ID_AFASTAMENTO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AGENCIA_ID_AGENCIA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AGENCIA_ID_AGENCIA_TRG" BEFORE INSERT OR UPDATE ON Agencia
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Agencia IS NULL THEN
    SELECT  Agencia_Id_Agencia_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Agencia),0) INTO v_newVal FROM Agencia;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Agencia_Id_Agencia_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Agencia := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."AGENCIA_ID_AGENCIA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ALCUNHA_ID_ALCUNHA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ALCUNHA_ID_ALCUNHA_TRG" BEFORE INSERT OR UPDATE ON Alcunha
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Alcunha IS NULL THEN
    SELECT  Alcunha_Id_Alcunha_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Alcunha),0) INTO v_newVal FROM Alcunha;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Alcunha_Id_Alcunha_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Alcunha := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ALCUNHA_ID_ALCUNHA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AREADISTRIBUICAO_ID_AREADIST_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AREADISTRIBUICAO_ID_AREADIST_1" BEFORE
INSERT
OR UPDATE ON "PROJUDI"."AREA_DIST" REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW DECLARE 
v_newVal NUMBER(12) := 0; 
v_incval NUMBER(12) := 0; 
BEGIN 
  IF INSERTING AND :new.Id_Area_Dist IS NULL THEN 
    SELECT  AreaDistribuicao_Id_AreaDistri.NEXTVAL INTO v_newVal FROM DUAL; 
    -- If this is the first time this table have been inserted into (sequence == 1) 
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table 
      SELECT NVL(max(Id_Area_Dist),0) INTO v_newVal FROM Area_Dist; 
      v_newVal := v_newVal + 1; 
      --set the sequence to that value 
      LOOP 
           EXIT WHEN v_incval>=v_newVal; 
           SELECT AreaDistribuicao_Id_AreaDistri.nextval INTO v_incval FROM dual; 
      END LOOP; 
    END IF; 
    --used to emulate LAST_INSERT_ID() 
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column 
   :new.Id_Area_Dist := v_newVal; 
  END IF; 
END; 

/
ALTER TRIGGER "PROJUDI"."AREADISTRIBUICAO_ID_AREADIST_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AREA_ID_AREA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AREA_ID_AREA_TRG" BEFORE INSERT OR UPDATE ON Area
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Area IS NULL THEN
    SELECT  Area_Id_Area_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Area),0) INTO v_newVal FROM Area;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Area_Id_Area_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Area := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."AREA_ID_AREA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ARQUIVO_BANCO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARQUIVO_BANCO_TRG" BEFORE INSERT OR UPDATE ON ARQUIVO_BANCO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ARQUIVO_BANCO IS NULL THEN
    SELECT  ARQUIVO_BANCO_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ARQUIVO_BANCO),0) INTO v_newVal FROM ARQUIVO_BANCO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ARQUIVO_BANCO_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ARQUIVO_BANCO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ARQUIVO_BANCO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ARQUIVO_ID_ARQUIVO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARQUIVO_ID_ARQUIVO_1" BEFORE INSERT OR UPDATE ON ARQ
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ARQ IS NULL THEN
    SELECT  ARQUIVO_ID_ARQUIVO_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ARQ),0) INTO v_newVal FROM ARQ;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ARQUIVO_ID_ARQUIVO_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ARQ := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ARQUIVO_ID_ARQUIVO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ARQUIVOPALAVRA_ID_ARQUIVOPAL_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARQUIVOPALAVRA_ID_ARQUIVOPAL_1" BEFORE INSERT OR UPDATE ON ARQ_PALAVRA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ARQ_PALAVRA IS NULL THEN
    SELECT  ArquivoPalavra_Id_ArquivoPalav.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ARQ_PALAVRA),0) INTO v_newVal FROM ARQ_PALAVRA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ArquivoPalavra_Id_ArquivoPalav.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ARQ_PALAVRA := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ARQUIVOPALAVRA_ID_ARQUIVOPAL_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ARQUIVOTIPO_ID_ARQUIVOTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARQUIVOTIPO_ID_ARQUIVOTIPO_TRG" BEFORE INSERT OR UPDATE ON ARQ_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ARQ_TIPO IS NULL THEN
    SELECT  ArquivoTipo_Id_ArquivoTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ARQ_TIPO),0) INTO v_newVal FROM ARQ_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ArquivoTipo_Id_ArquivoTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ARQ_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ARQUIVOTIPO_ID_ARQUIVOTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ARRECADACAOCUSTA_ID_ARRECADA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ARRECADACAOCUSTA_ID_ARRECADA_1" BEFORE INSERT OR UPDATE ON ARRECADACAO_CUSTA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ARRECADACAO_CUSTA IS NULL THEN
    SELECT  ArrecadacaoCusta_Id_Arrecadaca.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ARRECADACAO_CUSTA),0) INTO v_newVal FROM ARRECADACAO_CUSTA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ArrecadacaoCusta_Id_Arrecadaca.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ARRECADACAO_CUSTA := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ARRECADACAOCUSTA_ID_ARRECADA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ASSUNTO_ID_ASSUNTO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ASSUNTO_ID_ASSUNTO_TRG" BEFORE INSERT OR UPDATE ON Assunto
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Assunto IS NULL THEN
    SELECT  Assunto_Id_Assunto_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Assunto),0) INTO v_newVal FROM Assunto;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Assunto_Id_Assunto_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Assunto := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ASSUNTO_ID_ASSUNTO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AUDIENCIA_ID_AUDIENCIA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AUDIENCIA_ID_AUDIENCIA_TRG" BEFORE INSERT OR UPDATE ON AUDI
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_AUDI IS NULL THEN
    SELECT  Audiencia_Id_Audiencia_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_AUDI),0) INTO v_newVal FROM AUDI;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Audiencia_Id_Audiencia_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_AUDI := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."AUDIENCIA_ID_AUDIENCIA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AUDIENCIAPROCESSORESPONSAVEL_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AUDIENCIAPROCESSORESPONSAVEL_1" BEFORE INSERT OR UPDATE ON AUDI_PROC_RESP
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_AUDI_PROC_RESPONSAVEL IS NULL THEN
    SELECT  AudienciaProcessoResponsavel_I.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_AUDI_PROC_RESPONSAVEL),0) INTO v_newVal FROM AUDI_PROC_RESP;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT AudienciaProcessoResponsavel_I.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_AUDI_PROC_RESPONSAVEL := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."AUDIENCIAPROCESSORESPONSAVEL_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger AUDIENCIATIPO_ID_AUDIENCIATI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."AUDIENCIATIPO_ID_AUDIENCIATI_1" BEFORE INSERT OR UPDATE ON AUDI_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_AUDI_TIPO IS NULL THEN
    SELECT  AudienciaTipo_Id_AudienciaTipo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_AUDI_TIPO),0) INTO v_newVal FROM AUDI_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT AudienciaTipo_Id_AudienciaTipo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_AUDI_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."AUDIENCIATIPO_ID_AUDIENCIATI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger BAIRRO_ID_BAIRRO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."BAIRRO_ID_BAIRRO_TRG" BEFORE INSERT OR UPDATE ON Bairro
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Bairro IS NULL THEN
    SELECT  Bairro_Id_Bairro_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Bairro),0) INTO v_newVal FROM Bairro;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Bairro_Id_Bairro_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Bairro := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."BAIRRO_ID_BAIRRO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger BANCO_ID_BANCO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."BANCO_ID_BANCO_TRG" BEFORE INSERT OR UPDATE ON Banco
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Banco IS NULL THEN
    SELECT  Banco_Id_Banco_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Banco),0) INTO v_newVal FROM Banco;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Banco_Id_Banco_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Banco := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."BANCO_ID_BANCO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CALCULOLIQUIDACAO_ID_CALCULO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CALCULOLIQUIDACAO_ID_CALCULO_1" BEFORE INSERT OR UPDATE ON CALCULO_LIQUIDACAO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CALCULO_LIQUIDACAO IS NULL THEN
    SELECT  CalculoLiquidacao_Id_CalculoLi.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CALCULO_LIQUIDACAO),0) INTO v_newVal FROM CALCULO_LIQUIDACAO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CalculoLiquidacao_Id_CalculoLi.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CALCULO_LIQUIDACAO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."CALCULOLIQUIDACAO_ID_CALCULO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CARGOTIPO_ID_CARGOTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CARGOTIPO_ID_CARGOTIPO_TRG" BEFORE INSERT OR UPDATE ON CARGO_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CARGO_TIPO IS NULL THEN
    SELECT  CargoTipo_Id_CargoTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CARGO_TIPO),0) INTO v_newVal FROM CARGO_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CargoTipo_Id_CargoTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CARGO_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CARGOTIPO_ID_CARGOTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CERTIDAOTIPO_ID_CERTIDAOTIPO_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CERTIDAOTIPO_ID_CERTIDAOTIPO_T" BEFORE INSERT OR UPDATE ON CERT_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CERT_TIPO IS NULL THEN
    SELECT  CertidaoTipo_Id_CertidaoTipo_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CERT_TIPO),0) INTO v_newVal FROM CERT_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CertidaoTipo_Id_CertidaoTipo_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CERT_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CERTIDAOTIPO_ID_CERTIDAOTIPO_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CERTIDAOTIPOPROCESSOTIPO_ID__1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CERTIDAOTIPOPROCESSOTIPO_ID__1" BEFORE INSERT OR UPDATE ON CERT_TIPO_PROC_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CERT_TIPO_PROC_TIPO IS NULL THEN
    SELECT  CertidaoTipoProcessoTipo_Id_Ce.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CERT_TIPO_PROC_TIPO),0) INTO v_newVal FROM CERT_TIPO_PROC_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CertidaoTipoProcessoTipo_Id_Ce.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CERT_TIPO_PROC_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CERTIDAOTIPOPROCESSOTIPO_ID__1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CERTIFICADOCONFIAVEL_ID_CERT_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CERTIFICADOCONFIAVEL_ID_CERT_1" BEFORE INSERT OR UPDATE ON CERTIFICADO_CONFIAVEL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CERTIFICADO_CONFIAVEL IS NULL THEN
    SELECT  CertificadoConfiavel_Id_Certif.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CERTIFICADO_CONFIAVEL),0) INTO v_newVal FROM CERTIFICADO_CONFIAVEL;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CertificadoConfiavel_Id_Certif.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CERTIFICADO_CONFIAVEL := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CERTIFICADOCONFIAVEL_ID_CERT_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CERTIFICADO_ID_CERTIFICADO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CERTIFICADO_ID_CERTIFICADO_TRG" BEFORE INSERT OR UPDATE ON Certificado
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Certificado IS NULL THEN
    SELECT  Certificado_Id_Certificado_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Certificado),0) INTO v_newVal FROM Certificado;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Certificado_Id_Certificado_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Certificado := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."CERTIFICADO_ID_CERTIFICADO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIDADE_ID_CIDADE_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CIDADE_ID_CIDADE_TRG" BEFORE INSERT OR UPDATE ON Cidade
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Cidade IS NULL THEN
    SELECT  Cidade_Id_Cidade_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Cidade),0) INTO v_newVal FROM Cidade;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Cidade_Id_Cidade_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Cidade := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CIDADE_ID_CIDADE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CIDADE_SSP_ID_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CIDADE_SSP_ID_TRG" BEFORE INSERT OR UPDATE ON CIDADE_SSP
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Cidade_ssp IS NULL THEN
    SELECT  CIDADE_SSP_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Cidade_ssp),0) INTO v_newVal FROM Cidade_ssp;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CIDADE_SSP_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Cidade_ssp := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CIDADE_SSP_ID_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CLASSIFICADOR_ID_CLASSIFICAD_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CLASSIFICADOR_ID_CLASSIFICAD_1" BEFORE INSERT OR UPDATE ON Classificador
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Classificador IS NULL THEN
    SELECT  Classificador_Id_Classificador.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Classificador),0) INTO v_newVal FROM Classificador;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Classificador_Id_Classificador.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Classificador := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."CLASSIFICADOR_ID_CLASSIFICAD_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger COMARCACIDADE_ID_COMARCACIDA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."COMARCACIDADE_ID_COMARCACIDA_1" BEFORE INSERT OR UPDATE ON COMARCA_CIDADE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_COMARCA_CIDADE IS NULL THEN
    SELECT  ComarcaCidade_Id_ComarcaCidade.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_COMARCA_CIDADE),0) INTO v_newVal FROM COMARCA_CIDADE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ComarcaCidade_Id_ComarcaCidade.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_COMARCA_CIDADE := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."COMARCACIDADE_ID_COMARCACIDA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger COMARCA_ID_COMARCA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."COMARCA_ID_COMARCA_TRG" BEFORE INSERT OR UPDATE ON Comarca
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Comarca IS NULL THEN
    SELECT  Comarca_Id_Comarca_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Comarca),0) INTO v_newVal FROM Comarca;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Comarca_Id_Comarca_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Comarca := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."COMARCA_ID_COMARCA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CONDENACAOEXECUCAO_ID_CONDEN_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CONDENACAOEXECUCAO_ID_CONDEN_1" BEFORE INSERT OR UPDATE ON CONDENACAO_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CONDENACAO_EXE IS NULL THEN
    SELECT  CondenacaoExecucao_Id_Condenac.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CONDENACAO_EXE),0) INTO v_newVal FROM CONDENACAO_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CondenacaoExecucao_Id_Condenac.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CONDENACAO_EXE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."CONDENACAOEXECUCAO_ID_CONDEN_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CONDENACAOEXECUCAOSITUACAO_I_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CONDENACAOEXECUCAOSITUACAO_I_1" BEFORE INSERT OR UPDATE ON CONDENACAO_EXE_SIT
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CONDENACAO_EXE_SIT IS NULL THEN
    SELECT  CondenacaoExecucaoSituacao_Id_.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CONDENACAO_EXE_SIT),0) INTO v_newVal FROM CONDENACAO_EXE_SIT;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CondenacaoExecucaoSituacao_Id_.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CONDENACAO_EXE_SIT := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CONDENACAOEXECUCAOSITUACAO_I_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CONTAUSUARIO_ID_CONTAUSUARIO_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CONTAUSUARIO_ID_CONTAUSUARIO_T" BEFORE INSERT OR UPDATE ON CONTA_USU
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CONTA_USU IS NULL THEN
    SELECT  ContaUsuario_Id_ContaUsuario_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CONTA_USU),0) INTO v_newVal FROM CONTA_USU;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ContaUsuario_Id_ContaUsuario_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CONTA_USU := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CONTAUSUARIO_ID_CONTAUSUARIO_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CRIMEEXECUCAO_ID_CRIMEEXECUC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CRIMEEXECUCAO_ID_CRIMEEXECUC_1" BEFORE INSERT OR UPDATE ON CRIME_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CRIME_EXE IS NULL THEN
    SELECT  CrimeExecucao_Id_CrimeExecucao.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CRIME_EXE),0) INTO v_newVal FROM CRIME_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CrimeExecucao_Id_CrimeExecucao.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CRIME_EXE := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CRIMEEXECUCAO_ID_CRIMEEXECUC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CUSTA_ID_CUSTA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CUSTA_ID_CUSTA_TRG" BEFORE INSERT OR UPDATE ON Custa
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Custa IS NULL THEN
    SELECT  Custa_Id_Custa_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Custa),0) INTO v_newVal FROM Custa;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Custa_Id_Custa_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Custa := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CUSTA_ID_CUSTA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger CUSTAVALOR_ID_CUSTAVALOR_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."CUSTAVALOR_ID_CUSTAVALOR_TRG" BEFORE INSERT OR UPDATE ON CUSTA_VALOR
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_CUSTA_VALOR IS NULL THEN
    SELECT  CustaValor_Id_CustaValor_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_CUSTA_VALOR),0) INTO v_newVal FROM CUSTA_VALOR;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT CustaValor_Id_CustaValor_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_CUSTA_VALOR := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."CUSTAVALOR_ID_CUSTAVALOR_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EMENTA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EMENTA_TRG" BEFORE
  INSERT OR UPDATE ON EMENTA FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  v_incval NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_EMENTA IS NULL THEN
      SELECT SEQ_EMENTA.NEXTVAL INTO v_newVal FROM DUAL;
      -- If this is the first time this table have been inserted into (sequence == 1)
      IF v_newVal = 1 THEN
        --get the max indentity value from the table
        SELECT NVL(MAX(ID_EMENTA),0)
        INTO v_newVal
        FROM EMENTA;
        v_newVal := v_newVal + 1;
        --set the sequence to that value
        LOOP
          EXIT
        WHEN v_incval>=v_newVal;
          SELECT SEQ_EMENTA.nextval INTO v_incval FROM dual;
        END LOOP;
      END IF;
      --used to emulate LAST_INSERT_ID()
      --mysql_utilities.identity := v_newVal;
      -- assign the value from the sequence to emulate the identity column
      :new.ID_EMENTA := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."EMENTA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EMPRESATIPO_ID_EMPRESATIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EMPRESATIPO_ID_EMPRESATIPO_TRG" BEFORE INSERT OR UPDATE ON EMPRESA_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EMPRESA_TIPO IS NULL THEN
    SELECT  EmpresaTipo_Id_EmpresaTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EMPRESA_TIPO),0) INTO v_newVal FROM EMPRESA_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EmpresaTipo_Id_EmpresaTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EMPRESA_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."EMPRESATIPO_ID_EMPRESATIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ENDERECO_ID_ENDERECO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ENDERECO_ID_ENDERECO_TRG" BEFORE INSERT OR UPDATE ON Endereco
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Endereco IS NULL THEN
    SELECT  Endereco_Id_Endereco_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Endereco),0) INTO v_newVal FROM Endereco;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Endereco_Id_Endereco_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Endereco := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ENDERECO_ID_ENDERECO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ESCALA_ID_ESCALA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ESCALA_ID_ESCALA_TRG" BEFORE INSERT OR UPDATE ON ESC
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ESC IS NULL THEN
    SELECT  Escala_Id_Escala_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ESC),0) INTO v_newVal FROM ESC;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Escala_Id_Escala_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ESC := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ESCALA_ID_ESCALA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ESCOLARIDADE_ID_ESCOLARIDADE_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ESCOLARIDADE_ID_ESCOLARIDADE_T" BEFORE INSERT OR UPDATE ON Escolaridade
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Escolaridade IS NULL THEN
    SELECT  Escolaridade_Id_Escolaridade_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Escolaridade),0) INTO v_newVal FROM Escolaridade;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Escolaridade_Id_Escolaridade_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Escolaridade := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ESCOLARIDADE_ID_ESCOLARIDADE_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ESTADOCIVIL_ID_ESTADOCIVIL_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ESTADOCIVIL_ID_ESTADOCIVIL_TRG" BEFORE INSERT OR UPDATE ON ESTADO_CIVIL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ESTADO_CIVIL IS NULL THEN
    SELECT  EstadoCivil_Id_EstadoCivil_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ESTADO_CIVIL),0) INTO v_newVal FROM ESTADO_CIVIL;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EstadoCivil_Id_EstadoCivil_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ESTADO_CIVIL := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ESTADOCIVIL_ID_ESTADOCIVIL_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ESTADO_ID_ESTADO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ESTADO_ID_ESTADO_TRG" BEFORE INSERT OR UPDATE ON Estado
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Estado IS NULL THEN
    SELECT  Estado_Id_Estado_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Estado),0) INTO v_newVal FROM Estado;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Estado_Id_Estado_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Estado := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ESTADO_ID_ESTADO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ESTATISTICARELATORIOESTPROIT_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ESTATISTICARELATORIOESTPROIT_1" BEFORE INSERT OR UPDATE ON EST_RELATORIO_EST_PRO_ITEMS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EST_REL_EST_PRO_ITENS IS NULL THEN
    SELECT  EstatisticaRelatorioEstProIten.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EST_REL_EST_PRO_ITENS),0) INTO v_newVal FROM EST_RELATORIO_EST_PRO_ITEMS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EstatisticaRelatorioEstProIten.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EST_REL_EST_PRO_ITENS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."ESTATISTICARELATORIOESTPROIT_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EVENTOEXECUCAO_ID_EVENTOEXEC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EVENTOEXECUCAO_ID_EVENTOEXEC_1" BEFORE INSERT OR UPDATE ON EVENTO_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EVENTO_EXE IS NULL THEN
    SELECT  EventoExecucao_Id_EventoExecuc.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EVENTO_EXE),0) INTO v_newVal FROM EVENTO_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EventoExecucao_Id_EventoExecuc.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EVENTO_EXE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."EVENTOEXECUCAO_ID_EVENTOEXEC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EVENTOEXECUCAOSTATUS_ID_EVEN_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EVENTOEXECUCAOSTATUS_ID_EVEN_1" BEFORE INSERT OR UPDATE ON EVENTO_EXE_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EVENTO_EXE_STATUS IS NULL THEN
    SELECT  EventoExecucaoStatus_Id_Evento.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EVENTO_EXE_STATUS),0) INTO v_newVal FROM EVENTO_EXE_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EventoExecucaoStatus_Id_Evento.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EVENTO_EXE_STATUS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."EVENTOEXECUCAOSTATUS_ID_EVEN_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EVENTOEXECUCAOTIPO_ID_EVENTO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EVENTOEXECUCAOTIPO_ID_EVENTO_1" BEFORE INSERT OR UPDATE ON EVENTO_EXE_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EVENTO_EXE_TIPO IS NULL THEN
    SELECT  EventoExecucaoTipo_Id_EventoEx.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EVENTO_EXE_TIPO),0) INTO v_newVal FROM EVENTO_EXE_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EventoExecucaoTipo_Id_EventoEx.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EVENTO_EXE_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."EVENTOEXECUCAOTIPO_ID_EVENTO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EVENTOLOCAL_ID_EVENTOLOCAL_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EVENTOLOCAL_ID_EVENTOLOCAL_TRG" BEFORE INSERT OR UPDATE ON EVENTO_LOCAL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EVENTO_LOCAL IS NULL THEN
    SELECT  EventoLocal_Id_EventoLocal_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EVENTO_LOCAL),0) INTO v_newVal FROM EVENTO_LOCAL;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EventoLocal_Id_EventoLocal_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EVENTO_LOCAL := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."EVENTOLOCAL_ID_EVENTOLOCAL_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EVENTOREGIME_ID_EVENTOREGIME_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."EVENTOREGIME_ID_EVENTOREGIME_T" BEFORE INSERT OR UPDATE ON EVENTO_REGIME
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_EVENTO_REGIME IS NULL THEN
    SELECT  EventoRegime_Id_EventoRegime_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_EVENTO_REGIME),0) INTO v_newVal FROM EVENTO_REGIME;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT EventoRegime_Id_EventoRegime_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_EVENTO_REGIME := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."EVENTOREGIME_ID_EVENTOREGIME_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger FINALIDADE_ID_FINALIDADE_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."FINALIDADE_ID_FINALIDADE_TRG" 
 BEFORE INSERT OR UPDATE ON PROJUDI.FINALIDADE
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_newVal NUMBER(10) := 0;
v_incval NUMBER(10) := 0;
BEGIN
  IF INSERTING AND :new.ID_FINALIDADE IS NULL THEN
    SELECT  FINALIDADE_ID_FINALIDADE_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_FINALIDADE),0) INTO v_newVal FROM FINALIDADE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT FINALIDADE_ID_FINALIDADE_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_FINALIDADE := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."FINALIDADE_ID_FINALIDADE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger FORUM_ID_FORUM_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."FORUM_ID_FORUM_TRG" BEFORE INSERT OR UPDATE ON Forum
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Forum IS NULL THEN
    SELECT  Forum_Id_Forum_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Forum),0) INTO v_newVal FROM Forum;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Forum_Id_Forum_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Forum := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."FORUM_ID_FORUM_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GOVERNOTIPO_ID_GOVERNOTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GOVERNOTIPO_ID_GOVERNOTIPO_TRG" BEFORE INSERT OR UPDATE ON GOVERNO_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GOVERNO_TIPO IS NULL THEN
    SELECT  GovernoTipo_Id_GovernoTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GOVERNO_TIPO),0) INTO v_newVal FROM GOVERNO_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GovernoTipo_Id_GovernoTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GOVERNO_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GOVERNOTIPO_ID_GOVERNOTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPOARQUIVOTIPO_ID_GRUPOARQ_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPOARQUIVOTIPO_ID_GRUPOARQ_1" BEFORE INSERT OR UPDATE ON GRUPO_ARQ_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GRUPO_ARQ_TIPO IS NULL THEN
    SELECT  GrupoArquivoTipo_Id_GrupoArqui.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GRUPO_ARQ_TIPO),0) INTO v_newVal FROM GRUPO_ARQ_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GrupoArquivoTipo_Id_GrupoArqui.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GRUPO_ARQ_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPOARQUIVOTIPO_ID_GRUPOARQ_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPO_ID_GRUPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPO_ID_GRUPO_TRG" BEFORE INSERT OR UPDATE ON Grupo
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Grupo IS NULL THEN
    SELECT  Grupo_Id_Grupo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Grupo),0) INTO v_newVal FROM Grupo;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Grupo_Id_Grupo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Grupo := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPO_ID_GRUPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPOMOVIMENTACAOTIPO_ID_GRU_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPOMOVIMENTACAOTIPO_ID_GRU_1" BEFORE INSERT OR UPDATE ON GRUPO_MOVI_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GRUPO_MOVI_TIPO IS NULL THEN
    SELECT  GrupoMovimentacaoTipo_Id_Grupo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GRUPO_MOVI_TIPO),0) INTO v_newVal FROM GRUPO_MOVI_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GrupoMovimentacaoTipo_Id_Grupo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GRUPO_MOVI_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPOMOVIMENTACAOTIPO_ID_GRU_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPOPENDENCIATIPO_ID_GRUPOP_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPOPENDENCIATIPO_ID_GRUPOP_1" BEFORE INSERT OR UPDATE ON GRUPO_PEND_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GRUPO_PEND_TIPO IS NULL THEN
    SELECT  GrupoPendenciaTipo_Id_GrupoPen.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GRUPO_PEND_TIPO),0) INTO v_newVal FROM GRUPO_PEND_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GrupoPendenciaTipo_Id_GrupoPen.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GRUPO_PEND_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPOPENDENCIATIPO_ID_GRUPOP_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPOPERMISSAO_ID_GRUPOPERMI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPOPERMISSAO_ID_GRUPOPERMI_1" BEFORE INSERT OR UPDATE ON GRUPO_PERM
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GRUPO_PERM IS NULL THEN
    SELECT  GrupoPermissao_Id_GrupoPermiss.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GRUPO_PERM),0) INTO v_newVal FROM GRUPO_PERM;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GrupoPermissao_Id_GrupoPermiss.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GRUPO_PERM := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPOPERMISSAO_ID_GRUPOPERMI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GRUPOTIPO_ID_GRUPOTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GRUPOTIPO_ID_GRUPOTIPO_TRG" BEFORE INSERT OR UPDATE ON GRUPO_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GRUPO_TIPO IS NULL THEN
    SELECT  GrupoTipo_Id_GrupoTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GRUPO_TIPO),0) INTO v_newVal FROM GRUPO_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GrupoTipo_Id_GrupoTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GRUPO_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GRUPOTIPO_ID_GRUPOTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIACUSTAMODELO_ID_GUIACUSTA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIACUSTAMODELO_ID_GUIACUSTA_1" BEFORE INSERT OR UPDATE ON GUIA_CUSTA_MODELO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_CUSTA_MODELO IS NULL THEN
    SELECT  GuiaCustaModelo_Id_GuiaCustaMo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_CUSTA_MODELO),0) INTO v_newVal FROM GUIA_CUSTA_MODELO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaCustaModelo_Id_GuiaCustaMo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_CUSTA_MODELO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIACUSTAMODELO_ID_GUIACUSTA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIAEMISSAO_ID_GUIAEMISSAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIAEMISSAO_ID_GUIAEMISSAO_TRG" BEFORE INSERT OR UPDATE ON GUIA_EMIS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_EMIS IS NULL THEN
    SELECT  GuiaEmissao_Id_GuiaEmissao_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_EMIS),0) INTO v_newVal FROM GUIA_EMIS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaEmissao_Id_GuiaEmissao_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_EMIS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIAEMISSAO_ID_GUIAEMISSAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIAITEM_ID_GUIAITEM_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIAITEM_ID_GUIAITEM_TRG" BEFORE INSERT OR UPDATE ON GUIA_ITEM
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_ITEM IS NULL THEN
    SELECT  GuiaItem_Id_GuiaItem_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_ITEM),0) INTO v_newVal FROM GUIA_ITEM;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaItem_Id_GuiaItem_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_ITEM := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIAITEM_ID_GUIAITEM_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIAMODELO_ID_GUIAMODELO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIAMODELO_ID_GUIAMODELO_TRG" BEFORE INSERT OR UPDATE ON GUIA_MODELO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_MODELO IS NULL THEN
    SELECT  GuiaModelo_Id_GuiaModelo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_MODELO),0) INTO v_newVal FROM GUIA_MODELO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaModelo_Id_GuiaModelo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_MODELO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIAMODELO_ID_GUIAMODELO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIANUMERO_ID_GUIANUMERO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIANUMERO_ID_GUIANUMERO_TRG" BEFORE INSERT OR UPDATE ON GUIA_NUMERO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_NUMERO IS NULL THEN
    SELECT  GuiaNumero_Id_GuiaNumero_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_NUMERO),0) INTO v_newVal FROM GUIA_NUMERO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaNumero_Id_GuiaNumero_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_NUMERO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIANUMERO_ID_GUIANUMERO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIASTATUS_ID_GUIASTATUS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIASTATUS_ID_GUIASTATUS_TRG" BEFORE INSERT OR UPDATE ON GUIA_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_STATUS IS NULL THEN
    SELECT  GuiaStatus_Id_GuiaStatus_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_STATUS),0) INTO v_newVal FROM GUIA_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaStatus_Id_GuiaStatus_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_STATUS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIASTATUS_ID_GUIASTATUS_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger GUIATIPO_ID_GUIATIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."GUIATIPO_ID_GUIATIPO_TRG" BEFORE INSERT OR UPDATE ON GUIA_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_GUIA_TIPO IS NULL THEN
    SELECT  GuiaTipo_Id_GuiaTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_GUIA_TIPO),0) INTO v_newVal FROM GUIA_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT GuiaTipo_Id_GuiaTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_GUIA_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."GUIATIPO_ID_GUIATIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger HISTORICOLOTACAO_ID_HISTORIC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."HISTORICOLOTACAO_ID_HISTORIC_1" BEFORE INSERT OR UPDATE ON HIS_LOTACAO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_HIS_LOTACAO IS NULL THEN
    SELECT  HistoricoLotacao_Id_HistoricoL.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_HIS_LOTACAO),0) INTO v_newVal FROM HIS_LOTACAO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT HistoricoLotacao_Id_HistoricoL.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_HIS_LOTACAO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."HISTORICOLOTACAO_ID_HISTORIC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger LOCALCUMPRIMENTOPENA_ID_LOCA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOCALCUMPRIMENTOPENA_ID_LOCA_1" BEFORE INSERT OR UPDATE ON LOCAL_CUMP_PENA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_LOCAL_CUMP_PENA IS NULL THEN
    SELECT  LocalCumprimentoPena_Id_LocalC.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_LOCAL_CUMP_PENA),0) INTO v_newVal FROM LOCAL_CUMP_PENA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT LocalCumprimentoPena_Id_LocalC.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_LOCAL_CUMP_PENA := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."LOCALCUMPRIMENTOPENA_ID_LOCA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger LOCOMOCAO_ID_LOCOMOCAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOCOMOCAO_ID_LOCOMOCAO_TRG" BEFORE INSERT OR UPDATE ON Locomocao
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Locomocao IS NULL THEN
    SELECT  Locomocao_Id_Locomocao_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Locomocao),0) INTO v_newVal FROM Locomocao;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Locomocao_Id_Locomocao_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Locomocao := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."LOCOMOCAO_ID_LOCOMOCAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger LOG_ERROS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOG_ERROS_TRG" BEFORE
  INSERT OR
  UPDATE ON LOG_ERROS FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  v_incval NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_LOG_ERROS IS NULL THEN
      SELECT LOG_ERROS_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
      -- If this is the first time this table have been inserted into (sequence == 1)
      IF v_newVal = 1 THEN
        --get the max indentity value from the table
        SELECT NVL(MAX(ID_LOG_ERROS),0)
        INTO v_newVal
        FROM LOG_ERROS;
        v_newVal := v_newVal + 1;
        --set the sequence to that value
        LOOP
          EXIT
        WHEN v_incval>=v_newVal;
          SELECT LOG_ERROS_SEQ.nextval INTO v_incval FROM dual;
        END LOOP;
      END IF;
      --used to emulate LAST_INSERT_ID()
      --mysql_utilities.identity := v_newVal;
      -- assign the value from the sequence to emulate the identity column
      :new.ID_LOG_ERROS := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."LOG_ERROS_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger LOG_ID_LOG_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOG_ID_LOG_TRG" BEFORE INSERT OR UPDATE ON LOG
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Log IS NULL THEN
    SELECT  Log_Id_Log_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Log),0) INTO v_newVal FROM Log;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Log_Id_Log_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Log := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."LOG_ID_LOG_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger LOGON_PROJUDI
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOGON_PROJUDI" AFTER
LOGON ON PROJUDI.SCHEMA

BEGIN
   execute immediate 'alter session set nls_comp=linguistic nls_sort=binary_ai';
END;
/
ALTER TRIGGER "PROJUDI"."LOGON_PROJUDI" ENABLE;
BEGIN 
  DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY('"PROJUDI"','"LOGON_PROJUDI"',FALSE) ; 
END;

/
--------------------------------------------------------
--  DDL for Trigger LOGTIPO_ID_LOGTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."LOGTIPO_ID_LOGTIPO_TRG" BEFORE INSERT OR UPDATE ON LOG_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_LOG_TIPO IS NULL THEN
    SELECT  LogTipo_Id_LogTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_LOG_TIPO),0) INTO v_newVal FROM LOG_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT LogTipo_Id_LogTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_LOG_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."LOGTIPO_ID_LOGTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MANDADOJUDICIAL_ID_MANDADOJU_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MANDADOJUDICIAL_ID_MANDADOJU_1" BEFORE INSERT OR UPDATE ON MAND_JUD
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MAND_JUD IS NULL THEN
    SELECT  MandadoJudicial_Id_MandadoJudi.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MAND_JUD),0) INTO v_newVal FROM MAND_JUD;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT MandadoJudicial_Id_MandadoJudi.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MAND_JUD := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MANDADOJUDICIAL_ID_MANDADOJU_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MANDADOTIPO_ID_MANDADOTIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MANDADOTIPO_ID_MANDADOTIPO_TRG" BEFORE INSERT OR UPDATE ON MAND_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MAND_TIPO IS NULL THEN
    SELECT  MandadoTipo_Id_MandadoTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MAND_TIPO),0) INTO v_newVal FROM MAND_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT MandadoTipo_Id_MandadoTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MAND_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MANDADOTIPO_ID_MANDADOTIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MAND_JUD_STATUS_ID_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MAND_JUD_STATUS_ID_TRG" BEFORE INSERT OR UPDATE ON MAND_JUD_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MAND_JUD_STATUS IS NULL THEN
    SELECT  MAND_JUD_STATUS_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MAND_JUD_STATUS),0) INTO v_newVal FROM MAND_JUD_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT MAND_JUD_STATUS_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MAND_JUD_STATUS := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."MAND_JUD_STATUS_ID_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MODELO_ID_MODELO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MODELO_ID_MODELO_TRG" BEFORE INSERT OR UPDATE ON Modelo
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Modelo IS NULL THEN
    SELECT  Modelo_Id_Modelo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Modelo),0) INTO v_newVal FROM Modelo;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Modelo_Id_Modelo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Modelo := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MODELO_ID_MODELO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MOVIMENTACAOARQUIVO_ID_MOVIM_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MOVIMENTACAOARQUIVO_ID_MOVIM_1" BEFORE INSERT OR UPDATE ON MOVI_ARQ
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MOVI_ARQ IS NULL THEN
    SELECT  MovimentacaoArquivo_Id_Movimen.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MOVI_ARQ),0) INTO v_newVal FROM MOVI_ARQ;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT MovimentacaoArquivo_Id_Movimen.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MOVI_ARQ := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MOVIMENTACAOARQUIVO_ID_MOVIM_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MOVIMENTACAO_ID_MOVIMENTACAO_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MOVIMENTACAO_ID_MOVIMENTACAO_T" BEFORE INSERT OR UPDATE ON MOVI
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MOVI IS NULL THEN
    SELECT  Movimentacao_Id_Movimentacao_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MOVI),0) INTO v_newVal FROM MOVI;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Movimentacao_Id_Movimentacao_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MOVI := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MOVIMENTACAO_ID_MOVIMENTACAO_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger MOVIMENTACAOTIPO_ID_MOVIMENT_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."MOVIMENTACAOTIPO_ID_MOVIMENT_1" BEFORE INSERT OR UPDATE ON MOVI_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_MOVI_TIPO IS NULL THEN
    SELECT  MovimentacaoTipo_Id_Movimentac.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_MOVI_TIPO),0) INTO v_newVal FROM MOVI_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT MovimentacaoTipo_Id_Movimentac.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_MOVI_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."MOVIMENTACAOTIPO_ID_MOVIMENT_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger OBJETOPEDIDO_ID_OBJETOPEDIDO_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."OBJETOPEDIDO_ID_OBJETOPEDIDO_T" BEFORE INSERT OR UPDATE ON OBJETO_PEDIDO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_OBJETO_PEDIDO IS NULL THEN
    SELECT  ObjetoPedido_Id_ObjetoPedido_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_OBJETO_PEDIDO),0) INTO v_newVal FROM OBJETO_PEDIDO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ObjetoPedido_Id_ObjetoPedido_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_OBJETO_PEDIDO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."OBJETOPEDIDO_ID_OBJETOPEDIDO_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger OCORRENCIA_INTERRUPCAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."OCORRENCIA_INTERRUPCAO_TRG" BEFORE
  INSERT OR
  UPDATE ON OCORRENCIA_INTERRUPCAO FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  v_incval NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_OCORRENCIA_INTERRUPCAO IS NULL THEN
      SELECT OCORRENCIA_INTERRUPCAO_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
      -- If this is the first time this table have been inserted into (sequence == 1)
      IF v_newVal = 1 THEN
        --get the max indentity value from the table
        SELECT NVL(MAX(ID_OCORRENCIA_INTERRUPCAO),0)
        INTO v_newVal
        FROM OCORRENCIA_INTERRUPCAO;
        v_newVal := v_newVal + 1;
        --set the sequence to that value
        LOOP
          EXIT
        WHEN v_incval>=v_newVal;
          SELECT OCORRENCIA_INTERRUPCAO_SEQ.nextval INTO v_incval FROM dual;
        END LOOP;
      END IF;
      --used to emulate LAST_INSERT_ID()
      --mysql_utilities.identity := v_newVal;
      -- assign the value from the sequence to emulate the identity column
      :new.ID_OCORRENCIA_INTERRUPCAO := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."OCORRENCIA_INTERRUPCAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAIS_ID_PAIS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PAIS_ID_PAIS_TRG" BEFORE INSERT OR UPDATE ON Pais
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Pais IS NULL THEN
    SELECT  Pais_Id_Pais_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Pais),0) INTO v_newVal FROM Pais;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Pais_Id_Pais_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Pais := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PAIS_ID_PAIS_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PALAVRA_ID_PALAVRA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PALAVRA_ID_PALAVRA_TRG" BEFORE INSERT OR UPDATE ON Palavra
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Palavra IS NULL THEN
    SELECT  Palavra_Id_Palavra_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Palavra),0) INTO v_newVal FROM Palavra;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Palavra_Id_Palavra_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Palavra := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PALAVRA_ID_PALAVRA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PARAMETROCOMUTACAOEXE_ID_PA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PARAMETROCOMUTACAOEXE_ID_PA_1" 
 BEFORE INSERT OR UPDATE ON PROJUDI.PARAMETRO_COMUTACAO_EXE
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PARAMETRO_COMUTACAO_EXE IS NULL THEN
    SELECT  ParametroComutacaoExe_Id_Seq.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PARAMETRO_COMUTACAO_EXE),0) INTO v_newVal FROM PARAMETRO_COMUTACAO_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ParametroComutacaoExe_Id_Seq.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PARAMETRO_COMUTACAO_EXE := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."PARAMETROCOMUTACAOEXE_ID_PA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PARAMETROCRIMEEXECUCAO_ID_PA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PARAMETROCRIMEEXECUCAO_ID_PA_1" BEFORE INSERT OR UPDATE ON PARAMETRO_CRIME_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PARAMETRO_CRIME_EXE IS NULL THEN
    SELECT  ParametroCrimeExecucao_Id_Para.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PARAMETRO_CRIME_EXE),0) INTO v_newVal FROM PARAMETRO_CRIME_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ParametroCrimeExecucao_Id_Para.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PARAMETRO_CRIME_EXE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PARAMETROCRIMEEXECUCAO_ID_PA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENAEXECUCAOTIPO_ID_PENAEXEC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENAEXECUCAOTIPO_ID_PENAEXEC_1" BEFORE INSERT OR UPDATE ON PENA_EXE_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PENA_EXE_TIPO IS NULL THEN
    SELECT  PenaExecucaoTipo_Id_PenaExecuc.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PENA_EXE_TIPO),0) INTO v_newVal FROM PENA_EXE_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PenaExecucaoTipo_Id_PenaExecuc.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PENA_EXE_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENAEXECUCAOTIPO_ID_PENAEXEC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIAARQUIVO_ID_PENDENCI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIAARQUIVO_ID_PENDENCI_1" BEFORE INSERT OR UPDATE ON PEND_ARQ
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_ARQ IS NULL THEN
    SELECT  PendenciaArquivo_Id_PendenciaA.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_ARQ),0) INTO v_newVal FROM PEND_ARQ;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaArquivo_Id_PendenciaA.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_ARQ := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIAARQUIVO_ID_PENDENCI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIA_ID_PENDENCIA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIA_ID_PENDENCIA_TRG" BEFORE INSERT OR UPDATE ON PEND
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND IS NULL THEN
    SELECT  Pendencia_Id_Pendencia_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND),0) INTO v_newVal FROM PEND;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Pendencia_Id_Pendencia_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIA_ID_PENDENCIA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIARESPONSAVEL_ID_PEND_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIARESPONSAVEL_ID_PEND_1" BEFORE INSERT OR UPDATE ON PEND_RESP
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_RESP IS NULL THEN
    SELECT  PendenciaResponsavel_Id_Penden.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_RESP),0) INTO v_newVal FROM PEND_RESP;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaResponsavel_Id_Penden.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_RESP := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIARESPONSAVEL_ID_PEND_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIASTATUS_ID_PENDENCIA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIASTATUS_ID_PENDENCIA_1" BEFORE INSERT OR UPDATE ON PEND_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_STATUS IS NULL THEN
    SELECT  PendenciaStatus_Id_PendenciaSt.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_STATUS),0) INTO v_newVal FROM PEND_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaStatus_Id_PendenciaSt.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_STATUS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIASTATUS_ID_PENDENCIA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIATIPOCONCLUSAO_ID_PE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIATIPOCONCLUSAO_ID_PE_1" BEFORE INSERT OR UPDATE ON PEND_TIPO_CONCLUSAO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_TIPO_CONCLUSAO IS NULL THEN
    SELECT  PendenciaTipoConclusao_Id_Pend.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_TIPO_CONCLUSAO),0) INTO v_newVal FROM PEND_TIPO_CONCLUSAO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaTipoConclusao_Id_Pend.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_TIPO_CONCLUSAO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIATIPOCONCLUSAO_ID_PE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIATIPO_ID_PENDENCIATI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIATIPO_ID_PENDENCIATI_1" BEFORE INSERT OR UPDATE ON PEND_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_TIPO IS NULL THEN
    SELECT  PendenciaTipo_Id_PendenciaTipo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_TIPO),0) INTO v_newVal FROM PEND_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaTipo_Id_PendenciaTipo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIATIPO_ID_PENDENCIATI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PENDENCIATIPORELACIONADA_ID__1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PENDENCIATIPORELACIONADA_ID__1" BEFORE INSERT OR UPDATE ON PEND_TIPO_RELACIONADA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PEND_TIPO_RELACIONADA IS NULL THEN
    SELECT  PendenciaTipoRelacionada_Id_Pe.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PEND_TIPO_RELACIONADA),0) INTO v_newVal FROM PEND_TIPO_RELACIONADA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PendenciaTipoRelacionada_Id_Pe.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PEND_TIPO_RELACIONADA := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PENDENCIATIPORELACIONADA_ID__1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PERMISSAOESPECIAL_ID_PERMISS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PERMISSAOESPECIAL_ID_PERMISS_1" BEFORE INSERT OR UPDATE ON PERM_ESPECIAL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PERM_ESPECIAL IS NULL THEN
    SELECT  PermissaoEspecial_Id_Permissao.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PERM_ESPECIAL),0) INTO v_newVal FROM PERM_ESPECIAL;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PermissaoEspecial_Id_Permissao.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PERM_ESPECIAL := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PERMISSAOESPECIAL_ID_PERMISS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PERMISSAO_ID_PERMISSAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PERMISSAO_ID_PERMISSAO_TRG" BEFORE INSERT OR UPDATE ON PERM
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PERM IS NULL THEN
    SELECT  Permissao_Id_Permissao_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PERM),0) INTO v_newVal FROM PERM;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Permissao_Id_Permissao_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PERM := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PERMISSAO_ID_PERMISSAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PRAZOSUSPENSO_ID_PRAZOSUSPEN_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PRAZOSUSPENSO_ID_PRAZOSUSPEN_1" BEFORE INSERT OR UPDATE ON PRAZO_SUSPENSO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PRAZO_SUSPENSO IS NULL THEN
    SELECT  PrazoSuspenso_Id_PrazoSuspenso.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PRAZO_SUSPENSO),0) INTO v_newVal FROM PRAZO_SUSPENSO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PrazoSuspenso_Id_PrazoSuspenso.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PRAZO_SUSPENSO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PRAZOSUSPENSO_ID_PRAZOSUSPEN_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PRAZOSUSPENSOTIPO_ID_PRAZOSU_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PRAZOSUSPENSOTIPO_ID_PRAZOSU_1" BEFORE INSERT OR UPDATE ON PRAZO_SUSPENSO_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PRAZO_SUSPENSO_TIPO IS NULL THEN
    SELECT  PrazoSuspensoTipo_Id_PrazoSusp.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PRAZO_SUSPENSO_TIPO),0) INTO v_newVal FROM PRAZO_SUSPENSO_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT PrazoSuspensoTipo_Id_PrazoSusp.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PRAZO_SUSPENSO_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PRAZOSUSPENSOTIPO_ID_PRAZOSU_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOASSUNTO_ID_PROCESSOA_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOASSUNTO_ID_PROCESSOA_1" BEFORE INSERT OR UPDATE ON PROC_ASSUNTO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_ASSUNTO IS NULL THEN
    SELECT  ProcessoAssunto_Id_ProcessoAss.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_ASSUNTO),0) INTO v_newVal FROM PROC_ASSUNTO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoAssunto_Id_ProcessoAss.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_ASSUNTO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOASSUNTO_ID_PROCESSOA_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOBENEFICIO_ID_PROCESS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOBENEFICIO_ID_PROCESS_1" BEFORE INSERT OR UPDATE ON PROC_BENEFICIO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_BENEFICIO IS NULL THEN
    SELECT  ProcessoBeneficio_Id_ProcessoB.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_BENEFICIO),0) INTO v_newVal FROM PROC_BENEFICIO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoBeneficio_Id_ProcessoB.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_BENEFICIO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOBENEFICIO_ID_PROCESS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSODEBITO_ID_PROCESSODE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSODEBITO_ID_PROCESSODE_1" BEFORE INSERT OR UPDATE ON PROC_DEBITO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_DEBITO IS NULL THEN
    SELECT  ProcessoDebito_Id_ProcessoDebi.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_DEBITO),0) INTO v_newVal FROM PROC_DEBITO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoDebito_Id_ProcessoDebi.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_DEBITO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSODEBITO_ID_PROCESSODE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOEVENTOEXECUCAO_ID_PR_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOEVENTOEXECUCAO_ID_PR_1" BEFORE INSERT OR UPDATE ON PROC_EVENTO_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_EVENTO_EXE IS NULL THEN
    SELECT  ProcessoEventoExecucao_Id_Proc.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_EVENTO_EXE),0) INTO v_newVal FROM PROC_EVENTO_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoEventoExecucao_Id_Proc.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_EVENTO_EXE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOEVENTOEXECUCAO_ID_PR_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOEXECUCAO_ID_PROCESSO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOEXECUCAO_ID_PROCESSO_1" BEFORE INSERT OR UPDATE ON PROC_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_EXE IS NULL THEN
    SELECT  ProcessoExecucao_Id_ProcessoEx.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_EXE),0) INTO v_newVal FROM PROC_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoExecucao_Id_ProcessoEx.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_EXE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOEXECUCAO_ID_PROCESSO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOFASE_ID_PROCESSOFASE_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOFASE_ID_PROCESSOFASE_T" BEFORE INSERT OR UPDATE ON PROC_FASE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_FASE IS NULL THEN
    SELECT  ProcessoFase_Id_ProcessoFase_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_FASE),0) INTO v_newVal FROM PROC_FASE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoFase_Id_ProcessoFase_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_FASE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOFASE_ID_PROCESSOFASE_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSO_ID_PROCESSO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSO_ID_PROCESSO_TRG" BEFORE INSERT OR UPDATE ON PROC
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC IS NULL THEN
    SELECT  Processo_Id_Processo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC),0) INTO v_newVal FROM PROC;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Processo_Id_Processo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSO_ID_PROCESSO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSONUMERO_ID_PROCESSONU_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSONUMERO_ID_PROCESSONU_1" BEFORE INSERT OR UPDATE ON PROC_NUMERO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_NUMERO IS NULL THEN
    SELECT  ProcessoNumero_Id_ProcessoNume.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_NUMERO),0) INTO v_newVal FROM PROC_NUMERO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoNumero_Id_ProcessoNume.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_NUMERO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSONUMERO_ID_PROCESSONU_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTEADVOGADO_ID_PRO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTEADVOGADO_ID_PRO_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_ADVOGADO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_ADVOGADO IS NULL THEN
    SELECT  ProcessoParteAdvogado_Id_Proce.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_ADVOGADO),0) INTO v_newVal FROM PROC_PARTE_ADVOGADO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteAdvogado_Id_Proce.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_ADVOGADO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTEADVOGADO_ID_PRO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTEALCUNHA_ID_PROC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTEALCUNHA_ID_PROC_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_ALCUNHA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_ALCUNHA IS NULL THEN
    SELECT  ProcessoParteAlcunha_Id_Proces.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_ALCUNHA),0) INTO v_newVal FROM PROC_PARTE_ALCUNHA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteAlcunha_Id_Proces.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_ALCUNHA := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTEALCUNHA_ID_PROC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTEAUSENCIA_ID_PRO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTEAUSENCIA_ID_PRO_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_AUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_AUS IS NULL THEN
    SELECT  ProcessoParteAusencia_Id_Proce.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_AUS),0) INTO v_newVal FROM PROC_PARTE_AUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteAusencia_Id_Proce.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_AUS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTEAUSENCIA_ID_PRO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTEBENEFICIO_ID_PR_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTEBENEFICIO_ID_PR_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_BENEFICIO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_BENEFICIO IS NULL THEN
    SELECT  ProcessoParteBeneficio_Id_Proc.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_BENEFICIO),0) INTO v_newVal FROM PROC_PARTE_BENEFICIO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteBeneficio_Id_Proc.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_BENEFICIO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTEBENEFICIO_ID_PR_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTEDEBITO_ID_PROCE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTEDEBITO_ID_PROCE_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_DEBITO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_DEBITO IS NULL THEN
    SELECT  ProcessoParteDebito_Id_Process.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_DEBITO),0) INTO v_newVal FROM PROC_PARTE_DEBITO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteDebito_Id_Process.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_DEBITO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTEDEBITO_ID_PROCE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTE_ID_PROCESSOPAR_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTE_ID_PROCESSOPAR_1" BEFORE INSERT OR UPDATE ON PROC_PARTE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE IS NULL THEN
    SELECT  ProcessoParte_Id_ProcessoParte.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE),0) INTO v_newVal FROM PROC_PARTE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParte_Id_ProcessoParte.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTE_ID_PROCESSOPAR_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTESINAL_ID_PROCES_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTESINAL_ID_PROCES_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_SINAL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_SINAL IS NULL THEN
    SELECT  ProcessoParteSinal_Id_Processo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_SINAL),0) INTO v_newVal FROM PROC_PARTE_SINAL;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteSinal_Id_Processo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_SINAL := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTESINAL_ID_PROCES_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPARTETIPO_ID_PROCESS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPARTETIPO_ID_PROCESS_1" BEFORE INSERT OR UPDATE ON PROC_PARTE_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PARTE_TIPO IS NULL THEN
    SELECT  ProcessoParteTipo_Id_ProcessoP.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PARTE_TIPO),0) INTO v_newVal FROM PROC_PARTE_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoParteTipo_Id_ProcessoP.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PARTE_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPARTETIPO_ID_PROCESS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOPRIORIDADE_ID_PROCES_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOPRIORIDADE_ID_PROCES_1" BEFORE INSERT OR UPDATE ON PROC_PRIOR
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_PRIOR IS NULL THEN
    SELECT  ProcessoPrioridade_Id_Processo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_PRIOR),0) INTO v_newVal FROM PROC_PRIOR;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoPrioridade_Id_Processo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_PRIOR := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOPRIORIDADE_ID_PROCES_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSORESPONSAVEL_ID_PROCE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSORESPONSAVEL_ID_PROCE_1" BEFORE INSERT OR UPDATE ON PROC_RESP
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_RESP IS NULL THEN
    SELECT  ProcessoResponsavel_Id_Process.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_RESP),0) INTO v_newVal FROM PROC_RESP;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoResponsavel_Id_Process.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_RESP := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSORESPONSAVEL_ID_PROCE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOSITUACAO_ID_PROCESSO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOSITUACAO_ID_PROCESSO_1" BEFORE INSERT OR UPDATE ON PROC_SIT
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_SIT IS NULL THEN
    SELECT  ProcessoSituacao_Id_ProcessoSi.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_SIT),0) INTO v_newVal FROM PROC_SIT;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoSituacao_Id_ProcessoSi.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_SIT := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOSITUACAO_ID_PROCESSO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOSTATUS_ID_PROCESSOST_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOSTATUS_ID_PROCESSOST_1" BEFORE INSERT OR UPDATE ON PROC_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_STATUS IS NULL THEN
    SELECT  ProcessoStatus_Id_ProcessoStat.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_STATUS),0) INTO v_newVal FROM PROC_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoStatus_Id_ProcessoStat.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_STATUS := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOSTATUS_ID_PROCESSOST_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOSUBTIPO_ID_PROCESSOS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOSUBTIPO_ID_PROCESSOS_1" BEFORE INSERT OR UPDATE ON PROC_SUBTIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_SUBTIPO IS NULL THEN
    SELECT  ProcessoSubtipo_Id_ProcessoSub.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_SUBTIPO),0) INTO v_newVal FROM PROC_SUBTIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoSubtipo_Id_ProcessoSub.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_SUBTIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOSUBTIPO_ID_PROCESSOS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOTIPO_ID_PROCESSOTIPO_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOTIPO_ID_PROCESSOTIPO_T" BEFORE INSERT OR UPDATE ON PROC_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_TIPO IS NULL THEN
    SELECT  ProcessoTipo_Id_ProcessoTipo_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_TIPO),0) INTO v_newVal FROM PROC_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoTipo_Id_ProcessoTipo_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_TIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOTIPO_ID_PROCESSOTIPO_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROCESSOTIPOPROCESSOSUBTIPO__1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROCESSOTIPOPROCESSOSUBTIPO__1" BEFORE INSERT OR UPDATE ON PROC_TIPO_PROC_SUBTIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_TIPO_PROC_SUBTIPO IS NULL THEN
    SELECT  ProcessoTipoProcessoSubtipo_Id.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROC_TIPO_PROC_SUBTIPO),0) INTO v_newVal FROM PROC_TIPO_PROC_SUBTIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProcessoTipoProcessoSubtipo_Id.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROC_TIPO_PROC_SUBTIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROCESSOTIPOPROCESSOSUBTIPO__1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROFISSAO_ID_PROFISSAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROFISSAO_ID_PROFISSAO_TRG" BEFORE INSERT OR UPDATE ON Profissao
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Profissao IS NULL THEN
    SELECT  Profissao_Id_Profissao_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Profissao),0) INTO v_newVal FROM Profissao;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Profissao_Id_Profissao_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Profissao := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROFISSAO_ID_PROFISSAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROJETO_ID_PROJETO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROJETO_ID_PROJETO_TRG" BEFORE INSERT OR UPDATE ON Projeto
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Projeto IS NULL THEN
    SELECT  Projeto_Id_Projeto_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Projeto),0) INTO v_newVal FROM Projeto;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Projeto_Id_Projeto_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Projeto := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROJETO_ID_PROJETO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROJETOPARTICIPANTE_ID_PROJE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROJETOPARTICIPANTE_ID_PROJE_1" BEFORE INSERT OR UPDATE ON PROJETO_PARTICIPANTE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROJETO_PARTICIPANTE IS NULL THEN
    SELECT  ProjetoParticipante_Id_Projeto.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_PROJETO_PARTICIPANTE),0) INTO v_newVal FROM PROJETO_PARTICIPANTE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ProjetoParticipante_Id_Projeto.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_PROJETO_PARTICIPANTE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROJETOPARTICIPANTE_ID_PROJE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PROPRIEDADE_ID_PROPRIEDADE_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."PROPRIEDADE_ID_PROPRIEDADE_TRG" BEFORE INSERT OR UPDATE ON Propriedade
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Propriedade IS NULL THEN
    SELECT  Propriedade_Id_Propriedade_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Propriedade),0) INTO v_newVal FROM Propriedade;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Propriedade_Id_Propriedade_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Propriedade := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."PROPRIEDADE_ID_PROPRIEDADE_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RECURSOASSUNTO_ID_RECURSOASS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RECURSOASSUNTO_ID_RECURSOASS_1" BEFORE INSERT OR UPDATE ON RECURSO_ASSUNTO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_RECURSO_ASSUNTO IS NULL THEN
    SELECT  RecursoAssunto_Id_RecursoAssun.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_RECURSO_ASSUNTO),0) INTO v_newVal FROM RECURSO_ASSUNTO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RecursoAssunto_Id_RecursoAssun.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_RECURSO_ASSUNTO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."RECURSOASSUNTO_ID_RECURSOASS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RECURSO_ID_RECURSO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RECURSO_ID_RECURSO_TRG" BEFORE INSERT OR UPDATE ON Recurso
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Recurso IS NULL THEN
    SELECT  Recurso_Id_Recurso_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Recurso),0) INTO v_newVal FROM Recurso;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Recurso_Id_Recurso_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Recurso := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."RECURSO_ID_RECURSO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RECURSOPARTE_ID_RECURSOPARTE_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RECURSOPARTE_ID_RECURSOPARTE_T" BEFORE INSERT OR UPDATE ON RECURSO_PARTE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_RECURSO_PARTE IS NULL THEN
    SELECT  RecursoParte_Id_RecursoParte_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_RECURSO_PARTE),0) INTO v_newVal FROM RECURSO_PARTE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RecursoParte_Id_RecursoParte_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_RECURSO_PARTE := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."RECURSOPARTE_ID_RECURSOPARTE_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger REGIAO_ID_REGIAO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."REGIAO_ID_REGIAO_TRG" BEFORE INSERT OR UPDATE ON Regiao
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Regiao IS NULL THEN
    SELECT  Regiao_Id_Regiao_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Regiao),0) INTO v_newVal FROM Regiao;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Regiao_Id_Regiao_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Regiao := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."REGIAO_ID_REGIAO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger REGIMEEXECUCAO_ID_REGIMEEXEC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."REGIMEEXECUCAO_ID_REGIMEEXEC_1" BEFORE INSERT OR UPDATE ON REGIME_EXE
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_REGIME_EXE IS NULL THEN
    SELECT  RegimeExecucao_Id_RegimeExecuc.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_REGIME_EXE),0) INTO v_newVal FROM REGIME_EXE;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RegimeExecucao_Id_RegimeExecuc.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_REGIME_EXE := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."REGIMEEXECUCAO_ID_REGIMEEXEC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RELATORIOESTPRO_ID_RELATORIOES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RELATORIOESTPRO_ID_RELATORIOES" BEFORE INSERT OR UPDATE ON RELATORIO_EST_PRO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_RELATORIO_EST_PRO IS NULL THEN
    SELECT  RELATORIOESTPRO_ID_RELATORIO_1.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_RELATORIO_EST_PRO),0) INTO v_newVal FROM RELATORIO_EST_PRO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RELATORIOESTPRO_ID_RELATORIO_1.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_RELATORIO_EST_PRO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."RELATORIOESTPRO_ID_RELATORIOES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RELATORIOPERIODICO_ID_RELATO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RELATORIOPERIODICO_ID_RELATO_1" BEFORE INSERT OR UPDATE ON RELATORIO_PERIODICO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_RELATORIO_PERIODICO IS NULL THEN
    SELECT  RelatorioPeriodico_Id_Relatori.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_RELATORIO_PERIODICO),0) INTO v_newVal FROM RELATORIO_PERIODICO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RelatorioPeriodico_Id_Relatori.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_RELATORIO_PERIODICO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."RELATORIOPERIODICO_ID_RELATO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger RGORGAOEXPEDIDOR_ID_RGORGAOE_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."RGORGAOEXPEDIDOR_ID_RGORGAOE_1" BEFORE INSERT OR UPDATE ON RG_ORGAO_EXP
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_RG_ORGAO_EXP IS NULL THEN
    SELECT  RgOrgaoExpedidor_Id_RgOrgaoExp.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_RG_ORGAO_EXP),0) INTO v_newVal FROM RG_ORGAO_EXP;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT RgOrgaoExpedidor_Id_RgOrgaoExp.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_RG_ORGAO_EXP := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."RGORGAOEXPEDIDOR_ID_RGORGAOE_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVAREADIST_ID_SERVAREADIST_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVAREADIST_ID_SERVAREADIST_T" BEFORE INSERT OR UPDATE ON SERV_AREA_DIST
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_AREA_DIST IS NULL THEN
    SELECT  ServAreaDist_Id_ServAreaDist_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_AREA_DIST),0) INTO v_newVal FROM SERV_AREA_DIST;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServAreaDist_Id_ServAreaDist_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_AREA_DIST := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."SERVAREADIST_ID_SERVAREADIST_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIACARGO_ID_SERVENTIAC_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIACARGO_ID_SERVENTIAC_1" BEFORE INSERT OR UPDATE ON SERV_CARGO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_CARGO IS NULL THEN
    SELECT  ServentiaCargo_Id_ServentiaCar.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_CARGO),0) INTO v_newVal FROM SERV_CARGO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaCargo_Id_ServentiaCar.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_CARGO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."SERVENTIACARGO_ID_SERVENTIAC_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIA_ID_SERVENTIA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIA_ID_SERVENTIA_TRG" BEFORE INSERT OR UPDATE ON SERV
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV IS NULL THEN
    SELECT  Serventia_Id_Serventia_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV),0) INTO v_newVal FROM SERV;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Serventia_Id_Serventia_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."SERVENTIA_ID_SERVENTIA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIARELACIONADA_ID_SERV_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIARELACIONADA_ID_SERV_1" BEFORE INSERT OR UPDATE ON SERV_RELACIONADA
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_RELACIONADA IS NULL THEN
    SELECT  ServentiaRelacionada_Id_Serven.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_RELACIONADA),0) INTO v_newVal FROM SERV_RELACIONADA;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaRelacionada_Id_Serven.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_RELACIONADA := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."SERVENTIARELACIONADA_ID_SERV_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIASUBTIPOASSUNTO_ID_S_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIASUBTIPOASSUNTO_ID_S_1" BEFORE INSERT OR UPDATE ON SERV_SUBTIPO_ASSUNTO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_SUBTIPO_ASSUNTO IS NULL THEN
    SELECT  ServentiaSubtipoAssunto_Id_Ser.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_SUBTIPO_ASSUNTO),0) INTO v_newVal FROM SERV_SUBTIPO_ASSUNTO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaSubtipoAssunto_Id_Ser.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_SUBTIPO_ASSUNTO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."SERVENTIASUBTIPOASSUNTO_ID_S_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIASUBTIPO_ID_SERVENTI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIASUBTIPO_ID_SERVENTI_1" BEFORE INSERT OR UPDATE ON SERV_SUBTIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_SUBTIPO IS NULL THEN
    SELECT  ServentiaSubtipo_Id_ServentiaS.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_SUBTIPO),0) INTO v_newVal FROM SERV_SUBTIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaSubtipo_Id_ServentiaS.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_SUBTIPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."SERVENTIASUBTIPO_ID_SERVENTI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIASUBTIPOPROCESSOTIPO_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIASUBTIPOPROCESSOTIPO_1" BEFORE INSERT OR UPDATE ON SERV_SUBTIPO_PROC_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_SUBTIPO_PROC_TIPO IS NULL THEN
    SELECT  ServentiaSubtipoProcessoTipo_I.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_SUBTIPO_PROC_TIPO),0) INTO v_newVal FROM SERV_SUBTIPO_PROC_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaSubtipoProcessoTipo_I.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_SUBTIPO_PROC_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."SERVENTIASUBTIPOPROCESSOTIPO_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SERVENTIATIPO_ID_SERVENTIATI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SERVENTIATIPO_ID_SERVENTIATI_1" BEFORE INSERT OR UPDATE ON SERV_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_SERV_TIPO IS NULL THEN
    SELECT  ServentiaTipo_Id_ServentiaTipo.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_SERV_TIPO),0) INTO v_newVal FROM SERV_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ServentiaTipo_Id_ServentiaTipo.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_SERV_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."SERVENTIATIPO_ID_SERVENTIATI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SINAL_ID_SINAL_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SINAL_ID_SINAL_TRG" BEFORE INSERT OR UPDATE ON Sinal
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Sinal IS NULL THEN
    SELECT  Sinal_Id_Sinal_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Sinal),0) INTO v_newVal FROM Sinal;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Sinal_Id_Sinal_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Sinal := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."SINAL_ID_SINAL_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger SISTEMA_ID_SISTEMA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."SISTEMA_ID_SISTEMA_TRG" BEFORE INSERT OR UPDATE ON Sistema
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Sistema IS NULL THEN
    SELECT  Sistema_Id_Sistema_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Sistema),0) INTO v_newVal FROM Sistema;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Sistema_Id_Sistema_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Sistema := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."SISTEMA_ID_SISTEMA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TAREFA_ID_TAREFA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TAREFA_ID_TAREFA_TRG" BEFORE INSERT OR UPDATE ON Tarefa
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Tarefa IS NULL THEN
    SELECT  Tarefa_Id_Tarefa_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Tarefa),0) INTO v_newVal FROM Tarefa;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Tarefa_Id_Tarefa_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Tarefa := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TAREFA_ID_TAREFA_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TAREFAPRIORIDADE_ID_TAREFAPR_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TAREFAPRIORIDADE_ID_TAREFAPR_1" BEFORE INSERT OR UPDATE ON TAREFA_PRIOR
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_TAREFA_PRIOR IS NULL THEN
    SELECT  TarefaPrioridade_Id_TarefaPrio.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_TAREFA_PRIOR),0) INTO v_newVal FROM TAREFA_PRIOR;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT TarefaPrioridade_Id_TarefaPrio.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_TAREFA_PRIOR := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TAREFAPRIORIDADE_ID_TAREFAPR_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TAREFASTATUS_ID_TAREFASTATUS_T
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TAREFASTATUS_ID_TAREFASTATUS_T" BEFORE INSERT OR UPDATE ON TAREFA_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_TAREFA_STATUS IS NULL THEN
    SELECT  TarefaStatus_Id_TarefaStatus_S.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_TAREFA_STATUS),0) INTO v_newVal FROM TAREFA_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT TarefaStatus_Id_TarefaStatus_S.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_TAREFA_STATUS := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TAREFASTATUS_ID_TAREFASTATUS_T" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TAREFATIPO_ID_TAREFATIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TAREFATIPO_ID_TAREFATIPO_TRG" BEFORE INSERT OR UPDATE ON TAREFA_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_TAREFA_TIPO IS NULL THEN
    SELECT  TarefaTipo_Id_TarefaTipo_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_TAREFA_TIPO),0) INTO v_newVal FROM TAREFA_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT TarefaTipo_Id_TarefaTipo_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_TAREFA_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TAREFATIPO_ID_TAREFATIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRANSITOJULGADOEVENTO_ID_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRANSITOJULGADOEVENTO_ID_1" BEFORE INSERT OR UPDATE ON PROJUDI.TRANS_JULGADO_EVENTO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_TRANS_JULGADO_EVENTO IS NULL THEN
    SELECT  TransitoJulgadoEvento_Id_Co.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_TRANS_JULGADO_EVENTO),0) INTO v_newVal FROM TRANS_JULGADO_EVENTO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT TransitoJulgadoEvento_Id_Co.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_TRANS_JULGADO_EVENTO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRANSITOJULGADOEVENTO_ID_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_AUDI_PROC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_AUDI_PROC" BEFORE INSERT  ON AUDI_PROC
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_AUDI_PROC IS NULL THEN
    SELECT  AudienciaProcesso_Id_Audiencia.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_AUDI_PROC),0) INTO v_newVal FROM AUDI_PROC;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT AudienciaProcesso_Id_Audiencia.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_AUDI_PROC := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRG_AUDI_PROC" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_AUDI_PROC_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_AUDI_PROC_STATUS" BEFORE INSERT  ON AUDI_PROC_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_AUDI_PROC_STATUS IS NULL THEN
    SELECT  AudienciaProcessoStatus_Id_Aud.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_AUDI_PROC_STATUS),0) INTO v_newVal FROM AUDI_PROC_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT AudienciaProcessoStatus_Id_Aud.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_AUDI_PROC_STATUS := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRG_AUDI_PROC_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_CERT
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_CERT" BEFORE
  INSERT ON CERT FOR EACH ROW DECLARE v_newVal NUMBER(20) := 0;
  BEGIN
    IF INSERTING AND :new.ID_CERT IS NULL THEN
      SELECT SEQ_CERT.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_CERT := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."TRG_CERT" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_GUIA_FINALIDADE_MODELO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_GUIA_FINALIDADE_MODELO" 
BEFORE INSERT OR UPDATE ON PROJUDI.GUIA_FINALIDADE_MODELO
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_newVal NUMBER(24) := 0;
BEGIN
 IF INSERTING AND :new.ID_GUIA_FINALIDADE_MODELO IS NULL THEN
   SELECT  GUIAFINALIDADEMODELO_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;      
  :new.ID_GUIA_FINALIDADE_MODELO := v_newVal;
 END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRG_GUIA_FINALIDADE_MODELO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_GUIA_FINALID_MODELO_CUSTA
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_GUIA_FINALID_MODELO_CUSTA" 
BEFORE INSERT OR UPDATE ON PROJUDI.GUIA_FINALID_MODELO_CUSTA
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW  
DECLARE
v_newVal NUMBER(10) := 0;

BEGIN
 IF INSERTING AND :new.ID_GUIA_FINALID_MODELO_CUSTA IS NULL THEN
   SELECT  GUIAFINALIDMODELOCUSTA_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;      
  :new.ID_GUIA_FINALID_MODELO_CUSTA := v_newVal;
 END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRG_GUIA_FINALID_MODELO_CUSTA" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_MANDADO_PRISAO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO" 
BEFORE INSERT ON "PROJUDI".MANDADO_PRISAO
REFERENCING NEW AS new 
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
BEGIN
    IF INSERTING AND :new.ID_MANDADO_PRISAO IS NULL THEN
      SELECT SEQ_MANDADO_PRISAO.NEXTVAL INTO v_newVal FROM DUAL;      
      :new.ID_MANDADO_PRISAO := v_newVal;      
    END IF;
END;
/
ALTER TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_MANDADO_PRISAO_ARQUIVO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_ARQUIVO" BEFORE
  INSERT ON "PROJUDI".MANDADO_PRISAO_ARQUIVO REFERENCING NEW AS NEW FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_MANDADO_PRISAO_ARQUIVO IS NULL THEN
      SELECT SEQ_MANDADO_PRISAO_ARQUIVO.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_MANDADO_PRISAO_ARQUIVO := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_ARQUIVO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_MANDADO_PRISAO_ORIGEM
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_ORIGEM" BEFORE
  INSERT ON "PROJUDI"."MANDADO_PRISAO_ORIGEM" FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_MANDADO_PRISAO_ORIGEM IS NULL THEN
      SELECT SEQ_MANDADO_PRISAO_ORIGEM.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_MANDADO_PRISAO_ORIGEM     := v_newVal;
      :new.MANDADO_PRISAO_ORIGEM_CODIGO := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_ORIGEM" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_MANDADO_PRISAO_STATUS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_STATUS" BEFORE
  INSERT ON "PROJUDI".MANDADO_PRISAO_STATUS FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_MANDADO_PRISAO_STATUS IS NULL THEN
      SELECT SEQ_MANDADO_PRISAO_STATUS.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_MANDADO_PRISAO_STATUS     := v_newVal;
      :new.MANDADO_PRISAO_STATUS_CODIGO := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."TRG_MANDADO_PRISAO_STATUS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_OFICIAL_CERT
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_OFICIAL_CERT" BEFORE
  INSERT ON OFICIAL_CERT REFERENCING NEW AS NEW FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_OFICIAL_CERT IS NULL THEN
      SELECT SEQ_OFICIAL_CERT.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_OFICIAL_CERT := v_newVal;
    END IF;
  END;
  
/
ALTER TRIGGER "PROJUDI"."TRG_OFICIAL_CERT" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_PEND_RESP_HIST
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_PEND_RESP_HIST" BEFORE
  INSERT ON PEND_RESP_HIST FOR EACH ROW DECLARE v_newVal NUMBER(20) := 0;
  BEGIN
    IF INSERTING AND :new.ID_PEND_RESP_HIST IS NULL THEN
      SELECT SEQ_PEND_RESP_HIST.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_PEND_RESP_HIST := v_newVal;
    END IF;
  END;
  
/
ALTER TRIGGER "PROJUDI"."TRG_PEND_RESP_HIST" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_PRISAO_TIPO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_PRISAO_TIPO" BEFORE
  INSERT ON PROJUDI.PRISAO_TIPO REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW DECLARE v_newVal NUMBER(12) := 0;
  BEGIN
    IF INSERTING AND :new.ID_PRISAO_TIPO IS NULL THEN
      SELECT SEQ_PRISAO_TIPO.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_PRISAO_TIPO     := v_newVal;
      :new.PRISAO_TIPO_CODIGO := v_newVal;
    END IF;
  END;
/
ALTER TRIGGER "PROJUDI"."TRG_PRISAO_TIPO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_PROC_CRIMINAL
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_PROC_CRIMINAL" 
 BEFORE INSERT ON PROJUDI.PROC_CRIMINAL
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_incval NUMBER(24) := 0;
BEGIN
 IF INSERTING AND :new.ID_PROC_CRIMINAL IS NULL THEN
    SELECT SEQ_PROC_CRIMINAL.nextval INTO v_incval FROM dual;
    :new.ID_PROC_CRIMINAL := v_incval;
 END IF;

END;
/
ALTER TRIGGER "PROJUDI"."TRG_PROC_CRIMINAL" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_SERV_CARGO_SERV_GRUPO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_SERV_CARGO_SERV_GRUPO" BEFORE
  INSERT ON SERV_CARGO_SERV_GRUPO FOR EACH ROW DECLARE v_newVal NUMBER(20) := 0;
  BEGIN
    IF INSERTING AND :new.ID_SERV_CARGO_SERV_GRUPO IS NULL THEN
      SELECT SEQ_SERV_CARGO_SERV_GRUPO.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_SERV_CARGO_SERV_GRUPO := v_newVal;
    END IF;
  END;
  
/
ALTER TRIGGER "PROJUDI"."TRG_SERV_CARGO_SERV_GRUPO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_SERV_GRUPO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRG_SERV_GRUPO" BEFORE
  INSERT ON SERV_GRUPO FOR EACH ROW DECLARE v_newVal NUMBER(20) := 0;
  BEGIN
    IF INSERTING AND :new.ID_SERV_GRUPO IS NULL THEN
      SELECT SEQ_SERV_GRUPO.NEXTVAL INTO v_newVal FROM DUAL;
      :new.ID_SERV_GRUPO := v_newVal;
    END IF;
  END;
  
/
ALTER TRIGGER "PROJUDI"."TRG_SERV_GRUPO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIGGER1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRIGGER1" 
 BEFORE INSERT ON PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_incval NUMBER(12) := 0;
BEGIN
 IF INSERTING AND :new.ID_MOVI_TIPO_MOVI_TIPO_CLASSE IS NULL THEN
    SELECT SEQ_MOVI_TIPO_MOVI_TIPO_CLASSE.nextval INTO v_incval FROM dual;
    :new.ID_MOVI_TIPO_MOVI_TIPO_CLASSE := v_incval;
 END IF;

END;
/
ALTER TRIGGER "PROJUDI"."TRIGGER1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRI_MOVI_TIPO_CLASSE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."TRI_MOVI_TIPO_CLASSE" 
 BEFORE INSERT ON PROJUDI.MOVI_TIPO_CLASSE
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
v_incval NUMBER(12) := 0;
BEGIN
 IF INSERTING AND :new.ID_MOVI_TIPO_CLASSE IS NULL THEN
    SELECT SEQ_MOVI_TIPO_CLASSE.nextval INTO v_incval FROM dual;
    :new.ID_MOVI_TIPO_CLASSE := v_incval;
 END IF;

END;
/
ALTER TRIGGER "PROJUDI"."TRI_MOVI_TIPO_CLASSE" ENABLE;
--------------------------------------------------------
--  DDL for Trigger UFR_VALOR_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."UFR_VALOR_TRG" BEFORE INSERT OR UPDATE ON UFR_VALOR
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_UFR_VALOR IS NULL THEN
    SELECT  UFRVALOR_ID_UFR_VALOR_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_UFR_VALOR),0) INTO v_newVal FROM UFR_VALOR;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UFRVALOR_ID_UFR_VALOR_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_UFR_VALOR := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."UFR_VALOR_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOAFASTAMENTO_ID_USUARI_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOAFASTAMENTO_ID_USUARI_1" BEFORE INSERT OR UPDATE ON USU_AFASTAMENTO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_AFASTAMENTO IS NULL THEN
    SELECT  UsuarioAfastamento_Id_UsuarioA.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_AFASTAMENTO),0) INTO v_newVal FROM USU_AFASTAMENTO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioAfastamento_Id_UsuarioA.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_AFASTAMENTO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USUARIOAFASTAMENTO_ID_USUARI_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOARQUIVO_ID_USUARIOARQ_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOARQUIVO_ID_USUARIOARQ_1" BEFORE INSERT OR UPDATE ON USU_ARQ
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_ARQ IS NULL THEN
    SELECT  UsuarioArquivo_Id_UsuarioArqui.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_ARQ),0) INTO v_newVal FROM USU_ARQ;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioArquivo_Id_UsuarioArqui.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_ARQ := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."USUARIOARQUIVO_ID_USUARIOARQ_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIO_ID_USUARIO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIO_ID_USUARIO_TRG" BEFORE INSERT OR UPDATE ON USU
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU IS NULL THEN
    SELECT  Usuario_Id_Usuario_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU),0) INTO v_newVal FROM USU;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Usuario_Id_Usuario_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."USUARIO_ID_USUARIO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOSERVENTIAESCALA_ID_US_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOSERVENTIAESCALA_ID_US_1" BEFORE INSERT OR UPDATE ON USU_SERV_ESC
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_ESC IS NULL THEN
    SELECT  UsuarioServentiaEscala_Id_Usua.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_ESC),0) INTO v_newVal FROM USU_SERV_ESC;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioServentiaEscala_Id_Usua.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_ESC := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USUARIOSERVENTIAESCALA_ID_US_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOSERVENTIAGRUPO_ID_USU_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOSERVENTIAGRUPO_ID_USU_1" BEFORE INSERT OR UPDATE ON USU_SERV_GRUPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_GRUPO IS NULL THEN
    SELECT  UsuarioServentiaGrupo_Id_Usuar.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_GRUPO),0) INTO v_newVal FROM USU_SERV_GRUPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioServentiaGrupo_Id_Usuar.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_GRUPO := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."USUARIOSERVENTIAGRUPO_ID_USU_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOSERVENTIA_ID_USUARIOS_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOSERVENTIA_ID_USUARIOS_1" BEFORE INSERT OR UPDATE ON USU_SERV
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV IS NULL THEN
    SELECT  UsuarioServentia_Id_UsuarioSer.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV),0) INTO v_newVal FROM USU_SERV;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioServentia_Id_UsuarioSer.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."USUARIOSERVENTIA_ID_USUARIOS_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOSERVENTIAOAB_ID_USUAR_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOSERVENTIAOAB_ID_USUAR_1" BEFORE INSERT OR UPDATE ON USU_SERV_OAB
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_OAB IS NULL THEN
    SELECT  UsuarioServentiaOab_Id_Usuario.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_OAB),0) INTO v_newVal FROM USU_SERV_OAB;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioServentiaOab_Id_Usuario.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_OAB := v_newVal;
  END IF;
END;

/
ALTER TRIGGER "PROJUDI"."USUARIOSERVENTIAOAB_ID_USUAR_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USUARIOSERVENTIAPERMISSAO_ID_1
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USUARIOSERVENTIAPERMISSAO_ID_1" BEFORE INSERT OR UPDATE ON USU_SERV_PERM
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_PERM IS NULL THEN
    SELECT  UsuarioServentiaPermissao_Id_U.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_PERM),0) INTO v_newVal FROM USU_SERV_PERM;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT UsuarioServentiaPermissao_Id_U.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_PERM := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USUARIOSERVENTIAPERMISSAO_ID_1" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USU_MOVI_TIPO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USU_MOVI_TIPO_TRG" BEFORE INSERT OR UPDATE ON USU_MOVI_TIPO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_MOVI_TIPO IS NULL THEN
    SELECT  USU_MOVI_TIPO_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_MOVI_TIPO),0) INTO v_newVal FROM USU_MOVI_TIPO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT USU_MOVI_TIPO_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_MOVI_TIPO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USU_MOVI_TIPO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USU_SERV_ESC_STATUS_HIST_ID
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USU_SERV_ESC_STATUS_HIST_ID" BEFORE INSERT OR UPDATE ON USU_SERV_ESC_STATUS_HIST
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_ESC_STATUS_HIST IS NULL THEN
    SELECT  USUARIOSERVENTIAESCALASTHIS_ID.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_ESC_STATUS_HIST),0) INTO v_newVal FROM USU_SERV_ESC_STATUS_HIST;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT USUARIOSERVENTIAESCALASTHIS_ID.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_ESC_STATUS_HIST := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USU_SERV_ESC_STATUS_HIST_ID" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USU_SERV_ESC_STATUS_ID_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."USU_SERV_ESC_STATUS_ID_TRG" BEFORE INSERT OR UPDATE ON USU_SERV_ESC_STATUS
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_USU_SERV_ESC_STATUS IS NULL THEN
    SELECT  USU_SERV_ESC_STATUS_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_USU_SERV_ESC_STATUS),0) INTO v_newVal FROM USU_SERV_ESC_STATUS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT USU_SERV_ESC_STATUS_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_USU_SERV_ESC_STATUS := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."USU_SERV_ESC_STATUS_ID_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ZONABAIRRO_ID_ZONABAIRRO_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ZONABAIRRO_ID_ZONABAIRRO_TRG" BEFORE INSERT OR UPDATE ON ZONA_BAIRRO
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_ZONA_BAIRRO IS NULL THEN
    SELECT  ZonaBairro_Id_ZonaBairro_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID_ZONA_BAIRRO),0) INTO v_newVal FROM ZONA_BAIRRO;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ZonaBairro_Id_ZonaBairro_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID_ZONA_BAIRRO := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ZONABAIRRO_ID_ZONABAIRRO_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger ZONA_ID_ZONA_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJUDI"."ZONA_ID_ZONA_TRG" BEFORE INSERT OR UPDATE ON Zona
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.Id_Zona IS NULL THEN
    SELECT  Zona_Id_Zona_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(Id_Zona),0) INTO v_newVal FROM Zona;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT Zona_Id_Zona_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.Id_Zona := v_newVal;
  END IF;
END;
/
ALTER TRIGGER "PROJUDI"."ZONA_ID_ZONA_TRG" ENABLE;