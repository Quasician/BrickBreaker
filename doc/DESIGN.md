#### NAME:
Thomas Chemmanoor

#### ROLE:
My role was to create all the features of the game. I. Did. All of it.

#### What are the project's design goals, specifically what kinds of new features did you want to make easy to add
The projects design goals were to create a breakout game. The user would need to control a paddle to hit a ball at bricks to clear the level.
After all levels were cleared the user would get a win screen. I f they lost all their lives, they would get a lose screen.
The game would need cheat keys and ball bouncing physics. The game would need to read levels in from a text file.
The game required multiple types of bricks and differnt powerups.
I wanted the creator to have an easy time adding new levels and new cheat keys.




#### Describe the high-level design of your project, focusing on the purpose and interaction of the core classes
The core classes are GameStatusUpdate which is the runner class, the Level class, and the GameObject class.

GameStatusUpdate is where the application is launched, levels are created and run, the step function that checks
interactions between GameObjects and other functions is implemented, and where the application handles key presses.

The Level class has a constructor to create a new level (by adding all pertinent bricks from a levelInfo object into the current group) as well
as a method to spawn x number of bricks in an equally spaced circular pattern on the screen away from a central point.

The GameObject class is an abstract class that is extend by every object in the game. It extends Rectangle 
and has its own constructor. Each GameObject recieves an hp, an x & y coordinate, a width & height, and a color.

The Brick, Paddle, Ball, and PowerUp classes extend GameObject and have their own respective methods to access and modify their own data
elements.

The LevelInfo and RingInfo classes main purpose is to store information read from a level text file instead of passing in
8 arguments into the Level's classes brick spawner method. This was also done so that the game could first create an array of 
LevelInfo objects that would represent the entire game. If the user needs to go to a certain level, the game will index
into the LevelInfo array and draw that particular scene.


#### What assumptions or decisions were made to simplify your project's design, especially those that affected adding required features
I assumed that the creator would only be creating levels that had bricks rotating in circles around a central point. As it is right now,
it would be very hard to to create a static brick array with the methods I'm using to read from the file that spawn concentric 
circles of bricks.

I assumed that if the creator wanted to add more cheat keys to the game, they would just create a new method to handle the new key press
inside GameStatusUpdate instead of generalizing the cheat key into a cheat key class.

I decided that all objects in the game would become a sub-class of GameObject. This made coding much easier and more organized as well.

I decided that my level class would be generalized for all levels instead of having different methods for different levels.

I decided that my game should automatically work if the user adds or deletes levels to the resource folder without any code needing to 
be changed. 

I decided to make GameObject extend Rectangle. By doing this, I didn't use any imageviews (I thought they were too much of a hassle to work with)
and just used rectangles. Since I didn't use imageviews, updating the brick became much easier since I wasn't setting a new image
everytime the ball connected just changing the brick color. Having all game objects extend Rectangle was a problem since even ball 
had to extend rectangle, but this problem was solved once I realized that you could curve the corners of a rectangle to make a circle 
in JavaFx.

I decided to move the paddle with the arrow keys instead of the mouse since I did not want to write cursor code that would work across 
the different screen sizes of people's computers.

I decided not to create multiple versions of bricks and have them extend a brick class. I made the same decision with powerups,
thinking that 2/3 if statements will be fine. Later we learned this was bad coding practice and should be changed so that bricks/power ups
with different behaviors should extend a common abstract class.


#### Describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline

Adding a new level:
    Just add a new file inside of the resources folder, everything else is automatic

Adding a cheat key:
    Create a new method inside GameStatusUpdate to handle the new key press and call it inside of handleKeyInput()

Adding a new PowerUp:
    Adding a new Boolean inside of the PowerUp class and changing the array bounds. You would also need to implement what the power Up
    does in updateGamePowerUpActivation.
    
Adding a new Brick:
    Add if statement inside of updateColor in the Brick class.
    
Adding different text to any win/lose/splash screen:
    Find were the pertinent string is located and change it.
    
