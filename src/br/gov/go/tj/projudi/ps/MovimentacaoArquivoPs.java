package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MovimentacaoArquivoPs extends MovimentacaoArquivoPsGen {

    private static final long serialVersionUID = 20879048627150109L;

    public MovimentacaoArquivoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação	
	 * @author msapaula
	 */
	public String consultarArquivosMovimentacaoJSON(String id_Movimentacao, UsuarioNe usuarioNe ) throws Exception {
		StringBuilder stTemp = new StringBuilder();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();


		String Sql = " SELECT m.id_proc, m.Id_Movi_Arq, m.ID_ARQ, m.NOME_ARQ, m.ID_ARQ_TIPO, ARQ_TIPO, m.USU_ASSINADOR, m.RECIBO, m.Codigo_Temp, m.id_movi_arq_acesso  FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA m";

		Sql += " WHERE m.ID_MOVI = ?  ";
		Sql += " ORDER BY m.ID_ARQ ASC ";
		ps.adicionarLong(id_Movimentacao);

		try{
			rs1 = consultar(Sql, ps);
			stTemp.append("[");
			boolean boVirgula = false;
			int inAcessoUsuario = usuarioNe.getNivelAcesso();
			
			while (rs1.next()) {
								
				MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				int inAcessoArquivo = rs1.getInt("id_movi_arq_acesso");
				//vejo de o nivel de acesso do usuário e compativel com o nivel de acesso do arquivo
				if (inAcessoUsuario>=inAcessoArquivo){
					movimentacaoArquivoDt.setValido(true);
					movimentacaoArquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));					
				}else{
					movimentacaoArquivoDt.setValido(false);
					movimentacaoArquivoDt.setNomeArquivo(movimentacaoArquivoDt.getTipoAcesso(inAcessoArquivo));
				}
				movimentacaoArquivoDt.setId(rs1.getString("ID_MOVI_ARQ"));
				movimentacaoArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));				 
				movimentacaoArquivoDt.setArquivoTipoCodigo(rs1.getString("ID_ARQ_TIPO"));
				movimentacaoArquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				movimentacaoArquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				movimentacaoArquivoDt.setCodigoTemp(rs1.getString("Codigo_Temp"));
				if (rs1.getString("USU_ASSINADOR")!=null) {
					//se estiver assinando o recibo pode ser gerado
					movimentacaoArquivoDt.setRecibo("true");
				}
				movimentacaoArquivoDt.setHash(usuarioNe.getCodigoHash(movimentacaoArquivoDt.getId()+rs1.getString("ID_PROC")));
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(rs1.getString("id_movi_arq_acesso") );
				
				movimentacaoArquivoDt.setAcessoArquivo(inAcessoArquivo);
				movimentacaoArquivoDt.setAcessoUsuario(inAcessoUsuario);
				
				if (boVirgula){
					stTemp.append(","); 
				}else{
					boVirgula = true;
				}
				stTemp.append(movimentacaoArquivoDt.GerarListaMovimentacaoCapaProcessoJSON());
			}
			stTemp.append("]");
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return stTemp.toString();
	}
	
	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao, String id_Processo) throws Exception {
		List arquivos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String Sql = " SELECT * FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA m";
		Sql += " WHERE m.ID_MOVI = ? AND m.ID_PROC = ? ";
		Sql += " ORDER BY m.ID_ARQ";
		ps.adicionarLong(id_Movimentacao);
		ps.adicionarLong(id_Processo);
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				this.associarDt(movimentacaoArquivoDt, rs1);
				if (!movimentacaoArquivoDt.getCodigoTemp().equals(String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL))) movimentacaoArquivoDt.setValido(false);

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				movimentacaoArquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(rs1.getString("ID_MOVI_ARQ_ACESSO"));
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);
				

				arquivos.add(movimentacaoArquivoDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return arquivos;
	}
	
	
	 /**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * 
	 * @author lsbernardes
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao) throws Exception {
		List arquivos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String Sql = " SELECT * FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA m";
		Sql += " WHERE m.ID_MOVI = ? ";
		ps.adicionarLong(id_Movimentacao);
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				this.associarDt(movimentacaoArquivoDt, rs1);
				if (!movimentacaoArquivoDt.getCodigoTemp().equals(String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL))) movimentacaoArquivoDt.setValido(false);

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setContentType(rs1.getString("CONTENT_TYPE"));	
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(rs1.getString("ID_MOVI_ARQ_ACESSO"));
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);

				arquivos.add(movimentacaoArquivoDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return arquivos;
	}

	/**
	 * Consultar arquivos e seu conteudo de uma determinada movimentação
	 * 
	 * @author lsbernardes/ jrcorrea
	 * @param MovimentacaoDt movimentacaoDt, objeto da movimentação
	 */
	public List consultarConteudoArquivosMovimentacao(String id_movimentacao, int inAcessoUsuario) throws Exception {
		List arquivos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String Sql = " SELECT ma.ID_MOVI_ARQ, ma.CODIGO_TEMP, a.ID_ARQ, a.RECIBO, a.NOME_ARQ, a.USU_ASSINADOR, a.DATA_INSERCAO,  ID_MOVI_ARQ_ACESSO, a.CAMINHO ";
		Sql += " FROM PROJUDI.MOVI_ARQ ma ";
		Sql += "    JOIN PROJUDI.ARQ a ON a.ID_ARQ = ma.ID_ARQ ";
		Sql += " WHERE ma.ID_MOVI = ? ";					ps.adicionarLong(id_movimentacao);	
		Sql += " ORDER BY a.ID_ARQ ASC ";
		
		try{
			rs1 = consultar(Sql, ps);
					
			
			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();
				
				int inAcessoArquivo = rs1.getInt("id_movi_arq_acesso");

				MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setAssinado(true);
				arquivoDt.setCaminho(rs1.getString("CAMINHO"));
				movimentacaoArquivoDt.setId(rs1.getString("ID_MOVI_ARQ"));
				movimentacaoArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(rs1.getString("ID_MOVI_ARQ_ACESSO"));
				movimentacaoArquivoDt.setId_Arquivo(arquivoDt.getId());
				movimentacaoArquivoDt.setId_Movimentacao(id_movimentacao);
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);
				movimentacaoArquivoDt.setAcessoArquivo(inAcessoArquivo);
				movimentacaoArquivoDt.setAcessoUsuario(inAcessoUsuario);

				arquivos.add(movimentacaoArquivoDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return arquivos;
	}

	/**
	 * Consultar todas as movimentacoes e arquivos de um processo
	 * 
	 * @author jrcorrea
	 * @param MovimentacaoDt movimentacaoDt, objeto da movimentação
	 */
	public Map consultarArquivosMovimentacoes(String id_processo, int inAcessoUsuario) throws Exception {
		Map maMovimentacoes = new HashMap();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String Sql = " SELECT mo.id_movi, ma.ID_MOVI_ARQ, ma.CODIGO_TEMP, a.ID_ARQ, a.RECIBO, a.CONTENT_TYPE, a.NOME_ARQ, a.USU_ASSINADOR,";			
		Sql += " length(a.ARQ) as TAMANHO, ";		
		Sql += " a.DATA_INSERCAO, ID_MOVI_ARQ_ACESSO, a.CAMINHO, a.CODIGO_TEMP AS CODIGO_TEMP_ARQ";
		Sql += " FROM PROJUDI.MOVI mo ";
		Sql += "    JOIN PROJUDI.MOVI_ARQ ma on mo.id_movi= ma.id_movi";
		Sql += "    JOIN PROJUDI.ARQ a ON a.ID_ARQ = ma.ID_ARQ ";
		Sql += " WHERE mo.ID_proc = ? ";									ps.adicionarLong(id_processo);	
		Sql += " ORDER BY ma.ID_MOVI, a.ID_ARQ ASC ";
		
		try{
			rs1 = consultar(Sql, ps);
						
			List lisArquivoMovimentacao = null;
			String id_movimentacao = "-100";
			while (rs1.next()) {				

				MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				
				int inAcessoArquivo = rs1.getInt("id_movi_arq_acesso");
				
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setContentType(rs1.getString("CONTENT_TYPE"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setAssinado(true);
				arquivoDt.setCaminho(rs1.getString("CAMINHO"));
				arquivoDt.setArquivo(rs1.getString("TAMANHO"));	
				if (arquivoDt.isArquivoFisico() && Funcoes.StringToLong(rs1.getString("CODIGO_TEMP_ARQ")) > 0) {
					arquivoDt.setArquivo(rs1.getString("CODIGO_TEMP_ARQ"));	
				}
				
				movimentacaoArquivoDt.setId(rs1.getString("ID_MOVI_ARQ"));
				movimentacaoArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(rs1.getString("ID_MOVI_ARQ_ACESSO"));
				movimentacaoArquivoDt.setId_Arquivo(arquivoDt.getId());
				movimentacaoArquivoDt.setId_Movimentacao(rs1.getString("id_movi"));
				movimentacaoArquivoDt.setAcessoArquivo(inAcessoArquivo);
				movimentacaoArquivoDt.setAcessoUsuario(inAcessoUsuario);
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);
				//para cada movimentação crio um lista com os arquivos dela
				if (!id_movimentacao.equals(movimentacaoArquivoDt.getId_Movimentacao())){
					lisArquivoMovimentacao = new ArrayList();
					id_movimentacao = movimentacaoArquivoDt.getId_Movimentacao();
					lisArquivoMovimentacao.add(movimentacaoArquivoDt);
					//no map para cada id_movimentacao terei um lista com os arquivos
					maMovimentacoes.put(id_movimentacao, lisArquivoMovimentacao);
				}else{
					lisArquivoMovimentacao.add(movimentacaoArquivoDt);
				}
				
			}

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return maMovimentacoes;
	}
	
	/**
	 * Consulta detalhada de dados de MovimentacaoArquivo
	 * 
	 * @param id_MovimentacaoArquivo, identificação de MovimentacaoArquivo
	 * 
	 * @author msapaula
	 */
	public MovimentacaoArquivoDt consultarIdCompleto(String id_MovimentacaoArquivo) throws Exception {
		String Sql;
		MovimentacaoArquivoDt movimentacaoArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA WHERE ID_MOVI_ARQ = ?";
		ps.adicionarLong(id_MovimentacaoArquivo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				associarDt(movimentacaoArquivoDt, rs1);
				movimentacaoArquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return movimentacaoArquivoDt;
	}
	
	public String consultarIdProcesso(String idMovimentacaoArquivo) throws Exception {
		String Sql;
		String idProcesso = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA WHERE ID_MOVI_ARQ = ?";
		ps.adicionarLong(idMovimentacaoArquivo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idProcesso = rs1.getString("ID_PROC");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return idProcesso;
	}
	/**
	 * Consulta id do Arquivo
	 * @param id_MovimentacaoArquivo, identificação de MovimentacaoArquivo
	 * 
	 * @author msapaula
	 */
	public String consultarIdArquivo(String id_MovimentacaoArquivo) throws Exception {
		String Sql;
		String stRetorno = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_ARQ FROM PROJUDI.MOVI_ARQ WHERE ID_MOVI_ARQ = ? ";		ps.adicionarLong(id_MovimentacaoArquivo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				stRetorno = rs1.getString("ID_ARQ");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return stRetorno;
	}
	
	/**
	 * Consulta detalhada de dados de MovimentacaoArquivo, retornando dados do processo e movimentação
	 * 
	 * @param id_MovimentacaoArquivo, identificação de MovimentacaoArquivo
	 * 
	 * @author msapaula
	 */
	public MovimentacaoArquivoDt consultarDadosMovimentacaoArquivo(String id_MovimentacaoArquivo) throws Exception {
		String Sql;
		MovimentacaoArquivoDt movimentacaoArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_MOV_ARQ_COMPLETA WHERE ID_MOVI_ARQ = ?";
		ps.adicionarLong(id_MovimentacaoArquivo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				associarDt(movimentacaoArquivoDt, rs1);
				movimentacaoArquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));

				// Captura dados do processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV_PROC"));

				// Seta no objeto
				MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setProcessoDt(processoDt);
				movimentacaoArquivoDt.setMovimentacaoDt(movimentacaoDt);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return movimentacaoArquivoDt;
	}

	/**
	 * Altera o status de um arquivo em uma movimentação
	 * 
	 * @param movimentacaoArquivodt, dt com dados do arquivo na movimentação
	 * @param novoStatus, novo status do arquivo 0 - normal, 1 - vírus, 2 - restrição download
	 * 
	 * @author msapaula
	 */
	public void alterarStatusMovimentacaoArquivo(String id, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.MOVI_ARQ SET ID_MOVI_ARQ_ACESSO = ?";		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_MOVI_ARQ = ?";								ps.adicionarLong(id);

			executarUpdateDelete(Sql, ps);

	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_MOVI_ARQ AS ID, NOME_ARQ AS DESCRICAO1 FROM PROJUDI.VIEW_MOVI_ARQ WHERE NOME_ARQ LIKE ?";
		stSql+= " ORDER BY NOME_ARQ ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MOVI_ARQ WHERE NOME_ARQ LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	
	/**
	 * Consulta os dados do arquivo que deverá ser publicado atráves de uma movimentação processual.
	 * Procura movimentação de publicação.
	 * @return
	 * @throws Exception
	 */
	public MovimentacaoArquivoDt consultarPorIdArquivo(String id_arquivo) throws Exception {
		
		MovimentacaoArquivoDt movimentacaoArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.id_proc, p.proc_numero, p.digito_verificador, p.forum_codigo, p.ano" + 
				 " , s.id_serv, s.serv, m.id_movi, m.data_realizacao as data_publicacao, atp.arq_tipo, atp.arq_tipo_codigo" +
				 " , u.id_usu" + 
				 " , CASE WHEN (g.grupo is not null) THEN UPPER(u.nome || ' - ('|| g.grupo || ')') ELSE UPPER(u.nome) END AS realizador" +					 
				 " , a.*" +					  
				 " FROM projudi.movi_arq ma " + 
				 " JOIN projudi.movi m on ma.id_movi = m.id_movi " +
				 " JOIN projudi.usu_serv us on m.id_usu_realizador = us.id_usu_serv" + 
				 " LEFT JOIN projudi.usu_serv_grupo usg on usg.id_usu_serv = us.id_usu_serv" + 
				 " LEFT JOIN projudi.grupo g on usg.id_grupo = g.id_grupo and g.grupo_codigo in (13, 31, 26, 24, 41, 51, 91, 92)" + 
				 " JOIN projudi.usu u on us.id_usu = u.id_usu" + 
				 " JOIN projudi.proc p on p.id_proc = m.id_proc " + 					 
				 " JOIN projudi.serv s on p.id_serv = s.id_serv " +
				 " JOIN projudi.arq a on ma.id_arq = a.id_arq " +
				 " JOIN projudi.arq_tipo atp on a.id_arq_tipo = atp.id_arq_tipo" +
				 " WHERE ma.id_arq = ?";
				
		ps.adicionarLong(id_arquivo);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			
			if (rs1.next()) {
				
				// Processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));				
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setServentia(rs1.getString("SERV"));

				// Arquivo
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setContentType(rs1.getString("CONTENT_TYPE"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setArquivo(rs1.getBytes("ARQ"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId(rs1.getString("ID_MOVI"));
				movimentacaoDt.setUsuarioRealizador(rs1.getString("REALIZADOR"));
				movimentacaoDt.setId_UsuarioRealizador(rs1.getString("ID_USU"));
				movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_PUBLICACAO")));
				movimentacaoDt.setProcessoDt(processoDt);
				
				movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);
				movimentacaoArquivoDt.setMovimentacaoDt(movimentacaoDt);
			}
				
        } finally{
             try {if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
				
		return movimentacaoArquivoDt;
		
	}
	
	/**
	 * Consulta os dados do arquivo que deverá ser publicado, fora do processo, isto é, sem vinculação explícita
	 * com movimentação. Apenas procura a pendência de publicação (PEND código 15) e os dados do arquivo (PEND_ARQ)
	 * @param id_arquivo
	 * @return
	 * @throws Exception
	 * 
	 * Ps: melhorar a consulta para olhar em pend e pend_final
	 */
	public MovimentacaoArquivoDt consultarArquivoPublicadoForaProcesso (String id_arquivo) throws Exception {
		
		MovimentacaoArquivoDt movimentacaoArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.id_arq"); 
		sql.append(" , a.id_arq_tipo"); 
		sql.append(" , a.nome_arq"); 
		sql.append(" , a.content_type"); 
		sql.append(" , a.recibo");
		sql.append(" , a.data_insercao"); 
		sql.append(" , a.arq");
		sql.append(" , atp.arq_tipo_codigo"); 
		sql.append(" , atp.arq_tipo");
		sql.append(" , s.id_serv");
		sql.append(" , s.serv");
		sql.append(" , u.id_usu");
		sql.append(" , u.nome as realizador");
		sql.append(" , vw.data_inicio as data_publicacao");
		sql.append(" , null id_movi");
		sql.append(" , null as id_proc");
		sql.append(" , null as proc_numero");
		sql.append(" , null as digito_verificador");
		sql.append(" , null as forum_codigo");
		sql.append(" , null as ano");
		sql.append(" FROM (");
		sql.append(" SELECT pe.id_pend, pe.id_pend_tipo, pe.id_pend_status, pe.id_usu_cadastrador, pe.data_inicio, pa.id_arq, pr.id_usu_resp, pr.id_serv, pe.id_movi");
		sql.append(" FROM projudi.pend pe");
		sql.append(" JOIN projudi.pend_arq pa ON pe.id_pend = pa.id_pend");
		sql.append(" JOIN projudi.pend_resp pr on pe.id_pend = pr.id_pend");
		sql.append(" UNION ");
		sql.append(" SELECT pf.id_pend, pf.id_pend_tipo, pf.id_pend_status, pf.id_usu_cadastrador, pf.data_inicio, pfa.id_arq, pfr.id_usu_resp, pfr.id_serv, pf.id_movi");
		sql.append(" FROM projudi.pend_final pf");
		sql.append(" JOIN projudi.pend_final_arq pfa ON pf.id_pend = pfa.id_pend");
		sql.append(" JOIN projudi.pend_final_resp pfr on pf.id_pend = pfr.id_pend");
		sql.append(" ) vw ");
		sql.append(" JOIN projudi.arq a on a.id_arq = vw.id_arq");
		sql.append(" JOIN projudi.arq_tipo atp ON a.id_arq_tipo = atp.id_arq_tipo");
		sql.append(" JOIN projudi.pend_tipo pt on pt.id_pend_tipo = vw.id_pend_tipo AND pt.pend_tipo_codigo = " + PendenciaTipoDt.PUBLICACAO_PUBLICA);
		sql.append(" JOIN projudi.pend_status ps on ps.id_pend_status = vw.id_pend_status  AND ps.pend_status_codigo = " + PendenciaStatusDt.ID_CUMPRIDA);
		sql.append(" JOIN projudi.serv s on s.id_serv = vw.id_serv");
		sql.append(" JOIN projudi.usu_serv us ON vw.id_usu_cadastrador = us.id_usu_serv AND us.ativo = 1"); 
		sql.append(" JOIN projudi.usu u ON us.id_usu = u.id_usu");
		sql.append(" WHERE vw.id_movi IS NULL AND vw.id_arq = ?");
		
		ps.adicionarLong(id_arquivo);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			
			if (rs1.next()) {
				
				// Processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));				
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setServentia(rs1.getString("SERV"));

				// Arquivo
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setContentType(rs1.getString("CONTENT_TYPE"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setArquivo(rs1.getBytes("ARQ"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId(rs1.getString("ID_MOVI"));
				movimentacaoDt.setUsuarioRealizador(rs1.getString("REALIZADOR"));
				movimentacaoDt.setId_UsuarioRealizador(rs1.getString("ID_USU"));
				movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_PUBLICACAO")));
				movimentacaoDt.setProcessoDt(processoDt);
				
				movimentacaoArquivoDt = new MovimentacaoArquivoDt();
				movimentacaoArquivoDt.setArquivoDt(arquivoDt);
				movimentacaoArquivoDt.setMovimentacaoDt(movimentacaoDt);
			}
				
        } finally{
             try {if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
				
		return movimentacaoArquivoDt;
		
	}
	
	public void inserir(MovimentacaoArquivoDt dados) throws Exception {

		String stSqlCampos="";				
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//
		stSqlCampos= "INSERT INTO PROJUDI.MOVI_ARQ (ID_ARQ, ID_MOVI, ID_MOVI_ARQ_ACESSO, CODIGO_TEMP) "; 
		
		stSqlCampos +=  " Values ( ?, ?, ?, ?)";
		
		 ps.adicionarLong(dados.getId_Arquivo());  			
		 ps.adicionarLong(dados.getId_Movimentacao());		 
		 ps.adicionarLong(dados.getId_MovimentacaoArquivoAcesso());		
		 if (dados.getCodigoTemp() == null || dados.getCodigoTemp().trim().length() == 0) {
			 ps.adicionarLongNull();
		 } else {
			 ps.adicionarLong(dados.getCodigoTemp());
		 }
		
		dados.setId(executarInsert(stSqlCampos,"ID_MOVI_ARQ",ps));
		 
	}
	
	public void alterar(MovimentacaoArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMovimentacaoArquivoalterar()");

		stSql= "UPDATE PROJUDI.MOVI_ARQ SET  ";
		stSql+= "ID_ARQ = ?";		 			ps.adicionarLong(dados.getId_Arquivo());  
		stSql+= ",ID_MOVI = ?";		 			ps.adicionarLong(dados.getId_Movimentacao());
		stSql+= ",ID_MOVI_ARQ_ACESSO = ?";		ps.adicionarLong(dados.getId_MovimentacaoArquivoAcesso());

		stSql += " WHERE ID_MOVI_ARQ  = ? "; 	ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 
	
	public List consultarArquivoMovimentacao(String id_Movimentacao) throws Exception {
		List tempList = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String Sql = "SELECT ID_ARQ FROM PROJUDI.MOVI_ARQ WHERE ID_MOVI = ? AND ID_MOVI_ARQ_ACESSO <= ? ";
		Sql += " ";
		ps.adicionarLong(id_Movimentacao);
		ps.adicionarLong(MovimentacaoArquivoDt.ACESSO_NORMAL);
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				tempList.add(rs1.getString("ID_ARQ"));
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return tempList;
	}
	
	public String consultarNumeroProcesso(String id_movimentacaoarquivo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String numeroProcesso = "";

		stSql = "SELECT P.PROC_NUMERO, P.DIGITO_VERIFICADOR FROM MOVI_ARQ MA INNER JOIN MOVI M ON M.ID_MOVI=MA.ID_MOVI INNER JOIN PROC P ON M.ID_PROC=P.ID_PROC WHERE MA.ID_MOVI_ARQ = ?";
		ps.adicionarLong(id_movimentacaoarquivo);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				numeroProcesso = rs1.getString("PROC_NUMERO")+"."+rs1.getString("DIGITO_VERIFICADOR");
			}
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return numeroProcesso;
	}

}
