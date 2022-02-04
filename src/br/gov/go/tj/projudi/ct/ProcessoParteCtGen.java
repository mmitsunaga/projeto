package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAusenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3063160396831117244L;

    public  ProcessoParteCtGen() {

	} 
		public int Permissao() {
			return ProcessoParteDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteDt ProcessoPartedt;
		ProcessoParteNe ProcessoPartene;
		int passoEditar = -1;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParte.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParte");

		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		ProcessoPartene =(ProcessoParteNe)request.getSession().getAttribute("ProcessoPartene");
		if (ProcessoPartene == null )  ProcessoPartene = new ProcessoParteNe();  


		ProcessoPartedt =(ProcessoParteDt)request.getSession().getAttribute("ProcessoPartedt");
		if (ProcessoPartedt == null )  ProcessoPartedt = new ProcessoParteDt();  

		ProcessoPartedt.setNome( request.getParameter("Nome")); 
		ProcessoPartedt.setSexo( request.getParameter("Sexo")); 
		ProcessoPartedt.setNomeMae( request.getParameter("NomeMae")); 
		ProcessoPartedt.setNomePai( request.getParameter("NomePai")); 
		ProcessoPartedt.setId_ProcessoParteTipo( request.getParameter("Id_ProcessoParteTipo")); 
		ProcessoPartedt.setProcessoParteTipo( request.getParameter("ProcessoParteTipo")); 
		ProcessoPartedt.setId_Processo( request.getParameter("Id_Processo")); 
		ProcessoPartedt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		ProcessoPartedt.setId_ProcessoParteAusencia( request.getParameter("Id_ProcessoParteAusencia")); 
		ProcessoPartedt.setProcessoParteAusencia( request.getParameter("ProcessoParteAusencia")); 
		ProcessoPartedt.setId_Naturalidade( request.getParameter("Id_Naturalidade")); 
		ProcessoPartedt.setCidadeNaturalidade( request.getParameter("CidadeNaturalidade")); 
		ProcessoPartedt.setDataNascimento( request.getParameter("DataNascimento")); 
		ProcessoPartedt.setId_EstadoCivil( request.getParameter("Id_EstadoCivil")); 
		ProcessoPartedt.setEstadoCivil( request.getParameter("EstadoCivil")); 
		ProcessoPartedt.setId_Profissao( request.getParameter("Id_Profissao")); 
		ProcessoPartedt.setProfissao( request.getParameter("Profissao")); 
		ProcessoPartedt.setId_Endereco( request.getParameter("Id_Endereco")); 
		ProcessoPartedt.setEndereco( request.getParameter("Endereco")); 
		ProcessoPartedt.setRg( request.getParameter("Rg")); 
		ProcessoPartedt.setId_RgOrgaoExpedidor( request.getParameter("Id_RgOrgaoExpedidor")); 
		ProcessoPartedt.setRgOrgaoExpedidor( request.getParameter("RgOrgaoExpedidor")); 
		ProcessoPartedt.setRgDataExpedicao( request.getParameter("RgDataExpedicao")); 
		ProcessoPartedt.setId_Escolaridade( request.getParameter("Id_Escolaridade")); 
		ProcessoPartedt.setEscolaridade( request.getParameter("Escolaridade")); 
		ProcessoPartedt.setCpf( request.getParameter("Cpf")); 
		ProcessoPartedt.setCnpj( request.getParameter("Cnpj")); 
		ProcessoPartedt.setTituloEleitor( request.getParameter("TituloEleitor")); 
		ProcessoPartedt.setTituloEleitorZona( request.getParameter("TituloEleitorZona")); 
		ProcessoPartedt.setTituloEleitorSecao( request.getParameter("TituloEleitorSecao")); 
		ProcessoPartedt.setCtps( request.getParameter("Ctps")); 
		ProcessoPartedt.setCtpsSerie( request.getParameter("CtpsSerie")); 
		ProcessoPartedt.setId_CtpsUf( request.getParameter("Id_CtpsUf")); 
		ProcessoPartedt.setEstadoCtpsUf( request.getParameter("EstadoCtpsUf")); 
		ProcessoPartedt.setPis( request.getParameter("Pis")); 
		ProcessoPartedt.setDataCadastro( request.getParameter("DataCadastro")); 
		ProcessoPartedt.setEMail( request.getParameter("EMail")); 
		ProcessoPartedt.setTelefone( request.getParameter("Telefone")); 
		ProcessoPartedt.setCelular( request.getParameter("Celular")); 
		if (request.getParameter("CitacaoOnline") != null)
			ProcessoPartedt.setCitacaoOnline( request.getParameter("CitacaoOnline")); 
		else ProcessoPartedt.setCitacaoOnline("false");
		if (request.getParameter("IntimacaoOnline") != null)
			ProcessoPartedt.setIntimacaoOnline( request.getParameter("IntimacaoOnline")); 
		else ProcessoPartedt.setIntimacaoOnline("false");
		if (request.getParameter("RecebeEMail") != null)
			ProcessoPartedt.setRecebeEMail( request.getParameter("RecebeEMail")); 
		else ProcessoPartedt.setRecebeEMail("false");
		ProcessoPartedt.setDataBaixa( request.getParameter("DataBaixa")); 
		if (request.getParameter("Citada") != null)
			ProcessoPartedt.setCitada( request.getParameter("Citada")); 
		else ProcessoPartedt.setCitada("false");
		ProcessoPartedt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 
		ProcessoPartedt.setUsuario( request.getParameter("Usuario")); 
		ProcessoPartedt.setId_GovernoTipo( request.getParameter("Id_GovernoTipo")); 
		ProcessoPartedt.setGovernoTipo( request.getParameter("GovernoTipo")); 
		ProcessoPartedt.setId_EmpresaTipo( request.getParameter("Id_EmpresaTipo")); 
		ProcessoPartedt.setEmpresaTipo( request.getParameter("EmpresaTipo")); 
		ProcessoPartedt.setProcessoParteTipoCodigo( request.getParameter("ProcessoParteTipoCodigo")); 
		ProcessoPartedt.setProcessoParteAusenciaCodigo( request.getParameter("ProcessoParteAusenciaCodigo")); 

		ProcessoPartedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoPartedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoPartene.excluir(ProcessoPartedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
				request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
				request.setAttribute("tempRetorno","ProcessoParte");
				tempList =ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoParte", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					ProcessoPartedt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoPartedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoPartene.Verificar(ProcessoPartedt); 
					if (Mensagem.length()==0){
						ProcessoPartene.salvar(ProcessoPartedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoParteTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoParteTipo","Id_ProcessoParteTipo");
					request.setAttribute("tempBuscaProcessoParteTipo","ProcessoParteTipo");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/ProcessoParteTipoLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoProcessoParteTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoParteTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Processo","Id_Processo");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteAusenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoParteAusencia","Id_ProcessoParteAusencia");
					request.setAttribute("tempBuscaProcessoParteAusencia","ProcessoParteAusencia");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/ProcessoParteAusenciaLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoProcessoParteAusencia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoParteAusencia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Cidade","Uf"};
						String[] lisDescricao = {"Cidade","Uf"};
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Cidade");
						request.setAttribute("tempBuscaDescricao", "Cidade");
						request.setAttribute("tempBuscaPrograma", "Cidade");
						request.setAttribute("tempRetorno", "ProcessoParte");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = 1;
					}else{
						String stTemp = "";
						stTemp = ProcessoPartene.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						try{
							enviarJSON(response, stTemp);
							
						} catch(Exception e) {}
						return;
					}
					break;
				case (ProfissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Profissao","Id_Profissao");
					request.setAttribute("tempBuscaProfissao","Profissao");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/ProfissaoLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoProfissao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProfissao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Endereco","Id_Endereco");
					request.setAttribute("tempBuscaEndereco","Endereco");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEndereco", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (RgOrgaoExpedidorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_RgOrgaoExpedidor","Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaRgOrgaoExpedidor","RgOrgaoExpedidor");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/RgOrgaoExpedidorLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoRgOrgaoExpedidor(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaRgOrgaoExpedidor", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

				case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_CtpsUf","Id_CtpsUf");
					request.setAttribute("tempBuscaEstado","Estado");
					request.setAttribute("tempRetorno","ProcessoParte");
					tempList =ProcessoPartene.consultarDescricaoEstado(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEstado", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_UsuarioServentia","Id_UsuarioServentia");
					request.setAttribute("tempBuscaUsuarioServentia","UsuarioServentia");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/UsuarioServentiaLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuarioServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_GovernoTipo","Id_GovernoTipo");
					request.setAttribute("tempBuscaGovernoTipo","GovernoTipo");
					request.setAttribute("tempRetorno","ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/GovernoTipoLocalizar.jsp";
					tempList =ProcessoPartene.consultarDescricaoGovernoTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaGovernoTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoPartene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoParte");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoPartedt.getId()))){
						ProcessoPartedt.limpar();
						ProcessoPartedt = ProcessoPartene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoPartedt",ProcessoPartedt );
		request.getSession().setAttribute("ProcessoPartene",ProcessoPartene );
		request.setAttribute("PassoEditar", passoEditar);

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
