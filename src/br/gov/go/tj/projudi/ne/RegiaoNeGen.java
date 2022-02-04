package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.ps.RegiaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class RegiaoNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4239288752390090885L;	
	protected  RegiaoDt obDados;


	public RegiaoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new RegiaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(RegiaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neRegiaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Regiao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Regiao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(RegiaoDt dados ); 


//---------------------------------------------------------

	public void excluir(RegiaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Regiao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public RegiaoDt consultarId(String id_regiao ) throws Exception {

		RegiaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_Regiao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_regiao ); 
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
		//////System.out.println("..ne-ConsultaRegiao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Comarcane.getQuantidadePaginas();
		Comarcane = null;
		
		return tempList;
	}

}
