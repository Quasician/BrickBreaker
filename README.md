game
====

This project implements the game of Breakout.

Name: Thomas Chemmanoor

### Timeline

Start Date: 1/12/20

Finish Date: 1/20/20

Hours Spent: 30-40 hours

### Resources Used

Stack Overflow, Code Smell reading from class, and Jet Brains Java Code documentation guidelines


### Running the Program

Main class: GameStatusUpdate

Data files needed: 

    maxScore.txt
    level1.txt
    level2.txt
    level3.txt

Key/Mouse inputs:

The enter key transports the player to the first level.

Cheat keys:

    L adds additional lives to the player
    
    R resets the ball and paddle to starting positions
    
    1-9 takes player to that corresponding level
    
    C key (toggle) prevents the ball from being able to leave the map from the bottom. Essentially Immortality.


Known Bugs:

    Sometimes the ball collision with the spinning bricks may be a little weird. 
    Nothing game breaking, but it will sometimes go off in unusual angles.
    
Extra credit:

    Implemented a high score feature that pulls the highest score in the game's history and
    shows it to the player if they beat the game.
    
    Advanced paddle physics.

    Successfully created jet ball in that bricks spin around a central point.

### Notes/Assumptions

    I assumed that I would not be using image files since I believed that I could create 
    everything within the shapes class. By making this decision, I had my root class extend 
    the Rectangle class instead of Imageview. The reason why I extended rectangle is because most
    objects in my game (paddle, bricks, power ups) were rectangles. To have this as root class I 
    needed for ball to be included within this. I found that I could make the ball class extend 
    GameObject which extended rectangle and use the parent's constructor since one can curve the 
    corners of a rectangle to create a circle.
    
    I assumed my Level class would be general so that I would not create a method for each level
    instantiation.
    
    I also assumed that my game should work for an arbitrary (but reasonable) number of text files
    in my resource folder without changing any code. In doing this, I made my code as general as 
    possible, often making some design problems a little more complicated than normal.

### Impressions

    I liked the project going into it and I have found that I still like the project after putting 
    35+ hours into it. I am more proud of my abstraction and object oriented design rather than
    the aesthetic of my game, however I still liked coding my variant of breakout. Through this
    project I have realized the importance of good design before just jumping into coding. I am 
    eagerly looking forward to the next project.