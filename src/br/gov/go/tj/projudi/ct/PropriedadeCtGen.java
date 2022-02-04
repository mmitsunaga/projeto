package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.projudi.ne.PropriedadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PropriedadeCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 467558839592294578L;

    public  PropriedadeCtGen() {

	} 
		public int Permissao(){
			return PropriedadeDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PropriedadeDt Propriedadedt;
		PropriedadeNe Propriedadene;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Propriedade.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Propriedade");




		Propriedadene =(PropriedadeNe)request.getSession().getAttribute("Propriedadene");
		if (Propriedadene == null )  Propriedadene = new PropriedadeNe();  


		Propriedadedt =(PropriedadeDt)request.getSession().getAttribute("Propriedadedt");
		if (Propriedadedt == null )  Propriedadedt = new PropriedadeDt();  

		Propriedadedt.setPropriedade( request.getParameter("Propriedade")); 
		Propriedadedt.setPropriedadeCodigo( request.getParameter("PropriedadeCodigo")); 
		Propriedadedt.setValor( request.getParameter("Valor")); 

		Propriedadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Propriedadedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Propriedadene.excluir(Propriedadedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PropriedadeLocalizar.jsp";
				request.setAttribute("tempBuscaId_Propriedade","Id_Propriedade");
				request.setAttribute("tempBuscaPropriedade","Propriedade");
				request.setAttribute("tempRetorno","Propriedade");
				tempList =Propriedadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPropriedade", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Propriedadene.getQuantidadePaginas());
					Propriedadedt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Propriedadedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Propriedadene.Verificar(Propriedadedt); 
					if (Mensagem.length()==0){
						Propriedadene.salvar(Propriedadedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Propriedade");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Propriedadedt.getId()))){
						Propriedadedt.limpar();
						Propriedadedt = Propriedadene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Propriedadedt",Propriedadedt );
		request.getSession().setAttribute("Propriedadene",Propriedadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
