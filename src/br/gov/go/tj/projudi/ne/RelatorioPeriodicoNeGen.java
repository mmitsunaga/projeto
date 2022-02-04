package br.gov.go.tj.projudi.ne;

import java.io.Serializable;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.projudi.ps.RelatorioPeriodicoPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class RelatorioPeriodicoNeGen extends Negocio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5589874242138823997L;
	protected RelatorioPeriodicoDt obDados;

	public RelatorioPeriodicoNeGen() {		
		obLog = new LogNe();
		obDados = new RelatorioPeriodicoDt();
	}

	public void salvar(RelatorioPeriodicoDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RelatorioPeriodicoPs obPersistencia = new  RelatorioPeriodicoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length() == 0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("RelatorioPeriodico", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("RelatorioPeriodico", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public abstract String Verificar(RelatorioPeriodicoDt dados);

	public void excluir(RelatorioPeriodicoDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			obFabricaConexao.iniciarTransacao();
			RelatorioPeriodicoPs obPersistencia = new  RelatorioPeriodicoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("RelatorioPeriodico", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());
			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public RelatorioPeriodicoDt consultarId(String id_relatorioperiodico) throws Exception {
		RelatorioPeriodicoDt dtRetorno = null;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioPeriodicoPs obPersistencia = new  RelatorioPeriodicoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_relatorioperiodico);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioPeriodicoPs obPersistencia = new  RelatorioPeriodicoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
}
