import java.util.ArrayList;

public class Query implements Cloneable {
    //Her sorguda "Node ismi:Attribute ismi" şeklinde ikili olarak tutulan duple nesneleri var
    ArrayList<Duple> list;

    public Query() {
        list = new ArrayList<>();
    }

    public void addDuple(String nodeName, String attributeName) {
        Duple duple = new Duple(nodeName, attributeName);
        list.add(duple);
    }

    public void addDuple(Duple duple) {
        list.add(duple);
    }

    public ArrayList<Duple> getDuples() {
        return list;
    }

    public Duple getDupleByNodeName(String name) {
        Duple duple = null;
        for (Duple d : list) {
            if (d.getNodeName().equals(name)) {
                duple = d;
                break;
            }
        }
        return duple;
    }

    public Query clone() {
        Query newQuery = new Query();
        this.getDuples().forEach(duple -> newQuery.addDuple(duple.clone()));
        return newQuery;
    }

    public boolean containsDuple(Duple duple) {
        return list.contains(duple);
    }

    @Override
    public String toString() {
        String str = "";
        for (Duple duple : list) {
            str += duple + ",";
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query = (Query) o;
        boolean flag = true;
        for (Duple duple : query.getDuples()) {
            if (this.getDuples().contains(duple) == false) {
                flag = false;
                break;
            }
        }
        return flag;
//        return query.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
