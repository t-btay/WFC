/**
 * Reef object can store the properties of a generated reef.
 * Making this into an object opens the possibility for customization of the reef properites going forward... might add recursion which actually generates the reef to this object.
 */
public class Reef {

    private int size;

    public Reef(int size){

        this.size = size;

    }

    public int getSize(){return this.size;}

    public void setSize(int size){this.size = size;}

    public void incrSize(){this.size += 1;}

}
