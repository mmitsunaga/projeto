/**
 * Mensagens em JS para o projudi
 * @author Ronneesley Moura Teles
 * @since 12/09/2008 09:46
 * @dependencias jquery.js e conflitos.js
 * Alteracoes
 *   22/09/2008
 *      Ronneesley Moura Teles
 *      Compatibilidade com outros navegadores
 */
 
/**
 * Construtor da classe
 * @author Ronneesley Moura Teles
 * @since 12/09/2008 09:56
 */
var Mensagens = function(url, elementoId, associar){
 	//Se nao teve o parametro
 	if (associar == null) associar = false;
 
 	this.url = url;
 	this.elementoId = elementoId; 	 	
 	
 	//Se e para associar
 	if (associar) this.buscar(true);
};
 
/**
 * Funcionalidades
 * @author Ronneesley Morua Teles
 * @since 12/09/2008 11:04
 */
Mensagens.prototype = {
 	//Versao da classe
 	versaoClasse: "1.0", 
 	
 	//Id do elemnto onde as mensagens aparecerao
 	elementoId: null,
 	
 	//URL onde sera buscado as mensagens
 	url: null,
	 
	//Ultimo xml requisitado
	xml: null, 	
 	
 	/**
 	 * Busca por js o conteudo das mensagens em XML
 	 * @author Ronneesley Moura Teles 	 
 	 * @since 12/09/2008 09:55
 	 * @param boolean as, caso deseje associar logo em seguida da busca
 	 */
 	buscar: function(as){
 	 	if (as == null) as = false;

 		this.xml = $.ajax({
 		 	type: "GET",
 		 	url: this.url, 
			data: null,
			async: false
		}).responseXML;
 		
 	 	//Se e para associar
	 	if (as) this.associar(this.elementoId);
 	},
 	
 	/**
 	 * Associar o resultado a um elemento
 	 * @author Ronneesley Moura Teles 	 
 	 * @since 12/09/2008 09:55
 	 * @param String id, id do elemento a ser trabalhado
 	 */
 	associar: function(id){
 	 	//Verifica se o xml esta preenchido
 	 	if (this.xml == null) return false;

		//Pega as noticias
		var mensagens = this.xml.getElementsByTagName("mensagem");
 		
		//Precorre as mensagens
		for (var i = 0; i < mensagens.length; i++){
		 	var mensagem = mensagens[i];
		 	var jmensagem = $(mensagem);
		 	
		 	var msg = document.createElement("div");
		 	var jmsg = $(msg);
		 	jmsg.attr("class", "mensagem");
		 	
		 	var titulo = document.createElement("strong");
		 	$(titulo).text(jmensagem.attr("data") + " - " + jmensagem.attr("titulo") );
		 	
		 	var conteudo = document.createElement("p");
		 	$(conteudo).html(jmensagem.text());

		 	jmsg.append(titulo);
		 	jmsg.append(conteudo);
		 	
		 	//Adiciona as mensagens na div
		 	this.elemento().append(jmsg);
		}
 	},

	/**
	 * Retorna o elemento das mensagens
	 * @author Ronneesley Moura Teles
	 * @since 12/09/2008 10:48
	 */
	elemento: function(){
		return $("#" + this.elementoId);
		//return document.getElementById(this.elementoId);
	}
} 