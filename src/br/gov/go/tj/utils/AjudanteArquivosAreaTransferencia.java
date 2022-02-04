package br.gov.go.tj.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;

public class AjudanteArquivosAreaTransferencia {
	
	public static String CHAVE_SESSAO_LISTA_ARQUIVOS = "ListaArquivosAreaTransferencia";
	
	public static Map getListaArquivosAreaTransferencia(HttpServletRequest request)
	{
		Map listaSessaoArquivosAreaTransferencia = (Map) request.getSession().getAttribute(CHAVE_SESSAO_LISTA_ARQUIVOS);					 
		if (listaSessaoArquivosAreaTransferencia == null) listaSessaoArquivosAreaTransferencia = new LinkedHashMap();	
		return listaSessaoArquivosAreaTransferencia;
	}
	
	public static void setListaArquivosAreaTransferencia(HttpServletRequest request, Map listaSessaoArquivosAreaTransferencia)
	{
		request.getSession().setAttribute(CHAVE_SESSAO_LISTA_ARQUIVOS, listaSessaoArquivosAreaTransferencia);
	}
	
	public static boolean limparAreaTransferencia(HttpServletRequest request){
		boolean retorno = false;
		
		Map listaArquivos =	getListaArquivosAreaTransferencia(request);
		if (listaArquivos.size() > 0)
		{
			listaArquivos.clear();
			setListaArquivosAreaTransferencia(request, null);
			retorno = true;
		}		
		return retorno;
	}
	
	public static ArquivoDt obtenhaArquivoParaTransferencia(String Id_Arquivo) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		return arquivoNe.consultarId(Id_Arquivo);
	}	
	
}
