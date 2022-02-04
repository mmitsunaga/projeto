package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 5955904170022202014L;

    public  AudienciaCtGen() {

	} 
		public int Permissao(){
			return AudienciaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaDt Audienciadt;
		AudienciaNe Audienciane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Audiencia.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Audiencia");




		Audienciane =(AudienciaNe)request.getSession().getAttribute("Audienciane");
		if (Audienciane == null )  Audienciane = new AudienciaNe();  


		Audienciadt =(AudienciaDt)request.getSession().getAttribute("Audienciadt");
		if (Audienciadt == null )  Audienciadt = new AudienciaDt();  

		Audienciadt.setId_AudienciaTipo( request.getParameter("Id_AudienciaTipo")); 
		Audienciadt.setAudienciaTipo( request.getParameter("AudienciaTipo")); 
		Audienciadt.setId_Serventia( request.getParameter("Id_Serventia")); 
		Audienciadt.setServentia( request.getParameter("Serventia")); 
		Audienciadt.setDataAgendada( request.getParameter("DataAgendada")); 
		Audienciadt.setDataMovimentacao( request.getParameter("DataMovimentacao")); 
		if (request.getParameter("Reservada") != null)
			Audienciadt.setReservada( request.getParameter("Reservada")); 
		else Audienciadt.setReservada("false");
		Audienciadt.setAudienciaTipoCodigo( request.getParameter("AudienciaTipoCodigo")); 

		Audienciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Audienciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Audienciane.excluir(Audienciadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AudienciaLocalizar.jsp";
				request.setAttribute("tempBuscaId_Audiencia","Id_Audiencia");
				request.setAttribute("tempBuscaAudiencia","Audiencia");
				request.setAttribute("tempRetorno","Audiencia");
				tempList =Audienciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAudiencia", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Audienciane.getQuantidadePaginas());
					Audienciadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Audienciadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Audienciane.Verificar(Audienciadt); 
					if (Mensagem.length()==0){
						Audienciane.salvar(Audienciadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (AudienciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_AudienciaTipo","Id_AudienciaTipo");
					request.setAttribute("tempBuscaAudienciaTipo","AudienciaTipo");
					request.setAttribute("tempRetorno","Audiencia");
					stAcao="/WEB-INF/jsptjgo/AudienciaTipoLocalizar.jsp";
					tempList =Audienciane.consultarDescricaoAudienciaTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaAudienciaTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Audienciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","Audiencia");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =Audienciane.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Audienciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Audiencia");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Audienciadt.getId()))){
						Audienciadt.limpar();
						Audienciadt = Audienciane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Audienciadt",Audienciadt );
		request.getSession().setAttribute("Audienciane",Audienciane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	

}
