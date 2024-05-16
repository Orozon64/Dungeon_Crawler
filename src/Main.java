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
        int should_be_special;
        char content = '?';
        boolean is_special = false;
        ArrayList<RoomCell> cells = new ArrayList<>();
        System.out.println("Legenda: P = puste pole, Z = zagrożenie(pułapka, potwór), S=skarb, W=wyjście, G=gracz");
        for(int n = 0; n < num_of_rooms; n++){
            room_width = rand.nextInt(8) + 2;
            room_height = rand.nextInt(8) + 2;
            num_of_dangers = rand.nextInt(4); //Może ilość zagrożeń i skarbów powinna zależeć od rozmiaru komnaty?
            num_of_treasures = rand.nextInt(3);
            for(int h = 0; h < room_height; h++){
                for(int w = 0; w < room_width; w++){

                    if(num_of_dangers != 0){
                        should_be_special = rand.nextInt(2);
                        if(should_be_special == 1){
                            content = 'Z';
                            num_of_dangers--;
                            is_special = true;
                            System.out.println(content);
                        }

                    }
                    if(num_of_treasures!= 0 && !is_special){
                        should_be_special = rand.nextInt(2);
                        if(should_be_special == 1){
                            content = 'S';
                            num_of_dangers--;
                            is_special = true;
                            System.out.println(content);
                        }

                    }
                    if(!is_special){
                        content = 'P';
                        System.out.println(content);
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

        }

    }
}