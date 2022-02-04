package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.ExpedirMandadoDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.AreaNe;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.projudi.ne.EnderecoNe;
import br.gov.go.tj.projudi.ne.EscalaNe;
import br.gov.go.tj.projudi.ne.ExpedirMandadoNe;
import br.gov.go.tj.projudi.ne.MandadoTipoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ExpedirMandadoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -6684896747181329319L;

    public  ExpedirMandadoCtGen() {

	} 

		public int Permissao(){
			return ExpedirMandadoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ExpedirMandadoDt ExpedirMandadodt;
		ExpedirMandadoNe ExpedirMandadone;
		ProcessoParteNe Partene;
		EnderecoNe Enderecone;
		BairroNe Bairrone;
		AreaNe Areane;
		MandadoTipoNe MandadoTipone;
		EscalaNe Escalane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ExpedirMandado");
						request.setAttribute("tempBuscaCodDocumento","CodDocumento");
						request.setAttribute("tempBuscaNumeroProcesso","NumeroProcesso");
						request.setAttribute("tempBuscaId_Parte",request.getParameter("tempBuscaId_Parte"));
						request.setAttribute("tempBuscaParte",request.getParameter("tempBuscaParte"));
						request.setAttribute("tempBuscaId_Endereco",request.getParameter("tempBuscaId_Endereco"));
						request.setAttribute("tempBuscaLogradouro",request.getParameter("tempBuscaLogradouro"));
						request.setAttribute("tempBuscaId_Bairro",request.getParameter("tempBuscaId_Bairro"));
						request.setAttribute("tempBuscaBairro",request.getParameter("tempBuscaBairro"));
						request.setAttribute("tempBuscaId_Area",request.getParameter("tempBuscaId_Area"));
						request.setAttribute("tempBuscaArea",request.getParameter("tempBuscaArea"));
						request.setAttribute("tempBuscaId_MandadoTipo",request.getParameter("tempBuscaId_MandadoTipo"));
						request.setAttribute("tempBuscaMandadoTipo",request.getParameter("tempBuscaMandadoTipo"));
						request.setAttribute("tempBuscaId_Escala",request.getParameter("tempBuscaId_Escala"));
						request.setAttribute("tempBuscaEscala",request.getParameter("tempBuscaEscala"));



		ExpedirMandadone =(ExpedirMandadoNe)request.getSession().getAttribute("ExpedirMandadone");
		if (ExpedirMandadone == null )  ExpedirMandadone = new ExpedirMandadoNe();  


		ExpedirMandadodt =(ExpedirMandadoDt)request.getSession().getAttribute("ExpedirMandadodt");
		if (ExpedirMandadodt == null )  ExpedirMandadodt = new ExpedirMandadoDt();  

		ExpedirMandadodt.setNumeroProcesso( request.getParameter("NumeroProcesso")); 

		ExpedirMandadodt.setData( request.getParameter("Data")); 

		ExpedirMandadodt.setCodCumprimento( request.getParameter("CodCumprimento")); 

		ExpedirMandadodt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 

		ExpedirMandadodt.setServentia( request.getParameter("Serventia")); 

		ExpedirMandadodt.setId_Parte( request.getParameter("Id_Parte")); 

		ExpedirMandadodt.setParte( request.getParameter("Parte")); 

		ExpedirMandadodt.setCpfCnpj( request.getParameter("CpfCnpj")); 

		ExpedirMandadodt.setRg( request.getParameter("Rg")); 

		ExpedirMandadodt.setMae( request.getParameter("Mae")); 

		ExpedirMandadodt.setCtps( request.getParameter("Ctps")); 

		ExpedirMandadodt.setInss( request.getParameter("Inss")); 

		ExpedirMandadodt.setDataNascimento( request.getParameter("DataNascimento")); 

		ExpedirMandadodt.setTelefone( request.getParameter("Telefone")); 

		ExpedirMandadodt.setCelular( request.getParameter("Celular")); 

		ExpedirMandadodt.setId_Endereco( request.getParameter("Id_Endereco")); 

		ExpedirMandadodt.setLogradouro( request.getParameter("Logradouro")); 

		ExpedirMandadodt.setNumero( request.getParameter("Numero")); 

		ExpedirMandadodt.setComplemento( request.getParameter("Complemento")); 

		ExpedirMandadodt.setId_Bairro( request.getParameter("Id_Bairro")); 

		ExpedirMandadodt.setBairro( request.getParameter("Bairro")); 

		ExpedirMandadodt.setCodCidade( request.getParameter("CodCidade")); 

		ExpedirMandadodt.setCidade( request.getParameter("Cidade")); 

		ExpedirMandadodt.setEstado( request.getParameter("Estado")); 

		ExpedirMandadodt.setCep( request.getParameter("Cep")); 

		ExpedirMandadodt.setId_Area( request.getParameter("Id_Area")); 

		ExpedirMandadodt.setArea( request.getParameter("Area")); 

		ExpedirMandadodt.setId_MandadoTipo( request.getParameter("Id_MandadoTipo")); 

		ExpedirMandadodt.setMandadoTipo( request.getParameter("MandadoTipo")); 

		ExpedirMandadodt.setAssistencia( request.getParameter("Assistencia")); 

		ExpedirMandadodt.setId_Escala( request.getParameter("Id_Escala")); 

		ExpedirMandadodt.setEscala( request.getParameter("Escala")); 

		ExpedirMandadodt.setRegiao( request.getParameter("Regiao")); 


		ExpedirMandadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ExpedirMandadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						request.setAttribute("PaginaAnterior",paginaatual);
						request.setAttribute("MensagemOk", "");
						request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ExpedirMandadone.excluir(ExpedirMandadodt);
						request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				ExpedirMandadodt.limpar();
						request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ExpedirMandadone.Verificar(ExpedirMandadodt); 
				if (Mensagem.length()==0){
					ExpedirMandadone.salvar(ExpedirMandadodt);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (((ProcessoParteDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					Partene = new ProcessoParteNe(); 
					tempList =Partene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaParte", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Partene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (((EnderecoDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					Enderecone = new EnderecoNe(); 
					tempList =Enderecone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEndereco", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Enderecone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (((BairroDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					Bairrone = new BairroNe(); 
					tempList =Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaBairro", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Bairrone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (((AreaDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					Areane = new AreaNe(); 
					tempList =Areane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaArea", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Areane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (((MandadoTipoDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					MandadoTipone = new MandadoTipoNe(); 
					tempList =MandadoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaMandadoTipo", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoTipone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (((EscalaDt.CodigoPermissao - ExpedirMandadoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ExpedirMandadoDt.CodigoPermissao * Configuracao.QtdPermissao))):
					Escalane = new EscalaNe(); 
					tempList =Escalane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEscala", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Escalane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("CodDocumento");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ExpedirMandadodt.getCodDocumento()))){
						ExpedirMandadodt.limpar();
						ExpedirMandadodt = ExpedirMandadone.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("ExpedirMandadodt",ExpedirMandadodt );
						request.getSession().setAttribute("ExpedirMandadone",ExpedirMandadone );

		RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/ExpedirMandado.jsp");
		dis.include(request, response);
	}
}
