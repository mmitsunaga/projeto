package br.gov.go.tj.projudi.ne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.bancos.GerenciaArquivo;
import br.gov.go.tj.utils.bancos.GerenciaArquivoCEF;

public class GerenciaArquivoBancoNe extends Negocio {

	private static final long serialVersionUID = -7507836433594086812L;
	private GerenciaArquivo gerenciaArquivo = null;
	private GuiaEmissaoNe guiaEmissaoNe;
	//Constantes
	public static final int SEM_SUCESSO = 0;
	public static final int SUCESSO = 1;
	
	
	/**
	 * Construtor default
	 */
	public GerenciaArquivoBancoNe() {
	}
	
	/**
	 * Construtor que descreve qual banco para a fábrica de gerencia de arquivo.
	 * @param String banco
	 * @throws ClassNotFoundException 
	 */
	public GerenciaArquivoBancoNe(Integer idBanco) throws ClassNotFoundException {
		switch(idBanco) {
			case BancoDt.CAIXA_ECONOMICA_FEDERAL : {
				gerenciaArquivo = new GerenciaArquivoCEF();
				break;
			}
		}
	}
	
	/**
	 * Método para ler conteúdo do arquivo.
	 * @param String conteudo
	 * @throws Exception
	 */
	public int lerArquivo(String conteudo) throws Exception {
		int retorno = SEM_SUCESSO;
		
		gerenciaArquivo.lerDados(conteudo);
		
		//Valida algumas listas para ver se está OK!
		if( gerenciaArquivo.getNumeroGuia().size() == gerenciaArquivo.getValorPago().size() && 
				gerenciaArquivo.getNumeroGuia().size() == gerenciaArquivo.getDataHoraRecebimento().size() ) {
			
			for(int i = 0; i < gerenciaArquivo.getNumeroGuia().size(); i++ ) {
				gerenciaArquivo.getNumeroGuia().get(i);
				String dataRecebimento = (String) gerenciaArquivo.getDataHoraRecebimento().get(i);
				
				dataRecebimento = dataRecebimento.substring(0,4) + "-" + dataRecebimento.substring(4,6) + "-" + dataRecebimento.substring(6);
				
				//guiaEmissaoNe.atualizarPagamento(numeroGuiaCompleto, GuiaStatusDt.PAGO, dataRecebimento);
			}
			
		}
				
		return retorno;
	}
	
	/**
	 * Método para atualizar as guias pagas de 15 em 15 minutos.
	 * @throws Exception
	 */
	public void lerArquivos15Minutos() throws Exception {
		BufferedReader in = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try{
			//Caminho dos arquivos dos bancos
			String caminhoArquivoBB 	= "";
			String caminhoArquivoITAU 	= "";
			String caminhoArquivoCEF 	= "";
			
			for( int i = 0; i < BancoDt.listaBancos.length; i++ ) {
				
				switch( BancoDt.listaBancos[i] ) {
				
					case BancoDt.CAIXA_ECONOMICA_FEDERAL : {
						gerenciaArquivo = new GerenciaArquivoCEF(obFabricaConexao);
						in = new BufferedReader(new FileReader( caminhoArquivoCEF ));
					}
				}
				
				String str = null;
				int dadosLidos = SEM_SUCESSO;
				
				if( in != null ) {
					if( guiaEmissaoNe == null ) {
						guiaEmissaoNe = new GuiaEmissaoNe();
					}
					
					while( in.ready() ) {
						str = in.readLine();
						
						//Faz a leitura de linha por linha extraindo os dados para as listas
						if( str != null ) {
							dadosLidos = gerenciaArquivo.lerDados(str);
						}
						
					}
					
					//Se os dados foram lidos, então pega os dados e gera as movimentações e atualizações
					if( dadosLidos == SUCESSO ) {
						//Atualiza as guias e Gera as movimentações
						for( int k = 0; k < gerenciaArquivo.getNumeroGuia().size(); k++ ) {
							String numeroGuiaCompleto = gerenciaArquivo.getNumeroGuia().get(k).toString();
							Integer idGuiaStatus = GuiaStatusDt.PAGO;
							Date dataPagamento = (Date) gerenciaArquivo.getDataHoraRecebimento().get(k);
							
							//Atualiza guia
							guiaEmissaoNe.atualizarPagamento( numeroGuiaCompleto, idGuiaStatus, dataPagamento);
							
						}
						
						//Zera listas
						gerenciaArquivo.limparListas();
					}
				}
				
				gerenciaArquivo = null;
			}
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para salvar na tabela de Arquivo os dados recebidos pelo arquivo do banco.
	 * @return boolean
	 * @throws Exception
	 */
	public boolean salvarLeituraArquivo(){
		boolean retorno = false;
						
		retorno = true;
		
		return retorno;
	}
}
