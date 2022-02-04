package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.ne.CertidaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CertidaoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2837112934581278913L;

    public  CertidaoTipoCtGen() {

	} 
		public int Permissao(){
			return CertidaoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertidaoTipoDt CertidaoTipodt;
		CertidaoTipoNe CertidaoTipone;


		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CertidaoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CertidaoTipo");




		CertidaoTipone =(CertidaoTipoNe)request.getSession().getAttribute("CertidaoTipone");
		if (CertidaoTipone == null )  CertidaoTipone = new CertidaoTipoNe();  


		CertidaoTipodt =(CertidaoTipoDt)request.getSession().getAttribute("CertidaoTipodt");
		if (CertidaoTipodt == null )  CertidaoTipodt = new CertidaoTipoDt();  

		CertidaoTipodt.setCertidaoTipoCodigo( request.getParameter("CertidaoTipoCodigo")); 
		CertidaoTipodt.setCertidaoTipo( request.getParameter("CertidaoTipo")); 

		CertidaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CertidaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CertidaoTipone.excluir(CertidaoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				CertidaoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CertidaoTipone.Verificar(CertidaoTipodt); 
					if (Mensagem.length()==0){
						CertidaoTipone.salvar(CertidaoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CertidaoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CertidaoTipodt.getId()))){
						CertidaoTipodt.limpar();
						CertidaoTipodt = CertidaoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CertidaoTipodt",CertidaoTipodt );
		request.getSession().setAttribute("CertidaoTipone",CertidaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
