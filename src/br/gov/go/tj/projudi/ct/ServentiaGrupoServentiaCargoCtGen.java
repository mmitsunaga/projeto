package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt;
import br.gov.go.tj.projudi.ne.ServentiaGrupoServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaGrupoServentiaCargoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4112994651452723164L;

	public  ServentiaGrupoServentiaCargoCtGen() {

	} 
		public int Permissao() {
			return ServentiaGrupoServentiaCargoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaGrupoServentiaCargoDt ServentiaGrupoServentiaCargodt;
		ServentiaGrupoServentiaCargoNe ServentiaGrupoServentiaCargone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ServentiaGrupoServentiaCargo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		
		request.setAttribute("tempPrograma","ServentiaGrupoServentiaCargo");
		request.setAttribute("ListaUlLiServentiaGrupoServentiaCargo","");
		
		request.setAttribute("tempBuscaID_SERV_GRUPO","ID_SERV_GRUPO");
		request.setAttribute("tempBuscaSERV_GRUPO","SERV_GRUPO");


		request.setAttribute("tempRetorno","ServentiaGrupoServentiaCargo");




		ServentiaGrupoServentiaCargone =(ServentiaGrupoServentiaCargoNe)request.getSession().getAttribute("ServentiaGrupoServentiaCargone");
		if (ServentiaGrupoServentiaCargone == null )  ServentiaGrupoServentiaCargone = new ServentiaGrupoServentiaCargoNe();  


		ServentiaGrupoServentiaCargodt =(ServentiaGrupoServentiaCargoDt)request.getSession().getAttribute("ServentiaGrupoServentiaCargodt");
		if (ServentiaGrupoServentiaCargodt == null )  ServentiaGrupoServentiaCargodt = new ServentiaGrupoServentiaCargoDt();  

		ServentiaGrupoServentiaCargodt.setServentiaCargoServentiaGrupo( request.getParameter("ServentiaCargoServentiaGrupo")); 
		ServentiaGrupoServentiaCargodt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		ServentiaGrupoServentiaCargodt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ServentiaGrupoServentiaCargodt.setId_ServentiaGrupo( request.getParameter("Id_ServentiaGrupo")); 
		ServentiaGrupoServentiaCargodt.setServentiaGrupo( request.getParameter("ServentiaGrupo")); 

		ServentiaGrupoServentiaCargodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaGrupoServentiaCargodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,ServentiaGrupoServentiaCargodt.getId_ServentiaGrupo() ,ServentiaGrupoServentiaCargone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ServentiaGrupoServentiaCargodt.limpar();
				request.setAttribute("ListaUlLiServentiaGrupoServentiaCargo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ServentiaGrupoServentiaCargone.Verificar(ServentiaGrupoServentiaCargodt); 
				if (Mensagem.length()==0){
					ServentiaGrupoServentiaCargone.salvarMultiplo(ServentiaGrupoServentiaCargodt, idsDados); 
					localizar(request,ServentiaGrupoServentiaCargodt.getId_ServentiaGrupo() ,ServentiaGrupoServentiaCargone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ServentiaGrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Serventia Grupo");
					descricao.add("Serventia Grupo (Unidade de Trabalho)");
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaGrupo");
					request.setAttribute("tempBuscaDescricao","ServentiaGrupo");
					request.setAttribute("tempBuscaPrograma","Serventia Grupo (Unidade de Trabalho)");
					request.setAttribute("tempRetorno","ServentiaGrupoServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", String.valueOf(ServentiaGrupoDt.CodigoPermissao   * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
					break;
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupoServentiaCargone.consultarDescricaoServentiaGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;
				}
//--------------------------------------------------------------------------------//
			default:
				break;
		}
		
		request.getSession().setAttribute("ServentiaGrupoServentiaCargodt",ServentiaGrupoServentiaCargodt );
		request.getSession().setAttribute("ServentiaGrupoServentiaCargone",ServentiaGrupoServentiaCargone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ServentiaGrupoServentiaCargoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarServentiaCargoServentiaGrupoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiServentiaGrupoServentiaCargo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
