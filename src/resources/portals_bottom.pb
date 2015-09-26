board name=Portals_bottom gravity = 25.0

# define a ball
ball name=baller x=12 y=1 xVelocity=0 yVelocity=0

# defining portals
portal name=p1 x=12 y=10 otherBoard=Portals_top otherPortal=p2
portal name=p2 x=12 y=18 otherBoard=Portals_top otherPortal=p1