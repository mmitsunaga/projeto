ALTER TABLE PROJUDI.AUDI_PROC ADD ID_PEND_VOTO_REDATOR NUMBER(24,0);

CREATE INDEX FK_AUDIPROC_PEND_VOTO_RED_I ON AUDI_PROC (ID_PEND_VOTO_REDATOR ASC); 

ALTER TABLE PROJUDI.AUDI_PROC ADD ID_PEND_EMENTA_REDATOR NUMBER(24,0);

CREATE INDEX FK_AUDIPROC_PEND_EMENTA_RED_I ON AUDI_PROC (ID_PEND_EMENTA_REDATOR ASC);