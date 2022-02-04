package br.gov.go.tj.projudi.ps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.AudienciaPublicadaDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoJSON;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaDRSNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

// ---------------------------------------------------------
public class MovimentacaoPs extends MovimentacaoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -1266391343012817348L;

    @SuppressWarnings("unused")
	private MovimentacaoPs( ) {}
    
    public MovimentacaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir para setar DataRealizacao com a data do banco
	 */
	public void inserir(MovimentacaoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//dados.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));

		SqlCampos = "INSERT INTO PROJUDI.MOVI ("; 
		SqlValores = " Values (";
		
		if (!(dados.getId_MovimentacaoTipo().length() == 0)){
			SqlCampos += "ID_MOVI_TIPO";
			SqlValores += "?";
			ps.adicionarLong(dados.getId_MovimentacaoTipo());
		}else if(!(dados.getMovimentacaoTipoCodigo().length() == 0)){
			SqlCampos += "ID_MOVI_TIPO";
			SqlValores += "(SELECT ID_MOVI_TIPO FROM PROJUDI.MOVI_TIPO WHERE MOVI_TIPO_CODIGO = ?)";			
			ps.adicionarLong(dados.getMovimentacaoTipoCodigo());
		}	
		if (!(dados.getId_Processo().length() == 0)){
			SqlCampos += ",ID_PROC";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Processo());			
		}
		if (!(dados.getId_UsuarioRealizador().length() == 0)){
			SqlCampos += ",ID_USU_REALIZADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioRealizador());	
		}
		if (!(dados.getComplemento().length() == 0)){
			SqlCampos += ",COMPLEMENTO";
			SqlValores += ",?";
			ps.adicionarString(dados.getComplemento());			
		}
		if (!(dados.getCodigoTemp().length() == 0)){
			SqlCampos += ",CODIGO_TEMP";
			SqlValores += ",?";
			ps.adicionarString(dados.getCodigoTemp());			
		}		
		SqlCampos += ",DATA_REALIZACAO";
		SqlValores += ",?";
		ps.adicionarDateTime(new Date());
		if (!(dados.getPalavraChave().length() == 0)){
			SqlCampos += ",PALAVRA_CHAVE";
			SqlValores += ",?";
			ps.adicionarString(dados.getPalavraChave());
		}	

		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
		
		dados.setId(executarInsert(Sql, "ID_MOVI", ps));

	}

	protected void consultarTemp(MovimentacaoDt dados) throws Exception{
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT m.MOVI_TIPO, m.ID_MOVI FROM PROJUDI.VIEW_MOVI m WHERE CODIGO_TEMP = ?";
		ps.adicionarLong(dados.getCodigoTemp());
		try{

			rs1 = consultar(Sql, ps);
			////System.out.println("......Executar Query OK");

			if (rs1.next()) {
				dados.setId(rs1.getString("ID_MOVI"));
				dados.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));

				dados.setCodigoTemp("");
				alterar(dados);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}

	/**
	 * Método que busca as movimentações de um processo
	 * 
	 * @param id_Processo, identificador do processo
	 * @author msapaula
	 */
	public List<MovimentacaoDt> consultarMovimentacoesProcesso(String id_Processo, boolean possuiAutorizacaoParaBaixarVisualizarVideo, int nivelAcessoUsuario) throws Exception {

		String sql;
		List<MovimentacaoDt> listaMovimentacoes = new ArrayList<>();
		MovimentacaoDt movimentacaoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT m.ID_MOVI, m.MOVI_TIPO, m.COMPLEMENTO, m.DATA_REALIZACAO, m.ID_USU_REALIZADOR,";
		sql += "m.USU_REALIZADOR, m.NOME_USU_REALIZADOR, m.CODIGO_TEMP,";
		sql += " (SELECT COUNT(1) as QtdArquivo FROM PROJUDI.MOVI_ARQ ma";
		sql += "	WHERE ma.ID_MOVI = m.ID_MOVI) as QtdArquivo";
		sql += " , m.MOVI_TIPO_CODIGO";
		sql += " FROM PROJUDI.VIEW_MOV_COMPLETA m";
		sql += " WHERE m.ID_PROC= ?";
		ps.adicionarLong(id_Processo);
		/*sql += " GROUP BY m.ID_MOVI, m.MOVI_TIPO, m.COMPLEMENTO, m.DATA_REALIZACAO, m.ID_USU_REALIZADOR,";
		sql += " m.USU_REALIZADOR, m.NOME_USU_REALIZADOR, m.CODIGO_TEMP";
		sql += " ORDER BY m.ID_MOVI";*/
		sql += " ORDER BY m.ID_MOVI, m.MOVI_TIPO, m.COMPLEMENTO, m.DATA_REALIZACAO, m.ID_USU_REALIZADOR,";
		sql += " m.USU_REALIZADOR, m.NOME_USU_REALIZADOR, m.CODIGO_TEMP";

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId(rs1.getString("ID_MOVI"));
				movimentacaoDt.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				movimentacaoDt.setMovimentacaoTipoCodigo(rs1.getString("MOVI_TIPO_CODIGO"));
				movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REALIZACAO")));
				movimentacaoDt.setId_UsuarioRealizador(rs1.getString("ID_USU_REALIZADOR"));
				movimentacaoDt.setUsuarioRealizador(rs1.getString("USU_REALIZADOR"));
				movimentacaoDt.setNomeUsuarioRealizador(rs1.getString("NOME_USU_REALIZADOR"));
				movimentacaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				
				int qtde = rs1.getInt("QtdArquivo");

				if (qtde > 0) movimentacaoDt.setTemArquivos(true);
								
				switch (Funcoes.StringToInt(movimentacaoDt.getCodigoTemp())) {
					case MovimentacaoArquivoDt.ACESSO_BLOQUEADO:
						movimentacaoDt.setMovimentacaoTipo("Movimentação Bloqueada");
						movimentacaoDt.setValida(false);
						break;
					case MovimentacaoArquivoDt.ACESSO_BLOQUEADO_VIRUS:
						movimentacaoDt.setMovimentacaoTipo("Movimentação Bloqueada por Virus");
						movimentacaoDt.setValida(false);
						break;
					case MovimentacaoArquivoDt.ACESSO_BLOQUEADO_ERRO_MIGRACAO:
						movimentacaoDt.setMovimentacaoTipo("Movimentação Bloqueada por Erro de Migração");
						movimentacaoDt.setValida(false);
						break;
					case MovimentacaoArquivoDt.ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO:
						if (nivelAcessoUsuario>=MovimentacaoArquivoDt.ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO){
							movimentacaoDt.setValida(true);
						}else{
							movimentacaoDt.setMovimentacaoTipo("Movimentação Visível aos Advs, Delegacia, Ministério Público, Cartório e ao Magistrado");
							movimentacaoDt.setValida(false);
						}
						break;
					case MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO:
						if (nivelAcessoUsuario>=MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO){
							movimentacaoDt.setValida(true);
						}else{
							movimentacaoDt.setMovimentacaoTipo("Movimentação Visível à Delegacia, Ministério Público, Cartório e ao Magistrado");
							movimentacaoDt.setValida(false);
						}
						break;
					case MovimentacaoArquivoDt.ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO:
						if (nivelAcessoUsuario>=MovimentacaoArquivoDt.ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO){
							movimentacaoDt.setValida(true);
						}else{
							movimentacaoDt.setMovimentacaoTipo("Movimentação Visível ao Ministério Público, Cartório e ao Magistrado");
							movimentacaoDt.setValida(false);
						}
						break;
					case MovimentacaoArquivoDt.ACESSO_SOMENTE_CARTORIO_MAGISTRADO:
						if (nivelAcessoUsuario>=MovimentacaoArquivoDt.ACESSO_SOMENTE_CARTORIO_MAGISTRADO){
							movimentacaoDt.setValida(true);
						}else{
							movimentacaoDt.setMovimentacaoTipo("Movimentação Visível ao Cartório e ao Magistrado");
							movimentacaoDt.setValida(false);
						}
						break;
					case MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO:
						if (nivelAcessoUsuario>=MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO){
							movimentacaoDt.setValida(true);
						}else{
							movimentacaoDt.setMovimentacaoTipo("Movimentação visível somente ao Magistrado");
							movimentacaoDt.setValida(false);
						}
						break;
					default:
						movimentacaoDt.setComplemento(rs1.getString("COMPLEMENTO"));
						movimentacaoDt.setValida(true);
				}
				
				if (movimentacaoDt.getCodigoTemp().equals("1")) movimentacaoDt.setValida(false);
				
				monteHashComplementoAudienciaPublicada(movimentacaoDt, possuiAutorizacaoParaBaixarVisualizarVideo);

				listaMovimentacoes.add(movimentacaoDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaMovimentacoes;
	}
	
	/**
	 * Método que busca as movimentações de sentenca de um processo físico
	 * 
	 * @param id_Processo, identificador do processo
	 * @author acbloureiro
	 */
	public List<MovimentacaoDt> consultarMovimentacoesProcessoFisico(String id_Processo, int nivelAcessoUsuario) throws Exception {

		String sql;
		List<MovimentacaoDt> listaMovimentacoes = new ArrayList<>();
		MovimentacaoDt movimentacaoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		/* para os movimentos 277 e 327 que serão usados apenas para estatística - mostra para o usuário o pai (com resolução 10076 e sem resolução 10062) */
		/* de acordo com grupo TPU, todos os classificados com resolução de mérito serão movimento 221 do cnj - projuid 277 e sem mérito 459 do cnj - projudi 327 */
		
		sql = " SELECT tab.ID_SPGU_SENTENCA, tab.ID_MOVI_TIPO, mt.MOVI_TIPO, tab.DATA_SENTENCA, tab.ID_USU, tab.NOME from ( ";
		sql += " SELECT m.ID_SPGU_SENTENCA, case m.ID_MOVI_TIPO when 277 then 10076 when 327 then 10062 else m.id_movi_tipo end AS ID_MOVI_TIPO, m.DATA_SENTENCA, m.ID_USU, u.nome ";
		sql += " FROM PROJUDI.SPGU_SENTENCA m left join usu u on u.id_usu = m.id_usu ";
		sql += " WHERE m.ID_PROC= ?";
		ps.adicionarLong(id_Processo);
		sql += " ORDER BY m.ID_SPGU_SENTENCA ) tab ";
		sql += " inner join movi_tipo mt on mt.id_movi_tipo = tab.id_movi_tipo  ";

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId(rs1.getString("ID_SPGU_SENTENCA"));
				movimentacaoDt.setId_MovimentacaoTipo(rs1.getString("ID_MOVI_TIPO"));
				movimentacaoDt.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_SENTENCA")));
				movimentacaoDt.setId_UsuarioRealizador(rs1.getString("ID_USU"));
				movimentacaoDt.setNomeUsuarioRealizador(rs1.getString("NOME"));
				listaMovimentacoes.add(movimentacaoDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaMovimentacoes;
	}
	

	/**
	 * Método que verifica se processo possui movimentação do tipo NÃO CONHECIDO
	 * 
	 * @param id_Processo, identificador do processo
	 * @author lsbernardes
	 */
	public boolean possuiMovimentacaoNaoConhecido(String id_Processo) throws Exception {

		String sql;
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = " SELECT m.ID_MOVI from PROJUDI.MOVI m inner join PROJUDI.MOVI_TIPO mt on mt.ID_MOVI_TIPO = m.ID_MOVI_TIPO ";
		sql += " where m.ID_PROC = ? and mt.MOVI_TIPO_CODIGO = ? ";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(MovimentacaoTipoDt.NAO_CONHECIDO);

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				retorno = true;
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	/**
	 * Método que busca as movimentações de um processo
	 * 
	 * @param id_Processo: identificador do processo, descricao: descrição da movimentação do processo
	 * @author kbsriccioppo
	 */
	public List consultarMovimentacoesProcesso(String id_Processo, boolean possuiAutorizacaoParaBaixarVisualizarVideo, String descricao, String posicao) throws Exception {

		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaMovimentacoes = new ArrayList();
		MovimentacaoDt movimentacaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT m.ID_MOVI, m.MOVI_TIPO, m.DATA_REALIZACAO, m.CODIGO_TEMP, m.COMPLEMENTO, m.MOVI_TIPO_CODIGO";
		sqlFrom = " FROM PROJUDI.VIEW_MOV_COMPLETA m";
		sqlFrom += " WHERE m.ID_PROC= ? AND m.MOVI_TIPO LIKE ?";
		ps.adicionarLong(id_Processo);
		ps.adicionarString( descricao +"%");
		sqlOrder = " ORDER BY m.ID_MOVI";
		
		ResultSetTJGO rs = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
		while (rs.next()) {
			movimentacaoDt = new MovimentacaoDt();
			movimentacaoDt.setId(rs.getString("ID_MOVI"));
			movimentacaoDt.setMovimentacaoTipo(rs.getString("MOVI_TIPO"));
			movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs.getDateTime("DATA_REALIZACAO")));
			movimentacaoDt.setCodigoTemp(rs.getString("CODIGO_TEMP"));
			movimentacaoDt.setComplemento(rs.getString("COMPLEMENTO"));
			movimentacaoDt.setMovimentacaoTipoCodigo(rs.getString("MOVI_TIPO_CODIGO"));
			//if (movimentacaoDt.getCodigoTemp().equals("1")) movimentacaoDt.setValida(false);
			
			monteHashComplementoAudienciaPublicada(movimentacaoDt, possuiAutorizacaoParaBaixarVisualizarVideo);

			listaMovimentacoes.add(movimentacaoDt);
		}
		rs.close();
		
		sql = "SELECT COUNT(*) AS QUANTIDADE";						
		rs = consultar(sql + sqlFrom, ps);
		if (rs.next()) listaMovimentacoes.add(rs.getLong("QUANTIDADE"));			
		rs.close();
		
		return listaMovimentacoes;
	}
	
	/**
	 * Método que busca as movimentações de uma data de realização
	 * 
	 * @param data da realizacao: data em que o movimento foi realizado
	 * @author acbloureiro
	 */
	public List consultarMovimentacaoDiariaComComplemento(String dataRealizacao) throws Exception {

		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaMovimentacoes = new ArrayList();
		MovimentacaoDt movimentacaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = "SELECT DISTINCT m.ID_MOVI, m.ID_MOVI_TIPO, m.DATA_REALIZACAO, m.ID_USU_REALIZADOR, m.COMPLEMENTO, mt.ID_CNJ_MOVI, cm.SEQ_COMPLEMENTO, mt.MOVI_TIPO_CODIGO, mt.MOVI_TIPO";
		sqlFrom = " FROM PROJUDI.MOVI m INNER JOIN PROJUDI.MOVI_TIPO mt ";
		sqlFrom += " ON m.ID_MOVI_TIPO = mt.ID_MOVI_TIPO INNER JOIN PROJUDI.CNJ_COMPLEMENTO_MOVIMENTO cm ON mt.ID_CNJ_MOVI = cm.COD_MOVIMENTO ";
		sqlFrom += " INNER JOIN PROJUDI.CNJ_COMPLEMENTO_TABELADO ct ON cm.SEQ_COMPLEMENTO = ct.SEQ_COMPLEMENTO ";
		sqlFrom += " WHERE m.DATA_REALIZACAO >= ? AND m.DATA_REALIZACAO <=  ? ";

		//da data da última movimentacao gerada até o último horário do dia anterior
		
		ps.adicionarDateTimePrimeiraHoraDia(dataRealizacao);
		
	    TJDataHora dataOntem = new TJDataHora();
	    dataOntem.adicioneDia(-1);
	    
		ps.adicionarDateTimeUltimaHoraDia(dataOntem.getDataFormatadaddMMyyyy());
				
		ResultSetTJGO rs = consultar(sql + sqlFrom, ps );
		
		while (rs.next()) {
			//para cada movimento, verificar se 
			movimentacaoDt = new MovimentacaoDt();
			movimentacaoDt.setId(rs.getString("ID_MOVI"));
			movimentacaoDt.setId_MovimentacaoTipo(rs.getString("ID_MOVI_TIPO"));
			movimentacaoDt.setComplemento(rs.getString("COMPLEMENTO"));
			movimentacaoDt.setDataRealizacao(Funcoes.FormatarDataHora(rs.getDateTime("DATA_REALIZACAO")));
			movimentacaoDt.setMovimentacaoTipoCodigo(rs.getString("MOVI_TIPO_CODIGO"));
			movimentacaoDt.setMovimentacaoTipo(rs.getString("MOVI_TIPO"));
			movimentacaoDt.setId_UsuarioRealizador(rs.getString("ID_USU_REALIZADOR"));
			listaMovimentacoes.add(movimentacaoDt);
		}
		rs.close();
		
		return listaMovimentacoes;
	}
	
	
	
	/**
	 * Método utilizado para montar o Rash do link para acesso às Audiências Publicadas
	 * @param movimentacaoDt
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	private void monteHashComplementoAudienciaPublicada(MovimentacaoDt movimentacaoDt, boolean possuiAutorizacaoParaBaixarVisualizarVideo) throws Exception{
		
		// Verificando o tipo da movimentação, pois só será gerado hash para as movimentações do tipo Audiência Publicada
		if ((movimentacaoDt.getMovimentacaoTipoCodigo() == null) || 
			(!movimentacaoDt.getMovimentacaoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_PUBLICADA))) ||
			(movimentacaoDt.getComplemento() == null) || 
			(movimentacaoDt.getComplemento().trim().length() == 0) ||
			(!movimentacaoDt.getComplemento().contains(AudienciaPublicadaDt.IDENTIFICADOR_HASH))) return;		
		
		// Quebrando o link em duas partes. ex: http://www.teste.com?processoNumero=xxxx&DataAudiencia=yyyy&Hash=<<HASH>>
		String[] link = movimentacaoDt.getComplemento().split("\\?");		
		if (link == null || link.length != 2) return;
		
		// Quebrando o link em três partes. ex: processoNumero=xxxx&DataAudiencia=yyyy&Hash=<<HASH>>
		String[] complemento = link[1].split("&");
		if (complemento == null || complemento.length != 3) return;
		
		// Quebrando o complemento em duas partes para obter o número do processo. ex: processoNumero=xxxx
		String[] valorComplementoNumeroProcesso = complemento[0].split("=");
		if (valorComplementoNumeroProcesso == null || valorComplementoNumeroProcesso.length != 2) return;
		String numeroProcesso = valorComplementoNumeroProcesso[1];
		
		// Quebrando o complemento em duas partes para obter a data e hora da audiência. ex: DataAudiencia=xxxx
		String[] valorComplementoDataHoraAudienciaPublicada = complemento[1].split("=");
		if (valorComplementoDataHoraAudienciaPublicada == null || valorComplementoDataHoraAudienciaPublicada.length != 2) return;
		String dataHoraAudienciaPublicada = valorComplementoDataHoraAudienciaPublicada[1];
		
		// Obtendo a data e hora da audiência
		TJDataHora dataHoraAudiencia = new TJDataHora();
		dataHoraAudiencia.setDataHoraFormatadayyyyMMddHHmmss(dataHoraAudienciaPublicada);
		
		// Gerando a base para o hash (ProcessoNumero + yyyyMMddHHmmss (Audiência) + yyyyMMddHH (Atual) + ChaveBaseHash)
		SimpleDateFormat sdfDataAtual = new SimpleDateFormat("yyyyMMddHH");
		String baseRash = numeroProcesso + dataHoraAudienciaPublicada + sdfDataAtual.format(new Date()) + ProjudiPropriedades.getInstance().getChaveBaseHashWebSiteAudienciaPublicada();
		
		// Gerando o hash
		String rashGerado = Funcoes.GeraHashMd5(baseRash);
		
		// Novo parâmetro para o visualizador de audiência pelo Arca
		rashGerado += "&EhProjudi=S";
		
		// Gerando o hash e substituindo do complemento a partir do identificador de valor no link
		movimentacaoDt.setComplemento(movimentacaoDt.getComplemento().replace(AudienciaPublicadaDt.IDENTIFICADOR_HASH, rashGerado));
		
		// Substituindo do complemento o endereço do website do repositório de arquivos
		movimentacaoDt.setComplemento(movimentacaoDt.getComplemento().replace(AudienciaPublicadaDt.IDENTIFICADOR_WEBSITE, ProjudiPropriedades.getInstance().getEnderecoWebSiteAudienciaPublicada()));
				
		// Incluíndo complemento	
		movimentacaoDt.setComplemento(movimentacaoDt.getComplemento().replace(AudienciaPublicadaDt.IDENTIFICADOR_MENSAGEM, AudienciaPublicadaDt.MENSAGEM_VISUALIZACAO));
		
		// Retirando texto do complemento padrão e complemento posterior
		int posicaoFechamentoAncora = movimentacaoDt.getComplemento().indexOf("</a>") + "</a>".length();
		String textoComplementarAposAncora = movimentacaoDt.getComplemento().substring(posicaoFechamentoAncora);
		String textoAteAncora = movimentacaoDt.getComplemento().substring(0, posicaoFechamentoAncora);
		
		// Retirando texto de carta precatória
		String textoCartaPrecatoria = "";		
		if (textoComplementarAposAncora.contains("- Em Carta Precatória")) {
			textoCartaPrecatoria = " " + textoComplementarAposAncora;
		}
		
		// Incluindo número do processo da audiência
		if (podeMontarLinkAudienciaPublicada(movimentacaoDt, possuiAutorizacaoParaBaixarVisualizarVideo)) {
			movimentacaoDt.setComplemento(textoAteAncora + " " + numeroProcesso + " (" + dataHoraAudiencia.getDataFormatadaddMMyyyyHHmm() + ")" + textoCartaPrecatoria);
			AudienciaDRSDt audienciaDRSDt = tenteObterAudienciaDRS(numeroProcesso, dataHoraAudiencia);
			if (audienciaDRSDt != null) {
				movimentacaoDt.setComplemento(String.format("%s <br /><a target=\"_blank\" href=\"%s\">%s</a>", 
						                      movimentacaoDt.getComplemento(),
						                      audienciaDRSDt.getUrlVideoDownloadCripto(), 
						                      AudienciaPublicadaDt.MENSAGEM_DOWNLOAD));	
			} 
		} else {
			movimentacaoDt.setComplemento(" " + numeroProcesso + " (" + dataHoraAudiencia.getDataFormatadaddMMyyyyHHmm() + ")" + textoCartaPrecatoria);	
		}
	}
	
	private AudienciaDRSDt tenteObterAudienciaDRS(String numeroProcesso, TJDataHora dataHora) {
		AudienciaDRSNe audienciaDRSNe = new AudienciaDRSNe();
		AudienciaDRSDt audienciaDRSDt = null;
		try {
			NumeroProcessoDt numeroProcessoDt = new NumeroProcessoDt(numeroProcesso);			
			audienciaDRSDt = audienciaDRSNe.consulteAudiencia(numeroProcessoDt, dataHora);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return audienciaDRSDt;
	}
	
	/**
	 * Verifica se 
	 * @param movimentacaoDt
	 * @return
	 */
	private boolean podeMontarLinkAudienciaPublicada(MovimentacaoDt movimentacaoDt, boolean possuiAutorizacaoParaBaixarVisualizarVideo)
	{
		if (!movimentacaoDt.isValida()) return false;
		
		return possuiAutorizacaoParaBaixarVisualizarVideo;
	}	
	
	/**
	 * Altera o status de uma movimentação passada
	 * 
	 * @param stId, identificação da movimentação
	 * @param status, novo status da movimentação 0 - normal, 1 - inválida
	 * 
	 * @author msapaula
	 */
	public void alterarStatusMovimentacao(String id, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.MOVI SET CODIGO_TEMP = ?";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_MOVI = ?";
		ps.adicionarLong(id);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Consultar dados da movimentação para permitir a geração de pendências
	 * 
	 * @param id_Movimentacao, identificação da movimentação 
	 * 
	 * @author msapaula
	 */
	public MovimentacaoProcessoDt consultarDadosMovimentacao(String id_Movimentacao) throws Exception {
		String Sql;
		MovimentacaoProcessoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_MOVI WHERE ID_MOVI = ?";
		ps.adicionarLong(id_Movimentacao);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new MovimentacaoProcessoDt();
				associarDt(Dados, rs1);
				Dados.setProcessoNumero(Dados.getProcessoNumero() + "-" + rs1.getString("DIGITO_VERIFICADOR"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consulta o idMovimentacao
	 * @param movimentacaoTipoCodigo: tipo da movimentação
	 * @param tcoNumero: número do Execpen //garante a consulta, em caso de número de processo duplicado
	 * @author wcsilva
	 */
	public String consultarIdMovimentacao(String movimentacaoTipoCodigo, String procNumero, String digito, String ano, String tcoNumero) throws Exception {
		String idMovimentacao = "";
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = "SELECT m.ID_MOVI " +
				" FROM PROJUDI.VIEW_PROC p " +
				" INNER JOIN VIEW_MOVI m ON p.ID_PROC = m.ID_PROC" +
				" WHERE m.MOVI_TIPO_CODIGO = ?" +  
				" AND p.TCO_NUMERO LIKE ?" +
				" AND p.PROC_NUMERO = ?" +
				" AND p.digito_verificador = ?" +
				" AND p.ano = ?" +
				" AND ( " +
					" p.ID_PROC_STATUS = (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = (?))" +
					" OR p.ID_PROC_STATUS = (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = (?))" +
				")";
		ps.adicionarLong(movimentacaoTipoCodigo);
		ps.adicionarString(tcoNumero);
		ps.adicionarLong(procNumero);
		ps.adicionarLong(digito);
		ps.adicionarLong(ano);
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()){
				idMovimentacao = rs1.getString("ID_MOVI");
			}
		
		} finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
       } 
		return idMovimentacao;
	}
	
	/**
	 * Método para consultar Interlocutórias do advogado no processo.
	 * @param String id_processo
	 * @return List
	 * @throws Exception
	 */
	public int consultarInterlocutoriasAdvogado(String id_processo) throws Exception {
		int listaMovimentacaoQuantidade = 0;
		String sql = "";
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT COUNT(vm.ID_MOVI) AS QUANTIDADE " +
				" FROM projudi.VIEW_MOVI vm, projudi.PROC p, projudi.USU_SERV us, projudi.SERV s, projudi.SERV_TIPO st " +
				" WHERE p.ID_PROC = ? " +
				" AND p.ID_PROC = vm.ID_PROC " +
				" AND vm.ID_USU_REALIZADOR = us.ID_USU_SERV" +
				" AND us.ID_SERV = s.ID_SERV " +
				" AND s.ID_SERV_TIPO = st.ID_SERV_TIPO " +
				" AND st.SERV_TIPO_CODIGO = ? " + 
				" AND vm.MOVI_TIPO_CODIGO NOT IN (?,?)";			
			ps.adicionarLong(id_processo);
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			ps.adicionarLong(MovimentacaoTipoDt.PETICAO_ENVIADA);
			ps.adicionarLong(MovimentacaoTipoDt.INTIMACAO_LIDA);
			
			rs1 = consultar(sql, ps);
			
			if(rs1 != null) {
				while(rs1.next()) {
					
					listaMovimentacaoQuantidade = rs1.getInt("QUANTIDADE");
				}
			}
			
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaMovimentacaoQuantidade;
	}
	
	/**
	 * Método para consultar Interlocutórias do advogado no processo + PETIÇÃO INICIAL
	 * @param String id_processo
	 * @return List
	 * @throws Exception
	 */
	public int consultarInterlocutoriasAdvogadoCompleto(String id_processo) throws Exception {
		int listaMovimentacaoQuantidade = 0;
		String sql = "";
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT COUNT(vm.ID_MOVI) as Quantidade " +
				" FROM PROJUDI.VIEW_MOVI vm, PROJUDI.PROC p, PROJUDI.USU_SERV us, PROJUDI.SERV s, PROJUDI.SERV_TIPO st " +
				" WHERE p.ID_PROC = ? " +
				" AND p.ID_PROC = vm.ID_PROC " +
				" AND vm.Id_USU_REALIZADOR = us.ID_USU_SERV" +
				" AND us.ID_SERV = s.ID_SERV " +
				" AND s.ID_SERV_TIPO = st.ID_SERV_TIPO " +
				" AND st.SERV_TIPO_CODIGO = ? " + 
				" AND vm.MOVI_TIPO_CODIGO NOT IN (?)";			
			ps.adicionarLong(id_processo);
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			ps.adicionarLong(MovimentacaoTipoDt.INTIMACAO_LIDA);
			
			rs1 = consultar(sql, ps);
			
			if(rs1 != null) {
				while(rs1.next()) {
					
					listaMovimentacaoQuantidade = rs1.getInt("Quantidade");
				}
			}
			
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaMovimentacaoQuantidade;
	}
	
	/**
	 * Método para verificar se um processo possui um tipo de movimentação específica.
	 * @param String id_processo
	 * @param String movimentacaoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean possuiMovimentacaoTipo(String id_processo, String movimentacaoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			String sql = "SELECT COUNT(m.ID_MOVI) FROM PROJUDI.MOVI m, PROJUDI.MOVI_TIPO mt "+
						" WHERE m.ID_PROC = ?" + 
						" AND m.ID_MOVI_TIPO = mt.ID_MOVI_TIPO " +
						" AND mt.MOVI_TIPO_CODIGO = ? ";
			
			ps.adicionarLong(id_processo);
			ps.adicionarLong(movimentacaoTipoCodigo);
			
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					int quantidade = Funcoes.StringToInt(rs1.getString(1));
					
					if( quantidade > 0 ) {
						retorno = true;
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Copia todas as movimentações de audiências publicadas de um processo de origem para um processo de destino 
	 * Utilizado inicialmente no retorno do processo da vara de precatória
	 * 
	 * @param id_processo_origem
	 * @param id_processo_destino
	 * @throws Exception
	 * @author mmgomes
	 */
	public void copieTodasAudienciasPublicadasProcesso(String id_processo_origem, String id_processo_destino, String complemento) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		if(complemento == null) complemento = "";

		Sql = " INSERT INTO PROJUDI.MOVI(ID_MOVI, ";
		Sql += " ID_MOVI_TIPO, ";
		Sql += " ID_PROC, ";
		Sql += " ID_PROC_PRIOR, ";
		Sql += " ID_USU_REALIZADOR, ";
		Sql += " COMPLEMENTO, ";
		Sql += " DATA_REALIZACAO, ";
		Sql += " PALAVRA_CHAVE, ";
		Sql += " CODIGO_TEMP )";						
		Sql += " (SELECT NULL, ";
		Sql += " M.ID_MOVI_TIPO, ";
		Sql += " ?, "; ps.adicionarLong(id_processo_destino);		
		Sql += " M.ID_PROC_PRIOR, ";
		Sql += " M.ID_USU_REALIZADOR, ";
		Sql += " SUBSTR(M.COMPLEMENTO || ? || '(' || TO_CHAR(M.DATA_REALIZACAO, 'DD/MM/YYYY')  || ')', 1, 255) , "; ps.adicionarString(complemento);
		Sql += " ?, "; ps.adicionarDateTime(new Date());		
		Sql += " M.PALAVRA_CHAVE, ";
		Sql += " M.ID_MOVI ";						
		Sql += " FROM PROJUDI.MOVI M INNER JOIN PROJUDI.MOVI_TIPO MT ON M.ID_MOVI_TIPO = MT.ID_MOVI_TIPO AND MT.MOVI_TIPO_CODIGO = ? "; ps.adicionarLong(MovimentacaoTipoDt.AUDIENCIA_PUBLICADA);
		Sql += " WHERE M.ID_PROC = ? "; ps.adicionarLong(id_processo_origem);
		Sql += " AND NOT EXISTS (SELECT 1 ";
		Sql += "                  FROM PROJUDI.MOVI M2 ";
		Sql += "                 WHERE M2.ID_PROC = ? "; ps.adicionarLong(id_processo_destino);
		Sql += "                  AND M2.CODIGO_TEMP = M.ID_MOVI) ";
		Sql += " ) ";

			super.executarUpdateDelete(Sql, ps);
	}
	
	public String consultarArquivosJSON(String id_processo, UsuarioNe usuarioNe)throws Exception {
		
		String stSql;
		ResultSetTJGO rs1 = null;
		MovimentacaoArquivoJSON moviArq = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = " SELECT M.ID_PROC, M.ID_MOVI, M.COMPLEMENTO, MT.MOVI_TIPO, MA.ID_MOVI_ARQ, A.ID_ARQ, A.NOME_ARQ ";
		stSql += "FROM MOVI M  ";
		stSql += "    INNER JOIN MOVI_TIPO MT ON M.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ";
		stSql += "    LEFT JOIN  (MOVI_ARQ MA INNER  JOIN ARQ A ON MA.ID_ARQ = A.ID_ARQ AND MA.ID_MOVI_ARQ_ACESSO<=?) ON M.ID_MOVI = MA.ID_MOVI"; 	ps.adicionarLong(usuarioNe.getNivelAcesso());
		stSql += " WHERE M.ID_PROC = ? ";																									ps.adicionarLong(id_processo);
		stSql += " AND M.CODIGO_TEMP <=? ";																									ps.adicionarLong(usuarioNe.getNivelAcesso());				
		stSql += " ORDER BY ID_MOVI, ID_ARQ ";
					
		rs1 = consultar(stSql, ps);
					
		String id_movi_atual ="";				
		
		moviArq = new MovimentacaoArquivoJSON();
		
		while( rs1.next() ) {
										
			if (rs1.getString("ID_MOVI").equals(id_movi_atual)){
				moviArq.addArquivo(rs1.getString("ID_ARQ"), rs1.getString("NOME_ARQ"), usuarioNe.getCodigoHash(rs1.getString("ID_MOVI_ARQ")+rs1.getString("ID_PROC")), rs1.getString("ID_MOVI_ARQ"));					
			}else{
				
				id_movi_atual = rs1.getString("ID_MOVI");
				
				moviArq.addMovimentacao(rs1.getString("MOVI_TIPO"), rs1.getString("COMPLEMENTO"));
									
				if (rs1.getString("ID_MOVI_ARQ")!=null) 
					moviArq.addArquivo(rs1.getString("ID_ARQ"), rs1.getString("NOME_ARQ"), usuarioNe.getCodigoHash(rs1.getString("ID_MOVI_ARQ")+rs1.getString("ID_PROC")), rs1.getString("ID_MOVI_ARQ"));
				
			}
			
		}
			 
			
		return moviArq.getJSON();
		
	}

	/**
	 * Retorna o id de origem da serventia do processo que foi enviado para conciliação / mediação CEJUSC
	 * @param id_Processo
	 * @return String id_serventia
	 * @throws Exception 
	 */
	public String consultarIdServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(String id_processo) throws Exception {
		String retorno = null;
		String stSql = null;		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			
			stSql = " SELECT US.ID_SERV ";
			stSql += "FROM PROJUDI.MOVI M INNER JOIN PROJUDI.USU_SERV US ON M.ID_USU_REALIZADOR = US.ID_USU_SERV ";
			stSql += " WHERE M.ID_MOVI = (SELECT MAX(ID_MOVI) ";
			stSql += "                      FROM MOVI M2";				        			      				
			stSql += "                     WHERE M2.ID_PROC = ? "; 	ps.adicionarLong(id_processo);
			stSql += "                      AND M2.ID_MOVI_TIPO IN (SELECT ID_MOVI_TIPO";
			stSql += "                                               FROM MOVI_TIPO MT";
			stSql += "                                              WHERE MOVI_TIPO_CODIGO IN (?, ?, ?)";
			stSql += "                                             )";
			ps.adicionarLong(MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC);
			ps.adicionarLong(MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT);
			ps.adicionarLong(MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_MEDIACAO_CEJUSC);
			stSql += "                    )";
			
			rs1 = consultar(stSql, ps);
			
			if(rs1.next())retorno = rs1.getString("ID_SERV");
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	public List<MovimentacaoDt> consultarMovimentacaoAnaliseRepasses(String idProcesso) throws Exception {
		List<MovimentacaoDt> listaMovimentacaoDt = null;
		String stSql = null;		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try {
			
			stSql = "select mt.MOVI_TIPO_CODIGO, mt.ID_CNJ_MOVI, UPPER(translate(m.COMPLEMENTO,'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëüªºÑñ','ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeuAONn')) AS COMPLEMENTO_TRATADO ";
			stSql += " from projudi.movi m "; 
			stSql += " inner join projudi.movi_tipo mt on (m.id_movi_tipo = mt.id_movi_tipo) ";
			
			stSql += " where m.id_proc = ? ";
			ps.adicionarLong(idProcesso);
			
			stSql += " order by m.data_realizacao ";
			
			rs1 = consultar(stSql, ps);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					
					if( listaMovimentacaoDt == null ) {
						listaMovimentacaoDt = new ArrayList<MovimentacaoDt>();
					}
					
					MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
					
					movimentacaoDt.setMovimentacaoTipoCodigo(rs1.getString("MOVI_TIPO_CODIGO"));
					movimentacaoDt.setComplemento(rs1.getString("COMPLEMENTO_TRATADO"));
					
					//**************************
					//ATENÇÃO: CAMPO UTILIZADO PARA SALVAR O VALOR DO ID CNJ.
					//**************************
					movimentacaoDt.setId_MovimentacaoTipo(rs1.getString("ID_CNJ_MOVI"));
					
					listaMovimentacaoDt.add(movimentacaoDt);
				}
			}
			
		}
		finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaMovimentacaoDt;
	}
	
	/**
	 * Método que verifica se processo possui movimentação do tipo AGUARDANDO_DIGITALIZACAO_PARA_DISTRIBUICAO_2G de codigo 40067
	 * 
	 * @param id_Processo, identificador do processo
	 * @author lsbernardes
	 */
	public boolean possuiMovimentacaoAguardandoDigitalizacao(String id_Processo) throws Exception {

		String sql;
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = " SELECT m.ID_MOVI from PROJUDI.MOVI m inner join PROJUDI.MOVI_TIPO mt on mt.ID_MOVI_TIPO = m.ID_MOVI_TIPO ";
		sql += " where m.ID_PROC = ? and mt.MOVI_TIPO_CODIGO = ? and m.CODIGO_TEMP < 7";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(MovimentacaoTipoDt.AGUARDANDO_DIGITALIZACAO_PARA_DISTRIBUICAO_2G);

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				retorno = true;
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	public MovimentacaoDt consultarMovimentacaoJuntadaDeDocumentoHistoricoProcessoFisico(String id_Processo) throws Exception {

		String sql;
		MovimentacaoDt retorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = " SELECT * FROM PROJUDI.VIEW_MOVI ";
		sql += " WHERE ID_PROC = ? "; ps.adicionarLong(id_Processo);
		sql += " AND MOVI_TIPO_CODIGO = ? "; ps.adicionarLong(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
		sql += " AND COMPLEMENTO = ? "; ps.adicionarString(MovimentacaoTipoDt.COMPLEMENTO_JUNTADA_DOCUMENTO_PROCESSO_FISICO);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				retorno = new MovimentacaoDt();
				super.associarDt(retorno, rs1);
			}			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	public List<VotanteDt> consultarVotantesPorMovimentacaoExtratoAtaInserida(String idMovi) throws Exception{
		
		StringBuilder sql = new StringBuilder();
		List<VotanteDt> listaVotantes = new ArrayList<VotanteDt>();
		VotanteDt votante = new VotanteDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append(" SELECT  ");
		sql.append(" 	m.ID_MOVI, u.NOME   ");
		sql.append(" FROM  ");
		sql.append(" 	MOVI m  ");
		sql.append(" INNER JOIN MOVI_ARQ ma ON  ");
		sql.append(" 	m.ID_MOVI = ma.ID_MOVI  ");
		sql.append(" INNER JOIN AUDI_PROC ap ON  ");
		sql.append(" 	ma.ID_ARQ = ap.ID_ARQ_ATA  ");
		sql.append(" INNER JOIN AUDI a ON   ");
		sql.append(" 	ap.ID_AUDI = a.ID_AUDI   ");
		sql.append(" INNER JOIN AUDI_PROC_VOTANTES apv ON  ");
		sql.append(" 	ap.ID_AUDI_PROC = apv.ID_AUDI_PROC  ");
		sql.append(" INNER JOIN VOTANTE_TIPO vt ON  ");
		sql.append(" 	apv.ID_VOTANTE_TIPO = vt.ID_VOTANTE_TIPO  ");
		sql.append(" INNER JOIN SERV_CARGO sc ON  ");
		sql.append(" 	apv.ID_SERV_CARGO = sc.ID_SERV_CARGO  ");
		sql.append(" INNER JOIN USU_SERV_GRUPO usg ON  ");
		sql.append(" 	sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO  ");
		sql.append(" INNER JOIN USU_SERV us ON  ");
		sql.append(" 	usg.ID_USU_SERV = us.ID_USU_SERV  ");
		sql.append(" INNER JOIN USU u ON  ");
		sql.append(" 	us.ID_USU = u.ID_USU  ");
		sql.append(" WHERE  ");
		sql.append(" 	m.ID_MOVI = ?  ");
		ps.adicionarLong(idMovi);
		sql.append(" 	AND vt.VOTANTE_TIPO_CODIGO = ?  ");
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql.append(" 	AND a.VIRTUAL IS NOT NULL   ");
		sql.append(" ORDER BY  ");
		sql.append(" 	m.ID_MOVI, apv.ORDEM_VOTANTE  ");

		try{
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()) {
				votante = new VotanteDt();
				String nome = rs1.getString("NOME");
				if(StringUtils.isNotBlank(nome)) {
					votante.setNome(nome);
					listaVotantes.add(votante);
				}
			}
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaVotantes;
		
	}
	
	/**
	 * Recebe o id do processo e, caso o processo esteja arquivado, retorna o id da última
	 * movimentação de arquivamento feita no processo. Caso o processo não esteja arquivado
	 * ou não hava movimentação de arquivamento, retorna null.
	 * @param idProc
	 * @return String
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
	public String consultarIdMoviUltimoArquivamento(String idProc) throws Exception {
		String idMovi = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		
	    try {
			sql = " SELECT  MAX(ID_MOVI) ID_MOVI FROM MOVI M ";
			sql += " INNER JOIN PROC P ON M.ID_PROC = P.ID_PROC ";
			sql += "  WHERE ";
			sql += " 	P.ID_PROC_STATUS = (SELECT ID_PROC_STATUS FROM PROC_STATUS WHERE PROC_STATUS_CODIGO = ?) AND ";
			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
			sql += " 	P.ID_PROC = ? AND ";
			ps.adicionarLong(idProc);
			sql += "  	M.ID_MOVI_TIPO = (SELECT ID_MOVI_TIPO FROM MOVI_TIPO WHERE MOVI_TIPO_CODIGO = ?) ";
			ps.adicionarLong(MovimentacaoTipoDt.PROCESSO_ARQUIVADO);
			
			rs1 = consultar(sql, ps);
			
			if(rs1 != null) {
				if(rs1.next()) idMovi = rs1.getString("ID_MOVI");
			}
			
		} finally {
			if (rs1 != null) rs1.close();
		}
	    
		return idMovi;
	}

	public String consultarMovimentacaoProcessoJSON(String descricaoMovimentacaoTipo, String id_processo, String posicaoPaginaAtual) throws Exception {
		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();				

		stSql= "SELECT ID_MOVI as id, MOVI || ' - ' || DATA_REALIZACAO  as descricao1 ";
		stSqlFrom= " FROM view_movi WHERE id_proc = ? ";																ps.adicionarLong(id_processo);
		if (descricaoMovimentacaoTipo!=null && !descricaoMovimentacaoTipo.isEmpty()) {
			stSqlFrom+= " AND MOVI like ? ";																			ps.adicionarString(descricaoMovimentacaoTipo+"%");
		}
		stSqlOrder= " ORDER BY DATA_REALIZACAO DESC ";
		try{

			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicaoPaginaAtual);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 	
	}

	public MovimentacaoProcessoDt consultarMovimentacaoECarta(String id_Processo, String codigoRastreamento) throws Exception {
		String Sql;
		MovimentacaoProcessoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT COMPLEMENTO, DATA_REALIZACAO FROM PROJUDI.VIEW_MOVI WHERE ID_PROC = ? AND COMPLEMENTO LIKE ? ";
		ps.adicionarLong(id_Processo);
		ps.adicionarString("%"+codigoRastreamento+"%");
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new MovimentacaoProcessoDt();
				Dados.setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getString("DATA_REALIZACAO")));
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}
	
	public MovimentacaoProcessoDt consultarMovimentacaoArquivoECarta(String id_Processo, String codigoRastreamento) throws Exception {
		String Sql;
		MovimentacaoProcessoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT VM.COMPLEMENTO, VM.DATA_REALIZACAO, VMA.NOME_ARQ FROM VIEW_MOVI VM INNER JOIN VIEW_MOVI_ARQ VMA ON VMA.ID_MOVI=VM.ID_MOVI WHERE ID_PROC = ? AND VMA.NOME_ARQ = ? ";
		ps.adicionarLong(id_Processo);
		ps.adicionarString(codigoRastreamento.toLowerCase()+".pdf");
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new MovimentacaoProcessoDt();
				Dados.setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.setDataRealizacao(Funcoes.FormatarDataHora(rs1.getString("DATA_REALIZACAO")));
				Dados.setArquivoTipo(rs1.getString("NOME_ARQ"));
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}
}
