/**
 *  Javascript para manipulacao de tabelas de arquivos dos eventos dos processos de execução do projudi
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:15
 * Dependencias: jquery.js, conflitos.js e tabela.js
 */
var TabelaArquivosEventoExecucao = function(id, pai, link, campo, pa, editavel, resposta, possivelMarcar, possivelBloquear){
	if (editavel == null) editavel = false;

	this.editavel = editavel;
	this.id = id;
	this.pai = pai;
	this.link = link;
	this.campo = campo;
	this.paginaAtual = pa;
	this.resposta = resposta;
	this.possivelMarcar = possivelMarcar;
	this.possivelBloquear = possivelBloquear;
};
 
TabelaArquivosEventoExecucao.prototype = {
 	/**
 	 * Id da tabela de arquivos
 	 */
 	id: null,
 	
 	/**
 	 * Objeto pai 
 	 */
 	pai: null,
 
 	/**
 	 * Tabela dos arquivos
 	 */
	tabela: null,
	
	/**
	 * Link para o arquivo a fazer upload
	 */
	link: null,
	
	/**
	 * Pagina atual
	 */
	paginaAtual: null,
	
	/**
	 * Campo
	 */
	campo: null,
	
	/**
	 * 	Determina se nos casos de arquivos nao assinados
	 * Aparecera a opcao de edicao do arquivo
	 */
	editavel: false,
	
	/**
	 * Se o arquivo e uma resposta
	 */
	resposta: false,
	
	/**
	 * Se e possivel marcar em um check box o arquivo
	 */
	possivelMarcar: false,

	/**
	 * Se é possível bloquear um arquivo
	 */
	 possivelBloquear: false,

	/**
	 * Adicionar uma lista de arquivos na tabela
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 10:29
	 * @param Array() lista, lista de objetos para serem dicionados
	 */
	adicionar: function(lista){
		var display = $('.imgValidar').css("display");
	 	//Percorre todos elementos da lista
	 	for (var i = 0; i < lista.length; i++){
	 	 	var obj = lista[i]; //Objeto do laco
	 	 	
	 	 	var linha = [];
	 	 	
	 	 	if (this.possivelMarcar) {
	 	 		linha.push("<input type=\"checkbox\" name=\"arquivos[]\" value=\"" + obj.arquivoDt.id + "\" />");
	 	 	}
	 	 	
	 	 	linha.push(obj.movimentacaoTipo);
	 	 	linha.push(obj.arquivoDt.arquivoTipo);
			linha.push(obj.arquivoDt.usuarioAssinadorFormatado == ""?"ARQUIVO NÃO ASSINADO":obj.arquivoDt.usuarioAssinadorFormatado);
			  				
			//Verifica se ira aparecera a resposta
			if (this.resposta == true){
				linha.push(obj.resposta == "true"?"Sim":"Não");
			}

			//Quando um arquivo estiver válido mostra links 
			if (obj.valido == true){
				linha.push("<a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "\">" + 
								(obj.arquivoDt.nomeArquivoFormatado == ""?"Arquivo sem nome":obj.arquivoDt.nomeArquivoFormatado) + "</a>");
			} else {
				linha.push("<span class='bloqueado' title='Arquivo Bloqueado'>" + (obj.arquivoDt.nomeArquivoFormatado == ""?"Arquivo sem nome":obj.arquivoDt.nomeArquivoFormatado) + "</span>");
			}
			
								
			//Se for editavel, aparece a opcao de editar
			if (this.editavel == true){
				if (obj.arquivoDt.usuarioAssinadorFormatado == ""){
					var hash = "";
					
					if (obj.hash != null) hash = obj.hash;
				
					linha.push("<a href='#' onclick=\"editarPendencia(" + obj.id + ", \'" + hash + "\' , "+ obj.arquivoDt.id_ArquivoTipo + ", \'" + obj.arquivoDt.arquivoTipo + "\')\"><img src='imagens/22x22/ico_editar.png' alt='Editar' title='Editar o arquivo não assinado' />Editar</a>");
				} else {
					linha.push("");
				}
			}

			//Só deve mostrar botões para validar ou invalidar arquivo se usuário tem permissão para bloquear
			if (this.possivelBloquear == true){
				if (obj.valido){
					linha.push("<img id='imgValidar' class='imgValidar' src='imagens/22x22/ico_fechar.png' alt='Bloquear Arquivo' title='Bloquear Arquivo' " +
					" onclick=\"javascript: invalidarArquivo('" + obj.id + "','" + obj.arquivoDt.nomeArquivoFormatado + "')\" />");
				} else {
					linha.push("<img id='imgValidar' class='imgValidar' src='imagens/22x22/ico_liberar.png' alt='Desbloquear Arquivo' title='Desbloquear Arquivo' " +
					" onclick=\"javascript: validarArquivo('" + obj.id + "','" + obj.arquivoDt.nomeArquivoFormatado + "')\" />");
				}
			}
			
	 	 	this.tabela.addLinha( linha );
	 	 	$('.imgValidar').css('display',display);
		}	
	},
	
	/**
	 * Criar a tabela de arquivos
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 09:11
	 * @param Array() lista, lista de objetos caso queira adicionar no final
	 */
	criar: function(lista){
	 	var fieldset = document.createElement("fieldset");
	 	//var legend = document.createElement("legend");	 	
	 	//$j(legend).html("Arquivos");	 	
	 	//$j(fieldset).append(legend);
		$(fieldset).attr("id", this.id);
	 	$("#" + this.pai).append(fieldset);

		var cabecalho = [];
		
		if (this.possivelMarcar){
			cabecalho.push("Op.");
		}
		cabecalho.push("Movimentação do Processo");
		cabecalho.push("Tipo Arquivo");
		cabecalho.push("Assinador");
		
		//Se e para aparecer a coluna resposta
		if (this.resposta == true){
			cabecalho.push("Resposta");
		}
		
		cabecalho.push("Arquivo");
		
		//Se e editavel, aparece a coluna de opcoes
		if (this.editavel == true){
			cabecalho.push("Opções");
		}

		this.tabela = new Tabela(this.id + "_tabela", cabecalho, this.id, "Arquivos");	 
		this.tabela.criar();
		
		//Verifica se passou a lista, caso tenha, adiciona os itens
		if (lista != null)
			this.adicionar(lista);
	}	

}