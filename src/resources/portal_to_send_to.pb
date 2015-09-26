board name=portalToSendTo gravity=20.0 friction1=0.05 friction2=0.01

# define a ball
ball name=Ball5 x=19.25 y=3.25 xVelocity=0.4 yVelocity=0.0 

# define some left flippers
leftFlipper name=FlipA x=9 y=8 orientation=180
leftFlipper name=FlipB x=15 y=12 orientation=270

# define some right flippers 
rightFlipper name=FlipC x=13 y=15 orientation=180
rightFlipper name=FlipD x=18 y=15 orientation=270

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
portal name=Portal1 x=1 y=1 otherBoard=PAPA otherPortal=John
portal name=Portal2 x=0 y=15 otherPortal=Portal1
portal name=Portal3 x=15 y=15 otherPortal=Portal2
portal name=Portal4 x=3 y=0 otherBoard=Portals otherPortal=Portal2
portal name=Portal5 x=5 y=0 otherBoard=Portals otherPortal=Portal1

# define events between gizmos
fire trigger=Circle1 action=FlipA
fire trigger=Circle1 action=FlipB
fire trigger=Circle1 action=Abs2
fire trigger=Circle2 action=FlipC
fire trigger=Circle1 action=FlipD
fire trigger=Circle3 action=FlipD

fire trigger=Abs1 action=FlipA
fire trigger=Abs1 action=Abs1
fire trigger=Abs1 action=Abs2
fire trigger=Abs2 action=FlipA


