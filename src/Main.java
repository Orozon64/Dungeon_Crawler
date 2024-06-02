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
    static int Dijkstra(RoomCell start, RoomCell finish){
        ArrayList <RoomCell> Q = cells;
        ArrayList <Integer> predecessors = new ArrayList<>();
        int i = 0;
        for(RoomCell inf_setter : Q){
            if(inf_setter == start){
                inf_setter.dis = 0;
            }
            else{
                inf_setter.dis = Double.POSITIVE_INFINITY;
                predecessors.set(i, -1);
            }
            i++;
        }
        while (!Q.isEmpty()){
            RoomCell v = new RoomCell('X', -99, -99);
            int distance = 9999;
            for(RoomCell ge: Q){ //szukamy wierzchołka o najmniejszej odległości
                if(ge.dis < distance){
                    distance = (int) ge.dis;
                    v = ge;
                }
            }
            Q.remove(v);
            for(RoomEdge k : v.edges){
                RoomCell u = k.cell_to_connect;
                if(k.weight < u.dis){
                    u.dis = v.dis + k.weight;
                    u.previous = v;
                }
            }
        }
        return (int) finish.dis;
    }
    static void draw_map(){
        System.out.println("Wysokość pokoju: " + room_height);
        System.out.println("Szerokość pokoju " + room_width);
        cellnum = 1;
        for(int h = 0; h < room_height; h++){
            for(int w = 0; w < room_width; w++){

                if(dangers_x.contains(w) && dangers_y.contains(h)){
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
                else if(w == player_x && h == player_y){
                    content = 'G';
                    System.out.print(content);
                }
                else {
                    content = 'P';
                    System.out.print(content);
                }

                if(w < room_width -1){
                    System.out.print("-");
                }
                RoomCell rc = new RoomCell(content, w+1, h+1);
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
                    }

                }
            }
            System.out.println("");
            cellnum++;
        }
    }
    static void set_parameters(){
        Random rand = new Random();
        room_width = rand.nextInt(3) + 2;
        room_height = rand.nextInt(3) + 2;
        num_of_dangers = rand.nextInt(4); //Może ilość zagrożeń i skarbów powinna zależeć od rozmiaru komnaty?
        num_of_treasures = rand.nextInt(3);
        for(int d = 0; d < num_of_dangers; d++){
            int d_x = rand.nextInt(room_width);
            int d_y = rand.nextInt(room_height);
            while((dangers_x.contains(d_x) && dangers_y.contains(d_y))){
                d_x = rand.nextInt(room_width);
                d_y = rand.nextInt(room_height);
            }
            dangers_x.add(d_x);
            dangers_y.add(d_y);
        }
        for(int t = 0; t < num_of_treasures; t++){
            int t_x = rand.nextInt(room_width);
            int t_y = rand.nextInt(room_height);
            while((treasures_x.contains(t_x) && treasures_y.contains(t_y)) || (dangers_x.contains(t_x) && dangers_y.contains(t_y))){
                t_x = rand.nextInt(room_width);
                t_y = rand.nextInt(room_height);
            }
            treasures_x.add(t_x);
            treasures_y.add(t_y);
        }
        exit_x = rand.nextInt(room_width);
        exit_y = rand.nextInt(room_height);
        while((treasures_x.contains(exit_x) && treasures_y.contains(exit_y)) || (dangers_x.contains(exit_x) && dangers_y.contains(exit_y))){
            exit_x = rand.nextInt(room_width - 1) +1;
            exit_y = rand.nextInt(room_height - 1) +1;
        }
        player_x = rand.nextInt(room_width - 1) +1;
        player_y = rand.nextInt(room_width - 1) + 1;
        while((treasures_x.contains(player_x) && treasures_y.contains(player_y)) || (dangers_x.contains(player_x) && dangers_y.contains(player_y)) || (exit_x == player_x && exit_y == player_y)){
            player_x = rand.nextInt(room_width);
            player_y = rand.nextInt(room_width);
        }
    }
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner scn = new Scanner(System.in);

        int num_of_rooms = rand.nextInt(10 - 2) + 2;
        int current_room = 1;

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
                        //wejście danych od użytkownika
                        RoomCell destination = new RoomCell('x', -99, -99);
                        RoomCell player_start = new RoomCell('x', -99, -99);
                        for(RoomCell cell: cells){
                            if(cell.xcoord == user_xcoord && cell.ycoord == user_ycoord){
                                destination = cell;
                            }
                            else if(cell.content == 'G') {
                                player_start = cell;
                            }
                        }

                        int distance = Dijkstra(player_start, destination);
                        break;
                }
            }


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
