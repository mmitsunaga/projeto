package br.gov.go.tj.projudi.ps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import br.gov.go.tj.projudi.dt.OcorrenciaInterrupcao;
import br.gov.go.tj.projudi.dt.ParametroRelatorioInterrupcaoDt;
import br.gov.go.tj.projudi.dt.RelatorioInterrupcao;
import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaPingdom;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Config.AjudanteConfiguracao;
/**
 * 
 * Classe:     RelatorioInterrupcoesPs.java
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       08/2010
 * Finalidade: Consultar na base de dados as indisponibilidas ocorridos no per�odo selecionado.
 *             Obter do webservice pingdom as interrup��es ocorridas desde a �ltima interrup��o
 *             at� a data de ontem.  
 *             
 */
public class RelatorioInterrupcoesPs extends Persistencia {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1702956927135702729L;
	
	public RelatorioInterrupcoesPs(Connection conexao){
    	Conexao = conexao;
	}
	
	private final String VALOR_STATUS = "down";
	
	private enum enumCamposInterrupcao
	{
		STATUS,
		TIME_FROM,
		TIME_TO,
		TOTAL_CAMPOS
	}
	
	private enum enumCamposChaveValor
	{
		CHAVE,
		VALOR,
		TOTAL_CAMPOS
	}
	
	/**
     * Obtem da base de dados as interrup��es, caso existam, armazendas pelo autom�tico no per�odo informado
     * 
     * @param ParametroRelatorioInterrupcaoDt paramRelInterrupcoesDt
     *  
     */
	public RelatorioInterrupcao obtenhaInterrupcoes(ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt) throws Exception{	
	
		//Cria o relat�rio de retorno
		RelatorioInterrupcao relatorio = new RelatorioInterrupcao();
		
		//Armazena no relat�rio os parametros utilizados 
		relatorio.setParametrosUtilizados(paramRelIndisponibilidadeDt);	
		
		//obtem da base de dados os registros
		carregueInterrupcoesBD(relatorio);
		
		return relatorio;
	}
	
	/**
     * Obtem da base de dados as interrup��es e armazena no objeto de relat�rio criado
     * 
     * @param RelatorioInterrupcao relatorio
     *  
     */
	private void carregueInterrupcoesBD(RelatorioInterrupcao relatorio) throws Exception{		
		
		//Atualiza os parametros para o hor�rio GMT brasil (-3Horas), pois o webservice, retorna por
		//padr�o o hor�rio GMT +2Horas
		relatorio.getParametrosUtilizados().getPeriodoInicialUtilizado().atualizeZonaBrasil();
		relatorio.getParametrosUtilizados().getPeriodoFinalUtilizado().atualizeZonaBrasil();
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//Preparando a consulta SQL utilizando o per�odo selecionado.
		String Sql = "SELECT OI.ID_OCORRENCIA_INTERRUPCAO, OI.DATA_INICIAL, OI.DATA_FINAL, IT.INTERRUPCAO_TIPO, IT.INTERRUPCAO_TOTAL " + 
		      " FROM PROJUDI.OCORRENCIA_INTERRUPCAO OI INNER JOIN INTERRUPCAO_TIPO IT ON IT.ID_INTERRUPCAO_TIPO = OI.ID_INTERRUPCAO_TIPO " +
		      " WHERE OI.SISTEMA = ? " +
		      " AND OI.DATA_INICIAL >= ? " +  
			  " AND OI.DATA_INICIAL <= ? ";
		
		ps.adicionarLong(relatorio.getParametrosUtilizados().getSistema().getId());
		ps.adicionarDateTime(relatorio.getParametrosUtilizados().getPeriodoInicialUtilizado().getDate());
		ps.adicionarDateTime(relatorio.getParametrosUtilizados().getPeriodoFinalUtilizado().getDate());
		
		if (relatorio.getParametrosUtilizados().isConsideraInterrupcaoTotal() && !relatorio.getParametrosUtilizados().isConsideraInterrupcaoParcial()) {
			Sql += " AND IT.INTERRUPCAO_TOTAL = ? ";
			ps.adicionarBoolean(true);
		} else if (!relatorio.getParametrosUtilizados().isConsideraInterrupcaoTotal() && relatorio.getParametrosUtilizados().isConsideraInterrupcaoParcial()) {
			Sql += " AND IT.INTERRUPCAO_TOTAL = ? ";
			ps.adicionarBoolean(false);
		}		
		
		Sql += " ORDER BY DATA_INICIAL"; 
		
		ResultSetTJGO rs1 = null;
		try{
			
			rs1 = consultar(Sql, ps);
			
			//Ponteiro tempor�rio
			OcorrenciaInterrupcao ocorrencia = null;
			while (rs1.next()) {
				//Criando uma nova int�ncia para o ponteiro
				ocorrencia = new OcorrenciaInterrupcao();
				
				ocorrencia.setId(rs1.getString("ID_OCORRENCIA_INTERRUPCAO"));
				ocorrencia.setPeriodoInicial(Funcoes.BancoTJDataHora(rs1.getString("DATA_INICIAL")));
				ocorrencia.setPeriodoFinal(Funcoes.BancoTJDataHora(rs1.getString("DATA_FINAL")));
				ocorrencia.setMotivo(rs1.getString("INTERRUPCAO_TIPO"));
				
				if (rs1.getBoolean("INTERRUPCAO_TOTAL")) {
					ocorrencia.setMotivo(ocorrencia.getMotivo() + " (TOTAL)");	
				} else {
					ocorrencia.setMotivo(ocorrencia.getMotivo() + " (PARCIAL)");
				}	 
				
				//Adicionando a interrup��o no relat�rio, que internamento ir� agrupar por data e hora,
				//totalizando por data
				relatorio.adicioneInterrupcao(ocorrencia);			
			}		
		
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}            
        }
		
	}
	

	/**
     * Executa o processamento autom�tico obtendo do webservice pingdom 
     * as indisponibilidades, caso existam, do per�odo entre a data posterior
     * � da �ltima interrup��o e a data do dia anterior � data do sistema (ontem)     
	 * @throws Exception 
     *  
     */
	public void executeProcessamentoAutomatico(EnumSistemaPingdom sistema) throws Exception{		
		//Obtendo a data inicial deste processamento...
		TJDataHora periodoInicial = obtenhaDataInicialProcessamento(sistema);
			
		executeProcessamentoAutomaticoPeriodico(sistema, periodoInicial);
	}
	
	/**
     * Execute o processo autom�tico peri�dico.     
     *  
     */
	private void executeProcessamentoAutomaticoPeriodico(EnumSistemaPingdom sistema, TJDataHora periodoInicial) throws Exception{	
		TJDataHora periodoFinal = null;	
		
		//Obtendo a data final deste processamento...
		periodoFinal = obtenhaDataFinalProcessamento();
		
		//Verificando se o processo j� foi executado para a data atual...
		if (processamentoJaExecutado(sistema, periodoFinal)) return;	
		
		//Executa o processo autom�tico em si...
		executeProcessamentoRest(sistema, periodoInicial, periodoFinal);		
	}
	
	/**
     * Realiza a consulta das interrup��es no webservice para o per�odo selecionado
     * 
     * @param TJDataHora periodoInicial
     * @param TJDataHora periodoFinal
     *  
     */
     private void executeProcessamentoRest(EnumSistemaPingdom sistema, TJDataHora periodoInicial, TJDataHora periodoFinal) throws Exception{   	
		//configureProxy();
		
		String APIKey = AjudanteConfiguracao.getConfigPingDom(sistema).getPingdomAPIKey();		
		String urlDoPingdom = AjudanteConfiguracao.getConfigPingDom(sistema).getPingdomURL();
		String usuarioPingdom = AjudanteConfiguracao.getConfigPingDom(sistema).getPingdomUserName();
		String senhaPingdom = AjudanteConfiguracao.getConfigPingDom(sistema).getPingdomPassword();		
				
		//Atualiza os parametros para o hor�rio GMT brasil (-3Horas), pois o webservice, retorna por
		//padr�o o hor�rio GMT +2Horas, se este procedimento for omitido, o hor�rio da indisponibilidade 
		//ser� retornado com 5 horas de diferen�a.
		periodoInicial.atualizeZonaBrasil();
		periodoFinal.atualizeZonaBrasil();				
		
		//Obtem as interrup��es para a p�gina corrente no web service
		String conteudoJason = obtenhaProximasOcorrenciasRest(periodoInicial,
													          periodoFinal,							                    		   
								                    		  APIKey,							                    		   
								                    		  urlDoPingdom,
								                    		  usuarioPingdom,
								                    		  senhaPingdom);
		
		//Realiza a valida��o do status de retorno pingdom
		valideStatusRetornoPingdom(conteudoJason);
		
		//Grava interrup��es no banco de dados...
		mapeieRepostaParaBD(sistema, conteudoJason);							
	}
	
	private void valideStatusRetornoPingdom(String conteudoJason) throws MensagemException {
		if (conteudoJason == null || conteudoJason.trim().length() == 0)
			throw new MensagemException("Retorno vazio.");
		
		if (!conteudoJason.contains("summary") || !conteudoJason.contains("[") || !conteudoJason.contains("[") || conteudoJason.contains("error"))
			throw new MensagemException("Ocorreu um erro na requisi��o ao pingdom.\n" + conteudoJason);
	}

	/**
	* Realiza a consulta das interrup��es no webservice para o per�odo selecionado e p�gina informada
	* 
	* @param TJDataHora periodoInicial
	* @param TJDataHora periodoFinal
	* @param Integer paginaAtual
	* @param String APIKey
	* @param String applicationName
	*  
	*/
	private String obtenhaProximasOcorrenciasRest(TJDataHora periodoInicial, 
			                                      TJDataHora periodoFinal,											      
											      String APIKey,							                      
							                      String urlServicoRest,
							                      String usuarioPingdom,
							                      String senhaPingdom) throws Exception{		
			
		String conteudoJason = null;
		
		//String urlApi = "https://api.pingdom.com/api/2.0/summary.outage/105203";
		//String appkey = "qa6i8o5pbh3bn1t6kku8zmb097hi0too";
		//String autorizacaoBasic = "Basic bW9uYW1iYUB0amdvLmp1cy5icjpFNU1EZGtabVZqWVQ=";		
		String usuarioSenha = usuarioPingdom + ":" + senhaPingdom;
		String autorizacaoBasic = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(usuarioSenha.getBytes());
		

		try(DefaultHttpClient httpclient = new DefaultHttpClient()){
			HttpGet httpget = new HttpGet(urlServicoRest + "?from=" + periodoInicial.getDataFormatadaUnixTimeStamp() + "&to=" + periodoFinal.getDataFormatadaUnixTimeStamp() + "&order=asc");		
			httpget.addHeader("App-Key", APIKey);
			httpget.addHeader("Authorization", autorizacaoBasic);
			
			HttpResponse response;				
			
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) conteudoJason = EntityUtils.toString(entity);
			
			return conteudoJason;
		}
	}
	
//	public static void configureProxy() throws Exception{		
//		Funcoes.configureProxy(AjudanteConfiguracao.getConfigProxy().getProxyURL(), 
//				               AjudanteConfiguracao.getConfigProxy().getProxyPort(),
//				               AjudanteConfiguracao.getConfigProxy().getProxyUserName(),
//				               AjudanteConfiguracao.getConfigProxy().getProxyPassword());
//		
//	}
		
	/**
	* Realiza o mapeamento das interrup��es retornadas pelo webservice (de cada p�gina)
	* e insere na base de dados...
	* 
	* @param Report_GetOutagesResponse reportRespGetOutAges
	*  
	*/
	private void mapeieRepostaParaBD(EnumSistemaPingdom sistema, String conteudoJason) throws Exception{

		//Se n�o existir interrup��es nesta p�gina a execu��o do m�todo � interrompida (�ltimo consumo ao webservice)
		if (conteudoJason == null || 
			conteudoJason.trim().length() == 0 || 
			!conteudoJason.contains("[") ||
			!conteudoJason.contains("]")) return;
		
		int posicaoInicial = conteudoJason.indexOf("[");
		int posicaoFinal = conteudoJason.indexOf("]");
		
		conteudoJason = conteudoJason.substring(posicaoInicial + 1, posicaoFinal);	
		conteudoJason = RetireAbreChaves(conteudoJason);
		String[] interrupcoes = conteudoJason.replaceAll("\"", "").trim().split("},");
		
		if (interrupcoes.length == 0) return;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String comandoSQL = "INSERT INTO PROJUDI.OCORRENCIA_INTERRUPCAO(DATA_INICIAL, DATA_FINAL, SISTEMA) VALUES (?,?,?)";	
		
		for (int i = 0; i < interrupcoes.length; i++)
		{
			String[] interrupcao = interrupcoes[i].split(",");
			if (interrupcao.length == enumCamposInterrupcao.TOTAL_CAMPOS.ordinal())
			{
				String valorStatus = ObtenhaValorValido(interrupcao, enumCamposInterrupcao.STATUS);				
				if (valorStatus != null && valorStatus.trim().equalsIgnoreCase(VALOR_STATUS))
				{
					long dataUnixTimeStampInicial = Funcoes.StringToLong(ObtenhaValorValido(interrupcao, enumCamposInterrupcao.TIME_FROM));
					long dataUnixTimeStampFinal = Funcoes.StringToLong(ObtenhaValorValido(interrupcao, enumCamposInterrupcao.TIME_TO));
					if (dataUnixTimeStampInicial > 0 && dataUnixTimeStampFinal > 0)
					{
						TJDataHora dataInicial = new TJDataHora();
						dataInicial.setDataUnixTimeStamp(dataUnixTimeStampInicial);
						TJDataHora dataFinal = new TJDataHora();
						dataFinal.setDataUnixTimeStamp(dataUnixTimeStampFinal);
						//Grava interrup��o no banco de dados...
						ps.limpar();
						ps.adicionarDateTime(dataInicial);
						ps.adicionarDateTime(dataFinal);
						ps.adicionarLong(sistema.getId());
						super.executarInsert(comandoSQL, "ID_OCORRENCIA_INTERRUPCAO", ps);	
					}	
				}	
			}
		}	
	}
	
	private String ObtenhaValorValido(String[] chaveValorInterrupcao, enumCamposInterrupcao campo)
	{
		String[] campoChaveValor = chaveValorInterrupcao[campo.ordinal()].split(":");
		
		if (campoChaveValor != null && campoChaveValor.length == enumCamposChaveValor.TOTAL_CAMPOS.ordinal())
		{
			return campoChaveValor[enumCamposChaveValor.VALOR.ordinal()];
		}
		
		return "";
	}
	
	private String RetireAbreChaves(String conteudoJason)
	{
		if (conteudoJason.contains("{"))
		{
			conteudoJason = conteudoJason.replace("{", "");
			
			return RetireAbreChaves(conteudoJason);
		}
		
		return conteudoJason;
	}
	
	/**
	* Verifica se o processamento j� foi realizado para este per�odo final
	* 
	* @param TJDataHora periodoFinal 
	*  
	*/
	private boolean processamentoJaExecutado(EnumSistemaPingdom sistema, TJDataHora periodoFinal) throws Exception{
		//obtem a data do �ltimo processamento armazenada no banco de dados...
		TJDataHora dataUltimoProcessamento = obtenhaDataUltimoProcessamento(sistema);		
		if (dataUltimoProcessamento == null) return false;
		
		dataUltimoProcessamento.atualizeUltimaHoraDia();		
		
		return !(periodoFinal.ehApos(dataUltimoProcessamento));
	}
	
	/**
	* Retorna a data do dia anterior � data atual do sistema (ontem)
	*  
	*/
	private TJDataHora obtenhaDataFinalProcessamento(){
		//Obtendo a data atual do sistema...
		TJDataHora periodoFinal = new TJDataHora();
		
		//Obtendo a data do dia anterior ao dia atual do sistema...
		periodoFinal.adicioneDia(-1);
		
		periodoFinal.atualizeUltimaHoraDia();
		
		return periodoFinal;
	}
	
	/**
	* Retorna a data inicial que ser� utilizada pelo processamento autom�tico
	*  
	*/
	private TJDataHora obtenhaDataInicialProcessamento(EnumSistemaPingdom sistema) throws Exception{		
		TJDataHora periodoInicial;
		
		//obtendo a data do �ltimo processamento...
		periodoInicial = obtenhaDataUltimoProcessamento(sistema);
		
		//adicionando um dia ao �ltimo processamento para que seja a data inicial deste processamento...
		periodoInicial.adicioneDia(1);
			
		periodoInicial.atualizePrimeiraHoraDia();
		
		return periodoInicial;		
	}
	
	/**
	* Retorna a maior data de interrup��o armazenada no banco, caso n�o exista retorna null
	*  
	*/
	private TJDataHora obtenhaDataUltimoProcessamento(EnumSistemaPingdom sistema) throws Exception{
		
		TJDataHora dataUltimoProcessamento =  null;
		
		ResultSetTJGO rs = null;	
		
		try{
		
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ps.adicionarLong(sistema.getId());
			rs = super.consultar("SELECT MAX(DATA_INICIAL) AS DATA_INICIAL FROM PROJUDI.OCORRENCIA_INTERRUPCAO WHERE SISTEMA = ?", ps);
			
			if (rs.next()){
				if (rs.isDate("DATA_INICIAL")) {
					dataUltimoProcessamento = Funcoes.BancoTJDataHora(rs.getString("DATA_INICIAL"));	
				} else {
					dataUltimoProcessamento = new TJDataHora("01/01/2010");
				}
			}
			
			return dataUltimoProcessamento;			
		}
		finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}		
	}
}