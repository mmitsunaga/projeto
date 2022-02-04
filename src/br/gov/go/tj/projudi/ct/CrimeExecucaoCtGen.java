package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.ne.CrimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CrimeExecucaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1169265740275072499L;

	public  CrimeExecucaoCtGen() {

	} 
		public int Permissao() {
			return CrimeExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CrimeExecucaoDt CrimeExecucaodt;
		CrimeExecucaoNe CrimeExecucaone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CrimeExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CrimeExecucao");




		CrimeExecucaone =(CrimeExecucaoNe)request.getSession().getAttribute("CrimeExecucaone");
		if (CrimeExecucaone == null )  CrimeExecucaone = new CrimeExecucaoNe();  


		CrimeExecucaodt =(CrimeExecucaoDt)request.getSession().getAttribute("CrimeExecucaodt");
		if (CrimeExecucaodt == null )  CrimeExecucaodt = new CrimeExecucaoDt();  

		CrimeExecucaodt.setCrimeExecucaoCodigo( request.getParameter("CrimeExecucaoCodigo")); 
		CrimeExecucaodt.setCrimeExecucao( request.getParameter("CrimeExecucao")); 
		CrimeExecucaodt.setLei( request.getParameter("Lei")); 
		CrimeExecucaodt.setArtigo( request.getParameter("Artigo")); 
		CrimeExecucaodt.setParagrafo( request.getParameter("Paragrafo")); 
		CrimeExecucaodt.setInciso( request.getParameter("Inciso")); 

		CrimeExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CrimeExecucaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CrimeExecucaone.excluir(CrimeExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("CrimeExecucao");
					descricao.add("CrimeExecucao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CrimeExecucao");
					request.setAttribute("tempBuscaDescricao","CrimeExecucao");
					request.setAttribute("tempBuscaPrograma","CrimeExecucao");
					request.setAttribute("tempRetorno","CrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","CrimeExecucao");
					stTemp = CrimeExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
						//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo.");
					}
					return;
				}
				break;
			case Configuracao.Novo: 
				CrimeExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CrimeExecucaone.Verificar(CrimeExecucaodt); 
					if (Mensagem.length()==0){
						CrimeExecucaone.salvar(CrimeExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CrimeExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CrimeExecucaodt.getId()))){
						CrimeExecucaodt.limpar();
						CrimeExecucaodt = CrimeExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CrimeExecucaodt",CrimeExecucaodt );
		request.getSession().setAttribute("CrimeExecucaone",CrimeExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
