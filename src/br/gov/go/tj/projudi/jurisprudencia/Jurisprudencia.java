package br.gov.go.tj.projudi.jurisprudencia;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ParserUtil;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;

import com.google.gson.Gson;

public class Jurisprudencia {
	
	 private final String ROOT = "DocJuris";
	 private static final String CODIGO_NAO_LIBERAR = "N";
	 private static final String CONTENT_TYPE_APPLICATION_PDF = "application/pdf";

	 private String cd_acao;
	 private String ds_comarca;
	 private String ds_camara;
	 private String nr_diario;
	 private String dt_publicacao_diario;
	 private String nr_livro;
	 private String ds_ementa;
	 private String dt_acordao;
	 private String nm_relator;
	 private String nm_redator;
	 private String ds_decisao;
	 private String ds_recurso;
	 private String nr_recurso;
	 private String nr_processo;
	 private String in_rflg;
	 private String in_rfdt;
	 private String nr_autuacao;
	 private String cd_libera;
	 private String cd_relator;
	 private String in_segredo; 
	 private String nm_autor;
	 private String nm_reu;
	 private String nm_vitima;
	 private String data_acordao;
	 private String data_publicacao;
	 private String parte1;
	 private String parte2;
	 private String parte3;
	 private String parte4;
	 private String parte5;
	 private String parte6;
	 private String parte7;
	 private String parte8;
	 private String parte9;
	 private String parte10;
	 
	 private String linkAcordao;
	 
	 private String contentType;
	 private byte[] conteudo;
	 private boolean temRecibo;

	public Jurisprudencia() {		
		this.cd_acao = "";
		this.ds_comarca = "";
		this.ds_camara = "";
		this.nr_diario = "";
		this.dt_publicacao_diario = "";
		this.nr_livro = "";
		this.ds_ementa = "";
		this.dt_acordao = "";
		this.nm_relator = "";
		this.nm_redator = "";
		this.ds_decisao = "";
		this.ds_recurso = "";
		this.nr_recurso = "";
		this.nr_processo = "";
		this.in_rflg = "";
		this.in_rfdt = "";
		this.nr_autuacao = "";
		this.cd_libera = "";
		this.cd_relator = "";
		this.in_segredo = "";
		this.nm_autor = "";
		this.nm_reu = "";
		this.nm_vitima = "";
		this.data_acordao = "";
		this.data_publicacao = "";
		this.parte1 = "";
		this.parte2 = "";
		this.parte3 = "";
		this.parte4 = "";
		this.parte5 = "";
		this.parte6 = "";
		this.parte7 = "";
		this.parte8 = "";
		this.parte9 = "";
		this.parte10 = "";
		this.linkAcordao = "";
		this.contentType = "";
		this.conteudo = null;
		this.temRecibo = true;
	}
	
	public String getCd_acao() {
		return cd_acao;
	}

	public void setCd_acao(String cd_acao) {
		this.cd_acao = cd_acao;
	}

	public String getDs_comarca() {
		return ds_comarca;
	}

	public void setDs_comarca(String ds_comarca) {
		this.ds_comarca = ds_comarca;
	}
	public String getDs_camara() {
		return ds_camara;
	}
	public void setDs_camara(String ds_camara) {
		this.ds_camara = ds_camara;
	}
	public String getNr_diario() {
		return nr_diario;
	}
	public void setNr_diario(String nr_diario) {
		this.nr_diario = nr_diario;
	}
	public String getDt_publicacao_diario() {
		return dt_publicacao_diario;
	}
	public void setDt_publicacao_diario(String dt_publicacao_diario) {
		this.dt_publicacao_diario = dt_publicacao_diario;
	}
	public String getNr_livro() {
		return nr_livro;
	}
	public void setNr_livro(String nr_livro) {
		this.nr_livro = nr_livro;
	}
	public String getDs_ementa() {
		return ds_ementa;
	}
	public void setDs_ementa(String ds_ementa) {
		this.ds_ementa = ds_ementa;
	}
	public String getDt_acordao() {
		return dt_acordao;
	}
	public void setDt_acordao(String dt_acordao) {
		this.dt_acordao = dt_acordao;
	}
	public String getNm_relator() {
		return nm_relator;
	}
	public void setNm_relator(String nm_relator) {
		this.nm_relator = nm_relator;
	}
	public String getNm_redator() {
		return nm_redator;
	}
	public void setNm_redator(String nm_redator) {
		this.nm_redator = nm_redator;
	}
	public String getDs_decisao() {
		return ds_decisao;
	}
	public void setDs_decisao(String ds_decisao) {
		this.ds_decisao = ds_decisao;
	}
	public String getDs_recurso() {
		return ds_recurso;
	}
	public void setDs_recurso(String ds_recurso) {
		this.ds_recurso = ds_recurso;
	}
	public String getNr_recurso() {
		return nr_recurso;
	}
	public void setNr_recurso(String nr_recurso) {
		this.nr_recurso = nr_recurso;
	}
	public String getNr_processo() {
		return nr_processo;
	}
	public void setNr_processo(String nr_processo) {
		this.nr_processo = nr_processo;
	}
	public String getIn_rflg() {
		return in_rflg;
	}
	public void setIn_rflg(String in_rflg) {
		this.in_rflg = in_rflg;
	}
	public String getIn_rfdt() {
		return in_rfdt;
	}

	public void setIn_rfdt(String in_rfdt) {
		this.in_rfdt = in_rfdt;
	}

	public String getNr_autuacao() {
		return nr_autuacao;
	}
	public void setNr_autuacao(String nr_autuacao) {
		this.nr_autuacao = nr_autuacao;
	}
	public String getCd_libera() {
		return cd_libera;
	}
	public void setCd_libera(String cd_libera) {
		this.cd_libera = cd_libera;
	}
	public String getCd_relator() {
		return cd_relator;
	}
	public void setCd_relator(String cd_relator) {
		this.cd_relator = cd_relator;
	}
	public String getIn_segredo() {
		return in_segredo;
	}
	public void setIn_segredo(String in_segredo) {
		this.in_segredo = in_segredo;
	}
	public String getNm_autor() {
		return nm_autor;
	}
	public void setNm_autor(String nm_autor) {
		this.nm_autor = nm_autor;
	}
	public String getNm_reu() {
		return nm_reu;
	}
	public void setNm_reu(String nm_reu) {
		this.nm_reu = nm_reu;
	}
	public String getNm_vitima() {
		return nm_vitima;
	}
	public void setNm_vitima(String nm_vitima) {
		this.nm_vitima = nm_vitima;
	}
	public String getData_acordao() {
		return data_acordao;
	}
	public void setData_acordao(String data_acordao) {
		this.data_acordao = data_acordao;
	}
	public String getData_publicacao() {
		return data_publicacao;
	}
	public void setData_publicacao(String data_publicacao) {
		this.data_publicacao = data_publicacao;
	}
	public String getParte1() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte1) : parte1; 
	}
	public void setParte1(String parte1) {
		this.parte1 = parte1;
	}
	public String getParte2() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte2) : parte2;
	}
	public void setParte2(String parte2) {
		this.parte2 = parte2;
	}
	public String getParte3() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte3) : parte3;
	}
	public void setParte3(String parte3) {
		this.parte3 = parte3;
	}
	public String getParte4() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte4) : parte4;
	}
	public void setParte4(String parte4) {
		this.parte4 = parte4;
	}
	public String getParte5() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte5) : parte5;
	}
	public void setParte5(String parte5) {
		this.parte5 = parte5;
	}
	public String getParte6() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte6) : parte6;
	}
	public void setParte6(String parte6) {
		this.parte6 = parte6;
	}
	public String getParte7() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte7) : parte7;
	}
	public void setParte7(String parte7) {
		this.parte7 = parte7;
	}
	public String getParte8() {
		return this.parte8;
	}
	public void setParte8(String parte8) {
		this.parte8 = parte8;
	}
	public String getParte9() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte9) : parte9;
	}
	public void setParte9(String parte9) {
		this.parte9 = parte9;
	}
	public String getParte10() {
		return isSegredoJustica() ? getNomeParteEmSegredoJustica(parte10) : parte10;
	}
	public void setParte10(String parte10) {
		this.parte10 = parte10;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getConteudo() {
		return conteudo;
	}
	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
	public boolean isTemRecibo() {
		return temRecibo;
	}
	public void setTemRecibo(boolean temRecibo) {
		this.temRecibo = temRecibo;
	}

	public boolean isSegredoJustica(){
		return ValidacaoUtil.isNaoVazio(this.in_segredo) && (this.in_segredo.equals("S"));
	}
	
	public String getXml() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement(this.ROOT);
		
		rootElement.setAttribute("nm_acor_jur", this.getLinkAcordao());

		doc.appendChild(rootElement);
		
		
		Element el = doc.createElement("cd_acao");
		rootElement.appendChild(el);
		el.appendChild(doc.createTextNode(this.getCd_acao()));
		
		el = doc.createElement("ds_comarca");
		rootElement.appendChild(el);

		if (!this.getDs_comarca().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDs_comarca()));
		}

		el = doc.createElement("ds_camara");
		rootElement.appendChild(el);
		if (!this.getDs_camara().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDs_camara()));
		}

		el = doc.createElement("nr_diario");
		rootElement.appendChild(el);
		if (!this.getNr_diario().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNr_diario()));
		}

		el = doc.createElement("dt_publicacao_diario");
		rootElement.appendChild(el);
		if (!this.getDt_publicacao_diario().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDt_publicacao_diario()));
		}

		el = doc.createElement("nr_livro");
		rootElement.appendChild(el);
		if (!this.getNr_livro().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNr_livro()));
		}

		el = doc.createElement("ds_ementa");
		rootElement.appendChild(el);
		if (!this.getDs_ementa().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDs_ementa()));
		}

		el = doc.createElement("dt_acordao");
		rootElement.appendChild(el);
		if (!this.getDt_acordao().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDt_acordao()));
		}

		el = doc.createElement("nm_relator");
		rootElement.appendChild(el);
		if (!this.getNm_relator().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNm_relator()));
		}

		el = doc.createElement("nm_redator");
		rootElement.appendChild(el);
		if (!this.getNm_redator().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNm_redator()));
		}

		el = doc.createElement("ds_decisao");
		rootElement.appendChild(el);
		if (!this.getDs_decisao().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDs_decisao()));
		}

		el = doc.createElement("ds_recurso");
		rootElement.appendChild(el);
		if (!this.getDs_recurso().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getDs_recurso()));
		}

		el = doc.createElement("nr_recurso");
		rootElement.appendChild(el);
		if (!this.getNr_recurso().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNr_recurso()));
		}

		el = doc.createElement("nr_processo");
		rootElement.appendChild(el);
		if (!this.getNr_processo().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNr_processo()));
		}

		el = doc.createElement("in_rflg");
		rootElement.appendChild(el);
		if (!this.getIn_rflg().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getIn_rflg()));
		}
		
		el = doc.createElement("in_rfdt");
		rootElement.appendChild(el);
		if (!this.getIn_rfdt().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getIn_rflg()));
		}

		el = doc.createElement("nr_autuacao");
		rootElement.appendChild(el);
		if (!this.getNr_autuacao().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNr_autuacao()));
		}

		el = doc.createElement("cd_libera");
		rootElement.appendChild(el);
		if (!this.getCd_libera().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getCd_libera()));
		}

		el = doc.createElement("cd_relator");
		rootElement.appendChild(el);
		if (!this.getCd_relator().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getCd_relator()));
		}

		el = doc.createElement("in_segredo");
		rootElement.appendChild(el);
		if (!this.getIn_segredo().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getIn_segredo()));
		}

		el = doc.createElement("nm_autor");
		rootElement.appendChild(el);
		if (!this.getNm_autor().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNm_autor()));
		}

		el = doc.createElement("nm_reu");
		rootElement.appendChild(el);
		if (!this.getNm_reu().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNm_reu()));
		}

		el = doc.createElement("nm_vitima");
		rootElement.appendChild(el);
		if (!this.getNm_vitima().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getNm_vitima()));
		}

		el = doc.createElement("data_acordao");
		rootElement.appendChild(el);
		if (!this.getData_acordao().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getData_acordao()));
		}

		el = doc.createElement("data_publicacao");
		rootElement.appendChild(el);
		if (!this.getData_publicacao().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getData_publicacao()));
		}

		el = doc.createElement("parte1");
		rootElement.appendChild(el);
		
		if (!this.getParte1().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte1()));
		}

		el = doc.createElement("parte2");
		rootElement.appendChild(el);
		
		if (!this.getParte2().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte2()));
		}

		el = doc.createElement("parte3");
		rootElement.appendChild(el);

		
		if (!this.getParte3().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte3()));
		}

		el = doc.createElement("parte4");
		rootElement.appendChild(el);
		
		if (!this.getParte4().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte4()));
		}

		el = doc.createElement("parte5");
		rootElement.appendChild(el);
		
		if (!this.getParte5().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte5()));
		}

		el = doc.createElement("parte6");
		rootElement.appendChild(el);
		
		if (!this.getParte6().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte6()));
		}

		el = doc.createElement("parte7");
		rootElement.appendChild(el);
		
		if (!this.getParte7().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte7()));
		}

		el = doc.createElement("parte8");
		rootElement.appendChild(el);
		
		if (!this.getParte8().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte8()));
		}

		el = doc.createElement("parte9");
		rootElement.appendChild(el);
		
		if (!this.getParte9().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte9()));
		}

		el = doc.createElement("parte10");
		rootElement.appendChild(el);
		
		if (!this.getParte10().isEmpty()) {
			el.appendChild(doc.createTextNode(this.getParte10()));
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(source, result);
				
		return writer.toString();
	}
	
	public static Jurisprudencia getPreJurisprudencia() {
		Jurisprudencia jurisprudencia = new Jurisprudencia();
		jurisprudencia.setCd_libera(Jurisprudencia.CODIGO_NAO_LIBERAR);
		jurisprudencia.setNr_livro("(S/R)");
		jurisprudencia.setDs_decisao("DECISÃO NOS AUTOS.");
		return jurisprudencia;
	}

	public void generateAcordaoLink(String mov, String id_voto) throws Exception {
		String x = new MovimentacaoArquivoNe().consultarArquivosMovimentacaoHashJSON(mov, new UsuarioNe());
		Map[] jsonJavaRootObject = new Gson().fromJson(x, Map[].class);
		String documento = "PJD";
		for (int i = 0; i < jsonJavaRootObject.length; i++) {
			if (((String) jsonJavaRootObject[i].get("id_arquivo")).equals(id_voto)) {
				documento += "." + (String) jsonJavaRootObject[i].get("hash");
				documento += "." + (String) jsonJavaRootObject[i].get("id");
				break;
			}
		}
		this.setLinkAcordao(documento);
	}
	
	public void gerarLinkAcordao(String id_movi_arq, String id_proc) throws Exception {
		String codigo = "PJD." + new UsuarioNe().getCodigoHash(id_movi_arq + id_proc) + "." + id_movi_arq;
		this.setLinkAcordao(codigo);
	}
	
	public String getLinkAcordao() {
		return linkAcordao;
	}

	public void setLinkAcordao(String linkAcordao) {
		this.linkAcordao = linkAcordao;
	}
		
	public void generateEmentaTexto(String contentType, byte[] texto, boolean temRecibo) throws Exception {
		String textoDoPdf = "";
		if (ValidacaoUtil.isNaoVazio(texto)){
			if (contentType.equals(CONTENT_TYPE_APPLICATION_PDF)){
				textoDoPdf = ParserUtil.parsePdfToPlainText(texto);
				this.setDs_ementa(textoDoPdf);
			} else {
				if (temRecibo){
					this.setDs_ementa(generateEmentaTexto(texto));
				} else {
					this.setDs_ementa(generateEmentaTextoSemRecibo(texto));
				}
			}
		}		
	}
	
	public String generateEmentaTexto(byte[] texto) throws Exception {
		byte[] bytes = Signer.extrairConteudoP7sRecibo(texto);
		String str = new String(bytes, StandardCharsets.ISO_8859_1);
		str = StringEscapeUtils.unescapeHtml(str);
		str = str.replaceAll("(?m)^\\s+$","");
		str = str.replaceAll("", "");
		return str.replaceAll("\\<[^>]*>","");
	}
	
	public String generateEmentaTextoSemRecibo(byte[] texto) throws Exception {
		byte[] bytes = Signer.extrairConteudoP7s(texto);
		String str = new String(bytes, StandardCharsets.ISO_8859_1);
		str = StringEscapeUtils.unescapeHtml(str);
		str = str.replaceAll("(?m)^\\s+$","");
		str = str.replaceAll("", "");
		return str.replaceAll("\\<[^>]*>","");
	}
	
	public void generateDatasAcordao(String data) {
		data = data.split(" ")[0];
		this.setData_acordao(data.replaceAll("-", ""));
		this.setData_publicacao(data.replaceAll("-", ""));
		String dt[] = data.split("-");
		this.setDt_acordao(dt[2] + "/" + dt[1] + "/" + dt[0]);
		this.setDt_publicacao_diario(dt[2] + "/" + dt[1] + "/" + dt[0]);	
	}
	
	/**
	 * Apenas exibe as iniciais do nome da parte caso o processo seja segredo de justica. 
	 * @param nomeParte - É formado pelo tipo da parte : nome da parte. Ex: Apelante : Fulano da Silva 
	 * @return
	 */
	public String getNomeParteEmSegredoJustica(String nomeParte){
		String [] texto = nomeParte.split(":");
		if (ValidacaoUtil.isNaoVazio(texto)){
			if (texto.length == 2){
				return texto[0] + ": " + Funcoes.iniciaisNomeComSeparador(texto[1], ".");
			} else {
				return Funcoes.iniciaisNomeComSeparador(texto[0], ".");
			}
		}
		return nomeParte;
	}
	
}
