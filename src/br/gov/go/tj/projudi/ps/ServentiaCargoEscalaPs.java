package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaCargoEscalaPs extends ServentiaCargoEscalaPsGen{

	private static final long serialVersionUID = 5524810462216449116L;
	
	public ServentiaCargoEscalaPs(Connection conexao){
		Conexao = conexao;
	}

	public List consultarServentiaCargoEscala(String descricao, String posicao ) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql= "SELECT ID_SERV_CARGO_ESC, SERV_CARGO, ESC, NOME_USU, ID_SERV_CARGO_ESC_STATUS, SERV_CARGO_ESC_STATUS, DATA_VINCULACAO  FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO LIKE ? OR NOME_USU LIKE ?";
		ps.adicionarString( descricao +"%");
		ps.adicionarString( descricao +"%");
		Sql+= " ORDER BY NOME_USU, SERV_CARGO";		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			while (rs1.next()) {
				ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
				
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				obTemp.setEscala(rs1.getString("ESC"));
				obTemp.setServentiaCargoEscala(rs1.getString("NOME_USU"));
				obTemp.setDataVinculacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VINCULACAO")));
				
				ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt = new ServentiaCargoEscalaStatusDt();
				serventiaCargoEscalaStatusDt.setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
				serventiaCargoEscalaStatusDt.setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
				
				obTemp.setServentiaCargoEscalaStatusDt(serventiaCargoEscalaStatusDt);
				
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO LIKE ? OR NOME_USU LIKE ?";
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
    public List consultarUsuariosServentiaEscala(String id_escala) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ususeresc.ID_SERV_CARGO, esc.QUANTIDADE_MAND as QUANTIDADE_DIST FROM ESC esc " ;
        Sql += " INNER JOIN SERV_CARGO_ESC ususeresc ON esc.ID_ESC=ususeresc.ID_ESC ";
        Sql += " WHERE esc.CODIGO_TEMP IS NULL";
        Sql += " AND esc.ID_ESC= ?";
        ps.adicionarLong(id_escala);
        Sql += " AND ususeresc.ID_SERV_CARGO_ESC_STATUS = ?";
        ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
        Sql += " ORDER BY ususeresc.ID_SERV_CARGO_ESC";

        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
            	ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
                obTemp.setId(rs1.getString("ID_SERV_CARGO"));
                obTemp.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
                liTemp.add(obTemp);
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp;
    }
    
    public String consultarUsuariosServentiaEscala(String id_servCargo, String id_escala) throws Exception {
        String Sql;
        String id = "";
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT sce.id_serv_cargo_esc as idServCargoEsc FROM projudi.serv_cargo_esc sce WHERE sce.id_serv_cargo = ? AND sce.id_esc = ?";
        ps.adicionarLong(id_servCargo);
        ps.adicionarLong(id_escala);
     
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
            	 
                id = rs1.getString("idServCargoEsc");
              
            }
        
        } finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return id;
    }    
    
    public void inserir(ServentiaCargoEscalaDt dados ) throws Exception {
    	String SqlCampos;
		String SqlValores;
    	String Sql;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	
    	SqlCampos= "INSERT INTO PROJUDI.SERV_CARGO_ESC ("; 
    	SqlValores = " Values (";
    	if (!(dados.getId_ServentiaCargo().length()==0)){
    		SqlCampos+= ",ID_SERV_CARGO " ;
    		SqlValores+=",?";
    		ps.adicionarLong(dados.getId_ServentiaCargo());    		
    	}
    	if (!(dados.getId_Escala().length()==0)){
    		SqlCampos+= ",ID_ESC " ;
    		SqlValores+=",?";
    		ps.adicionarLong(dados.getId_Escala());
    	}
    	SqlCampos += ",DATA_VINCULACAO";
    	SqlValores+=",?";
    	ps.adicionarDateTime(new Date());
    	SqlCampos += ",ID_SERV_CARGO_ESC_STATUS";
    	SqlValores+=",?";
    	ps.adicionarLong(dados.getServentiaCargoEscalaStatusDt().getId());   	
    	
    	SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
    	    	    	
    	dados.setId(executarInsert(Sql, "ID_SERV_CARGO_ESC", ps));
    	
    }
        
    /**
     * Método que ativa ou desativa um usuário serventia escala.
     * @param ServentiaCargoDt
     * @param EscalaDt
     * @param int flag
     * @return boolean
     * @throws Exception
     */
    public boolean ativaDesativaUsuarioEscala(UsuarioServentiaDt usuarioServentiaDt, EscalaDt escalaDt, int flag) throws Exception {
    	boolean retorno = false;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "UPDATE PROJUDI.SERV_CARGO_ESC SET ATIVO = ? WHERE ID_ESC = ? AND ID_SERV_CARGO = ?";
    	ps.adicionarLong(flag);
    	ps.adicionarLong(escalaDt.getId());
    	ps.adicionarLong(usuarioServentiaDt.getId());
    	
    	try{
    		this.executarUpdateDelete(sql, ps);
    		retorno = true;
    	}
    	catch(Exception e) {
    		retorno = false;
    	}
    	
    	return retorno;
    }
    
    /**
     * Método que inativa um usuário serventia escala.
     * @param idServCargoEsc
     * @return void
     * @throws Exception
     */
    public void desativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "UPDATE PROJUDI.SERV_CARGO_ESC SET ID_SERV_CARGO_ESC_STATUS = ";
    	sql += "(SELECT ID_SERV_CARGO_ESC_STATUS FROM SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ?) ";
    	ps.adicionarLong(ServentiaCargoEscalaStatusDt.INATIVO);
    	sql += " WHERE ID_SERV_CARGO_ESC = ? ";
    	ps.adicionarLong(idServCargoEsc);

   		this.executarUpdateDelete(sql, ps);
    }
    
    /**
     * Método que ativa um usuário serventia escala.
     * @param idServCargoEsc
     * @return void
     * @throws Exception
     */
    public void ativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "UPDATE PROJUDI.SERV_CARGO_ESC SET ID_SERV_CARGO_ESC_STATUS = ";
    	sql += "(SELECT ID_SERV_CARGO_ESC_STATUS FROM SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ?) ";
    	ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
    	sql += " WHERE ID_SERV_CARGO_ESC = ? ";
    	ps.adicionarLong(idServCargoEsc);

   		this.executarUpdateDelete(sql, ps);
    }
    
    /**
     * Método que desativa um usuário serventia em todas as escalas.
     * @param idServCargo
     * @return boolean
     * @throws Exception
     * @author hrrosa
     */
    public void desativaServCargoTodasEscalas(String idUsuServ) throws Exception {  //  so esta desativando as escalas cíveis (com custas) de um oficial.
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "UPDATE PROJUDI.VIEW_SERV_CARGO_ESC SET ID_SERV_CARGO_ESC_STATUS = ";
    	sql +=       " (SELECT ID_SERV_CARGO_ESC_STATUS FROM SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ?) ";
    	sql +=		 " WHERE ID_SERV_CARGO = ( ";
    	sql +=		 " 	SELECT ID_SERV_CARGO FROM SERV_CARGO SC ";
    	sql +=		 " 	INNER JOIN USU_SERV_GRUPO USG ON USG.ID_USU_SERV_GRUPO = SC.ID_USU_SERV_GRUPO ";
    	sql += 		 " 	INNER JOIN USU_SERV US ON US.ID_USU_SERV = USG.ID_USU_SERV ";
    	sql += 		 " 	WHERE US.ID_USU_SERV = ?) AND id_escala_tipo = ?";
    	ps.adicionarLong(ServentiaCargoEscalaStatusDt.SUSPENSO);
    	ps.adicionarLong(idUsuServ);
    	ps.adicionarLong(EscalaTipoDt.CIVEL);
		this.executarUpdateDelete(sql, ps);
    }
    
    /**
     * Método que ativa um usuário serventia em todas as escalas.
     * @param idServCargo
     * @return boolean
     * @throws Exception
     * @author hrrosa
     */
    public void ativaServCargoTodasEscalas(String idUsuServ) throws Exception {  // ativando todas as escalas de um oficial
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "UPDATE PROJUDI.SERV_CARGO_ESC SET ID_SERV_CARGO_ESC_STATUS = ";
    	sql +=       " (SELECT ID_SERV_CARGO_ESC_STATUS FROM SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ?) ";
    	sql +=		 " WHERE ID_SERV_CARGO = ( ";
    	sql +=		 " 	SELECT ID_SERV_CARGO FROM SERV_CARGO SC ";
    	sql +=		 " 	INNER JOIN USU_SERV_GRUPO USG ON USG.ID_USU_SERV_GRUPO = SC.ID_USU_SERV_GRUPO ";
    	sql += 		 " 	INNER JOIN USU_SERV US ON US.ID_USU_SERV = USG.ID_USU_SERV ";
    	sql += 		 " 	WHERE US.ID_USU_SERV = ?)";
    	ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
    	ps.adicionarLong(idUsuServ);
		this.executarUpdateDelete(sql, ps);
    }
    
    public void alterar(ServentiaCargoEscalaDt dados) throws Exception{
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql= "UPDATE PROJUDI.SERV_CARGO_ESC SET  ";
		if(dados.getId_ServentiaCargo() != null && !dados.getId_ServentiaCargo().equals("")){
			Sql+= ",ID_SERV_CARGO = ?";
			ps.adicionarLong(dados.getId_ServentiaCargo()); 
		}
		if(dados.getId_Escala() != null && !dados.getId_Escala().equals("")){
			Sql+= ",ID_ESC = ?";
			ps.adicionarLong(dados.getId_Escala()); 
		}
		if(dados.getServentiaCargoEscalaStatusDt().getId() != null && !dados.getServentiaCargoEscalaStatusDt().getId().equals("")){
			Sql+= ",ID_SERV_CARGO_ESC_STATUS = ?";
			ps.adicionarLong(dados.getServentiaCargoEscalaStatusDt().getId());
		}
		Sql = Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_SERV_CARGO_ESC  = ?";
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(Sql, ps); 
	}
    
//    public ServentiaCargoEscalaDt consultarId(String id_serventiacargoescala )  throws Exception {
//
//		ServentiaCargoEscalaDt serventiaCargoEscalaDt = null;
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		StringBuffer sql = new StringBuffer("SELECT use1.ID_SERV_CARGO_ESC AS ID_SERV_CARGO_ESC, u.USU AS SERV_CARGO_ESC, use1.ID_SERV_CARGO AS ID_SERV_CARGO, u.USU AS SERV_CARGO, u.NOME AS NOME_USU, use1.ID_ESC AS ID_ESC, e.ESC AS ESC, use1.DATA_VINCULACAO AS DATA_VINCULACAO, use1.ID_SERV_CARGO_ESC_STATUS AS ID_SERV_CARGO_ESC_STATUS, usestatus.SERV_CARGO_ESC_STATUS AS SERV_CARGO_ESC_STATUS, use1.CODIGO_TEMP AS CODIGO_TEMP, ush.DATA_INICIO AS DATA_STATUS ");
//		sql.append(" FROM PROJUDI.SERV_CARGO_ESC use1, PROJUDI.SERV_CARGO us, PROJUDI.ESC e, PROJUDI.USU u, PROJUDI.SERV_CARGO_ESC_STATUS usestatus, PROJUDI.SERV_CARGO_ESC_STATUS_HIST ush");
//		sql.append(" WHERE use1.ID_SERV_CARGO_ESC = ?");
//		ps.adicionarLong(id_serventiacargoescala);
//		sql.append(" AND use1.ID_SERV_CARGO = us.ID_SERV_CARGO AND use1.ID_ESC = e.ID_ESC AND us.ID_USU = u.ID_USU AND use1.ID_SERV_CARGO_ESC_STATUS = usestatus.ID_SERV_CARGO_ESC_STATUS ");
//		sql.append(" AND ush.DATA_INICIO = (SELECT MAX(ush2.DATA_INICIO) FROM PROJUDI.SERV_CARGO_ESC_STATUS_HIST ush2 WHERE ush2.ID_SERV_CARGO_ESC = ?)");
//		ps.adicionarLong(id_serventiacargoescala);
//		
//
//		try{
//			rs1 = consultar(sql.toString(), ps);
//			if (rs1.next()) {
//				serventiaCargoEscalaDt = new ServentiaCargoEscalaDt();
//				
//				serventiaCargoEscalaDt.setId(rs1.getString("ID_SERV_CARGO_ESC"));
//				serventiaCargoEscalaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
//				serventiaCargoEscalaDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
//				serventiaCargoEscalaDt.setId_Escala(rs1.getString("ID_ESC"));
//				serventiaCargoEscalaDt.setEscala(rs1.getString("ESC"));
//				serventiaCargoEscalaDt.setServentiaCargoEscala(rs1.getString("NOME_USU"));
//				serventiaCargoEscalaDt.setDataVinculacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VINCULACAO")));
//				
//				ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt = new ServentiaCargoEscalaStatusDt();
//				serventiaCargoEscalaStatusDt.setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
//				serventiaCargoEscalaStatusDt.setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
//				serventiaCargoEscalaStatusDt.setDataStatus(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_STATUS")));
//				
//				serventiaCargoEscalaDt.setServentiaCargoEscalaStatusDt(serventiaCargoEscalaStatusDt);
//			}
//		} finally {
//             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
//        } 
//		return serventiaCargoEscalaDt; 
//	}
    
    public ServentiaCargoEscalaDt consultarId(String id_serventiacargoescala )  throws Exception {
    	ServentiaCargoEscalaDt serventiCargoEscalaDt = new ServentiaCargoEscalaDt();
    	String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE ID_SERV_CARGO_ESC = ?";
		ps.adicionarLong(id_serventiacargoescala);

		try{

			rs1 = consultar(Sql, ps);

			rs1.next();
			serventiCargoEscalaDt.setId(rs1.getString("ID_SERV_CARGO_ESC"));
			serventiCargoEscalaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
			serventiCargoEscalaDt.setId_Escala (rs1.getString("ID_ESC"));
			serventiCargoEscalaDt.setEscala(rs1.getString("ESC"));
			serventiCargoEscalaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
			serventiCargoEscalaDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
			serventiCargoEscalaDt.setDataVinculacao(rs1.getString("DATA_VINCULACAO"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setAtivo(rs1.getString("ATIVO"));
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
    	return serventiCargoEscalaDt;
	}
    
    public ServentiaCargoEscalaDt consultarIdServentiaCargo(String id_serventiacargo)  throws Exception {
    	ServentiaCargoEscalaDt serventiCargoEscalaDt = new ServentiaCargoEscalaDt();
    	String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE ID_SERV_CARGO = ? ORDER BY ID_SERV_CARGO_ESC DESC";
		ps.adicionarLong(id_serventiacargo);

		try{

			rs1 = consultar(Sql, ps);

			rs1.next();
			serventiCargoEscalaDt.setId(rs1.getString("ID_SERV_CARGO_ESC"));
			serventiCargoEscalaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
			serventiCargoEscalaDt.setId_Escala (rs1.getString("ID_ESC"));
			serventiCargoEscalaDt.setEscala(rs1.getString("ESC"));
			serventiCargoEscalaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
			serventiCargoEscalaDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
			serventiCargoEscalaDt.setDataVinculacao(rs1.getString("DATA_VINCULACAO"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
			serventiCargoEscalaDt.getServentiaCargoEscalaStatusDt().setAtivo(rs1.getString("ATIVO"));
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
    	return serventiCargoEscalaDt;
	}
    
	public List consultarEscalaServentiaCargoGeral(String id_serventiacargo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT t2.ID_SERV_CARGO_ESC, t1.ID_ESC, t1.ESC, t3.ID_SERV_CARGO, t3.SERV_CARGO";
		Sql+= " FROM PROJUDI.ESC t1 ";
		Sql+= " LEFT JOIN PROJUDI.SERV_CARGO_ESC t2 ON t1.ID_ESC = t2.ID_ESC AND t2.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_serventiacargo);
		Sql+= " LEFT JOIN PROJUDI.SERV_CARGO t3 ON t3.ID_SERV_CARGO = t2.ID_SERV_CARGO";
		try{

			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC"));
				obTemp.setServentiaCargoEscala(rs1.getString("SERV_CARGO_ESC"));
				obTemp.setId_Escala (rs1.getString("ID_ESC"));
				obTemp.setEscala(rs1.getString("ESC"));
				obTemp.setId_ServentiaCargo (id_serventiacargo);
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}
	
	public String consultarServentiaCargoEscalaJSON(String descricao, String posicao ) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;
		
		Sql= "SELECT ID_SERV_CARGO_ESC AS ID, SERV_CARGO AS DESCRICAO1, NOME AS DESCRICAO2, ESC AS DESCRICAO3, SERV_CARGO_ESC_STATUS AS DESCRICAO4 FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO LIKE ? ";
		ps.adicionarString( descricao +"%");
		Sql+= " ORDER BY ID_SERV_CARGO_ESC";		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql= "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO LIKE ? ";
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);	
			
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
	
	/**
	 * Consulta os oficiais cadastrados na escala Ad Hoc da central de mandados especificada pelo idServ
	 * @param descricao
	 * @param posicao
	 * @param idServ
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarOficialAdhocJSON(String descricao, String posicao, String idServ ) throws Exception {
		String sqlSelect;
		String sqlFrom;
		String sqlWhere;
		String sqlOrderBy;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		sqlSelect =  "SELECT ID_SERV_CARGO_ESC AS ID, NOME AS DESCRICAO1, ESC AS DESCRICAO2 ";
		sqlFrom  = " FROM PROJUDI.VIEW_SERV_CARGO_ESC ";
		sqlWhere = " WHERE SERV_CARGO LIKE ? ";		ps.adicionarString( descricao +"%");
		sqlWhere += " AND ID_SERV = ? "; 			ps.adicionarLong(idServ);
		sqlWhere += " AND ESC_TIPO_ESPECIAL = ? ";		ps.adicionarDecimal(String.valueOf(EscalaDt.TIPO_ESPECIAL_ADHOC));
		sqlOrderBy = " ORDER BY ID_SERV_CARGO_ESC";		
		try{
			rs1 = consultarPaginacao(sqlSelect + sqlFrom + sqlWhere + sqlOrderBy, ps, posicao);
			rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlFrom + sqlWhere, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);	
			
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
	
	/**
	 * Método que consulta a lista de Usuario Serventia Escala cadastrados para determinada Escala.
	 * @param idEscala - ID da Escala
	 * @return lista de usuários
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarServentiaCargoEscalaPorEscala(String idEscala) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM VIEW_SERV_CARGO_ESC ";
		Sql+= " WHERE ID_ESC = ? ";
		ps.adicionarLong(idEscala);
		Sql+= " ORDER BY ID_SERV_CARGO_ESC ";
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC"));
				obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				obTemp.setId_Escala (rs1.getString("ID_ESC"));
//				obTemp.getServentiaCargoEscalaStatusDt().setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
				obTemp.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
				obTemp.getServentiaCargoEscalaStatusDt().setAtivo(rs1.getString("ATIVO"));
				obTemp.setDataVinculacao(Funcoes.FormatarData(rs1.getString("DATA_VINCULACAO")));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				liTemp.add(obTemp);
			}
		} finally {  
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        } 
		return liTemp; 
	}
	
	
	/**
	 * Método que consulta a lista de Usuario Serventia Escala cadastrados para determinada Escala.
	 * @param idEscala - ID da Escala
	 * @return lista de usuários
	 * @throws Exception
	 */
	public List consultarServentiaCargoEscalaAtivoSuspensoPorEscala(String idEscala) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM VIEW_SERV_CARGO_ESC ";
		Sql+= " WHERE ID_ESC = ? ";
		ps.adicionarLong(idEscala);
		Sql+= " AND SERV_CARGO_ESC_STATUS_CODIGO IN (?,?) ";
		ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
		ps.adicionarLong(ServentiaCargoEscalaStatusDt.SUSPENSO);
		Sql+= " ORDER BY ID_SERV_CARGO_ESC ";
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC"));
				obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				obTemp.setId_Escala (rs1.getString("ID_ESC"));
//				obTemp.getServentiaCargoEscalaStatusDt().setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
				obTemp.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
				obTemp.getServentiaCargoEscalaStatusDt().setAtivo(rs1.getString("ATIVO"));
				obTemp.setDataVinculacao(Funcoes.FormatarData(rs1.getString("DATA_VINCULACAO")));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				liTemp.add(obTemp);
			}
		} finally {  
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        } 
		return liTemp; 
	}
	
	public List<EscalaDt> consultarServentiaCargoEscalaPorServentiaCargo(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<EscalaDt> liTemp = new ArrayList<EscalaDt>();
		String sql;
		
		sql =  "SELECT ID_ESC, ESC FROM VIEW_SERV_CARGO_ESC ";
		sql += " WHERE ID_SERV_CARGO = ? AND ID_SERV_CARGO_ESC_STATUS = ";
		ps.adicionarLong(Funcoes.StringToLong(idServentiaCargo));
		sql += " (SELECT ID_SERV_CARGO_ESC_STATUS FROM SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ?) ";
		ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
		sql += " GROUP BY ID_ESC, ESC ORDER BY ESC";
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				EscalaDt obTemp = new EscalaDt();
				obTemp.setId(rs1.getString("ID_ESC"));
				obTemp.setEscala(rs1.getString("ESC"));
				liTemp.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        } 
		
		return liTemp;
	}

	public String[] retornarOficialAdhocEscolhido(String idServentiaCargoEscala, FabricaConexao obFabricaConexao) throws Exception {	
		String[] oficial = {"","","",""};
		String sql;		
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		

		try {
			
			//TODO: AMARRAR DEMAIS VALIDAÇÕES NO SQL
			sql = "SELECT ID_USU_SERV, ID_SERV_CARGO, ID_SERV_CARGO_ESC, ID_ESC FROM VIEW_SERV_CARGO_ESC WHERE ID_SERV_CARGO_ESC = ? AND ESC_TIPO_ESPECIAL = ?";
			ps.adicionarLong(idServentiaCargoEscala);
			ps.adicionarDecimal(String.valueOf(EscalaDt.TIPO_ESPECIAL_ADHOC));
			
			rs1 = consultar(sql, ps);

			if (rs1.next()){
				oficial[0] = rs1.getString("id_usu_serv");
				oficial[1] = rs1.getString("id_serv_cargo");
				oficial[2] = rs1.getString("id_serv_cargo_esc");
				oficial[3] = rs1.getString("id_esc");
				
			}else{
				throw new MensagemException("Não foi possivel localizar um oficial Ad hoc. Verifique o cadastro e tente novamente");
			}	
			
		} finally {
            try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
       }
		
		return oficial;
	}
	/**
	 * Método que ativa todas as escalas de um oficial.
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return void
	 * @throws Exception
	 * @author Fernando Meireles
	 */

	public void ativaServentiaCargoTodasEscalas(String idUsuServ) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
 
		String sql = "UPDATE projudi.serv_cargo_esc SET id_serv_cargo_esc_status = ?" + " WHERE id_serv_cargo ="
				+ " (select sc.id_serv_cargo FROM projudi.serv_cargo sc"
				+ " INNER JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo"
				+ "	INNER JOIN usu_serv us ON us.id_usu_serv = usg.id_usu_serv" + " WHERE us.id_usu_serv = ?)";

		ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
		ps.adicionarLong(idUsuServ);
		this.executarUpdateDelete(sql, ps);
 
	}

	/**
	 * Método que desativa escalas de um oficial dependendo do afastamento
	 * escolhido.
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return void
	 * @throws Exception
	 * @author Fernando Meireles
	 */

	public void desativaServCargoEscalaAfastamento(String idUsuServ, String idAfastamento) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "UPDATE projudi.view_serv_cargo_esc SET id_serv_cargo_esc_status = ?" + " WHERE id_serv_cargo ="
				+ " (select sc.id_serv_cargo FROM projudi.serv_cargo sc"
				+ " INNER JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo"
				+ "	INNER JOIN usu_serv us ON us.id_usu_serv = usg.id_usu_serv"
				+ " WHERE us.id_usu_serv = ?)";

		ps.adicionarLong(ServentiaCargoEscalaStatusDt.SUSPENSO);
		ps.adicionarLong(idUsuServ);

		if (idAfastamento.equalsIgnoreCase(AfastamentoDt.CODIGO_AFASTAMENTO_COM_CUSTAS)) {
			sql += " AND id_escala_tipo = ?";
			ps.adicionarLong(EscalaTipoDt.CIVEL);
		} else {

		}
		if (idAfastamento.equalsIgnoreCase(AfastamentoDt.CODIGO_AFASTAMENTO_SEM_CUSTAS)) {
			sql += " AND id_escala_tipo in(?,?)";
			ps.adicionarLong(EscalaTipoDt.CRIMINAL);
			ps.adicionarLong(EscalaTipoDt.ASSISTENCIA);
		}

	    this.executarUpdateDelete(sql, ps);
				
	}
}