ALTER TABLE PROJUDI.PROC_CRIMINAL ADD DATA_BAIXA DATE;

ALTER TABLE PROJUDI.PROC_CRIMINAL ADD ID_MOTIVO_BAIXA NUMBER(10);

CREATE INDEX PROCCRIMINAL_MOTIVOBAIXA_IDX ON PROJUDI.PROC_CRIMINAL (ID_MOTIVO_BAIXA);

ALTER TABLE PROJUDI.PROC_CRIMINAL ADD CONSTRAINT PROCCRIMINAL_MOTIVOBAIXA_FK FOREIGN KEY (ID_MOTIVO_BAIXA) REFERENCES PROJUDI.MOTIVO_BAIXA (ID_MOTIVO_BAIXA);

ALTER TABLE PROJUDI.PROC_CRIMINAL MODIFY ID_PROC_CRIMINAL NUMBER(24,0) NOT NULL;

ALTER TABLE PROJUDI.PROC_CRIMINAL MODIFY ID_PROC NUMBER(24,0) NOT NULL;

CREATE UNIQUE INDEX PROCCRIMINAL_IDPROCCRIMINAL_PK ON PROJUDI.PROC_CRIMINAL (ID_PROC_CRIMINAL);

ALTER TABLE PROJUDI.PROC_CRIMINAL ADD CONSTRAINT PK_PROC_CRIMINAL PRIMARY KEY(ID_PROC_CRIMINAL);

CREATE INDEX PROCCRIMINAL_PROC_IDX ON PROJUDI.PROC_CRIMINAL (ID_PROC);

ALTER TABLE PROJUDI.PROC_CRIMINAL ADD CONSTRAINT PROCCRIMINAL_PROC_FK FOREIGN KEY (ID_PROC) REFERENCES PROJUDI.PROC (ID_PROC);

CREATE SEQUENCE  PROJUDI.PROC_PRISAO_ID_SEQ  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 200000 NOCACHE  NOORDER  NOCYCLE;

CREATE OR REPLACE TRIGGER PROJUDI.PROC_CRIMINAL_ID_TRG BEFORE INSERT OR UPDATE ON PROC_CRIMINAL
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID_PROC_CRIMINAL IS NULL THEN
    SELECT PROC_PRISAO_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
   
   :new.ID_PROC_CRIMINAL := v_newVal;
  END IF;
END;

ALTER TRIGGER PROJUDI.PROC_CRIMINAL_ID_TRG ENABLE;
