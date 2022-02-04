package br.gov.go.tj.projudi.ct;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.InfosegDt;
import br.gov.go.tj.projudi.ne.InfosegNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class InfosegCt extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1169265740275072499L;

	public int Permissao() {
		return InfosegDt.CodigoPermissaoInfoseg; //TODO criar permissão para infoseg
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		String stAcao="/WEB-INF/jsptjgo/ConsultaInfoseg.jsp";
		String caminhoArquivo = "E:\\Infoseg\\";
		
		request.setAttribute("CaminhoArquivo", caminhoArquivo);
		
		request.setAttribute("TituloPagina", "Consulta dados da Execução Penal para INFOSEG");
		request.setAttribute("tempPrograma", "Infoseg");
		request.setAttribute("tempRetorno", "Infoseg");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		InfosegNe infosegNe = (InfosegNe)request.getSession().getAttribute("Infosegne");
		if (infosegNe == null )  infosegNe  = new InfosegNe();  
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
	
		//captura os dados da tela
//		String tipoArquivo = "";
		
//		if (request.getParameter("radioArquivo") != null) {
//            if (request.getParameter("radioArquivo").equalsIgnoreCase("3")) //dados de sentenciados
//                tipoArquivo = "3";
//            else if (request.getParameter("radioArquivo").equalsIgnoreCase("4")) //dados de processos
//            	tipoArquivo = "4";
//        }
		
		switch (paginaatual) {
			case Configuracao.Novo:		
				break;
				
			case Configuracao.Localizar:
				//nome do arquivo
//				GO<sequência de arquivo>-AAAAMM999.txt, 
//				onde sequência de arquivo é 3 ou 4. 
//				O de dados é o 3; 
//				o de processos é o 4;  
//				AAAA é o ano, MM é o mês e 999 é a sequência. 
//				A dessa semana é 323. 
//				O arquivo dessa semana seria então GO3-201110323.txt, 
//				lembrando, antes de enviar, de zipá-lo.
				
//				
				
				GregorianCalendar gr = new GregorianCalendar();
				gr.setTime(new Date());
				int mes = gr.get(Calendar.MONTH)+1;
				int dia = gr.get(Calendar.DAY_OF_MONTH);
				
				//mesma rotina utilizada na classe JobArquivosInfoseg do projeto scheduling
				String nomeArquivo = caminhoArquivo + "GO3-" + gr.get(Calendar.YEAR) + "-" + mes + "-" +  dia + ".txt";
				//System.out.println("ExecuçãoAutomatica - Início Arquivo Infoseg: dados do sentenciado - " + Funcoes.DataHora(new Date()));
				getArquivoSentenciado(nomeArquivo);
				//System.out.println("ExecuçãoAutomatica - Fim Arquivo Infoseg: dados do sentenciado - " + Funcoes.DataHora(new Date()));
				
				String nomeArquivo1 = caminhoArquivo + "GO4-"+gr.get(Calendar.YEAR)+ "-" + mes + "-" +  dia + ".txt";
				//System.out.println("ExecuçãoAutomatica - Início Arquivo Infoseg: dados do processo - " + Funcoes.DataHora(new Date()));
				getArquivoProcesso(nomeArquivo1);
				//System.out.println("ExecuçãoAutomatica - Fim Arquivo Infoseg: dados do processo - " + Funcoes.DataHora(new Date()));
				request.setAttribute("MensagemOk", "Arquivos gerados com sucesso: \n" + nomeArquivo + "\n" + nomeArquivo1);
		}

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	public void getArquivoSentenciado(String nomeArquivo) throws Exception{
		//se o arquivo existir apaga
		if(new File(nomeArquivo).exists()){
			deleteFile(new File(nomeArquivo), 0, 0);
		}
		new InfosegNe().gerarArquivoSentenciado(nomeArquivo);
	}
	
	public void getArquivoProcesso(String nomeArquivo) throws Exception {
		//se o arquivo existir apaga
		if(new File(nomeArquivo).exists()){
			deleteFile(new File(nomeArquivo), 0, 0);
		}
		new InfosegNe().gerarArquivoProcesso(nomeArquivo);
	}
	
	public static void deleteFile(File file, int tries, int tried) {
	    if (!file.delete()) {
	        System.gc();  // No caso da JVM por si mesma achar que possui um manipulador para o arquivo
	        if (!file.delete()) {
	            try{ // Agora espera e verifica se o arquivo ja foi apagado
	                Thread.sleep(200);
	            }catch(InterruptedException e) {}
	            /* Potencial loop permanete evitado com um contador.
	             * Se o arquivo ainda existe apos todas as tentativas,
	             * desiste. Voce agora tem outro problema.
	             */
	            if( tried < tries ){
	                deleteFile(file,tries,tried++);
	            }
	        }
	    }
	}
}
