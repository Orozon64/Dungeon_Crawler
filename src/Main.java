import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        Random rand = new Random();
        int num_of_rooms = rand.nextInt(10) + 1;
        int room_width;
        int room_height;
        int user_xcoord;
        int user_ycoord;
        int num_of_dangers;
        int num_of_treasures;
        char content;
        int exit_x;
        int exit_y;
        ArrayList<RoomCell> cells = new ArrayList<>();
        ArrayList<Integer> dangers_x = new ArrayList<>();
        ArrayList<Integer> dangers_y = new ArrayList<>();
        ArrayList<Integer> treasures_x = new ArrayList<>();
        ArrayList<Integer> treasures_y = new ArrayList<>();
        System.out.println("Legenda: P = puste pole, Z = zagrożenie(pułapka, potwór), S=skarb, W=wyjście, G=gracz");
        for(int n = 0; n < num_of_rooms; n++){
            room_width = rand.nextInt(8) + 2;
            room_height = rand.nextInt(8) + 2;
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
                exit_x = rand.nextInt(room_width);
                exit_y = rand.nextInt(room_height);
            }
            for(int h = 0; h < room_height; h++){
                for(int w = 0; w < room_width; w++){

                    if(num_of_dangers != 0 && dangers_x.contains(w) && dangers_y.contains(h)){
                            content = 'Z';
                            num_of_dangers--;
                            System.out.print(content);
                    }
                    else if(num_of_treasures!= 0 && treasures_x.contains(w) && treasures_y.contains(h)){

                            content = 'S';
                            num_of_treasures--;
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
                    RoomCell rc = new RoomCell(content, w+1, h+1);
                    cells.add(rc);
                }
                System.out.println("");
            }
            System.out.println("Wskaż koordynat x swojego celu");
            user_xcoord = Integer.parseInt(scn.nextLine());
            System.out.println("Wskaż koordynat y swojego celu");
            user_ycoord = Integer.parseInt(scn.nextLine());
            //wejście danych od użytkownika
            dangers_x.clear();
            dangers_y.clear();
            treasures_x.clear();
            treasures_y.clear();
        }

    }
}
