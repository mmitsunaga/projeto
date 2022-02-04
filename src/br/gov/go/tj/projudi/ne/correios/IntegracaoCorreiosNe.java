package br.gov.go.tj.projudi.ne.correios;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import br.com.correios.webservice.resource.Eventos;
import br.com.correios.webservice.resource.Objeto;
import br.com.correios.webservice.resource.ServiceProxy;
import br.com.correios.webservice.resource.Sroxml;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.WebServiceException;

public class IntegracaoCorreiosNe implements Serializable {

	private static final long serialVersionUID = -6826942698954798737L;
	
	private final Logger LOGGER = Logger.getLogger(IntegracaoCorreiosNe.class);
	
	private static IntegracaoCorreiosNe SINGLETON;
	
	public static IntegracaoCorreiosNe get() throws Exception {
		if (SINGLETON == null) {
			SINGLETON = new IntegracaoCorreiosNe();
		}
		return SINGLETON;
	}
	
	private final String USUARIO_SERVICO;
	private final String SENHA_SERVICO;
	private final ServiceProxy webserviceCorreios;
	private final String TIPO_SERVIDOR_SRO = "L";
	
	private enum TipoResultadoServidorSRO {
		TODOS_EVENTOS_OBJETO("T"), ULTIMO_EVENTO_OBJETO("U"), PRIMEIRO_EVENTO_OBJETO("P");		 
		private String valor;
		private TipoResultadoServidorSRO(String valor) {
			this.valor = valor;
		}		
		public String getValor() {
			return valor;
		}
	}
	
	private enum LinguaServidorSRO {
		PORTUGUES("101"), INGLES("102"), ESPANHOL("103");		 
		private String valor;
		private LinguaServidorSRO(String valor) {
			this.valor = valor;
		}		
		public String getValor() {
			return valor;
		}
	}

	private IntegracaoCorreiosNe() throws Exception {
		
		configuraAceitacaoCertificadoCorreios();
		
		ProjudiPropriedades prop = ProjudiPropriedades.getInstance();
		
		USUARIO_SERVICO = prop.getUsuarioCorreios();
		SENHA_SERVICO = prop.getSenhaCorreios();
		
		webserviceCorreios = new ServiceProxy(prop.getUrlCorreios());
		
		/*BindingProvider bindProv = (BindingProvider) webserviceCorreios;
		List<Handler> handlers = bindProv.getBinding().getHandlerChain();
		handlers.add(new ImprimeEnvelopesSoapEmDebug());
		bindProv.getBinding().setHandlerChain(handlers);*/
	}
	
	private void configuraAceitacaoCertificadoCorreios() throws Exception {
		SSLContext contexto = SSLContext.getInstance("TLS");
		X509TrustManager[] trustManagers = new X509TrustManager[] { new CorreiosTrustManager() };
		contexto.init(null, trustManagers, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(contexto.getSocketFactory());
	}
	
	public List<ObjetoCorreios> buscaTodosEventos(String objeto) throws WebServiceException {
		List<ObjetoCorreios> objetos = new ArrayList<ObjetoCorreios>();
		try {
			Sroxml retorno = webserviceCorreios.buscaEventos(USUARIO_SERVICO, 
															 SENHA_SERVICO, 
									                         TIPO_SERVIDOR_SRO, 
									                         TipoResultadoServidorSRO.TODOS_EVENTOS_OBJETO.getValor(), 
									                         LinguaServidorSRO.PORTUGUES.getValor(), 
									                         objeto);
			
			for (Objeto objetoCorreios :  retorno.getObjeto()) {
				ObjetoCorreios objetoProjudi = new ObjetoCorreios();
				objetoProjudi.setNumero(objetoCorreios.getNumero());
				if (ValidacaoUtil.isNaoVazio(objetoCorreios.getErro())){
					objetoProjudi.setErro(objetoCorreios.getErro());
				} else {
					for (Eventos eventoCorreios :  objetoCorreios.getEvento()) {
						EventoCorreios eventoProjudi = new EventoCorreios();
						eventoProjudi.setTipo(eventoCorreios.getTipo());
						eventoProjudi.setStatus(eventoCorreios.getStatus());
						eventoProjudi.setDescricao(eventoCorreios.getDescricao());
						eventoProjudi.setDetalhe(eventoCorreios.getDetalhe());
						eventoProjudi.setData(eventoCorreios.getData());
						eventoProjudi.setHora(eventoCorreios.getHora());
						eventoProjudi.setLocal(eventoCorreios.getLocal());
						eventoProjudi.setCidade(eventoCorreios.getCidade());
						eventoProjudi.setUf(eventoCorreios.getUf());
						objetoProjudi.adicioneEvento(eventoProjudi);
					}
				}
				objetos.add(objetoProjudi);
			}			
			
			return objetos;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new WebServiceException(String.format("Erro ao consultar todos os eventos do objeto %s.",objeto), e);
		}		
	}
	
}
