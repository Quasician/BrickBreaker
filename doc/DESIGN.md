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


#### Describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline