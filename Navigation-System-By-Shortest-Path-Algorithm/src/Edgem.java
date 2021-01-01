//Edge class for Adjacency Matrix graph representation
class Edgem implements Edge {
    private int vert1, vert2;
    private  double weight;// the vertex indices
    public Edgem (int vt1, int vt2,double we) {vert1 = vt1; vert2 = vt2; weight = we;}
    public Edgem (char vt1, char vt2,double we) {vert1 = (int) vt1 - 65; vert2 = (int) vt2 - 65; weight = we;}
    public Edgem (char vt1, char vt2) {vert1 = (int) vt1 - 65; vert2 = (int) vt2 - 65;}
    public Edgem (int vt1, int vt2) {vert1 = vt1; vert2 = vt2;}

    public int v1() {return vert1;}
    public int v2() {return vert2;}
    public double we() {return weight;}

}

