package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.ne.MandadoJudicialStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MandadoJudicialStatusCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7700637071770483424L;

    public  MandadoJudicialStatusCtGen() {

	} 
		public int Permissao(){
			return MandadoJudicialStatusDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoJudicialStatusDt MandadoJudicialStatusdt;
		MandadoJudicialStatusNe MandadoJudicialStatusne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoJudicialStatus.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoJudicialStatus");




		MandadoJudicialStatusne =(MandadoJudicialStatusNe)request.getSession().getAttribute("MandadoJudicialStatusne");
		if (MandadoJudicialStatusne == null )  MandadoJudicialStatusne = new MandadoJudicialStatusNe();  


		MandadoJudicialStatusdt =(MandadoJudicialStatusDt)request.getSession().getAttribute("MandadoJudicialStatusdt");
		if (MandadoJudicialStatusdt == null )  MandadoJudicialStatusdt = new MandadoJudicialStatusDt();  

		MandadoJudicialStatusdt.setMandadoJudicialStatus( request.getParameter("MandadoJudicialStatus")); 
		MandadoJudicialStatusdt.setMandadoJudicialStatusCodigo( request.getParameter("MandadoJudicialStatusCodigo")); 

		MandadoJudicialStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoJudicialStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoJudicialStatusne.excluir(MandadoJudicialStatusdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/MandadoJudicialStatusLocalizar.jsp";
				request.setAttribute("tempBuscaId_MandadoJudicialStatus","Id_MandadoJudicialStatus");
				request.setAttribute("tempBuscaMandadoJudicialStatus","MandadoJudicialStatus");
				request.setAttribute("tempRetorno","MandadoJudicialStatus");
				tempList =MandadoJudicialStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaMandadoJudicialStatus", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", MandadoJudicialStatusne.getQuantidadePaginas());
					MandadoJudicialStatusdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				MandadoJudicialStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoJudicialStatusne.Verificar(MandadoJudicialStatusdt); 
					if (Mensagem.length()==0){
						MandadoJudicialStatusne.salvar(MandadoJudicialStatusdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoJudicialStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoJudicialStatusdt.getId()))){
						MandadoJudicialStatusdt.limpar();
						MandadoJudicialStatusdt = MandadoJudicialStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoJudicialStatusdt",MandadoJudicialStatusdt );
		request.getSession().setAttribute("MandadoJudicialStatusne",MandadoJudicialStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
