board name=SelfFlipping gravity = 25.0

ball name=BallEE x=0.25 y=0.25 xVelocity=0 yVelocity=0
squareBumper name=squee x=0 y=1

# define a ball
#ball name=BallA x=0.25 y=3.25 xVelocity=0 yVelocity=0
#ball name=BallB x=5.25 y=3.25 xVelocity=0 yVelocity=0 
#ball name=BallC x=10.25 y=3.25 xVelocity=0 yVelocity=0 
#ball name=BallD x=15.25 y=3.25 xVelocity=0 yVelocity=0 
#ball name=BallE x=19.25 y=3.25 xVelocity=0 yVelocity=0 

# define some left flippers
leftFlipper name=FlipA x=0 y=8 orientation=0
leftFlipper name=FlipB x=4 y=10 orientation=90 
leftFlipper name=FlipC x=9 y=8 orientation=90

# define some right flippers 
rightFlipper name=FlipD x=15 y=8 orientation=90
rightFlipper name=FlipE x=2 y=15 orientation=0
rightFlipper name=FlipF x=17 y=15 orientation=0

fire trigger=squee action=FlipA
fire trigger=squee action=FlipD

fire trigger=FlipA action=FlipA
fire trigger=FlipB action=FlipB
fire trigger=FlipC action=FlipC
fire trigger=FlipD action=FlipD
fire trigger=FlipE action=FlipE
fire trigger=FlipF action=FlipF

# define some self-triggering absorbers
absorber name=Abs x=0 y=19 width=4 height=1 
fire trigger=Abs action=Abs
absorber name=Abs1 x=4 y=19 width=4 height=1 
fire trigger=Abs1 action=Abs1
absorber name=Abs2 x=8 y=19 width=4 height=1 
fire trigger=Abs2 action=Abs2
absorber name=Abs3 x=12 y=19 width=4 height=1 
fire trigger=Abs3 action=Abs3
absorber name=Abs4 x=16 y=19 width=4 height=1 
fire trigger=Abs4 action=Abs4
