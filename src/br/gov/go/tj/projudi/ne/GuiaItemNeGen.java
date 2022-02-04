package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaItemPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GuiaItemNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -498456612263660599L;
	
	protected  GuiaItemDt obDados;

	public GuiaItemNeGen() {
		
		obLog = new LogNe(); 

		obDados = new GuiaItemDt(); 

	}


//---------------------------------------------------------
	public void salvar(GuiaItemDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GuiaItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("GuiaItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GuiaItemDt dados ); 


//---------------------------------------------------------

	public void excluir(GuiaItemDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("GuiaItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GuiaItemDt consultarId(String id_guiaitem ) throws Exception {

		GuiaItemDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		try{
			 obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_guiaitem ); 
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

			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoGuiaEmissao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GuiaEmissaoNe GuiaEmissaone = new GuiaEmissaoNe(); 
		tempList = GuiaEmissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GuiaEmissaone.getQuantidadePaginas();
		GuiaEmissaone = null;
		return tempList;
	}

	public List consultarDescricaoCusta(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		CustaNe Custane = new CustaNe(); 
		tempList = Custane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Custane.getQuantidadePaginas();
		Custane = null;
		return tempList;
	}

}
