package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ZonaNe;
import br.gov.go.tj.utils.Configuracao;

public class ZonaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1549011887340550360L;

    public  ZonaCtGen() {

	} 
		public int Permissao() {
			return ZonaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ZonaDt Zonadt;
		ZonaNe Zonane;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Zona.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Zona");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		Zonane =(ZonaNe)request.getSession().getAttribute("Zonane");
		if (Zonane == null )  Zonane = new ZonaNe();  


		Zonadt =(ZonaDt)request.getSession().getAttribute("Zonadt");
		if (Zonadt == null )  Zonadt = new ZonaDt();  

		Zonadt.setZona( request.getParameter("Zona")); 
		Zonadt.setZonaCodigo( request.getParameter("ZonaCodigo")); 
		
		Zonadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Zonadt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Zonane.excluir(Zonadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo") == null) {
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Zona");
					nomeBusca.add("Area");
					nomeBusca.add("Cidade");
					descricao.add("Zona");
					descricao.add("Area");
					descricao.add("Cidade");
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Zona");
					request.setAttribute("tempBuscaDescricao", "Zona");
					request.setAttribute("tempBuscaPrograma", "Zona");
					request.setAttribute("tempRetorno", "Zona");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp = "";
					request.setAttribute("tempRetorno", "Zona");
					//stTemp = Zonane.consultarDescricaoJSON(stNomeBusca1,stNomeBusca2,PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;
				}
				break;
			case Configuracao.Novo: 
				Zonadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Zonane.Verificar(Zonadt); 
					if (Mensagem.length()==0){
						Zonane.salvar(Zonadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "Zona");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					//stTemp = Zonane.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					} catch(Exception e) {}
					return;
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Zona");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Zonadt.getId()))){
						Zonadt.limpar();
						Zonadt = Zonane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Zonadt",Zonadt );
		request.getSession().setAttribute("Zonane",Zonane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
