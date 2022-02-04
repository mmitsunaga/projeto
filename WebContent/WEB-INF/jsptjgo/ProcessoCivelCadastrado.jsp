<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="java.util.Date"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="ProcessoCadastroDt" scope="request" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">	
			<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
		
				<h2 align="center"> Processo cadastrado com sucesso </h2>
				
				<div> N&uacute;mero Processo</div>
				<span><a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=ProcessoCadastroDt.getId_Processo()%>"><%=ProcessoCadastroDt.getProcessoNumeroCompleto()%></a></span><br />
				
				<!-- PROMOVENTES -->
				<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   		<legend> Polo Ativo </legend>
				
			   		<%
							   			List listaPromoventes = ProcessoCadastroDt.getListaPolosAtivos();
							   			   	    		for (int i=0; i < listaPromoventes.size();i++){
							   			   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
							   		%>   	
			   		<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend> <%=parteDt.getNome()%></legend>
			       		
			       		<div> CPF/CNPJ </div>  <span> <%=parteDt.getCpfCnpjFormatado()%></span>
			  			<div>Identidade </div> <span><%=parteDt.getRg()%></span><br />
			  			<div> Endereço </div>
		       			<span class="spanEndereco">
							<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
							<%=parteDt.getEnderecoParte().getBairro()%> 
							<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
							<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
						</span>
					</fieldset>
			       	<%
			       		}
			       	%>
				</fieldset>
			
				<!-- PROMOVIDOS -->
				<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   		<legend> Polo Passivo </legend>
				   	<%
				   		List listaPromovidos = ProcessoCadastroDt.getListaPolosPassivos();
				   			   			   		if (listaPromovidos != null && listaPromovidos.size() > 0){
				   			   				   		for (int j=0;j<listaPromovidos.size();j++){
				   			   			   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(j);
				   	%>	 
			    	<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend> <%=parteDt.getNome()%></legend>
			       		
		       			<div> CPF/CNPJ </div> <span> <%=parteDt.getCpfCnpjFormatado()%></span>
		  				<div>Identidade </div> <span><%=parteDt.getRg()%></span><br />
		       			<div> Endereço </div>
		       			<span class="spanEndereco">
							<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
							<%=parteDt.getEnderecoParte().getBairro()%> 
							<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
							<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
						</span>
					</fieldset>
			       	<%
			       		}
			       				}
			       	%>
				</fieldset>
				
				<!-- OUTRAS PARTES -->
				<%
					List listaOutrasPartes = ProcessoCadastroDt.getListaOutrasPartes();
							if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
				%>
					<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   			<legend> Outras Partes / Sujeitos </legend>
				   		<%
				   			for (int j=0;j<listaOutrasPartes.size();j++){
				   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(j);
				   		%>	 
			    		<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			   				<legend> <%=parteDt.getProcessoParteTipo()+ " - " + parteDt.getNome()%></legend>
			       		
		       				<div> CPF/CNPJ </div> <span> <%=parteDt.getCpfCnpjFormatado()%></span>
		  					<div>Tipo </div> <span><%=parteDt.getProcessoParteTipo()%></span><br />
		       				<div> Endereço </div>
		       				<span class="spanEndereco">
								<%=parteDt.getEnderecoParte().getLogradouro() + " Nº " + parteDt.getEnderecoParte().getNumero() + parteDt.getEnderecoParte().getComplemento()%> 
								<%=parteDt.getEnderecoParte().getBairro()%> 
								<%=parteDt.getEnderecoParte().getCidade() + "-" + parteDt.getEnderecoParte().getUf() + " CEP: " + parteDt.getEnderecoParte().getCep()%> 
								<%=parteDt.getTelefone() + "  " + parteDt.getEMail()%>
							</span>
						</fieldset>
			       		<%
			       			}
			       		%>
					</fieldset>
				<%
					}
				%>
		    	
		    	<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
		    		<legend> Outras Informações </legend>
		    		
			   		<div> Ju&iacute;zo </div>
					<span><%=ProcessoCadastroDt.getServentiaDt().getServentia()%></span><br />
					
			   		<div> Classe </div>
		    	  	<span><%=ProcessoCadastroDt.getProcessoTipo()%> </span><br />
					
					<div> Assunto(s) </div>
					<%
						List listaAssuntos = ProcessoCadastroDt.getListaAssuntos();
					   	    				if (listaAssuntos != null && listaAssuntos.size() > 0){
					%>
		   				<table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;padding-left: 10px;">
		   					<tbody>
							<%
								for (int i=0;i < listaAssuntos.size();i++){
									   	    			ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(i);
							%>
									<tr>
										<td><b><%=assuntoDt.getAssunto()%></b></td>
									</tr>
				       		<%
				       			}
				       		%>
		       				</tbody>
		   				</table>
		   			<%
		   				}
		   			%>
		   							
					<div> Valor da Causa</div>
					<span><%=ProcessoCadastroDt.getValor()%></span>
					
					<div> Data Distribui&ccedil;&atilde;o </div>
					<span><%=Funcoes.dateToStringSoData(new Date())%></span><br />
					
					<div> Prioridade</div>
					<span><%=ProcessoCadastroDt.getProcessoPrioridade()%></span>	
					
					<div> Segredo de Justi&ccedil;a</div>
					<span><%=(ProcessoCadastroDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span><br />
					
					<div> Processo Principal</div>
					<span><%=ProcessoCadastroDt.getProcessoNumeroPrincipal()%></span><br />	
				</fieldset><br />
				<br />
				
				<%
									List preventos = ProcessoCadastroDt.getListaPreventos();
										if (preventos != null && preventos.size() > 0){
								%>
				<table border="1" width="95%" align="center" cellspacing="0" cellpadding="0">
					<thead>
						<tr align="center">
							<th colspan="3">Possíveis Preventos</th>
						</tr>
						<tr align="center">
							<th>Processo</th>
							<th>Serventia</th>
							<th>Data Distribui&ccedil;&atilde;o</th>
						</tr>
					</thead>
					<tbody>
					<%
						for (int i = 0; i < preventos.size(); i++) {
									ProcessoDt objPrevento = (ProcessoDt) preventos.get(i);
					%>
						<tr align="center">
							<td><a href="BuscaProcesso?Id_Processo=<%=objPrevento.getId()%>"><%=objPrevento.getProcessoNumero()%></a></td>
							<td><%=objPrevento.getServentia()%></td>
							<td><%=objPrevento.getDataRecebimento()%></td>
						</tr>
					<%
						}
					%>
					</tbody>	
				</table>
				<br />
				<%
					}
				%>
		
				<%
							if ((ProcessoCadastroDt.getAudienciaDt() != null) && (ProcessoCadastroDt.getAudienciaDt().getId().length() > 0)) {	
									ServentiaDt serventiaDt = ProcessoCadastroDt.getServentiaDt();
						%>
				<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			
					<b>Foi designada Audiência de <%=ProcessoCadastroDt.getAudienciaDt().getAudienciaTipo()%> para o dia <%=ProcessoCadastroDt.getAudienciaDt().getDataAgendada()%> 
					no(a) <%=serventiaDt.getServentia()%>, local:</b/><br />
					<%=serventiaDt.getLogradouro() + " N&ordm; " + serventiaDt.getNumero() + "  " + serventiaDt.getComplemento()%><br />
					<%=serventiaDt.getBairro() + "  " + serventiaDt.getCidade() + "-" + serventiaDt.getUf()%><br />
					<%="CEP:" + serventiaDt.getCep()%><br />
					<b>estando a(s) Polo Ativo devidamente intimada(s).</b>
		
					<p>Ciente(s):</p><br />
					<br />
				
					<%
										List promoventes = ProcessoCadastroDt.getListaPolosAtivos();
														for (int i=0;i<promoventes.size();i++){
															ProcessoParteDt parteDt = (ProcessoParteDt)promoventes.get(i);
									%>
							<center> <hr width="300"> <%= parteDt.getNome() %> </center>
							<br />
							<br />
					<% 		 } %>
				</fieldset>	
				<%	}%><br />
			
				<blockquote id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgImprimir" type="submit" value="Imprimir" OnClick="javascript:Imprimir()"> 
				</blockquote>
		
			</fieldset>
		</div>
	</body>
</html>	

