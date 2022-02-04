<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	
      	<script language="javascript" type="text/javascript">
	      	function VerificarCampos() {
	     		with(document.Formulario) {
		     		if((tituloPagina.value).indexOf("Próprios") == -1){
						if(Serventia.value=="" || Serventia.value=="null"){
			     			if ( (ProcessoNumero.value=="") && (NomeParte.value=="") && (CpfCnpjParte.value=="")){
			     				mostrarMensagemErro("Verifique os campos", "Preencha no mínimo um dos Critérios para a Consulta (Número do Processo, Nome da Parte ou CPF/CNPJ da Parte ou a Serventia).");
					         	ProcessoNumero.focus();
					         	return false; 
				     		}
		     			} 
	     			}
		     	submit();
	      		}
	    	}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="TipoConsulta" name="TipoConsulta" type="hidden" value="<%=request.getAttribute("TipoConsulta")%>">
				<input id="QuantidadeRegistrosPagina" name="QuantidadeRegistrosPagina" type="hidden" value="<%=request.getAttribute("QuantidadeRegistrosPagina")%>" />
				<input id="tituloPagina" name="tituloPagina" type="hidden" value="<%=request.getAttribute("TituloPagina")%>" />
				<input id="proprios" name="proprios" type="hidden" value="<%=request.getAttribute("proprios")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos </legend>
                        <p>
					   	<small> <strong>Nova Numeração</strong>:  Digite o Número do Processo "ponto" (ou "traço") e o Dígito Ex. <b>148032-91</b>.2009.8.09.002, ou seja, <strong>148032.91</strong> ou <strong>148032-91</strong><br />
   						<strong>Antiga Numeração</strong>:  Digite somente o Número do Processo Ex. 039.2006.<b>248.174</b>-5, ou seja, <strong>248174</strong><br />
   						</small>
                        </p>
					    <label class="formEdicaoLabel" for="ProcessoNumero">Número do Processo </label><br> 
					    <input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="18" value="<%=buscaProcessoDt.getProcessoNumero()%>"  onkeypress="return DigitarNumeroProcesso(this, event)" onkeyup=" autoTab(this,60)"/><br>ou<br />
					    
					    <label class="formEdicaoLabel" for="NomeParte">Nome da Parte </label><br> 
					    <input class="formEdicaoInput" name="NomeParte" id="NomeParte"  type="text" size="70" maxlength="60" value="<%=buscaProcessoDt.getPromovente()%>" onkeyup=" autoTab(this,60)" title="Digite o nome do promovente ou promovido"/>
					    Pesquisar nome exato<input type="checkbox" name="cbPesquisarNomeExato" id="cbPesquisarNomeExato" onchange="av('pesquisarNomeExato', this.checked)" <%if(request.getParameter("pesquisarNomeExato") != null && request.getParameter("pesquisarNomeExato").equals("true")){%> checked <%} %>/>
					    <input type="hidden" id="pesquisarNomeExato" name="pesquisarNomeExato" <%if(request.getParameter("pesquisarNomeExato") != null){%> value="<%=request.getParameter("pesquisarNomeExato")%>"  <%} else { %> value="false" <%} %>/>
					    <br>ou<br />					       	    
					    <label class="formEdicaoLabel" for="CpfCnpjParte">CPF/CNPJ da Parte</label><br> 
					    <input class="formEdicaoInput" name="CpfCnpjParte" id="CpfCnpjParte"  type="text" size="30" maxlength="18" value="<%=buscaProcessoDt.getCpfCnpjParte()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do promovente ou promovido"/><br />
					    
		    <fieldset class="formEdicao"> <legend> filtros</legend>		
                        					    
					    <label class="formEdicaoLabel" for="Id_ProcessoStatus">Status do Processo 
                        <input type="hidden" name="Id_ProcessoStatus" id="Id_ProcessoStatus" value="">
					    <input type="hidden" name="ProcessoStatusCodigo" id="ProcessoStatusCodigo" value="<%=buscaProcessoDt.getProcessoStatusCodigo()%>"/>
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoStatus" name="imaLocalizarProcessoStatus" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    <input class="FormEdicaoimgLocalizar" name="imaLimparStatusCodigo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoStatus','ProcessoStatus');LimparChaveEstrangeira('ProcessoStatusCodigo','ProcessoStatus'); return false;" title="Limpar Status do Processo">
					    </label><br> 
						<input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoStatus" id="ProcessoStatus" type="text" size="67" maxlength="100" value="<%=buscaProcessoDt.getProcessoStatus()%>"/><br />
					    
					    <% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
						    <label class="formEdicaoLabel" for="Id_Classificador">Classificador
   							<input type="hidden" name="Id_Classificador" id="Id_Classificador" value="">							
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">
						    </label><br>  
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="67" maxlength="100" value="<%=buscaProcessoDt.getClassificador()%>"/><br />
					    <% } %>
					    
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="">
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">  
					    </label><br>              					    
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=buscaProcessoDt.getServentia()%>"/><br />

					    
					    <% if (request.getAttribute("permissaoConsultar500ProcessosPorPagina") != null && request.getAttribute("permissaoConsultar500ProcessosPorPagina").equals("true")){ %>
							<input type="checkbox" name="cbConsultar500ProcessoPorPagina" id="cbConsultar500ProcessoPorPagina" onchange="AjusteQuantidadeDeProcessoPorPagina(this);" <%if(request.getParameter("QuantidadeRegistrosPagina") != null && br.gov.go.tj.utils.Funcoes.StringToInt(request.getParameter("QuantidadeRegistrosPagina")) > 0){%> checked <%} %>/> Listar 500 Processos por Página			    
					    <% } %>
				</fieldset>					    					      	
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual', '<%=request.getAttribute("TipoConsulta")%>');return VerificarCampos();">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
			</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
		<script type="text/javascript">
			function AjusteQuantidadeDeProcessoPorPagina(componente) {
				if (componente.checked) {
					av('QuantidadeRegistrosPagina', 500);					
				} else {
					av('QuantidadeRegistrosPagina', '');					
				}				
			}
		</script>
	</body>
</html>