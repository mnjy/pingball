board name=Portals_top gravity = 25.0

# define a ball
ball name=baller x=3 y=1 xVelocity=0 yVelocity=0

# defining portals
portal name=p1 x=3 y=4 otherBoard=Portals_bottom otherPortal=p2
portal name=p2 x=3 y=15 otherBoard=Portals_bottom otherPortal=p1