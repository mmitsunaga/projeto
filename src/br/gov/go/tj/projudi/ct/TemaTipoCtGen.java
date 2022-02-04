package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.projudi.ne.TemaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class TemaTipoCtGen extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1008802136483209211L;

	public  TemaTipoCtGen() {} 
	
	public int Permissao() {
		return TemaTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaTipoDt TemaTipodt;
		TemaTipoNe TemaTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/TemaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","TemaTipo");

		TemaTipone =(TemaTipoNe)request.getSession().getAttribute("TemaTipone");
		if (TemaTipone == null )  TemaTipone = new TemaTipoNe();  

		TemaTipodt =(TemaTipoDt)request.getSession().getAttribute("TemaTipodt");
		if (TemaTipodt == null )  TemaTipodt = new TemaTipoDt();  

		TemaTipodt.setTemaTipoCodigo( request.getParameter("TemaTipoCodigo")); 
		TemaTipodt.setTemaTipo( request.getParameter("TemaTipo")); 

		TemaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		TemaTipodt.setIpComputadorLog(request.getRemoteAddr());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					TemaTipone.excluir(TemaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Tipo"};
					String[] lisDescricao = {"Tema Tipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaTipo");
					request.setAttribute("tempBuscaDescricao","TemaTipo");
					request.setAttribute("tempBuscaPrograma","TemaTipo");
					request.setAttribute("tempRetorno","TemaTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = TemaTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				TemaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=TemaTipone.Verificar(TemaTipodt); 
					if (Mensagem.length()==0){
						TemaTipone.salvar(TemaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_TemaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( TemaTipodt.getId()))){
						TemaTipodt.limpar();
						TemaTipodt = TemaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("TemaTipodt",TemaTipodt );
		request.getSession().setAttribute("TemaTipone",TemaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
