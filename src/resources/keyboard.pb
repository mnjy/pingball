board name=Keyboard

# define some flippers
leftFlipper name=FlipL x=10 y=7 orientation=0 
rightFlipper name=FlipR x=12 y=7 orientation=0
rightFlipper name=FlipA x=4 y=10 orientation=0
leftFlipper name=FlipB x=15 y=3 orientation=0 

# define key commands
keydown key=space action = FlipL
keydown key=r action = FlipR
keydown key=a action = FlipA
keyup key=l action = FlipB
keydown key=3 action=FlipL

ball name=BallA x=0.25 y=5 xVelocity=0 yVelocity = 0
absorber name=Abs x=0 y=19 width=20 height =1

keydown key=space action=Abs