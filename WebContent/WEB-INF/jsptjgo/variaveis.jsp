<%@page import="br.gov.go.tj.projudi.ne.ModeloNe"%>
<div>
<ul>
	<li><a href="#" onclick="return false;">Vari&aacute;veis</a>
		<ul>
		
		
		<li><a class="TemFilho" href="#" onclick="return false;">Processo</a>
				<ul>
					<li><a class="TemFilho" href="#" onclick="return false;">Polos Ativos</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polosativos.inicio}');">In&iacute;cio</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polosativos.fim}');">Fim</a></li>
						</ul>
					</li>
					<li><a class="TemFilho" href="#" onclick="return false;">Polo Ativo</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.rg}');">Rg</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.cpfOuCnpj}');">Cpf ou Cnpj</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.profissao}');">Profissão</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.estadoCivil}');">Estado Civil</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.dataNascimento}');">Data de Nascimento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.nomePai}');">Nome do Pai</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.nomeMae}');">Nome da Mãe</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.naturalidade}');">Naturalidade</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.sexo}');">Sexo</a></li>
							<li><a class="TemFilho" href="#" onclick="return false;">Endere&ccedil;o</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.logradouro}');">Logradouro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.numero}');">N&uacute;mero</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.complemento}');">Complemento</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.bairro}');">Bairro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.telefone}');">Telefone</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.cidade}');">Cidade</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.estado}');">Estado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.poloativo.endereco.cep}');">CEP</a></li>
								</ul>
							</li>
						</ul>
					</li>
					<li><a class="TemFilho" href="#" onclick="return false;">Polos Passivos</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polospassivos.inicio}');">In&iacute;cio</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polospassivos.fim}');">Fim</a></li>
						</ul>
					</li>
					<li><a class="TemFilho" href="#" onclick="return false;">Polo Passivo</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.rg}');">Rg</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.cpfOuCnpj}');">Cpf ou Cnpj</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.profissao}');">Profissão</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.estadoCivil}');">Estado Civil</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.dataNascimento}');">Data de Nascimento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.nomePai}');">Nome do Pai</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.nomeMae}');">Nome da Mãe</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.naturalidade}');">Naturalidade</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.sexo}');">Sexo</a></li>
							<li><a class="TemFilho" href="#" onclick="return false;">Endere&ccedil;o</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.logradouro}');">Logradouro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.numero}');">N&uacute;mero</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.complemento}');">Complemento</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.bairro}');">Bairro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.telefone}');">Telefone</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.cidade}');">Cidade</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.estado}');">Estado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.polopassivo.endereco.cep}');">CEP</a></li>
								</ul>
							</li>							
						</ul>
					</li>
					<li><a class="TemFilho" href="#" onclick="return false;">Vítimas</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitimas.inicio}');">In&iacute;cio</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitimas.fim}');">Fim</a></li>
						</ul>
					</li>
					<li><a class="TemFilho" href="#" onclick="return false;">Vítima</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.rg}');">Rg</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.cpfOuCnpj}');">Cpf ou Cnpj</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.profissao}');">Profissão</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.estadoCivil}');">Estado Civil</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.dataNascimento}');">Data de Nascimento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.nomePai}');">Nome do Pai</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.nomeMae}');">Nome da Mãe</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.naturalidade}');">Naturalidade</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.sexo}');">Sexo</a></li>
							<li><a class="TemFilho" href="#" onclick="return false;">Endere&ccedil;o</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.logradouro}');">Logradouro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.numero}');">N&uacute;mero</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.complemento}');">Complemento</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.bairro}');">Bairro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.telefone}');">Telefone</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.cidade}');">Cidade</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.estado}');">Estado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vitima.endereco.cep}');">CEP</a></li>
								</ul>
							</li>
						</ul>
					</li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.numero}');">N&uacute;mero</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.dependente.numero}');">N&uacute;mero do processo dependente</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.data.distribuicao}');">Data Distribuiç&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.data.transito.julgado}');">Data Tr&acirc;nsito em Julgado</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">A&ccedil;&atilde;o</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.acao.tipo}');">Tipo</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.acao.valor}');">Valor</a></li>
						</ul>
					</li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.classe.cnj}');">Classe CNJ (Natureza)</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.assuntos}');">Assuntos</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.crimes}');">Crimes (Infrações)</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.localcumprimentopena}');">Local de Cumprimento da Pena</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Respons&aacute;veis</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.juiz}');">Juiz</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.relator}');">Relator</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.revisor}');">Revisor</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.vogal}');">Vogal</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.presidente.camara}');">Presidente C&acirc;mara</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${processo.presidente.turma}');">Presidente Turma</a></li>
						</ul>
					</li>
				</ul>
			</li>
		
		
		<li><a class="TemFilho" href="#" onclick="return false;">Certid&atilde;o</a>
			
			<ul>
			<li><a class="TemFilho" href="#" onclick="return false;">Identificaç&atilde;o</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.requerente}');">Requerente</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.profissao}');">Profiss&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.estado.civil}');">Estado Civil</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.sexo}');">Sexo</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.cpfCnpj}');">Cpf ou Cnpj</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.rg}');">Rg</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.nacionalidade}');">Nacionalidade</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.nome.mae}');">Nome da Mãe</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.nome.pai}');">Nome do Pai</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.data.nascimento}');">Data de Nascimento</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.identificacao.domicilio}');">Domicílio</a></li>
					
				</ul>
			</li>
			<li><a class="TemFilho" href="#" onclick="return false;">Processos</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processos.inicio}');">In&iacute;cio</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processos.fim}');">Fim</a></li>						
				</ul>
			</li>
			<li><a class="TemFilho" href="#" onclick="return false;">Negativa Positiva-Processo</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.numero}');">N&uacute;mero</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.serventia}');">Serventia</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.tipo}');">Processo Tipo</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.assunto}');">Assunto(Dispositivo Legal, Artigo)</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.promovente}');">Promovente</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.promovido}');">Promovido</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.data.distribuicao}');">Data da Distribuiç&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.data.transito.julgado}');">Data Transito Julgado</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.valor.da.acao}');">Valor da Aç&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.advogado.promovente}');">Advogado Promovente</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.advogado.promovido}');">Advogado Promovido</a></li>	
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.dataBaixa}');">Data Baixa</a></li>	
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.motivoBaixa}');">Motivo Baixa</a></li>	
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.processo.dataRecebimentoDenuncia}');">Data Recebimento Denúncia</a></li>	
						
				</ul>
			</li>

			<li><a class="TemFilho" href="#" onclick="return false;">Circunstanciada-Processo</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.numero.acao}');">N&uacute;mero da Ação Penal</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.cidade.origem}');">Cidade/Estado origem</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.vara.origem}');">Vara origem</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.distribuicao}');">Data Distribuição</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.denuncia}');">Data Denúncia</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.sentenca}');">Data Sentença</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.transito}');">Data Transito</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.pronuncia}');">Data Pronúncia</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.acordao}');">Data Acórdão</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.data.admonitoria}');">Data Admonitória</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.estabelecimento.penal}');">Estabelecimento Penal</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.regime}');">Regime</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.tipo.pena}');">Tipo de Pena</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.condenacoes}');">Condenações</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.modalidades}');">Modalidades</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${certidao.circunstanciada.processo.sursis}');">Tempo do Sursis</a></li>
				</ul>
			</li>
			</ul>
			</li>
		
		
		<li><a class="TemFilho" href="#" onclick="return false;">Cumprimento</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.codigo}');">Código</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.codigobarra}');">Código de barra</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Audi&ecirc;ncia</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.audiencia.data}');">Data</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.audiencia.hora}');">Hora</a></li>
						</ul>
					</li>			
					<li><a class="TemFilho" href="#" onclick="return false;">Parte</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.rg}');">Rg</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.cpfOuCnpj}');">Cpf ou Cnpj</a></li>
							<li><a class="TemFilho" href="#" onclick="return false;">Endere&ccedil;o</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.logradouro}');">Logradouro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.numero}');">N&uacute;mero</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.complemento}');">Complemento</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.bairro}');">Bairro</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.telefone}');">Telefone</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.cidade}');">Cidade</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.estado}');">Estado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.parte.endereco.cep}');">CEP</a></li>
								</ul>
							</li>
						</ul>
					</li>

					
					<li><a class="TemFilho" href="#" onclick="return false;">Prazo</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${cumprimento.prazo.dias}');">Dias</a></li>
						</ul>
					</li>
				</ul>
			</li>

		<li><a class="TemFilho" href="#" onclick="return false;">Mandado (Central)</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandadoCentral.numeroMandadoExpedido}');">Número</a></li>
				</ul>
		</li>		
		
		<li><a class="TemFilho" href="#" onclick="return false;">Mandado de Prisão</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.numeroMandado}');">Número</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.numeroProcessoCompleto}');">Numero processo completo</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.classeCNJ}');">Classe CNJ</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.assuntoCNJ}');">Assunto CNJ</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Promovido</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.sexo}');">Sexo</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.cpf}');">Cpf</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.endereco}');">Endereço completo</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.dataNascimento}');">Data de nascimento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.nomeMae}');">Nome da mãe</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.nomePai}');">Nome do pai</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.promovido.naturalidade}');">Naturalidade</a></li>
						</ul>
					</li>
<!--					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.assuntoDelitoPrincipal}');">Infração</a></li>-->
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.penaImposta}');">Pena Imposta</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.regime}');">Regime</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.localRecolhimento}');">Local de Recolhimento</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.dataPrescricao}');">Data da Prescrição</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.decisao}');">Decisão</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${mandado.titulo}');">Título</a></li>
				</ul>
			</li>
		
		
		<li><a class="TemFilho" href="#" onclick="return false;">Oficial Certid&atilde;o</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${brasao}');">Bras&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${brasao.goias}');">Bras&atilde;o do Estado de Goiás</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${data}');">Data atual</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${hora}');">Hora atual</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.numero}');">Número da Certidão</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.processoNumero}');">Número do Processo</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.numeroMandado}');">Número do Mandado</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.processoPromoventeNome}');">Nome do Promovente</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Endereço do Promovente</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoLogradouro}');">Logradouro</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoNumero}');">Número</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoComplemento}');">Complemento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoQuadra}');">Quadra</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoLote}');">Lote</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoBairro}');">Bairro</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoCEP}');">CEP</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoCidade}');">Cidade</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.promoventeEnderecoUf}');">UF</a></li>
						</ul>
					</li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.processoPromovidoNome}');">Nome do Promovido</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.dataDiligencia}');">Data da Diligência</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.horaDiligencia}');">Hora da Diligência</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.diligenciaNomeIntimado}');">Nome do Intimado</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.DiligenciaRGIntimado}');">Número do RG do Intimado</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.diligenciaEndereco}');">Endereço do Intimado</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.dataEmissao}');">Data da Emissão</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.oficialCertidao.usuario}');">Usuário</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${oficialCertidao.cargo}');">Cargo</a></li>
				</ul>
			</li>
			
			
			<li><a class="TemFilho" href="#" onclick="return false;">Outros</a>
				<ul>
					<li><a class="TemFilho" href="#" onclick="return false;">Usu&aacute;rio atual</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${usuario.nome}');">Nome</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${usuario.cargo}');">Cargo</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${usuario.matricula}');">Matricula</a></li>							
						</ul>
					</li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${brasao}');">Bras&atilde;o</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${brasao.goias}');">Bras&atilde;o do Estado de Goiás</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${data}');">Data atual</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${hora}');">Hora atual</a></li>
				</ul>
			</li>
		
		
		
		
		
			<li><a class="TemFilho" href="#" onclick="return false;">Serventia</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.nome}');">Nome</a></li>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.comarca}');">Comarca</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Endere&ccedil;o</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.logradouro}');">Logradouro</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.numero}');">N&uacute;mero</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.complemento}');">Complemento</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.bairro}');">Bairro</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.telefone}');">Telefone</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.cidade}');">Cidade</a></li>
							<li><a class="TemFilho" href="#" onclick="return false;">Estado</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.estado.completo}');">Completo</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.estado.sigla}');">Sigla</a></li>
								</ul>
							</li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${serventia.endereco.cep}');">CEP</a></li>
						</ul>
					</li>
				</ul>
			</li>
			
			<li><a class="TemFilho" href="#" onclick="return false;">Audi&ecirc;ncia/Sess&atilde;o</a>
				<ul>
					<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.classe.cnj}');">Classe CNJ (Natureza)</a></li>
					<li><a class="TemFilho" href="#" onclick="return false;">Processo</a>
						<ul>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.numero}');">N&uacute;mero</a></li>
							<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.comarca}');">Comarca</a></li>							
							<li><a class="TemFilho" href="#" onclick="return false;">Polo Ativo</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.poloativo.nome}');">Nome</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.poloativo.advogado}');">Advogado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.poloativo.formaDeTratamento}');">Forma de Tratamento</a></li>
								</ul>
							</li>
							<li><a class="TemFilho" href="#" onclick="return false;">Polo Passivo</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.polopassivo.nome}');">Nome</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.polopassivo.advogado}');">Advogado</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.polopassivo.formaDeTratamento}');">Forma de Tratamento</a></li>
								</ul>
							</li>							
							<li><a class="TemFilho" href="#" onclick="return false;">Respons&aacute;veis</a>
								<ul>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.relator}');">Relator</a></li>
									<li><a href="#" onclick="CKEDITOR.instances.editor1.insertText('\${audienciaSessao.processo.representanteMP}');">Representante MP</a></li>
								</ul>
							</li>					
						</ul>
					</li>
				</ul>
			</li>
			
		</ul>
	</li>
</ul>

</div>
