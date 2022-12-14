import entities.Film;
import exceptions.EmptyDataException;
import exceptions.EntityNotFoundException;
import exceptions.LoadMgrException;
import exceptions.NonValidInputValue;
import managers.CatalogIMDB;
import managers.LoadMgr;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class appIMDB {

    public static void main(String[] args){

        try{
            CatalogIMDB cat = CatalogIMDB.getInstance();

            final String filmFile = "smallerfiles/films_small";
            final String castFile = "smallerfiles/cast_small";

            LoadMgr loadMgr = new LoadMgr(filmFile, castFile);
            loadMgr.loadData();

            /*
             * Casos de uso como ejemplo para documentación.
             * La instanciación, nosotros lo hacemos en el
             * constructor de CatalogIMDB.java
             *
             * DataCollection<Artist<?>> casting = new LinealWrapper<>();
             * CatalogIMDB.setCasting(casting);

             * casting = new BTreeWrapper<>();
             * CatalogIMDB.setCasting(casting);

             * casting = new HashMapWrapper<>();
             * CatalogIMDB.setCasting(casting);
            */

            Scanner sc = new Scanner(System.in);
            int option =- 1;

            while(option != 0) {
                System.out.println("Escoja una opcion:");
                System.out.println("-----------------");
                System.out.println("[1] Mostrar informacion de pelicula.");
                System.out.println("[2] Mostrar informacion de interprete.");
                System.out.println("[3] Anadir voto a pelicula [0-10].");
                System.out.println("[4] Eliminar pelicula.");
                System.out.println("[5] Calcular distancia entre dos artistas.");
                System.out.println("[6] Mostrar camino mas corto entre dos artistas.");

                System.out.println("[0] Salir.");
                System.out.print(">> ");
                try{
                    option = Integer.parseInt(sc.nextLine());
                    clearConsole();
                    if(option < 0 || option > 6)
                        throw new NonValidInputValue();

                } catch(NumberFormatException | NonValidInputValue e){
                    clearConsole();
                    System.out.println("[EXCEPTION] Input incorrecto. Introduce un numero entre el 0-6.");
                    continue;
                }
                switch(option) {
                    case 1:
                        try{
                            System.out.println("[1] Introduce el nombre de la pelicula: ");
                            System.out.print(">> ");
                            String film = sc.nextLine();
                            clearConsole();
                            try{
                                cat.displayFilmInfo(film);
                            } catch(EntityNotFoundException e){
                                System.out.println(e.getMessage());
                            }
                        } catch(NoSuchElementException | IllegalStateException ignore){}
                        break;
                    case 2:
                        try{
                            System.out.println("[2] Introduce el nombre del artista: ");
                            System.out.print(">> ");
                            String init = sc.nextLine();
                            clearConsole();
                            cat.displayArtistInfo(init);
                        } catch(NoSuchElementException | IllegalStateException ignore){}{
                        }
                        break;
                    case 3:
                        try{
                            System.out.println("[3] Introduce el nombre de la pelicula: ");
                            System.out.print(">> ");
                            String film = sc.nextLine();
                            System.out.println("[3] Introduce tu voto: ");
                            System.out.print(">> ");
                            try{
                                cat.addFilmVote(film, Float.parseFloat(sc.nextLine()));
                            } catch(EntityNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            clearConsole();
                        } catch (NonValidInputValue | IllegalArgumentException | NoSuchElementException e){
                            clearConsole();
                            System.out.println("[EXCEPTION] Introduce un numero entre 0-10, por favor");
                        }
                        break;
                    case 4:
                        try{
                            System.out.println("[4] Introduce el nombre de la pelicula a borrar: ");
                            System.out.print(">> ");
                            String film = sc.nextLine();
                            clearConsole();
                            Film<?> f = cat.removeFilm(film);
                            System.out.println(cat.showStatusAfterDeletion(f));
                        } catch(EntityNotFoundException | EmptyDataException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        try{
                            System.out.println("[5] Introduce el nombre del primer artista: ");
                            System.out.print(">> ");
                            String init = sc.nextLine();
                            System.out.println("[5] Introduce el nombre del segundo artista: ");
                            System.out.print(">> ");
                            String dest = sc.nextLine();
                            clearConsole();
                            System.out.println("Distancia entre " + init + " y " + dest + ": " + cat.getGraphDistance(init, dest));
                        } catch(NoSuchElementException | IllegalStateException ignore){} catch (
                                EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    break;
                    case 6:
                        try{
                            System.out.println("[6] Introduce el nombre del primer artista: ");
                            System.out.print(">> ");
                            String init = sc.nextLine();
                            System.out.println("[6] Introduce el nombre del segundo artista: ");
                            System.out.print(">> ");
                            String dest = sc.nextLine();
                            clearConsole();
                            System.out.println("Camino más corto entre " + init + " y " + dest + ": ");
                            cat.displayShortestPath(init, dest);
                        } catch(NoSuchElementException | IllegalStateException ignore){}
                        break;
                    default:
                        break;
                }
            }
            sc.close();
        } catch (LoadMgrException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método encargado de "limpiar" la consola cuando corresponda.
     * Depende del sistema operativo en el que sea ejecutado, se llamará
     * a un comando u otro.
     *
     * Sin embargo, en las consolas integradas de los IDEs, estos comandos
     * no son reconocidos. Así que fuerzo un limpiado de 20 líneas.
     */
    public static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
                Runtime.getRuntime().exec("cls");
            else if(os.contains("Linux") || os.contains("Mac"))
                Runtime.getRuntime().exec("clear");

            for(int i=0; i <= 20; i++)
                System.out.println(" ");
        }
        catch (final Exception ignore){}
    }
}