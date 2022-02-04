package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.UsuarioAfastamentoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.UsuarioAfastamentoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioAfastamentoNe extends UsuarioAfastamentoNeGen {

	private static final long serialVersionUID = -8082686745213464336L;

	public String Verificar(UsuarioAfastamentoDt dados) {

		String stRetorno = "";

		if (dados.getId_Usuario().equalsIgnoreCase(""))
			stRetorno += "O Campo Usuario é obrigatório.";
		if (dados.getId_Afastamento().equalsIgnoreCase(""))
			stRetorno += "O Campo Afastamento é obrigatório.";

		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioAfastamentoPs obPersistencia = new  UsuarioAfastamentoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoUsuarioJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		stTemp = Usuarione.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	public String consultarDescricaoAfastamentoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		AfastamentoNe afastamentoNe = new AfastamentoNe();
		stTemp = afastamentoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * JOB - Registra o afastamento dos oficiais que não tiverem cumprido o prazo de algum mandado.
	 * Modifica o status da escala dos oficiais para "inativo" e inclui um registro na usu_serv_afastamento
	 * identificando o motivo da suspensão.
	 * @throws Exception
	 * @author hrrosa
	 */
	public void afastarOficiaisAtrasados() throws Exception {
		// HELLENO
		List<MandadoJudicialDt> listaMandadosAtrasados = null;
		
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			UsuarioAfastamentoPs usuAfastamentoPs = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());
			
			MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
			ServentiaCargoEscalaNe servCargoEscNe = new ServentiaCargoEscalaNe();	
			String mandJudExpirados = "";			
			listaMandadosAtrasados = mandadoJudicialNe.consultarMandadosAtrasadosExecucaoAutomatica(obFabricaConexao);

			// A lista virá ordenada pelo id_serv do oficial. Portanto, percorre a lista acumulando os números de mandados
			// que causaram a suspensão. Quando muda o id do oficial, suspende e começa a acumular a lista para o novo
			// oficial em questão.
		
			if(listaMandadosAtrasados != null) {				
				String idUsuServ1Temp = listaMandadosAtrasados.get(0).getId_UsuarioServentia_1();
				for(MandadoJudicialDt mandJudDt: listaMandadosAtrasados){			
					if (idUsuServ1Temp.equalsIgnoreCase(mandJudDt.getId_UsuarioServentia_1())) {	
						 mandJudExpirados += mandJudDt.getId() + ";";	
					} else {
						servCargoEscNe.desativaServCargoTodasEscalas(idUsuServ1Temp, obFabricaConexao);
						usuAfastamentoPs.registrarUsuarioAfastamento(idUsuServ1Temp, UsuarioDt.SistemaProjudi, mandJudExpirados);
						obLog.salvar(new LogDt("UsuarioAfastamento", idUsuServ1Temp, UsuarioServentiaDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.OficialAfastado), "", "Oficial id_usu_serv="+idUsuServ1Temp+" afastado. Mandados expirados id: " + mandJudExpirados), obFabricaConexao);
						mandJudExpirados = mandJudDt.getId() + ";";
						idUsuServ1Temp = mandJudDt.getId_UsuarioServentia_1();					
					}
				}
				servCargoEscNe.desativaServCargoTodasEscalas(idUsuServ1Temp, obFabricaConexao);
				usuAfastamentoPs.registrarUsuarioAfastamento(idUsuServ1Temp, UsuarioDt.SistemaProjudi, mandJudExpirados);
				obLog.salvar(new LogDt("UsuarioAfastamento", idUsuServ1Temp, UsuarioServentiaDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.OficialAfastado), "", "Oficial id_usu_serv="+idUsuServ1Temp+" afastado. Mandados expirados id: " + mandJudExpirados), obFabricaConexao);
			}			
			obFabricaConexao.finalizarTransacao();
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	public void retornaOficiaisSuspensos() throws Exception {

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			obFabricaConexao.iniciarTransacao();

			List<MandadoJudicialDt> listaOficiais = null;

			MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();

			listaOficiais = mandadoJudicialNe.consultaOficiaisParaRetornoDeSuspensao(obFabricaConexao);

			if (!listaOficiais.isEmpty()) {

				ServentiaCargoEscalaNe servCargoEscalaNe = new ServentiaCargoEscalaNe();

				for (int i = 0; i < listaOficiais.size(); i++) {
					
					String idUsuServ1Temp = listaOficiais.get(i).getId_UsuarioServentia_1();

					servCargoEscalaNe.ativaServCargoTodasEscalas(listaOficiais.get(i).getId_UsuarioServentia_1(),
							obFabricaConexao);

					UsuarioAfastamentoPs usuarioAfastamentoPs = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());

					usuarioAfastamentoPs.registrarFinalUsuarioAfastamento(
							listaOficiais.get(i).getId_UsuarioServentia_1(), UsuarioDt.SistemaProjudi);
					
					obLog.salvar(new LogDt("UsuarioAfastamento", idUsuServ1Temp, UsuarioServentiaDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.RetornarOficialAfastado), "", "Oficial id_usu_serv="+idUsuServ1Temp+" retornado."), obFabricaConexao);
					
				}
			}
			obFabricaConexao.finalizarTransacao();
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}
	
	private void graveErroExecucaoAutomatica(Exception exExecucaoAutomatica){
		try{
			obLog.salvar(new LogDt("AudienciaPublicada", "", UsuarioServentiaDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoExcecao(exExecucaoAutomatica)));
	    }catch(Exception e){
			exExecucaoAutomatica.printStackTrace();
			e.printStackTrace();
		}
	}
	
	private String obtenhaConteudoExcecao(Exception ex){
    	try{
    		return Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage();
    	}    	
    }
}
