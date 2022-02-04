package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.CargoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CargoTipoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4215696658491412038L;

	public  CargoTipoCtGen() {

	} 
		public int Permissao() {
			return CargoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CargoTipoDt CargoTipodt;
		CargoTipoNe CargoTipone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CargoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CargoTipo");




		CargoTipone =(CargoTipoNe)request.getSession().getAttribute("CargoTipone");
		if (CargoTipone == null )  CargoTipone = new CargoTipoNe();  


		CargoTipodt =(CargoTipoDt)request.getSession().getAttribute("CargoTipodt");
		if (CargoTipodt == null )  CargoTipodt = new CargoTipoDt();  

		CargoTipodt.setCargoTipo( request.getParameter("CargoTipo")); 
		CargoTipodt.setCargoTipoCodigo( request.getParameter("CargoTipoCodigo")); 
		CargoTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		CargoTipodt.setGrupo( request.getParameter("Grupo")); 
		CargoTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 

		CargoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CargoTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CargoTipone.excluir(CargoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("CargoTipo");
					descricao.add("CargoTipo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CargoTipo");
					request.setAttribute("tempBuscaDescricao","CargoTipo");
					request.setAttribute("tempBuscaPrograma","CargoTipo");
					request.setAttribute("tempRetorno","CargoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","CargoTipo");
					stTemp = CargoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				CargoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CargoTipone.Verificar(CargoTipodt); 
					if (Mensagem.length()==0){
						CargoTipone.salvar(CargoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Grupo");
					descricao.add("Grupo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Grupo");
					request.setAttribute("tempBuscaDescricao","Grupo");
					request.setAttribute("tempBuscaPrograma","Grupo");
					request.setAttribute("tempRetorno","CargoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","CargoTipo");
					stTemp = CargoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CargoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CargoTipodt.getId()))){
						CargoTipodt.limpar();
						CargoTipodt = CargoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CargoTipodt",CargoTipodt );
		request.getSession().setAttribute("CargoTipone",CargoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
