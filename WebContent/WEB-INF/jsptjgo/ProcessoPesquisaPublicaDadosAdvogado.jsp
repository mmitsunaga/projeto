<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="Estadodt" scope="session" class= "br.gov.go.tj.projudi.dt.EstadoDt"/>

<html>
	<head>
		<title>Busca Pública de Processos pelos Dados do Advogado</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	</head>

	<body class="fundo">
		<%@ include file="/CabecalhoPublico.html" %>   
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcessoPublica" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca Pública de Processos pelos Dados do Advogado</legend>                       						   					    
					    
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					     <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">  
					    </label><br>
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=processoDt.getId_Serventia()%>">  
					   
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=processoDt.getServentia()%>"/><br />
					    
					    <!-- OAB's do Advogado -->
				    	<fieldset>
				    		<legend class="formEdicaoLegenda">Dados do Advogado</legend>
				    		<div class="col25">
				    		<label class="formEdicaoLabel" for="OabNumero">*OAB/Matrícula</label> <br>
				    		<input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=processoDt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
				   			</div>
				   			<div class="col25">
				   			<label class="formEdicaoLabel" for="OabComplemento">*OAB Complemento</label><br> 
				   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
					        		<option></option>
					        		<option <%if(processoDt.getOabComplemento().equals("N")){%>selected="true"<%}%>>N</option>
						       		<option <%if(processoDt.getOabComplemento().equals("A")){%>selected="true"<%}%>>A</option>
						        	<option <%if(processoDt.getOabComplemento().equals("B")){%>selected="true"<%}%>>B</option>
				   		        	<option <%if(processoDt.getOabComplemento().equals("S")){%>selected="true"<%}%>>S</option>
					    	</select>
					    	</div>
					    	<div class="col25">
					    	<label class="formEdicaoLabel" for="OabUf">*OAB UF</label><br> 
					    	<select id="OabUf" name ="OabUf" class="formEdicaoCombo">
								<option value=""></option>
								<option value="1" <%if(processoDt.getOabUf().equals("1")){%>selected="true"<%}%>>GO</option>
								<option value="15" <%if(processoDt.getOabUf().equals("15")){%>selected="true"<%}%>>AC</option>
								<option value="12" <%if(processoDt.getOabUf().equals("12")){%>selected="true"<%}%>>AL</option>
								<option value="16" <%if(processoDt.getOabUf().equals("16")){%>selected="true"<%}%>>AM</option>
								<option value="25" <%if(processoDt.getOabUf().equals("25")){%>selected="true"<%}%>>AP</option>
								<option value="5" <%if(processoDt.getOabUf().equals("5")){%>selected="true"<%}%>>BA</option>
								<option value="20" <%if(processoDt.getOabUf().equals("20")){%>selected="true"<%}%>>CE</option>
								<option value="27" <%if(processoDt.getOabUf().equals("27")){%>selected="true"<%}%>>DF</option>
								<option value="6" <%if(processoDt.getOabUf().equals("6")){%>selected="true"<%}%>>ES</option>
								<option value="24" <%if(processoDt.getOabUf().equals("24")){%>selected="true"<%}%>>MA</option>
								<option value="3" <%if(processoDt.getOabUf().equals("3")){%>selected="true"<%}%>>MG</option>
								<option value="17" <%if(processoDt.getOabUf().equals("17")){%>selected="true"<%}%>>MS</option>
								<option value="18" <%if(processoDt.getOabUf().equals("18")){%>selected="true"<%}%>>MT</option>
								<option value="2" <%if(processoDt.getOabUf().equals("2")){%>selected="true"<%}%>>PA</option>
								<option value="22" <%if(processoDt.getOabUf().equals("22")){%>selected="true"<%}%>>PB</option>
								<option value="4" <%if(processoDt.getOabUf().equals("4")){%>selected="true"<%}%>>PE</option>
								<option value="23" <%if(processoDt.getOabUf().equals("23")){%>selected="true"<%}%>>PI</option>
								<option value="10" <%if(processoDt.getOabUf().equals("10")){%>selected="true"<%}%>>PR</option>
								<option value="7" <%if(processoDt.getOabUf().equals("7")){%>selected="true"<%}%>>RJ</option>
								<option value="21" <%if(processoDt.getOabUf().equals("21")){%>selected="true"<%}%>>RN</option>
								<option value="13" <%if(processoDt.getOabUf().equals("13")){%>selected="true"<%}%>>RO</option>
								<option value="14" <%if(processoDt.getOabUf().equals("14")){%>selected="true"<%}%>>RR</option>
								<option value="9" <%if(processoDt.getOabUf().equals("9")){%>selected="true"<%}%>>RS</option>
								<option value="19" <%if(processoDt.getOabUf().equals("19")){%>selected="true"<%}%>>SC</option>
								<option value="26" <%if(processoDt.getOabUf().equals("26")){%>selected="true"<%}%>>SE</option>
								<option value="8" <%if(processoDt.getOabUf().equals("8")){%>selected="true"<%}%>>SP</option>
								<option value="11" <%if(processoDt.getOabUf().equals("11")){%>selected="true"<%}%>>TO</option>
							</select>
							</div>
							
				    	</fieldset>
				    	
				    	<fieldset class="formEdicao">
					    <legend class="formEdicaoLegenda">DADOS DO PROCESSO</legend>                       						   					    
				    		<div class="col45">
								 <label class="formEdicaoLabel" for="situacaoAdvogadoProcesso">Situação do advogado no processo: </label><br>
							     	<select name="situacaoAdvogadoProcesso" id="situacaoAdvogadoProcesso" class="formEdicaoCombo">
							     			<option value="3" <%if(processoDt.getSituacaoAdvogadoProcesso() != null && processoDt.getSituacaoAdvogadoProcesso().equals("3")){%>selected="true"<%}%>>Ativo ou Inativo</option>
							        		<option value="1" <%if(processoDt.getSituacaoAdvogadoProcesso() != null && processoDt.getSituacaoAdvogadoProcesso().equals("1")){%>selected="true"<%}%>>Ativo</option>
							        		<option value="2" <%if(processoDt.getSituacaoAdvogadoProcesso() != null && processoDt.getSituacaoAdvogadoProcesso().equals("2")){%>selected="true"<%}%>>Inativo</option>
							    	</select>
							    
						     </div>
				    	<div class="clear"></div>
				    	</fieldset>
				    	
						<%@ include file="Padroes/reCaptcha.jspf" %>
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
						</div>
					</fieldset>		
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>