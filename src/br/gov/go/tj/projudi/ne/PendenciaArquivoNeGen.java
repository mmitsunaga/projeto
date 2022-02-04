package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.ps.PendenciaArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PendenciaArquivoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1192246933815373435L;	
	protected  PendenciaArquivoDt obDados;


	public PendenciaArquivoNeGen() {	

		obLog = new LogNe(); 

		obDados = new PendenciaArquivoDt(); 

	}


//---------------------------------------------------------
	public void salvar(PendenciaArquivoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..nePendenciaArquivosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PendenciaArquivoDt dados ); 


//---------------------------------------------------------

	public void excluir(PendenciaArquivoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PendenciaArquivoDt consultarId(String id_pendenciaarquivo ) throws Exception {

		PendenciaArquivoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_PendenciaArquivo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_pendenciaarquivo ); 
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
		//////System.out.println("..ne-ConsultaPendenciaArquivo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoArquivo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ArquivoNe Arquivone = new ArquivoNe(); 
		tempList = Arquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Arquivone.getQuantidadePaginas();
		Arquivone = null;
		
		return tempList;
	}

	public List consultarDescricaoPendencia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			PendenciaNe Pendenciane = new PendenciaNe(); 
			tempList = Pendenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Pendenciane.getQuantidadePaginas();
			Pendenciane = null;
		
		return tempList;
	}

	}
