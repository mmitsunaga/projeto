package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class PonteiroLogPs extends PonteiroLogPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7232925647395340691L;
	public PonteiroLogPs(Connection conexao){
		Conexao = conexao;
	}
//

	/**
	 * Consulta as serventias de uma determinada área de distribuição
	 * 			dada uma area de distribuição, pego a serventia que recebeu menos processo, se for passado uma id_serventia retiro-a das consulta
	 * 			uso o SORTEIO para o caso de distribuição 2/1, 3/1 ou 4/1	
	 * 			alteração para atender o provimento 16 de 2012 da corregedoria
	 * @param id_AreaDistribuicao
	 *            identificação da área de distribuição
	 * 	
	 * 
	 * obs. sempre que alterar essa rotinar verificar os relatórios de ponteiro de distribuição
	 * 
	 * @author jrcorrea
	 */
	
	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String Id_Serventia) throws Exception {
		String Sql;
		String stTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();					
		
 
		Sql = " select  CASE WHEN (sad.probabilidade > DBMS_RANDOM.Value(0, 1)) THEN 1  ELSE 0 END AS ordem1, s.id_serv, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  sum(pl.qtd) FROM ponteiro_log pl    WHERE pl.ID_AREA_DIST = ? and pl.id_serv = s.id_serv  ),0) as qtd "; ps.adicionarLong(id_AreaDistribuicao);
		Sql += "  from serv s ";
		Sql += "    inner join serv_area_dist sad on s.id_serv = sad.id_Serv and sad.id_area_dist = ?";			ps.adicionarLong(id_AreaDistribuicao);
		Sql += "  where s.QUANTIDADE_DIST > 0";   
		Sql += "    AND s.CODIGO_TEMP = ?"; 																	ps.adicionarLong(ServentiaDt.ATIVO);		 	
		if (Id_Serventia.length()>0) {
			Sql += "       AND s.ID_SERV <> ?";																	ps.adicionarLong(Id_Serventia);
		}
		Sql += " ORDER BY ordem1 DESC, QTD, SORTEIO ";				
		
		try{
			
			int inConte = 0;
			do{
				//tento fazer 10 sorteios
				if (inConte>=10){
					throw new MensagemException("Não foi possível realizar a distribuição do processo. Entre em contato com a Divisão de Gerenciamento do Processo Digital para verificar a estrutura de serventias da Área de Distribuição de ID: " + id_AreaDistribuicao);
				}			
				rs = consultar(Sql,ps);
				inConte++;
			}while (!rs.next() ); 	
		    //pego a primeira serventia da area distribuição
			stTemp = rs.getString("ID_SERV");				
			
		} finally{
			try{if (rs != null) rs.close();	} catch(Exception e) {	}
		}
		return stTemp;
	}
	
	public String consultarServentiaCargoServentias(String id_serventia, String id_serv_cargo, String id_area_distribuicao) throws Exception {
		
		String Sql;
		String obTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		//pegar os desembargadores
		Sql = " SELECT   CASE WHEN (sr.probabilidade > DBMS_RANDOM.Value(0, 1)) THEN 1  ELSE 0 END AS ordem1, sc.ID_SERV_CARGO, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  sum(pl.qtd) FROM ponteiro_log pl  WHERE pl.ID_SERV = s.id_serv and pl.ID_SERV_CARGO = sc.ID_SERV_CARGO and pl.id_area_dist = ? ),0) as qtd "; ps.adicionarLong(id_area_distribuicao); 	
		Sql += "     FROM PROJUDI.SERV s ";
		Sql += " 		  INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC"; 
		Sql += " 		  INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV"; 
		Sql += "          INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO"; 
		Sql += " 		  INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += " 		  INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO"; 
		Sql += " 	  WHERE s.ID_SERV = ?";																	ps.adicionarLong(id_serventia);
		Sql += " 	      AND sc.QUANTIDADE_DIST > 0 ";		 
		Sql += " 	      AND gt.GRUPO_TIPO_CODIGO IN (?, ?)"; 												ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU);
		Sql += " 	      AND sr.RECEBE_PROC = ?";															ps.adicionarBoolean(true);
		if (id_serv_cargo.length()>0) {
			Sql += "       AND sc.ID_SERV_CARGO <> ?";														ps.adicionarLong(id_serv_cargo);
		}				
		Sql += " union ";
		Sql += "  SELECT  CASE WHEN (sc.probabilidade > DBMS_RANDOM.Value(0, 1)) THEN 1  ELSE 0 END AS ordem1, sc.ID_SERV_CARGO, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  sum(pl.qtd) FROM ponteiro_log pl  WHERE pl.ID_SERV = s.id_serv and pl.ID_SERV_CARGO = sc.ID_SERV_CARGO and pl.id_area_dist = ? ),0) as qtd "; ps.adicionarLong(id_area_distribuicao);
		Sql += "     FROM PROJUDI.SERV s "; 			
		Sql += " 		  INNER JOIN PROJUDI.SERV_CARGO sc on s.ID_SERV = sc.ID_SERV "; 
		Sql += " 		  INNER JOIN PROJUDI.CARGO_TIPO ct ON ct.ID_CARGO_TIPO = sc.ID_CARGO_TIPO ";
		Sql += " 	  WHERE s.ID_SERV = ?";																	ps.adicionarLong(id_serventia);
		Sql += " 	      AND sc.QUANTIDADE_DIST > 0 ";				
		Sql += " 	      AND ct.cargo_tipo_codigo IN (?,?,?,?,?,?) ";										ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);	ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU); ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO); ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);	ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL); ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);			
		if (id_serv_cargo.length()>0) {
			Sql += "       AND sc.ID_SERV_CARGO <> ?";														ps.adicionarLong(id_serv_cargo);
		}

		Sql =  "SELECT * FROM (" + Sql + ") ORDER BY ordem1 DESC, QTD, SORTEIO ";
	
		try{
			rs = consultar(Sql,ps);			
			if (rs.next()) {	
			    //pego o primeiro serventia cargo				
				 obTemp =rs.getString("ID_SERV_CARGO");	             
			} else {
				throw new MensagemException("Não foi possível encontrar um cargo. Entre em contato com a Divisão de Gerenciamento do Processo Digital para verificar a estrutura de cargos da Serventia de ID: " + id_serventia);
			}
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return obTemp;
				
	}
	
	public String[] consultarServentiaCargoServentiasCompleto( String id_serv_cargo, String id_area_distribuicao) throws Exception {
		
		String Sql;
		String obTemp[] = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		//pegar os desembargadores
		Sql = " SELECT   CASE WHEN (sr.probabilidade > DBMS_RANDOM.Value(0, 1)) THEN 1  ELSE 0 END AS ordem1, sc.ID_SERV_CARGO, s.ID_SERV, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  sum(pl.qtd) FROM ponteiro_log pl  WHERE pl.ID_SERV = s.id_serv and pl.ID_SERV_CARGO = sc.ID_SERV_CARGO and pl.id_area_dist = ? ),0) as qtd "; ps.adicionarLong(id_area_distribuicao); 	
		Sql += "     FROM PROJUDI.serv_area_dist sad ";
		Sql += " 		  inner join PROJUDI.SERV s  on sad.id_serv = s.id_Serv ";
		Sql += " 		  INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC"; 
		Sql += " 		  INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV"; 
		Sql += "          INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO"; 
		Sql += " 		  INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += " 		  INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO"; 
		Sql += " 	  WHERE  sad.id_area_dist = ?";															ps.adicionarLong(id_area_distribuicao);
		Sql += " 	      AND sc.QUANTIDADE_DIST > 0 ";		 
		Sql += " 	      AND gt.GRUPO_TIPO_CODIGO IN (?, ?)"; 												ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU);
		Sql += " 	      AND sr.RECEBE_PROC = ?";															ps.adicionarBoolean(true);
		if (id_serv_cargo.length()>0) {
			Sql += "       AND sc.ID_SERV_CARGO <> ?";														ps.adicionarLong(id_serv_cargo);
		}				
		Sql += " union ";
		Sql += "  SELECT  CASE WHEN (sc.probabilidade > DBMS_RANDOM.Value(0, 1)) THEN 1  ELSE 0 END AS ordem1, sc.ID_SERV_CARGO, s.ID_SERV, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  sum(pl.qtd) FROM ponteiro_log pl  WHERE pl.ID_SERV = s.id_serv and pl.ID_SERV_CARGO = sc.ID_SERV_CARGO and pl.id_area_dist = ? ),0) as qtd "; ps.adicionarLong(id_area_distribuicao);
		Sql += "    PROJUDI.serv_area_dist sad";
		Sql += " 		  inner join PROJUDI.SERV s on sad.id_serv = s.id_Serv "; 
		Sql += " 		  INNER JOIN PROJUDI.SERV_CARGO sc on s.ID_SERV = sc.ID_SERV "; 
		Sql += " 		  INNER JOIN PROJUDI.CARGO_TIPO ct ON ct.ID_CARGO_TIPO = sc.ID_CARGO_TIPO ";
		Sql += " 	  WHERE  sad.id_area_dist = ?";															ps.adicionarLong(id_area_distribuicao);
		Sql += " 	      AND sc.QUANTIDADE_DIST > 0 ";				
		Sql += " 	      AND ct.cargo_tipo_codigo IN (?,?,?,?,?,?) ";										ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);	ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU); ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO); ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);	ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL); ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);			
		if (id_serv_cargo.length()>0) {
			Sql += "       AND sc.ID_SERV_CARGO <> ?";														ps.adicionarLong(id_serv_cargo);
		}

		Sql =  "SELECT * FROM (" + Sql + ") ORDER BY ordem1 DESC, QTD, SORTEIO ";
	
		try{
			rs = consultar(Sql,ps);			
			if (rs.next()) {	
			    //pego o primeiro serventia cargo				
				 obTemp = new String[] {rs.getString("ID_SERV"),rs.getString("ID_SERV_CARGO")};	             
			} else {
				throw new MensagemException("Não foi possível encontrar um cargo. Entre em contato com a Divisão de Gerenciamento do Processo Digital para verificar a estrutura de cargos  ");
			}
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return obTemp;
				
	}
	public void atualizarProbabilidadePonteiros()throws Exception {
		String stSql1;
		String stSql2;
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps1 = new PreparedStatementTJGO();		
		
		stSql1 = "Select id_serv_area_dist, id_serv, id_area_dist, PROBABILIDADE From Serv_Area_Dist Where Probabilidade<?" ; ps1.adicionarDouble(Configuracao.PROBABILIDADE_PADRAO);
		
		try{
			rs1 = consultar(stSql1,ps1);			
			while (rs1.next()) {
				PreparedStatementTJGO ps2 = new PreparedStatementTJGO();	
			    //pego o primeiro serventia cargo				
				 String stId_AreaDist =rs1.getString("id_area_dist");
				 String stId_Serv =rs1.getString("id_serv");
				 String stId_Serv_Area_Dist =rs1.getString("id_serv_area_dist");
				 Double  probabilidade = rs1.getDouble("PROBABILIDADE");
				 
				 stSql2 = "Select Max(Qtd) as Maior From ( ";
				 stSql2 += "   Select id_serv , sum(nvl(qtd,0)) as qtd From Ponteiro_Log"; 
				 stSql2 += "     Where Id_Area_Dist = ? ";									ps2.adicionarLong(stId_AreaDist);
				 stSql2 += "       Group By Id_Serv ) Tab ";
				 stSql2 += "  where tab.id_serv <>?";										ps2.adicionarLong(stId_Serv);
				 rs2 = consultar(stSql2,ps2);
				 
				 long loMaior = 0;
				 if(rs2.next())
					 loMaior =rs2.getLong("Maior");
				 
				 stSql2 = "Select sum(nvl(qtd,0)) as Total From Ponteiro_Log "; 
				 stSql2 += " Where Id_Area_Dist = ?";
				 stSql2 += " and id_serv = ? ";
				 
				 rs2 = consultar(stSql2,ps2);
				 
				 long loTotal = 0;
				 if(rs2.next())
					 loTotal =rs2.getLong("Total");
				 
				 //se o total do que está com a probabilidade menor chegar próximo ou ultrapassar 
				 //a quantidade do maior ponteiro, retorne a probabilidade de distribuição para o padrão
				 if (loTotal>=(loMaior-1)){	
					 PreparedStatementTJGO ps3 = new PreparedStatementTJGO();
					 stSql2 = "UPDATE serv_area_dist SET probabilidade = ? ";				ps3.adicionarDouble(Configuracao.PROBABILIDADE_PADRAO);
					 stSql2 += " WHERE id_serv_area_dist = ? ";								ps3.adicionarLong(stId_Serv_Area_Dist);
					 executarUpdateDelete(stSql2, ps3);
					 
					/* ---------- PONTEIRO ----------------*/
					 NumberFormat nF = NumberFormat.getPercentInstance();
					///salvo o ponteiro
					PonteiroLogDt ponteiroLogDt = new PonteiroLogDt(stId_AreaDist, PonteiroLogTipoDt.ALTERACAO_PROBABILIDADE_DISTRIBUICAO, "", stId_Serv, UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi,  new Date(), "Probabilidade De :"+ nF.format(probabilidade)+" Foi Alterada para: "+nF.format(Configuracao.PROBABILIDADE_PADRAO), 0, "");
					this.inserir(ponteiroLogDt);
					/* ---------- PONTEIRO ----------------*/
					 
				 }
				 
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
	}
	
	/**
	 * Método verifica se houve registro de qualquer ponteiro de distribuição para o cargo informado.
	 * @param idServentiaCargo - ID do serventia cargo
	 * @return true se houver registro ou false se não houver registro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean haDistribuicaoServentiaCargo(String idServentiaCargo) throws Exception { 
		String Sql;
		int obTemp = 0;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		
		Sql = " SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.PONTEIRO_LOG WHERE ID_SERV_CARGO = ?";	ps.adicionarLong(idServentiaCargo);

		try{
			rs = consultar(Sql,ps);			
			if (rs.next()) {	
				 obTemp = rs.getInt("QUANTIDADE");	       
				 if(obTemp > 0) {
					 return true;
				 } else {
					 return false;
				 }
			} else {
				throw new MensagemException("Ocorreu um erro ao executar a consula do método haDistribuicaoServentiaCargo. Favor entrar em contato com o suporte.");
			}
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
	}
	
	/**
	 * Método que retorna o id da serventia para a qual o ponteiro do processo está apontando.
	 * @param String idProc
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String ondeEstaPonteiroProcesso(String idProc) throws Exception {
		String sql;
		String idServ = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "Select id_serv From Ponteiro_Log Pl ";
		sql += "  Where pl.id_ponteiro_log = ";
		sql += "    (select max(id_ponteiro_log) from Ponteiro_Log ";
		sql += "        where id_proc = ? ";							ps.adicionarLong(idProc);
		sql += "		And Id_Ponteiro_Log_Tipo in (1,4))";
		try{
			rs = consultar(sql,ps);			
			if (rs.next()) {	
				 idServ = rs.getString("id_serv");	       
			}
		} finally{
				if (rs != null) rs.close();
		}
		
		return idServ;
	}

	public boolean temPonteiroCriado(String id_area_dist, String id_proc, String id_Serventia,String id_serv_cargo) throws Exception {
		String sql;
		boolean boRetorno = false;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "Select sum(QTD) as qtd From Ponteiro_Log Pl ";		
		sql += "  where ID_PROC = ? ";		ps.adicionarLong(id_proc);
		sql += "	And ID_SERV =?  ";		ps.adicionarLong(id_Serventia);
		sql += "	And ID_SERV_CARGO =?  ";		ps.adicionarLong(id_serv_cargo);
		sql += "	And ID_AREA_DIST =?  ";		ps.adicionarLong(id_area_dist);
		
		
		try{
			rs = consultar(sql,ps);			
			if (rs.next()) {
				if( rs.getInt("qtd")==1){
					boRetorno = true;
				}
			}
		} finally{
				if (rs != null) rs.close();
		}
		
		return boRetorno;
	}
}
