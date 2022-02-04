package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;

public class UsuarioCtGen extends Controle {


    private static final long serialVersionUID = 5042666560213129179L;

    public  UsuarioCtGen() {

	} 

	public int Permissao(){
		return UsuarioDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		return;
		
//		UsuarioDt Usuariodt;
//		UsuarioNe Usuarione;
//
//
//		List tempList=null; 
//		String Mensagem="";
//		String stId="";
//
//		String stAcao="/WEB-INF/jsptjgo/Usuario.jsp";
//
//		//--------------------------------------------------------------------------
//		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
//		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
//		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
//		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
//		//--------------------------------------------------------------------------
//		request.setAttribute("tempPrograma","Usuario");
//		request.setAttribute("tempBuscaId_Usuario","Id_Usuario");
//		request.setAttribute("tempBuscaUsuario","Usuario");
//		
//		request.setAttribute("tempBuscaId_CtpsUf","Id_CtpsUf");
//		request.setAttribute("tempBuscaCtpsUf","CtpsUf");
//		request.setAttribute("tempBuscaId_Naturalidade","Id_Naturalidade");
//		request.setAttribute("tempBuscaNaturalidade","Naturalidade");
//		request.setAttribute("tempBuscaId_Endereco","Id_Endereco");
//		request.setAttribute("tempBuscaEndereco","Endereco");
//		request.setAttribute("tempBuscaId_RgOrgaoExpedidor","Id_RgOrgaoExpedidor");
//		request.setAttribute("tempBuscaRgOrgaoExpedidor","RgOrgaoExpedidor");
//
//		request.setAttribute("tempRetorno","Usuario");
//
//		Usuarione =(UsuarioNe)request.getSession().getAttribute("Usuarione");
//		if (Usuarione == null )  Usuarione = new UsuarioNe();  
//
//
//		Usuariodt =(UsuarioDt)request.getSession().getAttribute("Usuariodt");
//		if (Usuariodt == null )  Usuariodt = new UsuarioDt();  
//
//		Usuariodt.setUsuario( request.getParameter("Usuario")); 
//		Usuariodt.setId_CtpsUf( request.getParameter("Id_CtpsUf")); 
//		Usuariodt.setCtpsEstado( request.getParameter("CtpsEstado")); 
//		Usuariodt.setId_Naturalidade( request.getParameter("Id_Naturalidade")); 
//		Usuariodt.setNaturalidade( request.getParameter("Naturalidade")); 
//		Usuariodt.setId_Endereco( request.getParameter("Id_Endereco")); 
//		Usuariodt.setEndereco( request.getParameter("Endereco")); 
//		Usuariodt.setId_RgOrgaoExpedidor( request.getParameter("Id_RgOrgaoExpedidor")); 
//		Usuariodt.setRgOrgaoExpedidor( request.getParameter("RgOrgaoExpedidor")); 
//		Usuariodt.setRgOrgaoExpedidorSigla( request.getParameter("RgOrgaoExpedidorSigla")); 
//		Usuariodt.setSenha( request.getParameter("Senha")); 
//		Usuariodt.setNome( request.getParameter("Nome")); 
//		Usuariodt.setSexo( request.getParameter("Sexo")); 
//		Usuariodt.setDataNascimento( request.getParameter("DataNascimento")); 
//		Usuariodt.setRg( request.getParameter("Rg")); 
//		Usuariodt.setRgDataExpedicao( request.getParameter("RgDataExpedicao")); 
//		Usuariodt.setCpf( request.getParameter("Cpf")); 
//		Usuariodt.setTituloEleitor( request.getParameter("TituloEleitor")); 
//		Usuariodt.setTituloEleitorZona( request.getParameter("TituloEleitorZona")); 
//		Usuariodt.setTituloEleitorSecao( request.getParameter("TituloEleitorSecao")); 
//		Usuariodt.setCtps( request.getParameter("Ctps")); 
//		Usuariodt.setCtpsSerie( request.getParameter("CtpsSerie")); 
//		Usuariodt.setPis( request.getParameter("Pis")); 
//		Usuariodt.setMatriculaTjGo( request.getParameter("MatriculaTjGo")); 
//		Usuariodt.setNumeroConciliador( request.getParameter("NumeroConciliador")); 
//		Usuariodt.setDataCadastro( request.getParameter("DataCadastro")); 
//		if (request.getParameter("Ativo") != null)
//			Usuariodt.setAtivo( request.getParameter("Ativo")); 
//		else Usuariodt.setAtivo("false");
//		Usuariodt.setDataAtivo( request.getParameter("DataAtivo")); 
//		Usuariodt.setDataExpiracao( request.getParameter("DataExpiracao")); 
//		Usuariodt.setEMail( request.getParameter("EMail")); 
//		Usuariodt.setTelefone( request.getParameter("Telefone")); 
//		Usuariodt.setCelular( request.getParameter("Celular")); 
//
//		Usuariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
//		Usuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
//
//
//		request.setAttribute("PaginaAnterior",paginaatual);
//		request.setAttribute("MensagemOk", "");
//		request.setAttribute("MensagemErro", "");
////--------------------------------------------------------------------------------//
//		switch (paginaatual) {
//			case Configuracao.ExcluirResultado: //Excluir
//				request.setAttribute("PaginaAtual", Configuracao.Editar);
//				Usuarione.excluir(Usuariodt); 
//				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
//				break;
//
//			case Configuracao.Imprimir: 
//				break;
//			case Configuracao.Localizar: //localizar
//				stAcao="/WEB-INF/jsptjgo/UsuarioLocalizar.jsp";
//				tempList =Usuarione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//				if (tempList.size()>0){
//					request.setAttribute("ListaUsuario", tempList); 
//					request.setAttribute("PaginaAtual", Configuracao.Localizar);
//					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//					request.setAttribute("QuantidadePaginas", Usuarione.getQuantidadePaginas());
//					Usuariodt.limpar();
//				}else{ 
//					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//					request.setAttribute("PaginaAtual", Configuracao.Editar);
//				}
//				break;
//			case Configuracao.Novo: 
//				Usuariodt.limpar();
//				request.setAttribute("PaginaAtual",Configuracao.Editar);
//				break;
//			case Configuracao.SalvarResultado: 
//				request.setAttribute("PaginaAtual", Configuracao.Editar);
//				Mensagem=Usuarione.Verificar(Usuariodt); 
//				if (Mensagem.length()==0){
//					Usuarione.salvar(Usuariodt); 
//					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
//				}else	request.setAttribute("MensagemErro", Mensagem );
//				break;
//				case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					stAcao="/WEB-INF/jsptjgo/CtpsUfLocalizar.jsp";
//						tempList =Usuarione.consultarDescricaoCtpsUf(tempNomeBusca, PosicaoPaginaAtual);
//						if (tempList.size()>0){
//							request.setAttribute("ListaCtpsUf", tempList); 
//							request.setAttribute("PaginaAtual", paginaatual);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", Usuarione.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//				case (CidadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					stAcao="/WEB-INF/jsptjgo/NaturalidadeLocalizar.jsp";
//						tempList =Usuarione.consultarDescricaoNaturalidade(tempNomeBusca, PosicaoPaginaAtual);
//						if (tempList.size()>0){
//							request.setAttribute("ListaNaturalidade", tempList); 
//							request.setAttribute("PaginaAtual", paginaatual);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", Usuarione.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
//						tempList =Usuarione.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
//						if (tempList.size()>0){
//							request.setAttribute("ListaEndereco", tempList); 
//							request.setAttribute("PaginaAtual", paginaatual);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", Usuarione.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//				case (RgOrgaoExpedidorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					stAcao="/WEB-INF/jsptjgo/RgOrgaoExpedidorLocalizar.jsp";
//						tempList =Usuarione.consultarDescricaoRgOrgaoExpedidor(tempNomeBusca, PosicaoPaginaAtual);
//						if (tempList.size()>0){
//							request.setAttribute("ListaRgOrgaoExpedidor", tempList); 
//							request.setAttribute("PaginaAtual", paginaatual);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", Usuarione.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
////--------------------------------------------------------------------------------//
//			default:
//				request.setAttribute("PaginaAtual", Configuracao.Editar);
//				stId = request.getParameter("Id_Usuario");
//				if (stId != null && !stId.isEmpty())
//					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Usuariodt.getId()))){
//						Usuariodt.limpar();
//						Usuariodt = Usuarione.consultarId(stId);
//					}
//				break;
//		}
//
//		request.getSession().setAttribute("Usuariodt",Usuariodt );
//		request.getSession().setAttribute("Usuarione",Usuarione );
//
//		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
//		dis.include(request, response);
	}

}
