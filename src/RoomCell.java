import java.util.ArrayList;
public class RoomCell {
    char content;
    int xcoord;
    int ycoord;
    ArrayList<RoomEdge> edges = new ArrayList<>();
    RoomCell(char c, int x, int y){
        content = c;
        xcoord = x;
        ycoord = y;
    }
}