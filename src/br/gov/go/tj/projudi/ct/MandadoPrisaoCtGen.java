package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.ne.MandadoPrisaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoPrisaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8458851830528298592L;

	public  MandadoPrisaoCtGen() {

	} 
		public int Permissao() {
			return MandadoPrisaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoPrisaoDt MandadoPrisaodt;
		MandadoPrisaoNe MandadoPrisaone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoPrisao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoPrisao");




		MandadoPrisaone =(MandadoPrisaoNe)request.getSession().getAttribute("MandadoPrisaone");
		if (MandadoPrisaone == null )  MandadoPrisaone = new MandadoPrisaoNe();  


		MandadoPrisaodt =(MandadoPrisaoDt)request.getSession().getAttribute("MandadoPrisaodt");
		if (MandadoPrisaodt == null )  MandadoPrisaodt = new MandadoPrisaoDt();  

		MandadoPrisaodt.setMandadoPrisaoNumero( request.getParameter("MandadoPrisaoNumero")); 
//		MandadoPrisaodt.setDataDelito( request.getParameter("DataDelito")); 
		MandadoPrisaodt.setDataExpedicao( request.getParameter("DataExpedicao")); 
//		MandadoPrisaodt.setMandadoPrisaoNumeroAnterior( request.getParameter("MandadoPrisaoNumeroAnterior")); 
		MandadoPrisaodt.setPenaImposta( request.getParameter("PenaImposta")); 
		MandadoPrisaodt.setPrazoPrisao( request.getParameter("PrazoPrisao")); 
//		MandadoPrisaodt.setRecaptura( request.getParameter("Recaptura")); 
		MandadoPrisaodt.setSinteseDecisao( request.getParameter("SinteseDecisao")); 
		MandadoPrisaodt.setId_MandadoPrisaoStatus( request.getParameter("Id_MandadoPrisaoStatus")); 
		MandadoPrisaodt.setMandadoPrisaoStatus( request.getParameter("MandadoPrisaoStatus")); 
		MandadoPrisaodt.setValorFianca( request.getParameter("ValorFianca")); 
		MandadoPrisaodt.setId_RegimeExecucao( request.getParameter("Id_RegimeExe")); 
		MandadoPrisaodt.setRegimeExecucao( request.getParameter("RegimeExe")); 
		MandadoPrisaodt.setId_PrisaoTipo( request.getParameter("Id_PrisaoTipo")); 
		MandadoPrisaodt.setPrisaoTipo( request.getParameter("PrisaoTipo")); 
		MandadoPrisaodt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		MandadoPrisaodt.setProcessoParte( request.getParameter("ProcessoParte")); 
		MandadoPrisaodt.setId_Assunto( request.getParameter("Id_Assunto")); 
		MandadoPrisaodt.setAssunto( request.getParameter("Assunto")); 
		MandadoPrisaodt.setDataValidade( request.getParameter("DataValidade")); 
		MandadoPrisaodt.setOrigem( request.getParameter("Origem")); 
		MandadoPrisaodt.setId_MandadoPrisaoOrigem( request.getParameter("Id_MandadoPrisaoOrigem")); 
		MandadoPrisaodt.setMandadoPrisaoOrigem( request.getParameter("MandadoPrisaoOrigem")); 
		MandadoPrisaodt.setId_Processo( request.getParameter("Id_Processo")); 
		MandadoPrisaodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		MandadoPrisaodt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		MandadoPrisaodt.setDigitoVerificador( request.getParameter("DigitoVerificador")); 
		MandadoPrisaodt.setAno( request.getParameter("Ano")); 
		MandadoPrisaodt.setDataNascimento( request.getParameter("DataNascimento")); 
		MandadoPrisaodt.setNomeMae( request.getParameter("NomeMae")); 
		MandadoPrisaodt.setNomePai( request.getParameter("NomePai")); 
		MandadoPrisaodt.setUfNaturalidade( request.getParameter("UfNaturalidade")); 
		MandadoPrisaodt.setSexo( request.getParameter("Sexo")); 
		MandadoPrisaodt.setCpf( request.getParameter("Cpf")); 
		MandadoPrisaodt.setNaturalidade( request.getParameter("Naturalidade")); 

		MandadoPrisaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoPrisaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoPrisaone.excluir(MandadoPrisaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("MandadoPrisao");
					descricao.add("MandadoPrisao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoPrisao");
					request.setAttribute("tempBuscaDescricao","MandadoPrisao");
					request.setAttribute("tempBuscaPrograma","MandadoPrisao");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");					
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				MandadoPrisaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoPrisaone.Verificar(MandadoPrisaodt); 
					if (Mensagem.length()==0){
						MandadoPrisaone.salvar(MandadoPrisaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (MandadoPrisaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("MandadoPrisaoStatus");
					descricao.add("MandadoPrisaoStatus");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoPrisaoStatus");
					request.setAttribute("tempBuscaDescricao","MandadoPrisaoStatus");
					request.setAttribute("tempBuscaPrograma","MandadoPrisaoStatus");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(MandadoPrisaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
					break;
				case (RegimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("RegimeExecucao");
					descricao.add("RegimeExecucao");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_RegimeExe");
					request.setAttribute("tempBuscaDescricao","RegimeExe");
					request.setAttribute("tempBuscaPrograma","RegimeExecucao");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(RegimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (PrisaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("PrisaoTipo");
					descricao.add("PrisaoTipo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PrisaoTipo");
					request.setAttribute("tempBuscaDescricao","PrisaoTipo");
					request.setAttribute("tempBuscaPrograma","PrisaoTipo");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(PrisaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("ProcessoParte");
					descricao.add("ProcessoParte");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoParte");
					request.setAttribute("tempBuscaDescricao","ProcessoParte");
					request.setAttribute("tempBuscaPrograma","ProcessoParte");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (AssuntoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Assunto");
					descricao.add("Assunto");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(AssuntoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (MandadoPrisaoOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("MandadoPrisaoOrigem");
					descricao.add("MandadoPrisaoOrigem");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoPrisaoOrigem");
					request.setAttribute("tempBuscaDescricao","MandadoPrisaoOrigem");
					request.setAttribute("tempBuscaPrograma","MandadoPrisaoOrigem");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(MandadoPrisaoOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Processo");
					descricao.add("Processo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Processo");
					request.setAttribute("tempBuscaDescricao","ProcessoTipo");
					request.setAttribute("tempBuscaPrograma","Processo");
					request.setAttribute("tempRetorno","MandadoPrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisao");
					stTemp = MandadoPrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoPrisao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoPrisaodt.getId()))){
						MandadoPrisaodt.limpar();
						MandadoPrisaodt = MandadoPrisaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoPrisaodt",MandadoPrisaodt );
		request.getSession().setAttribute("MandadoPrisaone",MandadoPrisaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
