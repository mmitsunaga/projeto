<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
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
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Confirmação de Dados </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<%	int grupo = Funcoes.StringToInt(((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).getGrupoCodigo()); %>
				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">				
				<!-- Variáveis para controlar Passos do Cadastro -->
				<input id="Passo1" name="Passo1" type="hidden" value="<%=ProcessoCadastroDt.getPasso1()%>">
				<input id="Passo2" name="Passo2" type="hidden" value="<%=ProcessoCadastroDt.getPasso2()%>">
				<input id="Passo3" name="Passo3" type="hidden" value="<%=ProcessoCadastroDt.getPasso3()%>">

		  	 	<div id="divEditar" class="divEditar">
		   			<% if (!ProcessoCadastroDt.getPasso1().equals("")){ %>
					<input name="imgPasso1" type="submit" value="<%=ProcessoCadastroDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
					<% } if (!ProcessoCadastroDt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=ProcessoCadastroDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<% } if (!ProcessoCadastroDt.getPasso3().equals("")){ %>				
					<input name="imgPasso3" type="submit" value="<%=ProcessoCadastroDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','2');">
					<% } %>
				
					<fieldset id="Editar">
						<legend> Dados do Processo </legend>
						
						<!-- PROMOVENTES -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> Polo Ativo </legend>
				   		<%
				   			List listaPromoventes = ProcessoCadastroDt.getListaPolosAtivos();
				   				   	    	if (listaPromoventes != null){
				   				   	    		for (int i=0;i<listaPromoventes.size();i++){
				   				   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
				   		%>
					       	<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
					       	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpj()%></span>
					        <div><a href="javascript: MostrarOcultar('sub<%=parteDt.getNome()%>')" title="Mostrar/Ocultar"> Endereço </a></div/><br />
					 		
					  		<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
	  							<fieldset class="fieldsetEndereco">
					  				<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
					    			<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
					    			<%=parteDt.getEnderecoParte().getCep() + " " + parteDt.getEMail() + " " + parteDt.getTelefone() %>				
								</fieldset>
							</div>
					       <% 		} %>
						</fieldset>
				   		<% } %>	  
					
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> Polo Passivo </legend>
					   	<%
					   		List listaPromovidos = ProcessoCadastroDt.getListaPolosPassivos();
					   				   		if (listaPromovidos != null){
					   				   			for (int i=0;i<listaPromovidos.size();i++){
					   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					   	%>
					    	<div> Nome </div> <span class="span1"><%=parteDt.getNome()%></span>
					       	<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpj()%></span>
				   	        <div><a href="javascript: MostrarOcultar('sub<%=parteDt.getNome()%>')" title="Mostrar/Ocultar"> Endereço </a></div/><br />
					 		
					  		<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
		  						<fieldset class="fieldsetEndereco">
						  			<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
						    		<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
							   		<%=parteDt.getEnderecoParte().getCep() + " " + parteDt.getEMail() + " " + parteDt.getTelefone() %>
								</fieldset>
							</div>
					       <% 	}   %>
						</fieldset>
					    <%  
					    	} 
					   	%>
					   	
					   <!-- OUTRAS PARTES -->
						<%
					   	List listaOutrasPartes = ProcessoCadastroDt.getListaOutrasPartes();
					   	if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){ %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
					   		<legend> Outras Partes / Sujeitos </legend>
				   			<%
				   			for (int i=0;i<listaOutrasPartes.size();i++){
				   				ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i); %>
				    			<div> Nome </div> <span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
				       			<div> CPF </div> <span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
			   	        		<div><a href="javascript: MostrarOcultar('sub<%=parteDt.getNome()%>')" title="Mostrar/Ocultar"> Endereço </a></div/><br />
				 		
				  				<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
  									<fieldset class="fieldsetEndereco">
					  					<legend> Endereço </legend>
										<%=parteDt.getEnderecoParte().getLogradouro() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
					    				<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
						   				<%=parteDt.getEnderecoParte().getCep() + " " + parteDt.getEMail() + " " + parteDt.getTelefone() %>
									</fieldset>
								</div>
				   			<% 	}	%>
						</fieldset>
					    <%  } 	  

			   			List listaAdvogados = ProcessoCadastroDt.getListaAdvogados();
					   	if (listaAdvogados != null && listaAdvogados.size() > 0){%>
						<fieldset id="VisualizaDados" class="VisualizaDados">
				   			<legend> Advogado(s) </legend>
							<% for (int i=0;i < listaAdvogados.size();i++){
				   			  	ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt)listaAdvogados.get(i); %>
					       			<div style="margin-left:-30px"> Advogado </div>
				       		 		<span><%=advogadoDt.getNomeAdvogado()%></span>	
					       		 		
					       			<div> Parte </div>
				       				<span><%=advogadoDt.getNomeParte()%></span/><br />	
					       <% 	}  %>
						</fieldset>
						<% } %>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Arquivo(s)</legend>
							<% 
								List arquivos = ProcessoCadastroDt.getListaArquivos();
								if (arquivos != null){
									for(int i = 0 ; i< arquivos.size();i++) {
								  		ArquivoDt arquivo = (ArquivoDt)arquivos.get(i); 
							%>
							<div> Tipo </div> <span> <%=arquivo.getArquivoTipo()%> </span>
							<div> Nome </div> <span> <%= arquivo.getNomeArquivoFormatado() %> </span/><br />
						  	<%
									}
							}
						%>
						</fieldset>
			    	
				    	<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Outras Informações </legend>
				    		<% if (ProcessoCadastroDt.isProcessoDependente()) {%>
				    		<div> Serventia </div>
							<span style="width: 300px;"><%= ProcessoCadastroDt.getProcessoDependenteDt().getAreaDistribuicao()%></span>
				    		<% } else { %>
					   		<div> Área Distribuição </div>
							<span style="width: 300px;"><%= ProcessoCadastroDt.getAreaDistribuicao()%></span>
							<% } %><br />
							
					   		<div> Tipo de A&ccedil;&atilde;o</div>
				    	  	<span><%=ProcessoCadastroDt.getProcessoTipo()%> </span/><br />
							
							<div> Segredo de Justi&ccedil;a</div>
							<span><%=(ProcessoCadastroDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span/>
							
							<div> Prioridade</div>
							<span><%=ProcessoCadastroDt.getProcessoPrioridade()%></span><br />
							
							<div> Protocolo SSP </div> 
							<span><%=ProcessoCadastroDt.getTcoNumero()%></span/><br />	
						</fieldset>
			
						<% if (grupo == GrupoDt.AUTORIDADES_POLICIAIS){ %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Intima&ccedil;&atilde;o em Delegacia </legend>
							<%
								List listaGeral = ProcessoCadastroDt.getPartesAtivasPassivas();
													for (int i=0;i < listaGeral.size();i++){
														ProcessoParteDt parteDt = (ProcessoParteDt)listaGeral.get(i);
							%>
							<div><input type="checkbox" name="partesIntimadas" value="<%=parteDt.getNome()%>"  
							<%=(parteDt.getProcessoParteTipoCodigo().equals(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))?"checked":"")%>></div>
							<span><%=parteDt.getNome()%></span/><br />
				   			<%
					         	}
							%>
							
						</fieldset/><br />
						<%} %>
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Opções</legend>
						    <div><input class="formEdicaoInput" name="AudienciaConciliacao" id="AudienciaConciliacao" type="checkbox" value="true"/></div>
							<span title="Marque essa opção se for necessário audiencia preliminar com conciliador">Marcar Audiencia Preliminar com Conciliador</span>
				    		<br />
						<% if (grupo != GrupoDt.AUTORIDADES_POLICIAIS){ %>
						    <div><input class="formEdicaoInput" name="NaoMarcarAudiencia" id="NaoMarcarAudiencia" type="checkbox" value="false" <%=(ProcessoCadastroDt.MarcarAudiencia()?"":"checked")%>/></div> 
				    		<span title="Marque essa opção se não deseja que seja marcada uma Audiência automaticamente">Não Marcar Audiência</span>
				    		<% if (grupo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL || grupo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL || grupo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL || grupo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL){ %> <br />
						    <div><input class="formEdicaoInput" name="NaoMandarConcluso" id="NaoMandarConcluso" type="checkbox" value="false" <%=(ProcessoCadastroDt.MandarConcluso()?"":"checked")%>/></div>
							<span title="Marque essa opção se não deseja remeter Autos Conclusos">Não remeter Autos Conclusos</span>
							<%	} %>
				    		<br />
				    	<%	} %>
						</fieldset>
				    				
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Confirmar" onclick="AlterarValue('PaginaAtual',<%=Configuracao.SalvarResultado%>);"> 
							<input name="imgInserir" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual',<%=Configuracao.Novo%>);">
						</div>
					</fieldset>
				</div>
			</form>
		</div>
	</body>
</html>