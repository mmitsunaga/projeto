package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaEmissaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GuiaEmissaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -594286030152118916L;
	
	protected GuiaEmissaoDt obDados;


	public GuiaEmissaoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new GuiaEmissaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(GuiaEmissaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GuiaEmissao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("GuiaEmissao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GuiaEmissaoDt dados ); 


//---------------------------------------------------------

	public void excluir(GuiaEmissaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("GuiaEmissao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GuiaEmissaoDt consultarId(String id_guiaemissao ) throws Exception {

		GuiaEmissaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_guiaemissao ); 
			obDados.copiar(dtRetorno);
		} finally {
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

			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoGuiaModelo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GuiaModeloNe GuiaModelone = new GuiaModeloNe(); 
		tempList = GuiaModelone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GuiaModelone.getQuantidadePaginas();
		GuiaModelone = null;
		return tempList;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoNe Processone = new ProcessoNe(); 
		tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Processone.getQuantidadePaginas();
		Processone = null;
		return tempList;
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ServentiaNe Serventiane = new ServentiaNe(); 
		tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;
		return tempList;
	}

	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
		tempList = ProcessoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoTipone.getQuantidadePaginas();
		ProcessoTipone = null;
		return tempList;
	}
}