package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ValidaConexaoNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ValidaConexaoCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6019024458024810447L;

	@Override
	public int Permissao() {
		return 862; //Busca processo pública
	}
	
	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
	private static String ORACLE_ESCRITA = "ORACLE_ESCRITA";
	private static String ORACLE_DATAGUARD = "ORACLE_DATAGUARD";
	private static String ADABAS_CONNX = "ADABAS_CONNX";
	private static String ADABAS_WEB = "ADABAS_WEB";
	private static String CEPH_STORAGE = "CEPH_STORAGE";

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		ValidaConexaoNe validaConexaoNe = new ValidaConexaoNe();
		
		try {
			if (paginaatual == Configuracao.Novo) {
				if (posicaopaginaatual == null) {
					RetornaErro(response, "Parametro PosicaoPaginaAtual ausente. Valores validos: ORACLE_ESCRITA ORACLE_DATAGUARD ADABAS_CONNX ADABAS_WEB CEPH_STORAGE.");
					return;
				} else if (posicaopaginaatual.trim().equalsIgnoreCase(ORACLE_ESCRITA)) {
					validaConexaoNe.valideConexaoOracleEscrita();
				} else if (posicaopaginaatual.trim().equalsIgnoreCase(ORACLE_DATAGUARD)) {
					validaConexaoNe.valideConexaoOracleDataGuard();
				} else if (posicaopaginaatual.trim().equalsIgnoreCase(ADABAS_CONNX)) {
					validaConexaoNe.valideConexaoAdabasConnx();
				} else if (posicaopaginaatual.trim().equalsIgnoreCase(ADABAS_WEB)) {
					validaConexaoNe.valideConexaoAdabasWeb();
				} else if (posicaopaginaatual.trim().equalsIgnoreCase(CEPH_STORAGE)) {
					validaConexaoNe.valideConexaoCephStorage();
				} else {
					RetornaErro(response, "Parametro PosicaoPaginaAtual invalido. Valores validos: ORACLE_ESCRITA ORACLE_DATAGUARD ADABAS_CONNX ADABAS_WEB CEPH_STORAGE.");
					return;
				}
			} else {
				RetornaErro(response, "Parametro PaginaAtual invalido. Valor valido: 4.");
				return;
			}
		} catch (Exception ex) {
			String conteudoPrimeiraExcecao = Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
			//TenteGravarLogErro(request, conteudoPrimeiraExcecao);
			RetornaErro(response, "Erro ao executar o teste. " + conteudoPrimeiraExcecao);			
		}
		
		super.redirecione("/WEB-INF/jsptjgo/ConexaoOK.jsp", request, response);		
	}
	
	private void RetornaErro(HttpServletResponse response, String mensagem) throws Exception {
		response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, mensagem);
	}
	
	private void TenteGravarLogErro(HttpServletRequest request, String conteudoPrimeiraExcecao) {
		try 
		{
			new LogNe().salvarErro(new LogDt(this.getServletName(), "ValidaConexao", UsuarioDt.SistemaProjudi, getIpCliente(request), String.valueOf(LogTipoDt.Erro), "",  conteudoPrimeiraExcecao));			
		} catch (Exception ex) {}
	}
}
