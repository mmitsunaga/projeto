<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PonteiroLogDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PonteiroLogTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<jsp:useBean id="PonteiroLogdt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroLogDt"/>

<html>
<head>
	
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Log do Ponteiro de Distribuição </title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
	</style>
		
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
 	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumeroENegativo.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type="text/javascript" src='./js/jscalendar/dhtmlgoodies_calendar.js'></script>
 	<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
 	<script type='text/javascript' src='./js/Digitacao/EvitarEspacoDuplo.js' ></script>
	
</head>

	<script language="javascript" type="text/javascript">
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>		
		function VerificarCampos() {
			if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
				with(document.Formulario) {
					if (SeNulo(PonteiroLogTipo, "O Campo Ponteiro Log Tipo é obrigatório!")){
						return false;
					}					
					if (SeNulo(AreaDistribuicao, "O Campo Área Distribuição é obrigatório!")){
						return false;
					}					
					if (SeNulo(Serventia, "O Campo Serventia é obrigatório!")){
						return false;
					}					
					if (SeNulo(UsuarioServentia, "O Campo Usuário Responsável é obrigatório!")){
						return false;
					}					
					if (SeNulo(Justificativa, "O Campo Justificativa é obrigatório!")){
						return false;
					}					
					if (SeNulo(Qtd, "O Campo Quantidade é obrigatório!")){
						return false;
					}
					submit();				
				}				
			}
		}			
	<%}%>
	</script>

<body>

	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Log do Ponteiro de Distribuição</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PonteiroLog" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PonteiroLog" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<a class="divPortaBotoesLink" href="Ajuda/PonteiroLogAjuda.html" target="_blank">
					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" />
				</a>
			</div>
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  >
				 
					<legend class="formEdicaoLegenda">Cadastro de Log do Ponteiro de Distribuição</legend>
					
					<label class="formEdicaoLabel" for="Id_PonteiroLog">Identificador</label>
					<input class="formEdicaoInputSomenteLeitura" name="Id_PonteiroLog" id="Id_PonteiroLog" type="text" size="10" readonly="true" value="<%=PonteiroLogdt.getId()%>"><br/>

					<label class="formEdicaoLabel" for="Id_PonteiroLogTipo">Ponteiro Log Tipo*</label>
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarPonteirologtipo" name="imaLocalizarPonteirologtipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(PonteiroLogTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparPonteirologtipo" name="imaLimparPonteirologtipo" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_PonteiroLogTipo','PonteiroLogTipo'); return false;" >
					<input name='Id_PonteiroLogTipo' id='Id_PonteiroLogTipo' type='hidden' value='<%=PonteiroLogdt.getId_PonteiroLogTipo()%>'>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="PonteiroLogTipo" id="PonteiroLogTipo" type="text" size="100" maxlength="60" value="<%=PonteiroLogdt.getPonteiroLogTipo()%>"><br/>
					
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">Área Distribuição*</label>					
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreadistribuicao" name="imaLocalizarAreadistribuicao" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparAreadistribuicao" name="imaLimparAreadistribuicao" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_AreaDistribuicao','AreaDistribuicao'); return false;" >
					<input name='Id_AreaDistribuicao' id='Id_AreaDistribuicao' type='hidden'  value='<%=PonteiroLogdt.getId_AreaDistribuicao()%>'>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="100" maxlength="100" value="<%=PonteiroLogdt.getAreaDistribuicao()%>"><br/>					
					
					<label class="formEdicaoLabel" for="Id_Serventia">Serventia*</label>					
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" >
					<input name='Id_Serventia' id='Id_Serventia' type='hidden'  value='<%=PonteiroLogdt.getId_Serv()%>'>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Serventia" id="Serventia" type="text" size="100" maxlength="100" value="<%=PonteiroLogdt.getServ()%>"><br/>	
		
					<label class="formEdicaoLabel" for="Id_Processo">Processo</label>
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcesso" name="imaLocalizarProcesso" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparProcesso" name="imaLimparProcesso" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Processo','Processo'); return false;" >
					<input name='Id_Processo' id='Id_Processo' type='hidden' value='<%=PonteiroLogdt.getId_Proc()%>'>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="Processo" id="Processo" type="text" size="100" maxlength="122" value="<%=PonteiroLogdt.getProcNumero()%>"><br/>
					
					<label class="formEdicaoLabel" for="Id_ServentiaCargo">Serventia Cargo</label>
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargo" name="imaLimparServentiaCargo" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" >
					<input name='Id_ServentiaCargo' id='Id_ServentiaCargo' type='hidden' value='<%=PonteiroLogdt.getId_ServentiaCargo()%>'>
					<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="100" maxlength="100" 
					<%if(PonteiroLogdt.getServentiaCargo() != null && !PonteiroLogdt.getServentiaCargo().equals("")) { %>
						value="<%=PonteiroLogdt.getServentiaCargo()%> - <%=PonteiroLogdt.getServentiaCargoUsuario()%>"
					<%} %>
					/>
				    <br />
				    
				   <label class="formEdicaoLabel" for="Responsavel">Usuário Responsável*</label>
					<input class="formEdicaoInputSomenteLeitura" name="Responsavel" id="Responsavel" type="text" size="100" readonly="true" value="<%=PonteiroLogdt.getNome()%>">
					<br/> 
					
					<label class="formEdicaoLabel" for="Data">Data de Registro*</label>
					<input class="formEdicaoInputSomenteLeitura" name="Data" id="Data" type="text" size="19" maxlength="19" readonly="true" value="<%=PonteiroLogdt.getData()%>">
					<br/>
						
					<label class="formEdicaoLabel" for="Justificativa">Justificativa*</label>
					<input class='<%=request.getAttribute("classe")%>' name="Justificativa" id="Justificativa" type="text" size="100" maxlength="4000" <%=request.getAttribute("readonly")%> value="<%=PonteiroLogdt.getJustificativa()%>" onkeyup="autoTab(this,4000)"><br/>
					
					<label class="formEdicaoLabel" for="Qtd">Quantidade*</label>
					<input class='<%=request.getAttribute("classe")%>' name="Qtd" id="Qtd" type="text" size="7" maxlength="6" <%=request.getAttribute("readonly")%> value="<%=PonteiroLogdt.getQtd()%>" onkeypress="return DigitarSoNumeroENegativo(this, event)" onkeyup="autoTab(this,22)"><br/>
					
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>