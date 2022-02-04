<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CargoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<title>Logon Serventia Grupo</title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
        <style type="text/css"> #bkg_projudi{ display:none } </style>
        <script type="text/javascript" src="./js/jquery.js"> </script>
        <script type="text/javascript" src="./js/projudi.js"></script>       
        
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>		
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type='text/javascript' src='./js/checks.js'></script>  
	</head>
	
	<body class="fundo">
		
		 	    
			<%@ include file="/Cabecalho.html" %>
        	<div id="divCorpo" class="divCorpo" >
            <div class="area"><h2>&raquo; Serventias Disponíveis</h2></div>
                
                    
            
                        <%
                        String id_Serventia="";
                        UsuarioDt dt;
                        //Pega lista de todas as Serventias e Grupos
                        List liTemp = (List)request.getAttribute("listaServentiasGrupos");
                        if (liTemp.size() > 0){
                            for(int i = 0 ; i< liTemp.size();i++) {
                                dt = (UsuarioDt)liTemp.get(i);
            
                                
                                //Se serventia atual é diferente da anterior abre fieldset e fecha anterior	
                                if (!id_Serventia.equals(dt.getId_Serventia())) {
                                    if (!id_Serventia.equals("")){ %> </fieldset> <%}	
                                    id_Serventia = dt.getId_Serventia();							
                        %>
                                <fieldset class="fieldEdicaoEscuro"> 
                                    <legend class="formEdicaoLegenda"> <%= dt.getServentia() + " - " + dt.getEstadoRepresentacao() %> </legend>
                        <%			
                                }
                        %>
                                <label>
                                	<a title="<%=dt.getServentiaCargoUsuarioChefe()%>" href="Usuario?PaginaAtual=7&a1=<%=dt.getId_UsuarioServentia()%>&a2=<%=dt.getGrupoCodigo()%>&a3=<%=dt.getId_ServentiaCargo()%>&a4=<%=dt.getId_ServentiaCargoUsuarioChefe()%>&a5=<%=dt.getId_UsuarioServentiaChefe()%>">
                            		<%=dt.getDescricaoApresentacaoSelecaoServentiaGrupo()%> 
                                	</a>
                                </label/>
                                <br />
                        <%
                            }//fim for
                        %>
                        </fieldset>
                    <% 
                        } else if (liTemp.size() == 0){ %>
                            <b>Nenhuma Serventia Disponível.</b>
                    <%} %>
				
           </div>
      	<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>