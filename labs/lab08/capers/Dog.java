package capers;

import java.io.File;
import java.io.Serializable;

/** Represents a dog that can be serialized.
 * @author Sean Dooher
*/
public class Dog { // FIXME

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = new File(".capers/dogs"); // FIXME

    static File DOG_FILE = new File(".capers/dogs/dog");

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        _age = age;
        _breed = breed;
        _name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        // FIXME
        String file_string = Utils.readContentsAsString(DOG_FILE);
        String[] search = file_string.split(" ");
        for (int i = 0; i < search.length; i++){
//            System.out.println(search[i]);
//            System.out.println(search.getClass());
            if (search[i].equals(name)){
                Dog dog = new Dog(search[i], search[i+1], Integer.parseInt(search[i+2]));
                return dog;
            }
        }
        return null;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        _age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        // FIXME
        boolean isSaved = false;
        String age_string = Integer.toString(_age);
        String now_dogs = Utils.readContentsAsString(DOG_FILE);
        String new_dogs = "";
        String[] info_split = now_dogs.split(" ");
        for (int i = 0; i < info_split.length; i++){
            if(info_split[i].equals(_name)){
                isSaved = true;
                info_split[i+2] = age_string;
                for (int j = 0; j < info_split.length; j++){
                    new_dogs = new_dogs + info_split[j] + " ";
//                    System.out.println(new_dogs);
                }
//                System.out.println("changed" + new_dogs);
                break;
            }
        }
        if (isSaved == false){
                new_dogs = now_dogs + " " + _name + " " + _breed + " " + age_string + " ";
//                System.out.println("||" + new_dogs);
        }
        Utils.writeContents(DOG_FILE, new_dogs);
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            _name, _breed, _age);
    }

    /** Age of dog. */
    private int _age;
    /** Breed of dog. */
    private String _breed;
    /** Name of dog. */
    private String _name;
}
