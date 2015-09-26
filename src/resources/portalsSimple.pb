board name=portals gravity = 25.0

# define a ball
ball name=baller x=3 y=1 xVelocity=0 yVelocity=0

# defining a portal
portal name=p1 x=3 y=4 otherPortal=p2
portal name=p2 x=3 y=10 otherPortal=p1
portal name=p3 x=3 y = 18 otherPortal=p1
