package managers;

import exceptions.EmptyDataException;
import exceptions.NonValidInputValue;
import libs.Stopwatch;
import entities.Artist;
import entities.Film;
import entities.models.DataModel;
import templates.DataWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import exceptions.EntityNotFoundException;

public class CatalogIMDB extends DataModel {
    private static CatalogIMDB instance;

    protected CatalogIMDB(){
        films = new DataWrapper<>();
        casting = new DataWrapper<>();
    }

    public static CatalogIMDB getInstance() {
        if(instance == null)
            instance = new CatalogIMDB();
        return instance;
    }

    public void addFilmVote(String filmName, float score) throws IllegalArgumentException, NonValidInputValue {
        try{
            if(score < 0 || score > 10)
                throw new NonValidInputValue();
            Film film = films.binarySearch(filmName);
            film.addVote(score);
        } catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void displayFilmInfo(String title) throws EntityNotFoundException {
        try{
            displayData(films, title);
        } catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void displayArtistInfo(String name){
        try{
            displayData(casting, name);
        } catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void displayData(DataWrapper collection, String identifier) throws EntityNotFoundException {

        Stopwatch sw = new Stopwatch();
        try{
            Object entity = collection.binarySearch(identifier);
            System.out.println("Busqueda finalizada en " + sw.elapsedTime() + " segundos.");
            System.out.println("Nombre: "+ identifier);

            try{
                double rating = 0.0;
                String dataText = "";
                String dataText2 = "";
                int dataNum = 0;
                ArrayList entityList = null;

                if(entity instanceof Artist){
                    rating = ((Artist)entity).getRating(true);
                    dataText = "Peliculas (";
                    dataNum = ((Artist)entity).getDataNum();
                    try{
                        entityList = ((Artist)entity).getDataList();
                    } catch(EmptyDataException e){
                        System.out.println(e.getMessage());
                    }
                }else if(entity instanceof Film){
                    rating = ((Film)entity).getRating(false);
                    dataText = "Actores (";
                    dataNum = ((Film)entity).getDataNum();
                    dataText2 = " | votos: " + ((Film)entity).getVotes();
                    try{
                        entityList = ((Film)entity).getDataList();
                    } catch(EmptyDataException e){
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println(dataText + dataNum+") ");
                System.out.println("Rating: " + BigDecimal.valueOf(rating).setScale(2, RoundingMode.FLOOR) + dataText2);
                System.out.println("_____________");
                System.out.println("_____________");
                assert entityList != null;
                for(Object subEntity : entityList){
                    try{
                        if(subEntity instanceof Artist)
                            System.out.println(((Artist)subEntity).getIdentifier() + " [r=" + BigDecimal.valueOf(((Artist)subEntity).getRating(true)).setScale(2, RoundingMode.FLOOR)+ "]");
                        else if(subEntity instanceof Film)
                            System.out.println(((Film)subEntity).getIdentifier() + " [r=" + BigDecimal.valueOf(((Film)subEntity).getRating(false)).setScale(2, RoundingMode.FLOOR)+ ", v="+ ((Film)subEntity).getVotes()+"]");
                    } catch(NumberFormatException e){
                        System.out.println("Rating: -");
                    }
                }
                System.out.println("_____________");
                System.out.println("_____________");

            } catch(NumberFormatException e){
                System.out.println("Rating: -");
            }

        } catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
}
