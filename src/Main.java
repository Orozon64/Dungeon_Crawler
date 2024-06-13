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

    static int monster_x;

    static int monster_y;

    static ArrayList<RoomCell> dangers = new ArrayList<>();
    static ArrayList<Integer> dangers_x = new ArrayList<>();
    static ArrayList<Integer> dangers_y = new ArrayList<>();
    static ArrayList<Integer> treasures_x = new ArrayList<>();
    static ArrayList<Integer> treasures_y = new ArrayList<>();
    static int cellnum = 0;
    static ArrayList<RoomCell> Dijkstra(RoomCell start, RoomCell finish){
        ArrayList <RoomCell> Q = cells;
        ArrayList <RoomCell> path = new ArrayList<>();
        if(start != finish){


            for(RoomCell inf_setter : Q){
                if(inf_setter == start){
                    inf_setter.dis = 0;
                }
                else{
                    inf_setter.dis = Double.POSITIVE_INFINITY;

                }

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
            int index = 0;
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
        }


        return path;
    }
    static void draw_map(){
        if(!cells.isEmpty()){
            RoomCell player_location = null;
            RoomCell monster_location = null;
            for(RoomCell cell: cells){
                if(cell.content == 'B'){
                    monster_location = cell;
                }
                else if(cell.content == 'G') {
                    player_location = cell;
                }
            }

            ArrayList<RoomCell> monster_path =  Dijkstra(monster_location, player_location);
            RoomCell step_one = monster_path.get(monster_path.size() -1);
            monster_x = step_one.xcoord;
            monster_y = step_one.ycoord;
        }

        cellnum = 0;
        for(int h = 0; h < room_height; h++){
            for(int w = 0; w < room_width; w++){
                RoomCell current_cell = new RoomCell(w, h);
                if(w == player_x && h == player_y){
                    content = 'G';
                    System.out.print(content);
                }
                else if (w == monster_x && h == player_y) {
                    content = 'B';
                    System.out.print(content);
                } else if(dangers.contains(current_cell)){ //sprawdzamy czy lista "dangers" zawiera komórkę o takich x i y
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
                    if(h!=0){
                        RoomCell connection_cell = cells.get(cellnum-room_width);
                        double con_w;
                        if(connection_cell.content == 'Z'){
                            con_w = 99;
                        }
                        else if (connection_cell.content == 'B') {
                            con_w = Double.POSITIVE_INFINITY;
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
                cellnum++;
            }
            System.out.println();

        }


    }
    static void set_parameters(){
        System.out.println("Ładowanie...");
        int room_dimensions_lower = 10;
        int room_dimensions_upper = 15;
        int room_dimensions_range = (room_dimensions_upper - room_dimensions_lower) + 1;
        room_width = (int)(Math.random() * room_dimensions_range) + room_dimensions_lower;
        room_height = (int)(Math.random() * room_dimensions_range) + room_dimensions_lower;
        num_of_dangers = (int)(Math.random() * 50);
        num_of_treasures = (int)(Math.random() * 3);
        ArrayList<RoomCell> possible_coords = new ArrayList<>();
        for(int h = 0; h < room_height; h++){
            for (int w = 0; w < room_width; w++){
                RoomCell rc = new RoomCell(w, h);
                possible_coords.add(rc);
            }
        }
        for(int d = 0; d < num_of_dangers; d++){

            int cell_index = (int)(Math.random() * possible_coords.size());
            RoomCell dc = new RoomCell(possible_coords.get(cell_index).xcoord, possible_coords.get(cell_index).ycoord);
            dangers.add(dc);
            dangers_x.add(possible_coords.get(cell_index).xcoord);
            dangers_y.add(possible_coords.get(cell_index).ycoord);
            possible_coords.remove(cell_index);
        }
        for(int t = 0; t < num_of_treasures; t++){
            int cell_index = (int)(Math.random() * possible_coords.size());
            RoomCell tc = new RoomCell(possible_coords.get(cell_index).xcoord, possible_coords.get(cell_index).ycoord);
            treasures_x.add(possible_coords.get(cell_index).xcoord);
            treasures_y.add(possible_coords.get(cell_index).ycoord);
            possible_coords.remove(cell_index);
        }
        int cell_index_exit = (int)(Math.random() * possible_coords.size());
        RoomCell ec = new RoomCell(possible_coords.get(cell_index_exit).xcoord, possible_coords.get(cell_index_exit).ycoord);
        exit_x = possible_coords.get(cell_index_exit).xcoord;
        exit_y = possible_coords.get(cell_index_exit).ycoord;
        possible_coords.remove(cell_index_exit);

        int cell_index_player = (int)(Math.random() * possible_coords.size());
        RoomCell pc = new RoomCell(possible_coords.get(cell_index_player).xcoord, possible_coords.get(cell_index_player).ycoord);
        player_x = possible_coords.get(cell_index_player).xcoord;
        player_y = possible_coords.get(cell_index_player).ycoord;
        possible_coords.remove(cell_index_player);

        int cell_index_monster = (int)(Math.random() * possible_coords.size());
        RoomCell mc = new RoomCell(possible_coords.get(cell_index_monster).xcoord, possible_coords.get(cell_index_monster).ycoord);
        monster_x = possible_coords.get(cell_index_monster).xcoord;
        monster_y = possible_coords.get(cell_index_monster).ycoord;

        System.out.println("Wysokość pokoju: " + room_height);
        System.out.println("Szerokość pokoju " + room_width);
    }
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int room_lower_bound = 3;
        int room_upper_bound = 5;
        int room_range = (room_upper_bound - room_lower_bound) + 1;
        int num_of_rooms = (int)(Math.random() * room_range) + room_lower_bound;
        int current_room = 1;
        System.out.println("Liczba pokoi w tym lochu: " + num_of_rooms);
        System.out.println("Legenda: P = puste pole, Z = zagrożenie, S=skarb, W=wyjście, G=gracz, B=Bestia");
        for(int n = 0; n < num_of_rooms; n++){
            set_parameters();
            draw_map();

            while (player_x != exit_x || player_y != exit_y){
                System.out.println("Chcesz poruszać się ręcznie (przy użyciu strzałek - wpisz 1) czy automatycznie dojść do danego pola (wpisz 2)?");
                int movement_mode = Integer.parseInt(scn.nextLine());
                if(player_y == monster_y && player_x == monster_x){
                    System.out.println("Bestia cię dorwała - koniec gry!");
                    System.exit(0);
                }
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
                        boolean targets_self = false;

                        do{
                            System.out.println("Wskaż koordynat x swojego celu");
                            user_xcoord = Integer.parseInt(scn.nextLine());
                        }while(user_xcoord >= room_width || user_xcoord < 0);

                        do{
                            System.out.println("Wskaż koordynat y swojego celu");
                            user_ycoord = Integer.parseInt(scn.nextLine());
                        }while(user_ycoord >= room_height || user_ycoord < 0);

                        RoomCell destination = null;
                        RoomCell player_start = null;
                        for(RoomCell cell: cells){
                            if(cell.xcoord == user_xcoord && cell.ycoord == user_ycoord){
                                destination = cell;
                                if(cell.content == 'G'){
                                    targets_self = true;
                                }
                            }
                            else if(cell.content == 'G') {
                                player_start = cell;
                            }
                        }
                        if(!targets_self){
                            ArrayList<RoomCell> path = Dijkstra(player_start, destination);
                            for(int i = path.size() - 1; i >= 0; i--){
                                player_x = path.get(i).xcoord;
                                player_y = path.get(i).ycoord;
                                draw_map();
                            }
                        }
                        else {
                            System.out.println("Już jesteś na tych koordynatach");
                            draw_map();
                        }

                        break;
                    default:
                        System.out.println("Podaj właściwą opcję");
                        draw_map();
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
            else{
                System.out.println("Wchodzisz do pokoju nr " + current_room);
            }
        }

    }
}
