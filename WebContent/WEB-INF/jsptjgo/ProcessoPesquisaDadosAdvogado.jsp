<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="Estadodt" scope="session" class= "br.gov.go.tj.projudi.dt.EstadoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%><html>
	<head>
		<title>Busca de Processos pelos Dados do Advogado</title>
	
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

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcesso" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id='fieldsetDadosProcesso'> 
					    <legend class="formEdicaoLegenda">Busca de Processos pelos Dados do Advogado</legend>
                       	
                       	<label class="formEdicaoLabel" for="Id_ProcessoStatus">Status do Processo
                       	 <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoStatus" name="imaLocalizarProcessoStatus" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'ProcessoStatus', 'Consulta de Status de Processo', 'Digite o Status e clique em consultar.', 'Id_ProcessoStatus', 'ProcessoStatus', ['Processo Status'], ['ProcessoStatusCodigo'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >  
                       	</label><br>  
					   <input type="hidden" name="Id_ProcessoStatusCodigo" id="Id_ProcessoStatusCodigo" value="<%=buscaProcessoDt.getId_ProcessoStatus() %>">
					    <input type="hidden" name="ProcessoStatusCodigo" id="ProcessoStatusCodigo" value="<%=buscaProcessoDt.getProcessoStatusCodigo() %>">
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoStatus" id="ProcessoStatus" type="text" size="67" maxlength="100" value="<%=buscaProcessoDt.getProcessoStatus()%>"/><br />
					    
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					     <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"   onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'Serventia', 'Consulta de Serventia', 'Digite a Serventia e clique em consultar.', 'Id_Serventia', 'Serventia', ['Serventia'], ['Estado'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">  
					    </label><br>
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia">  
					   
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=buscaProcessoDt.getServentia()%>"/><br />
					    
					    <!-- OAB's do Advogado -->
				    	<fieldset>
				    		<legend class="formEdicaoLegenda">Dados do Advogado</legend>
				    		<div class="col25">
				    		<label class="formEdicaoLabel" for="OabNumero">*OAB/Matrícula</label> <br>
				    		<input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=buscaProcessoDt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
				   			</div>
				   			<div class="col25">
				   			<label class="formEdicaoLabel" for="OabComplemento">OAB Complemento</label><br> 
				   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
					        		<option></option>
					        		<option <%if(buscaProcessoDt.getOabComplemento().equals("N")){%>selected="true"<%}%>>N</option>
						       		<option <%if(buscaProcessoDt.getOabComplemento().equals("A")){%>selected="true"<%}%>>A</option>
						        	<option <%if(buscaProcessoDt.getOabComplemento().equals("B")){%>selected="true"<%}%>>B</option>
				   		        	<option <%if(buscaProcessoDt.getOabComplemento().equals("S")){%>selected="true"<%}%>>S</option>
					    	</select>
					    	</div>
					    	<div class="col25">
					    	<label class="formEdicaoLabel" for="OabUf">OAB UF</label><br> 
					    	<select id="OabUf" name ="OabUf" class="formEdicaoCombo">
								<option value=""></option>
								<option value="1" <%if(buscaProcessoDt.getOabUf().equals("1")){%>selected="true"<%}%>>GO</option>
								<option value="15" <%if(buscaProcessoDt.getOabUf().equals("15")){%>selected="true"<%}%>>AC</option>
								<option value="12" <%if(buscaProcessoDt.getOabUf().equals("12")){%>selected="true"<%}%>>AL</option>
								<option value="16" <%if(buscaProcessoDt.getOabUf().equals("16")){%>selected="true"<%}%>>AM</option>
								<option value="25" <%if(buscaProcessoDt.getOabUf().equals("25")){%>selected="true"<%}%>>AP</option>
								<option value="5" <%if(buscaProcessoDt.getOabUf().equals("5")){%>selected="true"<%}%>>BA</option>
								<option value="20" <%if(buscaProcessoDt.getOabUf().equals("20")){%>selected="true"<%}%>>CE</option>
								<option value="27" <%if(buscaProcessoDt.getOabUf().equals("27")){%>selected="true"<%}%>>DF</option>
								<option value="6" <%if(buscaProcessoDt.getOabUf().equals("6")){%>selected="true"<%}%>>ES</option>
								<option value="24" <%if(buscaProcessoDt.getOabUf().equals("24")){%>selected="true"<%}%>>MA</option>
								<option value="3" <%if(buscaProcessoDt.getOabUf().equals("3")){%>selected="true"<%}%>>MG</option>
								<option value="17" <%if(buscaProcessoDt.getOabUf().equals("17")){%>selected="true"<%}%>>MS</option>
								<option value="18" <%if(buscaProcessoDt.getOabUf().equals("18")){%>selected="true"<%}%>>MT</option>
								<option value="2" <%if(buscaProcessoDt.getOabUf().equals("2")){%>selected="true"<%}%>>PA</option>
								<option value="22" <%if(buscaProcessoDt.getOabUf().equals("22")){%>selected="true"<%}%>>PB</option>
								<option value="4" <%if(buscaProcessoDt.getOabUf().equals("4")){%>selected="true"<%}%>>PE</option>
								<option value="23" <%if(buscaProcessoDt.getOabUf().equals("23")){%>selected="true"<%}%>>PI</option>
								<option value="10" <%if(buscaProcessoDt.getOabUf().equals("10")){%>selected="true"<%}%>>PR</option>
								<option value="7" <%if(buscaProcessoDt.getOabUf().equals("7")){%>selected="true"<%}%>>RJ</option>
								<option value="21" <%if(buscaProcessoDt.getOabUf().equals("21")){%>selected="true"<%}%>>RN</option>
								<option value="13" <%if(buscaProcessoDt.getOabUf().equals("13")){%>selected="true"<%}%>>RO</option>
								<option value="14" <%if(buscaProcessoDt.getOabUf().equals("14")){%>selected="true"<%}%>>RR</option>
								<option value="9" <%if(buscaProcessoDt.getOabUf().equals("9")){%>selected="true"<%}%>>RS</option>
								<option value="19" <%if(buscaProcessoDt.getOabUf().equals("19")){%>selected="true"<%}%>>SC</option>
								<option value="26" <%if(buscaProcessoDt.getOabUf().equals("26")){%>selected="true"<%}%>>SE</option>
								<option value="8" <%if(buscaProcessoDt.getOabUf().equals("8")){%>selected="true"<%}%>>SP</option>
								<option value="11" <%if(buscaProcessoDt.getOabUf().equals("11")){%>selected="true"<%}%>>TO</option>
							</select>
							</div>
				    	</fieldset>
					    
					    <fieldset class="formEdicao">
					    <legend class="formEdicaoLegenda">DADOS DO PROCESSO</legend>                       						   					    
				    		<div class="col45">
								 <label class="formEdicaoLabel" for="situacaoAdvogadoProcesso">Situação do advogado no processo: </label><br>
							     	<select name="situacaoAdvogadoProcesso" id="situacaoAdvogadoProcesso" class="formEdicaoCombo">
							     			<option value="3" <%if(buscaProcessoDt.getSituacaoAdvogadoProcesso() != null && buscaProcessoDt.getSituacaoAdvogadoProcesso().equals("3")){%>selected="true"<%}%>>Ativo ou Inativo</option>
							        		<option value="1" <%if(buscaProcessoDt.getSituacaoAdvogadoProcesso() != null && buscaProcessoDt.getSituacaoAdvogadoProcesso().equals("1")){%>selected="true"<%}%>>Ativo</option>
							        		<option value="2" <%if(buscaProcessoDt.getSituacaoAdvogadoProcesso() != null && buscaProcessoDt.getSituacaoAdvogadoProcesso().equals("2")){%>selected="true"<%}%>>Inativo</option>
							    	</select>
							    
						     </div>
				    	<div class="clear"></div>
				    	</fieldset>
					    
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