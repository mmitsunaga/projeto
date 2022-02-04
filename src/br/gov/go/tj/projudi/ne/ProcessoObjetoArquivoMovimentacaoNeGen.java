package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt;
import br.gov.go.tj.projudi.ps.ProcessoObjetoArquivoMovimentacaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoObjetoArquivoMovimentacaoNeGen extends Negocio{


	protected  ProcessoObjetoArquivoMovimentacaoDt obDados;


	public ProcessoObjetoArquivoMovimentacaoNeGen() {

		obLog = new LogNe(); 

		obDados = new ProcessoObjetoArquivoMovimentacaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoObjetoArquivoMovimentacaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoObjetoArquivoMovimentacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoObjetoArquivoMovimentacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoObjetoArquivoMovimentacaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoObjetoArquivoMovimentacaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ProcessoObjetoArquivoMovimentacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ProcessoObjetoArquivoMovimentacaoDt consultarId(String id_processoobjetoarquivomovimentacao ) throws Exception {

		ProcessoObjetoArquivoMovimentacaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{ 
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_processoobjetoarquivomovimentacao ); 
			obDados.copiar(dtRetorno);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
		List tempList=null;
		FabricaConexao obFabricaConexao = null;


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProcessoObjetoArquivo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoObjetoArquivoNe ProcessoObjetoArquivone = new ProcessoObjetoArquivoNe(); 
			tempList = ProcessoObjetoArquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoObjetoArquivone.getQuantidadePaginas();
			ProcessoObjetoArquivone = null;
		return tempList;
	}

	}
