

public class Bitpackingfactory {
    public static Bitpacking create(String type) {                                     //méthode de création d'instances de Bitpacking en fonction du type spécifié
        return switch (type.toLowerCase()) {                                           //utilisation d'un switch pour déterminer le type de Bitpacking à créer
            case "cross" -> new Bitpackingcross();                                     //création d'une instance de Bitpackingcross si le type est "cross"
            case "nocross" -> new Bitpackingnocross();                                 //création d'une instance de Bitpackingnocross si le type est "nocross"
            case "overflow" -> new Bitpackingoverflow();                               //création d'une instance de Bitpackingoverflow si le type est "overflow"
            default -> throw new IllegalArgumentException("Unknown type: " + type);    //gestion du cas où le type spécifié n'est pas reconnu
        };
    }
}

