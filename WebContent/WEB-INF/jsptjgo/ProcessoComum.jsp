<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PartesIsentaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="org.apache.commons.lang.StringUtils" %>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>

<html>
	<head>
		<title>Cadastro de Processo</title>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/balloon.css');					
		</style>
      	      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
   		<script type="text/javascript">
			function validar(field, ct, titulo, legenda, id, descricao, campos, colunasTabela, paginaAtual, tamanhoConsulta, filtro, descricaoCampo) {
				if ($('#' + filtro).val() == '' || $('#' + filtro).val() == 'null') {
					alert('Erro! Selecionar primeiramente, ' + descricaoCampo);
					return false;
				} else {
					MostrarBuscaPadrao(field, ct, titulo, legenda, id, descricao, campos, colunasTabela, paginaAtual, tamanhoConsulta, filtro);
				}
			}
		</script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type='text/javascript' src='./js/processoComum.js?random=20190908'></script>
	</head>
	<body>
  		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Cadastro de Processo Comum</h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input type="hidden"	id="PaginaAtual" 	name="PaginaAtual" 		value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden"	id="PaginaAnterior" name="PaginaAnterior" 	value="<%=request.getAttribute("PaginaAnterior")%>">				
				<input type="hidden"  	id="PassoEditar" 	name="PassoEditar" 		value="<%=request.getAttribute("PassoEditar")%>">
				<input type="hidden"  	id="ParteTipo" 		name="ParteTipo" 		value="<%=request.getAttribute("ParteTipo")%>">
				<input type="hidden"  	id="TituloPagina"  	name="TituloPagina" 	value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input type="hidden" id="dependente" name="dependente" value="<%=ProcessoCadastroDt.isProcessoDependente()%>">
				<input type="hidden" id="custaTipo"  name="custaTipo"  value="<%=ProcessoCadastroDt.getId_Custa_Tipo()%>" />
				
				<%@ include file="ProcessoPassos.jspf" %>	
				
				<%
					boolean permitePesquisaComarcaAreaETipo = !(request.getAttribute("tempRetorno") != null && (request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt")));
					if (request.getAttribute("permitePesquisaComarcaAreaETipo") != null && Boolean.valueOf(request.getAttribute("permitePesquisaComarcaAreaETipo").toString())) {
						permitePesquisaComarcaAreaETipo = true;
					}
				%>
				
<!-- 				<div id="divPortaBotoes" class="divPortaBotoes" style="width: 955px; height: 23px; "> -->
<%-- 					<input id="imgCarregar" alt="Carregar" class="imgCarregar" title="Carregar - Carrega os dados salvos" name="imaCarregar" type="image" src="./imagens/ex_ico_solucionar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','11');" /> --%>
<!-- 				</div> -->
				
				<div id="divEditar" class="divEditar">
					
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Passo 1 - Dados do Processo </legend>
					    
					    <fieldset>
					    	<legend class="formEdicaoLegenda">Tipo do Processo</legend>
					    	
						    <label class="formEdicaoLabel" for="id_ProcessoGrau">*Grau</label>
							<input type="radio" onchange="FormSubmit('Formulario');" name="grauProcesso" value="1" id="grauProcesso1" <%=ProcessoCadastroDt.getGrauProcesso().equals("1") ? "checked" : ""%> onclick="mostrarVarios('divTipoProcesso');ocultarVarios('divAssistencia','divDependencia','divGuiaInicial','divNatureza','divValor','imaLocalizarParteVitimas','lblVitima','divTco','divBuscaDependente','divProcesso100');uncheckedVariosRadio('assistenciaProcesso', 'dependenciaProcesso', 'tipoProcesso','digital100');" />1º Grau 
				       		<input type="radio" onchange="FormSubmit('Formulario');" name="grauProcesso" value="2" id="grauProcesso2" <%=ProcessoCadastroDt.getGrauProcesso().equals("2") ? "checked" : ""%> onclick="mostrarVarios('divTipoProcesso');ocultarVarios('divAssistencia','divDependencia','divGuiaInicial','divNatureza','divValor','imaLocalizarParteVitimas','lblVitima','divTco','divBuscaDependente','divProcesso100');uncheckedVariosRadio('assistenciaProcesso', 'dependenciaProcesso', 'tipoProcesso','digital100');" />2º Grau
							<input type="radio" onchange="FormSubmit('Formulario');" name="grauProcesso" value="3" id="grauProcesso3" <%=ProcessoCadastroDt.getGrauProcesso().equals("3") ? "checked" : ""%> onclick="mostrarVarios('divTipoProcesso');ocultarVarios('divAssistencia','divDependencia','divGuiaInicial','divNatureza','divValor','imaLocalizarParteVitimas','lblVitima','divTco','divBuscaDependente','divProcesso100');uncheckedVariosRadio('assistenciaProcesso', 'dependenciaProcesso', 'tipoProcesso','digital100');" />Turma Recursal
							<br />
							
							<div id="divTipoProcesso" style="display: <%=ProcessoCadastroDt.getTipoProcesso().equals("1")||ProcessoCadastroDt.getTipoProcesso().equals("2") ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="id_ProcessoTipo">*Tipo</label> 
							<input type="radio" name="tipoProcesso" value="1" id="tipoProcessoCivel" <%=ProcessoCadastroDt.getTipoProcesso().equals("1") ? "checked" : ""%> onclick="mostrarVarios('divAssistencia','divValor');uncheckedRadio('dependenciaProcesso');ocultarVarios('imaLocalizarParteVitimas','lblVitima','divTco');"/>Cível 
				       		<input type="radio" name="tipoProcesso" value="2" id="tipoProcessoCriminal" <%=ProcessoCadastroDt.getTipoProcesso().equals("2") ? "checked" : ""%> onclick="ocultarVarios('divGuiaInicial','divNatureza','divValor','divBuscaDependente');uncheckedVariosRadio('dependenciaProcesso','assistenciaProcesso','digital100');mostrarVarios('divTco','divAssistencia');mostrarVariosInline('imaLocalizarParteVitimas', 'lblVitima');" />Criminal
							<br />
							</div>
							
				        	<div id="divAssistencia" style="display: <%=ProcessoCadastroDt.getAssistenciaProcesso().equals("1")||ProcessoCadastroDt.getAssistenciaProcesso().equals("2")||ProcessoCadastroDt.getAssistenciaProcesso().equals("3") ? "block" : "none"%>">
				        	<label class="formEdicaoLabel" for="id_ProcessoAssistencia">*Assistência</label> 
							<input type="radio" onchange="FormSubmit('Formulario');"name="assistenciaProcesso" id="comAssistencia" value="1" <%=ProcessoCadastroDt.getAssistenciaProcesso().equals("1") ? "checked" : ""%>  onclick="Mostrar('divDependencia');ocultarVarios('divGuiaInicial','divNatureza');" />Com Assistência
				       		<input type="radio" onchange="FormSubmit('Formulario');"name="assistenciaProcesso" id="semAssistencia" value="2" <%=ProcessoCadastroDt.getAssistenciaProcesso().equals("2") ? "checked" : ""%>  onclick="ocultarVarios('divDependencia');uncheckedRadio('dependenciaProcesso');  " />Com custas
							<input type="radio" onchange="FormSubmit('Formulario');"name="assistenciaProcesso" id="isentoAssistencia" value="3" <%=ProcessoCadastroDt.getAssistenciaProcesso().equals("3") ? "checked" : ""%>  onclick="Mostrar('divDependencia');ocultarVarios('divGuiaInicial','divNatureza'); " />Isento
							<br />
							</div>
							
							<div id="divDependencia" style="display: <%=ProcessoCadastroDt.getDependenciaProcesso().equals("1")||ProcessoCadastroDt.getDependenciaProcesso().equals("2") ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="id_ProcessoDependencia">*Dependência</label> 
							<input type="radio" onchange="FormSubmit('Formulario');" name="dependenciaProcesso" id="comDependencia" value="1" <%=ProcessoCadastroDt.getDependenciaProcesso().equals("1") ? "checked" : ""%> onclick="mostrarVarios('divProcessoDependente','divBuscaDependente');Ocultar('divProcessoNaoDependente');" />Com Dependência
							<input type="radio" onchange="FormSubmit('Formulario');" name="dependenciaProcesso" id="semDependencia" value="2" <%=ProcessoCadastroDt.getDependenciaProcesso().equals("2") ? "checked" : ""%> onclick="ocultarVarios('divProcessoDependente','divBuscaDependente');Mostrar('divProcessoNaoDependente');"/>Sem Dependência
				       		</div>
							<br />
							
<!-- 							<div id="divProcesso100" class='col30' style="display:none" data-balloon-length="xlarge" aria-label="A opção pelo Juízo 100% Digital implica adesão às regras do Decreto Judiciário 837/2021, entre elas o dever de indicação do e-mail ou número de telefone com aplicativo de mensagem da parte contrária. A falta de indicação desses itens poderá gerar a exclusão, por ato do Juízo Competente, dos benefícios desta nova modalidade de tramitação processual." data-balloon-pos="down">							 -->
<%-- 								<input type="checkbox" name="digital100" id="digital100"  <%=(ProcessoCadastroDt.is100Digital()?"checked":"")%> value="true" />  Deseja aderir ao Juízo 100% Digital? Opção admitida apenas para as Varas Cíveis, Juizados Cíveis e Juizados da Fazenda Pública.	 --%>
<!-- 		     				</div> -->
														
							<div id="divGuiaInicial" style="display:none">
								<label id="formLocalizarLabel" class="formLocalizarLabel">
									Informe o número da Guia Inicial (Somente Números)
								</label>
								<br />
								<font color='red'><b>Por favor, caso a série de sua guia seja final 9 emitida pelo site do Tribunal de Justiça de Goiás ou pelo sistema SPG, então adicione 0(zero) antes do 9, ou seja, 09 ao final do número completo da guia. A série é composta por dois dígitos.</b></font>
								<br />
								<br />
								<input id="numeroCompletoGuiaInicial" class="formLocalizarInput" name="numeroCompletoGuiaInicial" type="text" value="" size="20" maxlength="11" onkeypress="return DigitarSoNumero(this, event)" title="Informe somente o número da Guia Inicial" />
								<br />
								<div id="divBotoesCentralizados" class="divBotoesCentralizados">
									<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" title="Localizar Guia Inicial" value="Localizar Guia Inicial" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar %>');" />
								</div>
							</div>
							
							<div id="divBuscaDependente" style="display: <%=ProcessoCadastroDt.getDependenciaProcesso().equals("1") ? "block" : "none"%>">
								<fieldset class="formEdicao">
									<legend class="formEdicaoLegenda"> Busca de Processo </legend>
			                        <p>
			                        <em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em><br />					    
			                        </p>
									
									<label class="formEdicaoLabel" for="CpfCnpj"> Número Processo </label><br>
									<input class="formEdicaoInput" type="text" name="ProcessoNumero"  size="30" maxlength="25"  onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)" value="<%=ProcessoCadastroDt.getProcessoNumeroPrincipal()%>">
									
									<br />
									
									<div id="divBotoesCentralizados" class="divBotoesCentralizados">
										<input id="buscaProcessoDependente" name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
									</div>
								</fieldset>
							</div>
					    	
					    </fieldset>
					    
					    <%if (request.getAttribute("numeroGuiaInicial") != null && request.getAttribute("numeroGuiaInicial").toString().length() > 0) {%>
						<fieldset>
					    	<legend class="formEdicaoLegenda">Número da guia Inicial utilizada</legend>
					    	<%=request.getAttribute("numeroGuiaInicial")%>
					    </fieldset>
						<% } %>
						
						<% if(ProcessoCadastroDt.getDependenciaProcesso().equals("1") && ProcessoCadastroDt.getAssistenciaProcesso().equals("3")) { %>
						    <%@ include file="PartesProcessoDependenciaIsento.jspf"%>
						<%
							} else if(ProcessoCadastroDt.getAssistenciaProcesso().equals("3")) {
						%> 
							<%@ include file="PartesProcessoIsentoCivel.jspf"%>			    
					    <%
			    					    	} else if(ProcessoCadastroDt.getDependenciaProcesso().equals("1") || ProcessoCadastroDt.getAssistenciaProcesso().equals("2")) {
			    					    %> 
							<%@ include file="PartesProcessoSegundoGrau.jspf"%> 
						<%
 							} else {
 						%>
						 	<%@ include file="PartesProcessoCivel.jspf"%> 
						<%}%>
						
						<%@ include file="AdvogadosProcesso.jspf"%> 
						
						<fieldset id='fieldsetCaracteristicasProcesso'>
					    	<legend class="formEdicaoLegenda">Características </legend>
					    	
					    	<div id="divProcessoDependente" class="col100" style="display: <%=ProcessoCadastroDt.getProcessoNumeroPrincipal() != null && ProcessoCadastroDt.getProcessoNumeroPrincipal().length() > 0 ? "block" : "none"%>">
						    	<label class="formEdicaoLabel" for="ProcessoNumeroDependente">*Processo Principal</label><br> 
					    		<input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoNumeroDependente" id="ProcessoNumeroDependente" type="text" size="30" maxlength="25" value="<%=ProcessoCadastroDt.getProcessoNumeroPrincipal()%>" />    		
							</div>
							<%
							if (StringUtils.isNotEmpty(ProcessoCadastroDt.getProcessoFisicoTipo())){
							%>
								<div class="col100" id="divAcervoFisico">
								 	<label class="vermelho"><i>Informação: Este Processo Principal ainda é do acervo físico - Comarca de Origem: <%=ProcessoCadastroDt.getProcessoFisicoComarcaNome() %></i></label>
								</div>
							<%
							}			
							%>
							
					    	<div id="divProcessoNaoDependente" style="display: <%= ProcessoCadastroDt.isMesmoGrauJurisdicao() && ProcessoCadastroDt.isProcessoMesmaArea()  ? "none" : "block"%>">
								<div class='col30'>
									<label for='Id_Comarca' class='formEdicaoLabel'>
										*Comarca
<%
										if (permitePesquisaComarcaAreaETipo) {
%>
											<input type='image' id='imaLocalizarComarca' name='imaLocalizarComarca' class='FormEdicaoimgLocalizar' src='./imagens/imgLocalizarPequena.png' onclick='MostrarBuscaPadrao("fieldsetCaracteristicasProcesso", "Comarca", "Consulta de Comarcas", "Digite a Comarca e clique em consultar.", "Id_Comarca", "Comarca", ["Comarca"], [], "<%=(Configuracao.Localizar)%>", "<%=Configuracao.TamanhoRetornoConsulta%>"); return false;')'>
											<input type='image' id='imaLimparComarca' name='imaLimparComarca' class='FormEdicaoimgLocalizar' src='./imagens/16x16/edit-clear.png' onclick='LimparChaveEstrangeira("Id_Comarca", "Comarca"); return false;'>
<%
										}
%>
									</label>
									<br>
									<input type='hidden' id='Id_Comarca' name='Id_Comarca' value='<%=ProcessoCadastroDt.getId_Comarca()%>'>
									<input type='text' id='Comarca' name='Comarca' class='formEdicaoInputSomenteLeitura' size='51' maxlength='100' value='<%=ProcessoCadastroDt.getComarca()%>' readonly='readonly'/>
									<br>
								</div>
								<div class='col30'>
									<label for='Id_Serventia' class='formEdicaoLabel'>
										*Área Distribuição
<%
										if (permitePesquisaComarcaAreaETipo) {
%>
											<input type='image' id='imaLocalizarAreaDistribuicao' name='imaLocalizarAreaDistribuicao' class='FormEdicaoimgLocalizar' src='./imagens/imgLocalizarPequena.png' onclick='validar("fieldsetCaracteristicasProcesso", "AreaDistribuicao", "Consulta de Áreas de Distribuição", "Digite a Área de Distribuição e clique em consultar.", "Id_AreaDistribuicao", "AreaDistribuicao", ["AreaDistribuicao"], ["ForumCodigo", "Id_ServentiaSubTipo"], "<%=(Configuracao.Localizar)%>", "<%=Configuracao.TamanhoRetornoConsulta%>", "Id_Comarca", "Comarca"); return false;')'>
											<input type='image' id='imaLimparAreaDistribuicao' name='imaLimparAreaDistribuicao' class='FormEdicaoimgLocalizar' src='./imagens/16x16/edit-clear.png' onclick='LimparChaveEstrangeira("Id_AreaDistribuicao", "AreaDistribuicao"); return false;'>
<%
										}
%>
									</label>
									<br>									
									<input type='hidden' id='ForumCodigo' name='ForumCodigo' value='<%=ProcessoCadastroDt.getForumCodigo()%>'>
									<input type='hidden' id='Id_AreaDistribuicao' name='Id_AreaDistribuicao' value='<%=ProcessoCadastroDt.getId_AreaDistribuicao()%>'>
									<input type='text' id='AreaDistribuicao' name='AreaDistribuicao' class='formEdicaoInputSomenteLeitura' size='51' maxlength='100' value='<%=ProcessoCadastroDt.getAreaDistribuicao()%>' readonly='readonly'/>
									<br>
								</div>
							</div>
							<div class='col100'>
								<label for='Id_ProcessoTipo' class='formEdicaoLabel'>
									<a href='http://www.cnj.jus.br/sgt/consulta_publica_classes.php' target='blank' title='Clique neste link para visualizar a tabela detalhada de classes do CNJ'>
										*Classe
									</a>
<%
									if (permitePesquisaComarcaAreaETipo) {
%>
										<input type='image' id='imaLocalizarProcessoTipo' name='imaLocalizarProcessoTipo' class='FormEdicaoimgLocalizar' src='./imagens/imgLocalizarPequena.png' onclick='validar("fieldsetCaracteristicasProcesso", "ProcessoTipo", "Consulta de Tipos de Processo", "Digite o Tipo de Processo e clique em consultar.", "Id_ProcessoTipo", "ProcessoTipo", ["ProcessoTipo"], [ "Id_ServentiaSubTipo"], "<%=(Configuracao.Localizar)%>", "<%=Configuracao.TamanhoRetornoConsulta%>", "Id_AreaDistribuicao", "Área de Distribuição"); return false;')'>
										<input type='image' id='imaLimparProcessoTipo' name='imaLimparProcessoTipo' class='FormEdicaoimgLocalizar' src='./imagens/16x16/edit-clear.png' onclick='LimparChaveEstrangeira("Id_ProcessoTipo", "ProcessoTipo"); return false;'>
<%
									}
%>
								</label>
								<br>
								<input type='hidden' id='Id_ProcessoTipo' name='Id_ProcessoTipo' value='<%=ProcessoCadastroDt.getId_ProcessoTipo()%>'>
								<input type='text' id='ProcessoTipo' name='ProcessoTipo' class='formEdicaoInputSomenteLeitura' size='112' maxlength='100' value='<%=ProcessoCadastroDt.getProcessoTipo()%>' readonly='readonly'/>
								<br>
							</div>
							
							<% if (ProcessoCadastroDt.getNaturezaSPG() != null && ProcessoCadastroDt.getNaturezaSPG().trim().length() > 0) {%>
							
							<div class="col100" id="divNatureza" style="display: none;">
					    	<label class="formEdicaoLabel" for="Id_NaturezaSPG">*Natureza</label><br>
					    	<input class="formEdicaoInputSomenteLeitura"  readonly name="NatruezaSPG" id="NatruezaSPG" type="text" size="113" maxlength="100" value="<%=ProcessoCadastroDt.getNaturezaSPG()%>"/><br />
					    	</div>
					    	
					    	<% } %>
							
						<div class="clear"></div>
							
				    		<%@ include file="AssuntosProcesso.jspf"%> 
							
							<div class="col50">
								<label class="formEdicaoLabel" for="Id_ProcessoPrioridade">
									*Prioridade
									<input type="image" src="./imagens/imgLocalizarPequena.png" class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoPrioridade" name="imaLocalizarProcessoPrioridade" onclick="MostrarBuscaPadrao('fieldsetCaracteristicasProcesso', 'ProcessoPrioridade', 'Consulta de Prioridades do Processo', 'Digite o ProcessoPrioridade e clique em consultar.', 'Id_ProcessoPrioridade', 'ProcessoPrioridade', ['ProcessoPrioridade'], ['Código', 'Ordem'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")"/>
								</label>
								<br>
								<input type="hidden" id="Id_ProcessoPrioridade" name="Id_ProcessoPrioridade" value="1">
								<input type="text" class="formEdicaoInputSomenteLeitura" id="ProcessoPrioridade" name="ProcessoPrioridade" size="67" maxlength="100" value="Normal" readonly="readonly"/>
								<br>
							</div>
							
							<div id="divValor" style="display:none" class="col30">
				    		<label class="formEdicaoLabel" for="Valor">*Valor</label><br> 
				    		<input <%if((request.getAttribute("tempRetorno") != null &&  ( request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoCivelSemAssistencia") || request.getAttribute("tempRetorno").toString().equalsIgnoreCase("ProcessoSegundoGrauCivelSemAssistenciaCt") ) ) ) { %>class="formEdicaoInputSomenteLeitura" readonly<%} else {%> class="formEdicaoInput" <%}%> name="Valor" id="Valor"  type="text" size="20" maxlength="20" value="<%=ProcessoCadastroDt.getValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/><br />    		
					    	</div>
					    	
					    	<div id="divTco" style="display:none" class="col30">
						    <label class="formEdicaoLabel" for="TcoNumero">Protocolo SSP</label><br>    
						    <input class="formEdicaoInput" name="TcoNumero" id="TcoNumero" type="text" size="15" maxlength="15" value="<%=ProcessoCadastroDt.getTcoNumero()%>"/><br />
						    </div>
					    	<div class="clear"></div>
					    </fieldset>	
					    
					    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Avançar" title="Passa para a próxima tela de cadastro" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','5');"> 
							<input name="imgInserir" type="submit" value="Limpar" title= "Limpa os campos da tela" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
							<input name="imgInserir" type="submit" value="Salvar Rascunho" title="Cria arquivo com os dados digitados" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','12');">
					    </div>
					</fieldset>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>