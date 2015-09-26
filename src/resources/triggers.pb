board name=Triggers gravity = 10.0

# define a ball
ball name=BallA x=1.8 y=4.5 xVelocity=10.4 yVelocity=10.3 
ball name=BallB x=10.0 y=13.0 xVelocity=-3.4 yVelocity=-2.3 

# define some bumpers
squareBumper name=Square x=0 y=10
squareBumper name=SquareB x=1 y=10
squareBumper name=SquareC x=2 y=10
squareBumper name=SquareD x=3 y=10
squareBumper name=SquareE x=4 y=10
squareBumper name=SquareF x=5 y=10
squareBumper name=SquareG x=6 y=10
squareBumper name=SquareH x=7 y=10

circleBumper name=Circle x=4 y=3
triangleBumper name=Tri x=19 y=3 orientation=90

# define some flippers
  leftFlipper name=FlipL x=10 y=7 orientation=0 
rightFlipper name=FlipR x=12 y=7 orientation=0

keydown key = r action = FlipR
keydown key = l action = FlipL


# define an absorber to catch the ball
 absorber name=Abs x=10 y=17 width=10 height=2 

# define events between gizmos
fire trigger=Square action=FlipR
fire trigger=SquareB action=FlipR
fire trigger=SquareC action=FlipR
fire trigger=SquareD action=FlipR
fire trigger=SquareE action=FlipR
fire trigger=SquareF action=FlipR
fire trigger=SquareG action=FlipR
fire trigger=SquareH action=FlipR

# make the absorber self-triggering
 fire trigger=Abs action=Abs 