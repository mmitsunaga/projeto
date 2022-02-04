<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.ct.Processo2Ct"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EscolaridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AlcunhaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SinalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RacaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteSinalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="ProcessoPartedt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDt"/>
<jsp:useBean id="Enderecodt" scope="session" class= "br.gov.go.tj.projudi.dt.EnderecoDt"/>

<html>
	<head>
		<title>Cadastrar Sentenciado</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#DataNascimento").mask("99/99/9999");
				$("#dataFinalPeriodo").mask("99/99/9999");
			});
			
		</script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Cadastrar Sentenciado </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<input id="Hash" name="Hash" type="hidden" value="<%= Funcoes.GeraHashMd5(processoDt.getId_Processo()) %>" />
				<input id="HashParte" name="HashParte" type="hidden" value="<%=Funcoes.GeraHashMd5(ProcessoPartedt.getId_ProcessoParte())%>" />
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="ParteTipo" name="ParteTipo" type="hidden" value="0">
			  	<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Cadastrar Sentenciado </legend>
			
					    <label for="Nome">*Nome</label><br>
					    <input class="formEdicaoInput" name="Nome" id="Nome"  type="text" size="90"  value="<%=ProcessoPartedt.getNome()%>" onkeyup=" autoTab(this,255)"/><br />
			
						<label for="NomeMae">*Nome da Mãe</label><br> 
					    <input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="90" value="<%=ProcessoPartedt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/><br />
						
						<label for="NomePai">Nome do Pai</label><br>
					    <input class="formEdicaoInput" name="NomePai" id="NomePai" type="text" size="90"  value="<%=ProcessoPartedt.getNomePai()%>" onkeyup=" autoTab(this,255)"/><br />

					    <label for="DataNascimento">*Data de Nascimento <img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this);return false;"/></label><br>
					    <input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataNascimento()%>" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"><br>
			    
  						<label for="Sexo">Sexo</label><br> 
					    <select name="Sexo">
					    	<option value="">Selecione</option> 
					    	<option value="M" <%if (ProcessoPartedt.getSexo().equalsIgnoreCase("M")){%>selected<%}%>>Masculino</option>
					    	<option value="F" <%if (ProcessoPartedt.getSexo().equalsIgnoreCase("F")){%>selected<%}%>>Feminino</option>
					    </select>
					    <br />
				
						<label for="Cpf">Cpf</label><br>
					    <input class="formEdicaoInput" name="Cpf" id="Cpf" type="text" size="20" maxlength="11" value="<%=ProcessoPartedt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"><br />
					    
					   	<label for="Rg">Rg</label><br>
					    <input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="20" maxlength="19" value="<%=ProcessoPartedt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,50)">
					    <br></br>

						<label for="Id_Raca">*Raça 
					    <input type="hidden" name="Id_Raca" id="Id_Raca">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRaca" name="imaLocalizarRaca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RacaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					   	<input class="FormEdicaoimgLocalizar" name="imaLimparRaca" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Raca','Raca'); return false;" title="Limpar Raca">  
					    </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Raca" id="Raca" type="text" size="35" maxlength="40" value="<%=ProcessoPartedt.getRaca()%>"/><br />

					    <label for="Id_RgOrgaoExpedidor">Órgão Expedidor
					    <input type="hidden" name="Id_OrgaoExpedidor" id="Id_OrgaoExpedidor">
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); " >
					    <input class="FormEdicaoimgLocalizar" name="imaLimparOrgaoExpedidor" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_OrgaoExpedidor','OrgaoExpedidor'); return false;" title="Limpar Órgão Expedidor">     
					    </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="OrgaoExpedidor" id="OrgaoExpedidor" type="text" size="50" maxlength="20" value="<%=ProcessoPartedt.getRgOrgaoExpedidor()%>"><br> 
					    
					    <label for="RgDataExpedicao">Data Expedição <img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].RgDataExpedicao,'dd/mm/yyyy',this)"/></label><br> 
					    <input class="formEdicaoInputSomenteLeitura" name="RgDataExpedicao" id="RgDataExpedicao"  type="text" size="10" maxlength="10" onkeypress="return formataCampo(event, this, 10)" value="<%=ProcessoPartedt.getRgDataExpedicao()%>" ><br>
			    
					    <label for="Id_Naturalidade">Naturalidade
					    <input type="hidden" name="Id_Cidade" id="Id_Cidade">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					    <input class="FormEdicaoimgLocalizar" name="imaLimparNaturalidade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Cidade','Cidade'); return false;" title="Limpar Naturalidade">  
					   </label><br>
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Cidade" type="text" size="50" maxlength="60" value="<%=ProcessoPartedt.getCidadeNaturalidade()%>"><br> 
					    
					    <label for="Id_EstadoCivil">Estado Civil
					    <input type="hidden" name="Id_EstadoCivil" id="Id_EstadoCivil">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarEstadoCivil" name="imaLocalizarEstadoCivil" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					   	<input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_EstadoCivil','EstadoCivil'); return false;" title="Limpar Estado Civil">  
					   </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoCivil" id="EstadoCivil" type="text" size="20" maxlength="20" value="<%=ProcessoPartedt.getEstadoCivil()%>"/><br />
					    
					    <label for="Id_Profissao">Profissão 
					    <input type="hidden" name="Id_Profissao" id="Id_Profissao">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProfissao" name="imaLocalizarProfissao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					   	<input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Profissao','Profissao'); return false;" title="Limpar Profissão">  
					    </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Profissao" id="Profissao" type="text" size="50" maxlength="50" value="<%=ProcessoPartedt.getProfissao()%>"/> <br />
					      
					    <label for="Id_Escolaridade">Escolaridade 
					    <input type="hidden" name="Id_Escolaridade" id="Id_Escolaridade">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarEscolaridade" name="imaLocalizarEscolaridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EscolaridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >
					   	<input class="FormEdicaoimgLocalizar" name="imaLimparEscolaridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Escolaridade','Escolaridade'); return false;" title="Limpar Escolaridade">  
					   </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Escolaridade" id="Escolaridade" type="text" size="35" maxlength="40" value="<%=ProcessoPartedt.getEscolaridade()%>"/><br />

						<label for="Sinal">Alcunha</label><br>
					    <input class="formEdicaoInput" name="Alcunha" id="Alcunha" type="text" size="60" value="<%=ProcessoPartedt.getAlcunha() %>"/>
						<button type="submit" name="operacao" value="Incluir Alcunha" onClick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','12');" style="font-size: 11px !important; margin-top: 0px; vertical-align: top;" >
							<img src="imagens/16x16/Mais.png" alt="Incluir Alcunha"/>
							Incluir Alcunha
						</button><br />
						<input type="hidden" id="posicaoLista" name="posicaoLista">
						<fieldset id="VisualizaDados" class="VisualizaDados" style="width:500px; margin-left: 175px;"> 
								<%  List listaAlcunha = ProcessoPartedt.getListaAlcunhaParte();
								  	if (listaAlcunha != null && listaAlcunha.size() > 0){ %>
						   			<table width="90%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
									<%
						   	    		for (int i=0;i < listaAlcunha.size();i++){
						   	    			ProcessoParteAlcunhaDt alcunhaDt = (ProcessoParteAlcunhaDt)listaAlcunha.get(i); %>
							   			<tbody>
											<tr>
						       					<td><%=alcunhaDt.getAlcunha()%></td>
						       	 				<td><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" 
														onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','12');
																AlterarValue('posicaoLista','<%=i%>')" title="Excluir alcunha"/></td>
						       	 			</tr>
						       	 		</tbody>
						       		<%	} %>
						       	</table>
								<% } else { %>
						   			<em> Nenhuma alcunha cadastrada </em>
						   		<% } %>
							</fieldset>
						<br />
						<br />

						<label for="Sinal">Sinal Particular</label><br>
					    <input class="formEdicaoInput" name="Sinal" id="Sinal" type="text" size="60" value="<%=ProcessoPartedt.getSinal() %>"/>
						<button type="submit" name="operacao" value="Incluir Alcunha" onClick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','13');" style="font-size: 11px !important; margin-top: 0px; vertical-align: top;" >
							<img src="imagens/16x16/Mais.png" alt="Incluir Alcunha"/>
							Incluir Sinal Particular
						</button><br />
						<%@ include file="SinaisProcessoParte.jspf"%>

					    <%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
						<%@page import="br.gov.go.tj.projudi.dt.EnderecoDt"%>
						
    					<div class="col25 clear">
	    				<label for="DataSentenca">Data da Sentença <img id="calendarioDataSentenca" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataSentenca,'dd/mm/yyyy',this);return false;"/></label> 
		    			<input class="formEdicaoInput" name="DataSentenca" id="DataSentenca"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataSentenca()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 
			        	</div>

					    <div class="col25">
					    <label for="DataPronuncia">Data da Pronúncia <img id="calendarioDataPronuncia" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataPronuncia,'dd/mm/yyyy',this);return false;"/></label> 
					    <input class="formEdicaoInput" name="DataPronuncia" id="DataPronuncia"  type="text" size="20" maxlength="10" value="<%=ProcessoPartedt.getDataPronuncia()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)"> 
			    	    </div><br />

						<fieldset class="formEdicao"> 
							<legend class="formEdicaoLegenda"> Endereço </legend>
							    				
							<label for="Logradouro">Logradouro</label><br> 
							<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="106" maxlength="100" value="<%=Enderecodt.getLogradouro()%>" onkeyup=" autoTab(this,60)">
							<br />
					    				
					    	<label for="Numero">Número</label><br>
					    	<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="11" value="<%=Enderecodt.getNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"><br> 
									
							<label for="Complemento">Complemento</label><br> 
							<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="<%=Enderecodt.getComplemento()%>" onkeyup=" autoTab(this,255)">
							<br />
									
							<label for="Bairro">Bairro 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							</label><br>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="<%=Enderecodt.getBairro()%>">
							<br />
					
							<label for="Cidade">Cidade</label><br>  
							<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="<%=Enderecodt.getCidade()%>">
							<br> 
										
					   		<label for="Uf">UF</label><br>
							<input class="formEdicaoInputSomenteLeitura" readonly name="Estado" id="Uf"  type="text" size="10" maxlength="10" value="<%=Enderecodt.getUf()%>">
							<br />
							
							<label for="Cep">CEP</label><br>
							<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="<%=Enderecodt.getCep()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
							<br />
						</fieldset>
					  
					    <div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Inserir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar %>');AlterarValue('PassoEditar','3');"> 
							<input name="imgInserir" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar %>');AlterarValue('PassoEditar','14');">
					    </div>
			 		</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>
