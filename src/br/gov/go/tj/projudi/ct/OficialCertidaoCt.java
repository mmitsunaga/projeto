package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.ModeloNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.OficialCertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class OficialCertidaoCt extends OficialCertidaoCtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -509830912903299452L;

//
	
	public  OficialCertidaoCt() {

	} 

	public int Permissao() {
		return OficialCertidaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		OficialCertidaoDt OficialCertidaodt;
		OficialCertidaoNe OficialCertidaone;
		////lisNomeBusca = new ArrayList();
		List tempList = null;
		byte[] byTemp = null; //Usado para imprimir relatório

		MovimentacaoNe Movimentacaone;
		int fluxo = -1;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		String stDataInicial = "";
		String stDataFinal = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("dataInicial") != null) stDataInicial = request.getParameter("dataInicial");
		if(request.getParameter("dataFinal") != null) stDataFinal = request.getParameter("dataFinal");
		
		//-fim controle de buscas ajax
		
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

		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null") && request.getParameter("tempFluxo1").length() > 0) 
			fluxo = Funcoes.StringToInt(request.getParameter("tempFluxo1"));

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();
		
		OficialCertidaodt.setCertidaoNome( request.getParameter("CertidaoNome")); 
		//OficialCertidaodt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia"));
		OficialCertidaodt.setId_UsuarioServentia( UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		OficialCertidaodt.setNome( request.getParameter("Nome")); 
		OficialCertidaodt.setStatus( request.getParameter("Status")); 
		OficialCertidaodt.setNumeroMandado( request.getParameter("NumeroMandado")); 
		OficialCertidaodt.setDataEmissao( request.getParameter("DataEmissao")); 
		OficialCertidaodt.setTexto( request.getParameter("TextoEditor")); 
		OficialCertidaodt.setGrupo( request.getParameter("Grupo")); 
		OficialCertidaodt.setId_Modelo(request.getParameter("Id_Modelo"));
		OficialCertidaodt.setModelo(request.getParameter("Modelo"));
		request.setAttribute("DataInicialCertidao", "");
		request.setAttribute("DataFinalCertidao", "");

		
		//Caso exista uma lista de mandado é preenchida
        if (OficialCertidaodt.getListaMandados() != null) {
            for (int i=0; i<OficialCertidaodt.getListaMandados().size(); i++) {
            	//InstituicaoEnderecoDt instituicaoEnderecoDt = (InstituicaoEnderecoDt) Instituicaodt.getListaEnderecoInstituicao().get(i);
            	OficialCertidaoDt oficialCertidaoDt = (OficialCertidaoDt) OficialCertidaodt.getListaMandados().get(i);
            	oficialCertidaoDt.setId(request.getParameter("Id_Certidao_" + i));
            	oficialCertidaoDt.setNumeroMandado(request.getParameter("NumeroMandado_" + i));
            	oficialCertidaoDt.setModelo(request.getParameter("Modelo_" + i));
            	oficialCertidaoDt.setDataEmissao(request.getParameter("DataEmissao_" + i));
            	oficialCertidaoDt.setStatus(request.getParameter("Status_" + i));
            }
        }
	
		OficialCertidaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		OficialCertidaodt.setUsuario(UsuarioSessao.getUsuarioDt().getNome());
		//OficialCertidaodt.setCargo(UsuarioSessao.getUsuarioDt().getGrupo());
		
		OficialCertidaodt.setCargo(UsuarioSessao.getUsuarioDt().getCargoTipo());
		OficialCertidaodt.setIpComputadorLog(request.getRemoteAddr());
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Excluir: //Excluir
				Mensagem=OficialCertidaone.VerificarTexto(OficialCertidaodt); 
				if (Mensagem.length()==0){

					if(OficialCertidaodt.getId().length()==0){
						request.setAttribute("MensagemErro", "Favor selecionar a certidão." );	
					}else{
						OficialCertidaone.excluir(OficialCertidaodt); 
						request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
					}
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Numero do mandado"};
					String[] lisDescricao = {"Numero do mandado","Certidão","Data de Emissão","Status"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_OficialCertidao");
					request.setAttribute("tempBuscaDescricao","Numero do mandado");
					request.setAttribute("tempBuscaPrograma","mandado");
					request.setAttribute("tempRetorno","OficialCertidao");
					request.setAttribute("tempDescricaoId","Id certidão");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					OficialCertidaodt.limpar();
				} else {
					String stTemp="";
					
					if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.COORDENADOR_CENTRAL_MANDADO){						
						stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, "", PosicaoPaginaAtual);		
					} else {					
						stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, OficialCertidaodt.getId_UsuarioLog(), PosicaoPaginaAtual);
					}
					
					enviarJSON(response, stTemp);						
					
					return;
				}
				break;
			case Configuracao.Novo: 
				OficialCertidaodt.limpar();
				break;
			case Configuracao.Salvar: 
					Mensagem=OficialCertidaone.VerificarTexto(OficialCertidaodt); 
					if (Mensagem.length()==0){
						OficialCertidaone.salvar(OficialCertidaodt); 
						OficialCertidaodt.setModelo("");
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"UsuarioServentia"};
					String[] lisDescricao = {"UsuarioServentia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuarioServentia");
					request.setAttribute("tempBuscaDescricao","Nome");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","OficialCertidao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, OficialCertidaodt.getId_UsuarioLog(), PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
					
				case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Modelo"};
						String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Modelo");
						request.setAttribute("tempBuscaDescricao", "Modelo");
						request.setAttribute("tempBuscaPrograma", "Modelo");
						request.setAttribute("tempRetorno", "OficialCertidao");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempDescricaoDescricao", "Modelo");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					}else{
						String stTemp = "";
						OficialCertidaodt.limpar();
						String CodigoArquivoTipo = Integer.toString(ArquivoTipoDt.CERTIDAO); //Caso queira passar um tipo especifico, como por exemplo código 33(mandados) é só colocar nesta variável
						stTemp = OficialCertidaone.consultarModeloTipoArquivoJSON(UsuarioSessao.getUsuarioDt(), CodigoArquivoTipo, stNomeBusca1, PosicaoPaginaAtual);
							
							enviarJSON(response, stTemp);
							
						
						return;
					}	
					break;

				//fluxo 1: consultar mandado
				case Configuracao.Editar:
					switch(fluxo){
						case 1: //consultar mandado
							//preeche o objeto certidão para teste
							OficialCertidaodt.setNumeroMandado( request.getParameter("NumeroMandadoSPG"));
							Mensagem=OficialCertidaone.Verificar(OficialCertidaodt); 
							if (Mensagem.length()==0){
								OficialCertidaodt.limpar();
								OficialCertidaodt.setNumeroMandado( request.getParameter("NumeroMandadoSPG"));
								OficialCertidaodt.setId_Modelo(request.getParameter("Id_Modelo"));
								OficialCertidaodt.setModelo(request.getParameter("Modelo"));
								
								String msg = OficialCertidaone.consultarMandado(OficialCertidaodt);
								if (msg.length()==0){
									OficialCertidaodt.setCertidaoNome(OficialCertidaodt.getId_Modelo());
									OficialCertidaodt.setDataEmissao(Funcoes.DataHora(new Date(System.currentTimeMillis())));
									
									//Fazer pesquisa primeiro no SPG, caso traga algum registro habilitar a chamada abaixo
									setParametrosAuxiliares(OficialCertidaodt, 0, paginaatual, request, Movimentacaone, UsuarioSessao);

									OficialCertidaodt.setStatus("0");
								}else{
									OficialCertidaodt.setTexto(""); //Limpa o texto
									OficialCertidaodt.setNumeroMandado(""); //Limpa numero do mandado
									OficialCertidaodt.setDataEmissao(""); //Limpa a data de emissão
									request.setAttribute("MensagemErro", msg );
								}
							}else{

								request.setAttribute("MensagemErro", Mensagem );
							}
							
							break;
						case 2: //Finaliza documento
							Mensagem=OficialCertidaone.VerificarTexto(OficialCertidaodt); 
							if (Mensagem.length()==0){
								OficialCertidaodt.setStatus("1"); //Seta status = 1 para finalizar o documento
								OficialCertidaone.salvar(OficialCertidaodt); 
								request.setAttribute("MensagemOk", "Documento finalizado com Sucesso");
							}else	request.setAttribute("MensagemErro", Mensagem );
						break;
						
						case 3: //Abre o formulário para emissão de relatório certidão
							if (request.getParameter("Passo")==null){
								String[] lisNomeBusca = {"Numero do mandado"};
								String[] lisDescricao = {"Numero do mandado","Certidão","Data de Emissão","Status"};
								stAcao="/WEB-INF/jsptjgo/OficialCertidaoRelatorio.jsp";
								request.setAttribute("tempBuscaId","Id_OficialCertidao");
								request.setAttribute("tempBuscaDescricao","Numero do mandado");
								request.setAttribute("tempBuscaPrograma","certidão");
								request.setAttribute("tempRetorno","OficialCertidao");
								request.setAttribute("tempDescricaoId","Id certidão");
								request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
								//request.setAttribute("PaginaAtual", (Configuracao.Localizar));
								request.setAttribute("PaginaAtual", "-1");
								request.setAttribute("tempFluxo1", "3");
								request.setAttribute("PosicaoPaginaAtual", "0");
								request.setAttribute("QuantidadePaginas", "0");
								request.setAttribute("lisNomeBusca", lisNomeBusca);
								request.setAttribute("lisDescricao", lisDescricao);
								request.setAttribute("dataInicial", Funcoes.dateToStringSoData(new Date()));
								request.setAttribute("dataFinal", Funcoes.dateToStringSoData(new Date()));
								OficialCertidaodt.limpar();
							} else {
								String stTemp="";
								
								if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.OFICIAL_JUSTICA){
									//try{
									//nao faz sentido duas consulta para a mesma variavel
									//stTemp = OficialCertidaone.consultarDescricaoJSON(stNomeBusca1, "", PosicaoPaginaAtual);									
									stTemp = OficialCertidaone.consultarMandadosJSON(stNomeBusca1, stDataInicial, stDataFinal, "", PosicaoPaginaAtual);
								} else {									
									stTemp = OficialCertidaone.consultarMandadosJSON(stNomeBusca1, stDataInicial, stDataFinal, OficialCertidaodt.getId_UsuarioLog(), PosicaoPaginaAtual);
								}
																
								enviarJSON(response, stTemp);
																	
								return;
							}
							break;

						case 4: //Imprimi relatórios selecionados
							boolean editar = false; //Usado para verificar se o usuário selecionou algum registro com status editar
							String[] idsDados = request.getParameterValues("chkSelecao");

							if (idsDados!=null){
								//Envia a consulta dos ids selecionado para pesquisa
								tempList = OficialCertidaone.consultarMandadosSelecionados(idsDados, OficialCertidaodt.getId_UsuarioLog(), "0");
								OficialCertidaodt.setListaMandados(tempList); //Atribue a lista para o objeto.
								
								if (OficialCertidaodt.getListaMandados() != null) {//Verifica  se lista não está vázia
									//Verifica se tem algum registro com status Editar
									OficialCertidaoDt Objeto = new OficialCertidaoDt();
									for (int i=0; i<OficialCertidaodt.getListaMandados().size(); i++) {
										Objeto = (OficialCertidaoDt) OficialCertidaodt.getListaMandados().get(i);
										if (Objeto.getStatus().equals("Editar")){
											editar = true;
										}
									}
									//Caso todos os registro estejam com o status finalizado, imprime a lista
									if (editar==false){

										//byTemp = OficialCertidaone.gerarVariasCertidoes(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , liTemp);
										byTemp = OficialCertidaone.gerarVariasCertidoes(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , OficialCertidaodt.getListaMandados());
							            
										String nome="RelatorioListaCertidao";										
										enviarPDF(response, byTemp, nome);
										return;
										
										
									}else{
										request.setAttribute("MensagemErro", "Favor selecionar somente registros finalizados!");
									}
								}else{
									request.setAttribute("MensagemErro", "Favor selecionar um registro para impressão!");
								}
							}else{
								request.setAttribute("MensagemErro", "Favor fazer novamente a pesquisa e selecionar um registro para impressão!");
							}
						break;
						
						default:
						stId = request.getParameter("Id_OficialCertidao");
						if (stId != null && !stId.isEmpty())
							if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( OficialCertidaodt.getId()))){
								OficialCertidaodt.limpar();
								OficialCertidaodt = OficialCertidaone.consultarId(stId);
								OficialCertidaodt.setModelo("");
							}
						break;
					}
					break;
		}

		request.setAttribute("TextoEditor", OficialCertidaodt.getTexto());
		request.getSession().setAttribute("OficialCertidaodt",OficialCertidaodt );
		request.getSession().setAttribute("OficialCertidaone",OficialCertidaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//	private void setParametrosAuxiliares(MovimentacaoProcessoDt movimentacaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioNe){
	protected void setParametrosAuxiliares(OficialCertidaoDt oficialCertidaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioNe) throws Exception{
		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo
		// do arquivo
	//	if (!oficialCertidaoDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
		if (!oficialCertidaoDt.getId_Modelo().equals("")) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(oficialCertidaoDt.getId_Modelo(), null, usuarioNe.getUsuarioDt());
		//	oficialCertidaoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			//oficialCertidaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			oficialCertidaoDt.setTexto(modeloDt.getTexto());
			ModeloNe modeloNe = new ModeloNe();
			oficialCertidaoDt.setTexto(modeloNe.montaModeloOficialCertidao(oficialCertidaoDt, usuarioNe.getUsuarioDt(), oficialCertidaoDt.getId_Modelo()));
		}
		
		
//		request.setAttribute("PaginaAnterior", paginaatual);
	//	request.setAttribute("TextoEditor", oficialCertidaoDt.getTexto());
		//request.setAttribute("Id_ArquivoTipo", movimentacaoDt.getId_ArquivoTipo());
		//request.setAttribute("ArquivoTipo", movimentacaoDt.getArquivoTipo());
//		request.setAttribute("Modelo", oficialCertidaoDt.getModelo());
	//	request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

	}
	
}
