package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaRelacionadaPs extends ServentiaRelacionadaPsGen {

    /**
     * 
     */
    private static final long serialVersionUID = -8954351006774929048L;

    public ServentiaRelacionadaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * Consulta as serventias relacionadas a serventia principal que está sendo
     * passada
     * 
     * @author msapaula
     */
    public List consultarServentiasRel(String id_Serventia) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO, ID_SERV_TIPO_RELACIONADA, SERV_TIPO_CODIGO_RELACIONADA, SERV_TIPO_RELACIONADA, PRESIDENCIA,  ";
        Sql += " ID_SERV_SUBSTITUICAO, SERV_SUBSTITUICAO, DATA_INICIAL_SUBSTITUICAO, DATA_FINAL_SUBSTITUICAO, RECEBE_PROC ";
        Sql += " FROM PROJUDI.VIEW_SERV_RELACIONADA ";
        Sql += " WHERE ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
                ServentiaDt obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
                obTemp.setId_ServentiaTipo(rs1.getString("ID_SERV_TIPO_RELACIONADA"));
                obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO_RELACIONADA"));
                obTemp.setServentiaTipo(rs1.getString("SERV_TIPO_RELACIONADA"));
                obTemp.setPresidencia(Funcoes.FormatarLogico(rs1.getString("PRESIDENCIA")));
                obTemp.setId_ServentiaSubstituicao(rs1.getString("ID_SERV_SUBSTITUICAO"));
                obTemp.setServentiaSubstituicao(rs1.getString("SERV_SUBSTITUICAO"));
                obTemp.setDataInicialSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
                obTemp.setDataFinalSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_FINAL_SUBSTITUICAO")));
                obTemp.setRecebeProcesso(Funcoes.FormatarLogico(rs1.getString("RECEBE_PROC")));
                liTemp.add(obTemp);
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp;
    }

    /**
     * Consulta a serventia relacionada de um determinado tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     * @author msapaula
     */
    public ServentiaDt consultarServentiaRelacionada(String id_Serventia, int tipoServentia) throws Exception {
        String Sql;
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO, SERV_TIPO_CODIGO_RELACIONADA FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " AND s.SERV_TIPO_CODIGO_RELACIONADA = ?";
        ps.adicionarLong(tipoServentia);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
                obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO_RELACIONADA"));
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return obTemp;
    }
    
    /**
     * Consulta uma lista de serventias relaciondadas do tipo especificado
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return List<dt> da serventia relacionada
     * @author hrrosa
     */
    public List<ServentiaDt> consultarListaServentiaRelacionada(String id_Serventia, int tipoServentia) throws Exception {
        String Sql;
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        List<ServentiaDt> listaServentiasRelacionadas = new ArrayList<ServentiaDt>();
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO, SERV_TIPO_CODIGO_RELACIONADA FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " AND s.SERV_TIPO_CODIGO_RELACIONADA = ?";
        ps.adicionarLong(tipoServentia);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
                obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO_RELACIONADA"));
                listaServentiasRelacionadas.add(obTemp);
            }
        
        } finally{
             if (rs1 != null) rs1.close();
        } 
        return listaServentiasRelacionadas;
    }
    
    public String consultarListaServentiaRelacionadaJSON(String id_Serventia, int tipoServentia, String posicao, int qtdeColunas) throws Exception {
        String sqlFrom;
        String sqlSelect;
        String sqlOrder;
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        String retorno;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sqlSelect = " SELECT ID_SERV_RELACAO AS ID,SERV_RELACAO AS DESCRICAO1 ";
        sqlFrom  = " FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s ";
        sqlFrom += " WHERE s.ID_SERV_PRINC = ? ";
        ps.adicionarLong(id_Serventia);
        sqlFrom += " AND s.SERV_TIPO_CODIGO_RELACIONADA = ? ";
        ps.adicionarLong(tipoServentia);
        sqlOrder = " ORDER BY SERV_RELACAO ";
        try{

        	rs1 = consultarPaginacao(sqlSelect + sqlFrom + sqlOrder, ps, posicao);

			sqlSelect = "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(sqlSelect + sqlFrom, ps);
			rs2.next();
			retorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
        	
        } finally{
             if (rs1 != null) rs1.close();
        } 
        return retorno;
    }
    
    
public String consultarDescricaoCentralMandadoRelacionadasJSON(String idCentralMandadosRelacionada, String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		//HELLENO
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";		
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
//		Sql = "SELECT ID_SERV AS ID,SERV AS DESCRICAO1";
//		SqlFrom = " FROM PROJUDI.VIEW_SERV";
//		SqlFrom += " WHERE SERV LIKE ?";
//		ps.adicionarString("%"+ descricao +"%");
//	
//		
//		SqlFrom += " AND SERV_TIPO_CODIGO = ?";
//		ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
//		SqlFrom += " AND  CODIGO_TEMP = ?";
//		ps.adicionarLong(ServentiaDt.ATIVO);
//		SqlOrder = " ORDER BY SERV";
		
		Sql = "SELECT ID_SERV_RELACAO AS ID,SERV_RELACAO AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_RELACIONADA";
		SqlFrom += " WHERE SERV_RELACAO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
	
		SqlFrom += " AND ID_SERV_PRINC = ?";
		ps.adicionarLong(idCentralMandadosRelacionada);
		SqlFrom += " AND SERV_TIPO_CODIGO_RELACIONADA = ?";
		ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
		// TODO: A VIEW_SERV_RELACIONADA não coloca no CODIGO_TEMP o valor da
		// tabela SEV. Atualizar a view e voltar a condição abaixo para retornar
		// apenas as serventias ativas.
		//SqlFrom += " AND  CODIGO_TEMP = ?";
		//ps.adicionarLong(ServentiaDt.ATIVO);
		SqlOrder = " ORDER BY SERV_RELACIONADA";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
    
    /**
     * Consulta a serventia relacionada de um determinado tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param ServentiaSubtipoCodigo,
     *            identificação da serventia subtipo da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     * @author lsbernardes
     */
    public ServentiaDt consultarServentiaRelacionada(String id_Serventia, String ServentiaSubtipoCodigo,  int tipoServentia) throws Exception {
        String Sql;
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " AND s.ID_SERV_RELACAO_TIPO = ?";
        ps.adicionarLong(tipoServentia);
        Sql += " AND s.SERV_SUBTIPO_CODIGO = ?";
        ps.adicionarLong(ServentiaSubtipoCodigo);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return obTemp;
    }
    
    /**
     * Consulta a serventia relacionada de um determinado sub tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param serventiaSubtipoCodigo,
     *            sub tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     * @author msapaula
     */
    public ServentiaDt consultarServentiaRelacionadaSubTipo(String id_Serventia, String serventiaSubtipoCodigo) throws Exception {
        String Sql;
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " AND s.SERV_SUBTIPO_CODIGO = ?";
        ps.adicionarLong(serventiaSubtipoCodigo);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return obTemp;
    }
    
    /**
     * Método que realiza a consulta das Serventias Principais relacionadas à Serventia
     * informada.
     * @param id_Serventia - ID da Serventia Relação
     * @return lista de Serventias Principais
     * @throws Exception
     * @author hmgodinho
     */
    public List consultarServentiasPrincipaisRelacionadas(String id_Serventia) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_PRINC, SERV_PRINC FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_RELACAO = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " ORDER BY S.SERV_PRINC ";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_PRINC"));
                obTemp.setServentia(rs1.getString("SERV_PRINC"));
                liTemp.add(obTemp);
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return liTemp;
    }
    
    
    /**
     * Atualiza uma serventia relacionada como presidência
     * 
     * @param id_SeventiaPrincipal
     * @param id_ServentiaRelacionadaPresidencia
     * @throws Exception
     * @author mmgomes
     */
    public void atualizeServentiaRelacionadaPresidencia(String id_SeventiaPrincipal, String id_ServentiaRelacionadaPresidencia) throws Exception{
    	PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql="";				

		Sql= "UPDATE PROJUDI.SERV_RELACIONADA";
		Sql+= " SET PRESIDENCIA = ?"; ps.adicionarLongNull();		
		Sql += " WHERE ID_SERV_PRINC  = ?"; ps.adicionarLong(id_SeventiaPrincipal);
		executarUpdateDelete(Sql,ps);
		
		ps.limpar();
		
		Sql= "UPDATE PROJUDI.SERV_RELACIONADA";
		Sql+= " SET PRESIDENCIA = ?"; ps.adicionarBoolean(true);						
		Sql += " WHERE ID_SERV_RELACIONADA  = ? "; ps.adicionarLong(id_ServentiaRelacionadaPresidencia); 			
		executarUpdateDelete(Sql,ps);
    }
    
    /**
     * Consultar a Serventia do Tipo Presidencia Relacionada à Câmara
     * 
     * @param id_ServentiaCamara
     * @return
     * @throws Exception
     */
    public ServentiaDt consultarServentiaRelacionadaPresidencia(String id_ServentiaCamara) throws Exception {
        String Sql;
        ServentiaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT ID_SERV_RELACAO, SERV_RELACAO, SERV_SUBTIPO_CODIGO FROM PROJUDI.VIEW_SERV_RELACIONADA_COMPLETA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";  ps.adicionarLong(id_ServentiaCamara);
        Sql += " AND s.PRESIDENCIA = ?"; ps.adicionarBoolean(true);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
                obTemp = new ServentiaDt();
                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentia(rs1.getString("SERV_RELACAO"));
                obTemp.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
            }            
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return obTemp;
    }
    
    /**
     * Altera os dados de uma serventia relacionada
     * 
     * @param dados - Serventia Relacionada
     * @return
     * @throws Exception
     */
    public void alterar(ServentiaRelacionadaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";	

		stSql= "UPDATE PROJUDI.SERV_RELACIONADA SET  ";
			
		stSql+= " ID_SERV_PRINC = ?";		 ps.adicionarLong(dados.getId_ServentiaPrincipal());  

		stSql+= ",ID_SERV_REL = ?";		 ps.adicionarLong(dados.getId_ServentiaRelacao());
		
		stSql+= ",ID_SERV_SUBSTITUICAO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubstituicao());
		
		stSql+= ",DATA_INICIAL_SUBSTITUICAO = ?";		 ps.adicionarDateTimePrimeiraHoraDia(dados.getDataInicialSubstituicao());
		
		stSql+= ",DATA_FINAL_SUBSTITUICAO = ?";		 ps.adicionarDateTimeUltimaHoraDia(dados.getDataFinalSubstituicao());
		
		stSql+= ",PROBABILIDADE = ?";		 ps.adicionarDecimal(dados.getProbabilidade());
		
		stSql+= ",RECEBE_PROC = ?";		 ps.adicionarBoolean(dados.getRecebeProcesso());
		
		stSql += " WHERE ID_SERV_RELACIONADA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 
    
    /**
     * Insere os dados de uma serventia relacionada
     * 
     * @param dados - Serventia Relacionada
     * @return
     * @throws Exception
     */
    public void inserir(ServentiaRelacionadaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO PROJUDI.SERV_RELACIONADA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaRelacionada().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_RELACIONADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaRelacionada());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaPrincipal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_PRINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaPrincipal());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaRelacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_REL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaRelacao());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaSubstituicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBSTITUICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubstituicao());  

			stVirgula=",";
		}
		if ((dados.getDataInicialSubstituicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIAL_SUBSTITUICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTimePrimeiraHoraDia(dados.getDataInicialSubstituicao());  

			stVirgula=",";
		}
		if ((dados.getDataFinalSubstituicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FINAL_SUBSTITUICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTimeUltimaHoraDia(dados.getDataFinalSubstituicao());  

			stVirgula=",";
		}
		if ((dados.getRecebeProcesso().length()>0)) {
			 stSqlCampos+=   stVirgula + "RECEBE_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getRecebeProcesso());  

			stVirgula=",";
		}	
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores;		

		dados.setId(executarInsert(stSql,"ID_SERV_RELACIONADA",ps)); 
	}

    /**
     * Consulta o gabinete de segundo grau que é substituto atual do id serventia do gabinete passado como parâmetro
     * 
     * @param id_ServentiaGabinetePrincipal
     * @return
     * @author mmgomes
     * @throws Exception
     */
	public List consultarGabinetesSubstitutosSegundoGrau(String id_ServentiaCamaraPrincipal, String idServentiaGabinetePrincipal, String dataInicial, String dataFinal) throws Exception {
		String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT sr.ID_SERV_RELACIONADA, s.ID_SERV, s.SERV_CODIGO, s.SERV, es.UF, sr.PRESIDENCIA, ss.SERV_SUBTIPO_CODIGO, st.SERV_TIPO_CODIGO, ";
		Sql += " sr.ID_SERV_SUBSTITUICAO, s1.SERV AS SERV_SUBSTITUICAO, sr.DATA_INICIAL_SUBSTITUICAO, sr.DATA_FINAL_SUBSTITUICAO, sr.RECEBE_PROC ";
		Sql += " FROM PROJUDI.SERV s ";
		Sql += " JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_REL  ";
		Sql += " JOIN PROJUDI.ESTADO es on s.ID_ESTADO_REPRESENTACAO = es.ID_ESTADO ";
		Sql += " LEFT JOIN PROJUDI.SERV_SUBTIPO ss on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		Sql += " LEFT JOIN PROJUDI.SERV_TIPO st on s.ID_SERV_TIPO = st.ID_SERV_TIPO ";
		Sql += " LEFT JOIN PROJUDI.SERV s1 on sr.ID_SERV_SUBSTITUICAO = s1.ID_SERV ";
		Sql += " WHERE sr.ID_SERV_PRINC = ?";       
        ps.adicionarLong(id_ServentiaCamaraPrincipal);
        Sql += " AND sr.ID_SERV_SUBSTITUICAO = ?"; 
        ps.adicionarLong(idServentiaGabinetePrincipal);
        Sql += " AND (";
        Sql += " (DATA_INICIAL_SUBSTITUICAO BETWEEN ? AND ?) ";
        ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
        ps.adicionarDateTimeUltimaHoraDia(dataFinal);
        Sql += " OR ";
        Sql += " (DATA_FINAL_SUBSTITUICAO BETWEEN ? AND ?) ";
        ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
        ps.adicionarDateTimeUltimaHoraDia(dataFinal);
        Sql += " )";
        try{
        	rs = consultar(Sql, ps);
            while (rs.next()) {
                ServentiaDt obTemp = new ServentiaDt();
                obTemp.setId_ServentiaRelacaoEdicao(rs.getString("ID_SERV_RELACIONADA"));
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("UF"));
				obTemp.setPresidencia(Funcoes.FormatarLogico(rs.getString("PRESIDENCIA")));		
				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaSubtipoCodigo(rs.getString("SERV_SUBTIPO_CODIGO"));
				obTemp.setId_ServentiaSubstituicao(rs.getString("ID_SERV_SUBSTITUICAO"));
	            obTemp.setServentiaSubstituicao(rs.getString("SERV_SUBSTITUICAO"));
	            obTemp.setDataInicialSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
	            obTemp.setDataFinalSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_FINAL_SUBSTITUICAO")));
	            obTemp.setRecebeProcesso(Funcoes.FormatarLogico(rs.getString("RECEBE_PROC")));
                liTemp.add(obTemp);
            }
            // rs1.close();
        
        } finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
        return liTemp;
	}

	/**
	 * Consulta o gabinete de segundo grau que é substituto atual do id serventia do gabinete passado como parâmetro
	 * 
	 * @param idServentiaGabinetePrincipal
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public ServentiaDt consultarGabineteSubstitutoAtualSegundoGrau(String id_ServentiaCamaraPrincipal, String idServentiaGabinetePrincipal) throws Exception {	
		String Sql;
		ServentiaDt obTemp = null;
        ResultSetTJGO rs = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT sr.ID_SERV_RELACIONADA, s.ID_SERV, s.SERV_CODIGO, s.SERV, es.UF, sr.PRESIDENCIA, ss.SERV_SUBTIPO_CODIGO, st.SERV_TIPO_CODIGO, ";
		Sql += " sr.ID_SERV_SUBSTITUICAO, s1.SERV AS SERV_SUBSTITUICAO, sr.DATA_INICIAL_SUBSTITUICAO, sr.DATA_FINAL_SUBSTITUICAO, sr.RECEBE_PROC ";
		Sql += " FROM PROJUDI.SERV s ";
		Sql += " JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_REL  ";
		Sql += " JOIN PROJUDI.ESTADO es on s.ID_ESTADO_REPRESENTACAO = es.ID_ESTADO ";
		Sql += " LEFT JOIN PROJUDI.SERV_SUBTIPO ss on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		Sql += " LEFT JOIN PROJUDI.SERV_TIPO st on s.ID_SERV_TIPO = st.ID_SERV_TIPO ";
		Sql += " LEFT JOIN PROJUDI.SERV s1 on sr.ID_SERV_SUBSTITUICAO = s1.ID_SERV ";
		Sql += " WHERE sr.ID_SERV_PRINC = ?";       
        ps.adicionarLong(id_ServentiaCamaraPrincipal);
        Sql += " AND sr.ID_SERV_SUBSTITUICAO = ?"; 
        ps.adicionarLong(idServentiaGabinetePrincipal);
        Sql += " AND SYSDATE BETWEEN DATA_INICIAL_SUBSTITUICAO AND DATA_FINAL_SUBSTITUICAO ";
        
        try{
        	rs = consultar(Sql, ps);
            while (rs.next()) { 
            	obTemp = new ServentiaDt();
            	obTemp.setId_ServentiaRelacaoEdicao(rs.getString("ID_SERV_RELACIONADA"));
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("UF"));
				obTemp.setPresidencia(Funcoes.FormatarLogico(rs.getString("PRESIDENCIA")));		
				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaSubtipoCodigo(rs.getString("SERV_SUBTIPO_CODIGO"));
				obTemp.setId_ServentiaSubstituicao(rs.getString("ID_SERV_SUBSTITUICAO"));
	            obTemp.setServentiaSubstituicao(rs.getString("SERV_SUBSTITUICAO"));
	            obTemp.setDataInicialSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
	            obTemp.setDataFinalSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_FINAL_SUBSTITUICAO")));
	            obTemp.setRecebeProcesso(Funcoes.FormatarLogico(rs.getString("RECEBE_PROC")));              
            }
            // rs1.close();
        
        } finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
        return obTemp;
	}
	
    /**
     * Consulta as serventias relacionadas de um determinado tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return lista de dt das serventias relacionadas
     * @author aamoraes
     */
    public List consultarServentiasRelacionadas(String id_Serventia, int tipoServentia) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ServentiaRelacionadaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT DISTINCT ID_SERV_RELACAO, SERV_RELACAO, SERV_TIPO_CODIGO_RELACIONADA, ORDEM_TURMA_JULGADORA FROM PROJUDI.VIEW_SERV_RELACIONADA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";
        ps.adicionarLong(id_Serventia);
        Sql += " AND s.SERV_TIPO_CODIGO_RELACIONADA = ?";
        ps.adicionarLong(tipoServentia);
//        Sql += " AND s.RECEBE_PROC = ?";
//        ps.adicionarBoolean(true);
        Sql += " ORDER BY ORDEM_TURMA_JULGADORA, SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
                obTemp = new ServentiaRelacionadaDt();
                obTemp.setId_ServentiaRelacao(rs1.getString("ID_SERV_RELACAO"));
                obTemp.setServentiaRelacao(rs1.getString("SERV_RELACAO"));                
                obTemp.setOrdemTurmaJulgadora(rs1.getString("ORDEM_TURMA_JULGADORA") != null ? rs1.getString("ORDEM_TURMA_JULGADORA") : "");
                liTemp.add(obTemp);
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return liTemp;
    }
    
    /**
     * Limpa a ordem que a Serventia Relacionada (Gabinete) para a formação das Turmas Julgadoras nas Sessões
     * 
     * @param id_SeventiaPrincipal
     * @param id_SeventiaRelacao
     * @throws Exception
     * @author aamoraes
     */
    public void limpaOrdemTurmaJulgadora(String id_SeventiaPrincipal) throws Exception{
    	PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql="";
		
		Sql= "UPDATE PROJUDI.SERV_RELACIONADA";
		Sql+= " SET ORDEM_TURMA_JULGADORA = ?"; ps.adicionarLongNull();	
		Sql += " WHERE ID_SERV_PRINC  = ?"; ps.adicionarLong(id_SeventiaPrincipal);
		executarUpdateDelete(Sql,ps);
    }
	
	 /**
     * Atualiza a ordem que a Serventia Relacionada (Gabinete) para a formação das Turmas Julgadoras nas Sessões
     * 
     * @param id_SeventiaPrincipal
     * @param id_SeventiaRelacao
     * @throws Exception
     * @author aamoraes
     */
    public void atualizeOrdemTurmaJulgadora(String id_SeventiaPrincipal, String id_SeventiaRelacao, String ordem_turma) throws Exception{
    	PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql="";

		Sql= "UPDATE PROJUDI.SERV_RELACIONADA";
		Sql+= " SET ORDEM_TURMA_JULGADORA = ?"; ps.adicionarLong(ordem_turma);		
		Sql += " WHERE ID_SERV_PRINC  = ?"; ps.adicionarLong(id_SeventiaPrincipal);
		Sql += " AND ID_SERV_REL  = ?"; ps.adicionarLong(id_SeventiaRelacao);
		executarUpdateDelete(Sql,ps);
    }

	public ServentiaRelacionadaDt consultarId_ServentiaRelacionada(String id_serventia_principal, String id_serventia_relacionada) throws Exception {
        String Sql;
        ServentiaRelacionadaDt obTemp = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT * FROM PROJUDI.VIEW_SERV_RELACIONADA s";
        Sql += " WHERE s.ID_SERV_PRINC = ?";								ps.adicionarLong(id_serventia_principal);
        Sql += " AND s.ID_SERV_RELACAO = ?";									ps.adicionarLong(id_serventia_relacionada);
        
        try{
            rs1 = consultar(Sql, ps);
            if (rs1.next()) {
            	obTemp = new ServentiaRelacionadaDt();
            	associarDt(obTemp, rs1);
               
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        return obTemp;
	}
	
	protected void associarDt( ServentiaRelacionadaDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_SERV_RELACIONADA"));		
		Dados.setId_ServentiaPrincipal( rs.getString("ID_SERV_PRINC"));
		Dados.setServentiaPrincipal( rs.getString("SERV_PRINC"));		
		Dados.setId_ServentiaRelacao( rs.getString("ID_SERV_RELACAO"));
		Dados.setServentiaRelacionada(rs.getString("SERV_RELACIONADA"));
		Dados.setId_ServentiaRelacao( rs.getString("ID_SERV_RELACAO"));
		Dados.setServentiaRelacao( rs.getString("SERV_RELACAO"));
		Dados.setId_ServentiaTipoRelacionada( rs.getString("ID_SERV_TIPO_RELACIONADA"));
		Dados.setServentiaTipoCodigoRelacionada( rs.getString("SERV_TIPO_CODIGO_RELACIONADA"));
		Dados.setServentiaTipoRelacionada( rs.getString("SERV_TIPO_RELACIONADA"));
		Dados.setPresidencia( rs.getString("PRESIDENCIA"));
		Dados.setId_ServentiaSubstituicao( rs.getString("ID_SERV_SUBSTITUICAO"));
		Dados.setServentiaSubstituicao( rs.getString("SERV_SUBSTITUICAO"));
		Dados.setDataInicialSubstituicao(Funcoes.FormatarData(rs.getDate("DATA_INICIAL_SUBSTITUICAO")));
		Dados.setDataFinalSubstituicao(Funcoes.FormatarData(rs.getDate("DATA_FINAL_SUBSTITUICAO")));
		Dados.setRecebeProcesso(rs.getString("RECEBE_PROC"));
		Dados.setOrdemTurmaJulgadora(rs.getString("ORDEM_TURMA_JULGADORA"));
		Dados.setProbabilidade(rs.getString("PROBABILIDADE"));
		Dados.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
		Dados.setServentiaRelacaoCodigo(rs.getString("SERV_RELACAO_codigo"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}
	
	/**
     * Consulta as serventias relacionadas a serventia principal que está sendo
     * passada
     * 
     * @author jrcorrea
     */
    public List<ServentiaRelacionadaDt> consultarServentiasRelacionadas(String id_Serventia) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql = "SELECT * ";
        Sql += " FROM PROJUDI.VIEW_SERV_RELACIONADA ";
        Sql += " WHERE ID_SERV_PRINC = ?";										ps.adicionarLong(id_Serventia);
        Sql += " ORDER BY SERV_RELACAO ";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
            	ServentiaRelacionadaDt obTemp = new ServentiaRelacionadaDt();
            	associarDt(obTemp, rs1);
//                obTemp.setId(rs1.getString("ID_SERV_RELACAO"));
//                obTemp.setServentiaRelacao(rs1.getString("SERV_RELACAO"));
//                obTemp.setId_ServentiaTipoRelacionada(rs1.getString("ID_SERV_TIPO_RELACIONADA"));
//                obTemp.setServentiaTipoCodigoRelacionada(rs1.getString("SERV_TIPO_CODIGO_RELACIONADA"));
//                obTemp.setServentiaTipoRelacionada(rs1.getString("SERV_TIPO_RELACIONADA"));
//                obTemp.setPresidencia(Funcoes.FormatarLogico(rs1.getString("PRESIDENCIA")));
//                obTemp.setId_ServentiaSubstituicao(rs1.getString("ID_SERV_SUBSTITUICAO"));
//                obTemp.setServentiaSubstituicao(rs1.getString("SERV_SUBSTITUICAO"));
//                obTemp.setDataInicialSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
//                obTemp.setDataFinalSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_FINAL_SUBSTITUICAO")));
//                obTemp.setRecebeProcesso(Funcoes.FormatarLogico(rs1.getString("RECEBE_PROC")));
                liTemp.add(obTemp);
            }
            // rs1.close();
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp;
    }
}

