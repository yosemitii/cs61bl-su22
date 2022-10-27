/** A class that represents a path via pursuit curves. */
public class Path {

    // TODO
    private Point curr;
    private Point next;

    public Path(double x, double y) {
        this.curr = new Point(x, y);
        this.next = new Point(x, y);
        // more code goes here!
    }

    /**
     * get methods
     */
    public double getCurrX(){
        double x = this.curr.getX();
        return x;
    }
    public double getCurrY(){
        double y = this.curr.getY();
        return y;
    }

    public double getNextX(){
        double x = this.next.getX();
        return x;
    }

    public double getNextY(){
        double y = this.next.getY();
        return y;
    }

    public Point getCurrentPoint(){
        Point p = this.curr;
        return p;
    }
    public void setCurrentPoint(Point point){
        this.curr = point;
    }

    public void iterate(double dx, double dy){
        setCurrentPoint(this.next);
        next = new Point(curr.getX()+dx, curr.getY()+dy);
    }


}
