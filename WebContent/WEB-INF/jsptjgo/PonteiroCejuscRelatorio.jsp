<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PonteiroCejuscDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="PonteiroCejuscdt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroCejuscDt"/>

<%@page import="br.gov.go.tj.projudi.dt.UsuarioCejuscDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PonteiroCejuscDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Relatório  </title>
	<style type="text/css">
 		@import url('./css/Principal.css');
 		@import url('./css/Paginacao.css');
 		@import url('js/jscalendar/dhtmlgoodies_calendar.css');
	</style>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
   	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(nomeBusca1, "O Campo Data Inicial é obrigatório!")) return false;
				if (SeNulo(nomeBusca1, "O Campo Data Final é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
	
	 <%boolean boMostrarExcluir = false;
	  String stTempRetorno = (String)request.getAttribute("tempRetorno");
	  String tempFluxo1 = (String)request.getAttribute("tempFluxo1");
	  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
	  int posicaoPaginaAtual = (int)request.getAttribute("PosicaoPaginaAtual");
	  //vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
	  if(stTempRetorno!=null && stTempBuscaPrograma!=null){
	    if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarExcluir=true;
	  }
	  String[] descricao = (String[])request.getAttribute("lisDescricao");
	  String[] nomeBusca = (String[])request.getAttribute("lisNomeBusca");
	  String[] camposHidden = (String[])request.getAttribute("camposHidden");
	  String tamColuna = String.valueOf(60/descricao.length) + "%";
	  %>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
			
			<div class="area"><h2>&raquo;Relatório de Audências</h2></div>
		
			<fieldset>
			
				<form action="PonteiroCejusc?fluxo=3&PaginaAtual=<%=Configuracao.Curinga9%>" method="post">
					
					<div class="col15">
					<label>Data inicial</label><br>
					<input id="nomeBusca1" name="dataInicialConsulta" size="10" maxlength="10" title="Clique para escolher uma data inicial."  value="" onkeyup="mascara_data(this);checarFoco()" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calDataConsulta" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].nomeBusca1,'dd/mm/yyyy',this)" />
					</div>
					
					<div class="col15">
					<label>Data final</label><br>
					<input id="nomeBusca2" name="dataFinalConsulta" size="10" maxlength="10" title="Clique para escolher uma data final."  value="" onkeyup="mascara_data(this);checarFoco()" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calDataConsulta" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].nomeBusca2,'dd/mm/yyyy',this)" />
					</div>
					
					<br/>
					
					<div class="divPortaBotoes center">
<!-- 					<input id="btnConsultar" type="submit" value="Consultar" /> -->
			          	<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosJSON('<%=stTempRetorno%>', false,  '9', '<%=nomeBusca.length%>' , <%=posicaoPaginaAtual%>, <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/>
<%-- 			          	<input id="formGerarRelatorioBotao" class="formGerarRelatorioBotao" type="submit" name="GerarRelatorio" value="Gerar Relatório" onclick="javascript:buscaDadosJSON('<%=stTempRetorno%>', false,  '9', '<%=nomeBusca.length%>' , <%=posicaoPaginaAtual%>, <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/> --%>
			          	<input id="formGerarRelatorioBotao" class="formGerarRelatorioBotao" type="submit" name="GerarRelatorio" value="Gerar Relatório" />
					</div>

					<div id="divTabela" class="divTabela"> 
			          <table id="tabelaLocalizar" class="Tabela">
			            <thead>
			              <tr>
			                <th width='20px' align="center"></th>
			                <th width='40px' align="center">Id</th>                
			                <%for (int i=0;i<descricao.length;i++) {%>				
							<th width=tamColuna><%=descricao[i]%></th><%}%> 
							 <th class="colunaMinima" >Ação</th>
			              </tr>
			            </thead>
			          	<tbody id="CorpoTabela">&nbsp;</tbody>
			          </table>
			        </div>
			        <div id="Paginacao" class="Paginacao"></div>
					
					
					<input type=hidden id="tempFluxo1" value="<%=tempFluxo1%>"/>
				</form>
				
			</fieldset>

	</div>	
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
		
</html>
