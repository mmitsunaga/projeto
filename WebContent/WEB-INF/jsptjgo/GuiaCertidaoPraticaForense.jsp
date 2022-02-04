<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="guiaCertidaoGeralDt" scope="session" class= "br.gov.go.tj.projudi.dt.GuiaCertidaoGeralDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<script language="javascript" type="text/javascript">
	function VerificarCampos() {
		if (SeNulo(MesInicial, "O campo Mês Inicial é obrigatório!")) return false;
	    if (SeNulo(AnoInicial, "O campo Ano Inicial é obrigatório!")) return false;
	    if (SeNulo(MesFinal, "O campo Mês Final é obrigatório!")) return false;
	    if (SeNulo(AnoFinal, "O campo Ano Final é obrigatório!")) return false;
	    if (SeNulo(OabNumero, "O campo OAB Número é obrigatório!")) return false;
	    if (SeNulo(OabComplemento, "O campo OAB Complemento é obrigatório!")) return false;
	    if (SeNulo(OabUf, "O campo OAB UF é obrigatório!")) return false;
	    if (SeNulo(OabComplemento, "O campo Comarca é obrigatório!")) return false;
	    if (SeNulo(Comarca, "O campo Comarca é obrigatório!")) return false;
	    if (SeNulo(Serventia, "O campo Serventia é obrigatório!")) return false;
	}
</script>

<html>
	<head>
		<title>Certidão de Prática Forense</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	    
	    
	    <script type='text/javascript' src='/js/Funcoes.js'></script>
	    <script type='text/javascript' src='/js/jquery.js'></script>
		<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='/js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="/js/Digitacao/AutoTab.js" ></script>
		<script type='text/javascript' src='/js/ckeditor/ckeditor.js'></script>
		<script type='text/javascript' src='/js/DivFlutuante.js'></script>
		<script type='text/javascript' src='/js/Mensagens.js'></script>
		<script type='text/javascript' src='/js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='/js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='/js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
		<script type="text/javascript" src="/js/Digitacao/DigitarData.js"></script>	
	</head>	
	<body>
		
		<% boolean cabecalho = Boolean.valueOf(request.getSession().getAttribute("Cabecalho").toString());
		if (request.getSession().getAttribute("Cabecalho") != null && cabecalho == true){ %>
			<%@ include file="/CabecalhoPublico.html" %>
		<%}%>
		
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			
			<form action="GuiaCertidaoPraticaForense" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<div id="divEditar" class="divEditar">
				
					<fieldset class="formEdicao"> 
				    	<legend class="formEdicaoLegenda">Dados da Certidão</legend>
						
						<fieldset> 
					    	<legend class="formEdicaoLegenda">Tipo</legend>
							<input type="radio" name="Tipo" value="Quantitativa" <%=guiaCertidaoGeralDt.getTipo().equals("Quantitativa") ? "checked" : ""%> />&nbsp;&nbsp; Quantitativa<br/>
							<input type="radio" name="Tipo" value="Descritiva" <%=guiaCertidaoGeralDt.getTipo().equals("Quantitativa") ? "" : "checked"%>/>&nbsp;&nbsp; Normal (Detalhada)
						</fieldset>
						
						<fieldset>
							<legend class="formEdicaoLegenda">Período</legend>
							
							<div class="periodo"> 
								<label for="DataInicial">*Mês/Ano Inicial</label><br>
								<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
									<option value="1" <%if(guiaCertidaoGeralDt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
									<option value="2" <%if(guiaCertidaoGeralDt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
									<option value="3" <%if(guiaCertidaoGeralDt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
									<option value="4" <%if(guiaCertidaoGeralDt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
									<option value="5" <%if(guiaCertidaoGeralDt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
									<option value="6" <%if(guiaCertidaoGeralDt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
									<option value="7" <%if(guiaCertidaoGeralDt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
									<option value="8" <%if(guiaCertidaoGeralDt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
									<option value="9" <%if(guiaCertidaoGeralDt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
									<option value="10" <%if(guiaCertidaoGeralDt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
									<option value="11" <%if(guiaCertidaoGeralDt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
									<option value="12" <%if(guiaCertidaoGeralDt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
								</select>
								
								<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
									<% for(int i = Funcoes.StringToInt(guiaCertidaoGeralDt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
										<option value="<%=i%>" <%if(guiaCertidaoGeralDt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
									<%} %>
								</select>								
							</div>
							
							<div class="periodo">			
								<label for="DataFinal">*Mês/Ano Final</label><br>
								<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
									<option value="1" <%if(guiaCertidaoGeralDt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
									<option value="2" <%if(guiaCertidaoGeralDt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
									<option value="3" <%if(guiaCertidaoGeralDt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
									<option value="4" <%if(guiaCertidaoGeralDt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
									<option value="5" <%if(guiaCertidaoGeralDt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
									<option value="6" <%if(guiaCertidaoGeralDt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
									<option value="7" <%if(guiaCertidaoGeralDt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
									<option value="8" <%if(guiaCertidaoGeralDt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
									<option value="9" <%if(guiaCertidaoGeralDt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
									<option value="10" <%if(guiaCertidaoGeralDt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
									<option value="11" <%if(guiaCertidaoGeralDt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
									<option value="12" <%if(guiaCertidaoGeralDt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
								</select>
								<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
								<% for(int i = Funcoes.StringToInt(guiaCertidaoGeralDt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
									<option value="<%=i%>" <%if(guiaCertidaoGeralDt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
								<% } %>
								</select>
							</div>
									
						</fieldset>					
						
						<!-- OAB's do Advogado -->
					    <fieldset>
					    	<legend class="formEdicaoLegenda">Dados do Advogado</legend>
					    	
					    	<div class="col30 clear">			
					    		<label class="formEdicaoLabel" for="OabNumero">*Número OAB/Matrícula </label><br> 
					    		<input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=guiaCertidaoGeralDt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
					   		</div>
					   			
					   		<div class="col15">
					   			<label class="formEdicaoLabel" for="OabComplemento">*OAB Complemento</label><br> 
					   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
						        	<option></option>
						        	<option <%if(guiaCertidaoGeralDt.getOabComplemento().equals("N")){%>selected="true"<%}%>>N</option>
							       	<option <%if(guiaCertidaoGeralDt.getOabComplemento().equals("A")){%>selected="true"<%}%>>A</option>
							        <option <%if(guiaCertidaoGeralDt.getOabComplemento().equals("B")){%>selected="true"<%}%>>B</option>
					   		        <option <%if(guiaCertidaoGeralDt.getOabComplemento().equals("S")){%>selected="true"<%}%>>S</option>
						    	</select>
						    </div>
						    	
						    <div class="col20">	
						    	<label class="formEdicaoLabel" for="OabUfLabel">*OAB UF</label><br> 
						    	<select id="OabUf" name ="OabUf" class="formEdicaoCombo">
									<option value=""></option>
									<option value="1-GO" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("1")){%>selected="true"<%}%>>GO</option>
									<option value="15-AC" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("15")){%>selected="true"<%}%>>AC</option>
									<option value="12-AL" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("12")){%>selected="true"<%}%>>AL</option>
									<option value="16-AM" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("16")){%>selected="true"<%}%>>AM</option>
									<option value="25-AP" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("25")){%>selected="true"<%}%>>AP</option>
									<option value="5-BA" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("5")){%>selected="true"<%}%>>BA</option>
									<option value="20-CE" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("20")){%>selected="true"<%}%>>CE</option>
									<option value="27-DF" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("27")){%>selected="true"<%}%>>DF</option>
									<option value="6-ES" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("6")){%>selected="true"<%}%>>ES</option>
									<option value="24-MA" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("24")){%>selected="true"<%}%>>MA</option>
									<option value="3-MG" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("3")){%>selected="true"<%}%>>MG</option>
									<option value="17-MS" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("17")){%>selected="true"<%}%>>MS</option>
									<option value="18-MT" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("18")){%>selected="true"<%}%>>MT</option>
									<option value="2-PA" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("2")){%>selected="true"<%}%>>PA</option>
									<option value="22-PB" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("22")){%>selected="true"<%}%>>PB</option>
									<option value="4-PE" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("4")){%>selected="true"<%}%>>PE</option>
									<option value="23-PI" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("23")){%>selected="true"<%}%>>PI</option>
									<option value="10-PR" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("10")){%>selected="true"<%}%>>PR</option>
									<option value="7-RJ" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("7")){%>selected="true"<%}%>>RJ</option>
									<option value="21-RN" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("21")){%>selected="true"<%}%>>RN</option>
									<option value="13-RO" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("13")){%>selected="true"<%}%>>RO</option>
									<option value="14-RR" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("14")){%>selected="true"<%}%>>RR</option>
									<option value="9-RS" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("9")){%>selected="true"<%}%>>RS</option>
									<option value="19-SC" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("19")){%>selected="true"<%}%>>SC</option>
									<option value="26-SE" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("26")){%>selected="true"<%}%>>SE</option>
									<option value="8-SP" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("8")){%>selected="true"<%}%>>SP</option>
									<option value="11-TO" <%if(guiaCertidaoGeralDt.getOabUfCodigo().equals("11")){%>selected="true"<%}%>>TO</option>
								</select>
							</div>
					    	
					    	<div class="col50">
					  			<label class="formEdicaoLabel" for="Nome">Nome</label><br> 
		    					<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=guiaCertidaoGeralDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
		    				</div>					
									
		    				<div class="col15">	
				    			<label class="formEdicaoLabel" for="Sexo">Sexo</label><br> 
						   		<select name="Sexo" id="Sexo" class="formEdicaoCombo" >
								<option value="F">F</option>
								<option value="M">M</option>
							    <option selected><%=guiaCertidaoGeralDt.getSexo()%></option>
								</select>
		    				</div>
		    			
	    					<div class="col30 clear">	
				    			<label class="formEdicaoLabel" for="Id_EstadoCivil">Estado Civil
				    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarEstadoCivil" name="imaLocalizarEstadoCivil" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
								<input class="FormEdicaoimgLocalizar" name="imaLimparEstadoCivil" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_EstadoCivil','EstadoCivil'); return false;" title="Limpar Estado Civil">  
				    			</label><br>  
								<input type="hidden" name="Id_EstadoCivil" id="Id_EstadoCivil">
								<input class="formEdicaoInputSomenteLeitura"  readonly name="EstadoCivil" id="EstadoCivil" type="text" size="20" maxlength="20" value="<%=guiaCertidaoGeralDt.getEstadoCivil()%>"/>					
							</div>
						
							<div class="col45">	
						   		<label class="formEdicaoLabel" for="Id_Naturalidade">Naturalidade
						   	 	<input class="FormEdicaoimgLocalizar" id="imaLocalizarNaturalidade" name="imaLocalizarNaturalidade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							    <input class="FormEdicaoimgLocalizar" name="imaLimparNaturalidade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Cidade','Cidade'); return false;" title="Limpar Naturalidade">  
						   	 	</label><br>
							    <input type="hidden" name="Id_Cidade" id="Id_Cidade">
							    <input class="formEdicaoInputSomenteLeitura"  readonly name="Cidade" id="Cidade" type="text" size="50" maxlength="60" value="<%=guiaCertidaoGeralDt.getNaturalidade()%>">
							</div>						
							
							<div class="col30 clear">	
				    			<label class="formEdicaoLabel" for="Rg">RG</label><br> 
				    			<input class="formEdicaoInput" name="Rg" id="Rg"  type="text" size="30" maxlength="18" value="<%=guiaCertidaoGeralDt.getRg()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="O RG do requerente."/>
		    				</div>
		    				
		    				<div class="col25">
							    <label class="formEdicaoLabel" for="Id_RgOrgaoExpedidor">Órgão Expedidor
							    <input class="FormEdicaoimgLocalizar" id="imaLocalizarRgOrgaoExpedidor" name="imaLocalizarRgOrgaoExpedidor" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"></label><br>				    
							    <input class="formEdicaoInputSomenteLeitura" readonly name="RgOrgaoExpedidor" id="RgOrgaoExpedidor" type="text" size="24" maxlength="255" value="<%=(guiaCertidaoGeralDt.getRgOrgaoExpedidor()!=null)?guiaCertidaoGeralDt.getRgOrgaoExpedidor():""%>">
						    </div>
						    
							<div class="col30 clear">		    									
								<label class="formEdicaoLabel" for="Cpf">CPF</label>
				    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="18" value="<%=guiaCertidaoGeralDt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do requerente."/>
				    		</div>
				    			
				    		<div class="col25">
							    <label class="formEdicaoLabel" for="Id_Comarca">*Comarca
							    <input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"></label><br>				    
							    <input class="formEdicaoInputSomenteLeitura" readonly name="Comarca" id="Comarca" type="text" size="24" maxlength="255" value="<%=(guiaCertidaoGeralDt.getComarca()!=null)?guiaCertidaoGeralDt.getComarca():""%>">
						    </div>
						    	
						    <div class="col25">
							    <label class="formEdicaoLabel" for="Id_Serventia">*Serventia
							    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')"></label><br>				    
							    <input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="24" maxlength="255" value="<%=(guiaCertidaoGeralDt.getServentia()!=null)?guiaCertidaoGeralDt.getServentia():""%>">
						    </div>
					    	
					    </fieldset>
					</fieldset>
				</div>			
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
                    <button name="imgPreviaCalculo" value="Prévia do Cálculo" id="botaoPrevia" onclick="javascript:VerificarCampos();AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>')">
	                   	<img src="./imagens/16x16/calculadora.png" alt="Prévia do Cálculo">
                    	Prévia do Cálculo
                    </button>
                    <button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
                    	<img src="./imagens/16x16/edit-clear.png" alt="Limpar">
                    	Limpar
                    </button>
                </div><br><br>		
  			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>