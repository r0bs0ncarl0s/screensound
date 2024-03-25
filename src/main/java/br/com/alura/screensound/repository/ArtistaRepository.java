package br.com.alura.screensound.repository;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    Optional<Artista> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT m FROM Artista a JOIN a.musicas m WHERE a.nome ILIKE %:nome%")
    List<Musica> buscaMusicasPorArtista(String nome);

    @Query("SELECT a FROM Musica m JOIN m.artista a WHERE m.titulo ILIKE %:nomeMusica%")
    List<Artista> buscaArtistaPorMusicas(String nomeMusica);
 
    @Query("SELECT a.nome FROM Artista a ORDER BY a.nome")
    List<String> listaTodosNomeArtitas();
}
