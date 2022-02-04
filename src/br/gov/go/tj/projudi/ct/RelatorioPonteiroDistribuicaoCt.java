package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPonteiroDistribuicaoDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class RelatorioPonteiroDistribuicaoCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioPonteiroDistribuicaoDt.CodigoPermissaoPonteiroDistribuicao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioPonteiroDistribuicaoDt relatorioPonteiroDistribuicaoDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		//-fim controle de buscas ajax
		
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempPrograma", "Relatório do Ponteiro de Distribuição por Serventia");
		request.setAttribute("tempRetorno", "RelatorioPonteiroDistribuicao");
		request.setAttribute("tempBuscaSistema", "Sistema");			
		
		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioPonteiroDistribuicaoDt = (RelatorioPonteiroDistribuicaoDt) request.getSession().getAttribute("RelatorioPonteiroDistribuicaodt");
		if (relatorioPonteiroDistribuicaoDt == null)
			relatorioPonteiroDistribuicaoDt = new RelatorioPonteiroDistribuicaoDt();
		
		if (request.getParameter("Id_AreaDistribuicao") != null) {
			if (request.getParameter("Id_AreaDistribuicao").equals("null")) {
				relatorioPonteiroDistribuicaoDt.setIdAreaDistribuicao("");
			} else {
				relatorioPonteiroDistribuicaoDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			}
		}		
		if (request.getParameter("AreaDistribuicao") != null) {
			if (request.getParameter("AreaDistribuicao").equals("null")) {
				relatorioPonteiroDistribuicaoDt.setAreaDistribuicao("");
			} else {
				relatorioPonteiroDistribuicaoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
			}
		}
		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioPonteiroDistribuicaoDt.setIdServentia("");
			} else {
				relatorioPonteiroDistribuicaoDt.setIdServentia(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioPonteiroDistribuicaoDt.setServentia("");
			} else {
				relatorioPonteiroDistribuicaoDt.setServentia(request.getParameter("Serventia"));
			}
		}		
		if (request.getParameter("Data_Verificacao") != null) {
			if (request.getParameter("Data_Verificacao").equals("null")) {
				relatorioPonteiroDistribuicaoDt.setDataVerificacao("");
			} else {
				relatorioPonteiroDistribuicaoDt.setDataVerificacao(request.getParameter("Data_Verificacao"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
    		case Configuracao.Imprimir:
    			
    			String tipoRelatorio = request.getParameter("Tipo_Relatorio");
    			mensagemRetorno = "";
    			
    			//validar campos preenchidos na tela.
    			////Tipo 3 = Relatório de Distribuição por Juízes/Desembargadores da Serventia
    			if(tipoRelatorio != null && tipoRelatorio.equals("1") || tipoRelatorio.equals("2")) {
    				if (relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao() == null || relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao().equalsIgnoreCase("") 
    						|| relatorioPonteiroDistribuicaoDt.getAreaDistribuicao() == null || relatorioPonteiroDistribuicaoDt.getAreaDistribuicao().equalsIgnoreCase("")) {
    					mensagemRetorno = "Área de Distribuição deve ser informada.";
    				}
    			} else if(tipoRelatorio != null && tipoRelatorio.equals("3")){
    			////Tipo 3 = Relatório de Distribuição por Juízes/Desembargadores da Serventia
    				if (relatorioPonteiroDistribuicaoDt.getIdServentia() == null || relatorioPonteiroDistribuicaoDt.getIdServentia().equalsIgnoreCase("")) {
    					mensagemRetorno = "Serventia deve ser informada.";
    				}
    			}
    			if (relatorioPonteiroDistribuicaoDt.getDataVerificacao() == null || relatorioPonteiroDistribuicaoDt.getDataVerificacao().equalsIgnoreCase("")){
    					mensagemRetorno = "Data de Verificação deve ser informada.";
    			} else {
    				//verifica se a data informada é maior que 03/06/2016
    				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
    				Date dataVerificacaoTela = new Date(format.parse(relatorioPonteiroDistribuicaoDt.getDataVerificacao()).getTime()); 
    				Date dataLimite = new Date(format.parse("03/06/2016").getTime());
    				
    				if(dataLimite.getTime() > dataVerificacaoTela.getTime()) {
    					mensagemRetorno = "Data de Verificação mínima deve ser 03/06/2016, data de entrada do novo ponteiro de distribuição.";
    				}
    				//Liberando o administrador dessa validação. Motivo: às vezes precisamos ver a situação
    				//do ponteiro no momento em que está sendo gerado. O usuário comum não precisa disso.
    				if(!UsuarioSessao.isAdministrador() &&  Funcoes.isDataMaiorIgualDataAtual(relatorioPonteiroDistribuicaoDt.getDataVerificacao())) {
    					mensagemRetorno = "Data de Verificação não pode ser maior que a data de ontem.";
    				}
    			}
    			
    			//se for os relatórios "Acompanhar Distribuição" ou "Acompanhar Situação do Ponteiro", não pode selecionar Tipo de Arquivo "Texto".
    			if(tipoRelatorio != null && tipoRelatorio.equals("5") || tipoRelatorio.equals("6")){
    				String tipoArquivo = request.getParameter("Tipo_Arquivo");
    				if(tipoArquivo.equals("2")){
    					mensagemRetorno = "O relatório selecionado não pode ser apresentado em Arquivo do Tipo Texto. Favor selecionar Arquivo do Tipo Relatório."; 
    				}
    			}
    
    			if (mensagemRetorno.equals("")) {
    				String tipoArquivo = request.getParameter("Tipo_Arquivo");
    
    				//passo os parametros para a thread do relatorio e fico aguardando o termino
    				//quando iniciar a execução a variavel ProcessandoReletorio recebera 1 na session
    				//quando terminar a variavel RelatorioPonteiroDistribuicao esta na session
    				//se não estiver processo um relatorio, inicia uma nova thead
    				if (request.getSession().getAttribute("ProcessandoReletorio")==null) {
        				ThreadRelatorio exec = new ThreadRelatorio(relatorioEstatisticaNe, 
                                            						tipoRelatorio, 
                                            						request.getSession(),
                                            						ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
                                            						relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(),
                                            						relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(),
                                            						relatorioPonteiroDistribuicaoDt.getDataVerificacao(),
                                            						tipoArquivo, 
                                            						UsuarioSessao.getUsuarioDt().getNome(),
                                            						relatorioPonteiroDistribuicaoDt.getIdServentia(), 
                                            						relatorioPonteiroDistribuicaoDt.getServentia());
        				new Thread(exec).start();
    				}
    				request.setAttribute("tempAguardandoProcessamento", "1");
    				
    				if(tipoRelatorio.equals("1") || tipoRelatorio.equals("2") || tipoRelatorio.equals("5") || tipoRelatorio.equals("6")) {
    					stAcao = "WEB-INF/jsptjgo/RelatorioPonteiroDistribuicao.jsp";
    				} else {
    					stAcao = "WEB-INF/jsptjgo/RelatorioPonteiroDistribuicaoJuizServentia.jsp";
    				}
    				
    //				if(tipoRelatorio.equals("1")) {
    //					//se tipoRelatorio for 1, será o relatório de Situação do Ponteiro por Área de Distribuição
    //					byTemp = relatorioEstatisticaNe.verificarSituacaoPonteiroDistribuicaoArea(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("2")) {
    //					//Se for 2, será a Listagem de Processos Distribuídos por Área de Distribuição
    //					byTemp = relatorioEstatisticaNe.listarProcessosPonteiroDistribuicaoArea(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("3")) {
    //					//Se for 3, será a verificação da Situação do Ponteiro por Serventia
    //					byTemp = relatorioEstatisticaNe.verificarSituacaoPonteiroDistribuicaoResponsavel(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdServentia(), relatorioPonteiroDistribuicaoDt.getServentia(), relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("4")) {
    //					//Se for 4, será a Listagem de Processo Distribuídos por Serventia
    //					byTemp = relatorioEstatisticaNe.listarProcessosPonteiroDistribuicaoResponsavel(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdServentia(), relatorioPonteiroDistribuicaoDt.getServentia(), relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("5")){
    //					//Se for 5, será o relatório de Acompanhamento da Distribuição nos últimos 30 dias por Área de Distribuição
    //					byTemp = relatorioEstatisticaNe.acompanharDistribuicaoArea(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("6")){
    //					//Se for 6, será o relatório de Acompanhamento da Situação do Ponteiro de Distribuição nos últimos 30 dias por Área de Distribuição
    //					byTemp = relatorioEstatisticaNe.acompanharSituacaoPonteiroDistribuicaoArea(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("7")){
    //					//Se for 7, será o relatório de Acompanhamento da Distribuição para a Serventia nos últimos 30 dias 
    //					byTemp = relatorioEstatisticaNe.acompanharDistribuicaoResponsavel(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getIdServentia(), relatorioPonteiroDistribuicaoDt.getServentia(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), UsuarioSessao.getUsuarioDt().getNome());
    //				} else if(tipoRelatorio.equals("8")){
    //					//Se for 8, será o relatório de Acompanhamento da Situação do Ponteiro de Distribuição dentro da Serventia nos últimos 30 dias 
    //					byTemp = relatorioEstatisticaNe.acompanharSituacaoPonteiroDistribuicaoResponsavel(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioPonteiroDistribuicaoDt.getIdAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getAreaDistribuicao(), relatorioPonteiroDistribuicaoDt.getIdServentia(), relatorioPonteiroDistribuicaoDt.getServentia(), relatorioPonteiroDistribuicaoDt.getDataVerificacao(), UsuarioSessao.getUsuarioDt().getNome());
    //				}
    //				
    //				// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um
    //				// arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
    //				if (tipoArquivo != null && tipoArquivo.equals("2")) {					
    //					enviarTXT(response, byTemp,"RelatorioPonteiroDistribuicao");
    //				} else {					
    //					enviarPDF(response, byTemp,"RelatorioPonteiroDistribuicao");
    //				}
    //				
    //				return;
    
    			} else {
    				if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
    					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
    				}
    				request.setAttribute("MensagemErro", mensagemRetorno);
    				request.setAttribute("PaginaAtual", Configuracao.Editar);
    			}
    			break;
    		case Configuracao.Novo:
    			relatorioPonteiroDistribuicaoDt.limparCamposConsulta();
    			relatorioPonteiroDistribuicaoDt = this.atribuirDataOntem(relatorioPonteiroDistribuicaoDt);
    			request.setAttribute("PaginaAtual", Configuracao.Editar);
    			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
    				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
    			}
    			break;
    			
    		case Configuracao.Curinga6:
    			//Relatório de Distribuição por Juízes/Desembargadores da Serventia
    			if (relatorioPonteiroDistribuicaoDt.getDataVerificacao().equals("")){
    				relatorioPonteiroDistribuicaoDt = this.atribuirDataOntem(relatorioPonteiroDistribuicaoDt);
    			}
    			stAcao = "WEB-INF/jsptjgo/RelatorioPonteiroDistribuicaoJuizServentia.jsp";
    			request.setAttribute("tempPrograma", "Relatório do Ponteiro de Distribuição por Serventia");
    			request.getSession().setAttribute("stAcaoRetorno", stAcao);
    			request.setAttribute("PaginaAtual", Configuracao.Editar);
    			//Esse relatório será considerado o relatório do tipo 3
    			request.setAttribute("tipoRelatorio", "3");
    			break;
    			//baixar relatorio salvo sa session
    		case Configuracao.Curinga9:
    			byte[] temp =(byte[]) request.getSession().getAttribute("RelatorioPonteiroDistribuicao");
    			if(request.getParameter("EstaPronto") != null) {
    				if(temp != null) {
    					enviarTXT(response, "ok".getBytes(),"RelatorioPonteiroDistribuicao");
    				}else {
    					throw new MensagemException("Ainda não terminou");
    				}	
    			}else {
    				request.getSession().removeAttribute("RelatorioPonteiroDistribuicao");
    				if (request.getSession().getAttribute("TipoArquivo") != null && request.getSession().getAttribute("TipoArquivo").equals("2")) {					
        				enviarTXT(response, temp,"RelatorioPonteiroDistribuicao");
        			} else {					
        				enviarPDF(response, temp,"RelatorioPonteiroDistribuicao");
        			}
    			}
    			
    			return;
    		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
    			if (request.getParameter("Passo")==null){
    				String[] lisNomeBusca = {"Área de Distribuição"};
    				String[] lisDescricao = {"Área de Distribuição"};
    				
    				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
    				request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
    				request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
    				request.setAttribute("tempBuscaPrograma","Área de Distribuição");			
    				request.setAttribute("tempRetorno","RelatorioPonteiroDistribuicao");		
    				request.setAttribute("tempDescricaoId","Id");
    				//Garantindo que essa consulta de Serventia retorne para o local correto no CT
    				String tipoRel = (String)request.getParameter("Tipo_Relatorio");
    				if(tipoRel != null && tipoRel.equals("3")){
    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
    				} else {
    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
    				}
    				request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
    				request.setAttribute("PosicaoPaginaAtual", "0");
    				request.setAttribute("QuantidadePaginas", "0");
    				request.setAttribute("lisNomeBusca", lisNomeBusca);
    				request.setAttribute("lisDescricao", lisDescricao);
    			} else {
    				String stTemp="";
    				
    					if(UsuarioSessao.podeConsultarOutrasAreasDistribuicao()){					
    							stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, posicaoPaginaAtual);
    					} else {
    						//se o usuário for comum, a consulta de área de distribuição ficará restrita às áreas relacionadas à serventia dele					
    						stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoServentiaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt().getId_Serventia(), posicaoPaginaAtual);
    					}
    									
    					enviarJSON(response, stTemp);
    					
    				
    				return;								
    			}
    		break;
    		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
    			if (request.getParameter("Passo")==null){
    				//lisNomeBusca = new ArrayList();
    				//lisDescricao = new ArrayList();
    				String[] lisNomeBusca = {"Serventia"};
    				String[] lisDescricao = {"Serventia","Estado"};	
    				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
    				request.setAttribute("tempBuscaId", "Id_Serventia");
    				request.setAttribute("tempBuscaDescricao", "Serventia");
    				request.setAttribute("tempBuscaPrograma", "Serventia");
    				request.setAttribute("tempRetorno", "RelatorioPonteiroDistribuicao");
    				request.setAttribute("tempDescricaoId", "Id");
    				//Garantindo que essa consulta de Serventia retorne para o local correto no CT
    				String tipoRel = (String)request.getParameter("Tipo_Relatorio");
    				if(tipoRel != null && tipoRel.equals("3")){
    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
    				} else {
    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
    				}
    				request.setAttribute("PaginaAtual",  String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
    				request.setAttribute("PosicaoPaginaAtual", "0");
    				request.setAttribute("QuantidadePaginas", "0");
    				request.setAttribute("lisNomeBusca", lisNomeBusca);
    				request.setAttribute("lisDescricao", lisDescricao);							
    			}else{
    				String stTemp = "";
    				stTemp =relatorioEstatisticaNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);
    								
    				enviarJSON(response, stTemp);
    					
    				
    				return;
    			}
    			break;
    			
    		default:
    			if (relatorioPonteiroDistribuicaoDt.getDataVerificacao().equals("")){
    				relatorioPonteiroDistribuicaoDt = this.atribuirDataOntem(relatorioPonteiroDistribuicaoDt);
    			}
    			
    			request.setAttribute("tempPrograma", "Relatório de Situação do Ponteiro por Área de Distribuição");
    			stAcao = "WEB-INF/jsptjgo/RelatorioPonteiroDistribuicao.jsp";
    			request.getSession().setAttribute("stAcaoRetorno", stAcao);
    			request.setAttribute("PaginaAtual", Configuracao.Editar);
    			break;
		}
				
		request.getSession().setAttribute("RelatorioPonteiroDistribuicaodt", relatorioPonteiroDistribuicaoDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data de ontem ao relatório ao DT.
	 * 
	 * @param relatorioPonteiroDistribuicaoDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioPonteiroDistribuicaoDt atribuirDataOntem(RelatorioPonteiroDistribuicaoDt relatorioPonteiroDistribuicaoDt) {
		Calendar dataAtual = Calendar.getInstance();
		dataAtual.add(Calendar.DAY_OF_MONTH, -1);
		relatorioPonteiroDistribuicaoDt.setDataVerificacao(String.valueOf(dataAtual.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(dataAtual.get(Calendar.MONTH) + 1) + "/" + String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioPonteiroDistribuicaoDt;
	}
	
	class ThreadRelatorio implements Runnable {
		
		private RelatorioEstatisticaNe relatorioEstatisticaNe;
		private String tipoRelatorio;
		private HttpSession Session;
		private String Caminho;
		private String Id_AreaDistribuicao;
		private String AreaDistribuicao;
		private String DataVerificacao;
		private String TipoArquivo;
		private String NomeUsuario;
		private String Id_Serv;
		private String Serv;
		
		public ThreadRelatorio( RelatorioEstatisticaNe rel, String tipo, HttpSession session, String caminho, String id_areaDistribuicao, String areaDistribuicao, String dataVerificacao, String tipoArquivo, String nomeUsuario, String id_serv, String serv ) {
			relatorioEstatisticaNe = rel;
			tipoRelatorio=tipo;
			Session = session;
			Caminho = caminho;
			Id_AreaDistribuicao = id_areaDistribuicao;
			AreaDistribuicao = areaDistribuicao;
			DataVerificacao=dataVerificacao;
			TipoArquivo = tipoArquivo;
			NomeUsuario = nomeUsuario;
			Id_Serv = id_serv;
			Serv = serv;
		}
		@Override
		public void run() {
			byte[] byTemp = null;
			Session.setAttribute("ProcessandoReletorio", 1);
			Session.setAttribute("TipoArquivo", TipoArquivo);
			try {
    			if(tipoRelatorio.equals("1")) {
    				//se tipoRelatorio for 1, será o relatório de Situação do Ponteiro por Área de Distribuição
    				byTemp = relatorioEstatisticaNe.verificarSituacaoPonteiroDistribuicaoArea(Caminho , Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, TipoArquivo, NomeUsuario);
    			} else if(tipoRelatorio.equals("2")) {
    				//Se for 2, será a Listagem de Processos Distribuídos por Área de Distribuição
    				byTemp = relatorioEstatisticaNe.listarProcessosPonteiroDistribuicaoArea(Caminho , Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, TipoArquivo, NomeUsuario);
    			} else if(tipoRelatorio.equals("3")) {
    				//Se for 3, será a verificação da Situação do Ponteiro por Serventia
    				byTemp = relatorioEstatisticaNe.verificarSituacaoPonteiroDistribuicaoResponsavel(Caminho ,Id_Serv, Serv, Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, TipoArquivo, NomeUsuario);
    			} else if(tipoRelatorio.equals("4")) {
    				//Se for 4, será a Listagem de Processo Distribuídos por Serventia
    				byTemp = relatorioEstatisticaNe.listarProcessosPonteiroDistribuicaoResponsavel(Caminho ,Id_Serv, Serv, Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, TipoArquivo, NomeUsuario);
    			} else if(tipoRelatorio.equals("5")){
    				//Se for 5, será o relatório de Acompanhamento da Distribuição nos últimos 30 dias por Área de Distribuição
    				byTemp = relatorioEstatisticaNe.acompanharDistribuicaoArea(Caminho , Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, NomeUsuario);
    			} else if(tipoRelatorio.equals("6")){
    				//Se for 6, será o relatório de Acompanhamento da Situação do Ponteiro de Distribuição nos últimos 30 dias por Área de Distribuição
    				byTemp = relatorioEstatisticaNe.acompanharSituacaoPonteiroDistribuicaoArea(Caminho , Id_AreaDistribuicao,AreaDistribuicao, DataVerificacao, NomeUsuario);
    			} else if(tipoRelatorio.equals("7")){
    				//Se for 7, será o relatório de Acompanhamento da Distribuição para a Serventia nos últimos 30 dias 
    				byTemp = relatorioEstatisticaNe.acompanharDistribuicaoResponsavel(Caminho , Id_AreaDistribuicao,AreaDistribuicao,Id_Serv, Serv, DataVerificacao, NomeUsuario);
    			} else if(tipoRelatorio.equals("8")){
    				//Se for 8, será o relatório de Acompanhamento da Situação do Ponteiro de Distribuição dentro da Serventia nos últimos 30 dias 
    				byTemp = relatorioEstatisticaNe.acompanharSituacaoPonteiroDistribuicaoResponsavel(Caminho , Id_AreaDistribuicao,AreaDistribuicao,Id_Serv, Serv, DataVerificacao, NomeUsuario);
    			}
    			Session.setAttribute("RelatorioPonteiroDistribuicao", byTemp);    			
			} catch (Exception e) {
				//se der erro, não há nada a fazer.
			}finally {
				Session.removeAttribute("ProcessandoReletorio");
			}
				
			
		}
	};
}
