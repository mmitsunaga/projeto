package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

// ---------------------------------------------------------
public class ServentiaCargoPs extends ServentiaCargoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -8652390593104368959L;

    public ServentiaCargoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir para setar CodigoTemp
	 */
	public void inserir(ServentiaCargoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.SERV_CARGO (";
		SqlValores = " Values (";
		if (!(dados.getServentiaCargo().length() == 0)){
			SqlCampos += "SERV_CARGO ";
			SqlValores += "?";
			ps.adicionarString(dados.getServentiaCargo());
		}
		if (!(dados.getId_Serventia().length() == 0)){
			SqlCampos += ",ID_SERV ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Serventia());
		}
		if (!(dados.getId_CargoTipo().length() == 0)){
			SqlCampos += ",ID_CARGO_TIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_CargoTipo());
		}
		if (!(dados.getId_ServentiaSubtipo().length() == 0)){
			SqlCampos += ",ID_SERV_SUBTIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ServentiaSubtipo());
		}
		if (!(dados.getId_UsuarioServentiaGrupo().length() == 0)){
			SqlCampos += ",ID_USU_SERV_GRUPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioServentiaGrupo());
		}
		if (!(dados.getQuantidadeDistribuicao().length() == 0)){
			SqlCampos += ",QUANTIDADE_DIST ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getQuantidadeDistribuicao());
		}
		if ((dados.getDataInicialSubstituicao().length()>0)) {
			SqlCampos+= ",DATA_INICIAL_SUBSTITUICAO " ;
			SqlValores+= ",? " ;
			ps.adicionarDate(dados.getDataInicialSubstituicao());  
		}
		if ((dados.getDataFinalSubstituicao().length()>0)) {
			SqlCampos+= ",DATA_FINAL_SUBSTITUICAO " ;
			SqlValores+= ",? " ;
			ps.adicionarDate(dados.getDataFinalSubstituicao());  
		}
		
		if (!(dados.getCodigoTemp().length() == 0)){
			SqlCampos += ",CODIGO_TEMP ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCodigoTemp());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");
 					
		dados.setId(executarInsert(Sql, "ID_SERV_CARGO", ps));

	}

	/**
	 * Altera um ServentiaCargo passado
	 */
	public void alterar(ServentiaCargoDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.SERV_CARGO SET  ";
		Sql += "SERV_CARGO  = ? ";
		ps.adicionarString(dados.getServentiaCargo());
		Sql += ",ID_SERV  = ? ";
		ps.adicionarLong(dados.getId_Serventia());
		Sql += ",ID_CARGO_TIPO  = ? ";
		ps.adicionarLong(dados.getId_CargoTipo());
		Sql += ",ID_SERV_SUBTIPO  = ? ";
		ps.adicionarLong(dados.getId_ServentiaSubtipo());
		Sql += ",ID_USU_SERV_GRUPO  = ? ";
		ps.adicionarLong(dados.getId_UsuarioServentiaGrupo());
		Sql += ",QUANTIDADE_DIST  = ? ";
		ps.adicionarLong(dados.getQuantidadeDistribuicao());
		Sql += ",PRAZO_AGENDA  = ? ";
		ps.adicionarLong(dados.getPrazoAgenda());
		Sql += ",DATA_INICIAL_SUBSTITUICAO  = ? ";
		ps.adicionarDate(dados.getDataInicialSubstituicao()); 
		Sql += ",DATA_FINAL_SUBSTITUICAO  = ? ";
		ps.adicionarDate(dados.getDataFinalSubstituicao()); 
		Sql += ",CODIGO_TEMP  = ? ";
		ps.adicionarLong(dados.getCodigoTemp());
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_SERV_CARGO  = ? ";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(Sql, ps);

	}

	/**
	 * Atualiza os dados de um serventiaCargo em virtude de uma habilitação ou
	 * desabilitação de um usuário em um cargo
	 * 
	 * param id_ServentiaCargo, identificação do cargo na serventia
	 * param id_UsuarioServentiaGrupo, usuário que irá ocupar o cargo
	 * param status,status do cargo
	 * 
	 * author msapaula
	 */
	public void alterarUsuarioServentiaCargo(String id_ServentiaCargo, String id_UsuarioServentiaGrupo, String status) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.SERV_CARGO SET ID_USU_SERV_GRUPO = ? ";
		if(id_UsuarioServentiaGrupo == null || id_UsuarioServentiaGrupo.equalsIgnoreCase("null")) {
			ps.adicionarNull(0);
		} else {
			ps.adicionarLong(id_UsuarioServentiaGrupo);
		}
		Sql += ", CODIGO_TEMP = ? ";
		ps.adicionarLong(status);
		Sql += " WHERE ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * Consulta todos os serventia cargos de um usuario da serventia
	 * 
	 * author Ronneesley Moura Teles
	 * since 07/11/2008 15:14
	 * param String
	 *            idUsuarioServentia, id do usuario da serventia
	 * return List
	 * throws Exception
	 */
	public List consultarServentiasCargos(String idUsuarioServentia) throws Exception {
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_SERV_CARGO " +		              
		             " WHERE ID_USU_SERV = ? ";
		ps.adicionarLong(idUsuarioServentia);
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();

				this.associarDt(serventiaCargoDt, rs1);

				lista.add(serventiaCargoDt);
			}

			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return lista;
	}

	/**
	 * Retorna o Id_ServentiaCargo para um usuário de determinada serventia
	 */
	public String consultarServentiaCargo(String id_UsuarioServentia) throws Exception {
		String Sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT sc.ID_SERV_CARGO FROM PROJUDI.SERV_CARGO sc";
		Sql += " INNER JOIN PROJUDI.USU_SERV_GRUPO ug on sc.ID_USU_SERV_GRUPO=ug.ID_USU_SERV_GRUPO";
		Sql += " INNER JOIN PROJUDI.USU_SERV us on ug.ID_USU_SERV=us.ID_USU_SERV";
		Sql += " WHERE us.ID_USU_SERV=?";
		ps.adicionarLong(id_UsuarioServentia);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				stRetorno = rs1.getString("ID_SERV_CARGO");
			}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}

	public String consultarCargosServentiaJSON(String idServentia, String descricao, String posicao) throws Exception {
		
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO AS DESCRICAO2, NOME_USU AS DESCRICAO1 FROM PROJUDI.VIEW_SERV_CARGO WHERE ID_SERV = ?";
		ps.adicionarLong(idServentia);

		if (descricao != null && descricao.length() > 0){
			Sql += " AND NOME_USU LIKE ? ";
			ps.adicionarString("%"+descricao+"%");
		}

		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO WHERE ID_SERV = ?"; 
			if (descricao != null && descricao.length() > 0){
				Sql += " AND NOME_USU LIKE ? ";
			} 
			
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp;
	}
	
//	/**
//	 * Consulta os cargos que serão utilizados na distribuição de processos, de
//	 * acordo com o tipo de cargo passado
//	 * 
//	 * param id_serventia, identificação da serventia
//	 * param cargoTipoCodigo, código do cargo a ser consultado
//	 * return
//	 * 
//	 * author msapaula
//	 */
//	public List consultarServentiaCargosDistribuicao(String id_serventia, int grupoTipoCodigo){
//		String Sql;
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs1 = null;
//
//		Sql = "SELECT s.ID_SERV_CARGO, s.QUANTIDADE_DIST FROM PROJUDI.SERV_CARGO s";
//		Sql += " INNER JOIN PROJUDI.CARGO_TIPO ct on s.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
//		Sql += " LEFT JOIN PROJUDI.USU_SERV_GRUPO usg on s.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO";
//		Sql += " INNER JOIN PROJUDI.GRUPO g on g.ID_GRUPO = ct.ID_GRUPO";
//		Sql += " INNER JOIN PROJUDI.GRUPO_TIPO gt on g.ID_GRUPO_TIPO = gt.ID_GRUPO_TIPO AND gt.GrupoTipoCodigo = " + grupoTipoCodigo;
//		Sql += " WHERE s.QUANTIDADE_DIST > 0";
//		Sql += " AND s.ID_SERV=" + id_serventia;
//		Sql += " ORDER BY s.ID_SERV_CARGO";
//
//		try{
//			rs1 = consultar(Sql);
//			while (rs1.next()) {
//				ServentiaCargoDt obTemp = new ServentiaCargoDt();
//				obTemp.setId(rs1.getString("ID_SERV_CARGO"));
//				obTemp.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
//				liTemp.add(obTemp);
//			}
//		} finally {
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//		}
//		return liTemp;
//	}

	
	   /**
     * Consulta os cargos que serão utilizados na distribuição de processos, de
     * acordo com o tipo de cargo passado
     * 
     * param id_serventia, identificação da serventia
     * param cargoTipoCodigo, código do cargo a ser consultado
     * return
     * 
     * author jrcorrea
     */
	
	public String consultarServentiaCargosDistribuicao1Grau(String id_serventia, String id_processoTipo, int grupotipocodigo) throws Exception {
        String Sql;
        String stTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		

        Sql = "SELECT * FROM (";
        Sql += "SELECT  sc.ID_SERV_CARGO,";
        Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";
        Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";        
        Sql += "             WHERE p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_processoTipo);
		Sql += "             AND pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		  
		Sql += "               AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
		Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());        
        Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);     
        Sql += "         ) as Qtd, ";
        Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";		
        Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";        
        Sql += "             WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		
        Sql += "               AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
		Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());	                
        Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);             
        Sql += "         ) as QtdTotal, ";        
        Sql += "         (DBMS_RANDOM.RANDOM ) as ordem ";
        Sql += " FROM PROJUDI.SERV_CARGO sc ";                
        Sql += "         INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
        Sql += " WHERE sc.ID_SERV = ? "; ps.adicionarLong(id_serventia);
        Sql += "     AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
        Sql += "     AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
        Sql += "     ) TAB";
        Sql += "     ORDER BY  Qtd, QtdTotal, ordem";
        
        try{
            rs1 = consultar(Sql, ps);
            if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
            
        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return stTemp;
    }

	   /**
  * Consulta os cargos que serão utilizados na distribuição de processos, de
  * acordo com o tipo de cargo passado
  * 
  * param id_serventia, identificação da serventia
  * param cargoTipoCodigo, código do cargo a ser consultado
  * return
  * para cumpir o provimento 16/2012
  * author jrcorrea
  */
	
	public String consultarServentiaCargosDistribuicao1Grau(String id_serventia, int grupotipocodigo) throws Exception {
     String Sql;
     String stTemp = null;
     ResultSetTJGO rs1 = null;
     PreparedStatementTJGO ps = new PreparedStatementTJGO();
     
     Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		

     Sql = "SELECT * FROM (";
     Sql += "SELECT  sc.ID_SERV_CARGO,";
     Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";		
     Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";        
     Sql += "             WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		
     Sql += "               AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
		Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());	                
     Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);             
     Sql += "         ) as QtdTotal, ";        
     Sql += "         (DBMS_RANDOM.RANDOM ) as ordem ";
     Sql += " FROM PROJUDI.SERV_CARGO sc ";                
     Sql += "         INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
     Sql += " WHERE sc.ID_SERV = ? "; ps.adicionarLong(id_serventia);
     Sql += "     AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
     Sql += "     AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
     Sql += "     ) TAB";
     Sql += "     ORDER BY   QtdTotal, ordem";
     
     try{
         rs1 = consultar(Sql, ps);
         if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
         
     
     } finally{
         try{
             if (rs1 != null) rs1.close();
         } catch(Exception e) {
         }
     }
     return stTemp;
 }
	
	   /**
  * Consulta os cargos que serão utilizados na distribuição de processos, de
  * acordo com o tipo de cargo passado
  * 
  * param id_serventia_grupo, identificação o serventia grupo (atividade) 
  * return o id_serv_cargo de quem recebeu menos processo
  * 
  * author jrcorrea
  */
	
	public String consultarServentiaGrupoDistribuicao(String id_serventia_grupo) throws Exception {
     String Sql;
     String stTemp = null;
     ResultSetTJGO rs1 = null;
     PreparedStatementTJGO ps = new PreparedStatementTJGO();
     
     Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		
		
		Sql = " SELECT  sc.ID_SERV_CARGO, ";
		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP_HIST p ";
		Sql += "          WHERE p.DATA_INICIO >= ?"; ps.adicionarDateTime(cal.getTime());
		Sql += "              AND p.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 		  		                
		Sql += "          ) + ";
		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP_HIST_FINAL p1 ";
		Sql += "          WHERE p1.DATA_INICIO >= ?"; ps.adicionarDateTime(cal.getTime());
		Sql += "              AND p1.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 		  		                
		Sql += "          ) as Qtd, ";    
		Sql += "     (DBMS_RANDOM.RANDOM ) as ordem "; 
		Sql += "   FROM PROJUDI.SERV_CARGO sc ";                  
		Sql += "     	inner join projudi.Serv_cargo_Serv_Grupo scsg on sc.id_serv_cargo = scsg.id_serv_cargo ";                  
		Sql += "     WHERE scsg.ID_SERV_grupo = ? ";  ps.adicionarLong(id_serventia_grupo);
		Sql += "         AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);                         
		Sql += "     ORDER BY  Qtd,  ordem";     
     
     try{
         rs1 = consultar(Sql, ps);
         if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
         
     
     } finally{
         try{
             if (rs1 != null) rs1.close();
         } catch(Exception e) {
         }
     }
     return stTemp;
 }
	
 /**
* Consulta os cargos que serão utilizados na distribuição de pendencia de
* acordo com a serventia passada
* 
* param id_serventiao, identificação o serventia 
* return o id_serv_cargo de quem recebeu menos processo
* 
* author jrcorrea
* data 08/08/2018
*/
	
public String consultarServentiaCargoDistribuicao(String id_serventia) throws Exception {
  String Sql;
  String stTemp = null;
  ResultSetTJGO rs1 = null;
  PreparedStatementTJGO ps = new PreparedStatementTJGO();
  
  Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		
		
		Sql = " SELECT  sc.ID_SERV_CARGO, ";
		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP p ";
		Sql += "          WHERE  p.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 		  		                
		Sql += "          ) + ";
		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.pend_final_resp p1 ";
		Sql += "          WHERE  p1.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 		  		                
		Sql += "          ) as Qtd, ";    
		Sql += "     (DBMS_RANDOM.RANDOM ) as ordem "; 
		Sql += "   FROM PROJUDI.SERV_CARGO sc ";                  		                  
		Sql += "     WHERE sc.ID_SERV = ? "; 				ps.adicionarLong(id_serventia);
		Sql += "         AND sc.QUANTIDADE_DIST > ? "; 		ps.adicionarLong(0);                         
		Sql += "     ORDER BY  Qtd,  ordem";     
  
  try{
      rs1 = consultar(Sql, ps);
      if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
      
  
  } finally{
      try{
          if (rs1 != null) rs1.close();
      } catch(Exception e) {
      }
  }
  return stTemp;
}

/**
* Consulta os cargos que serão utilizados na distribuição de pendencia de
* acordo com a serventia passada e o tipo de pendencia
* 
* param id_serventiao, identificação o serventia 
* param pendenciaTipoCodigo, identificação do tipo de pendencia 
* return o id_serv_cargo de quem recebeu menos pendencia
* 
* author lsbernardes
* data 08/08/2018
*/
	
public String consultarServentiaCargoDistribuicao(String id_serventia, String pendenciaTipoCodigo) throws Exception {
 String Sql;
 String stTemp = null;
 ResultSetTJGO rs1 = null;
 PreparedStatementTJGO ps = new PreparedStatementTJGO();
 
 Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO_CENOPES);
		
		Sql  = " SELECT  ";
		Sql += "  sc.ID_SERV_CARGO, ";
		Sql += "  ( ";
		Sql += "      ( "; 
		Sql += "        SELECT "; 
		Sql += "          COUNT(unique pr.id_pend) as Qtd "; 
		Sql += "        FROM "; 
		Sql += "           PROJUDI.PEND_RESP pr ";
		Sql += "           INNER JOIN PEND p ON p.id_pend = pr.id_pend ";
		Sql += "           INNER JOIN PEND_TIPO pt ON pt.id_pend_tipo = p.id_pend_tipo ";
		Sql += "        WHERE p.DATA_INICIO >= ?"; ps.adicionarDateTime(cal.getTime());
		Sql += "           AND pr.ID_SERV_CARGO = sc.ID_SERV_CARGO AND PT.pend_tipo_codigo = ? "; ps.adicionarLong(pendenciaTipoCodigo);
		Sql += "      ) "; 
		Sql += "      + ";      
		Sql += "      ( "; 
		Sql += "        SELECT "; 
		Sql += "          COUNT(unique pfr.id_pend) as Qtd "; 
		Sql += "        FROM ";
		Sql += "          PROJUDI.pend_final_resp pfr ";
		Sql += "          INNER JOIN PEND_FINAL pf ON pf.id_pend = pfr.id_pend ";
		Sql += "          INNER JOIN PEND_TIPO pt ON pt.id_pend_tipo = pf.id_pend_tipo ";
		Sql += "        WHERE pf.DATA_INICIO >= ?"; ps.adicionarDateTime(cal.getTime());
		Sql += "          AND pfr.ID_SERV_CARGO = sc.ID_SERV_CARGO  AND PT.pend_tipo_codigo = ? "; ps.adicionarLong(pendenciaTipoCodigo);
		Sql += "      ) "; 
		Sql += "    ) as Qtd, ";      
		Sql += "    (DBMS_RANDOM.RANDOM ) as ordem ";    
		Sql += "  FROM "; 
		Sql += "    PROJUDI.SERV_CARGO sc ";
		Sql += "  WHERE "; 
		Sql += "    sc.ID_SERV = ? AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(id_serventia); ps.adicionarLong(0);
		Sql += "  ORDER BY "; 
		Sql += "    Qtd,  ordem ";
		
 try{
     rs1 = consultar(Sql, ps);
     if(rs1.next()) {
    	 stTemp = rs1.getString("ID_SERV_CARGO");  
     }
     
 } finally{
     try{
         if (rs1 != null) rs1.close();
     } catch(Exception e) {
     }
 }
 return stTemp;
}
		
	 /* Consulta os cargos que serão utilizados na distribuição de processos, de
	  * acordo com o tipo de cargo passado
	  * 
	  * param id_serventia, identificação da serventia
	  * param cargoTipoCodigo, código do cargo a ser consultado
	  * return
	  * 
	  * author jrcorrea
	  */
		
		public String consultarPrevensaoServentiaGrupo(String id_serventia_grupo, String id_pend) throws Exception {
		     String Sql;
		     String stTemp = null;
		     ResultSetTJGO rs1 = null;
		     PreparedStatementTJGO ps = new PreparedStatementTJGO();	     	    
				
			Sql = "select p.id_serv_cargo from  projudi.pend_resp_hist p where id_pend_resp_hist = ( select  max(p1.id_pend_resp_hist) from projudi.pend_resp_hist p1 where p1.id_serv_grupo = ? and p1.id_pend = ? )";  ps.adicionarLong(id_serventia_grupo); ps.adicionarLong(id_pend);			     
		     
		     try{
		         rs1 = consultar(Sql, ps);
		         if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
		         
		     
		     } finally{
		         try{
		             if (rs1 != null) rs1.close();
		         } catch(Exception e) {
		         }
		     }
		     return stTemp;
	 }
	  
	   /**
  * Consulta os cargos que serão utilizados na distribuição de processos, de
  * acordo com o tipo de cargo passado
  * 
  * param id_serventia, identificação da serventia
  * param cargoTipoCodigo, código do cargo a ser consultado
  * return
  * 
  * author jrcorrea
  */
	
	public String consultarServentiaCargosDistribuicaoTurma(String id_serventia, String id_processoTipo, int grupotipocodigo) throws Exception {
     String Sql;
     String stTemp = null;
     ResultSetTJGO rs1 = null;
     PreparedStatementTJGO ps = new PreparedStatementTJGO();
     
     Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		

     Sql = "SELECT * FROM (";
     Sql += "SELECT  sc.ID_SERV_CARGO,";
     Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";
     Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";     
     Sql += "             WHERE p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_processoTipo);
	 Sql += "               AND pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		  
	 Sql += "               AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
	 Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc) )";  ps.adicionarDate(cal.getTime());
	 Sql += "               AND pr.REDATOR = ? "; ps.adicionarLong(1);     
     Sql += "         ) as Qtd, ";
     Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";		
     Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";     
     Sql += "             WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		
     Sql += "             AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
     Sql += "                  OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc) )";  ps.adicionarDate(cal.getTime());	 
     Sql += "             AND pr.REDATOR = ? "; ps.adicionarLong(1);        
     //Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);             
     Sql += "         ) as QtdTotal, ";        
     Sql += "         (DBMS_RANDOM.RANDOM ) as ordem ";
     Sql += " FROM PROJUDI.SERV_CARGO sc ";                
     Sql += "         INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
     Sql += " WHERE sc.ID_SERV = ? "; ps.adicionarLong(id_serventia);
     Sql += "     AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
     Sql += "     AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
     Sql += "     ) TAB";
     Sql += "     ORDER BY  Qtd, QtdTotal, ordem";
     
     try{
         rs1 = consultar(Sql, ps);
         if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
         
     
     } finally{
         try{
             if (rs1 != null) rs1.close();
         } catch(Exception e) {
         }
     }
     return stTemp;
 }
	
	   /**
* Consulta os cargos que serão utilizados na distribuição de processos, de
* acordo com o tipo de cargo passado
* 
* param id_serventia, identificação da serventia
* param cargoTipoCodigo, código do cargo a ser consultado
* return
* 
* author jrcorrea
* 09/10/2013 para atender o provimento 16/2012 da CGJ
*/
	
	public String consultarServentiaCargosDistribuicaoTurma(String id_serventia, int grupotipocodigo) throws Exception {
  String Sql;
  String stTemp = null;
  ResultSetTJGO rs1 = null;
  PreparedStatementTJGO ps = new PreparedStatementTJGO();
  
  Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		

  Sql = "SELECT * FROM (";
  Sql += "SELECT  sc.ID_SERV_CARGO,";
  Sql += "         (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr ";		
  Sql += "             INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";     
  Sql += "             WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";		
  Sql += "             AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
  Sql += "                  OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc) )";  ps.adicionarDate(cal.getTime());	 
  Sql += "             AND pr.REDATOR = ? "; ps.adicionarLong(1);        
  //Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);             
  Sql += "         ) as QtdTotal, ";        
  Sql += "         (DBMS_RANDOM.RANDOM ) as ordem ";
  Sql += " FROM PROJUDI.SERV_CARGO sc ";                
  Sql += "         INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "         INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
  Sql += " WHERE sc.ID_SERV = ? "; ps.adicionarLong(id_serventia);
  Sql += "     AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
  Sql += "     AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
  Sql += "     ) TAB";
  Sql += "     ORDER BY  QtdTotal, ordem";
  
  try{
      rs1 = consultar(Sql, ps);
      if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
      
  
  } finally{
      try{
          if (rs1 != null) rs1.close();
      } catch(Exception e) {
      }
  }
  return stTemp;
}
	   /**
     * Consulta os cargos que serão utilizados na distribuição de processos, de
     * acordo com o tipo de cargo passado
     * 
     * param id_serventia, identificação da serventia
     * param cargoTipoCodigo, código do cargo a ser consultado
     * return
     * 
     * author jrcorrea
     */
	
	public ServentiaCargoDt consultarServentiaCargosDistribuicao2Grau(String id_serventia, String id_processoTipo, int grupotipocodigo) throws Exception {
        String Sql;        
        ResultSetTJGO rs1 = null;
        ServentiaCargoDt obTemp = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);	

        Sql = "SELECT * FROM (";
        Sql += " SELECT  sc.ID_SERV_CARGO,";
		Sql += "		(SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
		Sql += "			INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC"; 		
		Sql += " 			WHERE p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_processoTipo);
		Sql += "				AND pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += "			    AND pr.REDATOR = ? "; ps.adicionarLong(1);
		Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
        Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());		
		Sql += "		) as Qtd,";
		Sql += "        (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
        Sql += "            INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";         
        Sql += "            WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
        Sql += "                AND pr.REDATOR = ? "; ps.adicionarLong(1);          
        Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
        Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());
        Sql += "        ) as QtdTotal,"; 		
		Sql += "		DBMS_RANDOM.RANDOM as ordem ";
		Sql += "   FROM PROJUDI.SERV s ";
		Sql += "	INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC "; 
		Sql += "    INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV ";
        Sql += "    INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
		Sql += "      WHERE s.ID_SERV = ? "; ps.adicionarLong(id_serventia);
		Sql += "      AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
		Sql += "      AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
		Sql += "      AND sr.RECEBE_PROC = ? "; ps.adicionarBoolean(true); 
		Sql += "    ) TAB";
		Sql += "     ORDER BY  Qtd, QtdTotal, ordem";
        		              
        try{
            rs1 = consultar(Sql, ps);            
            if (rs1.next()) {
                obTemp = new ServentiaCargoDt();
                obTemp.setId(rs1.getString("ID_SERV_CARGO"));
                obTemp.setQuantidadeDistribuicao(rs1.getString("Qtd"));
                               
            }
        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return obTemp;
    }
	
	   /**
  * Consulta os cargos que serão utilizados na distribuição de processos, de
  * acordo com o tipo de cargo passado
  * 
  * param id_serventia, identificação da serventia
  * param cargoTipoCodigo, código do cargo a ser consultado
  * return
  * para cumprir o provimento 16/2012 da CGJ
  * 09/10/2013
  * author jrcorrea
  */
	
	public ServentiaCargoDt consultarServentiaCargosDistribuicao2Grau(String id_serventia,  int grupotipocodigo) throws Exception {
     String Sql;        
     ResultSetTJGO rs1 = null;
     ServentiaCargoDt obTemp = null;
     PreparedStatementTJGO ps = new PreparedStatementTJGO();
     
     Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);	

     Sql = "SELECT * FROM (";
     Sql += " SELECT  sc.ID_SERV_CARGO,";
     Sql += "        (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
     Sql += "            INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC ";         
     Sql += "            WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
     Sql += "                AND pr.REDATOR = ? "; ps.adicionarLong(1);          
     Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
     Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());
     Sql += "        ) as QtdTotal,"; 		
		Sql += "		DBMS_RANDOM.RANDOM as ordem ";
		Sql += "   FROM PROJUDI.SERV s ";
		Sql += "	INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC "; 
		Sql += "    INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV ";
     Sql += "    INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
		Sql += "      WHERE s.ID_SERV = ? ";		 	ps.adicionarLong(id_serventia);
		Sql += "      AND sc.QUANTIDADE_DIST > ? "; 	ps.adicionarLong(0);
		Sql += "      AND gt.GRUPO_TIPO_CODIGO = ? "; 	ps.adicionarLong(grupotipocodigo);
		Sql += "      AND sr.RECEBE_PROC = ? "; 		ps.adicionarBoolean(true); 
		Sql += "    ) TAB";
		Sql += "     ORDER BY  QtdTotal, ordem";
     		              
     try{
         rs1 = consultar(Sql, ps);            
         if (rs1.next()) {
             obTemp = new ServentiaCargoDt();
             obTemp.setId(rs1.getString("ID_SERV_CARGO"));
             obTemp.setQuantidadeDistribuicao(rs1.getString("QtdTotal"));
                            
         }
     
     } finally{
         try{
             if (rs1 != null) rs1.close();
         } catch(Exception e) {
         }
     }
     return obTemp;
 }
	
	/**
	 * Método que consulta outros relator, revisor e vogal dentro da mesma câmara para receber o processo.
	 * param id_serventia - ID da Servendia
	 * param Id_ProcessoTipo - Tipo de processo
	 * param grupoTipoCodigo - grupo tipo
	 * param id_RelatorAtual - id do relator atual do processo
	 * return lista de possíveis desembargadores que podem receber o processo
	 * throws Exception
	 * author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargosDistribuicao2GrauPropriaServentia(String id_serventia, String id_processoTipo, String grupotipocodigo, String cargoTipoCodigo, String id_RelatorAtual) throws Exception {
        String Sql;
        ServentiaCargoDt obTemp = null;        
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);	

        Sql = "SELECT * FROM (";
        Sql += " SELECT  sc.ID_SERV_CARGO,";
		Sql += "		(SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
		Sql += "			INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC"; 		
		Sql += " 			WHERE p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_processoTipo);
		Sql += "				AND pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += "                AND pr.CODIGO_TEMP = ? ";	ps.adicionarLong(0);		
		Sql += "			    AND ct.CARGO_TIPO_CODIGO = ? "; ps.adicionarLong(cargoTipoCodigo);
		Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
        Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());		
		Sql += "		) as Qtd,";
		Sql += "        (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
        Sql += "            INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC"; 		
        Sql += "            INNER JOIN PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO";        
        Sql += "            WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
        Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);
        Sql += "                AND ct.CARGO_TIPO_CODIGO = ? "; ps.adicionarLong(cargoTipoCodigo);
        Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
        Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());       
        Sql += "        ) as QtdTotal,"; 		
		Sql += "		DBMS_RANDOM.RANDOM as ordem ";
		Sql += " FROM PROJUDI.SERV s ";
		Sql += "	INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC ";		
		Sql += "    INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV ";
        Sql += "    INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
		Sql += "      WHERE s.ID_SERV = ? "; ps.adicionarLong(id_serventia);
		Sql += "      AND sc.ID_SERV_CARGO <> ? "; ps.adicionarLong(id_RelatorAtual);
		Sql += "      AND sc.QUANTIDADE_DIST > ? ";  ps.adicionarLong(0);
		Sql += "      AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
		Sql += "      AND sr.RECEBE_PROC = ? "; ps.adicionarBoolean(true);
		Sql += "    ) TAB";
		Sql += "     ORDER BY  Qtd, QtdTotal, ordem";
        		              
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaCargoDt();
                obTemp.setId(rs1.getString("ID_SERV_CARGO"));
                obTemp.setQuantidadeDistribuicao(rs1.getString("Qtd"));                
            }
        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return obTemp;
    }

	/**
	 * Método que consulta outros relator, revisor e vogal dentro da mesma câmara para receber o processo.
	 * param id_serventia - ID da Servendia
	 * param Id_ProcessoTipo - Tipo de processo
	 * param grupoTipoCodigo - grupo tipo
	 * param id_RelatorAtual - id do relator atual do processo
	 * return lista de possíveis desembargadores que podem receber o processo
	 * throws Exception
	 * author jrcorrea
	 * 09/10/2013 para atender o provimento 16/2012 da CGJ
	 */
	public ServentiaCargoDt consultarServentiaCargosDistribuicao2GrauPropriaServentia(String id_serventia, String grupotipocodigo, String cargoTipoCodigo, String id_RelatorAtual) throws Exception {
        String Sql;
        ServentiaCargoDt obTemp = null;        
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);	

        Sql = "SELECT * FROM (";
        Sql += " SELECT  sc.ID_SERV_CARGO,";
		Sql += "        (SELECT COUNT(*) as Qtd FROM  PROJUDI.PROC_RESP pr"; 
        Sql += "            INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC"; 		
        Sql += "            INNER JOIN PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO";        
        Sql += "            WHERE pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
        Sql += "                AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(0);
        Sql += "                AND ct.CARGO_TIPO_CODIGO = ? "; ps.adicionarLong(cargoTipoCodigo);
        Sql += "                AND (p.DATA_RECEBIMENTO >= ? "; ps.adicionarDate(cal.getTime());
        Sql += "                    OR ? <= (SELECT MAX(r.data_recebimento) FROM recurso r where r.id_proc=p.id_proc and r.data_retorno is null) )";  ps.adicionarDate(cal.getTime());       
        Sql += "        ) as QtdTotal,"; 		
		Sql += "		DBMS_RANDOM.RANDOM as ordem ";
		Sql += " FROM PROJUDI.SERV s ";
		Sql += "	INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC ";		
		Sql += "    INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV ";
        Sql += "    INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO= g.ID_GRUPO ";
		Sql += "    INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO ";
		Sql += "      WHERE s.ID_SERV = ? "; ps.adicionarLong(id_serventia);
		Sql += "      AND sc.ID_SERV_CARGO <> ? "; ps.adicionarLong(id_RelatorAtual);
		Sql += "      AND sc.QUANTIDADE_DIST > ? ";  ps.adicionarLong(0);
		Sql += "      AND gt.GRUPO_TIPO_CODIGO = ? "; ps.adicionarLong(grupotipocodigo);
		Sql += "      AND sr.RECEBE_PROC = ? "; ps.adicionarBoolean(true);
		Sql += "    ) TAB";
		Sql += "     ORDER BY  QtdTotal, ordem";
        		              
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaCargoDt();
                obTemp.setId(rs1.getString("ID_SERV_CARGO"));
                obTemp.setQuantidadeDistribuicao(rs1.getString("QtdTotal"));                
            }
        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return obTemp;
    }
/**
	 * Consulta os cargos que serão utilizados na distribuição de processos no segundo grau.
	 * Irá consultar os cargos das serventias relacionadas que são do tipo Gabinete, retornando os cargos dessas que 
	 * são do tipo Desembargador.
	 * param id_serventia, identificação da serventia
	 * author msapaula
	 */
//	 public List consultarServentiaCargosDistribuicaoSegundoGrau(String id_serventia){
//		String sql;
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		sql = "SELECT * FROM PROJUDI.SERV_RELACIONADA sr";
//		sql += " INNER JOIN PROJUDI.SERV s on sr.ID_SERV_REL = s.ID_SERV";
//		sql += " INNER JOIN PROJUDI.SERV_SUBTIPO sst on (s.ID_SERV_SUBTIPO = sst.ID_SERV_SUBTIPO";
//		sql += " 	AND sst.SERV_SUBTIPO_CODIGO = ?)";
//		ps.adicionarLong(ServentiaSubtipoDt.GABINETE);
//		sql += " INNER JOIN PROJUDI.SERV_CARGO sc on s.ID_SERV = sc.ID_SERV";
//		sql += " INNER JOIN PROJUDI.CARGO_TIPO ct on (sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
//		sql += " 	AND ct.CARGO_TIPO_CODIGO = ?)";
//		ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);
//		sql += " WHERE sc.QUANTIDADE_DIST > 0";
//		sql += " AND sr.ID_SERV_PRINC = ? ";
//		ps.adicionarLong(id_serventia);
//
//		try{
//			rs1 = consultar(sql, ps);
//			while (rs1.next()) {
//				ServentiaCargoDt obTemp = new ServentiaCargoDt();
//				obTemp.setId(rs1.getString("ID_SERV_CARGO"));
//				obTemp.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
//				liTemp.add(obTemp);
//			}
//		} finally{
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//		}
//		return liTemp;
//	}

	/**
	 * Consulta todos os cargos cadastrados para uma determinada serventia.
	 * Retorna tanto os cargos ocupados quanto os vazios
	 * 
	 * param id_serventia,
	 *            identificação da serventia
	 * param usuarioNe,
	 *            usuário logado para permitir geração de Hash - campo não obrigatório
	 * author msapaula
	 */
	public List consultarServentiaCargos(String id_Serventia, UsuarioNe usuarioNe) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_SERV_CARGO sc ";
		if (id_Serventia != null) {
			Sql += " WHERE sc.ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		Sql += " ORDER BY sc.ID_SERV, sc.ID_CARGO_TIPO, sc.SERV_CARGO";

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt obTemp = new ServentiaCargoDt();
				this.associarDt(obTemp, rs1);
				if (usuarioNe != null) {
					obTemp.setHash(usuarioNe.getCodigoHash(obTemp.getId()));
				}
				liTemp.add(obTemp);
			}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta todos os cargos cadastrados nas serventias relacionadas a uma determinada serventia.
	 * Retorna tanto os cargos ocupados quanto os vazios
	 * param usuarioNe,
	 *            usuário logado
	 * author hmgodinho
	 */
	public List consultarCargosServentiasRelacionadas(List listaServentias) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		for (int i = 0; i < listaServentias.size(); i++) {
			ServentiaDt serventia = (ServentiaDt) listaServentias.get(i);

			ps.limpar();
			Sql = "SELECT * FROM PROJUDI.VIEW_SERV_CARGO sc ";
			if (serventia.getId() != null) {
				Sql += " WHERE sc.ID_SERV= ? ";
				ps.adicionarLong(serventia.getId());
			}
			Sql += " ORDER BY sc.ID_SERV, sc.ID_CARGO_TIPO, sc.SERV_CARGO";

			try{
				rs1 = consultar(Sql, ps);
				while (rs1.next()) {
					ServentiaCargoDt obTemp = new ServentiaCargoDt();
					this.associarDt(obTemp, rs1);
					liTemp.add(obTemp);
				}
						
			}finally{
				try{
					if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
		}
		return liTemp;
	}

	/**
	 * Sobrescrevendo método para retornar campos Serventia e Grupo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV_CARGO, SERV_CARGO, SERV, CARGO_TIPO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		SqlFrom += " WHERE SERV_CARGO LIKE ? ";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY SERV_CARGO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ServentiaCargoDt obTemp = new ServentiaCargoDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setCargoTipo(rs1.getString("CARGO_TIPO"));
				liTemp.add(obTemp);
			}
			// rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())  liTemp.add(rs2.getLong("QUANTIDADE"));
			
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta os cargos cadastrados para uma determinada serventia
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do cargo
	 * param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * param id_Serventia,
	 *            identificação da serventia
	 * param cargoTipoCodigo,
	 *            tipo de cargo a ser filtrado
	 * 
	 * author Keila Sousa Silva, msapaula
	 * since 08/10/2008
	 */
	public List consultarServentiaCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoTipoCodigo) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaServentiaCargos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_SERV_CARGO, ID_USU_SERV, SERV_CARGO, ID_SERV, SERV, ID_CARGO_TIPO, CARGO_TIPO, NOME_USU, CARGO_TIPO_CODIGO ";		
		sqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		sqlFrom += " WHERE SERV_CARGO LIKE ?  ";
		ps.adicionarString( nomeBusca +"%");
		if (id_Serventia != null && !id_Serventia.equals("")){
			sqlFrom += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		if (grupoTipoCodigo != null && !grupoTipoCodigo.equals("")){
			sqlFrom += " AND GRUPO_TIPO_USU_CODIGO = ? ";
			ps.adicionarLong(grupoTipoCodigo);
		}
		sqlOrder = " ORDER BY SERV_CARGO ";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargos.add(serventiaCargoDt);
			}

			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs12 = consultar(sql + sqlFrom, ps);
			if (rs12.next())  listaServentiaCargos.add(rs12.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs12 != null) rs12.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargos;
	}
	
	/**
	 * Consulta os cargos cadastrados para uma determinada serventia
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do cargo
	 * param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * param id_Serventia,
	 *            identificação da serventia
	 * param cargoTipoCodigo,
	 *            tipo de cargo a ser filtrado
	 * 
	 * author leandro bernardes
	 */
	public List consultarServentiaCargosGrupo(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoCodigo) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaServentiaCargos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_SERV_CARGO, ID_USU_SERV, SERV_CARGO, ID_SERV, SERV, ID_CARGO_TIPO, CARGO_TIPO, NOME_USU, CARGO_TIPO_CODIGO ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		sqlFrom += " WHERE SERV_CARGO LIKE ?  ";
		ps.adicionarString( nomeBusca +"%");
		if (id_Serventia != null && !id_Serventia.equals("")){
			sqlFrom += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		if (grupoCodigo != null && !grupoCodigo.equals("")){
			sqlFrom += " AND GRUPO_USU_CODIGO = ? ";
			ps.adicionarLong(grupoCodigo);
		}
		sqlOrder = " ORDER BY SERV_CARGO ";		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargos.add(serventiaCargoDt);
			}

			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs12 = consultar(sql + sqlFrom, ps);
			if (rs12.next())  listaServentiaCargos.add(rs12.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs12 != null) rs12.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargos;
	}

	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passados.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param cargoTipoCodigo, código do cargo para o qual será retornado o usuário
	 * 
	 * author msapaula
	 */
	public ServentiaCargoDt consultarServentiaCargo(String id_Serventia, int cargoTipoCodigo, boolean temDistribuicao) throws Exception {
		String Sql;
		ServentiaCargoDt dtRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV= ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND sc.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(cargoTipoCodigo);
		
		if (temDistribuicao){
			Sql += " AND sc.QUANTIDADE_DIST > ? ";
			ps.adicionarLong(0);
		}

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				dtRetorno = new ServentiaCargoDt();
				dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
				dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
				dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
			}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return dtRetorno;
	}

	/**
	 * Retorna o Id_UsuarioServentia que está ocupando um cargo na serventia
	 * 
	 * param id_Serventia,
	 *            identificação da serventia
	 * param cargoTipoCodigo,
	 *            código do cargo para o qual será retornado o usuário
	 * 
	 * author msapaula
	 */
	public String consultarUsuarioServentiaCargo(String id_Serventia, int cargoTipoCodigo) throws Exception {
		String Sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT sc.ID_USU_SERV FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND sc.CARGO_TIPO_CODIGO= ? ";
		ps.adicionarLong(cargoTipoCodigo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_USU_SERV");
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}

	/**
	 * Consulta os tipos de cargos cadastrados para uma determinada serventia
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do tipo de cargo
	 * param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * param id_Serventia,
	 *            identificação da serventia
	 * 
	 * author msapaula
	 */
	public List consultarTiposCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String sql;
		List listaCargos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT DISTINCT ID_CARGO_TIPO, CARGO_TIPO FROM PROJUDI.VIEW_SERV_CARGO WHERE ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		sql += " ORDER BY CARGO_TIPO ";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				CargoTipoDt cargoTipoDt = new CargoTipoDt();
				cargoTipoDt.setId(rs1.getString("ID_CARGO_TIPO"));
				cargoTipoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				listaCargos.add(cargoTipoDt);
			}
			//rs1.close();
			sql = "SELECT COUNT(DISTINCT ID_CARGO_TIPO) as QUANTIDADE FROM PROJUDI.VIEW_SERV_CARGO WHERE ID_SERV = ? ";
			rs12 = consultar(sql, ps);
			if (rs12.next()) listaCargos.add(rs12.getLong("QUANTIDADE"));

			//rs12.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs12 != null) rs12.close();
			} catch(Exception e) {
			}
		}
		return listaCargos;
	}

	/**
	 * Método responsável por consultar os cargos de uma serventia, para o qual
	 * podem sem criadas agendas de audiências
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do cargo
	 * param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * param id_Serventia,
	 *            identificação da serventia
	 * 
	 * author Keila Sousa Silva, msapaula
	 */
	public List consultarServentiaCargosAgendaAudiencia(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String sql;
		List listaServentiaCargos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_SERV_CARGO, SERV_CARGO, ID_SERV, SERV, ID_CARGO_TIPO, CARGO_TIPO, NOME_USU ";
		sql += " FROM PROJUDI.VIEW_SERV_CARGO";
		sql += " WHERE SERV_CARGO LIKE ?";
		ps.adicionarString( nomeBusca +"%");
		sql += " AND (GRUPO_TIPO_USU_CODIGO = ? OR GRUPO_TIPO_USU_CODIGO = ?)";
		ps.adicionarLong(GrupoTipoDt.CONCILIADOR_VARA);
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU);
		if (id_Serventia != null && !id_Serventia.equals("")){
			sql += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		sql += " ORDER BY SERV_CARGO ";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargos.add(serventiaCargoDt);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.VIEW_SERV_CARGO WHERE SERV_CARGO LIKE ? ";
			sql += " AND (GRUPO_TIPO_USU_CODIGO = ? OR GRUPO_TIPO_USU_CODIGO = ?)";
			if (id_Serventia != null && !id_Serventia.equals("")) sql += " AND ID_SERV = ? ";
			rs12 = consultar(sql,ps);
			if (rs12.next()) listaServentiaCargos.add(rs12.getLong("QUANTIDADE"));
			//rs12.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs12 != null) rs12.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargos;
	}
	
	/**
	 * Consulta os cargos cadastrados do tipo desembargador relacionados aos gabinetes vinculados à câmara
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do cargo
	 * param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * param id_ServentiaCamara,
	 *            identificação da serventia 
	 * 
	 * author mmgomes
	 * since 16/12/2010
	 */
	public List consultarServentiaCargosDesembargadores(String nomeBusca, String posicaoPaginaAtual, String id_ServentiaCamara) throws Exception {
		String sql = "";
		String sqlComun = "";		
		List listaServentiaCargosDesembargadores = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, sc.ID_CARGO_TIPO, ct.CARGO_TIPO, ct.CARGO_TIPO_CODIGO, vsc.NOME_USU,";
		sql += " (SELECT SERV";
		sql += "    FROM PROJUDI.SERV s";
		sql += "   WHERE s.ID_SERV = sc.ID_SERV";
		sql += "   AND ROWNUM = 1) as SERV";		
		sqlComun = " FROM (PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO)";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE gt.GRUPO_TIPO_CODIGO = ? ";
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		if (id_ServentiaCamara != null & id_ServentiaCamara.trim().length() > 0) {
			sqlComun += " AND s.ID_SERV = ? ";
			ps.adicionarLong(id_ServentiaCamara);
		}
		if (nomeBusca != null & nomeBusca.trim().length() > 0){
			sqlComun += " AND NOME_USU LIKE ? ";
			ps.adicionarString("%" + nomeBusca.replace("'", "") + "%");
		}
				
		sql += sqlComun;
		sql += " ORDER BY SERV_CARGO, NOME_USU";		
		try{
			rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargosDesembargadores.add(serventiaCargoDt);
			}

			rs12 = consultar("SELECT COUNT(1) as QUANTIDADE " + sqlComun, ps);
			if (rs12.next()) listaServentiaCargosDesembargadores.add(rs12.getLong("QUANTIDADE"));
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs12 != null) rs12.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargosDesembargadores;
	}
	
	/**
	 * Consulta os serventia cargo dos juízes cadastrados nos gabinetes vinculados a uma UPJ.
	 * @param id_Serventia: identificação da serventia UPJ
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarServentiaCargosJuizUPJ(String idServentia) throws Exception {
		String sql = "";
		String sqlComun = "";		
		List listaServentiaCargosJuizes = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql += "SELECT DISTINCT vsc.ID_SERV_CARGO, vsc.SERV_CARGO, vsc.NOME_USU, vsc.ID_SERV, vsc.SERV, vsc.CARGO_TIPO, vsc.QUANTIDADE_DIST,";
		sql += " (SELECT SERV";
		sql += "    FROM PROJUDI.SERV s";
		sql += "   WHERE s.ID_SERV = sc.ID_SERV";
		sql += "   AND ROWNUM = 1) as SERV";		
		sqlComun = " FROM (PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO)";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE ct.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
		if (idServentia != null & idServentia.trim().length() > 0) {
			sqlComun += " AND s.ID_SERV = ? ";
			ps.adicionarLong(idServentia);
		}

		sql += sqlComun;
		sql += " ORDER BY vsc.SERV, NOME_USU";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
				listaServentiaCargosJuizes.add(serventiaCargoDt);
			}

		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargosJuizes;
	}
	
	/**
	 * Consulta os juízes cadastrados nas Turmas ou 1º grau
	 * 
	 * @param id_Serventia: identificação da serventia 
	 * @param id_ServentiaSubTipo: identificação do subtipo da serventia
	 * 
	 * @author lsbernardes, hmgodinho
	 */
	public List consultarServentiaCargosJuizes(String id_serventia, int id_ServentiaSubTipo) throws Exception {
		String sql = "";
		List listaServentiaCargosJuizes = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.CARGO_TIPO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.QUANTIDADE_DIST FROM PROJUDI.VIEW_SERV_CARGO sc";
		sql += " WHERE sc.ID_SERV= ? ";
		ps.adicionarLong(id_serventia);
		//Se o ServentiaSubtipo for de Turma, buscar apenas relator
		if(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL == id_ServentiaSubTipo 
				|| ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL == id_ServentiaSubTipo 
				|| ServentiaSubtipoDt.UPJ_TURMA_RECURSAL == id_ServentiaSubTipo
				|| ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL == id_ServentiaSubTipo) {
			sql += " AND sc.CARGO_TIPO_CODIGO = ? ";
			ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		} else {
			//se não for turma, será de 1º grau, logo deve buscar juiz de 1 grau
			sql += " AND sc.CARGO_TIPO_CODIGO in (?,?,?,?) ";
			ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
			ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO);
			ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);
			ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL);
			
		}		
		sql += " AND sc.ID_USU_SERV_GRUPO IS NOT NULL ";
		sql += " ORDER BY SERV_CARGO, NOME_USU";

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
				listaServentiaCargosJuizes.add(serventiaCargoDt);
			}	

			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaServentiaCargosJuizes;
	}
	
	/**
	 * Consulta todos os cargos cadastrados do tipo desembargador relacionados aos gabinetes vinculados à câmara
	 * 
	 * param nomeBusca,
	 *            filtro para descrição do cargo 
	 * param id_ServentiaCamara,
	 *            identificação da serventia 
	 * 
	 * author mmgomes
	 * since 24/11/2011
	 */
	public List consultarServentiaCargosDesembargadores(String nomeBusca, String id_ServentiaCamara) throws Exception {
		String sql = "";
		String sqlComun = "";		
		List listaServentiaCargosDesembargadores = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, sc.ID_CARGO_TIPO, ct.CARGO_TIPO, ct.CARGO_TIPO_CODIGO, vsc.NOME_USU,";
		sql += " (SELECT SERV";
		sql += "    FROM PROJUDI.SERV s";
		sql += "   WHERE s.ID_SERV = sc.ID_SERV";
		sql += "   AND ROWNUM = 1) as SERV";		
		sqlComun = " FROM (PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO)";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE gt.GRUPO_TIPO_CODIGO = ? ";
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		if (id_ServentiaCamara != null & id_ServentiaCamara.trim().length() > 0) {
			sqlComun += " AND s.ID_SERV = ? ";
			ps.adicionarLong(id_ServentiaCamara);
		}
		if (nomeBusca != null & nomeBusca.trim().length() > 0){
			sqlComun += " AND NOME_USU LIKE ? ";
			ps.adicionarString("%" + nomeBusca.replace("'", "") + "%");
		}
				
		sql += sqlComun;
		sql += " ORDER BY SERV_CARGO, NOME_USU";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargosDesembargadores.add(serventiaCargoDt);
			}
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}			
		}
		return listaServentiaCargosDesembargadores;
	}

	
	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que tenha quantidade de distribuição informada.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param cargoTipoCodigo, código do cargo para o qual será retornado o usuário
	 * 
	 * author mmgomes
	 */
	public ServentiaCargoDt consultarServentiaCargoComQuantidadeDistribuicao(String id_Serventia, int cargoTipoCodigo) throws Exception {
		String Sql;
		ServentiaCargoDt dtRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV= ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND sc.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(cargoTipoCodigo);
		Sql += " AND sc.QUANTIDADE_DIST >= ? ";
		ps.adicionarLong(1);		

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				dtRetorno = new ServentiaCargoDt();
				dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
				dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
				dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
				dtRetorno.setId_Serventia(rs1.getString("ID_SERV"));
				dtRetorno.setServentia(rs1.getString("SERV"));
				dtRetorno.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dtRetorno;
	}

	
	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param cargoTipoCodigo, código do cargo para o qual será retornado o usuário
	 * 
	 * author jvosantos - Correção emergencial Sessão Virtual
	 */
	public ServentiaCargoDt consultarServentiaCargo(String id_Serventia, int cargoTipoCodigo) throws Exception {
		String Sql;
		ServentiaCargoDt dtRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV= ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND sc.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(cargoTipoCodigo);
		Sql += " ORDER BY QUANTIDADE_DIST DESC";

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				dtRetorno = new ServentiaCargoDt();
				dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
				dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
				dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
				dtRetorno.setId_Serventia(rs1.getString("ID_SERV"));
				dtRetorno.setServentia(rs1.getString("SERV"));
				dtRetorno.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dtRetorno;
	}

	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que tenha quantidade de distribuição informada
	 * assim como período de substituição.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param cargoTipoCodigo, código do cargo para o qual será retornado o usuário
	 * 
	 * author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoSubstituicao(String id_Serventia, String codServentiaSubTipo, int cargoTipoCodigo) throws Exception {
		String Sql;
		ServentiaCargoDt dtRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.SERV FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV= ? ";																				ps.adicionarLong(id_Serventia);
		Sql += " AND sc.SERV_SUBTIPO_CODIGO = ? ";																	ps.adicionarLong(codServentiaSubTipo);
		Sql += " AND sc.CARGO_TIPO_CODIGO = ? ";																	ps.adicionarLong(cargoTipoCodigo);
		Sql += " AND SYSDATE >= sc.DATA_INICIAL_SUBSTITUICAO ";
		Sql += " AND SYSDATE < (sc.data_final_substituicao + 1) ";
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				dtRetorno = new ServentiaCargoDt();
				dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
				dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
				dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
				dtRetorno.setServentia(rs1.getString("SERV"));
				dtRetorno.setId_Serventia(id_Serventia);
				dtRetorno.setServentiaSubtipoCodigo(codServentiaSubTipo);
				dtRetorno.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que NÃO tenha informado período de substituição.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param cargoTipoCodigo, código do cargo para o qual será retornado o usuário
	 * 
	 * author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoNaoSubstituicao(String id_Serventia, int cargoTipoCodigo) throws Exception {
		String Sql;
		ServentiaCargoDt dtRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV FROM PROJUDI.VIEW_SERV_CARGO sc";
		Sql += " WHERE sc.ID_SERV= ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND sc.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(cargoTipoCodigo);
		Sql += " AND sc.DATA_INICIAL_SUBSTITUICAO is null ";
		Sql += " AND sc.DATA_FINAL_SUBSTITUICAO is null ";

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				dtRetorno = new ServentiaCargoDt();
				dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
				dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
				dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
				dtRetorno.setId_Serventia(rs1.getString("ID_SERV"));
				dtRetorno.setServentia(rs1.getString("SERV"));
				dtRetorno.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que tenha quantidade de distribuição informada
	 * assim como período de substituição.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param id_Processo, identificação do processo
	 * 
	 * author lsbernardes
	 */
	public String getDesembargadorResponsavel(String id_Serventia, String id_Processo) throws Exception {
		String Sql;
		String id_ServentiaCargo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT pr.id_serv_cargo FROM proc_resp pr ";
		Sql += " INNER JOIN serv_cargo sc on pr.id_serv_cargo = sc.id_serv_cargo ";
		Sql += " INNER JOIN  usu_serv_grupo usg on sc.id_usu_serv_grupo = usg.id_usu_serv_grupo ";
		Sql += " INNER JOIN  grupo g on usg.id_grupo = g.id_grupo ";
		
		Sql += " WHERE pr.id_proc = ? and g.grupo_codigo = ? and sc.id_serv = ? and pr.codigo_temp <> ? ";
		ps.adicionarLong(id_Processo); ps.adicionarLong(GrupoDt.DESEMBARGADOR); 
		ps.adicionarLong(id_Serventia); ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				id_ServentiaCargo = rs1.getString("id_serv_cargo");
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return id_ServentiaCargo;
	}
	
	/**
	 * Método que localiza o último relator a receber o processo naquela serventia. 
	 * 
	 * @param id_Serventia - ID da serventia do processo
	 * @param id_Processo - ID do processo
	 * @param id_areaDistribuicao - ID da área de distribuição
	 * @return ID do serv cargo do relator
	 * @throws Exception
	 * 
	 * @author hmgodinho
	 */
	public String getUltimoRelatorProcesso(String id_Serventia, String id_Processo, String id_areaDistribuicao) throws Exception {
		String Sql;
		String id_ServentiaCargo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT id_ponteiro_log, id_serv_cargo ";
		Sql += " FROM ponteiro_log ";
		Sql += " WHERE id_proc = ? AND id_area_dist = ? AND id_serv = ? ";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(id_areaDistribuicao); 
		ps.adicionarLong(id_Serventia);
		Sql += " ORDER BY id_ponteiro_log DESC ";
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				id_ServentiaCargo = rs1.getString("id_serv_cargo");
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return id_ServentiaCargo;
	}
	
	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que tenha quantidade de distribuição informada
	 * assim como período de substituição.
	 * 
	 * param id_Serventia, identificação da serventia
	 * 
	 * author lsbernardes
	 */
	public String getDesembargadorServentia(String id_Serventia) throws Exception {
		String Sql;
		String id_ServentiaCargo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT sc.id_serv_cargo FROM serv_cargo sc ";
		Sql += " INNER JOIN  usu_serv_grupo usg on sc.id_usu_serv_grupo = usg.id_usu_serv_grupo ";
		Sql += " INNER JOIN  grupo g on usg.id_grupo = g.id_grupo ";
		
		Sql += " WHERE g.grupo_codigo = ? and sc.id_serv = ?";
		ps.adicionarLong(GrupoDt.DESEMBARGADOR); 
		ps.adicionarLong(id_Serventia);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				id_ServentiaCargo = rs1.getString("id_serv_cargo");
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return id_ServentiaCargo;
	}
	
	public String consultarServentiaCargosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO AS DESCRICAO1, SERV AS DESCRICAO4, CARGO_TIPO AS DESCRICAO2, NOME_USU AS DESCRICAO3 ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		sqlFrom += " WHERE SERV_CARGO LIKE ?  ";
		ps.adicionarString( nomeBusca +"%");
		if (id_Serventia != null && !id_Serventia.equals("")){
			sqlFrom += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		sqlOrder = " ORDER BY SERV_CARGO ";		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicaoPaginaAtual);
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO AS DESCRICAO1, SERV AS DESCRICAO4, CARGO_TIPO AS DESCRICAO2, NOME_USU AS DESCRICAO3 ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		sqlFrom += " WHERE SERV_CARGO LIKE ?  ";
		ps.adicionarString( nomeBusca +"%");
		if (id_Serventia != null && !id_Serventia.equals("")){
			sqlFrom += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		
		sqlFrom += " AND CARGO_TIPO_CODIGO in (?,?,?,?,?,?,?) ";
		ps.adicionarLong(CargoTipoDt.PRESIDENTE_TURMA); ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);ps.adicionarLong(CargoTipoDt.JUIZ_CORREGEDOR);
		ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);ps.adicionarLong(CargoTipoDt.JUIZ_AUXILIAR_PRESIDENCIA);ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
		
		sqlOrder = " ORDER BY SERV_CARGO ";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom, ps, posicaoPaginaAtual);
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}

	public String consultarServentiaGrupoReDistribuicao(String id_serventia_grupo, String id_serventia_cargo) throws Exception {
	     String Sql;
	     String stTemp = null;
	     ResultSetTJGO rs1 = null;
	     PreparedStatementTJGO ps = new PreparedStatementTJGO();
	     
	     Calendar cal = Calendar.getInstance();		
			cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		
			
			Sql = " SELECT  sc.ID_SERV_CARGO, ";
			Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP_HIST p ";
			Sql += "          WHERE p.DATA_INICIO >= ?"; ps.adicionarDate(cal.getTime());
			Sql += "              AND p.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 		  		                
			Sql += "          ) as Qtd, ";    
			Sql += "     (DBMS_RANDOM.RANDOM ) as ordem "; 
			Sql += "   FROM PROJUDI.SERV_CARGO sc ";                  
			Sql += "     	inner join projudi.Serv_cargo_Serv_Grupo scsg on sc.id_serv_cargo = scsg.id_serv_cargo ";                  
			Sql += "     WHERE scsg.ID_SERV_grupo = ? ";  ps.adicionarLong(id_serventia_grupo);
			Sql += "         AND scsg.ID_SERV_CARGO <> ? ";  ps.adicionarLong(id_serventia_cargo);
			Sql += "         AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);                         
			Sql += "     ORDER BY  Qtd,  ordem";     
	     
	     try{
	         rs1 = consultar(Sql, ps);
	         if(rs1.next()) stTemp = rs1.getString("ID_SERV_CARGO");                
	         
	     
	     } finally{
	         try{
	             if (rs1 != null) rs1.close();
	         } catch(Exception e) {
	         }
	     }
	     return stTemp;
	}
	
	public String consultarServentiaCargosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoTipoCodigo) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO || ' [' || SERV || ']' AS DESCRICAO1, NOME_USU AS DESCRICAO2, CARGO_TIPO AS DESCRICAO3 ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO";
		sqlFrom += " WHERE SERV_CARGO LIKE ?  ";															ps.adicionarString( nomeBusca +"%");
		if (id_Serventia != null && !id_Serventia.equals("")){
			sqlFrom += " AND ID_SERV = ? ";																	ps.adicionarLong(id_Serventia);
		}
		if (grupoTipoCodigo != null && !grupoTipoCodigo.equals("")){
			sqlFrom += " AND GRUPO_TIPO_USU_CODIGO = ? ";													ps.adicionarLong(grupoTipoCodigo);
		}
		sqlOrder = " ORDER BY SERV_CARGO ";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicaoPaginaAtual);

			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiaCargosJuizUPJJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String sql = "";
		String sqlComun = "";		
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO AS ID, sc.SERV_CARGO  || ' [' || vsc.serv || ']' AS DESCRICAO1, vsc.NOME_USU  AS DESCRICAO2, ct.CARGO_TIPO AS DESCRICAO3 ";
		sqlComun = " FROM PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE ct.CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
		if (id_Serventia != null & id_Serventia.trim().length() > 0) {
			sqlComun += " AND s.ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		if (nomeBusca != null & nomeBusca.trim().length() > 0){
			sqlComun += " AND NOME_USU LIKE ? ";
			ps.adicionarString( nomeBusca.replace("'", "") +"%");
		}
				
		sql += sqlComun;
		sql += " ORDER BY DESCRICAO1, vsc.NOME_USU";		
		try{
			rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
			rs2 = consultar("SELECT COUNT(1) as QUANTIDADE " + sqlComun, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiaCargosDesembargadoresJSON(String nomeBusca, String posicaoPaginaAtual, String id_ServentiaCamara) throws Exception {
		String sql = "";
		String sqlComun = "";		
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO AS ID, sc.SERV_CARGO || ' [' || vsc.SERV || ']' AS DESCRICAO1, vsc.NOME_USU AS DESCRICAO2, ct.CARGO_TIPO AS DESCRICAO3 ";
		sqlComun = " FROM PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE gt.GRUPO_TIPO_CODIGO = ? ";
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		if (id_ServentiaCamara != null & id_ServentiaCamara.trim().length() > 0) {
			sqlComun += " AND s.ID_SERV = ? ";
			ps.adicionarLong(id_ServentiaCamara);
		}
		if (nomeBusca != null & nomeBusca.trim().length() > 0){
			sqlComun += " AND NOME_USU LIKE ? ";
			ps.adicionarString( nomeBusca.replace("'", "") +"%");
		}
				
		sql += sqlComun;
		sql += " ORDER BY sc.SERV_CARGO, vsc.NOME_USU";		
		try{
			rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
			rs2 = consultar("SELECT COUNT(1) as QUANTIDADE " + sqlComun, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiaCargosAgendaAudienciaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO AS DESCRICAO1, NOME_USU AS DESCRICAO2, SERV AS DESCRICAO3  ";
		sql += " FROM PROJUDI.VIEW_SERV_CARGO";
		sql += " WHERE SERV_CARGO LIKE ?";
		ps.adicionarString( nomeBusca +"%");
		sql += " AND (GRUPO_TIPO_USU_CODIGO = ? OR GRUPO_TIPO_USU_CODIGO = ?)";
		ps.adicionarLong(GrupoTipoDt.CONCILIADOR_VARA);
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU);
		if (id_Serventia != null && !id_Serventia.equals("")){
			sql += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		sql += " ORDER BY SERV_CARGO ";		
		try{
			rs1 = consultar(sql, ps);
			sql = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.VIEW_SERV_CARGO WHERE SERV_CARGO LIKE ? ";
			sql += " AND (GRUPO_TIPO_USU_CODIGO = ? OR GRUPO_TIPO_USU_CODIGO = ?)";
			if (id_Serventia != null && !id_Serventia.equals("")) sql += " AND ID_SERV = ? ";
			rs2 = consultar(sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}

	public String consultarId_ServentiaCargo( String id_UsuarioServentia, int grupo) throws Exception {
		String Sql;
		String id_ServentiaCargo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT sc.id_serv_cargo FROM serv_cargo sc ";
		Sql += " INNER JOIN  usu_serv_grupo usg on sc.id_usu_serv_grupo = usg.id_usu_serv_grupo ";
		Sql += " INNER JOIN  grupo g on usg.id_grupo = g.id_grupo ";
		
		Sql += " WHERE g.grupo_codigo = ? and usg.id_usu_serv = ?"; ps.adicionarLong(grupo);  ps.adicionarLong(id_UsuarioServentia);		
		
		try {
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				id_ServentiaCargo = rs1.getString("id_serv_cargo");
			}		
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return id_ServentiaCargo;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Consulta os cargos cadastrados do tipo desembargador relacionados aos gabinetes vinculados à câmara conforme ordem de antiguidade
	 * param id_ServentiaCamara,
	 *            identificação da serventia 
	 * 
	 * author aamoraes
	 * since 09/10/2018
	 */
	public List consultarServentiaCargosDesembargadoresOrdemAntiguidade(String id_ServentiaCamara) throws Exception {
		String sql = "";
		String sqlComun = "";		
		List listaServentiaCargosDesembargadores = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, sc.ID_CARGO_TIPO, ct.CARGO_TIPO, ct.CARGO_TIPO_CODIGO, vsc.NOME_USU, sr.ORDEM_TURMA_JULGADORA,";
		sql += " (SELECT SERV";
		sql += "   FROM PROJUDI.SERV s";
		sql += "   WHERE s.ID_SERV = sc.ID_SERV";
		sql += "   AND ROWNUM = 1) as SERV";		
		sqlComun = " FROM (PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO)";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE gt.GRUPO_TIPO_CODIGO = ? ";	ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		sqlComun += " AND s.ID_SERV = ? "; ps.adicionarLong(id_ServentiaCamara);		
		sqlComun += " AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
		sqlComun += " AND sr.ORDEM_TURMA_JULGADORA > ? "; ps.adicionarLong(0);
				
		sql += sqlComun;
		sql += " ORDER BY ORDEM_TURMA_JULGADORA, SERV_CARGO, NOME_USU";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				listaServentiaCargosDesembargadores.add(serventiaCargoDt);
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}			
		}
		return listaServentiaCargosDesembargadores;
	}

	/**
	 * Consulta os cargos cadastrados do tipo desembargador relacionados aos gabinetes vinculados à câmara conforme ordem de antiguidade, trazendo a ordem
	 * param id_ServentiaCamara,
	 *            identificação da serventia 
	 * 
	 * author jvosantos
	 * since 15/06/2020
	 */
	public List<ServentiaCargoDt> consultarServentiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem(String id_ServentiaCamara) throws Exception {
		String sql = "";
		String sqlComun = "";		
		List<ServentiaCargoDt> listaServentiaCargosDesembargadores = new ArrayList<ServentiaCargoDt>();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql += "SELECT DISTINCT sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, sc.ID_CARGO_TIPO, ct.CARGO_TIPO, ct.CARGO_TIPO_CODIGO, vsc.NOME_USU, sr.ORDEM_TURMA_JULGADORA,";
		sql += " (SELECT SERV";
		sql += "   FROM PROJUDI.SERV s";
		sql += "   WHERE s.ID_SERV = sc.ID_SERV";
		sql += "   AND ROWNUM = 1) as SERV";		
		sqlComun = " FROM (PROJUDI.SERV s ";
		sqlComun += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlComun += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV";
		sqlComun += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlComun += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO)";
		sqlComun += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlComun += " WHERE gt.GRUPO_TIPO_CODIGO = ? ";	ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		sqlComun += " AND s.ID_SERV = ? "; ps.adicionarLong(id_ServentiaCamara);		
		sqlComun += " AND sc.QUANTIDADE_DIST > ? "; ps.adicionarLong(0);
		sqlComun += " AND sr.ORDEM_TURMA_JULGADORA > ? "; ps.adicionarLong(0);
				
		sql += sqlComun;
		sql += " ORDER BY ORDEM_TURMA_JULGADORA, SERV_CARGO, NOME_USU";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setOrdemTurmaJulgadora(rs1.getInt("ORDEM_TURMA_JULGADORA"));
				listaServentiaCargosDesembargadores.add(serventiaCargoDt);
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}			
		}
		return listaServentiaCargosDesembargadores;
	}

	/**
	 * Retorna o ServentiaCargo correspondente a serventia e tipo de cargo passado e que tenha quantidade de distribuição informada
	 * assim como período de substituição.
	 * 
	 * param id_Serventia, identificação da serventia
	 * param id_Processo, identificação do processo
	 * 
	 * author lsbernardes
	 */
	public String getJuizUPJResponsavel(String id_Serventia, String id_Processo) throws Exception {
		String Sql;
		String id_ServentiaCargo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		Sql = "SELECT pr.id_serv_cargo FROM proc_resp pr ";
		Sql += " INNER JOIN serv_cargo sc on pr.id_serv_cargo = sc.id_serv_cargo ";
		Sql += " INNER JOIN  usu_serv_grupo usg on sc.id_usu_serv_grupo = usg.id_usu_serv_grupo ";
		Sql += " INNER JOIN  grupo g on usg.id_grupo = g.id_grupo ";
		
		Sql += " WHERE pr.id_proc = ? and g.grupo_codigo = ? and sc.id_serv = ? and pr.codigo_temp <> ? ";
		ps.adicionarLong(id_Processo); ps.adicionarLong(GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU); 
		ps.adicionarLong(id_Serventia); ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				id_ServentiaCargo = rs1.getString("id_serv_cargo");
			}		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return id_ServentiaCargo;
	}

	public String consultarServentiaCargosAgendaAudienciaUpjJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		
		sql = "SELECT sc.ID_SERV_CARGO AS ID, sc.SERV_CARGO AS DESCRICAO1, vsc.NOME_USU AS DESCRICAO2, s1.SERV AS DESCRICAO3  ";		
		sqlFrom = " FROM PROJUDI.SERV s ";
		sqlFrom += " INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_PRINC";
		sqlFrom += " INNER JOIN PROJUDI.SERV S1 ON S1.ID_SERV = SR.ID_SERV_REL "; 	 
		sqlFrom += " INNER JOIN PROJUDI.SERV_SUBTIPO SB ON SB.ID_SERV_SUBTIPO = S1.ID_SERV_SUBTIPO AND SB.SERV_SUBTIPO_CODIGO =?";		ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ);
		sqlFrom += " INNER JOIN PROJUDI.SERV_CARGO sc on sr.ID_SERV_REL = sc.ID_SERV and SC.QUANTIDADE_DIST> ? ";						ps.adicionarLong(0);
		sqlFrom += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sqlFrom += " INNER JOIN PROJUDI.GRUPO g on ct.ID_GRUPO = g.ID_GRUPO";
		sqlFrom += " INNER JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO ";
		sqlFrom += " LEFT JOIN PROJUDI.VIEW_SERV_CARGO vsc on vsc.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sqlFrom += " WHERE gt.GRUPO_TIPO_CODIGO in (?,?) ";											ps.adicionarLong(GrupoTipoDt.CONCILIADOR_VARA);		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU);
		if (id_Serventia != null & id_Serventia.trim().length() > 0) {
			sqlFrom += " AND s.ID_SERV = ? ";															ps.adicionarLong(id_Serventia);
		}

		sqlOrder = " ORDER BY SC.SERV_CARGO";		
		
//		sql = "SELECT ID_SERV_CARGO AS ID, SERV_CARGO AS DESCRICAO1, NOME_USU AS DESCRICAO2, SERV AS DESCRICAO3  ";
//		sql += " FROM PROJUDI.VIEW_SERV_CARGO";
//		sql += " WHERE SERV_CARGO LIKE ?";											ps.adicionarString( nomeBusca +"%");
//		sql += " AND (GRUPO_TIPO_USU_CODIGO = ? OR GRUPO_TIPO_USU_CODIGO = ?)";		ps.adicionarLong(GrupoTipoDt.CONCILIADOR_VARA);		ps.adicionarLong(GrupoTipoDt.JUIZ_VARA);
//		if (id_Serventia != null && !id_Serventia.equals("")){
//			sql += " AND ID_SERV = ? ";												ps.adicionarLong(id_Serventia);
//		}
//		sql += " ORDER BY SERV_CARGO ";		
		try{
			rs1 = consultar(sql + sqlFrom + sqlOrder, ps);
			sql = "SELECT COUNT(*) as QUANTIDADE ";					
			rs2 = consultar(sql + sqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	   /**
* Consulta os cargos que serão utilizados na distribuição de conclusão para um gabinete, de
* acordo com  a serventia passada
* 
* param id_serventia, identificação da serventia
* param cargoTipoCodigo, O cargo que será distribuido o processo    
* return o id_serv_cargo de quem vai receber a conclusão/processo
* 
* author jrcorrea
* 05/02/2020
*/
	
    public ServentiaCargoDt consultarServentiaCargpoDistribuicaoGabineteFluxo(String id_serventia, int cargoTipoCodigo) throws Exception {
      String Sql;
      ServentiaCargoDt dtRetorno = null;
      ResultSetTJGO rs1 = null;
      PreparedStatementTJGO ps = new PreparedStatementTJGO();
      
      Calendar cal = Calendar.getInstance();		
    		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		
    		
    		Sql = " SELECT  sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU,   ";
    		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP_HIST p ";
    		Sql += "          WHERE p.DATA_INICIO >= ?"; 																ps.adicionarDateTime(cal.getTime());
    		Sql += "              AND p.ID_SERV_CARGO = sc.ID_SERV_CARGO "; 
    		Sql += "          ) + ";
    		Sql += "     (SELECT COUNT(unique id_pend) as Qtd FROM PROJUDI.PEND_RESP_HIST_FINAL p1 ";
    		Sql += "          WHERE p1.DATA_INICIO >= ?"; 																ps.adicionarDateTime(cal.getTime());
    		Sql += "              AND p1.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
    		Sql += "          ) as Qtd, ";    
    		Sql += "     (DBMS_RANDOM.RANDOM ) as ordem,  ";
    		Sql += "     (SELECT MIN(SG.ID_SERV_GRUPO) FROM SERV_GRUPO SG INNER JOIN SERV_CARGO_SERV_GRUPO SCSG ON SCSG.ID_SERV_GRUPO = SG.ID_SERV_GRUPO  WHERE SG.ID_SERV=sc.ID_SERV and scsg.id_serv_cargo = sc.id_serv_cargo ) as ID_SERV_GRUPO  ";
    		Sql += "   FROM PROJUDI.VIEW_SERV_CARGO sc ";
    		Sql += "     	inner join projudi.cargo_tipo ct on sc.id_cargo_tipo = ct.id_cargo_tipo ";
    		Sql += "     WHERE sc.ID_SERV = ? ";  																		ps.adicionarLong(id_serventia);
    		Sql += "        AND ct.Cargo_tipo_codigo  = ? ";  															ps.adicionarLong(cargoTipoCodigo);
    		Sql += "        AND sc.QUANTIDADE_DIST > ? "; 																ps.adicionarLong(0);
    		Sql += "     ORDER BY  Qtd,  ordem";     
      
      try{
          rs1 = consultar(Sql, ps);
          if(rs1.next()) {
        	  dtRetorno = new ServentiaCargoDt();
    			dtRetorno.setId(rs1.getString("ID_SERV_CARGO"));
    			dtRetorno.setServentiaCargo(rs1.getString("SERV_CARGO"));
    			dtRetorno.setNomeUsuario(rs1.getString("NOME_USU"));
    			dtRetorno.setId_ServentiaGrupo(rs1.getString("ID_SERV_GRUPO"));
          }
          
      
      } finally{
          try{
              if (rs1 != null) rs1.close();
          } catch(Exception e) {
          }
      }
      return dtRetorno;
    }

}
