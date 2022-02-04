package br.gov.go.tj.projudi.ps;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PersistenciaException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.PreparedStatementTJGO.CampoTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class Persistencia implements Serializable {	
		
	/**
     * 
     */
    private static final long serialVersionUID = -2917808681372992090L;

    protected transient Connection Conexao;	
	
    public static final String ORDENACAO_PADRAO = "PROJUDI";

	protected int executarComando(String sql)  throws Exception{
	    PreparedStatement pst= null;
	    int inRetorno =-1;
		try{
			pst = Conexao.prepareStatement(sql);
			inRetorno = pst.executeUpdate();						
		} catch (Exception e) {
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS"), e);					
		} finally{
		    try{if (pst != null) pst.close();} catch(Exception e1) {}            
		}
		return inRetorno ;
	}
	
	protected int[] executarComandos(String sql)  throws Exception{
	    Statement pst= null;
	    int inRetorno[];
		try{
			pst = Conexao.createStatement();
			String[] stSql = sql.split(";");
			for (int i=0;i<stSql.length;i++)
				if(stSql[i].trim().length() != 0) {
					pst.addBatch(stSql[i]);
				}
						
			inRetorno = pst.executeBatch();
			
		} catch (Exception e) {
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS"), e);					
		} finally{
		    try{if (pst != null) pst.close();} catch(Exception e1) {}            
		}
		return inRetorno ;
	}
	
	protected void executarUpdateDeleteSemParametros(String sql)  throws Exception{
	    PreparedStatement pst= null;	    
		try{
			pst = Conexao.prepareStatement(sql);
			pst.executeUpdate();
			
		} catch (Exception e) {
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS"), e);					
		} finally{
		    try{if (pst != null) pst.close();} catch(Exception e1) {}            
		}
	}

	protected ResultSetTJGO  consultarSemParametros(String sql) throws Exception{
		ResultSet rs1 = null;
		PreparedStatement pst=null;
		ResultSetTJGO rsRetoro = null;		
		try{
			pst = Conexao.prepareStatement(sql);
			rs1 = pst.executeQuery();
			rsRetoro = new ResultSetTJGO(pst, rs1);
		}catch(Exception e){
		    try{if (pst != null) pst.close();} catch(Exception e1) {}			
		    throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS"), e);
		}
		
		return rsRetoro;
	}	
	
	/**
	 * Executa uma instrução de Update e Delete
	 * @param sql
	 * @param ps
	 * @return retorna a quantidade de registros afetados
	 * @throws Exception
	 * @author mmgomes
	 */
	protected int executarUpdateDelete(String sql, PreparedStatementTJGO ps)  throws Exception{	    	
		PreparedStatement pst= null;		   				
		try{			
			pst = obtenhaPreparedStatement(sql, ps);
			return pst.executeUpdate();	
		} catch (Exception e) {
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, ps.getValoresLog()), e);					
		} finally {
		    try{if (pst != null) pst.close();} catch(Exception e1) {}			
		}
	}
	
	/**
	 * Executa uma comando de Insert
	 * @param sql
	 * @param campoID
	 * @param ps
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected String executarInsert(String sql, String campoID, PreparedStatementTJGO ps)  throws Exception{	    	
		try{			
			return executarInsertOracle(sql, campoID, ps);	
		} catch (Exception e) {
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n campoID: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, ps.getValoresLog(), campoID), e);					
		}
	}

	/**
	 * Executa um comando de Insert no Oracle
	 * @param sql
	 * @param campoID
	 * @param ps
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private String executarInsertOracle(String sql, String campoID, PreparedStatementTJGO ps) throws Exception{
		CallableStatement cs = null;	    
	    String novoID = "";				
		try{			
			cs = obtenhaCallableStatement(sql, ps, campoID);
			cs.execute();
			novoID = cs.getString((ps.getListaParametros().size() + 1));			
		} finally{		                
			try{if (cs != null) cs.close();} catch(Exception e1) {}
		}
		return novoID;
	}
	
	/**
	 * Executa uma consulta e retorna um ResultSetTJGO
	 * @param sql
	 * @param ps
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected ResultSetTJGO consultar(String sql, PreparedStatementTJGO ps) throws Exception{
		ResultSet rs1 = null;
		PreparedStatement pst=null;
		ResultSetTJGO rsRetorno = null;		
		try{
			pst = obtenhaPreparedStatement(sql, ps);
			rs1 = pst.executeQuery();
			rsRetorno = new ResultSetTJGO(pst, rs1);
		}catch(Exception e){
		    try{if (pst != null) pst.close();} catch(Exception e1) {}
		    throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, ps.getValoresLog()), e);
		}
		
		return rsRetorno;
	}
	
	/**Executa uma consulta e inclui paginação
	 * 
	 * @param sql
	 * @param ps
	 * @param posicao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected ResultSetTJGO consultarPaginacao(String sql, PreparedStatementTJGO ps, String posicao) throws Exception{
		try {
			return consultar(incluaPaginacao(sql, posicao, ""), ps);	
		} catch (Exception e){
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n posicao: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, ps.getValoresLog(), posicao), e);
		}		
	}
	
	/**Executa uma consulta e inclui paginação
	 * 
	 * @param sql
	 * @param ps
	 * @param posicao
	 * @param quantidadePagina
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected ResultSetTJGO consultarPaginacao(String sql, PreparedStatementTJGO ps, String posicao, String quantidadeRegistrosPagina) throws Exception{
		try {
			return consultar(incluaPaginacao(sql, posicao, quantidadeRegistrosPagina), ps);	
		} catch (Exception e){
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n posicao: %s \n quantidadeRegistrosPagina: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, ps.getValoresLog(), posicao, quantidadeRegistrosPagina), e);
		}		
	}
	
	/**Executa uma consulta e inclui paginação
	 * 
	 * @param sql
	 * @param ps
	 * @param posicao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected ResultSetTJGO consultarPaginacaoSemParametros(String sql, String posicao) throws Exception{
		try {
			return consultarSemParametros(incluaPaginacao(sql, posicao, ""));	
		} catch (Exception e){
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n posicao: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS", posicao), e);
		}		
	}
	
	/**Executa uma consulta e inclui paginação
	 * 
	 * @param sql
	 * @param ps
	 * @param posicao
	 * @param quantidadePagina
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected ResultSetTJGO consultarPaginacaoSemParametros(String sql, String posicao, String quantidadeRegistrosPagina) throws Exception{
		try {
			return consultarSemParametros(incluaPaginacao(sql, posicao, quantidadeRegistrosPagina));	
		} catch (Exception e){
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n posicao: %s \n quantidadeRegistrosPagina: %s", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS", posicao, quantidadeRegistrosPagina), e);
		}		
	}
	
	/**
	 * Executa uma consulta incluindo limitação de registros no retorno
	 * @param sql - instrução sql
	 * @param ps - parametros de consulta
	 * @param limiteRegistros - limitacao de registros
	 * @return
	 * @throws Exception
	 * @author hmgodinho
	 */
	protected ResultSetTJGO consultarLimitacaoRegistros(String sql, PreparedStatementTJGO ps, int limiteRegistros) throws Exception{
		try {
			return consultar(incluirLimitacaoRegistros(sql, limiteRegistros), ps);	
		} catch (Exception e){
			throw new PersistenciaException(String.format("<{Erro ao executar instruções no banco de dados!}>\n Exception: %s \n SQL: %s \n Parâmetros: %s \n limiteRegistros: %s ", Funcoes.obtenhaConteudoPrimeiraExcecao(e), sql, "SEM PARÂMETROS", limiteRegistros), e);
		}		
	}
	
	/**
	 * Inclui paginação em uma consulta SQL
	 * @param sql
	 * @param posicao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private String incluaPaginacao(String sql, String posicao, String quantidadeRegistrosPagina) throws Exception{
		int posicaoInt = 0;
		int quantidadeRegistrosPaginaInt = 0;
		posicaoInt = Funcoes.StringToInt(posicao);
		
		//Se for menor que zero
		if (posicaoInt < 0)	posicaoInt = 0;
		
		if (quantidadeRegistrosPagina!=null) quantidadeRegistrosPaginaInt = Funcoes.StringToInt(quantidadeRegistrosPagina);
		 
		if (quantidadeRegistrosPaginaInt <= 0) quantidadeRegistrosPaginaInt = Configuracao.TamanhoRetornoConsulta;
		
		String sqlPaginacao = sql + " OFFSET " + (quantidadeRegistrosPaginaInt * posicaoInt) + 	 " ROWS FETCH NEXT  " +  quantidadeRegistrosPaginaInt + " ROWS ONLY";
		
//		sqlPaginacao = "SELECT * FROM (";
//		sqlPaginacao += "SELECT ROWNUM LINHA, TAB_INT.* FROM (";
//		sqlPaginacao += sql + ") TAB_INT";
//		sqlPaginacao += ") TAB_EXT";
//		sqlPaginacao += " WHERE LINHA > " + (quantidadeRegistrosPaginaInt * posicaoInt);
//		sqlPaginacao += " AND LINHA <= " + ((quantidadeRegistrosPaginaInt * posicaoInt) + quantidadeRegistrosPaginaInt);		
				
		return sqlPaginacao;
	}
	
	/**
	 * Incliu limitação de registros na consulta
	 * @param sql - instrução sql
	 * @param limiteRegistros - limite de registros que serão retornados
	 * @return instrução sql com a limitação
	 * @throws Exception
	 * @author hmgodinho
	 */
	private String incluirLimitacaoRegistros(String sql, int limiteRegistros) throws Exception{
		//Se for menor que zero
		if (limiteRegistros < 0)	limiteRegistros = 0;
		
		String sqlLimitacao = sql + " FETCH FIRST " + limiteRegistros + " rows only";
		
//		sqlLimitacao = "SELECT * FROM (";
//		sqlLimitacao += "SELECT ROWNUM LINHA, TAB_INT.* FROM (";
//		sqlLimitacao += sql + ") TAB_INT";
//		sqlLimitacao += ") TAB_EXT";
//		sqlLimitacao += " WHERE LINHA <= " + limiteRegistros;		
				
		return sqlLimitacao;
	}
	
	/**
	 * Cria um CallableStatement para que o novo Id Gerado seja retornado ao objeto chamador.
	 * @param comandoSQL
	 * @param pstjgo
	 * @param campoID
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private CallableStatement obtenhaCallableStatement(String comandoSQL, PreparedStatementTJGO pstjgo, String campoID) throws Exception{
		CallableStatement cs = Conexao.prepareCall("BEGIN " + comandoSQL + " returning " + campoID + " into ?; END;");	
		
		this.ajusteValoresPreparedStatement(cs, pstjgo);
		cs.registerOutParameter((pstjgo.getListaParametros().size() + 1), Types.VARCHAR);
		return cs;
	}
	
	/**
	 * Cria um PreparedStatement com o comando SQL passado.
	 * @param comandoSQL
	 * @param pstjgo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private PreparedStatement obtenhaPreparedStatement(String comandoSQL, PreparedStatementTJGO pstjgo) throws Exception{
		PreparedStatement pst = Conexao.prepareStatement(comandoSQL);
		this.ajusteValoresPreparedStatement(pst, pstjgo);
		return pst;
	}
	
	/**
	 * Ajusta os valores do PreparedStatement utilizando os registros inseridos no PreparedStatementTJGO
	 * @param ps
	 * @param pstjgo
	 * @throws Exception
	 * @author mmgomes
	 */
	private void ajusteValoresPreparedStatement(PreparedStatement ps, PreparedStatementTJGO pstjgo) throws Exception{
		CampoTJGO campo = null;
		for (int i = 0; i < pstjgo.getListaParametros().size(); i++){
			campo = (CampoTJGO) pstjgo.getListaParametros().get(i);
			if (campo.isNulo()){
				// O método setNull para o tipo booleano não aceita nulo, somente true e false...
				if (campo.getTipo() == Types.BOOLEAN) ps.setNull((i + 1), Types.INTEGER);
				else ps.setNull((i + 1), campo.getTipo());
			}
			else setValor((i + 1), campo, ps);							
		}
	}
	
	/**
	 * Ajusta no indice do PreparedStatement o valor inserido no objeto CampoTJGO. 
	 * @param indice
	 * @param campo
	 * @param ps
	 * @throws Exception
	 * @author mmgomes
	 */
	private void setValor(int indice, CampoTJGO campo, PreparedStatement ps) throws Exception{
		switch (campo.getTipo()) {
			case Types.VARCHAR:			
			case Types.CLOB:
				ps.setString(indice, String.valueOf(campo.getValor()));
				break;				
			case Types.DATE:
				java.util.Date valorDate = (java.util.Date) campo.getValor();
				ps.setDate(indice, new java.sql.Date(valorDate.getTime()));				
				break;				
			case Types.FLOAT:
				ps.setFloat(indice, (Float) campo.getValor());
				break;				
			case Types.BLOB:
				ps.setBytes(indice, (byte[]) campo.getValor());
				break;				
			case Types.BOOLEAN:
				ps.setBoolean(indice, (Boolean) campo.getValor());
				break;			
			case Types.INTEGER:			
				ps.setInt(indice, (Integer) campo.getValor());
				break;
			case Types.BIGINT:
				ps.setLong(indice, (Long) campo.getValor());				
				break;
			case Types.DECIMAL:
				ps.setBigDecimal(indice, (BigDecimal) campo.getValor());
				break;
			case Types.REAL:
				// Esta condição será extinta, estou utilizando aqui o type Real x Decimal como alias apenas para manter o funcionamento atual, posteriormente iremos utilizar apenas o bigdecimal.
				ps.setObject(indice, campo.getValor(), Types.DECIMAL);
				break;				
			case Types.TIMESTAMP:
				java.util.Date valorTimestamp = (java.util.Date) campo.getValor();				
				ps.setTimestamp(indice, new java.sql.Timestamp(valorTimestamp.getTime()));				
				break;
			default:		
				ps.setObject(indice, campo.getValor(), campo.getTipo());		
				
			
		}
	}
	
	/**
	 * Ajusta no indice do PreparedStatement o valor inserido no objeto CampoTJGO. 
	 * @param nomeSequencia
	 * @param campo
	 * @param ps 
	 * @author mmgomes
	 * @throws Exception 
	 */
	protected void reiniciarSequenciaNoOracle(String nomeSequencia, String numeroInicialStr) throws Exception{     
        ResultSetTJGO rs = null;
        long valorAtual;
        long proximoValor;
        long numeroInicial;
        
        try{
        	
        	// Tentamos converter o número recebido
        	numeroInicial = Funcoes.StringToLong(numeroInicialStr);
        	
        	// Alterando o valor de incremento da sequencia para 1.
        	executarUpdateDeleteSemParametros("ALTER SEQUENCE " + nomeSequencia + " INCREMENT BY 1");
        	
        	// Inicializando a sequencia.
        	executarUpdateDeleteSemParametros("SELECT " + nomeSequencia + ".NEXTVAL AS VALOR_ATUAL FROM DUAL"); 
        	
        	// Obtendo o valor atual da sequencia.        	             
            rs = consultarSemParametros("SELECT " + nomeSequencia + ".CURRVAL AS VALOR_ATUAL FROM DUAL");            
            if (rs.next()){          	
            	
            	valorAtual = rs.getLong("VALOR_ATUAL");
            	proximoValor = (valorAtual - numeroInicial);
            	
            	// Alterando o valor inicial da sequencia para zero.
            	executarUpdateDeleteSemParametros("ALTER SEQUENCE " + nomeSequencia + " MINVALUE 0");
            	
            	// Alterando o valor de incremento da sequencia para o valor atual.
            	executarUpdateDeleteSemParametros("ALTER SEQUENCE " + nomeSequencia + " INCREMENT BY -" + proximoValor);
            	
            	// Reiniciando o valor da sequencia.
            	executarUpdateDeleteSemParametros("SELECT " + nomeSequencia + ".NEXTVAL AS VALOR_ATUAL FROM DUAL"); 
            	
            	// Alterando o valor de incremento da sequencia para 1.
            	executarUpdateDeleteSemParametros("ALTER SEQUENCE " + nomeSequencia + " INCREMENT BY 1");
            }
        }finally{
        	if (rs != null) rs.close();
        }       
        
	}
	
	/**
	 *   Cria o sql de paginacao, para limitar os valores de controle de tamanhos e diminuir os codigos
	 * nas classes filhas
	 * @author Jesus Rodrigo
	 * @since 27/10/2011
	 * @param String posicao, posicao da paginacao
	 * @return
	 * @throws Exception 
	 */
	
	public String gerarJSON( long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas) throws Exception{
		
		JSONArray aJson = new JSONArray();
		JSONObject oJson = new JSONObject();
			     
	    oJson.put("id", "-50000");
		oJson.put("desc1", qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			oJson.put("desc"+i, "");
		}
		aJson.put(oJson);
		
		oJson = new JSONObject();
		oJson.put("id", "-60000");
		oJson.put("desc1", posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			oJson.put("desc"+i, "");
		}
		aJson.put(oJson);
		
		while (rs.next()){	
			oJson = new JSONObject();
			oJson.put("id", rs.getString("Id"));
			oJson.put("desc1", rs.getString("descricao1"));
			for (int i = 2; i <= qtdeColunas; i++) {
				if(rs.getString("descricao" + i) != null) {
					oJson.put("desc"+i, rs.getString("descricao" + i));
				} else {
					oJson.put("desc"+i, "");
				}
			}
			aJson.put(oJson);
		}
		
		return aJson.toString();
	}
	
	/**
	 *   Cria o sql de paginacao, para limitar os valores de controle de tamanhos e diminuir os codigos
	 * nas classes filhas
	 * @author Jesus Rodrigo
	 * @since 27/10/2011
	 * @param String posicao, posicao da paginacao
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	
	public String gerarJsonHash( long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas, UsuarioNe usuario) throws Exception{
			
		JSONArray aJson = new JSONArray();
		JSONObject oJson = new JSONObject();
			     
	    oJson.put("id", "-50000");
		oJson.put("desc1", qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			oJson.put("desc"+i, "");
		}
		aJson.put(oJson);
		
		oJson = new JSONObject();
		oJson.put("id", "-60000");
		oJson.put("desc1", posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			oJson.put("desc"+i, "");
		}
		aJson.put(oJson);
		
		while (rs.next()){	
			oJson = new JSONObject();
			oJson.put("id", rs.getString("Id"));
			oJson.put("desc1", rs.getString("descricao1"));
			for (int i = 2; i <= qtdeColunas; i++) {
				if(rs.getString("descricao" + i) != null) {
					oJson.put("desc"+i, rs.getString("descricao" + i));
				} else {
					oJson.put("desc"+i, "");
				}
			}
			oJson.put("hash", usuario.getCodigoHash(rs.getString("Id")));
			aJson.put(oJson);
		}
		
		return aJson.toString();
	}
	
	protected void libereResultset(ResultSetTJGO rs)
	{
		try{if (rs != null) rs.close();} catch(Exception e) {}
	}
	
	protected String obtenhaValorFiltroConsultarDescricaoCombo(String descricao)
	{
		String valorFiltro = null;
		
		if (descricao != null && descricao.trim().length() > 0) {
			String vetorDescricao[] = descricao.split(" ");
			if (vetorDescricao.length > 0) {
				valorFiltro = "%";
				for(int i = 0; i < vetorDescricao.length; i++)
				{
					valorFiltro += vetorDescricao[i];
					valorFiltro += "%";
				}				
			}
		}
		
		return valorFiltro;
	}
}
