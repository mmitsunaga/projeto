<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.Iterator"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<html>
<head>
  <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
  <title> |<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%>  </title>
  <link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  <link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
  <script type='text/javascript' src='./js/jquery.js'></script>
  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
  <script type='text/javascript' src='./js/Funcoes.js'></script>	
  <script type='text/javascript'>		
    var _PaginaEditar = <%=request.getAttribute("tempPaginaAtualJSON")%>;
    var _PaginaExcluir = <%=Configuracao.Excluir%>;
    var _Acao = "Editar";
  </script>
  <%boolean boMostrarExcluir = false;
  String stTempRetorno = (String)request.getAttribute("tempRetorno");
  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
  //vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
  if(stTempRetorno!=null && stTempBuscaPrograma!=null){
    if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarExcluir=true;
  }
  String[] descricao = (String[])request.getAttribute("lisDescricao");
  
  String[] nomeBusca = new String[0];
  if(null != request.getAttribute("lisNomeBusca")){
	  nomeBusca = (String[])request.getAttribute("lisNomeBusca");  
  }

  String[] camposHidden = (String[])request.getAttribute("camposHidden");
  String tamColuna = String.valueOf(60/descricao.length) + "%";

  String[][] localizarBusca = new String[0][0];
if(null != request.getAttribute("lislocalizarBusca")){
	  localizarBusca = (String[][])request.getAttribute("lislocalizarBusca");	  
  }

String[][] localizarDropdown = new String[0][0];
if(null != request.getAttribute("lislocalizarDropdown")){
	  localizarDropdown = (String[][])request.getAttribute("lislocalizarDropdown");	  
  }

  %>
</head>
<body>
  <% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  <%@ include file="/CabecalhoPublico.html" %>
  <% } %>
  <div id="divCorpo" class="divCorpo" >
  <div class="area"><h2>&raquo;|<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%></h2></div>
<!--     <div id="divLocalizar" class="divLocalizar"> -->
    <div id="divEditar" class="divEditar"> 
      <form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
        <input type="hidden" id="tempBuscaId" name="<%=request.getAttribute("tempBuscaId").toString()%>"/>
        <input type="hidden" id="tempBuscaDescricao" name="<%=request.getAttribute("tempBuscaDescricao").toString()%>"/>
        <input type="hidden" id="PaginaAtual" name="PaginaAtual"  value="<%=request.getAttribute("PaginaAtual")%>"/>
        <input type="hidden" id="PaginaAnterior" name="PaginaAnterior"  value="<%=request.getAttribute("PaginaAnterior")%>"/>        				
		<input type="hidden" id="tempFluxo1" name="tempFluxo1"  value="<%=request.getAttribute("tempFluxo1")%>"/>
		<input type="hidden" id="tempFluxo2" name="tempFluxo2"  value="<%=request.getAttribute("tempFluxo2")%>"/>
		<input type="hidden" id="PassoEditar" name="PassoEditar"  value="<%=request.getAttribute("PassoEditar")%>"/>
		<input type="hidden" id="ParteTipo" name="ParteTipo"  value="<%=request.getAttribute("ParteTipo")%>"/>
		
		<%if (camposHidden!=null)
			for (int i=0;i<camposHidden.length;i++) {%>
				<input type="hidden" id="desc<%=i+2%>" name="<%=camposHidden[i]%>"  value=""/>
			<%}%>			
		
<!-- 		<fieldset id="formEdicao" class="formEdicao">  -->
        <fieldset id="formLocalizar" class="formEdicao"> 
          
          <legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de <%=request.getAttribute("tempBuscaPrograma")%></legend>
          <%for (int i=0;i<nomeBusca.length;i++) {%>          	                    
          	<label id="formLocalizarLabel<%=i%>" class="formLocalizarLabel"><%=nomeBusca[i]%></label><br> 
          	<input id="nomeBusca<%=i+1%>" class="formLocalizarInput" name="nomeBusca<%=i+1%>" type="text" value="" style="width : 700px;"/>
          <br/>
		  <%}%>
		  
<!-- 		  campos com lupa -->
		  <br/> 
		  <%for (int i=0;i<localizarBusca.length;i++) {%>
		<label class="formEdicaoLabel"><%=localizarBusca[i][0]%><!--[0]nome da busca-->
	    <input type="hidden" name="nomeBusca<%=i+1+nomeBusca.length%>" id="nomeBusca<%=i+1+nomeBusca.length%>" value="<%=localizarBusca[i][4]%>"><!--ID-->
		<input type="hidden" name="<%=localizarBusca[i][2]%>" id="<%=localizarBusca[i][2]%>" value="<%=localizarBusca[i][3]%>"><!--[2]nome da descricao [3]valor da descricao-->
	    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia<%=i+1+nomeBusca.length%>" name="imaLocalizarServentia<%=i+1+nomeBusca.length%>" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=((Funcoes.StringToInt(localizarBusca[i][1]))* Configuracao.QtdPermissao + Configuracao.Localizar)%>')" ><!--código redirecionar-->
<%-- 	    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia<%=i+1+nomeBusca.length%>" name="imaLocalizarServentia<%=i+1+nomeBusca.length%>" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=localizarBusca[i][1]%>')" ><!--código redirecionar--> --%>
		<input class="FormEdicaoimgLocalizar" name="imaLimparServentia<%=i+1+nomeBusca.length%>" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('nomeBusca<%=i+1+nomeBusca.length%>','nomeBuscaFake<%=i+1+nomeBusca.length%>'); return false;" title="Limpar <%=localizarBusca[i][0]%>"><!--nome busca-->  
	    </label><br>
	    <input class="formEdicaoInputSomenteLeitura"  readonly name="nomeBuscaFake<%=i+1+nomeBusca.length%>" id="nomeBuscaFake<%=i+1+nomeBusca.length%>" type="text" size="80" maxlength="100" style="width : 700px;" value="<%=localizarBusca[i][3]%>"/><!--[3]valor da descricao-->
	    <br /><br />
		  <%}%>
<!-- 	FIM	  campos com lupa -->

<!-- 		  campos dropdown -->
		  <%for (int i=0;i<localizarDropdown.length;i++) {%>
		<label for="nomeBusca<%=i+1+nomeBusca.length+localizarBusca.length%>"><%=localizarDropdown[i][0]%></label><br>  
		<select id="nomeBusca<%=i+1+nomeBusca.length+localizarBusca.length%>" name="nomeBusca<%=i+1+nomeBusca.length+localizarBusca.length%>" class="formEdicaoCombo">
							<%for (int j=1;j<localizarDropdown[i].length;j+=2) {%>
							<option value="<%=localizarDropdown[i][j+1]%>"><%=localizarDropdown[i][j]%></option>
							<%}%>
		</select>
	    <br /><br />
		  <%}%>
<!-- 	FIM	  campos dropdown -->

		  <br/> 
		  <br />
		  <br />
          <div class="Centralizado">
          	<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="
          	javascript:buscaDadosJSON('<%=stTempRetorno%>', <%=boMostrarExcluir%>,  <%=request.getAttribute("PaginaAtual")%>, '<%=nomeBusca.length+localizarBusca.length+localizarDropdown.length%>' , '0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;
          	"/>
          </div>
        </fieldset>
        <br/>
        <div id="divTabela" class="divTabela"> 
          <table id="tabelaLocalizar" class="Tabela">
            <thead>
              <tr>
                <th width='20px' align="center"></th>
                <th width='40px' align="center">Id</th>                
                <%for (int i=0;i<descricao.length;i++) {%>				
				<th width=tamColuna><%=descricao[i]%></th><%}%> 
                <th class="colunaMinima" title="Seleciona o registro para edição">Selecionar</th>
                <%if (boMostrarExcluir) {%>
                <th class="colunaMinima" title="Seleciona o registro para exclusão">Excluir</th><%}%> 
              </tr>
            </thead>
          	<tbody id="CorpoTabela">&nbsp;</tbody>
          </table>
        </div> 
      </form> 
    </div> 
    <div id="Paginacao" class="Paginacao"></div></div> 
    <%@ include file="Mensagens.jspf"%>
</body>
</html>