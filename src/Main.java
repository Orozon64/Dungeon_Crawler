import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    static ArrayList<RoomCell> cells = new ArrayList<>();
    static int room_width;
    static int room_height;
    static int user_xcoord;
    static int user_ycoord;
    static int num_of_dangers;
    static int num_of_treasures;
    static char content;
    static int exit_x;
    static int exit_y;
    static int player_x;
    static int player_y;
    static ArrayList<Integer> dangers_x = new ArrayList<>();
    static ArrayList<Integer> dangers_y = new ArrayList<>();
    static ArrayList<Integer> treasures_x = new ArrayList<>();
    static ArrayList<Integer> treasures_y = new ArrayList<>();
    static int cellnum = 0;
    static ArrayList<RoomCell> Dijkstra(RoomCell start, RoomCell finish){
        ArrayList <RoomCell> Q = cells;
        ArrayList <RoomCell> path = new ArrayList<>();
        int i = 0;
        for(RoomCell inf_setter : Q){
            if(inf_setter == start){
                inf_setter.dis = 0;
            }
            else{
                inf_setter.dis = Double.POSITIVE_INFINITY;

            }
            i++;
        }
        while (!Q.isEmpty()){
            RoomCell v = Q.get(0);
            for(RoomCell ge: Q){ //szukamy wierzchołka o najmniejszej odległości
                if(ge.dis < v.dis){
                    v = ge;
                }
            }
            Q.remove(v);
            for(RoomEdge k : v.edges){
                RoomCell u = k.cell_to_connect;
                if(k.weight + v.dis < u.dis){
                    u.dis = v.dis + k.weight;
                    u.previous = v;
                }
            }
        }
        path.add(finish);
        double predecessor_distance = finish.edges.get(0).cell_to_connect.dis;
        int index;
        for (RoomEdge re: finish.edges){
            if(re.cell_to_connect.dis < predecessor_distance){
                predecessor_distance = re.cell_to_connect.dis;
                index = finish.edges.indexOf(re);
            }
        }
        RoomCell predecessor = finish.edges.get(index).cell_to_connect;
        path.add(predecessor);
        while (predecessor != start){
            for (RoomEdge re: predecessor.edges){
                if(re.cell_to_connect.dis < predecessor_distance){
                    predecessor_distance = re.cell_to_connect.dis;
                    predecessor = re.cell_to_connect;
                }
            }
            path.add(predecessor);
        }

        return path;
    }
    static void draw_map(){
        System.out.println("Wysokość pokoju: " + room_height);
        System.out.println("Szerokość pokoju " + room_width);
        cellnum = 1;
        for(int h = 0; h < room_height; h++){
            for(int w = 0; w < room_width; w++){

                if(w == player_x && h == player_y){
                    content = 'G';
                    System.out.print(content);
                }
                else if(dangers_x.contains(w) && dangers_y.contains(h)){
                    content = 'Z';
                    System.out.print(content);
                }
                else if(treasures_x.contains(w) && treasures_y.contains(h)){

                    content = 'S';
                    System.out.print(content);

                }
                else if (w == exit_x && h == exit_y) {
                    content = 'W';
                    System.out.print(content);
                }

                else {
                    content = 'P';
                    System.out.print(content);
                }

                if(w < room_width -1){
                    System.out.print("-");
                }
                RoomCell rc = new RoomCell(content, w, h);
                cells.add(rc);
                if(cellnum != 0){ //jeśli nie jesteśmy na pierwszej komórce
                    if (w != 0){
                        RoomCell connection_cell = cells.get(cellnum-1);
                        int con_w;
                        if(connection_cell.content == 'Z'){
                            con_w = 99;
                        }
                        else {
                            con_w = 1;
                        }
                        RoomEdge re = new RoomEdge(connection_cell, con_w);
                        rc.edges.add(re);
                        RoomEdge undir_re = new RoomEdge(rc, con_w);
                        connection_cell.edges.add(undir_re);
                    }
                    if(cellnum > room_width){
                        RoomCell connection_cell = cells.get(cellnum-room_width);
                        int con_w;
                        if(connection_cell.content == 'Z'){
                            con_w = 99;
                        }
                        else {
                            con_w = 1;
                        }
                        RoomEdge re = new RoomEdge(connection_cell, con_w);
                        rc.edges.add(re);
                        RoomEdge undir_re = new RoomEdge(rc, con_w);
                        connection_cell.edges.add(undir_re);
                    }

                }
            }
            System.out.println("");
            cellnum++;
        }
    }
    static void set_parameters(){

        Random rand = new Random();
        int room_dimensions_lower = 2;
        int room_dimensions_upper = 5;
        int room_dimensions_range = (room_dimensions_upper - room_dimensions_lower) + 1;
        room_width = (int)(Math.random() * room_dimensions_range) + room_dimensions_lower;
        room_height = (int)(Math.random() * room_dimensions_range) + room_dimensions_lower;
        num_of_dangers = (int)(Math.random() * 4); //Może ilość zagrożeń i skarbów powinna zależeć od rozmiaru komnaty?
        num_of_treasures = (int)(Math.random() * 3);
        for(int d = 0; d < num_of_dangers; d++){
            int d_x = (int)(Math.random() * room_width);
            int d_y = (int)(Math.random() * room_height);
            while((dangers_x.contains(d_x) && dangers_y.contains(d_y))){
                d_x = (int)(Math.random() * room_width);
                d_y = (int)(Math.random() * room_height);
            }
            dangers_x.add(d_x);
            dangers_y.add(d_y);
        }
        for(int t = 0; t < num_of_treasures; t++){
            int t_x = (int)(Math.random() * room_width);
            int t_y = (int)(Math.random() * room_height);
            while((treasures_x.contains(t_x) && treasures_y.contains(t_y)) || (dangers_x.contains(t_x) && dangers_y.contains(t_y))){
                t_x = (int)(Math.random() * room_width);
                t_y = (int)(Math.random() * room_height);
            }
            treasures_x.add(t_x);
            treasures_y.add(t_y);
        }
        exit_x = (int)(Math.random() * room_width);
        exit_y = (int)(Math.random() * room_height);
        while((treasures_x.contains(exit_x) && treasures_y.contains(exit_y)) || (dangers_x.contains(exit_x) && dangers_y.contains(exit_y))){
            exit_x = (int)(Math.random() * room_width);
            exit_y = (int)(Math.random() * room_height);
        }
        player_x = (int)(Math.random() * room_width);
        player_y = (int)(Math.random() * room_height);
        while((treasures_x.contains(player_x) && treasures_y.contains(player_y)) || (dangers_x.contains(player_x) && dangers_y.contains(player_y)) || (exit_x == player_x && exit_y == player_y)){
            player_x = (int)(Math.random() * room_width);
            player_y = (int)(Math.random() * room_height);
        }
    }
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int room_lower_bound = 3;
        int room_upper_bound = 5;
        int room_range = (room_upper_bound - room_lower_bound) + 1;
        int num_of_rooms = (int)(Math.random() * room_range) + room_lower_bound;
        int current_room = 1;
        System.out.println("Liczba pokoi w tym lochu: " + num_of_rooms);
        System.out.println("Legenda: P = puste pole, Z = zagrożenie(pułapka, potwór), S=skarb, W=wyjście, G=gracz");
        for(int n = 0; n < num_of_rooms; n++){
            set_parameters();
            draw_map();
            while (player_x != exit_x || player_y != exit_y){
                System.out.println("Chcesz poruszać się ręcznie (przy użyciu strzałek - wpisz 1) czy automatycznie dojść do danego pola (wpisz 2)?");
                int movement_mode = Integer.parseInt(scn.nextLine());
                switch (movement_mode){
                    case 1:
                        System.out.println("Naciśnij strzałki na klawiaturze numerycznej, aby poruszyć postać.");
                        int direction = Integer.parseInt(scn.nextLine());
                        switch (direction){
                            case 4:
                                player_x--;
                                break;
                            case 8:
                                player_y--;
                                break;
                            case 6:
                                player_x++;
                                break;
                            case 2:
                                player_y++;
                                break;
                            default:
                                System.out.println("Proszę wprowadzić właściwą opcję");
                                break;
                        }
                        draw_map();
                        break;
                    case 2:
                        System.out.println("Wskaż koordynat x swojego celu");
                        user_xcoord = Integer.parseInt(scn.nextLine());
                        System.out.println("Wskaż koordynat y swojego celu");
                        user_ycoord = Integer.parseInt(scn.nextLine());
                        RoomCell destination = null;
                        RoomCell player_start = null;
                        for(RoomCell cell: cells){
                            if(cell.xcoord == user_xcoord && cell.ycoord == user_ycoord){
                                destination = cell;
                            }
                            else if(cell.content == 'G') {
                                player_start = cell;
                            }
                        }

                        ArrayList<RoomCell> path = Dijkstra(player_start, destination);
                        for(int i = path.size() - 1; i >= 0; i--){
                            player_x = path.get(i).xcoord;
                            player_y = path.get(i).ycoord;
                            draw_map();
                        }
                        break;
                }
            }
            System.out.println("Dotarłeś do wyjścia!");

            dangers_x.clear();
            dangers_y.clear();
            treasures_x.clear();
            treasures_y.clear();
            cells.clear();
            current_room++;
            if(current_room > num_of_rooms){
                System.out.println("Gratulacje - to koniec lochu!");
            }
        }

    }
}
