package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainModule{
   // public Image player=new Image("file:C:\\Users\\pasca\\Downloads\\classroom-2093744_1920.jpg");
   public Image player = new Image(getClass().getResourceAsStream("/graphics/png/SpaceShip.gif"));
    public ImageView playerView=new ImageView(player);
}
