package net.mirolls.thecoins.libs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialItemClickedAction {
  @JsonProperty("actionType")
  private String actionType;
  @JsonProperty("actionFunction")
  private String actionFunction;

  public SpecialItemClickedAction(String actionType, String actionFunction) {
    this.actionType = actionType;
    this.actionFunction = actionFunction;
  }

  public SpecialItemClickedAction() {

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
