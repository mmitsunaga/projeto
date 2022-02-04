package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SinalDt;
import br.gov.go.tj.projudi.ps.SinalPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class SinalNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1226738834716264056L;	
	protected  SinalDt obDados;


	public SinalNeGen() {		

		obLog = new LogNe(); 

		obDados = new SinalDt(); 

	}


//---------------------------------------------------------
	public void salvar(SinalDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neSinalsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SinalPs obPersistencia = new SinalPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Sinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Sinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(SinalDt dados ); 


//---------------------------------------------------------

	public void excluir(SinalDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SinalPs obPersistencia = new SinalPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Sinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public SinalDt consultarId(String id_sinal ) throws Exception {

		SinalDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_Sinal" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SinalPs obPersistencia = new SinalPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_sinal ); 
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
		//////System.out.println("..ne-ConsultaSinal" ); 
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				SinalPs obPersistencia = new SinalPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				if (tempList != null){
					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
					tempList.remove(tempList.size()-1);	
				} else QuantidadePaginas = 0;
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
