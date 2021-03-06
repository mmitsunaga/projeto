ALTER TABLE PROJUDI.PROC_PARTE ADD ID_MOTIVO_BAIXA NUMBER(10) NULL;

ALTER TABLE PROJUDI.PROC_PARTE ADD DATA_SUSPENSAO DATE NULL;

ALTER TABLE PROJUDI.PROC_PARTE ADD CODIGO_ARTIGO_SUSPENSAO NUMBER(10,0) NULL;

CREATE INDEX PROCPARTE_MOTIVOBAIXA_IDX ON PROJUDI.PROC_PARTE (ID_MOTIVO_BAIXA);

ALTER TABLE PROJUDI.PROC_PARTE ADD CONSTRAINT PROCPARTE_MOTIVOBAIXA_FK FOREIGN KEY (ID_MOTIVO_BAIXA) REFERENCES PROJUDI.MOTIVO_BAIXA (ID_MOTIVO_BAIXA);
