<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>

<jsp:useBean id="ServentiaCargodt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.CargoTipoDt"%>
<html>
	<head>
		<title>Cadastro de Cargo da Serventia</title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		
		<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
		
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>		
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarData.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script language="javascript" type="text/javascript">
			
			function serventiaCargoSustituicao(check){		
				if (check.checked){
					Mostrar('divLocalizar');
					Ocultar('divLocalizar2');				
					AlterarValue('emSubstituicao', 'true');			
				}else{
					Ocultar('divLocalizar');
					Mostrar('divLocalizar2');
					AlterarValue('emSubstituicao', 'false');	
				}				
			}
		</script>
		
		<script type="text/javascript">
			//<![CDATA[
			onload = function()
			{				
				var check = document.getElementById('chkSubst');
				serventiaCargoSustituicao(check);
			}
			//]]>
		</script>		
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Cadastro de Cargo da Serventia</h2></div>
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
				<form action="ServentiaCargo" method="post" name="Formulario" id="Formulario" onsubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
				<form action="ServentiaCargo" method="post" name="Formulario" id="Formulario">
			<%}%>
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />  
					<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
					<input id="emSubstituicao" name="emSubstituicao" type="hidden" value="<%=request.getAttribute("emSubstituicao")%>" />
					<input type="hidden" name="__Pedido__" value="<%=request.getAttribute("__Pedido__")%>" />
					<input type="hidden" id="tempFluxo2" name="tempFluxo2"  value="<%=request.getAttribute("tempFluxo2")%>"/>
					<input type="hidden" id="estruturaPadrao" name="estruturaPadrao"  value="<%=request.getAttribute("estruturaPadrao")%>"/>
					
					<div id="divPortaBotoes" class="divPortaBotoes">
						<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
						<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />
						<input id="imgEditar" class="imgEditar" title="Copiar Cargo" name="imgEditar" type="image" src="./imagens/imgEditar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
						<input id="imgEditar" class="imgEditar" title="Criar Estrutura Padrão" name="imgEditar" type="image" src="./imagens/imgEstrutura.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" />
					</div>
					
					<div id="divEditar" class="divEditar">
						<fieldset class="formEdicao">
							<legend class="formEdicaoLegenda">Cadastro de Cargo da Serventia </legend>
							
							<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('tempFluxo2','1')" > 
							</label><br> 
							<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=ServentiaCargodt.getServentia()%>"/>
								<label for="Aviso" style="float:left;margin-left:25px" ><small>Selecione a Serventia para ver os Cargos disponíveis.</small></label><br> <br />
							
							<label class="formEdicaoLabel" for="Id_ServentiaCargo">Identificador</label><br> 
							<input class="formEdicaoInputSomenteLeitura" name="Id_ServentiaCargo"  id="Id_ServentiaCargo"  type="text"  readonly="true" value="<%=ServentiaCargodt.getId()%>"/><br />
							
							<label class="formEdicaoLabel" for="ServentiaCargo">*Descrição</label><br> 
							<input class="formEdicaoInput" name="ServentiaCargo" id="ServentiaCargo"  type="text" size="64" maxlength="60" value="<%=ServentiaCargodt.getServentiaCargo()%>" onkeyup=" autoTab(this,60)"/><br />
							
							
							<label class="formEdicaoLabel" for="Id_CargoTipo">*Tipo de Cargo 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarCargoTipo" name="imaLocalizarCargoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CargoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 					
							</label><br> 
							<input  class="formEdicaoInputSomenteLeitura" readonly="true" name="CargoTipo" id="CargoTipo" type="text" size="60" maxlength="100" value="<%=ServentiaCargodt.getCargoTipo()%>"/>
							<input name="Id_CargoTipo" id="Id_CargoTipo" type="hidden"  value="<%=ServentiaCargodt.getId_CargoTipo()%>" />
							
							<%if(ServentiaCargodt.getId_CargoTipo() != null && ServentiaCargodt.getId_CargoTipo().length()>0
									&& ServentiaCargodt.getId_CargoTipo().equals("19")) {%>
							<input type="checkbox" class="formEdicaoInput" id="chkSubst" value="Em Substituição" onchange="serventiaCargoSustituicao(this)" title="Em Sustituição" <%if(ServentiaCargodt.isSubstituicao()){%> checked="true" <%}%> >Substituto 2º Grau</input>
							<br />
							<%} else { %>
							<input type="hidden" class="formEdicaoInput" id="chkSubst"  onchange="serventiaCargoSustituicao(this)"/>
							<br />
							<%} %>
							
							<label class="formEdicaoLabel" for="Id_UsuarioServentiaGrupo">Usuário
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioServentiaGrupo" name="imaLocalizarUsuarioServentiaGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 					
							<input class="FormEdicaoimgLocalizar" id="imaLimparUsuarioServentiaGrupo" name="imaLimparUsuarioServentiaGrupo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_UsuarioServentiaGrupo','UsuarioServentiaGrupo'); document.getElementById('Nome').value=''; return false;" > 
							</label><br>  
							<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="UsuarioServentiaGrupo" id="UsuarioServentiaGrupo" type="text" size="30" maxlength="20" value="<%=ServentiaCargodt.getUsuarioServentiaGrupo()%>">
							<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Nome" id="Nome" type="text" size="70" value="<%=ServentiaCargodt.getNomeUsuario()%>">
							<input name="Id_UsuarioServentiaGrupo" id="Id_UsuarioServentiaGrupo" type="hidden"  value="" /><br />
							
							<div id="divLocalizar" class="divLocalizar" >
								<fieldset id="formLocalizar" class="formLocalizar"> 
									<legend class="formEdicaoLegenda">Período de Susbtituição </legend>
									    				
									<label class="formLocalizarLabel">*Período</label><br> 
									<input class="formLocalizarInput" name="dataInicio" id="dataInicio" type="text" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" onkeyup="mascara_data(this)" value="<%=ServentiaCargodt.getDataInicialSubstituicao()%>" maxlength="60" 
											title="Data inicial da Substituição" />
									<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
											alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicio,'dd/mm/yyyy',this)" />
									     
									<label>a</label><br>
									<input class="formLocalizarInput" name="dataFinal" id="dataFinal"  onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" onkeyup="mascara_data(this)" type="text" value="<%=ServentiaCargodt.getDataFinalSubstituicao()%>" maxlength="60" 
											title="Data final da Substituição" />
									<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
										alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" />
									<br />
									<label class="formEdicaoLabel" for="Id_ServentiaSubtipo">*Sub-Tipo Serventia 
		    						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaSubtipo" name="imaLocalizarServentiaSubtipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ServentiaSubtipoDt.CodigoPermissao - ServentiaCargoDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
		    						<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaSubtipo" name="imaLimparServentiaSubtipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaSubtipo','ServentiaSubtipo'); return false;" > 
		    						</label><br> 
		    						<input  name='Id_ServentiaSubtipo' id='Id_ServentiaSubtipo' type='hidden'  value=''> 
		    						<input class="formEdicaoInputSomenteLeitura" readonly name="ServentiaSubtipo" id="ServentiaSubtipo" type="text" size="48" maxlength="60" value="<%=ServentiaCargodt.getServentiaSubtipo()%>"/><br />
									<label class="formEdicaoLabel" for="PrazoAgenda"> Prazo Agenda</label><br> 
									<input class="formEdicaoInput" name="PrazoAgendaSub" id="PrazoAgendaSub"  type="text" size="6" maxlength="6" value="<%=ServentiaCargodt.getPrazoAgenda()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
								</fieldset>
							</div>
							
							<div id="divLocalizar2" class="divLocalizar2" >
								<label class="formEdicaoLabel" for="QuantidadeDistribuicao">*Quantidade Distribuição</label><br> 
								<input class="formEdicaoInput" name="QuantidadeDistribuicao" id="QuantidadeDistribuicao"  type="text" size="11" maxlength="11" value="<%=ServentiaCargodt.getQuantidadeDistribuicao()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
								<label for="Aviso" style="float:left;margin-left:25px" ><small>Valores possíveis: Atribuir 0 para NÃO receber processos ou 1 (ou mais) para receber processos.</small></label><br> <br />
								
								<label class="formEdicaoLabel" for="PrazoAgenda"> Prazo Agenda</label><br> 
								<input class="formEdicaoInput" name="PrazoAgenda" id="PrazoAgenda"  type="text" size="6" maxlength="6" value="<%=ServentiaCargodt.getPrazoAgenda()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
							</div>
						
					    	<%if (ServentiaCargodt.getListaCargosServentia() != null) { %>
						<fieldset class="formEdicao">
					    	<legend class="formEdicaoLegenda">Cargos da Serventia</legend> 
					    		<div id="divTabela" class="divTabela" > 
							    	<table id="Tabela" class="Tabela">
							        	<thead>
							            	<tr class="TituloColuna">
							                	<th></td>                  
							                  	<th>Cargo</th>                
							                  	<th>Servidor</th>
							                  	<th>Excluir</th>
							                  	<th>Editar</th>
							               	</tr>
							           	</thead>
							           	<tbody id="tabListaUsuario">
										<%
							  			List liTemp = ServentiaCargodt.getListaCargosServentia();
										ServentiaCargoDt objTemp;
							  			String stTempNome="";
							  			for(int i = 0 ; i< liTemp.size();i++) {
							      			objTemp = (ServentiaCargoDt)liTemp.get(i); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                <tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                <tr class="TabelaLinha2">
											<%}%>
							                	<td> <%=i+1%></td>                   
							                   	<td><%= objTemp.getServentiaCargo()%> - ID: <%= objTemp.getId()%></td>               
							                   	<td><%= objTemp.getNomeUsuario()%></td>
							     				<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Excluir Cargo Serventia" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_ServentiaCargo','<%=objTemp.getId()%>')"> </td>
							     				<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Editar Cargo Serventia" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('Id_ServentiaCargo','<%=objTemp.getId()%>')"></td>
							               </tr>
										<%}%>
							           </tbody>
							       </table>
						   </div> 
					    	
				    	</fieldset>
					    	<%}%>
					 </fieldset>
						<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					</div>
				</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>