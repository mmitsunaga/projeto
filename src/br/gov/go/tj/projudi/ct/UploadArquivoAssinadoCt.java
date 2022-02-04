package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Base64Utils;

/**
 * Servlet que controla o upload de arquivo assinados externamente..
 * 
 * @author lsrbsilva
 * 
 */
public class UploadArquivoAssinadoCt extends Controle {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		int passoUpload = Configuracao.Editar;
		if (request.getParameter("PassoUpload") != null){
			passoUpload = Funcoes.StringToInt(request.getParameter("PassoUpload"));
        }else if (request.getAttribute("PassoUpload") != null){
        	passoUpload = Funcoes.StringToInt(request.getAttribute("PassoUpload").toString());
        }
		
		List<ArquivoDt> arquivos = (List<ArquivoDt>) request.getSession().getAttribute("ArquivoAssinadoCompleto");
		if (arquivos == null)
			arquivos = new ArrayList<ArquivoDt>();
		switch (passoUpload) {
			case Configuracao.Curinga6:
				String nome = getNomeArquivo(request);
				for(int i=0; ;i++){
					try{
						ArquivoDt arquivo = arquivos.get(i);
						if((arquivo.getNomeArquivo() + ".p7s").equalsIgnoreCase(nome)){
							arquivo.setArquivo(Base64Utils.base64Encode(getArquivoBytes(request)));
							break;
						}
					} catch(IndexOutOfBoundsException e){
						throw new MensagemException("Erro ao extrair conteúdo do arquivo " + nome + ", tente novamente.");			
					}
				}
				PrintWriter writer = response.getWriter();
		        response.setContentType("application/json");
		        JSONArray json = new JSONArray();
                JSONObject jsono = new JSONObject();
                jsono.put("name", nome);
                jsono.put("size", getArquivoBytes(request).length);
                jsono.put("url", "");
                jsono.put("thumbnail_url", "");
                jsono.put("delete_url", "");
                jsono.put("delete_type", "GET");
                json.put(jsono);
	            writer.write(json.toString());
				break;
			case Configuracao.Curinga7:
				String retornarPara = (String) request.getAttribute("RetornarPara");
				if (retornarPara == null || retornarPara.trim().isEmpty())
					throw new MensagemException("Fluxo inválido, favor recomeçar a operação.");
				RequestDispatcher dis = request.getRequestDispatcher(retornarPara);
				dis.include(request, response);
				break;
			default:
				throw new MensagemException("Fluxo inválido, favor recomeçar a operação.");
		}
	}

	@Override
	public int Permissao() {
		return MovimentacaoArquivoDt.CodigoPermissao;
	}


}
