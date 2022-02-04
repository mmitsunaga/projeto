package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.MandadoJudicialNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MandadoJudicialCtGen extends Controle {

    private static final long serialVersionUID = -8310542299274983976L;

    public  MandadoJudicialCtGen() {} 
		public int Permissao() {
			return MandadoJudicialDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoJudicialDt MandadoJudicialdt;
		MandadoJudicialNe MandadoJudicialne;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		
		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoJudicial.jsp";

		request.setAttribute("tempPrograma","MandadoJudicial");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		MandadoJudicialne =(MandadoJudicialNe)request.getSession().getAttribute("MandadoJudicialne");
		if (MandadoJudicialne == null )  MandadoJudicialne = new MandadoJudicialNe();  

		MandadoJudicialdt =(MandadoJudicialDt)request.getSession().getAttribute("MandadoJudicialdt");
		if (MandadoJudicialdt == null )  MandadoJudicialdt = new MandadoJudicialDt();  

		MandadoJudicialdt.setId_MandadoTipo( request.getParameter("Id_MandadoTipo")); 
		MandadoJudicialdt.setMandadoTipo( request.getParameter("MandadoTipo")); 
		MandadoJudicialdt.setId_MandadoJudicialStatus( request.getParameter("Id_MandadoJudicialStatus")); 
		MandadoJudicialdt.setMandadoJudicialStatus( request.getParameter("MandadoJudicialStatus")); 
		MandadoJudicialdt.setId_Area( request.getParameter("Id_Area")); 
		MandadoJudicialdt.setArea( request.getParameter("Area")); 
		MandadoJudicialdt.setId_Zona( request.getParameter("Id_Zona")); 
		MandadoJudicialdt.setZona( request.getParameter("Zona")); 
		MandadoJudicialdt.setId_Regiao( request.getParameter("Id_Regiao")); 
		MandadoJudicialdt.setRegiao( request.getParameter("Regiao")); 
		MandadoJudicialdt.setId_Bairro( request.getParameter("Id_Bairro")); 
		MandadoJudicialdt.setBairro( request.getParameter("Bairro")); 
		MandadoJudicialdt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		MandadoJudicialdt.setProcessoParte( request.getParameter("NomeParte")); 
		MandadoJudicialdt.setId_EnderecoParte( request.getParameter("Id_EnderecoParte")); 
		MandadoJudicialdt.setEnderecoParte( request.getParameter("EnderecoParte")); 
		MandadoJudicialdt.setId_Pendencia( request.getParameter("Id_Pendencia")); 
		MandadoJudicialdt.setPendencia( request.getParameter("Pendencia")); 
		MandadoJudicialdt.setId_Escala( request.getParameter("Id_Escala")); 
		MandadoJudicialdt.setId_UsuarioServentia_1( request.getParameter("Id_UsuarioServentia")); 
		MandadoJudicialdt.setId_UsuarioServentia_2( request.getParameter("Id_UsuarioServentia_2")); 
		MandadoJudicialdt.setEscala( request.getParameter("Escala")); 
		MandadoJudicialdt.setValor( request.getParameter("Valor")); 
		if (request.getParameter("Assistencia") != null)
			MandadoJudicialdt.setAssistencia( request.getParameter("Assistencia")); 
		else MandadoJudicialdt.setAssistencia("false");
		MandadoJudicialdt.setLocomocoesFrutiferas( request.getParameter("LocomocoesFrutiferas")); 
		MandadoJudicialdt.setLocomocoesInfrutiferas( request.getParameter("LocomocoesInfrutiferas")); 
		if (request.getParameter("LocomocaoHoraMarcada") != null)
			MandadoJudicialdt.setLocomocaoHoraMarcada( request.getParameter("LocomocaoHoraMarcada")); 
		else MandadoJudicialdt.setLocomocaoHoraMarcada("false");

		MandadoJudicialdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoJudicialdt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoJudicialne.excluir(MandadoJudicialdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/MandadoJudicialLocalizar.jsp";
				request.setAttribute("tempBuscaId_MandadoJudicial","Id_MandadoJudicial");
				request.setAttribute("tempBuscaMandadoJudicial","MandadoJudicial");
				request.setAttribute("tempRetorno","MandadoJudicial");
				tempList =MandadoJudicialne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaMandadoJudicial", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					MandadoJudicialdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				MandadoJudicialdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoJudicialne.Verificar(MandadoJudicialdt); 
					if (Mensagem.length()==0){
						MandadoJudicialne.salvar(MandadoJudicialdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (MandadoJudicialStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_MandadoJudicialStatus","Id_MandadoJudicialStatus");
					request.setAttribute("tempBuscaMandadoJudicialStatus","MandadoJudicialStatus");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/MandadoJudicialStatusLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoMandadoJudicialStatus(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaMandadoJudicialStatus", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AreaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						nomeBusca = new ArrayList();
						descricao = new ArrayList();
						nomeBusca.add("Area");
						descricao.add("Area");
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Area");
						request.setAttribute("tempBuscaDescricao","Area");
						request.setAttribute("tempBuscaPrograma","Area");			
						request.setAttribute("tempRetorno","MandadoJudicial");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", String.valueOf(AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("nomeBusca", nomeBusca);
						request.setAttribute("descricao", descricao);
						break;
					} else {
						String stTemp="";
						stTemp = MandadoJudicialne.consultarDescricaoAreaJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						try{
							enviarJSON(response, stTemp);
							
						}
						catch(Exception e) {
						}
						return;								
					}
				case (ZonaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						nomeBusca = new ArrayList();
						descricao = new ArrayList();
						nomeBusca.add("Zona");
						nomeBusca.add("Cidade");
						descricao.add("Zona");
						descricao.add("Cidade");
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Zona");
						request.setAttribute("tempBuscaDescricao","Zona");
						request.setAttribute("tempBuscaPrograma","Zona");			
						request.setAttribute("tempRetorno","Escala");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", String.valueOf(ZonaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("nomeBusca", nomeBusca);
						request.setAttribute("descricao", descricao);
					} else {
						String stTemp="";
						request.setAttribute("tempRetorno","Escala");	
						stTemp = MandadoJudicialne.consultarDescricaoZonaJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						try{
							enviarJSON(response, stTemp);
							
						} catch(Exception e) {}
						return;								
					}
					break;
				case (BairroDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Bairro","Id_Bairro");
					request.setAttribute("tempBuscaBairro","Bairro");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/BairroLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoBairro(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaBairro", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
					request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoProcessoParte(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoParte", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_EnderecoParte","Id_EnderecoParte");
					request.setAttribute("tempBuscaEndereco","Endereco");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEndereco", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (PendenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Pendencia","Id_Pendencia");
					request.setAttribute("tempBuscaPendencia","Pendencia");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/PendenciaLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoPendencia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaPendencia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EscalaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Escala","Id_Escala");
					request.setAttribute("tempBuscaEscala","Escala");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/EscalaLocalizar.jsp";
					tempList =MandadoJudicialne.consultarDescricaoEscala(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEscala", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
					request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
					request.setAttribute("tempRetorno","MandadoJudicial");
					stAcao="/WEB-INF/jsptjgo/ServentiaCargoLocalizar.jsp";
//					tempList =MandadoJudicialne.consultarDescricaoServentiaCargo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentiaCargo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", MandadoJudicialne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoJudicial");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoJudicialdt.getId()))){
						MandadoJudicialdt.limpar();
						MandadoJudicialdt = MandadoJudicialne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoJudicialdt",MandadoJudicialdt );
		request.getSession().setAttribute("MandadoJudicialne",MandadoJudicialne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
