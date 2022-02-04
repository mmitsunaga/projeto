<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Pend&ecirc;ncia</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css"> 
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
	  <script type='text/javascript' src='./js/jquery.js'></script>      
	  <!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      <script type='text/javascript' src='./js/prototype.js'></script>
      <script type="text/javascript" src="./js/tabelas.js"></script>
      
	  <%@ include file="js/PendenciaPegar.js"%>
	  <%@ include file="js/buscarArquivos.js"%>
	  
	  <script type="text/javascript">
	  	//Modifica o modo de visualizacao da lista
	  	modoEdicaoPendencia = false;
	  </script>
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
		<fieldset class="fieldEdicaoEscuro">
			<legend>Pend&ecirc;ncia</legend>
			
			<%@ include file="Padroes/Mensagens.jspf"%>
			
			<%if (Pendenciadt!= null  && Pendenciadt.getId() != null && !Pendenciadt.equals("")){ %>
				<fieldset class="formLocalizar">
					<legend>Dados da Pend&ecirc;ncia</legend>
					
					<label class="formEdicaoLabel">N&uacute;mero:</label><br><b><%=Pendenciadt.getId()%></b><br />
			 
					<label class="formEdicaoLabel">Tipo de Pend&ecirc;ncia:</label><br> <%=Pendenciadt.getPendenciaTipo()%><br />
					<label class="formEdicaoLabel">Status:</label><br> <%=Pendenciadt.getPendenciaStatus()%><br />
					<label class="formEdicaoLabel">Serventia Cadastrador:</label><br> <%=Pendenciadt.getServentiaUsuarioCadastrador()%><br />
					<label class="formEdicaoLabel">Nome Cadastrador:</label><br> <%=Pendenciadt.getNomeUsuarioCadastrador()%><br />
					<label class="formEdicaoLabel">Data Inicio Exibição:</label><br> <%=Pendenciadt.getDataInicio()%><br />
					
					<%if (Pendenciadt.getDataLimite() != null && !Pendenciadt.getDataLimite().equals("") ){%>
						<label class="formEdicaoLabel">Data Limite de Exibição:</label><br> <%=Pendenciadt.getDataLimite()%><br />
					<%}%>
					
					<br />
				</fieldset>
				
				<fieldset class="formLocalizar">
					<legend>Arquivos</legend>
					
					<%
						int colspan = 4;
						if (request.getAttribute("comOpcao") != null){
							colspan++;
						}
					%>	
				
					<table class="Tabela">
						<thead>
					    	<tr>
					    		<%if (request.getAttribute("comOpcao") != null){%>
					    		<th title="Op&ccedil;&otilde;es para movimenta&ccedil;&atilde;o">Op.</th>
					    		<%}%>
					      		<th width="30%">Descri&ccedil;&atilde;o</th>
					      		<th>Usu&aacute;rio Assinador</th>
					      		<th title="Identifica se um arquivo &eacute; uma resposta">Resposta</th>
					      		<th>Nome do Arquivo</th>
					    	</tr>
					  	</thead>
					  	
					  	<%if (request.getAttribute("ListaArquivos") != null){
							List lista = (List)request.getAttribute("ListaArquivos");
							Iterator it = lista.iterator();
							int qtd = 0;
							%>
							
							<tfoot>
								<tr>
									<td colspan="<%=colspan%>">Quantidade de arquivos: <%=lista.size()%></td>
								</tr>
							</tfoot>
								
						  	<tbody>
								<%
								while (it.hasNext()){
									PendenciaArquivoDt obj = (PendenciaArquivoDt)it.next();
									%>
									<tr>
										<%if (request.getAttribute("comOpcao") != null){%>
										<td>
											<input type="checkbox" name="arquivos[]" value="<%=obj.getArquivoDt().getId()%>" />
										</td>
										<%}%>
									  	<td><%=obj.getArquivoDt().getArquivoTipo()%></td>
									  	<td>
									  		<%if (obj.getArquivoDt().getUsuarioAssinadorFormatado() == null || obj.getArquivoDt().getUsuarioAssinadorFormatado().trim().equals("")) {%>
									  			ARQUIVO N&Atilde;O ASSINADO
									  		<%}else {%>
									  			<%=obj.getArquivoDt().getUsuarioAssinadorFormatado()%>					  			
									  		<%}%>
									  	</td>
									  	<td><%=obj.getRespostaLiteral()%></td>
								      	<td>
								      		<%if (Pendenciadt.getDataVisto() != null && !(Pendenciadt.getDataVisto().equals(""))){ %>
									      		<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>&amp;finalizado=false">
									      			<%=obj.getArquivoDt().getNomeArquivoFormatado()%>
									      		</a>
									      		
									      		<%if (obj.getArquivoDt().getRecibo().equals("true")){%>
										      		<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>&amp;recibo=true&amp;finalizado=false">
										      			Recibo
										      		</a>
									      		<%}%>
									      <%}else {%>
									      		<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>">
									      			<%=obj.getArquivoDt().getNomeArquivoFormatado()%>
									      		</a>
									      		
									      		<%if (obj.getArquivoDt().getRecibo().equals("true")){%>
										      		<a href="<%=request.getAttribute("linkArquivo")%>?PaginaAtual=<%=request.getAttribute("paginaArquivo")%>&amp;<%=request.getAttribute("campoArquivo")%>=<%=obj.getId()%>&amp;hash=<%=obj.getHash()%>&amp;recibo=true">
										      			Recibo
										      		</a>
									      		<%}%>
									      <%}%>
								      	</td>			  	  							    
								    </tr>
									<%
								}
								%>
						  	</tbody>
						<%} else { %>
							<tbody>
								<tr>
									<td colspan="<%=colspan%>">N&atilde;o h&aacute; arquivos</td>
								</tr>
							</tbody>
						<%}%>
					</table>
				</fieldset>
			<%} else {%>
				<h2>Pend&ecirc;ncia n&atilde;o encontrada</h2>
			<%}%>
		</fieldset>
	</div>
 </div>
</body>
</html>
