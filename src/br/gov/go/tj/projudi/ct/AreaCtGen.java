package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.ne.AreaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AreaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1765799643038682234L;

    public  AreaCtGen() {

	} 
		public int Permissao() {
			return AreaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AreaDt Areadt;
		AreaNe Areane;


		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Area.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Area");




		Areane =(AreaNe)request.getSession().getAttribute("Areane");
		if (Areane == null )  Areane = new AreaNe();  


		Areadt =(AreaDt)request.getSession().getAttribute("Areadt");
		if (Areadt == null )  Areadt = new AreaDt();  

		Areadt.setArea( request.getParameter("Area")); 
		Areadt.setAreaCodigo( request.getParameter("AreaCodigo")); 

		Areadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Areadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Areane.excluir(Areadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Area");
				request.setAttribute("tempBuscaDescricao","Area");
				request.setAttribute("tempBuscaPrograma","Area");			
											
				request.setAttribute("tempRetorno","Area");		
				
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempDescricaoDescricao","Area");
				
//				tempList =Areane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//				if (tempList.size()>0){
//					request.setAttribute("ListaArea", tempList); 
//					request.setAttribute("PaginaAtual", Configuracao.Localizar);
//					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//					request.setAttribute("QuantidadePaginas", Areane.getQuantidadePaginas());
//					Areadt.limpar();
//				}else{
//					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//				}
				request.setAttribute("PaginaAtual", "0");
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				break;
			case Configuracao.LocalizarDWR:
				String stTemp="";
				
				request.setAttribute("tempRetorno","Area");	
				stTemp =Areane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
				
				try{
					enviarJSON(response, stTemp);
					
				}
				catch(Exception e) {
					//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo.");
				}
				return;								
												
			case Configuracao.Novo: 
				Areadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Areane.Verificar(Areadt); 
					if (Mensagem.length()==0){
						Areane.salvar(Areadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Area");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Areadt.getId()))){
						Areadt.limpar();
						Areadt = Areane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Areadt",Areadt );
		request.getSession().setAttribute("Areane",Areane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
