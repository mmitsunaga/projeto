<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioCejuscDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="UsuarioCejuscDt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioCejuscDt"/>
<jsp:useBean id="serventiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaDt"/>


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
  String[] nomeBusca = (String[])request.getAttribute("lisNomeBusca");
  String[] camposHidden = (String[])request.getAttribute("camposHidden");
  String tamColuna = String.valueOf(60/descricao.length) + "%";
  %>
</head>
<body>
  <% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  <%@ include file="/CabecalhoPublico.html" %>
  <% } %>
  <div id="divCorpo" class="divCorpo" >
  <div class="area"><h2>&raquo;|<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%></h2></div>
    <div id="divLocalizar" class="divLocalizar"> 
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
		
        <fieldset id="formLocalizar" class="formLocalizar"> 
          
          <legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de <%=request.getAttribute("tempBuscaPrograma")%></legend>
          <%for (int i=0;i<nomeBusca.length;i++) {%>          	                    
          	<label id="formLocalizarLabel<%=i%>" class="formLocalizarLabel"><%=nomeBusca[i]%></label><br> 
          	<input id="nomeBusca<%=i+1%>" class="formLocalizarInput" name="nomeBusca<%=i+1%>" type="text" value="" style="width : 700px;"/>
          	<br/>
		  <%}%>
		  <br/> 
		  
		  <div>
	    	<label class="formEdicaoLabel" for="statusAtualUsuario">Status</label> <br/>
	    	<select id="nomeBusca3" name="nomeBusca3" class="formEdicaoCombo">0
	    		<option value="" >Todos</option>
				<option value="<%= UsuarioCejuscDt.CODIGO_STATUS_AGUARDANDO_AVALIACAO %>" >Aguardando avaliação</option>						
				<option value="<%= UsuarioCejuscDt.CODIGO_STATUS_APROVADO %>" >Aprovado</option>
				<option value="<%= UsuarioCejuscDt.CODIGO_STATUS_PENDENTE %>" selected="selected">Pendente</option>
				<option value="<%= UsuarioCejuscDt.CODIGO_STATUS_REPROVADO %>" >Reprovado</option>
			</select>
	   	  </div>
	   	  <br/>
	   	  
		<!-- Serventia -->
		<label class="formEdicaoLabel" for="nomeBusca4">*Serventia
	    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
		</label>
		<br/>				    
	    <input type="hidden" name="nomeBusca4" id="nomeBusca4" value="<%=serventiaDt.getId()%>">
	    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="70" maxlength="100" value="<%=serventiaDt.getServentia()%>"/>
	    <br/>

		<br/>
 		<div>
	    	<label class="formEdicaoLabel" for="nomeBusca5">Apenas voluntários: </label> <br/>
	    	<select id="nomeBusca5" name="nomeBusca5" class="formEdicaoCombo">
	    		<option value="nao" >Não</option>
				<option value="sim" >Sim</option>
			</select>
	   	</div>



		  <br/>
		  <br/>
          <div class="Centralizado">
          	<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosJSON('<%=stTempRetorno%>', false,  <%=request.getAttribute("PaginaAtual")%>, '<%=nomeBusca.length+3%>' , '0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/>
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
              </tr>
            </thead>
          	<tbody id="CorpoTabela">&nbsp;</tbody>
          </table>
        </div> 
      </form> 
    </div> 
    <div id="Paginacao" class="Paginacao"></div></div> 
    <%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>