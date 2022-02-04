package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.ne.MandadoPrisaoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoPrisaoStatusCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -617906991279966007L;

	public  MandadoPrisaoStatusCtGen() {

	} 
		public int Permissao() {
			return MandadoPrisaoStatusDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoPrisaoStatusDt MandadoPrisaoStatusdt;
		MandadoPrisaoStatusNe MandadoPrisaoStatusne;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoPrisaoStatus.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoPrisaoStatus");




		MandadoPrisaoStatusne =(MandadoPrisaoStatusNe)request.getSession().getAttribute("MandadoPrisaoStatusne");
		if (MandadoPrisaoStatusne == null )  MandadoPrisaoStatusne = new MandadoPrisaoStatusNe();  


		MandadoPrisaoStatusdt =(MandadoPrisaoStatusDt)request.getSession().getAttribute("MandadoPrisaoStatusdt");
		if (MandadoPrisaoStatusdt == null )  MandadoPrisaoStatusdt = new MandadoPrisaoStatusDt();  

		MandadoPrisaoStatusdt.setMandadoPrisaoStatusCodigo( request.getParameter("MandadoPrisaoStatusCodigo")); 
		MandadoPrisaoStatusdt.setMandadoPrisaoStatus( request.getParameter("MandadoPrisaoStatus")); 

		MandadoPrisaoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoPrisaoStatusdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoPrisaoStatusne.excluir(MandadoPrisaoStatusdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("MandadoPrisaoStatus");
					descricao.add("MandadoPrisaoStatus");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoPrisaoStatus");
					request.setAttribute("tempBuscaDescricao","MandadoPrisaoStatus");
					request.setAttribute("tempBuscaPrograma","MandadoPrisaoStatus");
					request.setAttribute("tempRetorno","MandadoPrisaoStatus");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisaoStatus");
					stTemp = MandadoPrisaoStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				MandadoPrisaoStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoPrisaoStatusne.Verificar(MandadoPrisaoStatusdt); 
					if (Mensagem.length()==0){
						MandadoPrisaoStatusne.salvar(MandadoPrisaoStatusdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoPrisaoStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoPrisaoStatusdt.getId()))){
						MandadoPrisaoStatusdt.limpar();
						MandadoPrisaoStatusdt = MandadoPrisaoStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoPrisaoStatusdt",MandadoPrisaoStatusdt );
		request.getSession().setAttribute("MandadoPrisaoStatusne",MandadoPrisaoStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
