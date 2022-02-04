package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class LogCt extends LogCtGen {

	private static final long serialVersionUID = 1902312960920663516L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LogDt Logdt;
		LogNe Logne;
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String filtroId_LogTipo = "";
		String stAcao = "/WEB-INF/jsptjgo/Log.jsp";

		request.setAttribute("tempPrograma", "Log");

		Logne = (LogNe) request.getSession().getAttribute("Logne");
		if (Logne == null) Logne = new LogNe();

		Logdt = (LogDt) request.getSession().getAttribute("Logdt");
		if (Logdt == null) Logdt = new LogDt();

		Logdt.setId(request.getParameter("Id_Log"));
		Logdt.setTabela(request.getParameter("nomeTabela"));
		Logdt.setId_Tabela(request.getParameter("idTabela"));
		Logdt.setId_LogTipo(request.getParameter("Id_LogTipo"));
		Logdt.setLogTipo(request.getParameter("LogTipo"));
		Logdt.setDataInicial(request.getParameter("dataInicial"));
		Logdt.setDataFinal(request.getParameter("dataFinal"));
		Logdt.setData(request.getParameter("Data"));
		Logdt.setHora(request.getParameter("Hora"));
		Logdt.setId_Usuario(request.getParameter("Id_Usuario"));
		Logdt.setUsuario(request.getParameter("Usuario"));
		Logdt.setIpComputador(request.getParameter("IpComputador"));
		Logdt.setValorAtual(request.getParameter("ValorAtual"));
		Logdt.setValorNovo(request.getParameter("ValorNovo"));
		Logdt.setLogTipoCodigo(request.getParameter("LogTipoCodigo"));
		Logdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Logdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		boolean boLogErro = false;
		if (request.getParameter("log_erro")!=null) {
			boLogErro= Boolean.parseBoolean(request.getParameter("log_erro"));
		}
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				Logne.excluir(Logdt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Localizar:
				
				if (request.getParameter("Passo")==null){
					request.setAttribute("tempBuscaId", "Id_log");
					request.setAttribute("tempBuscaDescricao", "LogTipo");
					request.setAttribute("tempRetorno", "Log");					
					stAcao = "/WEB-INF/jsptjgo/LogLocalizar.jsp";															
					
				}else{					
					String stTemp = Logne.Verificar(Logdt.getId(),Logdt.getDataInicial(), Logdt.getDataFinal());
					
					if (stTemp.length()==0){
						if(boLogErro) {
							stTemp = Logne.consultarLogErroJSON(Logdt.getId());
						}else {
							stTemp = Logne.consultarLogJSON(Logdt.getId(), Logdt.getTabela(), Logdt.getDataInicial(), Logdt.getDataFinal(), filtroId_LogTipo, PosicaoPaginaAtual, Logdt.getId_Tabela());
						}
						
					}else{
						response.setContentType("text/plan");
						response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					}												
					enviarJSON(response, stTemp);
											
					return;
				}
				break;
				
			case Configuracao.Novo:
				Logdt.limpar();
				break;

			case Configuracao.SalvarResultado:
				Mensagem = Logne.Verificar(Logdt);
				if (Mensagem.length() == 0) {
					Logne.salvar(Logdt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;
			
			//Tela Log Erros
			case Configuracao.Curinga6:
				stAcao = "/WEB-INF/jsptjgo/LogData.jsp";
				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
				if(request.getParameter("ConsultaLogData") != null && !request.getParameter("ConsultaLogData").equals("") && Logdt.getData() != null && !Logdt.getData().equals("")){
					String posicaoPaginaAtual = "0"; 
					if(request.getParameter("PosicaoPaginaAtual") != null && !request.getParameter("PosicaoPaginaAtual").equals("")){
						posicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual").toString();
					}
					
					List listaLogs = Logne.listarLogErros(Logdt.getData(), posicaoPaginaAtual);
					
					request.setAttribute("qtdeTotalPaginas", Funcoes.StringToInt(listaLogs.get(listaLogs.size() - 1).toString()));
					listaLogs.remove(listaLogs.size() - 1);
					request.setAttribute("PosicaoPaginaAtual", posicaoPaginaAtual);
					Logdt.setListaLogData(listaLogs);
				} else {
					Logdt.limpar();
				}
				break;

			case (LogTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tipo de Log"};
					String[] lisDescricao = {"LogTipo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(LogTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);

					atribuirJSON(request, "Id_LogTipo", "LogTipo", "LogTipo", "Log", Configuracao.Localizar, stPemissao, lisNomeBusca, lisDescricao);
					
				}else{
					String stTemp = "";
						stTemp =Logne.consultarLogTipoDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			
				break;
				
			default:{

				stId = request.getParameter("Id_Log");
				if (stId != null && stId.length()>0) {
					Logdt.limpar();
					if(boLogErro) {
						Logdt = Logne.consultarIdLogErro(stId);
					}else {
						Logdt = Logne.consultarId(stId);
					}
				}
			}
			
		}

		request.getSession().setAttribute("Logdt", Logdt);
		request.getSession().setAttribute("Logne", Logne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
