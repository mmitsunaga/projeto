package br.gov.go.tj.projudi.ct.webservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class Servico07Ct extends Controle {

	private static final long serialVersionUID = -404639847143392126L;

	public int Permissao() {
		return 609;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaoPaginaAtual) throws Exception {

		
		try{
			switch(paginaatual) {
				case Configuracao.Curinga6 : {
					String conteudoArquivo = request.getParameter("xml");
					
					SAXBuilder builder = new SAXBuilder();
					Document doc = builder.build(new ByteArrayInputStream(conteudoArquivo.getBytes()));
					conteudoArquivo = null;
					Element dadosGuia = doc.getRootElement();
					List row = dadosGuia.getChildren("guia");
					for (int i = 0; i < row.size(); i++) {
						Element guia = (Element) row.get(i);
						
						guia.getChild("numeroGuia").getText();
						guia.getChild("dataPagamento").getText();
						
						//guiaEmissaoNe.atualizarPagamentos(numeroGuia, dataPagamento);
					}
				}
			}
		}
		catch(Exception e) {
			throw e;
		}
		
		return;
	}
}