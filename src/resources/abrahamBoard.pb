board name=Abraham gravity=20.0 friction1=0.05 friction2=0.01

# define a ball
ball name=Ball1 x=0.25 y=3.25 xVelocity=0.1 yVelocity=0.1 
ball name=Ball2 x=5.25 y=19.25 xVelocity=0.0 yVelocity=-32.0 
ball name=Ball3 x=10.25 y=10.25 xVelocity=0.8 yVelocity=0.1 
ball name=Ball4 x=15.25 y=15.25 xVelocity=-2.3 yVelocity=-2.3 
ball name=Ball5 x=19.25 y=3.25 xVelocity=0.4 yVelocity=0.0 

# define some left flippers
leftFlipper name=FlipA x=0 y=15 orientation=90 
leftFlipper name=FlipB x=4 y=10 orientation=0 
leftFlipper name=FlipC x=9 y=8 orientation=180
leftFlipper name=FlipD x=15 y=12 orientation=270

# define some right flippers 
rightFlipper name=FlipE x=2 y=15 orientation=0
rightFlipper name=FlipF x=6 y=15 orientation=90
rightFlipper name=FlipG x=13 y=15 orientation=180
rightFlipper name=FlipH x=18 y=15 orientation=270

# define some circle bumpers
circleBumper name=Circle1 x=5 y=18
circleBumper name=Circle2 x=7 y=13
circleBumper name=Circle3 x=5 y=5

# define some square bumpers
squareBumper name=Square1 x=10 y=5
squareBumper name=Square2 x=15 y=5

# define some triangle bumpers
triangleBumper name=Tri1 x=19 y=0 orientation=180
triangleBumper name=Tri2 x=10 y=18 orientation=90
triangleBumper name=Tri3 x=0 y=5 orientation=0

# define some absorbers
absorber name=Abs1 x=0 y=19 width=5 height=1 
absorber name=Abs2 x=10 y=19 width=5 height=1 

# define some portals
portal name=Portal1 x=1 y=1 otherPortal=Portal2
portal name=Portal2 x=0 y=15 otherPortal=Portal1
portal name=Portal3 x=15 y=15 otherPortal=Portal2

# define events between gizmos
fire trigger=Circle1 action=FlipA
fire trigger=Circle1 action=FlipB
fire trigger=Circle1 action=FlipC
fire trigger=Circle2 action=FlipC
fire trigger=Circle1 action=FlipD
fire trigger=Circle3 action=FlipD
fire trigger=Circle1 action=FlipE
fire trigger=Abs1 action=FlipE
fire trigger=Abs1 action=FlipF
fire trigger=Abs1 action=Abs1
fire trigger=Abs1 action=Abs2
fire trigger=Abs2 action=FlipA


