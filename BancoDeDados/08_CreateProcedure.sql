--------------------------------------------------------
--  DDL for Procedure PRC_DELETA_LOG
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJUDI"."PRC_DELETA_LOG" as 

BEGIN 

declare
vcont number := 0;

begin

  for registro in (
		      	select rowid,id_log,data
                        from projudi.log where data < '01/01/2013'  
                                              
                      ) 
loop
      
      delete  from  projudi.log where log.rowid=registro.rowid;
     
      vcont := vcont + 1;

   

     if vcont > 500 then      

        commit;

        vcont := 0;

     end if;

  end loop;

  commit;

end;

END ;

/
--------------------------------------------------------
--  DDL for Procedure PRC_INSERE_LOG
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJUDI"."PRC_INSERE_LOG" 
as 


BEGIN 

declare
vcont number := 0;

begin
  for registro in (
		      	select rowid,id_log,data
                        from projudi.log where data < '01/01/2013'  
                                             
                      ) 
loop
      
      insert into log_2012 select * from  log where log.rowid=registro.rowid;
     
      vcont := vcont + 1;

   

     if vcont > 500 then      

        commit;

        vcont := 0;

     end if;

  end loop;

  commit;

end;

END;

/
--------------------------------------------------------
--  DDL for Procedure PRC_INSERE_LOG_BULK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJUDI"."PRC_INSERE_LOG_BULK" 
IS
TYPE TObjectTable IS TABLE OF PROJUDI.LOG%ROWTYPE;
ObjectTable$ TObjectTable;

BEGIN
   SELECT * BULK COLLECT INTO ObjectTable$
     FROM PROJUDI.LOG WHERE DATA < '01/01/2013';

     FORALL x in ObjectTable$.First..ObjectTable$.Last
     INSERT INTO PROJUDI.LOG_2012 VALUES ObjectTable$(x) ;
END;

/
