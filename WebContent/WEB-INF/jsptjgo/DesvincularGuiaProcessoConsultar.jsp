<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>TJGO/Projudi - Formulário de Consultar Guia para Desvincular de Processo</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
	  	
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Consultar Guia para Desvincular de Processo</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="DesvincularGuiaProcesso" id="DesvincularGuiaProcesso">
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			
			<input id="numeroGuiaVincularConfirmar" name="numeroGuiaVincularConfirmar" type="hidden" value="<%=request.getAttribute("numeroGuiaVincularConfirmar")%>">
			<input id="numeroProcessoVincularConfirmar" name="numeroProcessoVincularConfirmar" type="hidden" value="<%=request.getAttribute("numeroProcessoVincularConfirmar")%>">
			<input id="motivoVincularConfirmar" name="motivoVincularConfirmar" type="hidden" value="<%=request.getAttribute("motivoVincular")%>">
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	
                	<legend>Informe o Número da Guia para Desvincular de Processo</legend>
                	
                	<div>Número da Guia</div>
                	<span>
                		<input type="text" name="numeroGuia" id="numeroGuia" value="" maxlength="15" onkeyup="autoTab(this,11)" onkeypress="return DigitarSoNumero(this, event)" />
                	</span>
                	
                	<div>Motivo da Desvinculação</div>
                	<span>
                		<input type="text" name="motivo" id="motivo" value="" maxlength="80" onkeyup="autoTab(this,80)" />
                	</span>
                	
                </fieldset>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<button name="imgPreviaCalculo" value="Consultar Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');" >
	                   	Consultar
	                </button>
                	<button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	Limpar
                    </button>
                </div>
			</div>
			
			<br/><br/>
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	
                	<legend>Informe o Número da Guia e Processo para Realizar a Vinculação</legend>
                	
                	<div>Número da Guia para Vincular</div>
                	<span>
                		<input type="text" name="numeroGuiaVincular" id="numeroGuiaVincular" value="" maxlength="15" onkeyup="autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)" />
                	</span>
                	
                	<div>Número do Processo que Receberá a Guia</div>
                	<span>
                		<input type="text" name="numeroProcessoVincular" id="numeroProcessoVincular" value="" maxlength="25" onkeyup="autoTab(this,25)" onkeypress="return DigitarSoNumero(this, event)" />
                	</span>
                	
                	<br />
                	<br />
                	
                	<div>Motivo da Vinculação</div>
                	<span>
                		<input type="text" name="motivoVincular" id="motivoVincular" value="" maxlength="80" onkeyup="autoTab(this,80)" />
                	</span>
                	
                </fieldset>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<button name="imgPreviaCalculo" value="Vincular Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga7%>');" >
	                   	Vincular Guia
	                </button>
                	<button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	Limpar
                    </button>
                </div>
			</div>
			
			<br /><br />
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDados" class="VisualizaDados">
                	
                	<legend>Informe o Novo Status da Guia</legend>
                	
                	<div>Número da Guia para Alterar o Status</div>
                	<span>
                		<input type="text" name="numeroGuiaAlterarStatus" id="numeroGuiaAlterarStatus" value="" maxlength="15" onkeyup="autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)" />
                	</span>
                	
                	<div>Novo Status</div>
                	<span>
                		<select id="novoStatusGuia" name="novoStatusGuia">
                			<option value="">Selecione uma opção</option>
                			<option value=""></option>
                			<option value="<%=GuiaStatusDt.CANCELADA%>">CANCELADA</option>
                			<option value=""></option>
                			<option value="<%=GuiaStatusDt.BAIXADA_COM_ASSISTENCIA%>">BAIXADA COM ASSISTÊNCIA</option>
                			<option value="<%=GuiaStatusDt.BAIXADA_COM_ISENCAO%>">BAIXADA COM ISENÇÃO</option>
                		</select>
                	</span>
                	
                	<br />
                	<br />
                	
                	<div>Motivo da Alteração</div>
                	<span>
                		<input type="text" name="motivoAlteracao" id="motivoAlteracao" value="" maxlength="80" onkeyup="autoTab(this,80)" />
                	</span>
                	
                </fieldset>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<button name="imgPreviaCalculo" value="Alterar Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga8%>');" >
	                   	Alterar Guia
	                </button>
                	<button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	Limpar
                    </button>
                </div>
			</div>
			
			<br /><br />
			
			<div id="divEditar" class="divEditar">
                <fieldset id="VisualizaDadosComarca" class="VisualizaDados">
                	
                	<legend>Informe a Comarca e Área de Distribuição</legend>
                	
                	<div>Número da Guia</div>
                	<span>
                		<input type="text" name="numeroGuiaAlterarComarca" id="numeroGuiaAlterarComarca" maxlength="15" onkeyup="autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)" />
                	</span>
                	
                	<br />
                	<br />
                	
                	<div>Comarca</div>
                	<span>
                		<input type='image' id='imaLocalizarComarca' name='imaLocalizarComarca' class='FormEdicaoimgLocalizar' src='./imagens/imgLocalizarPequena.png' onclick='MostrarBuscaPadrao("VisualizaDadosComarca", "Comarca", "Consulta de Comarcas", "Digite a Comarca e clique em consultar.", "Id_Comarca", "Comarca", ["Comarca"], [], "<%=(Configuracao.Localizar)%>", "<%=Configuracao.TamanhoRetornoConsulta%>"); return false;')' />
                		<input type='text' id='Comarca' name='Comarca' class='formEdicaoInputSomenteLeitura' size='51' maxlength='100' value='' readonly='readonly'/>
                		<input type='hidden' id='Id_Comarca' name='Id_Comarca' value='' />
                	</span>
                	
                	<br />
                	<br />
                	
                	<div>Área de Distribuição</div>
                	<span>
                		<input type='image' id='imaLocalizarArea' name='imaLocalizarArea' class='FormEdicaoimgLocalizar' src='./imagens/imgLocalizarPequena.png' onclick='MostrarBuscaPadrao("VisualizaDadosComarca", "AreaDistribuicao", "Consulta de Área de Distribuição", "Digite a Área de Distribuição e clique em consultar.", "Id_AreaDistribuicao", "AreaDistribuicao", ["AreaDistribuicao"], [], "<%=(Configuracao.Localizar)%>", "<%=Configuracao.TamanhoRetornoConsulta%>"); return false;')' />
                		<input type='text' id='AreaDistribuicao' name='AreaDistribuicao' class='formEdicaoInputSomenteLeitura' size='51' maxlength='100' value='' readonly='readonly'/>
                		<input type='hidden' id='Id_AreaDistribuicao' name='Id_AreaDistribuicao' value='' />
                	</span>
                	
                	<br />
                	<br />
                	
                	<div>Motivo da Alteração</div>
                	<span>
                		<input type="text" name="motivoAlteracaoComarca" id="motivoAlteracaoComarca" value="" maxlength="80" onkeyup="autoTab(this,80)" />
                	</span>
                	
                </fieldset>
                
                <div id="divBotoesCentralizados" class="divBotoesCentralizados">
                	<button name="imgPreviaCalculo" value="Alterar Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('PassoEditar','<%=Configuracao.Curinga9%>');" >
	                   	Alterar Guia
	                </button>
                	<button name="imgLimpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" >
                    	Limpar
                    </button>
                </div>
			</div>
			
			<br /><br />
		
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			
	  	</form>   
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>