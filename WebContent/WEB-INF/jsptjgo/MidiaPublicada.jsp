<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="midiaPublicadaDt" scope="session" class= "br.gov.go.tj.projudi.dt.MidiaPublicadaDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	
	<link
      rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous"
    />
    <!-- Generic page styles -->
    <style>
      #navigation {
        margin: 10px 0;
      }
      @media (max-width: 767px) {
        #title,
        #description {
          display: none;
        }
      }
    </style>
    <!-- blueimp Gallery styles -->
    <link
      rel="stylesheet"
      href="https://blueimp.github.io/Gallery/css/blueimp-gallery.min.css"
    />
    <!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
    <link rel="stylesheet" href="./css/file-upload/jquery.fileupload.css" />
    <link rel="stylesheet" href="./css/file-upload/jquery.fileupload-ui.css" />
    <!-- CSS adjustments for browsers with JavaScript disabled -->
    <noscript>
    	<link rel="stylesheet" href="./css/file-upload/jquery.fileupload-noscript.css"/>
    </noscript>
    <noscript>
    	<link rel="stylesheet" href="./css/file-upload/jquery.fileupload-ui-noscript.css"/>
    </noscript>
	
	<script type='text/javascript' src='./js/jquery.js'></script>
  	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
    <script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
    <script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>	
	
	<%@ include file="./js/MidiaPublicada.js"%>
</head>

<body>
	<div id="divCorpo" class="divCorpo ">
		<div class="area"><h2>&raquo; | TJGO Mídias </h2></div>
		
		<!-- form id="fileupload"
		       action="http://audiencias-judiciais-2020.s3.tjgo.jus.br/" 
		       method="post"
		       enctype="multipart/form-data"
		       name="Formulario"-->
		<form id="fileupload"
		       action="<%=request.getAttribute("tempPrograma")%>" 
		       method="post"
		       name="Formulario">
		    
		    <input type="hidden" name="_method" value="put" />
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar container">						   		
		   		<label class="formEdicaoLabel"> Número do Processo </label>
		   		<br />
				<span class="destaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
    			<br />
    			<label class="formEdicaoLabel"> Serventia </label><br>
    			<span class="destaque"><%=processoDt.getServentia()%></span/>
    			<br />
    			
    			<label class="formEdicaoLabel" for="DataRealizacao"> Data da Realização </label><br>
				<input class="destaque" id="DataRealizacao" name="DataRealizacao" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=(midiaPublicadaDt.getDataHora() != null ? midiaPublicadaDt.getDataHora().getDataFormatadaddMMyyyy() : "")%>" onkeyup="mascara_data(this)" onblur='verifica_data(this);' onkeypress="return DigitarSoNumero(this, event)" />
				<img id="calData" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataRealizacao,'dd/mm/yyyy',this)" />
				
				<br />
				<label class="formEdicaoLabel" for="HoraRealizacao"> Hora da Realização </label>
				<br />
				<input class="destaque" id="HoraRealizacao" name="HoraRealizacao" index="0" maxlength="5" size="5" onkeypress="return DigitarHoraResumida(this, event)" onkeyup="MascararHoraResumida(this); autoTab(this,5)" value="<%=(midiaPublicadaDt.getDataHora() != null ? midiaPublicadaDt.getDataHora().getHoraFormatadaHHmm() : "")%>" />
    			<br />
    			
    			<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
				<input class="FormEdicaoimgLocalizar" type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
				</label>
				<br /> 						
				<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="50" maxlength="80" value="<%=midiaPublicadaDt.getComplemento()%>"/>
    			<br />
    			<div id="divStatus"><span class="label label-warning">Atenção</span> Podem ser enviados até <%=UsuarioSessao.getQuantidadeMaximaArquivosMidiaPublicada()%> arquivos de <%=UsuarioSessao.getTamanhoMBArquivoMidiaPublicada()%>MB cada com os seguintes formatos: <%=UsuarioSessao.getTipoArquivoMidiaPublicada()%>.</div>
    			<br />
    			<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
		        <div class="row fileupload-buttonbar">
		          <div class="col-lg-7">
		            <!-- The fileinput-button span is used to style the file input field as button -->
		            <span class="btn btn-success fileinput-button">
		              <i class="glyphicon glyphicon-plus"></i>
		              <span>Selecionar...</span>
		              <input type="file" name="files[]" accept="<%=UsuarioSessao.getTipoArquivoMidiaPublicada()%>" onchange="if (!selecionaArquivo(this.files)) {this.value='';}" />
		            </span>
		            <button id="btnReset" type="reset" class="btn btn-warning cancel">
		              <i class="glyphicon glyphicon-ban-circle"></i>
		              <span>Cancelar</span>
		            </button>
		            <!-- The global file processing state -->
		            <span class="fileupload-process"></span>
		          </div>
		          <!-- The global progress state -->
		          <div class="col-lg-5 fileupload-progress fade">
		            <!-- The global progress bar -->
		            <div
		              class="progress progress-striped active"
		              role="progressbar"
		              aria-valuemin="0"
		              aria-valuemax="100"
		            >
		              <div
		                class="progress-bar progress-bar-success"
		                style="width: 0%;"
		              ></div>
		            </div>
		            <!-- The extended global progress state -->
		            <div class="progress-extended">&nbsp;</div>
		          </div>		          		        
		          <!-- The table listing the files available for upload/download -->
			      <table role="presentation" class="table table-striped">
			        <tbody class="files"></tbody>
			      </table>
			      
			      	<br />
		          			
	   				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<button name="imgInserir" type="button" title="Clique para salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');valideUploadArquivo();"> Enviar Mídia </button>
					</div>
					<!-- div class="clear"></div-->		
					<div id="divConfirmarSalvar" class="ConfirmarSalvar" style="display:none">
						<div class="divMensagemsalvar">Clique para confirmar a operação </div>
		           		<button type="submit" class="btn btn-primary start" value="Confirmar Salvar">
		              		<i class="glyphicon glyphicon-upload"></i>
		              		<span>Confirmar</span>
		               	</button> 
		      		</div>
		      		<div id="divAlerta" style="display:none"><span class="label label-warning">Alerta</span> Esse processamento poderá demorar vários minutos, por favor aguarde e não feche esta aba do navegador.</div>
		      	</div>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
		
		 <!-- The blueimp Gallery widget -->
	    <div
	      id="blueimp-gallery"
	      class="blueimp-gallery blueimp-gallery-controls"
	      aria-label="image gallery"
	      aria-modal="true"
	      role="dialog"
	      data-filter=":even"
	    >
	      <div class="slides" aria-live="polite"></div>
	      <h3 class="title"></h3>
	      <a
	        class="prev"
	        aria-controls="blueimp-gallery"
	        aria-label="previous slide"
	        aria-keyshortcuts="ArrowLeft"
	      ></a>
	      <a
	        class="next"
	        aria-controls="blueimp-gallery"
	        aria-label="next slide"
	        aria-keyshortcuts="ArrowRight"
	      ></a>
	      <a
	        class="close"
	        aria-controls="blueimp-gallery"
	        aria-label="close"
	        aria-keyshortcuts="Escape"
	      ></a>
	      <a
	        class="play-pause"
	        aria-controls="blueimp-gallery"
	        aria-label="play slideshow"
	        aria-keyshortcuts="Space"
	        aria-pressed="false"
	        role="button"
	      ></a>
	      <ol class="indicator"></ol>
	    </div>
	</div>
	
	<!-- The template to display files available for upload -->
    <script id="template-upload" type="text/x-tmpl">
      {% for (var i=0, file; file=o.files[i]; i++) { %}
          <tr class="template-upload fade{%=o.options.loadImageFileTypes.test(file.type)?' image':''%}">
              <td>
                  <!--span class="preview"></span-->
              </td>
              <td>
                  <p class="name">{%=file.name%}</p>
                  <strong class="error text-danger"></strong>
              </td>
              <td>
                  <p class="size">Processing...</p>
                  <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
              </td>
              <td>
                  {% if (!o.options.autoUpload && o.options.edit && o.options.loadImageFileTypes.test(file.type)) { %}
                    <!--button class="btn btn-success edit" data-index="{%=i%}" disabled>
                        <i class="glyphicon glyphicon-edit"></i>
                        <span>Edit</span>
                    </button-->
                  {% } %}
                  {% if (!i && !o.options.autoUpload) { %}
                      <button class="btn btn-primary start" disabled style="display:none">
                          <i class="glyphicon glyphicon-upload"></i>
                          <span>Start</span>
                      </button>
                  {% } %}
                  {% if (!i) { %}
                      <button class="btn btn-warning cancel">
                          <i class="glyphicon glyphicon-ban-circle"></i>
                          <span>Cancel</span>
                      </button>
                  {% } %}
              </td>
          </tr>
      {% } %}
    </script>
    <!-- The template to display files available for download -->
    <script id="template-download" type="text/x-tmpl">
      {% for (var i=0, file; file=o.files[i]; i++) { %}
          {% console.log(new Date());
             //console.log("_response Template:", i, "\n", __response);  
           %}
          <tr class="template-download fade{%=file.thumbnailUrl?' image':''%}">
              <td>
                  <!--span class="preview"-->
                      {% if (file.thumbnailUrl) { %}
                          <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                      {% } %}
                  <!--/span-->
              </td>
              <td>
                  <p class="name">
                      {% if (file.url) { %}
                          <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" target="_blank" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                      {% } else { %}
                          <span>{%=file.name%}</span>
                      {% } %}
                  </p>
                  {% if (file.error) { %}
                      <div><span class="label label-danger">Error</span> Erro ao enviar arquivo. {%=file.error%}.</div>
                  {% } %}
              </td>
              <td>
                  <span class="size">{%=o.formatFileSize(file.size)%}</span>
              </td>
              <td>
                  {% if (file.deleteUrl) { %}
                      <!--button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                          <i class="glyphicon glyphicon-trash"></i>
                          <span>Delete</span>
                      </button>
                      <input type="checkbox" name="delete" value="1" class="toggle"-->
                  {% } else { %}
                      <!--button class="btn btn-warning cancel">
                          <i class="glyphicon glyphicon-ban-circle"></i>
                          <span>Cancel</span>
                      </button-->
                  {% } %}
              </td>
          </tr>
      {% } %}
    </script>
    <script
      src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"
      integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ"
      crossorigin="anonymous"
    ></script>
    <!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
    <script src="./js/file-upload/vendor/jquery.ui.widget.js"></script>
    <!-- The Templates plugin is included to render the upload/download listings -->
    <script src="https://blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script>
    <!-- The Load Image plugin is included for the preview images and image resizing functionality -->
    <script src="https://blueimp.github.io/JavaScript-Load-Image/js/load-image.all.min.js"></script>
    <!-- The Canvas to Blob plugin is included for image resizing functionality -->
    <script src="https://blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
    <!-- blueimp Gallery script -->
    <script src="https://blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
    <!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
    <script src="./js/file-upload/jquery.iframe-transport.js"></script>
    <!-- The basic File Upload plugin -->
    <script src="./js/file-upload/jquery.fileupload.js"></script>
    <!-- The File Upload processing plugin -->
    <script src="./js/file-upload/jquery.fileupload-process.js"></script>
    <!-- The File Upload image preview & resize plugin -->
    <script src="./js/file-upload/jquery.fileupload-image.js"></script>
    <!-- The File Upload audio preview plugin -->
    <script src="./js/file-upload/jquery.fileupload-audio.js"></script>
    <!-- The File Upload video preview plugin -->
    <script src="./js/file-upload/jquery.fileupload-video.js"></script>
    <!-- The File Upload validation plugin -->
    <script src="./js/file-upload/jquery.fileupload-validate.js"></script>
    <!-- The File Upload user interface plugin -->
    <script src="./js/file-upload/jquery.fileupload-ui.js"></script>
    <!-- The main application script -->
    <script>
	    $(function () {
		  'use strict';	   	  
		   $('#fileupload').fileupload({
			   	submit: function (e, data) {		        
			    	var $this = $(this);
			    	if (data.files.length > 0) {
			    		var arquivo = data.files[0];			    		
			    		var contentType = arquivo.type;
			    		if (contentType==undefined || contentType==null || contentType==""){
			    			contentType='audio/mpeg';
			    		}
			    		console.log("start " + arquivo.name,"\t", new Date());
			    		//mostrarMensagemOk("start", arquivo.name);
			    		if(TIPO_ENVIO_JS === 'true') {
			    			fileuploadJs(e, data, $this, arquivo, contentType);		
			    		} else {
			    			fileuploadServer(e, data, $this, arquivo, contentType);	
			    		}			    			    		
			    	}
			        return false;
			   	},
			   	
			   	maxFileSize: eval(TAMANHO_MAXIMO_MEGABYTES), // 10 MB
			   	minFileSize: 1, // 1 Byte
			   	maxNumberOfFiles: eval(QUANTIDADE_MAXIMA_ARQUIVOS),
			   	messages: {
			   	  "uploadedBytes": "Bytes carregados excedem o tamanho do arquivo",
			   	  "maxNumberOfFiles": "Número máximo de arquivos excedido",
			   	  "acceptFileTypes": "Tipo de arquivo não permitido",
			   	  "maxFileSize": "O arquivo é muito grande",
			   	  "minFileSize": "O arquivo é muito pequeno",
			   	  "unknownError": "Erro desconhecido"
			   	}
		   	}); 
		   
		   $('#fileupload')
		   .on('fileuploaddone', 
				 function (e, data) 
				 {
			        //sucesso para upload - request
			        console.log("done: " + data.files[0].name, new Date(), "\t", data);
			        //mostraStatus("Arquivo " + data.files[0].name + " enviado com sucesso");
			        //__response = data._response;
			        //console.log("_response: \n", __response);
				 })
		   .on('fileuploadfail', 
				 function (e, data) 
				 {
			        //falha para upload - request
			        //data.files[0].error = "Erro ao enviar o arquivo";
			        console.log("fail: " + data.files[0].name, new Date(), "\t", data);
				 })
		   .on('fileuploadalways', 
				 function (e, data) 
				 {
			       console.timeEnd();
				 })
		    .on('chunkdone', 
				 function (e, data) 
				 {
			       console.log("chunkdone: ", e);
			       console.log("chunkdone: ", data);
				 })
		   .on('fileuploadstart',
				 function (e) 
				 {
			   		console.time();
			   		console.log('Uploads started');
				 })		   
		   .on('fileuploadstop', 
				 function (e) 
				 {
		    		console.log('Uploads finished');
		    		mostraStatus("Todos os arquivos foram enviados com sucesso. Gerando movimentação, aguarde");
		    		var url = 'MidiaPublicada?AJAX=ajax&PaginaAtual=' + <%=Configuracao.SalvarResultado%>;
		    		
		    		$.ajax({
		    			url: encodeURI(url),
		    			method: "POST",  				
		    			data: { '__Pedido__': $('#__Pedido__').val(), 'DataRealizacao': $('#DataRealizacao').val(), 'HoraRealizacao': $('#HoraRealizacao').val(), 'MovimentacaoComplemento': $('#MovimentacaoComplemento').val() },
		    			context: document.body,
		    			timeout: 300000,
		    			success: function(retorno){	 
		    				if (retorno.sucesso) {
		    					var urlProcesso = 'BuscaProcesso?Id_Processo=' + <%=processoDt.getId_Processo()%>;
		    					window.location.replace("BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&MensagemOk=Midia Publicada com Sucesso!");
		    					// similar behavior as an HTTP redirect
		    					// window.location.replace("http://stackoverflow.com");
		    					// similar behavior as clicking on a link
		    					//window.location.href = "http://stackoverflow.com";
		    					//Mostrar('divConfirmarSalvar');						
		    				} else {
		    					alert(retorno.mensagem);
		    				}				
		    			},
		    			beforeSend: function(data){},
		    			error: function(request, status, error){
		    				alert(request.responseText); 
		    				Ocultar('divConfirmarSalvar');
		    			},
		    			complete: function(data){
		    				console.timeEnd();
		    				$(this).removeClass('fileupload-processing');
		    			}
		    		});			    		
		    		
				 });
// 		   .on('fileuploadcompleted', 
// 				 function (e, data) 
// 				 {
// 		    	    //sucesso para download - response
// 			        console.log("completed:\n", data);	
// 				 })
// 		   .on('fileuploadfailed', 
// 				 function (e, data) 
// 				 {
// 			        //falha para download - response 
// 		            console.log("failed:\n", data);
// 				 });
	  
			// Enable iframe cross-domain access via redirect option:
		   	//$('#fileupload').fileupload(
			//   'option',
			//   'redirect',
			//   window.location.href.replace(/\/[^/]*$/, '/cors/result.html?%s')
		    //);
		   
		   function fileuploadJs(e, data, $this, arquivo, contentType) {
			   var dados = { 'id':-1, 'nomeArquivo': arquivo.name, 'arquivo':null, "contentType":contentType };
	    	   var url = 'MidiaPublicada?AJAX=ajax&PaginaAtual=' + <%=Configuracao.Curinga7%>;
	    	   $this.addClass('fileupload-processing')
	    	   $.ajax({
	    			url: encodeURI(url),
	    			method: "POST",  				
	    			data: dados,
	    			context: document.body,
	    			timeout: 300000,
	    			success: function(retorno){	
	    				var form = document.getElementById("fileupload");
	    				form.action = retorno.urlCephAssinada;
	    				
	    				data.url = retorno.urlCephAssinada;
	    				data.sequentialUploads = true;
	    				
	    				if(VERBO_ENVIO_PUT_JS === 'true') {
	    					data.type = "PUT";
	    					if(FORCE_IFRAME_TRANSPORT_JS === 'true') {
	    						data.forceIframeTransport = true;
		    				}
	    				} else {
	    					data.type = "POST";
	    					data.maxChunkSize = eval(TAMANHO_PARTE_MEGABYTES);// 5000000 - 5MB;
	    				}	    				
	    				
	                  	$this.fileupload('send', data);
	    			},			    			
	    			beforeSend: function(data){},
	    			error: function(request, status, error){
	    				mostrarMensagemErro("Erro", request.responseText);
	    			},
	    			complete: function(data){}
	    		})
	    		.always(function () {
	    			$this.removeClass('fileupload-processing');	    			
	    		})
	    		.done(function (result) {
	    			console.log('done arquivo.name: ' + arquivo.name);
	    			console.log('done result', result);
				});	
		   }
		   
		   function fileuploadServer(e, data, $this, arquivo, contentType) {
			   	Ocultar('divConfirmarSalvar');
			   	Ocultar('divBotoesCentralizados');
				Ocultar('btnReset');
				Mostrar('divAlerta');
				$this.addClass('fileupload-processing');
			   
		   		var url = 'MidiaPublicada?AJAX=ajax&PaginaAtual=' + <%=Configuracao.Curinga9%> + '&contentTypeFile='+contentType;	
	    	   	
	    	   	var form = document.getElementById("fileupload");
	    	   	form.action = url;
   				
	    	   	data.url = url;
   				data.type = "POST";
   				data.maxChunkSize = eval(TAMANHO_PARTE_MEGABYTES);// 5000000 - 5MB;
   				
   				$this.fileupload('send', data);
		   }
		   
		   function mostraStatus(mensagem) {
			   Mostrar('divStatus');
			   $("#divStatus").html('<span class="label label-info">Status</span> ' + mensagem + ' ...');
		   }
		});
    </script>
    <!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
    <!--[if (gte IE 8)&(lt IE 10)]>
      <script src="./js/file-upload/cors/jquery.xdr-transport.js"></script>
    <![endif]-->
</body>
</html>
