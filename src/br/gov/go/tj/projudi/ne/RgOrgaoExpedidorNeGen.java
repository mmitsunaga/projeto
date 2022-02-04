package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.ps.RgOrgaoExpedidorPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class RgOrgaoExpedidorNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7510297247717148543L;
		
	protected  RgOrgaoExpedidorDt obDados;
		 

	public RgOrgaoExpedidorNeGen() {
		

		obLog = new LogNe(); 

		obDados = new RgOrgaoExpedidorDt(); 

	}


//---------------------------------------------------------
	public void salvar(RgOrgaoExpedidorDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neRgOrgaoExpedidorsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("RgOrgaoExpedidor", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("RgOrgaoExpedidor", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(RgOrgaoExpedidorDt dados ); 


//---------------------------------------------------------

	public void excluir(RgOrgaoExpedidorDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("RgOrgaoExpedidor", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public RgOrgaoExpedidorDt consultarId(String id_rgorgaoexpedidor ) throws Exception {

		RgOrgaoExpedidorDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_RgOrgaoExpedidor" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_rgorgaoexpedidor ); 
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
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaRgOrgaoExpedidor" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoEstado(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		EstadoNe Estadone = new EstadoNe(); 
		tempList = Estadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Estadone.getQuantidadePaginas();
		Estadone = null;
		
		return tempList;
	}

}
