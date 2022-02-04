package br.gov.go.tj.projudi.ne;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.indexacao.Hit;
import br.gov.go.tj.projudi.dt.indexacao.ResponseHits;
import br.gov.go.tj.projudi.ps.UsuarioPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.AnalisadorPadrao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchNe extends Negocio {

	private static final long serialVersionUID = 5329759004627769953L;

	private String SEARCH_PATH = "/_search";
		
	public ElasticSearchNe() {
		obLog = new LogNe();
	}
	
	/**
	 * Faz a consulta de uma palavra no campo texto, usando o método SimpleQueryString do Elasticsearch.
	 * @param texto palavra que está sendo consultada
	 * @param limit número máximo de itens na resposta
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public ResponseHits consultarPublicacoesPorTexto(String texto, int limit, int offset) throws Exception {
		texto = AnalisadorPadrao.avaliar(texto);
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(getHostPortPath() + SEARCH_PATH + "?size=" + limit + "&from=" + offset);
	    httpPost.setHeader("content-type", "application/json");	    
	    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
	    httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));	    
	    httpPost.setEntity(new StringEntity(getJsonSimpleQueryString(texto), "UTF-8"));
	    CloseableHttpResponse response = client.execute(httpPost);
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.readValue(response.getEntity().getContent(), ResponseHits.class);
	}
	
	/**
	 * Faz a consulta de um documento através de campos de filtro, usando o método BooleanQuery do Elasticsearch
	 * @param params - mapa com a chave (número do campo) e o valor do filtro
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public ResponseHits consultarPublicacoesPorCamposEspecificos(Map<String, String> params, int limit, int offset) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(getHostPortPath() + SEARCH_PATH + "?size=" + limit + "&from=" + offset);
	    httpPost.setHeader("content-type", "application/json");
	    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
	    httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
	    httpPost.setEntity(new StringEntity(getJsonBoolQueryString(params), "UTF-8"));
	    CloseableHttpResponse response = client.execute(httpPost);
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.readValue(response.getEntity().getContent(), ResponseHits.class);
	}
	
	/**
	 * Faz a consulta de um documento através de campos de filtro, usando o método BooleanQuery do Elasticsearch
	 * @param params
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public ResponseHits consultarJurisprudenciasPorCamposEspecificos(Map<String, String> params, int limit, int offset) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(getHostPortPath() + SEARCH_PATH + "?size=" + limit + "&from=" + offset);
	    httpPost.setHeader("content-type", "application/json");
	    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
	    httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
	    httpPost.setEntity(new StringEntity(getJsonBoolQueryStringAndExists(params), "UTF-8"));
	    CloseableHttpResponse response = client.execute(httpPost);
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.readValue(response.getEntity().getContent(), ResponseHits.class);
	}
	
	/**
	 * Faz a consulta por documento usando o campo id_arq e numero do processo
	 * @param id
	 * @param numeroProcesso
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public ResponseHits consultarPublicacoesPorIdArquivoENumeroProcesso (String id, String numeroProcesso) throws ClientProtocolException, IOException {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(getHostPortPath() + SEARCH_PATH);
		    httpPost.setHeader("content-type", "application/json");
		    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
		    httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		    httpPost.setEntity(new StringEntity(getJsonQueryPorIdArquivoENumeroProcesso(id, numeroProcesso), "UTF-8"));
		    response = client.execute(httpPost);
		    ObjectMapper objectMapper = new ObjectMapper();
		    return objectMapper.readValue(response.getEntity().getContent(), ResponseHits.class);		    
		} catch (ClientProtocolException cpex){
			throw new ClientProtocolException(cpex);
		} catch (IOException ioex){
			throw new IOException(ioex);
		} finally {
			if (response != null ) response.close();
			if (client != null) client.close();
		}		
	}
	
	/**
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 */
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarUsuarioServentiaGrupoMagistrados(tempNomeBusca, posicaoPaginaAtual);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Salva o arquivo indexado no Elastisearch, onde é enviado um JSON com os dados.
	 * @param json
	 * @return Retorna o código do HTTP status
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int salvarArquivoIndexado(JSONObject json) throws ClientProtocolException, IOException {
		CloseableHttpClient client =  HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(getHostPortPath());
		httpPost.setHeader("Content-type", "application/json");
	    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
	    httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
	    httpPost.setEntity(new StringEntity(json.toString(), StandardCharsets.UTF_8));
	    CloseableHttpResponse response = client.execute(httpPost);
	    client.close();
	    return response.getStatusLine().getStatusCode();
	}
	
	/**
	 * Excluir um arquivo no elasticsearch de acordo com o ID do arquivo e número do processo
	 * @param id - identificador do arquivo (id_arq)
	 */
	public void excluirArquivoPublicado(String id){
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		if (ValidacaoUtil.isNaoVazio(id)){
			try {
				Map<String, String> param = new HashMap<>();
				param.put("id_arq", id);
				ResponseHits hits = this.consultarPublicacoesPorCamposEspecificos(param, 10, 0);
				if (ValidacaoUtil.isNaoNulo(hits) && hits.getHits().getTotal() > 0){
					client = HttpClients.createDefault();
					for (Hit item : hits.getHits().getItems()){
						HttpDelete httpDelete = new HttpDelete(getHostPortPath() + "/" + item.getId());
					    byte[] credentials = Base64.getEncoder().encode((getUsuario() + ":" + getSenha()).getBytes(StandardCharsets.UTF_8));
					    httpDelete.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
					    response = client.execute(httpDelete);
					}
				}
			} catch (ClientProtocolException ex){
			} catch (IOException ex){
			} finally {			
				try {if (response != null ) response.close(); } catch (Exception ex){}
				try {if (client != null) client.close(); } catch (Exception ex){}
			}
		}
	}
	
	/**
	 * 
	 * Faz indexação de arquivos do projudi para o Elasticsearch.
	 * Esse método será chamado pelo programa de execução automática.
	 * Os arquivos serão consultados da tabela ARQ_INDEX, que será carregada todo dia, em hora determinada,
	 * com os novos arquivos elegíveis para publicação.
	 * @throws Exception  
	 * 
	 */
	public void inicializarIndexacao(int limit) throws Exception {
		int offset = 0;
		List<Integer> rows = null;
		ArquivoIndexacaoNe arquivoIndexacaoNe = new ArquivoIndexacaoNe();
		IndexadorNe indexadorNe = new IndexadorNe();
		do {
			rows = arquivoIndexacaoNe.listarIdsArquivosParaIndexacaoNoElasticSearch(offset, limit);
			if (!rows.isEmpty()){
				indexadorNe.indexar(rows);
				offset = rows.get(rows.size()-1);
			}
		} while(rows.size() > 0);
	}
	
	/**
	 * Cria o JSON da requisição para a consulta de texto.
	 * O método de pesquisa é o SIMPLE_QUERY_STRING do Elasticsearch.
	 * Esse JSON é encaminhado numa chamada POST. Utilizado apenas para a Consulta de Publicação
	 * @param query
	 * @return
	 */
	public String getJsonSimpleQueryString(String query){
		return "{" +
				"\"_source\": [\"id_arq\", \"numero_processo\"]," +
				"\"query\": {" + 
					"\"simple_query_string\": {" +
						"\"query\": \"" + query + " \", \"fields\": [\"texto\"], \"default_operator\": \"and\", \"flags\":-1, \"lenient\": true, \"analyze_wildcard\": true, \"boost\" : 1.0" +
					"}" +
				"}," +
		    	"\"highlight\": {" + 
		    		"\"tags_schema\" : \"styled\"," + 
		            "\"fields\" : {" + 
		                "\"texto\" : {}" + 
		               "}" + 
		    	"}" + 
		    "}";    
	}
	
	/**
	 * Cria o JSON da requisição para a consulta de texto por campos específicos
	 * @param params
	 * @return
	 */
	public String getJsonBoolQueryString(Map<String, String> params){
		
		StringBuilder query = new StringBuilder();
		
		for (Entry<String, String> entry : params.entrySet()){
			if (!entry.getKey().startsWith("data_pub")){
				query.append(",{\"match\": { \"" + entry.getKey() + "\": { \"query\": \"" + entry.getValue() + "\", \"operator\" : \"and\"} } }");
			}			
    	}
		
		if (ValidacaoUtil.isNaoVazio(params.get("data_pub_ini")) && ValidacaoUtil.isNaoVazio(params.get("data_pub_fim"))) {			
			query.append(", { \"range\": { \"data_publicacao\": { \"gte\": \"" + params.get("data_pub_ini") + "\", \"lte\": \"" + params.get("data_pub_fim") + "\"} } }");
			
		} else if (ValidacaoUtil.isNaoVazio(params.get("data_pub_ini"))) {
			query.append(", { \"range\": { \"data_publicacao\": { \"gte\": \"" + params.get("data_pub_ini") + "\"} } }");
			
		} else if (ValidacaoUtil.isNaoVazio(params.get("data_pub_fim"))) {
			query.append(", { \"range\": { \"data_publicacao\": { \"lte\": \"" + params.get("data_pub_fim") + "\"} } }");
		}
		
		return "{" +
				"\"_source\": [\"numero_processo\", \"serv\", \"realizador\", \"tipo_arq\", \"data_publicacao\", \"texto\", \"id_arq\"]," +
				"\"query\": {" + 
					"\"bool\": {" +
						"\"must\": [" + query.toString().substring(1) + "]," +
					    "\"adjust_pure_negative\" : true," + 
					    "\"boost\" : 1.0" +
					"}" + 
				"},"+ 
				"\"highlight\": {" + 
					"\"tags_schema\" : \"styled\"," + 
					"\"fields\" : {" + 
				    	"\"texto\" : {}" +
				    "}" + 
				 "}," +
				"\"sort\": {" +
					"\"data_publicacao\": {\"order\" : \"desc\"}" +
				"}" +
		    "}";
	}
	
	/**
	 * Consulta de jurisprudências por campos específicos
	 * @param params
	 * @return
	 */
	public String getJsonBoolQueryStringAndExists(Map<String, String> params){
		
		StringBuilder query = new StringBuilder();
		
		for (Entry<String, String> entry : params.entrySet()){
			if (!entry.getKey().startsWith("data_pub")){
				if (entry.getKey().equals("texto")){
					query.append(", { \"simple_query_string\": {\"query\": \"" + entry.getValue() + " \", \"fields\": [\"texto\", \"extra\"], \"default_operator\": \"and\", \"flags\":-1, \"lenient\": true, \"analyze_wildcard\": true, \"boost\" : 1.0}}");
				} else {
					query.append(",{\"match\": { \"" + entry.getKey() + "\": { \"query\": \"" + entry.getValue() + "\", \"operator\" : \"and\"} } }");
				}
			}
    	}
		
		if (ValidacaoUtil.isNaoVazio(params.get("data_pub_ini")) && ValidacaoUtil.isNaoVazio(params.get("data_pub_fim"))) {			
			query.append(", { \"range\": { \"data_publicacao\": { \"gte\": \"" + params.get("data_pub_ini") + "\", \"lte\": \"" + params.get("data_pub_fim") + "\"} } }");
			
		} else if (ValidacaoUtil.isNaoVazio(params.get("data_pub_ini"))) {
			query.append(", { \"range\": { \"data_publicacao\": { \"gte\": \"" + params.get("data_pub_ini") + "\"} } }");
			
		} else if (ValidacaoUtil.isNaoVazio(params.get("data_pub_fim"))) {
			query.append(", { \"range\": { \"data_publicacao\": { \"lte\": \"" + params.get("data_pub_fim") + "\"} } }");
		}
		
		return "{" +
				"\"_source\": [\"numero_processo\", \"serv\", \"realizador\", \"tipo_arq\", \"data_publicacao\", \"texto\", \"extra\", \"id_arq\"]," +
				"\"query\": {" + 
					"\"bool\": {" +
						"\"must\": [" + query.toString().substring(1) + ", { \"exists\" : { \"field\" : \"extra\" } } ]," +						
					    "\"adjust_pure_negative\" : true," + 
					    "\"boost\" : 1.0" +
					"}" + 
				"},"+
				"\"highlight\": {" + 
					"\"tags_schema\" : \"styled\"," + 
					"\"fields\" : {" + 
				    	"\"texto\" : {}, \"extra\" : {}" +
				    "}" + 
				"}," + 	
				"\"sort\": {" +
					"\"data_publicacao\": {\"order\" : \"desc\"}" +
				"}" +
		    "}";
	}
	
	/**
	 * 
	 * @param id
	 * @param numero
	 * @return
	 */
	public String getJsonQueryPorIdArquivoENumeroProcesso (String id, String numero){
		return "{" +
			"\"_source\": [\"id_arq\", \"numero_processo\"]," +
    		"\"query\": { \"bool\": { \"must\": [ { \"match\": { \"id_arq\": " + id.trim() + "} }, { \"match\": { \"numero_processo\": \"" + numero.trim() + "\"} } ] } } " +    		
	    "}";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJsonBoolQueryPorIdTipoArquivoENaoExisteCampoExtra(String tipo){
		return "{" +
			"\"query\": {" +
				"\"bool\": {" +
					"\"must\": [ { \"match\": { \"id_tipo_arq\": " + tipo + "} } ]," +
					"\"must_not\": [ { \"exists\" : { \"field\" : \"extra\" } } ]," +
				"}" +
			"},"+  
		"}";
	}
	
	public String getHostPortPath(){		
		return ProjudiPropriedades.getInstance().getHostElasticSearch() + ":"
				+ ProjudiPropriedades.getInstance().getPortElasticSearch()
				+ ProjudiPropriedades.getInstance().getPathElasticSearch();
	}
	
	public String getUsuario(){
		return ProjudiPropriedades.getInstance().getUserElasticSearch();
	}
	
	public String getSenha(){
		return ProjudiPropriedades.getInstance().getPasswordElasticSearch();
	}
	
}
