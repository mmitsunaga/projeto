package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.PonteiroLogTipoNe;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroLogTipoCtGen extends Controle { 


	public  PonteiroLogTipoCtGen() { 

	} 
		public int Permissao(){
			return PonteiroLogTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroLogTipoDt PonteiroLogTipodt;
		PonteiroLogTipoNe PonteiroLogTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PonteiroLogTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PonteiroLogTipo");




		PonteiroLogTipone =(PonteiroLogTipoNe)request.getSession().getAttribute("PonteiroLogTipone");
		if (PonteiroLogTipone == null )  PonteiroLogTipone = new PonteiroLogTipoNe();  


		PonteiroLogTipodt =(PonteiroLogTipoDt)request.getSession().getAttribute("PonteiroLogTipodt");
		if (PonteiroLogTipodt == null )  PonteiroLogTipodt = new PonteiroLogTipoDt();  

		PonteiroLogTipodt.setPonteiroLogTipo( request.getParameter("PonteiroLogTipo")); 

		PonteiroLogTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PonteiroLogTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PonteiroLogTipone.excluir(PonteiroLogTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroLogTipo"};
					String[] lisDescricao = {"PonteiroLogTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroLogTipo");
					request.setAttribute("tempBuscaDescricao","PonteiroLogTipo");
					request.setAttribute("tempBuscaPrograma","PonteiroLogTipo");
					request.setAttribute("tempRetorno","PonteiroLogTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PonteiroLogTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PonteiroLogTipone.Verificar(PonteiroLogTipodt); 
					if (Mensagem.length()==0){
						PonteiroLogTipone.salvar(PonteiroLogTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PonteiroLogTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PonteiroLogTipodt.getId()))){
						PonteiroLogTipodt.limpar();
						PonteiroLogTipodt = PonteiroLogTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PonteiroLogTipodt",PonteiroLogTipodt );
		request.getSession().setAttribute("PonteiroLogTipone",PonteiroLogTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
