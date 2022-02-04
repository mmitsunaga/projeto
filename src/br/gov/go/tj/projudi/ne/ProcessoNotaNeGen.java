package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.projudi.ps.ProcessoNotaPs;

 import br.gov.go.tj.projudi.ne.UsuarioServentiaNe;
 import br.gov.go.tj.projudi.ne.ProcessoNe;
//---------------------------------------------------------
public abstract class ProcessoNotaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4650232587118321313L;
	protected  ProcessoNotaDt obDados;


	public ProcessoNotaNeGen() {

		obLog = new LogNe(); 

		obDados = new ProcessoNotaDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoNotaDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoNota",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoNota",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			
			throw new Exception(" <{Não foi possível salvar a anotação.}> ProcessoNotaNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoNotaDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoNotaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ProcessoNota",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
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
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ProcessoNotaDt consultarId(String id_processonota ) throws Exception {

		ProcessoNotaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_processonota ); 
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

			ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		return tempList;
	}

	public String consultarDescricaoUsuarioServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoNe Processone = new ProcessoNe(); 
			tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Processone.getQuantidadePaginas();
			Processone = null;
		return tempList;
	}

	public String consultarDescricaoProcessoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ProcessoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
