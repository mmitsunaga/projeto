package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.EscalaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EscalaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 1039366467262757777L;

    public  EscalaCtGen() {

	} 
		public int Permissao() {
			return EscalaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EscalaDt Escaladt;
		EscalaNe Escalane;
		
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Escala.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Escala");
		request.setAttribute("tempBuscaId_Escala","Id_Escala");
		request.setAttribute("tempBuscaEscala","Escala");
		request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
		request.setAttribute("tempBuscaServentia","Serventia");
		request.setAttribute("tempBuscaId_EscalaTipo","Id_EscalaTipo");
		request.setAttribute("tempBuscaEscalaTipo","EscalaTipo");
		request.setAttribute("tempBuscaId_MandadoTipo","Id_MandadoTipo");
		request.setAttribute("tempBuscaMandadoTipo","MandadoTipo");
		request.setAttribute("tempBuscaId_Zona","Id_Zona");
		request.setAttribute("tempBuscaZona","Zona");
		request.setAttribute("tempBuscaId_Regiao","Id_Regiao");
		request.setAttribute("tempBuscaRegiao","Regiao");
		request.setAttribute("tempBuscaId_Bairro","Id_Bairro");
		request.setAttribute("tempBuscaBairro","Bairro");

		request.setAttribute("tempRetorno","Escala");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		Escalane =(EscalaNe)request.getSession().getAttribute("Escalane");
		if (Escalane == null )  Escalane = new EscalaNe();  


		Escaladt =(EscalaDt)request.getSession().getAttribute("Escaladt");
		if (Escaladt == null )  Escaladt = new EscalaDt();  

		Escaladt.setEscala( request.getParameter("Escala")); 
		Escaladt.setId_Serventia( request.getParameter("Id_Serventia")); 
		Escaladt.setServentia( request.getParameter("Serventia")); 
		Escaladt.setId_EscalaTipo( request.getParameter("Id_EscalaTipo")); 
		Escaladt.setEscalaTipo( request.getParameter("EscalaTipo")); 
		Escaladt.setId_MandadoTipo( request.getParameter("Id_MandadoTipo")); 
		Escaladt.setMandadoTipo( request.getParameter("MandadoTipo")); 
		Escaladt.setId_Zona( request.getParameter("Id_Zona")); 
		Escaladt.setZona( request.getParameter("Zona")); 
		Escaladt.setId_Regiao( request.getParameter("Id_Regiao")); 
		Escaladt.setRegiao( request.getParameter("Regiao")); 
		Escaladt.setId_Bairro( request.getParameter("Id_Bairro")); 
		Escaladt.setBairro( request.getParameter("Bairro")); 
		Escaladt.setQuantidadeMandado( request.getParameter("QuantidadeMandado")); 
		Escaladt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 
		Escaladt.setEscalaTipoCodigo( request.getParameter("EscalaTipoCodigo")); 
		Escaladt.setMandadoTipoCodigo( request.getParameter("MandadoTipoCodigo")); 
		Escaladt.setZonaCodigo( request.getParameter("ZonaCodigo")); 
		Escaladt.setRegiaoCodigo( request.getParameter("RegiaoCodigo")); 
		Escaladt.setBairroCodigo( request.getParameter("BairroCodigo")); 

		Escaladt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Escaladt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Escalane.excluir(Escaladt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				tempList =Escalane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaEscala", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Escalane.getQuantidadePaginas());
					Escaladt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				Escaladt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Escalane.Verificar(Escaladt); 
				if (Mensagem.length()==0){
					Escalane.salvar(Escaladt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
						tempList =Escalane.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Escalane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EscalaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/EscalaTipoLocalizar.jsp";
						//HELLENO
						//Mudei a linah abaixo de consultarDescricaoEscalaTipo para consultarDescricao. Testar.
						tempList =Escalane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaEscalaTipo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Escalane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ZonaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						nomeBusca = new ArrayList();
						descricao = new ArrayList();
						nomeBusca.add("Zona");
						nomeBusca.add("Cidade");
						descricao.add("Zona");
						descricao.add("Cidade");
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Zona");
						request.setAttribute("tempBuscaDescricao","Zona");
						request.setAttribute("tempBuscaPrograma","Zona");			
						request.setAttribute("tempRetorno","Escala");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", String.valueOf(ZonaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("nomeBusca", nomeBusca);
						request.setAttribute("descricao", descricao);
					} else {
						String stTemp="";
						request.setAttribute("tempRetorno","Escala");	
						stTemp = Escalane.consultarDescricaoZonaJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						try{
							enviarJSON(response, stTemp);
							
						} catch(Exception e) {}
						return;								
					}
					break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Escala");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Escaladt.getId()))){
						Escaladt.limpar();
						Escaladt = Escalane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Escaladt",Escaladt );
		request.getSession().setAttribute("Escalane",Escalane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
