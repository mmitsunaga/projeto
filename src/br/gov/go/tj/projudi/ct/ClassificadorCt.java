package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ClassificadorNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ClassificadorCt extends ClassificadorCtGen {

	private static final long serialVersionUID = -6149313449104275955L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ClassificadorDt Classificadordt;
		ClassificadorNe Classificadorne;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/Classificador.jsp";
		
		request.setAttribute("tempPrograma", "Classificador");
		request.setAttribute("tempBuscaId_Classificador", "Id_Classificador");
		request.setAttribute("tempBuscaClassificador", "Classificador");
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempRetorno", "Classificador");
		request.setAttribute("TituloPagina", "Classificar Processos");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null)
			request.getParameter("nomeBusca2");
		
		Classificadorne = (ClassificadorNe) request.getSession().getAttribute("Classificadorne");
		if (Classificadorne == null) Classificadorne = new ClassificadorNe();

		Classificadordt = (ClassificadorDt) request.getSession().getAttribute("Classificadordt");
		if (Classificadordt == null) Classificadordt = new ClassificadorDt();

		Classificadordt.setId(request.getParameter("Id_Classificador"));
		Classificadordt.setClassificador(request.getParameter("Classificador"));
		Classificadordt.setServentia(request.getParameter("Serventia"));
		Classificadordt.setPrioridade(request.getParameter("Prioridade"));
		Classificadordt.setServentiaCodigo(request.getParameter("ServentiaCodigo"));
		Classificadordt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Classificadordt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		//Verifica se usuário pode selecionar a serventia no momento do cadastro ou se essa deve ser recuperada da sessão
		boolean podeSelecionarServentia = false;
		if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.ADMINISTRADORES) {
			Classificadordt.setId_Serventia(request.getParameter("Id_Serventia"));
			podeSelecionarServentia = true;
		} else Classificadordt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
		request.setAttribute("podeSelecionarServentia", podeSelecionarServentia);

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade"};
					String[] camposHidden = {"Prioridade"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Classificador");
					request.setAttribute("tempBuscaDescricao", "Classificador");
					request.setAttribute("tempBuscaPrograma", "Classificador");
					request.setAttribute("tempRetorno", "Classificador");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					Classificadordt.limpar();
				}else{
					String stTemp = "";
					stTemp = Classificadorne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					enviarJSON(response, stTemp);
					return;
				}
				break;	

			case Configuracao.Novo:
					Classificadordt.limpar();
					break;
					
			case Configuracao.Excluir:
				String mensagem = Classificadorne.validarExclusaoClassificador(Classificadordt);
				if(mensagem.length() > 0) {
					request.setAttribute("MensagemOk", mensagem);
				} 
				break;

			case Configuracao.ExcluirResultado:
				Classificadorne.excluir(Classificadordt);
				request.setAttribute("MensagemOk", "Classificador Excluído com Sucesso");
				break;

			case Configuracao.SalvarResultado:
				Mensagem = Classificadorne.Verificar(Classificadordt);
				if (Mensagem.length() == 0) {
					Classificadorne.salvar(Classificadordt);
					request.setAttribute("MensagemOk", "Classificador Salvo com sucesso");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};	
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "Classificador");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
					stTemp = Classificadorne.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
											
					return;					
				}
				break;

		case Configuracao.Curinga6:			
			String posicaoPagina = "";
			int fluxo = Funcoes.StringToInt(request.getParameter("fluxoClassificador"), 1);

			String cpfPoloAtivo = request.getParameter("CpfCnpjPoloAtivo") == null ? "" : request.getParameter("CpfCnpjPoloAtivo");
			boolean boPoloAtivoNull = Funcoes.StringToBoolean(request.getParameter("PoloAtivoNull"));
			String cpfPoloPassivel = request.getParameter("CpfCnpjPoloPassivo") == null ? "" : request.getParameter("CpfCnpjPoloPassivo");
			boolean boPoloPassivoNull = Funcoes.StringToBoolean(request.getParameter("PoloPassivoNull"));
			String id_proc_tipo = request.getParameter("Id_ProcessoTipo") == null ? "" : request.getParameter("Id_ProcessoTipo");
			String proc_tipo = request.getParameter("ProcessoTipo") == null ? "" : request.getParameter("ProcessoTipo");
			String id_assunto = request.getParameter("Id_Assunto") == null ? "" : request.getParameter("Id_Assunto");
			String assunto = request.getParameter("Assunto") == null ? "" : request.getParameter("Assunto");
			String id_classificador = request.getParameter("Id_Classificador") == null ? "" : request.getParameter("Id_Classificador");
			String classificador = request.getParameter("Classificador") == null ? "" : request.getParameter("Classificador");
			String maxValor = request.getParameter("ValorMinimo") == null ? "" : request.getParameter("ValorMinimo");
			String minValor = request.getParameter("ValorMaximo") == null ? "" : request.getParameter("ValorMaximo");
			String id_classificarAlteracao = request.getParameter("Id_Classificador_Alteracao") == null ? "" : request.getParameter("Id_Classificador_Alteracao");
			String classificarAlteracao = request.getParameter("Classificador_Alteracao") == null ? "" : request.getParameter("Classificador_Alteracao");

			if (fluxo == 2) {
				Integer qtdClassificado = Classificadorne.classificarProcessos(cpfPoloAtivo, boPoloAtivoNull, cpfPoloPassivel, boPoloPassivoNull, id_proc_tipo, id_assunto, id_classificador, maxValor, minValor, id_classificarAlteracao, UsuarioSessao.getId_Serventia());
				if (qtdClassificado > 0) {
					if (StringUtils.isEmpty(id_classificarAlteracao)) {
						request.setAttribute("MensagemOk", "Processos desclassificados\nQuantidade de processos desclassificados: " + qtdClassificado);
					} else {
						request.setAttribute("MensagemOk", "Processos classificados com Sucesso\nQuantidade de processos classificados: " + qtdClassificado);
					}
				} else {
					request.setAttribute("MensagemErro", "Erro! Processos não classificados");
				}
			}

			request.setAttribute("PaginaAtual", Configuracao.Curinga6);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", Classificadorne.getQuantidadePaginas());

			request.setAttribute("CpfCnpjPoloAtivo", cpfPoloAtivo);
			request.setAttribute("PoloAtivoNull", boPoloAtivoNull);
			request.setAttribute("CpfCnpjPoloPassivo", cpfPoloPassivel);
			request.setAttribute("PoloPassivoNull", boPoloPassivoNull);
			request.setAttribute("Id_ProcessoTipo", id_proc_tipo);
			request.setAttribute("ProcessoTipo", proc_tipo);
			request.setAttribute("Id_Assunto", id_assunto);
			request.setAttribute("Assunto", assunto);
			request.setAttribute("Id_Classificador", id_classificador);
			request.setAttribute("Classificador", classificador);
			request.setAttribute("ValorMinimo", maxValor);
			request.setAttribute("ValorMaximo", minValor);
			request.setAttribute("Id_Classificador_Alteracao", id_classificarAlteracao);
			request.setAttribute("Classificador_Alteracao", classificarAlteracao);

			request.setAttribute("ListaProcesso", null);
			request.setAttribute("podeClassificar", false);

			stAcao = "/WEB-INF/jsptjgo/ProcessoClassificar.jsp";

			break;
			
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("Classificadordt", Classificadordt);
		request.getSession().setAttribute("Classificadorne", Classificadorne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		
		dis.include(request, response);
	}

}
