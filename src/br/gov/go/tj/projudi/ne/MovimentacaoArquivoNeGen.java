package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.ps.MovimentacaoArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class MovimentacaoArquivoNeGen extends Negocio {

	private static final long serialVersionUID = -8928437242681126007L;

	protected LogNe obLog;
	protected MovimentacaoArquivoDt obDados;
	protected String stUltimaConsulta = "%";
	protected long QuantidadePaginas = 0;

	public MovimentacaoArquivoNeGen() {
		
		obLog = new LogNe();

		obDados = new MovimentacaoArquivoDt();

	}

	// ---------------------------------------------------------
	public void salvar(MovimentacaoArquivoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// ////System.out.println("..neMovimentacaoArquivosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------
	public abstract String Verificar(MovimentacaoArquivoDt dados);

	// ---------------------------------------------------------

	public void excluir(MovimentacaoArquivoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------

	public MovimentacaoArquivoDt consultarId(String id_movimentacaoarquivo) throws Exception {

		MovimentacaoArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		// ////System.out.println("..ne-ConsultaId_MovimentacaoArquivo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_movimentacaoarquivo);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	// ---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	// ---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarDescricaoArquivo(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		ArquivoNe Arquivone = new ArquivoNe();
		tempList = Arquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Arquivone.getQuantidadePaginas();
		Arquivone = null;
		return tempList;
	}

	public List consultarDescricaoMovimentacao(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		MovimentacaoNe Movimentacaone = new MovimentacaoNe();
		tempList = Movimentacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Movimentacaone.getQuantidadePaginas();
		Movimentacaone = null;
		return tempList;
	}
}
