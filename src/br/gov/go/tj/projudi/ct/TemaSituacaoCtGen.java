package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.ne.TemaSituacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class TemaSituacaoCtGen extends Controle {

	private static final long serialVersionUID = 1643404221714523755L;

	public  TemaSituacaoCtGen() {} 
	
	public int Permissao() {
		return TemaSituacaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaSituacaoDt Temasituacaodt;
		TemaSituacaoNe Temasituacaone;

		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/TemaSituacao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","TemaSituacao");

		Temasituacaone =(TemaSituacaoNe)request.getSession().getAttribute("Temasituacaone");
		if (Temasituacaone == null )  Temasituacaone = new TemaSituacaoNe();  

		Temasituacaodt =(TemaSituacaoDt)request.getSession().getAttribute("Temasituacaodt");
		if (Temasituacaodt == null )  Temasituacaodt = new TemaSituacaoDt();  

		Temasituacaodt.setTemaSituacaoCodigo( request.getParameter("TemaSituacaoCodigo")); 
		Temasituacaodt.setTemaSituacao( request.getParameter("TemaSituacao")); 

		Temasituacaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Temasituacaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Temasituacaone.excluir(Temasituacaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Situação"};
					String[] lisDescricao = {"Tema Situação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaSituacao");
					request.setAttribute("tempBuscaDescricao","TemaSituacao");
					request.setAttribute("tempBuscaPrograma","TemaSituacao");
					request.setAttribute("tempRetorno","TemaSituacao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temasituacaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				Temasituacaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Temasituacaone.Verificar(Temasituacaodt); 
					if (Mensagem.length()==0){
						Temasituacaone.salvar(Temasituacaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_TemaSituacao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Temasituacaodt.getId()))){
						Temasituacaodt.limpar();
						Temasituacaodt = Temasituacaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Temasituacaodt",Temasituacaodt );
		request.getSession().setAttribute("Temasituacaone",Temasituacaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
