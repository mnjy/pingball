board name=Default gravity = 25.0

#tab in front of comment
		# define a ball
#tab at end of comment and ball def:			 
ball name=BallA x=1.25 y=1.25 xVelocity=0 yVelocity=0					

#spaces in front of comment and squareBumper def:
       # define a series of square bumpers
  squareBumper name=SquareA x=0 y=17
#spaces in end of comment and def: 
squareBumper name=SquareB x=1 y=17        
squareBumper name=SquareC x=2 y=17        

#spaces and tabs in front of definitions:
 	 	 	    # define a series of circle bumpers
	 	 	circleBumper name=CircleA x=1 y=10

#Spaces and tabs in end of definitions: 
circleBumper name=CircleB x=7 y=18 	 	 		  
circleBumper name=CircleC x=8 y=18 	 	 		 	
#Both spaces and tabs in front and end of definitions: 
 	 	 	 	circleBumper name=CircleD x=9 y=18 	 	 	 

	 	 	# define a triangle bumper 		 	 	 
triangleBumper name=Tri x=12 y=15 orientation=180