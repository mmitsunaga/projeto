<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>

<%@page import="java.util.ArrayList"%><html>
	<head>
		<title>Processo</title>       
		
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type='text/javascript' src='./js/tabelas.js'></script>
   		<script type='text/javascript' src='./js/tabelaArquivos.js'></script> 
	</head>

	<body>	
		
		<script type="text/javascript">
			function carregarApletImprimirEtiqueta(){
				$( ".divApletImprimrEtiqueta" ).html("<object name='ImprimirEtiqueta'  id='ImprimirEtiqueta' type='application/x-java-applet' width='0' height='0' align='top'  style='z-index:-100'> <param name='code' value='ImprimirEtiqueta'> <param name='archive' value='./applet/ImprimirEtiqueta.jar'> <param name='mayscript' value='yes'> <param name='scriptable' value='true'> <param name='name' value='ImprimirEtiqueta'> <param name='java_arguments' value='-Djnlp.packEnabled=true'/> </object>"); 	
			}
		</script>
		
		<div id="divApletImprimrEtiqueta" class="divApletImprimrEtiqueta" ></div>
	
		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Partes no Processo </h2></div>
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados" style="background-color:#eee"> 
					<legend class="formEdicaoLegenda">Partes no Processo</legend>
					
					<label class="formEdicaoLabel"> Processo </label><br>
					<span class="span"> <a href="<%=request.getAttribute("tempRetorno")%>"><%=processoDt.getProcessoNumero()%></a> </span>
	
					<!-- PROMOVENTES -->
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Polo Ativo </legend>
					<%
						List listaPromoventes = processoDt.getListaPolosAtivos();
									if (listaPromoventes != null){
										for (int i=0; i<listaPromoventes.size();i++){
											ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
					%>   			 	    				
							<div> Nome </div> 
							<span class="span1">
								<%=parteDt.getNome()%>
								<% if (!parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
							</span>
							
							<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
							<div> CPF </div> 
							<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
							
							<div style="width:50px">
								<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
								<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
								<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
									<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
								</a> 
								<% } %>	 			
							</div>							
							<br />
							
							<div> Filia&ccedil;&atilde;o </div>
							<span class="span1"><%=parteDt.getNomeMae()%></span>
							
							<div> Dt. Nascimento </div> 
							<span class="span2"><%=parteDt.getDataNascimento()%></span>
							
							<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
								<fieldset class="fieldsetEndereco">
									<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
									<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
									<%=parteDt.getEnderecoParte().getCep()%><br />
									<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	   		    		
								</fieldset>
								<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
									<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
									'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
									'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
									'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
									'<%=parteDt.getEnderecoParte().getComplemento() %>')">
								<%} %>
							</div>
							
							<% } %>
							<br />
							<div style="width:100%; margin:3px 0"></div>
							
						<%
							}
						}
						%>
					</fieldset>
					
					<!-- PROMOVIDOS -->
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> Polo Passivo </legend>
					<%
						List listaPromovidos = processoDt.getListaPolosPassivos();
									if (listaPromovidos != null){
										for (int i=0; i<listaPromovidos.size();i++){
											ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					%>   				
							<div> Nome </div> 
							<span class="span1">
							<%=parteDt.getNome()%>
							<% if (!parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
							</span>
							
							<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
							<div> CPF </div>
							<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
							
							<div style="width:50px">
								<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
								<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
								<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
									<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
								</a> 
								<% } %>	 			
							</div><br />
							
							<div> Filia&ccedil;&atilde;o </div> 
							<span class="span1"><%=parteDt.getNomeMae()%></span>
							
							<div> Dt. Nascimento </div> 
							<span class="span2"><%=parteDt.getDataNascimento()%></span>
							
							<div id="sub<%=parteDt.getNome()%>" class="DivInvisivel">
								<fieldset class="fieldsetEndereco">
									<legend> Endereço </legend>
									<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
									<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
									<%=parteDt.getEnderecoParte().getCep()%><br />
									<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	 	
								</fieldset>
								<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
									<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
									'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
									'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
									'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
									'<%=parteDt.getEnderecoParte().getComplemento() %>')">
								<%} %>
							</div>
							
							<% } %>
							<br />
							<div style="width:100%; margin:3px 0"></div>
						<% 	
							}
						}
						%>
					</fieldset> 

					<%
						List listaOutrasPartes = processoDt.getListaOutrasPartes();
						if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
					%>
						<!-- OUTRAS PARTES -->
						
						<%
				   		List litisconsorteAtivos = new ArrayList();
				   		List litisconsortePassivos = new ArrayList();
				   		List substitutosProcessual = new ArrayList();
				   		List comunicantes = new ArrayList();
				   		List curadores = new ArrayList();
				   		List terceiros = new ArrayList();
				   		List testemunhas = new ArrayList();
				   		List outros = new ArrayList();
				   		
				   		String strLitisconsorteAtivo = "";
				   		String strLitisconsortePassivo = "";
				   		String strSubstitutoProcessual = "";
				   		String strComunicante = "";
				   		String strCurador = "";
				   		String strTerceiro = "";
				   		String strTestemunha = "";
				   		
				   		for (int i=0; i<listaOutrasPartes.size();i++){
			   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
			   			  	if (parteDt.getProcessoParteTipoCodigo() != null) {
			   			  		int parteTipoCodigo =  Funcoes.StringToInt(parteDt.getProcessoParteTipoCodigo());
				   			  	switch (parteTipoCodigo) {
					   			 	case ProcessoParteTipoDt.LITIS_CONSORTE_ATIVO:
						   				strLitisconsorteAtivo = (String) parteDt.getProcessoParteTipo();
				   			  			litisconsorteAtivos.add(parteDt);
					 					break;
					   				case ProcessoParteTipoDt.LITIS_CONSORTE_PASSIVO:
						   				strLitisconsortePassivo = (String) parteDt.getProcessoParteTipo();
				   			  			litisconsortePassivos.add(parteDt);
						 				break;
					   				case ProcessoParteTipoDt.SUBSTITUTO_PROCESSUAL:
					   					strSubstitutoProcessual = (String) parteDt.getProcessoParteTipo();
				   			  			substitutosProcessual.add(parteDt);
				   			  			break;
					   				case ProcessoParteTipoDt.COMUNICANTE:
					   					strComunicante = (String) parteDt.getProcessoParteTipo();
					   					comunicantes.add(parteDt);
					   					break;
					   				case ProcessoParteTipoDt.CURADOR:
					   					strCurador = (String) parteDt.getProcessoParteTipo();
					   					curadores.add(parteDt);
					   					break;
					   				case ProcessoParteTipoDt.TERCEIRO:
					   					strTerceiro = (String) parteDt.getProcessoParteTipo();
					   					terceiros.add(parteDt);
					   					break;
					   				case ProcessoParteTipoDt.TESTEMUNHA:
					   					strTestemunha = (String) parteDt.getProcessoParteTipo();
					   					testemunhas.add(parteDt);
					   					break;
					   				default:
					   					outros.add(parteDt);
					   					break;
				   			  	}
			   			  	}
	   					} %>
	   					
	   				<% if (litisconsorteAtivos != null && litisconsorteAtivos.size() > 0 ){%>
	   					<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend><%=strLitisconsorteAtivo%></legend>
							<%
								for (int i=0; i<litisconsorteAtivos.size();i++){
									ProcessoParteDt parteDt = (ProcessoParteDt)litisconsorteAtivos.get(i);%>	   				
								<div> Nome </div>	    	
								<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
								
								 
								<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
								<div> CPF </div>
								<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
								
								<div style="width:50px">
									<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
										<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
									</a> 
									<% } %>	 			
								</div><br />
							
								<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
									<fieldset class="fieldsetEndereco">
										<legend> Endereço </legend>
										<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
										<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
										<%=parteDt.getEnderecoParte().getCep()%><br />
										<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
									</fieldset>
									<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
										<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
										'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
										'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
										'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
										'<%=parteDt.getEnderecoParte().getComplemento() %>')">
									<%} %>
								</div>
							<%	} else { %>
							<br />
							<%	
								}
							 }
							%>
						</fieldset>
					<%} %>
	   					
	   					 <% if (litisconsortePassivos != null && litisconsortePassivos.size() > 0 ){%>
		   					<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strLitisconsortePassivo%></legend>
								<%
									for (int i=0; i<litisconsortePassivos.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)litisconsortePassivos.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
										<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
										<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
											<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
										</a> 
										<% } %>	 			
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%} %>
	   					
	   					<%if (substitutosProcessual != null && substitutosProcessual.size() > 0) { %>
		   					<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strSubstitutoProcessual%></legend>
								<%
									for (int i=0; i<substitutosProcessual.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)substitutosProcessual.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%} %>
						
						<%if (comunicantes != null && comunicantes.size() > 0) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strComunicante%></legend>
								<%
									for (int i=0; i<comunicantes.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)comunicantes.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%} %>
	   					
	   					<%if (curadores != null && curadores.size() > 0) { %>
		   					<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strCurador%></legend>
								<%
									for (int i=0; i<curadores.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)curadores.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%} %>
	   					
	   					<%if (terceiros != null && terceiros.size() > 0) { %>
		   					<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strTerceiro%></legend>
								<%
									for (int i=0; i<terceiros.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)terceiros.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%} %>
	   					
	   					<%if (testemunhas != null && testemunhas.size() > 0) { %>
		   					<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend><%=strTestemunha%></legend>
								<%
									for (int i=0; i<testemunhas.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)testemunhas.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset> 
						<%} %>
						
						<%if (outros != null && outros.size() > 0) { %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
								<legend> Outras Partes / Sujeitos </legend>
								<%
									for (int i=0; i<outros.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)outros.get(i);%>	   				
									<div> Nome </div>	    	
									<span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
									
									 
									<% if (!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									<div> CPF </div>
									<span class="span2"><%=parteDt.getCpfCnpjFormatado()%></span>
									
									<div style="width:50px">
										<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									</div><br />
								
									<div id="sub<%=parteDt.getNome()%>"  class="DivInvisivel">
										<fieldset class="fieldsetEndereco">
											<legend> Endereço </legend>
											<%=parteDt.getEnderecoParte().getLogradouro() + " nº " + parteDt.getEnderecoParte().getNumero() + " " + parteDt.getEnderecoParte().getComplemento()%><br />
											<%=parteDt.getEnderecoParte().getBairro() + " " + parteDt.getEnderecoParte().getCidade() + " " + parteDt.getEnderecoParte().getUf()%><br />
											<%=parteDt.getEnderecoParte().getCep()%><br />
											<%=parteDt.getEMail() + " " + parteDt.getTelefone()%>  	    	
										</fieldset>
										<%if (UsuarioSessao.isPodeCarregarApplet()){ %>
											<input type="button" name="Imprimir" value="Imprimir" title="Clique para imprimir o Endereço" OnClick="carregarApletImprimirEtiqueta(); javascript:DoPrintingApplet('<%=processoDt.getProcessoNumeroCompleto() %>','<%=parteDt.getNome() %>',
											'<%=parteDt.getEnderecoParte().getBairro()%>','<%=parteDt.getEnderecoParte().getCidade()%>',
											'<%=parteDt.getEnderecoParte().getUf()%>','<%=parteDt.getEnderecoParte().getLogradouro()%>',
											'<%=parteDt.getEnderecoParte().getNumero()%>','<%=parteDt.getEnderecoParte().getCep() %>',
											'<%=parteDt.getEnderecoParte().getComplemento() %>')">
										<%} %>
									</div>
								<%	} else { %>
								<br />
								<%	
									}
								 }
								%>
							</fieldset>
						<%}%>
					<% 	} %>
				</fieldset>					
			</div>		
		</div>
	</body>
</html>