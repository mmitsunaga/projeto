<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="AudienciaMovimentacaoDt" class= "br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Concluir Audiência</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
        	
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>	
	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<script type='text/javascript'  src='js/ckeditor/ckeditor.js?v=24092018'></script>

<!-- 	<script type='text/javascript' src='./dwr/engine.js'></script> -->
<!-- 	<script type='text/javascript' src='./dwr/util.js'></script> -->

<%-- 	<%@ include file="./dwr/InsercaoArquivoAudienciaMovimentacao.dwr"%> --%>

	
	<%@ include file="js/InsercaoArquivo.js"%>
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">			
			<%		
				   AudienciaDt audienciaDt = AudienciaMovimentacaoDt.getAudienciaDt();
				   AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();	
				   boolean podeAnalisar_preAnalisar = audienciaProcessoDt.podeAnalisarOuPreanalisarVotoEmenta(String.valueOf(request.getAttribute("GrupoTipoUsuarioLogado")));				   
				   boolean ehAnalistaTecnicoSegundoGrau = (!AudienciaMovimentacaoDt.isPreAnalise() && !podeAnalisar_preAnalisar && (UsuarioSessao.isSegundoGrau() || UsuarioSessao.isUPJSegundoGrau()) && !(AudienciaMovimentacaoDt.isMovimentacaoSessaoAdiada() || AudienciaMovimentacaoDt.isMovimentacaoSessaoIniciada()));
				   
				   if(!AudienciaMovimentacaoDt.isPreAnalise()) {
					   if (!AudienciaMovimentacaoDt.getPasso1().equals("")){
					%>
				<input name="imgPasso1" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<%}%>
				<%if (!AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%>
					<% if (!AudienciaMovimentacaoDt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<%}%>
				<%}%>
				<% if (!AudienciaMovimentacaoDt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<%}%>
		   <%}%>		
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>">			
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="TipoAudienciaProcessoMovimentacao" name="TipoAudienciaProcessoMovimentacao" type="hidden" value="<%=request.getAttribute("TipoAudienciaProcessoMovimentacao")%>" />	
			<!-- Variáveis para controlar Passos da Movimentação -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso1()%>">
			<%if (!AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%>
				<input id="Passo2" name="Passo2" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso2()%>">
			<%}%>
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso3()%>">			
			<input id="ConsultaEmenta" name="ConsultaEmenta" type="hidden" value="N">
			<input id="PodeAnalisarPreAnalisar" name="PodeAnalisarPreAnalisar" type="hidden" value="<%=(podeAnalisar_preAnalisar ? "S" : "N")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="votoPorMaioria" name="votoPorMaioria" type="hidden" value="<%=request.getAttribute("votoPorMaioria")%>" />
			<input id="ehAnalistaTecnicoSegundoGrau" name="ehAnalistaTecnicoSegundoGrau" type="hidden" value="<%=(ehAnalistaTecnicoSegundoGrau ? "S" : "N")%>">
			<input id="SomentePreAnalisadas" name="SomentePreAnalisadas" type="hidden" value="<%=request.getAttribute("SomentePreAnalisadas")%>">
			
			<div id="divEditar" class="divEditar">				
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Passo 1 - Dados da Movimentação </legend>				
					
					<% if (AudienciaMovimentacaoDt.isRelatorio()) {%>
					<fieldset style="width:70%">
					<%} else { %>
					<fieldset>
					<%} %>
						
							<input type="hidden" name="AudienciaTipoCodigo" id="AudienciaTipoCodigo" value="<%=audienciaDt.getAudienciaTipoCodigo()%>" />
							<legend class="formEdicaoLegenda"> Dados Audiência <%=audienciaDt.getAudienciaTipo()%></legend>
							
							<label class="formEdicaoLabel"> Data </label><br>
							<span class="spanDestaque"> <%=audienciaDt.getDataAgendada()%> </span>
							
							<br />
							
							<label class="formEdicaoLabel"> Processo </label><br>
							<span class="spanDestaque">
								<%if(request.getSession().getAttribute("processos") != null) {
									List listaProcessos = (List)request.getSession().getAttribute("processos");
									for (int i = 0; i < listaProcessos.size(); i++) { 
										ProcessoDt processoTemp = (ProcessoDt)listaProcessos.get(i); %>
										<a href="BuscaProcesso?Id_Processo=<%=processoTemp.getId()%>"><%=processoTemp.getProcessoNumero()%></a>
										<%if(i < listaProcessos.size() - 1) {%>
										,&nbsp;
										<%} %>
									<% } 									
								  } else { %>
									<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>"><%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
									<%if(UsuarioSessao.isSegundoGrau() || UsuarioSessao.isUPJSegundoGrau() || UsuarioSessao.isGabineteSegundoGrau() || UsuarioSessao.isGabineteFluxoUPJ() && (audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0)){%>
										&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
									<%}%>									
									</a>
								<%} %>
							</span>
																		
							<br />
							
							<label class="formEdicaoLabel">*Status </label><br>  
							<input type="hidden" name="AudienciaStatus" id="AudienciaStatus" value="<%=AudienciaMovimentacaoDt.getAudienciaStatus()%>" />
							<select name="AudienciaStatusCodigo" id="AudienciaStatusCodigo" size="1" onchange="capturaTextoSelect('AudienciaStatusCodigo', 'AudienciaStatus');" <%=((AudienciaMovimentacaoDt.isAudienciaProcessoStatusSomenteLeitura())?"disabled=disabled style=\"color: #000000; background-color: #ffffff;\" class=\"formEdicaoInputSomenteLeitura\"":"")%>>
								<option value="-1">--Selecione o Status da Audiência-- </option>
								<%
									List listaAudienciaStatus = AudienciaMovimentacaoDt.getListaAudienciaProcessoStatus();
									for (int i=0;i<listaAudienciaStatus.size();i++){
										AudienciaProcessoStatusDt audienciaStatusDt = (AudienciaProcessoStatusDt)listaAudienciaStatus.get(i);
								%>
									<option value="<%=audienciaStatusDt.getAudienciaProcessoStatusCodigo()%>" <%=(AudienciaMovimentacaoDt.getAudienciaStatusCodigo().equals(audienciaStatusDt.getAudienciaProcessoStatusCodigo())?"selected":"")%>>
										<%=audienciaStatusDt.getAudienciaProcessoStatus()%>
									</option>
								<%		
									}
								%> 	  						   
							</select>	
							<% if(audienciaDt.devePossuirIndicadorDeAcordo()) {%>
								<br />						
								<label class="formEdicaoLabel" for="Acordo"> *Houve Acordo? </label><br>  
								<input type="radio" name="Acordo" value="1" onclick="alterarValorRadio('1')" <%=audienciaProcessoDt.isHouveAcordo() ? "checked" : ""%> />Sim
					       		<input type="radio" name="Acordo" value="0" onclick="alterarValorRadio('0')" <%=audienciaProcessoDt.isNaoHouveAcordo() ? "checked" : ""%> />Não
					       		
					       		<div id="divValorAcordo">
						       		<label class="formEdicaoLabel" for="ValorAcordo"> *Valor do Acordo</label><br>
						    		<input class="formEdicaoInput" name="ValorAcordo" id="ValorAcordo"  type="text" size="20" maxlength="20" value="<%=audienciaProcessoDt.getValorAcordo()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/>				    		
					       		</div>	
							<% } %>						
							<% if(podeAnalisar_preAnalisar) {%>
								&nbsp;&nbsp;&nbsp;				
								<label>Ata </label><br>							
								<span class="spanDestaque">
									<a target='_blanck' href="<%=request.getAttribute("tempRetorno")%>?PassoEditar=1&now=<%=new Date().getTime()%>"><img style="border: none;" src="./imagens/22x22/ico_arquivos.png">&nbsp;<%=audienciaProcessoDt.getNomeArquivoAta()%></a>
								</span>
							<%}%>																										
					</fieldset>
					<% if (AudienciaMovimentacaoDt.isRelatorio()) {%>
						<fieldset style="float:right;width:25%; height:100%;"> 
							<legend class="formEdicaoLegenda">Arquivo(s) </legend>
								<center style="width:100%; height:100%" >
									<a  href="BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo=<%=AudienciaMovimentacaoDt.getId_Relatorio()%>&hash=<%=AudienciaMovimentacaoDt.getHashRelatorio()%>&now=<%=new Date().getTime()%>"><img style="border: none;" src="./imagens/22x22/ico_arquivos.png"> </a>
									<br /> Relatório
								</center>
						</fieldset>
					<%} %>
					<fieldset>	
					 	<legend><%=((podeAnalisar_preAnalisar)?"Relatório, Voto e Acórdão":"Inserir Arquivo(s)")%> </legend>
					 							
						<input type="hidden" id="assinado" name="assinado" value="true" />
						<input type="hidden" id="gerarAssinatura" name="gerarAssinatura" value="false" />
	
						<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo")%>" />
						<label class="formEdicaoLabel"> *Tipo de Arquivo
						<%if(!AudienciaMovimentacaoDt.isArquivoTipoSomenteLeitura()) {%>
						<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('ConsultaEmenta', 'N');" title="Selecionar Tipo de Arquivo" />
						<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" title="Limpar Tipo de Arquivo" />
						<%}%>
						</label><br>						
						<%if(AudienciaMovimentacaoDt.isArquivoTipoSomenteLeitura()) {%>
						<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" readonly type="text" size="58" maxlength="50" id="arquivoTipo" value="<%=request.getAttribute("ArquivoTipo")%>" />
						<%} else {%>						
						<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" readonly type="text" size="50" maxlength="50" id="arquivoTipo" value="<%=request.getAttribute("ArquivoTipo")%>" />	
						<%}%>
						<br />

						<input type="hidden" id="id_Modelo" name="Id_Modelo" value="<%=request.getAttribute("Id_Modelo")%>" />
						<label class="formEdicaoLabel"> Modelo 
						<%if(!AudienciaMovimentacaoDt.isModeloSomenteLeitura()) {%>
						<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('ConsultaEmenta', 'N');" title="Selecionar Modelo de Arquivo" />
						<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_Modelo', 'modelo'); return false;" title="Limpar Modelo" />
						<% } %>
						</label><br>
						
						<input class="formEdicaoInputSomenteLeitura" name="Modelo" readonly type="text" size="58" maxlength="50" id="modelo" value="<%=request.getAttribute("Modelo")%>" />		
						<br />
	
						<label class="formEdicaoLabel"> Nome Arquivo
						<%if(!AudienciaMovimentacaoDt.isNomeArquivoSomenteLeitura()) {%>
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeArquivo', ''); return false;" title="Limpar nome do arquivo" />
						<%}%>
						</label><br>
						<%if(AudienciaMovimentacaoDt.isNomeArquivoSomenteLeitura()) {%>
						<input id="nomeArquivo" name="nomeArquivo" type="text" size="73" maxlength="255" value="<%=AudienciaMovimentacaoDt.getNomeArquivo()%>" readonly="readonly" class="formEdicaoInputSomenteLeitura"/>						
						<%} else {%>						
						<input id="nomeArquivo" name="nomeArquivo" type="text" size="54" maxlength="255" value="<%=AudienciaMovimentacaoDt.getNomeArquivo()%>"/>
						<%}%>
						
						<br />
						
						<label class="formEdicaoLabel"> Editor Texto
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
								onclick="MostrarOcultar('Editor'); return false;" title="Abrir Editor de Texto" />		
						</label><br>
										
						<br />	
        		
						<div id="Editor" class="Editor" style="display:none" >        			
							<textarea class="ckeditor" cols="80" id="editor1" name="TextoEditor" rows="20"><%=(request.getAttribute("TextoEditor") != null ? request.getAttribute("TextoEditor") : "")%></textarea>
							<%if (!podeAnalisar_preAnalisar){%>
								<script type="text/javascript">
									<%if (request.getAttribute("TextoEditor") != null && !request.getAttribute("TextoEditor").equals("") ){	%>MostrarOcultar('Editor');<%}%>
									<%if (UsuarioSessao.isSegundoGrau() || UsuarioSessao.isUPJSegundoGrau()) {%> marcarExtratoAta2Grau(); <%}%>
								</script>
							<%}%>
						</div>						
					</fieldset>
					
					<%if (podeAnalisar_preAnalisar) { %>
						<fieldset>	
						 	<legend>Ementa</legend>
							<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
							<input type="hidden" id="assinadoEmenta" name="assinadoEmenta" value="true" />
							<input type="hidden" id="gerarAssinaturaEmenta" name="gerarAssinaturaEmenta" value="false" />
		
							<input type="hidden" id="id_ArquivoTipoEmenta" name="Id_ArquivoTipoEmenta" value="<%=request.getAttribute("Id_ArquivoTipoEmenta")%>" />
							<label class="formEdicaoLabel"> *Tipo de Arquivo</label><br>							
							<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipoEmenta" readonly type="text" size="58" maxlength="50" id="arquivoTipoEmenta" value="<%=request.getAttribute("ArquivoTipoEmenta")%>" />		
							<br />
	
							<input type="hidden" id="id_ModeloEmenta" name="Id_ModeloEmenta" value="<%=request.getAttribute("Id_ModeloEmenta")%>" />
							<label class="formEdicaoLabel"> Modelo 
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarModeloEmenta" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('ConsultaEmenta', 'S');" title="Selecionar Modelo de Arquivo" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparModeloEmenta" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ModeloEmenta', 'modeloEmenta'); return false;" title="Limpar Modelo" />
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="ModeloEmenta" readonly type="text" size="50" maxlength="50" id="modeloEmenta" value="<%=request.getAttribute("ModeloEmenta")%>" />		
							<br />
		
							<label class="formEdicaoLabel"> Nome Arquivo</label><br>							
							<input id="nomeArquivoEmenta" name="nomeArquivoEmenta" type="text" size="73" maxlength="255" readonly="readonly" class="formEdicaoInputSomenteLeitura" value="<%=AudienciaMovimentacaoDt.getNomeArquivoEmenta()%>" />							
							<br />
							
							<label class="formEdicaoLabel"> Editor Texto
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipoEmenta" type="image"  src="./imagens/imgEditorTextoPequena.png" 
								onclick="MostrarOcultar('EditorEmenta'); return false;" title="Abrir Editor de Texto" />
							</label><br>
							
							<br />	
	        		
							<div id="EditorEmenta" style="display:none" >        			
								<textarea class="ckeditor" cols="80" id="editorEmenta" name="TextoEditorEmenta" rows="20"><%=request.getAttribute("TextoEditorEmenta")%></textarea>																													
							</div>						
						</fieldset>												
					<%} %>				
					<br />			
					
					<% if (!AudienciaMovimentacaoDt.isPreAnalise()) {%>		
						<input type="hidden" name="arquivo" id="arquivo" value="" />
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">															    
	  						<button id="div_ser" type="button"  onclick="javascript:digitarSenhaCertificado(inserirArquivo); return false;">
									Assinar
							 </button> 
						     <button type="button"  onclick="javascript:incluirArquivosAreaTransferencia();">
									Colar
							 </button> 						 
							 <button type="button"  onclick="javascript:limparArquivosAreaTransferencia();">
									Limpar
							 </button>
						</div>
												
						<% if (!(UsuarioSessao.isSegundoGrau() || ehAnalistaTecnicoSegundoGrau || UsuarioSessao.isUPJSegundoGrau())) { %>
							<div id="arquivo_assinado" class="divBotoesCentralizados">					   
								<span class="btn btn-success fileinput-button" style="display:none">
								    <i class="glyphicon glyphicon-plus"></i>
							    	<span>Selecionar arquivos...</span>
									<input type="file" id="fileupload" name="files[]" multiple accept="<%=UsuarioSessao.getTipoArquivoUpload()%>" onchange="if (!selecionaArquivoAssinado(this.files)) {this.value='';preparaUploadArquivoAssinado();}"/>
								</span>	        
					        	<button type="button" title="Clique para adicionar o arquivo" onclick="submeteArquivoAssinado();LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" id="botaoAplicar" style="display:none">Confirmar</button>
					        	<button type="button" title="Clique para carregar o arquivo" onclick="abreDialogoUploadArquivoAssinado();"  id="botaoUpload">Anexar Arquivos</button>
					        	<br />
					        	(<a href="https://docs.tjgo.jus.br/projudi/jar/sai.jar"><i class="fa fa-download"></i> Assinador Externo</a>)
				        	</div>
						<% } %>	 					 		               
						
						<%@ include file="Padroes/ListaArquivos.jspf"%>
						<br />
					<% } %>
					<%if (podeAnalisar_preAnalisar) { %>												
						<label class="formEdicaoLabel" for="julgadoMerito">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br><input class="FormEdicaoimgLocalizar" id="julgadoMerito" type="checkbox" name="julgadoMerito" onclick="AlterarValue('fluxo','1')";  value="true" <%if (AudienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal()!=null && AudienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){%> checked<%}%> />
					<% } %>
					<% if (!AudienciaMovimentacaoDt.isPreAnalise()) {%>						
						<%if (!podeAnalisar_preAnalisar && (UsuarioSessao.isSegundoGrau() || UsuarioSessao.isGabineteSegundoGrau() || UsuarioSessao.isGabineteFluxoUPJ() || UsuarioSessao.isUPJSegundoGrau()) && !(AudienciaMovimentacaoDt.isMovimentacaoSessaoAdiada() || AudienciaMovimentacaoDt.isMovimentacaoSessaoIniciada())){%>
							<fieldset id="idNovoResponsavel">
								<legend>								
									Presidente																
								</legend>							
								<input name="Id_NovoServentiaCargoPresidente"  id="Id_NovoServentiaCargoPresidente"  type="hidden"  value="<%=AudienciaMovimentacaoDt.getId_ServentiaCargoPresidente()%>"/>					
								<label class="formEdicaoLabel" for="Id_NovoServentiaCargoPresidente">*Presidente
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','1');" >  
								</label><br>  
								<input class="formEdicaoInputSomenteLeitura" readonly id="NovoServentiaCargoPresidente" type="text" size="80" maxlength="100" value="<%=AudienciaMovimentacaoDt.getServentiaCargoPresidente()%>"/>
				    			<br />    			
				    		</fieldset>
			    		
			    		<%if(request.getAttribute("ocultarRepresentanteMP") == null) {%>
				    		<fieldset>
								<legend>Representante MP</legend>
								
								<input name="Id_NovaServentiaMP"  id="Id_NovaServentiaMP"  type="hidden" value="<%=AudienciaMovimentacaoDt.getId_ServentiaMp()%>"/>						
								<label class="formEdicaoLabel" for="Id_NovaServentiaMP">*MP Serventia
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','1');" >							 
								</label><br>  
								
								<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="NovaSeventiaMP" id="NovaSeventiaMP" type="text" size="60" maxlength="60" value="<%=AudienciaMovimentacaoDt.getServentiaMp()%>"/>
								<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para ver os Cargos disponíveis.</small></label><br> <br />							
							
								<input name="Id_NovoServentiaCargoMP"  id="Id_NovoServentiaCargoMP"  type="hidden" value="<%=(request.getSession().getAttribute("Id_NovoServentiaCargoMP")!= null?String.valueOf(request.getSession().getAttribute("Id_NovoServentiaCargoMP")):"")%>"/>
								<label class="formEdicaoLabel" for="Id_NovoServentiaCargoMP">*MP Responsável
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargoMP" name="imaLocalizarId_ServentiaCargoMP" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','2');" >  
								</label><br>  
				    			
				    			<input class="formEdicaoInputSomenteLeitura"  readonly  id="NovoServentiaCargoMP" type="text" size="80" maxlength="100" value="<%=AudienciaMovimentacaoDt.getServentiaCargoMp()%>"/><br />
				    		</fieldset>
				    		<br />
				    	<%}%>
				    		<input type="checkbox" class="formEdicaoInput" id="chkPorMaioria" value="Por Maioria" onchange="processaHabilitacaoPorMaioria(this)" title="Por Maioria" <%if(AudienciaMovimentacaoDt.isVotoPorMaioria()){%> checked <%}%> />Por Maioria
							<fieldset id="NovoRedatorFieldset">
								<legend>								
									Redator																
								</legend>							
								<input name="Id_NovoServentiaCargoRedator"  id="Id_NovoServentiaCargoRedator"  type="hidden"  value="<%=AudienciaMovimentacaoDt.getId_ServentiaCargoRedator()%>"/>					
								<label class="formEdicaoLabel" for="Id_NovoServentiaCargoRedator">*Redator
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargoRedator" name="imaLocalizarId_ServentiaCargoRedator" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','3');" >  
								</label><br>  
								
								<input class="formEdicaoInputSomenteLeitura" readonly  id="NovoServentiaCargoRedator" type="text" size="80" maxlength="100" value="<%=AudienciaMovimentacaoDt.getServentiaCargoRedator()%>"/>
								<br />    			
							</fieldset>
							<br />				    		
						<% } %>						
					<%}%>					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');"> 
					</div>
					<br />
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>			
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>		
 	</div>	
</body>
	<script type="text/javascript">
		function processaHabilitacaoPorMaioria(check){
			if (check) {
				if (check.checked){
					Mostrar('NovoRedatorFieldset');	
					AlterarValue('votoPorMaioria', 'true');
				} else {
					Ocultar('NovoRedatorFieldset');
					AlterarValue('votoPorMaioria', 'false');					
				}	
			} else {
				Ocultar('NovoRedatorFieldset');
				AlterarValue('votoPorMaioria', 'false');
			}			
		}
		
		function alterarValorRadio(tipoConsulta){
			if (tipoConsulta == "1") {
				$("#divValorAcordo").show();				
			} else {
				$("#divValorAcordo").hide();				
			}			
		}
	</script>
	<script type="text/javascript">
		$('.corpo').hide();	
       	$(document).ready(function(){
       		var check = document.getElementById('chkPorMaioria');			
			processaHabilitacaoPorMaioria(check);	
			alterarValorRadio($('input[name=Acordo]:checked', '#Formulario').val());
			//iniciarMovimentacao();
			atualizarArquivos();	
		});
	</script>
</html>