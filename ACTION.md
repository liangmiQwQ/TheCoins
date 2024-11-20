# The JSON type of GUI ItemStack Actions

```JSON5
/* Use JSON5 to add notes 
   Use standard JSON in the project */
{
  "actionType": "",
  /* "ActionType" is the same as itemStackActionType 
     There is 
     "Close" (Clicked and the GUI will close)
     "Link" (Will open another GUI to replace this GUI)
     "Function" (Do some codes) 
     “Background" (Do not do anything) 
  */
  "actionFunction": "",
  /* "actionFunction" will be different with different "actionType"
     You should to registry your action function at
     "net.mirolls.thecoins.item.ItemStackGUI
     .registryAction(String, (PlayerEntity)->{})"
      Your actions should registry at ScreenHandler
      
      write down a function name in this key
      if the name is "" (empty)
      "ActionType:Close": "Player.closeHandledScreen()"
      "ActionType:Link": do not anything
      "ActionType:Function": print an error
      "ActionType: Background": do not anything
  */
}
```