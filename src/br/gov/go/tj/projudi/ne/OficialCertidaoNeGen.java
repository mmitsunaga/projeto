package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.ps.OficialCertidaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class OficialCertidaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1264309225624432970L;
	protected  OficialCertidaoDt obDados;


	public OficialCertidaoNeGen() {

		obLog = new LogNe(); 

		obDados = new OficialCertidaoDt(); 

	}


	//---------------------------------------------------------
	public void salvar(OficialCertidaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(OficialCertidaoDt dados ); 


//---------------------------------------------------------

	public void excluir(OficialCertidaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

	public String consultarDescricaoJSON(String descricao, String id_Usuario, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, id_Usuario, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public OficialCertidaoDt consultarId(String id_oficialcertidao ) throws Exception {

		OficialCertidaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_oficialcertidao ); 
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

		OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception{
		List tempList=null;
		
		UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
		tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
		UsuarioServentiane = null;

		return tempList;
	}

}
