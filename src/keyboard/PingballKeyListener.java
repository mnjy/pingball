package keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import implementation.GameObject;

/**
 * A PingballKeyListener stores the key commands defined in the board
 */
public class PingballKeyListener implements KeyListener {
    public Map<Integer, List<GameObject>> keyUpActions = new HashMap<Integer , List<GameObject>>();
    public Map<Integer, List<GameObject>> keyDownActions = new HashMap<Integer, List<GameObject>>();
    
    public enum KeyEnum{
        KEYUP, KEYDOWN;
    }
    
    public PingballKeyListener(){
    }
    
    /**
     * Adds the command to the key listener
     * @param triggeredGadget the gadget to trigger
     * @param key the key code of the key that triggers the gadget
     * @param keydown specifies whether the gadget is triggered when the key is pressed (KEYDOWN) or released (KEYUP)
     */
    public void addCommand(GameObject triggeredGadget, int key, KeyEnum keydown){
        if (keydown.equals(KeyEnum.KEYUP)){
            if (!keyUpActions.containsKey(key)){
                keyUpActions.put(key, new ArrayList<GameObject>());
            }
            List<GameObject> prevList = keyUpActions.get(key);
            prevList.add(triggeredGadget);
            keyUpActions.put(key, prevList);
        }
        else if (keydown.equals(KeyEnum.KEYDOWN)){
            if (!keyDownActions.containsKey(key)){
                keyDownActions.put(key, new ArrayList<GameObject>());
            }
            List<GameObject> prevList = keyDownActions.get(key);
            prevList.add(triggeredGadget);
            keyDownActions.put(key, prevList);
        }
    }
    
    /**
     * @return a list of all the commands in the listener, in the form "(keyup|keydown) key gadgetName",
     * where key corresponds to that returned by Java Swing's KeyEvent.getKeyText, and gadgetName is the name of
     * the triggered gadget; or "None" if there are none.
     */
    public String getCommands(){
        String output = "";
        for (Integer key : keyUpActions.keySet()){
            for (GameObject gadget: keyUpActions.get(key)){
                String command = "release "+KeyEvent.getKeyText(key)+" --> "+gadget.getName()+"\n";
                output+=command;
            }
        }
        for (Integer key : keyDownActions.keySet()){
            for (GameObject gadget: keyDownActions.get(key)){
                String command = "press "+KeyEvent.getKeyText(key)+" --> "+gadget.getName()+"\n";
                output+=command;
            }
        }
        if (output.isEmpty()){
            return "None";
        }
        else{
            return output;
        }
    }
    
    @Override
      public void keyPressed(KeyEvent e) {
          int key =e.getKeyCode();
          if (keyDownActions.containsKey(key)){
              for(GameObject triggeredGadget: keyDownActions.get(key)){                  
                  triggeredGadget.doTriggerAction();
              }
          }
      }
      @Override
      public void keyReleased(KeyEvent e) {
          int key = e.getKeyCode();
          if (keyUpActions.containsKey(key)){
              for(GameObject triggeredGadget: keyUpActions.get(key)){                  
                  triggeredGadget.doTriggerAction();
              }
          }    
      }
      
      @Override
      public void keyTyped(KeyEvent e) {
      }
}
