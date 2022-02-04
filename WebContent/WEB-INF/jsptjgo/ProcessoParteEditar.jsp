<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.EscolaridadeDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EmpresaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GovernoTipoDt" %>
<%@page import="br.gov.go.tj.projudi.ne.GuiaLocomocaoNe" %>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="ProcessoPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDt"/>
<jsp:useBean id="GuiaEmissaoDt" scope="session" class="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"/>


<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%><html>
	<head>
		<title>Cadastro de Partes Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarNomeParteProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
		<script type="text/javascript">
			function processaHabilitacaoEndereco(){		
				var check = document.getElementById('chkEndDesc');
				if (check != null){									
					if (check.checked){
						Ocultar('idCadastroEndereco');
						AlterarValue('parteEnderecoDesconhecido', 'true');					
					}else{
						Mostrar('idCadastroEndereco');					
						AlterarValue('parteEnderecoDesconhecido', 'false');	
					}
				}				
			}
			
			function processaHabilitacaoParteLocomocoes(){		
				var check = document.getElementById('chkParteLocomocao');
				if (check != null){									
					if (check.checked){
						Mostrar('idParteLocomocoes');
						AlterarValue('parteLocomocoes', 'true');					
					}else{
						Ocultar('idParteLocomocoes');					
						AlterarValue('parteLocomocoes', 'false');	
					}
				}				
			}
			
	</script>
	<script type="text/javascript">
		//<![CDATA[
		onload = function()
		{					
			processaHabilitacaoEndereco();
			processaHabilitacaoParteLocomocoes();

			
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) { %>
		
			 $("input").each(function(i){
				 if ($(this).attr("id") != "Submeter" && $(this).attr("type") != "submit" && $(this).attr("type") != "hidden" && $(this).attr("class") != "FormEdicaoimgLocalizar") {
					 $(this).attr("readonly","true"),
					 $(this).attr("class","formEdicaoInputSomenteLeitura"),
					 $(this).attr("disabled","disabled")
					 }
				 if ($(this).attr("id") != "Submeter" && $(this).attr("type") != "submit" && $(this).attr("type") != "hidden" && $(this).attr("class") == "FormEdicaoimgLocalizar") {
					 $(this).attr("readonly","true"),
					 $(this).attr("disabled","disabled")
					 }			   });

			$("select").attr({
				"class": "formEdicaoInputSomenteLeitura",					
				"disabled": "disabled"
			});
			
			<%} %>							
			
		}
		//]]>
		</script>	
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Cadastro de Parte </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
						
				<input id="Hash" name="Hash" type="hidden" value="<%= Funcoes.GeraHashMd5(processoDt.getId_Processo()) %>" />
				<input id="HashParte" name="HashParte" type="hidden" value="<%=Funcoes.GeraHashMd5(ProcessoPartedt.getId_ProcessoParte())%>" />
						
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="<%=request.getAttribute("ParteTipo")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="parteEnderecoDesconhecido" name="parteEnderecoDesconhecido" type="hidden" value="<%=request.getAttribute("parteEnderecoDesconhecido")%>" />
				<input id="parteLocomocoes" name="parteLocomocoes" type="hidden" value="<%=request.getAttribute("parteLocomocoes")%>" />
				
			  	<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Cadastro de Parte</legend>			    	
				    					    	
				    	<% if (	ProcessoPartedt.getProcessoParteTipoCodigo() != null
				    			&& ProcessoPartedt.getProcessoParteTipoCodigo().length() > 0
				    			&& Funcoes.StringToInt(ProcessoPartedt.getProcessoParteTipoCodigo()) != ProcessoParteTipoDt.POLO_ATIVO_CODIGO
				    			&& Funcoes.StringToInt(ProcessoPartedt.getProcessoParteTipoCodigo()) != ProcessoParteTipoDt.POLO_PASSIVO_CODIGO
				    			&& Funcoes.StringToInt(ProcessoPartedt.getProcessoParteTipoCodigo()) != ProcessoParteTipoDt.POLO_ATIVO_CODIGO
				    			&& Funcoes.StringToInt(ProcessoPartedt.getProcessoParteTipoCodigo()) != ProcessoParteTipoDt.POLO_PASSIVO_CODIGO
				    			&& Funcoes.StringToInt(ProcessoPartedt.getProcessoParteTipoCodigo()) != ProcessoParteTipoDt.VITIMA) { %>
							<div class="col75">
						    	<label for="Bairro">*Tipo da Parte 
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarTipoParte" name="imaLocalizarTipoParte" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
								</label> 
								<input class="formEdicaoInputSomenteLeitura" readonly name="ProcessoParteTipo" id="ProcessoParteTipo" type="text" size="50" maxlength="100" value="<%=ProcessoPartedt.getProcessoParteTipo()%>">						
							</div>
				    	<% } %>
						<div class="col75">
						<%if (ProcessoPartedt.isParteIsenta()) {%>
					    	<label for="Nome">*Nome</label> 
					    	<input class="formEdicaoInputSomenteLeitura" readonly name="Nome" id="Nome"  type="text" size="100" maxlength="150" value="<%=ProcessoPartedt.getNome()%>" onkeypress="return DigitarNomeParteProcesso(this,event)" onkeyup=" autoTab(this,150)"/><br />			
						<%} else { %>
					    	<label for="Nome">*Nome</label> 
					    	<input <%if(ProcessoPartedt.getCodigoTemp() != null && ProcessoPartedt.getCodigoTemp().equals("GuiaInicial")){%>class="formEdicaoInputSomenteLeitura" readonly<%} else {%> class="formEdicaoInput" <%}%> name="Nome" id="Nome"  type="text" size="100" maxlength="150" value="<%=ProcessoPartedt.getNome()%>" onkeypress="return DigitarNomeParteProcesso(this,event)" onkeyup=" autoTab(this,150)"/>
						<%} %>
						</div>
						
						<div class="col15">
  							<label for="Sexo">Sexo</label> 
						    <select name="Sexo">
						    	<option value="M" <%=(ProcessoPartedt.isMasculino()?"selected":"")%>>Masculino</option>
						    	<option value="F" <%=(ProcessoPartedt.isFeminino()?"selected":"")%>>Feminino</option>
						    </select>
					    </div>
					    		    
					    					
						<div class="col25 clear">
						<label for="DataNascimento">Data de Nascimento <img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this);return false;"/></label> 
					    <input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 
			    		</div>
			    		
			    		<div class="col45">
			    		<label for="Id_Naturalidade">Naturalidade
			    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
			    		 <input class="FormEdicaoimgLocalizar" name="imaLimparNaturalidade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Cidade','Cidade'); return false;" title="Limpar Naturalidade">  
			    		</label>
					    <input type="hidden" name="Id_Cidade" id="Id_Cidade">  
					    
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Cidade" type="text" size="50" maxlength="60" value="<%=ProcessoPartedt.getCidadeNaturalidade()%>">
					    
					   
					    </div>

						<div class="col25 clear">
 						<label for="Id_EstadoCivil">Estado Civil
 						<input class="FormEdicaoimgLocalizar" id="imaLocalizarEstadoCivil" name="imaLocalizarEstadoCivil" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
 							<input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_EstadoCivil','EstadoCivil'); return false;" title="Limpar Estado Civil">  
 						</label>  
					    <input type="hidden" name="Id_EstadoCivil" id="Id_EstadoCivil">  
					    
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoCivil" id="EstadoCivil" type="text" size="20" maxlength="20" value="<%=ProcessoPartedt.getEstadoCivil()%>"/>
					    
					   
					    </div>
					    
					    <div class="col45">
					     <label for="Id_Profissao">Profissão
					      <input class="FormEdicaoimgLocalizar" id="imaLocalizarProfissao" name="imaLocalizarProfissao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					     	<input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Profissao','Profissao'); return false;" title="Limpar Profissão">  
					     </label>  
					    <input type="hidden" name="Id_Profissao" id="Id_Profissao">  
					   
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Profissao" id="Profissao" type="text" size="50" maxlength="50" value="<%=ProcessoPartedt.getProfissao()%>"/> 
					    
					   
					     </div>
					     
					    <div class="col45 clear"> 
						    <label for="Id_Escolaridade">Escolaridade
						     <input class="FormEdicaoimgLocalizar" id="imaLocalizarEscolaridade" name="imaLocalizarEscolaridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EscolaridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
						    	<input class="FormEdicaoimgLocalizar" name="imaLimparEscolaridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Escolaridade','Escolaridade'); return false;" title="Limpar Escolaridade">  
						    </label>  
						    <input type="hidden" name="Id_Escolaridade" id="Id_Escolaridade">  						   
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Escolaridade" id="Escolaridade" type="text" size="35" maxlength="40" value="<%=ProcessoPartedt.getEscolaridade()%>"/>					    									    					   
					    </div>					    
					    
					    <div class="col75 ">
						    <label for="NomeMae">Mãe</label> 
						    <input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="65" maxlength="100" value="<%=ProcessoPartedt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
						</div>
						
						<div class="col75">
						    <label for="NomePai">Pai</label> 
						    <input class="formEdicaoInput" name="NomePai" id="NomePai" type="text" size="65" maxlength="100" value="<%=ProcessoPartedt.getNomePai()%>" onkeyup=" autoTab(this,255)"/>
						</div>
					    
						<div class="col15 clear">
						<label for="Telefone">Telefone</label> 
					    <input class="formEdicaoInput" name="Telefone" id="Telefone"  type="text" size="15" maxlength="20" value="<%=ProcessoPartedt.getTelefone()%>" onkeyup=" autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)">
					    </div>
					    
					    <div class="col15">				    		    
				    	<label for="Celular">Celular</label> 
			    		<input class="formEdicaoInput" name="Celular" id="Celular"  type="text" size="15" maxlength="20" value="<%=ProcessoPartedt.getCelular()%>" onkeyup=" autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)"/>
			    		</div>
			    		
			    		<div class="col15">
			    		<label for="EMail">E-mail</label> 
					    <input class="formEdicaoInput" name="EMail" id="EMail"  type="text" size="40" maxlength="70" value="<%=ProcessoPartedt.getEMail()%>" onkeyup=" autoTab(this,50)"/>			    
					    </div>
					    
					    
					    <%if (ProcessoPartedt.isParteIsenta()) {%>
						    <div class="col25 clear">
							<label for="Cpf">CPF</label> 
						    <input class="formEdicaoInputSomenteLeitura" name="Cpf" id="Cpf" type="text" size="20" maxlength="11" value="<%=ProcessoPartedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" readonly/>
						    </div>
						    
						    <div class="col25">
						    <label for="Cnpj">CNPJ</label> 
						    <input class="formEdicaoInputSomenteLeitura" name="Cnpj" id="Cnpj"  type="text" size="20" maxlength="14" value="<%=ProcessoPartedt.getCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" readonly/>		
						    </div>
					    <%} else { %>
						    <div class="col25 clear">
							<label for="Cpf">CPF</label> 
						    <input class="formEdicaoInput" name="Cpf" id="Cpf" type="text" size="20" maxlength="11" value="<%=ProcessoPartedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" <%=(ProcessoPartedt.getCnpj().length()>0?"readonly":"") %>>
						    </div>
						    
						    <div class="col25">
						    <label for="Cnpj">CNPJ</label> 
						    <input class="formEdicaoInput" name="Cnpj" id="Cnpj"  type="text" size="20" maxlength="14" value="<%=ProcessoPartedt.getCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" <%=(ProcessoPartedt.getCpf().length()>0?"readonly":"") %>/>		
						    </div>
					    <%} %>
					    
					    <%if (ProcessoPartedt != null && ProcessoPartedt.getId() != null && ProcessoPartedt.getId().length()>0
					    		&& processoDt != null &&  processoDt.isCriminal()){ %>
					    	<label for="Culpado">Culpado</label> 
					    	<input class="formEdicaoInput" name="Culpado" id="Culpado"  type="checkbox"  value="true" <% if(ProcessoPartedt.getCulpado().equalsIgnoreCase("true")){%>  checked<%}%>>
					    <%} else {%>
					    	<br />
					    <%} %>
					    
					    
					    <div class="col25 clear">
					   	<label for="Rg">RG</label> 
					    <input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="50" value="<%=ProcessoPartedt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
					    </div>
					    
					    <div class="col25">
					    <label  for="Id_RgOrgaoExpedidor">Órgão Expedidor
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					     <input class="FormEdicaoimgLocalizar" name="imaLimparOrgaoExpedidor" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_RgOrgaoExpedidor','RgOrgaoExpedidor'); return false;" title="Limpar Órgão Expedidor">     
					    </label> 
					    <input type="hidden" name="Id_RgOrgaoExpedidor" id="Id_RgOrgaoExpedidor">
					    
					    <%if(request.getAttribute("RgOrgaoExpedidorEstado") != null){ %>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="RgOrgaoExpedidor" type="text" size="17" maxlength="20" value="<%=ProcessoPartedt.getRgOrgaoExpedidor()+"-"+request.getAttribute("RgOrgaoExpedidorEstado")%>">
						<% } else {%>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="RgOrgaoExpedidor" id="RgOrgaoExpedidor" type="text" size="17" maxlength="20" value="<%=ProcessoPartedt.getRgOrgaoExpedidor()%>">
						<% } %>
					    					    					   
					    </div>
					    
					    <div class="col25">
					    <label for="RgDataExpedicao">Data Expedição <img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"/></label> 
					    <input class="formEdicaoInput" name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" value="<%=ProcessoPartedt.getRgDataExpedicao()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 					    
			    		</div>
			    
			    		
			    		<div class="col25 clear">
					    <label for="TituloEleitor">Título Eleitor</label> 
					    <input class="formEdicaoInput" name="TituloEleitor" id="TituloEleitor"  type="text" size="20" maxlength="20" value="<%=ProcessoPartedt.getTituloEleitor()%>" onkeyup=" autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)">
					    </div>
					    
					    <div class="col25">
					    <label for="TituloEleitorZona">Zona</label> 
					    <input class="formEdicaoInput" name="TituloEleitorZona" id="TituloEleitorZona"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getTituloEleitorZona()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)">
					    </div>
					    
					    <div class="col25 "> 
					    <label for="TituloEleitorSecao">Seção</label> 
					    <input class="formEdicaoInput" name="TituloEleitorSecao" id="TituloEleitorSecao"  type="text" size="13" maxlength="10" value="<%=ProcessoPartedt.getTituloEleitorSecao()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/>
			    		</div>
			    		
			    		
			   			<div class="col25 clear">
					    <label for="Ctps">CTPS</label> 
					    <input class="formEdicaoInput" name="Ctps" id="Ctps"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getCtps()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)">
					    </div>
					    
					    <div class="col25">
					    <label for="CtpsSerie">Série</label> 
					    <input class="formEdicaoInput" name="CtpsSerie" id="CtpsSerie"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getCtpsSerie()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)">
					    </div>
					    
					    <div class="col25">
					    <label for="Id_CtpsUf">UF
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarCtpsUf" name="imaLocalizarCtpsUf" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					    <input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCtpsUf" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_CtpsUf','CtpsUf'); return false;" title="Limpar Estado CTPS">  
					    </label>  
					    <input type="hidden" name="Id_CtpsUf" id="Id_CtpsUf" value="<%=ProcessoPartedt.getId_CtpsUf()%>">
					    					   	
					    <input class="formEdicaoInputSomenteLeitura" readonly name="SiglaCtpsUf" id="SiglaCtpsUf" type="text" size="10" maxlength="10" value="<%=ProcessoPartedt.getEstadoCtpsUf()%>"/>
						</div>
						
					 	<div class="col25 clear">
					    <label for="Pis">PIS</label> 
					    <input class="formEdicaoInput" name="Pis" id="Pis"  type="text" size="20" maxlength="100" value="<%=ProcessoPartedt.getPis()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/>
						</div>
					    
					   <div class="col25 clear">
					   <label for="DataSentenca">Data da Sentença <img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataSentenca,'dd/mm/yyyy',this);return false;"/></label> 
					   <input class="formEdicaoInput" name="DataSentenca" id="DataSentenca"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataSentenca()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 					   
			    	   </div>

					   <div class="col25">
					   <label for="DataPronuncia">Data da Pronúncia <img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataPronuncia,'dd/mm/yyyy',this);return false;"/></label> 
					   <input class="formEdicaoInput" name="DataPronuncia" id="DataPronuncia"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataPronuncia()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 
					   
			    	   </div>

					   <div class="clear"></div>
					   <div class="space"></div>
					    
					    <%if (request.getAttribute("exibeGovernoEmpresa") != null && Boolean.valueOf(request.getAttribute("exibeGovernoEmpresa").toString())){%>   	    
					 	    
					 	    <div class="col45">
					 	    <label for="Id_GovernoTipo">Tipo de Governo
					 	     <input class="FormEdicaoimgLocalizar" id="imaLocalizarGovernoTipo" name="imaLocalizarGovernoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					 	    <input class="FormEdicaoimgLocalizar" name="imaLimparGovernoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_GovernoTipo','GovernoTipo'); return false;" title="Limpar Governo">  
					 	    </label>  
						    <input type="hidden" name="Id_GovernoTipo" id="Id_GovernoTipo">  
						   
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="GovernoTipo" id="GovernoTipo" type="text" size="50" maxlength="60" value="<%=ProcessoPartedt.getGovernoTipo()%>">
						    
						   	
						    </div>
						    
						    <div class="col45">
						    <label for="Id_EmpresaTipo">Tipo de Empresa
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarEmpresa" name="imaLocalizarEmpresa" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EmpresaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						    <input class="FormEdicaoimgLocalizar" name="imaLimparEmpresa" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_EmpresaTipo','EmpresaTipo'); return false;" title="Limpar Empresa">  
						    </label>
						    <input type="hidden" name="Id_EmpresaTipo" id="Id_EmpresaTipo">  
						    
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="EmpresaTipo" id="EmpresaTipo" type="text" size="50" maxlength="60" value="<%=ProcessoPartedt.getEmpresaTipo()%>">
													    
							</div>
							<div class="clear"></div>
						<%}%>
						
						
						<%if (request.getAttribute("exibeparteEnderecoDesconhecido") != null && Boolean.valueOf(request.getAttribute("exibeparteEnderecoDesconhecido").toString())){%>
							<br />
							<input type="checkbox" class="formEdicaoInput" id="chkEndDesc" value="Endereço desconhecido" onchange="processaHabilitacaoEndereco(this)" title="Endereço desconhecido" <%if(ProcessoPartedt.isParteEnderecoDesconhecido()){%> checked <%}%> />Endereço desconhecido
							
						<%}%>
					    					   
					    <%@ include file="Padroes/CadastroEndereco.jspf"%> 
					    
	                	<%if (request.getAttribute("exibeparteLocomocoes") != null && request.getAttribute("exibeparteLocomocoes").toString().length()>0 && Boolean.valueOf(request.getAttribute("exibeparteLocomocoes").toString())){%>
							<br />
							<input type="checkbox" class="formEdicaoInput" id="chkParteLocomocao" value="Informar Locomocoes" onchange="processaHabilitacaoParteLocomocoes(this)" title="Gerar Intimação/Locomoções" <%if(ProcessoPartedt.isParteLocomocoes()){%> checked <%}%> />Gerar Intimação/Locomoções
						
                	
		                	<fieldset id="idParteLocomocoes"> 
		                	<legend>
		                		Finalidade
		                	</legend>
		                	
		                	<div class="col35"><label>Escolha a Finalidade</label>
		                	
		                		<select id="finalidade" name="finalidade" onChange="finalidadeGuiaLocomocaoAlterada()">
		                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.LOCOMOCAO)?"selected":"")%>>Locomoção</option>
		                			<option value="<%=GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.PENHORA_AVALIACAO_ALIENACAO)?"selected":"")%>>Penhora, avaliação e alienação</option>
		                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO)?"selected":"")%>>Citação, penhora, avaliação e alienação</option>
		                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO)?"selected":"")%>>Citação, penhora e praça/leilão</option>
		                			<option value="<%=GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO)?"selected":"")%>>Citação, penhora, avaliação e praça/leilão</option>
		                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO)?"selected":"")%>>Locomoção para avaliação</option>
		                			<option value="<%=GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA%>" <%=((ProcessoPartedt.getLocomocaoDt().getFinalidadeCodigo() == GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA)?"selected":"")%>>Locomoção para avaliação e Praça</option>
		                		</select>
		                	</div>
		                	
		                	<% if(Funcoes.StringToBoolean(String.valueOf(request.getAttribute("exibeOficialCompanheiro")))) { %>
			                	<div class="col35"><label>Oficial Companheiro?</label>
			                	
			                		<select id="oficialCompanheiro" name="oficialCompanheiro" onChange="oficialCompanheiroAlterado()">
			                			<option value="<%=false%>" <%=(ProcessoPartedt.getLocomocaoDt().isOficialCompanheiro()?"":"selected")%> >NÃO</option>
			                			<option value="<%=true%>" <%=(ProcessoPartedt.getLocomocaoDt().isOficialCompanheiro()?"selected":"")%> >SIM</option>
			               			</select>
			                	</div>
		                	<% } %>
		                	
		                	<div class="clear"></div>
		                	
		                	<div class="col35"><label>Penhora?</label>		                	
		                		<select id="penhora" name="penhora" onChange="penhoraAlterada()">
		                			<option value="<%=false%>" <%=(ProcessoPartedt.getLocomocaoDt().isPenhora()?"":"selected")%> >NÃO</option>
		                			<option value="<%=true%>" <%=(ProcessoPartedt.getLocomocaoDt().isPenhora()?"selected":"")%> >SIM</option>
		               			</select>
		                	</div>
		                	
		                	<div class="col35"><label>Intimação?</label>		                	
		                		<select id="intimacao" name="intimacao">
		                			<option value="<%=false%>" <%=(ProcessoPartedt.getLocomocaoDt().isIntimacao()?"":"selected")%> >NÃO</option>
		                			<option value="<%=true%>" <%=(ProcessoPartedt.getLocomocaoDt().isIntimacao()?"selected":"")%> >SIM</option>
		               			</select>
		                	</div>
		                	
		                	<div class="col35"><label>Quantidade:</label>
								<span style="width: 120px;">
									<input class="formEdicaoInputSomenteLeitura" type="text" readonly id="quantidadeLocomocao" name="quantidadeLocomocao" value="<%=ProcessoPartedt.getLocomocaoDt().getQtdLocomocao()%>" size="1" />
									<input type="button" id="" name="" value="+" onclick="somarQuantidade(quantidadeLocomocao);" />
									<input type="button" id="" name="" value="-" onclick="subtrairQuantidade(quantidadeLocomocao,'0');" />
								</span>
							</div>
		                	
		                </fieldset>
                <%}%>		      
					    
					    <%if(ProcessoPartedt.getCodigoTemp() != null && ProcessoPartedt.getCodigoTemp().equals("GuiaInicial")){%>
					    	 <div id="divBotoesCentralizados" class="divBotoesCentralizados">
						    	<%String valueBotao = "Atualizar";
						    	if (request.getAttribute("valueBotaoSalvar") != null && request.getAttribute("valueBotaoSalvar").toString().length() > 0) valueBotao = request.getAttribute("valueBotaoSalvar").toString();%>
								<input name="imgInserir" type="submit" value="<%=valueBotao%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','3');"> 
						    </div>
					    <%} else { %>
						    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
						    	<%if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%>
							    	<%String valueBotao = "Inserir";
							    	if (request.getAttribute("valueBotaoSalvar") != null && request.getAttribute("valueBotaoSalvar").toString().length() > 0) valueBotao = request.getAttribute("valueBotaoSalvar").toString();%>
									<input name="imgInserir" type="submit" value="<%=valueBotao%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','3');"> 
									<input name="imgInserir" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
								<%} %>
						    </div>
					    <%} %>
			 		</fieldset>

					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>
