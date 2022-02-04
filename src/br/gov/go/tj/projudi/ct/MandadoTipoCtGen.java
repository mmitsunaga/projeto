package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.ne.MandadoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7138458031163452927L;

    public  MandadoTipoCtGen() {

	} 
		public int Permissao(){
			return MandadoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoTipoDt MandadoTipodt;
		MandadoTipoNe MandadoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoTipo");




		MandadoTipone =(MandadoTipoNe)request.getSession().getAttribute("MandadoTipone");
		if (MandadoTipone == null )  MandadoTipone = new MandadoTipoNe();  


		MandadoTipodt =(MandadoTipoDt)request.getSession().getAttribute("MandadoTipodt");
		if (MandadoTipodt == null )  MandadoTipodt = new MandadoTipoDt();  

		MandadoTipodt.setMandadoTipo( request.getParameter("MandadoTipo")); 
		MandadoTipodt.setMandadoTipoCodigo( request.getParameter("MandadoTipoCodigo")); 

		MandadoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoTipone.excluir(MandadoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				MandadoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoTipone.Verificar(MandadoTipodt); 
					if (Mensagem.length()==0){
						MandadoTipone.salvar(MandadoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoTipodt.getId()))){
						MandadoTipodt.limpar();
						MandadoTipodt = MandadoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoTipodt",MandadoTipodt );
		request.getSession().setAttribute("MandadoTipone",MandadoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
