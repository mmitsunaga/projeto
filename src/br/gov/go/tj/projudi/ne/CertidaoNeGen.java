package br.gov.go.tj.projudi.ne;



import br.gov.go.tj.projudi.dt.CertidaoDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CertidaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class CertidaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5683231431178596868L;
	protected  CertidaoValidacaoDt obDados;


	public CertidaoNeGen() {

		obLog = new LogNe(); 

		obDados = new CertidaoValidacaoDt(); 

	}



//---------------------------------------------------------
	 public abstract String Verificar(CertidaoDt dados ); 


//---------------------------------------------------------

	public void excluir(CertidaoValidacaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("Certidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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
			try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public CertidaoValidacaoDt consultarId(String id_certidao ) throws Exception {

		CertidaoValidacaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_certidao ); 
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

}
