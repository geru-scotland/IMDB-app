package managers;

import entities.Film;
import exceptions.EmptyDataException;
import exceptions.EntityNotFoundException;
import exceptions.LoadMgrException;
import exceptions.NonValidInputValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class CatalogIMDBTest {

    static CatalogIMDB cat;

    // Para mantener congruencia entre los tests.
    static int initFilmSize = 0;
    static int initCastSize = 0;
    static int modFilmSize = 0;
    static int modCastSize = 0;

    @BeforeAll
    static void setUp() {

        cat = CatalogIMDB.getInstance();
        try{
            LoadMgr loadMgr = new LoadMgr("files/films", "files/cast");
            loadMgr.loadData();
        } catch(LoadMgrException e){
            System.out.println(e.getMessage());
        }
        initFilmSize = cat.getFilms().size();
        initCastSize = cat.getCasting().size();
    }

    @Test
    public void addFilmVoteTest(){
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.addFilmVote("Non existent film", 8));
        Assertions.assertThrows(NonValidInputValue.class, () -> cat.addFilmVote("Fight Club", 12));

        // Se necesitan los ficheros grandes:
        Assertions.assertDoesNotThrow(()-> cat.addFilmVote("Fight Club", 9));
        Assertions.assertDoesNotThrow(()-> cat.addFilmVote("I Love Sydney", 4));
    }

    @Test
    void removeFilmTest() throws EmptyDataException, EntityNotFoundException {

        /*
         * Intento de eliminaci�n de peliculas no existentes, deben lanzar excepci�n.
         */
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.removeFilm("Wrong film"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.removeFilm("Filmatronss"));

        /*
         * Me aseguro de la existencia de un artista en concreto.
         */
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.getCasting().search("Chiesatypo, Ricardo"));
        Assertions.assertDoesNotThrow(() -> cat.getCasting().search("Vera, Brandon"));
        Assertions.assertDoesNotThrow(() -> cat.getCasting().search("Chiesa, Ricardo"));

        /*
         * Eliminaci�n de pel�cula existente, no debe de lanzar exepci�n.
         */
        Assertions.assertDoesNotThrow(() -> cat.removeFilm("Fights"));
        modFilmSize++;

        /*
         * NOTA: Necesita los ficheros grandes.
         * El artista Rotstein, Sebastian ha participado en 2 peliculas.
         */
        Assertions.assertEquals(3, cat.getCasting().search("Rotstein, Sebastian").getDataList().size());

        /*
         * Nos aseguramos da la existencia de una pelicula.
         */
        Assertions.assertDoesNotThrow(() -> cat.getFilms().search("Filmatron"));

        /*
         * Obtenemos el n�mero de peliculas de una actriz que ha participado en
         * Filmatron y que sabemos en una pelicula m�s (ergo, 2).
         */
        int artistNumFilms = cat.getCasting().search("Setton, Carolina").getWrapper().size();

        /*
         * La borramos.
         */
        Film<?> film = cat.removeFilm("Filmatron");
        modFilmSize++;
        modCastSize += 2; //2 artistas van a ser eliminados al ser Filmatron su �nica pelicula

        /*
         * Buscamos la pel�cula de manera expl�cita, se ha de lanzar una excepci�n.
         */
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.getFilms().search(film.getIdentifier()));

        /*
         * Despu�s de las 2 eliminaciones, debemos tener 998 peliculas (films_tiny.txt).
         * Si se utilizan los ficheros grandes: 692084 peliculas y 2792967 artistas.
         */
        Assertions.assertEquals(initFilmSize - modFilmSize, cat.getFilms().size());
        Assertions.assertEquals(initCastSize - modCastSize, cat.getCasting().size());

        /*
         * Los artistas Chiesa, Rocardo y Goncalves, Luciano - al �nicamente haber participado en la pelicula anterior
         * son eliminados.
         */
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.getCasting().search("Chiesa, Ricardo"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.getCasting().search("Goncalves, Luciano-"));

        /*
          Comprobamos que la actriz cuyo n�mero de pel�culas hemos almacenado antes y eran 2,
          Sigue existiendo:
         */
        Assertions.assertDoesNotThrow(() -> cat.getCasting().search("Setton, Carolina"));

        /*
         * Y ahora, que efectivamente, consta que �nicamente ha participado en 1 pelicula tras el
         * borrado de Filmatron.
         */
        Assertions.assertEquals(artistNumFilms - 1, cat.getCasting().search("Setton, Carolina").getWrapper().size());

        /*
         * Despu�s del borrado de Filmatron, en nuestro sistema ha de constar que Rotstein, Sebastian
         * ha participado en 2 pel�culas.
         */
        Assertions.assertEquals(2, cat.getCasting().search("Rotstein, Sebastian").getDataList().size());
    }

    @Test
    void addTest(){
        Film film = new Film();
        film.populateInfo("Random new Film\t2022\t-1\t-1");
        cat.getFilms().add(film);
        modFilmSize--;
        Assertions.assertEquals(initFilmSize - modFilmSize, cat.getFilms().size());
    }

    @Test
    void artistSearch(){
        Assertions.assertThrows(EntityNotFoundException.class, () -> cat.getCasting().search("Non existant artist"));
        Assertions.assertDoesNotThrow(() -> cat.getCasting().search("Morris, Nato"));
    }

    @AfterAll
    static void showInfo(){
        System.out.println(" ");
        System.out.println("[POST-TESTS]");
        System.out.println("Peliculas: " + cat.getFilms().size() + " | Artistas: " + cat.getCasting().size());
    }
}