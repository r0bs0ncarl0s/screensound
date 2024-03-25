package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import br.com.alura.screensound.model.TipoArtista;
import br.com.alura.screensound.repository.ArtistaRepository;
import br.com.alura.screensound.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***                    
                                        
                    1 - Cadastrar artistas
                    2 - Cadastrar músicas
                    3 - Listar músicas
                    4 - Buscar músicas por artistas
                    5 - Buscar artistas por música
                    6 - Pesquisar dados sobre um artista
                    7 - Pesquisar qtd. músicas cadastradas por artista
                    8 - Listar artistas cadastrados com suas músicas                 
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    buscarArtistaPorMusica();
                    break;    
                case 6:
                    pesquisarDadosDoArtista();
                    break;
                case 7:
                    qtdMusicasPorArtista();
                    break;
                case 8:
                    listarTodosArtistasComSuasMusicas();
                    break;    
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

	private void pesquisarDadosDoArtista() {
        System.out.println("Pesquisar dados sobre qual artista? ");
        var nome = leitura.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

    private void buscarMusicasPorArtista() {
        System.out.println("Buscar músicas de que artista? ");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscaMusicasPorArtista(nome);
        musicas.forEach(System.out::println);
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de que artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
        if (artista.isPresent()) {
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista não encontrado");
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("s")) {
            System.out.println("Informe o nome desse artista: ");
            var nome = leitura.nextLine();
            System.out.println("Informe o tipo desse artista: (solo, dupla ou banda)");
            var tipo = leitura.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);
            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovo = leitura.nextLine();
        }
    }
    
    private void buscarArtistaPorMusica() {
    	System.out.println("Buscar artista por qual música? ");
        var nomeMusica = leitura.nextLine();
        List<Artista> artistas = repositorio.buscaArtistaPorMusicas(nomeMusica);
        artistas.forEach(e -> System.out.println("Artista: " + e.getNome()));
	}
    
	private void qtdMusicasPorArtista() {
		System.out.println("Buscar músicas de que artista? ");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscaMusicasPorArtista(nome);
		System.out.println("Artista: " + musicas.get(0).getArtista().getNome() + "\nMúsica(s) cadastrada(s): " + musicas.size());
		System.out.println("Nome das músicas:");
		musicas.forEach(m -> System.out.println(m.getTitulo()));
	}
	

    private void listarTodosArtistasComSuasMusicas() {
    	List<String> listaArtistas = repositorio.listaTodosNomeArtitas();
    	if (!listaArtistas.isEmpty()) {
    		for (int i = 0; i < listaArtistas.size(); i++) {
    			System.out.println("\nArtista: \n" + listaArtistas.get(i));
    			List<Musica> musicas = repositorio.buscaMusicasPorArtista(listaArtistas.get(i));
    			System.out.println("Músicas:");
    			if(!musicas.isEmpty()) {
    				musicas.forEach(m -> System.out.println(m.getTitulo()));
    			}else {
    				System.out.println("Nenhuma música encontrada");
    			}
			}	
    	}
	}
}
