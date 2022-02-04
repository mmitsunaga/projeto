package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.QuantidadeDias;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;


public class EstatisticaPs extends Persistencia{

    private static final long serialVersionUID = -4485166990537548348L;

    public EstatisticaPs(Connection conexao){
    	Conexao = conexao;
	}

	// TODO: Verificar IDs fixos e comandos específicos do banco
    public void gerarEstatisticaMes(int anoInicial, int mesInicial, int anoFinal, int mesFinal ) throws Exception {
        String Sql01;
        String Sql02;
        String Sql03;
        String Sql04;
        String Sql05;
        String Sql06;
        String Sql07;
        String Sql08;
        String Sql09;        
        String Sql11;
        String Sql12;
        String Sql13;
        String Sql14;
        String Sql15;
        String Sql16;
        String erros = "";
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
              
        Calendar priDiaPriHoraMesAtaul = Calendar.getInstance();
        Calendar priDiaPriHoraMesSeguinte = Calendar.getInstance();        
        //O mês no objeto Calendar inicia de 0, portando Jan = 0 e Dez = 11
        priDiaPriHoraMesAtaul.set(anoInicial, (mesInicial-1), 1,0,0,0);
        priDiaPriHoraMesSeguinte.set(anoFinal, (mesFinal - 1), 1,0,0,0);    
        
        try {
	      //Estatistica de produtividade
	        Sql01= " INSERT INTO EST_PROD ( ANO ,MES , ID_USU , ID_SERV , ID_EST_PROD_ITEM , ID_SISTEMA , QUANTIDADE)";
	        Sql01+=" SELECT TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'YYYY')) as ANO,  TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'MM')) as MES,  us.ID_USU, us.ID_SERV, m.ID_MOVI_TIPO as ID_EST_PROD_ITEM , 1 as ID_SISTEMA, COUNT(m.ID_MOVI_TIPO) AS QUANTIDADE";  
	        Sql01+=" FROM MOVI m";
	        Sql01+=" INNER JOIN MOVI_TIPO mt on m.ID_MOVI_TIPO=mt.ID_MOVI_TIPO";
	        Sql01+=" INNER JOIN USU_SERV us on m.ID_USU_REALIZADOR=us.ID_USU_SERV";
	        Sql01+=" WHERE m.DATA_REALIZACAO >= ? AND m.DATA_REALIZACAO<  ? "  ; 													 					ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql01+=" and mt.ID_CNJ_MOVI not in (12739,12743,12750,12751,12742,970,12744,12752,12740,12753,12749,12747,12624,12745,12741,12746,14096)"  ;
	        //teste para só gerar uma unica vez
	        Sql01+=" AND 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_USU = us.ID_USU AND ep.ID_SERV = us.ID_SERV AND ep.ID_EST_PROD_ITEM = m.ID_MOVI_TIPO AND ep.ID_SISTEMA = ?)" ;       
	        ps.adicionarLong(anoInicial); ps.adicionarLong(mesInicial);   ps.adicionarLong(1);
	        Sql01+=" GROUP BY TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'YYYY')), TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'MM')), m.ID_MOVI_TIPO, us.ID_SERV, us.ID_USU";
	       
	        executarUpdateDelete(Sql01, ps);           
	        ps.limpar();
        }catch (Exception e) {
			erros+="\n Estatistica de produtividade não executada";
		}
        
      /*  //Estatistica de produtividade processos distribuidos
        Sql02= "  INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";
        Sql02+= "   select tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema, sum(tab.QUANTIDADE) as QUANTIDADE "; 
        Sql02+= "   FROM ( ";                           
        Sql02+= "       SELECT TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')) as MES, p.ID_SERV, 50000 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA, COUNT(1) AS QUANTIDADE ";  
        Sql02+= "       FROM PROC p ";
        Sql02+= "       WHERE p.DATA_RECEBIMENTO>= ? and  p.DATA_RECEBIMENTO< ?  ";  																ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
        Sql02+= "       AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 																		ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
        Sql02+= "       GROUP BY TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')), p.id_serv ";

        Sql02+= "       union ";

        Sql02+= "       SELECT TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')) as ANO,  TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')) as MES, gabinete.id_serv, 50000 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA, COUNT(DISTINCT P.ID_PROC) AS QUANTIDADE "; 
        Sql02+= "       FROM PROC p ";
        Sql02+= "       inner join projudi.serv_relacionada sr on p.id_serv = sr.id_serv_princ ";
        Sql02+= "       INNER JOIN projudi.serv_cargo sc ON sr.id_serv_rel = sc.id_serv ";
        Sql02+= "       INNER JOIN projudi.cargo_tipo ct ON sc.id_cargo_tipo = ct.id_cargo_tipo AND ct.cargo_tipo_codigo = ? "; 						ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
        Sql02+= "       inner join projudi.proc_resp pr on pr.id_proc = p.id_proc AND pr.id_serv_cargo = sc.id_serv_cargo AND pr.CODIGO_TEMP = 0";
        Sql02+= "       inner join projudi.serv gabinete on sr.id_serv_rel = gabinete.id_serv ";
        Sql02+= "       WHERE p.DATA_RECEBIMENTO>= ? and  p.DATA_RECEBIMENTO< ? ";  																	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
        Sql02+= "       AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 																			ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
        Sql02+= "       GROUP BY TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')), gabinete.id_serv ";

        Sql02+= "       union ";

        Sql02+= "       SELECT TO_NUMBER(TO_CHAR(r.DATA_RECEBIMENTO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(r.DATA_RECEBIMENTO,'MM')) as MES, r.id_serv_recurso as id_serv,  50000 as ID_EST_PROD_ITEM,  1 as ID_SISTEMA,  COUNT(1) AS QUANTIDADE "; 
        Sql02+= "       FROM recurso r ";
        Sql02+= "       WHERE r.DATA_RECEBIMENTO>= ? and  r.DATA_RECEBIMENTO< ? ";  																	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());        
        Sql02+= "       GROUP BY TO_NUMBER(TO_CHAR(r.DATA_RECEBIMENTO,'YYYY')), TO_NUMBER(TO_CHAR(r.DATA_RECEBIMENTO,'MM')), r.id_serv_recurso ";
        Sql02+= "   ) tab ";          
        Sql02+= " GROUP BY tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema ";
        Sql02+= " having 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = tab.ano AND ep.MES = tab.mes AND ep.ID_SERV = tab.ID_SERV AND ep.ID_EST_PROD_ITEM = tab.ID_EST_PROD_ITEM  AND ep.ID_SISTEMA = tab.ID_SISTEMA )";                                
       
        executarUpdateDelete(Sql02, ps);
        ps.limpar();*/
        
        try {
                      
	        //Estatistica baseadas nos logs de ponteiros - distribuidos
	        Sql02= " INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";
	        Sql02+=" SELECT TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(pl.DATA,'MM')) as MES, pl.ID_SERV, 50000 as id_est_prod_item , 1 as id_sistema, SUM(QTD) AS QUANTIDADE ";          
	        Sql02+=" FROM PONTEIRO_LOG pl inner join PROC p on p.id_proc = pl.id_proc ";
	        Sql02+=" where ID_PONTEIRO_LOG_TIPO = 1 ";
	        Sql02+=" AND pl.DATA >= ? AND pl.DATA < ? ";  																	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime()); ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql02+=" AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 												ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql02+=" and not exists (select 1 from est_prod ep    where ep.ano = TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')) and ep.mes = TO_NUMBER(TO_CHAR(pl.DATA,'MM')) and ep.id_serv = pl.ID_SERV and ep.id_est_prod_item = 50000 and ep.id_sistema = 1)" ; 	
	        Sql02+=" GROUP BY TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')), TO_NUMBER(TO_CHAR(pl.DATA,'MM')),pl.ID_SERV ";
	               
	        executarUpdateDelete(Sql02, ps);           
	        ps.limpar(); 
	    }catch (Exception e) {
			erros+="\n Estatistica distribuidos(item 50.000) não executada";
		}
        
        try {
	        //Estatistica de produtividade --processos Arquivados
	        Sql03= "INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";                     
	        Sql03+= "   SELECT tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema, sum(tab.QUANTIDADE) as QUANTIDADE "; 
	        Sql03+= "   FROM ( ";
	        Sql03+= "        SELECT TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'MM')) as MES, p.ID_SERV,  50001 as ID_EST_PROD_ITEM, 1  as ID_SISTEMA,  COUNT(1) AS QUANTIDADE "; 
	        Sql03+= "        FROM PROC p ";        
	        Sql03+= "       WHERE p.DATA_ARQUIVAMENTO>= ? and  p.DATA_ARQUIVAMENTO< ? "; 																	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());  	ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql03+= "       AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 																			ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql03+= "        GROUP BY TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'MM')), p.ID_SERV ";
	        
	        Sql03+= "        union ";
	        
	        Sql03+= "        SELECT TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'MM')) as MES, gabinete.id_serv, 50001 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA, COUNT(DISTINCT P.ID_PROC) AS QUANTIDADE "; 
	        Sql03+= "        FROM PROC p ";
	        Sql03+= "        inner join projudi.serv_relacionada sr on p.id_serv = sr.id_serv_princ ";
	        Sql03+= "        INNER JOIN projudi.serv_cargo sc ON sr.id_serv_rel = sc.id_serv ";
	        Sql03+= "        INNER JOIN projudi.cargo_tipo ct ON sc.id_cargo_tipo = ct.id_cargo_tipo AND ct.cargo_tipo_codigo = ? "; 							ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
	        Sql03+= "        inner join projudi.proc_resp pr on pr.id_proc = p.id_proc AND pr.id_serv_cargo = sc.id_serv_cargo AND pr.CODIGO_TEMP = 0 ";
	        Sql03+= "        inner join projudi.serv gabinete on sr.id_serv_rel = gabinete.id_serv ";
	        Sql03+= "       WHERE p.DATA_ARQUIVAMENTO>= ? and  p.DATA_ARQUIVAMENTO< ? "; 																		ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql03+= "       AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 																				ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql03+= "        GROUP BY TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_ARQUIVAMENTO,'MM')), gabinete.id_serv ";
	
	        Sql03+= "        union  ";
	
	        Sql03+= "        SELECT TO_NUMBER(TO_CHAR(r.data_retorno,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(r.data_retorno,'MM')) as MES, r.id_serv_recurso as id_serv,  50001 as ID_EST_PROD_ITEM, 1  as ID_SISTEMA,  COUNT(1) AS QUANTIDADE ";
	        Sql03+= "        FROM recurso r ";        
	        Sql03+= "       WHERE  r.data_retorno is not null AND r.data_retorno>= ? and  r.data_retorno< ? "; 																				ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());  	ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql03+= "        GROUP BY TO_NUMBER(TO_CHAR(r.data_retorno,'YYYY')), TO_NUMBER(TO_CHAR(r.data_retorno,'MM')), r.id_serv_recurso ";                
	        Sql03+= "    ) tab";
	        Sql03+= " GROUP BY tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema";
	        Sql03+= " having 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = tab.ano AND ep.MES = tab.mes AND ep.ID_SERV = tab.ID_SERV AND ep.ID_EST_PROD_ITEM = tab.ID_EST_PROD_ITEM  AND ep.ID_SISTEMA = tab.ID_SISTEMA )";
	          
	        executarUpdateDelete(Sql03, ps);
	        ps.limpar();
        
	    }catch (Exception e) {
			erros+="\n Estatistica Arquivados(item 50.001) não executada";
		}
           
        try {
	        //Estatistica de produtividade -- Acervo Processos Ativos
	        Sql04= "  INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE) ";       
	        Sql04+= "  SELECT tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema, sum(tab.QUANTIDADE) as QUANTIDADE ";
	        Sql04+= "  FROM ( ";
	        Sql04+= "        SELECT "+anoInicial+" as ANO, "+mesInicial+" as MES, p.ID_SERV, 50002 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA, COUNT(*) AS QUANTIDADE "; 
	        Sql04+= "        FROM PROC p ";
	        Sql04+= "        WHERE (p.DATA_ARQUIVAMENTO IS NULL OR p.DATA_ARQUIVAMENTO >= ?) AND p.DATA_RECEBIMENTO < ? ";    	ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime()); ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());        
	        Sql04+= "        AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 											ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql04+= "        GROUP BY p.ID_SERV";
	        Sql04+= "        union ";
	        Sql04+= "        SELECT "+anoInicial+" as ANO, "+mesInicial+" as MES, gabinete.id_serv, 50002 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA, COUNT(DISTINCT P.ID_PROC) AS QUANTIDADE "; 
	        Sql04+= "        FROM PROC p ";
	        Sql04+= "        inner join projudi.serv_relacionada sr on p.id_serv = sr.id_serv_princ ";
	        Sql04+= "        INNER JOIN projudi.serv_cargo sc ON sr.id_serv_rel = sc.id_serv ";
	        Sql04+= "        INNER JOIN projudi.cargo_tipo ct ON sc.id_cargo_tipo = ct.id_cargo_tipo AND ct.cargo_tipo_codigo = ? "; ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
	        Sql04+= "        inner join projudi.proc_resp pr on pr.id_proc = p.id_proc AND pr.id_serv_cargo = sc.id_serv_cargo AND pr.CODIGO_TEMP = 0 ";
	        Sql04+= "        inner join projudi.serv gabinete on sr.id_serv_rel = gabinete.id_serv ";
	        Sql04+= "        WHERE (p.DATA_ARQUIVAMENTO IS NULL OR p.DATA_ARQUIVAMENTO >= ?) AND  p.DATA_RECEBIMENTO < ? ";    	ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime()); ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());        
	        Sql04+= "        AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 											ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql04+= "        GROUP BY gabinete.id_serv ";
	        Sql04+= "    ) tab";
	        Sql04+= " GROUP BY tab.ano, tab.mes, tab.id_serv, tab.id_est_prod_item, tab.id_sistema";
	        Sql04+= " having 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = tab.ano AND ep.MES = tab.mes AND ep.ID_SERV = tab.ID_SERV AND ep.ID_EST_PROD_ITEM = tab.ID_EST_PROD_ITEM  AND ep.ID_SISTEMA = tab.ID_SISTEMA )";
	
	        executarUpdateDelete(Sql04, ps);
	        ps.limpar();
        
	    }catch (Exception e) {
			erros+="\n Estatistica produtividade(item 50.002) não executada";
		}
        
        try {
	        //Estatistica de processo por Tipo (natureza)       
	        Sql05 = "INSERT INTO EST_PROC (ANO , MES , ID_SERV , ID_PROC_TIPO , ID_SISTEMA , QUANTIDADE)";
	        Sql05 +=" SELECT TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')) as MES,  p.ID_SERV,  p.ID_PROC_TIPO,  1 as ID_SISTEMA, COUNT(p.ID_PROC_TIPO) AS QUANTIDADE";        
	        Sql05 +=" FROM PROC p ";        
	        Sql05+= "       WHERE p.DATA_RECEBIMENTO>= ? and  p.DATA_RECEBIMENTO< ? ";  										ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());  		 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        
	        //teste para só gerar uma unica vez
	        Sql05 +=" AND 1 not IN (SELECT 1 FROM EST_PROC ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_SERV = p.ID_SERV AND ep.ID_PROC_TIPO = p.ID_PROC_TIPO AND ep.ID_SISTEMA = ?)" ;
	        ps.adicionarLong(anoInicial);
	        ps.adicionarLong(mesInicial);
	        ps.adicionarLong(1);
	        Sql05 +=" GROUP BY TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO,'MM')), p.ID_PROC_TIPO, p.ID_SERV";
	        executarUpdateDelete(Sql05, ps);
	        ps.limpar();
        
	    }catch (Exception e) {
			erros+="\n Estatistica processo por Tipo não executada";
		}
        
        try {
	        Sql06 = "INSERT INTO EST_PEND (ANO ,MES ,ID_USU ,ID_SERV ,ID_PEND_TIPO ,ID_SISTEMA , QUANTIDADE)";
	        Sql06 += " SELECT TO_NUMBER(TO_CHAR(p.DATA_FIM,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(p.DATA_FIM,'MM')) as MES, us.ID_USU, us.ID_SERV, p.ID_PEND_TIPO, 1 as ID_SISTEMA, COUNT(p.ID_PEND_TIPO) AS QUANTIDADE";        
	        Sql06 += " FROM PEND_FINAL p ";
	        Sql06 += " INNER JOIN PEND_TIPO pt on p.ID_PEND_TIPO=pt.ID_PEND_TIPO";
	        Sql06 += " INNER JOIN USU_SERV us on p.ID_USU_FINALIZADOR=us.ID_USU_SERV";        
	        Sql06+= "       WHERE p.DATA_FIM>= ? and p.DATA_FIM< ? ";  														ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());  		 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        //teste para só gerar uma unica vez
	        Sql06 += " AND 1 not IN (SELECT 1 FROM EST_PEND ep WHERE ep.ANO=? AND ep.MES = ? AND ep.ID_USU=us.ID_USU AND ep.ID_SERV = us.ID_SERV AND ep.ID_PEND_TIPO = p.ID_PEND_TIPO AND ep.ID_SISTEMA = ?)" ;        
	        ps.adicionarLong(anoInicial);
	        ps.adicionarLong(mesInicial);
	        ps.adicionarLong(1);
	        Sql06 += " GROUP BY TO_NUMBER(TO_CHAR(p.DATA_FIM,'YYYY')), TO_NUMBER(TO_CHAR(p.DATA_FIM,'MM')), p.ID_PEND_TIPO, us.ID_SERV, us.ID_USU";          
	        executarUpdateDelete(Sql06, ps);
	        ps.limpar();
        
	    }catch (Exception e) {
			erros+="\n Estatistica de Pendência não executada";
		}
        
        try {
	        //Atualizando tabela EST_PROD_ITEM para gerar corretamente os relatórios sumários de produtividade
	        Sql07 = "INSERT INTO est_prod_item (id_est_prod_item,est_prod_item) ";
	        Sql07 +="   SELECT mt.id_movi_tipo AS id_est_prod_item, mt.movi_tipo AS est_prod_item FROM movi_tipo mt ";
	        Sql07 +="       INNER JOIN (SELECT DISTINCT (ep.id_est_prod_item) ";
	        Sql07 +="                       FROM est_prod ep ";
	        Sql07 +="                       WHERE ep.id_est_prod_item NOT IN (SELECT epi.id_est_prod_item ";
	        Sql07 +="                                                           FROM est_prod_item epi ";
	        Sql07 +="                                                           WHERE epi.id_est_prod_item = ep.id_est_prod_item) ) t ON mt.id_movi_tipo = t.id_est_prod_item";
	        executarUpdateDeleteSemParametros(Sql07);
	        
	    }catch (Exception e) {
			erros+="\n atualização do EST_PROD_ITEM não executada";
		}
        
        try {
	        Sql08= "INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";                     
	        Sql08+= "   SELECT TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'MM')) as MES, r.ID_SERV_ORIGEM as ID_SERV, 50003 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA,  COUNT(r.ID_RECURSO) AS QUANTIDADE ";  
	        Sql08+= "       FROM RECURSO r ";        
	        Sql08+= "       WHERE r.DATA_ENVIO>= ? and  r.DATA_ENVIO< ? ";  													ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql08+= "       AND  not exists (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'YYYY')) AND ep.MES = TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'MM')) AND ep.ID_EST_PROD_ITEM = 50003 AND ep.ID_SISTEMA = 1 and ep.id_serv =r.ID_SERV_ORIGEM ) ";  
	        Sql08+= "   GROUP BY TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'YYYY')), TO_NUMBER(TO_CHAR(r.DATA_ENVIO,'MM')), r.ID_SERV_ORIGEM ";
	        executarUpdateDelete(Sql08, ps);
	        ps.limpar();
        }catch (Exception e) {
			erros+="\n Estatistica 50003 não executada";
		}
        
        try {
	        Sql09= "INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";                     
	        Sql09+= "   SELECT TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'MM')) as MES, r.ID_SERV_ORIGEM as ID_SERV, 50004 as ID_EST_PROD_ITEM, 1 as ID_SISTEMA,  COUNT(r.ID_RECURSO) AS QUANTIDADE "; 
	        Sql09+= "       FROM RECURSO r ";        
	        Sql09+= "       WHERE r.DATA_RETORNO>= ? and  r.DATA_RETORNO< ? ";  											ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql09+= "       AND not exists (SELECT 1 FROM EST_PROD ep WHERE ep.ANO =  TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'YYYY')) AND ep.MES = TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'MM')) AND ep.ID_EST_PROD_ITEM = 50004 AND ep.ID_SISTEMA = 1 and ep.id_serv = r.ID_SERV_ORIGEM) ";  
	        Sql09+= "   GROUP BY TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'YYYY')), TO_NUMBER(TO_CHAR(r.DATA_RETORNO,'MM')), r.ID_SERV_ORIGEM ";
	        executarUpdateDelete(Sql09, ps);
	        ps.limpar();
        }catch (Exception e) {
			erros+="\n Estatistica 50004 não executada";
		}
        
                    
        try {
	        //Estatistica baseadas nos logs de ponteiros - redistribuidos
	        Sql11= " INSERT INTO EST_PROD (ANO, MES, ID_SERV, ID_EST_PROD_ITEM, ID_SISTEMA, QUANTIDADE)";
	        Sql11+=" SELECT TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')) as ANO, TO_NUMBER(TO_CHAR(pl.DATA,'MM')) as MES, pl.ID_SERV, 50006 as id_est_prod_item , 1 as id_sistema, SUM(QTD) AS QUANTIDADE ";          
	        Sql11+=" FROM PONTEIRO_LOG pl inner join PROC p on p.id_proc = pl.id_proc ";
	        Sql11+=" where ID_PONTEIRO_LOG_TIPO = 2 ";
	        Sql11+=" AND pl.DATA >= ? AND pl.DATA < ? ";  																	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime()); ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
	        Sql11+=" AND (p.ID_PROC_STATUS <> ? AND p.ID_PROC_TIPO <> ?)" ; 												ps.adicionarLong(ProcessoStatusDt.CALCULO);  ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_DE_LIQUIDACAO_DE_PENA);
	        Sql11+=" and not exists (select 1 from est_prod ep    where ep.ano = TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')) and ep.mes = TO_NUMBER(TO_CHAR(pl.DATA,'MM')) and ep.id_serv = pl.ID_SERV and ep.id_est_prod_item = 50006 and ep.id_sistema = 1)" ; 	
	        Sql11+=" GROUP BY TO_NUMBER(TO_CHAR(pl.DATA,'YYYY')), TO_NUMBER(TO_CHAR(pl.DATA,'MM')),pl.ID_SERV ";
	
	        executarUpdateDelete(Sql11, ps);           
	        ps.limpar(); 
        }catch (Exception e) {
			erros+="\n Estatistica 50006 não executada";
		}
        
        try {
    	      //Estatistica de produtividade de audiências MARCADAS
        	Sql14= " INSERT INTO EST_PROD ( ANO ,MES , ID_USU , ID_SERV , ID_EST_PROD_ITEM , ID_SISTEMA , QUANTIDADE)";
        	Sql14+=" select TO_NUMBER(TO_CHAR(data_agendada,'YYYY')) as ANO,  TO_NUMBER(TO_CHAR(data_agendada,'MM')) as MES,  us.ID_USU, us.ID_SERV, 50007 as ID_EST_PROD_ITEM , 1 as ID_SISTEMA, COUNT(1) AS QUANTIDADE";
        	Sql14+=" from audi a inner join audi_proc ap on a.id_audi = ap.id_audi";
        	Sql14+=" inner join serv_cargo sc on ap.id_serv_cargo = sc.id_serv_cargo";
        	Sql14+=" inner join usu_serv_grupo ug on sc.id_usu_serv_grupo = ug.id_usu_serv_grupo";
        	Sql14+=" inner join grupo g on g.id_grupo = ug.id_grupo";
        	Sql14+=" inner join usu_serv us on us.id_usu_serv = ug.id_usu_serv";
        	Sql14+=" where data_agendada  >= ?) AND data_agendada <  ?)";  
        		ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
        	//teste para só gerar uma unica vez
  	        Sql14+=" AND 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_USU = us.ID_USU AND ep.ID_SERV = us.ID_SERV AND ep.ID_EST_PROD_ITEM = 50007 AND ep.ID_SISTEMA = ?)" ;       
  	        ps.adicionarLong(anoInicial); ps.adicionarLong(mesInicial);   ps.adicionarLong(1);
  	        Sql14+=" GROUP BY TO_NUMBER(TO_CHAR(data_agendada,'YYYY')), TO_NUMBER(TO_CHAR(data_agendada,'MM')), 50007 , us.ID_SERV, us.ID_USU, g.grupo";
  	        
  	        executarUpdateDelete(Sql14, ps);           
	        ps.limpar();
	        
        }catch (Exception e) {
			erros+="\n Estatistica de produtividade de audiencias marcadas não executada";
		}
        
             
        try {
    	      //Estatistica de produtividade de audiências realizadas
        	Sql15= " INSERT INTO EST_PROD ( ANO ,MES , ID_USU , ID_SERV , ID_EST_PROD_ITEM , ID_SISTEMA , QUANTIDADE)";
        	Sql15+= " select TO_NUMBER(TO_CHAR(DATA_movi,'YYYY')) as ANO,  TO_NUMBER(TO_CHAR(DATA_movi,'MM')) as MES,  us.ID_USU, us.ID_SERV, 50008 as ID_EST_PROD_ITEM , 1 as ID_SISTEMA, COUNT(1) AS QUANTIDADE";  
        	Sql15+=" from audi_proc ap inner join audi_proc_resp apr on apr.id_audi_proc = ap.id_audi_proc";
        	Sql15+=" inner join usu_serv us on us.id_usu_serv = apr.id_usu_serv";
        	Sql15+=" where data_movi  >= ? AND data_movi < ? ";
        	ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
        	Sql15+=" and id_audi_proc_status in (15,16,17,18,19,44,46,47)";
        	//teste para só gerar uma unica vez
	        Sql15+=" AND 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_USU = us.ID_USU AND ep.ID_SERV = us.ID_SERV AND ep.ID_EST_PROD_ITEM = 50008 AND ep.ID_SISTEMA = ?)" ;       
	        ps.adicionarLong(anoInicial); ps.adicionarLong(mesInicial);   ps.adicionarLong(1);
        	Sql15+=" GROUP BY TO_NUMBER(TO_CHAR(data_movi,'YYYY')), TO_NUMBER(TO_CHAR(data_movi,'MM')), 50008 , us.ID_SERV, us.ID_USU";
        	          
    	    executarUpdateDelete(Sql15, ps);           
    	    ps.limpar();
    	        
        	}catch (Exception e) {
    			erros+="\n Estatistica de produtividade de audiencias realizadas não executada";
    	}
          
        
        try {
  	      //Estatistica de produtividade de audiências
          // para tornar o SQL mais genérico, os itens de estatísticas foram gerados com o codigo do complemento concatenados com o id_movi_tipo. Ex: item 1300940 - 13-Realizada e 940- Audiencia de conciliação
  	        Sql16= " INSERT INTO EST_PROD ( ANO ,MES , ID_USU , ID_SERV , ID_EST_PROD_ITEM , ID_SISTEMA , QUANTIDADE)";
  	        Sql16+=" SELECT TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'YYYY')) as ANO,  TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'MM')) as MES,  us.ID_USU, us.ID_SERV,  to_number(mc.id_complemento_tabelado || to_char(m.ID_MOVI_TIPO,'FM00000')) as ID_EST_PROD_ITEM , 1 as ID_SISTEMA, COUNT(m.ID_MOVI_TIPO) AS QUANTIDADE";  
  	        Sql16+=" FROM MOVI m inner join movi_complemento mc on m.id_movi = mc.id_movi";
  	        Sql16+=" INNER JOIN MOVI_TIPO mt on m.ID_MOVI_TIPO=mt.ID_MOVI_TIPO";
  	        Sql16+=" INNER JOIN USU_SERV us on m.ID_USU_REALIZADOR=us.ID_USU_SERV";
  	        Sql16+=" WHERE mc.id_complemento = 15 "  ;
  	        Sql16+=" WHERE mt.ID_CNJ_MOVI in (12739,12743,12750,12751,12742,970,12744,12752,12740,12753,12749,12747,12624,12745,12741,12746,14096)"  ; 
  	        Sql16+=" and m.DATA_REALIZACAO >= ? AND m.DATA_REALIZACAO<  ? "  ; 													 					ps.adicionarDateTime(priDiaPriHoraMesAtaul.getTime());	 ps.adicionarDateTime(priDiaPriHoraMesSeguinte.getTime());
  	        //teste para só gerar uma unica vez
  	        Sql16+=" AND 1 not IN (SELECT 1 FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_USU = us.ID_USU AND ep.ID_SERV = us.ID_SERV AND ep.ID_EST_PROD_ITEM = to_number(mc.id_complemento_tabelado || to_char(m.ID_MOVI_TIPO,'FM00000'))  AND ep.ID_SISTEMA = ?)" ;       
  	        ps.adicionarLong(anoInicial); ps.adicionarLong(mesInicial);   ps.adicionarLong(1);
  	        Sql16+=" GROUP BY TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'YYYY')), TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO,'MM')), to_number(mc.id_complemento_tabelado || to_char(m.ID_MOVI_TIPO,'FM00000')) , us.ID_SERV, us.ID_USU";
  	       
  	        executarUpdateDelete(Sql16, ps);           
  	        ps.limpar();
          }catch (Exception e) {
  			erros+="\n Estatistica de produtividade de audiencias movimentadas não executada";
  		}
        
        
          
          if (!erros.isEmpty()) {
          	throw new Exception(erros);
          }
       
    }
    
    /**
     * Método que gera as estatísticas solicitadas na Resolução 76/2009 do CNJ.
     * @param ano - ano referência
     * @param mes - mês referência
     * @throws Exception
     * @author hmgodinho
     */
    public void gerarEstatisticaResolucao76(Date dataInicial, Date dataFinal, String ano, String mes) throws Exception {
    	String sql;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
      //CnCCrim1º - Casos Novos de Conhecimento no 1º Grau Criminais
    	sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51001); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		" 	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51001); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnCNCrim1º - Casos Novos de Conhecimento no  1º Grau Não-Criminais
    	sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51002); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 1" +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,218,228,436,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51002); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnExtFisc1° - Casos Novos de Execução Fiscal no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51003); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (1116) " +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51003); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnExtNFisc1° - Casos Novos de Execução de Título Extrajudicial no 1º Grau, Exceto Execuções Fiscais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51004); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (159,1117) " +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51004); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnElet1° - Casos Novos Eletrônicos no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51005); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (1727,7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,159,166,167,170,171,172,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,436,1116,1117,1118,1122,1124,1199,1268,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1710,1726,10933,10943,10944,10967,10979,11026,11397) " +
        		" 	AND s.id_serv_subtipo IN (1,2,3,7,8,9,12,13,25,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51005); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();

        //CpCCrim1° - Casos Pendentes de Conhecimento no 1º Grau Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51006); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.ID_AREA = 2" +
        		" 	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51006); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCNCrim1º - Casos Pendentes de Conhecimento no 1º Grau Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51007); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.ID_AREA = 1" +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,436,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51007); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpExtFisc1º - Casos Pendentes de Execução Fiscal no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51008); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.ID_AREA = 1" +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND pt.id_cnj_classe IN (1116) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51008); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpExtNFisc1º - Casos Pendentes de Execução de Título Extrajudicial, Exceto Execuções Fiscais  
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51009); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.ID_AREA = 1" +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND pt.id_cnj_classe IN (159,1117) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51009); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DeRext1º - Decisões no 1º Grau Passíveis de Recurso Externo
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51010); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,46,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,74,76,77,79,80,81,83,84,85,86,87,89,92,93,94,96,97,98,99,100,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,156,157,159,166,167,170,171,172,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,228,229,230,231,232,234,236,239,240,241,251,258,261,218,282,283,287,288,289,293,294,295,297,300,302,307,309,310,311,326,329,330,407,408,409,410,411,1112,1114,1116,1117,1118,1122,1124,1199,1230,1231,1232,1262,1268,1283,1284,1288,1289,1294,1295,1298,1301,1308,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1432,1434,1435,1438,1440,1451,1455,1461,1462,1463,1464,1465,1474,1478,1682,1683,1690,1691,1701,1702,1703,1704,1705,1706,1707,1709,1710,1726,1727,10933,10943,10944,10960,10967,10972,10973,10974,10975,10976,10977,10979,10980,10981,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (128,146,172,175,190,196,202,208,219,220,221,235,263,264,268,272,275,276,332,334,335,339,347,348,349,352,353,354,355,357,358,371,373,374,381,383,388,389,391,393,394,399,400,402,404,429,432,442,443,444,446,447,448,450,451,452,454,455,457,458,459,460,461,462,463,464,465,466,471,785,787,788,792,804,818,819,821,823,889,892,898,900,940,941,944,945,961,988,1002,1003,1004,1009,1010,1011,1014,1016,1017,1018,1019,1042,1043,1044,1045,1046,1047,1048,1049,1050,1059,1063,10953,10961,10962,10963,10964,11382,11393,11395,11396,11401,11402,11404,11405,11406,11407,11408,11409) " +	
        		"	AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51010); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudCrimNPL1º - Execuções de Penas Não-Privativas de Liberdade
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51011); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc "+
    			"INNER JOIN assunto a ON a.id_assunto = pa.id_assunto" ;
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51011); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudCrimPL1º - Execuções de Penas Privativas de Liberdade
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51012); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc "+
    			"INNER JOIN assunto a ON a.id_assunto = pa.id_assunto " +
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7791) " +
        		" 	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51012); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudNCrim1° - Execuções Judiciais de 1º Grau, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51013); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) " +
        		" 	AND s.id_serv_subtipo IN (1,2,3,7,8,9,12,13,25,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51013); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
            
        //ExeJudPCrimNPL1º - Execuções Pendentes de Penas Não-Privativas de Liberdade
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51014); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc "+
    			"INNER JOIN assunto a ON a.id_assunto = pa.id_assunto " +
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +
        		" 	AND s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51014); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudPCrimPL1º - Execuções Pendentes de Penas Privativas de Liberdade
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51015); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc "+
    			"INNER JOIN assunto a ON a.id_assunto = pa.id_assunto " +
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7791) " +
        		" 	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51015); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudPNCrim1° - Execuções Judiciais Pendentes no 1º Grau, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51016); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) " +
        		" 	AND s.id_serv_subtipo IN (1,2,3,7,8,9,12,13,25,26) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51016); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeSuSFisc1º - Execuções Fiscais Sobrestadas ou Suspensas ou em Arquivo Provisório
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51017); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			" INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.id_proc IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (245,276,11012,11013,11014,11015,11016,11017,11018) " +
        		"								AND m1.data_realizacao BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51017); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeSuSNFisc1º - Execuções Sobrestadas ou Suspensas ou em Arquivo Provisório, Exceto Execuções Fiscais e Penais   
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51018); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (156,157,159,1112,1114,1117,1432,1434,1435,10980) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.id_proc IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (245,276,11012,11013,11014,11015,11016,11017,11018) " +
        		"								AND m1.data_realizacao BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51018); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //IncExFisc1º - Incidentes de Execução Fiscal no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51019); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (170,171) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51019); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //IncExNFisc1º - Incidentes de Execução no 1º Grau, Exceto em Execuções Fiscais e Penais.
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51020); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (170,171,172,173,229,10981) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51020); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //IncExPFisc1º - Incidentes de Execução Fiscal Pendentes no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51021); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (1118) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51021); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //IncExPNFisc1º - Incidentes de Execução Pendentes no 1º Grau, Exceto em Execuções Fiscais e Penais.
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51022); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (170,171,172,173,229,10981) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51022); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntC1º - Recursos Internos no 1º Grau na Fase de Conhecimento
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51023); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (420,1689) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51023); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntCP1º - Recursos Internos Pendentes no 1º Grau na Fase de Conhecimento
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51024); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo "+
    			"INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE pt.id_cnj_classe IN (420,1689) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ? )"; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51024); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentCCrim1º - Sentenças de Conhecimento no 1º Grau Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51025); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2 " +
        		"	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (282,283,287,288,289,293,294,295,297,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		"	AND mt.id_cnj_movi IN (219,220,221,442,443,442,447,442,451,459,460,461,464,1042,1043,1044,1045,1046,1047,1048,1049,10953,10961) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51025); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentCNCrim1º - Sentenças de Conhecimento no 1º Grau Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51026); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 1 " +
        		"	AND s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (202,208,219,220,221,235,442,443,444,446,447,448,450,451,452,454,455,457,458,459,460,461,462,463,464,465,466,471,11401,11402,11404,11405,11406,11407,11408,11409) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51026); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentExtFisc1º - Sentenças em Execução Fiscal no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51027); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (1116) " +
        		"	AND mt.id_cnj_movi IN (196,454,455,457,458,459,460,461,462,463,464,465,466,471,900) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51027); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentExtNFisc1º - Sentença em  Execução de Título Extrajudicial, Exceto Execuções Fiscais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51028); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (159,1117) " +
        		"	AND mt.id_cnj_movi IN (196,454,455,457,458,459,460,461,462,463,464,465,466,471) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51028); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentJudCrimNPL1º - Sentenças em Execução de Penas Não-Privativas de Liberdade no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51029); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +
        		"	AND mt.id_cnj_movi IN (196,1050) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51029); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentJudCrimPL1º - Sentenças em Execução de Penas Privativas de Liberdade no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51030); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7791) " +
        		"	AND mt.id_cnj_movi IN (196,1050) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51030); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentJudNCrim1º - Sentenças em Execução Judicial no 1º Grau, Exceto Sentenças em Execução Penal
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51031); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,1465,10980) " +
        		"	AND mt.id_cnj_movi IN (196,455,457,458,460,462,463,464,465,466,471,871,10964) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51031); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SeRInt1º - Sentenças no 1º Grau Passíveis de Recurso Interno
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51032); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (7,8,9,12,13) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,282,283,287,288,289,293,294,295,297,300,302,307,309,310,311,326,329,330,1118,1122,1124,1199,1268,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1710,1726,10933,10943,10944,10967,10979,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (202,208,219,220,221,235,442,443,444,446,447,448,450,451,452,454,471,11401,11402,11404,11405,11406,11407,11408,11409) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51032); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixCCrim1º - Processos de Conhecimento Baixados no 1º Grau Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51033); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2 " +
        		"	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51033); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixCNCrim1º - Total de Processos de Conhecimento no 1º Grau Baixados Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51034); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 1 " +
        		"	AND s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,436,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51034); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixExtFisc1º - Total de Processos Baixados de Execução Fiscal no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51035); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND pt.id_cnj_classe IN (1116) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51035); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
       	//TBaixExtNFisc1º - Total de Processos Baixados de Execução de Títulos Extrajudiciais, Exceto Execuções Fiscais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51036); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,7,8,12,13,25,26) " +
        		"	AND pt.id_cnj_classe IN (159,1117) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51036); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
       	//TBaixJudCrimNPL1º - Total de Processos Baixados de Execução de Penas Não-Privativas de Liberdade no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51037); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,7,8,9,12,13,26) " +
        		" 	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51037); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
       	//TBaixJudCrimPL1º - Total de Processos Baixados de Execução de Penas Privativas de Liberdade no 1º Grau
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51038); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (2,3,25) " +
        		" 	AND a.id_cnj_assunto IN (7791) " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51038); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
       	//TBaixJudNCrim1º - Total de Processos Baixados de Execução Judicial no 1º Grau, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51039); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,7,8,9,12,13,25,26) " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51039); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
       	//CnCrimTR – Casos  Novos nas Turmas Recursais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51040); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (307,284,1710,417,424,292) " +
        		" 	AND s.id_serv_subtipo IN (4,6) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51040); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnNCrimTR - Casos Novos nas Turmas Recursais Não-criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51041); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 1" +
        		"	AND pt.id_cnj_classe IN (1436,241,1727,120,460,1269) " +
        		" 	AND s.id_serv_subtipo IN (5,6) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51041); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnEletTR – Casos Novos Eletrônicos nas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51042); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (292,307,284,1710,417,424,1436,241,1727,1269,120,460) " +
        		" 	AND s.id_serv_subtipo IN (4,5,6) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51042); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCrimTR – Casos Pendentes nas Turmas Recursais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51043); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (307,284,1710,417,424,292) " +
        		" 	AND s.id_serv_subtipo IN (4,6) " +
        		"	AND p.id_area = 2 "+
         		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
    	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
    			"							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51043); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpNCrimTR - Casos Pendentes nas Turmas Recursais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51044); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (307,1710,417,202,460,27) " +
        		" 	AND s.id_serv_subtipo IN (5,6) " +
        		"	AND p.id_area = 1 "+
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
    	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
    			"							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51044); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DecCrimTR – Decisões que põem fim à relação processual no processo criminal nas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51045); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,6) " +
        		"	AND p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (307,284,1710,417,424) " +
        		"	AND mt.id_cnj_movi IN (230,235,236,237,238,239,240,241,242,442,443,446,447,450,451,455,457,458,459,460,461,464,466,471) " +	
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51045); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DecNCrimTR - Decisões que põem fim à relação processual no processo não-criminal nas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51046); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (5,6) " +
        		"	AND p.id_area = 1 " +
        		"	AND pt.id_cnj_classe IN (436,241,1727,120,460,1269) " +
        		"	AND mt.id_cnj_movi IN (230,235,236,237,238,239,240,241,242,442,443,446,447,450,451,454,455,457,458,459,460,461,462,464,466,471) " +	
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51046); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DeRIntTR - Decisões nas Turmas Recursais Passíveis de Recurso Interno
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51047); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (307,284,1710,417,424,1436,241,1727,1269,120,460,417,420,204,1712,218,1230,1231) " +
        		"	AND mt.id_cnj_movi IN (157,190,230,235,236,237,238,239,240,241,242,265,268,269,272,275,334,339,348,373,394,429,432,442,443,446,447,450,451,454,455,457,458,459,460,461,462,464,466,471,792,804,898,940,941,944,945,961,1059,11382,11423,11425,11426,11554) " +	
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51047); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntTR - Recursos Internos nas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51048); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv ";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (1689,420) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51048); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntPTR - Recursos Internos Pendentes nas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51049); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (1689,420) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51049); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RjExtTR - Recursos Extraordinários julgados pelo STF -- NÃO É EXTRAÍDA NO PROJUDI

        //RpExtTR - Recursos das decisões de Turmas Recursais providos pelo STF em Recurso Extraordinário (ainda que parcialmente) -- NÃO É EXTRAÍDA NO PROJUDI

        //TBaixCrimTR - Total de Processos Baixados nas Turmas Recursais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51052); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,6) " +
        		" 	AND p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (307,284,1710,417,424) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51052); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixNCrimTR - Total de Processos Baixados nas Turmas Recursais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51053); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (5,6) " +
        		" 	AND p.id_area = 1 " +
        		"	AND pt.id_cnj_classe IN (1436,241,1727,120,460,1269) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51053); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnCCrimJE - Casos Novos de Conhecimento nos Juizados Especiais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51054); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		"	AND p.id_area = 2 " +
        		" 	AND s.id_serv_subtipo IN (2,3,25) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51054); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnCNCrimJE - Casos Novos de Conhecimento nos Juizados Especiais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51055); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,218,228,436,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		"	AND p.id_area = 1 " +
        		" 	AND s.id_serv_subtipo IN (1,3,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51055); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnExtJE - Casos Novos de Execução de Título Extrajudicial nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51056); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (159) " +
        		" 	AND s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51056); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnEletJE - Casos Novos Eletrônicos nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51057); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,159,166,167,170,171,172,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,436,1117,1118,1122,1124,1199,1268,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1710,1726,1727,10933,10943,10944,10967,10979,11026,11397) " +
        		" 	AND s.id_serv_subtipo IN (1,2,3,25,26) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51057); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCCrimJE - Casos Pendentes de Conhecimento nos Juizados Especiais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51058); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (2,3) " +
        		"	AND p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51058); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCNCrimJE - Casos Pendentes de Conhecimento nos Juizados Especiais Grau Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51059); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,26) " +
        		"	AND p.id_area = 1 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,436,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51059); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpExtJE - Casos Pendentes de Execução de Título Extrajudicial nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51060); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51060); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DeRextJE - Decisões nos Juizados Especiais Passíveis de Recurso Externo
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51061); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,46,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,74,76,77,79,80,81,83,84,85,86,87,89,92,93,94,96,97,98,99,100,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,156,157,159,166,167,170,171,172,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,228,229,230,231,232,234,236,239,240,241,251,258,261,218,282,283,287,288,289,293,294,295,297,300,302,307,309,310,311,326,329,330,407,408,409,410,411,1112,1114,1116,1117,1118,1122,1124,1199,1230,1231,1232,1262,1268,1283,1284,1288,1289,1294,1295,1298,1301,1308,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1432,1434,1435,1438,1440,1451,1455,1461,1462,1463,1464,1465,1474,1478,1682,1683,1690,1691,1701,1702,1703,1704,1705,1706,1707,1709,1710,1726,1727,10933,10943,10944,10960,10967,10972,10973,10974,10975,10976,10977,10979,10980,10981,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (128,146,172,175,190,196,202,208,219,220,221,235,263,264,268,272,275,276,332,334,335,339,347,348,349,352,353,354,355,357,358,371,373,374,381,383,388,389,391,393,394,399,400,402,404,429,432,442,443,444,446,447,448,450,451,452,454,455,457,458,459,460,461,462,463,464,465,466,471,785,787,788,792,804,818,819,821,823,889,892,898,900,940,941,944,945,961,988,1002,1003,1004,1009,1010,1011,1014,1016,1017,1018,1019,1042,1043,1044,1045,1046,1047,1048,1049,1050,1059,1063,10953,10961,10962,10963,10964,11382,11393,11395,11396,11401,11402,11404,11405,11406,11407,11408,11409) ";	
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51061); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //DeImpJE – Decisões nos Juizados Especiais Passíveis de Impugnação
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51062); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (37,74,92,93,94,112,173,190,241,287,288,289,293,294,295,297,300,302,305,307,309,318,319,320,321,322,323,324,326,436,1230,1231,1232,1707,1709,1719,10944,10967) " +
        		"	AND mt.id_cnj_movi IN (146,163,175,190,264,268,269,272,275,276,332,334,335,339,348,349,355,373,381,383,389,391,393,394,400,402,404,792,818,821,892,898,940,941,944,945,961,1059,1063,11002,11012,11013,1014,11015,11017,11018,11382,11423,11424,11426,11554) ";
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51062); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudCrimNPLJE - Execuções de Penas Não-Privativas de Liberdade nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51063); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		" 	AND p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +	
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51063); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudNCrimJE - Execuções Judiciais nos Juizados Especiais, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51064); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51064); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudPCrimNPLJE - Execuções Pendentes de Penas Não-Privativas de Liberdade nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51065); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN projudi.proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		" 	AND p.id_area = 2" +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +	
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
    	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
    			"							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51065); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeJudPNCrimJE - Execuções Judiciais Pendentes nos Juizados Especiais, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51066); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
    	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
    			"							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51066); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ExeSuSJE - Execuções Sobrestadas ou Suspensas ou em Arquivo Provisório
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51067); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) " +
        		"	AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
    	sql+="			AND p.id_proc IN (SELECT m1.id_proc " +
    			"							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (245,276,11012,11013,11014,11015,11016,11017,11018) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51067); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();


        //IncExJE - Incidentes de Execução nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51068); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51068); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //IncExPJE - Incidentes de Execução Pendentes nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51069); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) " +
        		"	AND p.data_recebimento <= ? ";  ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51069); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //ISupJE - Impugnações a Decisões dos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51070); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (202,424,1271) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51070); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntCJE - Recursos Internos nos Juizados Especiais na Fase de Conhecimento
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51071); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (420,1689) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51071); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RIntCPJE - Recursos Internos Pendentes nos Juizados Especiais na Fase de Conhecimento
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51072); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (420,1689) " +
        		"	AND p.data_recebimento <= ? "; ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
        sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
        		"							FROM movi m1 " +
        		"							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
        		"							WHERE m1.id_proc = p.id_proc " +
        		"								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
        		"								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51072); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RjInJE -  Recursos Inominados julgados pelas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51073); ps.adicionarLong(1); 
    	sql+=" FROM recurso r"; 
    	sql+=" INNER JOIN proc_tipo pt on r.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = r.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON r.id_serv_recurso = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (460) " +
        		"	AND mt.id_cnj_movi IN (230,235,236,237,238,239,240,241,242) " +	
        		"	AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51073); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RpINJE - Recursos Inominados das decisões dos Juizados Especiais providos pelas Turmas Recursais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51074); ps.adicionarLong(1); 
    	sql+=" FROM recurso r"; 
    	sql+=" INNER JOIN proc_tipo pt on r.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = r.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON r.id_serv_recurso = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (460) " +
        		"	AND mt.id_cnj_movi IN (237,238,240,241) " +	
        		"	AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51074); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //RSupJE - Recursos à Instância Superior nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51075); ps.adicionarLong(1); 
    	sql+=" FROM recurso r"; 
    	sql+=" INNER JOIN proc_tipo pt on r.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = r.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON r.id_serv_recurso = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (4,5,6) " +
        		"	AND pt.id_cnj_classe IN (202,204,417,460,1712) " +
        		"	AND mt.id_cnj_movi IN (230,235,236,237,238,239,240,241,242) " +	
        		"	AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51075); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SeRIntJE - Sentenças nos Juizados Especiais Pasíveis de Recurso Interno
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51076); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,282,283,287,288,289,293,294,295,297,300,302,307,309,310,311,326,329,330,1118,1122,1124,1199,1268,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1710,1726,10933,10943,10944,10967,10979,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (202,208,219,220,221,235,442,443,444,446,447,448,450,451,452,454,471,11401,11402,11404,11405,11406,11407,11408,11409) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51076); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentCCrimJE - Sentenças de Conhecimento  nos Juizados Especiais Criminais    
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51077); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (2,3) " +
        		"	AND p.id_area = 2 " +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) " +
        		"	AND mt.id_cnj_movi IN (219,220,221,442,443,442,447,442,451,459,460,461,464,1042,1043,1044,1045,1046,1047,1048,1049,10953,10961) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51077); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentCNCrimJE - Sentenças de Conhecimento nos Juizados Especiais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51078); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,26) " +
        		"	AND p.id_area = 1 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,218,228,436,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) " +
        		"	AND mt.id_cnj_movi IN (202,208,219,220,221,235,442,443,444,446,447,448,450,451,452,454,455,457,458,459,460,461,462,463,464,465,466,471,11401,11402,11404,11405,11406,11407,11408,11409) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51078); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentExtJE - Sentenças em Execução de Título ExtraJudicial nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51079); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) " +
        		"	AND mt.id_cnj_movi IN (196,454,455,457,458,459,460,461,462,463,464,465,466,471,900) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51079); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentJudCrimNPLJE - Sentenças em Execução de Penas Não-Privativas de Liberdade nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51080); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN assunto a ON a.id_assunto = pa.id_assunto " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (386,11399,1714) " +
        		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " +
        		"	AND mt.id_cnj_movi IN (196,1050) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51080); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //SentJudNCrimJE - Sentenças em Execução nos Juizados Especiais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51081); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN movi m ON m.id_proc = p.id_proc " +
    			" INNER JOIN movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo ";
    	sql+=" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,3,26) " +
        		"	AND p.id_area = 1 " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,1465,10980) " +
        		"	AND mt.id_cnj_movi IN (196,455,457,458,460,462,463,464,465,466,471,871,10964) " ;
        sql+="		AND m.data_realizacao BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51081); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixCCrimJE - Processos de Conhecimento Baixados nos Juizados Especiais Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51082); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 2 " +
        		"	AND s.id_serv_subtipo IN (2,3) " +
        		"	AND pt.id_cnj_classe IN (218,282,283,287,288,289,293,294,295,297,299,300,302,307,309,310,311,326,329,330,1268,1710,1727,10943,10944,10967) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51082); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixCNCrimJE - Total de Processos de Conhecimento Baixados nos Juizados Especiais Não-Criminais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51083); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE p.id_area = 1 " +
        		"	AND s.id_serv_subtipo IN (1,3,26) " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,218,228,436,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51083); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixExtJE - Total de Processos Baixados de Execução de Título Extrajudicial nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51084); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (159) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51084); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixJudCrimNPLJE - Total de Processos Baixados de Execução de Penas Não-Privativas de Liberdade nos Juizados Especiais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51085); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN proc_assunto pa ON p.id_proc = pa.id_proc " +
    			" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
         		"	AND a.id_cnj_assunto IN (7790,7788,7789,7786,7787,7785) " + 
        		"	AND pt.id_cnj_classe IN (386,11399,1714) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51085); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //TBaixJudNCrimJE - Total de Processos Baixados de Execução Judicial nos Juizados Especiais, Exceto Execuções Penais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51086); ps.adicionarLong(1); 
    	sql+=" FROM proc p"; 
    	sql+=" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo " +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE s.id_serv_subtipo IN (1,2,3,26) " +
        		"	AND pt.id_cnj_classe IN (156,157,1112,1114,1432,1434,1435,10980,1465) ";
        sql+="		AND ((p.data_arquivamento BETWEEN ? AND ?) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
    	sql+="			OR p.id_proc IN (SELECT m1.id_proc " +
    	        "							FROM movi m1 " +
    	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
    	        "							WHERE m1.id_proc = p.id_proc " +
    	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
    	        "								AND m1.data_realizacao BETWEEN ? AND ?)) "; ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51086); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnPF1º - Casos Novos no 1º Grau Propostos Pela União, Autarquias, Fundações e Empresas Públicas Federais
    	sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51087); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 3 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51087); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar(); 
        
       	//CnPE1º - Casos Novos no 1º Grau Propostos Pelos Estados, Disrito Federal, Autarquias, Fundações e Empresas Públicas Estaduas ou Distritais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51088); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 2 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51088); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar(); 
        
       	//CnPM1º - Casos Novos no 1º Grau Propostos Pelos Municípios, Autarquias, Fundações e Empresas Públicas Municipais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51089); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 1 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51089); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar(); 
        
       	//CpPF1º - Casos Pendentes no 1º Grau Propostos Pela União, Autarquias, Fundações e Empresas Públicas Federais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51090); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 3 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51090); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpPE1º - Casos Pendentes no 1º Grau Propostos Pelos Estados, Disrito Federal, Autarquias, Fundações e Empresas Públicas Estaduas ou Distritais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51091); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 2 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51091); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
                
        //CpPM1º - Casos Pendentes no 1º Grau Propostos Pelos Municípios, Autarquias, Fundações e Empresas Públicas Municipais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51092); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 1 " +
         		" 	AND pp.id_proc_parte_tipo = 2 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51092); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        // CnCF1º - Casos Novos no 1º Grau Propostos Contra a União, Autarquias, Fundações e Empresas Públicas Federai
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51093); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 3 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51093); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnCE1º - Casos Novos no 1º Grau Propostos Contra os Estados, Disrito Federal, Autarquias, Fundações e Empresas Públicas Estaduas ou Distritais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51094); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 2 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51094); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CnCM1º - Casos Novos no 1º Grau Propostos Contra os Municípios, Autarquias, Fundações e Empresas Públicas Municipais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51095); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 1 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51095); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCF1º - Casos Pendentes no 1º Grau Propostos Contra a União, Autarquias, Fundações e Empresas Públicas Federais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51096); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 3 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51096); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        
        //CpCE1º - Casos Pendentes no 1º Grau Propostos Contra os Estados, Disrito Federal, Autarquias, Fundações e Empresas Públicas Estaduas ou Distritais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51097); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 2 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51097); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar();
        
        //CpCM1º - Casos Pendentes no 1º Grau Propostos Contra os Municípios, Autarquias, Fundações e Empresas Públicas Municipais
        sql= " INSERT INTO EST_PROD (ANO,MES,ID_EST_PROD_ITEM,ID_SISTEMA,ID_SERV,QUANTIDADE)"; 
    	sql+=" SELECT ? AS ANO, ? AS MES, ? AS ID_EST_PROD_ITEM , ? AS ID_SISTEMA, s.id_serv AS ID_SERV, NVL(COUNT(*),0) AS QUANTIDADE";  ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51098); ps.adicionarLong(1); 
    	sql+=" FROM proc_parte pp "; 
    	sql+=" INNER JOIN proc p ON pp.id_proc = p.id_proc " +
    			" INNER JOIN proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo" +
    			" INNER JOIN serv s ON p.id_serv = s.id_serv";
        sql+=" WHERE pp.id_governo_tipo = 1 " +
         		" 	AND pp.id_proc_parte_tipo = 3 " +
        		"	AND pt.id_cnj_classe IN (7,22,28,29,30,31,32,34,35,37,38,39,40,41,44,45,48,49,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,69,72,74,76,77,79,80,81,83,84,85,86,87,89,90,92,93,94,96,97,98,99,108,110,111,112,113,114,115,118,119,120,123,124,127,128,129,134,135,136,137,138,140,141,142,143,151,152,153,154,166,167,170,171,172,159,1117,173,176,177,178,179,180,181,182,183,186,188,190,191,192,193,194,195,196,210,1118,1122,1124,1199,1289,1294,1295,1389,1390,1391,1392,1396,1399,1401,1412,1414,1415,1417,1420,1425,1426,1438,1440,1464,1682,1683,1690,1691,1703,1704,1705,1706,1707,1709,1726,10933,10979,11026,11397,1116) " +
        		" 	AND s.id_serv_subtipo IN (7,8,9,11,12,13,27) " +
        		"	AND p.data_recebimento BETWEEN ? AND ? ";  ps.adicionarDate(dataInicial); ps.adicionarDate(dataFinal);
        sql+="		AND ((p.data_arquivamento IS NULL OR p.data_arquivamento > ?) "; ps.adicionarDate(dataFinal);
     	sql+="			AND p.id_proc NOT IN (SELECT m1.id_proc " +
     			"							FROM movi m1 " +
     	        "							INNER JOIN movi_tipo mt1 ON mt1.id_movi_tipo = m1.id_movi_tipo" +
     	        "							WHERE m1.id_proc = p.id_proc " +
     	        "								AND mt1.id_cnj_movi IN (22,246,869,488,123,982) " +
     	        "								AND m1.data_realizacao <= ?)) "; ps.adicionarDate(dataFinal);
        //teste para só gerar uma unica vez
        sql+=" AND 0 = (SELECT COUNT(1) FROM EST_PROD ep WHERE ep.ANO = ? AND ep.MES = ? AND ep.ID_EST_PROD_ITEM = ? AND ep.ID_SISTEMA = ?)"; ps.adicionarLong(ano); ps.adicionarLong(mes); ps.adicionarLong(51098); ps.adicionarLong(1);
        sql+=" GROUP BY s.id_serv ";
        executarUpdateDelete(sql, ps);  	        
        ps.limpar(); 
    }
    
    
    public String gerarJsonDistribuicaoProcessoXDiasComarca(String id_serv, QuantidadeDias qtdDias) throws Exception {
    	String stJSON = "";
    	String stSql ;
    	ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql = "select comarca, ponteiro_log_tipo, sum(qtd) total from "+qtdDias.getView()+" where id_comarca = (select id_comarca from serv s where id_serv = ?) ";	ps.adicionarLong(id_serv); 
		
		stSql +=" group by comarca, ponteiro_log_tipo";		
		stSql +=" ORDER by ponteiro_log_tipo";	
		
		try{
			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				stJSON = gerarJsonComarcaQtd(rs1, qtdDias.getDescricao(), qtdDias.getDias());				
			}
		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
				
    	return stJSON;
    	
    }
    
    public String gerarJsonDistribuicaoProcessoXDiasComarcaServentia(String id_serv, QuantidadeDias qtdDias) throws Exception {
    	String stJSON = "";
    	String stSql ;
    	ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql = "select comarca, serv, ponteiro_log_tipo, sum(qtd) total from "+qtdDias.getView()+" where id_serv = ? ";	ps.adicionarLong(id_serv); 
		
		stSql +=" group by comarca, serv, ponteiro_log_tipo";		
		stSql +=" ORDER by ponteiro_log_tipo";	
		
		try{
			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				stJSON = gerarJsonComarcaServentiaQtd(rs1, qtdDias.getDescricao(), qtdDias.getDias());				
			}
		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
				
    	return stJSON;
    	
    }

    
    public String gerarJsonDistribuicaoProcessoXDiasComarcaServentiaServentiaCargo(String id_serv, QuantidadeDias qtdDias) throws Exception {
    	String stJSON = "";
    	String stSql ;
    	ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql = "select comarca, serv, SERV_CARGO, ponteiro_log_tipo, sum(qtd) total from "+qtdDias.getView()+" where id_serv = ? ";	ps.adicionarLong(id_serv); 
		
		stSql +=" group by comarca, serv, SERV_CARGO, ponteiro_log_tipo";		
		stSql +=" ORDER by ponteiro_log_tipo";	
		
		try{
			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				stJSON = gerarJsonComarcaServentiaServentiaCargoQtd(rs1, qtdDias.getDescricao(), qtdDias.getDias());				
			}
		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
				
    	return stJSON;
    	
    }
       
	
    private String gerarJsonComarcaQtd(ResultSetTJGO rs1, String descricao, int dias) throws Exception {
		JSONObject jsonO = new JSONObject();
		//JSONArray jsonA = new JSONArray();
		JSONObject jsonAux = new JSONObject();
		
		jsonO.put("comarca", rs1.getString("COMARCA"));		
		jsonO.put("coluna", descricao);
		jsonO.put("dias", dias);
		jsonAux = new JSONObject();
		do {
			JSONObject jsonAux2 = new JSONObject();
			jsonAux2.put("qtd", rs1.getString("TOTAL"));
			if (dias>1) {				
				jsonAux2.put("media", Funcoes.FormatarDecimal(rs1.getDouble("TOTAL") / dias));
			}
			jsonAux.put(rs1.getString("PONTEIRO_LOG_TIPO"),jsonAux2 );
		} while (rs1.next());
		jsonO.put(descricao, jsonAux);
		
		return jsonO.toString();
	}
	
	private String gerarJsonComarcaServentiaQtd(ResultSetTJGO rs1, String descricao, int dias) throws Exception {
		JSONObject jsonO = new JSONObject();
		//JSONArray jsonA = new JSONArray();
		JSONObject jsonAux = new JSONObject();
		
		jsonO.put("comarca", rs1.getString("COMARCA"));
		jsonO.put("serventia", rs1.getString("SERV"));
		jsonO.put("coluna", descricao);
		jsonO.put("dias", dias);
		jsonAux = new JSONObject();
		do {
			JSONObject jsonAux2 = new JSONObject();
			jsonAux2.put("qtd", rs1.getString("TOTAL"));
			if (dias>1) {
				jsonAux2.put("media", Funcoes.FormatarDecimal( rs1.getDouble("TOTAL") / dias));
			}
			jsonAux.put(rs1.getString("PONTEIRO_LOG_TIPO"),jsonAux2 );
		} while (rs1.next());
		jsonO.put(descricao, jsonAux);
		
		return jsonO.toString();
	}
	
	private String gerarJsonComarcaServentiaServentiaCargoQtd(ResultSetTJGO rs1, String descricao, int dias) throws Exception {
		JSONObject jsonO = new JSONObject();
		//JSONArray jsonA = new JSONArray();
		JSONObject jsonAux = new JSONObject();
		
		jsonO.put("comarca", rs1.getString("COMARCA"));
		jsonO.put("serventia", rs1.getString("SERV"));
		jsonO.put("coluna", descricao);
		jsonO.put("dias", dias);
		jsonAux = new JSONObject();
		do {
			JSONObject jsonAux2 = new JSONObject();
			jsonAux2.put("usuario", rs1.getString("SERV_CARGO"));
			jsonAux2.put("qtd", rs1.getString("TOTAL"));
			if (dias>1) {
				jsonAux2.put("media", Funcoes.FormatarDecimal(rs1.getDouble("TOTAL") / dias));
			}
			jsonAux.put(rs1.getString("PONTEIRO_LOG_TIPO"),jsonAux2 );
		} while (rs1.next());
		jsonO.put(descricao, jsonAux);
		
		return jsonO.toString();
	}
    
}
