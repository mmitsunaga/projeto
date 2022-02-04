package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.HistoricoLotacaoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.HistoricoLotacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class HistoricoLotacaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -3668972296851511548L;

    public  HistoricoLotacaoCtGen() {

	} 
		public int Permissao(){
			return HistoricoLotacaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		HistoricoLotacaoDt HistoricoLotacaodt;
		HistoricoLotacaoNe HistoricoLotacaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/HistoricoLotacao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","HistoricoLotacao");
		request.setAttribute("tempBuscaId_HistoricoLotacao","Id_HistoricoLotacao");
		request.setAttribute("tempBuscaHistoricoLotacao","HistoricoLotacao");
		
		request.setAttribute("tempBuscaId_UsuarioServentia","Id_UsuarioServentia");
		request.setAttribute("tempBuscaUsuarioServentia","UsuarioServentia");

		request.setAttribute("tempRetorno","HistoricoLotacao");



		HistoricoLotacaone =(HistoricoLotacaoNe)request.getSession().getAttribute("HistoricoLotacaone");
		if (HistoricoLotacaone == null )  HistoricoLotacaone = new HistoricoLotacaoNe();  


		HistoricoLotacaodt =(HistoricoLotacaoDt)request.getSession().getAttribute("HistoricoLotacaodt");
		if (HistoricoLotacaodt == null )  HistoricoLotacaodt = new HistoricoLotacaoDt();  

		HistoricoLotacaodt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 
		HistoricoLotacaodt.setUsuarioServentia( request.getParameter("UsuarioServentia")); 
		HistoricoLotacaodt.setDataInicio( request.getParameter("DataInicio")); 
		HistoricoLotacaodt.setDataFim( request.getParameter("DataFim")); 

		HistoricoLotacaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		HistoricoLotacaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				HistoricoLotacaone.excluir(HistoricoLotacaodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/HistoricoLotacaoLocalizar.jsp";
				tempList =HistoricoLotacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaHistoricoLotacao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", HistoricoLotacaone.getQuantidadePaginas());
					HistoricoLotacaodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				HistoricoLotacaodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=HistoricoLotacaone.Verificar(HistoricoLotacaodt); 
				if (Mensagem.length()==0){
					HistoricoLotacaone.salvar(HistoricoLotacaodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/UsuarioServentiaLocalizar.jsp";
						tempList =HistoricoLotacaone.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuarioServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", HistoricoLotacaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_HistoricoLotacao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( HistoricoLotacaodt.getId()))){
						HistoricoLotacaodt.limpar();
						HistoricoLotacaodt = HistoricoLotacaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("HistoricoLotacaodt",HistoricoLotacaodt );
		request.getSession().setAttribute("HistoricoLotacaone",HistoricoLotacaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
