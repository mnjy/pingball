// Generated from src\boardparser\Pingball.g4 by ANTLR 4.4

package boardparser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PingballParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__24=1, T__23=2, T__22=3, T__21=4, T__20=5, T__19=6, T__18=7, T__17=8, 
		T__16=9, T__15=10, T__14=11, T__13=12, T__12=13, T__11=14, T__10=15, T__9=16, 
		T__8=17, T__7=18, T__6=19, T__5=20, T__4=21, T__3=22, T__2=23, T__1=24, 
		T__0=25, FLOAT=26, NAME=27, EQUALS=28, COMMENT=29, NEWLINE=30, WS=31;
	public static final String[] tokenNames = {
		"<INVALID>", "'board'", "'otherBoard'", "'height'", "'xVelocity'", "'name'", 
		"'y'", "'friction2'", "'fire'", "'squareBumper'", "'action'", "'otherPortal'", 
		"'orientation'", "'ball'", "'portal'", "'absorber'", "'x'", "'rightFlipper'", 
		"'width'", "'trigger'", "'friction1'", "'circleBumper'", "'yVelocity'", 
		"'triangleBumper'", "'leftFlipper'", "'gravity'", "FLOAT", "NAME", "'='", 
		"COMMENT", "NEWLINE", "WS"
	};
	public static final int
		RULE_root = 0, RULE_file = 1, RULE_board_header = 2, RULE_board_arguments = 3, 
		RULE_gravity = 4, RULE_friction1 = 5, RULE_friction2 = 6, RULE_gadget = 7, 
		RULE_ball = 8, RULE_squareBumper = 9, RULE_circleBumper = 10, RULE_triangleBumper = 11, 
		RULE_rightFlipper = 12, RULE_leftFlipper = 13, RULE_absorber = 14, RULE_portal = 15, 
		RULE_trigger = 16, RULE_name_input = 17, RULE_location_input = 18, RULE_orientation_input = 19, 
		RULE_ball_location_input = 20, RULE_ball_velocity_input = 21, RULE_absorber_dimensions_input = 22, 
		RULE_portal_other_board_name_input = 23, RULE_portal_other_portal_name_input = 24;
	public static final String[] ruleNames = {
		"root", "file", "board_header", "board_arguments", "gravity", "friction1", 
		"friction2", "gadget", "ball", "squareBumper", "circleBumper", "triangleBumper", 
		"rightFlipper", "leftFlipper", "absorber", "portal", "trigger", "name_input", 
		"location_input", "orientation_input", "ball_location_input", "ball_velocity_input", 
		"absorber_dimensions_input", "portal_other_board_name_input", "portal_other_portal_name_input"
	};

	@Override
	public String getGrammarFileName() { return "Pingball.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PingballParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PingballParser.EOF, 0); }
		public FileContext file() {
			return getRuleContext(FileContext.class,0);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50); file();
			setState(51); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FileContext extends ParserRuleContext {
		public List<GadgetContext> gadget() {
			return getRuleContexts(GadgetContext.class);
		}
		public GadgetContext gadget(int i) {
			return getRuleContext(GadgetContext.class,i);
		}
		public Board_headerContext board_header() {
			return getRuleContext(Board_headerContext.class,0);
		}
		public TriggerContext trigger(int i) {
			return getRuleContext(TriggerContext.class,i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(PingballParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(PingballParser.NEWLINE, i);
		}
		public List<TriggerContext> trigger() {
			return getRuleContexts(TriggerContext.class);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53); board_header();
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(54); match(NEWLINE);
				setState(57);
				switch (_input.LA(1)) {
				case T__16:
				case T__12:
				case T__11:
				case T__10:
				case T__8:
				case T__4:
				case T__2:
				case T__1:
					{
					setState(55); gadget();
					}
					break;
				case T__17:
					{
					setState(56); trigger();
					}
					break;
				case EOF:
				case NEWLINE:
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Board_headerContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Board_argumentsContext board_arguments() {
			return getRuleContext(Board_argumentsContext.class,0);
		}
		public Board_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_board_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterBoard_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitBoard_header(this);
		}
	}

	public final Board_headerContext board_header() throws RecognitionException {
		Board_headerContext _localctx = new Board_headerContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_board_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64); match(T__24);
			setState(66);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(65); name_input();
				}
			}

			setState(68); board_arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Board_argumentsContext extends ParserRuleContext {
		public Friction1Context friction1() {
			return getRuleContext(Friction1Context.class,0);
		}
		public Friction2Context friction2() {
			return getRuleContext(Friction2Context.class,0);
		}
		public GravityContext gravity() {
			return getRuleContext(GravityContext.class,0);
		}
		public Board_argumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_board_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterBoard_arguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitBoard_arguments(this);
		}
	}

	public final Board_argumentsContext board_arguments() throws RecognitionException {
		Board_argumentsContext _localctx = new Board_argumentsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_board_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(70); gravity();
				}
			}

			setState(74);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(73); friction1();
				}
			}

			setState(77);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(76); friction2();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GravityContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode FLOAT() { return getToken(PingballParser.FLOAT, 0); }
		public GravityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gravity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterGravity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitGravity(this);
		}
	}

	public final GravityContext gravity() throws RecognitionException {
		GravityContext _localctx = new GravityContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_gravity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79); match(T__0);
			setState(80); match(EQUALS);
			setState(81); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Friction1Context extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode FLOAT() { return getToken(PingballParser.FLOAT, 0); }
		public Friction1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_friction1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterFriction1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitFriction1(this);
		}
	}

	public final Friction1Context friction1() throws RecognitionException {
		Friction1Context _localctx = new Friction1Context(_ctx, getState());
		enterRule(_localctx, 10, RULE_friction1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83); match(T__5);
			setState(84); match(EQUALS);
			setState(85); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Friction2Context extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode FLOAT() { return getToken(PingballParser.FLOAT, 0); }
		public Friction2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_friction2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterFriction2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitFriction2(this);
		}
	}

	public final Friction2Context friction2() throws RecognitionException {
		Friction2Context _localctx = new Friction2Context(_ctx, getState());
		enterRule(_localctx, 12, RULE_friction2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); match(T__18);
			setState(88); match(EQUALS);
			setState(89); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GadgetContext extends ParserRuleContext {
		public LeftFlipperContext leftFlipper() {
			return getRuleContext(LeftFlipperContext.class,0);
		}
		public TriangleBumperContext triangleBumper() {
			return getRuleContext(TriangleBumperContext.class,0);
		}
		public AbsorberContext absorber() {
			return getRuleContext(AbsorberContext.class,0);
		}
		public BallContext ball() {
			return getRuleContext(BallContext.class,0);
		}
		public RightFlipperContext rightFlipper() {
			return getRuleContext(RightFlipperContext.class,0);
		}
		public SquareBumperContext squareBumper() {
			return getRuleContext(SquareBumperContext.class,0);
		}
		public PortalContext portal() {
			return getRuleContext(PortalContext.class,0);
		}
		public CircleBumperContext circleBumper() {
			return getRuleContext(CircleBumperContext.class,0);
		}
		public GadgetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gadget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterGadget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitGadget(this);
		}
	}

	public final GadgetContext gadget() throws RecognitionException {
		GadgetContext _localctx = new GadgetContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_gadget);
		try {
			setState(99);
			switch (_input.LA(1)) {
			case T__12:
				enterOuterAlt(_localctx, 1);
				{
				setState(91); ball();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 2);
				{
				setState(92); squareBumper();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(93); circleBumper();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 4);
				{
				setState(94); triangleBumper();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(95); rightFlipper();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 6);
				{
				setState(96); leftFlipper();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 7);
				{
				setState(97); absorber();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 8);
				{
				setState(98); portal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BallContext extends ParserRuleContext {
		public Ball_location_inputContext ball_location_input() {
			return getRuleContext(Ball_location_inputContext.class,0);
		}
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Ball_velocity_inputContext ball_velocity_input() {
			return getRuleContext(Ball_velocity_inputContext.class,0);
		}
		public BallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ball; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterBall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitBall(this);
		}
	}

	public final BallContext ball() throws RecognitionException {
		BallContext _localctx = new BallContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_ball);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101); match(T__12);
			setState(103);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(102); name_input();
				}
			}

			setState(105); ball_location_input();
			setState(106); ball_velocity_input();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SquareBumperContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public SquareBumperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_squareBumper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterSquareBumper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitSquareBumper(this);
		}
	}

	public final SquareBumperContext squareBumper() throws RecognitionException {
		SquareBumperContext _localctx = new SquareBumperContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_squareBumper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108); match(T__16);
			setState(110);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(109); name_input();
				}
			}

			setState(112); location_input();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CircleBumperContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public CircleBumperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circleBumper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterCircleBumper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitCircleBumper(this);
		}
	}

	public final CircleBumperContext circleBumper() throws RecognitionException {
		CircleBumperContext _localctx = new CircleBumperContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_circleBumper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114); match(T__4);
			setState(116);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(115); name_input();
				}
			}

			setState(118); location_input();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriangleBumperContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Orientation_inputContext orientation_input() {
			return getRuleContext(Orientation_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public TriangleBumperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triangleBumper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterTriangleBumper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitTriangleBumper(this);
		}
	}

	public final TriangleBumperContext triangleBumper() throws RecognitionException {
		TriangleBumperContext _localctx = new TriangleBumperContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_triangleBumper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120); match(T__2);
			setState(122);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(121); name_input();
				}
			}

			setState(124); location_input();
			setState(126);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(125); orientation_input();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RightFlipperContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Orientation_inputContext orientation_input() {
			return getRuleContext(Orientation_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public RightFlipperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rightFlipper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterRightFlipper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitRightFlipper(this);
		}
	}

	public final RightFlipperContext rightFlipper() throws RecognitionException {
		RightFlipperContext _localctx = new RightFlipperContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_rightFlipper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); match(T__8);
			setState(130);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(129); name_input();
				}
			}

			setState(132); location_input();
			setState(134);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(133); orientation_input();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeftFlipperContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Orientation_inputContext orientation_input() {
			return getRuleContext(Orientation_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public LeftFlipperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leftFlipper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterLeftFlipper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitLeftFlipper(this);
		}
	}

	public final LeftFlipperContext leftFlipper() throws RecognitionException {
		LeftFlipperContext _localctx = new LeftFlipperContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_leftFlipper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136); match(T__1);
			setState(138);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(137); name_input();
				}
			}

			setState(140); location_input();
			setState(142);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(141); orientation_input();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbsorberContext extends ParserRuleContext {
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public Absorber_dimensions_inputContext absorber_dimensions_input() {
			return getRuleContext(Absorber_dimensions_inputContext.class,0);
		}
		public AbsorberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absorber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterAbsorber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitAbsorber(this);
		}
	}

	public final AbsorberContext absorber() throws RecognitionException {
		AbsorberContext _localctx = new AbsorberContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_absorber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144); match(T__10);
			setState(146);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(145); name_input();
				}
			}

			setState(148); location_input();
			setState(149); absorber_dimensions_input();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PortalContext extends ParserRuleContext {
		public Portal_other_portal_name_inputContext portal_other_portal_name_input() {
			return getRuleContext(Portal_other_portal_name_inputContext.class,0);
		}
		public Name_inputContext name_input() {
			return getRuleContext(Name_inputContext.class,0);
		}
		public Portal_other_board_name_inputContext portal_other_board_name_input() {
			return getRuleContext(Portal_other_board_name_inputContext.class,0);
		}
		public Location_inputContext location_input() {
			return getRuleContext(Location_inputContext.class,0);
		}
		public PortalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterPortal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitPortal(this);
		}
	}

	public final PortalContext portal() throws RecognitionException {
		PortalContext _localctx = new PortalContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_portal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151); match(T__11);
			setState(153);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(152); name_input();
				}
			}

			setState(155); location_input();
			setState(157);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(156); portal_other_board_name_input();
				}
			}

			setState(159); portal_other_portal_name_input();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(PingballParser.NAME, i);
		}
		public List<TerminalNode> EQUALS() { return getTokens(PingballParser.EQUALS); }
		public List<TerminalNode> NAME() { return getTokens(PingballParser.NAME); }
		public TerminalNode EQUALS(int i) {
			return getToken(PingballParser.EQUALS, i);
		}
		public TriggerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trigger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterTrigger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitTrigger(this);
		}
	}

	public final TriggerContext trigger() throws RecognitionException {
		TriggerContext _localctx = new TriggerContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161); match(T__17);
			setState(162); match(T__6);
			setState(163); match(EQUALS);
			setState(164); match(NAME);
			setState(165); match(T__15);
			setState(166); match(EQUALS);
			setState(167); match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Name_inputContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode NAME() { return getToken(PingballParser.NAME, 0); }
		public Name_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterName_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitName_input(this);
		}
	}

	public final Name_inputContext name_input() throws RecognitionException {
		Name_inputContext _localctx = new Name_inputContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_name_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169); match(T__20);
			setState(170); match(EQUALS);
			setState(171); match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Location_inputContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(PingballParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(PingballParser.EQUALS, i);
		}
		public List<TerminalNode> FLOAT() { return getTokens(PingballParser.FLOAT); }
		public TerminalNode FLOAT(int i) {
			return getToken(PingballParser.FLOAT, i);
		}
		public Location_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_location_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterLocation_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitLocation_input(this);
		}
	}

	public final Location_inputContext location_input() throws RecognitionException {
		Location_inputContext _localctx = new Location_inputContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_location_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173); match(T__9);
			setState(174); match(EQUALS);
			setState(175); match(FLOAT);
			setState(176); match(T__19);
			setState(177); match(EQUALS);
			setState(178); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Orientation_inputContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode FLOAT() { return getToken(PingballParser.FLOAT, 0); }
		public Orientation_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orientation_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterOrientation_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitOrientation_input(this);
		}
	}

	public final Orientation_inputContext orientation_input() throws RecognitionException {
		Orientation_inputContext _localctx = new Orientation_inputContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_orientation_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180); match(T__13);
			setState(181); match(EQUALS);
			setState(182); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ball_location_inputContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(PingballParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(PingballParser.EQUALS, i);
		}
		public List<TerminalNode> FLOAT() { return getTokens(PingballParser.FLOAT); }
		public TerminalNode FLOAT(int i) {
			return getToken(PingballParser.FLOAT, i);
		}
		public Ball_location_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ball_location_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterBall_location_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitBall_location_input(this);
		}
	}

	public final Ball_location_inputContext ball_location_input() throws RecognitionException {
		Ball_location_inputContext _localctx = new Ball_location_inputContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_ball_location_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); match(T__9);
			setState(185); match(EQUALS);
			setState(186); match(FLOAT);
			setState(187); match(T__19);
			setState(188); match(EQUALS);
			setState(189); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ball_velocity_inputContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(PingballParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(PingballParser.EQUALS, i);
		}
		public List<TerminalNode> FLOAT() { return getTokens(PingballParser.FLOAT); }
		public TerminalNode FLOAT(int i) {
			return getToken(PingballParser.FLOAT, i);
		}
		public Ball_velocity_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ball_velocity_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterBall_velocity_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitBall_velocity_input(this);
		}
	}

	public final Ball_velocity_inputContext ball_velocity_input() throws RecognitionException {
		Ball_velocity_inputContext _localctx = new Ball_velocity_inputContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_ball_velocity_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191); match(T__21);
			setState(192); match(EQUALS);
			setState(193); match(FLOAT);
			setState(194); match(T__3);
			setState(195); match(EQUALS);
			setState(196); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Absorber_dimensions_inputContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(PingballParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(PingballParser.EQUALS, i);
		}
		public List<TerminalNode> FLOAT() { return getTokens(PingballParser.FLOAT); }
		public TerminalNode FLOAT(int i) {
			return getToken(PingballParser.FLOAT, i);
		}
		public Absorber_dimensions_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absorber_dimensions_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterAbsorber_dimensions_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitAbsorber_dimensions_input(this);
		}
	}

	public final Absorber_dimensions_inputContext absorber_dimensions_input() throws RecognitionException {
		Absorber_dimensions_inputContext _localctx = new Absorber_dimensions_inputContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_absorber_dimensions_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198); match(T__7);
			setState(199); match(EQUALS);
			setState(200); match(FLOAT);
			setState(201); match(T__22);
			setState(202); match(EQUALS);
			setState(203); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Portal_other_board_name_inputContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode NAME() { return getToken(PingballParser.NAME, 0); }
		public Portal_other_board_name_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portal_other_board_name_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterPortal_other_board_name_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitPortal_other_board_name_input(this);
		}
	}

	public final Portal_other_board_name_inputContext portal_other_board_name_input() throws RecognitionException {
		Portal_other_board_name_inputContext _localctx = new Portal_other_board_name_inputContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_portal_other_board_name_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205); match(T__23);
			setState(206); match(EQUALS);
			setState(207); match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Portal_other_portal_name_inputContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PingballParser.EQUALS, 0); }
		public TerminalNode NAME() { return getToken(PingballParser.NAME, 0); }
		public Portal_other_portal_name_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portal_other_portal_name_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).enterPortal_other_portal_name_input(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PingballListener ) ((PingballListener)listener).exitPortal_other_portal_name_input(this);
		}
	}

	public final Portal_other_portal_name_inputContext portal_other_portal_name_input() throws RecognitionException {
		Portal_other_portal_name_inputContext _localctx = new Portal_other_portal_name_inputContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_portal_other_portal_name_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209); match(T__14);
			setState(210); match(EQUALS);
			setState(211); match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3!\u00d8\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3<\n\3\7\3>\n\3\f\3\16\3A\13"+
		"\3\3\4\3\4\5\4E\n\4\3\4\3\4\3\5\5\5J\n\5\3\5\5\5M\n\5\3\5\5\5P\n\5\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\5\tf\n\t\3\n\3\n\5\nj\n\n\3\n\3\n\3\n\3\13\3\13\5\13q\n\13\3\13"+
		"\3\13\3\f\3\f\5\fw\n\f\3\f\3\f\3\r\3\r\5\r}\n\r\3\r\3\r\5\r\u0081\n\r"+
		"\3\16\3\16\5\16\u0085\n\16\3\16\3\16\5\16\u0089\n\16\3\17\3\17\5\17\u008d"+
		"\n\17\3\17\3\17\5\17\u0091\n\17\3\20\3\20\5\20\u0095\n\20\3\20\3\20\3"+
		"\20\3\21\3\21\5\21\u009c\n\21\3\21\3\21\5\21\u00a0\n\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\2\2\33\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\2\2\u00d8\2\64\3\2\2\2\4\67"+
		"\3\2\2\2\6B\3\2\2\2\bI\3\2\2\2\nQ\3\2\2\2\fU\3\2\2\2\16Y\3\2\2\2\20e\3"+
		"\2\2\2\22g\3\2\2\2\24n\3\2\2\2\26t\3\2\2\2\30z\3\2\2\2\32\u0082\3\2\2"+
		"\2\34\u008a\3\2\2\2\36\u0092\3\2\2\2 \u0099\3\2\2\2\"\u00a3\3\2\2\2$\u00ab"+
		"\3\2\2\2&\u00af\3\2\2\2(\u00b6\3\2\2\2*\u00ba\3\2\2\2,\u00c1\3\2\2\2."+
		"\u00c8\3\2\2\2\60\u00cf\3\2\2\2\62\u00d3\3\2\2\2\64\65\5\4\3\2\65\66\7"+
		"\2\2\3\66\3\3\2\2\2\67?\5\6\4\28;\7 \2\29<\5\20\t\2:<\5\"\22\2;9\3\2\2"+
		"\2;:\3\2\2\2;<\3\2\2\2<>\3\2\2\2=8\3\2\2\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2"+
		"\2@\5\3\2\2\2A?\3\2\2\2BD\7\3\2\2CE\5$\23\2DC\3\2\2\2DE\3\2\2\2EF\3\2"+
		"\2\2FG\5\b\5\2G\7\3\2\2\2HJ\5\n\6\2IH\3\2\2\2IJ\3\2\2\2JL\3\2\2\2KM\5"+
		"\f\7\2LK\3\2\2\2LM\3\2\2\2MO\3\2\2\2NP\5\16\b\2ON\3\2\2\2OP\3\2\2\2P\t"+
		"\3\2\2\2QR\7\33\2\2RS\7\36\2\2ST\7\34\2\2T\13\3\2\2\2UV\7\26\2\2VW\7\36"+
		"\2\2WX\7\34\2\2X\r\3\2\2\2YZ\7\t\2\2Z[\7\36\2\2[\\\7\34\2\2\\\17\3\2\2"+
		"\2]f\5\22\n\2^f\5\24\13\2_f\5\26\f\2`f\5\30\r\2af\5\32\16\2bf\5\34\17"+
		"\2cf\5\36\20\2df\5 \21\2e]\3\2\2\2e^\3\2\2\2e_\3\2\2\2e`\3\2\2\2ea\3\2"+
		"\2\2eb\3\2\2\2ec\3\2\2\2ed\3\2\2\2f\21\3\2\2\2gi\7\17\2\2hj\5$\23\2ih"+
		"\3\2\2\2ij\3\2\2\2jk\3\2\2\2kl\5*\26\2lm\5,\27\2m\23\3\2\2\2np\7\13\2"+
		"\2oq\5$\23\2po\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\5&\24\2s\25\3\2\2\2tv\7\27"+
		"\2\2uw\5$\23\2vu\3\2\2\2vw\3\2\2\2wx\3\2\2\2xy\5&\24\2y\27\3\2\2\2z|\7"+
		"\31\2\2{}\5$\23\2|{\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\u0080\5&\24\2\177\u0081"+
		"\5(\25\2\u0080\177\3\2\2\2\u0080\u0081\3\2\2\2\u0081\31\3\2\2\2\u0082"+
		"\u0084\7\23\2\2\u0083\u0085\5$\23\2\u0084\u0083\3\2\2\2\u0084\u0085\3"+
		"\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088\5&\24\2\u0087\u0089\5(\25\2\u0088"+
		"\u0087\3\2\2\2\u0088\u0089\3\2\2\2\u0089\33\3\2\2\2\u008a\u008c\7\32\2"+
		"\2\u008b\u008d\5$\23\2\u008c\u008b\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e"+
		"\3\2\2\2\u008e\u0090\5&\24\2\u008f\u0091\5(\25\2\u0090\u008f\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\35\3\2\2\2\u0092\u0094\7\21\2\2\u0093\u0095\5$\23"+
		"\2\u0094\u0093\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097"+
		"\5&\24\2\u0097\u0098\5.\30\2\u0098\37\3\2\2\2\u0099\u009b\7\20\2\2\u009a"+
		"\u009c\5$\23\2\u009b\u009a\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\3\2"+
		"\2\2\u009d\u009f\5&\24\2\u009e\u00a0\5\60\31\2\u009f\u009e\3\2\2\2\u009f"+
		"\u00a0\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2\5\62\32\2\u00a2!\3\2\2"+
		"\2\u00a3\u00a4\7\n\2\2\u00a4\u00a5\7\25\2\2\u00a5\u00a6\7\36\2\2\u00a6"+
		"\u00a7\7\35\2\2\u00a7\u00a8\7\f\2\2\u00a8\u00a9\7\36\2\2\u00a9\u00aa\7"+
		"\35\2\2\u00aa#\3\2\2\2\u00ab\u00ac\7\7\2\2\u00ac\u00ad\7\36\2\2\u00ad"+
		"\u00ae\7\35\2\2\u00ae%\3\2\2\2\u00af\u00b0\7\22\2\2\u00b0\u00b1\7\36\2"+
		"\2\u00b1\u00b2\7\34\2\2\u00b2\u00b3\7\b\2\2\u00b3\u00b4\7\36\2\2\u00b4"+
		"\u00b5\7\34\2\2\u00b5\'\3\2\2\2\u00b6\u00b7\7\16\2\2\u00b7\u00b8\7\36"+
		"\2\2\u00b8\u00b9\7\34\2\2\u00b9)\3\2\2\2\u00ba\u00bb\7\22\2\2\u00bb\u00bc"+
		"\7\36\2\2\u00bc\u00bd\7\34\2\2\u00bd\u00be\7\b\2\2\u00be\u00bf\7\36\2"+
		"\2\u00bf\u00c0\7\34\2\2\u00c0+\3\2\2\2\u00c1\u00c2\7\6\2\2\u00c2\u00c3"+
		"\7\36\2\2\u00c3\u00c4\7\34\2\2\u00c4\u00c5\7\30\2\2\u00c5\u00c6\7\36\2"+
		"\2\u00c6\u00c7\7\34\2\2\u00c7-\3\2\2\2\u00c8\u00c9\7\24\2\2\u00c9\u00ca"+
		"\7\36\2\2\u00ca\u00cb\7\34\2\2\u00cb\u00cc\7\5\2\2\u00cc\u00cd\7\36\2"+
		"\2\u00cd\u00ce\7\34\2\2\u00ce/\3\2\2\2\u00cf\u00d0\7\4\2\2\u00d0\u00d1"+
		"\7\36\2\2\u00d1\u00d2\7\35\2\2\u00d2\61\3\2\2\2\u00d3\u00d4\7\r\2\2\u00d4"+
		"\u00d5\7\36\2\2\u00d5\u00d6\7\35\2\2\u00d6\63\3\2\2\2\25;?DILOeipv|\u0080"+
		"\u0084\u0088\u008c\u0090\u0094\u009b\u009f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}