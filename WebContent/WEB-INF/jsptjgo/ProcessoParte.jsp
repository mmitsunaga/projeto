<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="ProcessoPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	</head>

	<body>	
		
		<script type="text/javascript">
			function carregarApletImprimirEtiqueta(){
				$( ".divApletImprimrEtiqueta" ).html("<object name='ImprimirEtiqueta'  id='ImprimirEtiqueta' type='application/x-java-applet' width='0' height='0' align='top'  style='z-index:-100'> <param name='code' value='ImprimirEtiqueta'> <param name='archive' value='./applet/ImprimirEtiqueta.jar'> <param name='mayscript' value='yes'> <param name='scriptable' value='true'> <param name='name' value='ImprimirEtiqueta'> <param name='java_arguments' value='-Djnlp.packEnabled=true'/> </object>"); 	
			}
		</script>
		
		<div id="divApletImprimrEtiqueta" class="divApletImprimrEtiqueta" ></div>
		
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Alterar Partes no Processo</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="Hash" name="Hash" type="hidden" value="<%= Funcoes.GeraHashMd5(processoDt.getId_Processo()) %>" />
				<input id="HashParte" name="HashParte" type="hidden" value="<%=Funcoes.GeraHashMd5(ProcessoPartedt.getId_ProcessoParte())%>" />
			
				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Alterar Partes no Processo</legend>
					    
					    <label class="formEdicaoLabel"> Processo </label><br>
					    <span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a> </span>
					    
				    	<!-- PROMOVENTES -->
		  				<input type="hidden" id="posicaoLista" name="posicaoLista">
			  	
		  				<fieldset id="VisualizaDados" class="VisualizaDados">   
		  					<legend> Polo Ativo 
		  						<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
   								<input class="FormEdicaoimgLocalizar" id="imaLocalizarPartePromovente" name="imaLocalizarPartePromovente" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_ATIVO_CODIGO%>');AlterarValue('PassoEditar','0');" title="Cadastrar Novo Promovente"/>
   								<% } %> 
   							</legend>
							<%
								List listaPromoventes = processoDt.getListaPolosAtivos();
										   	    	if (listaPromoventes != null){
										   	    		for (int i=0;i < listaPromoventes.size();i++){
											   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
							%>
							       	<div> Nome </div> 
							       	<span class="span1"><%=parteDt.getNome()%>
							       	<%	if (parteDt.isParteIsenta()){ %> <font color="red">&nbsp;(Isenta)</font> <% } %></span>
							       	
							   	<%	if (parteDt.getId().length()> 0){
								   		if (parteDt.getDataBaixa().equals("")) { %>
					        			<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados Promovente">
								       	<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_ATIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Promovente"/>
								       	<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_ATIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Promovente"/>
								       	
								       	<% if (!parteDt.getAusenciaProcessoParte().equals("")){ %> 
			      		   				<div><font color="red"><%=parteDt.getAusenciaProcessoParte()%></font></div> 
			      		   				<span class="span2">
				   							<input name="imgRetirar" class="imgRetirar" type="image" src="./imagens/imgVoltarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_ATIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','1');" title="Retirar Contumácia"/>
				   						</span>
				   						<%}%>
				   						<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
					   						<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
					   						<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
	 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
													<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
												</a> 
											<% } %>	
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
										<%}%>
									<%	} else { %>
								       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_ATIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');" 
								       	title="Restaurar Promovente">Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>
						       	<% 		}
							   		} %>
								   		
			   						<br />
					   	    		<%}
			  					}
			  				%>
						</fieldset>
			
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
			  				<legend> Polo Passivo 
			  					<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
				   				<input class="FormEdicaoimgLocalizar" id="imaLocalizarPartePromovido" name="imaLocalizarPartePromovido" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('PassoEditar','0');" title="Cadastrar Novo Promovido"/>
				   				<% } %>
							</legend>
					 		<%
					 			List listaPromovidos = processoDt.getListaPolosPassivos();
					 					   			if (listaPromovidos != null){
					 					   				for (int i=0;i < listaPromovidos.size();i++){
					 					   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					 		%>
				   					<div> Nome </div> 
				   					<span class="span1"><%=parteDt.getNome()%></span>
				      		    <%	if (parteDt.getId().length()> 0){
				      		    		if (parteDt.getDataBaixa().equals("")) { %>
					      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados Promovido">
							   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Promovido"/>
								       	<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Promovido"/>
							   			
							   			<%if (!parteDt.getAusenciaProcessoParte().equals("")){ %> 
					      		   			<div><font color="red"><%=parteDt.getAusenciaProcessoParte()%></font></div> 
					      		   			<span class="span2">
					   							<input name="imgRetirar" class="imgRetirar" type="image" src="./imagens/imgVoltarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','1');" title="Retirar Revelia"/>
					   						</span>
					   					<%} %>
					   					<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
						   					<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
						   					<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
		 										<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
													<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
												</a> 
											<% } %>
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
										<%}%>
									<%	} else { %>
										<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','<%=ProcessoParteTipoDt.POLO_PASSIVO_CODIGO%>');AlterarValue('posicaoLista','<%=i%>');" 
										title="Restaurar Promovido">Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>
						       	<% 		}
				      		    	} %>
					      		    	
					   				<br />
					   				<% }   
			  					} 
			  				%>	
						</fieldset/>
						
						<!-- OUTRAS PARTES -->
						
						<%
						List listaOutrasPartes = processoDt.getListaOutrasPartes();
						if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
						
					   		List litisconsorteAtivos = new ArrayList();
					   		List litisconsortePassivos = new ArrayList();
					   		List substitutosProcessual = new ArrayList();
					   		List comunicantes = new ArrayList();
					   		List curadores = new ArrayList();
					   		List terceiros = new ArrayList();
					   		List testemunhas = new ArrayList();
					   		List pacientes = new ArrayList();
					   		List outros = new ArrayList();
					   		
					   		String strLitisconsorteAtivo = "";
					   		String strLitisconsortePassivo = "";
					   		String strSubstitutoProcessual = "";
					   		String strComunicante = "";
					   		String strCurador = "";
					   		String strTerceiro = "";
					   		String strTestemunha = "";
					   		String strPaciente = "";
					   		
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
						   				case ProcessoParteTipoDt.PACIENTE:
						   					strPaciente = (String) parteDt.getProcessoParteTipo();
						   					pacientes.add(parteDt);
						   					break;
						   				default:
						   					outros.add(parteDt);
						   					break;
					   			  	}
				   			  	}
		   					} %>
		   					
		   					<%if (litisconsorteAtivos != null && litisconsorteAtivos.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strLitisconsorteAtivo%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteLitisconsorte" name="imaLocalizarParteLitisconsorte" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < litisconsorteAtivos.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)litisconsorteAtivos.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
							   					<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
								       	
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>								
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (litisconsortePassivos != null && litisconsortePassivos.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strLitisconsortePassivo%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteLitisconsortePassivo" name="imaLocalizarParteLitisconsortePassivo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < litisconsortePassivos.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)litisconsortePassivos.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>										
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (substitutosProcessual != null && substitutosProcessual.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strSubstitutoProcessual%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteSubstitutoProcessual" name="imaLocalizarParteSubstitutoProcessual" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < substitutosProcessual.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)substitutosProcessual.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>										
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (comunicantes != null && comunicantes.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strComunicante%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteComunicante" name="imaLocalizarParteComunicante" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < comunicantes.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)comunicantes.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>										
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (curadores != null && curadores.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strCurador%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteCurador" name="imaLocalizarParteCurador" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < curadores.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)curadores.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<%	if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>										
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (terceiros != null && terceiros.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strTerceiro%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteTerceiro" name="imaLocalizarParteTerceiro" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < terceiros.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)terceiros.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>									
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
		   					
		   					<%if (testemunhas != null && testemunhas.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strTestemunha%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteTestemunha" name="imaLocalizarParteTestemunha" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < testemunhas.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)testemunhas.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>								
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
							
							<%if (pacientes != null && pacientes.size() > 0) { %>
								<fieldset id="VisualizaDados" class="VisualizaDados">
							   		<legend><%=strPaciente%>
							   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
							   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarPartePaciente" name="imaLocalizarPartePaciente" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
							   			<%} %>
									</legend>
							 		<%
							   			
							   				for (int i=0;i < pacientes.size();i++){
							   			  		ProcessoParteDt parteDt = (ProcessoParteDt)pacientes.get(i);
								   	%>
							    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
							       			<%	if (parteDt.getId().length()> 0){
						      		    		if (parteDt.getDataBaixa().equals("")) { %>
							      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
									   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
									   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
									   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
									   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
									   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
			 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
															<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
														</a> 
													<% 		} %>	
													<% } %>	
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
									   			<%}%>
									   			
											<%	} else { %>
										       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
												Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>										
								       		<% 	}%>
								       		<br />
						      		    	<%}
							        		} %>  
								</fieldset><br />
							<% } %>
							
							<fieldset id="VisualizaDados" class="VisualizaDados">
								   		<legend> Outras Partes / Sujeitos 
								   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
								   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteOutrasPartes" name="imaLocalizarParteOutrasPartes" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
								   			<%} %>
										</legend>
								 		<%
								 		if (outros != null && outros.size() > 0) {
								   				for (int i=0;i < outros.size();i++){
								   			  		ProcessoParteDt parteDt = (ProcessoParteDt)outros.get(i);
									   	%>
								    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
								       			<%	if (parteDt.getId().length()> 0){
							      		    		if (parteDt.getDataBaixa().equals("")) { %>
								      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
										   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
										   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
										   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
										   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
										   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
				 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
																<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
															</a> 
														<% 		} %>	
														<% } %>	
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
										   			<%}%>
										   			
												<%	} else { %>
											       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
													Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>									
									       		<% 	}%>
									       		<br />
							      		    	<%}
								        		} 
								 		} else {  %>
							   			<em> Insira Outras Partes. </em>
							   		<%  } %>
									</fieldset><br />
							
						<%} else {  %>
				   			<fieldset id="VisualizaDados" class="VisualizaDados">
								   		<legend> Outras Partes / Sujeitos 
								   			<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
								   			<input class="FormEdicaoimgLocalizar" id="imaLocalizarParteOutrasPartes2" name="imaLocalizarParteOutrasPartes2" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('ParteTipo','-1');AlterarValue('PassoEditar','0');" title="Cadastrar Outras Partes"/> <br />
								   			<%} %>
										</legend>
								 		<%
								 		if (listaOutrasPartes != null && listaOutrasPartes.size() > 0) {
								   				for (int i=0;i < listaOutrasPartes.size();i++){
								   			  		ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
									   	%>
								    			<div> Nome </div><span class="span1" title="<%=parteDt.getProcessoParteTipo()%>"><%=parteDt.getNome()%></span>
								       			<%	if (parteDt.getId().length()> 0){
							      		    		if (parteDt.getDataBaixa().equals("")) { %>
								      		    	<input name="imgEditar" class="imgEditar" type="image" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');" title="Editar dados de Outras Partes">
										   			<input name="imgBaixar" class="imgBaixar" type="image" src="./imagens/imgBaixaPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','0');" title="Dar baixa em Parte"/>
								       			<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','2');" title="Excluir Parte"/>
										   			<%if(!(UsuarioSessao.isConsultor() || UsuarioSessao.isPublico())) { %>
										   				<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onclick="MostrarOcultar('sub<%=parteDt.getNome()%>');return false;" title="Mostrar/Ocultar Endereço" />
										   				<% if (UsuarioSessao.isPodeGerarCodigoAcesso(processoDt.getId_Serventia())){ %>
										   				<% 		if ( !parteDt.isComunicante() && !parteDt.isTestemunha()){ %>
				 											<a href="DescartarPendenciaProcesso?PaginaAtual=1&amp;id_Parte=<%=parteDt.getId_ProcessoParte()%>">
																<img src="imagens/16x16/btn_codigo.png" lt="Gerar Código de Acesso para Parte" title="Gerar Código de Acesso para Parte" style="color: white;"/>
															</a> 
														<% 		} %>	
														<% } %>	
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
										   			<%}%>
										   			
												<%	} else { %>
											       	<input name="imgRestaurar" class="imgExcluir" type="image" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','2');AlterarValue('ParteTipo','-1');AlterarValue('Id_ProcessoParte','<%=parteDt.getId()%>');AlterarValue('posicaoLista','<%=i%>');" title="Restaurar Parte">
													Parte <%if(parteDt.getExcluido() != null && !parteDt.getExcluido().equals("") && Integer.parseInt(parteDt.getExcluido()) == 1) {%>EXCLUÍDA<%} else {%>BAIXADA<%} %> em <%=parteDt.getDataBaixa()%>									
									       		<% 	}%>
									       		<br />
							      		    	<%}
								        		} 
								 		} else {  %>
							   			<em> Insira Outras Partes. </em>
							   		<%  } %>
									</fieldset><br />
			   		<%  } %>
					</fieldset>
			
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>