<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="mandadoJudicialDt" scope="session" class="br.gov.go.tj.projudi.dt.MandadoJudicialDt"/>
<jsp:useBean id="pendenciaDt" 		scope="session" class="br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="processoParteDt" 	scope="session" class="br.gov.go.tj.projudi.dt.ProcessoParteDt"/>
<jsp:useBean id="processoDt" 		scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Distribuir  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script language="javascript" type="text/javascript">
	function VerificarCampos(Formulario) {
		if(!validarRadioButtons(Formulario.assistenciaRadio,"Informe se este mandado tem assistência!")) {
			return false;
		}
		if(!validarRadioButtons(Formulario.oficialCompanheiro,"Informe se este mandado necessita de oficial companheiro!")) {
			return false;
		}
		if(SeNulo(Formulario.idMandadoTipo, "Informe o tipo de mandado!")) {
			return false;
		}	
		if(SeNulo(Formulario.prazoMandadoJudicial, "Informe o prazo de cumprimento deste mandado judicial!")) {
			return false;
		}
		return true;
	}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Distribuição de Mandado Judicial</h2></div>
		
		<form id="Formulario" name="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
		
			<input type="hidden" id="tempBuscaId_UsuarioServentiaEscala" name="tempBuscaId_UsuarioServentiaEscala" value="<%=request.getParameter("tempBuscaId_UsuarioServentiaEscala")%>" />
			<input type="hidden" id="tempBuscaUsuarioServentiaEscala" name="tempBuscaUsuarioServentiaEscala" value="<%=request.getParameter("tempBuscaUsuarioServentiaEscala")%>" />
			
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend class="formEdicaoLegenda">Dados do Mandado Judicial</legend>
					<br/>
					
					<div>Número Processo&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getProcessoNumero()%></span>
					<br />
					
					<div>Natureza&nbsp;&nbsp;</div>
					<span class="span1"><%=processoDt.getProcessoTipo().toUpperCase()%></span>
					<br/>
					
					<div>Pendência Tipo&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getPendenciaTipo().toUpperCase()%></span>
					<br />
					
					<div>Requerido&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getNomeParte()%></span>
					<br/>
					
					<div>Endereço&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getLogradouro()%></span>
					<br/>
					
					<div>Complemento&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getComplemento()%></span>
					<br/>
					
					<div>Bairro&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getBairro()%></span>
					<br/>
					
					<div>CEP&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getCep()%></span>
					<br/>
					
					<div>Localidade&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getCidade()%></span>
					<br/>
					
					<div>UF&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getUf()%></span>
					<br/>
					
					<div>CPF/CGC&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getCpfCnpj()%></span>
					<br/>
				</fieldset>
			</div>
			<br/>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend id="formLocalizarLegenda" class="formEdicaoLegenda">Distribuir</legend>
					
					<label id="assistenciaLabel" for="assistenciaRadio" class="formEdicaoLabel">*Tem assistência?</label><br>
					<input type="radio" id="assistenciaRadio" name="assistenciaRadio" class="formEdicaoInput" value="<%=MandadoJudicialDt.SIM_ASSISTENCIA%>" />Sim
			       	<input type="radio" id="assistenciaRadio" name="assistenciaRadio" class="formEdicaoInput" value="<%=MandadoJudicialDt.NAO_ASSISTENCIA%>" />Não
					
					<br/>
					
					<label id="oficialCompanheiroLabel" for="oficialCompanheiro" class="formEdicaoLabel">*Necessita Oficial Companheiro?</label><br>
					<input type="radio" id="oficialCompanheiro" name="oficialCompanheiro" class="formEdicaoInput" value="<%=MandadoJudicialDt.SIM_OFICIAL_COMPANHEIRO%>" />Sim
			       	<input type="radio" id="oficialCompanheiro" name="oficialCompanheiro" class="formEdicaoInput" value="<%=MandadoJudicialDt.NAO_OFICIAL_COMPANHEIRO%>" checked />Não
			       	
			       	<br/>
			       	
			       	<label class="formEdicaoLabel" for="Id_UsuarioServentiaEscala">*Oficial Companheiro?
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioServentiaEscala" name="imaLocalizarUsuarioServentiaEscala" type="image" src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoEscalaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="true" name="UsuarioServentiaEscala" id="UsuarioServentiaEscala" type="text" size="20" maxlength="20" value="<%=((request.getParameter("tempBuscaUsuarioServentiaEscala") == null)?"":request.getParameter("tempBuscaUsuarioServentiaEscala"))%>"/>
					
					<br />
			       	
		       		<label id="tipoMandadoLabel" for="tipoMandadoLabel" class="formEdicaoLabel">*Tipo de Mandado</label><br>
			       	<select id="idMandadoTipo" name="idMandadoTipo" class="formEdicaoInput">
			       		<option value=""></option>
				       	<%
				       	List liTemp = (List)request.getAttribute("listaTiposMandados");
				       	for(int i = 0; i < liTemp.size(); i++ ) {
				       		MandadoTipoDt mandadoTipoDt = (MandadoTipoDt)liTemp.get(i);
				       		%>
				       		<option value="<%=mandadoTipoDt.getId()%>"><%=mandadoTipoDt.getMandadoTipo()%></option>
				       		<%
				       	}
				       	%>
			       	</select>
			       	
			       	<br/>
			       	
		       		<label id="prazoMandadoJudicialLabel" for="prazoMandadoJudicial" class="formEdicaoLabel">*Prazo <i>(dias)</i></label><br>
			       	<input type="text" name="prazoMandadoJudicial" id="prazoMandadoJudicial" class="formEdicaoInput" value="<%=request.getAttribute("prazoMandadoJudicial")%>" onkeypress="return DigitarSoNumero(this, event)" size="10" maxlength="3" />
			       	
			       	<br/>
			       	
			       	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
			       		<button type="submit" name="operacao" value="Distribuir" title="Distribuir Mandado Judicial" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');">
							<!-- <img src="imagens/22x22/btn_encaminhar.png" alt="Distribuir" /> -->
							Distribuir
						</button>
						
						<button type="reset" name="operacao" value="Limpar" title="Limpar">
							<img src='imagens/22x22/edit-clear.png' alt="Limpar" />
							Limpar
						</button>
						
						<button type="button" name="operacao" value="Cancelar" title="Cancelar Distribuição de Mandado Judicial" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>');FormSubmit('Formulario');">
							<!--  <img src="imagens/22x22/btn_nao_expedir.png" alt="Cancelar" /> -->
							Cancelar
						</button>
					</div>
				</fieldset>
			</div>
			
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			
		</form>
	</div>
</body>
</html>