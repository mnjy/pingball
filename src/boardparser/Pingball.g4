grammar Pingball;

@header {
package boardparser;
}

/* Lexical rules */
FLOAT : '-'?([0-9]+'.'[0-9]*|'.'?[0-9]+) ;
NAME : [A-Za-z_][A-Za-z_0-9]* ;
EQUALS : '=' ;
COMMENT : '#' .*? [\r\n] -> skip ;
NEWLINE : [\r\n]+ ;
WS : [ \t]+ -> skip ;

/* Parser rules */
root : file EOF ;
file : board_header (NEWLINE (gadget | trigger)? )* ;
board_header : 'board' name_input? board_arguments ;
board_arguments : gravity? friction1? friction2? ;
gravity : 'gravity' EQUALS FLOAT ;
friction1 : 'friction1' EQUALS FLOAT ;
friction2 : 'friction2' EQUALS FLOAT ;

gadget : ball | squareBumper | circleBumper | triangleBumper | rightFlipper | leftFlipper | absorber | portal ;

ball : 'ball' name_input? ball_location_input ball_velocity_input ;
squareBumper : 'squareBumper' name_input? location_input ;
circleBumper : 'circleBumper' name_input? location_input ;
triangleBumper : 'triangleBumper' name_input? location_input  orientation_input? ;
rightFlipper : 'rightFlipper' name_input? location_input  orientation_input? ;
leftFlipper : 'leftFlipper' name_input? location_input  orientation_input? ;
absorber : 'absorber' name_input? location_input absorber_dimensions_input ;
portal : 'portal' name_input? location_input portal_other_board_name_input? portal_other_portal_name_input ;

trigger : 'fire' 'trigger' EQUALS NAME 'action' EQUALS NAME ;

name_input : 'name' EQUALS NAME ;
location_input : 'x' EQUALS FLOAT 'y' EQUALS FLOAT ;
orientation_input : 'orientation' EQUALS FLOAT ;

ball_location_input : 'x' EQUALS FLOAT 'y' EQUALS FLOAT ;
ball_velocity_input : 'xVelocity' EQUALS FLOAT 'yVelocity' EQUALS FLOAT ;

absorber_dimensions_input : 'width' EQUALS FLOAT 'height' EQUALS FLOAT ;

portal_other_board_name_input : 'otherBoard' EQUALS NAME ;
portal_other_portal_name_input : 'otherPortal' EQUALS NAME ;