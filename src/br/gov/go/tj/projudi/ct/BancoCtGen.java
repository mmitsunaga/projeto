package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.ne.BancoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class BancoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8401948292181469416L;

    public  BancoCtGen() {

	} 
		public int Permissao(){
			return BancoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		BancoDt Bancodt;
		BancoNe Bancone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Banco.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Banco");
		request.setAttribute("tempBuscaId_Banco","Id_Banco");
		request.setAttribute("tempBuscaBanco","Banco");

		request.setAttribute("tempRetorno","Banco");



		Bancone =(BancoNe)request.getSession().getAttribute("Bancone");
		if (Bancone == null )  Bancone = new BancoNe();  


		Bancodt =(BancoDt)request.getSession().getAttribute("Bancodt");
		if (Bancodt == null )  Bancodt = new BancoDt();  

		Bancodt.setBancoCodigo( request.getParameter("BancoCodigo")); 
		Bancodt.setBanco( request.getParameter("Banco")); 

		Bancodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Bancodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Bancone.excluir(Bancodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/BancoLocalizar.jsp";
				tempList =Bancone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaBanco", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Bancone.getQuantidadePaginas());
					Bancodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Bancodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Bancone.Verificar(Bancodt); 
					if (Mensagem.length()==0){
						Bancone.salvar(Bancodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Banco");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Bancodt.getId()))){
						Bancodt.limpar();
						Bancodt = Bancone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Bancodt",Bancodt );
		request.getSession().setAttribute("Bancone",Bancone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
