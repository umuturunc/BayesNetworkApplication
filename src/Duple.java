public class Duple implements Cloneable {
    //Duple, class ve attribute isimlerini ikili olarak tutan bir sınıf

    private String dupleString;
    private String nodeName;
    private String attributeName;


    public String getNodeName() {
        return nodeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Duple(String nodeName, String attributeName) {
        this.nodeName = nodeName;
        this.attributeName = attributeName;
        updateDupleString();
    }

    //Deep copy
    public Duple clone() {
        Duple newDuple = new Duple(this.nodeName, this.attributeName);
        return newDuple;
    }

    @Override
    public String toString() {
        return dupleString;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
        updateDupleString();
    }

    private void updateDupleString() {
        dupleString = nodeName + ":" + attributeName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Duple duple1 = (Duple) o;

        return duple1.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
