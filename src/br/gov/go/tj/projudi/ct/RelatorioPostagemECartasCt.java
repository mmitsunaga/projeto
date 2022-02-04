package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPostagemECartasDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.CorreiosNe;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.RelatorioPostagemECartasNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ps.ArquivoPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;

/**
 * Relatório Analítico de postagem do E-Cartas por período e serventia
 * @author mmitsunaga
 *
 */
public class RelatorioPostagemECartasCt extends Controle {

	private static final long serialVersionUID = -8367262003089072627L;
	
	@Override
	public int Permissao() {		
		return RelatorioPostagemECartasDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		RelatorioPostagemECartasNe relatorioPostagemECartasNe = new RelatorioPostagemECartasNe();
		RelatorioPostagemECartasDt relatorioPostagemECartasDt = (RelatorioPostagemECartasDt) request.getSession().getAttribute("RelatorioPostagemECartasDt");
		String nomeBusca1 = "";
		byte[] arquivoAR = null;
		String codRastreamento1 = "";
		String idPendencia1 = "";
		String idPendencia2 = "";
		String codInconsistencia2 = "";
		String msgInconsistencia2 = "";
		String idPendencia3 = "";
		String codRastreamento3 = "";
		String dataPostagem3 = "";
		String codRastreamento4 = "";
		String codBaixa4 = "";
		String dataEntrega4 = "";
		String horaEntrega4 = "";
		String idPendencia5 = "";
		String codRastreamento5 = "";
		String nomeArquivo5 = "";
		String idPendencia6 = "";
		String codRastreamento6 = "";
		String intervalo6 = "";
		String codRastreamento7 = "";
		String idMovimentacaoArquivo7 = "";
		String nomeArquivo7 = "";
		String nomeArquivo9 = "";
		String numeroRegistros11 = "";
		String numeroProcesso = "";
		boolean realizado = false;
		String stAcao = "/WEB-INF/jsptjgo/RelatorioPostagemECartas.jsp";
		
		if (relatorioPostagemECartasDt == null) relatorioPostagemECartasDt = new RelatorioPostagemECartasDt();
		if (request.getParameter("PaginaAtual") != null) paginaatual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));
		
		if (request.getParameter("DataInicial") != null){
			relatorioPostagemECartasDt.setDataInicial(request.getParameter("DataInicial"));
		} else {
			relatorioPostagemECartasDt.setDataInicial(Funcoes.FormatarData(Funcoes.getPrimeiroDiaMes()));
		}
		
		if (request.getParameter("DataFinal") != null){
			relatorioPostagemECartasDt.setDataFinal(request.getParameter("DataFinal"));
		} else {
			relatorioPostagemECartasDt.setDataFinal(Funcoes.dateToStringSoData(new Date()));
		}
		
		if (request.getParameter("Id_Comarca") != null){
			if (request.getParameter("Id_Comarca").equals("null")) relatorioPostagemECartasDt.setIdComarca("");
			else relatorioPostagemECartasDt.setIdComarca(request.getParameter("Id_Comarca"));						
		}
		if (request.getParameter("Comarca") != null){
			if (request.getParameter("Comarca").equals("null")) relatorioPostagemECartasDt.setComarca("");
			else relatorioPostagemECartasDt.setComarca(request.getParameter("Comarca"));			
		}
		if (request.getParameter("Id_Serventia") != null){
			if (request.getParameter("Id_Serventia").equals("null")) relatorioPostagemECartasDt.setIdServentia("");
			else relatorioPostagemECartasDt.setIdServentia(request.getParameter("Id_Serventia"));						
		}
		if (request.getParameter("Serventia") != null) { 
			if (request.getParameter("Serventia").equals("null")) relatorioPostagemECartasDt.setServentia("");
			else relatorioPostagemECartasDt.setServentia(request.getParameter("Serventia"));			
		}
		if (request.getParameter("nomeBusca1") != null) nomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma", "Relatório Analítico de Postagem - [e-Carta]");
		request.setAttribute("tempRetorno", "RelatorioPostagemECartas");		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual){
			case Configuracao.Novo:
				relatorioPostagemECartasDt.limpar();				
				break;
				
			case Configuracao.Imprimir:
				String mensagemRetorno = "";
				if (ValidacaoUtil.isVazio(relatorioPostagemECartasDt.getDataInicial())){
					mensagemRetorno = "O campo Data Inicial é obrigatório. \n";
				} else if (ValidacaoUtil.isVazio(relatorioPostagemECartasDt.getDataFinal())){
					mensagemRetorno = "O campo Data Final é obrigatório. \n";
				}
				
				if (mensagemRetorno.isEmpty()){
					String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();
					String nomeUsuarioResponsavel = UsuarioSessao.getUsuarioDt().getNome();
					
					byte [] byTemp = relatorioPostagemECartasNe.emitirRelatorioPostagemECartas(diretorioProjeto
							, relatorioPostagemECartasDt.getDataInicial() + " 00:00:00"
							, relatorioPostagemECartasDt.getDataFinal() + " 23:59:59"
							, relatorioPostagemECartasDt.getIdComarca()
							, relatorioPostagemECartasDt.getIdServentia()
							, nomeUsuarioResponsavel);
					
					if (ValidacaoUtil.isNaoNulo(byTemp)){
						enviarPDF(response, byTemp, "RelatorioAnaliticoPostagemECartas");
					} else {
						request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						break;
					}
					return;
				} else {
					request.setAttribute("MensagemErro", mensagemRetorno);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
				
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","RelatorioPostagemECartas");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = relatorioPostagemECartasNe.consultarDescricaoComarcaJSON(nomeBusca1, posicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;		
				
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (ValidacaoUtil.isVazio(relatorioPostagemECartasDt.getIdComarca())){
					request.setAttribute("MensagemErro", "É necessário informar a Comarca.");
				} else {
					if (request.getParameter("Passo")==null){					
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia","Estado"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Serventia");
						request.setAttribute("tempBuscaDescricao","Serventia");
						request.setAttribute("tempBuscaPrograma","Serventia");			
						request.setAttribute("tempRetorno","RelatorioPostagemECartas");					
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						break;
					} else {
						String stTemp="";
						stTemp = relatorioPostagemECartasNe.consultarServentiasAtivasComarcaJSON(nomeBusca1, posicaoPaginaAtual, relatorioPostagemECartasDt.getIdComarca(), String.valueOf(ServentiaTipoDt.VARA));														
						enviarJSON(response, stTemp);
						return;								
					}
				}
				break;
				
			case Configuracao.Curinga6:
				String[][] historico = null;
				String fluxo = "";
				CorreiosNe correiosNe = new CorreiosNe();
				stAcao = "/WEB-INF/jsptjgo/FerramentasECarta.jsp";
				if (request.getAttribute("tempFluxo1") != null) fluxo = request.getAttribute("tempFluxo1").toString();
				if (request.getAttribute("Intervalo6") != null && !request.getAttribute("Intervalo6").toString().isEmpty()) intervalo6 = request.getAttribute("Intervalo6").toString();
				
				// RECIBO
				if (fluxo.equalsIgnoreCase("1")) {
					try {
						if (request.getAttribute("Id_Pendencia1") != null) idPendencia1 = request.getAttribute("Id_Pendencia1").toString();
						if (request.getAttribute("CodRastreamento1") != null) codRastreamento1 = request.getAttribute("CodRastreamento1").toString();
						realizado = correiosNe.executarRecibo(idPendencia1, codRastreamento1);
						if(realizado)
							request.setAttribute("MensagemOk", "Recibo lido com sucesso");						
						else
							request.setAttribute("MensagemErro", "Recibo não encontrado");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// INCONSISTENCIA
				} else if (fluxo.equalsIgnoreCase("2")) {
					try {
						if (request.getAttribute("Id_Pendencia2") != null) idPendencia2 = request.getAttribute("Id_Pendencia2").toString();
						if (request.getAttribute("CodInconsistencia2") != null) codInconsistencia2 = request.getAttribute("CodInconsistencia2").toString();
						if (request.getAttribute("MsgInconsistencia2") != null) msgInconsistencia2 = request.getAttribute("MsgInconsistencia2").toString();
						realizado = correiosNe.executarInconsistencia(idPendencia2, codInconsistencia2, msgInconsistencia2);
						if(realizado)
							request.setAttribute("MensagemOk", "Inconsistencia lida com sucesso");
						else
							request.setAttribute("MensagemErro", "Inconsistencia não encontrada");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// POSTAGEM
				} else if (fluxo.equalsIgnoreCase("3")) {
					try {
						if (request.getAttribute("Id_Pendencia3") != null) idPendencia3 = request.getAttribute("Id_Pendencia3").toString();
						if (request.getAttribute("CodRastreamento3") != null) codRastreamento3 = request.getAttribute("CodRastreamento3").toString();
						if (request.getAttribute("DataPostagem3") != null) dataPostagem3 = request.getAttribute("DataPostagem3").toString();
						realizado = correiosNe.executarPostagem(idPendencia3, codRastreamento3, dataPostagem3);
						if(realizado)
							request.setAttribute("MensagemOk", "Postagem lida com sucesso");
						else
							request.setAttribute("MensagemErro", "Postagem não encontrada");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// ENTREGA
				} else if (fluxo.equalsIgnoreCase("4")) {	
					try {
						if (request.getAttribute("CodRastreamento4") != null) codRastreamento4 = request.getAttribute("CodRastreamento4").toString();
						if (request.getAttribute("CodBaixa4") != null) codBaixa4 = request.getAttribute("CodBaixa4").toString();
						if (request.getAttribute("DataEntrega4") != null) dataEntrega4 = request.getAttribute("DataEntrega4").toString();
						if (request.getAttribute("HoraEntrega4") != null) horaEntrega4 = request.getAttribute("HoraEntrega4").toString();
						realizado = correiosNe.executarEntrega(codRastreamento4, codBaixa4, dataEntrega4, horaEntrega4);
						if(realizado)
							request.setAttribute("MensagemOk", "Entrega lida com sucesso");
						else
							request.setAttribute("MensagemErro", "Entrega não encontrada");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// AVISO DE RECEBIMENTO
				} else if(fluxo.equalsIgnoreCase("5")) {
					try {
						if (request.getAttribute("Id_Pendencia5") != null) idPendencia5 = request.getAttribute("Id_Pendencia5").toString();
						if (request.getAttribute("CodRastreamento5") != null) codRastreamento5 = request.getAttribute("CodRastreamento5").toString();
						if (request.getAttribute("NomeArquivo5") != null) nomeArquivo5 = request.getAttribute("NomeArquivo5").toString();
						realizado = correiosNe.executarAR(idPendencia5, codRastreamento5, nomeArquivo5);
						if(realizado)
							request.setAttribute("MensagemOk", "Aviso de recebimento lido com sucesso");
						else
							request.setAttribute("MensagemErro", "Aviso de recebimento não encontrado");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// HISTÓRICO DA CARTA
				} else if(fluxo.equalsIgnoreCase("6")) {
					try {
						if (request.getAttribute("Id_Pendencia6") != null) idPendencia6 = request.getAttribute("Id_Pendencia6").toString();
						if (request.getAttribute("CodRastreamento6") != null) codRastreamento6 = request.getAttribute("CodRastreamento6").toString();
						historico = correiosNe.consultarHistorico(idPendencia6, codRastreamento6, intervalo6);
						request.setAttribute("HistoricoCarta", historico);
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// SUBSTITUIR AR CORROMPIDO
				} else if(fluxo.equalsIgnoreCase("7")) {
					try {
						String conteudoArquivo = getConteudoArquivo(request);
						String[] rastreamentoMovimentacao = null;
						String[] aux = null;
						if (conteudoArquivo != null) {
							String[] dadosCorreios = conteudoArquivo.split("\r\n");
							for (int i = 0; i < dadosCorreios.length; i++) {
								rastreamentoMovimentacao = dadosCorreios[i].split(" ");
								Map codigoR = new HashedMap();
								codigoR.put(rastreamentoMovimentacao[0].toUpperCase(), "");
								String[] arquivoARs = new ArquivoNe().lerDevolucaoARsBD(ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR, "", "", "", codigoR);	//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
								aux = arquivoARs[0].split("\\|");
								nomeArquivo7 = aux[0];
								arquivoAR = correiosNe.getArBD(rastreamentoMovimentacao[0], nomeArquivo7);
								if (arquivoAR != null) {
									MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoNe().consultarIdCompleto(rastreamentoMovimentacao[1]);
									MovimentacaoDt movimentacaoDt = new MovimentacaoNe().consultarId(movimentacaoArquivoDt.getId_Movimentacao());
									numeroProcesso = new MovimentacaoArquivoNe().consultarNumeroProcesso(rastreamentoMovimentacao[1]);
									if (movimentacaoArquivoDt != null) {
										ArquivoDt arquivoDt = new ArquivoNe().consultarId(movimentacaoArquivoDt.getId_Arquivo());
										if (arquivoDt != null) {
											FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
											try {
												arquivoDt.setArquivo(arquivoAR);
												Signer.gerarReciboECarta(movimentacaoArquivoDt.getId_Movimentacao(), numeroProcesso, arquivoDt, movimentacaoDt.getDataRealizacao());
												//new ArquivoNe().atualizaConteudoRecibo(arquivoDt, obFabricaConexao);
												request.setAttribute("MensagemOk", "AR inserido com sucesso");
											} finally {
												obFabricaConexao.fecharConexao();
											}
										}
									}
								} else {
									request.setAttribute("MensagemErro", "AR não encontrado");
								}
							}
						} else {
							request.setAttribute("MensagemErro", "Arquivo não encontrado");
						}
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// INSERIR ARQUIVO .ZIP DOS CORREIOS
				} else if(fluxo.equalsIgnoreCase("8")) {
					try {
						ArquivoDt arquivo = new ArquivoDt();
						arquivo.setNomeArquivo(getNomeArquivo(request));
						if(arquivo.getNomeArquivo().contains("evolucao")) {
							arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_AVISO_RECEBIMENTO));
						} else if(arquivo.getNomeArquivo().contains("ostado")) {
							arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_POSTAGEM));
						} else if(arquivo.getNomeArquivo().contains("inalizador")) {
							arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_ENTREGA));
						} else if(arquivo.getNomeArquivo().contains("ecibo")) {
							arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_RECIBO));
						} else if(arquivo.getNomeArquivo().contains("nconsistencia")) {
							arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA));
						} else 
							throw new MensagemException("Tipo não definido");
						if(!new ArquivoNe().arquivoExiste(Funcoes.limparNomeArquivo(arquivo.getNomeArquivo()), Funcoes.StringToInt(arquivo.getId_ArquivoTipo()))) {
							byte[] arquivoZip = getConteudoArquivoBytes(request);
							arquivo.setNomeArquivo(getNomeArquivo(request));
							arquivo.setArquivo(arquivoZip);
							arquivo.setContentType("application/zip");
							arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
							arquivo.setIpComputadorLog("Servidor");
							new ArquivoNe().salvar(arquivo);
							request.setAttribute("MensagemOk", "\"" + arquivo.getNomeArquivo() +  "\" inserido com sucesso");						
						} else {
							request.setAttribute("MensagemOk", "\"" + arquivo.getNomeArquivo() +  "\" arquivo já existe!");	
						}	
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// EXECUTAR ARQUIVO
				} else if(fluxo.equalsIgnoreCase("9")) {
					try {
						if (request.getAttribute("NomeArquivo9") != null) nomeArquivo9 = request.getAttribute("NomeArquivo9").toString();
						if(nomeArquivo9.contains("ecibo")) {
							correiosNe.confirmarRecebimentoCorreios(nomeArquivo9);						
						} else if(nomeArquivo9.contains("ostado")) {
							correiosNe.confirmarPostagemCorreios(nomeArquivo9);
						} else if(nomeArquivo9.contains("inalizador")) {
							correiosNe.confirmarEntregaCorreios(nomeArquivo9);							
						} else if(nomeArquivo9.contains("evolucao")) {
							correiosNe.confirmarRecebimentoAR(nomeArquivo9);
						} else 
							throw new MensagemException("Tipo não definido");
						request.setAttribute("MensagemOk", "\"" + nomeArquivo9 +  "\" lido com sucesso");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// EXECUTAR FLUXO COMPLETO
				} else if(fluxo.equalsIgnoreCase("10")) {
					try {
						String conteudoArquivo = getConteudoArquivo(request);
						String[] dadosCorreios = conteudoArquivo.split("\r\n");
						StringBuffer processados = new StringBuffer();
						byte[] data = null;
						String idPendencia = null;
						String codRastreamento = null;
						boolean recibo = false;
						boolean inconsistencia = false;
						boolean postado = false;
						boolean finalizador = false;
						boolean devolucaoAR = false;
						
						for (int i = 0; i < dadosCorreios.length; i++) {
							codRastreamento = "";
							idPendencia = "";
							if(dadosCorreios[i].contains("BR")) 
								codRastreamento = dadosCorreios[i];
							else
								idPendencia = dadosCorreios[i];
							historico = correiosNe.consultarHistorico(idPendencia, codRastreamento, intervalo6);
							recibo = false;
							inconsistencia = false;
							postado = false;
							finalizador = false;
							devolucaoAR = false;
							for (int j = 4; j < historico.length; j++) {
								if(historico[j][0] == null) {
									break;
								} else if(historico[j][0] != null && historico[j][0].contains("ecibo")) {					//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Informação sobre Limite de Postagem]
									recibo = correiosNe.executarRecibo(historico[j][2], historico[j][4]);
									idPendencia = historico[j][2];
									codRastreamento = historico[j][4];
								} else if(historico[j][0] != null && historico[j][0].contains("nconsistencia")) {			//[Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
									inconsistencia = correiosNe.executarInconsistencia(historico[j][2], historico[j][3], historico[j][4]);
								} else if(historico[j][0] != null && historico[j][0].contains("ostado")) {					//[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataPostagem|HoraPostagem|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPrimeiraTentativaEntrega]
									postado = correiosNe.executarPostagem(idPendencia, codRastreamento, historico[j][8]);
								} else if(historico[j][0] != null && historico[j][0].contains("inalizador")) {				//[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
									finalizador = correiosNe.executarEntrega(codRastreamento, historico[j][7], historico[j][8], historico[j][9]);
								} else if(historico[j][0] != null && historico[j][0].contains("evolucao")) {
									devolucaoAR = correiosNe.executarAR(idPendencia, codRastreamento, historico[j][0]);		//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
								}
							}
							processados.append(dadosCorreios[i]);
							if(recibo) processados.append(">>Recibo>>");
							if(inconsistencia) processados.append(">>Inconsistencia>>");	
							if(postado) processados.append(">>Postado>>");
							if(finalizador) processados.append(">>Finalizador>>");
							if(devolucaoAR) processados.append(">>DevolucaoAR>>");
							processados.append("\n");
						}
						data = processados.toString().getBytes("UTF-8");
						enviarTXT(response, data, "registros_processados");
						request.setAttribute("MensagemOk", "Arquivo lido com sucesso");	
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// PREENCHER META DADOS
				} else if(fluxo.equalsIgnoreCase("11")) {
					try {
						if (request.getAttribute("NumeroRegistros11") != null) numeroRegistros11 = request.getAttribute("NumeroRegistros11").toString();
						int cartas = correiosNe.preencherMetaDados(numeroRegistros11);
						request.setAttribute("MensagemOk", cartas + " cartas preenchidas com sucesso");	
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}				
				// DESCARTAR PENDÊNCIAS
				} else if(fluxo.equalsIgnoreCase("12")) {
					try {
						String conteudoArquivo = getConteudoArquivo(request);
						if(conteudoArquivo != null) {
							String[] dadosCorreios = conteudoArquivo.split("\r\n");
							PendenciaDt pendenciaDt = null;
							UsuarioDt usuarioDt = new UsuarioDt();
							usuarioDt.setId(UsuarioDt.SistemaProjudi);
							usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
							usuarioDt.setIpComputadorLog("Servidor");
							usuarioDt.setNome("Sistema PROJUDI");
							
							for (int i = 0; i < dadosCorreios.length; i++) {
								pendenciaDt = new PendenciaDt();
								pendenciaDt.setId(dadosCorreios[i]);
								pendenciaDt.setDataVisto("1");
								new PendenciaNe().descartarPendencia(pendenciaDt, usuarioDt);
							}
							request.setAttribute("MensagemOk", "Pendências descartadas");	
						} else {
							request.setAttribute("MensagemErro", "Arquivo não encontrado");	
						}
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}
				// CORRIGIR DATA INSERÇÃO ARQUIVOS ZIP
				} else if(fluxo.equalsIgnoreCase("13")) {
					try {
						if(correiosNe.corrigirDataInsercao())
							request.setAttribute("MensagemOk", "Data de inserção corrigidas com sucesso");
						else
							request.setAttribute("MensagemErro", "Arquivos não encontrados");
					} catch (Exception e) {
						throw new MensagemException(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					}				
				}
				request.setAttribute("Id_Pendencia1", idPendencia1);
				request.setAttribute("CodRastreamento1", codRastreamento1);				
				request.setAttribute("Id_Pendencia2", idPendencia2);
				request.setAttribute("CodInconsistencia2", codInconsistencia2);
				request.setAttribute("MsgInconsistencia2", msgInconsistencia2);				
				request.setAttribute("Id_Pendencia3", idPendencia3);
				request.setAttribute("CodRastreamento3", codRastreamento3);
				request.setAttribute("DataPostagem3", dataPostagem3);				
				request.setAttribute("CodRastreamento4", codRastreamento4);
				request.setAttribute("CodBaixa4", codBaixa4);
				request.setAttribute("DataEntrega4", dataEntrega4);
				request.setAttribute("HoraEntrega4", horaEntrega4);
				request.setAttribute("Id_Pendencia5", idPendencia5);
				request.setAttribute("CodRastreamento5", codRastreamento5);
				request.setAttribute("NomeArquivo5", nomeArquivo5);
				request.setAttribute("Id_Pendencia6", idPendencia6);
				request.setAttribute("CodRastreamento6", codRastreamento6);
				request.setAttribute("Intervalo6", intervalo6);
				request.setAttribute("CodRastreamento7", codRastreamento7);
				request.setAttribute("NomeArquivo7", nomeArquivo7);
				request.setAttribute("Id_MovimentacaoArquivo7", idMovimentacaoArquivo7);
				request.setAttribute("NomeArquivo9", nomeArquivo9);
				request.setAttribute("NumeroRegistros11", numeroRegistros11);
				request.setAttribute("tempPrograma", "Ferramentas e-Carta");
				request.setAttribute("HistoricoCarta", historico);
				break;
				
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);								
				break;				
		}
		
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PosicaoPaginaAtual", posicaoPaginaAtual);
		request.getSession().setAttribute("RelatorioPostagemECartasDt", relatorioPostagemECartasDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}	
}

