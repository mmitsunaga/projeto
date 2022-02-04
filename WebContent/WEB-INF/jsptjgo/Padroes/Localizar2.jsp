<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.Dados"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%>  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   	
	<script type='text/javascript'>		
		var _PaginaEditar = <%=Configuracao.Editar%>;
		var _PaginaExcluir = <%=Configuracao.Excluir%>;
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>	
	
	<%		
		boolean boMostrarExcluir = false;
		String stTempRetorno = (String)request.getAttribute("tempRetorno");
		String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
		//vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
		if(stTempRetorno!=null && stTempBuscaPrograma!=null){
			if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarExcluir=true;
		}
	%>
</head>

<body>
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  	<%@ include file="/CabecalhoPublico.html" %>
  	<% } %>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%>  </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<fieldset id="formLocalizar" class="formLocalizar"  > 
			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de <%=request.getAttribute("tempBuscaPrograma")%></legend>
			<label  id="formLocalizarLabel"   class="formLocalizarLabel"><%=request.getAttribute("tempDescricaoDescricao1")%></label><br> 
			<input  id="nomeBusca"   class="formLocalizarInput" name="nomeBusca" type="text" value=""  /> </br>
			<label  id="formLocalizarLabel"   class="formLocalizarLabel"><%=request.getAttribute("tempDescricaoDescricao2")%></label><br> 
			<input  id="nomeBusca1"   class="formLocalizarInput" name="nomeBusca1" type="text" value="" /> 
			<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosPadrao2(  '<%=stTempRetorno%>' ,  '0' , <%=Configuracao.TamanhoRetornoConsulta%> , <%=boMostrarExcluir%>,  <%=Configuracao.LocalizarDWR%> ); return false;" />
			
		</fieldset>
		<input type="hidden" id="tempBuscaId" name="<%=request.getAttribute("tempBuscaId").toString()%>" />
		<input type="hidden" id="tempBuscaDescricao" name="<%=request.getAttribute("tempBuscaDescricao").toString()%>" />
		<input type="hidden" id="PaginaAtual" name="PaginaAtual"  value="<%=request.getAttribute("PaginaAtual")%>" />
		
		<br />
		<div id="divTabela" class="divTabela" > 
			<table id="tabelaLocalizar" class="Tabela">
				<thead>
					<tr>
						<th width='15px' align="center"></th>
						<th width='15px' align="center"><%=request.getAttribute("tempDescricaoId")%></th>
						<th width='25%'><%=request.getAttribute("tempDescricaoDescricao1")%></th>
						<th width='25%'><%=request.getAttribute("tempDescricaoDescricao2")%></th>
						<th class="colunaMinima" title="Seleciona o registro para edição">Editar</th>
						<%if (boMostrarExcluir) {%>
							<th class="colunaMinima" title="Seleciona o registro para exclusão"> Excluir </th> 
						<%}%> 
					</tr>
					</thead>
				<tbody id="CorpoTabela">
						&nbsp;
				</tbody>
			</table>
		</div> 
		</form> 
	</div> 
		
	<div id="Paginacao" class="Paginacao"> </div>
</div> 
<%@ include file="Mensagens.jspf"%>
</body>
</html>
