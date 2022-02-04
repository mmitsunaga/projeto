package br.gov.go.tj.projudi.ne;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ImportaEscalaOficiaisDt;
import br.gov.go.tj.projudi.ps.ImportaEscalaOficiaisPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ImportaEscalaOficiaisNe {

	Funcoes funcao = new Funcoes();
	int contaCon = 0;

	ImportaEscalaOficiaisPs objPS = null;
	FabricaConexao objFC = null;

	public ImportaEscalaOficiaisNe() {
	}

	public int importaBancos() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		FileInputStream stream = new FileInputStream("D:/bancos.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);

			String array[] = new String[2];
			array = linha.split("#");

			// pesquisa banco pelo codigo
			lista = objPS.consultaBanco(array[0]);
			checaConexao();

			if (lista.get(0).getIdBanco() == 0) {

				objDt = new ImportaEscalaOficiaisDt();
				objDt.setCodigoBanco(Integer.parseInt(array[0]));
				objDt.setNomeBanco(array[1]);

				// cadastra bancos
				objPS.importaBancos(objDt);
				checaConexao();
				contaGravados++;
			}

			linha = br.readLine();
			contaLinha++;

		}

		if (objFC != null) {
			objFC.fecharConexao();
		}

		br.close();

		return contaGravados;
	}

	public int importaUsuarios() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;
		String cpf = "";
		int contaRepetido = 0;

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		FileInputStream stream = new FileInputStream("D:/contasBancariasSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);

			String array[] = new String[10];
			array = linha.split("#");

			if (array[12].trim().equalsIgnoreCase("ATIVO") && array[8].equalsIgnoreCase("122")) { // senador canedo

				cpf = funcao.completarZeros(String.valueOf(array[1]), 11);

				// pesquisa usuario pelo cpf
				lista = objPS.consultaUsuario(cpf);
				checaConexao();

				if (lista.get(0).getIdUsuario() == 0) {

					objDt = new ImportaEscalaOficiaisDt();
					objDt.setNomeUsuario(array[0]);
					objDt.setCpfUsuario(cpf);
					objDt.setIdEndereco(ImportaEscalaOficiaisDt.ID_ENDERECO_PROD);

					// cadastra usuarios
					objPS.importaUsuarios(objDt);
					checaConexao();

					contaGravados++;

				}
			}
			linha = br.readLine();
			contaLinha++;
		}

		if (objFC != null) {
			objFC.fecharConexao();
		}

		br.close();

		return contaGravados;
	}

	public int importaContaBancaria() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;
		String cpf = "";

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> listaUsuario = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaBanco = new ArrayList<>();
		FileInputStream stream = new FileInputStream("D:/contasBancariasSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {
			System.out.println(contaLinha + "...." + linha);
			String array[] = new String[13];
			array = linha.split("#");

			if (array[12].trim().equalsIgnoreCase("ATIVO") && array[8].equalsIgnoreCase("122")) { // senador canedo
				if (!((Integer.parseInt(array[2])) == 0) && !((Integer.parseInt(array[3])) == 0)
						&& !((Integer.parseInt(array[6])) == 0)) {

					// pesquisa usuario pelo cpf
					cpf = funcao.completarZeros(String.valueOf(array[1]), 11);
					listaUsuario = objPS.consultaUsuario(cpf);
					checaConexao();

					if (listaUsuario.get(0).getIdUsuario() == 0)
						System.out.println("...." + "Saiu pelo Cpf Usuario.." + array[1]);

					if (listaUsuario.get(0).getIdUsuario() != 0) {

						// pesquisa banco pelo codigo
						listaBanco = objPS.consultaBanco(array[2]);
						checaConexao();

						if (listaBanco.get(0).getIdBanco() == 0)
							System.out.println("...." + "Saiu pelo Codigo Banco..." + array[2]);

						if (listaBanco.get(0).getIdBanco() != 0) {

							// pesquisa conta bancaria com todos os dados
							int idContaBancaria = objPS.consultaContasBancarias(array[3], array[6], array[7],
									listaUsuario.get(0).getIdUsuario(), listaBanco.get(0).getIdBanco());
							checaConexao();

							if (idContaBancaria == 0) {

								// pesquisa conta bancaria so com o id do usuario
								// idContaBancaria =
								// objPS.consultaContasBancarias(listaUsuario.get(0).getIdUsuario());
								// checaConexao();

								// if (idContaBancaria != 0) {

								//
								// se ja existe conta bancaria para o usuario, mas com dados bancarios
								// diferentes
								//
								// desativa a conta existente para deixar ativa somente a que esta sendo
								// incluida
								//

								// objPS = new ImportaEscalaOficiaisPs(objFC.getConexao()); // altera conta
								// objPS.alteraContaBancaria(listaUsuario.get(0).getIdUsuario());
								// checaConexao();
								// }

								// pesquisa agencia

								objDt = new ImportaEscalaOficiaisDt();
								objDt.setAgencia(Integer.parseInt(array[3]));
								objDt.setNomeAgencia("Agencia " + Integer.parseInt(array[3]));
								objDt.setIdBanco(listaBanco.get(0).getIdBanco());
								int idAgencia = objPS.consultaAgencia(objDt);
								checaConexao();

								if (idAgencia == 0) {

									// cadastra agencia

									objDt = new ImportaEscalaOficiaisDt();
									objDt.setAgencia(Integer.parseInt(array[3]));
									objDt.setNomeAgencia("Agencia " + Integer.parseInt(array[3]));
									objDt.setIdBanco(listaBanco.get(0).getIdBanco());
									idAgencia = objPS.importaAgencia(objDt);
									checaConexao();
									contaGravados++;
								}
								// cadastra conta bancaria

								objDt = new ImportaEscalaOficiaisDt();
								objDt.setConta(Integer.parseInt(array[6]));
								objDt.setIdUsuario(listaUsuario.get(0).getIdUsuario());
								objDt.setIdAgencia(idAgencia);
								objDt.setContaDv(array[7]);
								objDt.setOperacao(array[5]);

								objPS.importaContaBancaria(objDt);
								checaConexao();
								contaGravados++;
							}
						}
					}
				}
			}
			linha = br.readLine();
			contaLinha++;
		}

		if (objFC != null)
			objFC.fecharConexao();

		br.close();

		return contaGravados;
	}

	public int criaEstruturaUsuario() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;
		String nomeServentia = "";
		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> listaUsuario = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaServentia = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaComarca = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaUsuarioServentia = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaUsuarioServentiaGrupo = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaServentiaCargo = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaGrupo = new ArrayList();
		FileInputStream stream = new FileInputStream("D:/contasBancariasSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);
			String array[] = new String[12];
			array = linha.split("#");

			if (array[12].trim().equalsIgnoreCase("ATIVO") && array[8].equalsIgnoreCase("122")) { // senador canedo

				// consulta comarca
				listaComarca = objPS.consultaComarca(array[8]);
				checaConexao();

				if (listaComarca.get(0).getIdComarca() != 0) {
					//
					// monta nome serventia central de mandados para todas as comarcas
					//
					nomeServentia = listaComarca.get(0).getNomeComarca().toLowerCase();
					StringBuilder sb = new StringBuilder();
					String[] letras = nomeServentia.split(" ");
					for (int i = 0; i < letras.length; i++) {
						String word = letras[i];
						word = word.substring(0, 1).toUpperCase() + word.substring(1);
						sb.append(" ").append(word);
					}
					nomeServentia = (sb.toString().replaceFirst(" ", "")) + " - " + "Central de Mandados";
					// pesquisa serventia
					listaServentia = objPS.consultaServentia(nomeServentia);
					checaConexao();

					if (listaServentia.get(0).getIdServentia() == 0) {

						objDt = new ImportaEscalaOficiaisDt();
						objDt.setCodigoServentia(array[9]);
						objDt.setIdComarca(listaComarca.get(0).getIdComarca());
						objDt.setNomeServentia(nomeServentia);
						objDt.setIdEndereco(ImportaEscalaOficiaisDt.ID_ENDERECO_PROD);
						// cadastra serventia
						int idServentia = objPS.cadastraServentia(objDt);
						checaConexao();
						contaGravados++;

						listaServentia.get(0).setIdServentia(idServentia);

					}

					// pesquisa usuario
					listaUsuario = objPS.consultaUsuario(funcao.completarZeros(array[1], 11));
					checaConexao();

					if (listaUsuario.get(0).getIdUsuario() != 0) {

						// pesquisa usuario serventia

						listaUsuarioServentia = objPS.consultaUsuarioServentia(listaUsuario.get(0).getIdUsuario(),
								listaServentia.get(0).getIdServentia());
						checaConexao();

						if (listaUsuarioServentia.get(0).getIdUsuServ() == 0) {

							objDt = new ImportaEscalaOficiaisDt();
							objDt.setIdUsuario(listaUsuario.get(0).getIdUsuario());
							objDt.setIdServentia(listaServentia.get(0).getIdServentia());

							// cadastra usuario serventia
							int idUsuServ = objPS.cadastraUsuarioServentia(objDt);
							checaConexao();
							contaGravados++;

							listaUsuarioServentia.get(0).setIdUsuServ(idUsuServ);

						}

						objDt = new ImportaEscalaOficiaisDt();
						objDt.setIdUsuServ(listaUsuarioServentia.get(0).getIdUsuServ());

						// pesquisa usuario serventia grupo
						listaUsuarioServentiaGrupo = objPS.consultaUsuarioServentiaGrupo(objDt);
						checaConexao();

						if (listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo() == 0) {

							objDt = new ImportaEscalaOficiaisDt();
							objDt.setIdUsuServ(listaUsuarioServentia.get(0).getIdUsuServ());

							// cadastra usuario serventia grupo
							int idUsuServGrupo = objPS.cadastraUsuarioServentiaGrupo(objDt);
							checaConexao();
							contaGravados++;

							listaUsuarioServentiaGrupo.get(0).setIdUsuServGrupo(idUsuServGrupo);

						}
						objDt = new ImportaEscalaOficiaisDt();
						objDt.setIdServentia(listaServentia.get(0).getIdServentia());
						objDt.setIdUsuServGrupo(listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo());

						// pesquisa serventia cargo
						listaServentiaCargo = objPS.consultaServentiaCargo(objDt);
						checaConexao();

						if (listaServentiaCargo.get(0).getIdServentiaCargo() == 0) {

							objDt = new ImportaEscalaOficiaisDt();
							objDt.setIdServentia(listaServentia.get(0).getIdServentia());
							objDt.setIdUsuServGrupo(listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo());

							// cadastra serventia cargo
							objPS.cadastraServentiaCargo(objDt);
							checaConexao();
							contaGravados++;
						}
					}
				}
			}

			linha = br.readLine();
			contaLinha++;

		}

		if (objFC != null)
			objFC.fecharConexao();

		br.close();

		return contaGravados;

	}

	public int cadastraRegiaoGenerica() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> listaComarca = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaRegiao = new ArrayList<>();
		FileInputStream stream = new FileInputStream("D:/regiaoBairroSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);

			String array[] = new String[6];
			array = linha.split("#");

			// consulta comarca
			listaComarca = objPS.consultaComarca(array[2]);
			checaConexao();

			if (listaComarca.get(0).getIdComarca() == 0)
				System.out.println("...." + "saiu pelo Codigo Comarca..." + array[2]);

			if (listaComarca.get(0).getIdComarca() != 0 && array[2].equalsIgnoreCase("122")) {

				// consulta regiao generica
				listaRegiao = objPS.consultaRegiaoGenerica(listaComarca.get(0).getIdComarca());
				checaConexao();

				if (listaRegiao.get(0).getIdRegiao() == 0) {

					objDt = new ImportaEscalaOficiaisDt();
					objDt.setIdComarca(listaComarca.get(0).getIdComarca());

					// cadastra regiao generica para todas as comarcas
					int id = objPS.cadastraRegiaoGenerica(objDt);
					checaConexao();
					contaGravados++;

					// altera regiao
					objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());
					objPS.alteraRegiao(id);
					checaConexao();
				}
			}
			linha = br.readLine();
			contaLinha++;
		}

		if (objFC != null)
			objFC.fecharConexao();

		br.close();

		return contaGravados;

	}

	public int cadastraZonaRegiaoBairro() throws Exception {

		int contaLinha = 0;
		int contaGravados = 0;

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> listaComarca = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaRegiao = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaZonaBairroRegiao = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaBairro = new ArrayList<>();
		List<ImportaEscalaOficiaisDt> listaZona = new ArrayList<>();
		FileInputStream stream = new FileInputStream("D:/regiaoBairroSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);

			String array[] = new String[6];
			array = linha.split("#");

			// pesquisa comarca
			listaComarca = objPS.consultaComarca(array[2]);
			checaConexao();

			if (listaComarca.get(0).getIdComarca() == 0)
				System.out.println(contaLinha + "...." + "Saiu pela comarca.." + array[2]);

			if (listaComarca.get(0).getIdComarca() != 0 && array[2].equalsIgnoreCase("122")) {
				//
				// projudi tem essa regiao com dois espacos entre VI e VARIAS
				//
				if (array[1].equalsIgnoreCase("REGIAO VI VARIAS ZONAS"))
					array[1] = "REGIAO VI  VARIAS ZONAS"; // pesquisa com dois espacos

				// pesquisa regiao
				listaRegiao = objPS.consultaRegiao(array[1], listaComarca.get(0).getIdComarca());
				checaConexao();

				if (listaRegiao.get(0).getIdRegiao() == 0) {

					objDt = new ImportaEscalaOficiaisDt();
					objDt.setCodgRegiao(Integer.parseInt(array[0]));
					objDt.setNomeRegiao(array[1]);
					objDt.setIdComarca(listaComarca.get(0).getIdComarca());

					// importa regiao

					int id = objPS.importaRegiao(objDt);

					listaRegiao.get(0).setIdRegiao(id);

					contaGravados++;
					checaConexao();
					// altera regiao
					objPS.alteraRegiao(id);
					checaConexao();

				}

				// consulta bairro
				listaBairro = objPS.consultaBairro(array[3]);
				checaConexao();

				if (listaBairro.get(0).getIdBairro() == 0)
					System.out.println("...." + "Saiu pelo bairro.." + array[3]);

				if (listaBairro.get(0).getIdBairro() != 0) {

					// consulta zona bairro regiao
					listaZonaBairroRegiao = objPS.consultaZonaBairroRegiao(listaBairro.get(0).getIdBairro());
					checaConexao();

					if (listaZonaBairroRegiao.get(0).getIdZonaBairroRegiao() == 0) {
						//
						// quando o bairro nao esta regionalizado
						// busca zona urbana da comarca e grava com a regiao
						//
						listaZona = objPS.consultaZona(listaComarca.get(0).getIdComarca());
						checaConexao();

						if (listaZona.get(0).getIdZona() == 0)
							System.out.println("...." + "Saiu pela zona.." + array[2]);

						if (listaZona.get(0).getIdZona() != 0) {

							objDt = new ImportaEscalaOficiaisDt();
							objDt.setIdRegiao(listaRegiao.get(0).getIdRegiao());
							objDt.setIdBairro(listaBairro.get(0).getIdBairro());
							objDt.setIdZona(listaZona.get(0).getIdZona());

							// cadastra zona bairro regiao
							objPS.cadastraZonaBairroRegiao(objDt);
							contaGravados++;
							checaConexao();
						}
					} else {
						// ja esta regionalizado. altera regiao pela que vem da exportacao. com codigo
						// temp 90991
						// so altera aqueles que nao vieram da exportacao.
						// codigo 90990 no caso de processar novamente.
						// 90991 nao podem ser deletados - ja existiam - nao vieram da exportacao
						if (Funcoes.isStringVazia(listaZonaBairroRegiao.get(0).getCodigoTemp()))
							listaZonaBairroRegiao.get(0).setCodigoTemp("0");
						if (!listaZonaBairroRegiao.get(0).getCodigoTemp().equalsIgnoreCase("90990")) {
							objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());
							objPS.alteraZonaBairroRegiao(listaZonaBairroRegiao.get(0).getIdZonaBairroRegiao(),
									listaRegiao.get(0).getIdRegiao());
							checaConexao();
						}
					}
				}
			}
			linha = br.readLine();
			contaLinha++;
		}

		if (objFC != null)
			objFC.fecharConexao();

		br.close();

		return contaGravados;

	}

	public int importaEscalasDeOficiais() throws Exception {

		int cont = 0;
		int cont1 = 0;
		int contaS = 0;
		int contaEsc = 0;
		int contaEsc1 = 0;

		int contaLinha = 0;
		int contaGravados = 0;

		String nomeServentia = "";
		String nomeRegiao = "";
		int idEscala = 0;

		ImportaEscalaOficiaisDt objDt = null;
		List<ImportaEscalaOficiaisDt> listaComarca = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaUsuario = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaServentia = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaMandadoTipo = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaRegiao = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaEscala = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaUsuarioServentia = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaUsuarioServentiaGrupo = new ArrayList();
		List<ImportaEscalaOficiaisDt> listaServentiaCargo = new ArrayList();
		FileInputStream stream = new FileInputStream("D:/escalasDeOficiaisSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();
		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		while (!(linha == null)) {

			System.out.println(contaLinha + "...." + linha);

			String array[] = new String[8];
			array = linha.split("#");

			// BUSCA COMARCA PELO CODIGO
			listaComarca = objPS.consultaComarca(array[0]);
			checaConexao();

			if (listaComarca.get(0).getIdComarca() == 0) {
				contaS++;

				System.out.println(contaLinha + "...." + "Saiu pela comarca......codigo comarca...." + array[0]);
			}

			if (listaComarca.get(0).getIdComarca() != 0 && array[0].equalsIgnoreCase("122")) {

				nomeServentia = listaComarca.get(0).getNomeComarca().toLowerCase(); // passar para minuscula
				StringBuilder sb = new StringBuilder();
				String[] letras = nomeServentia.split(" ");
				for (int i = 0; i < letras.length; i++) {
					String word = letras[i];
					word = word.substring(0, 1).toUpperCase() + word.substring(1);
					sb.append(" ").append(word);
				}
				nomeServentia = (sb.toString().replaceFirst(" ", "")) + " - " + "Central de Mandados";

				// consulta serventia
				listaServentia = objPS.consultaServentia(nomeServentia);
				checaConexao();

				if (listaServentia.get(0).getIdServentia() == 0) {
					contaS++;
					System.out
							.println(contaLinha + "...." + "Saiu pela serventia......id serventia...." + nomeServentia);
				}

				if (listaServentia.get(0).getIdServentia() != 0) {

					// PESQUISA USUARIO
					listaUsuario = objPS.consultaUsuario(funcao.completarZeros(array[4], 11));
					checaConexao();

					if (listaUsuario.get(0).getIdUsuario() == 0) {

						contaS++;
						System.out.println(contaLinha + "...." + "Saiu pelo usuario......cpf usuario...."
								+ funcao.completarZeros(array[4], 11));
					}

					if (listaUsuario.get(0).getIdUsuario() != 0) {

						if (array[7].trim().equalsIgnoreCase(""))
							nomeRegiao = "Região Genérica";
						else
							nomeRegiao = array[7];

						// pesquisa regiao pela regiao e id_comarca
						listaRegiao = objPS.consultaRegiao(nomeRegiao, listaComarca.get(0).getIdComarca());
						checaConexao();

						if (listaRegiao.get(0).getIdRegiao() == 0) {
							contaS++;
							System.out.println(contaLinha + "...." + "Saiu pela regiao......nome regiao...."
									+ nomeRegiao + ".id comarca..." + listaComarca.get(0).getIdComarca());
						}
						if (listaRegiao.get(0).getIdRegiao() != 0) {

							// busca mand_tipo pelo mand_tipo_codigo
							listaMandadoTipo = objPS.consultaTipoMandado(array[2]);
							checaConexao();

							if (listaMandadoTipo.get(0).getIdTipoMandado() == 0) {
								contaS++;
								System.out
										.println(contaLinha + "...." + "Saiu pelo tipo mandado......id tipo mandado...."
												+ listaMandadoTipo.get(0).getIdTipoMandado());
							}

							if (listaMandadoTipo.get(0).getIdTipoMandado() != 0) {

								// consulta escala pelo id_mand_tipo id_escala_tipo id_regiao id_serv
								idEscala = objPS.consultaEscala(listaMandadoTipo.get(0).getIdTipoMandado(),
										Integer.parseInt(array[1]), listaRegiao.get(0).getIdRegiao(),
										listaServentia.get(0).getIdServentia());
								checaConexao();

								if (idEscala == 0) {

									// consulta area
									String nome = objPS.consultaArea(Integer.parseInt(array[1]));
									checaConexao();

									if (nome.equalsIgnoreCase("Cível e Criminal"))
										nome = "Assistência";

									objDt = new ImportaEscalaOficiaisDt();
									objDt.setNomeEscala(listaComarca.get(0).getNomeComarca() + " - "
											+ listaRegiao.get(0).getNomeRegiao() + " - " + nome + " - "
											+ listaMandadoTipo.get(0).getNomeTipoMandado());
									objDt.setIdTipoMandado(listaMandadoTipo.get(0).getIdTipoMandado());
									objDt.setIdTipoEscala(Integer.parseInt(array[1]));
									objDt.setIdRegiao(listaRegiao.get(0).getIdRegiao());
									objDt.setIdServentia(listaServentia.get(0).getIdServentia());

									if (Integer.parseInt(array[6]) == 0)
										array[6] = "1";

									objDt.setQuantMandados(Integer.parseInt(array[6]));

									// cadastra escala
									idEscala = objPS.importaEscala(objDt);
									checaConexao();
									contaGravados++;
									contaEsc++;
								} else {
									contaEsc1++;
								}

								// busca usuario serventia
								listaUsuarioServentia = objPS.consultaUsuarioServentia(
										listaUsuario.get(0).getIdUsuario(), listaServentia.get(0).getIdServentia());
								checaConexao();

								if (listaUsuarioServentia.get(0).getIdUsuServ() == 0) {
									contaS++;
									System.out.println(
											contaLinha + "...." + "Saiu pelo usuario serventia......id usuario..."
													+ listaUsuario.get(0).getIdUsuario() + "....id serventia.."
													+ listaServentia.get(0).getIdServentia());
								}

								if (listaUsuarioServentia.get(0).getIdUsuServ() != 0) {

									objDt = new ImportaEscalaOficiaisDt();
									objDt.setIdUsuServ(listaUsuarioServentia.get(0).getIdUsuServ());

									// busca usuario serventia grupo
									listaUsuarioServentiaGrupo = objPS.consultaUsuarioServentiaGrupo(objDt);
									checaConexao();

									if (listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo() == 0) {
										contaS++;
										System.out.println(contaLinha + "...."
												+ "Saiu pelo usuario serventia grupo......id usuario serv..."
												+ listaUsuarioServentia.get(0).getIdUsuServ() + "...id grupo = 69");

									}

									if (listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo() != 0) {

										objDt = new ImportaEscalaOficiaisDt();
										objDt.setIdServentia(listaServentia.get(0).getIdServentia());
										objDt.setIdUsuServGrupo(listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo());

										// busca serv cargo
										listaServentiaCargo = objPS.consultaServentiaCargo(objDt);
										checaConexao();

										if (listaServentiaCargo.get(0).getIdServentiaCargo() == 0) {
											contaS++;
											System.out.println(contaLinha + "...."
													+ "Saiu pelo serventia cargo.....id serventia..."
													+ listaServentia.get(0).getIdServentia() + " id usu serv grupo..."
													+ listaUsuarioServentiaGrupo.get(0).getIdUsuServGrupo());

										}

										if (listaServentiaCargo.get(0).getIdServentiaCargo() != 0) {

											// busca serv cargo esc
											int idServCargoEsc = objPS.consultaServCargoEscala(
													listaServentiaCargo.get(0).getIdServentiaCargo(), idEscala);
											checaConexao();

											if (idServCargoEsc == 0) {

												objDt = new ImportaEscalaOficiaisDt();
												objDt.setIdServentiaCargo(
														listaServentiaCargo.get(0).getIdServentiaCargo());
												objDt.setIdEscala(idEscala);

												// cadastra servCargoEscala
												objPS.importaServCargoEscala(objDt);
												cont++;
												checaConexao();
												contaGravados++;

											} else {
												cont1++;
											}
										}
									}
								}
							}
						}
					}
				}
			}

			linha = br.readLine();
			contaLinha++;

		}

		if (objFC != null)
			objFC.fecharConexao();

		br.close();
		System.out.println("escalas GRAVADAS.............." + contaEsc);
		System.out.println("escalas JA GRAVADAS..........." + contaEsc1);

		System.out.println("oficial escalas GRAVADAS.............." + cont);
		System.out.println("oficial escalas JA GRAVADAS..........." + cont1);

		System.out.println("saidas.............." + contaS);

		System.out.println("TOTAL LINHAS........" + contaLinha);

		return contaGravados;

	}

	public void checaConexao() throws Exception {

		if (contaCon > 90) {
			objFC.fecharConexao();
			objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());
			contaCon = 0;
		} else
			contaCon++;
	}

	//
	// metodo importa guia saldo
	//
	public int importaGuiaSaldo() throws Exception {
		
		//
		// Novo jeito de fazer.
		//
		
	    // Usa um texto so. Repete a guia de saldo para cada guia vinculada ao saldo.
		
		// Se o saldo for maior que zero. Grava saldo no projudi - verifica se ainda não esta gravado.
		// Se a guia vinculada ao saldo existir no projudi, vincula ela no saldo projudi.
				
		// Se o saldo for zero - Se não existir guia vinculada ao saldo no projudi, NÃO GRAVA NADA.
		// Se existir grava o saldo e desativa guia vinculada a este saldo.
		

		int contaLinha = 0;
		int contaGravados = 0;
		String idGuiaEmis = "";
		int idProc = 0;
		int contaS = 0;
		int contaS1 = 0;
		String numrProcesso = "";
		String digitoProcesso = "";
		String anoProcesso = "";

		ImportaEscalaOficiaisDt objDt = null;
		FileInputStream stream = new FileInputStream("D:/guiaSaldoProdSenadorCanedo.txt");
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();

		contaLinha++;

		objFC = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		objPS = new ImportaEscalaOficiaisPs(objFC.getConexao());

		/*
		 * while (!(linha == null)) {
		 * 
		 * System.out.println(contaLinha + "...." + linha);
		 * 
		 * String array[] = new String[10]; array = linha.split("#");
		 * 
		 * // pesquisa guia pelo numero
		 * 
		 * idGuiaEmis = objPS.consultaGuiaSaldo(Integer.parseInt(array[0])); // numero
		 * guia completo checaConexao();
		 * 
		 * if (idGuiaEmis == 0) { // // consultar processo pelo numero // numrProcesso =
		 * array[2].substring(0, 8); digitoProcesso = array[2].substring(8, 10);
		 * anoProcesso = array[2].substring(10, 14); idProc =
		 * objPS.consultaProcesso(numrProcesso, digitoProcesso, anoProcesso);
		 * 
		 * if (idProc == 0) { contaS++; System.out.println( contaLinha + "...." +
		 * "Saiu pelo processo..." + numrProcesso + "-" + digitoProcesso); }
		 * 
		 * if (idProc != 0) {
		 * 
		 * objDt = new ImportaEscalaOficiaisDt(); objDt.setNumeroGuiaCompleto(array[0]);
		 * objDt.setGuiaSaldoStatus(array[1]); // 88 - liberada 89 - nao liberada
		 * objDt.setIdProc(Integer.toString(idProc));
		 * objDt.setGuiaSaldoValor(Double.parseDouble(array[3]) / 100); // // grava guia
		 * saldo // // objPS.importaGuiaSaldo(objDt); checaConexao(); contaGravados++; }
		 * } linha = br.readLine(); contaLinha++; }
		 * 
		 * System.out.println ("Foram gravadas..." + contaGravados +
		 * "  guia(s) de saldo");
		 */
		//
		// Inativa guias projudi para saldo de locomoção, que foram usadas para compor
		// saldo da Guia de Saldo.
		//

		contaLinha = 0;
		contaGravados = 0;
		stream = new FileInputStream("D:/guiaNaoLiberadaProdSenadorCanedo.txt");
		reader = new InputStreamReader(stream);
		br = new BufferedReader(reader);
		linha = br.readLine();
		contaLinha++;

		while (!(linha == null)) {
			System.out.println(contaLinha + "................................" + linha);

			String array[] = new String[10];
			array = linha.split("#");

			// pesquisa guia vinculada pelo numero

			idGuiaEmis = objPS.consultaGuia(array[0]); 
			checaConexao();

			if (idGuiaEmis.equalsIgnoreCase("")) {
				contaS++;
				System.out.println(contaLinha + "......Saiu pela guia..." + array[0]);
			}

			if (!idGuiaEmis.equalsIgnoreCase("")) {

				// pesquisa guia saldo

				idGuiaEmis = objPS.consultaGuiaSaldo(array[1]);
				checaConexao();

				if (idGuiaEmis.equalsIgnoreCase("")) {
					contaS1++;
					System.out.println(contaLinha + "......Saiu pela guia saldo..." + array[1]);
				}
				
				if (!idGuiaEmis.equalsIgnoreCase("")) {
					contaGravados++;

					// altera status guia

					objPS.alteraStatusGuiaProjudi(array[0], array[1]);
					checaConexao();
				}							
			}
			linha = br.readLine();
			contaLinha++;
		}
		System.out.println("Foram perdidas..." + contaS + "  guia(s)");
		System.out.println("Foram perdidas SALDO..." + contaS1 + "  guia(s)");
		System.out.println("Foram alteradas..." + contaGravados + "  guia(s)");
		if (objFC != null)
			objFC.fecharConexao();
		br.close();
		return contaGravados;
	}
}
