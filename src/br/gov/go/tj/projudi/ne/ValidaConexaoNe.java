package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ps.GuiaSPGPs;
import br.gov.go.tj.projudi.ps.PropriedadePs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Web;

public class ValidaConexaoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7636339844392759502L;
	
	public void valideConexaoOracleEscrita() throws Exception {
		FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PropriedadePs obPersistencia = new PropriedadePs(obFabricaConexao.getConexao());                
            obPersistencia.getPropriedades();
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	public void valideConexaoOracleDataGuard() throws Exception {
		FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            PropriedadePs obPersistencia = new PropriedadePs(obFabricaConexao.getConexao());                
            obPersistencia.getPropriedades();
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	public void valideConexaoAdabasConnx() throws Exception {
		FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
            GuiaSPGPs obPersistencia = new GuiaSPGPs(obFabricaConexao.getConexao());
            obPersistencia.consultarGuiaEmissaoSPG("123456", ComarcaDt.GOIANIA.trim());
            
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	public void valideConexaoAdabasWeb() throws Exception {
		CertidaoNe certidaoNe = new CertidaoNe();
		certidaoNe.getDtGuia("123456");
	}
	
	public void valideConexaoCephStorage() throws Exception {
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		String url = projudiConfiguration.getObjectStorageHost(); 
		if (!url.toLowerCase().contains("http://")) url = "http://" + url;
		Web.sendGetUrl(url);		
	}
}
