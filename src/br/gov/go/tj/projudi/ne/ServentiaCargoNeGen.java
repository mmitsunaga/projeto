package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ps.CidadePs;
import br.gov.go.tj.projudi.ps.ServentiaCargoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaCargoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6327222278796953857L;	
	protected  ServentiaCargoDt obDados;


	public ServentiaCargoNeGen() {		

		obLog = new LogNe(); 

		obDados = new ServentiaCargoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaCargoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neServentiaCargosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaCargoDt dados ) throws Exception; 


//---------------------------------------------------------

	public void excluir(ServentiaCargoDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaCargoDt consultarId(String id_serventiacargo ) throws Exception {

		ServentiaCargoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_ServentiaCargo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventiacargo ); 
			if (obDados != null)
				obDados.copiar(dtRetorno); 
			else 
				obDados = new ServentiaCargoDt(); 
		
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
		//////System.out.println("..ne-ConsultaServentiaCargo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoCargoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CargoTipoNe CargoTipone = new CargoTipoNe(); 
			tempList = CargoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = CargoTipone.getQuantidadePaginas();
			CargoTipone = null;
		
		return tempList;
	}

	public List consultarDescricaoUsuarioServentiaGrupo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			UsuarioServentiaGrupoNe UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe(); 
			tempList = UsuarioServentiaGrupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiaGrupone.getQuantidadePaginas();
			UsuarioServentiaGrupone = null;
		
		return tempList;
	}
	

}
