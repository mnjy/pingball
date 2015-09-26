board name=Default gravity = 25.0

# define a ball
#extra spaces
ball       name=BallA    x=1.25           y=1.25  xVelocity=0 yVelocity=0

# define a series of square bumpers
#extra tabs 
squareBumper		name=SquareA	x=0		y=17

#extra tabs and spaces 
squareBumper   				name=SquareB x=1 		y=17
squareBumper name=SquareC x=2 y=17

# define a series of circle bumpers
#tabs and spaces in front and at end and in between tokens 
  		circleBumper 	    name=CircleA     x=1 			 y=10 			
circleBumper name=CircleB x=7 y=18
circleBumper name=CircleC x=8 y=18
circleBumper name=CircleD x=9 y=18

# define a triangle bumper
triangleBumper name=Tri x=12 y=15 orientation=180