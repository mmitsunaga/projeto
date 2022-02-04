package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla o cadastro de modelos
 */
public class ModeloCt extends ModeloCtGen {

    private static final long serialVersionUID = -7492128176487662094L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ModeloDt Modelodt;
		ModeloNe Modelone;
        int passoEditar = -1;
		String stId = "";
		String Mensagem = "";
		String filtroId_ArquivoTipo = "";
		String filtroArquivoTipo = "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");

		request.setAttribute("tempPrograma", "Modelo");
		request.setAttribute("tempRetorno", "Modelo");
		String stAcao = "/WEB-INF/jsptjgo/Modelo.jsp";

		Modelone = (ModeloNe) request.getSession().getAttribute("Modelone");
		if (Modelone == null) Modelone = new ModeloNe();

		Modelodt = (ModeloDt) request.getSession().getAttribute("Modelodt");
		if (Modelodt == null) Modelodt = new ModeloDt();

		Modelodt.setModelo(request.getParameter("Modelo"));
		Modelodt.setId_Serventia(request.getParameter("Id_Serventia"));
		Modelodt.setServentia(request.getParameter("Serventia"));
		Modelodt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		Modelodt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		Modelodt.setTexto(request.getParameter("Texto"));
		Modelodt.setCodigoTemp(request.getParameter("CodigoTemp"));
		Modelodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Modelodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		request.setAttribute("serventia", UsuarioSessao.getId_Serventia());
		request.setAttribute("grupo", UsuarioSessao.getGrupoCodigo());
		
		String qtdLocomocao = null;
		if (request.getParameter("qtdLocomocao") != null) {		   	        
		    qtdLocomocao = request.getParameter("qtdLocomocao");
		    request.getSession().setAttribute("qtdLocomocao" , qtdLocomocao);
			request.setAttribute("qtdLocomocao", qtdLocomocao);
    		Modelodt.setQtdLocomocao(qtdLocomocao);
		}
	
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if(request.getParameter("nomeBusca1") != null && request.getParameter("nomeBusca1").length() > 0) stNomeBusca1 = request.getParameter("nomeBusca1");
//		if (request.getParameter("nomeBusca2") != null && request.getParameter("nomeBusca2").length() > 0) filtroId_ArquivoTipo = request.getParameter("nomeBusca2");
		if (request.getParameter("id_ArquivoTipo") != null && request.getParameter("id_ArquivoTipo").length() > 0) filtroId_ArquivoTipo = request.getParameter("id_ArquivoTipo");
		if (request.getParameter("tempFluxo1") != null && request.getParameter("tempFluxo1").length() > 0 && !request.getParameter("tempFluxo1").equalsIgnoreCase("null") ) passoEditar = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		if (request.getParameter("arquivoTipo") != null) filtroArquivoTipo = request.getParameter("arquivoTipo");

		String id_ServentiaUsuario = UsuarioSessao.getUsuarioDt().getId_Serventia();
		int grupoUsuario = Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				Modelodt.limpar();
				request.getSession().setAttribute("qtdLocomocao" , null);				
				break;

			//Antes de salvar um modelo realiza verificações
			case Configuracao.Salvar:	
				////  se permitir ao gerenciamento alterar madelos de outra serventia
				///  alterar a regra de negocio para nao salvar como genérico.
				 
				
				if (!UsuarioSessao.getId_Serventia().equalsIgnoreCase(ServentiaDt.GERENCIAMENTO_SISTEMA_PRODUDI) &&
						 !UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.GERENCIAMENTO_TABELAS))) { 
				
				
				if (!Modelodt.getId().equalsIgnoreCase("") && (grupoUsuario != GrupoDt.ADMINISTRADORES)) {
					if (Modelodt.getId_Serventia().length() == 0 && Modelodt.getId_UsuarioServentia().length() == 0) {
						request.setAttribute("MensagemErro", "Não é possível alterar modelos genéricos");
					} else if (Modelodt.getId_Serventia().length() > 0 && !Modelodt.getId_Serventia().equals(id_ServentiaUsuario)) {
						request.setAttribute("MensagemErro", "Não é possível alterar modelos de outra serventia.");
					} else if (Modelodt.getId_UsuarioServentia().length() > 0 
								&& !Modelodt.getId_UsuarioServentia().equals(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia())
								&& !Modelodt.getId_UsuarioServentia().equals(UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe())) {
						String nomeProprietario = Modelone.consultarNomeProprietarioModelo(Modelodt.getId_UsuarioServentia());
						request.setAttribute("MensagemErro", "Não é possível alterar modelos de outro usuário. Proprietário do modelo: " + nomeProprietario);
					}
				}
				
				
				}
				
				
				break;

			//Salva o modelo
			case Configuracao.SalvarResultado:
				Mensagem = Modelone.Verificar(Modelodt);
				if (Mensagem.length() == 0) {
					Modelone.salvarModelo(Modelodt, UsuarioSessao.getUsuarioDt());
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			//Localizar Modelos
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo", "Serventia", "Tipo Modelo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("id_ArquivoTipo", filtroId_ArquivoTipo);
					request.setAttribute("arquivoTipo", filtroArquivoTipo);
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "Modelo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				}else{
					String stTemp = "";
					stTemp = Modelone.consultarModelosJSON(stNomeBusca1, PosicaoPaginaAtual, filtroId_ArquivoTipo, UsuarioSessao.getUsuarioDt());
					enviarJSON(response, stTemp);
					return;
				}
			
			// Consulta o modelo e retorna o json com os seguintes campos: id, modelo e texto (html) 
			case Configuracao.LocalizarDWR:
				String codigoModelo = request.getParameter("codigo");
				String texto = Modelone.consultarTextoModeloParaECartaJSON(codigoModelo);							
				enviarJSON(response, texto);
				return;	
				
			//Antes de excluir um modelo deve realizar verificações
			case Configuracao.Excluir:
				stId = request.getParameter("Id_Modelo");
				if (stId != null && !stId.isEmpty()) {
					if(  !stId.equalsIgnoreCase(Modelodt.getId())) {
						Modelodt.limpar();
						Modelodt = Modelone.consultarId(stId);
					}
				}
				if (!Modelodt.getId().equalsIgnoreCase("") && (grupoUsuario != GrupoDt.ADMINISTRADORES)) {
					if (Modelodt.getId_Serventia().length() == 0 && Modelodt.getId_UsuarioServentia().length() == 0) {
						request.setAttribute("MensagemErro", "Não é possível excluir modelos genéricos");
					} else if (Modelodt.getId_Serventia().length() > 0 && !Modelodt.getId_Serventia().equals(id_ServentiaUsuario)) {
						request.setAttribute("MensagemErro", "Não é possível excluir modelos de outra serventia.");
					} else if (Modelodt.getId_UsuarioServentia().length() > 0 
							&& !Modelodt.getId_UsuarioServentia().equals(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia())
							&& !Modelodt.getId_UsuarioServentia().equals(UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe())) {
					String nomeProprietario = Modelone.consultarNomeProprietarioModelo(Modelodt.getId_UsuarioServentia());
					request.setAttribute("MensagemErro", "Não é possível alterar modelos de outro usuário. Proprietário do modelo: " + nomeProprietario);
					}
				}
				break;

			case Configuracao.ExcluirResultado:
				Modelone.excluir(Modelodt, UsuarioSessao.isAdministrador());
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			//Prepara cópia de Modelo
			case Configuracao.Curinga6:
				Modelodt.setId("");
				Modelodt.setModelo("");
				Modelodt.setId_Serventia("null");
				Modelodt.setId_UsuarioServentia("null");
				Modelodt.setModeloCodigo("");
				request.setAttribute("MensagemOk", "Copiando Modelo");
				break;
				
			case Configuracao.Curinga7:
				String codigoBarra = request.getParameter("numero");
				byte[] imageBytes = Funcoes.ImageParaByte( Funcoes.CodigoBarraGeraImagem(codigoBarra, ""));				 							
				
				enviarImagem(response, imageBytes);
									
				return;						

			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "Modelo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "ArquivoTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if (passoEditar == 1) {
						request.setAttribute("tempBuscaId", "id_ArquivoTipo");
						request.setAttribute("tempBuscaDescricao", "arquivoTipo");
						request.setAttribute("tempFluxo1", "1");
						request.setAttribute("arquivoTipo", "");
					}
				}else{
					String stTemp = "";
					stTemp = Modelone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo().toString(),stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;

			default:
				//Significa que é está vindo da consulta de tipo de arquivo e deve retornar para a listagem de modelos já filtrado
				if (passoEditar == 1) {
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;
				} else {
					stId = request.getParameter("Id_Modelo");
					if (stId != null && !stId.isEmpty()) { 
						if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Modelodt.getId())) {
							Modelodt.limpar();
							Modelodt = Modelone.consultarId(stId);						
							request.setAttribute("qtdLocomocao",  Modelodt.getQtdLocomocao());
							request.getSession().setAttribute("qtdLocomocao" , Modelodt.getQtdLocomocao());
						}  
					}
						
				}
		}
		
		request.setAttribute("qtdLocomocao", request.getSession().getAttribute("qtdLocomocao"));
		request.getSession().setAttribute("Modelodt", Modelodt);
		request.getSession().setAttribute("Modelone", Modelone);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

}
