package br.gov.go.tj.projudi.temp;

import java.util.List;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class AtualizacaoNe {	
	protected LogNe obLog;
	protected AtualizacaoDt obDados;
	
	protected long QuantidadePaginas = 0;
	    
	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	public AtualizacaoNe() {		

		obLog = new LogNe();

		obDados = new AtualizacaoDt();
	}

	public List consultarArquivosProjudi(long id) throws Exception {

		List listObjetos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
			listObjetos = obPersistencia.consultarArquivosProjudi( id);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listObjetos;
	}

   public long consultarIdInicial() throws Exception {
        long loTemp;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
            loTemp = obPersistencia.consultarIdInicial();
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        
        return loTemp;
    }
	   
	public long consultarQtdTotalArquivos() throws Exception {
		long loTemp;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
			loTemp = obPersistencia.consultarQtdTotalArquivos();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return loTemp;
	}
	
	public List consultarCertificadosProjudi() throws Exception {

		List listObjetos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
			listObjetos = obPersistencia.consultarCertificadosProjudi();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listObjetos;
	}
	
	public long consultarQtdTotalCertificados() throws Exception {
		long loTemp = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
			loTemp = obPersistencia.consultarQtdTotalCertificados();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return loTemp;
	}

	public void migracaoConteudoArquivoP12(CertificadoDt certificadoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AtualizacaoPs obPersistencia = new AtualizacaoPs(obFabricaConexao.getConexao());
			obPersistencia.migracaoConteudoArquivoP12(certificadoDt);
		
		} finally{
				obFabricaConexao.fecharConexao();
		}
	}
}
