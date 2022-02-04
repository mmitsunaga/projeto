package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.ne.AgenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AgenciaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8773538492071667522L;

    public  AgenciaCtGen() {

	} 
		public int Permissao(){
			return AgenciaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AgenciaDt Agenciadt;
		AgenciaNe Agenciane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Agencia.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Agencia");
		request.setAttribute("tempBuscaId_Agencia","Id_Agencia");
		request.setAttribute("tempBuscaAgencia","Agencia");
		request.setAttribute("tempBuscaId_Banco","Id_Banco");
		request.setAttribute("tempBuscaBanco","Banco");

		request.setAttribute("tempRetorno","Agencia");



		Agenciane =(AgenciaNe)request.getSession().getAttribute("Agenciane");
		if (Agenciane == null )  Agenciane = new AgenciaNe();  


		Agenciadt =(AgenciaDt)request.getSession().getAttribute("Agenciadt");
		if (Agenciadt == null )  Agenciadt = new AgenciaDt();  

		Agenciadt.setAgencia( request.getParameter("Agencia")); 
		Agenciadt.setAgenciaCodigo( request.getParameter("AgenciaCodigo")); 
		Agenciadt.setId_Banco( request.getParameter("Id_Banco")); 
		Agenciadt.setBanco( request.getParameter("Banco")); 
		Agenciadt.setBancoCodigo( request.getParameter("BancoCodigo")); 

		Agenciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Agenciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Agenciane.excluir(Agenciadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AgenciaLocalizar.jsp";
				tempList =Agenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAgencia", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Agenciane.getQuantidadePaginas());
					Agenciadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Agenciadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Agenciane.Verificar(Agenciadt); 
					if (Mensagem.length()==0){
						Agenciane.salvar(Agenciadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/BancoLocalizar.jsp";
						tempList =Agenciane.consultarDescricaoBanco(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaBanco", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Agenciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Agencia");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Agenciadt.getId()))){
						Agenciadt.limpar();
						Agenciadt = Agenciane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Agenciadt",Agenciadt );
		request.getSession().setAttribute("Agenciane",Agenciane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
