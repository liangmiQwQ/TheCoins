package net.mirolls.thecoins.libs;

public class SpecialItemClickedAction {
  private String actionType;
  private String actionFunction;

  public SpecialItemClickedAction(String actionType, String actionFunction) {
    this.actionType = actionType;
    this.actionFunction = actionFunction;
  }

  public String getActionFunction() {
    return actionFunction;
  }

  public void setActionFunction(String actionFunction) {
    this.actionFunction = actionFunction;
  }

  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }
}
