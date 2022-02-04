ALTER TABLE PROJUDI.RECURSO_PARTE ADD ID_PROC_TIPO NUMBER(10,0);

ALTER TABLE PROJUDI.RECURSO_PARTE ADD CONSTRAINT FK_RECURSOPARTE_PROCTIPO FOREIGN KEY (ID_PROC_TIPO) REFERENCES PROJUDI.PROC_TIPO (ID_PROC_TIPO);

UPDATE PROJUDI.RECURSO_PARTE RP SET ID_PROC_TIPO = (SELECT MAX(ID_PROC_TIPO) FROM PROJUDI.RECURSO R WHERE R.ID_RECURSO = RP.ID_RECURSO);

ALTER TABLE PROJUDI.RECURSO_PARTE MODIFY ID_PROC_TIPO NUMBER(10,0) NOT NULL;

ALTER TABLE PROJUDI.AUDI_PROC ADD ID_PROC_TIPO NUMBER(10,0);

ALTER TABLE PROJUDI.AUDI_PROC ADD CONSTRAINT FK_AUDIPROC_PROCTIPO FOREIGN KEY (ID_PROC_TIPO) REFERENCES PROJUDI.PROC_TIPO (ID_PROC_TIPO);

ALTER TABLE PROJUDI.AUDI_PROC ADD ID_PEND_VOTO NUMBER(24,0);

ALTER TABLE PROJUDI.AUDI_PROC ADD CONSTRAINT FK_AUDIPROC_PEND_VOTO FOREIGN KEY (ID_PEND_VOTO) REFERENCES PROJUDI.PEND (ID_PEND);

ALTER TABLE PROJUDI.AUDI_PROC ADD ID_PEND_EMENTA NUMBER(24,0);

ALTER TABLE PROJUDI.AUDI_PROC ADD CONSTRAINT FK_AUDIPROC_PEND_EMENTA FOREIGN KEY (ID_PEND_EMENTA) REFERENCES PROJUDI.PEND (ID_PEND);