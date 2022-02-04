package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import br.gov.go.tj.projudi.ne.ParametroCrimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ParametroCrimeExecucaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6878336454946688785L;

	public  ParametroCrimeExecucaoCtGen() {

	} 
		public int Permissao() {
			return ParametroCrimeExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ParametroCrimeExecucaoDt ParametroCrimeExecucaodt;
		ParametroCrimeExecucaoNe ParametroCrimeExecucaone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ParametroCrimeExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Parâmetro dos Crimes de Execução Penal");

		ParametroCrimeExecucaone =(ParametroCrimeExecucaoNe)request.getSession().getAttribute("ParametroCrimeExecucaone");
		if (ParametroCrimeExecucaone == null )  ParametroCrimeExecucaone = new ParametroCrimeExecucaoNe();  


		ParametroCrimeExecucaodt =(ParametroCrimeExecucaoDt)request.getSession().getAttribute("ParametroCrimeExecucaodt");
		if (ParametroCrimeExecucaodt == null )  ParametroCrimeExecucaodt = new ParametroCrimeExecucaoDt();  

		ParametroCrimeExecucaodt.setData( request.getParameter("Data")); 
		ParametroCrimeExecucaodt.setId_CrimeExecucao( request.getParameter("Id_CrimeExecucao")); 
		ParametroCrimeExecucaodt.setCrimeExecucao( request.getParameter("CrimeExecucao")); 
		ParametroCrimeExecucaodt.setArtigo( request.getParameter("Artigo")); 
		ParametroCrimeExecucaodt.setParagrafo( request.getParameter("Paragrafo")); 
		ParametroCrimeExecucaodt.setLei( request.getParameter("Lei")); 
		ParametroCrimeExecucaodt.setInciso( request.getParameter("Inciso")); 
		if (request.getParameterValues("chkParametro") != null){
			ParametroCrimeExecucaodt.setHediondoProgressao("false");
			ParametroCrimeExecucaodt.setHediondoLivramCond("false");
			ParametroCrimeExecucaodt.setEquiparaHediondoLivramCond("false");
			
			String[] chkParametros = request.getParameterValues("chkParametro");
			for (int i=0; i<chkParametros.length; i++){
				if (chkParametros[i].equals("HediondoProgressao")){
					ParametroCrimeExecucaodt.setHediondoProgressao("true");
				} else if (chkParametros[i].equals("HediondoLivramCond")){
					ParametroCrimeExecucaodt.setHediondoLivramCond("true");
				} else if (chkParametros[i].equals("EquiparaHediondoLivramCond")){
					ParametroCrimeExecucaodt.setEquiparaHediondoLivramCond("true");
				}
			}
		}
		
		ParametroCrimeExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ParametroCrimeExecucaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1"); //crime
		if(request.getParameter("nomeBusca2") != null)
			request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null)
			request.getParameter("nomeBusca3");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ParametroCrimeExecucaone.excluir(ParametroCrimeExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("ParametroCrimeExecucao");
					descricao.add("ParametroCrimeExecucao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ParametroCrimeExecucao");
					request.setAttribute("tempBuscaDescricao","ParametroCrimeExecucao");
					request.setAttribute("tempBuscaPrograma","ParametroCrimeExecucao");
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					stTemp = ParametroCrimeExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
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
				ParametroCrimeExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ParametroCrimeExecucaone.Verificar(ParametroCrimeExecucaodt); 
					if (Mensagem.length()==0){
						ParametroCrimeExecucaone.salvar(ParametroCrimeExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (CrimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("CrimeExecucao");
					descricao.add("CrimeExecucao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CrimeExecucao");
					request.setAttribute("tempBuscaDescricao","CrimeExecucao");
					request.setAttribute("tempBuscaPrograma","CrimeExecucao");
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(CrimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					stTemp = ParametroCrimeExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
						//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo.");
					}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ParametroCrimeExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ParametroCrimeExecucaodt.getId()))){
						ParametroCrimeExecucaodt.limpar();
						ParametroCrimeExecucaodt = ParametroCrimeExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ParametroCrimeExecucaodt",ParametroCrimeExecucaodt );
		request.getSession().setAttribute("ParametroCrimeExecucaone",ParametroCrimeExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
