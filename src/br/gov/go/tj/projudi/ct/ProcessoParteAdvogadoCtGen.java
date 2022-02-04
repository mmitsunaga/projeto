package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteAdvogadoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3540499069130388502L;

    public  ProcessoParteAdvogadoCtGen() {

	} 
		public int Permissao(){
			return ProcessoParteAdvogadoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteAdvogadoDt ProcessoParteAdvogadodt;
		ProcessoParteAdvogadoNe ProcessoParteAdvogadone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParteAdvogado.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParteAdvogado");
		request.setAttribute("tempBuscaId_ProcessoParteAdvogado","Id_ProcessoParteAdvogado");
		request.setAttribute("tempBuscaProcessoParteAdvogado","ProcessoParteAdvogado");
		
		request.setAttribute("tempBuscaId_UsuarioServentiaAdvogado","Id_UsuarioServentiaAdvogado");
		request.setAttribute("tempBuscaUsuarioServentia","UsuarioServentia");
		request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
		request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
		request.setAttribute("tempBuscaId_Processo","Id_Processo");
		request.setAttribute("tempBuscaProcesso","Processo");
		request.setAttribute("tempBuscaId_ProcessoParteTipo","Id_ProcessoParteTipo");
		request.setAttribute("tempBuscaProcessoParteTipo","ProcessoParteTipo");

		request.setAttribute("tempRetorno","ProcessoParteAdvogado");



		ProcessoParteAdvogadone =(ProcessoParteAdvogadoNe)request.getSession().getAttribute("ProcessoParteAdvogadone");
		if (ProcessoParteAdvogadone == null )  ProcessoParteAdvogadone = new ProcessoParteAdvogadoNe();  


		ProcessoParteAdvogadodt =(ProcessoParteAdvogadoDt)request.getSession().getAttribute("ProcessoParteAdvogadodt");
		if (ProcessoParteAdvogadodt == null )  ProcessoParteAdvogadodt = new ProcessoParteAdvogadoDt();  

		ProcessoParteAdvogadodt.setNomeAdvogado( request.getParameter("NomeAdvogado")); 
		ProcessoParteAdvogadodt.setId_UsuarioServentiaAdvogado( request.getParameter("Id_UsuarioServentiaAdvogado")); 
		ProcessoParteAdvogadodt.setUsuarioAdvogado( request.getParameter("UsuarioAdvogado")); 
		ProcessoParteAdvogadodt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		ProcessoParteAdvogadodt.setNomeParte( request.getParameter("NomeParte")); 
		ProcessoParteAdvogadodt.setDataEntrada( request.getParameter("DataEntrada")); 
		ProcessoParteAdvogadodt.setDataSaida( request.getParameter("DataSaida")); 
		if (request.getParameter("Principal") != null)
			ProcessoParteAdvogadodt.setPrincipal( request.getParameter("Principal")); 
		else ProcessoParteAdvogadodt.setPrincipal("false");
		ProcessoParteAdvogadodt.setOabNumero( request.getParameter("OabNumero")); 
		ProcessoParteAdvogadodt.setOabComplemento( request.getParameter("OabComplemento")); 
		ProcessoParteAdvogadodt.setId_Processo( request.getParameter("Id_Processo")); 
		ProcessoParteAdvogadodt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		ProcessoParteAdvogadodt.setId_ProcessoParteTipo( request.getParameter("Id_ProcessoParteTipo")); 
		ProcessoParteAdvogadodt.setProcessoParteTipo( request.getParameter("ProcessoParteTipo")); 

		ProcessoParteAdvogadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteAdvogadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ProcessoParteAdvogadone.excluir(ProcessoParteAdvogadodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoParteAdvogadoLocalizar.jsp";
				tempList =ProcessoParteAdvogadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoParteAdvogado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoParteAdvogadone.getQuantidadePaginas());
					ProcessoParteAdvogadodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				ProcessoParteAdvogadodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ProcessoParteAdvogadone.Verificar(ProcessoParteAdvogadodt); 
				if (Mensagem.length()==0){
					ProcessoParteAdvogadone.salvar(ProcessoParteAdvogadodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/UsuarioServentiaLocalizar.jsp";
						tempList =ProcessoParteAdvogadone.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuarioServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ProcessoParteAdvogadone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
						tempList =ProcessoParteAdvogadone.consultarDescricaoProcessoParte(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcessoParte", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ProcessoParteAdvogadone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
						tempList =ProcessoParteAdvogadone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcesso", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ProcessoParteAdvogadone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoParteTipoLocalizar.jsp";
						tempList =ProcessoParteAdvogadone.consultarDescricaoProcessoParteTipo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcessoParteTipo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ProcessoParteAdvogadone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ProcessoParteAdvogado");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoParteAdvogadodt.getId()))){
						ProcessoParteAdvogadodt.limpar();
						ProcessoParteAdvogadodt = ProcessoParteAdvogadone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteAdvogadodt",ProcessoParteAdvogadodt );
		request.getSession().setAttribute("ProcessoParteAdvogadone",ProcessoParteAdvogadone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
