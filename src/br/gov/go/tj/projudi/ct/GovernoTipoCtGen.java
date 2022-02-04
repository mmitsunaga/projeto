package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.ne.GovernoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GovernoTipoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8885764341160107905L;

	public  GovernoTipoCtGen() {

	} 
		public int Permissao() {
			return GovernoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GovernoTipoDt GovernoTipodt;
		GovernoTipoNe GovernoTipone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/GovernoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GovernoTipo");




		GovernoTipone =(GovernoTipoNe)request.getSession().getAttribute("GovernoTipone");
		if (GovernoTipone == null )  GovernoTipone = new GovernoTipoNe();  


		GovernoTipodt =(GovernoTipoDt)request.getSession().getAttribute("GovernoTipodt");
		if (GovernoTipodt == null )  GovernoTipodt = new GovernoTipoDt();  

		GovernoTipodt.setGovernoTipoCodigo( request.getParameter("GovernoTipoCodigo")); 
		GovernoTipodt.setGovernoTipo( request.getParameter("GovernoTipo")); 

		GovernoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GovernoTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					GovernoTipone.excluir(GovernoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("GovernoTipo");
					descricao.add("GovernoTipo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_GovernoTipo");
					request.setAttribute("tempBuscaDescricao","GovernoTipo");
					request.setAttribute("tempBuscaPrograma","GovernoTipo");
					request.setAttribute("tempRetorno","GovernoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","GovernoTipo");
					stTemp = GovernoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				GovernoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=GovernoTipone.Verificar(GovernoTipodt); 
					if (Mensagem.length()==0){
						GovernoTipone.salvar(GovernoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_GovernoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GovernoTipodt.getId()))){
						GovernoTipodt.limpar();
						GovernoTipodt = GovernoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GovernoTipodt",GovernoTipodt );
		request.getSession().setAttribute("GovernoTipone",GovernoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
