//By: Juan Ospina, Mieszko Szablinski
import javax.swing.JOptionPane;
import java.applet.Applet;
import java.applet.AudioClip;
public class OspinaSzablinskiGame
{
  private Grid grid;
  private int userRow;
  private int msElapsed;
  private int ammo;
  private int kills;
  private int userCol;
  private int cityHealth;
  private int userHealth;
  private AudioClip shot;
  private AudioClip zombieNoise;
  private AudioClip intro;
  private AudioClip win;
  private AudioClip lose;
  public OspinaSzablinskiGame()
  {
    shot = Applet.newAudioClip(getClass().getResource("gunshot.wav"));
    zombieNoise = Applet.newAudioClip(getClass().getResource("zombieSound.wav"));
    win = Applet.newAudioClip(getClass().getResource("victory.wav"));
    lose = Applet.newAudioClip(getClass().getResource("Scream.wav"));
    intro = Applet.newAudioClip(getClass().getResource("creepy.wav"));
    grid = new Grid(5, 10, "background1.gif");
    userRow = 2;
    userCol = 0;
    userHealth = 100;
    cityHealth = 1000;
    msElapsed = 0;
    ammo = 100;
    kills = 0;
    updateTitle();
    grid.setImage(new Location(userRow, userCol), "guy1.gif");
  }
  
  public void play()
  {
    intro.play();  
    JOptionPane.showMessageDialog(null, "The City is in danger and you're the only thing standing in the way of the\nzombies and the innocent people of the city. You must protect them!");
    intro.stop();
    zombieNoise.loop();
    while (!isGameOver() && !win())
    {
      grid.pause(100);
      handleKeyPress();
      if(msElapsed % 150 == 0)
          moveBullet();
      if (msElapsed % 700 == 0 && msElapsed <= 60000)
      { 
        scrollLeft();
        populateRightEdge();
      }
      else if(msElapsed % 500 == 0 && msElapsed <= 180000 && msElapsed > 60000){
          scrollLeft();
          populateRightEdge();
      }
      else if(msElapsed % 350 == 0 && msElapsed > 180000){
          scrollLeft();
          populateRightEdge();
      }
      updateTitle();
      msElapsed += 100;
    }
    zombieNoise.stop();
    if(isGameOver()){
        grid = new Grid(5, 10, "background2.gif");
        lose.play();
    }
    else if(win()){
        grid = new Grid(5, 10, "YouWon.gif");
        win.play();
    }
      
  }
   
  public void handleKeyPress()
  {
      int key = grid.checkLastKeyPressed();
      if(key == 38 && userRow != 2){
          handleCollision(new Location(userRow-1, userCol));
          grid.setImage(new Location(userRow, userCol), null);
          userRow--;
          grid.setImage(new Location(userRow, userCol), "guy1.gif");
      }
      else if(key == 40 && userRow != grid.getNumRows()-1){
          handleCollision(new Location(userRow+1, userCol));
          grid.setImage(new Location(userRow, userCol), null);
          userRow++;
          grid.setImage(new Location(userRow, userCol), "guy1.gif");
      }
      else if(key == 37 && userCol != 0){
          handleCollision(new Location(userRow, userCol-1));
          grid.setImage(new Location(userRow, userCol), null);
          userCol--;
          grid.setImage(new Location(userRow, userCol), "guy1.gif");
      }
      else if(key == 39 && userCol != 5){
          handleCollision(new Location(userRow, userCol+1));
          grid.setImage(new Location(userRow, userCol), null);
          userCol++;
          grid.setImage(new Location(userRow, userCol), "guy1.gif");
      }
      else if(key == 32){
          grid.setImage(new Location(userRow, userCol), "guy.gif");
          bullet();
          ammo--;
          shot.play();
      }
      else
          grid.setImage(new Location(userRow, userCol), "guy1.gif");
          
  }
  
  public void populateRightEdge()
  {
      int random = (int) (Math.random()*4);
      int random2 = (int) (Math.random()*10);
      int random3 = (int) (Math.random()*20);
      if(msElapsed <= 60000){
        if(random == 0)
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "ammo.gif");
        else 
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "zombie.gif");
      }
      else if(msElapsed <= 120000){
        if(random2 == 0)
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "ammo.gif");
        else 
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "zombie.gif");
      }
      else{
          if(random3 == 0)
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "ammo.gif");
        else 
            grid.setImage(new Location((int)((Math.random()*3)+2), grid.getNumCols()-1), "zombie.gif");
      }
  }
  public void bullet(){
      String image = grid.getImage(new Location(userRow, userCol+1));
      if(ammo <= 0){}
      else if(image!= null && image.equals("zombie.gif")){
          grid.setImage(new Location(userRow, userCol+1), null);
          kills++;
      }
      else if(image!= null && image.equals("ammo.gif")){
          grid.setImage(new Location(userRow, userCol+1), null);
          ammo+=5;
      }
      else if(image == null){
          grid.setImage(new Location(userRow, userCol+1), "bullet.gif");
      }
  }
  public void moveBullet(){
        for(int r = 0; r < grid.getNumRows(); r++){
            for(int c = grid.getNumCols()-1; c >= 0; c--){
                String image = grid.getImage(new Location(r,c));
                if(image != null && image.equals("bullet.gif")){
                    if(c==grid.getNumCols()-1)
                        grid.setImage(new Location(r, c), null);
                    else if(grid.getImage(new Location(r,c+1)) != null && (grid.getImage(new Location(r,c+1)).equals("ammo.gif") || grid.getImage(new Location(r,c+1)).equals("zombie.gif"))){
                        if(grid.getImage(new Location(r,c+1)).equals("zombie.gif"))
                            kills+=1;
                        else
                            ammo+=5;
                        grid.setImage(new Location(r,c+1), null);
                        grid.setImage(new Location(r, c), null);
                            
                    }
                    else{
                        grid.setImage(new Location(r, c+1), image);
                        grid.setImage(new Location(r,c), null);
                    }
                }
            }
        }
  }
  
  public void scrollLeft()
  {
      for(int r = 0; r <grid.getNumRows();r++){
          
      }
      for(int r = 0; r < grid.getNumRows(); r++){
          for(int c = 0; c < grid.getNumCols(); c++){
            String image = grid.getImage(new Location(r,c));
            if(image != null && image.equals("bullet.gif")){
            }
            else if(c == userCol+1 && r == userRow){
                handleCollision(new Location(r,c));
            }
            else if(image != null && (image.equals("guy1.gif") || image.equals("guy.gif"))){}
            else if(c == 0){
                if(image != null && image.equals("zombie.gif"))
                    cityHealth -= 10;
                grid.setImage(new Location(r, c), null);
            }
            else if(image != null && c > 0 && grid.getImage(new Location(r, c-1)) != null && grid.getImage(new Location(r, c-1)).equals("bullet.gif")){
            }
            else if(image != null){
                grid.setImage(new Location(r, c-1), image);
                grid.setImage(new Location(r,c), null);
            }
          }
      }
      
  }
  
  public void handleCollision(Location loc)
  {
      if(grid.getImage(loc) == null){
      }
      else if(grid.getImage(loc).equals("ammo.gif"))
          ammo+=5;
      else if(grid.getImage(loc).equals("zombie.gif"))
          userHealth-=5;
      grid.setImage(loc, null);
  }
  
  public int getScore()
  {
    return kills*10;
  }
  
  public void updateTitle()
  {
    grid.setTitle("Points:  " + getScore() + "     " + "Ammo: " + ammo + "      Citys Population: " + cityHealth + "     Your Health: " + userHealth);
  }
  
  public boolean isGameOver()
  {
    if(userHealth == 0 || cityHealth == 0 || msElapsed == 240000)
        return true;
    else
        return false;
  }
  public boolean win()
  {
      if(msElapsed == 240000)
          return true;
      else
          return false;
  }
  
  public static void test()
  {
    OspinaSzablinskiGame game = new OspinaSzablinskiGame();
    game.play();
  }
  
  public static void main(String[] args)
  {
    OspinaSzablinskiGame.test();
  }
}