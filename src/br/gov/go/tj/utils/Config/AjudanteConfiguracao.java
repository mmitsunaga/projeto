package br.gov.go.tj.utils.Config;

import java.util.HashMap;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaPingdom;

/**
 * 
 * Classe:     AjudanteConfiguracao.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Abstrair o acesso às configurações do proxy e do PingDom   
 *             
 */
public class AjudanteConfiguracao {
	
	private static ConfigProxy configProxy = null;
	private static HashMap<EnumSistemaPingdom, ConfigPingDom> configPingdom = new HashMap<EnumSistemaPingdom, ConfigPingDom>();
    private static Logger logger = Logger.getLogger(AjudanteConfiguracao.class);
    
    public static void setConfigProxy(ConfigProxy config) {
    	AjudanteConfiguracao.configProxy = config;
        logger.info("Set ConfigProxy OK");
    }

    public static synchronized ConfigProxy getConfigProxy() throws Exception{ 			
      		return AjudanteConfiguracao.configProxy;
    }
      
    public static void setConfigPingdom(EnumSistemaPingdom sistema, ConfigPingDom config) {
      	AjudanteConfiguracao.configPingdom.put(sistema, config);
          logger.info("Set ConfigPingdom OK");
    }

    public static synchronized ConfigPingDom getConfigPingDom(EnumSistemaPingdom sistema) throws Exception{ 			
        return AjudanteConfiguracao.configPingdom.get(sistema);
    }
}