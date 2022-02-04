<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"%>
<%@page import="java.util.Date"%>

<jsp:useBean id="ProcessoCadastroDt" scope="request" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">	
	
			<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
		
				<h2 align="center"> Processo de 2º Grau cadastrado com sucesso </h2>
				
				<div> N&uacute;mero Processo</div>
				<span><a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=ProcessoCadastroDt.getId_Processo()%>"><%=ProcessoCadastroDt.getProcessoNumeroCompleto()%></a></span/><br />
				
				<!-- PROMOVENTES -->
				<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   		<legend> Polo Ativo </legend>
				
			   		<%
							   			List listaPromoventes = ProcessoCadastroDt.getListaPolosAtivos();
							   			   	    		for (int i=0; i < listaPromoventes.size();i++){
							   			   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
							   		%>   	
			   		<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend> <%=parteDt.getNome() %></legend>
			       		
			       			<div> CPF/CNPJ </div>  <span> <%=parteDt.getCpfCnpjFormatado()%></span>
			  				<div>Identidade </div> <span><%=parteDt.getRg() %></span/><br />
			  				
			       			<div> Endereço </div>
			       			<span class="spanEndereco">
								<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
								<%=parteDt.getEnderecoParte().getBairro() %> 
								<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
								<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
							</span>
					</fieldset>
			       	<% 	} %>
				</fieldset>
			
				<!-- PROMOVIDOS -->
				<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   		<legend> Polo Passivo </legend>
				   	<%
				   		List listaPromovidos = ProcessoCadastroDt.getListaPolosPassivos();
				   				   		for (int j=0;j<listaPromovidos.size();j++){
				   			   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(j);
				   	%>	 
			    	<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend> <%=parteDt.getNome() %></legend>
			       		
			       			<div> CPF/CNPJ </div> <span> <%=parteDt.getCpfCnpjFormatado()%></span>
			  				<div>Identidade </div> <span><%=parteDt.getRg() %></span/><br />
			  				
			       			<div> Endereço </div>
			       			<span class="spanEndereco">
								<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
								<%=parteDt.getEnderecoParte().getBairro() %> 
								<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
								<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
							</span>
					</fieldset>
			       	<% 	}   %>
				</fieldset>
				
				<!-- OUTRAS PARTES -->
				<%
				   	List listaOutrasPartes = ProcessoCadastroDt.getListaOutrasPartes();
					if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){%>
					<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend>  Outras Partes / Sujeitos </legend>
				   		<%
					   		for (int j=0;j<listaOutrasPartes.size();j++){
				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(j);
				   		%>	 
			    		<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   				<legend> <%= parteDt.getProcessoParteTipo()+ " - " + parteDt.getNome() %></legend>
			       		
		       				<div> CPF/CNPJ </div> <span> <%=parteDt.getCpfCnpjFormatado()%></span>
		  					<div>Tipo </div> <span><%=parteDt.getProcessoParteTipo() %></span><br />
		       				<div> Endereço </div>
		       				<span class="spanEndereco">
								<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
								<%=parteDt.getEnderecoParte().getBairro() %> 
								<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
								<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
							</span>
						</fieldset>
			       		<% 	}   %>
					</fieldset>
				<% 	} %>
				
				<%    
													
				   	List listaAdvogados = ProcessoCadastroDt.getListaAdvogados();
				   	if (listaAdvogados != null && listaAdvogados.size() > 0){%>
					<fieldset id="VisualizaDados" class="VisualizaDados">
			   			<legend> Advogado(s) </legend>
						<% for (int i=0;i < listaAdvogados.size();i++){
			   			  	UsuarioServentiaOabDt advogadoDt = (UsuarioServentiaOabDt)listaAdvogados.get(i); %>
				       			<div style="margin-left:-30px"> Advogado </div>
			       		 		<span><%=advogadoDt.getNomeUsuario()%></span>	
				       		 		
				       			<div> OAB/Matrícula </div>
			       				<span><%=advogadoDt.getOabNumero()%></span><br />	
				       <% 	}  %>
					</fieldset>
				<% } %>
		    	
		    	<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
		    		<legend> Outras Informações </legend>
		    		
			   		<div> Ju&iacute;zo </div>
					<span><%= ProcessoCadastroDt.getServentiaDt().getServentia()%></span/><br />
					
			   		<div> Classe </div>
		    	  	<span><%=ProcessoCadastroDt.getProcessoTipo()%> </span/><br />
					
					<div> Assunto(s) </div>
					<%
 					List listaAssuntos = ProcessoCadastroDt.getListaAssuntos();
 	    			if (listaAssuntos != null && listaAssuntos.size() > 0){ %>
	    			<span style="width: 750px; height: auto;">
 						<table border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
 							<tbody>
							<%
 	    						for (int i=0;i < listaAssuntos.size();i++){
 	    						ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(i); %>
  									<tr><td><%=assuntoDt.getAssunto()%></td></tr>
   							<% 	}
 							} %>
							</tbody>
						</table>
					</span>
 					<br />
 					
 					<div> Valor da Causa</div>
					<span><%=ProcessoCadastroDt.getValor()%></span>
		   							
					<div> Data Distribui&ccedil;&atilde;o </div>
					<span><%=Funcoes.dateToStringSoData(new Date())%></span><br />
					
					<div> Prioridade</div>
					<span><%=ProcessoCadastroDt.getProcessoPrioridade()%></span>
					
					<div> Segredo de Justi&ccedil;a</div>
					<span><%=(ProcessoCadastroDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span><br />
					
					<% if (ProcessoCadastroDt.getProcessoDependenteDt() != null) {%>
					<div> Processo Originário</div>
					<span><%=ProcessoCadastroDt.getProcessoDependenteDt().getProcessoNumero()%></span><br />
					<% } %>	
				</fieldset>
				<br />
				<br />
				<br />
			
				<blockquote id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgImprimir" type="submit" value="Imprimir" OnClick="javascript:Imprimir()"> 
				</blockquote>
		
			</fieldset>
		</div>
	</body>
</html>	

