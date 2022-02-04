package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.ps.ServentiaRelacionadaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaRelacionadaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -9013533181140864666L;
	
	protected  ServentiaRelacionadaDt obDados;		

	public ServentiaRelacionadaNeGen() {		

		obLog = new LogNe(); 

		obDados = new ServentiaRelacionadaDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaRelacionadaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neServentiaRelacionadasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaRelacionada", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaRelacionada", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaRelacionadaDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaRelacionadaDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaRelacionada", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaRelacionadaDt consultarId(String id_serventiarelacionada ) throws Exception {

		ServentiaRelacionadaDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_ServentiaRelacionada" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventiarelacionada ); 
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
		//////System.out.println("..ne-ConsultaServentiaRelacionada" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
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

}
