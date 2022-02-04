package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoArquivoDt;
import br.gov.go.tj.projudi.ps.MandadoPrisaoArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public class MandadoPrisaoArquivoNe extends Negocio{


	private static final long serialVersionUID = -6632924814143917608L;
	/**
	 * 
	 */
	protected  MandadoPrisaoArquivoDt obDados;


	public MandadoPrisaoArquivoNe() {
		
		obLog = new LogNe(); 

		obDados = new MandadoPrisaoArquivoDt(); 

	}


//---------------------------------------------------------
	public void salvar(MandadoPrisaoArquivoDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoArquivoPs obPersistencia = new MandadoPrisaoArquivoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MandadoPrisaoArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MandadoPrisaoArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void salvar(MandadoPrisaoArquivoDt dados, FabricaConexao obFabricaConexao, LogDt obLogDt) throws Exception{

		
		MandadoPrisaoArquivoPs obPersistencia = new MandadoPrisaoArquivoPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("MandadoPrisaoArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("MandadoPrisaoArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	public void excluir(MandadoPrisaoArquivoDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoArquivoPs obPersistencia = new MandadoPrisaoArquivoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MandadoPrisaoArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


	public MandadoPrisaoArquivoDt consultarId(String idMandadoPrisaoArquivo ) throws Exception{

		MandadoPrisaoArquivoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoArquivoPs obPersistencia = new MandadoPrisaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(idMandadoPrisaoArquivo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarIdArquivo(String idMandadoPrisao) throws Exception{
		String stRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoArquivoPs obPersistencia = new MandadoPrisaoArquivoPs(obFabricaConexao.getConexao());
			stRetorno= obPersistencia.consultarIdArquivo(idMandadoPrisao); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}



}
