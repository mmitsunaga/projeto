package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.OficialCertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class OficialCertidaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7656480651047475232L;

	public  OficialCertidaoCtGen() {

	} 
		public int Permissao() {
			return OficialCertidaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		OficialCertidaoDt OficialCertidaodt;
		OficialCertidaoNe OficialCertidaone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/OficialCertidao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","OficialCertidao");




		OficialCertidaone =(OficialCertidaoNe)request.getSession().getAttribute("OficialCertidaone");
		if (OficialCertidaone == null )  OficialCertidaone = new OficialCertidaoNe();  


		OficialCertidaodt =(OficialCertidaoDt)request.getSession().getAttribute("OficialCertidaodt");
		if (OficialCertidaodt == null )  OficialCertidaodt = new OficialCertidaoDt();  

		OficialCertidaodt.setCertidaoNome( request.getParameter("CertidaoNome")); 
		OficialCertidaodt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 
		OficialCertidaodt.setNome( request.getParameter("Nome")); 
		OficialCertidaodt.setStatus( request.getParameter("Status")); 
		OficialCertidaodt.setNumeroMandado( request.getParameter("NumeroMandado")); 
		OficialCertidaodt.setDataEmissao( request.getParameter("DataEmissao")); 
		OficialCertidaodt.setTexto( request.getParameter("Texto")); 
		OficialCertidaodt.setGrupo( request.getParameter("Grupo")); 

		OficialCertidaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		OficialCertidaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					OficialCertidaone.excluir(OficialCertidaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("OficialCertidao");
					descricao.add("OficialCertidao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_OficialCertidao");
					request.setAttribute("tempBuscaDescricao","OficialCertidao");
					request.setAttribute("tempBuscaPrograma","OficialCertidao");
					request.setAttribute("tempRetorno","OficialCertidao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","OficialCertidao");
					stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, OficialCertidaodt.getId_UsuarioLog(), PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				OficialCertidaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=OficialCertidaone.Verificar(OficialCertidaodt); 
					if (Mensagem.length()==0){
						OficialCertidaone.salvar(OficialCertidaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("UsuarioServentia");
					descricao.add("UsuarioServentia");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuarioServentia");
					request.setAttribute("tempBuscaDescricao","Nome");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","OficialCertidao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","OficialCertidao");
					stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, OficialCertidaodt.getId_UsuarioLog(), PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_OficialCertidao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( OficialCertidaodt.getId()))){
						OficialCertidaodt.limpar();
						OficialCertidaodt = OficialCertidaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("OficialCertidaodt",OficialCertidaodt );
		request.getSession().setAttribute("OficialCertidaone",OficialCertidaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
