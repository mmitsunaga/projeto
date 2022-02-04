package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8044852090811629385L;

    public  ServentiaCargoCtGen() {

	} 
		public int Permissao(){
			return ServentiaCargoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoDt ServentiaCargodt;
		ServentiaCargoNe ServentiaCargone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ServentiaCargo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ServentiaCargo");
		request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
		request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
		request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
		request.setAttribute("tempBuscaServentia","Serventia");
		request.setAttribute("tempBuscaId_CargoTipo","Id_CargoTipo");
		request.setAttribute("tempBuscaCargoTipo","CargoTipo");
		request.setAttribute("tempBuscaId_UsuarioServentiaGrupo","Id_UsuarioServentiaGrupo");
		request.setAttribute("tempBuscaUsuarioServentiaGrupo","UsuarioServentiaGrupo");

		request.setAttribute("tempRetorno","ServentiaCargo");



		ServentiaCargone =(ServentiaCargoNe)request.getSession().getAttribute("ServentiaCargone");
		if (ServentiaCargone == null )  ServentiaCargone = new ServentiaCargoNe();  


		ServentiaCargodt =(ServentiaCargoDt)request.getSession().getAttribute("ServentiaCargodt");
		if (ServentiaCargodt == null )  ServentiaCargodt = new ServentiaCargoDt();  

		ServentiaCargodt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ServentiaCargodt.setId_Serventia( request.getParameter("Id_Serventia")); 
		ServentiaCargodt.setServentia( request.getParameter("Serventia")); 
		ServentiaCargodt.setId_CargoTipo(request.getParameter("Id_CargoTipo"));
		ServentiaCargodt.setCargoTipo(request.getParameter("CargoTipo"));
		ServentiaCargodt.setCargoTipoCodigo(request.getParameter("Id_CargoTipo"));
		ServentiaCargodt.setId_UsuarioServentiaGrupo( request.getParameter("Id_UsuarioServentiaGrupo")); 
		ServentiaCargodt.setUsuarioServentiaGrupo( request.getParameter("UsuarioServentiaGrupo")); 
		ServentiaCargodt.setQuantidadeDistribuicao( request.getParameter("QuantidadeDistribuicao")); 
		ServentiaCargodt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 
		ServentiaCargodt.setCargoTipoCodigo( request.getParameter("CargoTipoCodigo")); 
		ServentiaCargodt.setServentiaUsuario( request.getParameter("ServentiaUsuario")); 
		ServentiaCargodt.setNomeUsuario( request.getParameter("NomeUsuario")); 
		ServentiaCargodt.setGrupoUsuario( request.getParameter("GrupoUsuario")); 
		ServentiaCargodt.setGrupoUsuarioCodigo( request.getParameter("GrupoUsuarioCodigo")); 

		ServentiaCargodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ServentiaCargone.excluir(ServentiaCargodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ServentiaCargoLocalizar.jsp";
				tempList =ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentiaCargo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaCargone.getQuantidadePaginas());
					ServentiaCargodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ServentiaCargodt.limpar();
				request.removeAttribute("estruturaPadrao");
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ServentiaCargone.Verificar(ServentiaCargodt); 
					if (Mensagem.length()==0){
						ServentiaCargone.salvar(ServentiaCargodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
						if(ServentiaCargodt.getId_Serventia() != null && !ServentiaCargodt.getId_Serventia().equalsIgnoreCase("")){
							tempList = ServentiaCargone.consultarServentiaCargos(ServentiaCargodt.getId_Serventia());
							if (tempList.size() > 0) ServentiaCargodt.setListaCargosServentia(tempList);
						}
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
						tempList =ServentiaCargone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ServentiaCargone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ServentiaCargo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ServentiaCargodt.getId()))){
						ServentiaCargodt.limpar();
						ServentiaCargodt = ServentiaCargone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaCargodt",ServentiaCargodt );
		request.getSession().setAttribute("ServentiaCargone",ServentiaCargone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
