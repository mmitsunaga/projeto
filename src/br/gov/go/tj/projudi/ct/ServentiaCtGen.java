package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -9013680441854065366L;

    public  ServentiaCtGen() {

	} 
		public int Permissao(){
			return ServentiaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaDt Serventiadt;
		ServentiaNe Serventiane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Serventia.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Serventia");
		request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
		request.setAttribute("tempBuscaServentia","Serventia");
		request.setAttribute("tempBuscaId_ServentiaTipo","Id_ServentiaTipo");
		request.setAttribute("tempBuscaServentiaTipo","ServentiaTipo");
		request.setAttribute("tempBuscaId_ServentiaSubtipo","Id_ServentiaSubtipo");
		request.setAttribute("tempBuscaServentiaSubtipo","ServentiaSubtipo");
		request.setAttribute("tempBuscaId_Area","Id_Area");
		request.setAttribute("tempBuscaArea","Area");
		request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
		request.setAttribute("tempBuscaComarca","Comarca");
		request.setAttribute("tempBuscaId_AreaDistribuicao","Id_AreaDistribuicao");
		request.setAttribute("tempBuscaAreaDistribuicao","AreaDistribuicao");
		request.setAttribute("tempBuscaId_EstadoRepresentacao","Id_EstadoRepresentacao");
		request.setAttribute("tempBuscaEstadoRepresentacao","EstadoRepresentacao");
		request.setAttribute("tempBuscaId_AudienciaTipo","Id_AudienciaTipo");
		request.setAttribute("tempBuscaAudienciaTipo","AudienciaTipo");
		request.setAttribute("tempBuscaId_Endereco","Id_Endereco");
		request.setAttribute("tempBuscaEndereco","Endereco");
		request.setAttribute("tempBuscaId_Bairro","Id_Bairro");
		request.setAttribute("tempBuscaBairro","Bairro");

		request.setAttribute("tempRetorno","Serventia");



		Serventiane =(ServentiaNe)request.getSession().getAttribute("Serventiane");
		if (Serventiane == null )  Serventiane = new ServentiaNe();  


		Serventiadt =(ServentiaDt)request.getSession().getAttribute("Serventiadt");
		if (Serventiadt == null )  Serventiadt = new ServentiaDt();  

		Serventiadt.setServentia( request.getParameter("Serventia")); 
		Serventiadt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 
		Serventiadt.setServentiaCodigoExterno( request.getParameter("ServentiaCodigoExterno")); 
		Serventiadt.setId_ServentiaTipo( request.getParameter("Id_ServentiaTipo")); 
		Serventiadt.setServentiaTipo( request.getParameter("ServentiaTipo")); 
		Serventiadt.setId_ServentiaSubtipo( request.getParameter("Id_ServentiaSubtipo")); 
		Serventiadt.setServentiaSubtipo( request.getParameter("ServentiaSubtipo")); 
		Serventiadt.setId_Area( request.getParameter("Id_Area")); 
		Serventiadt.setArea( request.getParameter("Area")); 
		Serventiadt.setId_Comarca( request.getParameter("Id_Comarca")); 
		Serventiadt.setComarca( request.getParameter("Comarca")); 
		Serventiadt.setId_AreaDistribuicao( request.getParameter("Id_AreaDistribuicao")); 
		Serventiadt.setAreaDistribuicao( request.getParameter("AreaDistribuicao")); 
		Serventiadt.setId_EstadoRepresentacao( request.getParameter("Id_EstadoRepresentacao")); 
		Serventiadt.setEstadoRepresentacao( request.getParameter("EstadoRepresentacao")); 
		Serventiadt.setId_AudienciaTipo( request.getParameter("Id_AudienciaTipo")); 
		Serventiadt.setAudienciaTipo( request.getParameter("AudienciaTipo")); 
		Serventiadt.setQuantidadeDistribuicao( request.getParameter("QuantidadeDistribuicao")); 
				
		Serventiadt.setTelefone( request.getParameter("Telefone")); 
		if (request.getParameter("Online") != null)
			Serventiadt.setOnline( request.getParameter("Online")); 
		else Serventiadt.setOnline("false");

		Serventiadt.setServentiaTipoCodigo( request.getParameter("ServentiaTipoCodigo")); 
		Serventiadt.setServentiaSubtipoCodigo( request.getParameter("ServentiaSubtipoCodigo")); 
		Serventiadt.setAreaCodigo( request.getParameter("AreaCodigo")); 
		Serventiadt.setComarcaCodigo( request.getParameter("ComarcaCodigo")); 

		Serventiadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Serventiadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Serventiane.excluir(Serventiadt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
				tempList =Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentia", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					Serventiadt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				Serventiadt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Serventiane.Verificar(Serventiadt); 
				if (Mensagem.length()==0){
					Serventiane.salvar(Serventiadt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoServentiaSubtipo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaServentiaSubtipo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AreaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/AreaLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoArea(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaArea", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ComarcaLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoComarca(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaComarca", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/AreaDistribuicaoLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoAreaDistribuicao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaAreaDistribuicao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/EstadoRepresentacaoLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoEstadoRepresentacao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaEstadoRepresentacao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AudienciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/AudienciaTipoLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoAudienciaTipo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaAudienciaTipo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaEndereco", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (BairroDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/BairroLocalizar.jsp";
						tempList =Serventiane.consultarDescricaoBairro(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaBairro", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Serventiane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Serventia");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Serventiadt.getId()))){
						Serventiadt.limpar();
						Serventiadt = Serventiane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Serventiadt",Serventiadt );
		request.getSession().setAttribute("Serventiane",Serventiane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
