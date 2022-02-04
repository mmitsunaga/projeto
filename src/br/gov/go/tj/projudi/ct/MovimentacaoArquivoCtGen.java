package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MovimentacaoArquivoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -5107152986740026215L;

    public  MovimentacaoArquivoCtGen() {

	} 
		public int Permissao(){
			return MovimentacaoArquivoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoArquivoDt MovimentacaoArquivodt;
		MovimentacaoArquivoNe MovimentacaoArquivone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MovimentacaoArquivo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MovimentacaoArquivo");
		request.setAttribute("tempBuscaId_MovimentacaoArquivo","Id_MovimentacaoArquivo");
		request.setAttribute("tempBuscaMovimentacaoArquivo","MovimentacaoArquivo");
		
		request.setAttribute("tempBuscaId_ArquivoTipo","Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo","ArquivoTipo");
		request.setAttribute("tempBuscaId_Movimentacao","Id_Movimentacao");
		request.setAttribute("tempBuscaMovimentacao","Movimentacao");

		request.setAttribute("tempRetorno","MovimentacaoArquivo");



		MovimentacaoArquivone =(MovimentacaoArquivoNe)request.getSession().getAttribute("MovimentacaoArquivone");
		if (MovimentacaoArquivone == null )  MovimentacaoArquivone = new MovimentacaoArquivoNe();  


		MovimentacaoArquivodt =(MovimentacaoArquivoDt)request.getSession().getAttribute("MovimentacaoArquivodt");
		if (MovimentacaoArquivodt == null )  MovimentacaoArquivodt = new MovimentacaoArquivoDt();  

		MovimentacaoArquivodt.setNomeArquivo( request.getParameter("NomeArquivo")); 
		MovimentacaoArquivodt.setId_Movimentacao( request.getParameter("Id_Movimentacao")); 
		MovimentacaoArquivodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 

		MovimentacaoArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				MovimentacaoArquivone.excluir(MovimentacaoArquivodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/MovimentacaoArquivoLocalizar.jsp";
				tempList =MovimentacaoArquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaMovimentacaoArquivo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", MovimentacaoArquivone.getQuantidadePaginas());
					MovimentacaoArquivodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				MovimentacaoArquivodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=MovimentacaoArquivone.Verificar(MovimentacaoArquivodt); 
				if (Mensagem.length()==0){
					MovimentacaoArquivone.salvar(MovimentacaoArquivodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (MovimentacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/MovimentacaoLocalizar.jsp";
						tempList =MovimentacaoArquivone.consultarDescricaoMovimentacao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList!=null && tempList.size()>0){
							request.setAttribute("ListaMovimentacao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", MovimentacaoArquivone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_MovimentacaoArquivo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MovimentacaoArquivodt.getId()))){
						MovimentacaoArquivodt.limpar();
						MovimentacaoArquivodt = MovimentacaoArquivone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MovimentacaoArquivodt",MovimentacaoArquivodt );
		request.getSession().setAttribute("MovimentacaoArquivone",MovimentacaoArquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
