package br.gov.go.tj.projudi.types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PendenciaStatusTipo {
	AGUARDANDO_PARECER_CODIGO_TEMP(-1),                   
	AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP(-2),
	AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP(-3),    
	AGUARDANDO_PARECER_DIARIO_ELETRONICO_CODIGO_TEMP(-4), 
	ID_EM_ANDAMENTO(1),                                   
	ID_CUMPRIDA(2),                                       
	ID_NAO_CUMPRIDA(3),                                   
	ID_CUMPRIDA_PARCIALMENTE(4),                          
	ID_CANCELADA(5),                                      
	ID_ENCAMINHADA(6),                                    
	ID_CORRECAO(7),                                       
	ID_AGUARDANDO_RETORNO(8),                             
	ID_AGUARDANDO_VISTO(9),                               
	ID_AGUARDANDO_CUMPRIMENTO(10),                        
	ID_CUMPRIMENTO_AGUARDANDO_VISTO(11),                  
	ID_PRE_ANALISADA(12),                                 
	ID_DESCARTADA(13),                                    
	ID_AGUARDANDO_ENVIO_CORREIO(14),                        
	ID_AGUARDANDO_CONFIRMACAO_CORREIO(15),                  
	ID_RECEBIDO_CORREIO(16);                                

	private int status;

	private PendenciaStatusTipo(int status) {
		this.status = status;
	}

	public int getValue() {
		return status;
	}

	public static List<Integer> getAllValues() {
		return Stream.of(PendenciaStatusTipo.values()).map(PendenciaStatusTipo::getValue).collect(Collectors.toList());
	}
}