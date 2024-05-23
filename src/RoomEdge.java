public class RoomEdge {
    RoomCell cell_to_connect;
    int weight;
    RoomEdge(RoomCell c, int w){
        cell_to_connect = c;
        weight = w;
    }
}
