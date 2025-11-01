import java.io.IOException;                                                  //import des biblioteques nécessaires pour la gestion des fichiers pris en parametres
import java.nio.file.Files;
import java.nio.file.Path;

public class Lancement {
    public static void main(String[] args) {
        int[] ladata = {};                                                    //declaration d'un tableau vide dans le cas ou aucun fichier n est passe en parametre
        int[] lautredata;                                                     //declaration d'un tableau pour stocker les donnees lues depuis le fichier
        if (args.length > 0) {
            String filePath = args[0];                                        //recuperation du chemin du fichier passe en parametre
            try {
                String content = Files.readString(Path.of(filePath)).trim();  //lecture du contenu du fichier et suppression des espaces inutiles
                if (content.isEmpty()) lautredata = new int[0];               //gestion du cas ou le fichier est vide

                String[] parts = content.split("\\s+");                 //division du contenu en parties en utilisant les espaces comme separateurs
                lautredata = new int[parts.length];                           //initialisation du tableau lautredata avec la taille appropriee
                for (int i = 0; i < parts.length; i++) {                      //conversion de chaque partie en entier et stockage dans le tableau lautredata
                    lautredata[i] = Integer.parseInt(parts[i]);
                }
            } catch (IOException e) {                                         //gestion des erreurs de lecture du fichier
                System.err.println(" Erreur de lecture du fichier : " + e.getMessage());
                return;
            }
            Benchmark.run(lautredata);                                        //appel de la methode run de la classe Benchmark avec les donnees lues depuis le fichier
        }
        else{
            Benchmark.run(ladata);                                            //appel de la methode run de la classe Benchmark avec le tableau vide
        }
        
    }
}